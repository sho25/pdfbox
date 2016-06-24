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
name|interactive
operator|.
name|action
operator|.
name|PDAction
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
name|action
operator|.
name|PDActionURI
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
name|annotation
operator|.
name|PDAnnotation
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
name|annotation
operator|.
name|PDAnnotationLink
import|;
end_import

begin_comment
comment|/**  * This is an example of how to replace a URL in a PDF document.  This  * will only replace the URL that the text refers to and not the text  * itself.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ReplaceURLs
block|{
comment|/**      * Constructor.      */
specifier|private
name|ReplaceURLs
parameter_list|()
block|{
comment|//utility class
block|}
comment|/**      * This will read in a document and replace all of the urls with      * http://pdfbox.apache.org.      *<br>      * see usage() for commandline      *      * @param args Command line arguments.      *      * @throws IOException If there is an error during the process.      */
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
name|doc
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
literal|2
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|doc
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
name|int
name|pageNum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|PDPage
name|page
range|:
name|doc
operator|.
name|getPages
argument_list|()
control|)
block|{
name|pageNum
operator|++
expr_stmt|;
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|annotations
init|=
name|page
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
for|for
control|(
name|PDAnnotation
name|annotation
range|:
name|annotations
control|)
block|{
name|PDAnnotation
name|annot
init|=
name|annotation
decl_stmt|;
if|if
condition|(
name|annot
operator|instanceof
name|PDAnnotationLink
condition|)
block|{
name|PDAnnotationLink
name|link
init|=
operator|(
name|PDAnnotationLink
operator|)
name|annot
decl_stmt|;
name|PDAction
name|action
init|=
name|link
operator|.
name|getAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|action
operator|instanceof
name|PDActionURI
condition|)
block|{
name|PDActionURI
name|uri
init|=
operator|(
name|PDActionURI
operator|)
name|action
decl_stmt|;
name|String
name|oldURI
init|=
name|uri
operator|.
name|getURI
argument_list|()
decl_stmt|;
name|String
name|newURI
init|=
literal|"http://pdfbox.apache.org"
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Page "
operator|+
name|pageNum
operator|+
literal|": Replacing "
operator|+
name|oldURI
operator|+
literal|" with "
operator|+
name|newURI
argument_list|)
expr_stmt|;
name|uri
operator|.
name|setURI
argument_list|(
name|newURI
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|doc
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
block|}
finally|finally
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
literal|"usage: "
operator|+
name|ReplaceURLs
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<input-file><output-file>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

