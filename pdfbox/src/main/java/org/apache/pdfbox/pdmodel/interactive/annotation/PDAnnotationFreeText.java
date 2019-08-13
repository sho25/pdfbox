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
name|pdmodel
operator|.
name|PDDocument
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
name|PDFreeTextAppearanceHandler
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
name|form
operator|.
name|PDVariableText
import|;
end_import

begin_comment
comment|/**  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationFreeText
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
literal|"FreeText"
decl_stmt|;
comment|/**      * A plain free-text annotation, also known as a text box comment.      */
specifier|public
specifier|static
specifier|final
name|String
name|IT_FREE_TEXT
init|=
literal|"FreeText"
decl_stmt|;
comment|/**      * A callout, associated with an area on the page through the callout line specified.      */
specifier|public
specifier|static
specifier|final
name|String
name|IT_FREE_TEXT_CALLOUT
init|=
literal|"FreeTextCallout"
decl_stmt|;
comment|/**      * The annotation is intended to function as a click-to-type or typewriter object.      */
specifier|public
specifier|static
specifier|final
name|String
name|IT_FREE_TEXT_TYPE_WRITER
init|=
literal|"FreeTextTypeWriter"
decl_stmt|;
specifier|private
name|PDAppearanceHandler
name|customAppearanceHandler
decl_stmt|;
specifier|public
name|PDAnnotationFreeText
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
comment|/**      * Creates a FreeText annotation from a COSDictionary, expected to be a correct object definition.      *      * @param field the PDF object to represent as a field.      */
specifier|public
name|PDAnnotationFreeText
parameter_list|(
name|COSDictionary
name|field
parameter_list|)
block|{
name|super
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default appearance.      *       * @return a string describing the default appearance.      */
specifier|public
name|String
name|getDefaultAppearance
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|DA
argument_list|)
return|;
block|}
comment|/**      * Set the default appearance.      *      * @param daValue a string describing the default appearance.      */
specifier|public
name|void
name|setDefaultAppearance
parameter_list|(
name|String
name|daValue
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DA
argument_list|,
name|daValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default style string.      *      * The default style string defines the default style for rich text fields.      *      * @return the DS element of the dictionary object      */
specifier|public
name|String
name|getDefaultStyleString
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|DS
argument_list|)
return|;
block|}
comment|/**      * Set the default style string.      *      * Providing null as the value will remove the default style string.      *      * @param defaultStyleString a string describing the default style.      */
specifier|public
name|void
name|setDefaultStyleString
parameter_list|(
name|String
name|defaultStyleString
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DS
argument_list|,
name|defaultStyleString
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the 'quadding' or justification of the text to be displayed.      *<br>      * 0 - Left (default)<br>      * 1 - Centered<br>      * 2 - Right<br>      * Please see the QUADDING_CONSTANTS in {@link PDVariableText }.      *      * @return The justification of the text strings.      */
specifier|public
name|int
name|getQ
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|Q
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the quadding/justification of the text. Please see the QUADDING_CONSTANTS      * in {@link PDVariableText }.      *      * @param q The new text justification.      */
specifier|public
name|void
name|setQ
parameter_list|(
name|int
name|q
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|Q
argument_list|,
name|q
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the difference between the annotations "outer" rectangle defined by      * /Rect and the border.      *       *<p>This will set an equal difference for all sides</p>      *       * @param difference from the annotations /Rect entry      */
specifier|public
name|void
name|setRectDifferences
parameter_list|(
name|float
name|difference
parameter_list|)
block|{
name|setRectDifferences
argument_list|(
name|difference
argument_list|,
name|difference
argument_list|,
name|difference
argument_list|,
name|difference
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the difference between the annotations "outer" rectangle defined by      * /Rect and the border.      *       * @param differenceLeft left difference from the annotations /Rect entry      * @param differenceTop top difference from the annotations /Rect entry      * @param differenceRight right difference from  the annotations /Rect entry      * @param differenceBottom bottom difference from the annotations /Rect entry      *       */
specifier|public
name|void
name|setRectDifferences
parameter_list|(
name|float
name|differenceLeft
parameter_list|,
name|float
name|differenceTop
parameter_list|,
name|float
name|differenceRight
parameter_list|,
name|float
name|differenceBottom
parameter_list|)
block|{
name|COSArray
name|margins
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|margins
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|differenceLeft
argument_list|)
argument_list|)
expr_stmt|;
name|margins
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|differenceTop
argument_list|)
argument_list|)
expr_stmt|;
name|margins
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|differenceRight
argument_list|)
argument_list|)
expr_stmt|;
name|margins
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|differenceBottom
argument_list|)
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RD
argument_list|,
name|margins
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the margin between the annotations "outer" rectangle defined by      * /Rect and the border.      *       * @return the differences. If the entry hasn't been set am empty array is returned.      */
specifier|public
name|float
index|[]
name|getRectDifferences
parameter_list|()
block|{
name|COSBase
name|margin
init|=
name|getCOSObject
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|RD
argument_list|)
decl_stmt|;
if|if
condition|(
name|margin
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|(
operator|(
name|COSArray
operator|)
name|margin
operator|)
operator|.
name|toFloatArray
argument_list|()
return|;
block|}
return|return
operator|new
name|float
index|[]
block|{}
return|;
block|}
comment|/**      * This will set the coordinates of the callout line. (PDF 1.6 and higher) Only relevant if the      * intent is FreeTextCallout.      *      * @param callout An array of four or six numbers specifying a callout line attached to the free      * text annotation. Six numbers [ x1 y1 x2 y2 x3 y3 ] represent the starting, knee point, and      * ending coordinates of the line in default user space, four numbers [ x1 y1 x2 y2 ] represent      * the starting and ending coordinates of the line.      */
specifier|public
specifier|final
name|void
name|setCallout
parameter_list|(
name|float
index|[]
name|callout
parameter_list|)
block|{
name|COSArray
name|newCallout
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|newCallout
operator|.
name|setFloatArray
argument_list|(
name|callout
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CL
argument_list|,
name|newCallout
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the coordinates of the callout line. (PDF 1.6 and higher) Only relevant if the      * intent is FreeTextCallout.      *      * @return An array of four or six numbers specifying a callout line attached to the free text      * annotation. Six numbers [ x1 y1 x2 y2 x3 y3 ] represent the starting, knee point, and ending      * coordinates of the line in default user space, four numbers [ x1 y1 x2 y2 ] represent the      * starting and ending coordinates of the line.      */
specifier|public
name|float
index|[]
name|getCallout
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
name|CL
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
comment|/**      * This will set the line ending style.      *      * @param style The new style.      */
specifier|public
specifier|final
name|void
name|setLineEndingStyle
parameter_list|(
name|String
name|style
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|LE
argument_list|,
name|style
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the line ending style.      *      * @return The line ending style, possible values shown in the LE_ constants section, LE_NONE if      * missing, never null.      */
specifier|public
name|String
name|getLineEndingStyle
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|LE
argument_list|,
name|PDAnnotationLine
operator|.
name|LE_NONE
argument_list|)
return|;
block|}
comment|/**      * This will set the border effect dictionary, specifying effects to be applied when drawing the      * line. This is supported by PDF 1.6 and higher.      *      * @param be The border effect dictionary to set.      *      */
specifier|public
name|void
name|setBorderEffect
parameter_list|(
name|PDBorderEffectDictionary
name|be
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BE
argument_list|,
name|be
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border effect dictionary, specifying effects to be applied used in      * drawing the line.      *      * @return The border effect dictionary      */
specifier|public
name|PDBorderEffectDictionary
name|getBorderEffect
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
name|BE
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|new
name|PDBorderEffectDictionary
argument_list|(
operator|(
name|COSDictionary
operator|)
name|base
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the rectangle difference rectangle. Giving the difference between the      * annotations rectangle and where the drawing occurs. (To take account of any effects applied      * through the BE entry for example)      *      * @param rd the rectangle difference      *      */
specifier|public
name|void
name|setRectDifference
parameter_list|(
name|PDRectangle
name|rd
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RD
argument_list|,
name|rd
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the rectangle difference rectangle. Giving the difference between the      * annotations rectangle and where the drawing occurs. (To take account of any effects applied      * through the BE entry for example)      *      * @return the rectangle difference      */
specifier|public
name|PDRectangle
name|getRectDifference
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
name|RD
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
operator|new
name|PDRectangle
argument_list|(
operator|(
name|COSArray
operator|)
name|base
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Set a custom appearance handler for generating the annotations appearance streams.      *       * @param appearanceHandler      */
specifier|public
name|void
name|setCustomAppearanceHandler
parameter_list|(
name|PDAppearanceHandler
name|appearanceHandler
parameter_list|)
block|{
name|customAppearanceHandler
operator|=
name|appearanceHandler
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|constructAppearances
parameter_list|()
block|{
name|this
operator|.
name|constructAppearances
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|constructAppearances
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
if|if
condition|(
name|customAppearanceHandler
operator|==
literal|null
condition|)
block|{
name|PDFreeTextAppearanceHandler
name|appearanceHandler
init|=
operator|new
name|PDFreeTextAppearanceHandler
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
name|customAppearanceHandler
operator|.
name|generateAppearanceStreams
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

