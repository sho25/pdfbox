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

begin_comment
comment|/**  * This class represents a boolean value in the PDF document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|COSBoolean
extends|extends
name|COSBase
block|{
comment|/**      * The true boolean token.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|TRUE_BYTES
init|=
operator|new
name|byte
index|[]
block|{
literal|116
block|,
literal|114
block|,
literal|117
block|,
literal|101
block|}
decl_stmt|;
comment|// "true".getBytes("ISO-8859-1")
comment|/**      * The false boolean token.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|FALSE_BYTES
init|=
operator|new
name|byte
index|[]
block|{
literal|102
block|,
literal|97
block|,
literal|108
block|,
literal|115
block|,
literal|101
block|}
decl_stmt|;
comment|// "false".getBytes("ISO-8859-1")
comment|/**      * The PDF true value.      */
specifier|public
specifier|static
specifier|final
name|COSBoolean
name|TRUE
init|=
operator|new
name|COSBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|/**      * The PDF false value.      */
specifier|public
specifier|static
specifier|final
name|COSBoolean
name|FALSE
init|=
operator|new
name|COSBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|value
decl_stmt|;
comment|/**      * Constructor.      *      * @param aValue The boolean value.      */
specifier|private
name|COSBoolean
parameter_list|(
name|boolean
name|aValue
parameter_list|)
block|{
name|value
operator|=
name|aValue
expr_stmt|;
block|}
comment|/**      * This will get the value that this object wraps.      *      * @return The boolean value of this object.      */
specifier|public
name|boolean
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * This will get the value that this object wraps.      *      * @return The boolean value of this object.      */
specifier|public
name|Boolean
name|getValueAsObject
parameter_list|()
block|{
return|return
name|value
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
return|;
block|}
comment|/**      * This will get the boolean value.      *      * @param value Parameter telling which boolean value to get.      *      * @return The single boolean instance that matches the parameter.      */
specifier|public
specifier|static
name|COSBoolean
name|getBoolean
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
return|return
name|value
condition|?
name|TRUE
else|:
name|FALSE
return|;
block|}
comment|/**      * This will get the boolean value.      *      * @param value Parameter telling which boolean value to get.      *      * @return The single boolean instance that matches the parameter.      */
specifier|public
specifier|static
name|COSBoolean
name|getBoolean
parameter_list|(
name|Boolean
name|value
parameter_list|)
block|{
return|return
name|getBoolean
argument_list|(
name|value
operator|.
name|booleanValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * visitor pattern double dispatch method.      *      * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws IOException If an error occurs while visiting this object.      */
annotation|@
name|Override
specifier|public
name|Object
name|accept
parameter_list|(
name|ICOSVisitor
name|visitor
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|visitor
operator|.
name|visitFromBoolean
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Return a string representation of this object.      *      * @return The string value of this object.      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
return|;
block|}
comment|/**      * This will write this object out to a PDF stream.      *      * @param output The stream to write this object out to.      *      * @throws IOException If an error occurs while writing out this object.      */
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
if|if
condition|(
name|value
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|TRUE_BYTES
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|output
operator|.
name|write
argument_list|(
name|FALSE_BYTES
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

