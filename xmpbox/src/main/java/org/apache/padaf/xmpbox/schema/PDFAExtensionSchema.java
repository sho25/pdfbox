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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
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
name|Attribute
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
name|Elementable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * Representation of a PDF/A Extension schema description schema  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|PDFAExtensionSchema
extends|extends
name|XMPSchema
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PDFAEXTENSION
init|=
literal|"pdfaExtension"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFAEXTENSIONURI
init|=
literal|"http://www.aiim.org/pdfa/ns/extension/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFASCHEMA
init|=
literal|"pdfaSchema"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFASCHEMASEP
init|=
literal|"pdfaSchema:"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFASCHEMAURI
init|=
literal|"http://www.aiim.org/pdfa/ns/schema#"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFAPROPERTY
init|=
literal|"pdfaProperty"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFAPROPERTYSEP
init|=
literal|"pdfaProperty:"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFAPROPERTYURI
init|=
literal|"http://www.aiim.org/pdfa/ns/property#"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFATYPE
init|=
literal|"pdfaType"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFATYPESEP
init|=
literal|"pdfaType:"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFATYPEURI
init|=
literal|"http://www.aiim.org/pdfa/ns/type#"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFAFIELD
init|=
literal|"pdfaField"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFAFIELDSEP
init|=
literal|"pdfaField:"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PDFAFIELDURI
init|=
literal|"http://www.aiim.org/pdfa/ns/field#"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|SCHEMA
init|=
literal|"schema"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"URI"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|NS_URI
init|=
literal|"namespaceURI"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|PREFIX
init|=
literal|"prefix"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Seq Property"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|PROPERTY
init|=
literal|"property"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Seq ValueType"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|VALUETYPE
init|=
literal|"valueType"
decl_stmt|;
specifier|private
name|SchemaDescriptionContainer
name|descriptions
decl_stmt|;
comment|/**      * Build a new PDFExtension schema      *       * @param metadata      *            The metadata to attach this schema XMPMetadata      */
specifier|public
name|PDFAExtensionSchema
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|this
argument_list|(
name|metadata
argument_list|,
name|PDFAEXTENSION
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PDFAExtensionSchema
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|prefix
argument_list|,
name|PDFAEXTENSIONURI
argument_list|)
expr_stmt|;
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
name|NS_NAMESPACE
argument_list|,
literal|"xmlns"
argument_list|,
name|PDFASCHEMA
argument_list|,
name|PDFASCHEMAURI
argument_list|)
argument_list|)
expr_stmt|;
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
name|NS_NAMESPACE
argument_list|,
literal|"xmlns"
argument_list|,
name|PDFAPROPERTY
argument_list|,
name|PDFAPROPERTYURI
argument_list|)
argument_list|)
expr_stmt|;
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
name|NS_NAMESPACE
argument_list|,
literal|"xmlns"
argument_list|,
name|PDFATYPE
argument_list|,
name|PDFATYPEURI
argument_list|)
argument_list|)
expr_stmt|;
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
name|NS_NAMESPACE
argument_list|,
literal|"xmlns"
argument_list|,
name|PDFAFIELD
argument_list|,
name|PDFAFIELDURI
argument_list|)
argument_list|)
expr_stmt|;
name|descriptions
operator|=
operator|new
name|SchemaDescriptionContainer
argument_list|()
expr_stmt|;
name|getElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|descriptions
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Build a new PDFExtension schema with specified namespaces declaration      *       * @param metadata      *            The metadata to attach this schema      * @param namespaces      *            List of namespaces to define      * @throws XmpSchemaException      *             The namespace URI of PDF/A Extension schema missing      */
specifier|public
name|PDFAExtensionSchema
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
throws|throws
name|XmpSchemaException
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|PDFAEXTENSION
argument_list|,
name|PDFAEXTENSIONURI
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|namespaces
operator|.
name|containsKey
argument_list|(
name|PDFAEXTENSION
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|XmpSchemaException
argument_list|(
literal|"Extension schema is declared without the pdfaSchema namespace specification"
argument_list|)
throw|;
block|}
name|namespaces
operator|.
name|remove
argument_list|(
name|PDFAEXTENSION
argument_list|)
expr_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|namespaces
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
name|NS_NAMESPACE
argument_list|,
literal|"xmlns"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|descriptions
operator|=
operator|new
name|SchemaDescriptionContainer
argument_list|()
expr_stmt|;
name|getElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|descriptions
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Give the prefix of the PDF/A Extension schema      *       * @return prefix value      */
specifier|public
name|String
name|getPrefixValue
parameter_list|()
block|{
return|return
name|PDFAEXTENSION
return|;
block|}
comment|/**      * Give the namespace URI of the PDF/A Extension schema      *       * @return namespace URI      */
specifier|public
name|String
name|getNamespaceValue
parameter_list|()
block|{
return|return
name|PDFAEXTENSIONURI
return|;
block|}
comment|/**      * Add to the Extension Schema a new description schema param desc the      * schema description      *       * @param desc      *            the new schema description      * @return the previous schema with same prefix, null otherwise      */
specifier|public
name|SchemaDescription
name|addSchemaDescription
parameter_list|(
name|SchemaDescription
name|desc
parameter_list|)
block|{
return|return
name|descriptions
operator|.
name|addSchemaDescription
argument_list|(
name|desc
argument_list|)
return|;
block|}
comment|/**      * create Extension Schema a new description schema      *       * @return a new empty description schema      */
specifier|public
name|SchemaDescription
name|createSchemaDescription
parameter_list|()
block|{
return|return
operator|new
name|SchemaDescription
argument_list|(
name|getMetadata
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Give a list of all description declared      *       * @return List of all schemaDescriptions declared      */
specifier|public
name|List
argument_list|<
name|SchemaDescription
argument_list|>
name|getDescriptionSchema
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|descriptions
operator|.
name|schemaDescriptions
argument_list|)
return|;
block|}
comment|/**      * Give an iterator of all description declared      *       * @return a SchemaDescription Iterator      */
specifier|public
name|Iterator
argument_list|<
name|SchemaDescription
argument_list|>
name|getIteratorOfDescriptions
parameter_list|()
block|{
return|return
name|descriptions
operator|.
name|getAllSchemasDescription
argument_list|()
return|;
block|}
comment|/**      * Container of Description Schema embedded in PDF/A Extension Schema      *       * @author a183132      *       */
specifier|public
class|class
name|SchemaDescriptionContainer
implements|implements
name|Elementable
block|{
specifier|private
name|Element
name|element
decl_stmt|,
name|content
decl_stmt|;
specifier|private
name|List
argument_list|<
name|SchemaDescription
argument_list|>
name|schemaDescriptions
decl_stmt|;
comment|/**          *           * SchemasDescription Container constructor          */
specifier|public
name|SchemaDescriptionContainer
parameter_list|()
block|{
name|element
operator|=
name|getMetadata
argument_list|()
operator|.
name|getFuturOwner
argument_list|()
operator|.
name|createElement
argument_list|(
name|PDFAEXTENSION
operator|+
literal|":schemas"
argument_list|)
expr_stmt|;
name|content
operator|=
name|getMetadata
argument_list|()
operator|.
name|getFuturOwner
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"rdf:Bag"
argument_list|)
expr_stmt|;
name|element
operator|.
name|appendChild
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|schemaDescriptions
operator|=
operator|new
name|ArrayList
argument_list|<
name|SchemaDescription
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**          * Add a SchemaDescription to the current structure          *           * @param obj          *            the property to add          * @return the old SchemaDescription corresponding to the same namespace          *         prefix if exist, else null          */
specifier|public
name|SchemaDescription
name|addSchemaDescription
parameter_list|(
name|SchemaDescription
name|obj
parameter_list|)
block|{
name|SchemaDescription
name|sd
init|=
name|getSameSchemaDescription
argument_list|(
name|obj
argument_list|)
decl_stmt|;
if|if
condition|(
name|sd
operator|!=
literal|null
condition|)
block|{
name|schemaDescriptions
operator|.
name|remove
argument_list|(
name|sd
argument_list|)
expr_stmt|;
name|content
operator|.
name|removeChild
argument_list|(
name|sd
operator|.
name|getContent
argument_list|()
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// if(containsSchemaDescription(obj)){
comment|// removeSchemaDescription(obj);
comment|// }
name|schemaDescriptions
operator|.
name|add
argument_list|(
name|obj
argument_list|)
expr_stmt|;
name|content
operator|.
name|appendChild
argument_list|(
name|obj
operator|.
name|getContent
argument_list|()
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sd
return|;
block|}
comment|/**          * Get Schema Description embedded with the same prefix as that given in          * parameters          *           * @param obj          *            Schema Description with same prefix          * @return The schema Description contained          */
specifier|protected
name|SchemaDescription
name|getSameSchemaDescription
parameter_list|(
name|SchemaDescription
name|obj
parameter_list|)
block|{
name|String
name|oPrefix
init|=
name|obj
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
for|for
control|(
name|SchemaDescription
name|existing
range|:
name|schemaDescriptions
control|)
block|{
if|if
condition|(
name|oPrefix
operator|.
name|equals
argument_list|(
name|existing
operator|.
name|getPrefix
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|existing
return|;
block|}
block|}
comment|// else not found
return|return
literal|null
return|;
block|}
comment|/**          * Return all SchemaDescription          *           * @return SchemaDescriptions Iterator in order to be use in PDF/A          *         Extension Schema class          */
specifier|public
name|Iterator
argument_list|<
name|SchemaDescription
argument_list|>
name|getAllSchemasDescription
parameter_list|()
block|{
return|return
name|schemaDescriptions
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|// /**
comment|// * Check if two SchemaDescription are similar
comment|// * @param prop1
comment|// * @param prop2
comment|// * @return
comment|// */
comment|// public boolean isSameSchemaDescription(SchemaDescription prop1,
comment|// SchemaDescription prop2){
comment|// if(prop1.getClass().equals(prop2.getClass()) ){
comment|// if(prop1.content.getElement().getTextContent().equals(prop2.content.getElement().getTextContent())){
comment|// return true;
comment|// }
comment|// }
comment|// return false;
comment|// }
comment|// /**
comment|// * Check if a specified SchemaDescription is embedded
comment|// * @param schema
comment|// * @return
comment|// */
comment|// public boolean containsSchemaDescription(SchemaDescription schema){
comment|// Iterator<SchemaDescription> it=getAllSchemasDescription();
comment|// SchemaDescription tmp;
comment|// while(it.hasNext()){
comment|// tmp=it.next();
comment|// if(isSameSchemaDescription(tmp, schema) ){
comment|// return true;
comment|// }
comment|// }
comment|// return false;
comment|// }
comment|// /**
comment|// * Remove a schema
comment|// * @param schema
comment|// */
comment|// public void removeSchemaDescription(SchemaDescription schema){
comment|// if(containsSchemaDescription(schema)){
comment|// schemaDescriptions.remove(schema);
comment|// content.removeChild(schema.content.getElement());
comment|// }
comment|// }
comment|/**          * Get Dom Element for xml/rdf serialization          *           * @return the DOM Element          */
specifier|public
name|Element
name|getElement
parameter_list|()
block|{
return|return
name|element
return|;
block|}
block|}
block|}
end_class

end_unit

