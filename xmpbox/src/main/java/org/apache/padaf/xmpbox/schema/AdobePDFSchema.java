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
name|Cardinality
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
name|StructuredType
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
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|Types
import|;
end_import

begin_comment
comment|/**  * Representation of Adobe PDF Schema  *   * @author a183132  *   */
end_comment

begin_class
annotation|@
name|StructuredType
argument_list|(
name|preferedPrefix
operator|=
literal|"pdf"
argument_list|,
name|namespace
operator|=
literal|"http://ns.adobe.com/pdf/1.3/"
argument_list|)
specifier|public
class|class
name|AdobePDFSchema
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
name|Text
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Simple
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|KEYWORDS
init|=
literal|"Keywords"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|Text
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Simple
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|PDF_VERSION
init|=
literal|"PDFVersion"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|Text
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Simple
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|PRODUCER
init|=
literal|"Producer"
decl_stmt|;
comment|/** 	 * Constructor of an Adobe PDF schema with preferred prefix 	 *  	 * @param metadata 	 *            The metadata to attach this schema 	 */
specifier|public
name|AdobePDFSchema
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
comment|/** 	 * Constructor of an Adobe PDF schema with specified prefix 	 *  	 * @param metadata 	 *            The metadata to attach this schema 	 * @param ownPrefix 	 *            The prefix to assign 	 */
specifier|public
name|AdobePDFSchema
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|ownPrefix
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|ownPrefix
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Set the PDF keywords 	 *  	 * @param value 	 *            Value to set 	 */
specifier|public
name|void
name|setKeywords
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|TextType
name|keywords
decl_stmt|;
name|keywords
operator|=
name|createTextType
argument_list|(
name|KEYWORDS
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|addProperty
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Set the PDF keywords 	 *  	 * @param keywords 	 *            Property to set 	 */
specifier|public
name|void
name|setKeywordsProperty
parameter_list|(
name|TextType
name|keywords
parameter_list|)
block|{
name|addProperty
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Set the PDFVersion 	 *  	 * @param value 	 *            Value to set 	 */
specifier|public
name|void
name|setPDFVersion
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|TextType
name|version
decl_stmt|;
name|version
operator|=
name|createTextType
argument_list|(
name|PDF_VERSION
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|addProperty
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Set the PDFVersion 	 *  	 * @param version 	 *            Property to set 	 */
specifier|public
name|void
name|setPDFVersionProperty
parameter_list|(
name|TextType
name|version
parameter_list|)
block|{
name|addProperty
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Set the PDFProducer 	 *  	 * @param value 	 *            Value to set 	 */
specifier|public
name|void
name|setProducer
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|TextType
name|producer
decl_stmt|;
name|producer
operator|=
name|createTextType
argument_list|(
name|PRODUCER
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|addProperty
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Set the PDFProducer 	 *  	 * @param producer 	 *            Property to set 	 */
specifier|public
name|void
name|setProducerProperty
parameter_list|(
name|TextType
name|producer
parameter_list|)
block|{
name|addProperty
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Give the PDF Keywords property 	 *  	 * @return The property object 	 */
specifier|public
name|TextType
name|getKeywordsProperty
parameter_list|()
block|{
name|AbstractField
name|tmp
init|=
name|getProperty
argument_list|(
name|KEYWORDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmp
operator|instanceof
name|TextType
condition|)
block|{
return|return
operator|(
name|TextType
operator|)
name|tmp
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/** 	 * Give the PDF Keywords property value (string) 	 *  	 * @return The property value 	 */
specifier|public
name|String
name|getKeywords
parameter_list|()
block|{
name|AbstractField
name|tmp
init|=
name|getProperty
argument_list|(
name|KEYWORDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmp
operator|instanceof
name|TextType
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
return|return
literal|null
return|;
block|}
comment|/** 	 * Give the PDFVersion property 	 *  	 * @return The property object 	 */
specifier|public
name|TextType
name|getPDFVersionProperty
parameter_list|()
block|{
name|AbstractField
name|tmp
init|=
name|getProperty
argument_list|(
name|PDF_VERSION
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmp
operator|instanceof
name|TextType
condition|)
block|{
return|return
operator|(
name|TextType
operator|)
name|tmp
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/** 	 * Give the PDFVersion property value (string) 	 *  	 * @return The property value 	 */
specifier|public
name|String
name|getPDFVersion
parameter_list|()
block|{
name|AbstractField
name|tmp
init|=
name|getProperty
argument_list|(
name|PDF_VERSION
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmp
operator|instanceof
name|TextType
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
return|return
literal|null
return|;
block|}
comment|/** 	 * Give the producer property 	 *  	 * @return The property object 	 */
specifier|public
name|TextType
name|getProducerProperty
parameter_list|()
block|{
name|AbstractField
name|tmp
init|=
name|getProperty
argument_list|(
name|PRODUCER
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmp
operator|instanceof
name|TextType
condition|)
block|{
return|return
operator|(
name|TextType
operator|)
name|tmp
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/** 	 * Give the producer property value (string) 	 *  	 * @return The property value 	 */
specifier|public
name|String
name|getProducer
parameter_list|()
block|{
name|AbstractField
name|tmp
init|=
name|getProperty
argument_list|(
name|PRODUCER
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmp
operator|instanceof
name|TextType
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
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

