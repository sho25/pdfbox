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
comment|/**  * This class models the  *<a href="https://docs.microsoft.com/en-us/typography/opentype/spec/chapter2#lookup-table">Lookup Sub-Table</a> in the  * Open Type layout common tables.  *   * @author Palash Ray  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|LookupSubTable
block|{
specifier|private
specifier|final
name|int
name|substFormat
decl_stmt|;
specifier|private
specifier|final
name|CoverageTable
name|coverageTable
decl_stmt|;
specifier|public
name|LookupSubTable
parameter_list|(
name|int
name|substFormat
parameter_list|,
name|CoverageTable
name|coverageTable
parameter_list|)
block|{
name|this
operator|.
name|substFormat
operator|=
name|substFormat
expr_stmt|;
name|this
operator|.
name|coverageTable
operator|=
name|coverageTable
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|int
name|doSubstitution
parameter_list|(
name|int
name|gid
parameter_list|,
name|int
name|coverageIndex
parameter_list|)
function_decl|;
specifier|public
name|int
name|getSubstFormat
parameter_list|()
block|{
return|return
name|substFormat
return|;
block|}
specifier|public
name|CoverageTable
name|getCoverageTable
parameter_list|()
block|{
return|return
name|coverageTable
return|;
block|}
block|}
end_class

end_unit

