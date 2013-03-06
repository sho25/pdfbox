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
name|xmpbox
package|;
end_package

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
name|xmpbox
operator|.
name|schema
operator|.
name|XMPSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|xml
operator|.
name|XmpSerializationException
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

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_comment
comment|/**  * Test XMP MetaData Transformer  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|XMPMetaDataTest
block|{
specifier|protected
name|XMPMetadata
name|metadata
decl_stmt|;
specifier|protected
name|Document
name|parent
decl_stmt|;
specifier|protected
name|XMPSchema
name|tmp
decl_stmt|,
name|tmp2
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|init
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
name|String
name|tmpNsURI
init|=
literal|"http://www.test.org/schem/"
decl_stmt|;
name|tmp
operator|=
operator|new
name|XMPSchema
argument_list|(
name|metadata
argument_list|,
name|tmpNsURI
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|addQualifiedBagValue
argument_list|(
literal|"BagContainer"
argument_list|,
literal|"Value1"
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|addQualifiedBagValue
argument_list|(
literal|"BagContainer"
argument_list|,
literal|"Value2"
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|addQualifiedBagValue
argument_list|(
literal|"BagContainer"
argument_list|,
literal|"Value3"
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|addUnqualifiedSequenceValue
argument_list|(
literal|"SeqContainer"
argument_list|,
literal|"Value1"
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|addUnqualifiedSequenceValue
argument_list|(
literal|"SeqContainer"
argument_list|,
literal|"Value2"
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|addUnqualifiedSequenceValue
argument_list|(
literal|"SeqContainer"
argument_list|,
literal|"Value3"
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|addProperty
argument_list|(
name|metadata
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createText
argument_list|(
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"simpleProperty"
argument_list|,
literal|"YEP"
argument_list|)
argument_list|)
expr_stmt|;
name|tmp2
operator|=
operator|new
name|XMPSchema
argument_list|(
name|metadata
argument_list|,
literal|"http://www.space.org/schem/"
argument_list|,
literal|"space"
argument_list|,
literal|"space"
argument_list|)
expr_stmt|;
name|tmp2
operator|.
name|addUnqualifiedSequenceValue
argument_list|(
literal|"SeqSpContainer"
argument_list|,
literal|"ValueSpace1"
argument_list|)
expr_stmt|;
name|tmp2
operator|.
name|addUnqualifiedSequenceValue
argument_list|(
literal|"SeqSpContainer"
argument_list|,
literal|"ValueSpace2"
argument_list|)
expr_stmt|;
name|tmp2
operator|.
name|addUnqualifiedSequenceValue
argument_list|(
literal|"SeqSpContainer"
argument_list|,
literal|"ValueSpace3"
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|addSchema
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|metadata
operator|.
name|addSchema
argument_list|(
name|tmp2
argument_list|)
expr_stmt|;
comment|// Check schema getting
name|Assert
operator|.
name|assertEquals
argument_list|(
name|tmp
argument_list|,
name|metadata
operator|.
name|getSchema
argument_list|(
name|tmpNsURI
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|metadata
operator|.
name|getSchema
argument_list|(
literal|"THIS URI NOT EXISTS !"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddingSchem
parameter_list|()
block|{
name|List
argument_list|<
name|XMPSchema
argument_list|>
name|vals
init|=
name|metadata
operator|.
name|getAllSchemas
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|vals
operator|.
name|contains
argument_list|(
name|tmp
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|vals
operator|.
name|contains
argument_list|(
name|tmp2
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*      * @Test public void displayResult() throws TransformException { System.out.println      * ("info used:\n XPacketBegin:"+metadata.getXpacketBegin()+ "\n XPacketID:"+metadata.getXpacketId());      * SaveMetadataHelper.serialize(metadata, true, System.out);      *       * }      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|xml
operator|.
name|XmpSerializationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testTransformerExceptionMessage
parameter_list|()
throws|throws
name|XmpSerializationException
block|{
throw|throw
operator|new
name|XmpSerializationException
argument_list|(
literal|"TEST"
argument_list|)
throw|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|xml
operator|.
name|XmpSerializationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testTransformerExceptionWithCause
parameter_list|()
throws|throws
name|XmpSerializationException
block|{
throw|throw
operator|new
name|XmpSerializationException
argument_list|(
literal|"TEST"
argument_list|,
operator|new
name|Throwable
argument_list|()
argument_list|)
throw|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInitMetaDataWithInfo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|xpacketBegin
init|=
literal|"TESTBEG"
decl_stmt|,
name|xpacketId
init|=
literal|"TESTID"
decl_stmt|,
name|xpacketBytes
init|=
literal|"TESTBYTES"
decl_stmt|,
name|xpacketEncoding
init|=
literal|"TESTENCOD"
decl_stmt|;
name|metadata
operator|=
name|XMPMetadata
operator|.
name|createXMPMetadata
argument_list|(
name|xpacketBegin
argument_list|,
name|xpacketId
argument_list|,
name|xpacketBytes
argument_list|,
name|xpacketEncoding
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|xpacketBegin
argument_list|,
name|metadata
operator|.
name|getXpacketBegin
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|xpacketId
argument_list|,
name|metadata
operator|.
name|getXpacketId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|xpacketBytes
argument_list|,
name|metadata
operator|.
name|getXpacketBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|xpacketEncoding
argument_list|,
name|metadata
operator|.
name|getXpacketEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

