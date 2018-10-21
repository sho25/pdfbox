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
name|cos
operator|.
name|COSObjectKey
import|;
end_import

begin_comment
comment|/**  * This will parse a PDF 1.5 (or better) Xref stream and  * extract the xref information from the stream.  *  *  @author Justin LeFebvre  */
end_comment

begin_class
specifier|public
class|class
name|PDFXrefStreamParser
extends|extends
name|BaseParser
block|{
specifier|private
specifier|final
name|COSStream
name|stream
decl_stmt|;
specifier|private
specifier|final
name|XrefTrailerResolver
name|xrefTrailerResolver
decl_stmt|;
comment|/**      * Constructor.      *      * @param stream The stream to parse.      * @param document The document for the current parsing.      * @param resolver resolver to read the xref/trailer information      *      * @throws IOException If there is an error initializing the stream.      */
specifier|public
name|PDFXrefStreamParser
parameter_list|(
name|COSStream
name|stream
parameter_list|,
name|COSDocument
name|document
parameter_list|,
name|XrefTrailerResolver
name|resolver
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
name|this
operator|.
name|xrefTrailerResolver
operator|=
name|resolver
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
name|COSBase
name|w
init|=
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|W
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|w
operator|instanceof
name|COSArray
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"/W array is missing in Xref stream"
argument_list|)
throw|;
block|}
name|COSArray
name|xrefFormat
init|=
operator|(
name|COSArray
operator|)
name|w
decl_stmt|;
name|COSBase
name|base
init|=
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|INDEX
argument_list|)
decl_stmt|;
name|COSArray
name|indexArray
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
name|indexArray
operator|=
operator|(
name|COSArray
operator|)
name|base
expr_stmt|;
block|}
else|else
block|{
comment|// If /Index doesn't exist, we will use the default values.
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
name|COSInteger
operator|.
name|get
argument_list|(
name|stream
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|SIZE
argument_list|,
literal|0
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Long
argument_list|>
name|objNums
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/*          * Populates objNums with all object numbers available          */
name|Iterator
argument_list|<
name|COSBase
argument_list|>
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
name|base
operator|=
name|indexIter
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSInteger
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Xref stream must have integer in /Index array"
argument_list|)
throw|;
block|}
name|long
name|objID
init|=
operator|(
operator|(
name|COSInteger
operator|)
name|base
operator|)
operator|.
name|longValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|indexIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
break|break;
block|}
name|base
operator|=
name|indexIter
operator|.
name|next
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSInteger
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Xref stream must have integer in /Index array"
argument_list|)
throw|;
block|}
name|int
name|size
init|=
operator|(
operator|(
name|COSInteger
operator|)
name|base
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
name|objID
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
block|}
name|Iterator
argument_list|<
name|Long
argument_list|>
name|objIter
init|=
name|objNums
operator|.
name|iterator
argument_list|()
decl_stmt|;
comment|/*          * Calculating the size of the line in bytes          */
name|int
name|w0
init|=
name|xrefFormat
operator|.
name|getInt
argument_list|(
literal|0
argument_list|,
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
argument_list|,
literal|0
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
argument_list|,
literal|0
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
operator|!
name|seqSource
operator|.
name|isEOF
argument_list|()
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
name|seqSource
operator|.
name|read
argument_list|(
name|currLine
argument_list|)
expr_stmt|;
name|int
name|type
decl_stmt|;
if|if
condition|(
name|w0
operator|==
literal|0
condition|)
block|{
comment|// "If the first element is zero,
comment|// the type field shall not be present, and shall default to type 1"
name|type
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
literal|0
expr_stmt|;
comment|/*                  * Grabs the number of bytes specified for the first column in                  * the W array and stores it.                  */
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
block|}
comment|//Need to remember the current objID
name|Long
name|objID
init|=
name|objIter
operator|.
name|next
argument_list|()
decl_stmt|;
comment|/*              * 3 different types of entries.              */
switch|switch
condition|(
name|type
condition|)
block|{
case|case
literal|0
case|:
comment|/*                      * Skipping free objects                      */
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
argument_list|,
name|genNum
argument_list|)
decl_stmt|;
name|xrefTrailerResolver
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
comment|/*                      * object stored in object stream:                       * 2nd argument is object number of object stream                      * 3rd argument is index of object within object stream                      *                       * For sequential PDFParser we do not need this information                      * because                      * These objects are handled by the dereferenceObjects() method                      * since they're only pointing to object numbers                      *                       * However for XRef aware parsers we have to know which objects contain                      * object streams. We will store this information in normal xref mapping                      * table but add object stream number with minus sign in order to                      * distinguish from file offsets                      */
name|int
name|objstmObjNr
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
name|objstmObjNr
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
name|objKey
operator|=
operator|new
name|COSObjectKey
argument_list|(
name|objID
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|xrefTrailerResolver
operator|.
name|setXRef
argument_list|(
name|objKey
argument_list|,
operator|-
name|objstmObjNr
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
block|}
block|}
end_class

end_unit

