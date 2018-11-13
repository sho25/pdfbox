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
name|pdmodel
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
name|PageMode
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
name|Rule
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
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|Timeout
import|;
end_import

begin_class
specifier|public
class|class
name|PageModeTest
block|{
annotation|@
name|Rule
specifier|public
name|ExpectedException
name|thrown
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|fromStringInputNotNullOutputNotNull
parameter_list|()
block|{
comment|// Arrange
specifier|final
name|String
name|value
init|=
literal|"FullScreen"
decl_stmt|;
comment|// Act
specifier|final
name|PageMode
name|retval
init|=
name|PageMode
operator|.
name|fromString
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// Assert result
name|Assert
operator|.
name|assertEquals
argument_list|(
name|PageMode
operator|.
name|FULL_SCREEN
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|fromStringInputNotNullOutputNotNull2
parameter_list|()
block|{
comment|// Arrange
specifier|final
name|String
name|value
init|=
literal|"UseThumbs"
decl_stmt|;
comment|// Act
specifier|final
name|PageMode
name|retval
init|=
name|PageMode
operator|.
name|fromString
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// Assert result
name|Assert
operator|.
name|assertEquals
argument_list|(
name|PageMode
operator|.
name|USE_THUMBS
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|fromStringInputNotNullOutputNotNull3
parameter_list|()
block|{
comment|// Arrange
specifier|final
name|String
name|value
init|=
literal|"UseOC"
decl_stmt|;
comment|// Act
specifier|final
name|PageMode
name|retval
init|=
name|PageMode
operator|.
name|fromString
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// Assert result
name|Assert
operator|.
name|assertEquals
argument_list|(
name|PageMode
operator|.
name|USE_OPTIONAL_CONTENT
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|fromStringInputNotNullOutputNotNull4
parameter_list|()
block|{
comment|// Arrange
specifier|final
name|String
name|value
init|=
literal|"UseNone"
decl_stmt|;
comment|// Act
specifier|final
name|PageMode
name|retval
init|=
name|PageMode
operator|.
name|fromString
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// Assert result
name|Assert
operator|.
name|assertEquals
argument_list|(
name|PageMode
operator|.
name|USE_NONE
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|fromStringInputNotNullOutputNotNull5
parameter_list|()
block|{
comment|// Arrange
specifier|final
name|String
name|value
init|=
literal|"UseAttachments"
decl_stmt|;
comment|// Act
specifier|final
name|PageMode
name|retval
init|=
name|PageMode
operator|.
name|fromString
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// Assert result
name|Assert
operator|.
name|assertEquals
argument_list|(
name|PageMode
operator|.
name|USE_ATTACHMENTS
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|fromStringInputNotNullOutputNotNull6
parameter_list|()
block|{
comment|// Arrange
specifier|final
name|String
name|value
init|=
literal|"UseOutlines"
decl_stmt|;
comment|// Act
specifier|final
name|PageMode
name|retval
init|=
name|PageMode
operator|.
name|fromString
argument_list|(
name|value
argument_list|)
decl_stmt|;
comment|// Assert result
name|Assert
operator|.
name|assertEquals
argument_list|(
name|PageMode
operator|.
name|USE_OUTLINES
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|fromStringInputNotNullOutputIllegalArgumentException
parameter_list|()
block|{
comment|// Arrange
specifier|final
name|String
name|value
init|=
literal|""
decl_stmt|;
comment|// Act
name|thrown
operator|.
name|expect
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
expr_stmt|;
name|PageMode
operator|.
name|fromString
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// Method is not expected to return due to exception thrown
block|}
annotation|@
name|Test
specifier|public
name|void
name|fromStringInputNotNullOutputIllegalArgumentException2
parameter_list|()
block|{
comment|// Arrange
specifier|final
name|String
name|value
init|=
literal|"Dulacb`ecj"
decl_stmt|;
comment|// Act
name|thrown
operator|.
name|expect
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
expr_stmt|;
name|PageMode
operator|.
name|fromString
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// Method is not expected to return due to exception thrown
block|}
annotation|@
name|Test
specifier|public
name|void
name|stringValueOutputNotNull
parameter_list|()
block|{
comment|// Arrange
specifier|final
name|PageMode
name|objectUnderTest
init|=
name|PageMode
operator|.
name|USE_OPTIONAL_CONTENT
decl_stmt|;
comment|// Act
specifier|final
name|String
name|retval
init|=
name|objectUnderTest
operator|.
name|stringValue
argument_list|()
decl_stmt|;
comment|// Assert result
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"UseOC"
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

