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
name|rendering
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Paint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|PaintContext
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
name|RenderingHints
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
name|Rectangle2D
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
name|BufferedImage
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
name|awt
operator|.
name|image
operator|.
name|Raster
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
name|WritableRaster
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
name|function
operator|.
name|PDFunction
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
name|function
operator|.
name|PDFunctionTypeIdentity
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
name|graphics
operator|.
name|color
operator|.
name|PDColor
import|;
end_import

begin_comment
comment|/**  * A Paint which applies a soft mask to an underlying Paint.  *   * @author Petr Slaby  * @author John Hewson  * @author Matthias Bläsing  * @author Tilman Hausherr  */
end_comment

begin_class
class|class
name|SoftMask
implements|implements
name|Paint
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
name|SoftMask
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ColorModel
name|ARGB_COLOR_MODEL
init|=
operator|new
name|BufferedImage
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_ARGB
argument_list|)
operator|.
name|getColorModel
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Paint
name|paint
decl_stmt|;
specifier|private
specifier|final
name|BufferedImage
name|mask
decl_stmt|;
specifier|private
specifier|final
name|Rectangle2D
name|bboxDevice
decl_stmt|;
specifier|private
name|int
name|bc
init|=
literal|0
decl_stmt|;
specifier|private
specifier|final
name|PDFunction
name|transferFunction
decl_stmt|;
comment|/**      * Creates a new soft mask paint.      *      * @param paint underlying paint.      * @param mask soft mask      * @param bboxDevice bbox of the soft mask in the underlying Graphics2D device space      * @param backdropColor the color to be used outside the transparency group’s bounding box; if      * null, black will be used.      * @param transferFunction the transfer function, may be null.      */
name|SoftMask
parameter_list|(
name|Paint
name|paint
parameter_list|,
name|BufferedImage
name|mask
parameter_list|,
name|Rectangle2D
name|bboxDevice
parameter_list|,
name|PDColor
name|backdropColor
parameter_list|,
name|PDFunction
name|transferFunction
parameter_list|)
block|{
name|this
operator|.
name|paint
operator|=
name|paint
expr_stmt|;
name|this
operator|.
name|mask
operator|=
name|mask
expr_stmt|;
name|this
operator|.
name|bboxDevice
operator|=
name|bboxDevice
expr_stmt|;
if|if
condition|(
name|transferFunction
operator|instanceof
name|PDFunctionTypeIdentity
condition|)
block|{
name|this
operator|.
name|transferFunction
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|transferFunction
operator|=
name|transferFunction
expr_stmt|;
block|}
if|if
condition|(
name|backdropColor
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Color
name|color
init|=
operator|new
name|Color
argument_list|(
name|backdropColor
operator|.
name|toRGB
argument_list|()
argument_list|)
decl_stmt|;
comment|// http://stackoverflow.com/a/25463098/535646
name|bc
operator|=
operator|(
literal|299
operator|*
name|color
operator|.
name|getRed
argument_list|()
operator|+
literal|587
operator|*
name|color
operator|.
name|getGreen
argument_list|()
operator|+
literal|114
operator|*
name|color
operator|.
name|getBlue
argument_list|()
operator|)
operator|/
literal|1000
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// keep default
name|LOG
operator|.
name|debug
argument_list|(
literal|"Couldn't convert backdropColor to RGB - keeping default"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|PaintContext
name|createContext
parameter_list|(
name|ColorModel
name|cm
parameter_list|,
name|Rectangle
name|deviceBounds
parameter_list|,
name|Rectangle2D
name|userBounds
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|RenderingHints
name|hints
parameter_list|)
block|{
name|PaintContext
name|ctx
init|=
name|paint
operator|.
name|createContext
argument_list|(
name|cm
argument_list|,
name|deviceBounds
argument_list|,
name|userBounds
argument_list|,
name|xform
argument_list|,
name|hints
argument_list|)
decl_stmt|;
return|return
operator|new
name|SoftPaintContext
argument_list|(
name|ctx
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getTransparency
parameter_list|()
block|{
return|return
name|TRANSLUCENT
return|;
block|}
specifier|private
class|class
name|SoftPaintContext
implements|implements
name|PaintContext
block|{
specifier|private
specifier|final
name|PaintContext
name|context
decl_stmt|;
name|SoftPaintContext
parameter_list|(
name|PaintContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ColorModel
name|getColorModel
parameter_list|()
block|{
return|return
name|ARGB_COLOR_MODEL
return|;
block|}
annotation|@
name|Override
specifier|public
name|Raster
name|getRaster
parameter_list|(
name|int
name|x1
parameter_list|,
name|int
name|y1
parameter_list|,
name|int
name|w
parameter_list|,
name|int
name|h
parameter_list|)
block|{
name|Raster
name|raster
init|=
name|context
operator|.
name|getRaster
argument_list|(
name|x1
argument_list|,
name|y1
argument_list|,
name|w
argument_list|,
name|h
argument_list|)
decl_stmt|;
name|ColorModel
name|rasterCM
init|=
name|context
operator|.
name|getColorModel
argument_list|()
decl_stmt|;
name|float
index|[]
name|input
init|=
literal|null
decl_stmt|;
name|Float
index|[]
name|map
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|transferFunction
operator|!=
literal|null
condition|)
block|{
name|map
operator|=
operator|new
name|Float
index|[
literal|256
index|]
expr_stmt|;
name|input
operator|=
operator|new
name|float
index|[
literal|1
index|]
expr_stmt|;
block|}
comment|// buffer
name|WritableRaster
name|output
init|=
name|getColorModel
argument_list|()
operator|.
name|createCompatibleWritableRaster
argument_list|(
name|w
argument_list|,
name|h
argument_list|)
decl_stmt|;
comment|// the soft mask has its own bbox
name|x1
operator|=
name|x1
operator|-
operator|(
name|int
operator|)
name|bboxDevice
operator|.
name|getX
argument_list|()
expr_stmt|;
name|y1
operator|=
name|y1
operator|-
operator|(
name|int
operator|)
name|bboxDevice
operator|.
name|getY
argument_list|()
expr_stmt|;
name|int
index|[]
name|gray
init|=
operator|new
name|int
index|[
literal|4
index|]
decl_stmt|;
name|Object
name|pixelInput
init|=
literal|null
decl_stmt|;
name|int
index|[]
name|pixelOutput
init|=
operator|new
name|int
index|[
literal|4
index|]
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
name|h
condition|;
name|y
operator|++
control|)
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
name|w
condition|;
name|x
operator|++
control|)
block|{
name|pixelInput
operator|=
name|raster
operator|.
name|getDataElements
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|pixelInput
argument_list|)
expr_stmt|;
name|pixelOutput
index|[
literal|0
index|]
operator|=
name|rasterCM
operator|.
name|getRed
argument_list|(
name|pixelInput
argument_list|)
expr_stmt|;
name|pixelOutput
index|[
literal|1
index|]
operator|=
name|rasterCM
operator|.
name|getGreen
argument_list|(
name|pixelInput
argument_list|)
expr_stmt|;
name|pixelOutput
index|[
literal|2
index|]
operator|=
name|rasterCM
operator|.
name|getBlue
argument_list|(
name|pixelInput
argument_list|)
expr_stmt|;
name|pixelOutput
index|[
literal|3
index|]
operator|=
name|rasterCM
operator|.
name|getAlpha
argument_list|(
name|pixelInput
argument_list|)
expr_stmt|;
comment|// get the alpha value from the gray mask, if within mask bounds
name|gray
index|[
literal|0
index|]
operator|=
literal|0
expr_stmt|;
if|if
condition|(
name|x1
operator|+
name|x
operator|>=
literal|0
operator|&&
name|y1
operator|+
name|y
operator|>=
literal|0
operator|&&
name|x1
operator|+
name|x
operator|<
name|mask
operator|.
name|getWidth
argument_list|()
operator|&&
name|y1
operator|+
name|y
operator|<
name|mask
operator|.
name|getHeight
argument_list|()
condition|)
block|{
name|mask
operator|.
name|getRaster
argument_list|()
operator|.
name|getPixel
argument_list|(
name|x1
operator|+
name|x
argument_list|,
name|y1
operator|+
name|y
argument_list|,
name|gray
argument_list|)
expr_stmt|;
name|int
name|g
init|=
name|gray
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|transferFunction
operator|!=
literal|null
condition|)
block|{
comment|// apply transfer function
try|try
block|{
if|if
condition|(
name|map
index|[
name|g
index|]
operator|!=
literal|null
condition|)
block|{
comment|// was calculated before
name|pixelOutput
index|[
literal|3
index|]
operator|=
name|Math
operator|.
name|round
argument_list|(
name|pixelOutput
index|[
literal|3
index|]
operator|*
name|map
index|[
name|g
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// calculate and store in map
name|input
index|[
literal|0
index|]
operator|=
name|g
operator|/
literal|255f
expr_stmt|;
name|float
name|f
init|=
name|transferFunction
operator|.
name|eval
argument_list|(
name|input
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
name|map
index|[
name|g
index|]
operator|=
name|f
expr_stmt|;
name|pixelOutput
index|[
literal|3
index|]
operator|=
name|Math
operator|.
name|round
argument_list|(
name|pixelOutput
index|[
literal|3
index|]
operator|*
name|f
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// ignore exception, treat as outside
name|LOG
operator|.
name|debug
argument_list|(
literal|"Couldn't apply transferFunction - treating as outside"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
name|pixelOutput
index|[
literal|3
index|]
operator|=
name|Math
operator|.
name|round
argument_list|(
name|pixelOutput
index|[
literal|3
index|]
operator|*
operator|(
name|bc
operator|/
literal|255f
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|pixelOutput
index|[
literal|3
index|]
operator|=
name|Math
operator|.
name|round
argument_list|(
name|pixelOutput
index|[
literal|3
index|]
operator|*
operator|(
name|g
operator|/
literal|255f
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|pixelOutput
index|[
literal|3
index|]
operator|=
name|Math
operator|.
name|round
argument_list|(
name|pixelOutput
index|[
literal|3
index|]
operator|*
operator|(
name|bc
operator|/
literal|255f
operator|)
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|setPixel
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|pixelOutput
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|output
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|dispose
parameter_list|()
block|{         }
block|}
block|}
end_class

end_unit

