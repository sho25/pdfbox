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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

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
name|FileOutputStream
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
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|PDPage
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
name|PDResources
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
name|StandardDecryptionMaterial
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
name|form
operator|.
name|PDFormXObject
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
name|image
operator|.
name|PDImageXObject
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
name|pdmodel
operator|.
name|graphics
operator|.
name|image
operator|.
name|TIFFInputStream
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
name|ImageIOUtil
import|;
end_import

begin_comment
comment|/**  * This will read a read pdf and extract images.<br/><br/>  *  * usage: java org.apache.pdfbox.ExtractImages&lt;pdffile&gt;&lt;password&gt; [imageprefix]  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.7 $  */
end_comment

begin_class
specifier|public
class|class
name|ExtractImages
block|{
specifier|private
name|int
name|imageCounter
init|=
literal|1
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
name|PREFIX
init|=
literal|"-prefix"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ADDKEY
init|=
literal|"-addkey"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NONSEQ
init|=
literal|"-nonSeq"
decl_stmt|;
specifier|private
name|ExtractImages
parameter_list|()
block|{     }
comment|/**      * This is the entry point for the application.      *      * @param args The command-line arguments.      *      * @throws Exception If there is an error decrypting the document.      */
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
name|Exception
block|{
name|ExtractImages
name|extractor
init|=
operator|new
name|ExtractImages
argument_list|()
decl_stmt|;
name|extractor
operator|.
name|extractImages
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|extractImages
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
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
literal|4
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|pdfFile
init|=
literal|null
decl_stmt|;
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|prefix
init|=
literal|null
decl_stmt|;
name|boolean
name|addKey
init|=
literal|false
decl_stmt|;
name|boolean
name|useNonSeqParser
init|=
literal|false
decl_stmt|;
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
name|args
index|[
name|i
index|]
operator|.
name|equals
argument_list|(
name|PREFIX
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
name|prefix
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
name|ADDKEY
argument_list|)
condition|)
block|{
name|addKey
operator|=
literal|true
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
name|NONSEQ
argument_list|)
condition|)
block|{
name|useNonSeqParser
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|pdfFile
operator|==
literal|null
condition|)
block|{
name|pdfFile
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|pdfFile
operator|==
literal|null
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|prefix
operator|==
literal|null
operator|&&
name|pdfFile
operator|.
name|length
argument_list|()
operator|>
literal|4
condition|)
block|{
name|prefix
operator|=
name|pdfFile
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pdfFile
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|useNonSeqParser
condition|)
block|{
name|document
operator|=
name|PDDocument
operator|.
name|loadNonSeq
argument_list|(
operator|new
name|File
argument_list|(
name|pdfFile
argument_list|)
argument_list|,
literal|null
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|pdfFile
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
name|StandardDecryptionMaterial
name|spm
init|=
operator|new
name|StandardDecryptionMaterial
argument_list|(
name|password
argument_list|)
decl_stmt|;
name|document
operator|.
name|openProtection
argument_list|(
name|spm
argument_list|)
expr_stmt|;
block|}
block|}
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
operator|!
name|ap
operator|.
name|canExtractContent
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: You do not have permission to extract images."
argument_list|)
throw|;
block|}
name|List
name|pages
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
argument_list|()
decl_stmt|;
name|Iterator
name|iter
init|=
name|pages
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PDPage
name|page
init|=
operator|(
name|PDPage
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|PDResources
name|resources
init|=
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
comment|// extract all XObjectImages which are part of the page resources
name|processResources
argument_list|(
name|resources
argument_list|,
name|prefix
argument_list|,
name|addKey
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
block|}
block|}
block|}
block|}
specifier|private
name|void
name|processResources
parameter_list|(
name|PDResources
name|resources
parameter_list|,
name|String
name|prefix
parameter_list|,
name|boolean
name|addKey
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
name|xobjects
init|=
name|resources
operator|.
name|getXObjects
argument_list|()
decl_stmt|;
if|if
condition|(
name|xobjects
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|String
argument_list|>
name|xobjectIter
init|=
name|xobjects
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|xobjectIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|key
init|=
name|xobjectIter
operator|.
name|next
argument_list|()
decl_stmt|;
name|PDXObject
name|xobject
init|=
name|xobjects
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
comment|// write the images
if|if
condition|(
name|xobject
operator|instanceof
name|PDImageXObject
condition|)
block|{
name|PDImageXObject
name|image
init|=
operator|(
name|PDImageXObject
operator|)
name|xobject
decl_stmt|;
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|addKey
condition|)
block|{
name|name
operator|=
name|getUniqueFileName
argument_list|(
name|prefix
operator|+
literal|"_"
operator|+
name|key
argument_list|,
name|image
operator|.
name|getSuffix
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|getUniqueFileName
argument_list|(
name|prefix
argument_list|,
name|image
operator|.
name|getSuffix
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Writing image:"
operator|+
name|name
argument_list|)
expr_stmt|;
name|write2file
argument_list|(
name|image
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|// maybe there are more images embedded in a form object
elseif|else
if|if
condition|(
name|xobject
operator|instanceof
name|PDFormXObject
condition|)
block|{
name|PDFormXObject
name|xObjectForm
init|=
operator|(
name|PDFormXObject
operator|)
name|xobject
decl_stmt|;
name|PDResources
name|formResources
init|=
name|xObjectForm
operator|.
name|getResources
argument_list|()
decl_stmt|;
name|processResources
argument_list|(
name|formResources
argument_list|,
name|prefix
argument_list|,
name|addKey
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Writes the image to a file with the filename + an appropriate suffix, like "Image.jpg".      * The suffix is automatically set by the      * @param filename the filename      * @throws IOException When somethings wrong with the corresponding file.      */
specifier|private
name|void
name|write2file
parameter_list|(
name|PDImageXObject
name|xobj
parameter_list|,
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
block|{
name|FileOutputStream
name|out
init|=
literal|null
decl_stmt|;
try|try
block|{
name|out
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|filename
operator|+
literal|"."
operator|+
name|xobj
operator|.
name|getSuffix
argument_list|()
argument_list|)
expr_stmt|;
name|BufferedImage
name|image
init|=
name|xobj
operator|.
name|getImage
argument_list|()
decl_stmt|;
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
literal|"tiff"
operator|.
name|equals
argument_list|(
name|xobj
operator|.
name|getSuffix
argument_list|()
argument_list|)
condition|)
block|{
name|TIFFInputStream
operator|.
name|writeToOutputStream
argument_list|(
name|xobj
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image
argument_list|,
name|xobj
operator|.
name|getSuffix
argument_list|()
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|getUniqueFileName
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|suffix
parameter_list|)
block|{
name|String
name|uniqueName
init|=
literal|null
decl_stmt|;
name|File
name|f
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|f
operator|==
literal|null
operator|||
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
name|uniqueName
operator|=
name|prefix
operator|+
literal|"-"
operator|+
name|imageCounter
expr_stmt|;
name|f
operator|=
operator|new
name|File
argument_list|(
name|uniqueName
operator|+
literal|"."
operator|+
name|suffix
argument_list|)
expr_stmt|;
name|imageCounter
operator|++
expr_stmt|;
block|}
return|return
name|uniqueName
return|;
block|}
comment|/**      * This will print the usage requirements and exit.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Usage: java org.apache.pdfbox.ExtractImages [OPTIONS]<PDF file>\n"
operator|+
literal|"  -password<password>        Password to decrypt document\n"
operator|+
literal|"  -prefix<image-prefix>      Image prefix(default to pdf name)\n"
operator|+
literal|"  -addkey                      add the internal image key to the file name\n"
operator|+
literal|"  -nonSeq                      Enables the new non-sequential parser\n"
operator|+
literal|"<PDF file>                   The PDF document to use\n"
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

