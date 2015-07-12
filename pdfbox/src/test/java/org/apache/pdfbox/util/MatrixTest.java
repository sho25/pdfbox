begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2015 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|util
package|;
end_package

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
name|COSArray
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
name|COSFloat
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
name|MatrixTest
block|{
comment|/**      * Test of PDFBOX-2872 bug      */
annotation|@
name|Test
specifier|public
name|void
name|testPdfbox2872
parameter_list|()
block|{
name|Matrix
name|m
init|=
operator|new
name|Matrix
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|,
literal|5
argument_list|,
literal|8
argument_list|,
literal|2
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|COSArray
name|toCOSArray
init|=
name|m
operator|.
name|toCOSArray
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|2
argument_list|)
argument_list|,
name|toCOSArray
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|4
argument_list|)
argument_list|,
name|toCOSArray
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|5
argument_list|)
argument_list|,
name|toCOSArray
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|8
argument_list|)
argument_list|,
name|toCOSArray
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|2
argument_list|)
argument_list|,
name|toCOSArray
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0
argument_list|)
argument_list|,
name|toCOSArray
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

