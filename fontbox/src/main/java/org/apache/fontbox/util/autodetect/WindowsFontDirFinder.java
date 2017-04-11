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
name|BufferedReader
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
name|io
operator|.
name|InputStreamReader
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
name|fontbox
operator|.
name|util
operator|.
name|Charsets
import|;
end_import

begin_comment
comment|/**  * FontFinder for native Windows platforms. This class is based on a class provided by Apache FOP. see  * org.apache.fop.fonts.autodetect.WindowsFontDirFinder  */
end_comment

begin_class
specifier|public
class|class
name|WindowsFontDirFinder
implements|implements
name|FontDirFinder
block|{
comment|/**      * Attempts to read windir environment variable on windows (disclaimer: This is a bit dirty but seems to work      * nicely).      */
specifier|private
name|String
name|getWinDir
parameter_list|(
name|String
name|osName
parameter_list|)
throws|throws
name|IOException
block|{
name|Process
name|process
decl_stmt|;
name|Runtime
name|runtime
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
decl_stmt|;
if|if
condition|(
name|osName
operator|.
name|startsWith
argument_list|(
literal|"Windows 9"
argument_list|)
condition|)
block|{
name|process
operator|=
name|runtime
operator|.
name|exec
argument_list|(
literal|"command.com /c echo %windir%"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|process
operator|=
name|runtime
operator|.
name|exec
argument_list|(
literal|"cmd.exe /c echo %windir%"
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|BufferedReader
name|bufferedReader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|process
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|Charsets
operator|.
name|ISO_8859_1
argument_list|)
argument_list|)
init|)
block|{
return|return
name|bufferedReader
operator|.
name|readLine
argument_list|()
return|;
block|}
block|}
comment|/**      * {@inheritDoc}      *       * @return a list of detected font files      */
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|File
argument_list|>
name|find
parameter_list|()
block|{
name|List
argument_list|<
name|File
argument_list|>
name|fontDirList
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|windir
init|=
literal|null
decl_stmt|;
try|try
block|{
name|windir
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"env.windir"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
comment|// should continue if this fails
block|}
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
name|windir
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|windir
operator|=
name|getWinDir
argument_list|(
name|osName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|SecurityException
name|e
parameter_list|)
block|{
comment|// should continue if this fails
block|}
block|}
name|File
name|osFontsDir
decl_stmt|;
name|File
name|psFontsDir
decl_stmt|;
if|if
condition|(
name|windir
operator|!=
literal|null
condition|)
block|{
comment|// remove any trailing '/'
if|if
condition|(
name|windir
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|windir
operator|=
name|windir
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|windir
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|osFontsDir
operator|=
operator|new
name|File
argument_list|(
name|windir
operator|+
name|File
operator|.
name|separator
operator|+
literal|"FONTS"
argument_list|)
expr_stmt|;
if|if
condition|(
name|osFontsDir
operator|.
name|exists
argument_list|()
operator|&&
name|osFontsDir
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|fontDirList
operator|.
name|add
argument_list|(
name|osFontsDir
argument_list|)
expr_stmt|;
block|}
name|psFontsDir
operator|=
operator|new
name|File
argument_list|(
name|windir
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
literal|"PSFONTS"
argument_list|)
expr_stmt|;
if|if
condition|(
name|psFontsDir
operator|.
name|exists
argument_list|()
operator|&&
name|psFontsDir
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|fontDirList
operator|.
name|add
argument_list|(
name|psFontsDir
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|windowsDirName
init|=
name|osName
operator|.
name|endsWith
argument_list|(
literal|"NT"
argument_list|)
condition|?
literal|"WINNT"
else|:
literal|"WINDOWS"
decl_stmt|;
comment|// look for true type font folder
for|for
control|(
name|char
name|driveLetter
init|=
literal|'C'
init|;
name|driveLetter
operator|<=
literal|'E'
condition|;
name|driveLetter
operator|++
control|)
block|{
name|osFontsDir
operator|=
operator|new
name|File
argument_list|(
name|driveLetter
operator|+
literal|":"
operator|+
name|File
operator|.
name|separator
operator|+
name|windowsDirName
operator|+
name|File
operator|.
name|separator
operator|+
literal|"FONTS"
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|osFontsDir
operator|.
name|exists
argument_list|()
operator|&&
name|osFontsDir
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|fontDirList
operator|.
name|add
argument_list|(
name|osFontsDir
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
comment|// should continue if this fails
block|}
block|}
comment|// look for type 1 font folder
for|for
control|(
name|char
name|driveLetter
init|=
literal|'C'
init|;
name|driveLetter
operator|<=
literal|'E'
condition|;
name|driveLetter
operator|++
control|)
block|{
name|psFontsDir
operator|=
operator|new
name|File
argument_list|(
name|driveLetter
operator|+
literal|":"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"PSFONTS"
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|psFontsDir
operator|.
name|exists
argument_list|()
operator|&&
name|psFontsDir
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|fontDirList
operator|.
name|add
argument_list|(
name|psFontsDir
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
comment|// should continue if this fails
block|}
block|}
block|}
return|return
name|fontDirList
return|;
block|}
block|}
end_class

end_unit

