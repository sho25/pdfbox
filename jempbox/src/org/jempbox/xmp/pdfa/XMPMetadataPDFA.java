begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2006, www.jempbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.jempbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|jempbox
operator|.
name|xmp
operator|.
name|pdfa
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
name|org
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
name|jempbox
operator|.
name|xmp
operator|.
name|XMPMetadata
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
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_comment
comment|/**  * PDFA Metadata.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|XMPMetadataPDFA
extends|extends
name|XMPMetadata
block|{
comment|/**      * Constructor.      *       * @throws IOException If there is an error creating this metadata.      */
specifier|public
name|XMPMetadataPDFA
parameter_list|()
throws|throws
name|IOException
block|{
name|super
argument_list|()
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param doc The XML document that maps to the metadata.      */
specifier|public
name|XMPMetadataPDFA
parameter_list|(
name|Document
name|doc
parameter_list|)
block|{
name|super
argument_list|(
name|doc
argument_list|)
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
comment|// PDFA specific schemas
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaPDFAField
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaPDFAField
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaPDFAId
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaPDFAId
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaPDFAProperty
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaPDFAProperty
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaPDFASchema
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaPDFASchema
operator|.
name|class
argument_list|)
expr_stmt|;
name|nsMappings
operator|.
name|put
argument_list|(
name|XMPSchemaPDFAType
operator|.
name|NAMESPACE
argument_list|,
name|XMPSchemaPDFAType
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Load a a PDFA metadata.      *       * @param is An XML input stream      * @return A PDFA metadata.      * @throws IOException If there is an error loading the XML document.      */
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
name|XMPMetadataPDFA
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
comment|/**      * Get the PDFAField schema.      *       * @return A PDFAField schema.      *       * @throws IOException If there is an error finding the scheam.      */
specifier|public
name|XMPSchemaPDFAField
name|getPDFAFieldSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaPDFAField
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaPDFAField
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Add a new PDFAField schema.      *       * @return The newly added PDFA schema.      */
specifier|public
name|XMPSchemaPDFAField
name|addPDFAFieldSchema
parameter_list|()
block|{
name|XMPSchemaPDFAField
name|schema
init|=
operator|new
name|XMPSchemaPDFAField
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaPDFAField
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Get the PDFA ID schema.      * @return The PDFA ID schema.      * @throws IOException If there is an error accessing the PDFA id schema.      */
specifier|public
name|XMPSchemaPDFAId
name|getPDFAIdSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaPDFAId
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaPDFAId
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Add a PDFA Id schema and return the result.      *       * @return The newly created PDFA Id schema.      */
specifier|public
name|XMPSchemaPDFAId
name|addPDFAIdSchema
parameter_list|()
block|{
name|XMPSchemaPDFAId
name|schema
init|=
operator|new
name|XMPSchemaPDFAId
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaPDFAId
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Get the PDFA property schema.      *       * @return The PDFA property schema.      *       * @throws IOException If there is an error accessing the PDFA property schema.      */
specifier|public
name|XMPSchemaPDFAProperty
name|getPDFAPropertySchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaPDFAProperty
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaPDFAProperty
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Create a PDFA property schema.      *       * @return The newly created property schema.      */
specifier|public
name|XMPSchemaPDFAProperty
name|addPDFAPropertySchema
parameter_list|()
block|{
name|XMPSchemaPDFAProperty
name|schema
init|=
operator|new
name|XMPSchemaPDFAProperty
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaPDFAProperty
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Get the PDFA schema.      *       * @return The PDFA schema.      *       * @throws IOException If there is an error getting the PDFA schema.      */
specifier|public
name|XMPSchemaPDFASchema
name|getPDFASchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaPDFASchema
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaPDFASchema
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Add a PDFA schema.      *       * @return The newly created PDFA schema.      */
specifier|public
name|XMPSchemaPDFASchema
name|addPDFASchema
parameter_list|()
block|{
name|XMPSchemaPDFASchema
name|schema
init|=
operator|new
name|XMPSchemaPDFASchema
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaPDFASchema
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
comment|/**      * Get the PDFA type schema.      *       * @return The PDFA type schema.      *       * @throws IOException If there is an error accessing the PDFA type schema.      */
specifier|public
name|XMPSchemaPDFAType
name|getPDFATypeSchema
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|(
name|XMPSchemaPDFAType
operator|)
name|getSchemaByClass
argument_list|(
name|XMPSchemaPDFAType
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Add a new PDFA type schema.      *       * @return The newly created PDFA type schema.      */
specifier|public
name|XMPSchemaPDFAType
name|addPDFATypeSchema
parameter_list|()
block|{
name|XMPSchemaPDFAType
name|schema
init|=
operator|new
name|XMPSchemaPDFAType
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
operator|(
name|XMPSchemaPDFAType
operator|)
name|basicAddSchema
argument_list|(
name|schema
argument_list|)
return|;
block|}
block|}
end_class

end_unit

