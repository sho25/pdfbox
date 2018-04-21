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
name|table
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
name|Arrays
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
name|LookupSubTable
import|;
end_import

begin_comment
comment|/**  * This class is a part of the<a href="https://docs.microsoft.com/en-us/typography/opentype/spec/gsub">GSUB — Glyph  * Substitution Table</a> system of tables in the Open Type Font specs. This is a part of the<a href=  * "https://docs.microsoft.com/en-us/typography/opentype/spec/gsub#lookuptype-1-single-substitution-subtable">LookupType  * 1: Single Substitution Subtable</a>. It specifically models the  *<a href= "https://docs.microsoft.com/en-us/typography/opentype/spec/gsub#12-single-substitution-format-2">Single  * Substitution Format 2</a>.  *   * @author Palash Ray  *  */
end_comment

begin_class
specifier|public
class|class
name|LookupTypeSingleSubstFormat2
extends|extends
name|LookupSubTable
block|{
specifier|private
specifier|final
name|int
index|[]
name|substituteGlyphIDs
decl_stmt|;
specifier|public
name|LookupTypeSingleSubstFormat2
parameter_list|(
name|int
name|substFormat
parameter_list|,
name|CoverageTable
name|coverageTable
parameter_list|,
name|int
index|[]
name|substituteGlyphIDs
parameter_list|)
block|{
name|super
argument_list|(
name|substFormat
argument_list|,
name|coverageTable
argument_list|)
expr_stmt|;
name|this
operator|.
name|substituteGlyphIDs
operator|=
name|substituteGlyphIDs
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|doSubstitution
parameter_list|(
name|int
name|gid
parameter_list|,
name|int
name|coverageIndex
parameter_list|)
block|{
return|return
name|coverageIndex
operator|<
literal|0
condition|?
name|gid
else|:
name|substituteGlyphIDs
index|[
name|coverageIndex
index|]
return|;
block|}
specifier|public
name|int
index|[]
name|getSubstituteGlyphIDs
parameter_list|()
block|{
return|return
name|substituteGlyphIDs
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"LookupTypeSingleSubstFormat2[substFormat=%d,substituteGlyphIDs=%s]"
argument_list|,
name|getSubstFormat
argument_list|()
argument_list|,
name|Arrays
operator|.
name|toString
argument_list|(
name|substituteGlyphIDs
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

