begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
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
name|apache
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
comment|/**      * The lowest integer to be kept in the {@link #STATIC} array.      */
specifier|private
specifier|static
name|int
name|LOW
init|=
operator|-
literal|100
decl_stmt|;
comment|/**      * The highest integer to be kept in the {@link #STATIC} array.      */
specifier|private
specifier|static
name|int
name|HIGH
init|=
literal|256
decl_stmt|;
comment|/**      * Static instances of all COSIntegers in the range from {@link #LOW}      * to {@link #HIGH}.      */
specifier|private
specifier|static
specifier|final
name|COSInteger
index|[]
name|STATIC
init|=
operator|new
name|COSInteger
index|[
name|HIGH
operator|-
name|LOW
operator|+
literal|1
index|]
decl_stmt|;
static|static
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|STATIC
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|STATIC
index|[
name|i
index|]
operator|=
operator|new
name|COSInteger
argument_list|(
name|i
operator|+
name|LOW
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns a COSInteger instance with the given value.      *      * @param val integer value      * @return COSInteger instance      */
specifier|public
specifier|static
name|COSInteger
name|get
parameter_list|(
name|long
name|val
parameter_list|)
block|{
if|if
condition|(
name|LOW
operator|<=
name|val
operator|&&
name|val
operator|<=
name|HIGH
condition|)
block|{
return|return
name|STATIC
index|[
operator|(
name|int
operator|)
name|val
operator|-
name|LOW
index|]
return|;
block|}
else|else
block|{
return|return
operator|new
name|COSInteger
argument_list|(
name|val
argument_list|)
return|;
block|}
block|}
specifier|private
name|long
name|value
decl_stmt|;
comment|/**      * constructor.      *      * @deprecated use the static {@link #get(long)} method instead      * @param val The integer value of this object.      */
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
comment|/**      * constructor.      *      * @deprecated use the static {@link #get(long)} method instead      * @param val The integer value of this object.      */
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
comment|/**      * This will create a new PDF Int object using a string.      *      * @param val The string value of the integer.      * @deprecated use the static {@link #get(long)} method instead      * @throws IOException If the val is not an integer type.      */
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
comment|/**      * Change the value of this reference.      *      * @param newValue The new value.      */
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
comment|/**      * This will output this string as a PDF object.      *      * @param output The stream to write to.      * @throws IOException If there is an error writing to the stream.      */
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

