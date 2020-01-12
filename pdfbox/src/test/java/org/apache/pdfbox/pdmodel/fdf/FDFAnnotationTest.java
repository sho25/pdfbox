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
name|fdf
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
name|net
operator|.
name|URISyntaxException
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdfparser
operator|.
name|FDFParser
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
comment|/**  * Tests for the FDFAnnotation class.  *   * @author Johanneke Lamberink  *  */
end_comment

begin_class
specifier|public
class|class
name|FDFAnnotationTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|loadXFDFAnnotations
parameter_list|()
throws|throws
name|IOException
throws|,
name|URISyntaxException
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|FDFAnnotationTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"xfdf-test-document-annotations.xml"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|FDFDocument
name|fdfDoc
init|=
name|FDFParser
operator|.
name|loadXFDF
argument_list|(
name|f
argument_list|)
init|)
block|{
name|List
argument_list|<
name|FDFAnnotation
argument_list|>
name|fdfAnnots
init|=
name|fdfDoc
operator|.
name|getCatalog
argument_list|()
operator|.
name|getFDF
argument_list|()
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|18
argument_list|,
name|fdfAnnots
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// test PDFBOX-4345 and PDFBOX-3646
comment|// before the fix, the richtext output was
comment|//<body style="font:12pt Helvetica; color:#D66C00;" xfa:APIVersion="Acrobat:7.0.8" xfa:spec="2.0.2" xmlns="http://www.w3.org/1999/xhtml" xmlns:xfa="http://www.xfa.org/schema/xfa-data/1.0/"><p dir="ltr"><span style="text-decoration:word;font-family:Helvetica">P&2</span></p></body>
comment|// i.e. the& was not escaped, and P&amp;1 and P&amp;3 was missing
name|boolean
name|testedPDFBox4345andPDFBox3646
init|=
literal|false
decl_stmt|;
for|for
control|(
name|FDFAnnotation
name|ann
range|:
name|fdfAnnots
control|)
block|{
if|if
condition|(
name|ann
operator|instanceof
name|FDFAnnotationFreeText
condition|)
block|{
name|FDFAnnotationFreeText
name|annotationFreeText
init|=
operator|(
name|FDFAnnotationFreeText
operator|)
name|ann
decl_stmt|;
if|if
condition|(
literal|"P&1 P&2 P&3"
operator|.
name|equals
argument_list|(
name|annotationFreeText
operator|.
name|getContents
argument_list|()
argument_list|)
condition|)
block|{
name|testedPDFBox4345andPDFBox3646
operator|=
literal|true
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"<body style=\"font:12pt Helvetica; "
operator|+
literal|"color:#D66C00;\" xfa:APIVersion=\"Acrobat:7.0.8\" "
operator|+
literal|"xfa:spec=\"2.0.2\" xmlns=\"http://www.w3.org/1999/xhtml\" "
operator|+
literal|"xmlns:xfa=\"http://www.xfa.org/schema/xfa-data/1.0/\">\n"
operator|+
literal|"<p dir=\"ltr\">P&amp;1<span style=\"text-"
operator|+
literal|"decoration:word;font-family:Helvetica\">P&amp;2</span> "
operator|+
literal|"P&amp;3</p>\n"
operator|+
literal|"</body>"
argument_list|,
name|annotationFreeText
operator|.
name|getRichContents
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Assert
operator|.
name|assertTrue
argument_list|(
name|testedPDFBox4345andPDFBox3646
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

