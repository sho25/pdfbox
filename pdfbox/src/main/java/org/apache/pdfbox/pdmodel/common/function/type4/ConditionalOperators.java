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
name|Stack
import|;
end_import

begin_comment
comment|/**  * Provides the conditional operators such as "if" and "ifelse".  *  */
end_comment

begin_class
class|class
name|ConditionalOperators
block|{
comment|/** Implements the "if" operator. */
specifier|static
class|class
name|If
implements|implements
name|Operator
block|{
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
name|InstructionSequence
name|proc
init|=
operator|(
name|InstructionSequence
operator|)
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|Boolean
name|condition
init|=
operator|(
name|Boolean
operator|)
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
name|condition
condition|)
block|{
name|proc
operator|.
name|execute
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** Implements the "ifelse" operator. */
specifier|static
class|class
name|IfElse
implements|implements
name|Operator
block|{
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
name|InstructionSequence
name|proc2
init|=
operator|(
name|InstructionSequence
operator|)
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|InstructionSequence
name|proc1
init|=
operator|(
name|InstructionSequence
operator|)
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|Boolean
name|condition
init|=
operator|(
name|Boolean
operator|)
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
name|condition
condition|)
block|{
name|proc1
operator|.
name|execute
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|proc2
operator|.
name|execute
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

