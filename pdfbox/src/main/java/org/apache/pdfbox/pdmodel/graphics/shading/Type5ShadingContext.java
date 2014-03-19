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
name|COSArray
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
name|COSName
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
comment|/**  * AWT PaintContext for Gouraud Triangle Lattice (Type 5) shading.  * @author Tilman Hausherr  */
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
comment|/**      * Constructor creates an instance to be used for fill operations.      * @param shading the shading type to be used      * @param cm the color model to be used      * @param xform transformation for user to device space      * @param ctm current transformation matrix      * @param pageHeight height of the current page      * @throws IOException if something went wrong      */
specifier|public
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
name|ctm
parameter_list|,
name|int
name|pageHeight
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
name|ctm
argument_list|,
name|pageHeight
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Type5ShadingContext"
argument_list|)
expr_stmt|;
name|bitsPerColorComponent
operator|=
name|shading
operator|.
name|getBitsPerComponent
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"bitsPerColorComponent: "
operator|+
name|bitsPerColorComponent
argument_list|)
expr_stmt|;
name|bitsPerCoordinate
operator|=
name|shading
operator|.
name|getBitsPerCoordinate
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
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
argument_list|)
expr_stmt|;
name|long
name|maxSrcCoord
init|=
operator|(
name|int
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
name|int
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"maxSrcCoord: "
operator|+
name|maxSrcCoord
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"maxSrcColor: "
operator|+
name|maxSrcColor
argument_list|)
expr_stmt|;
name|COSDictionary
name|cosDictionary
init|=
name|shading
operator|.
name|getCOSDictionary
argument_list|()
decl_stmt|;
name|COSStream
name|cosStream
init|=
operator|(
name|COSStream
operator|)
name|cosDictionary
decl_stmt|;
comment|//The Decode key specifies how
comment|//to decode coordinate and color component data into the ranges of values
comment|//appropriate for each. The ranges are specified as [xmin xmax ymin ymax c1,min,
comment|//c1,max,..., cn, min, cn,max].
comment|//
comment|// see p344
name|COSArray
name|decode
init|=
operator|(
name|COSArray
operator|)
name|cosDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DECODE
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"decode: "
operator|+
name|decode
argument_list|)
expr_stmt|;
name|PDRange
name|rangeX
init|=
name|shading
operator|.
name|getDecodeForParameter
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDRange
name|rangeY
init|=
name|shading
operator|.
name|getDecodeForParameter
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"rangeX: "
operator|+
name|rangeX
operator|.
name|getMin
argument_list|()
operator|+
literal|", "
operator|+
name|rangeX
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"rangeY: "
operator|+
name|rangeY
operator|.
name|getMin
argument_list|()
operator|+
literal|", "
operator|+
name|rangeY
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
name|PDRange
index|[]
name|colRangeTab
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
name|colRangeTab
index|[
name|i
index|]
operator|=
name|shading
operator|.
name|getDecodeForParameter
argument_list|(
literal|2
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"bitsPerCoordinate: "
operator|+
name|bitsPerCoordinate
argument_list|)
expr_stmt|;
comment|// get background values if available
name|COSArray
name|bg
init|=
name|shading
operator|.
name|getBackground
argument_list|()
decl_stmt|;
if|if
condition|(
name|bg
operator|!=
literal|null
condition|)
block|{
name|background
operator|=
name|bg
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
block|}
comment|//TODO missing: BBox, AntiAlias (p. 305 in 1.7 spec)
comment|// p318:
comment|//  reading in sequence from higher-order to lower-order bit positions
name|ImageInputStream
name|mciis
init|=
operator|new
name|MemoryCacheImageInputStream
argument_list|(
name|cosStream
operator|.
name|getFilteredStream
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|verticesPerRow
init|=
name|shading
operator|.
name|getVerticesPerRow
argument_list|()
decl_stmt|;
comment|//TODO check>=2
name|LOG
operator|.
name|debug
argument_list|(
literal|"verticesPerRow"
operator|+
name|verticesPerRow
argument_list|)
expr_stmt|;
try|try
block|{
name|ArrayList
argument_list|<
name|Vertex
argument_list|>
name|prevVertexRow
init|=
operator|new
name|ArrayList
argument_list|<
name|Vertex
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
comment|// read a vertex row
name|ArrayList
argument_list|<
name|Vertex
argument_list|>
name|vertexList
init|=
operator|new
name|ArrayList
argument_list|<
name|Vertex
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|row
init|=
literal|0
init|;
name|row
operator|<
name|verticesPerRow
condition|;
operator|++
name|row
control|)
block|{
name|vertexList
operator|.
name|add
argument_list|(
name|readVertex
argument_list|(
name|mciis
argument_list|,
operator|(
name|byte
operator|)
literal|0
argument_list|,
name|maxSrcCoord
argument_list|,
name|maxSrcColor
argument_list|,
name|rangeX
argument_list|,
name|rangeY
argument_list|,
name|colRangeTab
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|transformVertices
argument_list|(
name|vertexList
argument_list|,
name|ctm
argument_list|,
name|xform
argument_list|)
expr_stmt|;
comment|// create the triangles from two rows
if|if
condition|(
operator|!
name|prevVertexRow
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|vj
init|=
literal|0
init|;
name|vj
operator|<
name|vertexList
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|;
operator|++
name|vj
control|)
block|{
comment|// p.192,194 pdf spec 1.7
name|Vertex
name|vij
init|=
name|prevVertexRow
operator|.
name|get
argument_list|(
name|vj
argument_list|)
decl_stmt|;
comment|// v i,j
name|Vertex
name|vijplus1
init|=
name|prevVertexRow
operator|.
name|get
argument_list|(
name|vj
operator|+
literal|1
argument_list|)
decl_stmt|;
comment|// v i,j+1
name|Vertex
name|viplus1j
init|=
name|vertexList
operator|.
name|get
argument_list|(
name|vj
argument_list|)
decl_stmt|;
comment|// v i+1,j
name|Vertex
name|viplus1jplus1
init|=
name|vertexList
operator|.
name|get
argument_list|(
name|vj
operator|+
literal|1
argument_list|)
decl_stmt|;
comment|// v i+1,j+1
name|GouraudTriangle
name|g
init|=
operator|new
name|GouraudTriangle
argument_list|(
name|vij
operator|.
name|point
argument_list|,
name|vij
operator|.
name|color
argument_list|,
name|vijplus1
operator|.
name|point
argument_list|,
name|vijplus1
operator|.
name|color
argument_list|,
name|viplus1j
operator|.
name|point
argument_list|,
name|viplus1j
operator|.
name|color
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|g
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|triangleList
operator|.
name|add
argument_list|(
name|g
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"triangle is empty!"
argument_list|)
expr_stmt|;
block|}
name|g
operator|=
operator|new
name|GouraudTriangle
argument_list|(
name|vijplus1
operator|.
name|point
argument_list|,
name|vijplus1
operator|.
name|color
argument_list|,
name|viplus1j
operator|.
name|point
argument_list|,
name|viplus1j
operator|.
name|color
argument_list|,
name|viplus1jplus1
operator|.
name|point
argument_list|,
name|viplus1jplus1
operator|.
name|color
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|g
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|triangleList
operator|.
name|add
argument_list|(
name|g
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"triangle is empty!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|prevVertexRow
operator|=
name|vertexList
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|EOFException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"EOF"
argument_list|)
expr_stmt|;
block|}
name|mciis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dispose
parameter_list|()
block|{
name|super
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

