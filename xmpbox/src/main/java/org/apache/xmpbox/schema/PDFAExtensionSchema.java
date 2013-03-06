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
operator|.
name|schema
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
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
name|xmpbox
operator|.
name|type
operator|.
name|ArrayProperty
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
name|Cardinality
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
name|PropertyType
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
name|Types
import|;
end_import

begin_comment
comment|/**  * Representation of a PDF/A Extension schema description schema  *   * @author a183132  *   */
end_comment

begin_class
annotation|@
name|StructuredType
argument_list|(
name|preferedPrefix
operator|=
literal|"pdfaExtension"
argument_list|,
name|namespace
operator|=
literal|"http://www.aiim.org/pdfa/ns/extension/"
argument_list|)
specifier|public
class|class
name|PDFAExtensionSchema
extends|extends
name|XMPSchema
block|{
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|PDFASchema
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Bag
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|SCHEMAS
init|=
literal|"schemas"
decl_stmt|;
comment|/**      * Build a new PDFExtension schema      *       * @param metadata      *            The metadata to attach this schema XMPMetadata      */
specifier|public
name|PDFAExtensionSchema
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
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
argument_list|)
expr_stmt|;
block|}
comment|/**      *       * @return the list of subject values      */
specifier|public
name|ArrayProperty
name|getSchemasProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|SCHEMAS
argument_list|)
return|;
block|}
block|}
end_class

end_unit

