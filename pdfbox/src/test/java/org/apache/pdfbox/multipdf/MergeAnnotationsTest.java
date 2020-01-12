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
name|multipdf
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|FileInputStream
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
name|InputStream
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
name|List
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
name|pdfparser
operator|.
name|PDFParser
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
name|PDDocumentNameDestinationDictionary
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test merging different PDFs with Annotations.  */
end_comment

begin_class
specifier|public
class|class
name|MergeAnnotationsTest
block|{
specifier|private
specifier|static
specifier|final
name|File
name|OUT_DIR
init|=
operator|new
name|File
argument_list|(
literal|"target/test-output/merge/"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|File
name|TARGET_PDF_DIR
init|=
operator|new
name|File
argument_list|(
literal|"target/pdfs"
argument_list|)
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|OUT_DIR
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
comment|/*      * PDFBOX-1065 Ensure that after merging the PDFs there are all link      * annotations and they point to the correct page.      */
annotation|@
name|Test
specifier|public
name|void
name|testLinkAnnotations
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Merge the PDFs from PDFBOX-1065
name|PDFMergerUtility
name|merger
init|=
operator|new
name|PDFMergerUtility
argument_list|()
decl_stmt|;
name|File
name|file1
init|=
operator|new
name|File
argument_list|(
name|TARGET_PDF_DIR
argument_list|,
literal|"PDFBOX-1065-1.pdf"
argument_list|)
decl_stmt|;
name|File
name|file2
init|=
operator|new
name|File
argument_list|(
name|TARGET_PDF_DIR
argument_list|,
literal|"PDFBOX-1065-2.pdf"
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|is1
init|=
operator|new
name|FileInputStream
argument_list|(
name|file1
argument_list|)
init|;
name|InputStream
name|is2
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file2
argument_list|)
init|)
block|{
name|File
name|pdfOutput
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
literal|"PDFBOX-1065.pdf"
argument_list|)
decl_stmt|;
name|merger
operator|.
name|setDestinationFileName
argument_list|(
name|pdfOutput
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|merger
operator|.
name|addSource
argument_list|(
name|is1
argument_list|)
expr_stmt|;
name|merger
operator|.
name|addSource
argument_list|(
name|is2
argument_list|)
expr_stmt|;
name|merger
operator|.
name|mergeDocuments
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Test merge result
name|PDDocument
name|mergedPDF
init|=
name|PDFParser
operator|.
name|load
argument_list|(
name|pdfOutput
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"There shall be 6 pages"
argument_list|,
literal|6
argument_list|,
name|mergedPDF
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|PDDocumentNameDestinationDictionary
name|destinations
init|=
name|mergedPDF
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getDests
argument_list|()
decl_stmt|;
comment|// Each document has 3 annotations with 2 entries in the /Dests dictionary per annotation. One for the
comment|// source and one for the target.
name|assertEquals
argument_list|(
literal|"There shall be 12 entries"
argument_list|,
literal|12
argument_list|,
name|destinations
operator|.
name|getCOSObject
argument_list|()
operator|.
name|entrySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|sourceAnnotations01
init|=
name|mergedPDF
operator|.
name|getPage
argument_list|(
literal|0
argument_list|)
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|sourceAnnotations02
init|=
name|mergedPDF
operator|.
name|getPage
argument_list|(
literal|3
argument_list|)
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|targetAnnotations01
init|=
name|mergedPDF
operator|.
name|getPage
argument_list|(
literal|2
argument_list|)
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|targetAnnotations02
init|=
name|mergedPDF
operator|.
name|getPage
argument_list|(
literal|5
argument_list|)
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
comment|// Test for the first set of annotations to be merged an linked correctly
name|assertEquals
argument_list|(
literal|"There shall be 3 source annotations at the first page"
argument_list|,
literal|3
argument_list|,
name|sourceAnnotations01
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There shall be 3 source annotations at the third page"
argument_list|,
literal|3
argument_list|,
name|targetAnnotations01
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The annotations shall match to each other"
argument_list|,
name|testAnnotationsMatch
argument_list|(
name|sourceAnnotations01
argument_list|,
name|targetAnnotations01
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test for the second set of annotations to be merged an linked correctly
name|assertEquals
argument_list|(
literal|"There shall be 3 source annotations at the first page"
argument_list|,
literal|3
argument_list|,
name|sourceAnnotations02
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"There shall be 3 source annotations at the third page"
argument_list|,
literal|3
argument_list|,
name|targetAnnotations02
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The annotations shall match to each other"
argument_list|,
name|testAnnotationsMatch
argument_list|(
name|sourceAnnotations02
argument_list|,
name|targetAnnotations02
argument_list|)
argument_list|)
expr_stmt|;
name|mergedPDF
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/*      * Source and target annotations are línked by name with the target annotation's name      * being the source annotation's name prepended with 'annoRef_'      */
specifier|private
name|boolean
name|testAnnotationsMatch
parameter_list|(
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|sourceAnnots
parameter_list|,
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|targetAnnots
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PDAnnotation
argument_list|>
name|targetAnnotsByName
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|COSName
name|destinationName
decl_stmt|;
comment|// fill the map with the annotations destination name
for|for
control|(
name|PDAnnotation
name|targetAnnot
range|:
name|targetAnnots
control|)
block|{
name|destinationName
operator|=
operator|(
name|COSName
operator|)
name|targetAnnot
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DEST
argument_list|)
expr_stmt|;
name|targetAnnotsByName
operator|.
name|put
argument_list|(
name|destinationName
operator|.
name|getName
argument_list|()
argument_list|,
name|targetAnnot
argument_list|)
expr_stmt|;
block|}
comment|// try to lookup the target annotation for the source annotation by destination name
for|for
control|(
name|PDAnnotation
name|sourceAnnot
range|:
name|sourceAnnots
control|)
block|{
name|destinationName
operator|=
operator|(
name|COSName
operator|)
name|sourceAnnot
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DEST
argument_list|)
expr_stmt|;
if|if
condition|(
name|targetAnnotsByName
operator|.
name|get
argument_list|(
literal|"annoRef_"
operator|+
name|destinationName
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

