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
name|assertFalse
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
name|COSDictionaryTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCOSDictionaryNotEqualsCOSStream
parameter_list|()
block|{
name|COSDictionary
name|cosDictionary
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|COSStream
name|cosStream
init|=
operator|new
name|COSStream
argument_list|()
decl_stmt|;
name|cosDictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BE
argument_list|,
name|COSName
operator|.
name|BE
argument_list|)
expr_stmt|;
name|cosDictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cosStream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BE
argument_list|,
name|COSName
operator|.
name|BE
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"a COSDictionary shall not be equal to a COSStream with the same dictionary entries"
argument_list|,
name|cosDictionary
operator|.
name|equals
argument_list|(
name|cosStream
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"a COSStream shall not be equal to a COSDictionary with the same dictionary entries"
argument_list|,
name|cosStream
operator|.
name|equals
argument_list|(
name|cosDictionary
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

