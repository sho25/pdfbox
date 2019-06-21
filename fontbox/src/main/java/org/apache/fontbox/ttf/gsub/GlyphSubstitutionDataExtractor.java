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
name|fontbox
operator|.
name|ttf
operator|.
name|gsub
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|model
operator|.
name|GsubData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|model
operator|.
name|Language
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|model
operator|.
name|MapBackedGsubData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|common
operator|.
name|CoverageTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|common
operator|.
name|FeatureListTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|common
operator|.
name|FeatureRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|common
operator|.
name|LangSysTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|common
operator|.
name|LookupListTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|common
operator|.
name|LookupSubTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|common
operator|.
name|LookupTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|common
operator|.
name|ScriptTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|gsub
operator|.
name|LigatureSetTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|gsub
operator|.
name|LigatureTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|gsub
operator|.
name|LookupTypeLigatureSubstitutionSubstFormat1
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|gsub
operator|.
name|LookupTypeSingleSubstFormat1
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|table
operator|.
name|gsub
operator|.
name|LookupTypeSingleSubstFormat2
import|;
end_import

begin_comment
comment|/**  * This class has utility methods to extract meaningful data from the highly obfuscated GSUB Tables. This data is then  * used to determine which combination of Glyphs or words have to be replaced.  *   * @author Palash Ray  *   */
end_comment

