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
name|awt
operator|.
name|image
operator|.
name|WritableRaster
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
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|PDJPXColorSpace
import|;
end_import

begin_comment
comment|/**  * Decompress data encoded using the wavelet-based JPEG 2000 standard,  * reproducing the original data.  *  * Requires the Java Advanced Imaging (JAI) Image I/O Tools to be installed from java.net, see  *<a href="http://download.java.net/media/jai-imageio/builds/release/1.1/">jai-imageio</a>.  * Alternatively you can build from the source available in the  *<a href="https://java.net/projects/jai-imageio-core/">jai-imageio-core svn repo</a>.  *  * Mac OS X users should download the tar.gz file for linux and unpack it to obtain the  * required jar files. The .so file can be safely ignored.  *  * @author John Hewson  * @author Timo Boehme  */
end_comment

begin_class
specifier|public
class|class
name|JPXFilter
implements|implements
name|Filter
block|{
comment|/**      * Decode JPEG 2000 data using Java ImageIO library.      *      * {@inheritDoc}      */
specifier|public
name|void
name|decode
parameter_list|(
name|InputStream
name|compressedData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedImage
name|image
init|=
name|readJPX
argument_list|(
name|compressedData
argument_list|)
decl_stmt|;
name|WritableRaster
name|raster
init|=
name|image
operator|.
name|getRaster
argument_list|()
decl_stmt|;
if|if
condition|(
name|raster
operator|.
name|getDataBuffer
argument_list|()
operator|.
name|getDataType
argument_list|()
operator|!=
name|DataBuffer
operator|.
name|TYPE_BYTE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not implemented: greater than 8-bit depth"
argument_list|)
throw|;
block|}
name|DataBufferByte
name|buffer
init|=
operator|(
name|DataBufferByte
operator|)
name|raster
operator|.
name|getDataBuffer
argument_list|()
decl_stmt|;
name|result
operator|.
name|write
argument_list|(
name|buffer
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|BufferedImage
name|readJPX
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
comment|// try to read using JAI Image I/O
name|ImageIO
operator|.
name|setUseCache
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|BufferedImage
name|image
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|input
argument_list|)
decl_stmt|;
if|if
condition|(
name|image
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MissingImageReaderException
argument_list|(
literal|"Cannot read JPEG 2000 (JPX) image: "
operator|+
literal|"Java Advanced Imaging (JAI) Image I/O Tools are not installed"
argument_list|)
throw|;
block|}
return|return
name|image
return|;
block|}
comment|/**      * Returns the embedded color space from a JPX file.      * @param input The JPX input stream      */
comment|// TODO this method is something of a hack, we'd rather be able to return info from decode(...)
specifier|public
specifier|static
name|PDColorSpace
name|getColorSpace
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|BufferedImage
name|image
init|=
name|readJPX
argument_list|(
name|input
argument_list|)
decl_stmt|;
return|return
operator|new
name|PDJPXColorSpace
argument_list|(
name|image
operator|.
name|getColorModel
argument_list|()
operator|.
name|getColorSpace
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|encode
parameter_list|(
name|InputStream
name|rawData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"JPX encoding not implemented"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

