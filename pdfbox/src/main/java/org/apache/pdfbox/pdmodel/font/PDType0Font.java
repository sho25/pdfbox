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
name|encoding
operator|.
name|DictionaryEncoding
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|PDRectangle
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
name|util
operator|.
name|ResourceLoader
import|;
end_import

begin_comment
comment|/**  * Type 0 (composite) Font.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDType0Font
extends|extends
name|PDFont
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
name|PDType0Font
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PDCIDFont
name|descendantFont
decl_stmt|;
specifier|private
name|COSDictionary
name|descendantFontDictionary
decl_stmt|;
comment|/**      * Constructor.      *       * @param fontDictionary The font dictionary according to the PDF specification.      */
specifier|public
name|PDType0Font
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
name|COSArray
name|descendantFonts
init|=
operator|(
name|COSArray
operator|)
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DESCENDANT_FONTS
argument_list|)
decl_stmt|;
name|descendantFontDictionary
operator|=
operator|(
name|COSDictionary
operator|)
name|descendantFonts
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|descendantFontDictionary
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|descendantFont
operator|=
operator|(
name|PDCIDFont
operator|)
name|PDFontFactory
operator|.
name|createFont
argument_list|(
name|descendantFontDictionary
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error while creating the descendant font!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns the descendant font.      *      * @return the descendant font.      */
specifier|public
name|PDCIDFont
name|getDescendantFont
parameter_list|()
block|{
return|return
name|descendantFont
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDFontDescriptor
name|getFontDescriptor
parameter_list|()
block|{
return|return
name|descendantFont
operator|.
name|getFontDescriptor
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDRectangle
name|getFontBoundingBox
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Not yet implemented"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getFontWidth
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
return|return
name|descendantFont
operator|.
name|getFontWidth
argument_list|(
name|c
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getFontHeight
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
return|return
name|descendantFont
operator|.
name|getFontHeight
argument_list|(
name|c
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getAverageFontWidth
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|descendantFont
operator|.
name|getAverageFontWidth
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
name|getFontWidth
parameter_list|(
name|int
name|charCode
parameter_list|)
block|{
return|return
name|descendantFont
operator|.
name|getFontWidth
argument_list|(
name|charCode
argument_list|)
return|;
block|}
comment|// todo: copied from PDSimpleFont and modified
comment|// todo: for a Type 0 font this can only be "The name of a predefined CMap, or a stream containing a
comment|// CMap that maps character codes to font numbers and CIDs", so I should adjust this accordingly
annotation|@
name|Override
specifier|protected
name|void
name|determineEncoding
parameter_list|()
block|{
name|String
name|cmapName
init|=
literal|null
decl_stmt|;
name|COSName
name|encodingName
init|=
literal|null
decl_stmt|;
name|COSBase
name|encoding
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
name|Encoding
name|fontEncoding
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|encoding
operator|instanceof
name|COSName
condition|)
block|{
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
name|encodingName
operator|=
operator|(
name|COSName
operator|)
name|encoding
expr_stmt|;
name|cmap
operator|=
name|cmapObjects
operator|.
name|get
argument_list|(
name|encodingName
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
name|cmapName
operator|=
name|encodingName
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cmap
operator|==
literal|null
operator|&&
name|cmapName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|fontEncoding
operator|=
name|Encoding
operator|.
name|getInstance
argument_list|(
name|encodingName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Debug: Could not find encoding for "
operator|+
name|encodingName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|encoding
operator|instanceof
name|COSStream
condition|)
block|{
if|if
condition|(
name|cmap
operator|==
literal|null
condition|)
block|{
name|COSStream
name|encodingStream
init|=
operator|(
name|COSStream
operator|)
name|encoding
decl_stmt|;
try|try
block|{
name|InputStream
name|is
init|=
name|encodingStream
operator|.
name|getUnfilteredStream
argument_list|()
decl_stmt|;
name|cmap
operator|=
name|parseCmap
argument_list|(
literal|null
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error: Could not parse the embedded CMAP"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|encoding
operator|instanceof
name|COSDictionary
condition|)
block|{
try|try
block|{
name|fontEncoding
operator|=
operator|new
name|DictionaryEncoding
argument_list|(
operator|(
name|COSDictionary
operator|)
name|encoding
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error: Could not create the DictionaryEncoding"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|this
operator|.
name|fontEncoding
operator|=
name|fontEncoding
expr_stmt|;
name|extractToUnicodeEncoding
argument_list|()
expr_stmt|;
comment|// todo: IMPORTANT!
if|if
condition|(
name|cmap
operator|==
literal|null
operator|&&
name|cmapName
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|cmapStream
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// look for a predefined CMap with the given name
name|cmapStream
operator|=
name|ResourceLoader
operator|.
name|loadResource
argument_list|(
name|resourceRootCMAP
operator|+
name|cmapName
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmapStream
operator|!=
literal|null
condition|)
block|{
name|cmap
operator|=
name|parseCmap
argument_list|(
name|resourceRootCMAP
argument_list|,
name|cmapStream
argument_list|)
expr_stmt|;
if|if
condition|(
name|cmap
operator|==
literal|null
operator|&&
name|encodingName
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error: Could not parse predefined CMAP file for '"
operator|+
name|cmapName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Debug: '"
operator|+
name|cmapName
operator|+
literal|"' isn't a predefined map, most likely it's"
operator|+
literal|"embedded in the pdf itself."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error: Could not find predefined CMAP file for '"
operator|+
name|cmapName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|cmapStream
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|String
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|hasToUnicode
argument_list|()
condition|)
block|{
name|retval
operator|=
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
expr_stmt|;
block|}
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|int
name|result
init|=
name|cmap
operator|.
name|lookupCID
argument_list|(
name|c
argument_list|,
name|offset
argument_list|,
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
operator|-
literal|1
condition|)
block|{
name|retval
operator|=
name|descendantFont
operator|.
name|cmapEncoding
argument_list|(
name|result
argument_list|,
literal|2
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|descendantFont
operator|!=
literal|null
condition|)
block|{
name|descendantFont
operator|.
name|clear
argument_list|()
expr_stmt|;
name|descendantFont
operator|=
literal|null
expr_stmt|;
block|}
name|descendantFontDictionary
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

