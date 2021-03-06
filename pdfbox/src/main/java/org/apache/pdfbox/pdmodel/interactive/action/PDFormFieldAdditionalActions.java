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
name|action
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
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This class represents a form field's dictionary of actions  * that occur due to events.  *  * @author Ben Litchfield  * @author Panagiotis Toumasis  */
end_comment

begin_class
specifier|public
class|class
name|PDFormFieldAdditionalActions
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|final
name|COSDictionary
name|actions
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDFormFieldAdditionalActions
parameter_list|()
block|{
name|actions
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param a The action dictionary.      */
specifier|public
name|PDFormFieldAdditionalActions
parameter_list|(
name|COSDictionary
name|a
parameter_list|)
block|{
name|actions
operator|=
name|a
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|actions
return|;
block|}
comment|/**      * This will get a JavaScript action to be performed when the user      * types a keystroke into a text field or combo box or modifies the      * selection in a scrollable list box. This allows the keystroke to      * be checked for validity and rejected or modified.      *      * @return The K entry of form field's additional actions dictionary.      */
specifier|public
name|PDAction
name|getK
parameter_list|()
block|{
name|COSDictionary
name|k
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|K
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|k
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|PDActionFactory
operator|.
name|createAction
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a JavaScript action to be performed when the user      * types a keystroke into a text field or combo box or modifies the      * selection in a scrollable list box. This allows the keystroke to      * be checked for validity and rejected or modified.      *      * @param k The action to be performed.      */
specifier|public
name|void
name|setK
parameter_list|(
name|PDAction
name|k
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a JavaScript action to be performed before      * the field is formatted to display its current value. This      * allows the field's value to be modified before formatting.      *      * @return The F entry of form field's additional actions dictionary.      */
specifier|public
name|PDAction
name|getF
parameter_list|()
block|{
name|COSDictionary
name|f
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|F
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|PDActionFactory
operator|.
name|createAction
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a JavaScript action to be performed before      * the field is formatted to display its current value. This      * allows the field's value to be modified before formatting.      *      * @param f The action to be performed.      */
specifier|public
name|void
name|setF
parameter_list|(
name|PDAction
name|f
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a JavaScript action to be performed      * when the field's value is changed. This allows the      * new value to be checked for validity.      * The name V stands for "validate".      *      * @return The V entry of form field's additional actions dictionary.      */
specifier|public
name|PDAction
name|getV
parameter_list|()
block|{
name|COSDictionary
name|v
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|PDActionFactory
operator|.
name|createAction
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a JavaScript action to be performed      * when the field's value is changed. This allows the      * new value to be checked for validity.      * The name V stands for "validate".      *      * @param v The action to be performed.      */
specifier|public
name|void
name|setV
parameter_list|(
name|PDAction
name|v
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a JavaScript action to be performed in order to recalculate      * the value of this field when that of another field changes. The order in which      * the document's fields are recalculated is defined by the CO entry in the      * interactive form dictionary.      * The name C stands for "calculate".      *      * @return The C entry of form field's additional actions dictionary.      */
specifier|public
name|PDAction
name|getC
parameter_list|()
block|{
name|COSDictionary
name|c
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|C
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|PDActionFactory
operator|.
name|createAction
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a JavaScript action to be performed in order to recalculate      * the value of this field when that of another field changes. The order in which      * the document's fields are recalculated is defined by the CO entry in the      * interactive form dictionary.      * The name C stands for "calculate".      *      * @param c The action to be performed.      */
specifier|public
name|void
name|setC
parameter_list|(
name|PDAction
name|c
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

