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
name|List
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
name|COSObject
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
name|COSStream
import|;
end_import

begin_comment
comment|/**  * This will parse a PDF 1.5 object stream and extract all of the objects from the stream.  *  * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|PDFObjectStreamParser
extends|extends
name|BaseParser
block|{
comment|/**      * Log instance.      */
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
name|PDFObjectStreamParser
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|List
argument_list|<
name|COSObject
argument_list|>
name|streamObjects
init|=
literal|null
decl_stmt|;
specifier|private
specifier|final
name|COSStream
name|stream
decl_stmt|;
comment|/**      * Constructor.      *      * @param stream The stream to parse.      * @param document The document for the current parsing.      * @throws IOException If there is an error initializing the stream.      */
specifier|public
name|PDFObjectStreamParser
parameter_list|(
name|COSStream
name|stream
parameter_list|,
name|COSDocument
name|document
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
operator|new
name|InputStreamSource
argument_list|(
name|stream
operator|.
name|createInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|stream
operator|=
name|stream
expr_stmt|;
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
block|}
comment|/**      * This will parse the tokens in the stream.  This will close the      * stream when it is finished parsing.      *      * @throws IOException If there is an error while parsing the stream.      */
specifier|public
name|void
name|parse
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
comment|//need to first parse the header.
name|int
name|numberOfObjects
init|=
name|stream
operator|.
name|getInt
argument_list|(
literal|"N"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|objectNumbers
init|=
operator|new
name|ArrayList
argument_list|<
name|Long
argument_list|>
argument_list|(
name|numberOfObjects
argument_list|)
decl_stmt|;
name|streamObjects
operator|=
operator|new
name|ArrayList
argument_list|<
name|COSObject
argument_list|>
argument_list|(
name|numberOfObjects
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfObjects
condition|;
name|i
operator|++
control|)
block|{
name|long
name|objectNumber
init|=
name|readObjectNumber
argument_list|()
decl_stmt|;
comment|// skip offset
name|readLong
argument_list|()
expr_stmt|;
name|objectNumbers
operator|.
name|add
argument_list|(
name|objectNumber
argument_list|)
expr_stmt|;
block|}
name|COSObject
name|object
decl_stmt|;
name|COSBase
name|cosObject
decl_stmt|;
name|int
name|objectCounter
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|cosObject
operator|=
name|parseDirObject
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|object
operator|=
operator|new
name|COSObject
argument_list|(
name|cosObject
argument_list|)
expr_stmt|;
name|object
operator|.
name|setGenerationNumber
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|objectCounter
operator|>=
name|objectNumbers
operator|.
name|size
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"/ObjStm (object stream) has more objects than /N "
operator|+
name|numberOfObjects
argument_list|)
expr_stmt|;
break|break;
block|}
name|object
operator|.
name|setObjectNumber
argument_list|(
name|objectNumbers
operator|.
name|get
argument_list|(
name|objectCounter
argument_list|)
argument_list|)
expr_stmt|;
name|streamObjects
operator|.
name|add
argument_list|(
name|object
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"parsed="
operator|+
name|object
argument_list|)
expr_stmt|;
block|}
comment|// According to the spec objects within an object stream shall not be enclosed
comment|// by obj/endobj tags, but there are some pdfs in the wild using those tags
comment|// skip endobject marker if present
if|if
condition|(
operator|!
name|seqSource
operator|.
name|isEOF
argument_list|()
operator|&&
name|seqSource
operator|.
name|peek
argument_list|()
operator|==
literal|'e'
condition|)
block|{
name|readLine
argument_list|()
expr_stmt|;
block|}
name|objectCounter
operator|++
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|seqSource
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This will get the objects that were parsed from the stream.      *      * @return All of the objects in the stream.      */
specifier|public
name|List
argument_list|<
name|COSObject
argument_list|>
name|getObjects
parameter_list|()
block|{
return|return
name|streamObjects
return|;
block|}
block|}
end_class

end_unit

