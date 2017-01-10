begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Transparency
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ColorSpace
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
name|ComponentColorModel
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
name|DataBuffer
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
name|pdmodel
operator|.
name|common
operator|.
name|PDRectangle
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
name|PDColorSpace
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
comment|/**  * A base class to handle what is common to all shading types.  *  * @author Shaola Ren  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ShadingContext
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
name|ShadingContext
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|PDRectangle
name|bboxRect
decl_stmt|;
specifier|protected
name|float
name|minBBoxX
decl_stmt|,
name|minBBoxY
decl_stmt|,
name|maxBBoxX
decl_stmt|,
name|maxBBoxY
decl_stmt|;
specifier|private
name|float
index|[]
name|background
decl_stmt|;
specifier|private
name|int
name|rgbBackground
decl_stmt|;
specifier|private
specifier|final
name|PDShading
name|shading
decl_stmt|;
specifier|private
name|ColorModel
name|outputColorModel
decl_stmt|;
specifier|private
name|PDColorSpace
name|shadingColorSpace
decl_stmt|;
comment|/**      * Constructor.      *      * @param shading the shading type to be used      * @param cm the color model to be used      * @param xform transformation for user to device space      * @param matrix the pattern matrix concatenated with that of the parent content stream      * @throws java.io.IOException if there is an error getting the color space      * or doing background color conversion.      */
specifier|public
name|ShadingContext
parameter_list|(
name|PDShading
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
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|shading
operator|=
name|shading
expr_stmt|;
name|shadingColorSpace
operator|=
name|shading
operator|.
name|getColorSpace
argument_list|()
expr_stmt|;
comment|// create the output color model using RGB+alpha as color space
name|ColorSpace
name|outputCS
init|=
name|ColorSpace
operator|.
name|getInstance
argument_list|(
name|ColorSpace
operator|.
name|CS_sRGB
argument_list|)
decl_stmt|;
name|outputColorModel
operator|=
operator|new
name|ComponentColorModel
argument_list|(
name|outputCS
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|,
name|Transparency
operator|.
name|TRANSLUCENT
argument_list|,
name|DataBuffer
operator|.
name|TYPE_BYTE
argument_list|)
expr_stmt|;
name|bboxRect
operator|=
name|shading
operator|.
name|getBBox
argument_list|()
expr_stmt|;
comment|// all bbox handling will be removed in further commit
name|bboxRect
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|bboxRect
operator|!=
literal|null
condition|)
block|{
name|transformBBox
argument_list|(
name|matrix
argument_list|,
name|xform
argument_list|)
expr_stmt|;
block|}
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
name|rgbBackground
operator|=
name|convertToRGB
argument_list|(
name|background
argument_list|)
expr_stmt|;
block|}
block|}
name|PDColorSpace
name|getShadingColorSpace
parameter_list|()
block|{
return|return
name|shadingColorSpace
return|;
block|}
name|PDShading
name|getShading
parameter_list|()
block|{
return|return
name|shading
return|;
block|}
name|float
index|[]
name|getBackground
parameter_list|()
block|{
return|return
name|background
return|;
block|}
name|int
name|getRgbBackground
parameter_list|()
block|{
return|return
name|rgbBackground
return|;
block|}
specifier|private
name|void
name|transformBBox
parameter_list|(
name|Matrix
name|matrix
parameter_list|,
name|AffineTransform
name|xform
parameter_list|)
block|{
name|float
index|[]
name|bboxTab
init|=
operator|new
name|float
index|[
literal|4
index|]
decl_stmt|;
name|bboxTab
index|[
literal|0
index|]
operator|=
name|bboxRect
operator|.
name|getLowerLeftX
argument_list|()
expr_stmt|;
name|bboxTab
index|[
literal|1
index|]
operator|=
name|bboxRect
operator|.
name|getLowerLeftY
argument_list|()
expr_stmt|;
name|bboxTab
index|[
literal|2
index|]
operator|=
name|bboxRect
operator|.
name|getUpperRightX
argument_list|()
expr_stmt|;
name|bboxTab
index|[
literal|3
index|]
operator|=
name|bboxRect
operator|.
name|getUpperRightY
argument_list|()
expr_stmt|;
comment|// transform the coords using the given matrix
name|matrix
operator|.
name|createAffineTransform
argument_list|()
operator|.
name|transform
argument_list|(
name|bboxTab
argument_list|,
literal|0
argument_list|,
name|bboxTab
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|xform
operator|.
name|transform
argument_list|(
name|bboxTab
argument_list|,
literal|0
argument_list|,
name|bboxTab
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|minBBoxX
operator|=
name|Math
operator|.
name|min
argument_list|(
name|bboxTab
index|[
literal|0
index|]
argument_list|,
name|bboxTab
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|minBBoxY
operator|=
name|Math
operator|.
name|min
argument_list|(
name|bboxTab
index|[
literal|1
index|]
argument_list|,
name|bboxTab
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|maxBBoxX
operator|=
name|Math
operator|.
name|max
argument_list|(
name|bboxTab
index|[
literal|0
index|]
argument_list|,
name|bboxTab
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|maxBBoxY
operator|=
name|Math
operator|.
name|max
argument_list|(
name|bboxTab
index|[
literal|1
index|]
argument_list|,
name|bboxTab
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|minBBoxX
operator|>=
name|maxBBoxX
operator|||
name|minBBoxY
operator|>=
name|maxBBoxY
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"empty BBox is ignored"
argument_list|)
expr_stmt|;
name|bboxRect
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Convert color values from shading colorspace to RGB color values encoded      * into an integer.      *      * @param values color values in shading colorspace.      * @return RGB values encoded in an integer.      * @throws java.io.IOException if the color conversion fails.      */
specifier|final
name|int
name|convertToRGB
parameter_list|(
name|float
index|[]
name|values
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|normRGBValues
decl_stmt|;
name|float
index|[]
name|rgbValues
init|=
name|shadingColorSpace
operator|.
name|toRGB
argument_list|(
name|values
argument_list|)
decl_stmt|;
name|normRGBValues
operator|=
call|(
name|int
call|)
argument_list|(
name|rgbValues
index|[
literal|0
index|]
operator|*
literal|255
argument_list|)
expr_stmt|;
name|normRGBValues
operator||=
call|(
name|int
call|)
argument_list|(
name|rgbValues
index|[
literal|1
index|]
operator|*
literal|255
argument_list|)
operator|<<
literal|8
expr_stmt|;
name|normRGBValues
operator||=
call|(
name|int
call|)
argument_list|(
name|rgbValues
index|[
literal|2
index|]
operator|*
literal|255
argument_list|)
operator|<<
literal|16
expr_stmt|;
return|return
name|normRGBValues
return|;
block|}
name|ColorModel
name|getColorModel
parameter_list|()
block|{
return|return
name|outputColorModel
return|;
block|}
name|void
name|dispose
parameter_list|()
block|{
name|outputColorModel
operator|=
literal|null
expr_stmt|;
name|shadingColorSpace
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

