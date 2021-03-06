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
name|preflight
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|parser
operator|.
name|PreflightParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|TestPDFBox3741
block|{
comment|/**      * Test whether use of default colorspace without output intent for text output is detected.      *      * @throws IOException      */
annotation|@
name|Test
specifier|public
name|void
name|testPDFBox3741
parameter_list|()
throws|throws
name|IOException
block|{
name|ValidationResult
name|result
init|=
name|PreflightParser
operator|.
name|validate
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/PDFBOX-3741.pdf"
argument_list|)
argument_list|)
decl_stmt|;
comment|// Error should be:
comment|// 2.4.3: Invalid Color space, /DeviceGray default for operator "Tj" can't be used without Color Profile
name|Assert
operator|.
name|assertFalse
argument_list|(
literal|"File PDFBOX-3741.pdf should be detected as not PDF/A-1b"
argument_list|,
name|result
operator|.
name|isValid
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"List should contain one result"
argument_list|,
literal|1
argument_list|,
name|result
operator|.
name|getErrorsList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"2.4.3"
argument_list|,
name|result
operator|.
name|getErrorsList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

