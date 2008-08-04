begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2004, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|operator
package|;
end_package

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|PDFOperator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|PDFStreamEngine
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  *  *<p>Titre : OperatorProcessor</p>  *<p>Description : This class is the strategy abstract class  * in the strategy GOF pattern. After instancated, you must ever call * the setContext method to initiamise OPeratorProcessor</p>  *<p>Copyright : Copyright (c) 2004</p>  *<p>Société : DBGS</p>  * @author Huault : huault@free.fr  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|OperatorProcessor
block|{
comment|/**      * The stream engine processing context.      */
specifier|protected
name|PDFStreamEngine
name|context
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      *      */
specifier|protected
name|OperatorProcessor
parameter_list|()
block|{     }
comment|/**      * Get the context for processing.      *       * @return The processing context.      */
specifier|protected
name|PDFStreamEngine
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
comment|/**      * Set the processing context.      *       * @param ctx The context for processing.      */
specifier|public
name|void
name|setContext
parameter_list|(
name|PDFStreamEngine
name|ctx
parameter_list|)
block|{
name|context
operator|=
name|ctx
expr_stmt|;
block|}
comment|/**      * process the operator.      * @param operator The operator that is being processed.      * @param arguments arguments needed by this operator.      *      * @throws IOException If there is an error processing the operator.      */
specifier|public
specifier|abstract
name|void
name|process
parameter_list|(
name|PDFOperator
name|operator
parameter_list|,
name|List
name|arguments
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_class

end_unit

