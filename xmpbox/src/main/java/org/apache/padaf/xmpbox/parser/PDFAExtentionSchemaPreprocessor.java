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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|NSMapping
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
name|PDFAExtensionSchema
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
name|TypeMapping
import|;
end_import

begin_class
specifier|public
class|class
name|PDFAExtentionSchemaPreprocessor
extends|extends
name|XMPDocumentBuilder
implements|implements
name|XMPDocumentPreprocessor
block|{
specifier|public
name|PDFAExtentionSchemaPreprocessor
parameter_list|()
throws|throws
name|XmpSchemaException
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|NSMapping
name|process
parameter_list|(
name|byte
index|[]
name|xmp
parameter_list|)
throws|throws
name|XmpParsingException
throws|,
name|XmpSchemaException
throws|,
name|XmpUnknownValueTypeException
throws|,
name|XmpExpectedRdfAboutAttribute
throws|,
name|XmpXpacketEndException
throws|,
name|BadFieldValueException
block|{
name|parse
argument_list|(
name|xmp
argument_list|)
expr_stmt|;
return|return
name|this
operator|.
name|getNsMap
argument_list|()
return|;
block|}
specifier|protected
name|void
name|parseDescription
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
throws|throws
name|XmpParsingException
throws|,
name|XMLStreamException
throws|,
name|XmpSchemaException
throws|,
name|XmpUnknownValueTypeException
throws|,
name|XmpExpectedRdfAboutAttribute
throws|,
name|BadFieldValueException
block|{
name|NSMapping
name|nsMap
init|=
name|getNsMap
argument_list|()
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|getReader
argument_list|()
decl_stmt|;
name|nsMap
operator|.
name|resetComplexBasicTypesDeclarationInSchemaLevel
argument_list|()
expr_stmt|;
name|int
name|cptNS
init|=
name|reader
operator|.
name|getNamespaceCount
argument_list|()
decl_stmt|;
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
name|cptNS
condition|;
name|i
operator|++
control|)
block|{
name|namespaces
operator|.
name|put
argument_list|(
name|reader
operator|.
name|getNamespacePrefix
argument_list|(
name|i
argument_list|)
argument_list|,
name|reader
operator|.
name|getNamespaceURI
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|getTypeMapping
argument_list|()
operator|.
name|isStructuredTypeNamespace
argument_list|(
name|reader
operator|.
name|getNamespaceURI
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|nsMap
operator|.
name|setComplexBasicTypesDeclarationForLevelSchema
argument_list|(
name|reader
operator|.
name|getNamespaceURI
argument_list|(
name|i
argument_list|)
argument_list|,
name|reader
operator|.
name|getNamespacePrefix
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Different treatment for PDF/A Extension schema
if|if
condition|(
name|namespaces
operator|.
name|containsKey
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFAEXTENSION
argument_list|)
condition|)
block|{
if|if
condition|(
name|namespaces
operator|.
name|containsKey
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFAPROPERTY
argument_list|)
operator|&&
name|namespaces
operator|.
name|containsKey
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFASCHEMA
argument_list|)
condition|)
block|{
if|if
condition|(
name|namespaces
operator|.
name|containsValue
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFAEXTENSIONURI
argument_list|)
operator|&&
name|namespaces
operator|.
name|containsValue
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFAPROPERTYURI
argument_list|)
operator|&&
name|namespaces
operator|.
name|containsValue
argument_list|(
name|PDFAExtensionSchema
operator|.
name|PDFASCHEMAURI
argument_list|)
condition|)
block|{
name|PDFAExtensionSchema
name|schema
init|=
name|metadata
operator|.
name|createAndAddPDFAExtensionSchemaWithNS
argument_list|(
name|namespaces
argument_list|)
decl_stmt|;
name|treatDescriptionAttributes
argument_list|(
name|metadata
argument_list|,
name|schema
argument_list|)
expr_stmt|;
name|parseExtensionSchema
argument_list|(
name|schema
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|XmpUnexpectedNamespaceURIException
argument_list|(
literal|"Unexpected namespaceURI in PDFA Extension Schema encountered"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|XmpUnexpectedNamespacePrefixException
argument_list|(
literal|"Unexpected namespace Prefix in PDFA Extension Schema"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|int
name|openedTag
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|reader
operator|.
name|nextTag
argument_list|()
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
condition|)
block|{
name|openedTag
operator|=
literal|1
expr_stmt|;
do|do
block|{
name|int
name|tag
init|=
name|reader
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|tag
operator|==
name|XMLStreamReader
operator|.
name|START_ELEMENT
condition|)
block|{
name|openedTag
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tag
operator|==
name|XMLStreamReader
operator|.
name|END_ELEMENT
condition|)
block|{
name|openedTag
operator|--
expr_stmt|;
block|}
block|}
do|while
condition|(
name|openedTag
operator|>
literal|0
condition|)
do|;
block|}
block|}
block|}
block|}
end_class

end_unit

