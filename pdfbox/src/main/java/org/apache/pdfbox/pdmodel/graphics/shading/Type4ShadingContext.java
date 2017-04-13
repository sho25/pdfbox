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
name|graphics
operator|.
name|shading
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Rectangle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|Point2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|ColorModel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|EOFException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|ImageInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|MemoryCacheImageInputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSDictionary
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSStream
import|;
end_import

begin_import
import|import
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
name|PDRange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * AWT PaintContext for Gouraud Triangle Mesh (Type 4) shading.  *  * @author Tilman Hausherr  * @author Shaola Ren  */
end_comment

begin_class
class|class
name|Type4ShadingContext
extends|extends
name|GouraudShadingContext
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|Type4ShadingContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|int
name|bitsPerFlag
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      *      * @param shading the shading type to be used      * @param cm the color model to be used      * @param xform transformation for user to device space      * @param matrix the pattern matrix concatenated with that of the parent content stream      */
name|Type4ShadingContext
parameter_list|(
name|PDShadingType4
name|shading
parameter_list|,
name|ColorModel
name|cm
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|Matrix
name|matrix
parameter_list|,
name|Rectangle
name|deviceBounds
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|shading
argument_list|,
name|cm
argument_list|,
name|xform
argument_list|,
name|matrix
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Type4ShadingContext"
argument_list|)
expr_stmt|;
name|bitsPerFlag
operator|=
name|shading
operator|.
name|getBitsPerFlag
argument_list|()
expr_stmt|;
comment|//TODO handle cases where bitperflag isn't 8
name|LOG
operator|.
name|debug
argument_list|(
literal|"bitsPerFlag: "
operator|+
name|bitsPerFlag
argument_list|)
expr_stmt|;
name|setTriangleList
argument_list|(
name|collectTriangles
argument_list|(
name|shading
argument_list|,
name|xform
argument_list|,
name|matrix
argument_list|)
argument_list|)
expr_stmt|;
name|createPixelTable
argument_list|(
name|deviceBounds
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|ShadedTriangle
argument_list|>
name|collectTriangles
parameter_list|(
name|PDShadingType4
name|freeTriangleShadingType
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|Matrix
name|matrix
parameter_list|)
throws|throws
name|IOException
block|{
name|COSDictionary
name|dict
init|=
name|freeTriangleShadingType
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|PDRange
name|rangeX
init|=
name|freeTriangleShadingType
operator|.
name|getDecodeForParameter
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDRange
name|rangeY
init|=
name|freeTriangleShadingType
operator|.
name|getDecodeForParameter
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|PDRange
index|[]
name|colRange
init|=
operator|new
name|PDRange
index|[
name|numberOfColorComponents
index|]
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
name|numberOfColorComponents
condition|;
operator|++
name|i
control|)
block|{
name|colRange
index|[
name|i
index|]
operator|=
name|freeTriangleShadingType
operator|.
name|getDecodeForParameter
argument_list|(
literal|2
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ShadedTriangle
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|long
name|maxSrcCoord
init|=
operator|(
name|long
operator|)
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
name|bitsPerCoordinate
argument_list|)
operator|-
literal|1
decl_stmt|;
name|long
name|maxSrcColor
init|=
operator|(
name|long
operator|)
name|Math
operator|.
name|pow
argument_list|(
literal|2
argument_list|,
name|bitsPerColorComponent
argument_list|)
operator|-
literal|1
decl_stmt|;
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|dict
decl_stmt|;
name|ImageInputStream
name|mciis
init|=
operator|new
name|MemoryCacheImageInputStream
argument_list|(
name|stream
operator|.
name|createInputStream
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|byte
name|flag
init|=
operator|(
name|byte
operator|)
literal|0
decl_stmt|;
try|try
block|{
name|flag
operator|=
call|(
name|byte
call|)
argument_list|(
name|mciis
operator|.
name|readBits
argument_list|(
name|bitsPerFlag
argument_list|)
operator|&
literal|3
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EOFException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
name|boolean
name|eof
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|eof
condition|)
block|{
name|Vertex
name|p0
decl_stmt|,
name|p1
decl_stmt|,
name|p2
decl_stmt|;
name|Point2D
index|[]
name|ps
decl_stmt|;
name|float
index|[]
index|[]
name|cs
decl_stmt|;
name|int
name|lastIndex
decl_stmt|;
try|try
block|{
switch|switch
condition|(
name|flag
condition|)
block|{
case|case
literal|0
case|:
name|p0
operator|=
name|readVertex
argument_list|(
name|mciis
argument_list|,
name|maxSrcCoord
argument_list|,
name|maxSrcColor
argument_list|,
name|rangeX
argument_list|,
name|rangeY
argument_list|,
name|colRange
argument_list|,
name|matrix
argument_list|,
name|xform
argument_list|)
expr_stmt|;
name|flag
operator|=
call|(
name|byte
call|)
argument_list|(
name|mciis
operator|.
name|readBits
argument_list|(
name|bitsPerFlag
argument_list|)
operator|&
literal|3
argument_list|)
expr_stmt|;
if|if
condition|(
name|flag
operator|!=
literal|0
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"bad triangle: "
operator|+
name|flag
argument_list|)
expr_stmt|;
block|}
name|p1
operator|=
name|readVertex
argument_list|(
name|mciis
argument_list|,
name|maxSrcCoord
argument_list|,
name|maxSrcColor
argument_list|,
name|rangeX
argument_list|,
name|rangeY
argument_list|,
name|colRange
argument_list|,
name|matrix
argument_list|,
name|xform
argument_list|)
expr_stmt|;
name|mciis
operator|.
name|readBits
argument_list|(
name|bitsPerFlag
argument_list|)
expr_stmt|;
if|if
condition|(
name|flag
operator|!=
literal|0
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"bad triangle: "
operator|+
name|flag
argument_list|)
expr_stmt|;
block|}
name|p2
operator|=
name|readVertex
argument_list|(
name|mciis
argument_list|,
name|maxSrcCoord
argument_list|,
name|maxSrcColor
argument_list|,
name|rangeX
argument_list|,
name|rangeY
argument_list|,
name|colRange
argument_list|,
name|matrix
argument_list|,
name|xform
argument_list|)
expr_stmt|;
name|ps
operator|=
operator|new
name|Point2D
index|[]
block|{
name|p0
operator|.
name|point
block|,
name|p1
operator|.
name|point
block|,
name|p2
operator|.
name|point
block|}
expr_stmt|;
name|cs
operator|=
operator|new
name|float
index|[]
index|[]
block|{
name|p0
operator|.
name|color
block|,
name|p1
operator|.
name|color
block|,
name|p2
operator|.
name|color
block|}
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|ShadedTriangle
argument_list|(
name|ps
argument_list|,
name|cs
argument_list|)
argument_list|)
expr_stmt|;
name|flag
operator|=
call|(
name|byte
call|)
argument_list|(
name|mciis
operator|.
name|readBits
argument_list|(
name|bitsPerFlag
argument_list|)
operator|&
literal|3
argument_list|)
expr_stmt|;
break|break;
case|case
literal|1
case|:
case|case
literal|2
case|:
name|lastIndex
operator|=
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
expr_stmt|;
if|if
condition|(
name|lastIndex
operator|<
literal|0
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"broken data stream: "
operator|+
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ShadedTriangle
name|preTri
init|=
name|list
operator|.
name|get
argument_list|(
name|lastIndex
argument_list|)
decl_stmt|;
name|p2
operator|=
name|readVertex
argument_list|(
name|mciis
argument_list|,
name|maxSrcCoord
argument_list|,
name|maxSrcColor
argument_list|,
name|rangeX
argument_list|,
name|rangeY
argument_list|,
name|colRange
argument_list|,
name|matrix
argument_list|,
name|xform
argument_list|)
expr_stmt|;
name|ps
operator|=
operator|new
name|Point2D
index|[]
block|{
name|flag
operator|==
literal|1
condition|?
name|preTri
operator|.
name|corner
index|[
literal|1
index|]
else|:
name|preTri
operator|.
name|corner
index|[
literal|0
index|]
block|,
name|preTri
operator|.
name|corner
index|[
literal|2
index|]
block|,
name|p2
operator|.
name|point
block|}
expr_stmt|;
name|cs
operator|=
operator|new
name|float
index|[]
index|[]
block|{
name|flag
operator|==
literal|1
condition|?
name|preTri
operator|.
name|color
index|[
literal|1
index|]
else|:
name|preTri
operator|.
name|color
index|[
literal|0
index|]
block|,
name|preTri
operator|.
name|color
index|[
literal|2
index|]
block|,
name|p2
operator|.
name|color
block|}
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|ShadedTriangle
argument_list|(
name|ps
argument_list|,
name|cs
argument_list|)
argument_list|)
expr_stmt|;
name|flag
operator|=
call|(
name|byte
call|)
argument_list|(
name|mciis
operator|.
name|readBits
argument_list|(
name|bitsPerFlag
argument_list|)
operator|&
literal|3
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
name|LOG
operator|.
name|warn
argument_list|(
literal|"bad flag: "
operator|+
name|flag
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|EOFException
name|ex
parameter_list|)
block|{
name|eof
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|mciis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

