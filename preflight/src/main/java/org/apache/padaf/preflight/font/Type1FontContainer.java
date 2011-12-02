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
name|padaf
operator|.
name|preflight
operator|.
name|font
package|;
end_package

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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cff
operator|.
name|CFFFont
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cff
operator|.
name|CFFFont
operator|.
name|Mapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|font
operator|.
name|type1
operator|.
name|Type1
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
name|PDFont
import|;
end_import

begin_class
specifier|public
class|class
name|Type1FontContainer
extends|extends
name|AbstractFontContainer
block|{
comment|/** 	 * Represent the missingWidth value of the FontDescriptor dictionary. 	 * According to the PDF Reference, if this value is missing, the default  	 * one is 0. 	 */
specifier|private
name|float
name|defaultGlyphWidth
init|=
literal|0
decl_stmt|;
comment|/** 	 * Object which contains the Type1Font data extracted by the 	 * Type1Parser object 	 */
specifier|private
name|Type1
name|fontObject
init|=
literal|null
decl_stmt|;
specifier|private
name|List
argument_list|<
name|CFFFont
argument_list|>
name|cffFonts
init|=
literal|null
decl_stmt|;
specifier|public
name|Type1FontContainer
parameter_list|(
name|PDFont
name|fd
parameter_list|)
block|{
name|super
argument_list|(
name|fd
argument_list|)
expr_stmt|;
block|}
name|void
name|setFontObject
parameter_list|(
name|Type1
name|fontObject
parameter_list|)
block|{
name|this
operator|.
name|fontObject
operator|=
name|fontObject
expr_stmt|;
block|}
name|void
name|setCFFFontObjects
parameter_list|(
name|List
argument_list|<
name|CFFFont
argument_list|>
name|fontObject
parameter_list|)
block|{
name|this
operator|.
name|cffFonts
operator|=
name|fontObject
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|checkCID
parameter_list|(
name|int
name|cid
parameter_list|)
throws|throws
name|GlyphException
block|{
if|if
condition|(
name|isAlreadyComputedCid
argument_list|(
name|cid
argument_list|)
condition|)
block|{
return|return;
block|}
specifier|final
name|float
name|widthProvidedByPdfDictionary
init|=
name|this
operator|.
name|font
operator|.
name|getFontWidth
argument_list|(
name|cid
argument_list|)
decl_stmt|;
name|int
name|widthInFontProgram
init|=
literal|0
decl_stmt|;
try|try
block|{
if|if
condition|(
name|this
operator|.
name|fontObject
operator|!=
literal|null
condition|)
block|{
name|widthInFontProgram
operator|=
name|this
operator|.
name|fontObject
operator|.
name|getWidthOfCID
argument_list|(
name|cid
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// -- Retrieves the SID with the Character Name in the encoding map
comment|// -- Need more PDF with a Type1C subfont to valid this implementation
name|String
name|name
init|=
name|this
operator|.
name|font
operator|.
name|getFontEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|cid
argument_list|)
decl_stmt|;
for|for
control|(
name|CFFFont
name|cff
range|:
name|cffFonts
control|)
block|{
name|int
name|SID
init|=
name|cff
operator|.
name|getEncoding
argument_list|()
operator|.
name|getSID
argument_list|(
name|cid
argument_list|)
decl_stmt|;
for|for
control|(
name|Mapping
name|m
range|:
name|cff
operator|.
name|getMappings
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|SID
operator|=
name|m
operator|.
name|getSID
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
name|widthInFontProgram
operator|=
name|cff
operator|.
name|getWidth
argument_list|(
name|SID
argument_list|)
expr_stmt|;
if|if
condition|(
name|widthInFontProgram
operator|!=
name|defaultGlyphWidth
condition|)
block|{
break|break;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|GlyphException
name|e
parameter_list|)
block|{
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|GlyphException
name|ge
init|=
operator|new
name|GlyphException
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_FONTS_DAMAGED
argument_list|,
name|cid
argument_list|,
literal|"Unable to get width of the CID/SID : "
operator|+
name|cid
argument_list|)
decl_stmt|;
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|,
name|ge
argument_list|)
argument_list|)
expr_stmt|;
throw|throw
name|ge
throw|;
block|}
name|checkWidthsConsistency
argument_list|(
name|cid
argument_list|,
name|widthProvidedByPdfDictionary
argument_list|,
name|widthInFontProgram
argument_list|)
expr_stmt|;
name|addKnownCidElement
argument_list|(
operator|new
name|GlyphDetail
argument_list|(
name|cid
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

