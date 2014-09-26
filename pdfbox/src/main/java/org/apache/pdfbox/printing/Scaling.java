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
name|printing
package|;
end_package

begin_comment
comment|/**  * Scale of the image on printed pages.  *  * @author John Hewson  */
end_comment

begin_enum
specifier|public
enum|enum
name|Scaling
block|{
comment|/** Print the image at 100% scale. */
name|ACTUAL_SIZE
block|,
comment|/** Shrink the image to fit the page, if needed. */
name|SHRINK_TO_FIT
block|,
comment|/** Stretch the image to fill the page, if needed. */
name|STRETCH_TO_FIT
block|,
comment|/** Stretch or shrink the image to fill the page, as needed. */
name|SCALE_TO_FIT
block|}
end_enum

end_unit

