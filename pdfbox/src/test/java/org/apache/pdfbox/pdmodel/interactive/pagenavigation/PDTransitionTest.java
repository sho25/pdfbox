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
name|pagenavigation
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertTrue
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
name|COSInteger
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @author Andrea Vacondio  *  */
end_comment

begin_class
specifier|public
class|class
name|PDTransitionTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|defaultStyle
parameter_list|()
block|{
name|PDTransition
name|transition
init|=
operator|new
name|PDTransition
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|COSName
operator|.
name|TRANS
argument_list|,
name|transition
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PDTransitionStyle
operator|.
name|R
operator|.
name|name
argument_list|()
argument_list|,
name|transition
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|getStyle
parameter_list|()
block|{
name|PDTransition
name|transition
init|=
operator|new
name|PDTransition
argument_list|(
name|PDTransitionStyle
operator|.
name|Fade
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|COSName
operator|.
name|TRANS
argument_list|,
name|transition
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PDTransitionStyle
operator|.
name|Fade
operator|.
name|name
argument_list|()
argument_list|,
name|transition
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|defaultValues
parameter_list|()
block|{
name|PDTransition
name|transition
init|=
operator|new
name|PDTransition
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|PDTransitionStyle
operator|.
name|R
operator|.
name|name
argument_list|()
argument_list|,
name|transition
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PDTransitionDimension
operator|.
name|H
operator|.
name|name
argument_list|()
argument_list|,
name|transition
operator|.
name|getDimension
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PDTransitionMotion
operator|.
name|I
operator|.
name|name
argument_list|()
argument_list|,
name|transition
operator|.
name|getMotion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|,
name|transition
operator|.
name|getDirection
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|transition
operator|.
name|getDuration
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|transition
operator|.
name|getFlyScale
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|transition
operator|.
name|isFlyAreaOpaque
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|dimension
parameter_list|()
block|{
name|PDTransition
name|transition
init|=
operator|new
name|PDTransition
argument_list|()
decl_stmt|;
name|transition
operator|.
name|setDimension
argument_list|(
name|PDTransitionDimension
operator|.
name|H
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PDTransitionDimension
operator|.
name|H
operator|.
name|name
argument_list|()
argument_list|,
name|transition
operator|.
name|getDimension
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|directionNone
parameter_list|()
block|{
name|PDTransition
name|transition
init|=
operator|new
name|PDTransition
argument_list|()
decl_stmt|;
name|transition
operator|.
name|setDirection
argument_list|(
name|PDTransitionDirection
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSName
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|transition
operator|.
name|getDirection
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSName
operator|.
name|NONE
argument_list|,
name|transition
operator|.
name|getDirection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|directionNumber
parameter_list|()
block|{
name|PDTransition
name|transition
init|=
operator|new
name|PDTransition
argument_list|()
decl_stmt|;
name|transition
operator|.
name|setDirection
argument_list|(
name|PDTransitionDirection
operator|.
name|LEFT_TO_RIGHT
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSInteger
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|transition
operator|.
name|getDirection
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|,
name|transition
operator|.
name|getDirection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|motion
parameter_list|()
block|{
name|PDTransition
name|transition
init|=
operator|new
name|PDTransition
argument_list|()
decl_stmt|;
name|transition
operator|.
name|setMotion
argument_list|(
name|PDTransitionMotion
operator|.
name|O
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PDTransitionMotion
operator|.
name|O
operator|.
name|name
argument_list|()
argument_list|,
name|transition
operator|.
name|getMotion
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|duration
parameter_list|()
block|{
name|PDTransition
name|transition
init|=
operator|new
name|PDTransition
argument_list|()
decl_stmt|;
name|transition
operator|.
name|setDuration
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|transition
operator|.
name|getDuration
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|flyScale
parameter_list|()
block|{
name|PDTransition
name|transition
init|=
operator|new
name|PDTransition
argument_list|()
decl_stmt|;
name|transition
operator|.
name|setFlyScale
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|transition
operator|.
name|getFlyScale
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|flyArea
parameter_list|()
block|{
name|PDTransition
name|transition
init|=
operator|new
name|PDTransition
argument_list|()
decl_stmt|;
name|transition
operator|.
name|setFlyAreaOpaque
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|transition
operator|.
name|isFlyAreaOpaque
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

