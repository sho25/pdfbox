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
name|type
operator|.
name|AbstractField
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
name|BadFieldValueException
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
name|ComplexPropertyContainer
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
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|TextType
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
comment|/**  * Representation of one schema description (used in PDFAExtension Schema)  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|SchemaDescription
implements|implements
name|Elementable
block|{
specifier|private
name|XMPMetadata
name|metadata
decl_stmt|;
specifier|private
name|ValueTypesDescriptionContainer
name|valueTypes
decl_stmt|;
specifier|private
name|PropertyDescriptionContainer
name|properties
decl_stmt|;
specifier|private
name|ComplexPropertyContainer
name|content
decl_stmt|;
comment|/** 	 * Create a new Schema Description 	 *  	 * @param metadata 	 *            Metadata where this SchemaDescription will be included 	 */
specifier|public
name|SchemaDescription
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|this
operator|.
name|metadata
operator|=
name|metadata
expr_stmt|;
name|content
operator|=
operator|new
name|ComplexPropertyContainer
argument_list|(
name|metadata
argument_list|,
literal|null
argument_list|,
literal|"rdf"
argument_list|,
literal|"li"
argument_list|)
expr_stmt|;
name|content
operator|.
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
literal|null
argument_list|,
literal|"rdf"
argument_list|,
literal|"parseType"
argument_list|,
literal|"Resource"
argument_list|)
argument_list|)
expr_stmt|;
comment|//<pdfaSchema:property><seq>
name|properties
operator|=
operator|new
name|PropertyDescriptionContainer
argument_list|()
expr_stmt|;
name|content
operator|.
name|getElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|properties
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
comment|//<pdfaSchema:valueType><seq>
name|valueTypes
operator|=
operator|new
name|ValueTypesDescriptionContainer
argument_list|()
expr_stmt|;
name|content
operator|.
name|getElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|valueTypes
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Add a property to the current structure 	 *  	 * @param obj 	 *            the property to add 	 */
specifier|public
name|void
name|addProperty
parameter_list|(
name|AbstractField
name|obj
parameter_list|)
block|{
name|content
operator|.
name|addProperty
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Get schema Description property value as a string 	 *  	 * @param qualifiedName 	 *            the name of one of properties that constitute a schema 	 *            description 	 * @return the value of property specified 	 */
specifier|private
name|String
name|getPdfaTextValue
parameter_list|(
name|String
name|qualifiedName
parameter_list|)
block|{
name|Iterator
argument_list|<
name|AbstractField
argument_list|>
name|it
init|=
name|content
operator|.
name|getAllProperties
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|AbstractField
name|tmp
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|tmp
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|tmp
operator|.
name|getQualifiedName
argument_list|()
operator|.
name|equals
argument_list|(
name|qualifiedName
argument_list|)
condition|)
block|{
return|return
operator|(
operator|(
name|TextType
operator|)
name|tmp
operator|)
operator|.
name|getStringValue
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/** 	 * Set Description of this schema 	 *  	 * @param description 	 *            The value to set 	 */
specifier|public
name|void
name|setSchemaValue
parameter_list|(
name|String
name|description
parameter_list|)
block|{
comment|//<pdfaSchema:schema>
name|content
operator|.
name|addProperty
argument_list|(
operator|new
name|TextType
argument_list|(
name|metadata
argument_list|,
literal|null
argument_list|,
name|PDFAExtensionSchema
operator|.
name|PDFASCHEMA
argument_list|,
name|PDFAExtensionSchema
operator|.
name|SCHEMA
argument_list|,
name|description
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Return the schema description value 	 *  	 * @return The value to set 	 */
specifier|public
name|String
name|getSchema
parameter_list|()
block|{
return|return
name|getPdfaTextValue
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFASCHEMASEP
operator|+
name|PDFAExtensionSchema
operator|.
name|SCHEMA
argument_list|)
return|;
block|}
comment|/** 	 * Set the Schema Namespace URI 	 *  	 * @param uri 	 *            the namespace URI to set for this Schema Description 	 */
specifier|public
name|void
name|setNameSpaceURIValue
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|content
operator|.
name|addProperty
argument_list|(
operator|new
name|TextType
argument_list|(
name|metadata
argument_list|,
literal|null
argument_list|,
name|PDFAExtensionSchema
operator|.
name|PDFASCHEMA
argument_list|,
name|PDFAExtensionSchema
operator|.
name|NS_URI
argument_list|,
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Return the Schema nameSpaceURI value 	 *  	 * @return the namespace URI defined for this Schema Description 	 */
specifier|public
name|String
name|getNameSpaceURI
parameter_list|()
block|{
return|return
name|getPdfaTextValue
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFASCHEMASEP
operator|+
name|PDFAExtensionSchema
operator|.
name|NS_URI
argument_list|)
return|;
block|}
comment|/** 	 * Set the preferred schema namespace prefix 	 *  	 * @param prefix 	 *            the prefix to set for this Schema Description 	 */
specifier|public
name|void
name|setPrefixValue
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|content
operator|.
name|addProperty
argument_list|(
operator|new
name|TextType
argument_list|(
name|metadata
argument_list|,
literal|null
argument_list|,
name|PDFAExtensionSchema
operator|.
name|PDFASCHEMA
argument_list|,
name|PDFAExtensionSchema
operator|.
name|PREFIX
argument_list|,
name|prefix
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Return the preferred schema namespace prefix value 	 *  	 * @return the namespace URI defined for this Schema Description 	 */
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|getPdfaTextValue
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFASCHEMASEP
operator|+
name|PDFAExtensionSchema
operator|.
name|PREFIX
argument_list|)
return|;
block|}
comment|/** 	 * Give all PDFAProperties description embedded in this schema 	 *  	 * @return List of all PDF/A Property Descriptions that specified properties 	 *         used for the schema associed to this schema description 	 */
specifier|public
name|List
argument_list|<
name|PDFAPropertyDescription
argument_list|>
name|getProperties
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|properties
operator|.
name|properties
argument_list|)
return|;
block|}
comment|/** 	 * Add a property description to this schema 	 *  	 * @param name 	 *            property name 	 * @param type 	 *            property value type 	 * @param category 	 *            property category 	 * @param desc 	 *            property description 	 * @return the created PDFAPropertyDescription 	 * @throws BadFieldValueException 	 *             When Category field not contain 'internal' or 'external' 	 */
specifier|public
name|PDFAPropertyDescription
name|addProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|category
parameter_list|,
name|String
name|desc
parameter_list|)
throws|throws
name|BadFieldValueException
block|{
name|PDFAPropertyDescription
name|prop
init|=
operator|new
name|PDFAPropertyDescription
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|prop
operator|.
name|setNameValue
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setValueTypeValue
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setCategoryValue
argument_list|(
name|category
argument_list|)
expr_stmt|;
name|prop
operator|.
name|setDescriptionValue
argument_list|(
name|desc
argument_list|)
expr_stmt|;
name|properties
operator|.
name|addPropertyDescription
argument_list|(
name|prop
argument_list|)
expr_stmt|;
return|return
name|prop
return|;
block|}
comment|/** 	 * Give all ValueTypes description embedded in this schema 	 *  	 * @return list of all valuetypes defined in this schema description 	 */
specifier|public
name|List
argument_list|<
name|PDFAValueTypeDescription
argument_list|>
name|getValueTypes
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|valueTypes
operator|.
name|valueTypes
argument_list|)
return|;
block|}
comment|/** 	 * Add a valueType description to this schema 	 *  	 * @param type 	 *            valuetype type (its name) 	 * @param namespaceURI 	 *            valuetype namespace URI 	 * @param prefix 	 *            valuetype prefix 	 * @param description 	 *            valuetype description 	 * @param fields 	 *            list of PDF/A Field Descriptions associated 	 * @return the created PDFAPropertyDescription 	 */
specifier|public
name|PDFAValueTypeDescription
name|addValueType
parameter_list|(
name|String
name|type
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|description
parameter_list|,
name|List
argument_list|<
name|PDFAFieldDescription
argument_list|>
name|fields
parameter_list|)
block|{
name|PDFAValueTypeDescription
name|valueType
init|=
operator|new
name|PDFAValueTypeDescription
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|valueType
operator|.
name|setTypeNameValue
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|valueType
operator|.
name|setNamespaceURIValue
argument_list|(
name|namespaceURI
argument_list|)
expr_stmt|;
name|valueType
operator|.
name|setPrefixValue
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
name|valueType
operator|.
name|setDescriptionValue
argument_list|(
name|description
argument_list|)
expr_stmt|;
comment|// Field is optional
if|if
condition|(
name|fields
operator|!=
literal|null
condition|)
block|{
name|int
name|size
init|=
name|fields
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|valueType
operator|.
name|addField
argument_list|(
name|fields
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|valueTypes
operator|.
name|addValueTypeDescription
argument_list|(
name|valueType
argument_list|)
expr_stmt|;
return|return
name|valueType
return|;
block|}
specifier|public
name|ComplexPropertyContainer
name|getContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
comment|/** 	 * Container for PDF/A Value Type Descriptions associated to a Schema 	 * Description 	 *  	 * @author a183132 	 *  	 */
specifier|public
class|class
name|ValueTypesDescriptionContainer
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
name|PDFAValueTypeDescription
argument_list|>
name|valueTypes
decl_stmt|;
comment|/** 		 *  		 * PDF/A Value Type Descriptions Container constructor 		 */
specifier|public
name|ValueTypesDescriptionContainer
parameter_list|()
block|{
name|element
operator|=
name|metadata
operator|.
name|getFuturOwner
argument_list|()
operator|.
name|createElement
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFASCHEMASEP
operator|+
name|PDFAExtensionSchema
operator|.
name|VALUETYPE
argument_list|)
expr_stmt|;
name|content
operator|=
name|metadata
operator|.
name|getFuturOwner
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"rdf:Seq"
argument_list|)
expr_stmt|;
name|element
operator|.
name|appendChild
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|valueTypes
operator|=
operator|new
name|ArrayList
argument_list|<
name|PDFAValueTypeDescription
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/** 		 * Add a PDF/A Value Type Description to the current structure 		 *  		 * @param obj 		 *            the PDF/A Value Type Description to add 		 */
specifier|public
name|void
name|addValueTypeDescription
parameter_list|(
name|PDFAValueTypeDescription
name|obj
parameter_list|)
block|{
if|if
condition|(
name|containsValueTypeDescription
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|removeValueTypeDescription
argument_list|(
name|getValueTypeDescription
argument_list|(
name|obj
operator|.
name|getTypeNameValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|valueTypes
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
block|}
comment|/** 		 * access all PDF/A Value Type Descriptions 		 *  		 * @return Iterator on all PDF/A Value Type Descriptions defined in 		 *         order to be used in SchemaDescription class 		 */
specifier|public
name|Iterator
argument_list|<
name|PDFAValueTypeDescription
argument_list|>
name|getAllValueTypeDescription
parameter_list|()
block|{
return|return
name|valueTypes
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/** 		 * Get the PDF/A Value Type description from its type name 		 *  		 * @param type 		 *            the name defined for the value type wanted 		 * @return The wanted PDF/A Value Type Description 		 */
specifier|public
name|PDFAValueTypeDescription
name|getValueTypeDescription
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|Iterator
argument_list|<
name|PDFAValueTypeDescription
argument_list|>
name|it
init|=
name|getAllValueTypeDescription
argument_list|()
decl_stmt|;
name|PDFAValueTypeDescription
name|tmp
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|tmp
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|tmp
operator|.
name|getTypeNameValue
argument_list|()
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|tmp
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/** 		 * Check if two PDFAValueTypeDescription are similar 		 *  		 * @param prop1 		 *            first PDF/A Value Type Description 		 * @param prop2 		 *            second PDF/A Value Type Description 		 * @return comparison result 		 */
specifier|public
name|boolean
name|isSameValueTypeDescription
parameter_list|(
name|PDFAValueTypeDescription
name|prop1
parameter_list|,
name|PDFAValueTypeDescription
name|prop2
parameter_list|)
block|{
if|if
condition|(
name|prop1
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|prop2
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|prop1
operator|.
name|getTypeNameValue
argument_list|()
operator|.
name|equals
argument_list|(
name|prop2
operator|.
name|getTypeNameValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/** 		 * Check if a specified PDFAValueTypeDescription is embedded 		 *  		 * @param vtype 		 *            PDF/A Value Type Descriptions 		 * @return result 		 */
specifier|public
name|boolean
name|containsValueTypeDescription
parameter_list|(
name|PDFAValueTypeDescription
name|vtype
parameter_list|)
block|{
name|Iterator
argument_list|<
name|PDFAValueTypeDescription
argument_list|>
name|it
init|=
name|getAllValueTypeDescription
argument_list|()
decl_stmt|;
name|PDFAValueTypeDescription
name|tmp
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|tmp
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|isSameValueTypeDescription
argument_list|(
name|tmp
argument_list|,
name|vtype
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/** 		 * Remove a PDF/A Value Type Description 		 *  		 * @param vtype 		 *            PDF/A Value Type Description to remove 		 */
specifier|public
name|void
name|removeValueTypeDescription
parameter_list|(
name|PDFAValueTypeDescription
name|vtype
parameter_list|)
block|{
if|if
condition|(
name|containsValueTypeDescription
argument_list|(
name|vtype
argument_list|)
condition|)
block|{
name|valueTypes
operator|.
name|remove
argument_list|(
name|vtype
argument_list|)
expr_stmt|;
name|content
operator|.
name|removeChild
argument_list|(
name|vtype
operator|.
name|getContent
argument_list|()
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 		 * get the DOM element for xml/rdf serialization 		 *  		 * @return the DOM Element 		 */
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
comment|/** 	 * Container for PDF/A Property Descriptions associated to a Schema 	 * Description 	 *  	 * @author a183132 	 *  	 */
specifier|public
class|class
name|PropertyDescriptionContainer
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
name|PDFAPropertyDescription
argument_list|>
name|properties
decl_stmt|;
comment|/** 		 *  		 * PDF/A Property Descriptions Container constructor 		 */
specifier|public
name|PropertyDescriptionContainer
parameter_list|()
block|{
name|element
operator|=
name|metadata
operator|.
name|getFuturOwner
argument_list|()
operator|.
name|createElement
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFASCHEMASEP
operator|+
name|PDFAExtensionSchema
operator|.
name|PROPERTY
argument_list|)
expr_stmt|;
name|content
operator|=
name|metadata
operator|.
name|getFuturOwner
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"rdf:Seq"
argument_list|)
expr_stmt|;
name|element
operator|.
name|appendChild
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|properties
operator|=
operator|new
name|ArrayList
argument_list|<
name|PDFAPropertyDescription
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/** 		 * Add a PropertyDescription to the current structure 		 *  		 * @param obj 		 *            the property to add 		 */
specifier|public
name|void
name|addPropertyDescription
parameter_list|(
name|PDFAPropertyDescription
name|obj
parameter_list|)
block|{
if|if
condition|(
name|containsPropertyDescription
argument_list|(
name|obj
argument_list|)
condition|)
block|{
name|removePropertyDescription
argument_list|(
name|getPropertyDescription
argument_list|(
name|obj
operator|.
name|getNameValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|properties
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
block|}
comment|/** 		 * Return all PropertyDescription 		 *  		 * @return Iterator on all PDF/A Property Descriptions in order to be 		 *         used in SchemaDescription class 		 */
specifier|public
name|Iterator
argument_list|<
name|PDFAPropertyDescription
argument_list|>
name|getAllPropertyDescription
parameter_list|()
block|{
return|return
name|properties
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/** 		 * Check if two PDF/A Property Description are similar 		 *  		 * @param prop1 		 *            first PDF/A Property Description 		 * @param prop2 		 *            second PDF/A Property Description 		 * @return comparison result 		 */
specifier|public
name|boolean
name|isSamePropertyDescription
parameter_list|(
name|PDFAPropertyDescription
name|prop1
parameter_list|,
name|PDFAPropertyDescription
name|prop2
parameter_list|)
block|{
if|if
condition|(
name|prop1
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|prop2
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
comment|// Assuming that 2 properties can't have the same name
if|if
condition|(
name|prop1
operator|.
name|getNameValue
argument_list|()
operator|.
name|equals
argument_list|(
name|prop2
operator|.
name|getNameValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|// if(prop1.content.getElement().getTextContent().equals(prop2.content.getElement().getTextContent())){
comment|// return true;
comment|// }
block|}
return|return
literal|false
return|;
block|}
comment|/** 		 * Check if a specified PDF/A Property Description is embedded 		 *  		 * @param prop 		 *            PDF/A Property Description 		 * @return result 		 */
specifier|public
name|boolean
name|containsPropertyDescription
parameter_list|(
name|PDFAPropertyDescription
name|prop
parameter_list|)
block|{
name|Iterator
argument_list|<
name|PDFAPropertyDescription
argument_list|>
name|it
init|=
name|getAllPropertyDescription
argument_list|()
decl_stmt|;
name|PDFAPropertyDescription
name|tmp
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|tmp
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|isSamePropertyDescription
argument_list|(
name|tmp
argument_list|,
name|prop
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/** 		 * Get a PDFAPropertyDescription from its name 		 *  		 * @param name 		 *            name defined for PDF/A Property Description 		 * @return the PDF/A Property Description 		 */
specifier|public
name|PDFAPropertyDescription
name|getPropertyDescription
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Iterator
argument_list|<
name|PDFAPropertyDescription
argument_list|>
name|it
init|=
name|getAllPropertyDescription
argument_list|()
decl_stmt|;
name|PDFAPropertyDescription
name|tmp
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|tmp
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
name|tmp
operator|.
name|getNameValue
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|tmp
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/** 		 * Remove a PDF/A Property Description 		 *  		 * @param prop 		 *            PDF/A Property Description 		 */
specifier|public
name|void
name|removePropertyDescription
parameter_list|(
name|PDFAPropertyDescription
name|prop
parameter_list|)
block|{
if|if
condition|(
name|containsPropertyDescription
argument_list|(
name|prop
argument_list|)
condition|)
block|{
name|properties
operator|.
name|remove
argument_list|(
name|prop
argument_list|)
expr_stmt|;
name|content
operator|.
name|removeChild
argument_list|(
name|prop
operator|.
name|getContent
argument_list|()
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 		 * get the DOM element for xml/rdf serialization 		 *  		 * @return the DOM Element 		 */
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
comment|/** 	 * get the DOM element for xml/rdf serialization 	 *  	 * @return the DOM Element 	 */
specifier|public
name|Element
name|getElement
parameter_list|()
block|{
return|return
name|content
operator|.
name|getElement
argument_list|()
return|;
block|}
block|}
end_class

end_unit

