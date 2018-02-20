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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Native font finder base class. This class is based on a class provided by Apache FOP. see  * org.apache.fop.fonts.autodetect.NativeFontDirFinder  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|NativeFontDirFinder
implements|implements
name|FontDirFinder
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|NativeFontDirFinder
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Generic method used by Mac and Unix font finders.      *       * @return list of natively existing font directories {@inheritDoc}      */
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
index|[]
name|searchableDirectories
init|=
name|getSearchableDirectories
argument_list|()
decl_stmt|;
if|if
condition|(
name|searchableDirectories
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|searchableDirectorie
range|:
name|searchableDirectories
control|)
block|{
name|File
name|fontDir
init|=
operator|new
name|File
argument_list|(
name|searchableDirectorie
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|fontDir
operator|.
name|exists
argument_list|()
operator|&&
name|fontDir
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|fontDirList
operator|.
name|add
argument_list|(
name|fontDir
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Couldn't get native font directories - ignoring"
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// should continue if this fails
block|}
block|}
block|}
return|return
name|fontDirList
return|;
block|}
comment|/**      * Returns an array of directories to search for fonts in.      *       * @return an array of directories      */
specifier|protected
specifier|abstract
name|String
index|[]
name|getSearchableDirectories
parameter_list|()
function_decl|;
block|}
end_class

end_unit

