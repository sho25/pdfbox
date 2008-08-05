begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|graphics
operator|.
name|predictor
package|;
end_package

begin_comment
comment|/**  * The up algorithm.  *  *<code>Up(i,j) = Raw(i,j) - Raw(i,j-1)</code>  *  *<code>Raw(i,j) = Up(i,j) + Raw(i,j-1)</code>  *  * @author xylifyx@yahoo.co.uk  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|Up
extends|extends
name|PredictorAlgorithm
block|{
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|encodeLine
parameter_list|(
name|byte
index|[]
name|src
parameter_list|,
name|byte
index|[]
name|dest
parameter_list|,
name|int
name|srcDy
parameter_list|,
name|int
name|srcOffset
parameter_list|,
name|int
name|destDy
parameter_list|,
name|int
name|destOffset
parameter_list|)
block|{
name|int
name|bpl
init|=
name|getWidth
argument_list|()
operator|*
name|getBpp
argument_list|()
decl_stmt|;
comment|// case: y = 0;
if|if
condition|(
name|srcOffset
operator|-
name|srcDy
operator|<
literal|0
condition|)
block|{
if|if
condition|(
literal|0
operator|<
name|getHeight
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|bpl
condition|;
name|x
operator|++
control|)
block|{
name|dest
index|[
name|destOffset
operator|+
name|x
index|]
operator|=
name|src
index|[
name|srcOffset
operator|+
name|x
index|]
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|bpl
condition|;
name|x
operator|++
control|)
block|{
name|dest
index|[
name|destOffset
operator|+
name|x
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|src
index|[
name|srcOffset
operator|+
name|x
index|]
operator|-
name|src
index|[
name|srcOffset
operator|+
name|x
operator|-
name|srcDy
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|decodeLine
parameter_list|(
name|byte
index|[]
name|src
parameter_list|,
name|byte
index|[]
name|dest
parameter_list|,
name|int
name|srcDy
parameter_list|,
name|int
name|srcOffset
parameter_list|,
name|int
name|destDy
parameter_list|,
name|int
name|destOffset
parameter_list|)
block|{
comment|// case: y = 0;
name|int
name|bpl
init|=
name|getWidth
argument_list|()
operator|*
name|getBpp
argument_list|()
decl_stmt|;
if|if
condition|(
name|destOffset
operator|-
name|destDy
operator|<
literal|0
condition|)
block|{
if|if
condition|(
literal|0
operator|<
name|getHeight
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|bpl
condition|;
name|x
operator|++
control|)
block|{
name|dest
index|[
name|destOffset
operator|+
name|x
index|]
operator|=
name|src
index|[
name|srcOffset
operator|+
name|x
index|]
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|bpl
condition|;
name|x
operator|++
control|)
block|{
name|dest
index|[
name|destOffset
operator|+
name|x
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|src
index|[
name|srcOffset
operator|+
name|x
index|]
operator|+
name|dest
index|[
name|destOffset
operator|+
name|x
operator|-
name|destDy
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

