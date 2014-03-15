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
name|graphics
operator|.
name|image
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|OutputStream
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
name|RandomAccess
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
name|common
operator|.
name|PDStream
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
name|color
operator|.
name|PDDeviceGray
import|;
end_import

begin_comment
comment|/**  * Factory for creating a PDImageXObject containing a CCITT Fax compressed TIFF image.  * @author Ben Litchfield  * @author Paul King  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CCITTFactory
block|{
specifier|private
name|CCITTFactory
parameter_list|()
block|{     }
comment|/**      * Creates a new CCITT Fax compressed Image XObject from a TIFF file.      *       * @param document the document to create the image as part of.      * @param reader the random access TIFF file which contains a suitable CCITT compressed image      * @return a new Image XObject      * @throws IOException if there is an error reading the TIFF data.      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromRandomAccess
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|RandomAccess
name|reader
parameter_list|)
throws|throws
name|IOException
block|{
name|COSDictionary
name|decodeParms
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|extractFromTiff
argument_list|(
name|reader
argument_list|,
name|bos
argument_list|,
name|decodeParms
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|byteStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|PDImageXObject
name|pdImage
init|=
operator|new
name|PDImageXObject
argument_list|(
name|document
argument_list|,
name|byteStream
argument_list|)
decl_stmt|;
name|COSDictionary
name|dict
init|=
name|pdImage
operator|.
name|getCOSStream
argument_list|()
decl_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|,
name|COSName
operator|.
name|CCITTFAX_DECODE
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|IMAGE
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|XOBJECT
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DECODE_PARMS
argument_list|,
name|decodeParms
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setBitsPerComponent
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setColorSpace
argument_list|(
name|PDDeviceGray
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setWidth
argument_list|(
name|decodeParms
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COLUMNS
argument_list|)
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setHeight
argument_list|(
name|decodeParms
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|ROWS
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|pdImage
return|;
block|}
comment|// extracts the CCITT stream from the TIFF file
specifier|private
specifier|static
name|void
name|extractFromTiff
parameter_list|(
name|RandomAccess
name|reader
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|COSDictionary
name|params
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
comment|// First check the basic tiff header
name|reader
operator|.
name|seek
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|char
name|endianess
init|=
operator|(
name|char
operator|)
name|reader
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|char
operator|)
name|reader
operator|.
name|read
argument_list|()
operator|!=
name|endianess
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not a valid tiff file"
argument_list|)
throw|;
block|}
comment|// ensure that endianess is either M or I
if|if
condition|(
name|endianess
operator|!=
literal|'M'
operator|&&
name|endianess
operator|!=
literal|'I'
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not a valid tiff file"
argument_list|)
throw|;
block|}
name|int
name|magicNumber
init|=
name|readshort
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
decl_stmt|;
if|if
condition|(
name|magicNumber
operator|!=
literal|42
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not a valid tiff file"
argument_list|)
throw|;
block|}
comment|// Relocate to the first set of tags
name|reader
operator|.
name|seek
argument_list|(
name|readlong
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|numtags
init|=
name|readshort
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
decl_stmt|;
comment|// The number 50 is somewhat arbitary, it just stops us load up junk from somewhere
comment|// and tramping on
if|if
condition|(
name|numtags
operator|>
literal|50
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not a valid tiff file"
argument_list|)
throw|;
block|}
comment|// Loop through the tags, some will convert to items in the parms dictionary
comment|// Other point us to where to find the data stream
comment|// The only parm which might change as a result of other options is K, so
comment|// We'll deal with that as a special;
name|int
name|k
init|=
operator|-
literal|1000
decl_stmt|;
comment|// Default Non CCITT compression
name|int
name|dataoffset
init|=
literal|0
decl_stmt|;
name|int
name|datalength
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
name|numtags
condition|;
name|i
operator|++
control|)
block|{
name|int
name|tag
init|=
name|readshort
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
decl_stmt|;
name|int
name|type
init|=
name|readshort
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
decl_stmt|;
name|int
name|count
init|=
name|readlong
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
decl_stmt|;
name|int
name|val
init|=
name|readlong
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
decl_stmt|;
comment|// See note
comment|// Note, we treated that value as a long. The value always occupies 4 bytes
comment|// But it might only use the first byte or two. Depending on endianess we might
comment|// need to correct.
comment|// Note we ignore all other types, they are of little interest for PDFs/CCITT Fax
if|if
condition|(
name|endianess
operator|==
literal|'M'
condition|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
literal|1
case|:
block|{
name|val
operator|=
name|val
operator|>>
literal|24
expr_stmt|;
break|break;
comment|// byte value
block|}
case|case
literal|3
case|:
block|{
name|val
operator|=
name|val
operator|>>
literal|16
expr_stmt|;
break|break;
comment|// short value
block|}
case|case
literal|4
case|:
block|{
break|break;
comment|// long value
block|}
default|default:
block|{
comment|// do nothing
block|}
block|}
block|}
switch|switch
condition|(
name|tag
condition|)
block|{
case|case
literal|256
case|:
block|{
name|params
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|COLUMNS
argument_list|,
name|val
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
literal|257
case|:
block|{
name|params
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|ROWS
argument_list|,
name|val
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
literal|259
case|:
block|{
if|if
condition|(
name|val
operator|==
literal|4
condition|)
block|{
name|k
operator|=
operator|-
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|val
operator|==
literal|3
condition|)
block|{
name|k
operator|=
literal|0
expr_stmt|;
block|}
break|break;
comment|// T6/T4 Compression
block|}
case|case
literal|262
case|:
block|{
if|if
condition|(
name|val
operator|==
literal|1
condition|)
block|{
name|params
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|BLACK_IS_1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|273
case|:
block|{
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
name|dataoffset
operator|=
name|val
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|279
case|:
block|{
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
name|datalength
operator|=
name|val
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|292
case|:
block|{
if|if
condition|(
name|val
operator|==
literal|1
condition|)
block|{
name|k
operator|=
literal|50
expr_stmt|;
comment|// T4 2D - arbitary K value
block|}
break|break;
block|}
case|case
literal|324
case|:
block|{
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
name|dataoffset
operator|=
name|val
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|325
case|:
block|{
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
name|datalength
operator|=
name|val
expr_stmt|;
block|}
break|break;
block|}
default|default:
block|{
comment|// do nothing
block|}
block|}
block|}
if|if
condition|(
name|k
operator|==
operator|-
literal|1000
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"First image in tiff is not CCITT T4 or T6 compressed"
argument_list|)
throw|;
block|}
if|if
condition|(
name|dataoffset
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"First image in tiff is not a single tile/strip"
argument_list|)
throw|;
block|}
name|params
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
name|k
argument_list|)
expr_stmt|;
name|reader
operator|.
name|seek
argument_list|(
name|dataoffset
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|int
name|amountRead
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|reader
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|Math
operator|.
name|min
argument_list|(
literal|8192
argument_list|,
name|datalength
argument_list|)
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|datalength
operator|-=
name|amountRead
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|int
name|readshort
parameter_list|(
name|char
name|endianess
parameter_list|,
name|RandomAccess
name|raf
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|endianess
operator|==
literal|'I'
condition|)
block|{
return|return
name|raf
operator|.
name|read
argument_list|()
operator||
operator|(
name|raf
operator|.
name|read
argument_list|()
operator|<<
literal|8
operator|)
return|;
block|}
return|return
operator|(
name|raf
operator|.
name|read
argument_list|()
operator|<<
literal|8
operator|)
operator||
name|raf
operator|.
name|read
argument_list|()
return|;
block|}
specifier|private
specifier|static
name|int
name|readlong
parameter_list|(
name|char
name|endianess
parameter_list|,
name|RandomAccess
name|raf
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|endianess
operator|==
literal|'I'
condition|)
block|{
return|return
name|raf
operator|.
name|read
argument_list|()
operator||
operator|(
name|raf
operator|.
name|read
argument_list|()
operator|<<
literal|8
operator|)
operator||
operator|(
name|raf
operator|.
name|read
argument_list|()
operator|<<
literal|16
operator|)
operator||
operator|(
name|raf
operator|.
name|read
argument_list|()
operator|<<
literal|24
operator|)
return|;
block|}
return|return
operator|(
name|raf
operator|.
name|read
argument_list|()
operator|<<
literal|24
operator|)
operator||
operator|(
name|raf
operator|.
name|read
argument_list|()
operator|<<
literal|16
operator|)
operator||
operator|(
name|raf
operator|.
name|read
argument_list|()
operator|<<
literal|8
operator|)
operator||
name|raf
operator|.
name|read
argument_list|()
return|;
block|}
block|}
end_class

end_unit

