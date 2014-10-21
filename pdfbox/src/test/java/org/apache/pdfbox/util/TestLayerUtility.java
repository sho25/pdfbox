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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|PDDocumentCatalog
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
name|PDResources
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
name|pdmodel
operator|.
name|graphics
operator|.
name|optionalcontent
operator|.
name|PDOptionalContentGroup
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
name|optionalcontent
operator|.
name|PDOptionalContentProperties
import|;
end_import

begin_comment
comment|/**  * Tests the {@link LayerUtility} class.  *  * @version $Revision$  */
end_comment

begin_class
specifier|public
class|class
name|TestLayerUtility
extends|extends
name|TestCase
block|{
specifier|private
name|File
name|testResultsDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test-output"
argument_list|)
decl_stmt|;
comment|/** {@inheritDoc} */
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|testResultsDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests layer import.      * @throws Exception if an error occurs      */
specifier|public
name|void
name|testLayerImport
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|mainPDF
init|=
name|createMainPDF
argument_list|()
decl_stmt|;
name|File
name|overlay1
init|=
name|createOverlay1
argument_list|()
decl_stmt|;
name|File
name|targetFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"text-with-form-overlay.pdf"
argument_list|)
decl_stmt|;
name|PDDocument
name|targetDoc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|mainPDF
argument_list|)
decl_stmt|;
name|PDDocument
name|overlay1Doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|overlay1
argument_list|)
decl_stmt|;
try|try
block|{
name|LayerUtility
name|layerUtil
init|=
operator|new
name|LayerUtility
argument_list|(
name|targetDoc
argument_list|)
decl_stmt|;
name|PDFormXObject
name|form
init|=
name|layerUtil
operator|.
name|importPageAsForm
argument_list|(
name|overlay1Doc
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|PDDocumentCatalog
name|catalog
init|=
name|targetDoc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDPage
name|targetPage
init|=
operator|(
name|PDPage
operator|)
name|catalog
operator|.
name|getAllPages
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|layerUtil
operator|.
name|wrapInSaveRestore
argument_list|(
name|targetPage
argument_list|)
expr_stmt|;
name|AffineTransform
name|at
init|=
operator|new
name|AffineTransform
argument_list|()
decl_stmt|;
name|PDOptionalContentGroup
name|ocg
init|=
name|layerUtil
operator|.
name|appendFormAsLayer
argument_list|(
name|targetPage
argument_list|,
name|form
argument_list|,
name|at
argument_list|,
literal|"overlay"
argument_list|)
decl_stmt|;
comment|//This is how the layer could be disabled after adding it
comment|//catalog.getOCProperties().setGroupEnabled(ocg.getName(), false);
name|targetDoc
operator|.
name|save
argument_list|(
name|targetFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|targetDoc
operator|.
name|close
argument_list|()
expr_stmt|;
name|overlay1Doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|targetFile
argument_list|)
decl_stmt|;
try|try
block|{
name|PDDocumentCatalog
name|catalog
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
comment|//OCGs require PDF 1.5 or later
comment|//TODO need some comfortable way to enable/check the PDF version
comment|//assertEquals("%PDF-1.5", doc.getDocument().getHeaderString());
comment|//assertEquals("1.5", catalog.getVersion());
name|PDPage
name|page
init|=
operator|(
name|PDPage
operator|)
name|catalog
operator|.
name|getAllPages
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDOptionalContentGroup
name|ocg
init|=
operator|(
name|PDOptionalContentGroup
operator|)
name|page
operator|.
name|findResources
argument_list|()
operator|.
name|getProperties
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"MC0"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ocg
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"overlay"
argument_list|,
name|ocg
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|PDOptionalContentProperties
name|ocgs
init|=
name|catalog
operator|.
name|getOCProperties
argument_list|()
decl_stmt|;
name|PDOptionalContentGroup
name|overlay
init|=
name|ocgs
operator|.
name|getGroup
argument_list|(
literal|"overlay"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ocg
operator|.
name|getName
argument_list|()
argument_list|,
name|overlay
operator|.
name|getName
argument_list|()
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
specifier|private
name|File
name|createMainPDF
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|targetFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"text-doc.pdf"
argument_list|)
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
comment|//Create new page
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
name|PDResources
name|resources
init|=
name|page
operator|.
name|findResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
name|resources
operator|=
operator|new
name|PDResources
argument_list|()
expr_stmt|;
name|page
operator|.
name|setResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
index|[]
name|text
init|=
operator|new
name|String
index|[]
block|{
literal|"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer fermentum lacus in eros"
block|,
literal|"condimentum eget tristique risus viverra. Sed ac sem et lectus ultrices placerat. Nam"
block|,
literal|"fringilla tincidunt nulla id euismod. Vivamus eget mauris dui. Mauris luctus ullamcorper"
block|,
literal|"leo, et laoreet diam suscipit et. Nulla viverra commodo sagittis. Integer vitae rhoncus velit."
block|,
literal|"Mauris porttitor ipsum in est sagittis non luctus purus molestie. Sed placerat aliquet"
block|,
literal|"vulputate."
block|}
decl_stmt|;
comment|//Setup page content stream and paint background/title
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
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA_BOLD
decl_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|50
argument_list|,
literal|720
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|14
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
literal|"Simple test document with text."
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|font
operator|=
name|PDType1Font
operator|.
name|HELVETICA
expr_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|int
name|fontSize
init|=
literal|12
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
name|moveTextPositionByAmount
argument_list|(
literal|50
argument_list|,
literal|700
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|line
range|:
name|text
control|)
block|{
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|0
argument_list|,
operator|-
name|fontSize
operator|*
literal|1.2f
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
name|line
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
name|doc
operator|.
name|save
argument_list|(
name|targetFile
operator|.
name|getAbsolutePath
argument_list|()
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
return|return
name|targetFile
return|;
block|}
specifier|private
name|File
name|createOverlay1
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|targetFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"overlay1.pdf"
argument_list|)
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
comment|//Create new page
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
name|PDResources
name|resources
init|=
name|page
operator|.
name|findResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
name|resources
operator|=
operator|new
name|PDResources
argument_list|()
expr_stmt|;
name|page
operator|.
name|setResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
comment|//Setup page content stream and paint background/title
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
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA_BOLD
decl_stmt|;
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
name|Color
operator|.
name|LIGHT_GRAY
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|float
name|fontSize
init|=
literal|96
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
name|String
name|text
init|=
literal|"OVERLAY"
decl_stmt|;
comment|//float sw = font.getStringWidth(text);
comment|//Too bad, base 14 fonts don't return character metrics.
name|PDRectangle
name|crop
init|=
name|page
operator|.
name|findCropBox
argument_list|()
decl_stmt|;
name|float
name|cx
init|=
name|crop
operator|.
name|getWidth
argument_list|()
operator|/
literal|2f
decl_stmt|;
name|float
name|cy
init|=
name|crop
operator|.
name|getHeight
argument_list|()
operator|/
literal|2f
decl_stmt|;
name|AffineTransform
name|transform
init|=
operator|new
name|AffineTransform
argument_list|()
decl_stmt|;
name|transform
operator|.
name|translate
argument_list|(
name|cx
argument_list|,
name|cy
argument_list|)
expr_stmt|;
name|transform
operator|.
name|rotate
argument_list|(
name|Math
operator|.
name|toRadians
argument_list|(
literal|45
argument_list|)
argument_list|)
expr_stmt|;
name|transform
operator|.
name|translate
argument_list|(
operator|-
literal|190
comment|/* sw/2 */
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setTextMatrix
argument_list|(
name|transform
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
name|text
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
name|targetFile
operator|.
name|getAbsolutePath
argument_list|()
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
return|return
name|targetFile
return|;
block|}
block|}
end_class

end_unit

