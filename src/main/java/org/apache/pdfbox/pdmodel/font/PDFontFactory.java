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
name|java
operator|.
name|util
operator|.
name|Map
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

begin_comment
comment|/**  * This will create the correct type of font based on information in the dictionary.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.6 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFontFactory
block|{
comment|/**      * private constructor, should only use static methods in this class.      */
specifier|private
name|PDFontFactory
parameter_list|()
block|{     }
comment|/**      * Logger instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|log
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
comment|/**      * This will create the correct font based on information in the dictionary.      *      * @param dic The populated dictionary.      *      * @param fontCache A Map to cache already created fonts      *      * @return The corrent implementation for the font.      *      * @throws IOException If the dictionary is not valid.      */
specifier|public
specifier|static
name|PDFont
name|createFont
parameter_list|(
name|COSDictionary
name|dic
parameter_list|,
name|Map
name|fontCache
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFont
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fontCache
operator|!=
literal|null
condition|)
block|{
name|String
name|fontKey
init|=
name|dic
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|BASE_FONT
argument_list|)
operator|+
name|dic
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
operator|+
name|dic
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|dic
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|fontKey
operator|+=
name|dic
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|fontCache
operator|.
name|containsKey
argument_list|(
name|fontKey
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|(
name|PDFont
operator|)
name|fontCache
operator|.
name|get
argument_list|(
name|fontKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|PDFontFactory
operator|.
name|createFont
argument_list|(
name|dic
argument_list|)
expr_stmt|;
name|fontCache
operator|.
name|put
argument_list|(
name|fontKey
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|retval
operator|=
name|PDFontFactory
operator|.
name|createFont
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will create the correct font based on information in the dictionary.      *      * @param dic The populated dictionary.      *      * @return The corrent implementation for the font.      *      * @throws IOException If the dictionary is not valid.      */
specifier|public
specifier|static
name|PDFont
name|createFont
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFont
name|retval
init|=
literal|null
decl_stmt|;
name|COSName
name|type
init|=
operator|(
name|COSName
operator|)
name|dic
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|type
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|FONT
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot create font if /Type is not /Font.  Actual="
operator|+
name|type
argument_list|)
throw|;
block|}
name|COSName
name|subType
init|=
operator|(
name|COSName
operator|)
name|dic
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|subType
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|TYPE1
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDType1Font
argument_list|(
name|dic
argument_list|)
expr_stmt|;
name|COSDictionary
name|fontDic
init|=
operator|(
name|COSDictionary
operator|)
name|dic
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
name|fontDic
operator|!=
literal|null
condition|)
block|{
name|COSStream
name|ffStream
init|=
operator|(
name|COSStream
operator|)
name|fontDic
operator|.
name|getDictionaryObject
argument_list|(
literal|"FontFile"
argument_list|)
decl_stmt|;
name|COSStream
name|ff3Stream
init|=
operator|(
name|COSStream
operator|)
name|fontDic
operator|.
name|getDictionaryObject
argument_list|(
literal|"FontFile3"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ffStream
operator|==
literal|null
operator|&&
name|ff3Stream
operator|!=
literal|null
condition|)
block|{
name|String
name|ff3SubType
init|=
name|ff3Stream
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ff3SubType
operator|.
name|equals
argument_list|(
literal|"Type1C"
argument_list|)
condition|)
block|{
try|try
block|{
name|retval
operator|=
operator|new
name|PDType1CFont
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to create Type1C font. Falling back to Type1 font"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|subType
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|MM_TYPE1
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDMMType1Font
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|subType
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|TRUE_TYPE
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDTrueTypeFont
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|subType
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|TYPE3
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDType3Font
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|subType
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|TYPE0
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDType0Font
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|subType
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|CID_FONT_TYPE0
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDCIDFontType0Font
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|subType
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|CID_FONT_TYPE2
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDCIDFontType2Font
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Substituting TrueType for unknown font subtype="
operator|+
name|dic
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|//throw new IOException( "Unknown font subtype=" + subType );
name|retval
operator|=
operator|new
name|PDTrueTypeFont
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
block|}
end_class

end_unit

