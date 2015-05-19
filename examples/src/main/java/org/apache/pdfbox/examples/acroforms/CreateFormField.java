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
name|acroforms
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
name|font
operator|.
name|PDType0Font
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
name|PDAnnotationWidget
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
name|form
operator|.
name|PDAcroForm
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
name|form
operator|.
name|PDTextField
import|;
end_import

begin_comment
comment|/**  * An example of creating an AcroForm and a form field from scratch.  */
end_comment

begin_class
specifier|public
class|class
name|CreateFormField
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
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
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
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|String
name|dir
init|=
literal|"../pdfbox/src/main/resources/org/apache/pdfbox/resources/ttf/"
decl_stmt|;
name|PDType0Font
name|font
init|=
name|PDType0Font
operator|.
name|load
argument_list|(
name|document
argument_list|,
operator|new
name|File
argument_list|(
name|dir
operator|+
literal|"LiberationSans-Regular.ttf"
argument_list|)
argument_list|)
decl_stmt|;
comment|// add a new AcroForm and add that to the document
name|PDAcroForm
name|acroForm
init|=
operator|new
name|PDAcroForm
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setAcroForm
argument_list|(
name|acroForm
argument_list|)
expr_stmt|;
comment|// Add and set the resources and default appearance at the form level
name|PDResources
name|res
init|=
operator|new
name|PDResources
argument_list|()
decl_stmt|;
name|COSName
name|fontName
init|=
name|res
operator|.
name|add
argument_list|(
name|font
argument_list|)
decl_stmt|;
name|acroForm
operator|.
name|setDefaultResources
argument_list|(
name|res
argument_list|)
expr_stmt|;
name|String
name|da
init|=
literal|"/"
operator|+
name|fontName
operator|.
name|getName
argument_list|()
operator|+
literal|" 12 Tf 0 g"
decl_stmt|;
name|acroForm
operator|.
name|setDefaultAppearance
argument_list|(
name|da
argument_list|)
expr_stmt|;
comment|// add a form field to the form
name|PDTextField
name|textBox
init|=
operator|new
name|PDTextField
argument_list|(
name|acroForm
argument_list|)
decl_stmt|;
name|textBox
operator|.
name|setPartialName
argument_list|(
literal|"SampleField"
argument_list|)
expr_stmt|;
name|acroForm
operator|.
name|getFields
argument_list|()
operator|.
name|add
argument_list|(
name|textBox
argument_list|)
expr_stmt|;
comment|// specify the annotation associated with the field
comment|// and add it to the page
name|PDAnnotationWidget
name|widget
init|=
name|textBox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDRectangle
name|rect
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|rect
operator|.
name|setLowerLeftX
argument_list|(
operator|(
name|float
operator|)
literal|50
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setLowerLeftY
argument_list|(
operator|(
name|float
operator|)
literal|550
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightX
argument_list|(
operator|(
name|float
operator|)
literal|250
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightY
argument_list|(
operator|(
name|float
operator|)
literal|560
argument_list|)
expr_stmt|;
name|widget
operator|.
name|setRectangle
argument_list|(
name|rect
argument_list|)
expr_stmt|;
name|page
operator|.
name|getAnnotations
argument_list|()
operator|.
name|add
argument_list|(
name|widget
argument_list|)
expr_stmt|;
comment|// set the field value
name|textBox
operator|.
name|setValue
argument_list|(
literal|"English form contents"
argument_list|)
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
literal|"exampleForm.pdf"
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

