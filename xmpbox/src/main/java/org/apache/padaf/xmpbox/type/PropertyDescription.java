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

begin_comment
comment|/**  * Represents one Property Description described in xml file in order to be use  * in automatic SchemaDescriptionBulding  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|PropertyDescription
block|{
specifier|protected
name|String
name|propName
decl_stmt|;
specifier|protected
name|String
name|propDesc
decl_stmt|;
comment|/** 	 * Constructor of a propertyDescription in order to be use in automatic 	 * SchemaDescriptionBulding 	 *  	 * @param propName 	 *            the local Name of the property to describe 	 * @param propDesc 	 *            the description of the property to describe 	 */
specifier|public
name|PropertyDescription
parameter_list|(
name|String
name|propName
parameter_list|,
name|String
name|propDesc
parameter_list|)
block|{
name|this
operator|.
name|propName
operator|=
name|propName
expr_stmt|;
name|this
operator|.
name|propDesc
operator|=
name|propDesc
expr_stmt|;
block|}
comment|/** 	 * Get description declared 	 *  	 * @return description declared 	 */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|propDesc
return|;
block|}
comment|/** 	 * Get property name declared 	 *  	 * @return property name declared 	 */
specifier|public
name|String
name|getPropertyName
parameter_list|()
block|{
return|return
name|propName
return|;
block|}
block|}
end_class

end_unit

