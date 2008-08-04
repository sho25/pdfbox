begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2006, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|function
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
name|pdfbox
operator|.
name|cos
operator|.
name|COSStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|PDRange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|PDStream
import|;
end_import

begin_comment
comment|/**  * This class represents a function in a PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDFunction
implements|implements
name|COSObjectable
block|{
comment|/**      * Create the correct PD Model function based on the COS base function.      *       * @param function The COS function dictionary.      *       * @return The PDModel Function object.      *       * @throws IOException If we are unable to create the PDFunction object.      */
specifier|public
specifier|static
name|PDFunction
name|create
parameter_list|(
name|COSBase
name|function
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFunction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|function
operator|instanceof
name|COSObject
condition|)
block|{
name|function
operator|=
operator|(
operator|(
name|COSObject
operator|)
name|function
operator|)
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|function
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|funcDic
init|=
operator|(
name|COSDictionary
operator|)
name|function
decl_stmt|;
name|int
name|functionType
init|=
name|funcDic
operator|.
name|getInt
argument_list|(
literal|"FunctionType"
argument_list|)
decl_stmt|;
if|if
condition|(
name|function
operator|instanceof
name|COSStream
condition|)
block|{
if|if
condition|(
name|functionType
operator|==
literal|0
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFunctionType0
argument_list|(
operator|new
name|PDStream
argument_list|(
operator|(
name|COSStream
operator|)
name|function
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|functionType
operator|==
literal|4
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFunctionType4
argument_list|(
operator|new
name|PDStream
argument_list|(
operator|(
name|COSStream
operator|)
name|function
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Unknown stream function type "
operator|+
name|functionType
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|functionType
operator|==
literal|2
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFunctionType2
argument_list|(
name|funcDic
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|functionType
operator|==
literal|3
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFunctionType3
argument_list|(
name|funcDic
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Unknown dictionary function type "
operator|+
name|functionType
argument_list|)
throw|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Unknown COS type for function "
operator|+
name|function
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the number of output parameters that      * have a range specified.  A range for output parameters      * is optional so this may return zero for a function      * that does have output parameters, this will simply return the      * number that have the rnage specified.      *       * @return The number of input parameters that have a range      * specified.      */
specifier|public
specifier|abstract
name|int
name|getNumberOfOutputParameters
parameter_list|()
function_decl|;
comment|/**      * This will get the range for a certain output parameters.  This is will never      * return null.  If it is not present then the range 0 to 0 will      * be returned.      *      * @param n The output parameter number to get the range for.      *      * @return The range for this component.      */
specifier|public
specifier|abstract
name|PDRange
name|getRangeForOutput
parameter_list|(
name|int
name|n
parameter_list|)
function_decl|;
comment|/**      * This will set the a range for output parameter.      *      * @param range The new range for the output parameter.      * @param n The ouput parameter number to set the range for.      */
specifier|public
specifier|abstract
name|void
name|setRangeForOutput
parameter_list|(
name|PDRange
name|range
parameter_list|,
name|int
name|n
parameter_list|)
function_decl|;
comment|/**      * This will get the number of input parameters that      * have a domain specified.      *       * @return The number of input parameters that have a domain      * specified.      */
specifier|public
specifier|abstract
name|int
name|getNumberOfInputParameters
parameter_list|()
function_decl|;
comment|/**      * This will get the range for a certain input parameter.  This is will never      * return null.  If it is not present then the range 0 to 0 will      * be returned.      *      * @param n The parameter number to get the domain for.      *      * @return The domain range for this component.      */
specifier|public
specifier|abstract
name|PDRange
name|getDomainForInput
parameter_list|(
name|int
name|n
parameter_list|)
function_decl|;
comment|/**      * This will set the domain for the input values.      *      * @param range The new range for the input.      * @param n The number of the input parameter to set the domain for.      */
specifier|public
specifier|abstract
name|void
name|setDomainForInput
parameter_list|(
name|PDRange
name|range
parameter_list|,
name|int
name|n
parameter_list|)
function_decl|;
block|}
end_class

end_unit

