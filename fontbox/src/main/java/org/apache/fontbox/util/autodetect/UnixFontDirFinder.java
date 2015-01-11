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

begin_comment
comment|/**  * Unix font directory finder. This class is based on a class provided by Apache FOP. see  * org.apache.fop.fonts.autodetect.UnixFontDirFinder  */
end_comment

begin_class
specifier|public
class|class
name|UnixFontDirFinder
extends|extends
name|NativeFontDirFinder
block|{
comment|/**      * Some guesses at possible unix font directory locations.      *       * @return a list of possible font locations      */
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getSearchableDirectories
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
literal|"/.fonts"
block|,
comment|// user
literal|"/usr/local/fonts"
block|,
comment|// local
literal|"/usr/local/share/fonts"
block|,
comment|// local shared
literal|"/usr/share/fonts"
block|,
comment|// system
literal|"/usr/X11R6/lib/X11/fonts"
comment|// X
block|}
return|;
block|}
block|}
end_class

end_unit

