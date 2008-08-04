begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2005, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
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

