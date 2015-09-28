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
name|pdmodel
operator|.
name|font
package|;
end_package

begin_comment
comment|/**  * FontMapper factory class.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|FontMappers
block|{
specifier|private
specifier|static
name|FontMapper
name|instance
decl_stmt|;
comment|// lazy thread safe singleton
specifier|private
specifier|static
class|class
name|DefaultFontMapper
block|{
specifier|private
specifier|static
specifier|final
name|FontMapper
name|INSTANCE
init|=
operator|new
name|FontMapperImpl
argument_list|()
decl_stmt|;
block|}
comment|/**      * Returns the singleton FontMapper instance.      */
specifier|public
specifier|static
name|FontMapper
name|instance
parameter_list|()
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
name|DefaultFontMapper
operator|.
name|INSTANCE
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
comment|/**      * Sets the singleton FontMapper instance.      */
specifier|public
specifier|static
specifier|synchronized
name|void
name|set
parameter_list|(
name|FontMapper
name|fontMapper
parameter_list|)
block|{
name|instance
operator|=
name|fontMapper
expr_stmt|;
block|}
block|}
end_class

end_unit

