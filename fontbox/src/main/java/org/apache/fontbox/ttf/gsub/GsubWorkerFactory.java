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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|CmapLookup
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

begin_comment
comment|/**  * Gets a {@link Language} specific instance of a {@link GsubWorker}  *   * @author Palash Ray  *  */
end_comment

begin_class
specifier|public
class|class
name|GsubWorkerFactory
block|{
specifier|public
name|GsubWorker
name|getGsubWorker
parameter_list|(
name|CmapLookup
name|cmapLookup
parameter_list|,
name|GsubData
name|gsubData
parameter_list|)
block|{
switch|switch
condition|(
name|gsubData
operator|.
name|getLanguage
argument_list|()
condition|)
block|{
case|case
name|BENGALI
case|:
return|return
operator|new
name|GsubWorkerForBengali
argument_list|(
name|cmapLookup
argument_list|,
name|gsubData
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The language "
operator|+
name|gsubData
operator|.
name|getLanguage
argument_list|()
operator|+
literal|" is not yet supported"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

