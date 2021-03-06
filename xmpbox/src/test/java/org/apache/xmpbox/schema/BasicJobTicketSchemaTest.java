begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
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
name|xmpbox
operator|.
name|type
operator|.
name|JobType
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
name|type
operator|.
name|StructuredType
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
name|DomXmpParser
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
name|XmpSerializer
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
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|BasicJobTicketSchemaTest
block|{
specifier|protected
specifier|static
name|DomXmpParser
name|builder
decl_stmt|;
specifier|protected
name|XMPMetadata
name|metadata
decl_stmt|;
specifier|protected
name|XmpSerializer
name|serializer
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|bc
parameter_list|()
throws|throws
name|Exception
block|{
name|builder
operator|=
operator|new
name|DomXmpParser
argument_list|()
expr_stmt|;
block|}
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
name|serializer
operator|=
operator|new
name|XmpSerializer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddTwoJobs
parameter_list|()
throws|throws
name|Exception
block|{
name|XMPBasicJobTicketSchema
name|basic
init|=
name|metadata
operator|.
name|createAndAddBasicJobTicketSchema
argument_list|()
decl_stmt|;
name|basic
operator|.
name|addJob
argument_list|(
literal|"zeid1"
argument_list|,
literal|"zename1"
argument_list|,
literal|"zeurl1"
argument_list|,
literal|"aaa"
argument_list|)
expr_stmt|;
name|basic
operator|.
name|addJob
argument_list|(
literal|"zeid2"
argument_list|,
literal|"zename2"
argument_list|,
literal|"zeurl2"
argument_list|)
expr_stmt|;
comment|// serializer.serialize(metadata, System.out, true);
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|serializer
operator|.
name|serialize
argument_list|(
name|metadata
argument_list|,
name|bos
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|XMPMetadata
name|rxmp
init|=
name|builder
operator|.
name|parse
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMPBasicJobTicketSchema
name|jt
init|=
name|rxmp
operator|.
name|getBasicJobTicketSchema
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|jt
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|jt
operator|.
name|getJobs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddWithDefaultPrefix
parameter_list|()
throws|throws
name|Exception
block|{
name|XMPBasicJobTicketSchema
name|basic
init|=
name|metadata
operator|.
name|createAndAddBasicJobTicketSchema
argument_list|()
decl_stmt|;
name|basic
operator|.
name|addJob
argument_list|(
literal|"zeid2"
argument_list|,
literal|"zename2"
argument_list|,
literal|"zeurl2"
argument_list|)
expr_stmt|;
comment|// serializer.serialize(metadata, System.out, true);
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|serializer
operator|.
name|serialize
argument_list|(
name|metadata
argument_list|,
name|bos
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|XMPMetadata
name|rxmp
init|=
name|builder
operator|.
name|parse
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMPBasicJobTicketSchema
name|jt
init|=
name|rxmp
operator|.
name|getBasicJobTicketSchema
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|jt
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|jt
operator|.
name|getJobs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|StructuredType
name|stjob
init|=
name|JobType
operator|.
name|class
operator|.
name|getAnnotation
argument_list|(
name|StructuredType
operator|.
name|class
argument_list|)
decl_stmt|;
name|JobType
name|job
init|=
name|jt
operator|.
name|getJobs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"zeid2"
argument_list|,
name|job
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"zename2"
argument_list|,
name|job
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"zeurl2"
argument_list|,
name|job
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
comment|// Assert.assertEquals("Invalid namespace",stjob.namespace(),
comment|// job.getNamespace());
comment|// Assert.assertEquals(stjob.preferedPrefix(), job.getPrefix());
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddWithDefinedPrefix
parameter_list|()
throws|throws
name|Exception
block|{
name|XMPBasicJobTicketSchema
name|basic
init|=
name|metadata
operator|.
name|createAndAddBasicJobTicketSchema
argument_list|()
decl_stmt|;
name|basic
operator|.
name|addJob
argument_list|(
literal|"zeid2"
argument_list|,
literal|"zename2"
argument_list|,
literal|"zeurl2"
argument_list|,
literal|"aaa"
argument_list|)
expr_stmt|;
comment|// SaveMetadataHelper.serialize(metadata, System.out);
comment|// serializer.serialize(metadata, System.out, true);
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|serializer
operator|.
name|serialize
argument_list|(
name|metadata
argument_list|,
name|bos
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|XMPMetadata
name|rxmp
init|=
name|builder
operator|.
name|parse
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMPBasicJobTicketSchema
name|jt
init|=
name|rxmp
operator|.
name|getBasicJobTicketSchema
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|jt
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|jt
operator|.
name|getJobs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|JobType
name|job
init|=
name|jt
operator|.
name|getJobs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// SaveMetadataHelper.serialize(rxmp, System.out);
comment|// StructuredType stjob =
comment|// JobType.class.getAnnotation(StructuredType.class);
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"zeid2"
argument_list|,
name|job
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"zename2"
argument_list|,
name|job
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"zeurl2"
argument_list|,
name|job
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
comment|// Assert.assertEquals(stjob.namespace(), job.getNamespace());
comment|// Assert.assertEquals("aaa", job.getPrefix());
block|}
block|}
end_class

end_unit

