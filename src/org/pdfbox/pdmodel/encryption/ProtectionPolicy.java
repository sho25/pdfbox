begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * This class represents the protection policy to apply to a document.  *   * Objects implementing this abstract class can be passed to the protect method of PDDocument  * to protect a document.  *   * @see org.pdfbox.pdmodel.PDDocument#protect(ProtectionPolicy)  *    * @author Benoit Guillon (benoit.guillon@snv.jussieu.fr)  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ProtectionPolicy
block|{
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_KEY_LENGTH
init|=
literal|40
decl_stmt|;
specifier|private
name|int
name|encryptionKeyLength
init|=
name|DEFAULT_KEY_LENGTH
decl_stmt|;
comment|/**      * set the length in (bits) of the secret key that will be       * used to encrypt document data.      * The default value is 40 bits, which provides a low security level      * but is compatible with old versions of Acrobat Reader.      *       * @param l the length in bits (must be 40 or 128)      */
specifier|public
name|void
name|setEncryptionKeyLength
parameter_list|(
name|int
name|l
parameter_list|)
block|{
if|if
condition|(
name|l
operator|!=
literal|40
operator|&&
name|l
operator|!=
literal|128
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Invalid key length '"
operator|+
name|l
operator|+
literal|"' value must be 40 or 128!"
argument_list|)
throw|;
block|}
name|encryptionKeyLength
operator|=
name|l
expr_stmt|;
block|}
comment|/**      * Get the length of the secrete key that will be used to encrypt      * document data.      *       * @return The length (in bits) of the encryption key.      */
specifier|public
name|int
name|getEncryptionKeyLength
parameter_list|()
block|{
return|return
name|encryptionKeyLength
return|;
block|}
block|}
end_class

end_unit

