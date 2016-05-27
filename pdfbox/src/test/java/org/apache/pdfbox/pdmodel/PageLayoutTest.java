begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2016 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PageLayoutTest
block|{
comment|/**      * Test for completeness (PDFBOX-3362).      */
annotation|@
name|Test
specifier|public
name|void
name|testValues
parameter_list|()
block|{
name|Set
argument_list|<
name|PageLayout
argument_list|>
name|pageLayoutSet
init|=
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|PageLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|stringSet
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|PageLayout
name|pl
range|:
name|PageLayout
operator|.
name|values
argument_list|()
control|)
block|{
name|String
name|s
init|=
name|pl
operator|.
name|stringValue
argument_list|()
decl_stmt|;
name|stringSet
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|pageLayoutSet
operator|.
name|add
argument_list|(
name|PageLayout
operator|.
name|fromString
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|PageLayout
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|,
name|pageLayoutSet
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|PageLayout
operator|.
name|values
argument_list|()
operator|.
name|length
argument_list|,
name|stringSet
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
