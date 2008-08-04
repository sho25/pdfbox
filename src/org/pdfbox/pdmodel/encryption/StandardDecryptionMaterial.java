begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2006, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
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
comment|/**  *   * Represents the necessary information to decrypt a document protected by  * the standard security handler (password protection).  *   * This is only composed of a password.  *   * The following example shows how to decrypt a document protected with  * the standard security handler:  *   *<pre>  *  PDDocument doc = PDDocument.load(in);  *  StandardDecryptionMaterial dm = new StandardDecryptionMaterial("password");  *  doc.openProtection(dm);  *</pre>  *   * @author Benoit Guillon (benoit.guillon@snv.jussieu.fr)  *  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|StandardDecryptionMaterial
extends|extends
name|DecryptionMaterial
block|{
specifier|private
name|String
name|password
init|=
literal|null
decl_stmt|;
comment|/**      * Create a new standard decryption material with the given password.      *       * @param pwd The password.      */
specifier|public
name|StandardDecryptionMaterial
parameter_list|(
name|String
name|pwd
parameter_list|)
block|{
name|password
operator|=
name|pwd
expr_stmt|;
block|}
comment|/**      * Returns the password.      *       * @return The password used to decrypt the document.      */
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
block|}
end_class

end_unit

