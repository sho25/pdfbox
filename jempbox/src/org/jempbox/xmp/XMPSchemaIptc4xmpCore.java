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
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * Define XMP properties used with IPTC specification.  *   * @author $Author: benlitchfield $  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|XMPSchemaIptc4xmpCore
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
literal|"http://iptc.org/std/Iptc4xmpCore/1.0/xmlns/"
decl_stmt|;
comment|/**      * Construct a new blank IPTC schema.      *       * @param metadata The parent metadata schema that this will be part of.      */
specifier|public
name|XMPSchemaIptc4xmpCore
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
literal|"Iptc4xmpCore"
argument_list|,
name|NAMESPACE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor from an existing element.      *       * @param element The XML element.      * @param aPrefix The XML prefix; Iptc4xmpCore.      */
specifier|public
name|XMPSchemaIptc4xmpCore
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|aPrefix
parameter_list|)
block|{
name|super
argument_list|(
name|element
argument_list|,
name|aPrefix
argument_list|)
expr_stmt|;
block|}
comment|/**      * Contact Info Address City.      *       * @param city The city name.      */
specifier|public
name|void
name|setCiAdrCity
parameter_list|(
name|String
name|city
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiAdrCity"
argument_list|,
name|city
argument_list|)
expr_stmt|;
block|}
comment|/**      * Contact Info Address City.      *       * @return The city.      */
specifier|public
name|String
name|getCiAdrCity
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiAdrCity"
argument_list|)
return|;
block|}
comment|/**      * Contact Info country.      *       * @param country The CI country.      */
specifier|public
name|void
name|setCiAdrCtry
parameter_list|(
name|String
name|country
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiAdrCtry"
argument_list|,
name|country
argument_list|)
expr_stmt|;
block|}
comment|/**      * Contact Info country.      *       * @return The CI country.      */
specifier|public
name|String
name|getCiAdrCtry
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiAdrCtry"
argument_list|)
return|;
block|}
comment|/**      * Contact Info Extended Address(company name).      *       * @param adr Address info.      */
specifier|public
name|void
name|setCiAdrExtadr
parameter_list|(
name|String
name|adr
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiAdrExtadr"
argument_list|,
name|adr
argument_list|)
expr_stmt|;
block|}
comment|/**      * Contact Info Extended Address(company name).      *       * @return The extended address info.      */
specifier|public
name|String
name|getCiAdrExtadr
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiAdrExtadr"
argument_list|)
return|;
block|}
comment|/**      * Postal code.      *       * @param po The postal code.      */
specifier|public
name|void
name|setCiAdrPcode
parameter_list|(
name|String
name|po
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiAdrPcode"
argument_list|,
name|po
argument_list|)
expr_stmt|;
block|}
comment|/**      * Postal code.      *       * @return The postal code.      */
specifier|public
name|String
name|getCiAdrPcode
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiAdrPcode"
argument_list|)
return|;
block|}
comment|/**      * Postal region or state.      *       * @param state The postal region      */
specifier|public
name|void
name|setCiAdrRegion
parameter_list|(
name|String
name|state
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiAdrRegion"
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
comment|/**      * Postal region or state.      *       * @return The postal state.      */
specifier|public
name|String
name|getCiAdrRegion
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiAdrRegion"
argument_list|)
return|;
block|}
comment|/**      * Work email.      *       * @param email The work email.      */
specifier|public
name|void
name|setCiEmailWork
parameter_list|(
name|String
name|email
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiEmailWork"
argument_list|,
name|email
argument_list|)
expr_stmt|;
block|}
comment|/**      * Work email.      *       * @return The work email.      */
specifier|public
name|String
name|getCiEmailWork
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiEmailWork"
argument_list|)
return|;
block|}
comment|/**      * Work telephone.      *       * @param tel The work telephone.      */
specifier|public
name|void
name|setCiTelWork
parameter_list|(
name|String
name|tel
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiTelWork"
argument_list|,
name|tel
argument_list|)
expr_stmt|;
block|}
comment|/**      * Work Telephone.      *       * @return The work telephone.      */
specifier|public
name|String
name|getCiTelWork
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiTelWork"
argument_list|)
return|;
block|}
comment|/**      * Work URL.      *       * @param url The work URL.      */
specifier|public
name|void
name|setCiUrlWork
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiUrlWork"
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
comment|/**      * Work URL.      *       * @return work URL.      */
specifier|public
name|String
name|getCiUrlWork
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":CiUrlWork"
argument_list|)
return|;
block|}
comment|/**      * Name of location that the content is focussing on.      *       * @param loc The location.      */
specifier|public
name|void
name|setLocation
parameter_list|(
name|String
name|loc
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":Location"
argument_list|,
name|loc
argument_list|)
expr_stmt|;
block|}
comment|/**      * Name of location that the content is focussing on.      * @return The location.      */
specifier|public
name|String
name|getLocation
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":Location"
argument_list|)
return|;
block|}
comment|/**      * The IPTC scene.      *       * @param scene The IPTC scene.      */
specifier|public
name|void
name|addScene
parameter_list|(
name|String
name|scene
parameter_list|)
block|{
name|addBagValue
argument_list|(
name|prefix
operator|+
literal|":Scene"
argument_list|,
name|scene
argument_list|)
expr_stmt|;
block|}
comment|/**      * A list of all the scenes.      *       * @return The list of scenes.      */
specifier|public
name|List
name|getScenes
parameter_list|()
block|{
return|return
name|getBagList
argument_list|(
name|prefix
operator|+
literal|":Scene"
argument_list|)
return|;
block|}
comment|/**      * Add IPTC subject code.      * @param subject The IPTC subject.      */
specifier|public
name|void
name|addSubjectCode
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|addBagValue
argument_list|(
name|prefix
operator|+
literal|":SubjectCode"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get a list of all IPTC subject codes.      *       * @return All IPTC subject codes.      */
specifier|public
name|List
name|getSubjectCodes
parameter_list|()
block|{
return|return
name|getBagList
argument_list|(
name|prefix
operator|+
literal|":SubjectCode"
argument_list|)
return|;
block|}
comment|/**      * Nature of a news object.      *       * @param genre The news genre.      */
specifier|public
name|void
name|setIntellectualGenre
parameter_list|(
name|String
name|genre
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":IntellectualGenre"
argument_list|,
name|genre
argument_list|)
expr_stmt|;
block|}
comment|/**      * Nature of a news object.      *       * @return The news genre.      */
specifier|public
name|String
name|getIntellectualGenre
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":IntellectualGenre"
argument_list|)
return|;
block|}
comment|/**      * ISO Country Code.      *       * @param code The country code.      */
specifier|public
name|void
name|setCountryCode
parameter_list|(
name|String
name|code
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":CountryCode"
argument_list|,
name|code
argument_list|)
expr_stmt|;
block|}
comment|/**      * ISO Country Code.      *       * @return The country code.      */
specifier|public
name|String
name|getCountryCode
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":CountryCode"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

