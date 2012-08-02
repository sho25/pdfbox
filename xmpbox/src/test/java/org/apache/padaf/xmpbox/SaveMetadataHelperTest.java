begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|parser
operator|.
name|XMPDocumentBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|AdobePDFSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|DublinCoreSchema
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|SaveMetadataHelperTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSchemaParsing
parameter_list|()
throws|throws
name|Exception
block|{
name|DublinCoreSchema
name|dc
init|=
operator|new
name|DublinCoreSchema
argument_list|(
operator|new
name|XMPDocumentBuilder
argument_list|()
operator|.
name|createXMPMetadata
argument_list|()
argument_list|)
decl_stmt|;
name|dc
operator|.
name|setCoverage
argument_list|(
literal|"coverage"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addContributor
argument_list|(
literal|"contributor1"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addContributor
argument_list|(
literal|"contributor2"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addDescription
argument_list|(
literal|"x-default"
argument_list|,
literal|"Description"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|SaveMetadataHelper
operator|.
name|serialize
argument_list|(
name|dc
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|byte
index|[]
name|tmp
init|=
name|SaveMetadataHelper
operator|.
name|serialize
argument_list|(
name|dc
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|tmp
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMetadataParsing
parameter_list|()
throws|throws
name|Exception
block|{
name|XMPMetadata
name|meta
init|=
operator|new
name|XMPDocumentBuilder
argument_list|()
operator|.
name|createXMPMetadata
argument_list|()
decl_stmt|;
name|DublinCoreSchema
name|dc
init|=
name|meta
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
decl_stmt|;
name|dc
operator|.
name|setCoverage
argument_list|(
literal|"coverage"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addContributor
argument_list|(
literal|"contributor1"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addContributor
argument_list|(
literal|"contributor2"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addDescription
argument_list|(
literal|"x-default"
argument_list|,
literal|"Description"
argument_list|)
expr_stmt|;
name|AdobePDFSchema
name|pdf
init|=
name|meta
operator|.
name|createAndAddAdobePDFSchema
argument_list|()
decl_stmt|;
name|pdf
operator|.
name|setProducer
argument_list|(
literal|"Producer"
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setPDFVersion
argument_list|(
literal|"1.4"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|tmp
init|=
name|SaveMetadataHelper
operator|.
name|serialize
argument_list|(
name|meta
argument_list|)
decl_stmt|;
name|SaveMetadataHelper
operator|.
name|serialize
argument_list|(
name|meta
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|tmp
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|bosWithoutPI
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|SaveMetadataHelper
operator|.
name|serialize
argument_list|(
name|meta
argument_list|,
literal|false
argument_list|,
name|bosWithoutPI
argument_list|)
expr_stmt|;
name|byte
index|[]
name|tmpWithoutPI
init|=
name|SaveMetadataHelper
operator|.
name|serialize
argument_list|(
name|meta
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertArrayEquals
argument_list|(
name|bosWithoutPI
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|tmpWithoutPI
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

