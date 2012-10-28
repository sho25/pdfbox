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
comment|/**  * Object representation of a Text XMP type  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|TextType
extends|extends
name|AbstractSimpleProperty
block|{
specifier|private
name|String
name|textValue
decl_stmt|;
comment|/** 	 * Property Text type constructor (namespaceURI is given) 	 *  	 * @param metadata 	 *            The metadata to attach to this property 	 * @param namespaceURI 	 *            the namespace URI to associate to this property 	 * @param prefix 	 *            The prefix to set for this property 	 * @param propertyName 	 *            The local Name of this property 	 * @param value 	 *            The value to set 	 */
specifier|public
name|TextType
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
operator|!
operator|(
name|value
operator|instanceof
name|String
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value given is not allowed for the Text type : '"
operator|+
name|value
operator|+
literal|"'"
argument_list|)
throw|;
block|}
else|else
block|{
name|textValue
operator|=
operator|(
name|String
operator|)
name|value
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getStringValue
parameter_list|()
block|{
return|return
name|textValue
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getValue
parameter_list|()
block|{
return|return
name|textValue
return|;
block|}
block|}
end_class

end_unit

