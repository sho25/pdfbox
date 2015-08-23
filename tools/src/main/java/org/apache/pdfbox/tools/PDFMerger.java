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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|multipdf
operator|.
name|PDFMergerUtility
import|;
end_import

begin_comment
comment|/**  * This is the main program that will take a list of pdf documents and merge them,  * saving the result in a new document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDFMerger
block|{
specifier|private
name|PDFMerger
parameter_list|()
block|{     }
comment|/**      * Infamous main method.      *      * @param args Command line arguments, should be at least 3.      *      * @throws IOException If there is an error parsing the document.      */
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
name|PDFMerger
name|merge
init|=
operator|new
name|PDFMerger
argument_list|()
decl_stmt|;
name|merge
operator|.
name|merge
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|merge
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|firstFileArgPos
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|-
name|firstFileArgPos
operator|<
literal|3
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|PDFMergerUtility
name|merger
init|=
operator|new
name|PDFMergerUtility
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|firstFileArgPos
init|;
name|i
operator|<
name|args
operator|.
name|length
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|String
name|sourceFileName
init|=
name|args
index|[
name|i
index|]
decl_stmt|;
name|merger
operator|.
name|addSource
argument_list|(
name|sourceFileName
argument_list|)
expr_stmt|;
block|}
name|String
name|destinationFileName
init|=
name|args
index|[
name|args
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|merger
operator|.
name|setDestinationFileName
argument_list|(
name|destinationFileName
argument_list|)
expr_stmt|;
name|merger
operator|.
name|mergeDocuments
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will print the usage requirements and exit.      */
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
literal|"Usage: java -jar pdfbox-app-x.y.z.jar PDFMerger "
operator|+
literal|"<Source PDF File 2..n><Destination PDF File>\n"
operator|+
literal|"<Source PDF File 2..n>       2 or more source PDF documents to merge\n"
operator|+
literal|"<Destination PDF File>       The PDF document to save the merged documents to\n"
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
block|}
end_class

end_unit

