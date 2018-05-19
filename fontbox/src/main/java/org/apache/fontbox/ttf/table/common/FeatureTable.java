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
name|common
package|;
end_package

begin_comment
comment|/**  * This class models the  *<a href="https://docs.microsoft.com/en-us/typography/opentype/spec/chapter2#feature-table">Feature table</a> in the  * Open Type layout common tables.  *   * @author Palash Ray  *  */
end_comment

begin_class
specifier|public
class|class
name|FeatureTable
block|{
specifier|private
specifier|final
name|int
name|featureParams
decl_stmt|;
specifier|private
specifier|final
name|int
name|lookupIndexCount
decl_stmt|;
specifier|private
specifier|final
name|int
index|[]
name|lookupListIndices
decl_stmt|;
specifier|public
name|FeatureTable
parameter_list|(
name|int
name|featureParams
parameter_list|,
name|int
name|lookupIndexCount
parameter_list|,
name|int
index|[]
name|lookupListIndices
parameter_list|)
block|{
name|this
operator|.
name|featureParams
operator|=
name|featureParams
expr_stmt|;
name|this
operator|.
name|lookupIndexCount
operator|=
name|lookupIndexCount
expr_stmt|;
name|this
operator|.
name|lookupListIndices
operator|=
name|lookupListIndices
expr_stmt|;
block|}
specifier|public
name|int
name|getFeatureParams
parameter_list|()
block|{
return|return
name|featureParams
return|;
block|}
specifier|public
name|int
name|getLookupIndexCount
parameter_list|()
block|{
return|return
name|lookupIndexCount
return|;
block|}
specifier|public
name|int
index|[]
name|getLookupListIndices
parameter_list|()
block|{
return|return
name|lookupListIndices
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
literal|"FeatureTable[lookupListIndiciesCount=%d]"
argument_list|,
name|lookupListIndices
operator|.
name|length
argument_list|)
return|;
block|}
block|}
end_class

end_unit
