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
name|javacc
operator|.
name|ParseException
import|;
end_import

begin_comment
comment|/**  * A PdfParseException is thrown when the JavaCC Parser can't validate the pdf  * file. In this case a PdfParseException (or an inherited class) is thrown.  *   * A PdfParseException inherits from ParseException to avoid compilation errors  * in the classes generated by JavaCC.  */
end_comment

begin_class
specifier|public
class|class
name|PdfParseException
extends|extends
name|ParseException
block|{
specifier|protected
name|boolean
name|isTokenMgrError
init|=
literal|false
decl_stmt|;
specifier|protected
name|String
name|errorCode
init|=
literal|null
decl_stmt|;
specifier|protected
name|int
name|line
init|=
literal|0
decl_stmt|;
comment|/**    * This constructor clones the given ParseException and initialize the    * errorCode if it is possible. (e is an instance of PdfParseException)    *     * @param e    */
specifier|public
name|PdfParseException
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|currentToken
operator|=
name|e
operator|.
name|currentToken
expr_stmt|;
name|this
operator|.
name|expectedTokenSequences
operator|=
name|e
operator|.
name|expectedTokenSequences
expr_stmt|;
name|this
operator|.
name|tokenImage
operator|=
name|e
operator|.
name|tokenImage
expr_stmt|;
name|this
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
if|if
condition|(
name|e
operator|instanceof
name|PdfParseException
condition|)
block|{
name|this
operator|.
name|errorCode
operator|=
operator|(
operator|(
name|PdfParseException
operator|)
name|e
operator|)
operator|.
name|errorCode
expr_stmt|;
block|}
block|}
comment|/**    * This constructor calls the PdfParseException(String message, String code)    * constructor with a code set to null.    *     * @param message    *          the explanation message (The message of TokenMngError). This field    *          is mandatory.    */
specifier|public
name|PdfParseException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**    * This constructor is generally used when the PDF validation fails due to a    * TokenMngError. In this case, the message should be the message of the    * TokenMngError.    *     * @param message    *          the explanation message (The message of TokenMngError). This field    *          is mandatory.    * @param code    *          the error code if it can be determined by the creator of this    *          exception (Can be null)    */
specifier|public
name|PdfParseException
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|isTokenMgrError
operator|=
literal|true
expr_stmt|;
name|int
name|lineIndex
init|=
name|message
operator|.
name|indexOf
argument_list|(
literal|"Lexical error at line "
argument_list|)
decl_stmt|;
if|if
condition|(
name|lineIndex
operator|>
operator|-
literal|1
condition|)
block|{
name|String
name|truncMsg
init|=
name|message
operator|.
name|replace
argument_list|(
literal|"Lexical error at line "
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|String
name|nbLine
init|=
name|truncMsg
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|truncMsg
operator|.
name|indexOf
argument_list|(
literal|","
argument_list|)
argument_list|)
decl_stmt|;
name|line
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|nbLine
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|errorCode
operator|=
name|code
expr_stmt|;
block|}
comment|/**    * Get the validation error code    *     * @return    */
specifier|public
name|String
name|getErrorCode
parameter_list|()
block|{
return|return
name|this
operator|.
name|errorCode
return|;
block|}
block|}
end_class

end_unit

