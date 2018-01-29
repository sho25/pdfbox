begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2018 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|interactive
operator|.
name|annotation
package|;
end_package

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
name|COSBase
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
name|io
operator|.
name|ScratchFile
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
name|interactive
operator|.
name|annotation
operator|.
name|handlers
operator|.
name|PDAppearanceHandler
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
name|interactive
operator|.
name|annotation
operator|.
name|handlers
operator|.
name|PDPolygonAppearanceHandler
import|;
end_import

begin_comment
comment|/**  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationPolygon
extends|extends
name|PDAnnotationMarkup
block|{
comment|/**      * The type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"Polygon"
decl_stmt|;
specifier|private
name|PDAppearanceHandler
name|polygonAppearanceHandler
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDAnnotationPolygon
parameter_list|()
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dict The annotations dictionary.      */
specifier|public
name|PDAnnotationPolygon
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|super
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
comment|// PDF 32000 specification has "the interior color with which to fill the annotation’s line endings"
comment|// but it is the inside of the polygon.
comment|/**      * This will set interior color.      *      * @param ic color.      */
specifier|public
name|void
name|setInteriorColor
parameter_list|(
name|PDColor
name|ic
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|IC
argument_list|,
name|ic
operator|.
name|toCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the interior color.      *      * @return object representing the color.      */
specifier|public
name|PDColor
name|getInteriorColor
parameter_list|()
block|{
return|return
name|getColor
argument_list|(
name|COSName
operator|.
name|IC
argument_list|)
return|;
block|}
comment|/**      * This will retrieve the numbers that shall represent the alternating horizontal and vertical      * coordinates.      *      * @return An array of floats representing the alternating horizontal and vertical coordinates.      */
specifier|public
name|float
index|[]
name|getVertices
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|VERTICES
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|toFloatArray
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the numbers that shall represent the alternating horizontal and vertical      * coordinates.      *      * @param points an array with the numbers that shall represent the alternating horizontal and      * vertical coordinates.      */
specifier|public
name|void
name|setVertices
parameter_list|(
name|float
index|[]
name|points
parameter_list|)
block|{
name|COSArray
name|ar
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|ar
operator|.
name|setFloatArray
argument_list|(
name|points
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|VERTICES
argument_list|,
name|ar
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDF 2.0: This will retrieve the arrays that shall represent the alternating horizontal      * and vertical coordinates for path building.      *      * @return An array of float arrays, each supplying the operands for a path building operator      * (m, l or c). The first array should have 2 elements, the others should have 2 or 6 elements.      */
specifier|public
name|float
index|[]
index|[]
name|getPath
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PATH
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|base
decl_stmt|;
name|float
index|[]
index|[]
name|pathArray
init|=
operator|new
name|float
index|[
name|array
operator|.
name|size
argument_list|()
index|]
index|[]
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
name|array
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|COSBase
name|base2
init|=
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|base2
operator|instanceof
name|COSArray
condition|)
block|{
name|pathArray
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|COSArray
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|pathArray
index|[
name|i
index|]
operator|=
operator|new
name|float
index|[
literal|0
index|]
expr_stmt|;
block|}
block|}
return|return
name|pathArray
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Set a custom appearance handler for generating the annotations appearance streams.      *       * @param polygonAppearanceHandler      */
specifier|public
name|void
name|setCustomPolygonAppearanceHandler
parameter_list|(
name|PDAppearanceHandler
name|polygonAppearanceHandler
parameter_list|)
block|{
name|this
operator|.
name|polygonAppearanceHandler
operator|=
name|polygonAppearanceHandler
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|constructAppearances
parameter_list|(
name|ScratchFile
name|scratchFile
parameter_list|)
block|{
if|if
condition|(
name|polygonAppearanceHandler
operator|==
literal|null
condition|)
block|{
name|PDPolygonAppearanceHandler
name|appearanceHandler
init|=
operator|new
name|PDPolygonAppearanceHandler
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|appearanceHandler
operator|.
name|generateAppearanceStreams
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|polygonAppearanceHandler
operator|.
name|generateAppearanceStreams
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

