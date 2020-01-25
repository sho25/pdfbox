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
name|graphics
operator|.
name|color
operator|.
name|PDColor
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
name|color
operator|.
name|PDDeviceRGB
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
name|annotation
operator|.
name|PDAppearanceCharacteristicsDictionary
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
name|PDField
import|;
end_import

begin_comment
comment|/**  * Add a border to an existing field.  *   * This sample adds a border to a field.  *   * This sample builds on the form generated by @link CreateSimpleForm so you need to run that first.  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|AddBorderToField
block|{
specifier|private
name|AddBorderToField
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
comment|// Load the PDF document created by SimpleForm.java
try|try
init|(
name|PDDocument
name|document
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/SimpleForm.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|PDAcroForm
name|acroForm
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
comment|// Get the field and the widget associated to it.
comment|// Note: there might be multiple widgets
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"SampleField"
argument_list|)
decl_stmt|;
name|PDAnnotationWidget
name|widget
init|=
name|field
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// Create the definition for a green border
name|PDAppearanceCharacteristicsDictionary
name|fieldAppearance
init|=
operator|new
name|PDAppearanceCharacteristicsDictionary
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
name|PDColor
name|green
init|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|1
block|,
literal|0
block|}
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|fieldAppearance
operator|.
name|setBorderColour
argument_list|(
name|green
argument_list|)
expr_stmt|;
comment|// Set the information to be used by the widget which is responsible
comment|// for the visual style of the form field.
name|widget
operator|.
name|setAppearanceCharacteristics
argument_list|(
name|fieldAppearance
argument_list|)
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
literal|"target/AddBorderToField.pdf"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

