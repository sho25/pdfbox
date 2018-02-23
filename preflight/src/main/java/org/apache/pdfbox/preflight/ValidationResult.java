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
package|;
end_package

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
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_comment
comment|/**  * Object returned by the validate method of the PDFValidator. This object contains a boolean to know if the PDF is  * PDF/A-1<I>x</I> compliant. If the document isn't PDF/A-1<I>x</I> a list of errors is provided.  */
end_comment

begin_class
specifier|public
class|class
name|ValidationResult
block|{
comment|/**      * Boolean to know if the PDF is a valid PDF/A      */
specifier|private
name|boolean
name|isValid
init|=
literal|false
decl_stmt|;
comment|/**      * Errors to know why the PDF isn't valid. If the PDF is valid, this list is empty.      */
specifier|private
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lErrors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Object representation of the XMPMetaData contained by the pdf file This attribute can be null if the Validation      * fails.      */
specifier|private
name|XMPMetadata
name|xmpMetaData
init|=
literal|null
decl_stmt|;
comment|/**      * Create a Validation result object      *       * @param isValid      */
specifier|public
name|ValidationResult
parameter_list|(
name|boolean
name|isValid
parameter_list|)
block|{
name|this
operator|.
name|isValid
operator|=
name|isValid
expr_stmt|;
block|}
comment|/**      * Create a Validation Result object. This constructor force the isValid to false and add the given error to the      * list or ValidationErrors.      *       * @param error      *            if error is null, no error is added to the list.      */
specifier|public
name|ValidationResult
parameter_list|(
name|ValidationError
name|error
parameter_list|)
block|{
name|this
operator|.
name|isValid
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|lErrors
operator|.
name|add
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create a Validation Result object. This constructor force the isValid to false and add all the given errors to      * the list or ValidationErrors.      *       * @param errors      *            if error is null, no error is added to the list.      */
specifier|public
name|ValidationResult
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
block|{
name|this
operator|.
name|isValid
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|lErrors
operator|=
name|errors
expr_stmt|;
block|}
comment|/**      * Add the ValidationError object of the otherResult in the Error list of the current object. Apply a logical AND on      * the isValid boolean.      *       * @param otherResult      */
specifier|public
name|void
name|mergeResult
parameter_list|(
name|ValidationResult
name|otherResult
parameter_list|)
block|{
if|if
condition|(
name|otherResult
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|lErrors
operator|.
name|addAll
argument_list|(
name|otherResult
operator|.
name|getErrorsList
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|isValid
operator|&=
name|otherResult
operator|.
name|isValid
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @return the xmpMetaData      */
specifier|public
name|XMPMetadata
name|getXmpMetaData
parameter_list|()
block|{
return|return
name|xmpMetaData
return|;
block|}
comment|/**      * @param xmpMetaData      *            the xmpMetaData to set      */
name|void
name|setXmpMetaData
parameter_list|(
name|XMPMetadata
name|xmpMetaData
parameter_list|)
block|{
name|this
operator|.
name|xmpMetaData
operator|=
name|xmpMetaData
expr_stmt|;
block|}
comment|/**      * @return true if the PDF is valid,false otherwise      */
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
return|return
name|isValid
return|;
block|}
comment|/**      * Add error to the list of ValidationError. If the given error is null, this method does nothing      *       * @param error      */
specifier|public
name|void
name|addError
parameter_list|(
name|ValidationError
name|error
parameter_list|)
block|{
if|if
condition|(
name|error
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|isValid
operator|&=
name|error
operator|.
name|isWarning
argument_list|()
expr_stmt|;
name|this
operator|.
name|lErrors
operator|.
name|add
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Add a set of errors to the list of ValidationError. If the given list is null, this method does nothing.      *       * @param errors      */
specifier|public
name|void
name|addErrors
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
block|{
if|if
condition|(
name|errors
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ValidationError
name|validationError
range|:
name|errors
control|)
block|{
name|addError
argument_list|(
name|validationError
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @return the list of validation errors      */
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|getErrorsList
parameter_list|()
block|{
return|return
name|this
operator|.
name|lErrors
return|;
block|}
comment|/**      * This Class represents an error of validation. It contains an error code and an error explanation.      */
specifier|public
specifier|static
class|class
name|ValidationError
block|{
comment|/**          * Error identifier. This error code can be used as identifier to internationalize the logging message using          * i18n.          */
specifier|private
specifier|final
name|String
name|errorCode
decl_stmt|;
comment|/**          * Error details          */
specifier|private
name|String
name|details
decl_stmt|;
comment|/**          * false: this error can't be ignored; true: this error can be ignored          */
specifier|private
name|boolean
name|isWarning
init|=
literal|false
decl_stmt|;
comment|// TODO Add here COSObject or the PDObject that is linked to the error may a automatic fix can be done.
comment|/**          * Always record the place in the source code where the ValidationError          * was created, in case the ValidationError was not caused by a          * Throwable.          */
specifier|private
name|Throwable
name|t
init|=
literal|null
decl_stmt|;
comment|/**          * Get the place where the ValidationError was created, useful if the          * ValidationError was not caused by a Throwable.          *          * @return The place where the ValidationError was created.          */
specifier|public
name|Throwable
name|getThrowable
parameter_list|()
block|{
return|return
name|t
return|;
block|}
comment|/**          * The underlying cause if the ValidationError was caused by a Throwable.          */
specifier|private
name|Throwable
name|cause
init|=
literal|null
decl_stmt|;
comment|/**          * The page number on which the error happened, if known.          */
specifier|private
name|Integer
name|pageNumber
init|=
literal|null
decl_stmt|;
comment|/**          * Get the underlying cause if the ValidationError was caused by a          * Throwable.          *          * @return The underlying cause if the ValidationError was caused by a          * Throwable, or null if not.          */
specifier|public
name|Throwable
name|getCause
parameter_list|()
block|{
return|return
name|cause
return|;
block|}
comment|/**          * Returns the page number, or null if not known.          */
specifier|public
name|Integer
name|getPageNumber
parameter_list|()
block|{
return|return
name|pageNumber
return|;
block|}
comment|/**          * Sets or resets the page number.          *          * @param pageNumber zero based page number or null if none is known.          */
specifier|public
name|void
name|setPageNumber
parameter_list|(
name|Integer
name|pageNumber
parameter_list|)
block|{
name|this
operator|.
name|pageNumber
operator|=
name|pageNumber
expr_stmt|;
block|}
comment|/**          * Create a validation error with the given error code          *           * @param errorCode          */
specifier|public
name|ValidationError
parameter_list|(
name|String
name|errorCode
parameter_list|)
block|{
name|this
operator|.
name|errorCode
operator|=
name|errorCode
expr_stmt|;
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_COMMON
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Syntax error"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_HEADER
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Header Syntax error"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_BODY
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Body Syntax error"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_CROSS_REF
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"CrossRef Syntax error"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_TRAILER_OUTLINES_INVALID
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Outlines invalid"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_TRAILER
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Trailer Syntax error"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_GRAPHIC_INVALID
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Invalid graphics object"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_GRAPHIC_TRANSPARENCY
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Invalid graphics transparency"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Unexpected value for key in Graphic object definition"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_GRAPHIC_UNEXPECTED_KEY
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Unexpected key in Graphic object definition"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Invalid Color space"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_GRAPHIC_MAIN
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Unknown graphics error"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_INVALID_DATA
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Invalid Font definition"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_DAMAGED
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Font damaged"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_FONTS_GLYPH
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Glyph error"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_TRANSPARENCY_EXT_GRAPHICAL_STATE
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Transparency error"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_ANNOT_MISSING_FIELDS
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Missing field in an annotation definition"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_ANNOT_FORBIDDEN_ELEMENT
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Forbidden field in an annotation definition"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_ANNOT_INVALID_ELEMENT
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Invalid field value in an annotation definition"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_ACTION_INVALID_ACTIONS
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Invalid action definition"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_ACTION_FORBIDDEN_ACTIONS
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Action is forbidden"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_MAIN
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"Error on MetaData"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|errorCode
operator|.
name|startsWith
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_PDF_PROCESSING_MISSING
argument_list|)
condition|)
block|{
name|this
operator|.
name|details
operator|=
literal|"A Mandatory element is missing"
expr_stmt|;
block|}
else|else
block|{
comment|// default Unkown error
name|this
operator|.
name|details
operator|=
literal|"Unknown error"
expr_stmt|;
block|}
name|t
operator|=
operator|new
name|Exception
argument_list|()
expr_stmt|;
block|}
comment|/**          * Create a validation error with the given error code and the error          * explanation.          *          * @param errorCode the error code          * @param details the error explanation          * @param cause the error cause          */
specifier|public
name|ValidationError
parameter_list|(
name|String
name|errorCode
parameter_list|,
name|String
name|details
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|this
argument_list|(
name|errorCode
argument_list|)
expr_stmt|;
if|if
condition|(
name|details
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|this
operator|.
name|details
operator|.
name|length
argument_list|()
operator|+
name|details
operator|.
name|length
argument_list|()
operator|+
literal|2
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|this
operator|.
name|details
argument_list|)
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|details
argument_list|)
expr_stmt|;
name|this
operator|.
name|details
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|cause
operator|=
name|cause
expr_stmt|;
name|t
operator|=
operator|new
name|Exception
argument_list|()
expr_stmt|;
block|}
comment|/**          * Create a validation error with the given error code and the error          * explanation.          *          * @param errorCode the error code          * @param details the error explanation          */
specifier|public
name|ValidationError
parameter_list|(
name|String
name|errorCode
parameter_list|,
name|String
name|details
parameter_list|)
block|{
name|this
argument_list|(
name|errorCode
argument_list|,
name|details
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**          * @return the error code          */
specifier|public
name|String
name|getErrorCode
parameter_list|()
block|{
return|return
name|errorCode
return|;
block|}
comment|/**          * @return the error explanation          */
specifier|public
name|String
name|getDetails
parameter_list|()
block|{
return|return
name|details
return|;
block|}
comment|/**          * Set the error explanation          *           * @param details          */
specifier|public
name|void
name|setDetails
parameter_list|(
name|String
name|details
parameter_list|)
block|{
name|this
operator|.
name|details
operator|=
name|details
expr_stmt|;
block|}
specifier|public
name|boolean
name|isWarning
parameter_list|()
block|{
return|return
name|isWarning
return|;
block|}
specifier|public
name|void
name|setWarning
parameter_list|(
name|boolean
name|isWarning
parameter_list|)
block|{
name|this
operator|.
name|isWarning
operator|=
name|isWarning
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|errorCode
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|ValidationError
condition|)
block|{
name|ValidationError
name|ve
init|=
operator|(
name|ValidationError
operator|)
name|o
decl_stmt|;
comment|// check errorCode
if|if
condition|(
operator|!
name|errorCode
operator|.
name|equals
argument_list|(
name|ve
operator|.
name|errorCode
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|details
operator|.
name|equals
argument_list|(
name|ve
operator|.
name|details
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|pageNumber
operator|!=
literal|null
operator|&&
name|ve
operator|.
name|pageNumber
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|pageNumber
operator|==
literal|null
operator|&&
name|ve
operator|.
name|pageNumber
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|pageNumber
operator|!=
literal|null
operator|&&
name|ve
operator|.
name|pageNumber
operator|!=
literal|null
operator|&&
name|pageNumber
operator|.
name|compareTo
argument_list|(
name|ve
operator|.
name|pageNumber
argument_list|)
operator|!=
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// check warning
return|return
name|isWarning
operator|==
name|ve
operator|.
name|isWarning
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

