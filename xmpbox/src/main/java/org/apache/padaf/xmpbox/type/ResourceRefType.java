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
name|type
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|XmpConstants
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
name|schema
operator|.
name|XMPSchema
import|;
end_import

begin_class
specifier|public
class|class
name|ResourceRefType
extends|extends
name|AbstractStructuredType
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ELEMENT_NS
init|=
literal|"http://ns.adobe.com/xap/1.0/sType/ResourceRef#"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PREFERRED_PREFIX
init|=
literal|"stRef"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"URI"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|DOCUMENT_ID
init|=
literal|"documentID"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"URI"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|FILE_PATH
init|=
literal|"filePath"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"URI"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|INSTANCE_ID
init|=
literal|"instanceID"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Date"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|LAST_MODIFY_DATE
init|=
literal|"lastModifyDate"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"URI"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|MANAGE_TO
init|=
literal|"manageTo"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"URI"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|MANAGE_UI
init|=
literal|"manageUI"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"AgentName"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|MANAGER
init|=
literal|"manager"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|MANAGER_VARIANT
init|=
literal|"managerVariant"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|PART_MAPPING
init|=
literal|"partMapping"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|RENDITION_PARAMS
init|=
literal|"renditionParams"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Text"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|VERSION_ID
init|=
literal|"versionID"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Choice"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|MASK_MARKERS
init|=
literal|"maskMarkers"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"RenditionClass"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|RENDITION_CLASS
init|=
literal|"renditionClass"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Part"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|FROM_PART
init|=
literal|"fromPart"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|propertyType
operator|=
literal|"Part"
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|TO_PART
init|=
literal|"toPart"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ALTERNATE_PATHS
init|=
literal|"alternatePaths"
decl_stmt|;
comment|/** 	 *  	 * @param metadata 	 *            The metadata to attach to this property 	 * @param namespace 	 *            the namespace URI to associate to this property 	 * @param prefix 	 *            The prefix to set for this property 	 * @param propertyName 	 *            The local Name of this thumbnail type 	 */
specifier|public
name|ResourceRefType
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|XmpConstants
operator|.
name|RDF_NAMESPACE
argument_list|,
name|PREFERRED_PREFIX
argument_list|)
expr_stmt|;
name|setAttribute
argument_list|(
operator|new
name|Attribute
argument_list|(
name|XMPSchema
operator|.
name|NS_NAMESPACE
argument_list|,
literal|"xmlns"
argument_list|,
name|PREFERRED_PREFIX
argument_list|,
name|ELEMENT_NS
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getDocumentID
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|DOCUMENT_ID
argument_list|,
name|URIType
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setDocumentID
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|DOCUMENT_ID
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getFilePath
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|FILE_PATH
argument_list|,
name|URIType
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setFilePath
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|FILE_PATH
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getInstanceID
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|INSTANCE_ID
argument_list|,
name|URIType
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setInstanceID
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|INSTANCE_ID
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Calendar
name|getLastModifyDate
parameter_list|()
block|{
name|DateType
name|absProp
init|=
operator|(
name|DateType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|LAST_MODIFY_DATE
argument_list|,
name|DateType
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
name|absProp
operator|.
name|getValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setLastModifyDate
parameter_list|(
name|Calendar
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|LAST_MODIFY_DATE
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getManageUI
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|MANAGE_UI
argument_list|,
name|URIType
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setManageUI
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|MANAGE_UI
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getManageTo
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|MANAGE_TO
argument_list|,
name|URIType
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setManageTo
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|MANAGE_TO
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getManager
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|MANAGER
argument_list|,
name|AgentNameType
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setManager
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|MANAGER
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getManagerVariant
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|MANAGER_VARIANT
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setManagerVariant
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|MANAGER_VARIANT
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getPartMapping
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|PART_MAPPING
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setPartMapping
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|PART_MAPPING
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getRenditionParams
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|RENDITION_PARAMS
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setRenditionParams
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|RENDITION_PARAMS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getVersionID
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|VERSION_ID
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setVersionID
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|VERSION_ID
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getMaskMarkers
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|MASK_MARKERS
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setMaskMarkers
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|MASK_MARKERS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getRenditionClass
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|RENDITION_CLASS
argument_list|,
name|RenditionClassType
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setRenditionClass
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|RENDITION_CLASS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getFromPart
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|FROM_PART
argument_list|,
name|PartType
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setFromPart
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|FROM_PART
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getToPart
parameter_list|()
block|{
name|TextType
name|absProp
init|=
operator|(
name|TextType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|TO_PART
argument_list|,
name|PartType
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
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|setToPart
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addSimpleProperty
argument_list|(
name|TO_PART
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addAlternatePath
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|ComplexProperty
name|seq
init|=
operator|(
name|ComplexProperty
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|ALTERNATE_PATHS
argument_list|,
name|ComplexProperty
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|seq
operator|==
literal|null
condition|)
block|{
name|seq
operator|=
operator|new
name|ComplexProperty
argument_list|(
name|getMetadata
argument_list|()
argument_list|,
literal|null
argument_list|,
name|PREFERRED_PREFIX
argument_list|,
name|ALTERNATE_PATHS
argument_list|,
name|ComplexProperty
operator|.
name|ORDERED_ARRAY
argument_list|)
expr_stmt|;
name|addProperty
argument_list|(
name|seq
argument_list|)
expr_stmt|;
block|}
name|TextType
name|tt
init|=
operator|(
name|TextType
operator|)
name|TypeMapping
operator|.
name|instanciateSimpleProperty
argument_list|(
name|getMetadata
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|"rdf"
argument_list|,
literal|"li"
argument_list|,
name|value
argument_list|,
literal|"Text"
argument_list|)
decl_stmt|;
name|seq
operator|.
name|getContainer
argument_list|()
operator|.
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Get Versions property 	 *  	 * @return version property to set 	 */
specifier|public
name|ComplexProperty
name|getAlternatePathsProperty
parameter_list|()
block|{
return|return
operator|(
name|ComplexProperty
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|ALTERNATE_PATHS
argument_list|,
name|ComplexProperty
operator|.
name|class
argument_list|)
return|;
block|}
comment|/** 	 * Get List of Versions values 	 *  	 * @return List of Versions values 	 */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAlternatePaths
parameter_list|()
block|{
name|ComplexProperty
name|seq
init|=
operator|(
name|ComplexProperty
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|ALTERNATE_PATHS
argument_list|,
name|ComplexProperty
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|seq
operator|!=
literal|null
condition|)
block|{
return|return
name|TypeUtil
operator|.
name|getArrayListToString
argument_list|(
name|seq
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getFieldsNamespace
parameter_list|()
block|{
return|return
name|ELEMENT_NS
return|;
block|}
block|}
end_class

end_unit

