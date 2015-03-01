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
name|exception
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
name|preflight
operator|.
name|PreflightConstants
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
name|javacc
operator|.
name|PDFParserConstants
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
name|javacc
operator|.
name|ParseException
import|;
end_import

begin_comment
comment|/**  * This Exception is thrown if an validation error occurs during the javacc validation in the PDF Body.  *   * Error codes provided by this exception should start by 1.2 or 1.0.  */
end_comment

begin_class
specifier|public
class|class
name|BodyParseException
extends|extends
name|PdfParseException
block|{
comment|/*      * (non-Javadoc)      *       * @see net.awl.edoc.pdfa.validation.PdfParseException#PdfParseException(net.awl      * .edoc.pdfa.validation.ParseException)      */
specifier|public
name|BodyParseException
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|super
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see net.awl.edoc.pdfa.validation.PdfParseException#PdfParseException(java.lang .String,java.lang.String)      */
specifier|public
name|BodyParseException
parameter_list|(
name|String
name|message
parameter_list|,
name|String
name|code
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|code
argument_list|)
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see net.awl.edoc.pdfa.validation.PdfParseException#PdfParseException(java.lang .String)      */
specifier|public
name|BodyParseException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see net.awl.edoc.pdfa.validation.PdfParseException#getErrorCode()      */
annotation|@
name|Override
specifier|public
name|String
name|getErrorCode
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|errorCode
operator|!=
literal|null
condition|)
block|{
return|return
name|this
operator|.
name|errorCode
return|;
block|}
comment|// default error code
name|this
operator|.
name|errorCode
operator|=
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_BODY
expr_stmt|;
comment|// it is a parse error, according to the ExpectedTokens data
comment|// some error code can be returned
if|if
condition|(
operator|!
name|isTokenMgrError
operator|&&
name|this
operator|.
name|expectedTokenSequences
operator|!=
literal|null
condition|)
block|{
comment|// check object delimiters error
for|for
control|(
name|int
name|expectedTokenSequence
index|[]
range|:
name|this
operator|.
name|expectedTokenSequences
control|)
block|{
comment|// Check only the first expected token on each array.
comment|// Others can be checked if some choice can started by the same token
comment|// in this case, a factorization is possible
switch|switch
condition|(
name|expectedTokenSequence
index|[
literal|0
index|]
condition|)
block|{
case|case
name|PDFParserConstants
operator|.
name|START_OBJECT
case|:
case|case
name|PDFParserConstants
operator|.
name|END_OBJECT
case|:
name|this
operator|.
name|errorCode
operator|=
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_OBJ_DELIMITER
expr_stmt|;
break|break;
block|}
block|}
comment|// add other error code here
block|}
return|return
name|errorCode
return|;
block|}
block|}
end_class

end_unit

