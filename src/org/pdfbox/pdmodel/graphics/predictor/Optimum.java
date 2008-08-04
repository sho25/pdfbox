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
comment|/**  *   *   * In an Uptimum encoded image, each line takes up width*bpp+1 bytes. The first  * byte holds a number that signifies which algorithm encoded the line.  *   * @author xylifyx@yahoo.co.uk  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|Optimum
extends|extends
name|PredictorAlgorithm
block|{
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|checkBufsiz
parameter_list|(
name|byte
index|[]
name|filtered
parameter_list|,
name|byte
index|[]
name|raw
parameter_list|)
block|{
if|if
condition|(
name|filtered
operator|.
name|length
operator|!=
operator|(
name|getWidth
argument_list|()
operator|*
name|getBpp
argument_list|()
operator|+
literal|1
operator|)
operator|*
name|getHeight
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"filtered.length != (width*bpp + 1) * height, "
operator|+
name|filtered
operator|.
name|length
operator|+
literal|" "
operator|+
operator|(
name|getWidth
argument_list|()
operator|*
name|getBpp
argument_list|()
operator|+
literal|1
operator|)
operator|*
name|getHeight
argument_list|()
operator|+
literal|"w,h,bpp="
operator|+
name|getWidth
argument_list|()
operator|+
literal|","
operator|+
name|getHeight
argument_list|()
operator|+
literal|","
operator|+
name|getBpp
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|raw
operator|.
name|length
operator|!=
name|getWidth
argument_list|()
operator|*
name|getHeight
argument_list|()
operator|*
name|getBpp
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"raw.length != width * height * bpp, raw.length="
operator|+
name|raw
operator|.
name|length
operator|+
literal|" w,h,bpp="
operator|+
name|getWidth
argument_list|()
operator|+
literal|","
operator|+
name|getHeight
argument_list|()
operator|+
literal|","
operator|+
name|getBpp
argument_list|()
argument_list|)
throw|;
block|}
block|}
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"encodeLine"
argument_list|)
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"decodeLine"
argument_list|)
throw|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|encode
parameter_list|(
name|byte
index|[]
name|src
parameter_list|,
name|byte
index|[]
name|dest
parameter_list|)
block|{
name|checkBufsiz
argument_list|(
name|dest
argument_list|,
name|src
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"encode"
argument_list|)
throw|;
block|}
comment|/**       * Filter indexed by byte code.      */
name|PredictorAlgorithm
index|[]
name|filter
init|=
block|{
operator|new
name|None
argument_list|()
block|,
operator|new
name|Sub
argument_list|()
block|,
operator|new
name|Up
argument_list|()
block|,
operator|new
name|Average
argument_list|()
block|,
operator|new
name|Paeth
argument_list|()
block|}
decl_stmt|;
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|setBpp
parameter_list|(
name|int
name|bpp
parameter_list|)
block|{
name|super
operator|.
name|setBpp
argument_list|(
name|bpp
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|filter
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|filter
index|[
name|i
index|]
operator|.
name|setBpp
argument_list|(
name|bpp
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|setHeight
parameter_list|(
name|int
name|height
parameter_list|)
block|{
name|super
operator|.
name|setHeight
argument_list|(
name|height
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|filter
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|filter
index|[
name|i
index|]
operator|.
name|setHeight
argument_list|(
name|height
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|setWidth
parameter_list|(
name|int
name|width
parameter_list|)
block|{
name|super
operator|.
name|setWidth
argument_list|(
name|width
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|filter
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|filter
index|[
name|i
index|]
operator|.
name|setWidth
argument_list|(
name|width
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|decode
parameter_list|(
name|byte
index|[]
name|src
parameter_list|,
name|byte
index|[]
name|dest
parameter_list|)
block|{
name|checkBufsiz
argument_list|(
name|src
argument_list|,
name|dest
argument_list|)
expr_stmt|;
name|int
name|bpl
init|=
name|getWidth
argument_list|()
operator|*
name|getBpp
argument_list|()
decl_stmt|;
name|int
name|srcDy
init|=
name|bpl
operator|+
literal|1
decl_stmt|;
for|for
control|(
name|int
name|y
init|=
literal|0
init|;
name|y
operator|<
name|getHeight
argument_list|()
condition|;
name|y
operator|++
control|)
block|{
name|PredictorAlgorithm
name|f
init|=
name|filter
index|[
name|src
index|[
name|y
operator|*
name|srcDy
index|]
index|]
decl_stmt|;
name|int
name|srcOffset
init|=
name|y
operator|*
name|srcDy
operator|+
literal|1
decl_stmt|;
name|f
operator|.
name|decodeLine
argument_list|(
name|src
argument_list|,
name|dest
argument_list|,
name|srcDy
argument_list|,
name|srcOffset
argument_list|,
name|bpl
argument_list|,
name|y
operator|*
name|bpl
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

