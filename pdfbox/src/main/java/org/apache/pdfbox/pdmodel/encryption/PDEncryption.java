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
name|encryption
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
name|COSArray
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
name|COSBase
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
name|COSBoolean
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
name|cos
operator|.
name|COSString
import|;
end_import

begin_comment
comment|/**  * This class is a specialized view of the encryption dictionary of a PDF document.  * It contains a low level dictionary (COSDictionary) and provides the methods to  * manage its fields.  *  * The available fields are the ones who are involved by standard security handler  * and public key security handler.  *  * @author Ben Litchfield  * @author Benoit Guillon  */
end_comment

begin_class
specifier|public
class|class
name|PDEncryption
block|{
comment|/**      * See PDF Reference 1.4 Table 3.13.      */
specifier|public
specifier|static
specifier|final
name|int
name|VERSION0_UNDOCUMENTED_UNSUPPORTED
init|=
literal|0
decl_stmt|;
comment|/**      * See PDF Reference 1.4 Table 3.13.      */
specifier|public
specifier|static
specifier|final
name|int
name|VERSION1_40_BIT_ALGORITHM
init|=
literal|1
decl_stmt|;
comment|/**      * See PDF Reference 1.4 Table 3.13.      */
specifier|public
specifier|static
specifier|final
name|int
name|VERSION2_VARIABLE_LENGTH_ALGORITHM
init|=
literal|2
decl_stmt|;
comment|/**      * See PDF Reference 1.4 Table 3.13.      */
specifier|public
specifier|static
specifier|final
name|int
name|VERSION3_UNPUBLISHED_ALGORITHM
init|=
literal|3
decl_stmt|;
comment|/**      * See PDF Reference 1.4 Table 3.13.      */
specifier|public
specifier|static
specifier|final
name|int
name|VERSION4_SECURITY_HANDLER
init|=
literal|4
decl_stmt|;
comment|/**      * The default security handler.      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_NAME
init|=
literal|"Standard"
decl_stmt|;
comment|/**      * The default length for the encryption key.      */
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_LENGTH
init|=
literal|40
decl_stmt|;
comment|/**      * The default version, according to the PDF Reference.      */
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_VERSION
init|=
name|VERSION0_UNDOCUMENTED_UNSUPPORTED
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
specifier|private
name|SecurityHandler
name|securityHandler
decl_stmt|;
comment|/**      * creates a new empty encryption dictionary.      */
specifier|public
name|PDEncryption
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * creates a new encryption dictionary from the low level dictionary provided.      * @param dictionary a COS encryption dictionary      */
specifier|public
name|PDEncryption
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|dictionary
operator|=
name|dictionary
expr_stmt|;
name|securityHandler
operator|=
name|SecurityHandlerFactory
operator|.
name|INSTANCE
operator|.
name|newSecurityHandlerForFilter
argument_list|(
name|getFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the security handler specified in the dictionary's Filter entry.      * @return a security handler instance      * @throws IOException if there is no security handler available which matches the Filter      */
specifier|public
name|SecurityHandler
name|getSecurityHandler
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|securityHandler
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"No security handler for filter "
operator|+
name|getFilter
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|securityHandler
return|;
block|}
comment|/**      * Sets the security handler used in this encryption dictionary      * @param securityHandler new security handler      */
specifier|public
name|void
name|setSecurityHandler
parameter_list|(
name|SecurityHandler
name|securityHandler
parameter_list|)
block|{
name|this
operator|.
name|securityHandler
operator|=
name|securityHandler
expr_stmt|;
comment|// TODO set Filter (currently this is done by the security handlers)
block|}
comment|/**      * Returns true if the security handler specified in the dictionary's Filter is available.      * @return true if the security handler is available      */
specifier|public
name|boolean
name|hasSecurityHandler
parameter_list|()
block|{
return|return
name|securityHandler
operator|==
literal|null
return|;
block|}
comment|/**      * This will get the dictionary associated with this encryption dictionary.      *      * @return The COS dictionary that this object wraps.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * Sets the filter entry of the encryption dictionary.      *      * @param filter The filter name.      */
specifier|public
name|void
name|setFilter
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
name|filter
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the name of the filter.      *      * @return The filter name contained in this encryption dictionary.      */
specifier|public
specifier|final
name|String
name|getFilter
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|)
return|;
block|}
comment|/**      * Get the name of the subfilter.      *      * @return The subfilter name contained in this encryption dictionary.      */
specifier|public
name|String
name|getSubFilter
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUB_FILTER
argument_list|)
return|;
block|}
comment|/**      * Set the subfilter entry of the encryption dictionary.      *      * @param subfilter The value of the subfilter field.      */
specifier|public
name|void
name|setSubFilter
parameter_list|(
name|String
name|subfilter
parameter_list|)
block|{
name|dictionary
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUB_FILTER
argument_list|,
name|subfilter
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the V entry of the encryption dictionary.<br><br>      * See PDF Reference 1.4 Table 3.13.<br><br>      *<b>Note: This value is used to decrypt the pdf document.  If you change this when      * the document is encrypted then decryption will fail!.</b>      *      * @param version The new encryption version.      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|int
name|version
parameter_list|)
block|{
name|dictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the V entry of the encryption dictionary.<br><br>      * See PDF Reference 1.4 Table 3.13.      *      * @return The encryption version to use.      */
specifier|public
name|int
name|getVersion
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the number of bits to use for the encryption algorithm.      *      * @param length The new key length.      */
specifier|public
name|void
name|setLength
parameter_list|(
name|int
name|length
parameter_list|)
block|{
name|dictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the Length entry of the encryption dictionary.<br><br>      * The length in<b>bits</b> for the encryption algorithm.  This will return a multiple of 8.      *      * @return The length in bits for the encryption algorithm      */
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
literal|40
argument_list|)
return|;
block|}
comment|/**      * This will set the R entry of the encryption dictionary.<br><br>      * See PDF Reference 1.4 Table 3.14.<br><br>      *      *<b>Note: This value is used to decrypt the pdf document.  If you change this when      * the document is encrypted then decryption will fail!.</b>      *      * @param revision The new encryption version.      */
specifier|public
name|void
name|setRevision
parameter_list|(
name|int
name|revision
parameter_list|)
block|{
name|dictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|R
argument_list|,
name|revision
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the R entry of the encryption dictionary.<br><br>      * See PDF Reference 1.4 Table 3.14.      *      * @return The encryption revision to use.      */
specifier|public
name|int
name|getRevision
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|R
argument_list|,
name|DEFAULT_VERSION
argument_list|)
return|;
block|}
comment|/**      * This will set the O entry in the standard encryption dictionary.      *      * @param o A 32 byte array or null if there is no owner key.      *      * @throws IOException If there is an error setting the data.      */
specifier|public
name|void
name|setOwnerKey
parameter_list|(
name|byte
index|[]
name|o
parameter_list|)
throws|throws
name|IOException
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|O
argument_list|,
operator|new
name|COSString
argument_list|(
name|o
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the O entry in the standard encryption dictionary.      *      * @return A 32 byte array or null if there is no owner key.      *      * @throws IOException If there is an error accessing the data.      */
specifier|public
name|byte
index|[]
name|getOwnerKey
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|o
init|=
literal|null
decl_stmt|;
name|COSString
name|owner
init|=
operator|(
name|COSString
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|O
argument_list|)
decl_stmt|;
if|if
condition|(
name|owner
operator|!=
literal|null
condition|)
block|{
name|o
operator|=
name|owner
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
return|return
name|o
return|;
block|}
comment|/**      * This will set the U entry in the standard encryption dictionary.      *      * @param u A 32 byte array.      *      * @throws IOException If there is an error setting the data.      */
specifier|public
name|void
name|setUserKey
parameter_list|(
name|byte
index|[]
name|u
parameter_list|)
throws|throws
name|IOException
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|U
argument_list|,
operator|new
name|COSString
argument_list|(
name|u
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the U entry in the standard encryption dictionary.      *      * @return A 32 byte array or null if there is no user key.      *      * @throws IOException If there is an error accessing the data.      */
specifier|public
name|byte
index|[]
name|getUserKey
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|u
init|=
literal|null
decl_stmt|;
name|COSString
name|user
init|=
operator|(
name|COSString
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|U
argument_list|)
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
condition|)
block|{
name|u
operator|=
name|user
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
return|return
name|u
return|;
block|}
comment|/**      * This will set the OE entry in the standard encryption dictionary.      *      * @param oe A 32 byte array or null if there is no owner encryption key.      *      * @throws IOException If there is an error setting the data.      */
specifier|public
name|void
name|setOwnerEncryptionKey
parameter_list|(
name|byte
index|[]
name|oe
parameter_list|)
throws|throws
name|IOException
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OE
argument_list|,
operator|new
name|COSString
argument_list|(
name|oe
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the OE entry in the standard encryption dictionary.      *      * @return A 32 byte array or null if there is no owner encryption key.      *      * @throws IOException If there is an error accessing the data.      */
specifier|public
name|byte
index|[]
name|getOwnerEncryptionKey
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|oe
init|=
literal|null
decl_stmt|;
name|COSString
name|ownerEncryptionKey
init|=
operator|(
name|COSString
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OE
argument_list|)
decl_stmt|;
if|if
condition|(
name|ownerEncryptionKey
operator|!=
literal|null
condition|)
block|{
name|oe
operator|=
name|ownerEncryptionKey
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
return|return
name|oe
return|;
block|}
comment|/**      * This will set the UE entry in the standard encryption dictionary.      *      * @param ue A 32 byte array or null if there is no user encryption key.      *      * @throws IOException If there is an error setting the data.      */
specifier|public
name|void
name|setUserEncryptionKey
parameter_list|(
name|byte
index|[]
name|ue
parameter_list|)
throws|throws
name|IOException
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|UE
argument_list|,
operator|new
name|COSString
argument_list|(
name|ue
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the UE entry in the standard encryption dictionary.      *      * @return A 32 byte array or null if there is no user encryption key.      *      * @throws IOException If there is an error accessing the data.      */
specifier|public
name|byte
index|[]
name|getUserEncryptionKey
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|ue
init|=
literal|null
decl_stmt|;
name|COSString
name|userEncryptionKey
init|=
operator|(
name|COSString
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|UE
argument_list|)
decl_stmt|;
if|if
condition|(
name|userEncryptionKey
operator|!=
literal|null
condition|)
block|{
name|ue
operator|=
name|userEncryptionKey
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
return|return
name|ue
return|;
block|}
comment|/**      * This will set the permissions bit mask.      *      * @param permissions The new permissions bit mask      */
specifier|public
name|void
name|setPermissions
parameter_list|(
name|int
name|permissions
parameter_list|)
block|{
name|dictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|P
argument_list|,
name|permissions
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the permissions bit mask.      *      * @return The permissions bit mask.      */
specifier|public
name|int
name|getPermissions
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|P
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Will get the EncryptMetaData dictionary info.      *       * @return true if EncryptMetaData is explicitly set to false (the default is true)      */
specifier|public
name|boolean
name|isEncryptMetaData
parameter_list|()
block|{
comment|// default is true (see 7.6.3.2 Standard Encryption Dictionary PDF 32000-1:2008)
name|boolean
name|encryptMetaData
init|=
literal|true
decl_stmt|;
name|COSBase
name|value
init|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ENCRYPT_META_DATA
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|COSBoolean
condition|)
block|{
name|encryptMetaData
operator|=
operator|(
operator|(
name|COSBoolean
operator|)
name|value
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
return|return
name|encryptMetaData
return|;
block|}
comment|/**      * This will set the Recipients field of the dictionary. This field contains an array      * of string.      * @param recipients the array of bytes arrays to put in the Recipients field.      * @throws IOException If there is an error setting the data.      */
specifier|public
name|void
name|setRecipients
parameter_list|(
name|byte
index|[]
index|[]
name|recipients
parameter_list|)
throws|throws
name|IOException
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
index|[]
name|recipient
range|:
name|recipients
control|)
block|{
name|COSString
name|recip
init|=
operator|new
name|COSString
argument_list|(
name|recipient
argument_list|)
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|recip
argument_list|)
expr_stmt|;
block|}
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RECIPIENTS
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the number of recipients contained in the Recipients field of the dictionary.      *      * @return the number of recipients contained in the Recipients field.      */
specifier|public
name|int
name|getRecipientsLength
parameter_list|()
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|RECIPIENTS
argument_list|)
decl_stmt|;
return|return
name|array
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * returns the COSString contained in the Recipients field at position i.      *      * @param i the position in the Recipients field array.      *      * @return a COSString object containing information about the recipient number i.      */
specifier|public
name|COSString
name|getRecipientStringAt
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|RECIPIENTS
argument_list|)
decl_stmt|;
return|return
operator|(
name|COSString
operator|)
name|array
operator|.
name|get
argument_list|(
name|i
argument_list|)
return|;
block|}
comment|/**      * Returns the standard crypt filter.      *       * @return the standard crypt filter if available.      */
specifier|public
name|PDCryptFilterDictionary
name|getStdCryptFilterDictionary
parameter_list|()
block|{
return|return
name|getCryptFilterDictionary
argument_list|(
name|COSName
operator|.
name|STD_CF
argument_list|)
return|;
block|}
comment|/**      * Returns the crypt filter with the given name.      *       * @param cryptFilterName the name of the crypt filter      *       * @return the crypt filter with the given name if available      */
specifier|public
name|PDCryptFilterDictionary
name|getCryptFilterDictionary
parameter_list|(
name|COSName
name|cryptFilterName
parameter_list|)
block|{
name|COSDictionary
name|cryptFilterDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CF
argument_list|)
decl_stmt|;
if|if
condition|(
name|cryptFilterDictionary
operator|!=
literal|null
condition|)
block|{
name|COSDictionary
name|stdCryptFilterDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|cryptFilterDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|cryptFilterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|stdCryptFilterDictionary
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDCryptFilterDictionary
argument_list|(
name|stdCryptFilterDictionary
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the crypt filter with the given name.      *       * @param cryptFilterName the name of the crypt filter      * @param cryptFilterDictionary the crypt filter to set      */
specifier|public
name|void
name|setCryptFilterDictionary
parameter_list|(
name|COSName
name|cryptFilterName
parameter_list|,
name|PDCryptFilterDictionary
name|cryptFilterDictionary
parameter_list|)
block|{
name|COSDictionary
name|cfDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CF
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfDictionary
operator|==
literal|null
condition|)
block|{
name|cfDictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CF
argument_list|,
name|cfDictionary
argument_list|)
expr_stmt|;
block|}
name|cfDictionary
operator|.
name|setItem
argument_list|(
name|cryptFilterName
argument_list|,
name|cryptFilterDictionary
operator|.
name|getCOSDictionary
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the standard crypt filter.      *       * @param cryptFilterDictionary the standard crypt filter to set      */
specifier|public
name|void
name|setStdCryptFilterDictionary
parameter_list|(
name|PDCryptFilterDictionary
name|cryptFilterDictionary
parameter_list|)
block|{
name|setCryptFilterDictionary
argument_list|(
name|COSName
operator|.
name|STD_CF
argument_list|,
name|cryptFilterDictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the name of the filter which is used for de/encrypting streams.      * Default value is "Identity".      *       * @return the name of the filter      */
specifier|public
name|COSName
name|getStreamFilterName
parameter_list|()
block|{
name|COSName
name|stmF
init|=
operator|(
name|COSName
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|STM_F
argument_list|)
decl_stmt|;
if|if
condition|(
name|stmF
operator|==
literal|null
condition|)
block|{
name|stmF
operator|=
name|COSName
operator|.
name|IDENTITY
expr_stmt|;
block|}
return|return
name|stmF
return|;
block|}
comment|/**      * Sets the name of the filter which is used for de/encrypting streams.      *       * @param streamFilterName the name of the filter      */
specifier|public
name|void
name|setStreamFilterName
parameter_list|(
name|COSName
name|streamFilterName
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|STM_F
argument_list|,
name|streamFilterName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the name of the filter which is used for de/encrypting strings.      * Default value is "Identity".      *       * @return the name of the filter      */
specifier|public
name|COSName
name|getStringFilterName
parameter_list|()
block|{
name|COSName
name|strF
init|=
operator|(
name|COSName
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|STR_F
argument_list|)
decl_stmt|;
if|if
condition|(
name|strF
operator|==
literal|null
condition|)
block|{
name|strF
operator|=
name|COSName
operator|.
name|IDENTITY
expr_stmt|;
block|}
return|return
name|strF
return|;
block|}
comment|/**      * Sets the name of the filter which is used for de/encrypting strings.      *       * @param stringFilterName the name of the filter      */
specifier|public
name|void
name|setStringFilterName
parameter_list|(
name|COSName
name|stringFilterName
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|STR_F
argument_list|,
name|stringFilterName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the Perms entry in the encryption dictionary.      *      * @param perms A 16 byte array.      *      * @throws IOException If there is an error setting the data.      */
specifier|public
name|void
name|setPerms
parameter_list|(
name|byte
index|[]
name|perms
parameter_list|)
throws|throws
name|IOException
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PERMS
argument_list|,
operator|new
name|COSString
argument_list|(
name|perms
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the Perms entry in the encryption dictionary.      *      * @return A 16 byte array or null if there is no Perms entry.      *      * @throws IOException If there is an error accessing the data.      */
specifier|public
name|byte
index|[]
name|getPerms
parameter_list|()
throws|throws
name|IOException
block|{
name|byte
index|[]
name|perms
init|=
literal|null
decl_stmt|;
name|COSString
name|permsCosString
init|=
operator|(
name|COSString
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PERMS
argument_list|)
decl_stmt|;
if|if
condition|(
name|permsCosString
operator|!=
literal|null
condition|)
block|{
name|perms
operator|=
name|permsCosString
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
return|return
name|perms
return|;
block|}
comment|/**      * remove CF, StmF, and StrF entries. This is to be called if V is not 4 or 5.      */
specifier|public
name|void
name|removeV45filters
parameter_list|()
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CF
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|STM_F
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|STR_F
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

