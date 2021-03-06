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
name|debugger
operator|.
name|stringpane
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
name|javax
operator|.
name|swing
operator|.
name|JComponent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTabbedPane
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextPane
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
name|COSString
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
name|debugger
operator|.
name|hexviewer
operator|.
name|HexView
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  */
end_comment

begin_class
specifier|public
class|class
name|StringPane
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEXT_TAB
init|=
literal|"Text View"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HEX_TAB
init|=
literal|"Hex view"
decl_stmt|;
specifier|private
specifier|final
name|JTabbedPane
name|tabbedPane
decl_stmt|;
specifier|public
name|StringPane
parameter_list|(
name|COSString
name|cosString
parameter_list|)
block|{
name|tabbedPane
operator|=
operator|new
name|JTabbedPane
argument_list|()
expr_stmt|;
name|tabbedPane
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|300
argument_list|,
literal|500
argument_list|)
argument_list|)
expr_stmt|;
name|tabbedPane
operator|.
name|addTab
argument_list|(
name|TEXT_TAB
argument_list|,
name|createTextView
argument_list|(
name|cosString
argument_list|)
argument_list|)
expr_stmt|;
name|tabbedPane
operator|.
name|addTab
argument_list|(
name|HEX_TAB
argument_list|,
name|createHexView
argument_list|(
name|cosString
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|JTextPane
name|createTextView
parameter_list|(
name|COSString
name|cosString
parameter_list|)
block|{
name|JTextPane
name|textPane
init|=
operator|new
name|JTextPane
argument_list|()
decl_stmt|;
name|textPane
operator|.
name|setText
argument_list|(
name|getTextString
argument_list|(
name|cosString
argument_list|)
argument_list|)
expr_stmt|;
name|textPane
operator|.
name|setEditable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|textPane
return|;
block|}
specifier|private
name|JComponent
name|createHexView
parameter_list|(
name|COSString
name|cosString
parameter_list|)
block|{
name|HexView
name|hexView
init|=
operator|new
name|HexView
argument_list|(
name|cosString
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|hexView
operator|.
name|getPane
argument_list|()
return|;
block|}
specifier|private
name|String
name|getTextString
parameter_list|(
name|COSString
name|cosString
parameter_list|)
block|{
name|String
name|text
init|=
name|cosString
operator|.
name|getString
argument_list|()
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|text
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
name|Character
operator|.
name|isISOControl
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|text
operator|=
literal|"<"
operator|+
name|cosString
operator|.
name|toHexString
argument_list|()
operator|+
literal|">"
expr_stmt|;
break|break;
block|}
block|}
return|return
literal|""
operator|+
name|text
return|;
block|}
specifier|public
name|JTabbedPane
name|getPane
parameter_list|()
block|{
return|return
name|tabbedPane
return|;
block|}
block|}
end_class

end_unit

