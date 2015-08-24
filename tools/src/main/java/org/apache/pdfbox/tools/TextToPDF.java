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
name|tools
package|;
end_package

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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
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
name|Reader
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDDocument
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
name|PDPage
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
name|PDPageContentStream
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
name|PDType0Font
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
name|PDType1Font
import|;
end_import

begin_comment
comment|/**  * This will take a text file and ouput a pdf with that text.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|TextToPDF
block|{
comment|/**      * The scaling factor for font units to PDF units      */
specifier|private
specifier|static
specifier|final
name|int
name|FONTSCALE
init|=
literal|1000
decl_stmt|;
comment|/**      * The default font      */
specifier|private
specifier|static
specifier|final
name|PDType1Font
name|DEFAULT_FONT
init|=
name|PDType1Font
operator|.
name|HELVETICA
decl_stmt|;
comment|/**      * The default font size      */
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_FONT_SIZE
init|=
literal|10
decl_stmt|;
comment|/**      * The line height as a factor of the font size      */
specifier|private
specifier|static
specifier|final
name|float
name|LINE_HEIGHT_FACTOR
init|=
literal|1.05f
decl_stmt|;
specifier|private
name|int
name|fontSize
init|=
name|DEFAULT_FONT_SIZE
decl_stmt|;
specifier|private
name|PDFont
name|font
init|=
name|DEFAULT_FONT
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
name|PDType1Font
operator|.
name|TIMES_ROMAN
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|TIMES_ROMAN
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|TIMES_BOLD
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|TIMES_BOLD
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|TIMES_ITALIC
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|TIMES_ITALIC
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|TIMES_BOLD_ITALIC
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|TIMES_BOLD_ITALIC
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|HELVETICA
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|HELVETICA
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|HELVETICA_BOLD
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|HELVETICA_BOLD
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|HELVETICA_OBLIQUE
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|HELVETICA_OBLIQUE
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|HELVETICA_BOLD_OBLIQUE
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|HELVETICA_BOLD_OBLIQUE
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|COURIER
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|COURIER
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|COURIER_BOLD
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|COURIER_BOLD
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|COURIER_OBLIQUE
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|COURIER_OBLIQUE
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|COURIER_BOLD_OBLIQUE
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|COURIER_BOLD_OBLIQUE
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|SYMBOL
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|SYMBOL
argument_list|)
expr_stmt|;
name|STANDARD_14
operator|.
name|put
argument_list|(
name|PDType1Font
operator|.
name|ZAPF_DINGBATS
operator|.
name|getBaseFont
argument_list|()
argument_list|,
name|PDType1Font
operator|.
name|ZAPF_DINGBATS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a PDF document with some text.      *      * @param text The stream of text data.      *      * @return The document with the text in it.      *      * @throws IOException If there is an error writing the data.      */
specifier|public
name|PDDocument
name|createPDFFromText
parameter_list|(
name|Reader
name|text
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|createPDFFromText
argument_list|(
name|doc
argument_list|,
name|text
argument_list|)
expr_stmt|;
return|return
name|doc
return|;
block|}
comment|/**      * Create a PDF document with some text.      *      * @param text The stream of text data.      *      * @throws IOException If there is an error writing the data.      */
specifier|public
name|void
name|createPDFFromText
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|Reader
name|text
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
specifier|final
name|int
name|margin
init|=
literal|40
decl_stmt|;
name|float
name|height
init|=
name|font
operator|.
name|getBoundingBox
argument_list|()
operator|.
name|getHeight
argument_list|()
operator|/
name|FONTSCALE
decl_stmt|;
comment|//calculate font height and increase by a factor.
name|height
operator|=
name|height
operator|*
name|fontSize
operator|*
name|LINE_HEIGHT_FACTOR
expr_stmt|;
name|BufferedReader
name|data
init|=
operator|new
name|BufferedReader
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|String
name|nextLine
init|=
literal|null
decl_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|PDPageContentStream
name|contentStream
init|=
literal|null
decl_stmt|;
name|float
name|y
init|=
operator|-
literal|1
decl_stmt|;
name|float
name|maxStringLength
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
operator|.
name|getWidth
argument_list|()
operator|-
literal|2
operator|*
name|margin
decl_stmt|;
comment|// There is a special case of creating a PDF document from an empty string.
name|boolean
name|textIsEmpty
init|=
literal|true
decl_stmt|;
while|while
condition|(
operator|(
name|nextLine
operator|=
name|data
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
comment|// The input text is nonEmpty. New pages will be created and added
comment|// to the PDF document as they are needed, depending on the length of
comment|// the text.
name|textIsEmpty
operator|=
literal|false
expr_stmt|;
name|String
index|[]
name|lineWords
init|=
name|nextLine
operator|.
name|trim
argument_list|()
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|int
name|lineIndex
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|lineIndex
operator|<
name|lineWords
operator|.
name|length
condition|)
block|{
name|StringBuilder
name|nextLineToDraw
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|float
name|lengthIfUsingNextWord
init|=
literal|0
decl_stmt|;
do|do
block|{
name|nextLineToDraw
operator|.
name|append
argument_list|(
name|lineWords
index|[
name|lineIndex
index|]
argument_list|)
expr_stmt|;
name|nextLineToDraw
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|lineIndex
operator|++
expr_stmt|;
if|if
condition|(
name|lineIndex
operator|<
name|lineWords
operator|.
name|length
condition|)
block|{
name|String
name|lineWithNextWord
init|=
name|nextLineToDraw
operator|.
name|toString
argument_list|()
operator|+
name|lineWords
index|[
name|lineIndex
index|]
decl_stmt|;
name|lengthIfUsingNextWord
operator|=
operator|(
name|font
operator|.
name|getStringWidth
argument_list|(
name|lineWithNextWord
argument_list|)
operator|/
name|FONTSCALE
operator|)
operator|*
name|fontSize
expr_stmt|;
block|}
block|}
do|while
condition|(
name|lineIndex
operator|<
name|lineWords
operator|.
name|length
operator|&&
name|lengthIfUsingNextWord
operator|<
name|maxStringLength
condition|)
do|;
if|if
condition|(
name|y
operator|<
name|margin
condition|)
block|{
comment|// We have crossed the end-of-page boundary and need to extend the
comment|// document by another page.
name|page
operator|=
operator|new
name|PDPage
argument_list|()
expr_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
if|if
condition|(
name|contentStream
operator|!=
literal|null
condition|)
block|{
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|contentStream
operator|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
name|fontSize
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|y
operator|=
name|page
operator|.
name|getMediaBox
argument_list|()
operator|.
name|getHeight
argument_list|()
operator|-
name|margin
operator|+
name|height
expr_stmt|;
name|contentStream
operator|.
name|newLineAtOffset
argument_list|(
name|margin
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentStream
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error:Expected non-null content stream."
argument_list|)
throw|;
block|}
name|contentStream
operator|.
name|newLineAtOffset
argument_list|(
literal|0
argument_list|,
operator|-
name|height
argument_list|)
expr_stmt|;
name|y
operator|-=
name|height
expr_stmt|;
name|contentStream
operator|.
name|showText
argument_list|(
name|nextLineToDraw
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// If the input text was the empty string, then the above while loop will have short-circuited
comment|// and we will not have added any PDPages to the document.
comment|// So in order to make the resultant PDF document readable by Adobe Reader etc, we'll add an empty page.
if|if
condition|(
name|textIsEmpty
condition|)
block|{
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentStream
operator|!=
literal|null
condition|)
block|{
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
throw|throw
name|io
throw|;
block|}
block|}
comment|/**      * This will create a PDF document with some text in it.      *<br />      * see usage() for commandline      *      * @param args Command line arguments.      *      * @throws IOException If there is an error with the PDF.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
block|{
comment|// suppress the Dock icon on OS X
name|System
operator|.
name|setProperty
argument_list|(
literal|"apple.awt.UIElement"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|TextToPDF
name|app
init|=
operator|new
name|TextToPDF
argument_list|()
decl_stmt|;
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<
literal|2
condition|)
block|{
name|app
operator|.
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
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
name|args
operator|.
name|length
operator|-
literal|2
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-standardFont"
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
name|app
operator|.
name|setFont
argument_list|(
name|getStandardFont
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-ttf"
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
name|PDFont
name|font
init|=
name|PDType0Font
operator|.
name|load
argument_list|(
name|doc
argument_list|,
operator|new
name|File
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|app
operator|.
name|setFont
argument_list|(
name|font
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-fontSize"
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
name|app
operator|.
name|setFontSize
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown argument:"
operator|+
name|args
index|[
name|i
index|]
argument_list|)
throw|;
block|}
block|}
name|app
operator|.
name|createPDFFromText
argument_list|(
name|doc
argument_list|,
operator|new
name|FileReader
argument_list|(
name|args
index|[
name|args
operator|.
name|length
operator|-
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|save
argument_list|(
name|args
index|[
name|args
operator|.
name|length
operator|-
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This will print out a message telling how to use this example.      */
specifier|private
name|void
name|usage
parameter_list|()
block|{
name|String
index|[]
name|std14
init|=
name|getStandard14Names
argument_list|()
decl_stmt|;
name|StringBuilder
name|message
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|message
operator|.
name|append
argument_list|(
literal|"Usage: jar -jar pdfbox-app-x.y.z.jar TextToPDF [options]<outputfile><textfile>\n"
argument_list|)
expr_stmt|;
name|message
operator|.
name|append
argument_list|(
literal|"\nOptions:\n"
argument_list|)
expr_stmt|;
name|message
operator|.
name|append
argument_list|(
literal|"  -standardFont<name> : "
operator|+
name|DEFAULT_FONT
operator|.
name|getBaseFont
argument_list|()
operator|+
literal|" (default)\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|std14String
range|:
name|std14
control|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|"                         "
operator|+
name|std14String
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|append
argument_list|(
literal|"  -ttf<ttf file>      : The TTF font to use.\n"
argument_list|)
expr_stmt|;
name|message
operator|.
name|append
argument_list|(
literal|"  -fontSize<fontSize> : default: "
operator|+
name|DEFAULT_FONT_SIZE
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * A convenience method to get one of the standard 14 font from name.      *      * @param name The name of the font to get.      *      * @return The font that matches the name or null if it does not exist.      */
specifier|private
specifier|static
name|PDType1Font
name|getStandardFont
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|STANDARD_14
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * This will get the names of the standard 14 fonts.      *      * @return An array of the names of the standard 14 fonts.      */
specifier|private
specifier|static
name|String
index|[]
name|getStandard14Names
parameter_list|()
block|{
return|return
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
comment|/**      * @return Returns the font.      */
specifier|public
name|PDFont
name|getFont
parameter_list|()
block|{
return|return
name|font
return|;
block|}
comment|/**      * @param aFont The font to set.      */
specifier|public
name|void
name|setFont
parameter_list|(
name|PDFont
name|aFont
parameter_list|)
block|{
name|this
operator|.
name|font
operator|=
name|aFont
expr_stmt|;
block|}
comment|/**      * @return Returns the fontSize.      */
specifier|public
name|int
name|getFontSize
parameter_list|()
block|{
return|return
name|fontSize
return|;
block|}
comment|/**      * @param aFontSize The fontSize to set.      */
specifier|public
name|void
name|setFontSize
parameter_list|(
name|int
name|aFontSize
parameter_list|)
block|{
name|this
operator|.
name|fontSize
operator|=
name|aFontSize
expr_stmt|;
block|}
block|}
end_class

end_unit

