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
operator|.
name|type
package|;
end_package

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
name|List
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
name|Test
import|;
end_import

begin_comment
comment|/**  * Test MetaData Objects for simple properties  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|TestSimpleMetadataProperties
block|{
specifier|protected
name|XMPMetadata
name|parent
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|resetDocument
parameter_list|()
throws|throws
name|Exception
block|{
name|parent
operator|=
name|XMPMetadata
operator|.
name|createXMPMetadata
argument_list|()
expr_stmt|;
block|}
comment|/**      * Check the detection of a bad type      *       * @throws IllegalArgumentException      */
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
name|testBooleanBadTypeDetection
parameter_list|()
block|{
operator|new
name|BooleanType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"boolean"
argument_list|,
literal|"Not a Boolean"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check the detection of a bad type      *       * @throws IllegalArgumentException      */
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
name|testDateBadTypeDetection
parameter_list|()
block|{
operator|new
name|DateType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"date"
argument_list|,
literal|"Bad Date"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check the detection of a bad type      *       * @throws IllegalArgumentException      */
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
name|testIntegerBadTypeDetection
parameter_list|()
block|{
operator|new
name|IntegerType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"integer"
argument_list|,
literal|"Not an int"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check the detection of a bad type      *       * @throws IllegalArgumentException      */
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
name|testRealBadTypeDetection
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|RealType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"real"
argument_list|,
literal|"Not a real"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check the detection of a bad type      *       * @throws IllegalArgumentException      */
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
name|testTextBadTypeDetection
parameter_list|()
throws|throws
name|Exception
block|{
operator|new
name|TextType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"text"
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check if information between objects and the element generated are equals      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testElementAndObjectSynchronization
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|boolv
init|=
literal|true
decl_stmt|;
name|Calendar
name|datev
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|int
name|integerv
init|=
literal|1
decl_stmt|;
name|float
name|realv
init|=
name|Float
operator|.
name|parseFloat
argument_list|(
literal|"1.69"
argument_list|)
decl_stmt|;
name|String
name|textv
init|=
literal|"TEXTCONTENT"
decl_stmt|;
name|BooleanType
name|bool
init|=
name|parent
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createBoolean
argument_list|(
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"boolean"
argument_list|,
name|boolv
argument_list|)
decl_stmt|;
name|DateType
name|date
init|=
name|parent
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createDate
argument_list|(
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"date"
argument_list|,
name|datev
argument_list|)
decl_stmt|;
name|IntegerType
name|integer
init|=
name|parent
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createInteger
argument_list|(
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"integer"
argument_list|,
name|integerv
argument_list|)
decl_stmt|;
name|RealType
name|real
init|=
name|parent
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createReal
argument_list|(
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"real"
argument_list|,
name|realv
argument_list|)
decl_stmt|;
name|TextType
name|text
init|=
name|parent
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
literal|"text"
argument_list|,
name|textv
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|boolv
argument_list|,
name|bool
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|datev
argument_list|,
name|date
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|integerv
argument_list|)
argument_list|,
name|integer
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|realv
argument_list|,
name|real
operator|.
name|getValue
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|textv
argument_list|,
name|text
operator|.
name|getStringValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check the creation from string attributes      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testCreationFromString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|boolv
init|=
literal|"False"
decl_stmt|;
name|String
name|datev
init|=
literal|"2010-03-22T14:33:11+01:00"
decl_stmt|;
name|String
name|integerv
init|=
literal|"10"
decl_stmt|;
name|String
name|realv
init|=
literal|"1.92"
decl_stmt|;
name|String
name|textv
init|=
literal|"text"
decl_stmt|;
name|BooleanType
name|bool
init|=
operator|new
name|BooleanType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"boolean"
argument_list|,
name|boolv
argument_list|)
decl_stmt|;
name|DateType
name|date
init|=
operator|new
name|DateType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"date"
argument_list|,
name|datev
argument_list|)
decl_stmt|;
name|IntegerType
name|integer
init|=
operator|new
name|IntegerType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"integer"
argument_list|,
name|integerv
argument_list|)
decl_stmt|;
name|RealType
name|real
init|=
operator|new
name|RealType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"real"
argument_list|,
name|realv
argument_list|)
decl_stmt|;
name|TextType
name|text
init|=
operator|new
name|TextType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"text"
argument_list|,
name|textv
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|boolv
argument_list|,
name|bool
operator|.
name|getStringValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|datev
argument_list|,
name|date
operator|.
name|getStringValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|integerv
argument_list|,
name|integer
operator|.
name|getStringValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|realv
argument_list|,
name|real
operator|.
name|getStringValue
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|textv
argument_list|,
name|text
operator|.
name|getStringValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check creation when a namespace is specified      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testObjectCreationWithNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ns
init|=
literal|"http://www.test.org/pdfa/"
decl_stmt|;
name|BooleanType
name|bool
init|=
name|parent
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createBoolean
argument_list|(
name|ns
argument_list|,
literal|"test"
argument_list|,
literal|"boolean"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|DateType
name|date
init|=
name|parent
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createDate
argument_list|(
name|ns
argument_list|,
literal|"test"
argument_list|,
literal|"date"
argument_list|,
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
decl_stmt|;
name|IntegerType
name|integer
init|=
name|parent
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createInteger
argument_list|(
name|ns
argument_list|,
literal|"test"
argument_list|,
literal|"integer"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|RealType
name|real
init|=
name|parent
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createReal
argument_list|(
name|ns
argument_list|,
literal|"test"
argument_list|,
literal|"real"
argument_list|,
operator|(
name|float
operator|)
literal|1.6
argument_list|)
decl_stmt|;
name|TextType
name|text
init|=
name|parent
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createText
argument_list|(
name|ns
argument_list|,
literal|"test"
argument_list|,
literal|"text"
argument_list|,
literal|"TEST"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ns
argument_list|,
name|bool
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ns
argument_list|,
name|date
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ns
argument_list|,
name|integer
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ns
argument_list|,
name|real
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ns
argument_list|,
name|text
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Throw IllegalArgumentException      *       * @throws IllegalArgumentException      */
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
name|testExceptionWithCause
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"TEST"
argument_list|,
operator|new
name|Throwable
argument_list|()
argument_list|)
throw|;
block|}
comment|/**      * Check if attributes management works      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testAttribute
parameter_list|()
throws|throws
name|Exception
block|{
name|IntegerType
name|integer
init|=
operator|new
name|IntegerType
argument_list|(
name|parent
argument_list|,
literal|null
argument_list|,
literal|"test"
argument_list|,
literal|"integer"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Attribute
name|value
init|=
operator|new
name|Attribute
argument_list|(
literal|"http://www.test.org/test/"
argument_list|,
literal|"value1"
argument_list|,
literal|"StringValue1"
argument_list|)
decl_stmt|;
name|Attribute
name|value2
init|=
operator|new
name|Attribute
argument_list|(
literal|"http://www.test.org/test/"
argument_list|,
literal|"value2"
argument_list|,
literal|"StringValue2"
argument_list|)
decl_stmt|;
name|integer
operator|.
name|setAttribute
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// System.out.println(value.getQualifiedName());
name|Assert
operator|.
name|assertEquals
argument_list|(
name|value
argument_list|,
name|integer
operator|.
name|getAttribute
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|integer
operator|.
name|containsAttribute
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Replacement check
name|integer
operator|.
name|setAttribute
argument_list|(
name|value2
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|value2
argument_list|,
name|integer
operator|.
name|getAttribute
argument_list|(
name|value2
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|integer
operator|.
name|removeAttribute
argument_list|(
name|value2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|integer
operator|.
name|containsAttribute
argument_list|(
name|value2
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Attribute with namespace Creation checking
name|Attribute
name|valueNS
init|=
operator|new
name|Attribute
argument_list|(
literal|"http://www.tefst2.org/test/"
argument_list|,
literal|"value2"
argument_list|,
literal|"StringValue.2"
argument_list|)
decl_stmt|;
name|integer
operator|.
name|setAttribute
argument_list|(
name|valueNS
argument_list|)
expr_stmt|;
name|Attribute
name|valueNS2
init|=
operator|new
name|Attribute
argument_list|(
literal|"http://www.test2.org/test/"
argument_list|,
literal|"value2"
argument_list|,
literal|"StringValueTwo"
argument_list|)
decl_stmt|;
name|integer
operator|.
name|setAttribute
argument_list|(
name|valueNS2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Attribute
argument_list|>
name|atts
init|=
name|integer
operator|.
name|getAllAttributes
argument_list|()
decl_stmt|;
comment|/*          * for (Attribute attribute : atts) { System.out.println(attribute.getLocalName ()+" :"+attribute.getValue()); }          */
name|Assert
operator|.
name|assertFalse
argument_list|(
name|atts
operator|.
name|contains
argument_list|(
name|valueNS
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|atts
operator|.
name|contains
argument_list|(
name|valueNS2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

