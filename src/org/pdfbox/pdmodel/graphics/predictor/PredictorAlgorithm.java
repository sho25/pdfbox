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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_comment
comment|/**  * Implements different PNG predictor algorithms that is used in PDF files.  *  * @author xylifyx@yahoo.co.uk  * @version $Revision: 1.4 $  * @see<a href="http://www.w3.org/TR/PNG-Filters.html">PNG Filters</a>  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PredictorAlgorithm
block|{
specifier|private
name|int
name|width
decl_stmt|;
specifier|private
name|int
name|height
decl_stmt|;
specifier|private
name|int
name|bpp
decl_stmt|;
comment|/**      * check that buffer sizes matches width,height,bpp. This implementation is      * used by most of the filters, but not Uptimum.      *      * @param src The source buffer.      * @param dest The destination buffer.      */
specifier|public
name|void
name|checkBufsiz
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
if|if
condition|(
name|src
operator|.
name|length
operator|!=
name|dest
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"src.length != dest.length"
argument_list|)
throw|;
block|}
if|if
condition|(
name|src
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
literal|"src.length != width * height * bpp"
argument_list|)
throw|;
block|}
block|}
comment|/**      * encode line of pixel data in src from srcOffset and width*bpp bytes      * forward, put the decoded bytes into dest.      *      * @param src      *            raw image data      * @param dest      *            encoded data      * @param srcDy      *            byte offset between lines      * @param srcOffset      *            beginning of line data      * @param destDy      *            byte offset between lines      * @param destOffset      *            beginning of line data      */
specifier|public
specifier|abstract
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
function_decl|;
comment|/**      * decode line of pixel data in src from src_offset and width*bpp bytes      * forward, put the decoded bytes into dest.      *      * @param src      *            encoded image data      * @param dest      *            raw data      * @param srcDy      *            byte offset between lines      * @param srcOffset      *            beginning of line data      * @param destDy      *            byte offset between lines      * @param destOffset      *            beginning of line data      */
specifier|public
specifier|abstract
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
function_decl|;
comment|/**      * Simple command line program to test the algorithm.      *      * @param args The command line arguments.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|Random
name|rnd
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|int
name|width
init|=
literal|5
decl_stmt|;
name|int
name|height
init|=
literal|5
decl_stmt|;
name|int
name|bpp
init|=
literal|3
decl_stmt|;
name|byte
index|[]
name|raw
init|=
operator|new
name|byte
index|[
name|width
operator|*
name|height
operator|*
name|bpp
index|]
decl_stmt|;
name|rnd
operator|.
name|nextBytes
argument_list|(
name|raw
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"raw:   "
argument_list|)
expr_stmt|;
name|dump
argument_list|(
name|raw
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|10
init|;
name|i
operator|<
literal|15
condition|;
name|i
operator|++
control|)
block|{
name|byte
index|[]
name|decoded
init|=
operator|new
name|byte
index|[
name|width
operator|*
name|height
operator|*
name|bpp
index|]
decl_stmt|;
name|byte
index|[]
name|encoded
init|=
operator|new
name|byte
index|[
name|width
operator|*
name|height
operator|*
name|bpp
index|]
decl_stmt|;
name|PredictorAlgorithm
name|filter
init|=
name|PredictorAlgorithm
operator|.
name|getFilter
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|filter
operator|.
name|setWidth
argument_list|(
name|width
argument_list|)
expr_stmt|;
name|filter
operator|.
name|setHeight
argument_list|(
name|height
argument_list|)
expr_stmt|;
name|filter
operator|.
name|setBpp
argument_list|(
name|bpp
argument_list|)
expr_stmt|;
name|filter
operator|.
name|encode
argument_list|(
name|raw
argument_list|,
name|encoded
argument_list|)
expr_stmt|;
name|filter
operator|.
name|decode
argument_list|(
name|encoded
argument_list|,
name|decoded
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|filter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|dump
argument_list|(
name|decoded
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the left pixel from the buffer.      *      * @param buf The buffer.      * @param offset The offset into the buffer.      * @param dy The dy value.      * @param x The x value.      *      * @return The left pixel.      */
specifier|public
name|int
name|leftPixel
parameter_list|(
name|byte
index|[]
name|buf
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|dy
parameter_list|,
name|int
name|x
parameter_list|)
block|{
return|return
name|x
operator|>=
name|getBpp
argument_list|()
condition|?
name|buf
index|[
name|offset
operator|+
name|x
operator|-
name|getBpp
argument_list|()
index|]
else|:
literal|0
return|;
block|}
comment|/**      * Get the above pixel from the buffer.      *      * @param buf The buffer.      * @param offset The offset into the buffer.      * @param dy The dy value.      * @param x The x value.      *      * @return The above pixel.      */
specifier|public
name|int
name|abovePixel
parameter_list|(
name|byte
index|[]
name|buf
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|dy
parameter_list|,
name|int
name|x
parameter_list|)
block|{
return|return
name|offset
operator|>=
name|dy
condition|?
name|buf
index|[
name|offset
operator|+
name|x
operator|-
name|dy
index|]
else|:
literal|0
return|;
block|}
comment|/**      * Get the above-left pixel from the buffer.      *      * @param buf The buffer.      * @param offset The offset into the buffer.      * @param dy The dy value.      * @param x The x value.      *      * @return The above-left pixel.      */
specifier|public
name|int
name|aboveLeftPixel
parameter_list|(
name|byte
index|[]
name|buf
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|dy
parameter_list|,
name|int
name|x
parameter_list|)
block|{
return|return
name|offset
operator|>=
name|dy
operator|&&
name|x
operator|>=
name|getBpp
argument_list|()
condition|?
name|buf
index|[
name|offset
operator|+
name|x
operator|-
name|dy
operator|-
name|getBpp
argument_list|()
index|]
else|:
literal|0
return|;
block|}
comment|/**      * Simple helper to print out a buffer.      *      * @param raw The bytes to print out.      */
specifier|private
specifier|static
name|void
name|dump
parameter_list|(
name|byte
index|[]
name|raw
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|raw
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|raw
index|[
name|i
index|]
operator|+
literal|" "
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return Returns the bpp.      */
specifier|public
name|int
name|getBpp
parameter_list|()
block|{
return|return
name|bpp
return|;
block|}
comment|/**      * @param newBpp      *            The bpp to set.      */
specifier|public
name|void
name|setBpp
parameter_list|(
name|int
name|newBpp
parameter_list|)
block|{
name|bpp
operator|=
name|newBpp
expr_stmt|;
block|}
comment|/**      * @return Returns the height.      */
specifier|public
name|int
name|getHeight
parameter_list|()
block|{
return|return
name|height
return|;
block|}
comment|/**      * @param newHeight      *            The height to set.      */
specifier|public
name|void
name|setHeight
parameter_list|(
name|int
name|newHeight
parameter_list|)
block|{
name|height
operator|=
name|newHeight
expr_stmt|;
block|}
comment|/**      * @return Returns the width.      */
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
return|return
name|width
return|;
block|}
comment|/**      * @param newWidth      *            The width to set.      */
specifier|public
name|void
name|setWidth
parameter_list|(
name|int
name|newWidth
parameter_list|)
block|{
name|this
operator|.
name|width
operator|=
name|newWidth
expr_stmt|;
block|}
comment|/**      * encode a byte array full of image data using the filter that this object      * implements.      *      * @param src      *            buffer      * @param dest      *            buffer      */
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
name|int
name|dy
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
name|y
init|=
literal|0
init|;
name|y
operator|<
name|height
condition|;
name|y
operator|++
control|)
block|{
name|int
name|yoffset
init|=
name|y
operator|*
name|dy
decl_stmt|;
name|encodeLine
argument_list|(
name|src
argument_list|,
name|dest
argument_list|,
name|dy
argument_list|,
name|yoffset
argument_list|,
name|dy
argument_list|,
name|yoffset
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * decode a byte array full of image data using the filter that this object      * implements.      *      * @param src      *            buffer      * @param dest      *            buffer      */
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
name|dy
init|=
name|width
operator|*
name|bpp
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
name|height
condition|;
name|y
operator|++
control|)
block|{
name|int
name|yoffset
init|=
name|y
operator|*
name|dy
decl_stmt|;
name|decodeLine
argument_list|(
name|src
argument_list|,
name|dest
argument_list|,
name|dy
argument_list|,
name|yoffset
argument_list|,
name|dy
argument_list|,
name|yoffset
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @param predictor      *<ul>      *<li>1 No prediction (the default value)      *<li>2 TIFF Predictor 2      *<li>10 PNG prediction (on encoding, PNG None on all rows)      *<li>11 PNG prediction (on encoding, PNG Sub on all rows)      *<li>12 PNG prediction (on encoding, PNG Up on all rows)      *<li>13 PNG prediction (on encoding, PNG Average on all rows)      *<li>14 PNG prediction (on encoding, PNG Paeth on all rows)      *<li>15 PNG prediction (on encoding, PNG optimum)      *</ul>      *      * @return The predictor class based on the predictor code.      */
specifier|public
specifier|static
name|PredictorAlgorithm
name|getFilter
parameter_list|(
name|int
name|predictor
parameter_list|)
block|{
name|PredictorAlgorithm
name|filter
decl_stmt|;
switch|switch
condition|(
name|predictor
condition|)
block|{
case|case
literal|10
case|:
name|filter
operator|=
operator|new
name|None
argument_list|()
expr_stmt|;
break|break;
case|case
literal|11
case|:
name|filter
operator|=
operator|new
name|Sub
argument_list|()
expr_stmt|;
break|break;
case|case
literal|12
case|:
name|filter
operator|=
operator|new
name|Up
argument_list|()
expr_stmt|;
break|break;
case|case
literal|13
case|:
name|filter
operator|=
operator|new
name|Average
argument_list|()
expr_stmt|;
break|break;
case|case
literal|14
case|:
name|filter
operator|=
operator|new
name|Paeth
argument_list|()
expr_stmt|;
break|break;
case|case
literal|15
case|:
name|filter
operator|=
operator|new
name|Optimum
argument_list|()
expr_stmt|;
break|break;
default|default:
name|filter
operator|=
operator|new
name|None
argument_list|()
expr_stmt|;
block|}
return|return
name|filter
return|;
block|}
block|}
end_class

end_unit

