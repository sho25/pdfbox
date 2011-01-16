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
name|awt
operator|.
name|Font
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|FontFormatException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|fontbox
operator|.
name|afm
operator|.
name|FontMetric
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
name|COSArray
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
name|COSFloat
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
name|encoding
operator|.
name|AFMEncoding
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
name|Encoding
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
name|EncodingManager
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
name|Type1Encoding
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
name|WinAnsiEncoding
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
name|PDMatrix
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

begin_comment
comment|/**  * This is implementation of the Type1 Font.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.11 $  */
end_comment

begin_class
specifier|public
class|class
name|PDType1Font
extends|extends
name|PDSimpleFont
block|{
comment|/**      * Log instance.      */
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
name|PDType1Font
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PDType1CFont
name|type1CFont
init|=
literal|null
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|TIMES_ROMAN
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Times-Roman"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|TIMES_BOLD
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Times-Bold"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|TIMES_ITALIC
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Times-Italic"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|TIMES_BOLD_ITALIC
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Times-BoldItalic"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|HELVETICA
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Helvetica"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|HELVETICA_BOLD
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Helvetica-Bold"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|HELVETICA_OBLIQUE
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Helvetica-Oblique"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|HELVETICA_BOLD_OBLIQUE
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Helvetica-BoldOblique"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|COURIER
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Courier"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|COURIER_BOLD
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Courier-Bold"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|COURIER_OBLIQUE
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Courier-Oblique"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|COURIER_BOLD_OBLIQUE
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Courier-BoldOblique"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|SYMBOL
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"Symbol"
argument_list|)
decl_stmt|;
comment|/**      * Standard Base 14 Font.      */
specifier|public
specifier|static
specifier|final
name|PDType1Font
name|ZAPF_DINGBATS
init|=
operator|new
name|PDType1Font
argument_list|(
literal|"ZapfDingbats"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|PDType1Font
argument_list|>
name|STANDARD_14
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDType1Font
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|STANDARD_14
operator|.
name|put
argument_list|(
name|TIMES_ROMAN
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|TIMES_ROMAN
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|TIMES_BOLD
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|TIMES_BOLD
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|TIMES_ITALIC
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|TIMES_ITALIC
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|TIMES_BOLD_ITALIC
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|TIMES_BOLD_ITALIC
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|HELVETICA
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|HELVETICA
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|HELVETICA_BOLD
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|HELVETICA_BOLD
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|HELVETICA_OBLIQUE
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|HELVETICA_OBLIQUE
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|HELVETICA_BOLD_OBLIQUE
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|HELVETICA_BOLD_OBLIQUE
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|COURIER
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|COURIER
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|COURIER_BOLD
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|COURIER_BOLD
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|COURIER_OBLIQUE
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|COURIER_OBLIQUE
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|COURIER_BOLD_OBLIQUE
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|COURIER_BOLD_OBLIQUE
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|SYMBOL
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|SYMBOL
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|ZAPF_DINGBATS
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|ZAPF_DINGBATS
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Font
name|awtFont
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDType1Font
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|font
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|TYPE1
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param fontDictionary The font dictionary according to the PDF specification.      */
specifier|public
name|PDType1Font
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|)
expr_stmt|;
name|PDFontDescriptor
name|fd
init|=
name|getFontDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|fd
operator|!=
literal|null
operator|&&
name|fd
operator|instanceof
name|PDFontDescriptorDictionary
condition|)
block|{
comment|// a Type1 font may contain a Type1C font
name|PDStream
name|fontFile3
init|=
operator|(
operator|(
name|PDFontDescriptorDictionary
operator|)
name|fd
operator|)
operator|.
name|getFontFile3
argument_list|()
decl_stmt|;
if|if
condition|(
name|fontFile3
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|type1CFont
operator|=
operator|new
name|PDType1CFont
argument_list|(
name|super
operator|.
name|font
argument_list|)
expr_stmt|;
name|awtFont
operator|=
name|type1CFont
operator|.
name|getawtFont
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{}
block|}
block|}
block|}
comment|/**      * Constructor.      *      * @param baseFont The base font for this font.      */
specifier|public
name|PDType1Font
parameter_list|(
name|String
name|baseFont
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setBaseFont
argument_list|(
name|baseFont
argument_list|)
expr_stmt|;
name|setFontEncoding
argument_list|(
operator|new
name|WinAnsiEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|setEncoding
argument_list|(
name|COSName
operator|.
name|WIN_ANSI_ENCODING
argument_list|)
expr_stmt|;
block|}
comment|/**      * A convenience method to get one of the standard 14 font from name.      *      * @param name The name of the font to get.      *      * @return The font that matches the name or null if it does not exist.      */
specifier|public
specifier|static
name|PDType1Font
name|getStandardFont
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|PDType1Font
operator|)
name|STANDARD_14
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * This will get the names of the standard 14 fonts.      *      * @return An array of the names of the standard 14 fonts.      */
specifier|public
specifier|static
name|String
index|[]
name|getStandard14Names
parameter_list|()
block|{
return|return
operator|(
name|String
index|[]
operator|)
name|STANDARD_14
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
literal|14
index|]
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Font
name|getawtFont
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|awtFont
operator|==
literal|null
condition|)
block|{
name|String
name|baseFont
init|=
name|getBaseFont
argument_list|()
decl_stmt|;
name|PDFontDescriptor
name|fd
init|=
name|getFontDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|fd
operator|!=
literal|null
operator|&&
name|fd
operator|instanceof
name|PDFontDescriptorDictionary
condition|)
block|{
name|PDFontDescriptorDictionary
name|fdDictionary
init|=
operator|(
name|PDFontDescriptorDictionary
operator|)
name|fd
decl_stmt|;
if|if
condition|(
name|fdDictionary
operator|.
name|getFontFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// create a type1 font with the embedded data
name|awtFont
operator|=
name|Font
operator|.
name|createFont
argument_list|(
name|Font
operator|.
name|TYPE1_FONT
argument_list|,
name|fdDictionary
operator|.
name|getFontFile
argument_list|()
operator|.
name|createInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FontFormatException
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Can't read the embedded type1 font "
operator|+
name|fd
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|fdDictionary
operator|.
name|getFontFile3
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|type1CFont
operator|=
operator|new
name|PDType1CFont
argument_list|(
name|super
operator|.
name|font
argument_list|)
expr_stmt|;
name|awtFont
operator|=
name|type1CFont
operator|.
name|getawtFont
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|awtFont
operator|==
literal|null
condition|)
block|{
comment|// check if the font is part of our environment
name|awtFont
operator|=
name|FontManager
operator|.
name|getAwtFont
argument_list|(
name|fd
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|awtFont
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Can't find the specified font "
operator|+
name|fd
operator|.
name|getFontName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// check if the font is part of our environment
name|awtFont
operator|=
name|FontManager
operator|.
name|getAwtFont
argument_list|(
name|baseFont
argument_list|)
expr_stmt|;
if|if
condition|(
name|awtFont
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Can't find the specified basefont "
operator|+
name|baseFont
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|awtFont
operator|==
literal|null
condition|)
block|{
comment|// we can't find anything, so we have to use the standard font
name|awtFont
operator|=
name|FontManager
operator|.
name|getStandardFont
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using font "
operator|+
name|awtFont
operator|.
name|getName
argument_list|()
operator|+
literal|" instead"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|awtFont
return|;
block|}
specifier|protected
name|void
name|determineEncoding
parameter_list|()
block|{
name|super
operator|.
name|determineEncoding
argument_list|()
expr_stmt|;
name|Encoding
name|fontEncoding
init|=
name|getFontEncoding
argument_list|()
decl_stmt|;
if|if
condition|(
name|fontEncoding
operator|==
literal|null
condition|)
block|{
name|FontMetric
name|metric
init|=
name|getAFM
argument_list|()
decl_stmt|;
if|if
condition|(
name|metric
operator|!=
literal|null
condition|)
block|{
name|fontEncoding
operator|=
operator|new
name|AFMEncoding
argument_list|(
name|metric
argument_list|)
expr_stmt|;
block|}
block|}
name|getEncodingFromFont
argument_list|(
name|getFontEncoding
argument_list|()
operator|==
literal|null
argument_list|)
expr_stmt|;
name|setFontEncoding
argument_list|(
name|fontEncoding
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tries to get the encoding for the type1 font.      *      */
specifier|private
name|void
name|getEncodingFromFont
parameter_list|(
name|boolean
name|extractEncoding
parameter_list|)
block|{
comment|// This whole section of code needs to be replaced with an actual type1 font parser!!
comment|// Get the font program from the embedded type font.
name|PDFontDescriptor
name|fontDescriptor
init|=
name|getFontDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|fontDescriptor
operator|!=
literal|null
operator|&&
name|fontDescriptor
operator|instanceof
name|PDFontDescriptorDictionary
condition|)
block|{
name|PDStream
name|fontFile
init|=
operator|(
operator|(
name|PDFontDescriptorDictionary
operator|)
name|fontDescriptor
operator|)
operator|.
name|getFontFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|fontFile
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|BufferedReader
name|in
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|fontFile
operator|.
name|createInputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// this section parses the font program stream searching for a /Encoding entry
comment|// if it contains an array of values a Type1Encoding will be returned
comment|// if it encoding contains an encoding name the corresponding Encoding will be returned
name|String
name|line
init|=
literal|""
decl_stmt|;
name|Type1Encoding
name|encoding
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|in
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|extractEncoding
condition|)
block|{
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"currentdict end"
argument_list|)
condition|)
block|{
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
name|setFontEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"/Encoding"
argument_list|)
condition|)
block|{
if|if
condition|(
name|line
operator|.
name|contains
argument_list|(
literal|"array"
argument_list|)
condition|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|line
argument_list|)
decl_stmt|;
comment|// ignore the first token
name|st
operator|.
name|nextElement
argument_list|()
expr_stmt|;
name|int
name|arraySize
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
decl_stmt|;
name|encoding
operator|=
operator|new
name|Type1Encoding
argument_list|(
name|arraySize
argument_list|)
expr_stmt|;
block|}
comment|// if there is already an encoding, we don't need to
comment|// assign another one
elseif|else
if|if
condition|(
name|getFontEncoding
argument_list|()
operator|==
literal|null
condition|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|line
argument_list|)
decl_stmt|;
comment|// ignore the first token
name|st
operator|.
name|nextElement
argument_list|()
expr_stmt|;
name|String
name|type1Encoding
init|=
name|st
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|setFontEncoding
argument_list|(
name|EncodingManager
operator|.
name|INSTANCE
operator|.
name|getEncoding
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|type1Encoding
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
elseif|else
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"dup"
argument_list|)
condition|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|line
operator|.
name|replaceAll
argument_list|(
literal|"/"
argument_list|,
literal|" /"
argument_list|)
argument_list|)
decl_stmt|;
comment|// ignore the first token
name|st
operator|.
name|nextElement
argument_list|()
expr_stmt|;
name|int
name|index
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|st
operator|.
name|nextToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to get character encoding.  Encoding defintion found without /Encoding line."
argument_list|)
expr_stmt|;
else|else
name|encoding
operator|.
name|addCharacterEncoding
argument_list|(
name|index
argument_list|,
name|name
operator|.
name|replace
argument_list|(
literal|"/"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// according to the pdf reference, all font matrices should be same, except for type 3 fonts.
comment|// but obviously there are some type1 fonts with different matrix values, see pdf sample
comment|// attached to PDFBOX-935
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"/FontMatrix"
argument_list|)
condition|)
block|{
name|String
name|matrixValues
init|=
name|line
operator|.
name|substring
argument_list|(
name|line
operator|.
name|indexOf
argument_list|(
literal|"["
argument_list|)
operator|+
literal|1
argument_list|,
name|line
operator|.
name|lastIndexOf
argument_list|(
literal|"]"
argument_list|)
argument_list|)
decl_stmt|;
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|matrixValues
argument_list|)
decl_stmt|;
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
if|if
condition|(
name|st
operator|.
name|countTokens
argument_list|()
operator|>=
literal|6
condition|)
block|{
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|6
condition|;
name|i
operator|++
control|)
block|{
name|COSFloat
name|floatValue
init|=
operator|new
name|COSFloat
argument_list|(
name|Float
operator|.
name|parseFloat
argument_list|(
name|st
operator|.
name|nextToken
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|floatValue
argument_list|)
expr_stmt|;
block|}
name|fontMatrix
operator|=
operator|new
name|PDMatrix
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|exception
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Can't read the fontmatrix from embedded font file!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error: Could not extract the encoding from the embedded type1 font."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|String
name|encode
parameter_list|(
name|byte
index|[]
name|c
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|type1CFont
operator|!=
literal|null
operator|&&
name|getFontEncoding
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|type1CFont
operator|.
name|encode
argument_list|(
name|c
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|encode
argument_list|(
name|c
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
return|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|PDMatrix
name|getFontMatrix
parameter_list|()
block|{
if|if
condition|(
name|type1CFont
operator|!=
literal|null
condition|)
block|{
return|return
name|type1CFont
operator|.
name|getFontMatrix
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|getFontMatrix
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

