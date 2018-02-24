begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2018 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|action
package|;
end_package

begin_comment
comment|/**  * This will specify whether to open the destination document in a new window.  *  * @author Tilman Hausherr  */
end_comment

begin_enum
specifier|public
enum|enum
name|OpenMode
block|{
comment|/**      * The viewer application should behave in accordance with the current user preference.      */
name|USER_PREFERENCE
block|,
comment|/**      * Destination document will replace the current document in the same window.      */
name|SAME_WINDOW
block|,
comment|/**      * Open the destination document in a new window.      */
name|NEW_WINDOW
block|}
end_enum

end_unit

