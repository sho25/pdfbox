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
name|color
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
name|pdmodel
operator|.
name|graphics
operator|.
name|pattern
operator|.
name|PDAbstractPattern
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
name|pattern
operator|.
name|PDShadingPattern
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
name|pattern
operator|.
name|PDTilingPattern
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
name|pattern
operator|.
name|TilingPaint
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
name|shading
operator|.
name|PDShading
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
name|rendering
operator|.
name|PDFRenderer
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * A Pattern color space is either a Tiling pattern or a Shading pattern.  * @author John Hewson  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDPattern
extends|extends
name|PDSpecialColorSpace
block|{
comment|/**      * log instance.      */
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
name|PDPattern
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|PDAbstractPattern
argument_list|>
name|patterns
decl_stmt|;
specifier|private
name|PDColorSpace
name|underlyingColorSpace
decl_stmt|;
comment|/**      * Creates a new pattern color space.      */
specifier|public
name|PDPattern
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDAbstractPattern
argument_list|>
name|patterns
parameter_list|)
block|{
name|this
operator|.
name|patterns
operator|=
name|patterns
expr_stmt|;
block|}
comment|/**      * Creates a new uncolored tiling pattern color space.      */
specifier|public
name|PDPattern
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDAbstractPattern
argument_list|>
name|patterns
parameter_list|,
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
name|this
operator|.
name|patterns
operator|=
name|patterns
expr_stmt|;
name|this
operator|.
name|underlyingColorSpace
operator|=
name|colorSpace
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|COSName
operator|.
name|PATTERN
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getNumberOfComponents
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|float
index|[]
name|getDefaultDecode
parameter_list|(
name|int
name|bitsPerComponent
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|PDColor
name|getInitialColor
parameter_list|()
block|{
return|return
name|PDColor
operator|.
name|EMPTY_PATTERN
return|;
block|}
annotation|@
name|Override
specifier|public
name|float
index|[]
name|toRGB
parameter_list|(
name|float
index|[]
name|value
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|BufferedImage
name|toRGBImage
parameter_list|(
name|WritableRaster
name|raster
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Paint
name|toPaint
parameter_list|(
name|PDFRenderer
name|renderer
parameter_list|,
name|PDColor
name|color
parameter_list|,
name|Matrix
name|subStreamMatrix
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|int
name|pageHeight
parameter_list|)
throws|throws
name|IOException
block|{
name|PDAbstractPattern
name|pattern
init|=
name|getPattern
argument_list|(
name|color
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|instanceof
name|PDTilingPattern
condition|)
block|{
name|PDTilingPattern
name|tilingPattern
init|=
operator|(
name|PDTilingPattern
operator|)
name|pattern
decl_stmt|;
if|if
condition|(
name|tilingPattern
operator|.
name|getPaintType
argument_list|()
operator|==
name|PDTilingPattern
operator|.
name|PAINT_COLORED
condition|)
block|{
comment|// colored tiling pattern
return|return
operator|new
name|TilingPaint
argument_list|(
name|renderer
argument_list|,
name|tilingPattern
argument_list|,
name|subStreamMatrix
argument_list|,
name|xform
argument_list|)
return|;
block|}
else|else
block|{
comment|// uncolored tiling pattern
return|return
operator|new
name|TilingPaint
argument_list|(
name|renderer
argument_list|,
name|tilingPattern
argument_list|,
name|underlyingColorSpace
argument_list|,
name|color
argument_list|,
name|subStreamMatrix
argument_list|,
name|xform
argument_list|)
return|;
block|}
block|}
else|else
block|{
name|PDShadingPattern
name|shadingPattern
init|=
operator|(
name|PDShadingPattern
operator|)
name|pattern
decl_stmt|;
name|PDShading
name|shading
init|=
name|shadingPattern
operator|.
name|getShading
argument_list|()
decl_stmt|;
if|if
condition|(
name|shading
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"shadingPattern ist null, will be filled with transparency"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Color
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
return|;
block|}
return|return
name|shading
operator|.
name|toPaint
argument_list|(
name|shadingPattern
operator|.
name|getMatrix
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**      * Returns the pattern for the given color.      * @param color color containing a pattern name      * @return pattern for the given color      */
specifier|public
specifier|final
name|PDAbstractPattern
name|getPattern
parameter_list|(
name|PDColor
name|color
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|patterns
operator|.
name|containsKey
argument_list|(
name|color
operator|.
name|getPatternName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"pattern "
operator|+
name|color
operator|.
name|getPatternName
argument_list|()
operator|+
literal|" was not found"
argument_list|)
throw|;
block|}
return|return
name|patterns
operator|.
name|get
argument_list|(
name|color
operator|.
name|getPatternName
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Pattern"
return|;
block|}
block|}
end_class

end_unit

