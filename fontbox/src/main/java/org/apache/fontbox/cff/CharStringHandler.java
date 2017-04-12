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
name|cff
package|;
end_package

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
name|Stack
import|;
end_import

begin_comment
comment|/**  * A Handler for CharStringCommands.  *  * @author Villu Ruusmann  * @author John Hewson  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CharStringHandler
block|{
comment|/**      * Handler for a sequence of CharStringCommands.      *      * @param sequence of CharStringCommands      *      */
specifier|public
name|List
argument_list|<
name|Number
argument_list|>
name|handleSequence
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|sequence
parameter_list|)
block|{
name|Stack
argument_list|<
name|Number
argument_list|>
name|stack
init|=
operator|new
name|Stack
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|sequence
control|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|CharStringCommand
condition|)
block|{
name|List
argument_list|<
name|Number
argument_list|>
name|results
init|=
name|handleCommand
argument_list|(
name|stack
argument_list|,
operator|(
name|CharStringCommand
operator|)
name|obj
argument_list|)
decl_stmt|;
name|stack
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// this is basically returning the new stack
if|if
condition|(
name|results
operator|!=
literal|null
condition|)
block|{
name|stack
operator|.
name|addAll
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|stack
operator|.
name|push
argument_list|(
operator|(
name|Number
operator|)
name|obj
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|stack
return|;
block|}
comment|/**      * Handler for CharStringCommands.      *      * @param numbers a list of numbers      * @param command the CharStringCommand      */
specifier|public
specifier|abstract
name|List
argument_list|<
name|Number
argument_list|>
name|handleCommand
parameter_list|(
name|List
argument_list|<
name|Number
argument_list|>
name|numbers
parameter_list|,
name|CharStringCommand
name|command
parameter_list|)
function_decl|;
block|}
end_class

end_unit

