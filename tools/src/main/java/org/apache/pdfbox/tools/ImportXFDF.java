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
name|tools
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdfparser
operator|.
name|FDFParser
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
name|pdfparser
operator|.
name|PDFParser
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
name|fdf
operator|.
name|FDFDocument
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

begin_comment
comment|/**  * This example will take a PDF document and fill the fields with data from the  * XFDF fields.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|ImportXFDF
block|{
comment|/**      * Creates a new instance of ImportFDF.      */
specifier|public
name|ImportXFDF
parameter_list|()
block|{     }
comment|/**      * This will takes the values from the fdf document and import them into the      * PDF document.      *      * @param pdfDocument The document to put the fdf data into.      * @param fdfDocument The FDF document to get the data from.      *      * @throws IOException If there is an error setting the data in the field.      */
specifier|public
name|void
name|importFDF
parameter_list|(
name|PDDocument
name|pdfDocument
parameter_list|,
name|FDFDocument
name|fdfDocument
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
name|acroForm
operator|.
name|setCacheFields
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|acroForm
operator|.
name|importFDF
argument_list|(
name|fdfDocument
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will import an fdf document and write out another pdf.      *<br>      * see usage() for commandline      *      * @param args command line arguments      *      * @throws IOException If there is an error importing the FDF document.      */
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
comment|// suppress the Dock icon on OS X
name|System
operator|.
name|setProperty
argument_list|(
literal|"apple.awt.UIElement"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|ImportXFDF
name|importer
init|=
operator|new
name|ImportXFDF
argument_list|()
decl_stmt|;
name|importer
operator|.
name|importXFDF
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|importXFDF
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
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
name|ImportFDF
name|importer
init|=
operator|new
name|ImportFDF
argument_list|()
decl_stmt|;
try|try
init|(
name|PDDocument
name|pdf
init|=
name|PDFParser
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|)
init|;                     FDFDocument fdf = FDFParser.loadXFDF(args[1])
block|)
block|{
name|importer
operator|.
name|importFDF
argument_list|(
name|pdf
argument_list|,
name|fdf
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|save
argument_list|(
name|args
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

begin_comment
comment|/**      * This will print out a message telling how to use this example.      */
end_comment

begin_function
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
literal|"usage: org.apache.pdfbox.tools.ImportXFDF<pdf-file><fdf-file><output-file>"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

