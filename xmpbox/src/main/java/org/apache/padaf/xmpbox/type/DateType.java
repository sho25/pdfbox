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
name|padaf
operator|.
name|xmpbox
operator|.
name|type
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
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|parser
operator|.
name|DateConverter
import|;
end_import

begin_comment
comment|/**  * Object representation of a Date XMP type  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|DateType
extends|extends
name|AbstractSimpleProperty
block|{
comment|/**      * Property Date type constructor (namespaceURI is given)      *       * @param metadata      *            The metadata to attach to this property      * @param namespaceURI      *            the namespace URI to associate to this property      * @param prefix      *            The prefix to set for this property      * @param propertyName      *            The local Name of this property      * @param value      *            The value to set for this property      */
specifier|public
name|DateType
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|namespaceURI
argument_list|,
name|prefix
argument_list|,
name|propertyName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set property value      *       * @param value      *            the new Calendar element value      * @throws InappropriateTypeException      */
specifier|private
name|void
name|setValueFromCalendar
parameter_list|(
name|Calendar
name|value
parameter_list|)
block|{
name|setObjectValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|getElement
argument_list|()
operator|.
name|setTextContent
argument_list|(
name|DateConverter
operator|.
name|toISO8601
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * return the property value      *       * @return boolean      */
specifier|public
name|Calendar
name|getValue
parameter_list|()
block|{
return|return
operator|(
name|Calendar
operator|)
name|getObjectValue
argument_list|()
return|;
block|}
comment|/**      * Check if the value has a type which can be understood      *       * @param value      *            Object value to check      * @return True if types are compatibles      */
specifier|private
name|boolean
name|isGoodType
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|Calendar
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
try|try
block|{
name|DateConverter
operator|.
name|toCalendar
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Set value of this property      *       * @param value      *            The value to set      */
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isGoodType
argument_list|(
name|value
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value given is not allowed for the Date type."
argument_list|)
throw|;
block|}
else|else
block|{
comment|// if string object
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|setValueFromString
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if Calendar
name|setValueFromCalendar
argument_list|(
operator|(
name|Calendar
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Set the property value with a String      *       * @param value      *            The String value      */
specifier|private
name|void
name|setValueFromString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
try|try
block|{
name|setValueFromCalendar
argument_list|(
name|DateConverter
operator|.
name|toCalendar
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// SHOULD NEVER HAPPEN
comment|// STRING HAS BEEN CHECKED BEFORE
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

