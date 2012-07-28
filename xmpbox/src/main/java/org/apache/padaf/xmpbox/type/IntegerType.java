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

begin_comment
comment|/**  * Object representation of an Integer XMP type  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|IntegerType
extends|extends
name|AbstractSimpleProperty
block|{
comment|/** 	 * Property Integer type constructor (namespaceURI is given) 	 *  	 * @param metadata 	 *            The metadata to attach to this property 	 * @param namespaceURI 	 *            the namespace URI to associate to this property 	 * @param prefix 	 *            The prefix to set for this property 	 * @param propertyName 	 *            The local Name of this property 	 * @param value 	 *            The value to set 	 */
specifier|public
name|IntegerType
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
comment|/** 	 * return the property value 	 *  	 * @return the property value 	 */
specifier|public
name|int
name|getValue
parameter_list|()
block|{
return|return
operator|(
name|Integer
operator|)
name|getObjectValue
argument_list|()
return|;
block|}
comment|/** 	 * Set the property value 	 *  	 * @param value 	 *            The value to set 	 */
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
name|value
operator|instanceof
name|Integer
condition|)
block|{
name|setObjectValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
comment|// NumberFormatException is thrown (sub of InvalidArgumentException)
name|setObjectValue
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// invalid type of value
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value given is not allowed for the Integer type."
argument_list|)
throw|;
block|}
comment|// set value
name|getElement
argument_list|()
operator|.
name|setTextContent
argument_list|(
name|getObjectValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//
comment|//	public void setValue(Object value) {
comment|//		if (!isGoodType(value)) {
comment|//			throw new IllegalArgumentException(
comment|//					"Value given is not allowed for the Integer type.");
comment|//		} else {
comment|//			// if string object
comment|//			if (value instanceof String) {
comment|//				setValueFromString((String) value);
comment|//			} else {
comment|//				// if Integer
comment|//				setValueFromInt((Integer) value);
comment|//			}
comment|//
comment|//		}
comment|//
comment|//	}
block|}
end_class

end_unit

