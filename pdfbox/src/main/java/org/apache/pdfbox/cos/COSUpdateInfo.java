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
name|pdfbox
operator|.
name|cos
package|;
end_package

begin_interface
specifier|public
interface|interface
name|COSUpdateInfo
block|{
comment|/**      * Get the update state for the COSWriter. This indicates whether an object is to be written      * when there is an incremental save.      *      * @return the update state.      */
name|boolean
name|isNeedToBeUpdated
parameter_list|()
function_decl|;
comment|/**      * Set the update state of the dictionary for the COSWriter. This indicates whether an object is      * to be written when there is an incremental save.      *      * @param flag the update state.      */
name|void
name|setNeedToBeUpdated
parameter_list|(
name|boolean
name|flag
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

