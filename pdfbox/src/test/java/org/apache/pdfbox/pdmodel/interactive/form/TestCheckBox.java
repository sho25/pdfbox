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

begin_comment
comment|/**  * This will test the functionality of Radio Buttons in PDFBox.  */
end_comment

begin_class
specifier|public
class|class
name|TestCheckBox
extends|extends
name|TestCase
block|{
comment|/**      * Constructor.      *      * @param name The name of the test to run.      */
specifier|public
name|TestCheckBox
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
name|TestCheckBox
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
name|TestCheckBox
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
name|testCheckboxPDModel
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
operator|new
name|PDDocument
argument_list|()
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
name|PDCheckbox
name|checkBox
init|=
operator|new
name|PDCheckbox
argument_list|(
name|form
argument_list|)
decl_stmt|;
comment|// test that there are no nulls returned for an empty field
comment|// only specific methods are tested here
name|assertNotNull
argument_list|(
name|checkBox
operator|.
name|getDefaultValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|checkBox
operator|.
name|getOptions
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|checkBox
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test setting/getting option values - the dictionaries Opt entry
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|options
operator|.
name|add
argument_list|(
literal|"Value01"
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
literal|"Value02"
argument_list|)
expr_stmt|;
name|checkBox
operator|.
name|setOptions
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|COSArray
name|optItem
init|=
operator|(
name|COSArray
operator|)
name|checkBox
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
comment|// assert that the values have been correctly set
name|assertNotNull
argument_list|(
name|checkBox
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
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|options
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
comment|// assert that the values can be retrieved correctly
name|List
argument_list|<
name|String
argument_list|>
name|retrievedOptions
init|=
name|checkBox
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
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|retrievedOptions
argument_list|,
name|options
argument_list|)
expr_stmt|;
comment|// assert that the Opt entry is removed
name|checkBox
operator|.
name|setOptions
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|checkBox
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
name|checkBox
operator|.
name|getOptions
argument_list|()
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

