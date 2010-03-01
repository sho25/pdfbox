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
name|Iterator
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
name|COSInteger
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
name|COSStream
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
name|persistence
operator|.
name|util
operator|.
name|COSObjectKey
import|;
end_import

begin_comment
comment|/**  * This will parse a PDF 1.5 (or better) Xref stream and   * extract the xref information from the stream.  *   *  @author<a href="mailto:justinl@basistech.com">Justin LeFebvre</a>  *  @version $Revision: 1.0 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFXrefStreamParser
extends|extends
name|BaseParser
block|{
specifier|private
name|COSStream
name|stream
decl_stmt|;
comment|/**      * Constructor.      *      * @param strm The stream to parse.      * @param doc The document for the current parsing.      *      * @throws IOException If there is an error initializing the stream.      */
specifier|public
name|PDFXrefStreamParser
parameter_list|(
name|COSStream
name|strm
parameter_list|,
name|COSDocument
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|strm
operator|.
name|getUnfilteredStream
argument_list|()
argument_list|)
expr_stmt|;
name|setDocument
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|stream
operator|=
name|strm
expr_stmt|;
block|}
comment|/**      * Parses through the unfiltered stream and populates the xrefTable HashMap.      * @throws IOException If there is an error while parsing the stream.      */
specifier|public
name|void
name|parse
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|COSArray
name|xrefFormat
init|=
operator|(
name|COSArray
operator|)
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|W
argument_list|)
decl_stmt|;
name|COSArray
name|indexArray
init|=
operator|(
name|COSArray
operator|)
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|INDEX
argument_list|)
decl_stmt|;
comment|/*              * If Index doesn't exist, we will use the default values.               */
if|if
condition|(
name|indexArray
operator|==
literal|null
condition|)
block|{
name|indexArray
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|indexArray
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|indexArray
operator|.
name|add
argument_list|(
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SIZE
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ArrayList
name|objNums
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|/*              * Populates objNums with all object numbers available              */
name|Iterator
name|indexIter
init|=
name|indexArray
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|indexIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|int
name|objID
init|=
operator|(
operator|(
name|COSInteger
operator|)
name|indexIter
operator|.
name|next
argument_list|()
operator|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|int
name|size
init|=
operator|(
operator|(
name|COSInteger
operator|)
name|indexIter
operator|.
name|next
argument_list|()
operator|)
operator|.
name|intValue
argument_list|()
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|objNums
operator|.
name|add
argument_list|(
operator|new
name|Integer
argument_list|(
name|objID
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Iterator
name|objIter
init|=
name|objNums
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|/*              * Calculating the size of the line in bytes              */
name|int
name|w0
init|=
name|xrefFormat
operator|.
name|getInt
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|int
name|w1
init|=
name|xrefFormat
operator|.
name|getInt
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|int
name|w2
init|=
name|xrefFormat
operator|.
name|getInt
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|int
name|lineSize
init|=
name|w0
operator|+
name|w1
operator|+
name|w2
decl_stmt|;
while|while
condition|(
name|pdfSource
operator|.
name|available
argument_list|()
operator|>
literal|0
operator|&&
name|objIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|byte
index|[]
name|currLine
init|=
operator|new
name|byte
index|[
name|lineSize
index|]
decl_stmt|;
name|pdfSource
operator|.
name|read
argument_list|(
name|currLine
argument_list|)
expr_stmt|;
name|int
name|type
init|=
literal|0
decl_stmt|;
comment|/*                  * Grabs the number of bytes specified for the first column in                   * the W array and stores it.                  */
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|w0
condition|;
name|i
operator|++
control|)
block|{
name|type
operator|+=
operator|(
name|currLine
index|[
name|i
index|]
operator|&
literal|0x00ff
operator|)
operator|<<
operator|(
operator|(
name|w0
operator|-
name|i
operator|-
literal|1
operator|)
operator|*
literal|8
operator|)
expr_stmt|;
block|}
comment|//Need to remember the current objID
name|Integer
name|objID
init|=
operator|(
name|Integer
operator|)
name|objIter
operator|.
name|next
argument_list|()
decl_stmt|;
comment|/*                  * 3 different types of entries.                   */
switch|switch
condition|(
name|type
condition|)
block|{
case|case
literal|0
case|:
comment|/*                          * Skipping free objects                          */
break|break;
case|case
literal|1
case|:
name|int
name|offset
init|=
literal|0
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
name|w1
condition|;
name|i
operator|++
control|)
block|{
name|offset
operator|+=
operator|(
name|currLine
index|[
name|i
operator|+
name|w0
index|]
operator|&
literal|0x00ff
operator|)
operator|<<
operator|(
operator|(
name|w1
operator|-
name|i
operator|-
literal|1
operator|)
operator|*
literal|8
operator|)
expr_stmt|;
block|}
name|int
name|genNum
init|=
literal|0
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
name|w2
condition|;
name|i
operator|++
control|)
block|{
name|genNum
operator|+=
operator|(
name|currLine
index|[
name|i
operator|+
name|w0
operator|+
name|w1
index|]
operator|&
literal|0x00ff
operator|)
operator|<<
operator|(
operator|(
name|w2
operator|-
name|i
operator|-
literal|1
operator|)
operator|*
literal|8
operator|)
expr_stmt|;
block|}
name|COSObjectKey
name|objKey
init|=
operator|new
name|COSObjectKey
argument_list|(
name|objID
operator|.
name|intValue
argument_list|()
argument_list|,
name|genNum
argument_list|)
decl_stmt|;
name|document
operator|.
name|setXRef
argument_list|(
name|objKey
argument_list|,
name|offset
argument_list|)
expr_stmt|;
break|break;
case|case
literal|2
case|:
comment|/*                          * These objects are handled by the dereferenceObjects() method                          * since they're only pointing to object numbers                          */
break|break;
default|default:
break|break;
block|}
block|}
block|}
finally|finally
block|{
name|pdfSource
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

