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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|pdfwriter
operator|.
name|COSWriterXRefEntry
import|;
end_import

begin_comment
comment|/**  * @author Alexander Funk  * @version $Revision: $  */
end_comment

begin_class
specifier|public
class|class
name|PDFXRefStream
implements|implements
name|PDFXRef
block|{
specifier|private
specifier|static
specifier|final
name|int
name|ENTRY_OBJSTREAM
init|=
literal|2
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|ENTRY_NORMAL
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|ENTRY_FREE
init|=
literal|0
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
name|streamData
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|Integer
argument_list|>
name|objectNumbers
decl_stmt|;
specifier|private
name|COSStream
name|stream
decl_stmt|;
specifier|private
name|long
name|size
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * Create a fresh XRef stream like for a fresh file or an incremental update.      */
specifier|public
name|PDFXRefStream
parameter_list|()
block|{
name|this
operator|.
name|stream
operator|=
operator|new
name|COSStream
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|,
operator|new
name|RandomAccessBuffer
argument_list|()
argument_list|)
expr_stmt|;
name|streamData
operator|=
operator|new
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|objectNumbers
operator|=
operator|new
name|TreeSet
argument_list|<
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the stream of the XRef.      * @return the XRef stream      * @throws IOException if something went wrong      */
specifier|public
name|COSStream
name|getStream
parameter_list|()
throws|throws
name|IOException
block|{
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|XREF
argument_list|)
expr_stmt|;
if|if
condition|(
name|size
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"size is not set in xrefstream"
argument_list|)
throw|;
block|}
name|stream
operator|.
name|setLong
argument_list|(
name|COSName
operator|.
name|SIZE
argument_list|,
name|getSizeEntry
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|setFilters
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
expr_stmt|;
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|indexEntry
init|=
name|getIndexEntry
argument_list|()
decl_stmt|;
name|COSArray
name|indexAsArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|Integer
name|i
range|:
name|indexEntry
control|)
block|{
name|indexAsArray
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|INDEX
argument_list|,
name|indexAsArray
argument_list|)
expr_stmt|;
block|}
block|{
name|int
index|[]
name|wEntry
init|=
name|getWEntry
argument_list|()
decl_stmt|;
name|COSArray
name|wAsArray
init|=
operator|new
name|COSArray
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
name|wEntry
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|j
init|=
name|wEntry
index|[
name|i
index|]
decl_stmt|;
name|wAsArray
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|j
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|W
argument_list|,
name|wAsArray
argument_list|)
expr_stmt|;
name|OutputStream
name|unfilteredStream
init|=
name|stream
operator|.
name|createUnfilteredStream
argument_list|()
decl_stmt|;
name|writeStreamData
argument_list|(
name|unfilteredStream
argument_list|,
name|wEntry
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|COSName
argument_list|>
name|keySet
init|=
name|stream
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|COSName
name|cosName
range|:
name|keySet
control|)
block|{
name|COSBase
name|dictionaryObject
init|=
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|cosName
argument_list|)
decl_stmt|;
name|dictionaryObject
operator|.
name|setDirect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|stream
return|;
block|}
comment|/**      * Copy all Trailer Information to this file.      *       * @param trailerDict dictionary to be added as trailer info      */
specifier|public
name|void
name|addTrailerInfo
parameter_list|(
name|COSDictionary
name|trailerDict
parameter_list|)
block|{
name|Set
argument_list|<
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
argument_list|>
name|entrySet
init|=
name|trailerDict
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
name|entry
range|:
name|entrySet
control|)
block|{
name|COSName
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|INFO
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|COSName
operator|.
name|ROOT
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|COSName
operator|.
name|ENCRYPT
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|COSName
operator|.
name|ID
operator|.
name|equals
argument_list|(
name|key
argument_list|)
operator|||
name|COSName
operator|.
name|PREV
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|stream
operator|.
name|setItem
argument_list|(
name|key
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Add an new entry to the XRef stream.      *       * @param entry new entry to be added      */
specifier|public
name|void
name|addEntry
parameter_list|(
name|COSWriterXRefEntry
name|entry
parameter_list|)
block|{
name|objectNumbers
operator|.
name|add
argument_list|(
operator|(
name|int
operator|)
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|.
name|isFree
argument_list|()
condition|)
block|{
comment|// what would be a f-Entry in the xref table
name|FreeReference
name|value
init|=
operator|new
name|FreeReference
argument_list|()
decl_stmt|;
name|value
operator|.
name|nextGenNumber
operator|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getGeneration
argument_list|()
expr_stmt|;
name|value
operator|.
name|nextFree
operator|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getNumber
argument_list|()
expr_stmt|;
name|streamData
operator|.
name|put
argument_list|(
operator|(
name|int
operator|)
name|value
operator|.
name|nextFree
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we don't care for ObjectStreamReferences for now and only handle
comment|// normal references that would be f-Entrys in the xref table.
name|NormalReference
name|value
init|=
operator|new
name|NormalReference
argument_list|()
decl_stmt|;
name|value
operator|.
name|genNumber
operator|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getGeneration
argument_list|()
expr_stmt|;
name|value
operator|.
name|offset
operator|=
name|entry
operator|.
name|getOffset
argument_list|()
expr_stmt|;
name|streamData
operator|.
name|put
argument_list|(
operator|(
name|int
operator|)
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getNumber
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * determines the minimal length required for all the lengths.      *       * @return the length information      */
specifier|private
name|int
index|[]
name|getWEntry
parameter_list|()
block|{
name|long
index|[]
name|wMax
init|=
operator|new
name|long
index|[
literal|3
index|]
decl_stmt|;
for|for
control|(
name|Object
name|entry
range|:
name|streamData
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|instanceof
name|FreeReference
condition|)
block|{
name|FreeReference
name|free
init|=
operator|(
name|FreeReference
operator|)
name|entry
decl_stmt|;
name|wMax
index|[
literal|0
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|wMax
index|[
literal|0
index|]
argument_list|,
name|ENTRY_FREE
argument_list|)
expr_stmt|;
comment|// the type field for a free reference
name|wMax
index|[
literal|1
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|wMax
index|[
literal|1
index|]
argument_list|,
name|free
operator|.
name|nextFree
argument_list|)
expr_stmt|;
name|wMax
index|[
literal|2
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|wMax
index|[
literal|2
index|]
argument_list|,
name|free
operator|.
name|nextGenNumber
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|entry
operator|instanceof
name|NormalReference
condition|)
block|{
name|NormalReference
name|ref
init|=
operator|(
name|NormalReference
operator|)
name|entry
decl_stmt|;
name|wMax
index|[
literal|0
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|wMax
index|[
literal|0
index|]
argument_list|,
name|ENTRY_NORMAL
argument_list|)
expr_stmt|;
comment|// the type field for a normal reference
name|wMax
index|[
literal|1
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|wMax
index|[
literal|1
index|]
argument_list|,
name|ref
operator|.
name|offset
argument_list|)
expr_stmt|;
name|wMax
index|[
literal|2
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|wMax
index|[
literal|2
index|]
argument_list|,
name|ref
operator|.
name|genNumber
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|entry
operator|instanceof
name|ObjectStreamReference
condition|)
block|{
name|ObjectStreamReference
name|objStream
init|=
operator|(
name|ObjectStreamReference
operator|)
name|entry
decl_stmt|;
name|wMax
index|[
literal|0
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|wMax
index|[
literal|0
index|]
argument_list|,
name|ENTRY_OBJSTREAM
argument_list|)
expr_stmt|;
comment|// the type field for a objstm reference
name|wMax
index|[
literal|1
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|wMax
index|[
literal|1
index|]
argument_list|,
name|objStream
operator|.
name|offset
argument_list|)
expr_stmt|;
name|wMax
index|[
literal|2
index|]
operator|=
name|Math
operator|.
name|max
argument_list|(
name|wMax
index|[
literal|2
index|]
argument_list|,
name|objStream
operator|.
name|objectNumberOfObjectStream
argument_list|)
expr_stmt|;
block|}
comment|// TODO add here if new standard versions define new types
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"unexpected reference type"
argument_list|)
throw|;
block|}
block|}
comment|// find the max bytes needed to display that column
name|int
index|[]
name|w
init|=
operator|new
name|int
index|[
literal|3
index|]
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
name|w
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
while|while
condition|(
name|wMax
index|[
name|i
index|]
operator|>
literal|0
condition|)
block|{
name|w
index|[
name|i
index|]
operator|++
expr_stmt|;
name|wMax
index|[
name|i
index|]
operator|>>=
literal|8
expr_stmt|;
block|}
block|}
return|return
name|w
return|;
block|}
specifier|private
name|long
name|getSizeEntry
parameter_list|()
block|{
return|return
name|size
return|;
block|}
comment|/**      * Set the size of the XRef stream.      *       * @param streamSize size to bet set as stream size      */
specifier|public
name|void
name|setSize
parameter_list|(
name|long
name|streamSize
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|streamSize
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|Integer
argument_list|>
name|getIndexEntry
parameter_list|()
block|{
name|LinkedList
argument_list|<
name|Integer
argument_list|>
name|linkedList
init|=
operator|new
name|LinkedList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|Integer
name|first
init|=
literal|null
decl_stmt|;
name|Integer
name|length
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Integer
name|objNumber
range|:
name|objectNumbers
control|)
block|{
if|if
condition|(
name|first
operator|==
literal|null
condition|)
block|{
name|first
operator|=
name|objNumber
expr_stmt|;
name|length
operator|=
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|first
operator|+
name|length
operator|==
name|objNumber
condition|)
block|{
name|length
operator|+=
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|first
operator|+
name|length
operator|<
name|objNumber
condition|)
block|{
name|linkedList
operator|.
name|add
argument_list|(
name|first
argument_list|)
expr_stmt|;
name|linkedList
operator|.
name|add
argument_list|(
name|length
argument_list|)
expr_stmt|;
name|first
operator|=
name|objNumber
expr_stmt|;
name|length
operator|=
literal|1
expr_stmt|;
block|}
block|}
name|linkedList
operator|.
name|add
argument_list|(
name|first
argument_list|)
expr_stmt|;
name|linkedList
operator|.
name|add
argument_list|(
name|length
argument_list|)
expr_stmt|;
return|return
name|linkedList
return|;
block|}
specifier|private
name|void
name|writeNumber
parameter_list|(
name|OutputStream
name|os
parameter_list|,
name|long
name|number
parameter_list|,
name|int
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|bytes
index|]
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
name|bytes
condition|;
name|i
operator|++
control|)
block|{
name|buffer
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|number
operator|&
literal|0xff
argument_list|)
expr_stmt|;
name|number
operator|>>=
literal|8
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bytes
condition|;
name|i
operator|++
control|)
block|{
name|os
operator|.
name|write
argument_list|(
name|buffer
index|[
name|bytes
operator|-
name|i
operator|-
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|writeStreamData
parameter_list|(
name|OutputStream
name|os
parameter_list|,
name|int
index|[]
name|w
parameter_list|)
throws|throws
name|IOException
block|{
comment|// iterate over all streamData and write it in the required format
for|for
control|(
name|Object
name|entry
range|:
name|streamData
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|instanceof
name|FreeReference
condition|)
block|{
name|FreeReference
name|free
init|=
operator|(
name|FreeReference
operator|)
name|entry
decl_stmt|;
name|writeNumber
argument_list|(
name|os
argument_list|,
name|ENTRY_FREE
argument_list|,
name|w
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|writeNumber
argument_list|(
name|os
argument_list|,
name|free
operator|.
name|nextFree
argument_list|,
name|w
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|writeNumber
argument_list|(
name|os
argument_list|,
name|free
operator|.
name|nextGenNumber
argument_list|,
name|w
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|entry
operator|instanceof
name|NormalReference
condition|)
block|{
name|NormalReference
name|ref
init|=
operator|(
name|NormalReference
operator|)
name|entry
decl_stmt|;
name|writeNumber
argument_list|(
name|os
argument_list|,
name|ENTRY_NORMAL
argument_list|,
name|w
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|writeNumber
argument_list|(
name|os
argument_list|,
name|ref
operator|.
name|offset
argument_list|,
name|w
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|writeNumber
argument_list|(
name|os
argument_list|,
name|ref
operator|.
name|genNumber
argument_list|,
name|w
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|entry
operator|instanceof
name|ObjectStreamReference
condition|)
block|{
name|ObjectStreamReference
name|objStream
init|=
operator|(
name|ObjectStreamReference
operator|)
name|entry
decl_stmt|;
name|writeNumber
argument_list|(
name|os
argument_list|,
name|ENTRY_OBJSTREAM
argument_list|,
name|w
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|writeNumber
argument_list|(
name|os
argument_list|,
name|objStream
operator|.
name|offset
argument_list|,
name|w
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|writeNumber
argument_list|(
name|os
argument_list|,
name|objStream
operator|.
name|objectNumberOfObjectStream
argument_list|,
name|w
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
block|}
comment|// TODO add here if new standard versions define new types
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"unexpected reference type"
argument_list|)
throw|;
block|}
block|}
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * A class representing an object stream reference.       *      */
class|class
name|ObjectStreamReference
block|{
name|long
name|objectNumberOfObjectStream
decl_stmt|;
name|long
name|offset
decl_stmt|;
block|}
comment|/**      * A class representing a normal reference.       *      */
class|class
name|NormalReference
block|{
name|long
name|genNumber
decl_stmt|;
name|long
name|offset
decl_stmt|;
block|}
comment|/**      * A class representing a free reference.       *      */
class|class
name|FreeReference
block|{
name|long
name|nextGenNumber
decl_stmt|;
name|long
name|nextFree
decl_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|COSObject
name|getObject
parameter_list|(
name|int
name|objectNumber
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

