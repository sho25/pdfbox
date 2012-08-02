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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
class|class
name|AdobePDFTest
extends|extends
name|AbstractXMPSchemaTest
block|{
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
operator|new
name|XMPDocumentBuilder
argument_list|()
operator|.
name|createXMPMetadata
argument_list|()
expr_stmt|;
name|schema
operator|=
name|metadata
operator|.
name|createAndAddAdobePDFSchema
argument_list|()
expr_stmt|;
name|schemaClass
operator|=
name|AdobePDFSchema
operator|.
name|class
expr_stmt|;
block|}
annotation|@
name|Parameters
specifier|public
specifier|static
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|initializeParameters
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
index|[]
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
index|[]
argument_list|>
argument_list|()
decl_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Keywords"
argument_list|,
literal|"Text"
argument_list|,
literal|"kw1 kw2 kw3"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"PDFVersion"
argument_list|,
literal|"Text"
argument_list|,
literal|"1.4"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Producer"
argument_list|,
literal|"Text"
argument_list|,
literal|"testcase"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|data
return|;
block|}
specifier|public
name|AdobePDFTest
parameter_list|(
name|String
name|property
parameter_list|,
name|String
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|property
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|// @Test
comment|// public void testPDFAIdentification() throws BadFieldValueException,
comment|// TransformException{
comment|// AdobePDFSchema schem=metadata.createAndAddAdobePDFSchema();
comment|//
comment|// String keywords="keywords ihih";
comment|// String pdfVersion="1.4";
comment|// String producer="producer";
comment|//
comment|// schem.setKeywordsValue(keywords);
comment|// schem.setPDFVersionValue(pdfVersion);
comment|//
comment|// //Check get null if property not defined
comment|// Assert.assertNull(schem.getProducer());
comment|//
comment|// schem.setProducerValue(producer);
comment|//
comment|// Assert.assertEquals("pdf:Keywords",
comment|// schem.getKeywords().getQualifiedName());
comment|// Assert.assertEquals(keywords, schem.getKeywordsValue());
comment|//
comment|// Assert.assertEquals("pdf:PDFVersion",
comment|// schem.getPDFVersion().getQualifiedName());
comment|// Assert.assertEquals(pdfVersion, schem.getPDFVersionValue());
comment|//
comment|// Assert.assertEquals("pdf:Producer",
comment|// schem.getProducer().getQualifiedName());
comment|// Assert.assertEquals(producer, schem.getProducerValue());
comment|//
comment|// //check retrieve this schema in metadata
comment|// Assert.assertEquals(schem, metadata.getAdobePDFSchema());
comment|//
comment|// //SaveMetadataHelper.serialize(metadata, true, System.out);
comment|// }
comment|// @Test(expected=BadFieldValueException.class)
comment|// public void testBadPDFAConformanceId() throws BadFieldValueException{
comment|// PDFAIdentificationSchema pdfaid=
comment|// metadata.createAndAddPFAIdentificationSchema();
comment|// String conformance="kiohiohiohiohio";
comment|// pdfaid.setConformanceValue(conformance);
comment|// }
comment|//
comment|// @Test(expected=IllegalArgumentException.class)
comment|// public void testBadVersionIdValueType() throws Exception {
comment|// PDFAIdentificationSchema pdfaid=
comment|// metadata.createAndAddPFAIdentificationSchema();
comment|// pdfaid.setVersionIdentifierValueWithString("1");
comment|// pdfaid.setVersionIdentifierValueWithString("ojoj");
comment|// }
block|}
end_class

end_unit

