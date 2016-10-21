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
name|pdmodel
operator|.
name|interactive
operator|.
name|action
operator|.
name|PDActionFactory
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
name|action
operator|.
name|PDAnnotationAdditionalActions
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
name|action
operator|.
name|PDAction
import|;
end_import

begin_comment
comment|/**  * This is the class that represents a widget.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationWidget
extends|extends
name|PDAnnotation
block|{
comment|/**      * The type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"Widget"
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDAnnotationWidget
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
comment|/**      * Creates a PDWidget from a COSDictionary, expected to be a correct object definition for a field in PDF.      *      * @param field the PDF object to represent as a field.      */
specifier|public
name|PDAnnotationWidget
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
comment|/**      * Returns the highlighting mode. Default value:<code>I</code>      *<dl>      *<dt><code>N</code></dt>      *<dd>(None) No highlighting.</dd>      *<dt><code>I</code></dt>      *<dd>(Invert) Invert the contents of the annotation rectangle.</dd>      *<dt><code>O</code></dt>      *<dd>(Outline) Invert the annotation's border.</dd>      *<dt><code>P</code></dt>      *<dd>(Push) Display the annotation's down appearance, if any. If no down appearance is defined, the contents of      * the annotation rectangle shall be offset to appear as if it were pushed below the surface of the page</dd>      *<dt><code>T</code></dt>      *<dd>(Toggle) Same as<code>P</code> (which is preferred).</dd>      *</dl>      *       * @return the highlighting mode      */
specifier|public
name|String
name|getHighlightingMode
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|H
argument_list|,
literal|"I"
argument_list|)
return|;
block|}
comment|/**      * Sets the highlighting mode.      *<dl>      *<dt><code>N</code></dt>      *<dd>(None) No highlighting.</dd>      *<dt><code>I</code></dt>      *<dd>(Invert) Invert the contents of the annotation rectangle.</dd>      *<dt><code>O</code></dt>      *<dd>(Outline) Invert the annotation's border.</dd>      *<dt><code>P</code></dt>      *<dd>(Push) Display the annotation's down appearance, if any. If no down appearance is defined, the contents of      * the annotation rectangle shall be offset to appear as if it were pushed below the surface of the page</dd>      *<dt><code>T</code></dt>      *<dd>(Toggle) Same as<code>P</code> (which is preferred).</dd>      *</dl>      *       * @param highlightingMode the highlighting mode the defined values      */
specifier|public
name|void
name|setHighlightingMode
parameter_list|(
name|String
name|highlightingMode
parameter_list|)
block|{
if|if
condition|(
operator|(
name|highlightingMode
operator|==
literal|null
operator|)
operator|||
literal|"N"
operator|.
name|equals
argument_list|(
name|highlightingMode
argument_list|)
operator|||
literal|"I"
operator|.
name|equals
argument_list|(
name|highlightingMode
argument_list|)
operator|||
literal|"O"
operator|.
name|equals
argument_list|(
name|highlightingMode
argument_list|)
operator|||
literal|"P"
operator|.
name|equals
argument_list|(
name|highlightingMode
argument_list|)
operator|||
literal|"T"
operator|.
name|equals
argument_list|(
name|highlightingMode
argument_list|)
condition|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|H
argument_list|,
name|highlightingMode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Valid values for highlighting mode are "
operator|+
literal|"'N', 'N', 'O', 'P' or 'T'"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns the appearance characteristics dictionary.      *       * @return the appearance characteristics dictionary      */
specifier|public
name|PDAppearanceCharacteristicsDictionary
name|getAppearanceCharacteristics
parameter_list|()
block|{
name|COSBase
name|mk
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|MK
argument_list|)
decl_stmt|;
if|if
condition|(
name|mk
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|new
name|PDAppearanceCharacteristicsDictionary
argument_list|(
operator|(
name|COSDictionary
operator|)
name|mk
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the appearance characteristics dictionary.      *       * @param appearanceCharacteristics the appearance characteristics dictionary      */
specifier|public
name|void
name|setAppearanceCharacteristics
parameter_list|(
name|PDAppearanceCharacteristicsDictionary
name|appearanceCharacteristics
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|MK
argument_list|,
name|appearanceCharacteristics
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the action to be performed when this annotation is to be activated.      *      * @return The action to be performed when this annotation is activated.      */
specifier|public
name|PDAction
name|getAction
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|A
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
name|PDActionFactory
operator|.
name|createAction
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
comment|/**      * Set the annotation action. As of PDF 1.6 this is only used for Widget Annotations      *       * @param action The annotation action.      */
specifier|public
name|void
name|setAction
parameter_list|(
name|PDAction
name|action
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|A
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the additional actions for this field. This will return null if there are no additional actions for this      * field. As of PDF 1.6 this is only used for Widget Annotations.      *      * @return The actions of the field.      */
specifier|public
name|PDAnnotationAdditionalActions
name|getActions
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AA
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
name|PDAnnotationAdditionalActions
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
comment|/**      * Set the actions of the field.      *      * @param actions The field actions.      */
specifier|public
name|void
name|setActions
parameter_list|(
name|PDAnnotationAdditionalActions
name|actions
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AA
argument_list|,
name|actions
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the border style dictionary, specifying the width and dash pattern used in drawing the line.      *      * @param bs the border style dictionary to set.      *      */
specifier|public
name|void
name|setBorderStyle
parameter_list|(
name|PDBorderStyleDictionary
name|bs
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BS
argument_list|,
name|bs
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border style dictionary, specifying the width and dash pattern used in drawing the line.      *      * @return the border style dictionary.      */
specifier|public
name|PDBorderStyleDictionary
name|getBorderStyle
parameter_list|()
block|{
name|COSBase
name|bs
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BS
argument_list|)
decl_stmt|;
if|if
condition|(
name|bs
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|new
name|PDBorderStyleDictionary
argument_list|(
operator|(
name|COSDictionary
operator|)
name|bs
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|// TODO where to get acroForm from?
comment|// public PDField getParent() throws IOException
comment|// {
comment|// COSBase parent = this.getCOSObject().getDictionaryObject(COSName.PARENT);
comment|// if (parent instanceof COSDictionary)
comment|// {
comment|// PDAcroForm acroForm = null;
comment|// return PDFieldFactory.createField(acroForm, (COSDictionary) parent);
comment|// }
comment|// return null;
comment|// }
block|}
end_class

end_unit

