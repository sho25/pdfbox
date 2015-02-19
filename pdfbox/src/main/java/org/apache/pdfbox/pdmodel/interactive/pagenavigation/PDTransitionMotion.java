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
name|pdmodel
operator|.
name|interactive
operator|.
name|pagenavigation
package|;
end_package

begin_comment
comment|/**  * The direction of motion for the specified transition effect. Only for {@link PDTransitionStyle#Split},  * {@link PDTransitionStyle#Blinds} and {@link PDTransitionStyle#Fly}.  *   * @author Andrea Vacondio  *  */
end_comment

begin_enum
specifier|public
enum|enum
name|PDTransitionMotion
block|{
comment|/**      * Inward from the edges of the page      */
name|I
block|,
comment|/**      * Outward from the center of the page      */
name|O
block|; }
end_enum

end_unit

