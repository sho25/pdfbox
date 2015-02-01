begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2015 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|process
operator|.
name|reflect
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
name|COSDocument
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
name|COSObject
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDDestination
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
name|PreflightConstants
import|;
end_import

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
name|ERROR_SYNTAX_DICT_INVALID
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
name|PreflightPath
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
name|exception
operator|.
name|ValidationException
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
name|process
operator|.
name|AbstractProcess
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
name|utils
operator|.
name|COSUtils
import|;
end_import

begin_comment
comment|/**  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|DestinationValidationProcess
extends|extends
name|AbstractProcess
block|{
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PreflightPath
name|vPath
init|=
name|ctx
operator|.
name|getValidationPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|vPath
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|vPath
operator|.
name|isExpectedType
argument_list|(
name|COSBase
operator|.
name|class
argument_list|)
condition|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_ACTION_INVALID_TYPE
argument_list|,
literal|"Destination validation process needs at least one COSBase object"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|COSBase
name|dest
init|=
operator|(
name|COSBase
operator|)
name|vPath
operator|.
name|peek
argument_list|()
decl_stmt|;
comment|// "A destination may be specified either explicitly by
comment|// an array of parameters defining its properties or indirectly by name."
name|COSDocument
name|cosDocument
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|dest
operator|instanceof
name|COSName
operator|||
name|COSUtils
operator|.
name|isString
argument_list|(
name|dest
argument_list|,
name|cosDocument
argument_list|)
operator|||
name|COSUtils
operator|.
name|isArray
argument_list|(
name|dest
argument_list|,
name|cosDocument
argument_list|)
operator|)
condition|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
literal|"Destination type entry "
operator|+
name|dest
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" is invalid"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|dest
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|ar
init|=
operator|(
name|COSArray
operator|)
name|dest
decl_stmt|;
if|if
condition|(
name|ar
operator|.
name|size
argument_list|()
operator|<
literal|2
condition|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
literal|"Destination array must have at least 2 elements"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|validateExplicitDestination
argument_list|(
name|ctx
argument_list|,
name|ar
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|PDDestination
operator|.
name|create
argument_list|(
name|dest
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|validateExplicitDestination
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|COSArray
name|ar
parameter_list|)
block|{
comment|// "In each case, page is an indirect reference to a page object."
if|if
condition|(
name|ar
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|COSObject
condition|)
block|{
name|COSObject
name|ob
init|=
operator|(
name|COSObject
operator|)
name|ar
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|COSBase
name|type
init|=
name|ob
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|PAGE
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
try|try
block|{
name|PDDestination
operator|.
name|create
argument_list|(
name|ar
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
block|}
name|ctx
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
literal|"First element in Destination array entry must be an indirect reference to a dictionary of /Type /Page, but is "
operator|+
name|ar
operator|.
name|getName
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

