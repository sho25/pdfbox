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
name|documentinterchange
operator|.
name|logicalstructure
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|Loader
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
name|COSArray
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
name|COSBase
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
name|COSDictionary
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
name|cos
operator|.
name|COSObject
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
name|junit
operator|.
name|Assert
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
comment|/**  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PDStructureElementTest
block|{
specifier|private
specifier|static
specifier|final
name|File
name|TARGETPDFDIR
init|=
operator|new
name|File
argument_list|(
literal|"target/pdfs"
argument_list|)
decl_stmt|;
comment|/**      * PDFBOX-4197: test that object references in array attributes of a PDStructureElement are caught.      *      * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox4197
parameter_list|()
throws|throws
name|IOException
block|{
name|Set
argument_list|<
name|Revisions
argument_list|<
name|PDAttributeObject
argument_list|>
argument_list|>
name|attributeSet
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|PDDocument
name|doc
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|TARGETPDFDIR
argument_list|,
literal|"PDFBOX-4197.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|PDStructureTreeRoot
name|structureTreeRoot
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getStructureTreeRoot
argument_list|()
decl_stmt|;
name|checkElement
argument_list|(
name|structureTreeRoot
operator|.
name|getK
argument_list|()
argument_list|,
name|attributeSet
argument_list|)
expr_stmt|;
block|}
comment|// collect attributes and check their count.
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|117
argument_list|,
name|attributeSet
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|cnt
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Revisions
argument_list|<
name|PDAttributeObject
argument_list|>
name|attributes
range|:
name|attributeSet
control|)
block|{
name|cnt
operator|+=
name|attributes
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|111
argument_list|,
name|cnt
argument_list|)
expr_stmt|;
comment|// this one was 105 before PDFBOX-4197 was fixed
block|}
comment|// Each element can be an array, a dictionary or a number.
comment|// See PDF specification Table 323 - Entries in a structure element dictionary
specifier|private
name|void
name|checkElement
parameter_list|(
name|COSBase
name|base
parameter_list|,
name|Set
argument_list|<
name|Revisions
argument_list|<
name|PDAttributeObject
argument_list|>
argument_list|>
name|attributeSet
parameter_list|)
block|{
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
for|for
control|(
name|COSBase
name|base2
range|:
operator|(
name|COSArray
operator|)
name|base
control|)
block|{
if|if
condition|(
name|base2
operator|instanceof
name|COSObject
condition|)
block|{
name|base2
operator|=
operator|(
operator|(
name|COSObject
operator|)
name|base2
operator|)
operator|.
name|getObject
argument_list|()
expr_stmt|;
block|}
name|checkElement
argument_list|(
name|base2
argument_list|,
name|attributeSet
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|kdict
init|=
operator|(
name|COSDictionary
operator|)
name|base
decl_stmt|;
if|if
condition|(
name|kdict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|PG
argument_list|)
condition|)
block|{
name|PDStructureElement
name|structureElement
init|=
operator|new
name|PDStructureElement
argument_list|(
name|kdict
argument_list|)
decl_stmt|;
name|Revisions
argument_list|<
name|PDAttributeObject
argument_list|>
name|attributes
init|=
name|structureElement
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|attributeSet
operator|.
name|add
argument_list|(
name|attributes
argument_list|)
expr_stmt|;
name|Revisions
argument_list|<
name|String
argument_list|>
name|classNames
init|=
name|structureElement
operator|.
name|getClassNames
argument_list|()
decl_stmt|;
comment|//TODO: modify the test to also check for class names, if we ever have a file.
block|}
if|if
condition|(
name|kdict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
condition|)
block|{
name|checkElement
argument_list|(
name|kdict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
argument_list|,
name|attributeSet
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

