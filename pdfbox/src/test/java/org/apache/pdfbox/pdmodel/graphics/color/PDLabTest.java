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
name|color
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|pdmodel
operator|.
name|common
operator|.
name|PDRange
import|;
end_import

begin_comment
comment|/**  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PDLabTest
extends|extends
name|TestCase
block|{
comment|/**      * This test checks that getting default values do not alter the object,       * and checks getters and setters.      */
specifier|public
name|void
name|testLAB
parameter_list|()
block|{
name|PDLab
name|pdLab
init|=
operator|new
name|PDLab
argument_list|()
decl_stmt|;
name|COSArray
name|cosArray
init|=
operator|(
name|COSArray
operator|)
name|pdLab
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|cosArray
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// test with default values
name|assertEquals
argument_list|(
literal|"Lab"
argument_list|,
name|pdLab
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|pdLab
operator|.
name|getNumberOfComponents
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|pdLab
operator|.
name|getInitialColor
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|,
name|pdLab
operator|.
name|getInitialColor
argument_list|()
operator|.
name|getComponents
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0f
argument_list|,
name|pdLab
operator|.
name|getBlackPoint
argument_list|()
operator|.
name|getX
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0f
argument_list|,
name|pdLab
operator|.
name|getBlackPoint
argument_list|()
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0f
argument_list|,
name|pdLab
operator|.
name|getBlackPoint
argument_list|()
operator|.
name|getZ
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1f
argument_list|,
name|pdLab
operator|.
name|getWhitepoint
argument_list|()
operator|.
name|getX
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1f
argument_list|,
name|pdLab
operator|.
name|getWhitepoint
argument_list|()
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1f
argument_list|,
name|pdLab
operator|.
name|getWhitepoint
argument_list|()
operator|.
name|getZ
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|100f
argument_list|,
name|pdLab
operator|.
name|getARange
argument_list|()
operator|.
name|getMin
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100f
argument_list|,
name|pdLab
operator|.
name|getARange
argument_list|()
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|100f
argument_list|,
name|pdLab
operator|.
name|getBRange
argument_list|()
operator|.
name|getMin
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100f
argument_list|,
name|pdLab
operator|.
name|getBRange
argument_list|()
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"read operations should not change the size of /Lab objects"
argument_list|,
literal|0
argument_list|,
name|dict
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|dict
operator|.
name|toString
argument_list|()
expr_stmt|;
comment|// rev 1571125 did a stack overflow here
comment|// test setting specific values
name|PDRange
name|pdRange
init|=
operator|new
name|PDRange
argument_list|()
decl_stmt|;
name|pdRange
operator|.
name|setMin
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|pdRange
operator|.
name|setMax
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|pdLab
operator|.
name|setARange
argument_list|(
name|pdRange
argument_list|)
expr_stmt|;
name|pdRange
operator|=
operator|new
name|PDRange
argument_list|()
expr_stmt|;
name|pdRange
operator|.
name|setMin
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|pdRange
operator|.
name|setMax
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|pdLab
operator|.
name|setBRange
argument_list|(
name|pdRange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1f
argument_list|,
name|pdLab
operator|.
name|getARange
argument_list|()
operator|.
name|getMin
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2f
argument_list|,
name|pdLab
operator|.
name|getARange
argument_list|()
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3f
argument_list|,
name|pdLab
operator|.
name|getBRange
argument_list|()
operator|.
name|getMin
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4f
argument_list|,
name|pdLab
operator|.
name|getBRange
argument_list|()
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
name|PDTristimulus
name|pdTristimulus
init|=
operator|new
name|PDTristimulus
argument_list|()
decl_stmt|;
name|pdTristimulus
operator|.
name|setX
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|pdTristimulus
operator|.
name|setY
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|pdTristimulus
operator|.
name|setZ
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|pdLab
operator|.
name|setWhitePoint
argument_list|(
name|pdTristimulus
argument_list|)
expr_stmt|;
name|pdTristimulus
operator|=
operator|new
name|PDTristimulus
argument_list|()
expr_stmt|;
name|pdTristimulus
operator|.
name|setX
argument_list|(
literal|8
argument_list|)
expr_stmt|;
name|pdTristimulus
operator|.
name|setY
argument_list|(
literal|9
argument_list|)
expr_stmt|;
name|pdTristimulus
operator|.
name|setZ
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|pdLab
operator|.
name|setBlackPoint
argument_list|(
name|pdTristimulus
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5f
argument_list|,
name|pdLab
operator|.
name|getWhitepoint
argument_list|()
operator|.
name|getX
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6f
argument_list|,
name|pdLab
operator|.
name|getWhitepoint
argument_list|()
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7f
argument_list|,
name|pdLab
operator|.
name|getWhitepoint
argument_list|()
operator|.
name|getZ
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8f
argument_list|,
name|pdLab
operator|.
name|getBlackPoint
argument_list|()
operator|.
name|getX
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|9f
argument_list|,
name|pdLab
operator|.
name|getBlackPoint
argument_list|()
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10f
argument_list|,
name|pdLab
operator|.
name|getBlackPoint
argument_list|()
operator|.
name|getZ
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|3
block|}
argument_list|,
name|pdLab
operator|.
name|getInitialColor
argument_list|()
operator|.
name|getComponents
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

