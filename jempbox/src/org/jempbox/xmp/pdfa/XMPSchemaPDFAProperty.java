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
name|jempbox
operator|.
name|xmp
operator|.
name|XMPSchema
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
comment|/**  * Define XMP properties used PDFA extension schema description schemas.  * TODO 2 naked so far, implement  *   * @author Karsten Krieg (kkrieg@intarsys.de)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|XMPSchemaPDFAProperty
extends|extends
name|XMPSchema
block|{
comment|/**      * The namespace for this schema.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE
init|=
literal|"http://www.aiim.org/pdfa/ns/property"
decl_stmt|;
comment|/**      * Construct a new blank PDFA schema.      *      * @param parent The parent metadata schema that this will be part of.      */
specifier|public
name|XMPSchemaPDFAProperty
parameter_list|(
name|XMPMetadata
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"pdfaProperty"
argument_list|,
name|NAMESPACE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor from existing XML element.      *       * @param element The existing element.      * @param prefix The schema prefix.      */
specifier|public
name|XMPSchemaPDFAProperty
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|super
argument_list|(
name|element
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

