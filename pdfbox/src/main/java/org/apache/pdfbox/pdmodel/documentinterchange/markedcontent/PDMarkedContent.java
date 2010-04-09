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
name|documentinterchange
operator|.
name|markedcontent
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSDictionary
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSName
import|;
end_import

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
name|documentinterchange
operator|.
name|taggedpdf
operator|.
name|PDArtifactMarkedContent
import|;
end_import

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
name|graphics
operator|.
name|xobject
operator|.
name|PDXObject
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|TextPosition
import|;
end_import

begin_comment
comment|/**  * A marked content.  *   * @author<a href="mailto:Johannes%20Koch%20%3Ckoch@apache.org%3E">Johannes Koch</a>  * @version $Revision: $  */
end_comment

begin_class
specifier|public
class|class
name|PDMarkedContent
block|{
comment|/**      * Creates a marked-content sequence.      *       * @param tag the tag      * @param properties the properties      * @return the marked-content sequence      */
specifier|public
specifier|static
name|PDMarkedContent
name|create
parameter_list|(
name|COSName
name|tag
parameter_list|,
name|COSDictionary
name|properties
parameter_list|)
block|{
if|if
condition|(
name|COSName
operator|.
name|ARTIFACT
operator|.
name|equals
argument_list|(
name|tag
argument_list|)
condition|)
block|{
operator|new
name|PDArtifactMarkedContent
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PDMarkedContent
argument_list|(
name|tag
argument_list|,
name|properties
argument_list|)
return|;
block|}
specifier|private
name|String
name|tag
decl_stmt|;
specifier|private
name|COSDictionary
name|properties
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|contents
decl_stmt|;
comment|/**      * Creates a new marked content object.      *       * @param tag the tag      * @param properties the properties      */
specifier|public
name|PDMarkedContent
parameter_list|(
name|COSName
name|tag
parameter_list|,
name|COSDictionary
name|properties
parameter_list|)
block|{
name|this
operator|.
name|tag
operator|=
name|tag
operator|==
literal|null
condition|?
literal|null
else|:
name|tag
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
name|this
operator|.
name|contents
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Gets the tag.      *       * @return the tag      */
specifier|public
name|String
name|getTag
parameter_list|()
block|{
return|return
name|this
operator|.
name|tag
return|;
block|}
comment|/**      * Gets the properties.      *       * @return the properties      */
specifier|public
name|COSDictionary
name|getProperties
parameter_list|()
block|{
return|return
name|this
operator|.
name|properties
return|;
block|}
comment|/**      * Gets the marked-content identifier.      *       * @return the marked-content identifier      */
specifier|public
name|int
name|getMCID
parameter_list|()
block|{
return|return
name|this
operator|.
name|getProperties
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|this
operator|.
name|getProperties
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|MCID
argument_list|)
return|;
block|}
comment|/**      * Gets the language (Lang).      *       * @return the language      */
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|this
operator|.
name|getProperties
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|this
operator|.
name|getProperties
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|LANG
argument_list|)
return|;
block|}
comment|/**      * Gets the actual text (ActualText).      *       * @return the actual text      */
specifier|public
name|String
name|getActualText
parameter_list|()
block|{
return|return
name|this
operator|.
name|getProperties
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|this
operator|.
name|getProperties
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|ACTUAL_TEXT
argument_list|)
return|;
block|}
comment|/**      * Gets the alternate description (Alt).      *       * @return the alternate description      */
specifier|public
name|String
name|getAlternateDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|getProperties
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|this
operator|.
name|getProperties
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|ALT
argument_list|)
return|;
block|}
comment|/**      * Gets the expanded form (E).      *       * @return the expanded form      */
specifier|public
name|String
name|getExpandedForm
parameter_list|()
block|{
return|return
name|this
operator|.
name|getProperties
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|this
operator|.
name|getProperties
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|E
argument_list|)
return|;
block|}
comment|/**      * Gets the contents of the marked content sequence. Can be      *<ul>      *<li>{@link TextPosition},</li>      *<li>{@link PDMarkedContent}, or</li>      *<li>{@link PDXObject}.</li>      *</ul>      *       * @return the contents of the marked content sequence      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getContents
parameter_list|()
block|{
return|return
name|this
operator|.
name|contents
return|;
block|}
comment|/**      * Adds a text position to the contents.      *       * @param text the text position      */
specifier|public
name|void
name|addText
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
name|this
operator|.
name|getContents
argument_list|()
operator|.
name|add
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a marked content to the contents.      *       * @param markedContent the marked content      */
specifier|public
name|void
name|addMarkedContent
parameter_list|(
name|PDMarkedContent
name|markedContent
parameter_list|)
block|{
name|this
operator|.
name|getContents
argument_list|()
operator|.
name|add
argument_list|(
name|markedContent
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds an XObject to the contents.      *       * @param xobject the XObject      */
specifier|public
name|void
name|addXObject
parameter_list|(
name|PDXObject
name|xobject
parameter_list|)
block|{
name|this
operator|.
name|getContents
argument_list|()
operator|.
name|add
argument_list|(
name|xobject
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"tag="
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|tag
argument_list|)
operator|.
name|append
argument_list|(
literal|", properties="
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|properties
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", contents="
argument_list|)
operator|.
name|append
argument_list|(
name|this
operator|.
name|contents
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

