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
operator|.
name|descriptor
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
name|ERROR_FONTS_ENCODING
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
name|ERROR_FONTS_FONT_FILEX_INVALID
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
name|ERROR_FONTS_TRUETYPE_DAMAGED
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
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
name|ttf
operator|.
name|TTFParser
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
name|ttf
operator|.
name|TrueTypeFont
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
name|common
operator|.
name|PDStream
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
name|PDFontDescriptorDictionary
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
name|TrueTypeContainer
import|;
end_import

begin_class
specifier|public
class|class
name|TrueTypeDescriptorHelper
extends|extends
name|FontDescriptorHelper
argument_list|<
name|TrueTypeContainer
argument_list|>
block|{
specifier|public
name|TrueTypeDescriptorHelper
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDFont
name|font
parameter_list|,
name|TrueTypeContainer
name|fontContainer
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|font
argument_list|,
name|fontContainer
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PDStream
name|extractFontFile
parameter_list|(
name|PDFontDescriptorDictionary
name|fontDescriptor
parameter_list|)
block|{
name|PDStream
name|fontFile
init|=
name|fontDescriptor
operator|.
name|getFontFile2
argument_list|()
decl_stmt|;
name|COSStream
name|stream
init|=
operator|(
name|fontFile
operator|==
literal|null
condition|?
literal|null
else|:
name|fontFile
operator|.
name|getStream
argument_list|()
operator|)
decl_stmt|;
if|if
condition|(
name|stream
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_FONT_FILEX_INVALID
argument_list|,
literal|"The FontFile2 is missing for "
operator|+
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|fContainer
operator|.
name|notEmbedded
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
name|stream
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH1
argument_list|)
operator|<=
literal|0
condition|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_FONT_FILEX_INVALID
argument_list|,
literal|"The FontFile entry /Length1 is invalid for "
operator|+
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|fontFile
return|;
block|}
specifier|protected
name|void
name|processFontFile
parameter_list|(
name|PDFontDescriptorDictionary
name|fontDescriptor
parameter_list|,
name|PDStream
name|fontFile
parameter_list|)
block|{
comment|/*          * Try to load the font using the TTFParser object. If the font is invalid, an exception will be thrown. Because          * of it is a Embedded Font Program, some tables are required and other are optional see PDF Reference (§5.8)          */
name|ByteArrayInputStream
name|bis
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bis
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|fontFile
operator|.
name|getByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|TrueTypeFont
name|ttf
init|=
operator|new
name|TTFParser
argument_list|(
literal|true
argument_list|)
operator|.
name|parseTTF
argument_list|(
name|bis
argument_list|)
decl_stmt|;
if|if
condition|(
name|fontDescriptor
operator|.
name|isSymbolic
argument_list|()
operator|&&
name|ttf
operator|.
name|getCMAP
argument_list|()
operator|.
name|getCmaps
argument_list|()
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_ENCODING
argument_list|,
literal|"The Encoding should be missing for the Symbolic TTF"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|(
operator|(
name|TrueTypeContainer
operator|)
name|this
operator|.
name|fContainer
operator|)
operator|.
name|setTrueTypeFont
argument_list|(
name|ttf
argument_list|)
expr_stmt|;
comment|// TODO check the WIdth consistency too
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|this
operator|.
name|fContainer
operator|.
name|push
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_FONTS_TRUETYPE_DAMAGED
argument_list|,
literal|"The FontFile can't be read for "
operator|+
name|this
operator|.
name|font
operator|.
name|getBaseFont
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|bis
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

