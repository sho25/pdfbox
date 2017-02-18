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
name|tools
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|io
operator|.
name|IOUtils
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
name|PDDocument
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
name|encryption
operator|.
name|AccessPermission
import|;
end_import

begin_comment
comment|/**  * This will read a document from the filesystem, decrypt it and and then write  * the result to the filesystem.  *  * @author  Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Decrypt
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ALIAS
init|=
literal|"-alias"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"-password"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEYSTORE
init|=
literal|"-keyStore"
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|String
name|infile
decl_stmt|;
specifier|private
name|String
name|outfile
decl_stmt|;
specifier|private
name|String
name|alias
decl_stmt|;
specifier|private
name|String
name|keyStore
decl_stmt|;
specifier|private
name|Decrypt
parameter_list|()
block|{     }
comment|/**      * This is the entry point for the application.      *      * @param args The command-line arguments.      *      * @throws IOException If there is an error decrypting the document.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
block|{
comment|// suppress the Dock icon on OS X
name|System
operator|.
name|setProperty
argument_list|(
literal|"apple.awt.UIElement"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|Decrypt
name|decrypt
init|=
operator|new
name|Decrypt
argument_list|()
decl_stmt|;
name|decrypt
operator|.
name|parseCommandLineArgs
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|decrypt
operator|.
name|decrypt
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|parseCommandLineArgs
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
if|if
condition|(
name|args
operator|.
name|length
argument_list|<
literal|1
operator|||
name|args
operator|.
name|length
argument_list|>
literal|8
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|ALIAS
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|alias
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|KEYSTORE
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|keyStore
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|PASSWORD
argument_list|)
condition|)
block|{
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|password
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|infile
operator|==
literal|null
condition|)
block|{
name|infile
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|outfile
operator|==
literal|null
condition|)
block|{
name|outfile
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
else|else
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|infile
operator|==
literal|null
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|outfile
operator|==
literal|null
condition|)
block|{
name|outfile
operator|=
name|infile
expr_stmt|;
block|}
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|password
operator|=
literal|""
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|decrypt
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
name|InputStream
name|keyStoreStream
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|keyStore
operator|!=
literal|null
condition|)
block|{
name|keyStoreStream
operator|=
operator|new
name|FileInputStream
argument_list|(
name|keyStore
argument_list|)
expr_stmt|;
block|}
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|infile
argument_list|)
argument_list|,
name|password
argument_list|,
name|keyStoreStream
argument_list|,
name|alias
argument_list|)
expr_stmt|;
if|if
condition|(
name|document
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
name|AccessPermission
name|ap
init|=
name|document
operator|.
name|getCurrentAccessPermission
argument_list|()
decl_stmt|;
if|if
condition|(
name|ap
operator|.
name|isOwnerPermission
argument_list|()
condition|)
block|{
name|document
operator|.
name|setAllSecurityToBeRemoved
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
name|outfile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: You are only allowed to decrypt a document with the owner password."
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: Document is not encrypted."
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|keyStoreStream
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will print a usage message.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|String
name|message
init|=
literal|"Usage: java -jar pdfbox-app-x.y.z.jar Decrypt [options]<inputfile> [outputfile]\n"
operator|+
literal|"\nOptions:\n"
operator|+
literal|"  -alias    : The alias of the key in the certificate file (mandatory if several keys are available\n"
operator|+
literal|"  -password : The password to open the certificate and extract the private key from it.\n"
operator|+
literal|"  -keyStore : The KeyStore that holds the certificate."
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

