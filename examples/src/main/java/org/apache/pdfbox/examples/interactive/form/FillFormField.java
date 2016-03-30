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
comment|/**  * Example to show filling form fields.  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|FillFormField
block|{
specifier|private
name|FillFormField
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
name|String
name|formTemplate
init|=
literal|"src/main/resources/org/apache/pdfbox/examples/interactive/form/FillFormField.pdf"
decl_stmt|;
comment|// load the document
name|PDDocument
name|pdfDocument
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|formTemplate
argument_list|)
argument_list|)
decl_stmt|;
comment|// get the document catalog
name|PDAcroForm
name|acroForm
init|=
name|pdfDocument
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
comment|// as there might not be an AcroForm entry a null check is necessary
if|if
condition|(
name|acroForm
operator|!=
literal|null
condition|)
block|{
comment|// Retrieve an individual field and set its value.
name|PDTextField
name|field
init|=
operator|(
name|PDTextField
operator|)
name|acroForm
operator|.
name|getField
argument_list|(
literal|"sampleField"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"Text Entry"
argument_list|)
expr_stmt|;
comment|// If a field is nested within the form tree a fully qualified name
comment|// might be provided to access the field.
name|field
operator|=
operator|(
name|PDTextField
operator|)
name|acroForm
operator|.
name|getField
argument_list|(
literal|"fieldsContainer.nestedSampleField"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"Text Entry"
argument_list|)
expr_stmt|;
block|}
comment|// Save and close the filled out form.
name|pdfDocument
operator|.
name|save
argument_list|(
literal|"target/FillFormField.pdf"
argument_list|)
expr_stmt|;
name|pdfDocument
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

