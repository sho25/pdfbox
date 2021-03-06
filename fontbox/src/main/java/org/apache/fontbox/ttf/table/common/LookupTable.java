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
comment|/**  * This class models the  *<a href="https://docs.microsoft.com/en-us/typography/opentype/spec/chapter2#lookup-table">Lookup Table</a> in the  * Open Type layout common tables.  *   * @author Palash Ray  *  */
end_comment

begin_class
specifier|public
class|class
name|LookupTable
block|{
specifier|private
specifier|final
name|int
name|lookupType
decl_stmt|;
specifier|private
specifier|final
name|int
name|lookupFlag
decl_stmt|;
specifier|private
specifier|final
name|int
name|markFilteringSet
decl_stmt|;
specifier|private
specifier|final
name|LookupSubTable
index|[]
name|subTables
decl_stmt|;
specifier|public
name|LookupTable
parameter_list|(
name|int
name|lookupType
parameter_list|,
name|int
name|lookupFlag
parameter_list|,
name|int
name|markFilteringSet
parameter_list|,
name|LookupSubTable
index|[]
name|subTables
parameter_list|)
block|{
name|this
operator|.
name|lookupType
operator|=
name|lookupType
expr_stmt|;
name|this
operator|.
name|lookupFlag
operator|=
name|lookupFlag
expr_stmt|;
name|this
operator|.
name|markFilteringSet
operator|=
name|markFilteringSet
expr_stmt|;
name|this
operator|.
name|subTables
operator|=
name|subTables
expr_stmt|;
block|}
specifier|public
name|int
name|getLookupType
parameter_list|()
block|{
return|return
name|lookupType
return|;
block|}
specifier|public
name|int
name|getLookupFlag
parameter_list|()
block|{
return|return
name|lookupFlag
return|;
block|}
specifier|public
name|int
name|getMarkFilteringSet
parameter_list|()
block|{
return|return
name|markFilteringSet
return|;
block|}
specifier|public
name|LookupSubTable
index|[]
name|getSubTables
parameter_list|()
block|{
return|return
name|subTables
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
literal|"LookupTable[lookupType=%d,lookupFlag=%d,markFilteringSet=%d]"
argument_list|,
name|lookupType
argument_list|,
name|lookupFlag
argument_list|,
name|markFilteringSet
argument_list|)
return|;
block|}
block|}
end_class

end_unit

