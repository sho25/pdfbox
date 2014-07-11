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
name|common
operator|.
name|function
operator|.
name|type4
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
comment|/**  * Represents an instruction sequence, a combination of values, operands and nested procedures.  *  * @version $Revision$  */
end_comment

begin_class
specifier|public
class|class
name|InstructionSequence
block|{
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|instructions
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Add a name (ex. an operator)      * @param name the name      */
specifier|public
name|void
name|addName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|instructions
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds an int value.      * @param value the value      */
specifier|public
name|void
name|addInteger
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|this
operator|.
name|instructions
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a real value.      * @param value the value      */
specifier|public
name|void
name|addReal
parameter_list|(
name|float
name|value
parameter_list|)
block|{
name|this
operator|.
name|instructions
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a bool value.      * @param value the value      */
specifier|public
name|void
name|addBoolean
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|instructions
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a proc (sub-sequence of instructions).      * @param child the child proc      */
specifier|public
name|void
name|addProc
parameter_list|(
name|InstructionSequence
name|child
parameter_list|)
block|{
name|this
operator|.
name|instructions
operator|.
name|add
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
comment|/**      * Executes the instruction sequence.      * @param context the execution context      */
specifier|public
name|void
name|execute
parameter_list|(
name|ExecutionContext
name|context
parameter_list|)
block|{
name|Stack
argument_list|<
name|Object
argument_list|>
name|stack
init|=
name|context
operator|.
name|getStack
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|instructions
control|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|o
decl_stmt|;
name|Operator
name|cmd
init|=
name|context
operator|.
name|getOperators
argument_list|()
operator|.
name|getOperator
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmd
operator|!=
literal|null
condition|)
block|{
name|cmd
operator|.
name|execute
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Unknown operator or name: "
operator|+
name|name
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|stack
operator|.
name|push
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
comment|//Handles top-level procs that simply need to be executed
while|while
condition|(
operator|!
name|stack
operator|.
name|isEmpty
argument_list|()
operator|&&
name|stack
operator|.
name|peek
argument_list|()
operator|instanceof
name|InstructionSequence
condition|)
block|{
name|InstructionSequence
name|nested
init|=
operator|(
name|InstructionSequence
operator|)
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|nested
operator|.
name|execute
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

