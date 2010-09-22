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
name|COSDictionary
import|;
end_import

begin_comment
comment|/**  * Modeled on the JBIG2Decode filter.  *  * thanks to Timo Böhme<timo.boehme@ontochem.com>  */
end_comment

begin_class
specifier|public
class|class
name|JBIG2Filter
implements|implements
name|Filter
block|{
comment|/** Log instance */
specifier|private
specifier|static
specifier|final
name|Log
name|log
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
comment|/**      * Decode JBIG2 data using Java ImageIO library.      *       * {@inheritDoc}      *       */
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
name|bi
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|compressedData
argument_list|)
decl_stmt|;
if|if
condition|(
name|bi
operator|!=
literal|null
condition|)
block|{
name|DataBuffer
name|dBuf
init|=
name|bi
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
name|result
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
name|log
operator|.
name|error
argument_list|(
literal|"Image data buffer not of type byte but type "
operator|+
name|dBuf
operator|.
name|getDataType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Warning: JBIG2.encode is not implemented yet, skipping this stream."
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

