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
name|util
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Test
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
name|junit
operator|.
name|framework
operator|.
name|TestSuite
import|;
end_import

begin_comment
comment|/**  * Test the date conversion utility.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|TestDateUtil
extends|extends
name|TestCase
block|{
comment|/**      * Test class constructor.      *      * @param name The name of the test class.      *      * @throws IOException If there is an error creating the test.      */
specifier|public
name|TestDateUtil
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test common date formats.      *      * @throws Exception when there is an exception      */
specifier|public
name|void
name|testExtract
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"D:05/12/2005"
argument_list|)
argument_list|,
operator|new
name|GregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|4
argument_list|,
literal|12
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"5/12/2005 15:57:16"
argument_list|)
argument_list|,
operator|new
name|GregorianCalendar
argument_list|(
literal|2005
argument_list|,
literal|4
argument_list|,
literal|12
argument_list|,
literal|15
argument_list|,
literal|57
argument_list|,
literal|16
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test case for      *<a href="https://issues.apache.org/jira/browse/PDFBOX-598">PDFBOX-598</a>      */
specifier|public
name|void
name|testDateConversion
parameter_list|()
throws|throws
name|Exception
block|{
name|Calendar
name|c
init|=
name|DateConverter
operator|.
name|toCalendar
argument_list|(
literal|"D:20050526205258+01'00'"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2005
argument_list|,
name|c
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|YEAR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|05
operator|-
literal|1
argument_list|,
name|c
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MONTH
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|26
argument_list|,
name|c
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|c
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|HOUR_OF_DAY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|52
argument_list|,
name|c
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MINUTE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|58
argument_list|,
name|c
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|SECOND
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|c
operator|.
name|get
argument_list|(
name|Calendar
operator|.
name|MILLISECOND
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the tests in the suite for this test class.      *      * @return the Suite.      */
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
return|return
operator|new
name|TestSuite
argument_list|(
name|TestDateUtil
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Command line execution.      *      * @param args Command line arguments.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|String
index|[]
name|arg
init|=
block|{
name|TestDateUtil
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
name|junit
operator|.
name|textui
operator|.
name|TestRunner
operator|.
name|main
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

