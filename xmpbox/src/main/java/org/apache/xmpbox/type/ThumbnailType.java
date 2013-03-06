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
name|type
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
name|XmpConstants
import|;
end_import

begin_comment
comment|/**  * Object representation of an Thumbnail XMP type  *   * @author eric  */
end_comment

begin_class
annotation|@
name|StructuredType
argument_list|(
name|preferedPrefix
operator|=
literal|"xmpGImg"
argument_list|,
name|namespace
operator|=
literal|"http://ns.adobe.com/xap/1.0/g/img/"
argument_list|)
specifier|public
class|class
name|ThumbnailType
extends|extends
name|AbstractStructuredType
block|{
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|Choice
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
name|FORMAT
init|=
literal|"format"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|Integer
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
name|HEIGHT
init|=
literal|"height"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|Integer
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
name|WIDTH
init|=
literal|"width"
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
name|IMAGE
init|=
literal|"image"
decl_stmt|;
comment|/**      *       * @param metadata      *            The metadata to attach to this property      * @param namespace      *            the namespace URI to associate to this property      * @param prefix      *            The prefix to set for this property      * @param propertyName      *            The local Name of this thumbnail type      */
specifier|public
name|ThumbnailType
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
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
name|XmpConstants
operator|.
name|RDF_NAMESPACE
argument_list|,
literal|"parseType"
argument_list|,
literal|"Resource"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get Height      *       * @return the height      */
specifier|public
name|Integer
name|getHeight
parameter_list|()
block|{
name|AbstractField
name|absProp
init|=
name|getFirstEquivalentProperty
argument_list|(
name|HEIGHT
argument_list|,
name|IntegerType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|absProp
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
operator|(
name|IntegerType
operator|)
name|absProp
operator|)
operator|.
name|getValue
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Set Height      *       * @param prefix      *            the prefix of Height property to set      * @param name      *            the name of Height property to set      * @param height      *            the value of Height property to set      */
specifier|public
name|void
name|setHeight
parameter_list|(
name|Integer
name|height
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|HEIGHT
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get Width      *       * @return the width      */
specifier|public
name|Integer
name|getWidth
parameter_list|()
block|{
name|AbstractField
name|absProp
init|=
name|getFirstEquivalentProperty
argument_list|(
name|WIDTH
argument_list|,
name|IntegerType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|absProp
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
operator|(
name|IntegerType
operator|)
name|absProp
operator|)
operator|.
name|getValue
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Set Width      *       * @param prefix      *            the prefix of width property to set      * @param name      *            the name of width property to set      * @param width      *            the value of width property to set      */
specifier|public
name|void
name|setWidth
parameter_list|(
name|Integer
name|width
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|WIDTH
argument_list|,
name|width
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get The img data      *       * @return the img      */
specifier|public
name|String
name|getImage
parameter_list|()
block|{
name|AbstractField
name|absProp
init|=
name|getFirstEquivalentProperty
argument_list|(
name|IMAGE
argument_list|,
name|TextType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|absProp
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
operator|(
name|TextType
operator|)
name|absProp
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
comment|/**      * Set Image data      *       * @param prefix      *            the prefix of image property to set      * @param name      *            the name of image property to set      * @param image      *            the value of image property to set      */
specifier|public
name|void
name|setImage
parameter_list|(
name|String
name|image
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|IMAGE
argument_list|,
name|image
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get Format      *       * @return the format      */
specifier|public
name|String
name|getFormat
parameter_list|()
block|{
name|AbstractField
name|absProp
init|=
name|getFirstEquivalentProperty
argument_list|(
name|FORMAT
argument_list|,
name|ChoiceType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|absProp
operator|!=
literal|null
condition|)
block|{
return|return
operator|(
operator|(
name|TextType
operator|)
name|absProp
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
comment|/**      * Set Format      *       * @param prefix      *            the prefix of format property to set      * @param name      *            the name of format property to set      * @param format      *            the value of format property to set      */
specifier|public
name|void
name|setFormat
parameter_list|(
name|String
name|format
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|FORMAT
argument_list|,
name|format
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

