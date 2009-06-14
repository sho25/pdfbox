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
name|searchengine
operator|.
name|lucene
package|;
end_package

begin_comment
comment|/*  * This source was originally written as an example for the lucene project.  * It has been modified to use PDFBox as a  lucene document creator.  * -Ben Litchfield  *  *====================================================================  * The Apache Software License, Version 1.1  *  * Copyright (c) 2001 The Apache Software Foundation.  All rights  * reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions  * are met:  *  * 1. Redistributions of source code must retain the above copyright  *    notice, this list of conditions and the following disclaimer.  *  * 2. Redistributions in binary form must reproduce the above copyright  *    notice, this list of conditions and the following disclaimer in  *    the documentation and/or other materials provided with the  *    distribution.  *  * 3. The end-user documentation included with the redistribution,  *    if any, must include the following acknowledgment:  *       "This product includes software developed by the  *        Apache Software Foundation (http://www.apache.org/)."  *    Alternately, this acknowledgment may appear in the software itself,  *    if and wherever such third-party acknowledgments normally appear.  *  * 4. The names "Apache" and "Apache Software Foundation" and  *    "Apache Lucene" must not be used to endorse or promote products  *    derived from this software without prior written permission. For  *    written permission, please contact apache@apache.org.  *  * 5. Products derived from this software may not be called "Apache",  *    "Apache Lucene", nor may "Apache" appear in their name, without  *    prior written permission of the Apache Software Foundation.  *  * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED  * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES  * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR  * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,  * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT  * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF  * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND  * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT  * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF  * SUCH DAMAGE.  * ====================================================================  *  * This software consists of voluntary contributions made by many  * individuals on behalf of the Apache Software Foundation.  For more  * information on the Apache Software Foundation, please see  *<http://www.apache.org/>.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|standard
operator|.
name|StandardAnalyzer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|demo
operator|.
name|HTMLDocument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|Term
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|TermEnum
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Date
import|;
end_import

begin_comment
comment|/**  * This is a class that will index some files on a local filesystem.  This code  * was modified from a demo that comes with the lucene search engine.  *  * @author Lucene team  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  *  * @version $Revision: 1.8 $  */
end_comment

