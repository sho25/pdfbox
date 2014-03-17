begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Color
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
name|PDDeviceRGB
import|;
end_import

begin_comment
comment|/**  * Factory for creating a PDImageXObject containing a lossless compressed image.  *   * @author Tilman Hausherr  */
end_comment

begin_class
class|class
name|LosslessFactory
block|{
comment|/**      * Creates a new lossless encoded Image XObject from a Buffered Image.      *      * @param document the document where the image will be created      * @param image the buffered image to embed      * @return a new Image XObject      * @throws IOException if something goes wrong      */
specifier|static
name|PDImageXObject
name|createLosslessFromImage
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|BufferedImage
name|bim
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
comment|//TODO use bit writing, indexed, etc
name|int
name|h
init|=
name|bim
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|w
init|=
name|bim
operator|.
name|getWidth
argument_list|()
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
name|h
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
name|w
condition|;
operator|++
name|x
control|)
block|{
name|Color
name|co
init|=
operator|new
name|Color
argument_list|(
name|bim
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|)
decl_stmt|;
name|bos
operator|.
name|write
argument_list|(
name|co
operator|.
name|getRed
argument_list|()
argument_list|)
expr_stmt|;
name|bos
operator|.
name|write
argument_list|(
name|co
operator|.
name|getGreen
argument_list|()
argument_list|)
expr_stmt|;
name|bos
operator|.
name|write
argument_list|(
name|co
operator|.
name|getBlue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ByteArrayInputStream
name|bais
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
name|FLATE_DECODE
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos2
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|filter
operator|.
name|encode
argument_list|(
name|bais
argument_list|,
name|bos2
argument_list|,
operator|new
name|COSDictionary
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|filteredByteStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos2
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
name|filteredByteStream
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
name|FLATE_DECODE
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setColorSpace
argument_list|(
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
comment|//TODO from image
name|pdImage
operator|.
name|setBitsPerComponent
argument_list|(
literal|8
argument_list|)
expr_stmt|;
comment|//TODO other sizes
name|pdImage
operator|.
name|setHeight
argument_list|(
name|bim
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|pdImage
operator|.
name|setWidth
argument_list|(
name|bim
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|pdImage
return|;
block|}
block|}
end_class

end_unit

