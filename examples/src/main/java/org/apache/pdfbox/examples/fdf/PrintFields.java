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
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|COSObjectable
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
name|PDNonTerminalField
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
name|PDSignatureField
import|;
end_import

begin_comment
comment|/**  * This example will take a PDF document and print all the fields from the file.  *   * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|PrintFields
block|{
comment|/**      * This will print all the fields from the document.      *       * @param pdfDocument The PDF to get the fields from.      *       * @throws IOException If there is an error getting the fields.      */
specifier|public
name|void
name|printFields
parameter_list|(
name|PDDocument
name|pdfDocument
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
name|List
argument_list|<
name|PDField
argument_list|>
name|fields
init|=
name|acroForm
operator|.
name|getFields
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|PDField
argument_list|>
name|fieldsIter
init|=
name|fields
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|fields
operator|.
name|size
argument_list|()
operator|+
literal|" top-level fields were found on the form"
argument_list|)
expr_stmt|;
while|while
condition|(
name|fieldsIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PDField
name|field
init|=
name|fieldsIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|processField
argument_list|(
name|field
argument_list|,
literal|"|--"
argument_list|,
name|field
operator|.
name|getPartialName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|processField
parameter_list|(
name|PDField
name|field
parameter_list|,
name|String
name|sLevel
parameter_list|,
name|String
name|sParent
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|partialName
init|=
name|field
operator|!=
literal|null
condition|?
name|field
operator|.
name|getPartialName
argument_list|()
else|:
literal|""
decl_stmt|;
name|List
argument_list|<
name|COSObjectable
argument_list|>
name|kids
init|=
name|field
operator|.
name|getKids
argument_list|()
decl_stmt|;
if|if
condition|(
name|kids
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|COSObjectable
argument_list|>
name|kidsIter
init|=
name|kids
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|sParent
operator|.
name|equals
argument_list|(
name|field
operator|.
name|getPartialName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|partialName
operator|!=
literal|null
condition|)
block|{
name|sParent
operator|=
name|sParent
operator|+
literal|"."
operator|+
name|partialName
expr_stmt|;
block|}
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|sLevel
operator|+
name|sParent
argument_list|)
expr_stmt|;
while|while
condition|(
name|kidsIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|pdfObj
init|=
name|kidsIter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|pdfObj
operator|instanceof
name|PDField
condition|)
block|{
name|PDField
name|kid
init|=
operator|(
name|PDField
operator|)
name|pdfObj
decl_stmt|;
name|processField
argument_list|(
name|kid
argument_list|,
literal|"|  "
operator|+
name|sLevel
argument_list|,
name|sParent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|String
name|fieldValue
decl_stmt|;
if|if
condition|(
name|field
operator|instanceof
name|PDSignatureField
condition|)
block|{
comment|// PDSignatureField doesn't have a value
name|fieldValue
operator|=
literal|"PDSignatureField"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|field
operator|instanceof
name|PDNonTerminalField
condition|)
block|{
comment|// Non terminal fields don't have a value
name|fieldValue
operator|=
literal|"node"
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|field
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|fieldValue
operator|=
name|field
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|fieldValue
operator|=
literal|"no value available"
expr_stmt|;
block|}
block|}
name|StringBuilder
name|outputString
init|=
operator|new
name|StringBuilder
argument_list|(
name|sLevel
argument_list|)
decl_stmt|;
name|outputString
operator|.
name|append
argument_list|(
name|sParent
argument_list|)
expr_stmt|;
if|if
condition|(
name|partialName
operator|!=
literal|null
condition|)
block|{
name|outputString
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|partialName
argument_list|)
expr_stmt|;
block|}
name|outputString
operator|.
name|append
argument_list|(
literal|" = "
argument_list|)
operator|.
name|append
argument_list|(
name|fieldValue
argument_list|)
expr_stmt|;
name|outputString
operator|.
name|append
argument_list|(
literal|",  type="
argument_list|)
operator|.
name|append
argument_list|(
name|field
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|outputString
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will read a PDF file and print out the form elements.<br />      * see usage() for commandline      *       * @param args command line arguments      *       * @throws IOException If there is an error importing the FDF document.      */
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
literal|1
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
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|PrintFields
name|exporter
init|=
operator|new
name|PrintFields
argument_list|()
decl_stmt|;
name|exporter
operator|.
name|printFields
argument_list|(
name|pdf
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
literal|"usage: org.apache.pdfbox.examples.fdf.PrintFields<pdf-file>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

