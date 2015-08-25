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
name|tools
operator|.
name|pdfdebugger
operator|.
name|hexviewer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|FlowLayout
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JLabel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  *  * This shows the currently selected bytes, index, line number and column number.  */
end_comment

begin_class
class|class
name|StatusPane
extends|extends
name|JPanel
block|{
specifier|private
specifier|static
specifier|final
name|int
name|HEIGHT
init|=
literal|20
decl_stmt|;
specifier|private
name|JLabel
name|lineLabel
decl_stmt|;
specifier|private
name|JLabel
name|colLabel
decl_stmt|;
specifier|private
name|JLabel
name|indexLabel
decl_stmt|;
name|StatusPane
parameter_list|()
block|{
name|setLayout
argument_list|(
operator|new
name|FlowLayout
argument_list|(
name|FlowLayout
operator|.
name|LEFT
argument_list|)
argument_list|)
expr_stmt|;
name|createView
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|createView
parameter_list|()
block|{
name|JLabel
name|line
init|=
operator|new
name|JLabel
argument_list|(
literal|"Line:"
argument_list|)
decl_stmt|;
name|JLabel
name|column
init|=
operator|new
name|JLabel
argument_list|(
literal|"Column:"
argument_list|)
decl_stmt|;
name|lineLabel
operator|=
operator|new
name|JLabel
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|lineLabel
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|100
argument_list|,
name|HEIGHT
argument_list|)
argument_list|)
expr_stmt|;
name|colLabel
operator|=
operator|new
name|JLabel
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|colLabel
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|100
argument_list|,
name|HEIGHT
argument_list|)
argument_list|)
expr_stmt|;
name|JLabel
name|index
init|=
operator|new
name|JLabel
argument_list|(
literal|"Index:"
argument_list|)
decl_stmt|;
name|indexLabel
operator|=
operator|new
name|JLabel
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|lineLabel
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|column
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|colLabel
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|indexLabel
argument_list|)
expr_stmt|;
block|}
name|void
name|updateStatus
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|lineLabel
operator|.
name|setText
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|HexModel
operator|.
name|lineNumber
argument_list|(
name|index
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|colLabel
operator|.
name|setText
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|HexModel
operator|.
name|elementIndexInLine
argument_list|(
name|index
argument_list|)
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|indexLabel
operator|.
name|setText
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|index
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

