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
name|util
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
name|javax
operator|.
name|swing
operator|.
name|filechooser
operator|.
name|FileFilter
import|;
end_import

begin_comment
comment|/**  * A FileFilter that will only accept files of a certain extension.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|ExtensionFileFilter
extends|extends
name|FileFilter
block|{
specifier|private
name|String
index|[]
name|extensions
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|desc
decl_stmt|;
comment|/**      * Constructor.      *      * @param ext A list of filename extensions, ie new String[] { "PDF"}.      * @param description A description of the files.      */
specifier|public
name|ExtensionFileFilter
parameter_list|(
name|String
index|[]
name|ext
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|extensions
operator|=
name|ext
expr_stmt|;
name|desc
operator|=
name|description
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|pathname
parameter_list|)
block|{
if|if
condition|(
name|pathname
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
name|boolean
name|acceptable
init|=
literal|false
decl_stmt|;
name|String
name|name
init|=
name|pathname
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|!
name|acceptable
operator|&&
name|i
operator|<
name|extensions
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
name|extensions
index|[
name|i
index|]
operator|.
name|toUpperCase
argument_list|()
argument_list|)
condition|)
block|{
name|acceptable
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|acceptable
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|desc
return|;
block|}
block|}
end_class

end_unit

