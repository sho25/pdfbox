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
name|state
package|;
end_package

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
name|COSFloat
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
name|COSNumber
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
name|COSObjectable
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
name|PDFontSetting
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
name|PDLineDashPattern
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
name|blend
operator|.
name|BlendMode
import|;
end_import

begin_comment
comment|/**  * This class represents the graphics state dictionary that is stored in the PDF document.  * The PDGraphicsStateValue holds the current runtime values as a stream is being executed.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|PDExtendedGraphicsState
implements|implements
name|COSObjectable
block|{
comment|/**      * Rendering intent constants, see PDF Reference 1.5 Section 4.5.4 Rendering Intents.      */
specifier|public
specifier|static
specifier|final
name|String
name|RENDERING_INTENT_ABSOLUTE_COLORIMETRIC
init|=
literal|"AbsoluteColorimetric"
decl_stmt|;
comment|/**      * Rendering intent constants, see PDF Reference 1.5 Section 4.5.4 Rendering Intents.      */
specifier|public
specifier|static
specifier|final
name|String
name|RENDERING_INTENT_RELATIVE_COLORIMETRIC
init|=
literal|"RelativeColorimetric"
decl_stmt|;
comment|/**      * Rendering intent constants, see PDF Reference 1.5 Section 4.5.4 Rendering Intents.      */
specifier|public
specifier|static
specifier|final
name|String
name|RENDERING_INTENT_SATURATION
init|=
literal|"Saturation"
decl_stmt|;
comment|/**      * Rendering intent constants, see PDF Reference 1.5 Section 4.5.4 Rendering Intents.      */
specifier|public
specifier|static
specifier|final
name|String
name|RENDERING_INTENT_PERCEPTUAL
init|=
literal|"Perceptual"
decl_stmt|;
specifier|private
name|COSDictionary
name|graphicsState
decl_stmt|;
comment|/**      * Default constructor, creates blank graphics state.      */
specifier|public
name|PDExtendedGraphicsState
parameter_list|()
block|{
name|graphicsState
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|graphicsState
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|EXT_G_STATE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a graphics state from an existing dictionary.      *      * @param dictionary The existing graphics state.      */
specifier|public
name|PDExtendedGraphicsState
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|graphicsState
operator|=
name|dictionary
expr_stmt|;
block|}
comment|/**      * This will implement the gs operator.      *      * @param gs The state to copy this dictionaries values into.      *      * @throws IOException If there is an error copying font information.      */
specifier|public
name|void
name|copyIntoGraphicsState
parameter_list|(
name|PDGraphicsState
name|gs
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|COSName
name|key
range|:
name|graphicsState
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|LW
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setLineWidth
argument_list|(
name|getLineWidth
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|LC
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setLineCap
argument_list|(
name|getLineCapStyle
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|LJ
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setLineJoin
argument_list|(
name|getLineJoinStyle
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|ML
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setMiterLimit
argument_list|(
name|getMiterLimit
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setLineDashPattern
argument_list|(
name|getLineDashPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|RI
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setRenderingIntent
argument_list|(
name|getRenderingIntent
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|OPM
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setOverprintMode
argument_list|(
name|getOverprintMode
argument_list|()
operator|.
name|doubleValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|FONT
argument_list|)
condition|)
block|{
name|PDFontSetting
name|setting
init|=
name|getFontSetting
argument_list|()
decl_stmt|;
if|if
condition|(
name|setting
operator|!=
literal|null
condition|)
block|{
name|gs
operator|.
name|getTextState
argument_list|()
operator|.
name|setFont
argument_list|(
name|setting
operator|.
name|getFont
argument_list|()
argument_list|)
expr_stmt|;
name|gs
operator|.
name|getTextState
argument_list|()
operator|.
name|setFontSize
argument_list|(
name|setting
operator|.
name|getFontSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|FL
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setFlatness
argument_list|(
name|getFlatnessTolerance
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|SM
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setSmoothness
argument_list|(
name|getSmoothnessTolerance
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|SA
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setStrokeAdjustment
argument_list|(
name|getAutomaticStrokeAdjustment
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|CA
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setAlphaConstants
argument_list|(
name|getStrokingAlpaConstant
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|CA_NS
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setNonStrokeAlphaConstants
argument_list|(
name|getNonStrokingAlpaConstant
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|AIS
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setAlphaSource
argument_list|(
name|getAlphaSourceFlag
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|TK
argument_list|)
condition|)
block|{
name|gs
operator|.
name|getTextState
argument_list|()
operator|.
name|setKnockoutFlag
argument_list|(
name|getTextKnockoutFlag
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|SMASK
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setSoftMask
argument_list|(
name|getSoftMask
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|BM
argument_list|)
condition|)
block|{
name|gs
operator|.
name|setBlendMode
argument_list|(
name|getBlendMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will get the underlying dictionary that this class acts on.      *      * @return The underlying dictionary for this class.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|graphicsState
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|graphicsState
return|;
block|}
comment|/**      * This will get the line width.  This will return null if there is no line width      *      * @return null or the LW value of the dictionary.      */
specifier|public
name|Float
name|getLineWidth
parameter_list|()
block|{
return|return
name|getFloatItem
argument_list|(
name|COSName
operator|.
name|LW
argument_list|)
return|;
block|}
comment|/**      * This will set the line width.      *      * @param width The line width for the object.      */
specifier|public
name|void
name|setLineWidth
parameter_list|(
name|Float
name|width
parameter_list|)
block|{
name|setFloatItem
argument_list|(
name|COSName
operator|.
name|LW
argument_list|,
name|width
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the line cap style.      *      * @return null or the LC value of the dictionary.      */
specifier|public
name|int
name|getLineCapStyle
parameter_list|()
block|{
return|return
name|graphicsState
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LC
argument_list|)
return|;
block|}
comment|/**      * This will set the line cap style for the graphics state.      *      * @param style The new line cap style to set.      */
specifier|public
name|void
name|setLineCapStyle
parameter_list|(
name|int
name|style
parameter_list|)
block|{
name|graphicsState
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LC
argument_list|,
name|style
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the line join style.      *      * @return null or the LJ value in the dictionary.      */
specifier|public
name|int
name|getLineJoinStyle
parameter_list|()
block|{
return|return
name|graphicsState
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LJ
argument_list|)
return|;
block|}
comment|/**      * This will set the line join style.      *      * @param style The new line join style.      */
specifier|public
name|void
name|setLineJoinStyle
parameter_list|(
name|int
name|style
parameter_list|)
block|{
name|graphicsState
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LJ
argument_list|,
name|style
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the miter limit.      *      * @return null or the ML value in the dictionary.      */
specifier|public
name|Float
name|getMiterLimit
parameter_list|()
block|{
return|return
name|getFloatItem
argument_list|(
name|COSName
operator|.
name|ML
argument_list|)
return|;
block|}
comment|/**      * This will set the miter limit for the graphics state.      *      * @param miterLimit The new miter limit value      */
specifier|public
name|void
name|setMiterLimit
parameter_list|(
name|Float
name|miterLimit
parameter_list|)
block|{
name|setFloatItem
argument_list|(
name|COSName
operator|.
name|ML
argument_list|,
name|miterLimit
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the dash pattern.      *      * @return null or the D value in the dictionary.      */
specifier|public
name|PDLineDashPattern
name|getLineDashPattern
parameter_list|()
block|{
name|PDLineDashPattern
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|dp
init|=
operator|(
name|COSArray
operator|)
name|graphicsState
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
decl_stmt|;
if|if
condition|(
name|dp
operator|!=
literal|null
condition|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|dp
operator|.
name|addAll
argument_list|(
name|dp
argument_list|)
expr_stmt|;
name|dp
operator|.
name|remove
argument_list|(
name|dp
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|int
name|phase
init|=
name|dp
operator|.
name|getInt
argument_list|(
name|dp
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|retval
operator|=
operator|new
name|PDLineDashPattern
argument_list|(
name|array
argument_list|,
name|phase
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the dash pattern for the graphics state.      *      * @param dashPattern The dash pattern      */
specifier|public
name|void
name|setLineDashPattern
parameter_list|(
name|PDLineDashPattern
name|dashPattern
parameter_list|)
block|{
name|graphicsState
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|dashPattern
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the rendering intent.      *      * @return null or the RI value in the dictionary.      */
specifier|public
name|String
name|getRenderingIntent
parameter_list|()
block|{
return|return
name|graphicsState
operator|.
name|getNameAsString
argument_list|(
literal|"RI"
argument_list|)
return|;
block|}
comment|/**      * This will set the rendering intent for the graphics state.      *      * @param ri The new rendering intent      */
specifier|public
name|void
name|setRenderingIntent
parameter_list|(
name|String
name|ri
parameter_list|)
block|{
name|graphicsState
operator|.
name|setName
argument_list|(
literal|"RI"
argument_list|,
name|ri
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the overprint control.      *      * @return The overprint control or null if one has not been set.      */
specifier|public
name|boolean
name|getStrokingOverprintControl
parameter_list|()
block|{
return|return
name|graphicsState
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|OP
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * This will get the overprint control(OP).      *      * @param op The overprint control.      */
specifier|public
name|void
name|setStrokingOverprintControl
parameter_list|(
name|boolean
name|op
parameter_list|)
block|{
name|graphicsState
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|OP
argument_list|,
name|op
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the overprint control for non stroking operations.  If this      * value is null then the regular overprint control value will be returned.      *      * @return The overprint control or null if one has not been set.      */
specifier|public
name|boolean
name|getNonStrokingOverprintControl
parameter_list|()
block|{
return|return
name|graphicsState
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|OP_NS
argument_list|,
name|getStrokingOverprintControl
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This will get the overprint control(OP).      *      * @param op The overprint control.      */
specifier|public
name|void
name|setNonStrokingOverprintControl
parameter_list|(
name|boolean
name|op
parameter_list|)
block|{
name|graphicsState
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|OP_NS
argument_list|,
name|op
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the overprint control mode.      *      * @return The overprint control mode or null if one has not been set.      */
specifier|public
name|Float
name|getOverprintMode
parameter_list|()
block|{
return|return
name|getFloatItem
argument_list|(
name|COSName
operator|.
name|OPM
argument_list|)
return|;
block|}
comment|/**      * This will get the overprint mode(OPM).      *      * @param overprintMode The overprint mode      */
specifier|public
name|void
name|setOverprintMode
parameter_list|(
name|Float
name|overprintMode
parameter_list|)
block|{
name|setFloatItem
argument_list|(
name|COSName
operator|.
name|OPM
argument_list|,
name|overprintMode
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the font setting of the graphics state.      *      * @return The font setting.      */
specifier|public
name|PDFontSetting
name|getFontSetting
parameter_list|()
block|{
name|PDFontSetting
name|setting
init|=
literal|null
decl_stmt|;
name|COSBase
name|base
init|=
name|graphicsState
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FONT
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
name|font
init|=
operator|(
name|COSArray
operator|)
name|base
decl_stmt|;
if|if
condition|(
name|font
operator|!=
literal|null
condition|)
block|{
name|setting
operator|=
operator|new
name|PDFontSetting
argument_list|(
name|font
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|setting
return|;
block|}
comment|/**      * This will set the font setting for this graphics state.      *      * @param fs The new font setting.      */
specifier|public
name|void
name|setFontSetting
parameter_list|(
name|PDFontSetting
name|fs
parameter_list|)
block|{
name|graphicsState
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FONT
argument_list|,
name|fs
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the flatness tolerance.      *      * @return The flatness tolerance or null if one has not been set.      */
specifier|public
name|Float
name|getFlatnessTolerance
parameter_list|()
block|{
return|return
name|getFloatItem
argument_list|(
name|COSName
operator|.
name|FL
argument_list|)
return|;
block|}
comment|/**      * This will get the flatness tolerance.      *      * @param flatness The new flatness tolerance      */
specifier|public
name|void
name|setFlatnessTolerance
parameter_list|(
name|Float
name|flatness
parameter_list|)
block|{
name|setFloatItem
argument_list|(
name|COSName
operator|.
name|FL
argument_list|,
name|flatness
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the smothness tolerance.      *      * @return The smothness tolerance or null if one has not been set.      */
specifier|public
name|Float
name|getSmoothnessTolerance
parameter_list|()
block|{
return|return
name|getFloatItem
argument_list|(
name|COSName
operator|.
name|SM
argument_list|)
return|;
block|}
comment|/**      * This will get the smoothness tolerance.      *      * @param smoothness The new smoothness tolerance      */
specifier|public
name|void
name|setSmoothnessTolerance
parameter_list|(
name|Float
name|smoothness
parameter_list|)
block|{
name|setFloatItem
argument_list|(
name|COSName
operator|.
name|SM
argument_list|,
name|smoothness
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the automatic stroke adjustment flag.      *      * @return The automatic stroke adjustment flag or null if one has not been set.      */
specifier|public
name|boolean
name|getAutomaticStrokeAdjustment
parameter_list|()
block|{
return|return
name|graphicsState
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|SA
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * This will get the automatic stroke adjustment flag.      *      * @param sa The new automatic stroke adjustment flag.      */
specifier|public
name|void
name|setAutomaticStrokeAdjustment
parameter_list|(
name|boolean
name|sa
parameter_list|)
block|{
name|graphicsState
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|SA
argument_list|,
name|sa
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the stroking alpha constant.      *      * @return The stroking alpha constant or null if one has not been set.      */
specifier|public
name|Float
name|getStrokingAlpaConstant
parameter_list|()
block|{
return|return
name|getFloatItem
argument_list|(
name|COSName
operator|.
name|CA
argument_list|)
return|;
block|}
comment|/**      * This will get the stroking alpha constant.      *      * @param alpha The new stroking alpha constant.      */
specifier|public
name|void
name|setStrokingAlphaConstant
parameter_list|(
name|Float
name|alpha
parameter_list|)
block|{
name|setFloatItem
argument_list|(
name|COSName
operator|.
name|CA
argument_list|,
name|alpha
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the non stroking alpha constant.      *      * @return The non stroking alpha constant or null if one has not been set.      */
specifier|public
name|Float
name|getNonStrokingAlpaConstant
parameter_list|()
block|{
return|return
name|getFloatItem
argument_list|(
name|COSName
operator|.
name|CA_NS
argument_list|)
return|;
block|}
comment|/**      * This will get the non stroking alpha constant.      *      * @param alpha The new non stroking alpha constant.      */
specifier|public
name|void
name|setNonStrokingAlphaConstant
parameter_list|(
name|Float
name|alpha
parameter_list|)
block|{
name|setFloatItem
argument_list|(
name|COSName
operator|.
name|CA_NS
argument_list|,
name|alpha
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the alpha source flag.      *      * @return The alpha source flag.      */
specifier|public
name|boolean
name|getAlphaSourceFlag
parameter_list|()
block|{
return|return
name|graphicsState
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|AIS
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * This will get the alpha source flag.      *      * @param alpha The alpha source flag.      */
specifier|public
name|void
name|setAlphaSourceFlag
parameter_list|(
name|boolean
name|alpha
parameter_list|)
block|{
name|graphicsState
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|AIS
argument_list|,
name|alpha
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the blending mode stored in the COS dictionary      *      * @return the blending mode      */
specifier|public
name|BlendMode
name|getBlendMode
parameter_list|()
block|{
return|return
name|BlendMode
operator|.
name|getInstance
argument_list|(
name|graphicsState
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BM
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns the soft mask stored in the COS dictionary      *      * @return the soft mask      */
specifier|public
name|PDSoftMask
name|getSoftMask
parameter_list|()
block|{
return|return
name|PDSoftMask
operator|.
name|create
argument_list|(
name|graphicsState
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SMASK
argument_list|)
argument_list|)
return|;
block|}
comment|/**      /**      * This will get the text knockout flag.      *      * @return The text knockout flag.      */
specifier|public
name|boolean
name|getTextKnockoutFlag
parameter_list|()
block|{
return|return
name|graphicsState
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|TK
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * This will get the text knockout flag.      *      * @param tk The text knockout flag.      */
specifier|public
name|void
name|setTextKnockoutFlag
parameter_list|(
name|boolean
name|tk
parameter_list|)
block|{
name|graphicsState
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|TK
argument_list|,
name|tk
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a float item from the dictionary.      *      * @param key The key to the item.      *      * @return The value for that item.      */
specifier|private
name|Float
name|getFloatItem
parameter_list|(
name|COSName
name|key
parameter_list|)
block|{
name|Float
name|retval
init|=
literal|null
decl_stmt|;
name|COSNumber
name|value
init|=
operator|(
name|COSNumber
operator|)
name|graphicsState
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|value
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a float object.      *      * @param key The key to the data that we are setting.      * @param value The value that we are setting.      */
specifier|private
name|void
name|setFloatItem
parameter_list|(
name|COSName
name|key
parameter_list|,
name|Float
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|graphicsState
operator|.
name|removeItem
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|graphicsState
operator|.
name|setItem
argument_list|(
name|key
argument_list|,
operator|new
name|COSFloat
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

