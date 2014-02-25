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
name|ant
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
name|tools
operator|.
name|ExtractText
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|DirectoryScanner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|Task
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tools
operator|.
name|ant
operator|.
name|types
operator|.
name|FileSet
import|;
end_import

begin_comment
comment|/**  * This is an ant task that will allow pdf documents to be converted using an  * and task.  *  * @author<a href="ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.8 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFToTextTask
extends|extends
name|Task
block|{
specifier|private
name|List
name|fileSets
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|/**      * Adds a set of files (nested fileset attribute).      *      * @param set Another fileset to add.      */
specifier|public
name|void
name|addFileset
parameter_list|(
name|FileSet
name|set
parameter_list|)
block|{
name|fileSets
operator|.
name|add
argument_list|(
name|set
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will perform the execution.      */
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|log
argument_list|(
literal|"PDFToTextTask executing"
argument_list|)
expr_stmt|;
name|Iterator
name|fileSetIter
init|=
name|fileSets
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|fileSetIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|FileSet
name|next
init|=
operator|(
name|FileSet
operator|)
name|fileSetIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|DirectoryScanner
name|dirScanner
init|=
name|next
operator|.
name|getDirectoryScanner
argument_list|(
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
name|dirScanner
operator|.
name|scan
argument_list|()
expr_stmt|;
name|String
index|[]
name|files
init|=
name|dirScanner
operator|.
name|getIncludedFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|dirScanner
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|files
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|log
argument_list|(
literal|"processing: "
operator|+
name|f
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|pdfFile
init|=
name|f
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|pdfFile
operator|.
name|toUpperCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".PDF"
argument_list|)
condition|)
block|{
name|String
name|textFile
init|=
name|pdfFile
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pdfFile
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
decl_stmt|;
name|textFile
operator|=
name|textFile
operator|+
literal|"txt"
expr_stmt|;
try|try
block|{
name|ExtractText
operator|.
name|main
argument_list|(
operator|new
name|String
index|[]
block|{
name|pdfFile
block|,
name|textFile
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
argument_list|(
literal|"Error processing "
operator|+
name|pdfFile
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

