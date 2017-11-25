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
name|interactive
operator|.
name|form
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
comment|/**  * An example of creating an AcroForm and a form field from scratch with a font fully embedded to  * allow non-WinAnsiEncoding input.  *  * The form field is created with properties similar to creating a form with default settings in  * Adobe Acrobat.  *  */
end_comment

begin_class
specifier|public
class|class
name|CreateSimpleFormWithEmbeddedFont
block|{
specifier|private
name|CreateSimpleFormWithEmbeddedFont
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
comment|// Create a new document with an empty page.
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
name|PDAcroForm
name|acroForm
init|=
operator|new
name|PDAcroForm
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setAcroForm
argument_list|(
name|acroForm
argument_list|)
expr_stmt|;
comment|// Note that the font is fully embedded. If you use a different font, make sure that
comment|// its license allows full embedding.
name|PDFont
name|formFont
init|=
name|PDType0Font
operator|.
name|load
argument_list|(
name|doc
argument_list|,
name|CreateSimpleFormWithEmbeddedFont
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf"
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// Add and set the resources and default appearance at the form level
specifier|final
name|PDResources
name|resources
init|=
operator|new
name|PDResources
argument_list|()
decl_stmt|;
name|acroForm
operator|.
name|setDefaultResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
specifier|final
name|String
name|fontName
init|=
name|resources
operator|.
name|add
argument_list|(
name|formFont
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// Acrobat sets the font size on the form level to be
comment|// auto sized as default. This is done by setting the font size to '0'
name|acroForm
operator|.
name|setDefaultResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
name|String
name|defaultAppearanceString
init|=
literal|"/"
operator|+
name|fontName
operator|+
literal|" 0 Tf 0 g"
decl_stmt|;
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
name|textBox
operator|.
name|setDefaultAppearance
argument_list|(
name|defaultAppearanceString
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
comment|// Specify the widget annotation associated with the field
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
argument_list|(
literal|50
argument_list|,
literal|700
argument_list|,
literal|200
argument_list|,
literal|50
argument_list|)
decl_stmt|;
name|widget
operator|.
name|setRectangle
argument_list|(
name|rect
argument_list|)
expr_stmt|;
name|widget
operator|.
name|setPage
argument_list|(
name|page
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
comment|// set the field value. Note that the last character is a turkish capital I with a dot,
comment|// which is not part of WinAnsiEncoding
name|textBox
operator|.
name|setValue
argument_list|(
literal|"Sample field İ"
argument_list|)
expr_stmt|;
name|doc
operator|.
name|save
argument_list|(
literal|"target/SimpleFormWithEmbeddedFont.pdf"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

