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
name|edit
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
name|PDTrueTypeFont
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
comment|/**  * This will take a text file and ouput a pdf with that text.  *  * @author<a href="ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|TextToPDF
block|{
specifier|private
name|int
name|fontSize
init|=
literal|10
decl_stmt|;
specifier|private
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA
decl_stmt|;
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
literal|null
decl_stmt|;
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
name|getFontDescriptor
argument_list|()
operator|.
name|getFontBoundingBox
argument_list|()
operator|.
name|getHeight
argument_list|()
operator|/
literal|1000
decl_stmt|;
comment|//calculate font height and increase by 5 percent.
name|height
operator|=
name|height
operator|*
name|fontSize
operator|*
literal|1.05f
expr_stmt|;
name|doc
operator|=
operator|new
name|PDDocument
argument_list|()
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
name|StringBuffer
name|nextLineToDraw
init|=
operator|new
name|StringBuffer
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
literal|1000
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
name|moveTextPositionByAmount
argument_list|(
name|margin
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
comment|//System.out.println( "Drawing string at " + x + "," + y );
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
name|moveTextPositionByAmount
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
name|drawString
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
return|return
name|doc
return|;
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
literal|null
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
name|PDType1Font
operator|.
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
name|PDTrueTypeFont
name|font
init|=
name|PDTrueTypeFont
operator|.
name|loadTTF
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
name|doc
operator|=
name|app
operator|.
name|createPDFFromText
argument_list|(
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
name|PDType1Font
operator|.
name|getStandard14Names
argument_list|()
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: jar -jar pdfbox-app-x.y.z.jar TextToPDF [options]<output-file><text-file>"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"    -standardFont<name>    default:"
operator|+
name|PDType1Font
operator|.
name|HELVETICA
operator|.
name|getBaseFont
argument_list|()
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"                                    "
operator|+
name|std14String
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"    -ttf<ttf file>         The TTF font to use."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"    -fontSize<fontSize>    default:10"
argument_list|)
expr_stmt|;
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

