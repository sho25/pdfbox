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
name|examples
operator|.
name|pdmodel
package|;
end_package

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
name|PDPageContentStream
operator|.
name|AppendMode
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
name|PDType1Font
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
name|Matrix
import|;
end_import

begin_comment
comment|/**  * This is an example of how to add a message to every page  * in a pdf document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|AddMessageToEachPage
block|{
comment|/**      * Constructor.      */
specifier|public
name|AddMessageToEachPage
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * create the second sample document from the PDF file format specification.      *      * @param file The file to write the PDF to.      * @param message The message to write in the file.      * @param outfile The resulting PDF.      *      * @throws IOException If there is an error writing the data.      */
specifier|public
name|void
name|doIt
parameter_list|(
name|String
name|file
parameter_list|,
name|String
name|message
parameter_list|,
name|String
name|outfile
parameter_list|)
throws|throws
name|IOException
block|{
comment|// the document
name|PDDocument
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA_BOLD
decl_stmt|;
name|float
name|fontSize
init|=
literal|36.0f
decl_stmt|;
for|for
control|(
name|PDPage
name|page
range|:
name|doc
operator|.
name|getPages
argument_list|()
control|)
block|{
name|PDRectangle
name|pageSize
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
decl_stmt|;
name|float
name|stringWidth
init|=
name|font
operator|.
name|getStringWidth
argument_list|(
name|message
argument_list|)
operator|*
name|fontSize
operator|/
literal|1000f
decl_stmt|;
comment|// calculate to center of the page
name|int
name|rotation
init|=
name|page
operator|.
name|getRotation
argument_list|()
decl_stmt|;
name|boolean
name|rotate
init|=
name|rotation
operator|==
literal|90
operator|||
name|rotation
operator|==
literal|270
decl_stmt|;
name|float
name|pageWidth
init|=
name|rotate
condition|?
name|pageSize
operator|.
name|getHeight
argument_list|()
else|:
name|pageSize
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|float
name|pageHeight
init|=
name|rotate
condition|?
name|pageSize
operator|.
name|getWidth
argument_list|()
else|:
name|pageSize
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|float
name|centerX
init|=
name|rotate
condition|?
name|pageHeight
operator|/
literal|2f
else|:
operator|(
name|pageWidth
operator|-
name|stringWidth
operator|)
operator|/
literal|2f
decl_stmt|;
name|float
name|centerY
init|=
name|rotate
condition|?
operator|(
name|pageWidth
operator|-
name|stringWidth
operator|)
operator|/
literal|2f
else|:
name|pageHeight
operator|/
literal|2f
decl_stmt|;
comment|// append the content to the existing stream
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|,
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
comment|// set font and font size
name|contentStream
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
name|fontSize
argument_list|)
expr_stmt|;
comment|// set text color to red
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
literal|255
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|rotate
condition|)
block|{
comment|// rotate the text according to the page rotation
name|contentStream
operator|.
name|setTextMatrix
argument_list|(
name|Matrix
operator|.
name|getRotateInstance
argument_list|(
name|Math
operator|.
name|PI
operator|/
literal|2
argument_list|,
name|centerX
argument_list|,
name|centerY
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|contentStream
operator|.
name|setTextMatrix
argument_list|(
name|Matrix
operator|.
name|getTranslateInstance
argument_list|(
name|centerX
argument_list|,
name|centerY
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|contentStream
operator|.
name|showText
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
name|doc
operator|.
name|save
argument_list|(
name|outfile
argument_list|)
expr_stmt|;
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
comment|/**      * This will create a hello world PDF document.      *<br>      * see usage() for commandline      *      * @param args Command line arguments.      */
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
name|AddMessageToEachPage
name|app
init|=
operator|new
name|AddMessageToEachPage
argument_list|()
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|3
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
name|app
operator|.
name|doIt
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|,
name|args
index|[
literal|1
index|]
argument_list|,
name|args
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will print out a message telling how to use this example.      */
specifier|private
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: "
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"<input-file><Message><output-file>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

