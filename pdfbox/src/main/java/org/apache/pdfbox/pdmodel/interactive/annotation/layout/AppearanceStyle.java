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
name|annotation
operator|.
name|layout
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|font
operator|.
name|PDFont
import|;
end_import

begin_comment
comment|/**  * Define styling attributes to be used for text formatting.  *   */
end_comment

begin_class
specifier|public
class|class
name|AppearanceStyle
block|{
specifier|private
name|PDFont
name|font
decl_stmt|;
comment|/**      * The font size to be used for text formatting.      *      * Defaulting to 12 to match Acrobats default.      */
specifier|private
name|float
name|fontSize
init|=
literal|12.0f
decl_stmt|;
comment|/**      * The leading (distance between lines) to be used for text formatting.      *      * Defaulting to 1.2*fontSize to match Acrobats default.      */
specifier|private
name|float
name|leading
init|=
literal|14.4f
decl_stmt|;
comment|/**      * Get the font used for text formatting.      *       * @return the font used for text formatting.      */
name|PDFont
name|getFont
parameter_list|()
block|{
return|return
name|font
return|;
block|}
comment|/**      * Set the font to be used for text formatting.      *       * @param font the font to be used.      */
specifier|public
name|void
name|setFont
parameter_list|(
name|PDFont
name|font
parameter_list|)
block|{
name|this
operator|.
name|font
operator|=
name|font
expr_stmt|;
block|}
comment|/**      * Get the fontSize used for text formatting.      *       * @return the fontSize used for text formatting.      */
name|float
name|getFontSize
parameter_list|()
block|{
return|return
name|fontSize
return|;
block|}
comment|/**      * Set the font size to be used for formatting.      *       * @param fontSize the font size.      */
specifier|public
name|void
name|setFontSize
parameter_list|(
name|float
name|fontSize
parameter_list|)
block|{
name|this
operator|.
name|fontSize
operator|=
name|fontSize
expr_stmt|;
name|leading
operator|=
name|fontSize
operator|*
literal|1.2f
expr_stmt|;
block|}
comment|/**      * Get the leading used for text formatting.      *       * @return the leading used for text formatting.      */
name|float
name|getLeading
parameter_list|()
block|{
return|return
name|leading
return|;
block|}
comment|/**      * Set the leading used for text formatting.      *       * @param leading the leading to be used.      */
name|void
name|setLeading
parameter_list|(
name|float
name|leading
parameter_list|)
block|{
name|this
operator|.
name|leading
operator|=
name|leading
expr_stmt|;
block|}
block|}
end_class

end_unit

