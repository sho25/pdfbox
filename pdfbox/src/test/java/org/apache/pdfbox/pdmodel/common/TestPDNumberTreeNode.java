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
name|pdmodel
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

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
name|cos
operator|.
name|COSBase
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
name|cos
operator|.
name|COSInteger
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

begin_comment
comment|/**  * A test case for PDNumberTreeNode.  * Based on TestPDNameTreeNode.  *   * @author Dominic Tubach  */
end_comment

begin_class
specifier|public
class|class
name|TestPDNumberTreeNode
extends|extends
name|TestCase
block|{
specifier|private
name|PDNumberTreeNode
name|node1
decl_stmt|;
specifier|private
name|PDNumberTreeNode
name|node2
decl_stmt|;
specifier|private
name|PDNumberTreeNode
name|node4
decl_stmt|;
specifier|private
name|PDNumberTreeNode
name|node5
decl_stmt|;
specifier|private
name|PDNumberTreeNode
name|node24
decl_stmt|;
specifier|public
specifier|static
class|class
name|PDTest
implements|implements
name|COSObjectable
block|{
specifier|private
name|int
name|value
decl_stmt|;
specifier|public
name|PDTest
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|PDTest
parameter_list|(
name|COSInteger
name|cosInt
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|cosInt
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|COSInteger
operator|.
name|get
argument_list|(
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
name|value
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|PDTest
name|other
init|=
operator|(
name|PDTest
operator|)
name|obj
decl_stmt|;
return|return
name|value
operator|==
name|other
operator|.
name|value
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|node5
operator|=
operator|new
name|PDNumberTreeNode
argument_list|(
name|PDTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|PDTest
argument_list|>
name|Numbers
init|=
operator|new
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|PDTest
argument_list|>
argument_list|()
decl_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|1
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|89
argument_list|)
argument_list|)
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|2
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|13
argument_list|)
argument_list|)
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|3
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|95
argument_list|)
argument_list|)
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|4
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|51
argument_list|)
argument_list|)
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|5
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|18
argument_list|)
argument_list|)
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|6
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|33
argument_list|)
argument_list|)
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|7
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|85
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|node5
operator|.
name|setNumbers
argument_list|(
name|Numbers
argument_list|)
expr_stmt|;
name|this
operator|.
name|node24
operator|=
operator|new
name|PDNumberTreeNode
argument_list|(
name|PDTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|Numbers
operator|=
operator|new
name|TreeMap
argument_list|<
name|Integer
argument_list|,
name|PDTest
argument_list|>
argument_list|()
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|8
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|54
argument_list|)
argument_list|)
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|9
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|70
argument_list|)
argument_list|)
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|10
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|39
argument_list|)
argument_list|)
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|11
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|30
argument_list|)
argument_list|)
expr_stmt|;
name|Numbers
operator|.
name|put
argument_list|(
literal|12
argument_list|,
operator|new
name|PDTest
argument_list|(
literal|40
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|node24
operator|.
name|setNumbers
argument_list|(
name|Numbers
argument_list|)
expr_stmt|;
name|this
operator|.
name|node2
operator|=
operator|new
name|PDNumberTreeNode
argument_list|(
name|PDTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PDNumberTreeNode
argument_list|>
name|kids
init|=
name|this
operator|.
name|node2
operator|.
name|getKids
argument_list|()
decl_stmt|;
if|if
condition|(
name|kids
operator|==
literal|null
condition|)
block|{
name|kids
operator|=
operator|new
name|COSArrayList
argument_list|<
name|PDNumberTreeNode
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|kids
operator|.
name|add
argument_list|(
name|this
operator|.
name|node5
argument_list|)
expr_stmt|;
name|this
operator|.
name|node2
operator|.
name|setKids
argument_list|(
name|kids
argument_list|)
expr_stmt|;
name|this
operator|.
name|node4
operator|=
operator|new
name|PDNumberTreeNode
argument_list|(
name|PDTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|kids
operator|=
name|this
operator|.
name|node4
operator|.
name|getKids
argument_list|()
expr_stmt|;
if|if
condition|(
name|kids
operator|==
literal|null
condition|)
block|{
name|kids
operator|=
operator|new
name|COSArrayList
argument_list|<
name|PDNumberTreeNode
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|kids
operator|.
name|add
argument_list|(
name|this
operator|.
name|node24
argument_list|)
expr_stmt|;
name|this
operator|.
name|node4
operator|.
name|setKids
argument_list|(
name|kids
argument_list|)
expr_stmt|;
name|this
operator|.
name|node1
operator|=
operator|new
name|PDNumberTreeNode
argument_list|(
name|PDTest
operator|.
name|class
argument_list|)
expr_stmt|;
name|kids
operator|=
name|this
operator|.
name|node1
operator|.
name|getKids
argument_list|()
expr_stmt|;
if|if
condition|(
name|kids
operator|==
literal|null
condition|)
block|{
name|kids
operator|=
operator|new
name|COSArrayList
argument_list|<
name|PDNumberTreeNode
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|kids
operator|.
name|add
argument_list|(
name|this
operator|.
name|node2
argument_list|)
expr_stmt|;
name|kids
operator|.
name|add
argument_list|(
name|this
operator|.
name|node4
argument_list|)
expr_stmt|;
name|this
operator|.
name|node1
operator|.
name|setKids
argument_list|(
name|kids
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetValue
parameter_list|()
throws|throws
name|IOException
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
operator|new
name|PDTest
argument_list|(
literal|51
argument_list|)
argument_list|,
name|this
operator|.
name|node5
operator|.
name|getValue
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
operator|new
name|PDTest
argument_list|(
literal|70
argument_list|)
argument_list|,
name|this
operator|.
name|node1
operator|.
name|getValue
argument_list|(
literal|9
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|node1
operator|.
name|setKids
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|node1
operator|.
name|setNumbers
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|this
operator|.
name|node1
operator|.
name|getValue
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpperLimit
parameter_list|()
throws|throws
name|IOException
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|this
operator|.
name|node5
operator|.
name|getUpperLimit
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|this
operator|.
name|node2
operator|.
name|getUpperLimit
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|this
operator|.
name|node24
operator|.
name|getUpperLimit
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|this
operator|.
name|node4
operator|.
name|getUpperLimit
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|this
operator|.
name|node1
operator|.
name|getUpperLimit
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|node24
operator|.
name|setNumbers
argument_list|(
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|COSObjectable
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|this
operator|.
name|node24
operator|.
name|getUpperLimit
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|node5
operator|.
name|setNumbers
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|this
operator|.
name|node5
operator|.
name|getUpperLimit
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|node1
operator|.
name|setKids
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|this
operator|.
name|node1
operator|.
name|getUpperLimit
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLowerLimit
parameter_list|()
throws|throws
name|IOException
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|this
operator|.
name|node5
operator|.
name|getLowerLimit
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|this
operator|.
name|node2
operator|.
name|getLowerLimit
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|this
operator|.
name|node24
operator|.
name|getLowerLimit
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|this
operator|.
name|node4
operator|.
name|getLowerLimit
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|this
operator|.
name|node1
operator|.
name|getLowerLimit
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|node24
operator|.
name|setNumbers
argument_list|(
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|COSObjectable
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|this
operator|.
name|node24
operator|.
name|getLowerLimit
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|node5
operator|.
name|setNumbers
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|this
operator|.
name|node5
operator|.
name|getLowerLimit
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|node1
operator|.
name|setKids
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|this
operator|.
name|node1
operator|.
name|getLowerLimit
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

