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
operator|.
name|schema
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|XMPMetadata
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
name|type
operator|.
name|BadFieldValueException
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
name|PDFAIdentificationOthersTest
block|{
specifier|protected
name|XMPMetadata
name|metadata
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|initTempMetaData
parameter_list|()
throws|throws
name|Exception
block|{
name|metadata
operator|=
name|XMPMetadata
operator|.
name|createXMPMetadata
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPDFAIdentification
parameter_list|()
throws|throws
name|Exception
block|{
name|PDFAIdentificationSchema
name|pdfaid
init|=
name|metadata
operator|.
name|createAndAddPFAIdentificationSchema
argument_list|()
decl_stmt|;
name|Integer
name|versionId
init|=
literal|1
decl_stmt|;
name|String
name|amdId
init|=
literal|"2005"
decl_stmt|;
name|String
name|conformance
init|=
literal|"B"
decl_stmt|;
name|pdfaid
operator|.
name|setPartValueWithInt
argument_list|(
name|versionId
argument_list|)
expr_stmt|;
name|pdfaid
operator|.
name|setAmd
argument_list|(
name|amdId
argument_list|)
expr_stmt|;
name|pdfaid
operator|.
name|setConformance
argument_list|(
name|conformance
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|versionId
argument_list|,
name|pdfaid
operator|.
name|getPart
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|amdId
argument_list|,
name|pdfaid
operator|.
name|getAmendment
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|conformance
argument_list|,
name|pdfaid
operator|.
name|getConformance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|""
operator|+
name|versionId
argument_list|,
name|pdfaid
operator|.
name|getPartProperty
argument_list|()
operator|.
name|getStringValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|amdId
argument_list|,
name|pdfaid
operator|.
name|getAmdProperty
argument_list|()
operator|.
name|getStringValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|conformance
argument_list|,
name|pdfaid
operator|.
name|getConformanceProperty
argument_list|()
operator|.
name|getStringValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// check retrieve this schema in metadata
name|Assert
operator|.
name|assertEquals
argument_list|(
name|pdfaid
argument_list|,
name|metadata
operator|.
name|getPDFIdentificationSchema
argument_list|()
argument_list|)
expr_stmt|;
comment|// SaveMetadataHelper.serialize(metadata, true, System.out);
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|BadFieldValueException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testBadPDFAConformanceId
parameter_list|()
throws|throws
name|BadFieldValueException
block|{
name|PDFAIdentificationSchema
name|pdfaid
init|=
name|metadata
operator|.
name|createAndAddPFAIdentificationSchema
argument_list|()
decl_stmt|;
name|String
name|conformance
init|=
literal|"kiohiohiohiohio"
decl_stmt|;
name|pdfaid
operator|.
name|setConformance
argument_list|(
name|conformance
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testBadVersionIdValueType
parameter_list|()
throws|throws
name|Exception
block|{
name|PDFAIdentificationSchema
name|pdfaid
init|=
name|metadata
operator|.
name|createAndAddPFAIdentificationSchema
argument_list|()
decl_stmt|;
name|pdfaid
operator|.
name|setPartValueWithString
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|pdfaid
operator|.
name|setPartValueWithString
argument_list|(
literal|"ojoj"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

