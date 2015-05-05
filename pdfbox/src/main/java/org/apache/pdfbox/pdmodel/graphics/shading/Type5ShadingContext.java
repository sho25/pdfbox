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
comment|/**  * AWT PaintContext for Gouraud Triangle Lattice (Type 5) shading.  *  * @author Tilman Hausherr  * @author Shaola Ren  */
end_comment

begin_class
class|class
name|Type5ShadingContext
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
name|Type5ShadingContext
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      *      * @param shading the shading type to be used      * @param cm the color model to be used      * @param xform transformation for user to device space      * @param matrix the pattern matrix concatenated with that of the parent content stream      * @throws IOException if something went wrong      */
name|Type5ShadingContext
parameter_list|(
name|PDShadingType5
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
literal|"Type5ShadingContext"
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
name|PDShadingType5
name|latticeTriangleShadingType
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
name|cosDictionary
init|=
name|latticeTriangleShadingType
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|PDRange
name|rangeX
init|=
name|latticeTriangleShadingType
operator|.
name|getDecodeForParameter
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDRange
name|rangeY
init|=
name|latticeTriangleShadingType
operator|.
name|getDecodeForParameter
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|int
name|numPerRow
init|=
name|latticeTriangleShadingType
operator|.
name|getVerticesPerRow
argument_list|()
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
name|latticeTriangleShadingType
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
name|Vertex
argument_list|>
name|vlist
init|=
operator|new
name|ArrayList
argument_list|<
name|Vertex
argument_list|>
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
name|cosStream
init|=
operator|(
name|COSStream
operator|)
name|cosDictionary
decl_stmt|;
name|ImageInputStream
name|mciis
init|=
operator|new
name|MemoryCacheImageInputStream
argument_list|(
name|cosStream
operator|.
name|getUnfilteredStream
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|Vertex
name|p
decl_stmt|;
try|try
block|{
name|p
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
name|vlist
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EOFException
name|ex
parameter_list|)
block|{
break|break;
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
name|int
name|sz
init|=
name|vlist
operator|.
name|size
argument_list|()
decl_stmt|,
name|rowNum
init|=
name|sz
operator|/
name|numPerRow
decl_stmt|;
name|Vertex
index|[]
index|[]
name|latticeArray
init|=
operator|new
name|Vertex
index|[
name|rowNum
index|]
index|[
name|numPerRow
index|]
decl_stmt|;
name|List
argument_list|<
name|ShadedTriangle
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|ShadedTriangle
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|rowNum
operator|<
literal|2
condition|)
block|{
comment|// must have at least two rows; if not, return empty list
return|return
name|list
return|;
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
name|rowNum
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|numPerRow
condition|;
name|j
operator|++
control|)
block|{
name|latticeArray
index|[
name|i
index|]
index|[
name|j
index|]
operator|=
name|vlist
operator|.
name|get
argument_list|(
name|i
operator|*
name|numPerRow
operator|+
name|j
argument_list|)
expr_stmt|;
block|}
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
name|rowNum
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|numPerRow
operator|-
literal|1
condition|;
name|j
operator|++
control|)
block|{
name|Point2D
index|[]
name|ps
init|=
operator|new
name|Point2D
index|[]
block|{
name|latticeArray
index|[
name|i
index|]
index|[
name|j
index|]
operator|.
name|point
block|,
name|latticeArray
index|[
name|i
index|]
index|[
name|j
operator|+
literal|1
index|]
operator|.
name|point
block|,
name|latticeArray
index|[
name|i
operator|+
literal|1
index|]
index|[
name|j
index|]
operator|.
name|point
block|}
decl_stmt|;
name|float
index|[]
index|[]
name|cs
init|=
operator|new
name|float
index|[]
index|[]
block|{
name|latticeArray
index|[
name|i
index|]
index|[
name|j
index|]
operator|.
name|color
block|,
name|latticeArray
index|[
name|i
index|]
index|[
name|j
operator|+
literal|1
index|]
operator|.
name|color
block|,
name|latticeArray
index|[
name|i
operator|+
literal|1
index|]
index|[
name|j
index|]
operator|.
name|color
block|}
decl_stmt|;
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
name|ps
operator|=
operator|new
name|Point2D
index|[]
block|{
name|latticeArray
index|[
name|i
index|]
index|[
name|j
operator|+
literal|1
index|]
operator|.
name|point
block|,
name|latticeArray
index|[
name|i
operator|+
literal|1
index|]
index|[
name|j
index|]
operator|.
name|point
block|,
name|latticeArray
index|[
name|i
operator|+
literal|1
index|]
index|[
name|j
operator|+
literal|1
index|]
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
name|latticeArray
index|[
name|i
index|]
index|[
name|j
operator|+
literal|1
index|]
operator|.
name|color
block|,
name|latticeArray
index|[
name|i
operator|+
literal|1
index|]
index|[
name|j
index|]
operator|.
name|color
block|,
name|latticeArray
index|[
name|i
operator|+
literal|1
index|]
index|[
name|j
operator|+
literal|1
index|]
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
block|}
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

