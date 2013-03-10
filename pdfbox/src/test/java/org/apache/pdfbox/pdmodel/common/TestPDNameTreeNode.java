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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
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
name|junit
operator|.
name|framework
operator|.
name|Assert
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

begin_comment
comment|/**  * A test case for PDNameTreeNode.  *   * @author Koch  */
end_comment

begin_class
specifier|public
class|class
name|TestPDNameTreeNode
extends|extends
name|TestCase
block|{
specifier|private
name|PDNameTreeNode
name|node1
decl_stmt|;
specifier|private
name|PDNameTreeNode
name|node2
decl_stmt|;
specifier|private
name|PDNameTreeNode
name|node4
decl_stmt|;
specifier|private
name|PDNameTreeNode
name|node5
decl_stmt|;
specifier|private
name|PDNameTreeNode
name|node24
decl_stmt|;
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
name|PDNameTreeNode
argument_list|(
name|COSInteger
operator|.
name|class
argument_list|)
expr_stmt|;
name|SortedMap
argument_list|<
name|String
argument_list|,
name|COSObjectable
argument_list|>
name|names
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|COSObjectable
argument_list|>
argument_list|()
decl_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Actinium"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|89
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Aluminum"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|13
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Americium"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|95
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Antimony"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|51
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Argon"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|18
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Arsenic"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|33
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Astatine"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|85
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|node5
operator|.
name|setNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|this
operator|.
name|node24
operator|=
operator|new
name|PDNameTreeNode
argument_list|(
name|COSInteger
operator|.
name|class
argument_list|)
expr_stmt|;
name|names
operator|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|COSObjectable
argument_list|>
argument_list|()
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Xenon"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|54
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Ytterbium"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|70
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Yttrium"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|39
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Zinc"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|30
argument_list|)
argument_list|)
expr_stmt|;
name|names
operator|.
name|put
argument_list|(
literal|"Zirconium"
argument_list|,
name|COSInteger
operator|.
name|get
argument_list|(
literal|40
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|node24
operator|.
name|setNames
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|this
operator|.
name|node2
operator|=
operator|new
name|PDNameTreeNode
argument_list|(
name|COSInteger
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PDNameTreeNode
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
name|PDNameTreeNode
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
name|PDNameTreeNode
argument_list|(
name|COSInteger
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
name|PDNameTreeNode
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
name|PDNameTreeNode
argument_list|(
name|COSInteger
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
name|PDNameTreeNode
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
name|testUpperLimit
parameter_list|()
throws|throws
name|IOException
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Astatine"
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
literal|"Astatine"
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
literal|"Zirconium"
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
literal|"Zirconium"
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
literal|null
argument_list|,
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
literal|"Actinium"
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
literal|"Actinium"
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
literal|"Xenon"
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
literal|"Xenon"
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
literal|null
argument_list|,
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

