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
name|pdfbox
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
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_ANNOT_NOT_RECOMMENDED_FLAG
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
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationText
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
name|preflight
operator|.
name|PreflightContext
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
name|preflight
operator|.
name|ValidationResult
operator|.
name|ValidationError
import|;
end_import

begin_comment
comment|/**  * Validation class for Text Annotation  */
end_comment

begin_class
specifier|public
class|class
name|TextAnnotationValidator
extends|extends
name|AnnotationValidator
block|{
comment|/**      * PDFBox object which wraps the annotation dictionary      */
specifier|protected
name|PDAnnotationText
name|pdText
init|=
literal|null
decl_stmt|;
specifier|public
name|TextAnnotationValidator
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|COSDictionary
name|annotDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|ctx
argument_list|,
name|annotDictionary
argument_list|)
expr_stmt|;
name|this
operator|.
name|pdText
operator|=
operator|new
name|PDAnnotationText
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
name|pdText
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.pdfbox.preflight.annotation.AnnotationValidator#checkFlags( java.util.List)      */
annotation|@
name|Override
specifier|protected
name|boolean
name|checkFlags
parameter_list|()
block|{
comment|// call common flags settings
name|boolean
name|result
init|=
name|super
operator|.
name|checkFlags
argument_list|()
decl_stmt|;
comment|/*          * For Text Annotation, this two flags should be set to avoid potential ambiguity between the annotation          * dictionary and the reader behavior.          */
name|result
operator|=
name|result
operator|&&
name|this
operator|.
name|pdAnnot
operator|.
name|isNoRotate
argument_list|()
expr_stmt|;
name|result
operator|=
name|result
operator|&&
name|this
operator|.
name|pdAnnot
operator|.
name|isNoZoom
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|result
condition|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_ANNOT_NOT_RECOMMENDED_FLAG
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

