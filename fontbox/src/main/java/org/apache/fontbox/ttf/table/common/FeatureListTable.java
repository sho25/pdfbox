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
comment|/**  * This class models the  *<a href="https://docs.microsoft.com/en-us/typography/opentype/spec/chapter2#feature-list-table">Feature List  * table</a> in the Open Type layout common tables.  *   * @author Palash Ray  *  */
end_comment

begin_class
specifier|public
class|class
name|FeatureListTable
block|{
specifier|private
specifier|final
name|int
name|featureCount
decl_stmt|;
specifier|private
specifier|final
name|FeatureRecord
index|[]
name|featureRecords
decl_stmt|;
specifier|public
name|FeatureListTable
parameter_list|(
name|int
name|featureCount
parameter_list|,
name|FeatureRecord
index|[]
name|featureRecords
parameter_list|)
block|{
name|this
operator|.
name|featureCount
operator|=
name|featureCount
expr_stmt|;
name|this
operator|.
name|featureRecords
operator|=
name|featureRecords
expr_stmt|;
block|}
specifier|public
name|int
name|getFeatureCount
parameter_list|()
block|{
return|return
name|featureCount
return|;
block|}
specifier|public
name|FeatureRecord
index|[]
name|getFeatureRecords
parameter_list|()
block|{
return|return
name|featureRecords
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
literal|"%s[featureCount=%d]"
argument_list|,
name|FeatureListTable
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|featureCount
argument_list|)
return|;
block|}
block|}
end_class

end_unit

