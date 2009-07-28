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
name|xobject
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
name|InputStream
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
name|ByteArrayInputStream
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
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|IIOException
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
name|PDDeviceRGB
import|;
end_import

begin_comment
comment|/**  * An image class for JPegs.  *  * @author mathiak  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|PDJpeg
extends|extends
name|PDXObjectImage
block|{
specifier|private
specifier|static
specifier|final
name|List
name|DCT_FILTERS
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
static|static
block|{
name|DCT_FILTERS
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|DCT_DECODE
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|DCT_FILTERS
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|DCT_DECODE_ABBREVIATION
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Standard constructor.      *      * @param jpeg The COSStream from which to extract the JPeg      */
specifier|public
name|PDJpeg
parameter_list|(
name|PDStream
name|jpeg
parameter_list|)
block|{
name|super
argument_list|(
name|jpeg
argument_list|,
literal|"jpg"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct from a stream.      *      * @param doc The document to create the image as part of.      * @param is The stream that contains the jpeg data.      * @throws IOException If there is an error reading the jpeg data.      */
specifier|public
name|PDJpeg
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|,
name|is
argument_list|,
literal|true
argument_list|)
argument_list|,
literal|"jpg"
argument_list|)
expr_stmt|;
name|COSDictionary
name|dic
init|=
name|getCOSStream
argument_list|()
decl_stmt|;
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|,
name|COSName
operator|.
name|DCT_DECODE
argument_list|)
expr_stmt|;
name|dic
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
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"XObject"
argument_list|)
argument_list|)
expr_stmt|;
name|BufferedImage
name|image
init|=
name|getRGBImage
argument_list|()
decl_stmt|;
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
name|setBitsPerComponent
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|setColorSpace
argument_list|(
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|setHeight
argument_list|(
name|image
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|setWidth
argument_list|(
name|image
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Construct from a buffered image.      *      * @param doc The document to create the image as part of.      * @param bi The image to convert to a jpeg      * @throws IOException If there is an error processing the jpeg data.      */
specifier|public
name|PDJpeg
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|BufferedImage
name|bi
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|)
argument_list|,
literal|"jpg"
argument_list|)
expr_stmt|;
name|java
operator|.
name|io
operator|.
name|OutputStream
name|os
init|=
name|getCOSStream
argument_list|()
operator|.
name|createFilteredStream
argument_list|()
decl_stmt|;
try|try
block|{
name|ImageIO
operator|.
name|write
argument_list|(
name|bi
argument_list|,
literal|"jpeg"
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|COSDictionary
name|dic
init|=
name|getCOSStream
argument_list|()
decl_stmt|;
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|,
name|COSName
operator|.
name|DCT_DECODE
argument_list|)
expr_stmt|;
name|dic
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
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"XObject"
argument_list|)
argument_list|)
expr_stmt|;
name|setBitsPerComponent
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|setColorSpace
argument_list|(
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|setHeight
argument_list|(
name|bi
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|setWidth
argument_list|(
name|bi
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
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
comment|/**      * Returns an image of the JPeg, or null if JPegs are not supported. (They should be. )      * {@inheritDoc}      */
specifier|public
name|BufferedImage
name|getRGBImage
parameter_list|()
throws|throws
name|IOException
block|{
comment|//TODO PKOCH
name|File
name|imgFile
init|=
literal|null
decl_stmt|;
name|BufferedImage
name|bi
init|=
literal|null
decl_stmt|;
name|boolean
name|readError
init|=
literal|false
decl_stmt|;
try|try
block|{
name|imgFile
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"pdjpeg"
argument_list|,
literal|".jpeg"
argument_list|)
expr_stmt|;
name|write2file
argument_list|(
name|imgFile
argument_list|)
expr_stmt|;
comment|// 1. try to read jpeg image
try|try
block|{
name|bi
operator|=
name|ImageIO
operator|.
name|read
argument_list|(
name|imgFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IIOException
name|iioe
parameter_list|)
block|{
comment|// cannot read jpeg
name|readError
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{}
comment|// 2. try to read jpeg again. some jpegs have some strange header containing
comment|//    "Adobe " at some place. so just replace the header with a valid jpeg header.
comment|// TODO : not sure if it works for all cases
if|if
condition|(
name|bi
operator|==
literal|null
operator|&&
name|readError
condition|)
block|{
name|byte
index|[]
name|newImage
init|=
name|replaceHeader
argument_list|(
name|imgFile
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|bai
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|newImage
argument_list|)
decl_stmt|;
comment|// persist file temporarely, because i was not able to manage
comment|// to call the ImageIO.read(InputStream) successfully.
name|FileOutputStream
name|o
init|=
operator|new
name|FileOutputStream
argument_list|(
name|imgFile
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|512
index|]
decl_stmt|;
name|int
name|read
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|bai
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|o
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|bai
operator|.
name|close
argument_list|()
expr_stmt|;
name|o
operator|.
name|close
argument_list|()
expr_stmt|;
name|bi
operator|=
name|ImageIO
operator|.
name|read
argument_list|(
name|imgFile
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|imgFile
operator|!=
literal|null
condition|)
block|{
name|imgFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|bi
return|;
block|}
comment|/**      * This writes the JPeg to out.      * {@inheritDoc}      */
specifier|public
name|void
name|write2OutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|data
init|=
name|getPDStream
argument_list|()
operator|.
name|getPartiallyFilteredStream
argument_list|(
name|DCT_FILTERS
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|1024
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
name|data
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|out
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
comment|/**      * Returns the given file as byte array.      * @param file File to be read      * @return given file as byte array      * @throws IOException if somethin went wrong during reading the file      */
specifier|public
specifier|static
name|byte
index|[]
name|getBytesFromFile
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|long
name|length
init|=
name|file
operator|.
name|length
argument_list|()
decl_stmt|;
if|if
condition|(
name|length
operator|>
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
comment|// File is too large
throw|throw
operator|new
name|IOException
argument_list|(
literal|"File is tooo large"
argument_list|)
throw|;
block|}
comment|// Create the byte array to hold the data
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|length
index|]
decl_stmt|;
comment|// Read in the bytes
name|int
name|offset
init|=
literal|0
decl_stmt|;
name|int
name|numRead
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|offset
operator|<
name|bytes
operator|.
name|length
operator|&&
operator|(
name|numRead
operator|=
name|is
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
name|offset
argument_list|,
name|bytes
operator|.
name|length
operator|-
name|offset
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|offset
operator|+=
name|numRead
expr_stmt|;
block|}
comment|// Ensure all the bytes have been read in
if|if
condition|(
name|offset
operator|<
name|bytes
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not completely read file "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bytes
return|;
block|}
specifier|private
name|int
name|getHeaderEndPos
parameter_list|(
name|byte
index|[]
name|image
parameter_list|)
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
name|image
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|byte
name|b
init|=
name|image
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|b
operator|==
operator|(
name|byte
operator|)
literal|0xDB
condition|)
block|{
comment|// TODO : check for ff db
return|return
name|i
operator|-
literal|2
return|;
block|}
block|}
return|return
literal|0
return|;
block|}
specifier|private
name|byte
index|[]
name|replaceHeader
parameter_list|(
name|File
name|jpegFile
parameter_list|)
throws|throws
name|IOException
block|{
comment|// read image into memory
name|byte
index|[]
name|image
init|=
name|getBytesFromFile
argument_list|(
name|jpegFile
argument_list|)
decl_stmt|;
comment|// get end position of wrong header respectively startposition of "real jpeg data"
name|int
name|pos
init|=
name|getHeaderEndPos
argument_list|(
name|image
argument_list|)
decl_stmt|;
comment|// simple correct header
name|byte
index|[]
name|header
init|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0xFF
block|,
operator|(
name|byte
operator|)
literal|0xD8
block|,
operator|(
name|byte
operator|)
literal|0xFF
block|,
operator|(
name|byte
operator|)
literal|0xE0
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x10
block|,
operator|(
name|byte
operator|)
literal|0x4A
block|,
operator|(
name|byte
operator|)
literal|0x46
block|,
operator|(
name|byte
operator|)
literal|0x49
block|,
operator|(
name|byte
operator|)
literal|0x46
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x01
block|,
operator|(
name|byte
operator|)
literal|0x01
block|,
operator|(
name|byte
operator|)
literal|0x01
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x60
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x60
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0x00
block|}
decl_stmt|;
comment|// concat
name|byte
index|[]
name|newImage
init|=
operator|new
name|byte
index|[
name|image
operator|.
name|length
operator|-
name|pos
operator|+
name|header
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|header
argument_list|,
literal|0
argument_list|,
name|newImage
argument_list|,
literal|0
argument_list|,
name|header
operator|.
name|length
argument_list|)
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|image
argument_list|,
name|pos
operator|+
literal|1
argument_list|,
name|newImage
argument_list|,
name|header
operator|.
name|length
argument_list|,
name|image
operator|.
name|length
operator|-
name|pos
operator|-
literal|1
argument_list|)
expr_stmt|;
return|return
name|newImage
return|;
block|}
block|}
end_class

end_unit

