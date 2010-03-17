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

begin_comment
comment|/**  *  * Represents the necessary information to decrypt a document protected by  * the standard security handler (password protection).  *  * This is only composed of a password.  *  * The following example shows how to decrypt a document protected with  * the standard security handler:  *  *<pre>  *  PDDocument doc = PDDocument.load(in);  *  StandardDecryptionMaterial dm = new StandardDecryptionMaterial("password");  *  doc.openProtection(dm);  *</pre>  *  * @author Benoit Guillon (benoit.guillon@snv.jussieu.fr)  *  * @version $Revision: 1.2 $  */
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
comment|/**      * Create a new standard decryption material with the given password.      *      * @param pwd The password.      */
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
comment|/**      * Returns the password.      *      * @return The password used to decrypt the document.      */
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

