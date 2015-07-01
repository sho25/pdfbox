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
name|COSString
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
comment|/**  * This will test the form fields in PDFBox.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|TestFields
extends|extends
name|TestCase
block|{
comment|//private static Logger log = Logger.getLogger(TestFDF.class);
specifier|private
specifier|static
specifier|final
name|String
name|PATH_OF_PDF
init|=
literal|"src/test/resources/org/apache/pdfbox/pdmodel/interactive/form/AcroFormsBasicFields.pdf"
decl_stmt|;
comment|/**      * Constructor.      *      * @param name The name of the test to run.      */
specifier|public
name|TestFields
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
name|TestFields
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
name|TestFields
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
comment|/**      * This will test setting field flags on the PDField.      *      * @throws IOException If there is an error creating the field.      */
specifier|public
name|void
name|testFlags
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
name|PDTextField
name|textBox
init|=
operator|new
name|PDTextField
argument_list|(
name|form
argument_list|)
decl_stmt|;
comment|//assert that default is false.
name|assertFalse
argument_list|(
name|textBox
operator|.
name|isComb
argument_list|()
argument_list|)
expr_stmt|;
comment|//try setting and clearing a single field
name|textBox
operator|.
name|setComb
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|textBox
operator|.
name|isComb
argument_list|()
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|setComb
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|textBox
operator|.
name|isComb
argument_list|()
argument_list|)
expr_stmt|;
comment|//try setting and clearing multiple fields
name|textBox
operator|.
name|setComb
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|setDoNotScroll
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|textBox
operator|.
name|isComb
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|textBox
operator|.
name|doNotScroll
argument_list|()
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|setComb
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|setDoNotScroll
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|textBox
operator|.
name|isComb
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|textBox
operator|.
name|doNotScroll
argument_list|()
argument_list|)
expr_stmt|;
comment|//assert that setting a field to false multiple times works
name|textBox
operator|.
name|setComb
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|textBox
operator|.
name|isComb
argument_list|()
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|setComb
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|textBox
operator|.
name|isComb
argument_list|()
argument_list|)
expr_stmt|;
comment|//assert that setting a field to true multiple times works
name|textBox
operator|.
name|setComb
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|textBox
operator|.
name|isComb
argument_list|()
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|setComb
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|textBox
operator|.
name|isComb
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
comment|/**      * This will test some form fields functionality based with       * a sample form.      *      * @throws IOException If there is an error creating the field.      */
specifier|public
name|void
name|testAcroFormsBasicFields
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
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|PATH_OF_PDF
argument_list|)
argument_list|)
expr_stmt|;
comment|// get and assert that there is a form
name|PDAcroForm
name|form
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|form
argument_list|)
expr_stmt|;
comment|// assert that there is no value, set the field value and
comment|// ensure it has been set
name|PDTextField
name|textField
init|=
operator|(
name|PDTextField
operator|)
name|form
operator|.
name|getField
argument_list|(
literal|"TextField"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|textField
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
argument_list|)
expr_stmt|;
name|textField
operator|.
name|setValue
argument_list|(
literal|"field value"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|textField
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
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getValue
argument_list|()
argument_list|,
literal|"field value"
argument_list|)
expr_stmt|;
comment|// assert when setting to null the key has also been removed
name|assertNotNull
argument_list|(
name|textField
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
argument_list|)
expr_stmt|;
name|textField
operator|.
name|setValue
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|textField
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
argument_list|)
expr_stmt|;
comment|// get the TextField with a DV entry
name|textField
operator|=
operator|(
name|PDTextField
operator|)
name|form
operator|.
name|getField
argument_list|(
literal|"TextField-DefaultValue"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|textField
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getDefaultValue
argument_list|()
argument_list|,
literal|"DefaultValue"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getDefaultValue
argument_list|()
argument_list|,
operator|(
operator|(
name|COSString
operator|)
name|textField
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DV
argument_list|)
operator|)
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getDefaultAppearance
argument_list|()
argument_list|,
literal|"/Helv 12 Tf 0 g"
argument_list|)
expr_stmt|;
comment|// get a rich text field with a  DV entry
name|textField
operator|=
operator|(
name|PDTextField
operator|)
name|form
operator|.
name|getField
argument_list|(
literal|"RichTextField-DefaultValue"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|textField
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getDefaultValue
argument_list|()
argument_list|,
literal|"DefaultValue"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getDefaultValue
argument_list|()
argument_list|,
operator|(
operator|(
name|COSString
operator|)
name|textField
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DV
argument_list|)
operator|)
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getValue
argument_list|()
argument_list|,
literal|"DefaultValue"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getDefaultAppearance
argument_list|()
argument_list|,
literal|"/Helv 12 Tf 0 g"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getDefaultStyleString
argument_list|()
argument_list|,
literal|"font: Helvetica,sans-serif 12.0pt; text-align:left; color:#000000 "
argument_list|)
expr_stmt|;
comment|// do not test for the full content as this is a rather long xml string
name|assertEquals
argument_list|(
name|textField
operator|.
name|getRichTextValue
argument_list|()
operator|.
name|length
argument_list|()
argument_list|,
literal|338
argument_list|)
expr_stmt|;
comment|// get a rich text field with a text stream for the value
name|textField
operator|=
operator|(
name|PDTextField
operator|)
name|form
operator|.
name|getField
argument_list|(
literal|"LongRichTextField"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|textField
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|"org.apache.pdfbox.cos.COSStream"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|textField
operator|.
name|getValue
argument_list|()
operator|.
name|length
argument_list|()
argument_list|,
literal|145396
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

