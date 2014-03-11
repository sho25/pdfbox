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
name|fdf
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
name|security
operator|.
name|NoSuchAlgorithmException
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
name|exceptions
operator|.
name|CryptographyException
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
name|exceptions
operator|.
name|SignatureException
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
name|PDDocumentCatalog
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
comment|/**  * This example will take a PDF document and set a FDF field in it.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  *   */
end_comment

begin_class
specifier|public
class|class
name|SetField
block|{
comment|/**      * This will set a single field in the document.      *       * @param pdfDocument The PDF to set the field in.      * @param name The name of the field to set.      * @param value The new value of the field.      *       * @throws IOException If there is an error setting the field.      */
specifier|public
name|void
name|setField
parameter_list|(
name|PDDocument
name|pdfDocument
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocumentCatalog
name|docCatalog
init|=
name|pdfDocument
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDAcroForm
name|acroForm
init|=
name|docCatalog
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|field
operator|!=
literal|null
condition|)
block|{
name|field
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"No field found with name:"
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will read a PDF file and set a field and then write it the pdf out again.<br />      * see usage() for commandline      *       * @param args command line arguments      *       * @throws IOException If there is an error importing the FDF document.      */
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
throws|,
name|CryptographyException
throws|,
name|SignatureException
throws|,
name|NoSuchAlgorithmException
block|{
name|SetField
name|setter
init|=
operator|new
name|SetField
argument_list|()
decl_stmt|;
name|setter
operator|.
name|setField
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setField
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
throws|,
name|CryptographyException
throws|,
name|SignatureException
throws|,
name|NoSuchAlgorithmException
block|{
name|PDDocument
name|pdf
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|3
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|SetField
name|example
init|=
operator|new
name|SetField
argument_list|()
decl_stmt|;
name|pdf
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|example
operator|.
name|setField
argument_list|(
name|pdf
argument_list|,
name|args
index|[
literal|1
index|]
argument_list|,
name|args
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|save
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|pdf
operator|!=
literal|null
condition|)
block|{
name|pdf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will print out a message telling how to use this example.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: org.apache.pdfbox.examples.fdf.SetField<pdf-file><field-name><field-value>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

