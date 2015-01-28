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
name|interactive
operator|.
name|annotation
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
name|PDPage
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
name|pdmodel
operator|.
name|graphics
operator|.
name|color
operator|.
name|PDDeviceCMYK
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
name|PDDeviceGray
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
name|PDDeviceRGB
import|;
end_import

begin_comment
comment|/**  * A PDF annotation.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDAnnotation
implements|implements
name|COSObjectable
block|{
comment|/**      * Log instance.      */
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
name|PDAnnotation
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * An annotation flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_INVISIBLE
init|=
literal|1
operator|<<
literal|0
decl_stmt|;
comment|/**      * An annotation flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_HIDDEN
init|=
literal|1
operator|<<
literal|1
decl_stmt|;
comment|/**      * An annotation flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_PRINTED
init|=
literal|1
operator|<<
literal|2
decl_stmt|;
comment|/**      * An annotation flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_NO_ZOOM
init|=
literal|1
operator|<<
literal|3
decl_stmt|;
comment|/**      * An annotation flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_NO_ROTATE
init|=
literal|1
operator|<<
literal|4
decl_stmt|;
comment|/**      * An annotation flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_NO_VIEW
init|=
literal|1
operator|<<
literal|5
decl_stmt|;
comment|/**      * An annotation flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_READ_ONLY
init|=
literal|1
operator|<<
literal|6
decl_stmt|;
comment|/**      * An annotation flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_LOCKED
init|=
literal|1
operator|<<
literal|7
decl_stmt|;
comment|/**      * An annotation flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_TOGGLE_NO_VIEW
init|=
literal|1
operator|<<
literal|8
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Create the correct annotation from the base COS object.      *       * @param base The COS object that is the annotation.      * @return The correctly typed annotation object.      * @throws IOException If there is an error while creating the annotation.      */
specifier|public
specifier|static
name|PDAnnotation
name|createAnnotation
parameter_list|(
name|COSBase
name|base
parameter_list|)
throws|throws
name|IOException
block|{
name|PDAnnotation
name|annot
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|annotDic
init|=
operator|(
name|COSDictionary
operator|)
name|base
decl_stmt|;
name|String
name|subtype
init|=
name|annotDic
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|PDAnnotationFileAttachment
operator|.
name|SUB_TYPE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationFileAttachment
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PDAnnotationLine
operator|.
name|SUB_TYPE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationLine
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PDAnnotationLink
operator|.
name|SUB_TYPE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationLink
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PDAnnotationPopup
operator|.
name|SUB_TYPE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationPopup
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PDAnnotationRubberStamp
operator|.
name|SUB_TYPE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationRubberStamp
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PDAnnotationSquareCircle
operator|.
name|SUB_TYPE_SQUARE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
operator|||
name|PDAnnotationSquareCircle
operator|.
name|SUB_TYPE_CIRCLE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationSquareCircle
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PDAnnotationText
operator|.
name|SUB_TYPE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationText
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PDAnnotationTextMarkup
operator|.
name|SUB_TYPE_HIGHLIGHT
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
operator|||
name|PDAnnotationTextMarkup
operator|.
name|SUB_TYPE_UNDERLINE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
operator|||
name|PDAnnotationTextMarkup
operator|.
name|SUB_TYPE_SQUIGGLY
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
operator|||
name|PDAnnotationTextMarkup
operator|.
name|SUB_TYPE_STRIKEOUT
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationTextMarkup
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PDAnnotationLink
operator|.
name|SUB_TYPE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationLink
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PDAnnotationWidget
operator|.
name|SUB_TYPE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationWidget
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PDAnnotationMarkup
operator|.
name|SUB_TYPE_FREETEXT
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
operator|||
name|PDAnnotationMarkup
operator|.
name|SUB_TYPE_POLYGON
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
operator|||
name|PDAnnotationMarkup
operator|.
name|SUB_TYPE_POLYLINE
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
operator|||
name|PDAnnotationMarkup
operator|.
name|SUB_TYPE_CARET
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
operator|||
name|PDAnnotationMarkup
operator|.
name|SUB_TYPE_INK
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
operator|||
name|PDAnnotationMarkup
operator|.
name|SUB_TYPE_SOUND
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|annot
operator|=
operator|new
name|PDAnnotationMarkup
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO not yet implemented:
comment|// Movie, Screen, PrinterMark, TrapNet, Watermark, 3D, Redact
name|annot
operator|=
operator|new
name|PDAnnotationUnknown
argument_list|(
name|annotDic
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Unknown or unsupported annotation subtype "
operator|+
name|subtype
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Unknown annotation type "
operator|+
name|base
argument_list|)
throw|;
block|}
return|return
name|annot
return|;
block|}
comment|/**      * Constructor.      */
specifier|public
name|PDAnnotation
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|ANNOT
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param dict The annotations dictionary.      */
specifier|public
name|PDAnnotation
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|dictionary
operator|=
name|dict
expr_stmt|;
block|}
comment|/**      * returns the dictionary.      *       * @return the dictionary      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * The annotation rectangle, defining the location of the annotation on the page in default user space units. This      * is usually required and should not return null on valid PDF documents. But where this is a parent form field with      * children, such as radio button collections then the rectangle will be null.      *       * @return The Rect value of this annotation.      */
specifier|public
name|PDRectangle
name|getRectangle
parameter_list|()
block|{
name|COSArray
name|rectArray
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RECT
argument_list|)
decl_stmt|;
name|PDRectangle
name|rectangle
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|rectArray
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|rectArray
operator|.
name|size
argument_list|()
operator|==
literal|4
operator|&&
name|rectArray
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|COSNumber
operator|&&
name|rectArray
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|COSNumber
operator|&&
name|rectArray
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|instanceof
name|COSNumber
operator|&&
name|rectArray
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|instanceof
name|COSNumber
condition|)
block|{
name|rectangle
operator|=
operator|new
name|PDRectangle
argument_list|(
name|rectArray
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|rectArray
operator|+
literal|" is not a rectangle array, returning null"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|rectangle
return|;
block|}
comment|/**      * This will set the rectangle for this annotation.      *       * @param rectangle The new rectangle values.      */
specifier|public
name|void
name|setRectangle
parameter_list|(
name|PDRectangle
name|rectangle
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RECT
argument_list|,
name|rectangle
operator|.
name|getCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the flags for this field.      *       * @return flags The set of flags.      */
specifier|public
name|int
name|getAnnotationFlags
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the flags for this field.      *       * @param flags The new flags.      */
specifier|public
name|void
name|setAnnotationFlags
parameter_list|(
name|int
name|flags
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|flags
argument_list|)
expr_stmt|;
block|}
comment|/**      * Interface method for COSObjectable.      *       * @return This object as a standard COS object.      */
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
return|;
block|}
comment|/**      * Returns the annotations appearance state, which selects the applicable appearance stream      * from an appearance subdictionary.      */
specifier|public
name|COSName
name|getAppearanceState
parameter_list|()
block|{
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|name
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the annotations appearance state name.      *       * @param as The name of the appearance stream.      */
specifier|public
name|void
name|setAppearanceState
parameter_list|(
name|String
name|as
parameter_list|)
block|{
if|if
condition|(
name|as
operator|==
literal|null
condition|)
block|{
name|getDictionary
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|AS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AS
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
name|as
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get the appearance dictionary associated with this annotation. This may return null.      *       * @return This annotations appearance.      */
specifier|public
name|PDAppearanceDictionary
name|getAppearance
parameter_list|()
block|{
name|PDAppearanceDictionary
name|ap
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|apDic
init|=
operator|(
name|COSDictionary
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AP
argument_list|)
decl_stmt|;
if|if
condition|(
name|apDic
operator|!=
literal|null
condition|)
block|{
name|ap
operator|=
operator|new
name|PDAppearanceDictionary
argument_list|(
name|apDic
argument_list|)
expr_stmt|;
block|}
return|return
name|ap
return|;
block|}
comment|/**      * This will set the appearance associated with this annotation.      *       * @param appearance The appearance dictionary for this annotation.      */
specifier|public
name|void
name|setAppearance
parameter_list|(
name|PDAppearanceDictionary
name|appearance
parameter_list|)
block|{
name|COSDictionary
name|ap
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|appearance
operator|!=
literal|null
condition|)
block|{
name|ap
operator|=
name|appearance
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AP
argument_list|,
name|ap
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the appearance stream for this annotation, if any. The annotation state is taken      * into account, if present.      */
specifier|public
name|PDAppearanceStream
name|getNormalAppearanceStream
parameter_list|()
block|{
name|PDAppearanceDictionary
name|appearanceDict
init|=
name|getAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|appearanceDict
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|PDAppearanceEntry
name|normalAppearance
init|=
name|appearanceDict
operator|.
name|getNormalAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|normalAppearance
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|normalAppearance
operator|.
name|isSubDictionary
argument_list|()
condition|)
block|{
name|COSName
name|state
init|=
name|getAppearanceState
argument_list|()
decl_stmt|;
return|return
name|normalAppearance
operator|.
name|getSubDictionary
argument_list|()
operator|.
name|get
argument_list|(
name|state
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|normalAppearance
operator|.
name|getAppearanceStream
argument_list|()
return|;
block|}
block|}
comment|/**      * Get the invisible flag.      *       * @return The invisible flag.      */
specifier|public
name|boolean
name|isInvisible
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_INVISIBLE
argument_list|)
return|;
block|}
comment|/**      * Set the invisible flag.      *       * @param invisible The new invisible flag.      */
specifier|public
name|void
name|setInvisible
parameter_list|(
name|boolean
name|invisible
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_INVISIBLE
argument_list|,
name|invisible
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the hidden flag.      *       * @return The hidden flag.      */
specifier|public
name|boolean
name|isHidden
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_HIDDEN
argument_list|)
return|;
block|}
comment|/**      * Set the hidden flag.      *       * @param hidden The new hidden flag.      */
specifier|public
name|void
name|setHidden
parameter_list|(
name|boolean
name|hidden
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_HIDDEN
argument_list|,
name|hidden
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the printed flag.      *       * @return The printed flag.      */
specifier|public
name|boolean
name|isPrinted
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_PRINTED
argument_list|)
return|;
block|}
comment|/**      * Set the printed flag.      *       * @param printed The new printed flag.      */
specifier|public
name|void
name|setPrinted
parameter_list|(
name|boolean
name|printed
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_PRINTED
argument_list|,
name|printed
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the noZoom flag.      *       * @return The noZoom flag.      */
specifier|public
name|boolean
name|isNoZoom
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_NO_ZOOM
argument_list|)
return|;
block|}
comment|/**      * Set the noZoom flag.      *       * @param noZoom The new noZoom flag.      */
specifier|public
name|void
name|setNoZoom
parameter_list|(
name|boolean
name|noZoom
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_NO_ZOOM
argument_list|,
name|noZoom
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the noRotate flag.      *       * @return The noRotate flag.      */
specifier|public
name|boolean
name|isNoRotate
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_NO_ROTATE
argument_list|)
return|;
block|}
comment|/**      * Set the noRotate flag.      *       * @param noRotate The new noRotate flag.      */
specifier|public
name|void
name|setNoRotate
parameter_list|(
name|boolean
name|noRotate
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_NO_ROTATE
argument_list|,
name|noRotate
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the noView flag.      *       * @return The noView flag.      */
specifier|public
name|boolean
name|isNoView
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_NO_VIEW
argument_list|)
return|;
block|}
comment|/**      * Set the noView flag.      *       * @param noView The new noView flag.      */
specifier|public
name|void
name|setNoView
parameter_list|(
name|boolean
name|noView
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_NO_VIEW
argument_list|,
name|noView
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the readOnly flag.      *       * @return The readOnly flag.      */
specifier|public
name|boolean
name|isReadOnly
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_READ_ONLY
argument_list|)
return|;
block|}
comment|/**      * Set the readOnly flag.      *       * @param readOnly The new readOnly flag.      */
specifier|public
name|void
name|setReadOnly
parameter_list|(
name|boolean
name|readOnly
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_READ_ONLY
argument_list|,
name|readOnly
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the locked flag.      *       * @return The locked flag.      */
specifier|public
name|boolean
name|isLocked
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_LOCKED
argument_list|)
return|;
block|}
comment|/**      * Set the locked flag.      *       * @param locked The new locked flag.      */
specifier|public
name|void
name|setLocked
parameter_list|(
name|boolean
name|locked
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_LOCKED
argument_list|,
name|locked
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the toggleNoView flag.      *       * @return The toggleNoView flag.      */
specifier|public
name|boolean
name|isToggleNoView
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_TOGGLE_NO_VIEW
argument_list|)
return|;
block|}
comment|/**      * Set the toggleNoView flag.      *       * @param toggleNoView The new toggleNoView flag.      */
specifier|public
name|void
name|setToggleNoView
parameter_list|(
name|boolean
name|toggleNoView
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|FLAG_TOGGLE_NO_VIEW
argument_list|,
name|toggleNoView
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the "contents" of the field.      *       * @return the value of the contents.      */
specifier|public
name|String
name|getContents
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|)
return|;
block|}
comment|/**      * Set the "contents" of the field.      *       * @param value the value of the contents.      */
specifier|public
name|void
name|setContents
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the date and time the annotation was modified.      *       * @return the modified date/time (often in date format, but can be an arbitary string).      */
specifier|public
name|String
name|getModifiedDate
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|M
argument_list|)
return|;
block|}
comment|/**      * This will set the the date and time the annotation was modified.      *       * @param m the date and time the annotation was created.      */
specifier|public
name|void
name|setModifiedDate
parameter_list|(
name|String
name|m
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|M
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the name, a string intended to uniquely identify each annotation within a page. Not to be confused      * with some annotations Name entry which impact the default image drawn for them.      *       * @return The identifying name for the Annotation.      */
specifier|public
name|String
name|getAnnotationName
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|NM
argument_list|)
return|;
block|}
comment|/**      * This will set the name, a string intended to uniquely identify each annotation within a page. Not to be confused      * with some annotations Name entry which impact the default image drawn for them.      *       * @param nm The identifying name for the annotation.      */
specifier|public
name|void
name|setAnnotationName
parameter_list|(
name|String
name|nm
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|NM
argument_list|,
name|nm
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the key of this annotation in the structural parent tree.      *       * @return the integer key of the annotation's entry in the structural parent tree      */
specifier|public
name|int
name|getStructParent
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|STRUCT_PARENT
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the key for this annotation in the structural parent tree.      *       * @param structParent The new key for this annotation.      */
specifier|public
name|void
name|setStructParent
parameter_list|(
name|int
name|structParent
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|STRUCT_PARENT
argument_list|,
name|structParent
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the color used in drawing various elements. As of PDF 1.6 these are : Background of icon when      * closed Title bar of popup window Border of a link annotation      *       * Colour is in DeviceRGB colourspace      *       * @param c colour in the DeviceRGB colourspace      *       */
specifier|public
name|void
name|setColor
parameter_list|(
name|PDColor
name|c
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C
argument_list|,
name|c
operator|.
name|toCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the color used in drawing various elements. As of PDF      * 1.6 these are :      *<ul>      *<li>Background of icon when closed</li>      *<li>Title bar of popup window</li>      *<li>Border of a link annotation</li></ul>      *      * @return Color object representing the colour      *       */
specifier|public
name|PDColor
name|getColor
parameter_list|()
block|{
return|return
name|getColor
argument_list|(
name|COSName
operator|.
name|C
argument_list|)
return|;
block|}
specifier|protected
name|PDColor
name|getColor
parameter_list|(
name|COSName
name|itemName
parameter_list|)
block|{
name|COSBase
name|c
init|=
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getItem
argument_list|(
name|itemName
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|COSArray
condition|)
block|{
name|PDColorSpace
name|colorSpace
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
operator|(
operator|(
name|COSArray
operator|)
name|c
operator|)
operator|.
name|size
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
name|colorSpace
operator|=
name|PDDeviceGray
operator|.
name|INSTANCE
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|colorSpace
operator|=
name|PDDeviceRGB
operator|.
name|INSTANCE
expr_stmt|;
break|break;
case|case
literal|4
case|:
name|colorSpace
operator|=
name|PDDeviceCMYK
operator|.
name|INSTANCE
expr_stmt|;
break|break;
block|}
return|return
operator|new
name|PDColor
argument_list|(
operator|(
name|COSArray
operator|)
name|c
argument_list|,
name|colorSpace
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will retrieve the subtype of the annotation.      *       * @return the subtype      */
specifier|public
name|String
name|getSubtype
parameter_list|()
block|{
return|return
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
return|;
block|}
comment|/**      * This will set the corresponding page for this annotation.      *       * @param page is the corresponding page      */
specifier|public
name|void
name|setPage
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|P
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the corresponding page of this annotation.      *       * @return the corresponding page      */
specifier|public
name|PDPage
name|getPage
parameter_list|()
block|{
name|COSDictionary
name|p
init|=
operator|(
name|COSDictionary
operator|)
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|P
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDPage
argument_list|(
name|p
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

