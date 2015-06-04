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
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|cos
operator|.
name|COSString
import|;
end_import

begin_comment
comment|/**  * This class represents the additonal fields of a Markup type Annotation. See section 12.5.6 of ISO32000-1:2008  * (starting with page 390) for details on annotation types.  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationMarkup
extends|extends
name|PDAnnotation
block|{
comment|/**      * Constant for a FreeText type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_FREETEXT
init|=
literal|"FreeText"
decl_stmt|;
comment|/**      * Constant for an Polygon type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_POLYGON
init|=
literal|"Polygon"
decl_stmt|;
comment|/**      * Constant for an PolyLine type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_POLYLINE
init|=
literal|"PolyLine"
decl_stmt|;
comment|/**      * Constant for an Caret type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_CARET
init|=
literal|"Caret"
decl_stmt|;
comment|/**      * Constant for an Ink type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_INK
init|=
literal|"Ink"
decl_stmt|;
comment|/**      * Constant for an Sound type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_SOUND
init|=
literal|"Sound"
decl_stmt|;
comment|/*      * The various values of the reply type as defined in the PDF 1.7 reference Table 170      */
comment|/**      * Constant for an annotation reply type.      */
specifier|public
specifier|static
specifier|final
name|String
name|RT_REPLY
init|=
literal|"R"
decl_stmt|;
comment|/**      * Constant for an annotation reply type.      */
specifier|public
specifier|static
specifier|final
name|String
name|RT_GROUP
init|=
literal|"Group"
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDAnnotationMarkup
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dict The annotations dictionary.      */
specifier|public
name|PDAnnotationMarkup
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
comment|/**      * Retrieve the string used as the title of the popup window shown when open and active (by convention this      * identifies who added the annotation).      *      * @return The title of the popup.      */
specifier|public
name|String
name|getTitlePopup
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
name|T
argument_list|)
return|;
block|}
comment|/**      * Set the string used as the title of the popup window shown when open and active (by convention this identifies      * who added the annotation).      *      * @param t The title of the popup.      */
specifier|public
name|void
name|setTitlePopup
parameter_list|(
name|String
name|t
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|T
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the popup annotation used for entering/editing the text for this annotation.      *      * @return the popup annotation.      */
specifier|public
name|PDAnnotationPopup
name|getPopup
parameter_list|()
block|{
name|COSDictionary
name|popup
init|=
operator|(
name|COSDictionary
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"Popup"
argument_list|)
decl_stmt|;
if|if
condition|(
name|popup
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDAnnotationPopup
argument_list|(
name|popup
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * This will set the popup annotation used for entering/editing the text for this annotation.      *      * @param popup the popup annotation.      */
specifier|public
name|void
name|setPopup
parameter_list|(
name|PDAnnotationPopup
name|popup
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"Popup"
argument_list|,
name|popup
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the constant opacity value used when rendering the annotation (excluing any popup).      *      * @return the constant opacity value.      */
specifier|public
name|float
name|getConstantOpacity
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|CA
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * This will set the constant opacity value used when rendering the annotation (excluing any popup).      *      * @param ca the constant opacity value.      */
specifier|public
name|void
name|setConstantOpacity
parameter_list|(
name|float
name|ca
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|CA
argument_list|,
name|ca
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the rich text stream which is displayed in the popup window.      *      * @return the rich text stream.      */
specifier|public
name|String
name|getRichContents
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
name|RC
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSString
condition|)
block|{
return|return
operator|(
operator|(
name|COSString
operator|)
name|base
operator|)
operator|.
name|getString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSStream
condition|)
block|{
return|return
operator|(
operator|(
name|COSStream
operator|)
name|base
operator|)
operator|.
name|getString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * This will set the rich text stream which is displayed in the popup window.      *      * @param rc the rich text stream.      */
specifier|public
name|void
name|setRichContents
parameter_list|(
name|String
name|rc
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RC
argument_list|,
operator|new
name|COSString
argument_list|(
name|rc
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the date and time the annotation was created.      *      * @return the creation date/time.      * @throws IOException if there is a format problem when converting the date.      */
specifier|public
name|Calendar
name|getCreationDate
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getDate
argument_list|(
name|COSName
operator|.
name|CREATION_DATE
argument_list|)
return|;
block|}
comment|/**      * This will set the date and time the annotation was created.      *      * @param creationDate the date and time the annotation was created.      */
specifier|public
name|void
name|setCreationDate
parameter_list|(
name|Calendar
name|creationDate
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setDate
argument_list|(
name|COSName
operator|.
name|CREATION_DATE
argument_list|,
name|creationDate
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the annotation to which this one is "In Reply To" the actual relationship is specified by the      * RT entry.      *      * @return the other annotation.      * @throws IOException if there is an error with the annotation.      */
specifier|public
name|PDAnnotation
name|getInReplyTo
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|irt
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"IRT"
argument_list|)
decl_stmt|;
return|return
name|PDAnnotation
operator|.
name|createAnnotation
argument_list|(
name|irt
argument_list|)
return|;
block|}
comment|/**      * This will set the annotation to which this one is "In Reply To" the actual relationship is specified by the RT      * entry.      *      * @param irt the annotation this one is "In Reply To".      */
specifier|public
name|void
name|setInReplyTo
parameter_list|(
name|PDAnnotation
name|irt
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"IRT"
argument_list|,
name|irt
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the short description of the subject of the annotation.      *      * @return the subject.      */
specifier|public
name|String
name|getSubject
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
name|SUBJ
argument_list|)
return|;
block|}
comment|/**      * This will set the short description of the subject of the annotation.      *      * @param subj short description of the subject.      */
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subj
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|SUBJ
argument_list|,
name|subj
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the Reply Type (relationship) with the annotation in the IRT entry See the RT_* constants for      * the available values.      *      * @return the relationship.      */
specifier|public
name|String
name|getReplyType
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
literal|"RT"
argument_list|,
name|RT_REPLY
argument_list|)
return|;
block|}
comment|/**      * This will set the Reply Type (relationship) with the annotation in the IRT entry See the RT_* constants for the      * available values.      *      * @param rt the reply type.      */
specifier|public
name|void
name|setReplyType
parameter_list|(
name|String
name|rt
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
literal|"RT"
argument_list|,
name|rt
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the intent of the annotation The values and meanings are specific to the actual annotation See      * the IT_* constants for the annotation classes.      *      * @return the intent      */
specifier|public
name|String
name|getIntent
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
name|IT
argument_list|)
return|;
block|}
comment|/**      * This will set the intent of the annotation The values and meanings are specific to the actual annotation See the      * IT_* constants for the annotation classes.      *      * @param it the intent      */
specifier|public
name|void
name|setIntent
parameter_list|(
name|String
name|it
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|IT
argument_list|,
name|it
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the external data dictionary.      *       * @return the external data dictionary      */
specifier|public
name|PDExternalDataDictionary
name|getExternalData
parameter_list|()
block|{
name|COSBase
name|exData
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"ExData"
argument_list|)
decl_stmt|;
if|if
condition|(
name|exData
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|new
name|PDExternalDataDictionary
argument_list|(
operator|(
name|COSDictionary
operator|)
name|exData
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the external data dictionary.      *       * @param externalData the external data dictionary      */
specifier|public
name|void
name|setExternalData
parameter_list|(
name|PDExternalDataDictionary
name|externalData
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"ExData"
argument_list|,
name|externalData
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

