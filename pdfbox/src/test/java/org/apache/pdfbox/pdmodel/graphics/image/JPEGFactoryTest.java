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
name|InputStream
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
import|import static
name|junit
operator|.
name|framework
operator|.
name|TestCase
operator|.
name|assertEquals
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|JPEGFactoryTest
extends|extends
name|TestCase
block|{
comment|/** {@inheritDoc} */
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test of createFromStream method, of class JPEGFactory.      */
annotation|@
name|Test
specifier|public
name|void
name|testCreateFromStream
parameter_list|()
throws|throws
name|Exception
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|InputStream
name|stream
init|=
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/pdfbox/pdmodel/graphics/image/jpeg.jpg"
argument_list|)
argument_list|)
decl_stmt|;
name|PDImageXObject
name|ximage
init|=
name|JPEGFactory
operator|.
name|createFromStream
argument_list|(
name|document
argument_list|,
name|stream
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
literal|"jpg"
argument_list|,
name|ximage
operator|.
name|getSuffix
argument_list|()
argument_list|)
expr_stmt|;
comment|//TODO shouldn't ximage.getImage() return a real image?
comment|//        assertNotNull(ximage.getImage());
comment|//        assertEquals(344, ximage.getImage().getWidth());
comment|//        assertEquals(287, ximage.getImage().getHeight());
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test of createFromImage method, of class JPEGFactory.      */
annotation|@
name|Test
specifier|public
name|void
name|testCreateFromImage
parameter_list|()
throws|throws
name|Exception
block|{
comment|//TODO enable this test when JPEGFactory.createFromImage() works
comment|//        PDDocument document = new PDDocument();
comment|//        BufferedImage bim = ImageIO.read(new File("src/test/resources/org/apache/pdfbox/pdmodel/graphics/image/jpeg.jpg"));
comment|//        PDImageXObject ximage = JPEGFactory.createFromImage(document, bim);
comment|//        assertNotNull(ximage);
comment|//        assertNotNull(ximage.getCOSStream());
comment|//        assertTrue(ximage.getCOSStream().getFilteredLength()> 0);
comment|//        assertEquals(8, ximage.getBitsPerComponent());
comment|//        assertEquals(344, ximage.getWidth());
comment|//        assertEquals(287, ximage.getHeight());
comment|//        assertEquals("jpg", ximage.getSuffix());
comment|//TODO shouldn't ximage.getImage() return a real image?
comment|//        assertNotNull(ximage.getImage());
comment|//        assertEquals(344, ximage.getImage().getWidth());
comment|//        assertEquals(287, ximage.getImage().getHeight());
comment|//        document.close();
block|}
block|}
end_class

end_unit

