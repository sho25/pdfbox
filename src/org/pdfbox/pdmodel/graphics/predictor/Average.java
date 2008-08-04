begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * We can use raw on the right hand side of  * the decoding formula because it is already decoded.  *   *<code>average(i,j) = raw(i,j) + (raw(i-1,j)+raw(i,j-1)/2</code>  *   * decoding  *   *<code>raw(i,j) = avarage(i,j) - (raw(i-1,j)+raw(i,j-1)/2</code>  *   * @author xylifyx@yahoo.co.uk  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|Average
extends|extends
name|PredictorAlgorithm
block|{
comment|/**      * Not an optimal version, but close to the def.      *       * {@inheritDoc}      */
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
name|x
operator|+
name|destOffset
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|src
index|[
name|x
operator|+
name|srcOffset
index|]
operator|-
operator|(
operator|(
name|leftPixel
argument_list|(
name|src
argument_list|,
name|srcOffset
argument_list|,
name|srcDy
argument_list|,
name|x
argument_list|)
operator|+
name|abovePixel
argument_list|(
name|src
argument_list|,
name|srcOffset
argument_list|,
name|srcDy
argument_list|,
name|x
argument_list|)
operator|)
operator|>>>
literal|2
operator|)
argument_list|)
expr_stmt|;
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
name|int
name|bpl
init|=
name|getWidth
argument_list|()
operator|*
name|getBpp
argument_list|()
decl_stmt|;
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
name|x
operator|+
name|destOffset
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|src
index|[
name|x
operator|+
name|srcOffset
index|]
operator|+
operator|(
operator|(
name|leftPixel
argument_list|(
name|dest
argument_list|,
name|destOffset
argument_list|,
name|destDy
argument_list|,
name|x
argument_list|)
operator|+
name|abovePixel
argument_list|(
name|dest
argument_list|,
name|destOffset
argument_list|,
name|destDy
argument_list|,
name|x
argument_list|)
operator|)
operator|>>>
literal|2
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

