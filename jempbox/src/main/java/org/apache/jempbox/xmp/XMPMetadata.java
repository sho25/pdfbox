begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|jempbox
operator|.
name|xmp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jempbox
operator|.
name|impl
operator|.
name|XMLUtil
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
name|Document
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

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NamedNodeMap
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
name|Node
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
name|NodeList
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
name|ProcessingInstruction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_comment
comment|/**  * This class represents the top level XMP data structure and gives access to  * the various schemas that are available as part of the XMP specification.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.10 $  */
end_comment

begin_class
specifier|public
class|class
name|XMPMetadata
block|{
comment|/**      * Supported encoding for persisted XML.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENCODING_UTF8
init|=
literal|"UTF-8"
decl_stmt|;
comment|/**      * Supported encoding for persisted XML.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENCODING_UTF16BE
init|=
literal|"UTF-16BE"
decl_stmt|;
comment|/**      * Supported encoding for persisted XML.      */
specifier|public
specifier|static
specifier|final
name|String
name|ENCODING_UTF16LE
init|=
literal|"UTF-16LE"
decl_stmt|;
comment|/**      * The DOM representation of the metadata.      */
specifier|protected
name|Document
name|xmpDocument
decl_stmt|;
comment|/**      * The encoding of the XMP document. Default is UTF8.      */
specifier|protected
name|String
name|encoding
init|=
name|ENCODING_UTF8
decl_stmt|;
comment|/**      * A mapping of namespaces.      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|nsMappings
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Default constructor, creates blank XMP doc.      *       * @throws IOException      *             If there is an error creating the initial document.      */
specifier|public
name|XMPMetadata
parameter_list|()
throws|throws
name|IOException
block|{
name|xmpDocument
operator|=
name|XMLUtil
operator|.
name|newDocument
argument_list|()
expr_stmt|;
name|ProcessingInstruction
name|beginXPacket
init|=
name|xmpDocument
operator|.
name|createProcessingInstruction
argument_list|(
literal|"xpacket"
argument_list|,
literal|"begin=\"\uFEFF\" id=\"W5M0MpCehiHzreSzNTczkc9d\""
argument_list|)
decl_stmt|;
name|xmpDocument
operator|.
name|appendChild
argument_list|(
name|beginXPacket
argument_list|)
expr_stmt|;
name|Element
name|xmpMeta
init|=
name|xmpDocument
operator|.
name|createElementNS
argument_list|(
literal|"adobe:ns:meta/"
argument_list|,
literal|"x:xmpmeta"
argument_list|)
decl_stmt|;
name|xmpMeta
operator|.
name|setAttributeNS
argument_list|(
name|XMPSchema
operator|.
name|NS_NAMESPACE
argument_list|,
literal|"xmlns:x"
argument_list|,
literal|"adobe:ns:meta/"
argument_list|)
expr_stmt|;
name|xmpDocument
operator|.
name|appendChild
argument_list|(
name|xmpMeta
argument_list|)
expr_stmt|;
name|Element
name|rdf
init|=
name|xmpDocument
operator|.
name|createElement
argument_list|(
literal|"rdf:RDF"
argument_list|)
decl_stmt|;
name|rdf
operator|.
name|setAttributeNS
argument_list|(
name|XMPSchema
operator|.
name|NS_NAMESPACE
argument_list|,
literal|"xmlns:rdf"
argument_list|,
literal|"http://www.w3.org/1999/02/22-rdf-syntax-ns#"
argument_list|)
expr_stmt|;
name|xmpMeta
operator|.
name|appendChild
argument_list|(
name|rdf
argument_list|)
expr_stmt|;
name|ProcessingInstruction
name|endXPacket
init|=
name|xmpDocument
operator|.
name|createProcessingInstruction
argument_list|(
literal|"xpacket"
argument_list|,
literal|"end=\"w\""
argument_list|)
decl_stmt|;
name|xmpDocument
operator|.
name|appendChild
argument_list|(
name|endXPacket
argument_list|)
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor from an existing XML document.      *       * @param doc      *            The root XMP document.      */
specifier|public
name|XMPMetadata
parameter_list|(
name|Document
name|doc
parameter_list|)
block|{
name|xmpDocument
operator|=
name|doc
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|()
block|{
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaPDF
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaPDF
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaBasic
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaBasic
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaDublinCore
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaDublinCore
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaMediaManagement
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaMediaManagement
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaRightsManagement
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaRightsManagement
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaBasicJobTicket
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaBasicJobTicket
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaDynamicMedia
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaDynamicMedia
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaPagedText
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaPagedText
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaIptc4xmpCore
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaIptc4xmpCore
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaPhotoshop
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaPhotoshop
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Will add a XMPSchema to the set of identified schemas.      *       * The class needs to have a constructor with parameter Element      *       * @param namespace      *            The namespace URI of the schmema for instance      *            http://purl.org/dc/elements/1.1/.      * @param xmpSchema      *            The schema to associated this identifier with.      */
specifier|public
name|void
name|addXMLNSMapping
parameter_list|(
name|String
name|namespace
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|xmpSchema
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|XMPSchema
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|xmpSchema
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only XMPSchemas can be mapped to."
argument_list|)
throw|;
block|}
name|nsMappings
operator|.
name|put
argument_list|(
name|namespace
argument_list|,
name|xmpSchema
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the PDF Schema.      *       * @return The first PDF schema in the list.      *       * @throws IOException      *             If there is an error accessing the schema.      */
specifier|public
name|XMPSchemaPDF
name|getPDFSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaPDF
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaPDF
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Get the Basic Schema.      *       * @return The first Basic schema in the list.      *       * @throws IOException      *             If there is an error accessing the schema.      */
specifier|public
name|XMPSchemaBasic
name|getBasicSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaBasic
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaBasic
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Get the Dublin Core Schema.      *       * @return The first Dublin schema in the list.      *       * @throws IOException      *             If there is an error accessing the schema.      */
specifier|public
name|XMPSchemaDublinCore
name|getDublinCoreSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaDublinCore
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaDublinCore
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Get the Media Management Schema.      *       * @return The first Media Management schema in the list.      *       * @throws IOException      *             If there is an error accessing the schema.      */
specifier|public
name|XMPSchemaMediaManagement
name|getMediaManagementSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaMediaManagement
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaMediaManagement
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Get the Schema Rights Schema.      *       * @return The first Schema Rights schema in the list.      *       * @throws IOException      *             If there is an error accessing the schema.      */
specifier|public
name|XMPSchemaRightsManagement
name|getRightsManagementSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaRightsManagement
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaRightsManagement
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Get the Job Ticket Schema.      *       * @return The first Job Ticket schema in the list.      *       * @throws IOException      *             If there is an error accessing the schema.      */
specifier|public
name|XMPSchemaBasicJobTicket
name|getBasicJobTicketSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaBasicJobTicket
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaBasicJobTicket
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Get the Dynamic Media Schema.      *       * @return The first Dynamic Media schema in the list.      *       * @throws IOException      *             If there is an error accessing the schema.      */
specifier|public
name|XMPSchemaDynamicMedia
name|getDynamicMediaSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaDynamicMedia
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaDynamicMedia
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Get the Paged Text Schema.      *       * @return The first Paged Text schema in the list.      *       * @throws IOException      *             If there is an error accessing the schema.      */
specifier|public
name|XMPSchemaPagedText
name|getPagedTextSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaPagedText
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaPagedText
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Add a new Media Management schema.      *       * @return The newly added schema.      */
specifier|public
name|XMPSchemaMediaManagement
name|addMediaManagementSchema
parameter_list|()
block|{
name|XMPSchemaMediaManagement
name|schema
init|=
operator|new
name|XMPSchemaMediaManagement
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaMediaManagement
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Add a new Rights Managment schema.      *       * @return The newly added schema.      */
specifier|public
name|XMPSchemaRightsManagement
name|addRightsManagementSchema
parameter_list|()
block|{
name|XMPSchemaRightsManagement
name|schema
init|=
operator|new
name|XMPSchemaRightsManagement
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaRightsManagement
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Add a new Job Ticket schema.      *       * @return The newly added schema.      */
specifier|public
name|XMPSchemaBasicJobTicket
name|addBasicJobTicketSchema
parameter_list|()
block|{
name|XMPSchemaBasicJobTicket
name|schema
init|=
operator|new
name|XMPSchemaBasicJobTicket
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaBasicJobTicket
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Add a new Dynamic Media schema.      *       * @return The newly added schema.      */
specifier|public
name|XMPSchemaDynamicMedia
name|addDynamicMediaSchema
parameter_list|()
block|{
name|XMPSchemaDynamicMedia
name|schema
init|=
operator|new
name|XMPSchemaDynamicMedia
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaDynamicMedia
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Add a new Paged Text schema.      *       * @return The newly added schema.      */
specifier|public
name|XMPSchemaPagedText
name|addPagedTextSchema
parameter_list|()
block|{
name|XMPSchemaPagedText
name|schema
init|=
operator|new
name|XMPSchemaPagedText
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaPagedText
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Add a custom schema to the root rdf. The schema has to have been created      * as a child of this XMPMetadata.      *       * @param schema      *            The schema to add.      */
specifier|public
name|void
name|addSchema
parameter_list|(
name|XMPSchema
name|schema
parameter_list|)
block|{
name|Element
name|rdf
init|=
name|getRDFElement
argument_list|()
decl_stmt|;
name|rdf
operator|.
name|appendChild
argument_list|(
name|schema
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Save the XMP document to a file.      *       * @param file      *            The file to save the XMP document to.      *       * @throws Exception      *             If there is an error while writing to the stream.      */
specifier|public
name|void
name|save
parameter_list|(
name|String
name|file
parameter_list|)
throws|throws
name|Exception
block|{
name|XMLUtil
operator|.
name|save
argument_list|(
name|xmpDocument
argument_list|,
name|file
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
comment|/**      * Save the XMP document to a stream.      *       * @param outStream      *            The stream to save the XMP document to.      *       * @throws TransformerException      *             If there is an error while writing to the stream.      */
specifier|public
name|void
name|save
parameter_list|(
name|OutputStream
name|outStream
parameter_list|)
throws|throws
name|TransformerException
block|{
name|XMLUtil
operator|.
name|save
argument_list|(
name|xmpDocument
argument_list|,
name|outStream
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the XML document as a byte array.      *       * @return The metadata as an XML byte stream.      * @throws Exception      *             If there is an error creating the stream.      */
specifier|public
name|byte
index|[]
name|asByteArray
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|XMLUtil
operator|.
name|asByteArray
argument_list|(
name|xmpDocument
argument_list|,
name|encoding
argument_list|)
return|;
block|}
comment|/**      * Get the XML document from this object.      *       * @return This object as an XML document.      */
specifier|public
name|Document
name|getXMPDocument
parameter_list|()
block|{
return|return
name|xmpDocument
return|;
block|}
comment|/**      * Generic add schema method.      *       * @param schema      *            The schema to add.      *       * @return The newly added schema.      */
specifier|protected
name|XMPSchema
name|basicAddSchema
parameter_list|(
name|XMPSchema
name|schema
parameter_list|)
block|{
name|Element
name|rdf
init|=
name|getRDFElement
argument_list|()
decl_stmt|;
name|rdf
operator|.
name|appendChild
argument_list|(
name|schema
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|schema
return|;
block|}
comment|/**      * Create and add a new PDF Schema to this metadata. Typically a XMP      * document will only have one PDF schema (but multiple are supported) so it      * is recommended that you first check the existence of a PDF scheme by      * using getPDFSchema()      *       * @return A new blank PDF schema that is now part of the metadata.      */
specifier|public
name|XMPSchemaPDF
name|addPDFSchema
parameter_list|()
block|{
name|XMPSchemaPDF
name|schema
init|=
operator|new
name|XMPSchemaPDF
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaPDF
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Create and add a new Dublin Core Schema to this metadata. Typically a XMP      * document will only have one schema for each type (but multiple are      * supported) so it is recommended that you first check the existence of a      * this scheme by using getDublinCoreSchema()      *       * @return A new blank PDF schema that is now part of the metadata.      */
specifier|public
name|XMPSchemaDublinCore
name|addDublinCoreSchema
parameter_list|()
block|{
name|XMPSchemaDublinCore
name|schema
init|=
operator|new
name|XMPSchemaDublinCore
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaDublinCore
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Create and add a new Basic Schema to this metadata. Typically a XMP      * document will only have one schema for each type (but multiple are      * supported) so it is recommended that you first check the existence of a      * this scheme by using getDublinCoreSchema()      *       * @return A new blank PDF schema that is now part of the metadata.      */
specifier|public
name|XMPSchemaBasic
name|addBasicSchema
parameter_list|()
block|{
name|XMPSchemaBasic
name|schema
init|=
operator|new
name|XMPSchemaBasic
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaBasic
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Create and add a new IPTC schema to this metadata.      *       * @return A new blank IPTC schema that is now part of the metadata.      */
specifier|public
name|XMPSchemaIptc4xmpCore
name|addIptc4xmpCoreSchema
parameter_list|()
block|{
name|XMPSchemaIptc4xmpCore
name|schema
init|=
operator|new
name|XMPSchemaIptc4xmpCore
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaIptc4xmpCore
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Create and add a new Photoshop schema to this metadata.      *       * @return A new blank Photoshop schema that is now part of the metadata.      */
specifier|public
name|XMPSchemaPhotoshop
name|addPhotoshopSchema
parameter_list|()
block|{
name|XMPSchemaPhotoshop
name|schema
init|=
operator|new
name|XMPSchemaPhotoshop
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaPhotoshop
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * The encoding used to write the XML. Default value:UTF-8<br/> See the      * ENCODING_XXX constants      *       * @param xmlEncoding      *            The encoding to write the XML as.      */
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|xmlEncoding
parameter_list|)
block|{
name|encoding
operator|=
name|xmlEncoding
expr_stmt|;
block|}
comment|/**      * Get the current encoding that will be used to write the XML.      *       * @return The current encoding to write the XML to.      */
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * Get the root RDF element.      *       * @return The root RDF element.      */
specifier|private
name|Element
name|getRDFElement
parameter_list|()
block|{
name|Element
name|rdf
init|=
literal|null
decl_stmt|;
name|NodeList
name|nodes
init|=
name|xmpDocument
operator|.
name|getElementsByTagName
argument_list|(
literal|"rdf:RDF"
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodes
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|rdf
operator|=
operator|(
name|Element
operator|)
name|nodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|rdf
return|;
block|}
comment|/**      * Load metadata from the filesystem.      *       * @param file      *            The file to load the metadata from.      *       * @return The loaded XMP document.      *       * @throws IOException      *             If there is an error reading the data.      */
specifier|public
specifier|static
name|XMPMetadata
name|load
parameter_list|(
name|String
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|XMPMetadata
argument_list|(
name|XMLUtil
operator|.
name|parse
argument_list|(
name|file
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Load a schema from an input source.      *       * @param is      *            The input source to load the schema from.      *       * @return The loaded/parsed schema.      *       * @throws IOException      *             If there was an error while loading the schema.      */
specifier|public
specifier|static
name|XMPMetadata
name|load
parameter_list|(
name|InputSource
name|is
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|XMPMetadata
argument_list|(
name|XMLUtil
operator|.
name|parse
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Load metadata from the filesystem.      *       * @param is      *            The stream to load the data from.      *       * @return The loaded XMP document.      *       * @throws IOException      *             If there is an error reading the data.      */
specifier|public
specifier|static
name|XMPMetadata
name|load
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|XMPMetadata
argument_list|(
name|XMLUtil
operator|.
name|parse
argument_list|(
name|is
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Test main program.      *       * @param args      *            The command line arguments.      * @throws Exception      *             If there is an error.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|XMPMetadata
name|metadata
init|=
operator|new
name|XMPMetadata
argument_list|()
decl_stmt|;
name|XMPSchemaPDF
name|pdf
init|=
name|metadata
operator|.
name|addPDFSchema
argument_list|()
decl_stmt|;
name|pdf
operator|.
name|setAbout
argument_list|(
literal|"uuid:b8659d3a-369e-11d9-b951-000393c97fd8"
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setKeywords
argument_list|(
literal|"ben,bob,pdf"
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setPDFVersion
argument_list|(
literal|"1.3"
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setProducer
argument_list|(
literal|"Acrobat Distiller 6.0.1 for Macintosh"
argument_list|)
expr_stmt|;
name|XMPSchemaDublinCore
name|dc
init|=
name|metadata
operator|.
name|addDublinCoreSchema
argument_list|()
decl_stmt|;
name|dc
operator|.
name|addContributor
argument_list|(
literal|"Ben Litchfield"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addContributor
argument_list|(
literal|"Solar Eclipse"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addContributor
argument_list|(
literal|"Some Other Guy"
argument_list|)
expr_stmt|;
name|XMPSchemaBasic
name|basic
init|=
name|metadata
operator|.
name|addBasicSchema
argument_list|()
decl_stmt|;
name|Thumbnail
name|t
init|=
operator|new
name|Thumbnail
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|t
operator|.
name|setFormat
argument_list|(
name|Thumbnail
operator|.
name|FORMAT_JPEG
argument_list|)
expr_stmt|;
name|t
operator|.
name|setImage
argument_list|(
literal|"IMAGE_DATA"
argument_list|)
expr_stmt|;
name|t
operator|.
name|setHeight
argument_list|(
operator|new
name|Integer
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|t
operator|.
name|setWidth
argument_list|(
operator|new
name|Integer
argument_list|(
literal|200
argument_list|)
argument_list|)
expr_stmt|;
name|basic
operator|.
name|setThumbnail
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|basic
operator|.
name|setBaseURL
argument_list|(
literal|"http://www.pdfbox.org/"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|schemas
init|=
name|metadata
operator|.
name|getSchemas
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"schemas="
operator|+
name|schemas
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|save
argument_list|(
literal|"test.xmp"
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a list of XMPSchema(or subclass) objects.      *       * @return A non null read-only list of schemas that are part of this      *         metadata.      *       * @throws IOException      *             If there is an error creating a specific schema.      */
specifier|public
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|getSchemas
parameter_list|()
throws|throws
name|IOException
block|{
name|NodeList
name|schemaList
init|=
name|xmpDocument
operator|.
name|getElementsByTagName
argument_list|(
literal|"rdf:Description"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|retval
init|=
operator|new
name|ArrayList
argument_list|<
name|XMPSchema
argument_list|>
argument_list|(
name|schemaList
operator|.
name|getLength
argument_list|()
argument_list|)
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
name|schemaList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|schema
init|=
operator|(
name|Element
operator|)
name|schemaList
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
name|NamedNodeMap
name|attributes
init|=
name|schema
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|attributes
operator|.
name|getLength
argument_list|()
operator|&&
operator|!
name|found
condition|;
name|j
operator|++
control|)
block|{
name|Node
name|attribute
init|=
name|attributes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|attribute
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|attribute
operator|.
name|getNodeValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"xmlns:"
argument_list|)
operator|&&
name|nsMappings
operator|.
name|containsKey
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|schemaClass
init|=
name|nsMappings
operator|.
name|get
argument_list|(
name|value
argument_list|)
decl_stmt|;
try|try
block|{
name|Constructor
argument_list|<
name|?
argument_list|>
name|ctor
init|=
name|schemaClass
operator|.
name|getConstructor
argument_list|(
operator|new
name|Class
index|[]
block|{
name|Element
operator|.
name|class
block|,
name|String
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|retval
operator|.
name|add
argument_list|(
operator|(
name|XMPSchema
operator|)
name|ctor
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
name|schema
block|,
name|name
operator|.
name|substring
argument_list|(
literal|6
argument_list|)
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Class "
operator|+
name|schemaClass
operator|.
name|getName
argument_list|()
operator|+
literal|" must have a constructor with the signature of "
operator|+
name|schemaClass
operator|.
name|getName
argument_list|()
operator|+
literal|"( org.w3c.dom.Element, java.lang.String )"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|retval
operator|.
name|add
argument_list|(
operator|new
name|XMPSchema
argument_list|(
name|schema
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Will return all schemas that fit the given namespaceURI. Which is only      * done by using the namespace mapping (nsMapping) and not by actually      * checking the xmlns property.      *       * @param namespaceURI      *            The namespaceURI to filter for.      * @return A list containing the found schemas or an empty list if non are      *         found or the namespaceURI could not be found in the namespace      *         mapping.      * @throws IOException      *             If an operation on the document fails.      */
specifier|public
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|getSchemasByNamespaceURI
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|l
init|=
name|getSchemas
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|result
init|=
operator|new
name|LinkedList
argument_list|<
name|XMPSchema
argument_list|>
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|schemaClass
init|=
name|nsMappings
operator|.
name|get
argument_list|(
name|namespaceURI
argument_list|)
decl_stmt|;
if|if
condition|(
name|schemaClass
operator|==
literal|null
condition|)
block|{
return|return
name|result
return|;
block|}
name|Iterator
argument_list|<
name|XMPSchema
argument_list|>
name|i
init|=
name|l
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|XMPSchema
name|schema
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|schemaClass
operator|.
name|isAssignableFrom
argument_list|(
name|schema
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**      * This will return true if the XMP contains an unknown schema.      *       * @return True if an unknown schema is found, false otherwise      *       * @throws IOException      *             If there is an error      */
specifier|public
name|boolean
name|hasUnknownSchema
parameter_list|()
throws|throws
name|IOException
block|{
name|NodeList
name|schemaList
init|=
name|xmpDocument
operator|.
name|getElementsByTagName
argument_list|(
literal|"rdf:Description"
argument_list|)
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
name|schemaList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|schema
init|=
operator|(
name|Element
operator|)
name|schemaList
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|NamedNodeMap
name|attributes
init|=
name|schema
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Node
name|attribute
init|=
name|attributes
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|attribute
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|attribute
operator|.
name|getNodeValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"xmlns:"
argument_list|)
operator|&&
operator|!
name|nsMappings
operator|.
name|containsKey
argument_list|(
name|value
argument_list|)
operator|&&
operator|!
name|value
operator|.
name|equals
argument_list|(
name|ResourceEvent
operator|.
name|NAMESPACE
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Tries to retrieve a schema from this by classname.      *       * @param targetSchema      *            Class for targetSchema.      *       * @return XMPSchema or null if no target is found.      *       * @throws IOException      *             if there was an error creating the schemas of this.      */
specifier|public
name|XMPSchema
name|getSchemaByClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|targetSchema
parameter_list|)
throws|throws
name|IOException
block|{
name|Iterator
argument_list|<
name|XMPSchema
argument_list|>
name|iter
init|=
name|getSchemas
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|XMPSchema
name|element
init|=
operator|(
name|XMPSchema
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|element
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|targetSchema
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|element
return|;
block|}
block|}
comment|// not found
return|return
literal|null
return|;
block|}
comment|/**      * Merge this metadata with the given metadata object.      *       * @param metadata The metadata to merge with this document.      *       * @throws IOException If there is an error merging the data.      */
specifier|public
name|void
name|merge
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|schemas2
init|=
name|metadata
operator|.
name|getSchemas
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|XMPSchema
argument_list|>
name|iterator
init|=
name|schemas2
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|XMPSchema
name|schema2
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|XMPSchema
name|schema1
init|=
name|getSchemaByClass
argument_list|(
name|schema2
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|schema1
operator|==
literal|null
condition|)
block|{
name|Element
name|rdf
init|=
name|getRDFElement
argument_list|()
decl_stmt|;
name|rdf
operator|.
name|appendChild
argument_list|(
name|xmpDocument
operator|.
name|importNode
argument_list|(
name|schema2
operator|.
name|getElement
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|schema1
operator|.
name|merge
argument_list|(
name|schema2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

