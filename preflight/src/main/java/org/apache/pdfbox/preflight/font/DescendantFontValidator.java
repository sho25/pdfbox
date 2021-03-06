begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|font
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_FONTS_CIDKEYED_CIDTOGID
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_FONTS_CIDKEYED_SYSINFO
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_FONTS_DICTIONARY_INVALID
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSDictionary
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSInteger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|font
operator|.
name|PDCIDFont
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|ValidationResult
operator|.
name|ValidationError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|font
operator|.
name|container
operator|.
name|FontContainer
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|DescendantFontValidator
parameter_list|<
name|T
extends|extends
name|FontContainer
parameter_list|>
extends|extends
name|SimpleFontValidator
argument_list|<
name|T
argument_list|>
block|{
specifier|public
name|DescendantFontValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDCIDFont
name|font
parameter_list|,
name|T
name|fContainer
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|font
argument_list|,
name|font
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|fContainer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|checkMandatoryField
parameter_list|()
block|{
name|boolean
name|arePresent
init|=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
name|arePresent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
expr_stmt|;
name|arePresent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|)
expr_stmt|;
name|arePresent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|CIDSYSTEMINFO
argument_list|)
expr_stmt|;
name|arePresent
operator|&=
name|fontDictionary
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|FONT_DESC
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|arePresent
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_DICTIONARY_INVALID
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": Required keys are missing"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|checkCIDSystemInfo
argument_list|(
name|fontDictionary
operator|.
name|getCOSDictionary
argument_list|(
name|COSName
operator|.
name|CIDSYSTEMINFO
argument_list|)
argument_list|)
expr_stmt|;
name|checkCIDToGIDMap
argument_list|(
name|fontDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CID_TO_GID_MAP
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check the content of the CIDSystemInfo dictionary. A CIDSystemInfo dictionary must contain :      *<UL>      *<li>a Name - Registry      *<li>a Name - Ordering      *<li>a Integer - Supplement      *</UL>      *       * @param sysinfo      */
specifier|protected
name|void
name|checkCIDSystemInfo
parameter_list|(
name|COSDictionary
name|sysinfo
parameter_list|)
block|{
if|if
condition|(
name|sysinfo
operator|!=
literal|null
condition|)
block|{
name|String
name|reg
init|=
name|sysinfo
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|REGISTRY
argument_list|)
decl_stmt|;
name|String
name|ord
init|=
name|sysinfo
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|ORDERING
argument_list|)
decl_stmt|;
name|COSBase
name|sup
init|=
name|sysinfo
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SUPPLEMENT
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|reg
operator|!=
literal|null
operator|&&
name|ord
operator|!=
literal|null
operator|&&
name|sup
operator|instanceof
name|COSInteger
operator|)
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_SYSINFO
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_SYSINFO
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method checks the CIDtoGIDMap entry of the Font dictionary. call the      * {@linkplain #checkCIDToGIDMap(COSBase, boolean)} with right parameters      * according to the instance of DescendantFontValidator      *      * @param ctog      */
specifier|protected
specifier|abstract
name|void
name|checkCIDToGIDMap
parameter_list|(
name|COSBase
name|ctog
parameter_list|)
function_decl|;
comment|/**      * This method checks the CIDtoGIDMap entry of the Font dictionary. This      * element must be a Stream or a Name. If it is a name, it must be      * "Identity" otherwise, the PDF file isn't a PDF/A-1b.      *      * If the validation fails the list of errors in the FontContainer is      * updated.      *      * If the CIDtoGIDMap is a Stream, it is parsed as a CMap and the result is      * returned.      *      * @param ctog      * @param mandatory true for CIDType2 , false for CIDType0      */
specifier|protected
name|void
name|checkCIDToGIDMap
parameter_list|(
name|COSBase
name|ctog
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
block|{
if|if
condition|(
name|ctog
operator|instanceof
name|COSName
condition|)
block|{
comment|// ---- valid only if the string is Identity
if|if
condition|(
operator|!
name|COSName
operator|.
name|IDENTITY
operator|.
name|equals
argument_list|(
name|ctog
argument_list|)
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_CIDTOGID
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": The CIDToGID entry is invalid"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|ctog
operator|instanceof
name|COSStream
condition|)
block|{
try|try
block|{
comment|// todo: check the map's content? (won't pdfbox do this?)
name|InputStream
name|is
init|=
operator|(
operator|(
name|COSStream
operator|)
name|ctog
operator|)
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// map can be invalid, return a Validation Error
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_CIDTOGID
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": error getting CIDToGIDMap"
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|mandatory
condition|)
block|{
name|this
operator|.
name|fontContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_CIDKEYED_CIDTOGID
argument_list|,
name|font
operator|.
name|getName
argument_list|()
operator|+
literal|": mandatory CIDToGIDMap missing"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

