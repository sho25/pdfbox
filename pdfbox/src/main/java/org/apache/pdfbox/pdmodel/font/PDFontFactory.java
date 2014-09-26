begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
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
name|COSName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|encoding
operator|.
name|GlyphList
import|;
end_import

begin_comment
comment|/**  * Creates the appropriate font subtype based on information in the dictionary.  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDFontFactory
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDFontFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PDFontFactory
parameter_list|()
block|{     }
comment|/**      * Creates a new PDFont instance with the appropriate subclass.      *      * @param dictionary a font dictionary      * @return a PDFont instance, based on the SubType entry of the dictionary      * @throws IOException      */
specifier|public
specifier|static
name|PDFont
name|createFont
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
throws|throws
name|IOException
block|{
name|COSName
name|type
init|=
name|dictionary
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|FONT
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSName
operator|.
name|FONT
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Expected 'Font' dictionary but found '"
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|COSName
name|subType
init|=
name|dictionary
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|TYPE1
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
name|COSBase
name|fd
init|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FONT_DESC
argument_list|)
decl_stmt|;
if|if
condition|(
name|fd
operator|!=
literal|null
operator|&&
name|fd
operator|instanceof
name|COSDictionary
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|COSDictionary
operator|)
name|fd
operator|)
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|FONT_FILE3
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDType1CFont
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
block|}
return|return
operator|new
name|PDType1Font
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|MM_TYPE1
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
name|COSBase
name|fd
init|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FONT_DESC
argument_list|)
decl_stmt|;
if|if
condition|(
name|fd
operator|!=
literal|null
operator|&&
name|fd
operator|instanceof
name|COSDictionary
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|COSDictionary
operator|)
name|fd
operator|)
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|FONT_FILE3
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDType1CFont
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
block|}
return|return
operator|new
name|PDMMType1Font
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|TRUE_TYPE
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDTrueTypeFont
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|TYPE3
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDType3Font
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|TYPE0
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDType0Font
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|CID_FONT_TYPE0
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Type 0 descendant font not allowed"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|CID_FONT_TYPE2
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Type 0 descendant font not allowed"
argument_list|)
throw|;
block|}
else|else
block|{
comment|// assuming Type 1 font (see PDFBOX-1988) because it seems that Adobe Reader does this
comment|// however, we may need more sophisticated logic perhaps looking at the FontFile
name|LOG
operator|.
name|warn
argument_list|(
literal|"Invalid font subtype '"
operator|+
name|subType
operator|+
literal|"'"
argument_list|)
expr_stmt|;
return|return
operator|new
name|PDType1Font
argument_list|(
name|dictionary
argument_list|)
return|;
block|}
block|}
comment|/**      * Creates a new PDCIDFont instance with the appropriate subclass.      *      * @param dictionary descendant font dictionary      * @return a PDCIDFont instance, based on the SubType entry of the dictionary      * @throws IOException      */
specifier|static
name|PDCIDFont
name|createDescendantFont
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|,
name|PDType0Font
name|parent
parameter_list|)
throws|throws
name|IOException
block|{
name|COSName
name|type
init|=
name|dictionary
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|FONT
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSName
operator|.
name|FONT
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expected 'Font' dictionary but found '"
operator|+
name|type
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|COSName
name|subType
init|=
name|dictionary
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|CID_FONT_TYPE0
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDCIDFontType0
argument_list|(
name|dictionary
argument_list|,
name|parent
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|CID_FONT_TYPE2
operator|.
name|equals
argument_list|(
name|subType
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDCIDFontType2
argument_list|(
name|dictionary
argument_list|,
name|parent
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid font type: "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create a default font      *       * @return a default font      * @throws IOException if something goes wrong      */
specifier|public
specifier|static
name|PDFont
name|createDefaultFont
parameter_list|()
throws|throws
name|IOException
block|{
name|COSDictionary
name|dict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|FONT
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|TRUE_TYPE
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|,
literal|"Arial"
argument_list|)
expr_stmt|;
return|return
name|createFont
argument_list|(
name|dict
argument_list|)
return|;
block|}
block|}
end_class

end_unit

