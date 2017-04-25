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
comment|/**  * This is an example of how to use a text matrix.  */
end_comment

begin_class
specifier|public
class|class
name|UsingTextMatrix
block|{
comment|/**      * Constructor.      */
specifier|public
name|UsingTextMatrix
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * creates a sample document with some text using a text matrix.      *      * @param message The message to write in the file.      * @param outfile The resulting PDF.      *      * @throws IOException If there is an error writing the data.      */
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
block|{
comment|// the document
try|try
init|(
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
init|)
block|{
comment|// Page 1
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
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
decl_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|float
name|fontSize
init|=
literal|12.0f
decl_stmt|;
name|PDRectangle
name|pageSize
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
decl_stmt|;
name|float
name|centeredXPosition
init|=
operator|(
name|pageSize
operator|.
name|getWidth
argument_list|()
operator|-
name|fontSize
operator|/
literal|1000f
operator|)
operator|/
literal|2f
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
decl_stmt|;
name|float
name|centeredYPosition
init|=
operator|(
name|pageSize
operator|.
name|getHeight
argument_list|()
operator|-
operator|(
name|stringWidth
operator|*
name|fontSize
operator|)
operator|/
literal|1000f
operator|)
operator|/
literal|3f
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
name|AppendMode
operator|.
name|OVERWRITE
argument_list|,
literal|false
argument_list|)
decl_stmt|;
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
comment|// counterclockwise rotation
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|contentStream
operator|.
name|setTextMatrix
argument_list|(
name|Matrix
operator|.
name|getRotateInstance
argument_list|(
name|i
operator|*
name|Math
operator|.
name|PI
operator|*
literal|0.25
argument_list|,
name|centeredXPosition
argument_list|,
name|pageSize
operator|.
name|getHeight
argument_list|()
operator|-
name|centeredYPosition
argument_list|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
argument_list|(
name|message
operator|+
literal|" "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
comment|// clockwise rotation
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|8
condition|;
name|i
operator|++
control|)
block|{
name|contentStream
operator|.
name|setTextMatrix
argument_list|(
name|Matrix
operator|.
name|getRotateInstance
argument_list|(
operator|-
name|i
operator|*
name|Math
operator|.
name|PI
operator|*
literal|0.25
argument_list|,
name|centeredXPosition
argument_list|,
name|centeredYPosition
argument_list|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
argument_list|(
name|message
operator|+
literal|" "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
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
comment|// Page 2
name|page
operator|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|fontSize
operator|=
literal|1.0f
expr_stmt|;
name|contentStream
operator|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|,
name|AppendMode
operator|.
name|OVERWRITE
argument_list|,
literal|false
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
comment|// text scaling and translation
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|contentStream
operator|.
name|setTextMatrix
argument_list|(
operator|new
name|Matrix
argument_list|(
literal|12
operator|+
operator|(
name|i
operator|*
literal|6
operator|)
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|12
operator|+
operator|(
name|i
operator|*
literal|6
operator|)
argument_list|,
literal|100
argument_list|,
literal|100
operator|+
name|i
operator|*
literal|50
argument_list|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
argument_list|(
name|message
operator|+
literal|" "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
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
comment|// Page 3
name|page
operator|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|fontSize
operator|=
literal|1.0f
expr_stmt|;
name|contentStream
operator|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|,
name|AppendMode
operator|.
name|OVERWRITE
argument_list|,
literal|false
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
name|int
name|i
init|=
literal|0
decl_stmt|;
comment|// text scaling combined with rotation
name|contentStream
operator|.
name|setTextMatrix
argument_list|(
operator|new
name|Matrix
argument_list|(
literal|12
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|12
argument_list|,
name|centeredXPosition
argument_list|,
name|centeredYPosition
operator|*
literal|1.5f
argument_list|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
argument_list|(
name|message
operator|+
literal|" "
operator|+
name|i
operator|++
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setTextMatrix
argument_list|(
operator|new
name|Matrix
argument_list|(
literal|0
argument_list|,
literal|18
argument_list|,
operator|-
literal|18
argument_list|,
literal|0
argument_list|,
name|centeredXPosition
argument_list|,
name|centeredYPosition
operator|*
literal|1.5f
argument_list|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
argument_list|(
name|message
operator|+
literal|" "
operator|+
name|i
operator|++
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setTextMatrix
argument_list|(
operator|new
name|Matrix
argument_list|(
operator|-
literal|24
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
operator|-
literal|24
argument_list|,
name|centeredXPosition
argument_list|,
name|centeredYPosition
operator|*
literal|1.5f
argument_list|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
argument_list|(
name|message
operator|+
literal|" "
operator|+
name|i
operator|++
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setTextMatrix
argument_list|(
operator|new
name|Matrix
argument_list|(
literal|0
argument_list|,
operator|-
literal|30
argument_list|,
literal|30
argument_list|,
literal|0
argument_list|,
name|centeredXPosition
argument_list|,
name|centeredYPosition
operator|*
literal|1.5f
argument_list|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
argument_list|(
name|message
operator|+
literal|" "
operator|+
name|i
operator|++
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
name|doc
operator|.
name|save
argument_list|(
name|outfile
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will create a PDF document with some examples how to use a text matrix.      *       * @param args Command line arguments.      */
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
name|UsingTextMatrix
name|app
init|=
operator|new
name|UsingTextMatrix
argument_list|()
decl_stmt|;
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

