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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|pdfparser
operator|.
name|PDFStreamParser
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
name|pdfwriter
operator|.
name|ContentStreamWriter
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
name|common
operator|.
name|PDStream
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
name|contentstream
operator|.
name|operator
operator|.
name|Operator
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

begin_comment
comment|/**  * This is an example on how to remove all text from PDF document.  *  * Usage: java org.apache.pdfbox.examples.util.RemoveAllText&lt;input-pdf&gt;&lt;output-pdf&gt;  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|RemoveAllText
block|{
comment|/**      * Default constructor.      */
specifier|private
name|RemoveAllText
parameter_list|()
block|{
comment|//example class should not be instantiated
block|}
comment|/**      * This will remove all text from a PDF document.      *      * @param args The command line arguments.      *      * @throws IOException If there is an error parsing the document.      */
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
if|if
condition|(
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
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|document
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
if|if
condition|(
name|document
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: Encrypted documents are not supported for this example."
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
for|for
control|(
name|PDPage
name|page
range|:
name|document
operator|.
name|getPages
argument_list|()
control|)
block|{
name|PDFStreamParser
name|parser
init|=
operator|new
name|PDFStreamParser
argument_list|(
name|page
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|tokens
init|=
name|parser
operator|.
name|getTokens
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|newTokens
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|token
range|:
name|tokens
control|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|Operator
condition|)
block|{
name|Operator
name|op
init|=
operator|(
name|Operator
operator|)
name|token
decl_stmt|;
if|if
condition|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"TJ"
argument_list|)
operator|||
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Tj"
argument_list|)
condition|)
block|{
comment|//remove the one argument to this operator
name|newTokens
operator|.
name|remove
argument_list|(
name|newTokens
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
name|newTokens
operator|.
name|add
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
name|PDStream
name|newContents
init|=
operator|new
name|PDStream
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|OutputStream
name|out
init|=
name|newContents
operator|.
name|createOutputStream
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
decl_stmt|;
name|ContentStreamWriter
name|writer
init|=
operator|new
name|ContentStreamWriter
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|writer
operator|.
name|writeTokens
argument_list|(
name|newTokens
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|page
operator|.
name|setContents
argument_list|(
name|newContents
argument_list|)
expr_stmt|;
block|}
name|document
operator|.
name|save
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will print the usage for this document.      */
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
literal|"Usage: java org.apache.pdfbox.examples.pdmodel.RemoveAllText<input-pdf><output-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

