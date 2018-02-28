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
name|filter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics
import|;
end_import

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
name|awt
operator|.
name|image
operator|.
name|DataBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|DataBufferByte
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
name|SequenceInputStream
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
name|ImageReader
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
name|ImageInputStream
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

begin_comment
comment|/**  * Decompresses data encoded using the JBIG2 standard, reproducing the original  * monochrome (1 bit per pixel) image data (or an approximation of that data).  *  * Requires a JBIG2 plugin for Java Image I/O to be installed. A known working  * plug-in is the Apache PDFBox JBIG2 plugin.  *  * @author Timo Boehme  */
end_comment

begin_class
specifier|final
class|class
name|JBIG2Filter
extends|extends
name|Filter
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
name|JBIG2Filter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|levigoLogged
init|=
literal|false
decl_stmt|;
specifier|private
specifier|static
specifier|synchronized
name|void
name|logLevigoDonated
parameter_list|()
block|{
if|if
condition|(
operator|!
name|levigoLogged
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"The Levigo JBIG2 plugin has been donated to the Apache Foundation"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"and an improved version is available for download at "
operator|+
literal|"https://pdfbox.apache.org/download.cgi"
argument_list|)
expr_stmt|;
name|levigoLogged
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|DecodeResult
name|decode
parameter_list|(
name|InputStream
name|encoded
parameter_list|,
name|OutputStream
name|decoded
parameter_list|,
name|COSDictionary
name|parameters
parameter_list|,
name|int
name|index
parameter_list|)
throws|throws
name|IOException
block|{
name|ImageReader
name|reader
init|=
name|findImageReader
argument_list|(
literal|"JBIG2"
argument_list|,
literal|"jbig2-imageio is not installed"
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"levigo"
argument_list|)
condition|)
block|{
name|logLevigoDonated
argument_list|()
expr_stmt|;
block|}
name|int
name|bits
init|=
name|parameters
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|BITS_PER_COMPONENT
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|COSDictionary
name|params
init|=
name|getDecodeParams
argument_list|(
name|parameters
argument_list|,
name|index
argument_list|)
decl_stmt|;
name|InputStream
name|source
init|=
name|encoded
decl_stmt|;
if|if
condition|(
name|params
operator|!=
literal|null
condition|)
block|{
name|COSBase
name|globals
init|=
name|params
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|JBIG2_GLOBALS
argument_list|)
decl_stmt|;
if|if
condition|(
name|globals
operator|instanceof
name|COSStream
condition|)
block|{
name|source
operator|=
operator|new
name|SequenceInputStream
argument_list|(
operator|(
operator|(
name|COSStream
operator|)
name|globals
operator|)
operator|.
name|createInputStream
argument_list|()
argument_list|,
name|encoded
argument_list|)
expr_stmt|;
block|}
block|}
try|try
init|(
name|ImageInputStream
name|iis
init|=
name|ImageIO
operator|.
name|createImageInputStream
argument_list|(
name|source
argument_list|)
init|)
block|{
name|reader
operator|.
name|setInput
argument_list|(
name|iis
argument_list|)
expr_stmt|;
name|BufferedImage
name|image
decl_stmt|;
try|try
block|{
name|image
operator|=
name|reader
operator|.
name|read
argument_list|(
literal|0
argument_list|,
name|reader
operator|.
name|getDefaultReadParam
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// wrap and rethrow any exceptions
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not read JBIG2 image"
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// I am assuming since JBIG2 is always black and white
comment|// depending on your renderer this might or might be needed
if|if
condition|(
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getPixelSize
argument_list|()
operator|!=
name|bits
condition|)
block|{
if|if
condition|(
name|bits
operator|!=
literal|1
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Attempting to handle a JBIG2 with more than 1-bit depth"
argument_list|)
expr_stmt|;
block|}
name|BufferedImage
name|packedImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|image
operator|.
name|getWidth
argument_list|()
argument_list|,
name|image
operator|.
name|getHeight
argument_list|()
argument_list|,
name|BufferedImage
operator|.
name|TYPE_BYTE_BINARY
argument_list|)
decl_stmt|;
name|Graphics
name|graphics
init|=
name|packedImage
operator|.
name|getGraphics
argument_list|()
decl_stmt|;
name|graphics
operator|.
name|drawImage
argument_list|(
name|image
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|image
operator|=
name|packedImage
expr_stmt|;
block|}
name|DataBuffer
name|dBuf
init|=
name|image
operator|.
name|getData
argument_list|()
operator|.
name|getDataBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
name|dBuf
operator|.
name|getDataType
argument_list|()
operator|==
name|DataBuffer
operator|.
name|TYPE_BYTE
condition|)
block|{
name|decoded
operator|.
name|write
argument_list|(
operator|(
operator|(
name|DataBufferByte
operator|)
name|dBuf
operator|)
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unexpected image buffer type"
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|reader
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|DecodeResult
argument_list|(
name|parameters
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|encode
parameter_list|(
name|InputStream
name|input
parameter_list|,
name|OutputStream
name|encoded
parameter_list|,
name|COSDictionary
name|parameters
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"JBIG2 encoding not implemented"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

