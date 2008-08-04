begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2005, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
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
comment|/**  * The sub algorithm.  *   *<code>Sub(i,j) = Raw(i,j) - Raw(i-1,j)</code>  *   *<code>Raw(i,j) = Sub(i,j) + Raw(i-1,j)</code>  *   * @author xylifyx@yahoo.co.uk  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|Sub
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
name|int
name|bpp
init|=
name|getBpp
argument_list|()
decl_stmt|;
comment|// case: x< bpp
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
operator|&&
name|x
operator|<
name|bpp
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
name|src
index|[
name|x
operator|+
name|srcOffset
index|]
expr_stmt|;
block|}
comment|// otherwise
for|for
control|(
name|int
name|x
init|=
name|getBpp
argument_list|()
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
name|src
index|[
name|x
operator|+
name|srcOffset
operator|-
name|bpp
index|]
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
name|int
name|bpp
init|=
name|getBpp
argument_list|()
decl_stmt|;
comment|// case: x< bpp
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
operator|&&
name|x
operator|<
name|bpp
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
name|src
index|[
name|x
operator|+
name|srcOffset
index|]
expr_stmt|;
block|}
comment|// otherwise
for|for
control|(
name|int
name|x
init|=
name|getBpp
argument_list|()
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
name|dest
index|[
name|x
operator|+
name|destOffset
operator|-
name|bpp
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

