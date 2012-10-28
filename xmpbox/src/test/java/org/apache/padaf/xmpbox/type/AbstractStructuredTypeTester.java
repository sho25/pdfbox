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
name|type
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|xml
operator|.
name|DomXmpParser
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
specifier|abstract
class|class
name|AbstractStructuredTypeTester
block|{
specifier|protected
name|XMPMetadata
name|xmp
decl_stmt|;
specifier|protected
name|String
name|fieldName
decl_stmt|;
specifier|protected
name|Types
name|type
decl_stmt|;
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|AbstractStructuredType
argument_list|>
name|clz
decl_stmt|;
specifier|protected
name|TypeMapping
name|typeMapping
init|=
literal|null
decl_stmt|;
specifier|protected
name|DomXmpParser
name|builder
decl_stmt|;
specifier|public
name|void
name|before
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
name|xmp
operator|=
name|XMPMetadata
operator|.
name|createXMPMetadata
argument_list|()
expr_stmt|;
name|typeMapping
operator|=
name|xmp
operator|.
name|getTypeMapping
argument_list|()
expr_stmt|;
block|}
specifier|public
name|AbstractStructuredTypeTester
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|AbstractStructuredType
argument_list|>
name|clz
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|Types
name|type
parameter_list|)
block|{
name|this
operator|.
name|clz
operator|=
name|clz
expr_stmt|;
name|this
operator|.
name|fieldName
operator|=
name|fieldName
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|AbstractStructuredType
name|getStructured
parameter_list|()
function_decl|;
annotation|@
name|Test
specifier|public
name|void
name|testInitializedToNull
parameter_list|()
throws|throws
name|Exception
block|{
comment|// default method
name|Assert
operator|.
name|assertNull
argument_list|(
name|getStructured
argument_list|()
operator|.
name|getProperty
argument_list|(
name|fieldName
argument_list|)
argument_list|)
expr_stmt|;
comment|// accessor
name|Method
name|get
init|=
name|clz
operator|.
name|getMethod
argument_list|(
name|TypeTestingHelper
operator|.
name|calculateSimpleGetter
argument_list|(
name|fieldName
argument_list|)
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|get
operator|.
name|invoke
argument_list|(
name|getStructured
argument_list|()
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSettingValue
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|TypeTestingHelper
operator|.
name|getJavaValue
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|getStructured
argument_list|()
operator|.
name|addSimpleProperty
argument_list|(
name|fieldName
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|getStructured
argument_list|()
operator|.
name|getProperty
argument_list|(
name|fieldName
argument_list|)
argument_list|)
expr_stmt|;
comment|// check other properties not modified
name|List
argument_list|<
name|Field
argument_list|>
name|fields
init|=
name|TypeTestingHelper
operator|.
name|getXmpFields
argument_list|(
name|clz
argument_list|)
decl_stmt|;
for|for
control|(
name|Field
name|field
range|:
name|fields
control|)
block|{
comment|// do not check the current name
name|String
name|name
init|=
name|field
operator|.
name|get
argument_list|(
literal|null
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|fieldName
argument_list|)
condition|)
block|{
name|Assert
operator|.
name|assertNull
argument_list|(
name|getStructured
argument_list|()
operator|.
name|getProperty
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPropertyType
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|TypeTestingHelper
operator|.
name|getJavaValue
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|getStructured
argument_list|()
operator|.
name|addSimpleProperty
argument_list|(
name|fieldName
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|getStructured
argument_list|()
operator|.
name|getProperty
argument_list|(
name|fieldName
argument_list|)
argument_list|)
expr_stmt|;
comment|// check property type
name|AbstractSimpleProperty
name|asp
init|=
operator|(
name|AbstractSimpleProperty
operator|)
name|getStructured
argument_list|()
operator|.
name|getProperty
argument_list|(
name|fieldName
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|type
operator|.
name|getImplementingClass
argument_list|()
argument_list|,
name|asp
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|setter
init|=
name|TypeTestingHelper
operator|.
name|calculateSimpleSetter
argument_list|(
name|fieldName
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|TypeTestingHelper
operator|.
name|getJavaValue
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|Method
name|set
init|=
name|clz
operator|.
name|getMethod
argument_list|(
name|setter
argument_list|,
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|TypeTestingHelper
operator|.
name|getJavaType
argument_list|(
name|type
argument_list|)
block|}
block|)
function|;
name|set
operator|.
name|invoke
argument_list|(
name|getStructured
argument_list|()
argument_list|,
operator|new
name|Object
index|[]
block|{
name|value
block|}
argument_list|)
expr_stmt|;
comment|// check property set
name|Assert
operator|.
name|assertEquals
argument_list|(
name|value
argument_list|,
operator|(
operator|(
name|AbstractSimpleProperty
operator|)
name|getStructured
argument_list|()
operator|.
name|getProperty
argument_list|(
name|fieldName
argument_list|)
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// check getter
name|Method
name|get
init|=
name|clz
operator|.
name|getMethod
argument_list|(
name|TypeTestingHelper
operator|.
name|calculateSimpleGetter
argument_list|(
name|fieldName
argument_list|)
argument_list|,
operator|new
name|Class
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|get
operator|.
name|invoke
argument_list|(
name|getStructured
argument_list|()
argument_list|,
operator|new
name|Object
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
comment|//    	Assert.assertEquals(getJavaType(td),result.getClass());
name|Assert
operator|.
name|assertTrue
argument_list|(
name|TypeTestingHelper
operator|.
name|getJavaType
argument_list|(
name|type
argument_list|)
operator|.
name|isAssignableFrom
argument_list|(
name|result
operator|.
name|getClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
parameter_list|(
name|value
parameter_list|,
name|result
parameter_list|)
constructor_decl|;
block|}
end_class

unit|}
end_unit

