begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Define XMP properties used PDFA extension schema description schemas.  *   * @author Karsten Krieg (kkrieg@intarsys.de)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|XMPSchemaPDFASchema
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
literal|"http://www.aiim.org/pdfa/ns/schema"
decl_stmt|;
comment|/**      * Construct a new blank PDFA schema.      *      * @param parent The parent metadata schema that this will be part of.      */
specifier|public
name|XMPSchemaPDFASchema
parameter_list|(
name|XMPMetadata
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"pdfaSchema"
argument_list|,
name|NAMESPACE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor from existing XML element.      *       * @param element The existing element.      * @param prefix The schema prefix.      */
specifier|public
name|XMPSchemaPDFASchema
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
comment|/**      * PDFA schema.      *      * @param schema The optional description of schema.      */
specifier|public
name|void
name|setSchema
parameter_list|(
name|String
name|schema
parameter_list|)
block|{
name|setTextProperty
argument_list|(
literal|"pdfaSchema:schema"
argument_list|,
name|schema
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the PDFA schema.      *      * @return The optional description of schema.      */
specifier|public
name|String
name|getSchema
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
literal|"pdfaSchema:schema"
argument_list|)
return|;
block|}
comment|/**      * PDFA Schema prefix.      *      * @param prefix Preferred schema namespace prefix.      */
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|setTextProperty
argument_list|(
literal|"pdfaSchema:prefix"
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the PDFA Schema prefix.      *      * @return Preferred schema namespace prefix.      */
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
literal|"pdfaSchema:prefix"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

