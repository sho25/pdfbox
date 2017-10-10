begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2017 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|interactive
operator|.
name|action
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
name|COSString
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
name|assertEquals
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
name|assertNull
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

begin_comment
comment|/**  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PDActionURITest
block|{
comment|/**      * PDFBOX-3913: Check that URIs encoded in UTF-8 are also supported.      * PDFBOX-3946: Check that there is no NPE if URI missing.      */
annotation|@
name|Test
specifier|public
name|void
name|testUTF8URI
parameter_list|()
block|{
name|PDActionURI
name|actionURI
init|=
operator|new
name|PDActionURI
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|actionURI
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
name|actionURI
operator|.
name|setURI
argument_list|(
literal|"http://çµ„åŒ¶æ›¿ç¶Ž.com/"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://経営承継.com/"
argument_list|,
name|actionURI
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-3913: Check that URIs encoded in UTF16 (BE) are also supported.      *      * @throws IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testUTF16BEURI
parameter_list|()
throws|throws
name|IOException
block|{
name|PDActionURI
name|actionURI
init|=
operator|new
name|PDActionURI
argument_list|()
decl_stmt|;
comment|// found in govdocs file 534948.pdf
name|COSString
name|utf16URI
init|=
name|COSString
operator|.
name|parseHex
argument_list|(
literal|"FEFF0068007400740070003A002F002F00770077"
operator|+
literal|"0077002E006E00610070002E006500640075002F0063006100740061006C006F006700"
operator|+
literal|"2F00310031003100340030002E00680074006D006C"
argument_list|)
decl_stmt|;
name|actionURI
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|URI
argument_list|,
name|utf16URI
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.nap.edu/catalog/11140.html"
argument_list|,
name|actionURI
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * PDFBOX-3913: Check that URIs encoded in UTF16 (LE) are also supported.      *       * @throws IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testUTF16LEURI
parameter_list|()
throws|throws
name|IOException
block|{
name|PDActionURI
name|actionURI
init|=
operator|new
name|PDActionURI
argument_list|()
decl_stmt|;
name|COSString
name|utf16URI
init|=
name|COSString
operator|.
name|parseHex
argument_list|(
literal|"FFFE68007400740070003A00"
argument_list|)
decl_stmt|;
name|actionURI
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|URI
argument_list|,
name|utf16URI
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http:"
argument_list|,
name|actionURI
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUTF7URI
parameter_list|()
block|{
name|PDActionURI
name|actionURI
init|=
operator|new
name|PDActionURI
argument_list|()
decl_stmt|;
name|actionURI
operator|.
name|setURI
argument_list|(
literal|"http://pdfbox.apache.org/"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://pdfbox.apache.org/"
argument_list|,
name|actionURI
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

