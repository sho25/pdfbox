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
name|util
package|;
end_package

begin_comment
comment|/**  * wrapper of TextPosition that adds flags to track  * status as linestart and paragraph start positions.  *<p>  * This is implemented as a wrapper since the TextPosition  * class doesn't provide complete access to its  * state fields to subclasses.  Also, conceptually TextPosition is  * immutable while these flags need to be set post-creation so  * it makes sense to put these flags in this separate class.  *</p>  * @author m.martinez@ll.mit.edu  *  */
end_comment

begin_class
specifier|public
class|class
name|PositionWrapper
block|{
specifier|private
name|boolean
name|isLineStart
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|isParagraphStart
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|isPageBreak
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|isHangingIndent
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|isArticleStart
init|=
literal|false
decl_stmt|;
specifier|private
name|TextPosition
name|position
init|=
literal|null
decl_stmt|;
comment|/**      * Returns the underlying TextPosition object.      * @return the text position      */
specifier|protected
name|TextPosition
name|getTextPosition
parameter_list|()
block|{
return|return
name|position
return|;
block|}
specifier|public
name|boolean
name|isLineStart
parameter_list|()
block|{
return|return
name|isLineStart
return|;
block|}
comment|/**      * Sets the isLineStart() flag to true.      */
specifier|public
name|void
name|setLineStart
parameter_list|()
block|{
name|this
operator|.
name|isLineStart
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|boolean
name|isParagraphStart
parameter_list|()
block|{
return|return
name|isParagraphStart
return|;
block|}
comment|/**      * sets the isParagraphStart() flag to true.      */
specifier|public
name|void
name|setParagraphStart
parameter_list|()
block|{
name|this
operator|.
name|isParagraphStart
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|boolean
name|isArticleStart
parameter_list|()
block|{
return|return
name|isArticleStart
return|;
block|}
comment|/**      * Sets the isArticleStart() flag to true.      */
specifier|public
name|void
name|setArticleStart
parameter_list|()
block|{
name|this
operator|.
name|isArticleStart
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPageBreak
parameter_list|()
block|{
return|return
name|isPageBreak
return|;
block|}
comment|/**      * Sets the isPageBreak() flag to true.      */
specifier|public
name|void
name|setPageBreak
parameter_list|()
block|{
name|this
operator|.
name|isPageBreak
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|boolean
name|isHangingIndent
parameter_list|()
block|{
return|return
name|isHangingIndent
return|;
block|}
comment|/**      * Sets the isHangingIndent() flag to true.      */
specifier|public
name|void
name|setHangingIndent
parameter_list|()
block|{
name|this
operator|.
name|isHangingIndent
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Constructs a PositionWrapper around the specified TextPosition object.      * @param position the text position      */
specifier|public
name|PositionWrapper
parameter_list|(
name|TextPosition
name|position
parameter_list|)
block|{
name|this
operator|.
name|position
operator|=
name|position
expr_stmt|;
block|}
block|}
end_class

end_unit

