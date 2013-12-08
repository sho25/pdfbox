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
name|*
import|;
end_import

begin_comment
comment|/**  * Representation of XMPMediaManagement Schema  *   * @author gbailleul  *   */
end_comment

begin_class
annotation|@
name|StructuredType
argument_list|(
name|preferedPrefix
operator|=
literal|"xmpMM"
argument_list|,
name|namespace
operator|=
literal|"http://ns.adobe.com/xap/1.0/mm/"
argument_list|)
specifier|public
class|class
name|XMPMediaManagementSchema
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
name|URL
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
name|LAST_URL
init|=
literal|"LastURL"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|ResourceRef
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
name|RENDITION_OF
init|=
literal|"RenditionOf"
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
name|SAVE_ID
init|=
literal|"SaveID"
decl_stmt|;
comment|/**      * Constructor of XMPMediaManagement Schema with preferred prefix      *       * @param metadata      *            The metadata to attach this schema      */
specifier|public
name|XMPMediaManagementSchema
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
comment|/**      * Constructor of XMPMediaManagementSchema schema with specified prefix      *       * @param metadata      *            The metadata to attach this schema      * @param ownPrefix      *            The prefix to assign      */
specifier|public
name|XMPMediaManagementSchema
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
comment|// -------------------------------- ResourceRef --------------------
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|ResourceRef
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
name|DERIVED_FROM
init|=
literal|"DerivedFrom"
decl_stmt|;
comment|/**      * Set ResourceRef property      *       * @param tt      *            ResourceRef property to set      */
specifier|public
name|void
name|setDerivedFromProperty
parameter_list|(
name|ResourceRefType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get ResourceRef property      *       * @return ResourceRef property      */
specifier|public
name|ResourceRefType
name|getResourceRefProperty
parameter_list|()
block|{
return|return
operator|(
name|ResourceRefType
operator|)
name|getProperty
argument_list|(
name|DERIVED_FROM
argument_list|)
return|;
block|}
comment|// --------------------------------------- DocumentID
comment|// ----------------------------
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|URI
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
name|DOCUMENTID
init|=
literal|"DocumentID"
decl_stmt|;
comment|/**      * Set DocumentId value      *       * @param url      *            DocumentId value to set      */
specifier|public
name|void
name|setDocumentID
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|URIType
name|tt
init|=
operator|(
name|URIType
operator|)
name|instanciateSimple
argument_list|(
name|DOCUMENTID
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|setDocumentIDProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set DocumentId Property      *       * @param tt      *            DocumentId Property to set      */
specifier|public
name|void
name|setDocumentIDProperty
parameter_list|(
name|URIType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get DocumentId property      *       * @return DocumentId property      */
specifier|public
name|TextType
name|getDocumentIDProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|DOCUMENTID
argument_list|)
return|;
block|}
comment|/**      * Get DocumentId value      *       * @return DocumentId value      */
specifier|public
name|String
name|getDocumentID
parameter_list|()
block|{
name|TextType
name|tt
init|=
name|getDocumentIDProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Set DocumentId value      *      * @param url      *            DocumentId value to set      */
specifier|public
name|void
name|setLastURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|URLType
name|tt
init|=
operator|(
name|URLType
operator|)
name|instanciateSimple
argument_list|(
name|LAST_URL
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|setLastURLProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set DocumentId Property      *      * @param tt      *            DocumentId Property to set      */
specifier|public
name|void
name|setLastURLProperty
parameter_list|(
name|URLType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get DocumentId property      *      * @return DocumentId property      */
specifier|public
name|URLType
name|getLastURLProperty
parameter_list|()
block|{
return|return
operator|(
name|URLType
operator|)
name|getProperty
argument_list|(
name|LAST_URL
argument_list|)
return|;
block|}
comment|/**      * Get DocumentId value      *      * @return DocumentId value      */
specifier|public
name|String
name|getLastURL
parameter_list|()
block|{
name|URLType
name|tt
init|=
name|getLastURLProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Set DocumentId value      *      * @param url      *            DocumentId value to set      */
specifier|public
name|void
name|setSaveId
parameter_list|(
name|Integer
name|url
parameter_list|)
block|{
name|IntegerType
name|tt
init|=
operator|(
name|IntegerType
operator|)
name|instanciateSimple
argument_list|(
name|SAVE_ID
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|setSaveIDProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set DocumentId Property      *      * @param tt      *            DocumentId Property to set      */
specifier|public
name|void
name|setSaveIDProperty
parameter_list|(
name|IntegerType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get DocumentId property      *      * @return DocumentId property      */
specifier|public
name|IntegerType
name|getSaveIDProperty
parameter_list|()
block|{
return|return
operator|(
name|IntegerType
operator|)
name|getProperty
argument_list|(
name|SAVE_ID
argument_list|)
return|;
block|}
comment|/**      * Get DocumentId value      *      * @return DocumentId value      */
specifier|public
name|Integer
name|getSaveID
parameter_list|()
block|{
name|IntegerType
name|tt
init|=
name|getSaveIDProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|// --------------------------------------- Manager
comment|// ----------------------------
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|AgentName
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
name|MANAGER
init|=
literal|"Manager"
decl_stmt|;
comment|/**      * Set Manager value      *       * @param value      *            Manager value to set      */
specifier|public
name|void
name|setManager
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|AgentNameType
name|tt
init|=
operator|(
name|AgentNameType
operator|)
name|instanciateSimple
argument_list|(
name|MANAGER
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|setManagerProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set Manager property      *       * @param tt      *            Manager property to set      */
specifier|public
name|void
name|setManagerProperty
parameter_list|(
name|AgentNameType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get Manager property      *       * @return Manager property      */
specifier|public
name|TextType
name|getManagerProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|MANAGER
argument_list|)
return|;
block|}
comment|/**      * Get Manager value      *       * @return Manager value      */
specifier|public
name|String
name|getManager
parameter_list|()
block|{
name|TextType
name|tt
init|=
name|getManagerProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|// --------------------------------------- ManageTo
comment|// ----------------------------
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|URI
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
name|MANAGETO
init|=
literal|"ManageTo"
decl_stmt|;
comment|/**      * Set ManageTo Value      *       * @param value      *            ManageTo Value to set      */
specifier|public
name|void
name|setManageTo
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|URIType
name|tt
init|=
operator|(
name|URIType
operator|)
name|instanciateSimple
argument_list|(
name|MANAGETO
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|setManageToProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set ManageTo property      *       * @param tt      *            ManageTo property to set      */
specifier|public
name|void
name|setManageToProperty
parameter_list|(
name|URIType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * get ManageTo property      *       * @return ManageTo property      */
specifier|public
name|TextType
name|getManageToProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|MANAGETO
argument_list|)
return|;
block|}
comment|/**      * get ManageTo value      *       * @return ManageTo value      */
specifier|public
name|String
name|getManageTo
parameter_list|()
block|{
name|TextType
name|tt
init|=
name|getManageToProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|// --------------------------------------- ManageUI
comment|// ----------------------------
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|URI
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
name|MANAGEUI
init|=
literal|"ManageUI"
decl_stmt|;
comment|/**      * Set ManageUI value      *       * @param value      *            ManageUI value to set      */
specifier|public
name|void
name|setManageUI
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|URIType
name|tt
init|=
operator|(
name|URIType
operator|)
name|instanciateSimple
argument_list|(
name|MANAGEUI
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|setManageUIProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set ManageUI property      *       * @param tt      *            ManageUI property to set      */
specifier|public
name|void
name|setManageUIProperty
parameter_list|(
name|URIType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get ManageUI property      *       * @return ManageUI property      */
specifier|public
name|TextType
name|getManageUIProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|MANAGEUI
argument_list|)
return|;
block|}
comment|/**      * Get ManageUI Value      *       * @return ManageUI Value      */
specifier|public
name|String
name|getManageUI
parameter_list|()
block|{
name|TextType
name|tt
init|=
name|getManageUIProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|// --------------------------------------- ManagerVariant
comment|// ----------------------------
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
name|MANAGERVARIANT
init|=
literal|"ManagerVariant"
decl_stmt|;
comment|/**      * Set ManagerVariant value      *       * @param value      *            ManagerVariant value to set      */
specifier|public
name|void
name|setManagerVariant
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|TextType
name|tt
init|=
operator|(
name|TextType
operator|)
name|instanciateSimple
argument_list|(
name|MANAGERVARIANT
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|setManagerVariantProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set ManagerVariant Property      *       * @param tt      *            ManagerVariant Property to set      */
specifier|public
name|void
name|setManagerVariantProperty
parameter_list|(
name|TextType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get ManagerVariant property      *       * @return ManagerVariant property      */
specifier|public
name|TextType
name|getManagerVariantProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|MANAGERVARIANT
argument_list|)
return|;
block|}
comment|/**      * Get ManagerVariant value      *       * @return ManagerVariant value      */
specifier|public
name|String
name|getManagerVariant
parameter_list|()
block|{
name|TextType
name|tt
init|=
name|getManagerVariantProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|// --------------------------------------- InstanceID
comment|// ----------------------------
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|URI
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
name|INSTANCEID
init|=
literal|"InstanceID"
decl_stmt|;
comment|/**      * Set InstanceId value      *       * @param value      *            InstanceId value to set      */
specifier|public
name|void
name|setInstanceID
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|URIType
name|tt
init|=
operator|(
name|URIType
operator|)
name|instanciateSimple
argument_list|(
name|INSTANCEID
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|setInstanceIDProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set InstanceId property      *       * @param tt      *            InstanceId property to set      */
specifier|public
name|void
name|setInstanceIDProperty
parameter_list|(
name|URIType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get InstanceId property      *       * @return InstanceId property      */
specifier|public
name|TextType
name|getInstanceIDProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|INSTANCEID
argument_list|)
return|;
block|}
comment|/**      * Get InstanceId value      *       * @return InstanceId value      */
specifier|public
name|String
name|getInstanceID
parameter_list|()
block|{
name|TextType
name|tt
init|=
name|getInstanceIDProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|// --------------------------------------- ManageFrom
comment|// ----------------------------
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|ResourceRef
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
name|MANAGED_FROM
init|=
literal|"ManagedFrom"
decl_stmt|;
comment|// /**
comment|// * set ManageFrom Value
comment|// *
comment|// * @param url
comment|// * ManageFrom Value to set
comment|// */
comment|// public void setManagedFrom(ResourceRefType resourceRef) {
comment|//
comment|// setManagedFromProperty(new TextType(metadata, localPrefix, MANAGED_FROM,
comment|// url));
comment|// }
comment|/**      * set ManageFrom Property      *       * @param resourceRef      *            ManageFrom Property to set      */
specifier|public
name|void
name|setManagedFromProperty
parameter_list|(
name|ResourceRefType
name|resourceRef
parameter_list|)
block|{
name|addProperty
argument_list|(
name|resourceRef
argument_list|)
expr_stmt|;
block|}
comment|/**      * get ManageFrom Property      *       * @return ManageFrom Property      */
specifier|public
name|ResourceRefType
name|getManagedFromProperty
parameter_list|()
block|{
return|return
operator|(
name|ResourceRefType
operator|)
name|getProperty
argument_list|(
name|MANAGED_FROM
argument_list|)
return|;
block|}
comment|// /**
comment|// * Get ManageFrom value
comment|// *
comment|// * @return ManageFrom value
comment|// */
comment|// public String getManagedFrom() {
comment|// TextType tt = getManagedFromProperty();
comment|// return tt != null ? tt.getStringValue() : null;
comment|// }
comment|// --------------------------------------- OriginalDocumentID
comment|// ----------------------------
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
name|ORIGINALDOCUMENTID
init|=
literal|"OriginalDocumentID"
decl_stmt|;
comment|/**      * Set OriginalDocumentId value      *       * @param url      *            OriginalDocumentId value to set      */
specifier|public
name|void
name|setOriginalDocumentID
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|TextType
name|tt
init|=
operator|(
name|TextType
operator|)
name|instanciateSimple
argument_list|(
name|ORIGINALDOCUMENTID
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|setOriginalDocumentIDProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set OriginalDocumentId property      *       * @param tt      *            OriginalDocumentId property to set      */
specifier|public
name|void
name|setOriginalDocumentIDProperty
parameter_list|(
name|TextType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get OriginalDocumentId property      *       * @return OriginalDocumentId property      */
specifier|public
name|TextType
name|getOriginalDocumentIDProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|ORIGINALDOCUMENTID
argument_list|)
return|;
block|}
comment|/**      * Get OriginalDocumentId value      *       * @return OriginalDocumentId value      */
specifier|public
name|String
name|getOriginalDocumentID
parameter_list|()
block|{
name|TextType
name|tt
init|=
name|getOriginalDocumentIDProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|// --------------------------------------- RenditionClass
comment|// ----------------------------
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|RenditionClass
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
name|RENDITIONCLASS
init|=
literal|"RenditionClass"
decl_stmt|;
comment|/**      * Set renditionClass Value      *       * @param value      *            renditionClass Value to set      */
specifier|public
name|void
name|setRenditionClass
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|RenditionClassType
name|tt
init|=
operator|(
name|RenditionClassType
operator|)
name|instanciateSimple
argument_list|(
name|RENDITIONCLASS
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|setRenditionClassProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set RenditionClass Property      *       * @param tt      *            renditionClass Property to set      */
specifier|public
name|void
name|setRenditionClassProperty
parameter_list|(
name|RenditionClassType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get RenditionClass property      *       * @return RenditionClass property      */
specifier|public
name|TextType
name|getRenditionClassProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|RENDITIONCLASS
argument_list|)
return|;
block|}
comment|/**      * Get RenditionClass value      *       * @return RenditionClass value      */
specifier|public
name|String
name|getRenditionClass
parameter_list|()
block|{
name|TextType
name|tt
init|=
name|getRenditionClassProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|// --------------------------------------- RenditionParams
comment|// ----------------------------
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
name|RENDITIONPARAMS
init|=
literal|"RenditionParams"
decl_stmt|;
comment|/**      * Set RenditionParams Value      *       * @param url      *            RenditionParams Value to set      */
specifier|public
name|void
name|setRenditionParams
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|TextType
name|tt
init|=
operator|(
name|TextType
operator|)
name|instanciateSimple
argument_list|(
name|RENDITIONPARAMS
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|setRenditionParamsProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set RenditionParams property      *       * @param tt      *            RenditionParams property to set      */
specifier|public
name|void
name|setRenditionParamsProperty
parameter_list|(
name|TextType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get RenditionParams property      *       * @return RenditionParams property      */
specifier|public
name|TextType
name|getRenditionParamsProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|RENDITIONPARAMS
argument_list|)
return|;
block|}
comment|/**      * Get RenditionParams value      *       * @return RenditionParams value      */
specifier|public
name|String
name|getRenditionParams
parameter_list|()
block|{
name|TextType
name|tt
init|=
name|getRenditionParamsProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|// --------------------------------------- VersionID
comment|// ----------------------------
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
name|VERSIONID
init|=
literal|"VersionID"
decl_stmt|;
comment|/**      * Set VersionId value      *       * @param value      *            VersionId value to set      */
specifier|public
name|void
name|setVersionID
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|TextType
name|tt
init|=
operator|(
name|TextType
operator|)
name|instanciateSimple
argument_list|(
name|VERSIONID
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|setVersionIDProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set VersionId property      *       * @param tt      *            VersionId property to set      */
specifier|public
name|void
name|setVersionIDProperty
parameter_list|(
name|TextType
name|tt
parameter_list|)
block|{
name|addProperty
argument_list|(
name|tt
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get VersionId property      *       * @return VersionId property      */
specifier|public
name|TextType
name|getVersionIDProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|VERSIONID
argument_list|)
return|;
block|}
comment|/**      * Get VersionId value      *       * @return VersionId value      */
specifier|public
name|String
name|getVersionID
parameter_list|()
block|{
name|TextType
name|tt
init|=
name|getVersionIDProperty
argument_list|()
decl_stmt|;
return|return
name|tt
operator|!=
literal|null
condition|?
name|tt
operator|.
name|getStringValue
argument_list|()
else|:
literal|null
return|;
block|}
comment|// --------------------------------------- Versions
comment|// ----------------------------
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|Version
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Seq
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|VERSIONS
init|=
literal|"Versions"
decl_stmt|;
specifier|public
name|void
name|addVersions
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addQualifiedBagValue
argument_list|(
name|VERSIONS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get Versions property      *       * @return version property to set      */
specifier|public
name|ArrayProperty
name|getVersionsProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|VERSIONS
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getVersions
parameter_list|()
block|{
return|return
name|getUnqualifiedBagValueList
argument_list|(
name|VERSIONS
argument_list|)
return|;
block|}
comment|// --------------------------------------- History
comment|// ----------------------------
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|ResourceEvent
argument_list|,
name|card
operator|=
name|Cardinality
operator|.
name|Seq
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|HISTORY
init|=
literal|"History"
decl_stmt|;
comment|/**      * Add a History Value      *       * @param history      *            History Value to add      */
specifier|public
name|void
name|addHistory
parameter_list|(
name|String
name|history
parameter_list|)
block|{
name|addUnqualifiedSequenceValue
argument_list|(
name|HISTORY
argument_list|,
name|history
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get History Property      *       * @return History Property      */
specifier|public
name|ArrayProperty
name|getHistoryProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|HISTORY
argument_list|)
return|;
block|}
comment|/**      * Get List of History values      *       * @return List of History values      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getHistory
parameter_list|()
block|{
return|return
name|getUnqualifiedSequenceValueList
argument_list|(
name|HISTORY
argument_list|)
return|;
block|}
comment|// --------------------------------------- Ingredients
comment|// ----------------------------
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
name|Bag
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|INGREDIENTS
init|=
literal|"Ingredients"
decl_stmt|;
comment|/**      * Add an Ingredients value      *       * @param ingredients      *            Ingredients value to add      */
specifier|public
name|void
name|addIngredients
parameter_list|(
name|String
name|ingredients
parameter_list|)
block|{
name|addQualifiedBagValue
argument_list|(
name|INGREDIENTS
argument_list|,
name|ingredients
argument_list|)
expr_stmt|;
block|}
comment|/**      * . Get Ingredients Property      *       * @return Ingredients property      */
specifier|public
name|ArrayProperty
name|getIngredientsProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|INGREDIENTS
argument_list|)
return|;
block|}
comment|/**      * Get List of Ingredients values      *       * @return List of Ingredients values      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIngredients
parameter_list|()
block|{
return|return
name|getUnqualifiedBagValueList
argument_list|(
name|INGREDIENTS
argument_list|)
return|;
block|}
block|}
end_class

end_unit

