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
name|graphics
operator|.
name|image
operator|.
name|PDImageXObject
import|;
end_import

begin_comment
comment|/**  * Creates a PDF document from an image.  *  * The example is taken from the pdf file format specification.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ImageToPDF
block|{
specifier|private
name|ImageToPDF
parameter_list|()
block|{     }
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
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: "
operator|+
name|ImageToPDF
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<image><output-file>"
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
name|String
name|imagePath
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
name|String
name|pdfPath
init|=
name|args
index|[
literal|1
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|pdfPath
operator|.
name|endsWith
argument_list|(
literal|".pdf"
argument_list|)
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Last argument must be the destination .pdf file"
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
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
try|try
block|{
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
comment|// createFromFile is the easiest way with an image file
comment|// if you already have the image in a BufferedImage,
comment|// call LosslessFactory.createFromImage() instead
name|PDImageXObject
name|pdImage
init|=
name|PDImageXObject
operator|.
name|createFromFile
argument_list|(
name|imagePath
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|PDPageContentStream
name|contents
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|)
decl_stmt|;
comment|// draw the image at full size at (x=20, y=20)
name|contents
operator|.
name|drawImage
argument_list|(
name|pdImage
argument_list|,
literal|20
argument_list|,
literal|20
argument_list|)
expr_stmt|;
comment|// to draw the image at half size at (x=20, y=20) use
comment|// contents.drawImage(pdImage, 20, 20, pdImage.getWidth() / 2, pdImage.getHeight() / 2);
name|contents
operator|.
name|close
argument_list|()
expr_stmt|;
name|doc
operator|.
name|save
argument_list|(
name|pdfPath
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

