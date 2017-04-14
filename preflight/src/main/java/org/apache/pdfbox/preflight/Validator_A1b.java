begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|FileReader
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|javax
operator|.
name|activation
operator|.
name|FileDataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
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
name|preflight
operator|.
name|ValidationResult
operator|.
name|ValidationError
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
name|preflight
operator|.
name|exception
operator|.
name|SyntaxValidationException
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
name|preflight
operator|.
name|parser
operator|.
name|PreflightParser
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
name|preflight
operator|.
name|parser
operator|.
name|XmlResultParser
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
name|Version
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * This class is a simple main class used to check the validity of a pdf file.  *   * Usage : java net.awl.edoc.pdfa.Validator&lt;file path&gt;  *   * @author gbailleul  *   */
end_comment

begin_class
specifier|public
class|class
name|Validator_A1b
block|{
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
throws|,
name|TransformerException
throws|,
name|ParserConfigurationException
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// is output xml ?
name|int
name|posFile
init|=
literal|0
decl_stmt|;
name|boolean
name|outputXml
init|=
literal|"xml"
operator|.
name|equals
argument_list|(
name|args
index|[
name|posFile
index|]
argument_list|)
decl_stmt|;
name|posFile
operator|+=
name|outputXml
condition|?
literal|1
else|:
literal|0
expr_stmt|;
comment|// list
name|boolean
name|isGroup
init|=
literal|"group"
operator|.
name|equals
argument_list|(
name|args
index|[
name|posFile
index|]
argument_list|)
decl_stmt|;
name|posFile
operator|+=
name|isGroup
condition|?
literal|1
else|:
literal|0
expr_stmt|;
comment|// list
name|boolean
name|isBatch
init|=
literal|"batch"
operator|.
name|equals
argument_list|(
name|args
index|[
name|posFile
index|]
argument_list|)
decl_stmt|;
name|posFile
operator|+=
name|isBatch
condition|?
literal|1
else|:
literal|0
expr_stmt|;
if|if
condition|(
name|isGroup
operator|||
name|isBatch
condition|)
block|{
comment|// prepare the list
name|List
argument_list|<
name|File
argument_list|>
name|ftp
init|=
name|listFiles
argument_list|(
name|args
index|[
name|posFile
index|]
argument_list|)
decl_stmt|;
name|int
name|status
init|=
literal|0
decl_stmt|;
if|if
condition|(
operator|!
name|outputXml
condition|)
block|{
comment|// simple list of files
for|for
control|(
name|File
name|file2
range|:
name|ftp
control|)
block|{
name|status
operator||=
name|runSimple
argument_list|(
name|file2
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|exit
argument_list|(
name|status
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Transformer
name|transformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
literal|"{http://xml.apache.org/xslt}indent-amount"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|XmlResultParser
name|xrp
init|=
operator|new
name|XmlResultParser
argument_list|()
decl_stmt|;
if|if
condition|(
name|isGroup
condition|)
block|{
name|Document
name|document
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|Element
name|root
init|=
name|document
operator|.
name|createElement
argument_list|(
literal|"preflights"
argument_list|)
decl_stmt|;
name|document
operator|.
name|appendChild
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|root
operator|.
name|setAttribute
argument_list|(
literal|"count"
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"%d"
argument_list|,
name|ftp
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|File
name|file
range|:
name|ftp
control|)
block|{
name|Element
name|result
init|=
name|xrp
operator|.
name|validate
argument_list|(
name|document
argument_list|,
operator|new
name|FileDataSource
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|root
operator|.
name|appendChild
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
name|transformer
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|document
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
name|posFile
index|]
operator|+
literal|".preflight.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// isBatch
for|for
control|(
name|File
name|file
range|:
name|ftp
control|)
block|{
name|Element
name|result
init|=
name|xrp
operator|.
name|validate
argument_list|(
operator|new
name|FileDataSource
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|Document
name|document
init|=
name|result
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|document
operator|.
name|appendChild
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|document
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
operator|new
name|File
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".preflight.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|outputXml
condition|)
block|{
comment|// simple validation
name|System
operator|.
name|exit
argument_list|(
name|runSimple
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
name|posFile
index|]
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// generate xml output
name|XmlResultParser
name|xrp
init|=
operator|new
name|XmlResultParser
argument_list|()
decl_stmt|;
name|Element
name|result
init|=
name|xrp
operator|.
name|validate
argument_list|(
operator|new
name|FileDataSource
argument_list|(
name|args
index|[
name|posFile
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|Document
name|document
init|=
name|result
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|document
operator|.
name|appendChild
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|Transformer
name|transformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
literal|"{http://xml.apache.org/xslt}indent-amount"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|document
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|System
operator|.
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|version
init|=
name|Version
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Usage : java org.apache.pdfbox.preflight.Validator_A1b [xml] [<mode>]<file path>"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" * xml : if set, generate xml output instead of text"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" *<mode> : if set,<file path> must be a file containing the PDF files to parse.<mode> can have 2 values:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"       batch : generate xml result files for each PDF file in the list"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"       group : generate one xml result file for all the PDF files in the list."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Version : "
operator|+
name|version
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|int
name|runSimple
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|ValidationResult
name|result
decl_stmt|;
name|PreflightParser
name|parser
init|=
operator|new
name|PreflightParser
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|PreflightDocument
name|document
init|=
name|parser
operator|.
name|getPreflightDocument
argument_list|()
decl_stmt|;
name|document
operator|.
name|validate
argument_list|()
expr_stmt|;
name|result
operator|=
name|document
operator|.
name|getResult
argument_list|()
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SyntaxValidationException
name|e
parameter_list|)
block|{
name|result
operator|=
name|e
operator|.
name|getResult
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"The file "
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|" is a valid PDF/A-1b file"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
return|return
literal|0
return|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"The file "
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|" is not valid, error(s) :"
argument_list|)
expr_stmt|;
for|for
control|(
name|ValidationError
name|error
range|:
name|result
operator|.
name|getErrorsList
argument_list|()
control|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
name|error
operator|.
name|getErrorCode
argument_list|()
operator|+
literal|" : "
operator|+
name|error
operator|.
name|getDetails
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|error
operator|.
name|getPageNumber
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" on page "
operator|+
operator|(
name|error
operator|.
name|getPageNumber
argument_list|()
operator|+
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
return|return
operator|-
literal|1
return|;
block|}
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|File
argument_list|>
name|listFiles
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|File
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|FileReader
name|fr
init|=
operator|new
name|FileReader
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|BufferedReader
name|buf
init|=
operator|new
name|BufferedReader
argument_list|(
name|fr
argument_list|)
decl_stmt|;
while|while
condition|(
name|buf
operator|.
name|ready
argument_list|()
condition|)
block|{
name|File
name|fn
init|=
operator|new
name|File
argument_list|(
name|buf
operator|.
name|readLine
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|fn
operator|.
name|exists
argument_list|()
condition|)
block|{
name|files
operator|.
name|add
argument_list|(
name|fn
argument_list|)
expr_stmt|;
block|}
comment|// else warn ?
block|}
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|buf
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|File
index|[]
name|fileList
init|=
name|f
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|fileList
operator|!=
literal|null
condition|)
block|{
name|files
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|fileList
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|files
return|;
block|}
block|}
end_class

end_unit

