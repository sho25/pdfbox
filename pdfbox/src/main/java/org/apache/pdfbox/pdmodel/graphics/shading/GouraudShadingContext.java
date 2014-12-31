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
name|Point
import|;
end_import

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
name|HashMap
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
name|Map
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
comment|/**  * Shades Gouraud triangles for Type4ShadingContext and Type5ShadingContext.  *  * @author Andreas Lehmkühler  * @author Tilman Hausherr  * @author Shaola Ren  */
end_comment

begin_class
specifier|abstract
class|class
name|GouraudShadingContext
extends|extends
name|TriangleBasedShadingContext
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
name|GouraudShadingContext
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * triangle list.      */
specifier|protected
name|List
argument_list|<
name|ShadedTriangle
argument_list|>
name|triangleList
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      *      * @param shading the shading type to be used      * @param colorModel the color model to be used      * @param xform transformation for user to device space      * @param matrix the pattern matrix concatenated with that of the parent content stream      * @throws IOException if something went wrong      */
specifier|protected
name|GouraudShadingContext
parameter_list|(
name|PDShading
name|shading
parameter_list|,
name|ColorModel
name|colorModel
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
name|colorModel
argument_list|,
name|xform
argument_list|,
name|matrix
argument_list|,
name|deviceBounds
argument_list|)
expr_stmt|;
name|triangleList
operator|=
operator|new
name|ArrayList
argument_list|<
name|ShadedTriangle
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Read a vertex from the bit input stream performs interpolations.      *      * @param input bit input stream      * @param maxSrcCoord max value for source coordinate (2^bits-1)      * @param maxSrcColor max value for source color (2^bits-1)      * @param rangeX dest range for X      * @param rangeY dest range for Y      * @param colRangeTab dest range array for colors      * @param matrix the pattern matrix concatenated with that of the parent content stream      * @return a new vertex with the flag and the interpolated values      * @throws IOException if something went wrong      */
specifier|protected
name|Vertex
name|readVertex
parameter_list|(
name|ImageInputStream
name|input
parameter_list|,
name|long
name|maxSrcCoord
parameter_list|,
name|long
name|maxSrcColor
parameter_list|,
name|PDRange
name|rangeX
parameter_list|,
name|PDRange
name|rangeY
parameter_list|,
name|PDRange
index|[]
name|colRangeTab
parameter_list|,
name|Matrix
name|matrix
parameter_list|,
name|AffineTransform
name|xform
parameter_list|)
throws|throws
name|IOException
block|{
name|float
index|[]
name|colorComponentTab
init|=
operator|new
name|float
index|[
name|numberOfColorComponents
index|]
decl_stmt|;
name|long
name|x
init|=
name|input
operator|.
name|readBits
argument_list|(
name|bitsPerCoordinate
argument_list|)
decl_stmt|;
name|long
name|y
init|=
name|input
operator|.
name|readBits
argument_list|(
name|bitsPerCoordinate
argument_list|)
decl_stmt|;
name|float
name|dstX
init|=
name|interpolate
argument_list|(
name|x
argument_list|,
name|maxSrcCoord
argument_list|,
name|rangeX
operator|.
name|getMin
argument_list|()
argument_list|,
name|rangeX
operator|.
name|getMax
argument_list|()
argument_list|)
decl_stmt|;
name|float
name|dstY
init|=
name|interpolate
argument_list|(
name|y
argument_list|,
name|maxSrcCoord
argument_list|,
name|rangeY
operator|.
name|getMin
argument_list|()
argument_list|,
name|rangeY
operator|.
name|getMax
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"coord: "
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"[%06X,%06X] -> [%f,%f]"
argument_list|,
name|x
argument_list|,
name|y
argument_list|,
name|dstX
argument_list|,
name|dstY
argument_list|)
argument_list|)
expr_stmt|;
name|Point2D
name|p
init|=
name|matrix
operator|.
name|transformPoint
argument_list|(
name|dstX
argument_list|,
name|dstY
argument_list|)
decl_stmt|;
name|xform
operator|.
name|transform
argument_list|(
name|p
argument_list|,
name|p
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<
name|numberOfColorComponents
condition|;
operator|++
name|n
control|)
block|{
name|int
name|color
init|=
operator|(
name|int
operator|)
name|input
operator|.
name|readBits
argument_list|(
name|bitsPerColorComponent
argument_list|)
decl_stmt|;
name|colorComponentTab
index|[
name|n
index|]
operator|=
name|interpolate
argument_list|(
name|color
argument_list|,
name|maxSrcColor
argument_list|,
name|colRangeTab
index|[
name|n
index|]
operator|.
name|getMin
argument_list|()
argument_list|,
name|colRangeTab
index|[
name|n
index|]
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"color["
operator|+
name|n
operator|+
literal|"]: "
operator|+
name|color
operator|+
literal|"/"
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"%02x"
argument_list|,
name|color
argument_list|)
operator|+
literal|"-> color["
operator|+
name|n
operator|+
literal|"]: "
operator|+
name|colorComponentTab
index|[
name|n
index|]
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|Vertex
argument_list|(
name|p
argument_list|,
name|colorComponentTab
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
name|calcPixelTable
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|Point
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|super
operator|.
name|calcPixelTable
argument_list|(
name|triangleList
argument_list|,
name|map
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dispose
parameter_list|()
block|{
name|triangleList
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
comment|/**      * Calculate the interpolation, see p.345 pdf spec 1.7.      *      * @param src src value      * @param srcMax max src value (2^bits-1)      * @param dstMin min dst value      * @param dstMax max dst value      * @return interpolated value      */
specifier|private
name|float
name|interpolate
parameter_list|(
name|float
name|src
parameter_list|,
name|long
name|srcMax
parameter_list|,
name|float
name|dstMin
parameter_list|,
name|float
name|dstMax
parameter_list|)
block|{
return|return
name|dstMin
operator|+
operator|(
name|src
operator|*
operator|(
name|dstMax
operator|-
name|dstMin
operator|)
operator|/
name|srcMax
operator|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|isDataEmpty
parameter_list|()
block|{
return|return
name|triangleList
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

