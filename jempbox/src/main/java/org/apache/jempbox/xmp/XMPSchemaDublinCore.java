begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
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
name|io
operator|.
name|IOException
import|;
end_import

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
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * Define XMP properties used with the Dublin Core specification.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|XMPSchemaDublinCore
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
literal|"http://purl.org/dc/elements/1.1/"
decl_stmt|;
comment|/**      * Construct a new blank Dublin Core schema.      *      * @param parent The parent metadata schema that this will be part of.      */
specifier|public
name|XMPSchemaDublinCore
parameter_list|(
name|XMPMetadata
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"dc"
argument_list|,
name|NAMESPACE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor from existing XML element.      *       * @param element The existing element.      * @param prefix The schema prefix.      */
specifier|public
name|XMPSchemaDublinCore
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
comment|/**      * Remove a contributor from the list of contributors.      *      * @param contributor The contributor to remove.      */
specifier|public
name|void
name|removeContributor
parameter_list|(
name|String
name|contributor
parameter_list|)
block|{
name|removeBagValue
argument_list|(
name|prefix
operator|+
literal|":contributor"
argument_list|,
name|contributor
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a contributor to the list of contributors.  A contributor is someone other than an author.      *      * @param contributor The name of the contributor.      */
specifier|public
name|void
name|addContributor
parameter_list|(
name|String
name|contributor
parameter_list|)
block|{
name|addBagValue
argument_list|(
name|prefix
operator|+
literal|":contributor"
argument_list|,
name|contributor
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the complete list of contributors.      *      * @return The list of contributors.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getContributors
parameter_list|()
block|{
return|return
name|getBagList
argument_list|(
name|prefix
operator|+
literal|":contributor"
argument_list|)
return|;
block|}
comment|/**      * Set the coverage property.      *      * @param coverage The extend or scope of the resource.      */
specifier|public
name|void
name|setCoverage
parameter_list|(
name|String
name|coverage
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":coverage"
argument_list|,
name|coverage
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the coverage property.      *      * @return The extent or scope of the resource.      */
specifier|public
name|String
name|getCoverage
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":coverage"
argument_list|)
return|;
block|}
comment|/**      * Remove a creator from the list of creators.      *      * @param creator The author of the resource.      */
specifier|public
name|void
name|removeCreator
parameter_list|(
name|String
name|creator
parameter_list|)
block|{
name|removeSequenceValue
argument_list|(
name|prefix
operator|+
literal|":creator"
argument_list|,
name|creator
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a creator.      *      * @param creator The author of the resource.      */
specifier|public
name|void
name|addCreator
parameter_list|(
name|String
name|creator
parameter_list|)
block|{
name|addSequenceValue
argument_list|(
name|prefix
operator|+
literal|":creator"
argument_list|,
name|creator
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get a complete list of creators.      *      * @return A list of java.lang.String objects.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getCreators
parameter_list|()
block|{
return|return
name|getSequenceList
argument_list|(
name|prefix
operator|+
literal|":creator"
argument_list|)
return|;
block|}
comment|/**      * Remove a date from the list of 'interesting' dates.      *      * @param date The date to remove.      */
specifier|public
name|void
name|removeDate
parameter_list|(
name|Calendar
name|date
parameter_list|)
block|{
name|removeSequenceDateValue
argument_list|(
name|prefix
operator|+
literal|":date"
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a date of interest to this schema.      *      * @param date The date to add to the schema.      */
specifier|public
name|void
name|addDate
parameter_list|(
name|Calendar
name|date
parameter_list|)
block|{
name|addSequenceDateValue
argument_list|(
name|prefix
operator|+
literal|":date"
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get a list of all dates of interest to this resource.      *      * @return A list of java.util.Calendar objects.      *      * @throws IOException If there is an error creating the date object.      */
specifier|public
name|List
argument_list|<
name|Calendar
argument_list|>
name|getDates
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getSequenceDateList
argument_list|(
name|prefix
operator|+
literal|":date"
argument_list|)
return|;
block|}
comment|/**      * Set the default value for the description.      *      * @param description The description of this resource.      */
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|setLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":description"
argument_list|,
literal|null
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default value for the description.      *      * @return The description of this resource.      */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|getLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":description"
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Set the description of this resource in a specific language.      *      * @param language The language code.      * @param description The description in a specific language.      */
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|language
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|setLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":description"
argument_list|,
name|language
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the description in a specific language.      *      * @param language The language code to get the description for.      *      * @return The description in the specified language or null if it does not exist.      */
specifier|public
name|String
name|getDescription
parameter_list|(
name|String
name|language
parameter_list|)
block|{
return|return
name|getLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":description"
argument_list|,
name|language
argument_list|)
return|;
block|}
comment|/**      * Get a list of all languages that a description exists for.      *      * @return A non-null list of languages, potentially an empty list.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getDescriptionLanguages
parameter_list|()
block|{
return|return
name|getLanguagePropertyLanguages
argument_list|(
name|prefix
operator|+
literal|":description"
argument_list|)
return|;
block|}
comment|/**      * Set the format property.      *      * @param format The mime-type of the saved resource.      */
specifier|public
name|void
name|setFormat
parameter_list|(
name|String
name|format
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":format"
argument_list|,
name|format
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the format property.      *      * @return The mime-type of the resource.      */
specifier|public
name|String
name|getFormat
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":format"
argument_list|)
return|;
block|}
comment|/**      * Set the resource identifier.      *      * @param id An id to the resource.      */
specifier|public
name|void
name|setIdentifier
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":identifier"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the resource id.      *      * @return A key that identifies this resource.      */
specifier|public
name|String
name|getIdentifier
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":identifier"
argument_list|)
return|;
block|}
comment|/**      * Remove a language from the list of languages.      *      * @param language The language to remove.      */
specifier|public
name|void
name|removeLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|removeBagValue
argument_list|(
name|prefix
operator|+
literal|":language"
argument_list|,
name|language
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a language to the list of languages.        *      * @param language The name of the language.      */
specifier|public
name|void
name|addLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|addBagValue
argument_list|(
name|prefix
operator|+
literal|":language"
argument_list|,
name|language
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the complete list of languages.      *      * @return The list of languages.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getLanguages
parameter_list|()
block|{
return|return
name|getBagList
argument_list|(
name|prefix
operator|+
literal|":language"
argument_list|)
return|;
block|}
comment|/**      * Remove a publisher from the list of publishers.      *      * @param publisher The publisher to remove.      */
specifier|public
name|void
name|removePublisher
parameter_list|(
name|String
name|publisher
parameter_list|)
block|{
name|removeBagValue
argument_list|(
name|prefix
operator|+
literal|":publisher"
argument_list|,
name|publisher
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a publisher to the list of publishers.        *      * @param publisher The name of the publisher.      */
specifier|public
name|void
name|addPublisher
parameter_list|(
name|String
name|publisher
parameter_list|)
block|{
name|addBagValue
argument_list|(
name|prefix
operator|+
literal|":publisher"
argument_list|,
name|publisher
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the complete list of publishers.      *      * @return The list of publishers.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPublishers
parameter_list|()
block|{
return|return
name|getBagList
argument_list|(
name|prefix
operator|+
literal|":publisher"
argument_list|)
return|;
block|}
comment|/**      * Remove a relation from the list of relationships.        * A relationship to another resource.      *      * @param relation The publisher to remove.      */
specifier|public
name|void
name|removeRelation
parameter_list|(
name|String
name|relation
parameter_list|)
block|{
name|removeBagValue
argument_list|(
name|prefix
operator|+
literal|":relation"
argument_list|,
name|relation
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a relation to the list of relationships.      * A relationship to another resource.        *      * @param relation The relation to the other resource.      */
specifier|public
name|void
name|addRelation
parameter_list|(
name|String
name|relation
parameter_list|)
block|{
name|addBagValue
argument_list|(
name|prefix
operator|+
literal|":relation"
argument_list|,
name|relation
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the complete list of relationships.      *      * @return The list of relationships.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRelationships
parameter_list|()
block|{
return|return
name|getBagList
argument_list|(
name|prefix
operator|+
literal|":relation"
argument_list|)
return|;
block|}
comment|/**      * Set the default value for the rights of this document.  This property      * specifies informal rights of the document.      *      * @param rights The rights for this resource.      */
specifier|public
name|void
name|setRights
parameter_list|(
name|String
name|rights
parameter_list|)
block|{
name|setLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":rights"
argument_list|,
literal|null
argument_list|,
name|rights
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default value for the rights of this document.      *      * @return The informal rights for this resource.      */
specifier|public
name|String
name|getRights
parameter_list|()
block|{
return|return
name|getLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":rights"
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Set the rights for this resource in a specific language.      *      * @param language The language code.      * @param rights The rights in a specific language.      */
specifier|public
name|void
name|setRights
parameter_list|(
name|String
name|language
parameter_list|,
name|String
name|rights
parameter_list|)
block|{
name|setLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":rights"
argument_list|,
name|language
argument_list|,
name|rights
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the rights in a specific language.      *      * @param language The language code to get the description for.      *      * @return The rights in the specified language or null if it does not exist.      */
specifier|public
name|String
name|getRights
parameter_list|(
name|String
name|language
parameter_list|)
block|{
return|return
name|getLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":rights"
argument_list|,
name|language
argument_list|)
return|;
block|}
comment|/**      * Get a list of all languages that a rights description exists for.      *      * @return A non-null list of languages, potentially an empty list.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRightsLanguages
parameter_list|()
block|{
return|return
name|getLanguagePropertyLanguages
argument_list|(
name|prefix
operator|+
literal|":rights"
argument_list|)
return|;
block|}
comment|/**      * Set the resource source identifier.      *      * @param id An id to the resource source.      */
specifier|public
name|void
name|setSource
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":source"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the resource source id.      *      * @return A key that identifies this source of this resource.      */
specifier|public
name|String
name|getSource
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":source"
argument_list|)
return|;
block|}
comment|/**      * Remove a subject from the list of subjects.      *      * @param subject The subject to remove.      */
specifier|public
name|void
name|removeSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|removeBagValue
argument_list|(
name|prefix
operator|+
literal|":subject"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add a subject to the list of subjects.      *      * @param subject The subject of this resource.      */
specifier|public
name|void
name|addSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|addBagValue
argument_list|(
name|prefix
operator|+
literal|":subject"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the complete list of subjects.      *      * @return The list of subjects.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSubjects
parameter_list|()
block|{
return|return
name|getBagList
argument_list|(
name|prefix
operator|+
literal|":subject"
argument_list|)
return|;
block|}
comment|/**      * Set the default value for the title.      *      * @param title The title of this resource.      */
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|setLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":title"
argument_list|,
literal|null
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default value for the title.      *      * @return The title of this resource.      */
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|getLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":title"
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Set the title of this resource in a specific language.      *      * @param language The language code.      * @param title The title in a specific language.      */
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|language
parameter_list|,
name|String
name|title
parameter_list|)
block|{
name|setLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":title"
argument_list|,
name|language
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the title in a specific language.      *      * @param language The language code to get the description for.      *      * @return The title in the specified language or null if it does not exist.      */
specifier|public
name|String
name|getTitle
parameter_list|(
name|String
name|language
parameter_list|)
block|{
return|return
name|getLanguageProperty
argument_list|(
name|prefix
operator|+
literal|":title"
argument_list|,
name|language
argument_list|)
return|;
block|}
comment|/**      * Get a list of all languages that a title exists for.      *      * @return A non-null list of languages, potentially an empty list.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTitleLanguages
parameter_list|()
block|{
return|return
name|getLanguagePropertyLanguages
argument_list|(
name|prefix
operator|+
literal|":title"
argument_list|)
return|;
block|}
comment|/**      * Add a type to the bag of types of this resource.      *      * @param type The type of resource to add (poem, novel).      */
specifier|public
name|void
name|addType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|addBagValue
argument_list|(
name|prefix
operator|+
literal|":type"
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the list of types for this resource.      *      * @return A list of types for this resource.      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTypes
parameter_list|()
block|{
return|return
name|getBagList
argument_list|(
name|prefix
operator|+
literal|":type"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

