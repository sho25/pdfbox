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
name|font
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
name|FontBoxFont
import|;
end_import

begin_comment
comment|/**  * A font mapping from a PDF font to a FontBox font.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|FontMapping
parameter_list|<
name|T
extends|extends
name|FontBoxFont
parameter_list|>
block|{
specifier|private
specifier|final
name|T
name|font
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isFallback
decl_stmt|;
name|FontMapping
parameter_list|(
name|T
name|font
parameter_list|,
name|boolean
name|isFallback
parameter_list|)
block|{
name|this
operator|.
name|font
operator|=
name|font
expr_stmt|;
name|this
operator|.
name|isFallback
operator|=
name|isFallback
expr_stmt|;
block|}
comment|/**      * Returns the mapped, FontBox font. This is never null.      */
specifier|public
name|T
name|getFont
parameter_list|()
block|{
return|return
name|font
return|;
block|}
comment|/**      * Returns true if the mapped font is a fallback, i.e. a substitute based on basic font style,      * such as bold/italic, rather than font name.      */
specifier|public
name|boolean
name|isFallback
parameter_list|()
block|{
return|return
name|isFallback
return|;
block|}
block|}
end_class

end_unit

