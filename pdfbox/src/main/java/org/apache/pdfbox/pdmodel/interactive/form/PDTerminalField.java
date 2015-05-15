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
name|form
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|COSInteger
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
name|common
operator|.
name|COSArrayList
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
name|fdf
operator|.
name|FDFField
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
name|PDFormFieldAdditionalActions
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
name|PDAnnotationWidget
import|;
end_import

begin_comment
comment|/**  * A field in an interactive form.  * Fields may be one of four types: button, text, choice, or signature.  *  * @author sug  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDTerminalField
extends|extends
name|PDField
block|{
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      */
specifier|protected
name|PDTerminalField
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|)
block|{
name|super
argument_list|(
name|acroForm
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param acroForm The form that this field is part of.      * @param field the PDF object to represent as a field.      * @param parent the parent node of the node      */
name|PDTerminalField
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|,
name|COSDictionary
name|field
parameter_list|,
name|PDNonTerminalField
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|acroForm
argument_list|,
name|field
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the actions of the field.      *       * @param actions The field actions.      */
specifier|public
name|void
name|setActions
parameter_list|(
name|PDFormFieldAdditionalActions
name|actions
parameter_list|)
block|{
name|dictionary
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
annotation|@
name|Override
specifier|public
name|int
name|getFieldFlags
parameter_list|()
block|{
name|int
name|retval
init|=
literal|0
decl_stmt|;
name|COSInteger
name|ff
init|=
operator|(
name|COSInteger
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FF
argument_list|)
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|ff
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|parent
operator|.
name|getFieldFlags
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getFieldType
parameter_list|()
block|{
name|String
name|fieldType
init|=
name|dictionary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|FT
argument_list|)
decl_stmt|;
if|if
condition|(
name|fieldType
operator|==
literal|null
operator|&&
name|parent
operator|!=
literal|null
condition|)
block|{
name|fieldType
operator|=
name|parent
operator|.
name|getFieldType
argument_list|()
expr_stmt|;
block|}
return|return
name|fieldType
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|importFDF
parameter_list|(
name|FDFField
name|fdfField
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|importFDF
argument_list|(
name|fdfField
argument_list|)
expr_stmt|;
name|PDAnnotationWidget
name|widget
init|=
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// fixme: ignores multiple widgets
if|if
condition|(
name|widget
operator|!=
literal|null
condition|)
block|{
name|int
name|annotFlags
init|=
name|widget
operator|.
name|getAnnotationFlags
argument_list|()
decl_stmt|;
name|Integer
name|f
init|=
name|fdfField
operator|.
name|getWidgetFieldFlags
argument_list|()
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|widget
operator|.
name|setAnnotationFlags
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// these are suppose to be ignored if the F is set.
name|Integer
name|setF
init|=
name|fdfField
operator|.
name|getSetWidgetFieldFlags
argument_list|()
decl_stmt|;
if|if
condition|(
name|setF
operator|!=
literal|null
condition|)
block|{
name|annotFlags
operator|=
name|annotFlags
operator||
name|setF
expr_stmt|;
name|widget
operator|.
name|setAnnotationFlags
argument_list|(
name|annotFlags
argument_list|)
expr_stmt|;
block|}
name|Integer
name|clrF
init|=
name|fdfField
operator|.
name|getClearWidgetFieldFlags
argument_list|()
decl_stmt|;
if|if
condition|(
name|clrF
operator|!=
literal|null
condition|)
block|{
comment|// we have to clear the bits of the document fields for every bit that is
comment|// set in this field.
comment|//
comment|// Example:
comment|// docF = 1011
comment|// clrF = 1101
comment|// clrFValue = 0010;
comment|// newValue = 1011& 0010 which is 0010
name|int
name|clrFValue
init|=
name|clrF
decl_stmt|;
name|clrFValue
operator|^=
literal|0xFFFFFFFFL
expr_stmt|;
name|annotFlags
operator|=
name|annotFlags
operator|&
name|clrFValue
expr_stmt|;
name|widget
operator|.
name|setAnnotationFlags
argument_list|(
name|annotFlags
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
name|FDFField
name|exportFDF
parameter_list|()
throws|throws
name|IOException
block|{
name|FDFField
name|fdfField
init|=
operator|new
name|FDFField
argument_list|()
decl_stmt|;
name|fdfField
operator|.
name|setPartialFieldName
argument_list|(
name|getPartialName
argument_list|()
argument_list|)
expr_stmt|;
name|fdfField
operator|.
name|setValue
argument_list|(
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
argument_list|)
expr_stmt|;
comment|// fixme: the old code which was here assumed that Kids were PDField instances,
comment|//        which is never true. They're annotation widgets.
return|return
name|fdfField
return|;
block|}
comment|/**      * Returns the widget annotations associated with this field.      *       * @return The list of widget annotations.      */
specifier|public
name|List
argument_list|<
name|PDAnnotationWidget
argument_list|>
name|getWidgets
parameter_list|()
block|{
name|List
argument_list|<
name|PDAnnotationWidget
argument_list|>
name|widgets
init|=
operator|new
name|ArrayList
argument_list|<
name|PDAnnotationWidget
argument_list|>
argument_list|()
decl_stmt|;
name|COSArray
name|kids
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
name|KIDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|kids
operator|==
literal|null
condition|)
block|{
comment|// the field itself is a widget
name|widgets
operator|.
name|add
argument_list|(
operator|new
name|PDAnnotationWidget
argument_list|(
name|dictionary
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|kids
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// there are multiple widgets
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|kids
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|kid
init|=
name|kids
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|kid
operator|instanceof
name|COSDictionary
condition|)
block|{
name|widgets
operator|.
name|add
argument_list|(
operator|new
name|PDAnnotationWidget
argument_list|(
operator|(
name|COSDictionary
operator|)
name|kid
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|widgets
return|;
block|}
comment|/**      * Sets the field's widget annotations.      *      * @param children The list of widget annotations.      */
specifier|public
name|void
name|setWidgets
parameter_list|(
name|List
argument_list|<
name|PDAnnotationWidget
argument_list|>
name|children
parameter_list|)
block|{
name|COSArray
name|kidsArray
init|=
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|children
argument_list|)
decl_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|,
name|kidsArray
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the single associated widget that is part of this field. This occurs when the      * Widget is embedded in the fields dictionary. Sometimes there are multiple sub widgets      * associated with this field, in which case you want to use getWidgets(). If the kids entry is      * specified, then the first entry in that list will be returned.      *       * @return The widget that is associated with this field.      * @deprecated Fields may have more than one widget, call {@link #getWidgets() instead}.      */
annotation|@
name|Deprecated
specifier|public
name|PDAnnotationWidget
name|getWidget
parameter_list|()
block|{
return|return
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**      * Applies a value change to the field. Generates appearances if required and raises events.      *       * @throws IOException if the appearance couldn't be generated      */
specifier|protected
specifier|final
name|void
name|applyChange
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|acroForm
operator|.
name|getNeedAppearances
argument_list|()
condition|)
block|{
name|constructAppearances
argument_list|()
expr_stmt|;
block|}
comment|// if we supported JavaScript we would raise a field changed event here
block|}
comment|/**      * Constructs appearance streams and appearance dictionaries for all widget annotations.      * Subclasses should not call this method directly but via {@link #applyChange()}.      *       * @throws IOException if the appearance couldn't be generated      */
specifier|abstract
name|void
name|constructAppearances
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
end_class

end_unit

