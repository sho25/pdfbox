begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|fdf
operator|.
name|FDFDocument
import|;
end_import

begin_comment
comment|/**  * This example will take a PDF document and fill the fields with data from the  * FDF fields.  *  * @author<a href="ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|ExportXFDF
block|{
comment|/**      * Creates a new instance of ImportFDF.      */
specifier|public
name|ExportXFDF
parameter_list|()
block|{     }
comment|/**      * This will import an fdf document and write out another pdf.      *<br />      * see usage() for commandline      *      * @param args command line arguments      *      * @throws Exception If there is an error importing the FDF document.      */
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
name|Exception
block|{
name|ExportXFDF
name|exporter
init|=
operator|new
name|ExportXFDF
argument_list|()
decl_stmt|;
name|exporter
operator|.
name|exportXFDF
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|exportXFDF
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|PDDocument
name|pdf
init|=
literal|null
decl_stmt|;
name|FDFDocument
name|fdf
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
literal|1
operator|&&
name|args
operator|.
name|length
operator|!=
literal|2
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
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
name|PDAcroForm
name|form
init|=
name|pdf
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
if|if
condition|(
name|form
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: This PDF does not contain a form."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|fdfName
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|fdfName
operator|=
name|args
index|[
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|args
index|[
literal|0
index|]
operator|.
name|length
argument_list|()
operator|>
literal|4
condition|)
block|{
name|fdfName
operator|=
name|args
index|[
literal|0
index|]
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|args
index|[
literal|0
index|]
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
operator|+
literal|".xfdf"
expr_stmt|;
block|}
block|}
name|fdf
operator|=
name|form
operator|.
name|exportFDF
argument_list|()
expr_stmt|;
name|fdf
operator|.
name|saveXFDF
argument_list|(
name|fdfName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|close
argument_list|(
name|fdf
argument_list|)
expr_stmt|;
name|close
argument_list|(
name|pdf
argument_list|)
expr_stmt|;
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
literal|"usage: org.pdfbox.ExortXFDF<pdf-file> [output-xfdf-file]"
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"    [output-xfdf-file] - Default is pdf name, test.pdf->test.xfdf"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Close the document.      *      * @param doc The doc to close.      *      * @throws IOException If there is an error closing the document.      */
specifier|public
name|void
name|close
parameter_list|(
name|FDFDocument
name|doc
parameter_list|)
throws|throws
name|IOException
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
comment|/**      * Close the document.      *      * @param doc The doc to close.      *      * @throws IOException If there is an error closing the document.      */
specifier|public
name|void
name|close
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
throws|throws
name|IOException
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
end_class

end_unit

