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

begin_comment
comment|/**  * A Paint which applies a soft mask to an underlying Paint.  *   * @author John Hewson  */
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
comment|/**      * Creates a new soft mask paint.      *      * @param paint underlying paint.      * @param mask soft mask      * @param bboxDevice bbox of the soft mask in the underlying Graphics2D device space      */
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
name|cm
argument_list|,
name|deviceBounds
argument_list|,
name|userBounds
argument_list|,
name|xform
argument_list|,
name|hints
argument_list|,
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
parameter_list|,
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
name|WritableRaster
name|raster
init|=
operator|(
name|WritableRaster
operator|)
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
name|gray
index|[
literal|0
index|]
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

