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
name|optionalcontent
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
name|HashSet
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
name|pdmodel
operator|.
name|graphics
operator|.
name|optionalcontent
operator|.
name|PDOptionalContentProperties
operator|.
name|BaseState
import|;
end_import

begin_comment
comment|/**  * Tests optional content group functionality (also called layers).  */
end_comment

begin_class
specifier|public
class|class
name|TestOptionalContentGroups
extends|extends
name|TestCase
block|{
specifier|private
specifier|final
name|File
name|testResultsDir
init|=
operator|new
name|File
argument_list|(
literal|"target/test-output"
argument_list|)
decl_stmt|;
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
name|getResources
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
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
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
name|AppendMode
operator|.
name|OVERWRITE
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
name|beginMarkedContent
argument_list|(
name|COSName
operator|.
name|OC
argument_list|,
name|background
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
name|newLineAtOffset
argument_list|(
literal|80
argument_list|,
literal|700
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
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
name|newLineAtOffset
argument_list|(
literal|80
argument_list|,
literal|680
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
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
name|endMarkedContent
argument_list|()
expr_stmt|;
comment|//Paint enabled layer
name|contentStream
operator|.
name|beginMarkedContent
argument_list|(
name|COSName
operator|.
name|OC
argument_list|,
name|enabled
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
name|newLineAtOffset
argument_list|(
literal|80
argument_list|,
literal|600
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
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
name|endMarkedContent
argument_list|()
expr_stmt|;
comment|//Paint disabled layer
name|contentStream
operator|.
name|beginMarkedContent
argument_list|(
name|COSName
operator|.
name|OC
argument_list|,
name|disabled
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
name|newLineAtOffset
argument_list|(
literal|80
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
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
name|endMarkedContent
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
literal|1.5f
argument_list|,
name|doc
operator|.
name|getVersion
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
name|PDPage
name|page
init|=
name|doc
operator|.
name|getPage
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDResources
name|resources
init|=
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
name|COSName
name|mc0
init|=
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"oc1"
argument_list|)
decl_stmt|;
name|PDOptionalContentGroup
name|ocg
init|=
operator|(
name|PDOptionalContentGroup
operator|)
name|resources
operator|.
name|getProperties
argument_list|(
name|mc0
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
name|resources
operator|.
name|getProperties
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
argument_list|<>
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
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|coll
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|nameSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|PDOptionalContentGroup
name|ocg2
range|:
name|coll
control|)
block|{
name|nameSet
operator|.
name|add
argument_list|(
name|ocg2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|nameSet
operator|.
name|contains
argument_list|(
literal|"background"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|nameSet
operator|.
name|contains
argument_list|(
literal|"enabled"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|nameSet
operator|.
name|contains
argument_list|(
literal|"disabled"
argument_list|)
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
specifier|public
name|void
name|testOCGsWithSameNameCanHaveDifferentVisibility
parameter_list|()
throws|throws
name|Exception
block|{
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
name|getResources
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
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setOCProperties
argument_list|(
name|ocprops
argument_list|)
expr_stmt|;
comment|//ocprops.setBaseState(BaseState.ON); //ON=default
comment|//Create visible OCG
name|PDOptionalContentGroup
name|visible
init|=
operator|new
name|PDOptionalContentGroup
argument_list|(
literal|"layer"
argument_list|)
decl_stmt|;
name|ocprops
operator|.
name|addGroup
argument_list|(
name|visible
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ocprops
operator|.
name|isGroupEnabled
argument_list|(
name|visible
argument_list|)
argument_list|)
expr_stmt|;
comment|//Create invisible OCG
name|PDOptionalContentGroup
name|invisible
init|=
operator|new
name|PDOptionalContentGroup
argument_list|(
literal|"layer"
argument_list|)
decl_stmt|;
name|ocprops
operator|.
name|addGroup
argument_list|(
name|invisible
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ocprops
operator|.
name|setGroupEnabled
argument_list|(
name|invisible
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
name|invisible
argument_list|)
argument_list|)
expr_stmt|;
comment|//Check that visible layer is still visible
name|assertTrue
argument_list|(
name|ocprops
operator|.
name|isGroupEnabled
argument_list|(
name|visible
argument_list|)
argument_list|)
expr_stmt|;
comment|//Setup page content stream and paint background/title
try|try
init|(
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
init|)
block|{
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA_BOLD
decl_stmt|;
name|contentStream
operator|.
name|beginMarkedContent
argument_list|(
name|COSName
operator|.
name|OC
argument_list|,
name|visible
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
name|newLineAtOffset
argument_list|(
literal|80
argument_list|,
literal|700
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
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
name|newLineAtOffset
argument_list|(
literal|80
argument_list|,
literal|680
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
argument_list|(
literal|"You should see this text, but no red text line."
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|endMarkedContent
argument_list|()
expr_stmt|;
comment|//Paint disabled layer
name|contentStream
operator|.
name|beginMarkedContent
argument_list|(
name|COSName
operator|.
name|OC
argument_list|,
name|invisible
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
name|newLineAtOffset
argument_list|(
literal|80
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|showText
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
name|endMarkedContent
argument_list|()
expr_stmt|;
block|}
name|File
name|targetFile
init|=
operator|new
name|File
argument_list|(
name|testResultsDir
argument_list|,
literal|"ocg-generation-same-name.pdf"
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
block|}
block|}
end_class

end_unit

