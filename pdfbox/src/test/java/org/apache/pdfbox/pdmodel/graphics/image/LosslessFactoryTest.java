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
name|IOException
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|util
operator|.
name|ImageIOUtil
import|;
end_import

begin_comment
comment|/**  * Unit tests for JPEGFactory  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|LosslessFactoryTest
extends|extends
name|TestCase
block|{
comment|/**      * Tests LosslessFactoryTest#createFromImage(PDDocument document,      * BufferedImage image)      */
specifier|public
name|void
name|testCreateLosslessFromImage
parameter_list|()
throws|throws
name|IOException
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|BufferedImage
name|image
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|JPEGFactoryTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"png.png"
argument_list|)
argument_list|)
decl_stmt|;
name|PDImageXObject
name|ximage
init|=
name|LosslessFactory
operator|.
name|createFromImage
argument_list|(
name|document
argument_list|,
name|image
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ximage
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ximage
operator|.
name|getCOSStream
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ximage
operator|.
name|getCOSStream
argument_list|()
operator|.
name|getFilteredLength
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|ximage
operator|.
name|getBitsPerComponent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|344
argument_list|,
name|ximage
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|287
argument_list|,
name|ximage
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"png"
argument_list|,
name|ximage
operator|.
name|getSuffix
argument_list|()
argument_list|)
expr_stmt|;
comment|// check the image
name|assertNotNull
argument_list|(
name|ximage
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ximage
operator|.
name|getWidth
argument_list|()
argument_list|,
name|ximage
operator|.
name|getImage
argument_list|()
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ximage
operator|.
name|getHeight
argument_list|()
argument_list|,
name|ximage
operator|.
name|getImage
argument_list|()
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|writeOk
init|=
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|ximage
operator|.
name|getImage
argument_list|()
argument_list|,
literal|"png"
argument_list|,
operator|new
name|NullOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|writeOk
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

