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
comment|/**  * AWT PaintContext for coons patch meshes (type 6) shading.  * This was done as part of GSoC2014, Tilman Hausherr is the mentor.  * @author Shaola Ren  */
end_comment

begin_class
class|class
name|Type6ShadingContext
extends|extends
name|PatchMeshesShadingContext
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
name|Type6ShadingContext
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor creates an instance to be used for fill operations.      * @param shading the shading type to be used      * @param colorModel the color model to be used      * @param xform transformation for user to device space      * @param ctm current transformation matrix      * @param pageHeight height of the current page      * @param dBounds device bounds      * @throws IOException if something went wrong      */
specifier|public
name|Type6ShadingContext
parameter_list|(
name|PDShadingType6
name|shading
parameter_list|,
name|ColorModel
name|colorModel
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
name|colorModel
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
name|bitsPerColorComponent
operator|=
name|shading
operator|.
name|getBitsPerComponent
argument_list|()
expr_stmt|;
name|bitsPerCoordinate
operator|=
name|shading
operator|.
name|getBitsPerCoordinate
argument_list|()
expr_stmt|;
name|bitsPerFlag
operator|=
name|shading
operator|.
name|getBitsPerFlag
argument_list|()
expr_stmt|;
name|patchList
operator|=
name|getCoonsPatchList
argument_list|(
name|xform
argument_list|,
name|ctm
argument_list|)
expr_stmt|;
name|pixelTable
operator|=
name|calcPixelTable
argument_list|()
expr_stmt|;
block|}
comment|// get the patch list which forms the type 6 shading image from data stream
specifier|private
name|ArrayList
argument_list|<
name|Patch
argument_list|>
name|getCoonsPatchList
parameter_list|(
name|AffineTransform
name|xform
parameter_list|,
name|Matrix
name|ctm
parameter_list|)
throws|throws
name|IOException
block|{
name|PDShadingType6
name|coonsShadingType
init|=
operator|(
name|PDShadingType6
operator|)
name|patchMeshesShadingType
decl_stmt|;
name|COSDictionary
name|cosDictionary
init|=
name|coonsShadingType
operator|.
name|getCOSDictionary
argument_list|()
decl_stmt|;
name|PDRange
name|rangeX
init|=
name|coonsShadingType
operator|.
name|getDecodeForParameter
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDRange
name|rangeY
init|=
name|coonsShadingType
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
name|coonsShadingType
operator|.
name|getDecodeForParameter
argument_list|(
literal|2
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
return|return
name|getPatchList
argument_list|(
name|xform
argument_list|,
name|ctm
argument_list|,
name|cosDictionary
argument_list|,
name|rangeX
argument_list|,
name|rangeY
argument_list|,
name|colRange
argument_list|,
literal|12
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Patch
name|generatePatch
parameter_list|(
name|Point2D
index|[]
name|points
parameter_list|,
name|float
index|[]
index|[]
name|color
parameter_list|)
block|{
return|return
operator|new
name|CoonsPatch
argument_list|(
name|points
argument_list|,
name|color
argument_list|)
return|;
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

