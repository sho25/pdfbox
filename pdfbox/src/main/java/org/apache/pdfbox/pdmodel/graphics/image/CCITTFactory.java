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
name|OutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|MemoryCacheImageOutputStream
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
name|filter
operator|.
name|Filter
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
name|filter
operator|.
name|FilterFactory
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
name|graphics
operator|.
name|color
operator|.
name|PDColorSpace
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
comment|/**  * Factory for creating a PDImageXObject containing a CCITT Fax compressed TIFF image.  *   * @author Ben Litchfield  * @author Paul King  */
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
comment|/**      * Creates a new CCITT group 4 (T6) compressed image XObject from a b/w BufferedImage. This      * compression technique usually results in smaller images than those produced by {@link LosslessFactory#createFromImage(PDDocument, BufferedImage)      * }.      *      * @param document the document to create the image as part of.      * @param image the image.      * @return a new image XObject.      * @throws IOException if there is an error creating the image.      * @throws IllegalArgumentException if the BufferedImage is not a b/w image.      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromImage
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|BufferedImage
name|image
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|image
operator|.
name|getType
argument_list|()
operator|!=
name|BufferedImage
operator|.
name|TYPE_BYTE_BINARY
operator|&&
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getPixelSize
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only 1-bit b/w images supported"
argument_list|)
throw|;
block|}
name|int
name|height
init|=
name|image
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|width
init|=
name|image
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|MemoryCacheImageOutputStream
name|mcios
init|=
operator|new
name|MemoryCacheImageOutputStream
argument_list|(
name|bos
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|y
init|=
literal|0
init|;
name|y
operator|<
name|height
condition|;
operator|++
name|y
control|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|width
condition|;
operator|++
name|x
control|)
block|{
comment|// flip bit to avoid having to set /BlackIs1
name|mcios
operator|.
name|writeBits
argument_list|(
operator|~
operator|(
name|image
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|&
literal|1
operator|)
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mcios
operator|.
name|getBitOffset
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|mcios
operator|.
name|writeBits
argument_list|(
literal|0
argument_list|,
literal|8
operator|-
name|mcios
operator|.
name|getBitOffset
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|mcios
operator|.
name|flush
argument_list|()
expr_stmt|;
name|mcios
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|prepareImageXObject
argument_list|(
name|document
argument_list|,
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
argument_list|)
return|;
block|}
comment|/**      * Creates a new CCITT Fax compressed image XObject from a specific image of a TIFF file stored      * in a byte array. Only single-strip CCITT T4 or T6 compressed TIFF files are supported. If      * you're not sure what TIFF files you have, use      * {@link LosslessFactory#createFromImage(PDDocument, BufferedImage) }      * or {@link CCITTFactory#createFromImage(PDDocument, BufferedImage) }      * instead.      *      * @param document the document to create the image as part of.      * @param byteArray the TIFF file in a byte array which contains a suitable CCITT compressed      * image      * @return a new Image XObject      * @throws IOException if there is an error reading the TIFF data.      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromByteArray
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|byte
index|[]
name|byteArray
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createFromByteArray
argument_list|(
name|document
argument_list|,
name|byteArray
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Creates a new CCITT Fax compressed image XObject from a specific image of a TIFF file stored      * in a byte array. Only single-strip CCITT T4 or T6 compressed TIFF files are supported. If      * you're not sure what TIFF files you have, use      * {@link LosslessFactory#createFromImage(PDDocument, BufferedImage) }      * or {@link CCITTFactory#createFromImage(PDDocument, BufferedImage) }      * instead.      *      * @param document the document to create the image as part of.      * @param byteArray the TIFF file in a byte array which contains a suitable CCITT compressed      * image      * @param number TIFF image number, starting from 0      * @return a new Image XObject      * @throws IOException if there is an error reading the TIFF data.      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromByteArray
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|byte
index|[]
name|byteArray
parameter_list|,
name|int
name|number
parameter_list|)
throws|throws
name|IOException
block|{
name|RandomAccess
name|raf
init|=
operator|new
name|RandomAccessBuffer
argument_list|(
name|byteArray
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|createFromRandomAccessImpl
argument_list|(
name|document
argument_list|,
name|raf
argument_list|,
name|number
argument_list|)
return|;
block|}
finally|finally
block|{
name|raf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|PDImageXObject
name|prepareImageXObject
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|byte
index|[]
name|byteArray
parameter_list|,
name|int
name|width
parameter_list|,
name|int
name|height
parameter_list|,
name|PDColorSpace
name|initColorSpace
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Filter
name|filter
init|=
name|FilterFactory
operator|.
name|INSTANCE
operator|.
name|getFilter
argument_list|(
name|COSName
operator|.
name|CCITTFAX_DECODE
argument_list|)
decl_stmt|;
name|COSDictionary
name|dict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|dict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|COLUMNS
argument_list|,
name|width
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|ROWS
argument_list|,
name|height
argument_list|)
expr_stmt|;
name|filter
operator|.
name|encode
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|byteArray
argument_list|)
argument_list|,
name|baos
argument_list|,
name|dict
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|encodedByteStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|PDImageXObject
name|image
init|=
operator|new
name|PDImageXObject
argument_list|(
name|document
argument_list|,
name|encodedByteStream
argument_list|,
name|COSName
operator|.
name|CCITTFAX_DECODE
argument_list|,
name|width
argument_list|,
name|height
argument_list|,
literal|1
argument_list|,
name|initColorSpace
argument_list|)
decl_stmt|;
name|dict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|image
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DECODE_PARMS
argument_list|,
name|dict
argument_list|)
expr_stmt|;
return|return
name|image
return|;
block|}
comment|/**      * Creates a new CCITT Fax compressed image XObject from the first image of a TIFF file.      *       * @param document the document to create the image as part of.      * @param reader the random access TIFF file which contains a suitable CCITT      * compressed image      * @return a new image XObject      * @throws IOException if there is an error reading the TIFF data.      *       * @deprecated Use {@link #createFromFile(PDDocument, File)} instead.      */
annotation|@
name|Deprecated
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
return|return
name|createFromRandomAccessImpl
argument_list|(
name|document
argument_list|,
name|reader
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Creates a new CCITT Fax compressed image XObject from a specific image of a TIFF file.      *      * @param document the document to create the image as part of.      * @param reader the random access TIFF file which contains a suitable CCITT      * compressed image      * @param number TIFF image number, starting from 0      * @return a new image XObject, or null if no such page      * @throws IOException if there is an error reading the TIFF data.      *       * @deprecated Use {@link #createFromFile(PDDocument, File, int)} instead.      */
annotation|@
name|Deprecated
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
parameter_list|,
name|int
name|number
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createFromRandomAccessImpl
argument_list|(
name|document
argument_list|,
name|reader
argument_list|,
name|number
argument_list|)
return|;
block|}
comment|/**      * Creates a new CCITT Fax compressed image XObject from the first image of a TIFF file. Only      * single-strip CCITT T4 or T6 compressed TIFF files are supported. If you're not sure what TIFF      * files you have, use      * {@link LosslessFactory#createFromImage(org.apache.pdfbox.pdmodel.PDDocument, java.awt.image.BufferedImage)}      * or {@link CCITTFactory#createFromImage(PDDocument, BufferedImage) }      * instead.      *      * @param document the document to create the image as part of.      * @param file the  TIFF file which contains a suitable CCITT compressed image      * @return a new Image XObject      * @throws IOException if there is an error reading the TIFF data.      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromFile
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createFromFile
argument_list|(
name|document
argument_list|,
name|file
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Creates a new CCITT Fax compressed image XObject from a specific image of a TIFF file. Only      * single-strip CCITT T4 or T6 compressed TIFF files are supported. If you're not sure what TIFF      * files you have, use      * {@link LosslessFactory#createFromImage(PDDocument, BufferedImage) }      * or {@link CCITTFactory#createFromImage(PDDocument, BufferedImage) }      * instead.      *      * @param document the document to create the image as part of.      * @param file the TIFF file which contains a suitable CCITT compressed image      * @param number TIFF image number, starting from 0      * @return a new Image XObject      * @throws IOException if there is an error reading the TIFF data.      */
specifier|public
specifier|static
name|PDImageXObject
name|createFromFile
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|File
name|file
parameter_list|,
name|int
name|number
parameter_list|)
throws|throws
name|IOException
block|{
name|RandomAccessFile
name|raf
init|=
operator|new
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"r"
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|createFromRandomAccessImpl
argument_list|(
name|document
argument_list|,
name|raf
argument_list|,
name|number
argument_list|)
return|;
block|}
finally|finally
block|{
name|raf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Creates a new CCITT Fax compressed image XObject from a TIFF file.      *       * @param document the document to create the image as part of.      * @param reader the random access TIFF file which contains a suitable CCITT      * compressed image      * @param number TIFF image number, starting from 0      * @return a new Image XObject, or null if no such page      * @throws IOException if there is an error reading the TIFF data.      */
specifier|private
specifier|static
name|PDImageXObject
name|createFromRandomAccessImpl
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|RandomAccess
name|reader
parameter_list|,
name|int
name|number
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
argument_list|,
name|number
argument_list|)
expr_stmt|;
if|if
condition|(
name|bos
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ByteArrayInputStream
name|encodedByteStream
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
name|encodedByteStream
argument_list|,
name|COSName
operator|.
name|CCITTFAX_DECODE
argument_list|,
name|decodeParms
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|COLUMNS
argument_list|)
argument_list|,
name|decodeParms
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|ROWS
argument_list|)
argument_list|,
literal|1
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|COSDictionary
name|dict
init|=
name|pdImage
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
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
parameter_list|,
name|int
name|number
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
name|int
name|address
init|=
name|readlong
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
decl_stmt|;
name|reader
operator|.
name|seek
argument_list|(
name|address
argument_list|)
expr_stmt|;
comment|// If some higher page number is required, skip this page's tags,
comment|// then read the next page's address
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|number
condition|;
name|i
operator|++
control|)
block|{
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
name|reader
operator|.
name|seek
argument_list|(
name|address
operator|+
literal|2
operator|+
name|numtags
operator|*
literal|12
argument_list|)
expr_stmt|;
name|address
operator|=
name|readlong
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
expr_stmt|;
if|if
condition|(
name|address
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|reader
operator|.
name|seek
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
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
comment|// Loop through the tags, some will convert to items in the params dictionary
comment|// Other point us to where to find the data stream.
comment|// The only param which might change as a result of other TIFF tags is K, so
comment|// we'll deal with that differently.
comment|// Default value to detect error
name|int
name|k
init|=
operator|-
literal|1000
decl_stmt|;
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
decl_stmt|;
comment|// Note that when the type is shorter than 4 bytes, the rest can be garbage
comment|// and must be ignored. E.g. short (2 bytes) from "01 00 38 32" (little endian)
comment|// is 1, not 842530817 (seen in a real-life TIFF image).
switch|switch
condition|(
name|type
condition|)
block|{
case|case
literal|1
case|:
comment|// byte value
name|val
operator|=
name|reader
operator|.
name|read
argument_list|()
expr_stmt|;
name|reader
operator|.
name|read
argument_list|()
expr_stmt|;
name|reader
operator|.
name|read
argument_list|()
expr_stmt|;
name|reader
operator|.
name|read
argument_list|()
expr_stmt|;
break|break;
case|case
literal|3
case|:
comment|// short value
name|val
operator|=
name|readshort
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|reader
operator|.
name|read
argument_list|()
expr_stmt|;
name|reader
operator|.
name|read
argument_list|()
expr_stmt|;
break|break;
default|default:
comment|// long and other types
name|val
operator|=
name|readlong
argument_list|(
name|endianess
argument_list|,
name|reader
argument_list|)
expr_stmt|;
break|break;
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
literal|266
case|:
block|{
if|if
condition|(
name|val
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"FillOrder "
operator|+
name|val
operator|+
literal|" is not supported"
argument_list|)
throw|;
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
literal|274
case|:
block|{
comment|// http://www.awaresystems.be/imaging/tiff/tifftags/orientation.html
if|if
condition|(
name|val
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Orientation "
operator|+
name|val
operator|+
literal|" is not supported"
argument_list|)
throw|;
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
operator|(
name|val
operator|&
literal|1
operator|)
operator|!=
literal|0
condition|)
block|{
comment|// T4 2D - arbitary positive K value
name|k
operator|=
literal|50
expr_stmt|;
block|}
comment|// http://www.awaresystems.be/imaging/tiff/tifftags/t4options.html
if|if
condition|(
operator|(
name|val
operator|&
literal|4
operator|)
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"CCITT Group 3 'uncompressed mode' is not supported"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|(
name|val
operator|&
literal|2
operator|)
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"CCITT Group 3 'fill bits before EOL' is not supported"
argument_list|)
throw|;
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

