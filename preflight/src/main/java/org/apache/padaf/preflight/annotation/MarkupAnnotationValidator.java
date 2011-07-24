begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|annotation
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ANNOT_DICTIONARY_KEY_CONTENTS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ANNOT_DICTIONARY_KEY_F
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ANNOT_DICTIONARY_KEY_QUADPOINTS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ANNOT_DICTIONARY_KEY_RECT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|DICTIONARY_KEY_SUBTYPE
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
name|padaf
operator|.
name|preflight
operator|.
name|DocumentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationResult
operator|.
name|ValidationError
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
name|annotation
operator|.
name|PDAnnotationTextMarkup
import|;
end_import

begin_comment
comment|/**  * Validation class for the MarkupAnnotation  */
end_comment

begin_class
specifier|public
class|class
name|MarkupAnnotationValidator
extends|extends
name|AnnotationValidator
block|{
comment|/**    * PDFBox object which wraps the annotation dictionary    */
specifier|protected
name|PDAnnotationTextMarkup
name|pdMarkup
init|=
literal|null
decl_stmt|;
specifier|public
name|MarkupAnnotationValidator
parameter_list|(
name|DocumentHandler
name|handler
parameter_list|,
name|COSDictionary
name|annotDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|handler
argument_list|,
name|annotDictionary
argument_list|)
expr_stmt|;
name|this
operator|.
name|pdMarkup
operator|=
operator|new
name|PDAnnotationTextMarkup
argument_list|(
name|annotDictionary
argument_list|)
expr_stmt|;
name|this
operator|.
name|pdAnnot
operator|=
name|this
operator|.
name|pdMarkup
expr_stmt|;
block|}
comment|/*    * (non-Javadoc)    *     * @seenet.awl.edoc.pdfa.validation.annotation.AnnotationValidator#    * checkMandatoryFields(java.util.List)    */
specifier|protected
name|boolean
name|checkMandatoryFields
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
block|{
name|boolean
name|subtype
init|=
literal|false
decl_stmt|;
name|boolean
name|rect
init|=
literal|false
decl_stmt|;
name|boolean
name|f
init|=
literal|false
decl_stmt|;
name|boolean
name|contents
init|=
literal|false
decl_stmt|;
name|boolean
name|qp
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|this
operator|.
name|annotDictionary
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|key
operator|instanceof
name|COSName
operator|)
condition|)
block|{
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_DICTIONARY_KEY_INVALID
argument_list|,
literal|"Invalid key in The Annotation dictionary"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|String
name|cosName
init|=
operator|(
operator|(
name|COSName
operator|)
name|key
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|ANNOT_DICTIONARY_KEY_RECT
argument_list|)
condition|)
block|{
name|rect
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|DICTIONARY_KEY_SUBTYPE
argument_list|)
condition|)
block|{
name|subtype
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|ANNOT_DICTIONARY_KEY_F
argument_list|)
condition|)
block|{
name|f
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|ANNOT_DICTIONARY_KEY_CONTENTS
argument_list|)
condition|)
block|{
name|contents
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|ANNOT_DICTIONARY_KEY_QUADPOINTS
argument_list|)
condition|)
block|{
name|qp
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|boolean
name|result
init|=
operator|(
name|subtype
operator|&&
name|rect
operator|&&
name|f
operator|&&
name|contents
operator|&&
name|qp
operator|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
condition|)
block|{
name|errors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_ANNOT_MISSING_FIELDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

