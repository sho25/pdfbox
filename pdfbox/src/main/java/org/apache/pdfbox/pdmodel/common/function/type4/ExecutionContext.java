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
comment|/**  * Makes up the execution context, holding the available operators and the execution stack.  *  * @version $Revision$  */
end_comment

begin_class
specifier|public
class|class
name|ExecutionContext
block|{
specifier|private
name|Operators
name|operators
decl_stmt|;
specifier|private
name|Stack
argument_list|<
name|Object
argument_list|>
name|stack
init|=
operator|new
name|Stack
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Creates a new execution context.      * @param operatorSet the operator set      */
specifier|public
name|ExecutionContext
parameter_list|(
name|Operators
name|operatorSet
parameter_list|)
block|{
name|this
operator|.
name|operators
operator|=
name|operatorSet
expr_stmt|;
block|}
comment|/**      * Returns the stack used by this execution context.      * @return the stack      */
specifier|public
name|Stack
argument_list|<
name|Object
argument_list|>
name|getStack
parameter_list|()
block|{
return|return
name|this
operator|.
name|stack
return|;
block|}
comment|/**      * Returns the operator set used by this execution context.      * @return the operator set      */
specifier|public
name|Operators
name|getOperators
parameter_list|()
block|{
return|return
name|this
operator|.
name|operators
return|;
block|}
comment|/**      * Pops a number (int or real) from the stack. If it's neither data type, a      * ClassCastException is thrown.      * @return the number      */
specifier|public
name|Number
name|popNumber
parameter_list|()
block|{
return|return
operator|(
name|Number
operator|)
name|stack
operator|.
name|pop
argument_list|()
return|;
block|}
comment|/**      * Pops a value of type int from the stack. If the value is not of type int, a      * ClassCastException is thrown.      * @return the int value      */
specifier|public
name|int
name|popInt
parameter_list|()
block|{
return|return
operator|(
operator|(
name|Integer
operator|)
name|stack
operator|.
name|pop
argument_list|()
operator|)
operator|.
name|intValue
argument_list|()
return|;
block|}
comment|/**      * Pops a number from the stack and returns it as a real value. If the value is not of a      * numeric type, a ClassCastException is thrown.      * @return the real value      */
specifier|public
name|float
name|popReal
parameter_list|()
block|{
return|return
operator|(
operator|(
name|Number
operator|)
name|stack
operator|.
name|pop
argument_list|()
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

