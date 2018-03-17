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
name|LinkedList
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
name|Stack
import|;
end_import

begin_comment
comment|/**  * Provides the stack operators such as "pop" and "dup".  *  */
end_comment

begin_class
class|class
name|StackOperators
block|{
comment|/** Implements the "copy" operator. */
specifier|static
class|class
name|Copy
implements|implements
name|Operator
block|{
annotation|@
name|Override
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
name|int
name|n
init|=
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
name|intValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|int
name|size
init|=
name|stack
operator|.
name|size
argument_list|()
decl_stmt|;
comment|//Need to copy to a new list to avoid ConcurrentModificationException
name|List
argument_list|<
name|Object
argument_list|>
name|copy
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<>
argument_list|(
name|stack
operator|.
name|subList
argument_list|(
name|size
operator|-
name|n
argument_list|,
name|size
argument_list|)
argument_list|)
decl_stmt|;
name|stack
operator|.
name|addAll
argument_list|(
name|copy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** Implements the "dup" operator. */
specifier|static
class|class
name|Dup
implements|implements
name|Operator
block|{
annotation|@
name|Override
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
name|stack
operator|.
name|push
argument_list|(
name|stack
operator|.
name|peek
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Implements the "exch" operator. */
specifier|static
class|class
name|Exch
implements|implements
name|Operator
block|{
annotation|@
name|Override
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
name|Object
name|any2
init|=
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|Object
name|any1
init|=
name|stack
operator|.
name|pop
argument_list|()
decl_stmt|;
name|stack
operator|.
name|push
argument_list|(
name|any2
argument_list|)
expr_stmt|;
name|stack
operator|.
name|push
argument_list|(
name|any1
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Implements the "index" operator. */
specifier|static
class|class
name|Index
implements|implements
name|Operator
block|{
annotation|@
name|Override
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
name|int
name|n
init|=
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
name|intValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|n
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"rangecheck: "
operator|+
name|n
argument_list|)
throw|;
block|}
name|int
name|size
init|=
name|stack
operator|.
name|size
argument_list|()
decl_stmt|;
name|stack
operator|.
name|push
argument_list|(
name|stack
operator|.
name|get
argument_list|(
name|size
operator|-
name|n
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Implements the "pop" operator. */
specifier|static
class|class
name|Pop
implements|implements
name|Operator
block|{
annotation|@
name|Override
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
name|stack
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** Implements the "roll" operator. */
specifier|static
class|class
name|Roll
implements|implements
name|Operator
block|{
annotation|@
name|Override
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
name|int
name|j
init|=
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
name|intValue
argument_list|()
decl_stmt|;
name|int
name|n
init|=
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
name|intValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|j
operator|==
literal|0
condition|)
block|{
return|return;
comment|//Nothing to do
block|}
if|if
condition|(
name|n
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"rangecheck: "
operator|+
name|n
argument_list|)
throw|;
block|}
name|LinkedList
argument_list|<
name|Object
argument_list|>
name|rolled
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|LinkedList
argument_list|<
name|Object
argument_list|>
name|moved
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|j
operator|<
literal|0
condition|)
block|{
comment|//negative roll
name|int
name|n1
init|=
name|n
operator|+
name|j
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
name|n1
condition|;
name|i
operator|++
control|)
block|{
name|moved
operator|.
name|addFirst
argument_list|(
name|stack
operator|.
name|pop
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
name|j
init|;
name|i
operator|<
literal|0
condition|;
name|i
operator|++
control|)
block|{
name|rolled
operator|.
name|addFirst
argument_list|(
name|stack
operator|.
name|pop
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|stack
operator|.
name|addAll
argument_list|(
name|moved
argument_list|)
expr_stmt|;
name|stack
operator|.
name|addAll
argument_list|(
name|rolled
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//positive roll
name|int
name|n1
init|=
name|n
operator|-
name|j
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|j
init|;
name|i
operator|>
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|rolled
operator|.
name|addFirst
argument_list|(
name|stack
operator|.
name|pop
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|n1
condition|;
name|i
operator|++
control|)
block|{
name|moved
operator|.
name|addFirst
argument_list|(
name|stack
operator|.
name|pop
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|stack
operator|.
name|addAll
argument_list|(
name|rolled
argument_list|)
expr_stmt|;
name|stack
operator|.
name|addAll
argument_list|(
name|moved
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

