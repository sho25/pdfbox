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
name|documentmanipulation
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
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
name|form
operator|.
name|PDFormXObject
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
name|LayerUtility
import|;
end_import

begin_comment
comment|/**  * Example to show superimposing a PDF page onto another PDF.  *  */
end_comment

begin_class
specifier|public
class|class
name|SuperimposePage
block|{
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
try|try
block|{
comment|// Create a new document with some basic content
name|PDDocument
name|aDoc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|PDPage
name|aPage
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
comment|// get the page crop box. Will be used later to place the
comment|// imported page.
name|PDRectangle
name|cropBox
init|=
name|aPage
operator|.
name|getCropBox
argument_list|()
decl_stmt|;
name|aDoc
operator|.
name|addPage
argument_list|(
name|aPage
argument_list|)
expr_stmt|;
name|PDPageContentStream
name|aContent
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|aDoc
argument_list|,
name|aPage
argument_list|)
decl_stmt|;
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA_BOLD
decl_stmt|;
name|aContent
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|aContent
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|aContent
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|2
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|aContent
operator|.
name|drawString
argument_list|(
literal|"Import a pdf file:"
argument_list|)
expr_stmt|;
name|aContent
operator|.
name|endText
argument_list|()
expr_stmt|;
name|aContent
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Superimpose a page form a source document
comment|// This will handle the actual import and resources
name|LayerUtility
name|layerUtility
init|=
operator|new
name|LayerUtility
argument_list|(
name|aDoc
argument_list|)
decl_stmt|;
name|PDDocument
name|toBeImported
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|)
decl_stmt|;
comment|// Get the page as a PDXObjectForm to place it
name|PDFormXObject
name|mountable
init|=
name|layerUtility
operator|.
name|importPageAsForm
argument_list|(
name|toBeImported
argument_list|,
literal|0
argument_list|)
decl_stmt|;
comment|// add compression to the stream (import deactivates compression)
name|mountable
operator|.
name|getPDStream
argument_list|()
operator|.
name|addCompression
argument_list|()
expr_stmt|;
comment|// add to the existing content stream
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|aDoc
argument_list|,
name|aPage
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Store the graphics state
name|contentStream
operator|.
name|appendRawCommands
argument_list|(
literal|"q\n"
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
comment|// use some sample transformations
name|AffineTransform
name|transform
init|=
operator|new
name|AffineTransform
argument_list|(
literal|0
argument_list|,
literal|0.5
argument_list|,
operator|-
literal|0.5
argument_list|,
literal|0
argument_list|,
name|cropBox
operator|.
name|getWidth
argument_list|()
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|drawXObject
argument_list|(
name|mountable
argument_list|,
name|transform
argument_list|)
expr_stmt|;
name|transform
operator|=
operator|new
name|AffineTransform
argument_list|(
literal|0.5
argument_list|,
literal|0.5
argument_list|,
operator|-
literal|0.5
argument_list|,
literal|0.5
argument_list|,
literal|0.5
operator|*
name|cropBox
operator|.
name|getWidth
argument_list|()
argument_list|,
literal|0.2
operator|*
name|cropBox
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawXObject
argument_list|(
name|mountable
argument_list|,
name|transform
argument_list|)
expr_stmt|;
comment|// restore former graphics state
name|contentStream
operator|.
name|appendRawCommands
argument_list|(
literal|"Q\n"
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// close the imported document
name|toBeImported
operator|.
name|close
argument_list|()
expr_stmt|;
name|aDoc
operator|.
name|save
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|aDoc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" error creating pdf file."
operator|+
name|e
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

