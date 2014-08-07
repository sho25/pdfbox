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
comment|/**  * AWT PaintContext for Gouraud Triangle Mesh (Type 4) shading.  * @author Tilman Hausherr  */
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
name|int
name|bitsPerFlag
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      *      * @param shading the shading type to be used      * @param cm the color model to be used      * @param xform transformation for user to device space      * @param ctm current transformation matrix      * @param pageHeight height of the current page      * @param dBounds device bounds      * @throws IOException if something went wrong      */
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
name|ctm
parameter_list|,
name|int
name|pageHeight
parameter_list|,
name|Rectangle
name|dBounds
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
argument_list|,
name|dBounds
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Type4ShadingContext"
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
name|long
name|maxSrcCoord
init|=
call|(
name|long
call|)
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
decl_stmt|;
name|long
name|maxSrcColor
init|=
call|(
name|long
call|)
argument_list|(
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
argument_list|)
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
name|bitsPerFlag
operator|=
name|shading
operator|.
name|getBitsPerFlag
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"bitsPerFlag: "
operator|+
name|bitsPerFlag
argument_list|)
expr_stmt|;
comment|//TODO handle cases where bitperflag isn't 8
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stream size: "
operator|+
name|cosStream
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|)
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
name|getUnfilteredStream
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|byte
name|flag
init|=
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
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"flag: "
operator|+
name|flag
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|flag
condition|)
block|{
case|case
literal|0
case|:
name|Vertex
name|v1
init|=
name|readVertex
argument_list|(
name|mciis
argument_list|,
name|flag
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
decl_stmt|;
name|Vertex
name|v2
init|=
name|readVertex
argument_list|(
name|mciis
argument_list|,
operator|(
name|byte
operator|)
name|mciis
operator|.
name|readBits
argument_list|(
name|bitsPerFlag
argument_list|)
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
decl_stmt|;
name|Vertex
name|v3
init|=
name|readVertex
argument_list|(
name|mciis
argument_list|,
operator|(
name|byte
operator|)
name|mciis
operator|.
name|readBits
argument_list|(
name|bitsPerFlag
argument_list|)
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
decl_stmt|;
comment|// add them after they're read, so that they are never added if there is a premature EOF
name|vertexList
operator|.
name|add
argument_list|(
name|v1
argument_list|)
expr_stmt|;
name|vertexList
operator|.
name|add
argument_list|(
name|v2
argument_list|)
expr_stmt|;
name|vertexList
operator|.
name|add
argument_list|(
name|v3
argument_list|)
expr_stmt|;
break|break;
case|case
literal|1
case|:
case|case
literal|2
case|:
name|vertexList
operator|.
name|add
argument_list|(
name|readVertex
argument_list|(
name|mciis
argument_list|,
name|flag
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"EOF"
argument_list|)
expr_stmt|;
if|if
condition|(
name|vertexList
operator|.
name|size
argument_list|()
operator|<
literal|3
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Incomplete mesh is ignored"
argument_list|)
expr_stmt|;
name|vertexList
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|vertexList
operator|.
name|size
argument_list|()
operator|>
literal|1
operator|&&
name|vertexList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|flag
operator|!=
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Mesh with incorrect start flag "
operator|+
name|vertexList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|flag
operator|+
literal|" is ignored"
argument_list|)
expr_stmt|;
name|vertexList
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|// check that there are 3 entries if there is a 0 flag
name|int
name|vi
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|vi
operator|<
name|vertexList
operator|.
name|size
argument_list|()
condition|)
block|{
if|if
condition|(
name|vertexList
operator|.
name|get
argument_list|(
name|vi
argument_list|)
operator|.
name|flag
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|vi
operator|+
literal|2
operator|>=
name|vertexList
operator|.
name|size
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Mesh with incomplete triangle"
argument_list|)
expr_stmt|;
comment|// remove rest
while|while
condition|(
name|vertexList
operator|.
name|size
argument_list|()
operator|>=
name|vi
operator|+
literal|1
condition|)
block|{
name|vertexList
operator|.
name|remove
argument_list|(
name|vi
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
name|vi
operator|+=
literal|3
expr_stmt|;
block|}
else|else
block|{
operator|++
name|vi
expr_stmt|;
block|}
block|}
break|break;
block|}
block|}
name|mciis
operator|.
name|close
argument_list|()
expr_stmt|;
name|transformVertices
argument_list|(
name|vertexList
argument_list|,
name|ctm
argument_list|,
name|xform
argument_list|)
expr_stmt|;
name|createTriangleList
argument_list|(
name|vertexList
argument_list|)
expr_stmt|;
block|}
comment|// create GouraudTriangle list from vertices, see p.316 of pdf spec 1.7.
specifier|private
name|void
name|createTriangleList
parameter_list|(
name|ArrayList
argument_list|<
name|Vertex
argument_list|>
name|vertexList
parameter_list|)
block|{
name|Point2D
name|a
init|=
literal|null
decl_stmt|,
name|b
init|=
literal|null
decl_stmt|,
name|c
init|=
literal|null
decl_stmt|;
name|float
index|[]
name|aColor
init|=
literal|null
decl_stmt|,
name|bColor
init|=
literal|null
decl_stmt|,
name|cColor
init|=
literal|null
decl_stmt|;
name|int
name|vi
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|vi
operator|<
name|vertexList
operator|.
name|size
argument_list|()
condition|)
block|{
name|Vertex
name|v
init|=
name|vertexList
operator|.
name|get
argument_list|(
name|vi
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|v
operator|.
name|flag
condition|)
block|{
case|case
literal|0
case|:
name|a
operator|=
name|v
operator|.
name|point
expr_stmt|;
name|aColor
operator|=
name|v
operator|.
name|color
expr_stmt|;
operator|++
name|vi
expr_stmt|;
name|v
operator|=
name|vertexList
operator|.
name|get
argument_list|(
name|vi
argument_list|)
expr_stmt|;
name|b
operator|=
name|v
operator|.
name|point
expr_stmt|;
name|bColor
operator|=
name|v
operator|.
name|color
expr_stmt|;
operator|++
name|vi
expr_stmt|;
name|v
operator|=
name|vertexList
operator|.
name|get
argument_list|(
name|vi
argument_list|)
expr_stmt|;
name|c
operator|=
name|v
operator|.
name|point
expr_stmt|;
name|cColor
operator|=
name|v
operator|.
name|color
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|a
operator|=
name|b
expr_stmt|;
name|aColor
operator|=
name|bColor
expr_stmt|;
name|b
operator|=
name|c
expr_stmt|;
name|bColor
operator|=
name|cColor
expr_stmt|;
name|v
operator|=
name|vertexList
operator|.
name|get
argument_list|(
name|vi
argument_list|)
expr_stmt|;
name|c
operator|=
name|v
operator|.
name|point
expr_stmt|;
name|cColor
operator|=
name|v
operator|.
name|color
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|b
operator|=
name|c
expr_stmt|;
name|bColor
operator|=
name|cColor
expr_stmt|;
name|v
operator|=
name|vertexList
operator|.
name|get
argument_list|(
name|vi
argument_list|)
expr_stmt|;
name|c
operator|=
name|v
operator|.
name|point
expr_stmt|;
name|cColor
operator|=
name|v
operator|.
name|color
expr_stmt|;
break|break;
default|default:
break|break;
block|}
operator|++
name|vi
expr_stmt|;
name|GouraudTriangle
name|g
init|=
operator|new
name|GouraudTriangle
argument_list|(
name|a
argument_list|,
name|aColor
argument_list|,
name|b
argument_list|,
name|bColor
argument_list|,
name|c
argument_list|,
name|cColor
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
block|}
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

