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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|exceptions
operator|.
name|InvalidPasswordException
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
name|util
operator|.
name|PDFTextStripper
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
name|util
operator|.
name|TextPosition
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

begin_comment
comment|/**  * This is an example on how to get some x/y coordinates of text.  *  * Usage: java org.apache.pdfbox.examples.util.PrintTextLocations&lt;input-pdf&gt;  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.7 $  */
end_comment

begin_class
specifier|public
class|class
name|PrintTextLocations
extends|extends
name|PDFTextStripper
block|{
comment|/**      * Default constructor.      *      * @throws IOException If there is an error loading text stripper properties.      */
specifier|public
name|PrintTextLocations
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|setSortByPosition
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
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
name|load
argument_list|(
name|args
index|[
literal|0
index|]
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
try|try
block|{
name|document
operator|.
name|decrypt
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPasswordException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: Document is encrypted with a password."
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
name|PrintTextLocations
name|printer
init|=
operator|new
name|PrintTextLocations
argument_list|()
decl_stmt|;
name|List
name|allPages
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
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
name|allPages
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PDPage
name|page
init|=
operator|(
name|PDPage
operator|)
name|allPages
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Processing page: "
operator|+
name|i
argument_list|)
expr_stmt|;
name|PDStream
name|contents
init|=
name|page
operator|.
name|getContents
argument_list|()
decl_stmt|;
if|if
condition|(
name|contents
operator|!=
literal|null
condition|)
block|{
name|printer
operator|.
name|processStream
argument_list|(
name|page
argument_list|,
name|page
operator|.
name|findResources
argument_list|()
argument_list|,
name|page
operator|.
name|getContents
argument_list|()
operator|.
name|getStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|/**      * A method provided as an event interface to allow a subclass to perform      * some specific functionality when text needs to be processed      *      * @param text The text to be processed      */
specifier|protected
name|void
name|processTextPosition
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"String["
operator|+
name|text
operator|.
name|getXDirAdj
argument_list|()
operator|+
literal|","
operator|+
name|text
operator|.
name|getYDirAdj
argument_list|()
operator|+
literal|" fs="
operator|+
name|text
operator|.
name|getFontSize
argument_list|()
operator|+
literal|" xscale="
operator|+
name|text
operator|.
name|getXScale
argument_list|()
operator|+
literal|" height="
operator|+
name|text
operator|.
name|getHeightDir
argument_list|()
operator|+
literal|" space="
operator|+
name|text
operator|.
name|getWidthOfSpace
argument_list|()
operator|+
literal|" width="
operator|+
name|text
operator|.
name|getWidthDirAdj
argument_list|()
operator|+
literal|"]"
operator|+
name|text
operator|.
name|getCharacter
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Usage: java org.apache.pdfbox.examples.pdmodel.PrintTextLocations<input-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

