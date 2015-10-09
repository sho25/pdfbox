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
name|interactive
operator|.
name|form
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
name|assertFalse
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
name|assertNotNull
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
name|junit
operator|.
name|After
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
comment|/**  * Test for the PDButton class.  *  */
end_comment

begin_class
specifier|public
class|class
name|PDButtonTest
block|{
specifier|private
specifier|static
specifier|final
name|File
name|IN_DIR
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/pdfbox/pdmodel/interactive/form"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAME_OF_PDF
init|=
literal|"AcroFormsBasicFields.pdf"
decl_stmt|;
specifier|private
name|PDDocument
name|document
decl_stmt|;
specifier|private
name|PDAcroForm
name|acroForm
decl_stmt|;
specifier|private
name|PDDocument
name|acrobatDocument
decl_stmt|;
specifier|private
name|PDAcroForm
name|acrobatAcroForm
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|IOException
block|{
name|document
operator|=
operator|new
name|PDDocument
argument_list|()
expr_stmt|;
name|acroForm
operator|=
operator|new
name|PDAcroForm
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|acrobatDocument
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|IN_DIR
argument_list|,
name|NAME_OF_PDF
argument_list|)
argument_list|)
expr_stmt|;
name|acrobatAcroForm
operator|=
name|acrobatDocument
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createCheckBox
parameter_list|()
block|{
name|PDButton
name|buttonField
init|=
operator|new
name|PDCheckbox
argument_list|(
name|acroForm
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|buttonField
operator|.
name|getFieldType
argument_list|()
argument_list|,
name|buttonField
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|FT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|buttonField
operator|.
name|getFieldType
argument_list|()
argument_list|,
literal|"Btn"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|buttonField
operator|.
name|isPushButton
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|buttonField
operator|.
name|isRadioButton
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createPushButton
parameter_list|()
block|{
name|PDButton
name|buttonField
init|=
operator|new
name|PDPushButton
argument_list|(
name|acroForm
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|buttonField
operator|.
name|getFieldType
argument_list|()
argument_list|,
name|buttonField
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|FT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|buttonField
operator|.
name|getFieldType
argument_list|()
argument_list|,
literal|"Btn"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|buttonField
operator|.
name|isPushButton
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|buttonField
operator|.
name|isRadioButton
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|createRadioButton
parameter_list|()
block|{
name|PDButton
name|buttonField
init|=
operator|new
name|PDRadioButton
argument_list|(
name|acroForm
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|buttonField
operator|.
name|getFieldType
argument_list|()
argument_list|,
name|buttonField
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|FT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|buttonField
operator|.
name|getFieldType
argument_list|()
argument_list|,
literal|"Btn"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|buttonField
operator|.
name|isRadioButton
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|buttonField
operator|.
name|isPushButton
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|retrieveAcrobatCheckBoxProperties
parameter_list|()
throws|throws
name|IOException
block|{
name|PDCheckbox
name|checkbox
init|=
operator|(
name|PDCheckbox
operator|)
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"Checkbox"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|checkbox
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getOnValue
argument_list|()
argument_list|,
literal|"Yes"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getOnValues
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|checkbox
operator|.
name|getOnValues
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Yes"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcrobatCheckBoxProperties
parameter_list|()
throws|throws
name|IOException
block|{
name|PDCheckbox
name|checkbox
init|=
operator|(
name|PDCheckbox
operator|)
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"Checkbox"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getValue
argument_list|()
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|isChecked
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|checkbox
operator|.
name|check
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getValue
argument_list|()
argument_list|,
name|checkbox
operator|.
name|getOnValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|isChecked
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"Yes"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getValue
argument_list|()
argument_list|,
name|checkbox
operator|.
name|getOnValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|isChecked
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|YES
argument_list|)
expr_stmt|;
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"Off"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getValue
argument_list|()
argument_list|,
name|COSName
operator|.
name|Off
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|isChecked
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|Off
argument_list|)
expr_stmt|;
name|checkbox
operator|=
operator|(
name|PDCheckbox
operator|)
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"Checkbox-DefaultValue"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getDefaultValue
argument_list|()
argument_list|,
name|checkbox
operator|.
name|getOnValue
argument_list|()
argument_list|)
expr_stmt|;
name|checkbox
operator|.
name|setDefaultValue
argument_list|(
literal|"Off"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getDefaultValue
argument_list|()
argument_list|,
name|COSName
operator|.
name|Off
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|setValueForAbstractedAcrobatCheckBox
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|checkbox
init|=
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"Checkbox"
argument_list|)
decl_stmt|;
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"Yes"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getValueAsString
argument_list|()
argument_list|,
operator|(
operator|(
name|PDCheckbox
operator|)
name|checkbox
operator|)
operator|.
name|getOnValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
operator|(
name|PDCheckbox
operator|)
name|checkbox
operator|)
operator|.
name|isChecked
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|YES
argument_list|)
expr_stmt|;
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"Off"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getValueAsString
argument_list|()
argument_list|,
name|COSName
operator|.
name|Off
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
operator|(
name|PDCheckbox
operator|)
name|checkbox
operator|)
operator|.
name|isChecked
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|Off
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcrobatCheckBoxGroupProperties
parameter_list|()
throws|throws
name|IOException
block|{
name|PDCheckbox
name|checkbox
init|=
operator|(
name|PDCheckbox
operator|)
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"CheckboxGroup"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getValue
argument_list|()
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|isChecked
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|checkbox
operator|.
name|check
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getValue
argument_list|()
argument_list|,
name|checkbox
operator|.
name|getOnValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|isChecked
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|checkbox
operator|.
name|getOnValues
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|checkbox
operator|.
name|getOnValues
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Option1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|checkbox
operator|.
name|getOnValues
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Option2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|checkbox
operator|.
name|getOnValues
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Option3"
argument_list|)
argument_list|)
expr_stmt|;
comment|// test a value which sets one of the individual checkboxes within the group
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"Option1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Option1"
argument_list|,
name|checkbox
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Option1"
argument_list|,
name|checkbox
operator|.
name|getValueAsString
argument_list|()
argument_list|)
expr_stmt|;
comment|// ensure that for the widgets representing the individual checkboxes
comment|// the AS entry has been set
name|assertEquals
argument_list|(
literal|"Option1"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Off"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Off"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Off"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// test a value which sets two of the individual chekboxes within the group
comment|// as the have the same name entry for being checked
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"Option3"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Option3"
argument_list|,
name|checkbox
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Option3"
argument_list|,
name|checkbox
operator|.
name|getValueAsString
argument_list|()
argument_list|)
expr_stmt|;
comment|// ensure that for both widgets representing the individual checkboxes
comment|// the AS entry has been set
name|assertEquals
argument_list|(
literal|"Off"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Off"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Option3"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Option3"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|setValueForAbstractedCheckBoxGroup
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|checkbox
init|=
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"CheckboxGroup"
argument_list|)
decl_stmt|;
comment|// test a value which sets one of the individual checkboxes within the group
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"Option1"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Option1"
argument_list|,
name|checkbox
operator|.
name|getValueAsString
argument_list|()
argument_list|)
expr_stmt|;
comment|// ensure that for the widgets representing the individual checkboxes
comment|// the AS entry has been set
name|assertEquals
argument_list|(
literal|"Option1"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Off"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Off"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Off"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// test a value which sets two of the individual chekboxes within the group
comment|// as the have the same name entry for being checked
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"Option3"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Option3"
argument_list|,
name|checkbox
operator|.
name|getValueAsString
argument_list|()
argument_list|)
expr_stmt|;
comment|// ensure that for both widgets representing the individual checkboxes
comment|// the AS entry has been set
name|assertEquals
argument_list|(
literal|"Off"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Off"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Option3"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Option3"
argument_list|,
name|checkbox
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getAppearanceState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|setCheckboxInvalidValue
parameter_list|()
throws|throws
name|IOException
block|{
name|PDCheckbox
name|checkbox
init|=
operator|(
name|PDCheckbox
operator|)
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"Checkbox"
argument_list|)
decl_stmt|;
comment|// Set a value which doesn't match the radio button list
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"InvalidValue"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|setCheckboxGroupInvalidValue
parameter_list|()
throws|throws
name|IOException
block|{
name|PDCheckbox
name|checkbox
init|=
operator|(
name|PDCheckbox
operator|)
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"CheckboxGroup"
argument_list|)
decl_stmt|;
comment|// Set a value which doesn't match the radio button list
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"InvalidValue"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|setAbstractedCheckboxInvalidValue
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|checkbox
init|=
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"Checkbox"
argument_list|)
decl_stmt|;
comment|// Set a value which doesn't match the radio button list
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"InvalidValue"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|setAbstractedCheckboxGroupInvalidValue
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|checkbox
init|=
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"CheckboxGroup"
argument_list|)
decl_stmt|;
comment|// Set a value which doesn't match the radio button list
name|checkbox
operator|.
name|setValue
argument_list|(
literal|"InvalidValue"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|retrieveAcrobatRadioButtonProperties
parameter_list|()
throws|throws
name|IOException
block|{
name|PDRadioButton
name|radioButton
init|=
operator|(
name|PDRadioButton
operator|)
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"RadioButtonGroup"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|radioButton
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getOnValues
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|radioButton
operator|.
name|getOnValues
argument_list|()
operator|.
name|contains
argument_list|(
literal|"RadioButton01"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|radioButton
operator|.
name|getOnValues
argument_list|()
operator|.
name|contains
argument_list|(
literal|"RadioButton02"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAcrobatRadioButtonProperties
parameter_list|()
throws|throws
name|IOException
block|{
name|PDRadioButton
name|radioButton
init|=
operator|(
name|PDRadioButton
operator|)
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"RadioButtonGroup"
argument_list|)
decl_stmt|;
comment|// Set value so that first radio button option is selected
name|radioButton
operator|.
name|setValue
argument_list|(
literal|"RadioButton01"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getValue
argument_list|()
argument_list|,
literal|"RadioButton01"
argument_list|)
expr_stmt|;
comment|// First option shall have /RadioButton01, second shall have /Off
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"RadioButton01"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|Off
argument_list|)
expr_stmt|;
comment|// Set value so that second radio button option is selected
name|radioButton
operator|.
name|setValue
argument_list|(
literal|"RadioButton02"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getValue
argument_list|()
argument_list|,
literal|"RadioButton02"
argument_list|)
expr_stmt|;
comment|// First option shall have /Off, second shall have /RadioButton02
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|Off
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"RadioButton02"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|setValueForAbstractedAcrobatRadioButton
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|radioButton
init|=
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"RadioButtonGroup"
argument_list|)
decl_stmt|;
comment|// Set value so that first radio button option is selected
name|radioButton
operator|.
name|setValue
argument_list|(
literal|"RadioButton01"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getValueAsString
argument_list|()
argument_list|,
literal|"RadioButton01"
argument_list|)
expr_stmt|;
comment|// First option shall have /RadioButton01, second shall have /Off
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"RadioButton01"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|Off
argument_list|)
expr_stmt|;
comment|// Set value so that second radio button option is selected
name|radioButton
operator|.
name|setValue
argument_list|(
literal|"RadioButton02"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getValueAsString
argument_list|()
argument_list|,
literal|"RadioButton02"
argument_list|)
expr_stmt|;
comment|// First option shall have /Off, second shall have /RadioButton02
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|Off
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|radioButton
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"RadioButton02"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|setRadioButtonInvalidValue
parameter_list|()
throws|throws
name|IOException
block|{
name|PDRadioButton
name|radioButton
init|=
operator|(
name|PDRadioButton
operator|)
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"RadioButtonGroup"
argument_list|)
decl_stmt|;
comment|// Set a value which doesn't match the radio button list
name|radioButton
operator|.
name|setValue
argument_list|(
literal|"InvalidValue"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|setAbstractedRadioButtonInvalidValue
parameter_list|()
throws|throws
name|IOException
block|{
name|PDField
name|radioButton
init|=
name|acrobatAcroForm
operator|.
name|getField
argument_list|(
literal|"RadioButtonGroup"
argument_list|)
decl_stmt|;
comment|// Set a value which doesn't match the radio button list
name|radioButton
operator|.
name|setValue
argument_list|(
literal|"InvalidValue"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
name|acrobatDocument
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

