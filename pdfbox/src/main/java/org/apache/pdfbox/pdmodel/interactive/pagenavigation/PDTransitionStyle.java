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
comment|/**  * The transition style that shall be used when moving to the page from another during a presentation. Ref. table 162  * PDF32000-1:2008  *   * @author Andrea Vacondio  *  */
end_comment

begin_enum
specifier|public
enum|enum
name|PDTransitionStyle
block|{
name|Split
block|,
name|Blinds
block|,
name|Box
block|,
name|Wipe
block|,
name|Dissolve
block|,
name|Glitter
block|,
name|R
block|,
name|Fly
block|,
name|Push
block|,
name|Cover
block|,
name|Uncover
block|,
name|Fade
block|}
end_enum

end_unit

