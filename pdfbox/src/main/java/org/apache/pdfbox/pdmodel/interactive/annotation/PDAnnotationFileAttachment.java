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
name|common
operator|.
name|filespecification
operator|.
name|PDFileSpecification
import|;
end_import

begin_comment
comment|/**  * This is the class that represents a file attachement.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationFileAttachment
extends|extends
name|PDAnnotationMarkup
block|{
comment|/**      * See get/setAttachmentName.      */
specifier|public
specifier|static
specifier|final
name|String
name|ATTACHMENT_NAME_PUSH_PIN
init|=
literal|"PushPin"
decl_stmt|;
comment|/**      * See get/setAttachmentName.      */
specifier|public
specifier|static
specifier|final
name|String
name|ATTACHMENT_NAME_GRAPH
init|=
literal|"Graph"
decl_stmt|;
comment|/**      * See get/setAttachmentName.      */
specifier|public
specifier|static
specifier|final
name|String
name|ATTACHMENT_NAME_PAPERCLIP
init|=
literal|"Paperclip"
decl_stmt|;
comment|/**      * See get/setAttachmentName.      */
specifier|public
specifier|static
specifier|final
name|String
name|ATTACHMENT_NAME_TAG
init|=
literal|"Tag"
decl_stmt|;
comment|/**      * The type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"FileAttachment"
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDAnnotationFileAttachment
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
name|SUB_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Link annotation from a COSDictionary, expected to be a correct object definition.      *      * @param field the PDF objet to represent as a field.      */
specifier|public
name|PDAnnotationFileAttachment
parameter_list|(
name|COSDictionary
name|field
parameter_list|)
block|{
name|super
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
comment|/**      * Return the attached file.      *      * @return The attached file.      *      * @throws IOException If there is an error creating the file spec.      */
specifier|public
name|PDFileSpecification
name|getFile
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDFileSpecification
operator|.
name|createFS
argument_list|(
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"FS"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Set the attached file.      *      * @param file The file that is attached.      */
specifier|public
name|void
name|setFile
parameter_list|(
name|PDFileSpecification
name|file
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"FS"
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
comment|/**      * This is the name used to draw the type of attachment. See the ATTACHMENT_NAME_XXX constants.      *      * @return The name that describes the visual cue for the attachment.      */
specifier|public
name|String
name|getAttachmentName
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
literal|"Name"
argument_list|,
name|ATTACHMENT_NAME_PUSH_PIN
argument_list|)
return|;
block|}
comment|/**      * Set the name used to draw the attachement icon. See the ATTACHMENT_NAME_XXX constants.      *      * @param name The name of the visual icon to draw.      */
specifier|public
name|void
name|setAttachementName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
literal|"Name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

