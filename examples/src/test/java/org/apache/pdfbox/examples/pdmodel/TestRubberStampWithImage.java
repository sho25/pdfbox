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
name|examples
operator|.
name|pdmodel
package|;
end_package

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
name|exceptions
operator|.
name|CryptographyException
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
name|exceptions
operator|.
name|SignatureException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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

begin_comment
comment|/**  * Test for RubberStampWithImage  */
end_comment

begin_class
specifier|public
class|class
name|TestRubberStampWithImage
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|IOException
throws|,
name|CryptographyException
throws|,
name|SignatureException
block|{
name|String
name|documentFile
init|=
literal|"src/test/resources/org.apache.pdfbox.examples.pdmodel/document.pdf"
decl_stmt|;
name|String
name|stampFile
init|=
literal|"src/test/resources/org.apache.pdfbox.examples.pdmodel/stamp.jpg"
decl_stmt|;
name|String
name|outFile
init|=
literal|"target/test-output/TestRubberStampWithImage.pdf"
decl_stmt|;
operator|new
name|File
argument_list|(
literal|"target/test-output"
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|String
index|[]
name|args
init|=
operator|new
name|String
index|[]
block|{
name|documentFile
block|,
name|outFile
block|,
name|stampFile
block|}
decl_stmt|;
name|RubberStampWithImage
name|rubberStamp
init|=
operator|new
name|RubberStampWithImage
argument_list|()
decl_stmt|;
name|rubberStamp
operator|.
name|doIt
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

