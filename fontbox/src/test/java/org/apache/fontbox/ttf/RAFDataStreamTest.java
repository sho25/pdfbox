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
name|fontbox
operator|.
name|ttf
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
name|RAFDataStreamTest
block|{
comment|/**      * Test of PDFBOX-4242: make sure that the Closeable.close() contract is fulfilled.      *       * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testDoubleClose
parameter_list|()
throws|throws
name|IOException
block|{
name|RAFDataStream
name|raf
init|=
operator|new
name|RAFDataStream
argument_list|(
literal|"src/test/resources/ttf/LiberationSans-Regular.ttf"
argument_list|,
literal|"r"
argument_list|)
decl_stmt|;
name|raf
operator|.
name|close
argument_list|()
expr_stmt|;
name|raf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
