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
name|PDTextAppearanceHandler
import|;
end_import

begin_comment
comment|/**  * This is the class that represents a text annotation.  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationText
extends|extends
name|PDAnnotationMarkup
block|{
specifier|private
name|PDAppearanceHandler
name|customAppearanceHandler
decl_stmt|;
comment|/*      * The various values of the Text as defined in the PDF 1.7 reference Table 172      */
comment|/**      * Constant for the name of a text annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_COMMENT
init|=
literal|"Comment"
decl_stmt|;
comment|/**      * Constant for the name of a text annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_KEY
init|=
literal|"Key"
decl_stmt|;
comment|/**      * Constant for the name of a text annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_NOTE
init|=
literal|"Note"
decl_stmt|;
comment|/**      * Constant for the name of a text annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_HELP
init|=
literal|"Help"
decl_stmt|;
comment|/**      * Constant for the name of a text annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_NEW_PARAGRAPH
init|=
literal|"NewParagraph"
decl_stmt|;
comment|/**      * Constant for the name of a text annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_PARAGRAPH
init|=
literal|"Paragraph"
decl_stmt|;
comment|/**      * Constant for the name of a text annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_INSERT
init|=
literal|"Insert"
decl_stmt|;
comment|/**      * Constant for the name of a circle annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_CIRCLE
init|=
literal|"Circle"
decl_stmt|;
comment|/**      * Constant for the name of a cross annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_CROSS
init|=
literal|"Cross"
decl_stmt|;
comment|/**      * Constant for the name of a star annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_STAR
init|=
literal|"Star"
decl_stmt|;
comment|/**      * Constant for the name of a check annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_CHECK
init|=
literal|"Check"
decl_stmt|;
comment|/**      * Constant for the name of a right arrow annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_RIGHT_ARROW
init|=
literal|"RightArrow"
decl_stmt|;
comment|/**      * Constant for the name of a right pointer annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_RIGHT_POINTER
init|=
literal|"RightPointer"
decl_stmt|;
comment|/**      * The type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"Text"
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDAnnotationText
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
comment|/**      * Creates a Text annotation from a COSDictionary, expected to be a correct object definition.      *      * @param field the PDF object to represent as a field.      */
specifier|public
name|PDAnnotationText
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
comment|/**      * This will set initial state of the annotation, open or closed.      *      * @param open Boolean value, true = open false = closed      */
specifier|public
name|void
name|setOpen
parameter_list|(
name|boolean
name|open
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Open"
argument_list|)
argument_list|,
name|open
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the initial state of the annotation, open Or closed (default closed).      *      * @return The initial state, true = open false = closed      */
specifier|public
name|boolean
name|getOpen
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Open"
argument_list|)
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * This will set the name (and hence appearance, AP taking precedence) For this annotation. See the NAME_XXX      * constants for valid values.      *      * @param name The name of the annotation      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the name (and hence appearance, AP taking precedence) For this annotation. The default is      * NOTE.      *      * @return The name of this annotation, see the NAME_XXX constants.      */
specifier|public
name|String
name|getName
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
name|NAME
argument_list|,
name|NAME_NOTE
argument_list|)
return|;
block|}
comment|/**      * This will retrieve the annotation state.      *       * @return the annotation state      */
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|STATE
argument_list|)
return|;
block|}
comment|/**      * This will set the annotation state.      *       * @param state the annotation state      */
specifier|public
name|void
name|setState
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|STATE
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the annotation state model.      *       * @return the annotation state model      */
specifier|public
name|String
name|getStateModel
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|STATE_MODEL
argument_list|)
return|;
block|}
comment|/**      * This will set the annotation state model. Allowed values are "Marked" and "Review"      *       * @param stateModel the annotation state model      */
specifier|public
name|void
name|setStateModel
parameter_list|(
name|String
name|stateModel
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|STATE_MODEL
argument_list|,
name|stateModel
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|customAppearanceHandler
operator|==
literal|null
condition|)
block|{
name|PDTextAppearanceHandler
name|appearanceHandler
init|=
operator|new
name|PDTextAppearanceHandler
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

