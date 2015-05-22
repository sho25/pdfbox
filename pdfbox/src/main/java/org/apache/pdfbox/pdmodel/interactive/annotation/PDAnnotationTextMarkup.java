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
name|COSName
import|;
end_import

begin_comment
comment|/**  * This is the abstract class that represents a text markup annotation  * Introduced in PDF 1.3 specification, except Squiggly lines in 1.4.  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationTextMarkup
extends|extends
name|PDAnnotationMarkup
block|{
comment|/**      * The types of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_HIGHLIGHT
init|=
literal|"Highlight"
decl_stmt|;
comment|/**      * The types of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_UNDERLINE
init|=
literal|"Underline"
decl_stmt|;
comment|/**      * The types of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_SQUIGGLY
init|=
literal|"Squiggly"
decl_stmt|;
comment|/**      * The types of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE_STRIKEOUT
init|=
literal|"StrikeOut"
decl_stmt|;
specifier|private
name|PDAnnotationTextMarkup
parameter_list|()
block|{
comment|// Must be constructed with a subType or dictionary parameter
block|}
comment|/**      * Creates a TextMarkup annotation of the specified sub type.      *      * @param subType the subtype the annotation represents      */
specifier|public
name|PDAnnotationTextMarkup
parameter_list|(
name|String
name|subType
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|setSubtype
argument_list|(
name|subType
argument_list|)
expr_stmt|;
comment|// Quad points are required, set and empty array
name|setQuadPoints
argument_list|(
operator|new
name|float
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a TextMarkup annotation from a COSDictionary, expected to be a      * correct object definition.      *      * @param field the PDF objet to represent as a field.      */
specifier|public
name|PDAnnotationTextMarkup
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
comment|/**      * This will set the set of quadpoints which encompass the areas of this      * annotation.      *      * @param quadPoints      *            an array representing the set of area covered      */
specifier|public
name|void
name|setQuadPoints
parameter_list|(
name|float
index|[]
name|quadPoints
parameter_list|)
block|{
name|COSArray
name|newQuadPoints
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|newQuadPoints
operator|.
name|setFloatArray
argument_list|(
name|quadPoints
argument_list|)
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|QUADPOINTS
argument_list|,
name|newQuadPoints
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the set of quadpoints which encompass the areas of      * this annotation.      *      * @return An array of floats representing the quad points.      */
specifier|public
name|float
index|[]
name|getQuadPoints
parameter_list|()
block|{
name|COSArray
name|quadPoints
init|=
operator|(
name|COSArray
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|QUADPOINTS
argument_list|)
decl_stmt|;
if|if
condition|(
name|quadPoints
operator|!=
literal|null
condition|)
block|{
return|return
name|quadPoints
operator|.
name|toFloatArray
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
comment|// Should never happen as this is a required item
block|}
block|}
comment|/**      * This will set the sub type (and hence appearance, AP taking precedence) For      * this annotation. See the SUB_TYPE_XXX constants for valid values.      *      * @param subType The subtype of the annotation      */
specifier|public
name|void
name|setSubtype
parameter_list|(
name|String
name|subType
parameter_list|)
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
name|subType
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the sub type (and hence appearance, AP taking precedence)      * For this annotation.      *      * @return The subtype of this annotation, see the SUB_TYPE_XXX constants.      */
specifier|public
name|String
name|getSubtype
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
name|SUBTYPE
argument_list|)
return|;
block|}
block|}
end_class

end_unit

