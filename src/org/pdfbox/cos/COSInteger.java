begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2006, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

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
comment|/**  *  * This class represents an integer number in a PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.12 $  */
end_comment

begin_class
specifier|public
class|class
name|COSInteger
extends|extends
name|COSNumber
block|{
specifier|private
name|long
name|value
decl_stmt|;
comment|/**      * constructor.      *      * @param val The integer value of this object.      */
specifier|public
name|COSInteger
parameter_list|(
name|long
name|val
parameter_list|)
block|{
name|value
operator|=
name|val
expr_stmt|;
block|}
comment|/**      * constructor.      *      * @param val The integer value of this object.      */
specifier|public
name|COSInteger
parameter_list|(
name|int
name|val
parameter_list|)
block|{
name|this
argument_list|(
operator|(
name|long
operator|)
name|val
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will create a new PDF Int object using a string.      *      * @param val The string value of the integer.      *      * @throws IOException If the val is not an integer type.      */
specifier|public
name|COSInteger
parameter_list|(
name|String
name|val
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|value
operator|=
name|Long
operator|.
name|parseLong
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: value is not an integer type actual='"
operator|+
name|val
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|o
operator|instanceof
name|COSInteger
operator|&&
operator|(
operator|(
name|COSInteger
operator|)
name|o
operator|)
operator|.
name|intValue
argument_list|()
operator|==
name|intValue
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
comment|//taken from java.lang.Long
return|return
call|(
name|int
call|)
argument_list|(
name|value
operator|^
operator|(
name|value
operator|>>
literal|32
operator|)
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"COSInt{"
operator|+
name|value
operator|+
literal|"}"
return|;
block|}
comment|/**      * Change the value of this reference.      *       * @param newValue The new value.      */
specifier|public
name|void
name|setValue
parameter_list|(
name|long
name|newValue
parameter_list|)
block|{
name|value
operator|=
name|newValue
expr_stmt|;
block|}
comment|/**      * polymorphic access to value as float.      *      * @return The float value of this object.      */
specifier|public
name|float
name|floatValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * polymorphic access to value as float.      *      * @return The double value of this object.      */
specifier|public
name|double
name|doubleValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * Polymorphic access to value as int      * This will get the integer value of this object.      *      * @return The int value of this object,      */
specifier|public
name|int
name|intValue
parameter_list|()
block|{
return|return
operator|(
name|int
operator|)
name|value
return|;
block|}
comment|/**      * Polymorphic access to value as int      * This will get the integer value of this object.      *      * @return The int value of this object,      */
specifier|public
name|long
name|longValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * visitor pattern double dispatch method.      *      * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws COSVisitorException If an error occurs while visiting this object.      */
specifier|public
name|Object
name|accept
parameter_list|(
name|ICOSVisitor
name|visitor
parameter_list|)
throws|throws
name|COSVisitorException
block|{
return|return
name|visitor
operator|.
name|visitFromInt
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * This will output this string as a PDF object.      *        * @param output The stream to write to.      * @throws IOException If there is an error writing to the stream.      */
specifier|public
name|void
name|writePDF
parameter_list|(
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|output
operator|.
name|write
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

