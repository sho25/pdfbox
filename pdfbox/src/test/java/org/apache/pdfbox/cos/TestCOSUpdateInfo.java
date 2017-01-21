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
name|cos
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
name|assertTrue
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
name|fail
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Test class for {@link COSUpdateInfo}.  */
end_comment

begin_class
specifier|public
class|class
name|TestCOSUpdateInfo
block|{
comment|/**      * Tests isNeedToBeUpdate() and setNeedToBeUpdate() - tests the getter/setter methods.      */
annotation|@
name|Test
specifier|public
name|void
name|testIsSetNeedToBeUpdate
parameter_list|()
block|{
comment|// COSDictionary
name|COSUpdateInfo
name|testCOSDictionary
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|testCOSDictionary
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|testCOSDictionary
operator|.
name|isNeedToBeUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|testCOSDictionary
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|testCOSDictionary
operator|.
name|isNeedToBeUpdated
argument_list|()
argument_list|)
expr_stmt|;
comment|// COSObject
name|COSUpdateInfo
name|testCOSObject
decl_stmt|;
name|testCOSObject
operator|=
operator|new
name|COSObject
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|testCOSObject
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|testCOSObject
operator|.
name|isNeedToBeUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|testCOSObject
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|testCOSObject
operator|.
name|isNeedToBeUpdated
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

