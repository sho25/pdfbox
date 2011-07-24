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
name|schema
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|ElementType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Retention
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|RetentionPolicy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Target
import|;
end_import

begin_comment
comment|/**  * To be used at runtime  */
end_comment

begin_annotation_defn
annotation|@
name|Retention
argument_list|(
name|RetentionPolicy
operator|.
name|RUNTIME
argument_list|)
annotation|@
name|Target
argument_list|(
name|ElementType
operator|.
name|FIELD
argument_list|)
comment|/**  * Annotation to specify type schema associated to a property.  * This annotation help to make automatically PDF/A Extension schema description  * for a schema.  *   * In default case, property description will be read   * from xml file specified in ExtensionSchemaAnnotation which define  * all properties description. If this file is not specified, description will be read here.  *   * Note: if file and this propertyDescription are not specified, a default 'not documented description"  * is written  */
specifier|public
annotation_defn|@interface
name|PropertyExtensionDefinition
block|{
comment|/** 	 * get category defined in this description that must be used to build 	 * schema descriptions Note: More details in this class javadoc 	 */
name|String
name|propertyCategory
parameter_list|()
function_decl|;
comment|/** 	 * get description defined in this description that must be used to build 	 * schema descriptions Note: More details in this class javadoc 	 *  	 */
name|String
name|propertyDescription
parameter_list|()
default|default
literal|""
function_decl|;
block|}
end_annotation_defn

end_unit

