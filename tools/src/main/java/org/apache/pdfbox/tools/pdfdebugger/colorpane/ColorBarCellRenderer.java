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
name|colorpane
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
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
name|JTable
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|table
operator|.
name|TableCellRenderer
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar.  */
end_comment

begin_comment
comment|/**  * ColorBarCellRenderer class that says how to render color bar columns  */
end_comment

begin_class
specifier|public
class|class
name|ColorBarCellRenderer
implements|implements
name|TableCellRenderer
block|{
annotation|@
name|Override
specifier|public
name|Component
name|getTableCellRendererComponent
parameter_list|(
name|JTable
name|jTable
parameter_list|,
name|Object
name|o
parameter_list|,
name|boolean
name|b
parameter_list|,
name|boolean
name|b2
parameter_list|,
name|int
name|i
parameter_list|,
name|int
name|i2
parameter_list|)
block|{
name|JLabel
name|colorBar
init|=
operator|new
name|JLabel
argument_list|()
decl_stmt|;
name|colorBar
operator|.
name|setOpaque
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|colorBar
operator|.
name|setBackground
argument_list|(
operator|(
name|Color
operator|)
name|o
argument_list|)
expr_stmt|;
return|return
name|colorBar
return|;
block|}
block|}
end_class

end_unit