begin_class
specifier|public
class|class
name|IndexFiles
block|{
specifier|private
name|boolean
name|deleting
init|=
literal|false
decl_stmt|;
comment|// true during deletion pass
specifier|private
name|IndexReader
name|reader
decl_stmt|;
comment|// existing index
specifier|private
name|IndexWriter
name|writer
decl_stmt|;
comment|// new index being built
specifier|private
name|TermEnum
name|uidIter
decl_stmt|;
comment|// document id iterator
comment|/**      * This is the main entry point for the indexer.      *      * @param argv The command line arguments.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|argv
parameter_list|)
block|{
name|String
name|index
init|=
literal|"index"
decl_stmt|;
name|boolean
name|create
init|=
literal|false
decl_stmt|;
name|File
name|root
init|=
literal|null
decl_stmt|;
name|String
name|usage
init|=
literal|"org.apache.pdfbox.searchengine.lucene.IndexFiles [-create] [-index<index>]<root_directory>"
decl_stmt|;
if|if
condition|(
name|argv
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Usage: "
operator|+
name|usage
argument_list|)
expr_stmt|;
return|return;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|argv
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|argv
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-index"
argument_list|)
condition|)
block|{
comment|// parse -index option
name|index
operator|=
name|argv
index|[
operator|++
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|argv
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
literal|"-create"
argument_list|)
condition|)
block|{
comment|// parse -create option
name|create
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|!=
name|argv
operator|.
name|length
operator|-
literal|1
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Usage: "
operator|+
name|usage
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"root="
operator|+
name|argv
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|root
operator|=
operator|new
name|File
argument_list|(
name|argv
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|IndexFiles
name|indexer
init|=
operator|new
name|IndexFiles
argument_list|()
decl_stmt|;
name|indexer
operator|.
name|index
argument_list|(
name|root
argument_list|,
name|create
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will index a directory.      *      * @param root The root directory to start indexing.      * @param create Should we create a new index?      * @param index The name of the index.      */
specifier|public
name|void
name|index
parameter_list|(
name|File
name|root
parameter_list|,
name|boolean
name|create
parameter_list|,
name|String
name|index
parameter_list|)
block|{
try|try
block|{
name|Date
name|start
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|writer
operator|=
operator|new
name|IndexWriter
argument_list|(
name|index
argument_list|,
operator|new
name|StandardAnalyzer
argument_list|()
argument_list|,
name|create
argument_list|,
name|IndexWriter
operator|.
name|MaxFieldLength
operator|.
name|LIMITED
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|create
condition|)
block|{
comment|// delete stale docs
name|deleting
operator|=
literal|true
expr_stmt|;
name|indexDocs
argument_list|(
name|root
argument_list|,
name|index
argument_list|,
name|create
argument_list|)
expr_stmt|;
block|}
name|indexDocs
argument_list|(
name|root
argument_list|,
name|index
argument_list|,
name|create
argument_list|)
expr_stmt|;
comment|// add new docs
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Optimizing index..."
argument_list|)
expr_stmt|;
name|writer
operator|.
name|optimize
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|Date
name|end
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|end
operator|.
name|getTime
argument_list|()
operator|-
name|start
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" total milliseconds"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Walk directory hierarchy in uid order, while keeping uid iterator from      * existing index in sync.  Mismatches indicate one of: (a) old documents to      * be deleted; (b) unchanged documents, to be left alone; or (c) new      * documents, to be indexed.      *      * @param file The directory to index.      * @param index The index to add the file to.      * @param create A flag telling if we should create the index.      *      * @throws Exception If there is any error indexing the directory.      */
specifier|private
name|void
name|indexDocs
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|index
parameter_list|,
name|boolean
name|create
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|create
condition|)
block|{
comment|// incrementally update
name|reader
operator|=
name|IndexReader
operator|.
name|open
argument_list|(
name|index
argument_list|)
expr_stmt|;
comment|// open existing index
name|uidIter
operator|=
name|reader
operator|.
name|terms
argument_list|(
operator|new
name|Term
argument_list|(
literal|"uid"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
comment|// init uid iterator
name|indexDocs
argument_list|(
name|file
argument_list|)
expr_stmt|;
if|if
condition|(
name|deleting
condition|)
block|{
comment|// delete rest of stale docs
while|while
condition|(
name|uidIter
operator|.
name|term
argument_list|()
operator|!=
literal|null
operator|&&
name|uidIter
operator|.
name|term
argument_list|()
operator|.
name|field
argument_list|()
operator|.
name|equals
argument_list|(
literal|"uid"
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"deleting "
operator|+
name|HTMLDocument
operator|.
name|uid2url
argument_list|(
name|uidIter
operator|.
name|term
argument_list|()
operator|.
name|text
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|reader
operator|.
name|deleteDocuments
argument_list|(
name|uidIter
operator|.
name|term
argument_list|()
argument_list|)
expr_stmt|;
name|uidIter
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|deleting
operator|=
literal|false
expr_stmt|;
block|}
name|uidIter
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// close uid iterator
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// close existing index
block|}
else|else
block|{
name|indexDocs
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|indexDocs
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// if a directory
name|String
index|[]
name|files
init|=
name|file
operator|.
name|list
argument_list|()
decl_stmt|;
comment|// list its files
name|Arrays
operator|.
name|sort
argument_list|(
name|files
argument_list|)
expr_stmt|;
comment|// sort the files
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
comment|// recursively index them
block|{
name|indexDocs
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|,
name|files
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|uidIter
operator|!=
literal|null
condition|)
block|{
name|String
name|uid
init|=
name|HTMLDocument
operator|.
name|uid
argument_list|(
name|file
argument_list|)
decl_stmt|;
comment|// construct uid for doc
while|while
condition|(
name|uidIter
operator|.
name|term
argument_list|()
operator|!=
literal|null
operator|&&
name|uidIter
operator|.
name|term
argument_list|()
operator|.
name|field
argument_list|()
operator|.
name|equals
argument_list|(
literal|"uid"
argument_list|)
operator|&&
name|uidIter
operator|.
name|term
argument_list|()
operator|.
name|text
argument_list|()
operator|.
name|compareTo
argument_list|(
name|uid
argument_list|)
operator|<
literal|0
condition|)
block|{
if|if
condition|(
name|deleting
condition|)
block|{
comment|// delete stale docs
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"deleting "
operator|+
name|HTMLDocument
operator|.
name|uid2url
argument_list|(
name|uidIter
operator|.
name|term
argument_list|()
operator|.
name|text
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|reader
operator|.
name|deleteDocuments
argument_list|(
name|uidIter
operator|.
name|term
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|uidIter
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|uidIter
operator|.
name|term
argument_list|()
operator|!=
literal|null
operator|&&
name|uidIter
operator|.
name|term
argument_list|()
operator|.
name|field
argument_list|()
operator|.
name|equals
argument_list|(
literal|"uid"
argument_list|)
operator|&&
name|uidIter
operator|.
name|term
argument_list|()
operator|.
name|text
argument_list|()
operator|.
name|compareTo
argument_list|(
name|uid
argument_list|)
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Next uid="
operator|+
name|uidIter
argument_list|)
expr_stmt|;
name|uidIter
operator|.
name|next
argument_list|()
expr_stmt|;
comment|// keep matching docs
block|}
block|}
else|else
block|{
try|try
block|{
name|addDocument
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|//catch exception and move onto the next document
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
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
specifier|private
name|void
name|addDocument
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
name|String
name|path
init|=
name|file
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
literal|null
decl_stmt|;
comment|//Gee, this would be a great place for a command pattern
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
literal|".HTML"
argument_list|)
operator|||
comment|// index .html files
name|path
operator|.
name|endsWith
argument_list|(
literal|".HTM"
argument_list|)
operator|||
comment|// index .htm files
name|path
operator|.
name|endsWith
argument_list|(
literal|".TXT"
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Indexing Text document: "
operator|+
name|file
argument_list|)
expr_stmt|;
name|doc
operator|=
name|HTMLDocument
operator|.
name|Document
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
literal|".PDF"
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Indexing PDF document: "
operator|+
name|file
argument_list|)
expr_stmt|;
name|doc
operator|=
name|LucenePDFDocument
operator|.
name|getDocument
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Skipping "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|addDocument
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

