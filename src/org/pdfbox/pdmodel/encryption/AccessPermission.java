begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|encryption
package|;
end_package

begin_comment
comment|/**  * This class represents the access permissions to a document.  * These permissions are specified in the PDF format specifications, they include:  *<ul>  *<li>print the document</li>  *<li>modify the content of the document</li>  *<li>copy or extract content of the document</li>  *<li>add or modify annotations</li>  *<li>fill in interactive form fields</li>  *<li>extract text and graphics for accessibility to visually impaired people</li>  *<li>assemble the document</li>  *<li>print in degraded quality</li>  *</ul>  *  * This class can be used to protect a document by assigning access permissions to recipients.  * In this case, it must be used with a specific ProtectionPolicy.  *  *  * When a document is decrypted, it has a currentAccessPermission property which is the access permissions  * granted to the user who decrypted the document.  *  * @see ProtectionPolicy  * @see org.pdfbox.pdmodel.PDDocument#getCurrentAccessPermission()  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author Benoit Guillon (benoit.guillon@snv.jussieu.fr)  *  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|AccessPermission
block|{
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_PERMISSIONS
init|=
literal|0xFFFFFFFF
operator|^
literal|3
decl_stmt|;
comment|//bits 0& 1 need to be zero
specifier|private
specifier|static
specifier|final
name|int
name|PRINT_BIT
init|=
literal|3
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MODIFICATION_BIT
init|=
literal|4
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|EXTRACT_BIT
init|=
literal|5
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MODIFY_ANNOTATIONS_BIT
init|=
literal|6
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FILL_IN_FORM_BIT
init|=
literal|9
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|EXTRACT_FOR_ACCESSIBILITY_BIT
init|=
literal|10
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|ASSEMBLE_DOCUMENT_BIT
init|=
literal|11
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|DEGRADED_PRINT_BIT
init|=
literal|12
decl_stmt|;
specifier|private
name|int
name|bytes
init|=
name|DEFAULT_PERMISSIONS
decl_stmt|;
specifier|private
name|boolean
name|readOnly
init|=
literal|false
decl_stmt|;
comment|/**      * Create a new access permission object.      * By default, all permissions are granted.      */
specifier|public
name|AccessPermission
parameter_list|()
block|{
name|bytes
operator|=
name|DEFAULT_PERMISSIONS
expr_stmt|;
block|}
comment|/**      * Create a new access permission object from a byte array.      * Bytes are ordered most significant byte first.      *      * @param b the bytes as defined in PDF specs      */
specifier|public
name|AccessPermission
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
block|{
name|bytes
operator|=
literal|0
expr_stmt|;
name|bytes
operator||=
name|b
index|[
literal|0
index|]
operator|&
literal|0xFF
expr_stmt|;
name|bytes
operator|<<=
literal|8
expr_stmt|;
name|bytes
operator||=
name|b
index|[
literal|1
index|]
operator|&
literal|0xFF
expr_stmt|;
name|bytes
operator|<<=
literal|8
expr_stmt|;
name|bytes
operator||=
name|b
index|[
literal|2
index|]
operator|&
literal|0xFF
expr_stmt|;
name|bytes
operator|<<=
literal|8
expr_stmt|;
name|bytes
operator||=
name|b
index|[
literal|3
index|]
operator|&
literal|0xFF
expr_stmt|;
block|}
comment|/**      * Creates a new access permission object from a single integer.      *      * @param permissions The permission bits.      */
specifier|public
name|AccessPermission
parameter_list|(
name|int
name|permissions
parameter_list|)
block|{
name|bytes
operator|=
name|permissions
expr_stmt|;
block|}
specifier|private
name|boolean
name|isPermissionBitOn
parameter_list|(
name|int
name|bit
parameter_list|)
block|{
return|return
operator|(
name|bytes
operator|&
operator|(
literal|1
operator|<<
operator|(
name|bit
operator|-
literal|1
operator|)
operator|)
operator|)
operator|!=
literal|0
return|;
block|}
specifier|private
name|boolean
name|setPermissionBit
parameter_list|(
name|int
name|bit
parameter_list|,
name|boolean
name|value
parameter_list|)
block|{
name|int
name|permissions
init|=
name|bytes
decl_stmt|;
if|if
condition|(
name|value
condition|)
block|{
name|permissions
operator|=
name|permissions
operator||
operator|(
literal|1
operator|<<
operator|(
name|bit
operator|-
literal|1
operator|)
operator|)
expr_stmt|;
block|}
else|else
block|{
name|permissions
operator|=
name|permissions
operator|&
operator|(
literal|0xFFFFFFFF
operator|^
operator|(
literal|1
operator|<<
operator|(
name|bit
operator|-
literal|1
operator|)
operator|)
operator|)
expr_stmt|;
block|}
name|bytes
operator|=
name|permissions
expr_stmt|;
return|return
operator|(
name|bytes
operator|&
operator|(
literal|1
operator|<<
operator|(
name|bit
operator|-
literal|1
operator|)
operator|)
operator|)
operator|!=
literal|0
return|;
block|}
comment|/**      * This will tell if the access permission corresponds to owner      * access permission (no restriction).      *      * @return true if the access permission does not restrict the use of the document      */
specifier|public
name|boolean
name|isOwnerPermission
parameter_list|()
block|{
return|return
operator|(
name|this
operator|.
name|canAssembleDocument
argument_list|()
operator|&&
name|this
operator|.
name|canExtractContent
argument_list|()
operator|&&
name|this
operator|.
name|canExtractForAccessibility
argument_list|()
operator|&&
name|this
operator|.
name|canFillInForm
argument_list|()
operator|&&
name|this
operator|.
name|canModify
argument_list|()
operator|&&
name|this
operator|.
name|canModifyAnnotations
argument_list|()
operator|&&
name|this
operator|.
name|canPrint
argument_list|()
operator|&&
name|this
operator|.
name|canPrintDegraded
argument_list|()
operator|)
return|;
block|}
comment|/**      * returns an access permission object for a document owner.      *      * @return A standard owner access permission set.      */
specifier|public
specifier|static
name|AccessPermission
name|getOwnerAccessPermission
parameter_list|()
block|{
name|AccessPermission
name|ret
init|=
operator|new
name|AccessPermission
argument_list|()
decl_stmt|;
name|ret
operator|.
name|setCanAssembleDocument
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ret
operator|.
name|setCanExtractContent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ret
operator|.
name|setCanExtractForAccessibility
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ret
operator|.
name|setCanFillInForm
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ret
operator|.
name|setCanModify
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ret
operator|.
name|setCanModifyAnnotations
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ret
operator|.
name|setCanPrint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ret
operator|.
name|setCanPrintDegraded
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
comment|/**      * This returns an integer representing the access permissions.      * This integer can be used for public key encryption. This format      * is not documented in the PDF specifications but is necessary for compatibility      * with Adobe Acrobat and Adobe Reader.      *      * @return the integer representing access permissions      */
specifier|public
name|int
name|getPermissionBytesForPublicKey
parameter_list|()
block|{
name|setPermissionBit
argument_list|(
literal|1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|setPermissionBit
argument_list|(
literal|7
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|setPermissionBit
argument_list|(
literal|8
argument_list|,
literal|false
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|13
init|;
name|i
operator|<=
literal|32
condition|;
name|i
operator|++
control|)
block|{
name|setPermissionBit
argument_list|(
name|i
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|bytes
return|;
block|}
comment|/**      * The returns an integer representing the access permissions.      * This integer can be used for standard PDF encryption as specified      * in the PDF specifications.      *      * @return the integer representing the access permissions      */
specifier|public
name|int
name|getPermissionBytes
parameter_list|()
block|{
return|return
name|bytes
return|;
block|}
comment|/**      * This will tell if the user can print.      *      * @return true If supplied with the user password they are allowed to print.      */
specifier|public
name|boolean
name|canPrint
parameter_list|()
block|{
return|return
name|isPermissionBitOn
argument_list|(
name|PRINT_BIT
argument_list|)
return|;
block|}
comment|/**      * Set if the user can print.      * This method will have no effect if the object is in read only mode      *      * @param allowPrinting A boolean determining if the user can print.      */
specifier|public
name|void
name|setCanPrint
parameter_list|(
name|boolean
name|allowPrinting
parameter_list|)
block|{
if|if
condition|(
operator|!
name|readOnly
condition|)
block|{
name|setPermissionBit
argument_list|(
name|PRINT_BIT
argument_list|,
name|allowPrinting
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will tell if the user can modify contents of the document.      *      * @return true If supplied with the user password they are allowed to modify the document      */
specifier|public
name|boolean
name|canModify
parameter_list|()
block|{
return|return
name|isPermissionBitOn
argument_list|(
name|MODIFICATION_BIT
argument_list|)
return|;
block|}
comment|/**      * Set if the user can modify the document.      * This method will have no effect if the object is in read only mode      *      * @param allowModifications A boolean determining if the user can modify the document.      */
specifier|public
name|void
name|setCanModify
parameter_list|(
name|boolean
name|allowModifications
parameter_list|)
block|{
if|if
condition|(
operator|!
name|readOnly
condition|)
block|{
name|setPermissionBit
argument_list|(
name|MODIFICATION_BIT
argument_list|,
name|allowModifications
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will tell if the user can extract text and images from the PDF document.      *      * @return true If supplied with the user password they are allowed to extract content      *              from the PDF document      */
specifier|public
name|boolean
name|canExtractContent
parameter_list|()
block|{
return|return
name|isPermissionBitOn
argument_list|(
name|EXTRACT_BIT
argument_list|)
return|;
block|}
comment|/**      * Set if the user can extract content from the document.      * This method will have no effect if the object is in read only mode      *      * @param allowExtraction A boolean determining if the user can extract content      *                        from the document.      */
specifier|public
name|void
name|setCanExtractContent
parameter_list|(
name|boolean
name|allowExtraction
parameter_list|)
block|{
if|if
condition|(
operator|!
name|readOnly
condition|)
block|{
name|setPermissionBit
argument_list|(
name|EXTRACT_BIT
argument_list|,
name|allowExtraction
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will tell if the user can add/modify text annotations, fill in interactive forms fields.      *      * @return true If supplied with the user password they are allowed to modify annotations.      */
specifier|public
name|boolean
name|canModifyAnnotations
parameter_list|()
block|{
return|return
name|isPermissionBitOn
argument_list|(
name|MODIFY_ANNOTATIONS_BIT
argument_list|)
return|;
block|}
comment|/**      * Set if the user can modify annotations.      * This method will have no effect if the object is in read only mode      *      * @param allowAnnotationModification A boolean determining if the user can modify annotations.      */
specifier|public
name|void
name|setCanModifyAnnotations
parameter_list|(
name|boolean
name|allowAnnotationModification
parameter_list|)
block|{
if|if
condition|(
operator|!
name|readOnly
condition|)
block|{
name|setPermissionBit
argument_list|(
name|MODIFY_ANNOTATIONS_BIT
argument_list|,
name|allowAnnotationModification
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will tell if the user can fill in interactive forms.      *      * @return true If supplied with the user password they are allowed to fill in form fields.      */
specifier|public
name|boolean
name|canFillInForm
parameter_list|()
block|{
return|return
name|isPermissionBitOn
argument_list|(
name|FILL_IN_FORM_BIT
argument_list|)
return|;
block|}
comment|/**      * Set if the user can fill in interactive forms.      * This method will have no effect if the object is in read only mode      *      * @param allowFillingInForm A boolean determining if the user can fill in interactive forms.      */
specifier|public
name|void
name|setCanFillInForm
parameter_list|(
name|boolean
name|allowFillingInForm
parameter_list|)
block|{
if|if
condition|(
operator|!
name|readOnly
condition|)
block|{
name|setPermissionBit
argument_list|(
name|FILL_IN_FORM_BIT
argument_list|,
name|allowFillingInForm
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will tell if the user can extract text and images from the PDF document      * for accessibility purposes.      *      * @return true If supplied with the user password they are allowed to extract content      *              from the PDF document      */
specifier|public
name|boolean
name|canExtractForAccessibility
parameter_list|()
block|{
return|return
name|isPermissionBitOn
argument_list|(
name|EXTRACT_FOR_ACCESSIBILITY_BIT
argument_list|)
return|;
block|}
comment|/**      * Set if the user can extract content from the document for accessibility purposes.      * This method will have no effect if the object is in read only mode      *      * @param allowExtraction A boolean determining if the user can extract content      *                        from the document.      */
specifier|public
name|void
name|setCanExtractForAccessibility
parameter_list|(
name|boolean
name|allowExtraction
parameter_list|)
block|{
if|if
condition|(
operator|!
name|readOnly
condition|)
block|{
name|setPermissionBit
argument_list|(
name|EXTRACT_FOR_ACCESSIBILITY_BIT
argument_list|,
name|allowExtraction
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will tell if the user can insert/rotate/delete pages.      *      * @return true If supplied with the user password they are allowed to extract content      *              from the PDF document      */
specifier|public
name|boolean
name|canAssembleDocument
parameter_list|()
block|{
return|return
name|isPermissionBitOn
argument_list|(
name|ASSEMBLE_DOCUMENT_BIT
argument_list|)
return|;
block|}
comment|/**      * Set if the user can insert/rotate/delete pages.      * This method will have no effect if the object is in read only mode      *      * @param allowAssembly A boolean determining if the user can assemble the document.      */
specifier|public
name|void
name|setCanAssembleDocument
parameter_list|(
name|boolean
name|allowAssembly
parameter_list|)
block|{
if|if
condition|(
operator|!
name|readOnly
condition|)
block|{
name|setPermissionBit
argument_list|(
name|ASSEMBLE_DOCUMENT_BIT
argument_list|,
name|allowAssembly
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will tell if the user can print the document in a degraded format.      *      * @return true If supplied with the user password they are allowed to print the      *              document in a degraded format.      */
specifier|public
name|boolean
name|canPrintDegraded
parameter_list|()
block|{
return|return
name|isPermissionBitOn
argument_list|(
name|DEGRADED_PRINT_BIT
argument_list|)
return|;
block|}
comment|/**      * Set if the user can print the document in a degraded format.      * This method will have no effect if the object is in read only mode      *      * @param allowAssembly A boolean determining if the user can print the      *        document in a degraded format.      */
specifier|public
name|void
name|setCanPrintDegraded
parameter_list|(
name|boolean
name|allowAssembly
parameter_list|)
block|{
if|if
condition|(
operator|!
name|readOnly
condition|)
block|{
name|setPermissionBit
argument_list|(
name|DEGRADED_PRINT_BIT
argument_list|,
name|allowAssembly
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Locks the access permission read only (ie, the setters will have no effects).      * After that, the object cannot be unlocked.      * This method is used for the currentAccessPermssion of a document to avoid      * users to change access permission.      */
specifier|public
name|void
name|setReadOnly
parameter_list|()
block|{
name|readOnly
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * This will tell if the object has been set as read only.      *      * @return true if the object is in read only mode.      */
specifier|public
name|boolean
name|isReadOnly
parameter_list|()
block|{
return|return
name|readOnly
return|;
block|}
block|}
end_class

end_unit

