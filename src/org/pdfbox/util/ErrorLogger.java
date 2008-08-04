begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2005, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|util
package|;
end_package

begin_comment
comment|/**  * This class deals with some logging that is not handled by the log4j replacement.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|ErrorLogger
block|{
comment|/**      * Utility class, should not be instantiated.      *      */
specifier|private
name|ErrorLogger
parameter_list|()
block|{     }
comment|/**      * Log an error message.  This is only used for log4j replacement and      * should never be used when writing code.       *       * @param errorMessage The error message.      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|String
name|errorMessage
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
block|}
comment|/**      * Log an error message.  This is only used for log4j replacement and      * should never be used when writing code.      *       * @param errorMessage The error message.      * @param t The exception.      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|String
name|errorMessage
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

