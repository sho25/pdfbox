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
name|Color
import|;
end_import

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
name|Graphics
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Rectangle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|RenderingHints
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

begin_comment
comment|/**  * @author Khyrul Bashar  *  * This class shows the address of the currently selected byte.  */
end_comment

begin_class
class|class
name|AddressPane
extends|extends
name|JComponent
block|{
specifier|private
name|int
name|totalLine
decl_stmt|;
specifier|private
name|int
name|selectedLine
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|int
name|selectedIndex
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * Constructor.      * @param total int. Total line number needed to show all the bytes.      */
name|AddressPane
parameter_list|(
name|int
name|total
parameter_list|)
block|{
name|totalLine
operator|=
name|total
expr_stmt|;
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
name|HexView
operator|.
name|ADDRESS_PANE_WIDTH
argument_list|,
name|HexView
operator|.
name|CHAR_HEIGHT
operator|*
name|totalLine
argument_list|)
argument_list|)
expr_stmt|;
name|setFont
argument_list|(
name|HexView
operator|.
name|FONT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|paintComponent
parameter_list|(
name|Graphics
name|g
parameter_list|)
block|{
name|super
operator|.
name|paintComponent
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|Graphics2D
name|g2d
init|=
operator|(
name|Graphics2D
operator|)
name|g
decl_stmt|;
name|g2d
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_ANTIALIASING
argument_list|,
name|RenderingHints
operator|.
name|VALUE_ANTIALIAS_ON
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_TEXT_ANTIALIASING
argument_list|,
name|RenderingHints
operator|.
name|VALUE_TEXT_ANTIALIAS_ON
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_FRACTIONALMETRICS
argument_list|,
name|RenderingHints
operator|.
name|VALUE_FRACTIONALMETRICS_ON
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_RENDERING
argument_list|,
name|RenderingHints
operator|.
name|VALUE_RENDER_QUALITY
argument_list|)
expr_stmt|;
name|Rectangle
name|bound
init|=
name|getVisibleRect
argument_list|()
decl_stmt|;
name|int
name|x
init|=
name|HexView
operator|.
name|LINE_INSET
decl_stmt|;
name|int
name|y
init|=
name|bound
operator|.
name|y
decl_stmt|;
if|if
condition|(
name|y
operator|==
literal|0
operator|||
name|y
operator|%
name|HexView
operator|.
name|CHAR_HEIGHT
operator|!=
literal|0
condition|)
block|{
name|y
operator|+=
name|HexView
operator|.
name|CHAR_HEIGHT
operator|-
name|y
operator|%
name|HexView
operator|.
name|CHAR_HEIGHT
expr_stmt|;
block|}
name|int
name|firstLine
init|=
name|y
operator|/
name|HexView
operator|.
name|CHAR_HEIGHT
decl_stmt|;
for|for
control|(
name|int
name|line
init|=
name|firstLine
init|;
name|line
operator|<
name|firstLine
operator|+
name|bound
operator|.
name|getHeight
argument_list|()
operator|/
name|HexView
operator|.
name|CHAR_HEIGHT
condition|;
name|line
operator|++
control|)
block|{
if|if
condition|(
name|line
operator|>
name|totalLine
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|line
operator|==
name|selectedLine
condition|)
block|{
name|paintSelected
argument_list|(
name|g
argument_list|,
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|g
operator|.
name|drawString
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%08X"
argument_list|,
operator|(
name|line
operator|-
literal|1
operator|)
operator|*
literal|16
argument_list|)
argument_list|,
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
name|x
operator|=
name|HexView
operator|.
name|LINE_INSET
expr_stmt|;
name|y
operator|+=
name|HexView
operator|.
name|CHAR_HEIGHT
expr_stmt|;
block|}
block|}
comment|/**      * Paint a selected line      * @param g Graphics instance.      * @param x int. x axis value.      * @param y int. y axis value.      */
specifier|private
name|void
name|paintSelected
parameter_list|(
name|Graphics
name|g
parameter_list|,
name|int
name|x
parameter_list|,
name|int
name|y
parameter_list|)
block|{
name|g
operator|.
name|setColor
argument_list|(
name|HexView
operator|.
name|SELECTED_COLOR
argument_list|)
expr_stmt|;
name|g
operator|.
name|setFont
argument_list|(
name|HexView
operator|.
name|BOLD_FONT
argument_list|)
expr_stmt|;
name|g
operator|.
name|drawString
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%08X"
argument_list|,
name|selectedIndex
argument_list|)
argument_list|,
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|g
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|black
argument_list|)
expr_stmt|;
name|g
operator|.
name|setFont
argument_list|(
name|HexView
operator|.
name|FONT
argument_list|)
expr_stmt|;
block|}
comment|/**      * Updates the line text (index in hexadecimal) for a given index. It is used when a byte is      * selected in hex pane.      * @param index int.      */
name|void
name|setSelected
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|index
operator|!=
name|selectedIndex
condition|)
block|{
name|selectedLine
operator|=
name|HexModel
operator|.
name|lineNumber
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|selectedIndex
operator|=
name|index
expr_stmt|;
name|repaint
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

