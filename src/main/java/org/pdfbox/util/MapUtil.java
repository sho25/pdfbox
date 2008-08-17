begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * This class with handle some simple Map operations.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|MapUtil
block|{
specifier|private
name|MapUtil
parameter_list|()
block|{
comment|//utility class
block|}
comment|/**      * Generate a unique key for the map based on a prefix.      *      * @param map The map to look for existing keys.      * @param prefix The prefix to use when generating the key.      * @return The new unique key that does not currently exist in the map.      */
specifier|public
specifier|static
specifier|final
name|String
name|getNextUniqueKey
parameter_list|(
name|Map
name|map
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|int
name|counter
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|map
operator|.
name|get
argument_list|(
name|prefix
operator|+
name|counter
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|counter
operator|++
expr_stmt|;
block|}
return|return
name|prefix
operator|+
name|counter
return|;
block|}
block|}
end_class

end_unit

