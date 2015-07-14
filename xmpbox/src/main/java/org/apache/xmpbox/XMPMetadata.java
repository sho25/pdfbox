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
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|AdobePDFSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|DublinCoreSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
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
name|xmpbox
operator|.
name|schema
operator|.
name|PDFAIdentificationSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|PhotoshopSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPBasicJobTicketSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPBasicSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPMediaManagementSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPRightsManagementSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
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
name|xmpbox
operator|.
name|type
operator|.
name|StructuredType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|TypeMapping
import|;
end_import

begin_comment
comment|/**  * Object representation of XMPMetaData Be CAREFUL: typically, metadata should contain only one schema for each type  * (each NSURI). Retrieval of common schemas (like DublinCore) is based on this fact and take the first schema of this  * type encountered. However, XmpBox allow you to place schemas of same type with different prefix. If you do that, you  * must retrieve all schemas by yourself with getAllSchemas or with getSchema which use prefix parameter.  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|XMPMetadata
block|{
specifier|private
name|String
name|xpacketId
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|xpacketBegin
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|xpacketBytes
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|xpacketEncoding
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|xpacketEndData
init|=
name|XmpConstants
operator|.
name|DEFAULT_XPACKET_END
decl_stmt|;
specifier|private
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|schemas
decl_stmt|;
specifier|private
name|TypeMapping
name|typeMapping
decl_stmt|;
comment|/**      * Contructor of an empty default XMPMetaData.      *       */
specifier|protected
name|XMPMetadata
parameter_list|()
block|{
name|this
argument_list|(
name|XmpConstants
operator|.
name|DEFAULT_XPACKET_BEGIN
argument_list|,
name|XmpConstants
operator|.
name|DEFAULT_XPACKET_ID
argument_list|,
name|XmpConstants
operator|.
name|DEFAULT_XPACKET_BYTES
argument_list|,
name|XmpConstants
operator|.
name|DEFAULT_XPACKET_ENCODING
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates blank XMP doc with specified parameters.      *       * @param xpacketBegin Value of xpacketBegin      * @param xpacketId Value of xpacketId      * @param xpacketBytes Value of xpacketBytes      * @param xpacketEncoding Value of xpacket encoding      */
specifier|protected
name|XMPMetadata
parameter_list|(
name|String
name|xpacketBegin
parameter_list|,
name|String
name|xpacketId
parameter_list|,
name|String
name|xpacketBytes
parameter_list|,
name|String
name|xpacketEncoding
parameter_list|)
block|{
name|this
operator|.
name|schemas
operator|=
operator|new
name|ArrayList
argument_list|<
name|XMPSchema
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|typeMapping
operator|=
operator|new
name|TypeMapping
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|xpacketBegin
operator|=
name|xpacketBegin
expr_stmt|;
name|this
operator|.
name|xpacketId
operator|=
name|xpacketId
expr_stmt|;
name|this
operator|.
name|xpacketBytes
operator|=
name|xpacketBytes
expr_stmt|;
name|this
operator|.
name|xpacketEncoding
operator|=
name|xpacketEncoding
expr_stmt|;
block|}
comment|/**      * Creates blank XMP doc with default parameters.      *      * @return the XMPMetadata created.      */
specifier|public
specifier|static
name|XMPMetadata
name|createXMPMetadata
parameter_list|()
block|{
return|return
operator|new
name|XMPMetadata
argument_list|()
return|;
block|}
comment|/**      * Creates blank XMP doc with specified parameters.      *       * @param xpacketBegin Value of xpacketBegin      * @param xpacketId Value of xpacketId      * @param xpacketBytes Value of xpacketBytes      * @param xpacketEncoding Value of xpacket encoding      * @return the XMPMetadata created.      */
specifier|public
specifier|static
name|XMPMetadata
name|createXMPMetadata
parameter_list|(
name|String
name|xpacketBegin
parameter_list|,
name|String
name|xpacketId
parameter_list|,
name|String
name|xpacketBytes
parameter_list|,
name|String
name|xpacketEncoding
parameter_list|)
block|{
return|return
operator|new
name|XMPMetadata
argument_list|(
name|xpacketBegin
argument_list|,
name|xpacketId
argument_list|,
name|xpacketBytes
argument_list|,
name|xpacketEncoding
argument_list|)
return|;
block|}
comment|/**      * Get TypeMapping.      * @return the defined TypeMapping.      */
specifier|public
name|TypeMapping
name|getTypeMapping
parameter_list|()
block|{
return|return
name|this
operator|.
name|typeMapping
return|;
block|}
comment|/**      * Get xpacketBytes.      *       * @return value of xpacketBytes field      */
specifier|public
name|String
name|getXpacketBytes
parameter_list|()
block|{
return|return
name|xpacketBytes
return|;
block|}
comment|/**      * Get xpacket encoding.      *       * @return value of xpacket Encoding field      */
specifier|public
name|String
name|getXpacketEncoding
parameter_list|()
block|{
return|return
name|xpacketEncoding
return|;
block|}
comment|/**      * Get xpacket Begin.      *       * @return value of xpacket Begin field      */
specifier|public
name|String
name|getXpacketBegin
parameter_list|()
block|{
return|return
name|xpacketBegin
return|;
block|}
comment|/**      * Get xpacket Id.      *       * @return value of xpacket Id field      */
specifier|public
name|String
name|getXpacketId
parameter_list|()
block|{
return|return
name|xpacketId
return|;
block|}
comment|/**      * Get All Schemas declared in this metadata representation.      *       * @return List of declared schemas      */
specifier|public
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|getAllSchemas
parameter_list|()
block|{
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|schem
init|=
operator|new
name|ArrayList
argument_list|<
name|XMPSchema
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|XMPSchema
argument_list|>
name|it
init|=
name|schemas
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|schem
operator|.
name|add
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|schem
return|;
block|}
comment|/**      * Set special XPACKET END PI.      *       * @param data The XPacket End value      */
specifier|public
name|void
name|setEndXPacket
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|xpacketEndData
operator|=
name|data
expr_stmt|;
block|}
comment|/**      * get XPACKET END PI.      *       * @return XPACKET END Value      */
specifier|public
name|String
name|getEndXPacket
parameter_list|()
block|{
return|return
name|xpacketEndData
return|;
block|}
comment|/**      * Get the XMPSchema for the specified namespace.      *       * Return the schema corresponding to this nsURI<br/>      * BE CAREFUL: typically, Metadata should contain one schema for each type.      * This method returns the first schema encountered      * corresponding to this NSURI.<br/>      * Return null if unknown      *       * @param nsURI The namespace URI corresponding to the schema wanted      * @return The matching XMP schema representation      */
specifier|public
name|XMPSchema
name|getSchema
parameter_list|(
name|String
name|nsURI
parameter_list|)
block|{
name|Iterator
argument_list|<
name|XMPSchema
argument_list|>
name|it
init|=
name|schemas
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|XMPSchema
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
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|nsURI
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
comment|/**       * Get the XMPSchema for the specified Class.      *       * Return the schema corresponding to this Class<br/>      * BE CAREFUL: typically, Metadata should contain one schema for each type.      * This method returns the first schema encountered      * corresponding to this Class.<br/>      * Return null if unknown      *       * @param clz The Class corresponding to the schema wanted      * @return The matching XMP schema representation      */
specifier|public
name|XMPSchema
name|getSchema
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|XMPSchema
argument_list|>
name|clz
parameter_list|)
block|{
name|StructuredType
name|st
init|=
name|clz
operator|.
name|getAnnotation
argument_list|(
name|StructuredType
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|getSchema
argument_list|(
name|st
operator|.
name|namespace
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Return the schema corresponding to this nsURI and a prefix.      *       * This method is here to treat metadata which embed more      * than one time the same schema. It permits to retrieve a specific schema with its prefix      *       * @param prefix The prefix fixed in the schema wanted      * @param nsURI The namespace URI corresponding to the schema wanted      * @return The Class Schema representation      */
specifier|public
name|XMPSchema
name|getSchema
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|nsURI
parameter_list|)
block|{
name|Iterator
argument_list|<
name|XMPSchema
argument_list|>
name|it
init|=
name|getAllSchemas
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|XMPSchema
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
name|getNamespace
argument_list|()
operator|.
name|equals
argument_list|(
name|nsURI
argument_list|)
operator|&&
name|tmp
operator|.
name|getPrefix
argument_list|()
operator|.
name|equals
argument_list|(
name|prefix
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
comment|/**      * Create and add an unspecified schema.      *       * @param nsPrefix The prefix wanted for the schema      * @param nsURI The namespace URI wanted for the schema      * @return The schema added in order to work on it      */
specifier|public
name|XMPSchema
name|createAndAddDefaultSchema
parameter_list|(
name|String
name|nsPrefix
parameter_list|,
name|String
name|nsURI
parameter_list|)
block|{
name|XMPSchema
name|schem
init|=
operator|new
name|XMPSchema
argument_list|(
name|this
argument_list|,
name|nsURI
argument_list|,
name|nsPrefix
argument_list|)
decl_stmt|;
name|schem
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|schem
argument_list|)
expr_stmt|;
return|return
name|schem
return|;
block|}
comment|/**      * Create and add a default PDFA Extension schema to this metadata.      *       * This method return the created schema to enter information.      * This PDFAExtension is created with all default namespaces used in PDFAExtensionSchema.      *       * @return PDFAExtension schema added in order to work on it      */
specifier|public
name|PDFAExtensionSchema
name|createAndAddPDFAExtensionSchemaWithDefaultNS
parameter_list|()
block|{
name|PDFAExtensionSchema
name|pdfAExt
init|=
operator|new
name|PDFAExtensionSchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|pdfAExt
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|pdfAExt
argument_list|)
expr_stmt|;
return|return
name|pdfAExt
return|;
block|}
comment|/**      * Create and add a default PDFA Extension schema to this metadata.      *       * This method return the created schema to enter information.      * This PDFAExtension is created with specified list of namespaces.      *       * @param namespaces Special namespaces list to use      * @return schema added in order to work on it      * @throws XmpSchemaException If namespaces list not contains PDF/A Extension namespace URI      */
specifier|public
name|PDFAExtensionSchema
name|createAndAddPDFAExtensionSchemaWithNS
parameter_list|(
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
name|PDFAExtensionSchema
name|pdfAExt
init|=
operator|new
name|PDFAExtensionSchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|pdfAExt
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|pdfAExt
argument_list|)
expr_stmt|;
return|return
name|pdfAExt
return|;
block|}
comment|/**      * Get the PDFA Extension schema.      *       * This method return null if not found.      *       * @return The PDFAExtension schema or null if not declared      */
specifier|public
name|PDFAExtensionSchema
name|getPDFExtensionSchema
parameter_list|()
block|{
return|return
operator|(
name|PDFAExtensionSchema
operator|)
name|getSchema
argument_list|(
name|PDFAExtensionSchema
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Create and add a default PDFA Identification schema to this metadata.      *       * This method return the created schema to enter information.      *       * @return schema added in order to work on it      */
specifier|public
name|PDFAIdentificationSchema
name|createAndAddPFAIdentificationSchema
parameter_list|()
block|{
name|PDFAIdentificationSchema
name|pdfAId
init|=
operator|new
name|PDFAIdentificationSchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|pdfAId
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|pdfAId
argument_list|)
expr_stmt|;
return|return
name|pdfAId
return|;
block|}
comment|/**      * Get the PDFA Identification schema.      *       * This method return null if not found.      *       * @return The PDFAIdentificationSchema schema or null if not declared      */
specifier|public
name|PDFAIdentificationSchema
name|getPDFIdentificationSchema
parameter_list|()
block|{
return|return
operator|(
name|PDFAIdentificationSchema
operator|)
name|getSchema
argument_list|(
name|PDFAIdentificationSchema
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Create and add a default Dublin Core schema to this metadata.      *       * This method return the created schema to enter information      *       * @return schema added in order to work on it      */
specifier|public
name|DublinCoreSchema
name|createAndAddDublinCoreSchema
parameter_list|()
block|{
name|DublinCoreSchema
name|dc
init|=
operator|new
name|DublinCoreSchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|dc
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|dc
argument_list|)
expr_stmt|;
return|return
name|dc
return|;
block|}
comment|/**      * Get the Dublin Core schema.      *       * This method return null if not found      *       * @return The DublinCoreSchema schema or null if not declared.      */
specifier|public
name|DublinCoreSchema
name|getDublinCoreSchema
parameter_list|()
block|{
return|return
operator|(
name|DublinCoreSchema
operator|)
name|getSchema
argument_list|(
name|DublinCoreSchema
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Create and add a default Basic Job Ticket schema to this metadata.      *       * This method return the created schema to enter information.      *       * @return schema added in order to work on it.      */
specifier|public
name|XMPBasicJobTicketSchema
name|createAndAddBasicJobTicketSchema
parameter_list|()
block|{
name|XMPBasicJobTicketSchema
name|sc
init|=
operator|new
name|XMPBasicJobTicketSchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|sc
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|sc
argument_list|)
expr_stmt|;
return|return
name|sc
return|;
block|}
comment|/**      * Get the Basic Job Ticket Schema.      *       * This method return null if not found      *       * @return The XMPBasicJobTicketSchema schema or null if not declared.      */
specifier|public
name|XMPBasicJobTicketSchema
name|getBasicJobTicketSchema
parameter_list|()
block|{
return|return
operator|(
name|XMPBasicJobTicketSchema
operator|)
name|getSchema
argument_list|(
name|XMPBasicJobTicketSchema
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Create and add a default XMP Rights Management Schema to this metadata.      *       * This method return the created schema to enter information.      *       * @return schema added in order to work on it      */
specifier|public
name|XMPRightsManagementSchema
name|createAndAddXMPRightsManagementSchema
parameter_list|()
block|{
name|XMPRightsManagementSchema
name|rights
init|=
operator|new
name|XMPRightsManagementSchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|rights
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|rights
argument_list|)
expr_stmt|;
return|return
name|rights
return|;
block|}
comment|/**      * Get the XMP Rights Management Schema.      *       * This method return null if not found.      *       * @return The XMPRightsManagementSchema schema or null if not declared      */
specifier|public
name|XMPRightsManagementSchema
name|getXMPRightsManagementSchema
parameter_list|()
block|{
return|return
operator|(
name|XMPRightsManagementSchema
operator|)
name|getSchema
argument_list|(
name|XMPRightsManagementSchema
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Create and add a XMP Basic schema to this metadata.      *       * This method return the created schema to enter information      *       * @return schema added in order to work on it      */
specifier|public
name|XMPBasicSchema
name|createAndAddXMPBasicSchema
parameter_list|()
block|{
name|XMPBasicSchema
name|xmpB
init|=
operator|new
name|XMPBasicSchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|xmpB
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|xmpB
argument_list|)
expr_stmt|;
return|return
name|xmpB
return|;
block|}
comment|/**      * Get the XMP Basic schema.      *       * This method return null if not found      *       * @return The XMPBasicSchema schema or null if not declared      */
specifier|public
name|XMPBasicSchema
name|getXMPBasicSchema
parameter_list|()
block|{
return|return
operator|(
name|XMPBasicSchema
operator|)
name|getSchema
argument_list|(
name|XMPBasicSchema
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Create and add a XMP Media Management schema to this metadata.      *       * This method return the created schema to enter      * information      *       * @return schema added in order to work on it      */
specifier|public
name|XMPMediaManagementSchema
name|createAndAddXMPMediaManagementSchema
parameter_list|()
block|{
name|XMPMediaManagementSchema
name|xmpMM
init|=
operator|new
name|XMPMediaManagementSchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|xmpMM
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|xmpMM
argument_list|)
expr_stmt|;
return|return
name|xmpMM
return|;
block|}
comment|/***      * Create and add Photoshop Schema to this metadata.      *       * This method return the created schema to enter information      *       * @return schema added in order to work on it      */
specifier|public
name|PhotoshopSchema
name|createAndAddPhotoshopSchema
parameter_list|()
block|{
name|PhotoshopSchema
name|photoshop
init|=
operator|new
name|PhotoshopSchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|photoshop
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|photoshop
argument_list|)
expr_stmt|;
return|return
name|photoshop
return|;
block|}
comment|/**      * Get the Photoshop schema.      *       * This method return null if not found      *      * @return The PhotoshopSchema schema or null if not declared      */
specifier|public
name|PhotoshopSchema
name|getPhotoshopSchema
parameter_list|()
block|{
return|return
operator|(
name|PhotoshopSchema
operator|)
name|getSchema
argument_list|(
name|PhotoshopSchema
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Get the XMP Media Management schema.      *       * This method return null if not found      *       * @return The XMPMediaManagementSchema schema or null if not declared      */
specifier|public
name|XMPMediaManagementSchema
name|getXMPMediaManagementSchema
parameter_list|()
block|{
return|return
operator|(
name|XMPMediaManagementSchema
operator|)
name|getSchema
argument_list|(
name|XMPMediaManagementSchema
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Create and add an Adobe PDF schema to this metadata.      *       * This method return the created schema to enter information      *       * @return schema added in order to work on it      */
specifier|public
name|AdobePDFSchema
name|createAndAddAdobePDFSchema
parameter_list|()
block|{
name|AdobePDFSchema
name|pdf
init|=
operator|new
name|AdobePDFSchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|pdf
operator|.
name|setAboutAsSimple
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|addSchema
argument_list|(
name|pdf
argument_list|)
expr_stmt|;
return|return
name|pdf
return|;
block|}
comment|/**      * Get the Adobe PDF schema.      *       * This method return null if not found      *       * @return The AdobePDFSchema schema or null if not declared      */
specifier|public
name|AdobePDFSchema
name|getAdobePDFSchema
parameter_list|()
block|{
return|return
operator|(
name|AdobePDFSchema
operator|)
name|getSchema
argument_list|(
name|AdobePDFSchema
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Add a schema to the current structure.      *       * @param obj the schema to add      */
specifier|public
name|void
name|addSchema
parameter_list|(
name|XMPSchema
name|obj
parameter_list|)
block|{
name|schemas
operator|.
name|add
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
comment|/**      * Remove a schema.      *       * @param schema The schema to remove      */
specifier|public
name|void
name|removeSchema
parameter_list|(
name|XMPSchema
name|schema
parameter_list|)
block|{
name|schemas
operator|.
name|remove
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes all schemas defined.      */
specifier|public
name|void
name|clearSchemas
parameter_list|()
block|{
name|schemas
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

