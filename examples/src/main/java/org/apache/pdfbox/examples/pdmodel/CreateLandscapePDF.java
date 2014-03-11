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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
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
name|exceptions
operator|.
name|CryptographyException
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
name|exceptions
operator|.
name|SignatureException
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
name|PDType1Font
import|;
end_import

begin_comment
comment|/**  * This is an example of how to create a page with a landscape orientation.  * @version $Revision: 1.0 $  */
end_comment

begin_class
specifier|public
class|class
name|CreateLandscapePDF
block|{
comment|/**      * Constructor.      */
specifier|public
name|CreateLandscapePDF
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * creates a sample document with a landscape orientation and some text surrounded by a box.      *      * @param message The message to write in the file.      * @param outfile The resulting PDF.      *      * @throws IOException If there is an error writing the data.      */
specifier|public
name|void
name|doIt
parameter_list|(
name|String
name|message
parameter_list|,
name|String
name|outfile
parameter_list|)
throws|throws
name|IOException
throws|,
name|CryptographyException
throws|,
name|SignatureException
throws|,
name|NoSuchAlgorithmException
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
operator|new
name|PDDocument
argument_list|()
expr_stmt|;
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA
decl_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|page
operator|.
name|setMediaBox
argument_list|(
name|PDPage
operator|.
name|PAGE_SIZE_A4
argument_list|)
expr_stmt|;
name|page
operator|.
name|setRotation
argument_list|(
literal|90
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDRectangle
name|pageSize
init|=
name|page
operator|.
name|findMediaBox
argument_list|()
decl_stmt|;
name|float
name|pageWidth
init|=
name|pageSize
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|float
name|fontSize
init|=
literal|12
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
name|float
name|startX
init|=
literal|100
decl_stmt|;
name|float
name|startY
init|=
literal|100
decl_stmt|;
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
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// add the rotation using the current transformation matrix
comment|// including a translation of pageWidth to use the lower left corner as 0,0 reference
name|contentStream
operator|.
name|concatenate2CTM
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
operator|-
literal|1
argument_list|,
literal|0
argument_list|,
name|pageWidth
argument_list|,
literal|0
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
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
name|startX
argument_list|,
name|startY
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|0
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|100
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
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
name|drawLine
argument_list|(
name|startX
operator|-
literal|2
argument_list|,
name|startY
operator|-
literal|2
argument_list|,
name|startX
operator|-
literal|2
argument_list|,
name|startY
operator|+
literal|200
operator|+
name|fontSize
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawLine
argument_list|(
name|startX
operator|-
literal|2
argument_list|,
name|startY
operator|+
literal|200
operator|+
name|fontSize
argument_list|,
name|startX
operator|+
literal|100
operator|+
name|stringWidth
operator|+
literal|2
argument_list|,
name|startY
operator|+
literal|200
operator|+
name|fontSize
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawLine
argument_list|(
name|startX
operator|+
literal|100
operator|+
name|stringWidth
operator|+
literal|2
argument_list|,
name|startY
operator|+
literal|200
operator|+
name|fontSize
argument_list|,
name|startX
operator|+
literal|100
operator|+
name|stringWidth
operator|+
literal|2
argument_list|,
name|startY
operator|-
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawLine
argument_list|(
name|startX
operator|+
literal|100
operator|+
name|stringWidth
operator|+
literal|2
argument_list|,
name|startY
operator|-
literal|2
argument_list|,
name|startX
operator|-
literal|2
argument_list|,
name|startY
operator|-
literal|2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
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
comment|/**      * This will create a PDF document with a landscape orientation and some text surrounded by a box.      *       * @param args Command line arguments.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|CreateLandscapePDF
name|app
init|=
operator|new
name|CreateLandscapePDF
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
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
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
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
literal|"<Message><output-file>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

