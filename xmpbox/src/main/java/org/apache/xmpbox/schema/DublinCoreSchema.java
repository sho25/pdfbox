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
name|MIMEType
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
name|TextType
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
comment|/**  * Representation of a DublinCore Schema  *   * @author a183132  *   */
end_comment

begin_class
annotation|@
name|StructuredType
argument_list|(
name|preferedPrefix
operator|=
literal|"dc"
argument_list|,
name|namespace
operator|=
literal|"http://purl.org/dc/elements/1.1/"
argument_list|)
specifier|public
class|class
name|DublinCoreSchema
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
name|Bag
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|CONTRIBUTOR
init|=
literal|"contributor"
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
name|COVERAGE
init|=
literal|"coverage"
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
name|Seq
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|CREATOR
init|=
literal|"creator"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|Date
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
name|DATE
init|=
literal|"date"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|LangAlt
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
name|DESCRIPTION
init|=
literal|"description"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|MIMEType
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
name|IDENTIFIER
init|=
literal|"identifier"
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
name|Bag
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|LANGUAGE
init|=
literal|"language"
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
name|Bag
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|PUBLISHER
init|=
literal|"publisher"
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
name|Bag
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|RELATION
init|=
literal|"relation"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|LangAlt
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
name|RIGHTS
init|=
literal|"rights"
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
name|SOURCE
init|=
literal|"source"
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
name|Bag
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|SUBJECT
init|=
literal|"subject"
decl_stmt|;
annotation|@
name|PropertyType
argument_list|(
name|type
operator|=
name|Types
operator|.
name|LangAlt
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
name|TITLE
init|=
literal|"title"
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
name|Bag
argument_list|)
specifier|public
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"type"
decl_stmt|;
comment|/**      * Constructor of a Dublin Core schema with preferred prefix      *       * @param metadata      *            The metadata to attach this schema      */
specifier|public
name|DublinCoreSchema
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
comment|/**      * Constructor of a Dublin Core schema with specified prefix      *       * @param metadata      *            The metadata to attach this schema      * @param ownPrefix      *            The prefix to assign      */
specifier|public
name|DublinCoreSchema
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
comment|/**      * set contributor(s) to the resource (other than the authors)      *       * @param properName      *            Value to set      */
specifier|public
name|void
name|addContributor
parameter_list|(
name|String
name|properName
parameter_list|)
block|{
name|addQualifiedBagValue
argument_list|(
name|CONTRIBUTOR
argument_list|,
name|properName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeContributor
parameter_list|(
name|String
name|properName
parameter_list|)
block|{
name|removeUnqualifiedBagValue
argument_list|(
name|CONTRIBUTOR
argument_list|,
name|properName
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the extent or scope of the resource      *       * @param text      *            Value to set      */
specifier|public
name|void
name|setCoverage
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|addProperty
argument_list|(
name|createTextType
argument_list|(
name|COVERAGE
argument_list|,
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the extent or scope of the resource      *       * @param text      *            Property to set      */
specifier|public
name|void
name|setCoverageProperty
parameter_list|(
name|TextType
name|text
parameter_list|)
block|{
name|addProperty
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the autor(s) of the resource      *      * @param properName Value to add      */
specifier|public
name|void
name|addCreator
parameter_list|(
name|String
name|properName
parameter_list|)
block|{
name|addUnqualifiedSequenceValue
argument_list|(
name|CREATOR
argument_list|,
name|properName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeCreator
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|removeUnqualifiedSequenceValue
argument_list|(
name|CREATOR
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set date(s) that something interesting happened to the resource      *       * @param date      *            Value to add      */
specifier|public
name|void
name|addDate
parameter_list|(
name|Calendar
name|date
parameter_list|)
block|{
name|addUnqualifiedSequenceDateValue
argument_list|(
name|DATE
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeDate
parameter_list|(
name|Calendar
name|date
parameter_list|)
block|{
name|removeUnqualifiedSequenceDateValue
argument_list|(
name|DATE
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
comment|/**      * add a textual description of the content of the resource (multiple values may be present for different languages)      *       * @param lang      *            language concerned      * @param value      *            Value to add      */
specifier|public
name|void
name|addDescription
parameter_list|(
name|String
name|lang
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|setUnqualifiedLanguagePropertyValue
argument_list|(
name|DESCRIPTION
argument_list|,
name|lang
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the default value for the description.      *       * @param value      *            The description of this resource.      */
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|addDescription
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the file format used when saving the resource.      *       * @param mimeType      *            Value to set      */
specifier|public
name|void
name|setFormat
parameter_list|(
name|String
name|mimeType
parameter_list|)
block|{
name|addProperty
argument_list|(
name|createTextType
argument_list|(
name|FORMAT
argument_list|,
name|mimeType
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the unique identifier of the resource      *       * @param text      *            Value to set      */
specifier|public
name|void
name|setIdentifier
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|addProperty
argument_list|(
name|createTextType
argument_list|(
name|IDENTIFIER
argument_list|,
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the unique identifier of the resource      *       * @param text      *            Property to set      */
specifier|public
name|void
name|setIdentifierProperty
parameter_list|(
name|TextType
name|text
parameter_list|)
block|{
name|addProperty
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add language(s) used in this resource      *       * @param locale      *            Value to set      */
specifier|public
name|void
name|addLanguage
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
name|addQualifiedBagValue
argument_list|(
name|LANGUAGE
argument_list|,
name|locale
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeLanguage
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
name|removeUnqualifiedBagValue
argument_list|(
name|LANGUAGE
argument_list|,
name|locale
argument_list|)
expr_stmt|;
block|}
comment|/**      * add publisher(s)      *       * @param properName      *            Value to add      */
specifier|public
name|void
name|addPublisher
parameter_list|(
name|String
name|properName
parameter_list|)
block|{
name|addQualifiedBagValue
argument_list|(
name|PUBLISHER
argument_list|,
name|properName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removePublisher
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|removeUnqualifiedBagValue
argument_list|(
name|PUBLISHER
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add relationships to other documents      *       * @param text      *            Value to set      */
specifier|public
name|void
name|addRelation
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|addQualifiedBagValue
argument_list|(
name|RELATION
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeRelation
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|removeUnqualifiedBagValue
argument_list|(
name|RELATION
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
comment|/**      * add informal rights statement, by language.      *       * @param lang      *            Language concerned      * @param value      *            Value to set      */
specifier|public
name|void
name|addRights
parameter_list|(
name|String
name|lang
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|setUnqualifiedLanguagePropertyValue
argument_list|(
name|RIGHTS
argument_list|,
name|lang
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the unique identifer of the work from which this resource was derived      *       * @param text      *            Value to set      */
specifier|public
name|void
name|setSource
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|addProperty
argument_list|(
name|createTextType
argument_list|(
name|SOURCE
argument_list|,
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the unique identifer of the work from which this resource was derived      *       * @param text      *            Property to set      */
specifier|public
name|void
name|setSourceProperty
parameter_list|(
name|TextType
name|text
parameter_list|)
block|{
name|addProperty
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the unique identifer of the work from which this resource was derived      *       * @param text      *            Property to set      */
specifier|public
name|void
name|setFormatProperty
parameter_list|(
name|MIMEType
name|text
parameter_list|)
block|{
name|addProperty
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
comment|/**      * add descriptive phrases or keywords that specify the topic of the content of the resource      *       * @param text      *            Value to add      */
specifier|public
name|void
name|addSubject
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|addQualifiedBagValue
argument_list|(
name|SUBJECT
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeSubject
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|removeUnqualifiedBagValue
argument_list|(
name|SUBJECT
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the title of the document, or the name given to the resource (by language)      *       * @param lang      *            Language concerned      * @param value      *            Value to set      */
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|lang
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|setUnqualifiedLanguagePropertyValue
argument_list|(
name|TITLE
argument_list|,
name|lang
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * set default title      *       * @param value Value to set      */
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|setTitle
argument_list|(
literal|null
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the title of the document, or the name given to the resource (by language)      *       * @param lang Language concerned      * @param value Value to set      * @see DublinCoreSchema#setTitle(String)      *       */
specifier|public
name|void
name|addTitle
parameter_list|(
name|String
name|lang
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|setTitle
argument_list|(
name|lang
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * set the document type (novel, poem, ...)      *       * @param type      *            Value to set      */
specifier|public
name|void
name|addType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|addQualifiedBagValue
argument_list|(
name|TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Return the Bag of contributor(s)      *       * @return Contributor property      */
specifier|public
name|ArrayProperty
name|getContributorsProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|CONTRIBUTOR
argument_list|)
return|;
block|}
comment|/**      * Return a String list of contributor(s)      *       * @return List of contributors values      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getContributors
parameter_list|()
block|{
return|return
name|getUnqualifiedBagValueList
argument_list|(
name|CONTRIBUTOR
argument_list|)
return|;
block|}
comment|/**      * Return the Coverage TextType Property      *       * @return Coverage property      */
specifier|public
name|TextType
name|getCoverageProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|COVERAGE
argument_list|)
return|;
block|}
comment|/**      * Return the value of the coverage      *       * @return Coverage value      */
specifier|public
name|String
name|getCoverage
parameter_list|()
block|{
name|TextType
name|tt
init|=
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|COVERAGE
argument_list|)
decl_stmt|;
return|return
name|tt
operator|==
literal|null
condition|?
literal|null
else|:
name|tt
operator|.
name|getStringValue
argument_list|()
return|;
block|}
comment|/**      * Return the Sequence of contributor(s)      *       * @return Creator property      */
specifier|public
name|ArrayProperty
name|getCreatorsProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|CREATOR
argument_list|)
return|;
block|}
comment|/**      * Return the creator(s) string value      *       * @return List of creators values      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getCreators
parameter_list|()
block|{
return|return
name|getUnqualifiedSequenceValueList
argument_list|(
name|CREATOR
argument_list|)
return|;
block|}
comment|/**      * Return the sequence of date(s)      *       * @return date property      */
specifier|public
name|ArrayProperty
name|getDatesProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|DATE
argument_list|)
return|;
block|}
comment|/**      * Return a calendar list of date      *       * @return List of dates values      */
specifier|public
name|List
argument_list|<
name|Calendar
argument_list|>
name|getDates
parameter_list|()
block|{
return|return
name|getUnqualifiedSequenceDateValueList
argument_list|(
name|DATE
argument_list|)
return|;
block|}
comment|/**      * Return the Lang alt Description      *       * @return Description property      */
specifier|public
name|ArrayProperty
name|getDescriptionProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|DESCRIPTION
argument_list|)
return|;
block|}
comment|/**      * Return a list of languages defined in description property      *       * @return get List of languages defined for description property      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getDescriptionLanguages
parameter_list|()
block|{
return|return
name|getUnqualifiedLanguagePropertyLanguagesValue
argument_list|(
name|DESCRIPTION
argument_list|)
return|;
block|}
comment|/**      * Return a language value for description property      *       * @param lang      *            The language wanted      * @return Desription value for specified language      */
specifier|public
name|String
name|getDescription
parameter_list|(
name|String
name|lang
parameter_list|)
block|{
return|return
name|getUnqualifiedLanguagePropertyValue
argument_list|(
name|DESCRIPTION
argument_list|,
name|lang
argument_list|)
return|;
block|}
comment|/**      * Get the default value for the description.      *       * @return The description of this resource.      */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|getDescription
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * Return the file format property      *       * @return the format property      */
specifier|public
name|TextType
name|getFormatProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|FORMAT
argument_list|)
return|;
block|}
comment|/**      * return the file format value      *       * @return the format value      */
specifier|public
name|String
name|getFormat
parameter_list|()
block|{
name|TextType
name|tt
init|=
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|FORMAT
argument_list|)
decl_stmt|;
return|return
name|tt
operator|==
literal|null
condition|?
literal|null
else|:
name|tt
operator|.
name|getStringValue
argument_list|()
return|;
block|}
comment|/**      * Return the unique identifier property of this resource      *       * @return the identifier property      */
specifier|public
name|TextType
name|getIdentifierProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|IDENTIFIER
argument_list|)
return|;
block|}
comment|/**      * return the unique identifier value of this resource      *       * @return the unique identifier value      */
specifier|public
name|String
name|getIdentifier
parameter_list|()
block|{
name|TextType
name|tt
init|=
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|IDENTIFIER
argument_list|)
decl_stmt|;
return|return
name|tt
operator|==
literal|null
condition|?
literal|null
else|:
name|tt
operator|.
name|getStringValue
argument_list|()
return|;
block|}
comment|/**      * Return the bag DC language      *       * @return language property      */
specifier|public
name|ArrayProperty
name|getLanguagesProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|LANGUAGE
argument_list|)
return|;
block|}
comment|/**      * Return the list of values defined in the DC language      *       * @return list of languages defined for language property      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getLanguages
parameter_list|()
block|{
return|return
name|getUnqualifiedBagValueList
argument_list|(
name|LANGUAGE
argument_list|)
return|;
block|}
comment|/**      * Return the bag DC publisher      *       * @return publisher property      */
specifier|public
name|ArrayProperty
name|getPublishersProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|PUBLISHER
argument_list|)
return|;
block|}
comment|/**      * Return the list of values defined in the DC publisher      *       * @return list of values for publisher property      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getPublishers
parameter_list|()
block|{
return|return
name|getUnqualifiedBagValueList
argument_list|(
name|PUBLISHER
argument_list|)
return|;
block|}
comment|/**      * Return the bag DC relation      *       * @return relation property      */
specifier|public
name|ArrayProperty
name|getRelationsProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|RELATION
argument_list|)
return|;
block|}
comment|/**      * Return the list of values defined in the DC relation      *       * @return list of values for relation property      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRelations
parameter_list|()
block|{
return|return
name|getUnqualifiedBagValueList
argument_list|(
name|RELATION
argument_list|)
return|;
block|}
comment|/**      * Return the Lang alt Rights      *       * @return rights property      */
specifier|public
name|ArrayProperty
name|getRightsProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|RIGHTS
argument_list|)
return|;
block|}
comment|/**      * Return a list of languages defined in Right property      *       * @return list of rights languages values defined      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRightsLanguages
parameter_list|()
block|{
return|return
name|getUnqualifiedLanguagePropertyLanguagesValue
argument_list|(
name|RIGHTS
argument_list|)
return|;
block|}
comment|/**      * Return a language value for Right property      *       * @param lang      *            language concerned      * @return the rights value for specified language      */
specifier|public
name|String
name|getRights
parameter_list|(
name|String
name|lang
parameter_list|)
block|{
return|return
name|getUnqualifiedLanguagePropertyValue
argument_list|(
name|RIGHTS
argument_list|,
name|lang
argument_list|)
return|;
block|}
comment|/**      * Return the default value for Right property      *       * @see DublinCoreSchema#getRights(String)      * @return the default rights value      */
specifier|public
name|String
name|getRights
parameter_list|()
block|{
return|return
name|getRights
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * Return the source property of this resource      *       * @return source property      */
specifier|public
name|TextType
name|getSourceProperty
parameter_list|()
block|{
return|return
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|SOURCE
argument_list|)
return|;
block|}
comment|/**      * return the source value of this resource      *       * @return value of source property      */
specifier|public
name|String
name|getSource
parameter_list|()
block|{
name|TextType
name|tt
init|=
operator|(
name|TextType
operator|)
name|getProperty
argument_list|(
name|SOURCE
argument_list|)
decl_stmt|;
return|return
name|tt
operator|==
literal|null
condition|?
literal|null
else|:
name|tt
operator|.
name|getStringValue
argument_list|()
return|;
block|}
comment|/**      * Return the bag DC Subject      *       * @return the subject property      */
specifier|public
name|ArrayProperty
name|getSubjectsProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|SUBJECT
argument_list|)
return|;
block|}
comment|/**      * Return the list of values defined in the DC Subject      *       * @return the list of subject values      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSubjects
parameter_list|()
block|{
return|return
name|getUnqualifiedBagValueList
argument_list|(
name|SUBJECT
argument_list|)
return|;
block|}
comment|/**      * Return the Lang alt Title      *       * @return the title property      */
specifier|public
name|ArrayProperty
name|getTitleProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|TITLE
argument_list|)
return|;
block|}
comment|/**      * Return a list of languages defined in Title property      *       * @return list of languages defined for title property      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTitleLanguages
parameter_list|()
block|{
return|return
name|getUnqualifiedLanguagePropertyLanguagesValue
argument_list|(
name|TITLE
argument_list|)
return|;
block|}
comment|/**      * Return a language value for Title property      *       * @param lang      *            the language concerned      * @return the title value for specified language      */
specifier|public
name|String
name|getTitle
parameter_list|(
name|String
name|lang
parameter_list|)
block|{
return|return
name|getUnqualifiedLanguagePropertyValue
argument_list|(
name|TITLE
argument_list|,
name|lang
argument_list|)
return|;
block|}
comment|/**      * Get the default value for the title.      *       * @return The default title of this resource.      */
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|getTitle
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * Return the bag DC Type      *       * @return the type property      */
specifier|public
name|ArrayProperty
name|getTypesProperty
parameter_list|()
block|{
return|return
operator|(
name|ArrayProperty
operator|)
name|getProperty
argument_list|(
name|TYPE
argument_list|)
return|;
block|}
comment|/**      * Return the list of values defined in the DC Type      *       * @return the value of type property      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTypes
parameter_list|()
block|{
return|return
name|getUnqualifiedBagValueList
argument_list|(
name|TYPE
argument_list|)
return|;
block|}
specifier|public
name|void
name|removeType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|removeUnqualifiedBagValue
argument_list|(
name|TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

