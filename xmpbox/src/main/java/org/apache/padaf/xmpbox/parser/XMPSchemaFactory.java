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
name|parser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|schema
operator|.
name|XMPSchema
import|;
end_import

begin_comment
comment|/**  * A factory for each kind of schemas  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|XMPSchemaFactory
block|{
specifier|protected
name|String
name|namespace
decl_stmt|;
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|XMPSchema
argument_list|>
name|schemaClass
decl_stmt|;
specifier|protected
name|PropMapping
name|propDef
decl_stmt|;
specifier|protected
name|String
name|nsName
decl_stmt|;
specifier|protected
name|boolean
name|isDeclarative
decl_stmt|;
comment|/** 	 * Factory Constructor for basic known schemas 	 *  	 * @param namespace 	 *            namespace URI to treat 	 * @param schemaClass 	 *            Class representation associated to this URI 	 * @param propDef 	 *            Properties Types list associated 	 */
specifier|public
name|XMPSchemaFactory
parameter_list|(
name|String
name|namespace
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|XMPSchema
argument_list|>
name|schemaClass
parameter_list|,
name|PropMapping
name|propDef
parameter_list|)
block|{
name|this
operator|.
name|isDeclarative
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|schemaClass
operator|=
name|schemaClass
expr_stmt|;
name|this
operator|.
name|propDef
operator|=
name|propDef
expr_stmt|;
block|}
comment|/** 	 * Factory constructor for declarative XMP Schemas 	 *  	 * @param nsName 	 *            namespace name to treat 	 * @param namespace 	 *            namespace URI to treat 	 * @param schemaClass 	 *            Class representation associated to this URI 	 * @param propDef 	 *            Properties Types list associated 	 */
specifier|public
name|XMPSchemaFactory
parameter_list|(
name|String
name|nsName
parameter_list|,
name|String
name|namespace
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|XMPSchema
argument_list|>
name|schemaClass
parameter_list|,
name|PropMapping
name|propDef
parameter_list|)
block|{
name|this
operator|.
name|isDeclarative
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|schemaClass
operator|=
name|schemaClass
expr_stmt|;
name|this
operator|.
name|propDef
operator|=
name|propDef
expr_stmt|;
name|this
operator|.
name|nsName
operator|=
name|nsName
expr_stmt|;
block|}
comment|/** 	 * Get namespace URI treated by this factory 	 *  	 * @return The namespace URI 	 */
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
comment|/** 	 * Get type declared for the name property given 	 *  	 * @param name 	 *            The property name 	 * @return null if propery name is unknown 	 */
specifier|public
name|String
name|getPropertyType
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|propDef
operator|.
name|getPropertyType
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/** 	 * Get attributes declared for a property (NOT USED YET) 	 *  	 * @param name 	 *            The property Name 	 * @return List of all attributes defined for this property 	 */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPropertyAttributes
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|propDef
operator|.
name|getPropertyAttributes
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/** 	 * Create a schema that corresponding to this factory and add it to metadata 	 *  	 * @param metadata 	 *            Metadata to attach the Schema created 	 * @param prefix 	 * 						The namespace prefix (optional) 	 * @return the schema created and added to metadata 	 * @throws XmpSchemaException 	 *             When Instancing specified Object Schema failed 	 */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|XMPSchema
name|createXMPSchema
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|prefix
parameter_list|)
throws|throws
name|XmpSchemaException
block|{
name|XMPSchema
name|schema
init|=
literal|null
decl_stmt|;
name|Class
index|[]
name|argsClass
decl_stmt|;
name|Object
index|[]
name|schemaArgs
decl_stmt|;
if|if
condition|(
name|isDeclarative
condition|)
block|{
name|argsClass
operator|=
operator|new
name|Class
index|[]
block|{
name|XMPMetadata
operator|.
name|class
block|,
name|String
operator|.
name|class
block|,
name|String
operator|.
name|class
block|}
expr_stmt|;
name|schemaArgs
operator|=
operator|new
name|Object
index|[]
block|{
name|metadata
block|,
name|nsName
block|,
name|namespace
block|}
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|prefix
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|argsClass
operator|=
operator|new
name|Class
index|[]
block|{
name|XMPMetadata
operator|.
name|class
block|,
name|String
operator|.
name|class
block|}
expr_stmt|;
name|schemaArgs
operator|=
operator|new
name|Object
index|[]
block|{
name|metadata
block|,
name|prefix
block|}
expr_stmt|;
block|}
else|else
block|{
name|argsClass
operator|=
operator|new
name|Class
index|[]
block|{
name|XMPMetadata
operator|.
name|class
block|}
expr_stmt|;
name|schemaArgs
operator|=
operator|new
name|Object
index|[]
block|{
name|metadata
block|}
expr_stmt|;
block|}
name|Constructor
argument_list|<
name|?
extends|extends
name|XMPSchema
argument_list|>
name|schemaConstructor
decl_stmt|;
try|try
block|{
name|schemaConstructor
operator|=
name|schemaClass
operator|.
name|getConstructor
argument_list|(
name|argsClass
argument_list|)
expr_stmt|;
name|schema
operator|=
name|schemaConstructor
operator|.
name|newInstance
argument_list|(
name|schemaArgs
argument_list|)
expr_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|metadata
operator|.
name|addSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
return|return
name|schema
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XmpSchemaException
argument_list|(
literal|"Cannot Instanciate specified Object Schema"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

