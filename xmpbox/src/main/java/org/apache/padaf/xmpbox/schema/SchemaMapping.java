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
name|reflect
operator|.
name|Field
import|;
end_import

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
name|PropMapping
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
name|XMPSchemaFactory
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
name|XmpSchemaException
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
name|type
operator|.
name|PropertyType
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SchemaMapping
block|{
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|XMPSchemaFactory
argument_list|>
name|nsMaps
decl_stmt|;
static|static
block|{
name|nsMaps
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|XMPSchemaFactory
argument_list|>
argument_list|()
expr_stmt|;
name|addNameSpace
argument_list|(
literal|"http://ns.adobe.com/xap/1.0/"
argument_list|,
name|XMPBasicSchema
operator|.
name|class
argument_list|)
expr_stmt|;
name|addNameSpace
argument_list|(
name|DublinCoreSchema
operator|.
name|DCURI
argument_list|,
name|DublinCoreSchema
operator|.
name|class
argument_list|)
expr_stmt|;
name|addNameSpace
argument_list|(
literal|"http://www.aiim.org/pdfa/ns/extension/"
argument_list|,
name|PDFAExtensionSchema
operator|.
name|class
argument_list|)
expr_stmt|;
name|addNameSpace
argument_list|(
literal|"http://ns.adobe.com/xap/1.0/mm/"
argument_list|,
name|XMPMediaManagementSchema
operator|.
name|class
argument_list|)
expr_stmt|;
name|addNameSpace
argument_list|(
literal|"http://ns.adobe.com/pdf/1.3/"
argument_list|,
name|AdobePDFSchema
operator|.
name|class
argument_list|)
expr_stmt|;
name|addNameSpace
argument_list|(
literal|"http://www.aiim.org/pdfa/ns/id/"
argument_list|,
name|PDFAIdentificationSchema
operator|.
name|class
argument_list|)
expr_stmt|;
name|addNameSpace
argument_list|(
literal|"http://ns.adobe.com/xap/1.0/rights/"
argument_list|,
name|XMPRightsManagementSchema
operator|.
name|class
argument_list|)
expr_stmt|;
name|addNameSpace
argument_list|(
name|PhotoshopSchema
operator|.
name|PHOTOSHOPURI
argument_list|,
name|PhotoshopSchema
operator|.
name|class
argument_list|)
expr_stmt|;
name|addNameSpace
argument_list|(
name|XMPBasicJobTicketSchema
operator|.
name|JOB_TICKET_URI
argument_list|,
name|XMPBasicJobTicketSchema
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SchemaMapping
parameter_list|()
block|{
comment|// hide constructor
block|}
comment|/** 	 * Add a namespace declaration and Schema factory associated 	 *  	 * @param ns 	 *            the Namespace URI 	 * @param classSchem 	 *            The class representation of the schema linked to the namespace 	 * @throws XmpSchemaException 	 *             When could not read property name in Schema Class given 	 */
specifier|private
specifier|static
name|void
name|addNameSpace
parameter_list|(
name|String
name|ns
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|XMPSchema
argument_list|>
name|classSchem
parameter_list|)
block|{
name|nsMaps
operator|.
name|put
argument_list|(
name|ns
argument_list|,
operator|new
name|XMPSchemaFactory
argument_list|(
name|ns
argument_list|,
name|classSchem
argument_list|,
name|initializePropMapping
argument_list|(
name|ns
argument_list|,
name|classSchem
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Initialize the Property Mapping for a given schema 	 *  	 * @param ns 	 *            Namespace URI 	 * @param classSchem 	 *            The class representation of the schema linked to the namespace 	 * @return Construct expected properties types representation 	 * @throws XmpSchemaException 	 *             When could not read property name in field with properties 	 *             annotations 	 */
specifier|private
specifier|static
name|PropMapping
name|initializePropMapping
parameter_list|(
name|String
name|ns
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|XMPSchema
argument_list|>
name|classSchem
parameter_list|)
block|{
name|PropertyType
name|propType
decl_stmt|;
name|PropertyAttributesAnnotation
name|propAtt
decl_stmt|;
name|Field
index|[]
name|fields
decl_stmt|;
name|PropMapping
name|propMap
init|=
operator|new
name|PropMapping
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|fields
operator|=
name|classSchem
operator|.
name|getFields
argument_list|()
expr_stmt|;
name|String
name|propName
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Field
name|field
range|:
name|fields
control|)
block|{
if|if
condition|(
name|field
operator|.
name|isAnnotationPresent
argument_list|(
name|PropertyType
operator|.
name|class
argument_list|)
condition|)
block|{
try|try
block|{
name|propName
operator|=
operator|(
name|String
operator|)
name|field
operator|.
name|get
argument_list|(
name|propName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"couldn't read one type declaration, please check accessibility and declaration of fields annoted in "
operator|+
name|classSchem
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|propType
operator|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|PropertyType
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|field
operator|.
name|isAnnotationPresent
argument_list|(
name|PropertyAttributesAnnotation
operator|.
name|class
argument_list|)
condition|)
block|{
name|propMap
operator|.
name|addNewProperty
argument_list|(
name|propName
argument_list|,
name|propType
operator|.
name|propertyType
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// XXX Case where a special annotation is used to specify
comment|// attributes
comment|// NOT IMPLEMENTED YET, JUST TO GIVE A CLUE TO MAKE THIS
name|propAtt
operator|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|PropertyAttributesAnnotation
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|attributes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|att
range|:
name|propAtt
operator|.
name|expectedAttributes
argument_list|()
control|)
block|{
name|attributes
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
block|}
name|propMap
operator|.
name|addNewProperty
argument_list|(
name|propName
argument_list|,
name|propType
operator|.
name|propertyType
argument_list|()
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|propMap
return|;
block|}
comment|/** 	 * Return the specialized schema class representation if it's known (create 	 * and add it to metadata). In other cases, return null 	 *  	 * @param metadata 	 *            Metadata to link the new schema 	 * @param namespace 	 *            The namespace URI 	 * @return Schema representation 	 * @throws XmpSchemaException 	 *             When Instancing specified Object Schema failed 	 */
specifier|public
specifier|static
name|XMPSchema
name|getAssociatedSchemaObject
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|prefix
parameter_list|)
throws|throws
name|XmpSchemaException
block|{
if|if
condition|(
operator|!
name|nsMaps
operator|.
name|containsKey
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|XMPSchemaFactory
name|factory
init|=
name|nsMaps
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|createXMPSchema
argument_list|(
name|metadata
argument_list|,
name|prefix
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|XMPSchemaFactory
name|getSchemaFactory
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|nsMaps
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isContainedNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|nsMaps
operator|.
name|containsKey
argument_list|(
name|namespace
argument_list|)
return|;
block|}
block|}
end_class

end_unit

