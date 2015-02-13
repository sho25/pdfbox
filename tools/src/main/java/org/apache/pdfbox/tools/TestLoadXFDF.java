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
name|List
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
name|common
operator|.
name|PDTextStream
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
name|FDFAnnotation
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
name|FDFCatalog
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
name|FDFDictionary
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
name|fdf
operator|.
name|FDFField
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
name|PDFieldTreeNode
import|;
end_import

begin_comment
comment|/**  * This example will take a PDF document and fill the fields with data from the  * FDF fields.  *  */
end_comment

begin_class
specifier|public
class|class
name|TestLoadXFDF
block|{
comment|/**      * Creates a new instance of ImportFDF.      */
specifier|public
name|TestLoadXFDF
parameter_list|()
block|{     }
comment|/**      *       * This will import an fdf document and write out another pdf.      *<br />      * see usage() for commandline      *      * @param args command line arguments      *      * @throws Exception If there is an error importing the FDF document.      */
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
name|TestLoadXFDF
name|importer
init|=
operator|new
name|TestLoadXFDF
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
name|Exception
block|{
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
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|fdf
operator|=
name|FDFDocument
operator|.
name|loadXFDF
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|FDFDictionary
name|fdfDictionary
init|=
name|fdf
operator|.
name|getCatalog
argument_list|()
operator|.
name|getFDF
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|fields
init|=
name|fdfDictionary
operator|.
name|getFields
argument_list|()
decl_stmt|;
if|if
condition|(
name|fields
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|field
range|:
name|fields
control|)
block|{
name|FDFField
name|fdfField
init|=
operator|(
name|FDFField
operator|)
name|field
decl_stmt|;
if|if
condition|(
name|fdfField
operator|.
name|getValue
argument_list|()
operator|instanceof
name|PDTextStream
condition|)
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
operator|(
operator|(
name|PDTextStream
operator|)
name|fdfField
operator|.
name|getValue
argument_list|()
operator|)
operator|.
name|getAsString
argument_list|()
argument_list|)
expr_stmt|;
else|else
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|fdfField
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|FDFAnnotation
argument_list|>
name|annotations
init|=
name|fdfDictionary
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
if|if
condition|(
name|annotations
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|FDFAnnotation
name|annotation
range|:
name|annotations
control|)
block|{
if|if
condition|(
name|annotation
operator|!=
literal|null
condition|)
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|annotation
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"usage: org.apache.pdfbox.tools.testLoadXFDF<fdf-file>"
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
block|}
end_class

end_unit

