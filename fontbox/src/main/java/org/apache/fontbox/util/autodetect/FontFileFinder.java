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
name|fontbox
operator|.
name|util
operator|.
name|autodetect
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
name|net
operator|.
name|URI
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Helps to autodetect/locate available operating system fonts. This class is based on a class provided by Apache FOP.  * see org.apache.fop.fonts.autodetect.FontFileFinder  */
end_comment

begin_class
specifier|public
class|class
name|FontFileFinder
block|{
specifier|private
name|FontDirFinder
name|fontDirFinder
init|=
literal|null
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FontFileFinder
parameter_list|()
block|{     }
specifier|private
name|FontDirFinder
name|determineDirFinder
parameter_list|()
block|{
specifier|final
name|String
name|osName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
if|if
condition|(
name|osName
operator|.
name|startsWith
argument_list|(
literal|"Windows"
argument_list|)
condition|)
block|{
return|return
operator|new
name|WindowsFontDirFinder
argument_list|()
return|;
block|}
else|else
block|{
if|if
condition|(
name|osName
operator|.
name|startsWith
argument_list|(
literal|"Mac"
argument_list|)
condition|)
block|{
return|return
operator|new
name|MacFontDirFinder
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|UnixFontDirFinder
argument_list|()
return|;
block|}
block|}
block|}
comment|/**      * Automagically finds a list of font files on local system.      *       * @return List&lt;URI&gt; of font files      */
specifier|public
name|List
argument_list|<
name|URI
argument_list|>
name|find
parameter_list|()
block|{
if|if
condition|(
name|fontDirFinder
operator|==
literal|null
condition|)
block|{
name|fontDirFinder
operator|=
name|determineDirFinder
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|File
argument_list|>
name|fontDirs
init|=
name|fontDirFinder
operator|.
name|find
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|URI
argument_list|>
name|results
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|File
name|dir
range|:
name|fontDirs
control|)
block|{
name|walk
argument_list|(
name|dir
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
comment|/**      * Searches a given directory for font files.      *       * @param dir directory to search      * @return list&lt;URI&gt; of font files      */
specifier|public
name|List
argument_list|<
name|URI
argument_list|>
name|find
parameter_list|(
name|String
name|dir
parameter_list|)
block|{
name|List
argument_list|<
name|URI
argument_list|>
name|results
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|URI
argument_list|>
argument_list|()
decl_stmt|;
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|)
decl_stmt|;
if|if
condition|(
name|directory
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|walk
argument_list|(
name|directory
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
comment|/**      * walk down the driectory tree and search for font files.      *       * @param directory the directory to start at      * @param results names of all found font files      */
specifier|private
name|void
name|walk
parameter_list|(
name|File
name|directory
parameter_list|,
name|List
argument_list|<
name|URI
argument_list|>
name|results
parameter_list|)
block|{
comment|// search for font files recursively in the given directory
if|if
condition|(
name|directory
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|filelist
init|=
name|directory
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|filelist
operator|!=
literal|null
condition|)
block|{
name|int
name|numOfFiles
init|=
name|filelist
operator|.
name|length
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
name|numOfFiles
condition|;
name|i
operator|++
control|)
block|{
name|File
name|file
init|=
name|filelist
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
comment|// skip hidden directories
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|walk
argument_list|(
name|file
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|checkFontfile
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|file
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
comment|/**      * Check if the given name belongs to a font file.      *       * @param file the given file      * @return true if the given filename has a typical font file ending      */
specifier|private
name|boolean
name|checkFontfile
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|String
name|name
init|=
name|file
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
return|return
name|name
operator|.
name|endsWith
argument_list|(
literal|".ttf"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".otf"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".pfb"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".ttc"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

