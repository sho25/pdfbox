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
name|pdfparser
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|COSDocument
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
name|io
operator|.
name|RandomAccessBuffer
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
name|RandomAccessFile
import|;
end_import

begin_class
specifier|public
class|class
name|FDFParser
extends|extends
name|COSParser
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FDFParser
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructs parser for given file using memory buffer.      *       * @param filename the filename of the pdf to be parsed      *       * @throws IOException If something went wrong.      */
specifier|public
name|FDFParser
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
operator|new
name|File
argument_list|(
name|filename
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs parser for given file using given buffer for temporary      * storage.      *       * @param file the pdf to be parsed      *       * @throws IOException If something went wrong.      */
specifier|public
name|FDFParser
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
operator|new
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"r"
argument_list|)
argument_list|)
expr_stmt|;
name|fileLen
operator|=
name|file
operator|.
name|length
argument_list|()
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param input input stream representing the pdf.      * @throws IOException If something went wrong.      */
specifier|public
name|FDFParser
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
operator|new
name|RandomAccessBuffer
argument_list|(
name|input
argument_list|)
argument_list|)
expr_stmt|;
name|fileLen
operator|=
name|source
operator|.
name|length
argument_list|()
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|init
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|eofLookupRangeStr
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|SYSPROP_EOFLOOKUPRANGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|eofLookupRangeStr
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|setEOFLookupRange
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|eofLookupRangeStr
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"System property "
operator|+
name|SYSPROP_EOFLOOKUPRANGE
operator|+
literal|" does not contain an integer value, but: '"
operator|+
name|eofLookupRangeStr
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
name|document
operator|=
operator|new
name|COSDocument
argument_list|()
expr_stmt|;
block|}
comment|/**      * The initial parse will first parse only the trailer, the xrefstart and all xref tables to have a pointer (offset)      * to all the pdf's objects. It can handle linearized pdfs, which will have an xref at the end pointing to an xref      * at the beginning of the file. Last the root object is parsed.      *       * @throws IOException If something went wrong.      */
specifier|private
name|void
name|initialParse
parameter_list|()
throws|throws
name|IOException
block|{
name|COSDictionary
name|trailer
init|=
literal|null
decl_stmt|;
comment|// parse startxref
name|long
name|startXRefOffset
init|=
name|getStartxrefOffset
argument_list|()
decl_stmt|;
if|if
condition|(
name|startXRefOffset
operator|>
literal|0
condition|)
block|{
name|trailer
operator|=
name|parseXref
argument_list|(
name|startXRefOffset
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|trailer
operator|=
name|rebuildTrailer
argument_list|()
expr_stmt|;
block|}
name|COSBase
name|rootObject
init|=
name|parseTrailerValuesDynamically
argument_list|(
name|trailer
argument_list|)
decl_stmt|;
comment|// resolve all objects
comment|// A FDF doesn't have a catalog, all FDF fields are within the root object
if|if
condition|(
name|rootObject
operator|instanceof
name|COSDictionary
condition|)
block|{
name|parseDictObjects
argument_list|(
operator|(
name|COSDictionary
operator|)
name|rootObject
argument_list|,
operator|(
name|COSName
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
name|initialParseDone
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * This will parse the stream and populate the COSDocument object.  This will close      * the stream when it is done parsing.      *      * @throws IOException If there is an error reading from the stream or corrupt data      * is found.      */
specifier|public
name|void
name|parse
parameter_list|()
throws|throws
name|IOException
block|{
comment|// set to false if all is processed
name|boolean
name|exceptionOccurred
init|=
literal|true
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|parseFDFHeader
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Header doesn't contain versioninfo"
argument_list|)
throw|;
block|}
name|initialParse
argument_list|()
expr_stmt|;
name|exceptionOccurred
operator|=
literal|false
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|exceptionOccurred
operator|&&
name|document
operator|!=
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|document
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

