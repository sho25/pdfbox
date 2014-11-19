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
name|pdmodel
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
name|PDDocumentInformation
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
name|PDMetadata
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
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_comment
comment|/**  * This is an example on how to get a documents metadata information.  *  * Usage: java org.apache.pdfbox.examples.pdmodel.PrintDocumentMetaData&lt;input-pdf&gt;  *  * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|PrintDocumentMetaData
block|{
comment|/**      * This will print the documents data.      *      * @param args The command line arguments.      *      * @throws Exception If there is an error parsing the document.      */
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
name|loadNonSeq
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
name|PrintDocumentMetaData
name|meta
init|=
operator|new
name|PrintDocumentMetaData
argument_list|()
decl_stmt|;
name|meta
operator|.
name|printMetadata
argument_list|(
name|document
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
literal|"Usage: java org.apache.pdfbox.examples.pdmodel.PrintDocumentMetaData<input-pdf>"
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will print the documents data to System.out.      *      * @param document The document to get the metadata from.      *      * @throws IOException If there is an error getting the page count.      */
specifier|public
name|void
name|printMetadata
parameter_list|(
name|PDDocument
name|document
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocumentInformation
name|info
init|=
name|document
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
name|PDDocumentCatalog
name|cat
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDMetadata
name|metadata
init|=
name|cat
operator|.
name|getMetadata
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Page Count="
operator|+
name|document
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Title="
operator|+
name|info
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Author="
operator|+
name|info
operator|.
name|getAuthor
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Subject="
operator|+
name|info
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Keywords="
operator|+
name|info
operator|.
name|getKeywords
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Creator="
operator|+
name|info
operator|.
name|getCreator
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Producer="
operator|+
name|info
operator|.
name|getProducer
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Creation Date="
operator|+
name|formatDate
argument_list|(
name|info
operator|.
name|getCreationDate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Modification Date="
operator|+
name|formatDate
argument_list|(
name|info
operator|.
name|getModificationDate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Trapped="
operator|+
name|info
operator|.
name|getTrapped
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|metadata
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Metadata="
operator|+
name|metadata
operator|.
name|getInputStreamAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will format a date object.      *      * @param date The date to format.      *      * @return A string representation of the date.      */
specifier|private
name|String
name|formatDate
parameter_list|(
name|Calendar
name|date
parameter_list|)
block|{
name|String
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|date
operator|!=
literal|null
condition|)
block|{
name|SimpleDateFormat
name|formatter
init|=
operator|new
name|SimpleDateFormat
argument_list|()
decl_stmt|;
name|retval
operator|=
name|formatter
operator|.
name|format
argument_list|(
name|date
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
block|}
end_class

end_unit

