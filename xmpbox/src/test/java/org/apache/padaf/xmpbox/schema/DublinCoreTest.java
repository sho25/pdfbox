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
name|DublinCoreTest
extends|extends
name|AbstractSchemaTester
block|{
specifier|protected
name|DublinCoreSchema
name|schema
init|=
literal|null
decl_stmt|;
specifier|public
name|DublinCoreSchema
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|before
argument_list|()
expr_stmt|;
name|schema
operator|=
name|xmp
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
expr_stmt|;
block|}
specifier|public
name|DublinCoreTest
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|type
parameter_list|,
name|Cardinality
name|card
parameter_list|)
block|{
name|super
argument_list|(
name|fieldName
argument_list|,
name|type
argument_list|,
name|card
argument_list|)
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
name|Collection
argument_list|<
name|Object
index|[]
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
index|[]
argument_list|>
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"contributor"
block|,
literal|"ProperName"
block|,
name|Cardinality
operator|.
name|Bag
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"coverage"
block|,
literal|"Text"
block|,
name|Cardinality
operator|.
name|Simple
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"creator"
block|,
literal|"ProperName"
block|,
name|Cardinality
operator|.
name|Seq
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"date"
block|,
literal|"Date"
block|,
name|Cardinality
operator|.
name|Seq
block|}
argument_list|)
expr_stmt|;
comment|// description TODO TEST lang alt
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"format"
block|,
literal|"MIMEType"
block|,
name|Cardinality
operator|.
name|Simple
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"identifier"
block|,
literal|"Text"
block|,
name|Cardinality
operator|.
name|Simple
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"language"
block|,
literal|"Locale"
block|,
name|Cardinality
operator|.
name|Bag
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"publisher"
block|,
literal|"ProperName"
block|,
name|Cardinality
operator|.
name|Bag
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"relation"
block|,
literal|"Text"
block|,
name|Cardinality
operator|.
name|Bag
block|}
argument_list|)
expr_stmt|;
comment|// rights TODO TEST lang alt
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"source"
block|,
literal|"Text"
block|,
name|Cardinality
operator|.
name|Simple
block|}
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"subject"
block|,
literal|"Text"
block|,
name|Cardinality
operator|.
name|Bag
block|}
argument_list|)
expr_stmt|;
comment|// title TODO TEST lang alt
name|result
operator|.
name|add
argument_list|(
operator|new
name|Object
index|[]
block|{
literal|"type"
block|,
literal|"Text"
block|,
name|Cardinality
operator|.
name|Bag
block|}
argument_list|)
expr_stmt|;
comment|//		List<Object[]> data = new ArrayList<Object[]>();
comment|//		data.add(wrapProperty("contributor", "bag Text", new String[] {
comment|//				"contri 1", "contri 2" }));
comment|//		data.add(wrapProperty("coverage", "Text", "scope of the resource"));
comment|//		data.add(wrapProperty("creator", "seq Text", new String[] {
comment|//				"creator 1", "creator 2", "creator 3" }));
comment|//		data.add(wrapProperty("date", "seq Date", new Calendar[] { Calendar
comment|//				.getInstance() }));
comment|//
comment|//		Map<String, String> desc = new HashMap<String, String>(2);
comment|//		desc.put("fr", "en français");
comment|//		desc.put("en", "in english");
comment|//		data.add(wrapProperty("description", "Lang Alt", desc));
comment|//
comment|//		data.add(wrapProperty("format", "Text", "text/html"));
comment|//		data.add(wrapProperty("identifier", "Text", "my id"));
comment|//		data.add(wrapProperty("language", "bag Text", new String[] { "fr",
comment|//				"en", "es" }));
comment|//		data.add(wrapProperty("publisher", "bag Text", new String[] { "pub1",
comment|//				"pub2" }));
comment|//		data.add(wrapProperty("relation", "bag Text", new String[] { "rel1",
comment|//				"relation 2" }));
comment|//
comment|//		Map<String, String> rights = new HashMap<String, String>(2);
comment|//		rights.put("fr", "protégé");
comment|//		rights.put("en", "protected");
comment|//		data.add(wrapProperty("rights", "Lang Alt", rights));
comment|//
comment|//		data.add(wrapProperty("source", "Text", "my source"));
comment|//		data.add(wrapProperty("subject", "bag Text", new String[] { "subj1",
comment|//				"subj2" }));
comment|//
comment|//		Map<String, String> title = new HashMap<String, String>(2);
comment|//		title.put("fr", "essai");
comment|//		title.put("en", "test");
comment|//		title.put("es", "prueba");
comment|//		data.add(wrapProperty("title", "Lang Alt", title));
comment|//
comment|//		data.add(wrapProperty("type", "bag Text", new String[] { "text",
comment|//				"test", "dummy" }));
comment|//
comment|//		return data;
return|return
name|result
return|;
block|}
comment|//
comment|//	public DublinCoreTest(String property, String type, Object value) {
comment|//		super(property, type, value);
comment|//	}
block|}
end_class

end_unit

