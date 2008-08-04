begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|cos
package|;
end_package

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|exceptions
operator|.
name|COSVisitorException
import|;
end_import

begin_comment
comment|/**  * An interface for visiting a PDF document at the type (COS) level.  *  * @author Michael Traut  * @version $Revision: 1.6 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|ICOSVisitor
block|{
comment|/**      * Notification of visit to Array object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws COSVisitorException If there is an error while visiting this object.      */
specifier|public
name|Object
name|visitFromArray
parameter_list|(
name|COSArray
name|obj
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
comment|/**      * Notification of visit to boolean object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws COSVisitorException If there is an error while visiting this object.      */
specifier|public
name|Object
name|visitFromBoolean
parameter_list|(
name|COSBoolean
name|obj
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
comment|/**      * Notification of visit to dictionary object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws COSVisitorException If there is an error while visiting this object.      */
specifier|public
name|Object
name|visitFromDictionary
parameter_list|(
name|COSDictionary
name|obj
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
comment|/**      * Notification of visit to document object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws COSVisitorException If there is an error while visiting this object.      */
specifier|public
name|Object
name|visitFromDocument
parameter_list|(
name|COSDocument
name|obj
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
comment|/**      * Notification of visit to float object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws COSVisitorException If there is an error while visiting this object.      */
specifier|public
name|Object
name|visitFromFloat
parameter_list|(
name|COSFloat
name|obj
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
comment|/**      * Notification of visit to integer object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws COSVisitorException If there is an error while visiting this object.      */
specifier|public
name|Object
name|visitFromInt
parameter_list|(
name|COSInteger
name|obj
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
comment|/**      * Notification of visit to name object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws COSVisitorException If there is an error while visiting this object.      */
specifier|public
name|Object
name|visitFromName
parameter_list|(
name|COSName
name|obj
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
comment|/**      * Notification of visit to null object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws COSVisitorException If there is an error while visiting this object.      */
specifier|public
name|Object
name|visitFromNull
parameter_list|(
name|COSNull
name|obj
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
comment|/**      * Notification of visit to stream object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws COSVisitorException If there is an error while visiting this object.      */
specifier|public
name|Object
name|visitFromStream
parameter_list|(
name|COSStream
name|obj
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
comment|/**      * Notification of visit to string object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws COSVisitorException If there is an error while visiting this object.      */
specifier|public
name|Object
name|visitFromString
parameter_list|(
name|COSString
name|obj
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
block|}
end_interface

end_unit

