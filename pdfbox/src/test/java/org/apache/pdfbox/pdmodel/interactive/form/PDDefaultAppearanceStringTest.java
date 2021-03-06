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
operator|.
name|interactive
operator|.
name|form
package|;
end_package

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
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDResources
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
name|font
operator|.
name|PDType1Font
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_class
specifier|public
class|class
name|PDDefaultAppearanceStringTest
block|{
comment|// Used to check resources lookup
specifier|private
name|PDResources
name|resources
decl_stmt|;
specifier|private
name|COSName
name|fontResourceName
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|resources
operator|=
operator|new
name|PDResources
argument_list|()
expr_stmt|;
comment|// the resource name is created when the font is added so need
comment|// to capture that
name|fontResourceName
operator|=
name|resources
operator|.
name|add
argument_list|(
name|PDType1Font
operator|.
name|HELVETICA
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testParseDAString
parameter_list|()
throws|throws
name|IOException
block|{
name|COSString
name|sampleString
init|=
operator|new
name|COSString
argument_list|(
literal|"/"
operator|+
name|fontResourceName
operator|.
name|getName
argument_list|()
operator|+
literal|" 12 Tf 0.019 0.305 0.627 rg"
argument_list|)
decl_stmt|;
name|PDDefaultAppearanceString
name|defaultAppearanceString
init|=
operator|new
name|PDDefaultAppearanceString
argument_list|(
name|sampleString
argument_list|,
name|resources
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|defaultAppearanceString
operator|.
name|getFontSize
argument_list|()
argument_list|,
literal|0.001
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PDType1Font
operator|.
name|HELVETICA
argument_list|,
name|defaultAppearanceString
operator|.
name|getFont
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|,
name|defaultAppearanceString
operator|.
name|getFontColor
argument_list|()
operator|.
name|getColorSpace
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.019
argument_list|,
name|defaultAppearanceString
operator|.
name|getFontColor
argument_list|()
operator|.
name|getComponents
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|0.0001
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.305
argument_list|,
name|defaultAppearanceString
operator|.
name|getFontColor
argument_list|()
operator|.
name|getComponents
argument_list|()
index|[
literal|1
index|]
argument_list|,
literal|0.0001
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0.627
argument_list|,
name|defaultAppearanceString
operator|.
name|getFontColor
argument_list|()
operator|.
name|getComponents
argument_list|()
index|[
literal|2
index|]
argument_list|,
literal|0.0001
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IOException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testFontResourceUnavailable
parameter_list|()
throws|throws
name|IOException
block|{
name|COSString
name|sampleString
init|=
operator|new
name|COSString
argument_list|(
literal|"/Helvetica 12 Tf 0.019 0.305 0.627 rg"
argument_list|)
decl_stmt|;
operator|new
name|PDDefaultAppearanceString
argument_list|(
name|sampleString
argument_list|,
name|resources
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IOException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testWrongNumberOfColorArguments
parameter_list|()
throws|throws
name|IOException
block|{
name|COSString
name|sampleString
init|=
operator|new
name|COSString
argument_list|(
literal|"/Helvetica 12 Tf 0.305 0.627 rg"
argument_list|)
decl_stmt|;
operator|new
name|PDDefaultAppearanceString
argument_list|(
name|sampleString
argument_list|,
name|resources
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

