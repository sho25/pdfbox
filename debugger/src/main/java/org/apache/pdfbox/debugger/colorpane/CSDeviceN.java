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
name|Font
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|BoxLayout
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

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JScrollPane
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|COSArray
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
name|pdmodel
operator|.
name|graphics
operator|.
name|color
operator|.
name|PDDeviceN
import|;
end_import

begin_comment
comment|/**  * A class that provides the necessary UI and functionalities to show the DeviceN color space.  *  * @author Khyrul Bashar.  *  */
end_comment

begin_class
specifier|public
class|class
name|CSDeviceN
block|{
specifier|private
specifier|final
name|PDDeviceN
name|deviceN
decl_stmt|;
specifier|private
name|JPanel
name|panel
decl_stmt|;
comment|/**      * Constructor      *      * @param array COSArray instance that holds the DeviceN color space      */
specifier|public
name|CSDeviceN
parameter_list|(
name|COSArray
name|array
parameter_list|)
throws|throws
name|IOException
block|{
name|deviceN
operator|=
operator|new
name|PDDeviceN
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|DeviceNColorant
index|[]
name|colorants
init|=
name|getColorantData
argument_list|()
decl_stmt|;
name|initUI
argument_list|(
name|colorants
argument_list|)
expr_stmt|;
block|}
comment|/**      * Parses the colorant data from the array.      *      * @return the parsed colorants.      * @throws java.io.IOException if the color conversion fails.      */
specifier|private
name|DeviceNColorant
index|[]
name|getColorantData
parameter_list|()
throws|throws
name|IOException
block|{
name|int
name|componentCount
init|=
name|deviceN
operator|.
name|getNumberOfComponents
argument_list|()
decl_stmt|;
name|DeviceNColorant
index|[]
name|colorants
init|=
operator|new
name|DeviceNColorant
index|[
name|componentCount
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|componentCount
condition|;
name|i
operator|++
control|)
block|{
name|DeviceNColorant
name|colorant
init|=
operator|new
name|DeviceNColorant
argument_list|()
decl_stmt|;
name|colorant
operator|.
name|setName
argument_list|(
name|deviceN
operator|.
name|getColorantNames
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|float
index|[]
name|maximum
init|=
operator|new
name|float
index|[
name|componentCount
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|maximum
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|float
index|[]
name|minimum
init|=
operator|new
name|float
index|[
name|componentCount
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|minimum
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|maximum
index|[
name|i
index|]
operator|=
literal|1
expr_stmt|;
name|colorant
operator|.
name|setMaximum
argument_list|(
name|getColorObj
argument_list|(
name|deviceN
operator|.
name|toRGB
argument_list|(
name|maximum
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|colorant
operator|.
name|setMinimum
argument_list|(
name|getColorObj
argument_list|(
name|deviceN
operator|.
name|toRGB
argument_list|(
name|minimum
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|colorants
index|[
name|i
index|]
operator|=
name|colorant
expr_stmt|;
block|}
return|return
name|colorants
return|;
block|}
specifier|private
name|void
name|initUI
parameter_list|(
name|DeviceNColorant
index|[]
name|colorants
parameter_list|)
block|{
name|panel
operator|=
operator|new
name|JPanel
argument_list|()
expr_stmt|;
name|panel
operator|.
name|setLayout
argument_list|(
operator|new
name|BoxLayout
argument_list|(
name|panel
argument_list|,
name|BoxLayout
operator|.
name|Y_AXIS
argument_list|)
argument_list|)
expr_stmt|;
name|panel
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
name|JLabel
name|colorSpaceLabel
init|=
operator|new
name|JLabel
argument_list|(
literal|"DeviceN colorspace"
argument_list|)
decl_stmt|;
name|colorSpaceLabel
operator|.
name|setAlignmentX
argument_list|(
name|Component
operator|.
name|CENTER_ALIGNMENT
argument_list|)
expr_stmt|;
name|colorSpaceLabel
operator|.
name|setFont
argument_list|(
operator|new
name|Font
argument_list|(
name|Font
operator|.
name|MONOSPACED
argument_list|,
name|Font
operator|.
name|BOLD
argument_list|,
literal|30
argument_list|)
argument_list|)
expr_stmt|;
name|DeviceNTableModel
name|tableModel
init|=
operator|new
name|DeviceNTableModel
argument_list|(
name|colorants
argument_list|)
decl_stmt|;
name|JTable
name|table
init|=
operator|new
name|JTable
argument_list|(
name|tableModel
argument_list|)
decl_stmt|;
name|table
operator|.
name|setDefaultRenderer
argument_list|(
name|Color
operator|.
name|class
argument_list|,
operator|new
name|ColorBarCellRenderer
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setRowHeight
argument_list|(
literal|60
argument_list|)
expr_stmt|;
name|JScrollPane
name|scrollPane
init|=
operator|new
name|JScrollPane
argument_list|()
decl_stmt|;
name|scrollPane
operator|.
name|setViewportView
argument_list|(
name|table
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|colorSpaceLabel
argument_list|)
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|scrollPane
argument_list|)
expr_stmt|;
block|}
comment|/**      * return the main panel that hold all the UI elements.      *      * @return JPanel instance      */
specifier|public
name|Component
name|getPanel
parameter_list|()
block|{
return|return
name|panel
return|;
block|}
specifier|private
name|Color
name|getColorObj
parameter_list|(
name|float
index|[]
name|rgbValues
parameter_list|)
block|{
return|return
operator|new
name|Color
argument_list|(
name|rgbValues
index|[
literal|0
index|]
argument_list|,
name|rgbValues
index|[
literal|1
index|]
argument_list|,
name|rgbValues
index|[
literal|2
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit

