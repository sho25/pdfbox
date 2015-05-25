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
name|pdmodel
operator|.
name|graphics
operator|.
name|color
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
name|pdmodel
operator|.
name|PDDocument
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
name|assertNotNull
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
name|PDICCBasedTest
block|{
specifier|public
name|PDICCBasedTest
parameter_list|()
block|{     }
comment|/**      * Test of Constructor for PDFBOX-2812.      */
annotation|@
name|Test
specifier|public
name|void
name|testConstructor
parameter_list|()
block|{
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|PDICCBased
name|iccBased
init|=
operator|new
name|PDICCBased
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ICCBased"
argument_list|,
name|iccBased
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|iccBased
operator|.
name|getPDStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

