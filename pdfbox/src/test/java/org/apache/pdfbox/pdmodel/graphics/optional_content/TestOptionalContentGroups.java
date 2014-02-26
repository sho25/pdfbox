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
name|graphics
operator|.
name|optional_content
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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|optional_content
operator|.
name|PDOptionalContentProperties
operator|.
name|BaseState
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
name|PDPropertyList
import|;
end_import

begin_comment
comment|/**  * Tests optional content group functionality (also called layers).  *  * @version $Revision$  */
end_comment

begin_class
specifier|public
class|class
name|TestOptionalContentGroups
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
comment|/**      * Tests OCG generation.      * @throws Exception if an error occurs      */
specifier|public
name|void
name|testOCGGeneration
parameter_list|()
throws|throws
name|Exception
block|{
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
try|try
block|{
comment|//OCGs have been introduced with PDF 1.5
name|doc
operator|.
name|getDocument
argument_list|()
operator|.
name|setHeaderString
argument_list|(
literal|"%PDF-1.5"
argument_list|)
expr_stmt|;
name|PDDocumentCatalog
name|catalog
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|catalog
operator|.
name|setVersion
argument_list|(
literal|"1.5"
argument_list|)
expr_stmt|;
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
comment|//Prepare OCG functionality
name|PDOptionalContentProperties
name|ocprops
init|=
operator|new
name|PDOptionalContentProperties
argument_list|()
decl_stmt|;
name|catalog
operator|.
name|setOCProperties
argument_list|(
name|ocprops
argument_list|)
expr_stmt|;
comment|//ocprops.setBaseState(BaseState.ON); //ON=default
comment|//Create OCG for background
name|PDOptionalContentGroup
name|background
init|=
operator|new
name|PDOptionalContentGroup
argument_list|(
literal|"background"
argument_list|)
decl_stmt|;
name|ocprops
operator|.
name|addGroup
argument_list|(
name|background
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ocprops
operator|.
name|isGroupEnabled
argument_list|(
literal|"background"
argument_list|)
argument_list|)
expr_stmt|;
comment|//Create OCG for enabled
name|PDOptionalContentGroup
name|enabled
init|=
operator|new
name|PDOptionalContentGroup
argument_list|(
literal|"enabled"
argument_list|)
decl_stmt|;
name|ocprops
operator|.
name|addGroup
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ocprops
operator|.
name|setGroupEnabled
argument_list|(
literal|"enabled"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ocprops
operator|.
name|isGroupEnabled
argument_list|(
literal|"enabled"
argument_list|)
argument_list|)
expr_stmt|;
comment|//Create OCG for disabled
name|PDOptionalContentGroup
name|disabled
init|=
operator|new
name|PDOptionalContentGroup
argument_list|(
literal|"disabled"
argument_list|)
decl_stmt|;
name|ocprops
operator|.
name|addGroup
argument_list|(
name|disabled
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ocprops
operator|.
name|setGroupEnabled
argument_list|(
literal|"disabled"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ocprops
operator|.
name|isGroupEnabled
argument_list|(
literal|"disabled"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ocprops
operator|.
name|setGroupEnabled
argument_list|(
literal|"disabled"
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ocprops
operator|.
name|isGroupEnabled
argument_list|(
literal|"disabled"
argument_list|)
argument_list|)
expr_stmt|;
comment|//Add mapping to page
name|PDPropertyList
name|props
init|=
operator|new
name|PDPropertyList
argument_list|()
decl_stmt|;
name|resources
operator|.
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|COSName
name|mc0
init|=
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"MC0"
argument_list|)
decl_stmt|;
name|props
operator|.
name|putMapping
argument_list|(
name|mc0
argument_list|,
name|background
argument_list|)
expr_stmt|;
name|COSName
name|mc1
init|=
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"MC1"
argument_list|)
decl_stmt|;
name|props
operator|.
name|putMapping
argument_list|(
name|mc1
argument_list|,
name|enabled
argument_list|)
expr_stmt|;
name|COSName
name|mc2
init|=
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"MC2"
argument_list|)
decl_stmt|;
name|props
operator|.
name|putMapping
argument_list|(
name|mc2
argument_list|,
name|disabled
argument_list|)
expr_stmt|;
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
name|beginMarkedContentSequence
argument_list|(
name|COSName
operator|.
name|OC
argument_list|,
name|mc0
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
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
name|moveTextPositionByAmount
argument_list|(
literal|80
argument_list|,
literal|700
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
literal|"PDF 1.5: Optional Content Groups"
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
name|contentStream
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|80
argument_list|,
literal|680
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
literal|"You should see a green textline, but no red text line."
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|endMarkedContentSequence
argument_list|()
expr_stmt|;
comment|//Paint enabled layer
name|contentStream
operator|.
name|beginMarkedContentSequence
argument_list|(
name|COSName
operator|.
name|OC
argument_list|,
name|mc1
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
name|Color
operator|.
name|GREEN
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|80
argument_list|,
literal|600
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
literal|"This is from an enabled layer. If you see this, that's good."
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|endMarkedContentSequence
argument_list|()
expr_stmt|;
comment|//Paint disabled layer
name|contentStream
operator|.
name|beginMarkedContentSequence
argument_list|(
name|COSName
operator|.
name|OC
argument_list|,
name|mc2
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setNonStrokingColor
argument_list|(
name|Color
operator|.
name|RED
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|80
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
literal|"This is from a disabled layer. If you see this, that's NOT good!"
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|endMarkedContentSequence
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|File
name|targetFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"ocg-generation.pdf"
argument_list|)
decl_stmt|;
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
block|}
comment|/**      * Tests OCG functions on a loaded PDF.      * @throws Exception if an error occurs      */
specifier|public
name|void
name|testOCGConsumption
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|pdfFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"ocg-generation.pdf"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|pdfFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|testOCGGeneration
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
name|pdfFile
argument_list|)
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|"%PDF-1.5"
argument_list|,
name|doc
operator|.
name|getDocument
argument_list|()
operator|.
name|getHeaderString
argument_list|()
argument_list|)
expr_stmt|;
name|PDDocumentCatalog
name|catalog
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"1.5"
argument_list|,
name|catalog
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
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
name|PDPropertyList
name|props
init|=
name|page
operator|.
name|findResources
argument_list|()
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|props
argument_list|)
expr_stmt|;
name|PDOptionalContentGroup
name|ocg
init|=
name|props
operator|.
name|getOptionalContentGroup
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
literal|"background"
argument_list|,
name|ocg
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|props
operator|.
name|getOptionalContentGroup
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"inexistent"
argument_list|)
argument_list|)
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
name|assertEquals
argument_list|(
name|BaseState
operator|.
name|ON
argument_list|,
name|ocgs
operator|.
name|getBaseState
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ocgs
operator|.
name|getGroupNames
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|names
operator|.
name|contains
argument_list|(
literal|"background"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ocgs
operator|.
name|isGroupEnabled
argument_list|(
literal|"background"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ocgs
operator|.
name|isGroupEnabled
argument_list|(
literal|"enabled"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ocgs
operator|.
name|isGroupEnabled
argument_list|(
literal|"disabled"
argument_list|)
argument_list|)
expr_stmt|;
name|ocgs
operator|.
name|setGroupEnabled
argument_list|(
literal|"background"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ocgs
operator|.
name|isGroupEnabled
argument_list|(
literal|"background"
argument_list|)
argument_list|)
expr_stmt|;
name|PDOptionalContentGroup
name|background
init|=
name|ocgs
operator|.
name|getGroup
argument_list|(
literal|"background"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ocg
operator|.
name|getName
argument_list|()
argument_list|,
name|background
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ocgs
operator|.
name|getGroup
argument_list|(
literal|"inexistent"
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|PDOptionalContentGroup
argument_list|>
name|coll
init|=
name|ocgs
operator|.
name|getOptionalContentGroups
argument_list|()
decl_stmt|;
name|coll
operator|.
name|contains
argument_list|(
name|background
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

