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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|junit
operator|.
name|framework
operator|.
name|Test
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
name|junit
operator|.
name|framework
operator|.
name|TestSuite
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationWidget
import|;
end_import

begin_comment
comment|/**  * This will test the functionality of choice fields in PDFBox.  */
end_comment

begin_class
specifier|public
class|class
name|TestListBox
extends|extends
name|TestCase
block|{
comment|/**      * Constructor.      *      * @param name The name of the test to run.      */
specifier|public
name|TestListBox
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the suite of test that this class holds.      *      * @return All of the tests that this class holds.      */
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
return|return
operator|new
name|TestSuite
argument_list|(
name|TestListBox
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * infamous main method.      *      * @param args The command line arguments.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|String
index|[]
name|arg
init|=
block|{
name|TestListBox
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
name|junit
operator|.
name|textui
operator|.
name|TestRunner
operator|.
name|main
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will test the radio button PDModel.      *      * @throws IOException If there is an error creating the field.      */
specifier|public
name|void
name|testChoicePDModel
parameter_list|()
throws|throws
name|IOException
block|{
comment|/*          * Set up two data list which will be used for the tests          */
comment|// export values
name|List
argument_list|<
name|String
argument_list|>
name|exportValues
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|exportValues
operator|.
name|add
argument_list|(
literal|"export01"
argument_list|)
expr_stmt|;
name|exportValues
operator|.
name|add
argument_list|(
literal|"export02"
argument_list|)
expr_stmt|;
name|exportValues
operator|.
name|add
argument_list|(
literal|"export03"
argument_list|)
expr_stmt|;
comment|// display values, not sorted on purpose as this
comment|// will be used to test the sort option of the list box
name|List
argument_list|<
name|String
argument_list|>
name|displayValues
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|displayValues
operator|.
name|add
argument_list|(
literal|"display02"
argument_list|)
expr_stmt|;
name|displayValues
operator|.
name|add
argument_list|(
literal|"display01"
argument_list|)
expr_stmt|;
name|displayValues
operator|.
name|add
argument_list|(
literal|"display03"
argument_list|)
expr_stmt|;
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
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
decl_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDAcroForm
name|form
init|=
operator|new
name|PDAcroForm
argument_list|(
name|doc
argument_list|)
decl_stmt|;
comment|// Adobe Acrobat uses Helvetica as a default font and
comment|// stores that under the name '/Helv' in the resources dictionary
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA
decl_stmt|;
name|PDResources
name|resources
init|=
operator|new
name|PDResources
argument_list|()
decl_stmt|;
name|resources
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Helv"
argument_list|)
argument_list|,
name|font
argument_list|)
expr_stmt|;
comment|// Add and set the resources and default appearance at the form level
name|form
operator|.
name|setDefaultResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
comment|// Acrobat sets the font size on the form level to be
comment|// auto sized as default. This is done by setting the font size to '0'
name|String
name|defaultAppearanceString
init|=
literal|"/Helv 0 Tf 0 g"
decl_stmt|;
name|form
operator|.
name|setDefaultAppearance
argument_list|(
name|defaultAppearanceString
argument_list|)
expr_stmt|;
name|PDChoice
name|choice
init|=
operator|new
name|PDListBox
argument_list|(
name|form
argument_list|)
decl_stmt|;
name|choice
operator|.
name|setDefaultAppearance
argument_list|(
literal|"/Helv 12 Tf 0g"
argument_list|)
expr_stmt|;
comment|// Specify the annotation associated with the field
name|PDAnnotationWidget
name|widget
init|=
name|choice
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
literal|750
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
comment|// Add the annotation to the page
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
comment|// test that there are no nulls returned for an empty field
comment|// only specific methods are tested here
name|assertNotNull
argument_list|(
name|choice
operator|.
name|getOptions
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|choice
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
comment|/*              * Tests for setting the export values              */
comment|// setting/getting option values - the dictionaries Opt entry
name|choice
operator|.
name|setOptions
argument_list|(
name|exportValues
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exportValues
argument_list|,
name|choice
operator|.
name|getOptionsDisplayValues
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exportValues
argument_list|,
name|choice
operator|.
name|getOptionsExportValues
argument_list|()
argument_list|)
expr_stmt|;
comment|// assert that the option values have been correctly set
name|COSArray
name|optItem
init|=
operator|(
name|COSArray
operator|)
name|choice
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|choice
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|optItem
operator|.
name|size
argument_list|()
argument_list|,
name|exportValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exportValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|optItem
operator|.
name|getString
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert that the option values can be retrieved correctly
name|List
argument_list|<
name|String
argument_list|>
name|retrievedOptions
init|=
name|choice
operator|.
name|getOptions
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|retrievedOptions
operator|.
name|size
argument_list|()
argument_list|,
name|exportValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|retrievedOptions
argument_list|,
name|exportValues
argument_list|)
expr_stmt|;
comment|/*              * Tests for setting the field values              */
comment|// assert that the field value can be set
name|choice
operator|.
name|setValue
argument_list|(
literal|"export01"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|choice
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"export01"
argument_list|)
expr_stmt|;
comment|// ensure that the choice field doesn't allow multiple selections
name|choice
operator|.
name|setMultiSelect
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// without multiselect setting multiple items shall fail
try|try
block|{
name|choice
operator|.
name|setValue
argument_list|(
name|exportValues
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Missing IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"The list box does not allow multiple selections."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// ensure that the choice field does allow multiple selections
name|choice
operator|.
name|setMultiSelect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// now this call must suceed
name|choice
operator|.
name|setValue
argument_list|(
name|exportValues
argument_list|)
expr_stmt|;
comment|// assert that the option values have been correctly set
name|COSArray
name|valueItems
init|=
operator|(
name|COSArray
operator|)
name|choice
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|valueItems
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|valueItems
operator|.
name|size
argument_list|()
argument_list|,
name|exportValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exportValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|valueItems
operator|.
name|getString
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// assert that the index values have been correctly set
name|COSArray
name|indexItems
init|=
operator|(
name|COSArray
operator|)
name|choice
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|I
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|indexItems
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|indexItems
operator|.
name|size
argument_list|()
argument_list|,
name|exportValues
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// setting a single value shall remove the indices
name|choice
operator|.
name|setValue
argument_list|(
literal|"export01"
argument_list|)
expr_stmt|;
name|indexItems
operator|=
operator|(
name|COSArray
operator|)
name|choice
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|I
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|indexItems
argument_list|)
expr_stmt|;
comment|// assert that the Opt entry is removed
name|choice
operator|.
name|setOptions
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|choice
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
argument_list|)
expr_stmt|;
comment|// if there is no Opt entry an empty List shall be returned
name|assertEquals
argument_list|(
name|choice
operator|.
name|getOptions
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
comment|/*              * Test for setting export and display values              */
comment|// setting display and export value
name|choice
operator|.
name|setOptions
argument_list|(
name|exportValues
argument_list|,
name|displayValues
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|displayValues
argument_list|,
name|choice
operator|.
name|getOptionsDisplayValues
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|exportValues
argument_list|,
name|choice
operator|.
name|getOptionsExportValues
argument_list|()
argument_list|)
expr_stmt|;
comment|/*              * Testing the sort option              */
name|assertEquals
argument_list|(
name|choice
operator|.
name|getOptionsDisplayValues
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"display02"
argument_list|)
expr_stmt|;
name|choice
operator|.
name|setSort
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|choice
operator|.
name|setOptions
argument_list|(
name|exportValues
argument_list|,
name|displayValues
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|choice
operator|.
name|getOptionsDisplayValues
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|"display01"
argument_list|)
expr_stmt|;
comment|/*              * Setting options with an empty list              */
comment|// assert that the Opt entry is removed
name|choice
operator|.
name|setOptions
argument_list|(
literal|null
argument_list|,
name|displayValues
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|choice
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
argument_list|)
expr_stmt|;
comment|// if there is no Opt entry an empty list shall be returned
name|assertEquals
argument_list|(
name|choice
operator|.
name|getOptions
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|choice
operator|.
name|getOptionsDisplayValues
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|choice
operator|.
name|getOptionsExportValues
argument_list|()
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
comment|// test that an IllegalArgumentException is thrown when export and display
comment|// value lists have different sizes
name|exportValues
operator|.
name|remove
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|choice
operator|.
name|setOptions
argument_list|(
name|exportValues
argument_list|,
name|displayValues
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Missing exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"The number of entries for exportValue and displayValue shall be the same."
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

