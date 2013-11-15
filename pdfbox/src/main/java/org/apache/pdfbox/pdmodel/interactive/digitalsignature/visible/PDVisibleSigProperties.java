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
name|digitalsignature
operator|.
name|visible
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_comment
comment|/**  * This builder class is in order to create visible signature properties.  *   * @author<a href="mailto:vakhtang.koroghlishvili@gmail.com"> vakhtang koroghlishvili (gogebashvili)</a>  *   */
end_comment

begin_class
specifier|public
class|class
name|PDVisibleSigProperties
block|{
specifier|private
name|String
name|signerName
decl_stmt|;
specifier|private
name|String
name|signerLocation
decl_stmt|;
specifier|private
name|String
name|signatureReason
decl_stmt|;
specifier|private
name|boolean
name|visualSignEnabled
decl_stmt|;
specifier|private
name|int
name|page
decl_stmt|;
specifier|private
name|int
name|preferredSize
decl_stmt|;
specifier|private
name|InputStream
name|visibleSignature
decl_stmt|;
specifier|private
name|PDVisibleSignDesigner
name|pdVisibleSignature
decl_stmt|;
comment|/**      * start building of visible signature      *       * @throws IOException      */
specifier|public
name|void
name|buildSignature
parameter_list|()
throws|throws
name|IOException
block|{
name|PDFTemplateBuilder
name|builder
init|=
operator|new
name|PDVisibleSigBuilder
argument_list|()
decl_stmt|;
name|PDFTemplateCreator
name|creator
init|=
operator|new
name|PDFTemplateCreator
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|setVisibleSignature
argument_list|(
name|creator
operator|.
name|buildPDF
argument_list|(
name|getPdVisibleSignature
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      *       * @return - signer name      */
specifier|public
name|String
name|getSignerName
parameter_list|()
block|{
return|return
name|signerName
return|;
block|}
comment|/**      * Sets signer name      * @param signerName      * @return      */
specifier|public
name|PDVisibleSigProperties
name|signerName
parameter_list|(
name|String
name|signerName
parameter_list|)
block|{
name|this
operator|.
name|signerName
operator|=
name|signerName
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Gets signer locations      * @return - location      */
specifier|public
name|String
name|getSignerLocation
parameter_list|()
block|{
return|return
name|signerLocation
return|;
block|}
comment|/**      * Sets location      * @param signerLocation      * @return      */
specifier|public
name|PDVisibleSigProperties
name|signerLocation
parameter_list|(
name|String
name|signerLocation
parameter_list|)
block|{
name|this
operator|.
name|signerLocation
operator|=
name|signerLocation
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * gets reason of signing      * @return       */
specifier|public
name|String
name|getSignatureReason
parameter_list|()
block|{
return|return
name|signatureReason
return|;
block|}
comment|/**      * sets reason of signing      * @param signatureReason      * @return      */
specifier|public
name|PDVisibleSigProperties
name|signatureReason
parameter_list|(
name|String
name|signatureReason
parameter_list|)
block|{
name|this
operator|.
name|signatureReason
operator|=
name|signatureReason
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * returns your page      * @return       */
specifier|public
name|int
name|getPage
parameter_list|()
block|{
return|return
name|page
return|;
block|}
comment|/**      * sets page number      * @param page      * @return      */
specifier|public
name|PDVisibleSigProperties
name|page
parameter_list|(
name|int
name|page
parameter_list|)
block|{
name|this
operator|.
name|page
operator|=
name|page
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * gets our preferred size      * @return      */
specifier|public
name|int
name|getPreferredSize
parameter_list|()
block|{
return|return
name|preferredSize
return|;
block|}
comment|/**      * sets our preferred size      * @param preferredSize      * @return      */
specifier|public
name|PDVisibleSigProperties
name|preferredSize
parameter_list|(
name|int
name|preferredSize
parameter_list|)
block|{
name|this
operator|.
name|preferredSize
operator|=
name|preferredSize
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * checks if we need to add visible signature      * @return      */
specifier|public
name|boolean
name|isVisualSignEnabled
parameter_list|()
block|{
return|return
name|visualSignEnabled
return|;
block|}
comment|/**      * sets visible signature to be added or not      * @param visualSignEnabled      * @return      */
specifier|public
name|PDVisibleSigProperties
name|visualSignEnabled
parameter_list|(
name|boolean
name|visualSignEnabled
parameter_list|)
block|{
name|this
operator|.
name|visualSignEnabled
operator|=
name|visualSignEnabled
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * this method gets visible signature configuration object      * @return      */
specifier|public
name|PDVisibleSignDesigner
name|getPdVisibleSignature
parameter_list|()
block|{
return|return
name|pdVisibleSignature
return|;
block|}
comment|/**      * Sets visible signature configuration Object      * @param pdVisibleSignature      * @return      */
specifier|public
name|PDVisibleSigProperties
name|setPdVisibleSignature
parameter_list|(
name|PDVisibleSignDesigner
name|pdVisibleSignature
parameter_list|)
block|{
name|this
operator|.
name|pdVisibleSignature
operator|=
name|pdVisibleSignature
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * returns visible signature configuration object      * @return      */
specifier|public
name|InputStream
name|getVisibleSignature
parameter_list|()
block|{
return|return
name|visibleSignature
return|;
block|}
comment|/**      * sets configuration object of visible signature      * @param visibleSignature      */
specifier|public
name|void
name|setVisibleSignature
parameter_list|(
name|InputStream
name|visibleSignature
parameter_list|)
block|{
name|this
operator|.
name|visibleSignature
operator|=
name|visibleSignature
expr_stmt|;
block|}
block|}
end_class

end_unit

