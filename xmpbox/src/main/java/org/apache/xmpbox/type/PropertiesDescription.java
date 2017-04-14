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
name|xmpbox
operator|.
name|type
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Represents all properties known for a specific namespace Type and attributes associated to each properties are saved  * If a specific type well declared is used, this class map it to a basic type  *   * @author a183132  *   *         Attribute management pre-implemented in order to give clues to make an attribute management system  */
end_comment

begin_class
specifier|public
class|class
name|PropertiesDescription
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|PropertyType
argument_list|>
name|types
decl_stmt|;
comment|/**      * Build PropMapping for specified namespace      *       */
specifier|public
name|PropertiesDescription
parameter_list|()
block|{
name|types
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Get All Properties Name      *       * @return a list of properties qualifiedName      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPropertiesName
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|types
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Add a new property, an attributes list can be given or can be null      *       * @param name      *            new property name      * @param type      *            Valuetype of the new property      */
specifier|public
name|void
name|addNewProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|PropertyType
name|type
parameter_list|)
block|{
name|types
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Return a type of a property from its qualifiedName      *       * @param name      *            The name of the property concerned      * @return Type of property or null      */
specifier|public
name|PropertyType
name|getPropertyType
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|types
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

