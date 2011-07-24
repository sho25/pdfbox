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
name|Calendar
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
name|schema
operator|.
name|XMPBasicSchema
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
name|XMPBasicTest
extends|extends
name|AbstractXMPSchemaTest
block|{
specifier|public
name|XMPBasicTest
parameter_list|(
name|String
name|prop
parameter_list|,
name|String
name|type
parameter_list|,
name|Object
name|val
parameter_list|)
block|{
name|super
argument_list|(
name|prop
argument_list|,
name|type
argument_list|,
name|val
argument_list|)
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
operator|new
name|XMPMetadata
argument_list|()
expr_stmt|;
name|schema
operator|=
name|metadata
operator|.
name|createAndAddXMPBasicSchema
argument_list|()
expr_stmt|;
name|schemaClass
operator|=
name|XMPBasicSchema
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
literal|"Advisory"
argument_list|,
literal|"bag Xpath"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"xpath1"
block|,
literal|"xpath2"
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"BaseURL"
argument_list|,
literal|"URL"
argument_list|,
literal|"URL"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"CreateDate"
argument_list|,
literal|"Date"
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"CreatorTool"
argument_list|,
literal|"Text"
argument_list|,
literal|"CreatorTool"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Identifier"
argument_list|,
literal|"bag Text"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id1"
block|,
literal|"id2"
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Label"
argument_list|,
literal|"Text"
argument_list|,
literal|"label"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"MetadataDate"
argument_list|,
literal|"Date"
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"ModifyDate"
argument_list|,
literal|"Date"
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Nickname"
argument_list|,
literal|"Text"
argument_list|,
literal|"nick name"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Rating"
argument_list|,
literal|"Integer"
argument_list|,
literal|7
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO test Thumbnail when implemented in the XMPBasicSchema
name|data
operator|.
name|add
argument_list|(
name|wrapProperty
argument_list|(
literal|"Thumbnails"
argument_list|,
literal|"Alt Thumbnail"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|data
return|;
block|}
comment|// @Test
comment|// public void testPDFExt() throws TransformException{
comment|// XMPBasicSchema schem=metadata.createAndAddXMPBasicSchema();
comment|//
comment|// String xpath1="xpath1";
comment|// String xpath2="xpath2";
comment|// String url="URL";
comment|// Calendar createDate=Calendar.getInstance();
comment|// String creatorTool="CreatorTool";
comment|// String identifier1="id1";
comment|// String identifier2="id2";
comment|// String label="label";
comment|// Calendar metaDataDate=Calendar.getInstance();
comment|// Calendar modifyDate=Calendar.getInstance();
comment|// String nickname="nickname";
comment|// int rate=7;
comment|//
comment|// schem.addAdvisoryValue(xpath1);
comment|// schem.addAdvisoryValue(xpath2);
comment|// schem.setBaseURLValue(url);
comment|// schem.setCreateDateValue(createDate);
comment|// schem.setCreatorToolValue(creatorTool);
comment|// schem.addIdentifierValue(identifier1);
comment|// schem.addIdentifierValue(identifier2);
comment|// schem.setLabelValue(label);
comment|// schem.setMetadataDateValue(metaDataDate);
comment|// schem.setModifyDateValue(modifyDate);
comment|// schem.setNicknameValue(nickname);
comment|// schem.setRatingValue(rate);
comment|//
comment|// //check retrieve this schema in metadata
comment|// Assert.assertEquals(schem, metadata.getXMPBasicSchema());
comment|//
comment|// //check values embedded in this schema
comment|// Assert.assertEquals("xmp:Advisory",
comment|// schem.getAdvisory().getQualifiedName());
comment|// Assert.assertTrue(schem.getAdvisoryValues().contains(xpath1));
comment|// Assert.assertTrue(schem.getAdvisoryValues().contains(xpath2));
comment|//
comment|// Assert.assertEquals("xmp:BaseURL",
comment|// schem.getBaseURL().getQualifiedName());
comment|// Assert.assertEquals(url, schem.getBaseURLValue());
comment|//
comment|// Assert.assertEquals("xmp:CreateDate",
comment|// schem.getCreateDate().getQualifiedName());
comment|// Assert.assertEquals(createDate, schem.getCreateDateValue());
comment|//
comment|// Assert.assertEquals("xmp:CreatorTool",
comment|// schem.getCreatorTool().getQualifiedName());
comment|// Assert.assertEquals(creatorTool, schem.getCreatorToolValue());
comment|//
comment|// Assert.assertEquals("xmp:Identifier",
comment|// schem.getIdentifier().getQualifiedName());
comment|// Assert.assertTrue(schem.getIdentifierValues().contains(identifier1));
comment|// Assert.assertTrue(schem.getIdentifierValues().contains(identifier2));
comment|//
comment|// Assert.assertEquals("xmp:Label", schem.getLabel().getQualifiedName());
comment|// Assert.assertEquals(label, schem.getLabelValue());
comment|//
comment|// Assert.assertEquals("xmp:MetadataDate",
comment|// schem.getMetadataDate().getQualifiedName());
comment|// Assert.assertEquals(metaDataDate, schem.getMetadataDateValue());
comment|//
comment|// Assert.assertEquals("xmp:ModifyDate",
comment|// schem.getModifyDate().getQualifiedName());
comment|// Assert.assertEquals(modifyDate, schem.getModifyDateValue());
comment|//
comment|// Assert.assertEquals("xmp:Nickname",
comment|// schem.getNickname().getQualifiedName());
comment|// Assert.assertEquals(nickname, schem.getNicknameValue());
comment|//
comment|// Assert.assertEquals("xmp:Rating", schem.getRating().getQualifiedName());
comment|// Assert.assertEquals(rate, schem.getRatingValue());
comment|//
comment|// //SaveMetadataHelper.serialize(metadata, true, System.out);
comment|//
comment|// //TODO test Thumbnail when implemented in the XMPBasicSchema
comment|// }
block|}
end_class

end_unit

