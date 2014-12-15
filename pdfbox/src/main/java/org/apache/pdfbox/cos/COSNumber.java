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

begin_comment
comment|/**  * This class represents an abstract number in a PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.10 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|COSNumber
extends|extends
name|COSBase
block|{
comment|/**      * @deprecated Use the {@link COSInteger#ZERO} constant instead      */
specifier|public
specifier|static
specifier|final
name|COSInteger
name|ZERO
init|=
name|COSInteger
operator|.
name|ZERO
decl_stmt|;
comment|/**      * @deprecated Use the {@link COSInteger#ONE} constant instead      */
specifier|public
specifier|static
specifier|final
name|COSInteger
name|ONE
init|=
name|COSInteger
operator|.
name|ONE
decl_stmt|;
comment|/**      * This will get the float value of this number.      *      * @return The float value of this object.      */
specifier|public
specifier|abstract
name|float
name|floatValue
parameter_list|()
function_decl|;
comment|/**      * This will get the double value of this number.      *      * @return The double value of this number.      */
specifier|public
specifier|abstract
name|double
name|doubleValue
parameter_list|()
function_decl|;
comment|/**      * This will get the integer value of this number.      *      * @return The integer value of this number.      */
specifier|public
specifier|abstract
name|int
name|intValue
parameter_list|()
function_decl|;
comment|/**      * This will get the long value of this number.      *      * @return The long value of this number.      */
specifier|public
specifier|abstract
name|long
name|longValue
parameter_list|()
function_decl|;
comment|/**      * This factory method will get the appropriate number object.      *      * @param number The string representation of the number.      *      * @return A number object, either float or int.      *      * @throws IOException If the string is not a number.      */
specifier|public
specifier|static
name|COSNumber
name|get
parameter_list|(
name|String
name|number
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|number
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|)
block|{
name|char
name|digit
init|=
name|number
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
literal|'0'
operator|<=
name|digit
operator|&&
name|digit
operator|<=
literal|'9'
condition|)
block|{
return|return
name|COSInteger
operator|.
name|get
argument_list|(
name|digit
operator|-
literal|'0'
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|digit
operator|==
literal|'-'
operator|||
name|digit
operator|==
literal|'.'
condition|)
block|{
comment|// See https://issues.apache.org/jira/browse/PDFBOX-592
return|return
name|COSInteger
operator|.
name|ZERO
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not a number: "
operator|+
name|number
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|number
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|==
operator|-
literal|1
operator|&&
operator|(
name|number
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|'e'
argument_list|)
operator|==
operator|-
literal|1
operator|)
condition|)
block|{
try|try
block|{
if|if
condition|(
name|number
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'+'
condition|)
block|{
return|return
name|COSInteger
operator|.
name|get
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|number
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|COSInteger
operator|.
name|get
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|number
argument_list|)
argument_list|)
return|;
block|}
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
literal|"Value is not an integer: "
operator|+
name|number
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
operator|new
name|COSFloat
argument_list|(
name|number
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