begin_class
specifier|public
class|class
name|GlyphSubstitutionDataExtractor
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|GlyphSubstitutionDataExtractor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|GsubData
name|getGsubData
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ScriptTable
argument_list|>
name|scriptList
parameter_list|,
name|FeatureListTable
name|featureListTable
parameter_list|,
name|LookupListTable
name|lookupListTable
parameter_list|)
block|{
name|ScriptTableDetails
name|scriptTableDetails
init|=
name|getSupportedLanguage
argument_list|(
name|scriptList
argument_list|)
decl_stmt|;
if|if
condition|(
name|scriptTableDetails
operator|==
literal|null
condition|)
block|{
return|return
name|GsubData
operator|.
name|NO_DATA_FOUND
return|;
block|}
name|ScriptTable
name|scriptTable
init|=
name|scriptTableDetails
operator|.
name|getScriptTable
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|gsubData
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// the starting point is really the scriptTags
if|if
condition|(
name|scriptTable
operator|.
name|getDefaultLangSysTable
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|populateGsubData
argument_list|(
name|gsubData
argument_list|,
name|scriptTable
operator|.
name|getDefaultLangSysTable
argument_list|()
argument_list|,
name|featureListTable
argument_list|,
name|lookupListTable
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|LangSysTable
name|langSysTable
range|:
name|scriptTable
operator|.
name|getLangSysTables
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
name|populateGsubData
argument_list|(
name|gsubData
argument_list|,
name|langSysTable
argument_list|,
name|featureListTable
argument_list|,
name|lookupListTable
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|MapBackedGsubData
argument_list|(
name|scriptTableDetails
operator|.
name|getLanguage
argument_list|()
argument_list|,
name|scriptTableDetails
operator|.
name|getFeatureName
argument_list|()
argument_list|,
name|gsubData
argument_list|)
return|;
block|}
specifier|private
name|ScriptTableDetails
name|getSupportedLanguage
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ScriptTable
argument_list|>
name|scriptList
parameter_list|)
block|{
for|for
control|(
name|Language
name|lang
range|:
name|Language
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|scriptName
range|:
name|lang
operator|.
name|getScriptNames
argument_list|()
control|)
block|{
if|if
condition|(
name|scriptList
operator|.
name|containsKey
argument_list|(
name|scriptName
argument_list|)
condition|)
block|{
return|return
operator|new
name|ScriptTableDetails
argument_list|(
name|lang
argument_list|,
name|scriptName
argument_list|,
name|scriptList
operator|.
name|get
argument_list|(
name|scriptName
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|populateGsubData
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|gsubData
parameter_list|,
name|LangSysTable
name|langSysTable
parameter_list|,
name|FeatureListTable
name|featureListTable
parameter_list|,
name|LookupListTable
name|lookupListTable
parameter_list|)
block|{
for|for
control|(
name|int
name|featureIndex
range|:
name|langSysTable
operator|.
name|getFeatureIndices
argument_list|()
control|)
block|{
name|FeatureRecord
name|featureRecord
init|=
name|featureListTable
operator|.
name|getFeatureRecords
argument_list|()
index|[
name|featureIndex
index|]
decl_stmt|;
name|populateGsubData
argument_list|(
name|gsubData
argument_list|,
name|featureRecord
argument_list|,
name|lookupListTable
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|populateGsubData
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|gsubData
parameter_list|,
name|FeatureRecord
name|featureRecord
parameter_list|,
name|LookupListTable
name|lookupListTable
parameter_list|)
block|{
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|glyphSubstitutionMap
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|lookupIndex
range|:
name|featureRecord
operator|.
name|getFeatureTable
argument_list|()
operator|.
name|getLookupListIndices
argument_list|()
control|)
block|{
name|LookupTable
name|lookupTable
init|=
name|lookupListTable
operator|.
name|getLookups
argument_list|()
index|[
name|lookupIndex
index|]
decl_stmt|;
name|extractData
argument_list|(
name|glyphSubstitutionMap
argument_list|,
name|lookupTable
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"*********** extracting GSUB data for the feature: "
operator|+
name|featureRecord
operator|.
name|getFeatureTag
argument_list|()
operator|+
literal|", glyphSubstitutionMap: "
operator|+
name|glyphSubstitutionMap
argument_list|)
expr_stmt|;
name|gsubData
operator|.
name|put
argument_list|(
name|featureRecord
operator|.
name|getFeatureTag
argument_list|()
argument_list|,
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|glyphSubstitutionMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|extractData
parameter_list|(
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|glyphSubstitutionMap
parameter_list|,
name|LookupTable
name|lookupTable
parameter_list|)
block|{
for|for
control|(
name|LookupSubTable
name|lookupSubTable
range|:
name|lookupTable
operator|.
name|getSubTables
argument_list|()
control|)
block|{
if|if
condition|(
name|lookupSubTable
operator|instanceof
name|LookupTypeLigatureSubstitutionSubstFormat1
condition|)
block|{
name|extractDataFromLigatureSubstitutionSubstFormat1Table
argument_list|(
name|glyphSubstitutionMap
argument_list|,
operator|(
name|LookupTypeLigatureSubstitutionSubstFormat1
operator|)
name|lookupSubTable
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lookupSubTable
operator|instanceof
name|LookupTypeSingleSubstFormat1
condition|)
block|{
name|extractDataFromSingleSubstTableFormat1Table
argument_list|(
name|glyphSubstitutionMap
argument_list|,
operator|(
name|LookupTypeSingleSubstFormat1
operator|)
name|lookupSubTable
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lookupSubTable
operator|instanceof
name|LookupTypeSingleSubstFormat2
condition|)
block|{
name|extractDataFromSingleSubstTableFormat2Table
argument_list|(
name|glyphSubstitutionMap
argument_list|,
operator|(
name|LookupTypeSingleSubstFormat2
operator|)
name|lookupSubTable
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// usually null, due to being skipped in GlyphSubstitutionTable.readLookupTable()
name|LOG
operator|.
name|debug
argument_list|(
literal|"The type "
operator|+
name|lookupSubTable
operator|+
literal|" is not yet supported, will be ignored"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|extractDataFromSingleSubstTableFormat1Table
parameter_list|(
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|glyphSubstitutionMap
parameter_list|,
name|LookupTypeSingleSubstFormat1
name|singleSubstTableFormat1
parameter_list|)
block|{
name|CoverageTable
name|coverageTable
init|=
name|singleSubstTableFormat1
operator|.
name|getCoverageTable
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|coverageTable
operator|.
name|getSize
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|int
name|coverageGlyphId
init|=
name|coverageTable
operator|.
name|getGlyphId
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|int
name|substituteGlyphId
init|=
name|coverageGlyphId
operator|+
name|singleSubstTableFormat1
operator|.
name|getDeltaGlyphID
argument_list|()
decl_stmt|;
name|putNewSubstitutionEntry
argument_list|(
name|glyphSubstitutionMap
argument_list|,
name|substituteGlyphId
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|coverageGlyphId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|extractDataFromSingleSubstTableFormat2Table
parameter_list|(
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|glyphSubstitutionMap
parameter_list|,
name|LookupTypeSingleSubstFormat2
name|singleSubstTableFormat2
parameter_list|)
block|{
name|CoverageTable
name|coverageTable
init|=
name|singleSubstTableFormat2
operator|.
name|getCoverageTable
argument_list|()
decl_stmt|;
if|if
condition|(
name|coverageTable
operator|.
name|getSize
argument_list|()
operator|!=
name|singleSubstTableFormat2
operator|.
name|getSubstituteGlyphIDs
argument_list|()
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The no. coverage table entries should be the same as the size of the substituteGlyphIDs"
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|coverageTable
operator|.
name|getSize
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|int
name|coverageGlyphId
init|=
name|coverageTable
operator|.
name|getGlyphId
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|int
name|substituteGlyphId
init|=
name|coverageGlyphId
operator|+
name|singleSubstTableFormat2
operator|.
name|getSubstituteGlyphIDs
argument_list|()
index|[
name|i
index|]
decl_stmt|;
name|putNewSubstitutionEntry
argument_list|(
name|glyphSubstitutionMap
argument_list|,
name|substituteGlyphId
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|coverageGlyphId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|extractDataFromLigatureSubstitutionSubstFormat1Table
parameter_list|(
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|glyphSubstitutionMap
parameter_list|,
name|LookupTypeLigatureSubstitutionSubstFormat1
name|ligatureSubstitutionTable
parameter_list|)
block|{
for|for
control|(
name|LigatureSetTable
name|ligatureSetTable
range|:
name|ligatureSubstitutionTable
operator|.
name|getLigatureSetTables
argument_list|()
control|)
block|{
for|for
control|(
name|LigatureTable
name|ligatureTable
range|:
name|ligatureSetTable
operator|.
name|getLigatureTables
argument_list|()
control|)
block|{
name|extractDataFromLigatureTable
argument_list|(
name|glyphSubstitutionMap
argument_list|,
name|ligatureTable
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|extractDataFromLigatureTable
parameter_list|(
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|glyphSubstitutionMap
parameter_list|,
name|LigatureTable
name|ligatureTable
parameter_list|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsToBeSubstituted
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|componentGlyphID
range|:
name|ligatureTable
operator|.
name|getComponentGlyphIDs
argument_list|()
control|)
block|{
name|glyphsToBeSubstituted
operator|.
name|add
argument_list|(
name|componentGlyphID
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"glyphsToBeSubstituted: "
operator|+
name|glyphsToBeSubstituted
argument_list|)
expr_stmt|;
name|putNewSubstitutionEntry
argument_list|(
name|glyphSubstitutionMap
argument_list|,
name|ligatureTable
operator|.
name|getLigatureGlyph
argument_list|()
argument_list|,
name|glyphsToBeSubstituted
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|putNewSubstitutionEntry
parameter_list|(
name|Map
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|glyphSubstitutionMap
parameter_list|,
name|int
name|newGlyph
parameter_list|,
name|List
argument_list|<
name|Integer
argument_list|>
name|glyphsToBeSubstituted
parameter_list|)
block|{
name|Integer
name|oldValue
init|=
name|glyphSubstitutionMap
operator|.
name|put
argument_list|(
name|glyphsToBeSubstituted
argument_list|,
name|newGlyph
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldValue
operator|!=
literal|null
condition|)
block|{
name|String
name|message
init|=
literal|"For the newGlyph: "
operator|+
name|newGlyph
operator|+
literal|", newValue: "
operator|+
name|glyphsToBeSubstituted
operator|+
literal|" is trying to override the oldValue: "
operator|+
name|oldValue
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|ScriptTableDetails
block|{
specifier|private
specifier|final
name|Language
name|language
decl_stmt|;
specifier|private
specifier|final
name|String
name|featureName
decl_stmt|;
specifier|private
specifier|final
name|ScriptTable
name|scriptTable
decl_stmt|;
specifier|private
name|ScriptTableDetails
parameter_list|(
name|Language
name|language
parameter_list|,
name|String
name|featureName
parameter_list|,
name|ScriptTable
name|scriptTable
parameter_list|)
block|{
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
name|this
operator|.
name|featureName
operator|=
name|featureName
expr_stmt|;
name|this
operator|.
name|scriptTable
operator|=
name|scriptTable
expr_stmt|;
block|}
specifier|public
name|Language
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
specifier|public
name|String
name|getFeatureName
parameter_list|()
block|{
return|return
name|featureName
return|;
block|}
specifier|public
name|ScriptTable
name|getScriptTable
parameter_list|()
block|{
return|return
name|scriptTable
return|;
block|}
block|}
block|}
end_class

end_unit

