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
name|awt
operator|.
name|GridBagConstraints
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|GridBagLayout
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Insets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|ActionListener
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
name|java
operator|.
name|util
operator|.
name|Dictionary
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Hashtable
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
name|JSlider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTextField
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|border
operator|.
name|BevelBorder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|ChangeEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|event
operator|.
name|ChangeListener
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
name|PDSeparation
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar.  */
end_comment

begin_comment
comment|/**  *A class that provides the necessary UI and functionalities to show the Separation color space.  */
end_comment

begin_class
specifier|public
class|class
name|CSSeparation
implements|implements
name|ChangeListener
implements|,
name|ActionListener
block|{
specifier|private
name|JSlider
name|slider
decl_stmt|;
specifier|private
name|JTextField
name|tintField
decl_stmt|;
specifier|private
name|JLabel
name|colorBar
decl_stmt|;
specifier|private
name|JPanel
name|panel
decl_stmt|;
specifier|private
name|PDSeparation
name|separation
decl_stmt|;
specifier|private
name|float
name|tintValue
init|=
literal|1
decl_stmt|;
comment|/**      * Constructor      * @param array COSArray instance of the separation color space.      */
specifier|public
name|CSSeparation
parameter_list|(
name|COSArray
name|array
parameter_list|)
block|{
try|try
block|{
name|separation
operator|=
operator|new
name|PDSeparation
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|initUI
argument_list|()
expr_stmt|;
name|initValues
argument_list|()
expr_stmt|;
block|}
comment|/**      * initialize all the UI elements and arrange them.      */
specifier|private
name|void
name|initUI
parameter_list|()
block|{
name|Font
name|boldFont
init|=
operator|new
name|Font
argument_list|(
literal|"Monospaced"
argument_list|,
name|Font
operator|.
name|BOLD
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|GridBagConstraints
name|gbc
init|=
operator|new
name|GridBagConstraints
argument_list|()
decl_stmt|;
name|gbc
operator|.
name|insets
operator|=
operator|new
name|Insets
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|JPanel
name|inputPanel
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|GridBagLayout
argument_list|()
argument_list|)
decl_stmt|;
name|slider
operator|=
operator|new
name|JSlider
argument_list|(
literal|0
argument_list|,
literal|100
argument_list|,
literal|50
argument_list|)
expr_stmt|;
name|slider
operator|.
name|setMajorTickSpacing
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|slider
operator|.
name|setPaintTicks
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Dictionary
name|labelTable
init|=
operator|new
name|Hashtable
argument_list|()
decl_stmt|;
name|JLabel
name|lightest
init|=
operator|new
name|JLabel
argument_list|(
literal|"lightest"
argument_list|)
decl_stmt|;
name|lightest
operator|.
name|setFont
argument_list|(
operator|new
name|Font
argument_list|(
literal|"Monospaced"
argument_list|,
name|Font
operator|.
name|BOLD
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|JLabel
name|darkest
init|=
operator|new
name|JLabel
argument_list|(
literal|"darkest"
argument_list|)
decl_stmt|;
name|darkest
operator|.
name|setFont
argument_list|(
operator|new
name|Font
argument_list|(
literal|"Monospaced"
argument_list|,
name|Font
operator|.
name|BOLD
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|JLabel
name|midPoint
init|=
operator|new
name|JLabel
argument_list|(
literal|"0.5"
argument_list|)
decl_stmt|;
name|midPoint
operator|.
name|setFont
argument_list|(
operator|new
name|Font
argument_list|(
literal|"Monospaced"
argument_list|,
name|Font
operator|.
name|BOLD
argument_list|,
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|labelTable
operator|.
name|put
argument_list|(
literal|0
argument_list|,
name|lightest
argument_list|)
expr_stmt|;
name|labelTable
operator|.
name|put
argument_list|(
literal|50
argument_list|,
name|midPoint
argument_list|)
expr_stmt|;
name|labelTable
operator|.
name|put
argument_list|(
literal|100
argument_list|,
name|darkest
argument_list|)
expr_stmt|;
name|slider
operator|.
name|setPaintLabels
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|slider
operator|.
name|setLabelTable
argument_list|(
name|labelTable
argument_list|)
expr_stmt|;
name|slider
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|gbc
operator|.
name|gridx
operator|=
literal|0
expr_stmt|;
name|gbc
operator|.
name|gridy
operator|=
literal|0
expr_stmt|;
name|gbc
operator|.
name|gridwidth
operator|=
literal|10
expr_stmt|;
name|gbc
operator|.
name|weightx
operator|=
literal|1
expr_stmt|;
name|gbc
operator|.
name|fill
operator|=
name|GridBagConstraints
operator|.
name|HORIZONTAL
expr_stmt|;
name|inputPanel
operator|.
name|add
argument_list|(
name|slider
argument_list|,
name|gbc
argument_list|)
expr_stmt|;
name|JLabel
name|tintLabel
init|=
operator|new
name|JLabel
argument_list|(
literal|"Tint Value:"
argument_list|)
decl_stmt|;
name|tintLabel
operator|.
name|setFont
argument_list|(
name|boldFont
argument_list|)
expr_stmt|;
name|gbc
operator|.
name|gridx
operator|=
literal|0
expr_stmt|;
name|gbc
operator|.
name|gridy
operator|=
literal|1
expr_stmt|;
name|gbc
operator|.
name|weightx
operator|=
literal|0.5
expr_stmt|;
name|gbc
operator|.
name|gridwidth
operator|=
literal|1
expr_stmt|;
name|gbc
operator|.
name|fill
operator|=
name|GridBagConstraints
operator|.
name|HORIZONTAL
expr_stmt|;
name|inputPanel
operator|.
name|add
argument_list|(
name|tintLabel
argument_list|,
name|gbc
argument_list|)
expr_stmt|;
name|tintField
operator|=
operator|new
name|JTextField
argument_list|()
expr_stmt|;
name|tintField
operator|.
name|addActionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|tintField
operator|.
name|setPreferredSize
argument_list|(
operator|new
name|Dimension
argument_list|(
literal|10
argument_list|,
literal|30
argument_list|)
argument_list|)
expr_stmt|;
name|gbc
operator|.
name|gridx
operator|=
literal|1
expr_stmt|;
name|gbc
operator|.
name|weightx
operator|=
literal|0.5
expr_stmt|;
name|gbc
operator|.
name|gridwidth
operator|=
literal|1
expr_stmt|;
name|gbc
operator|.
name|fill
operator|=
name|GridBagConstraints
operator|.
name|HORIZONTAL
expr_stmt|;
name|inputPanel
operator|.
name|add
argument_list|(
name|tintField
argument_list|,
name|gbc
argument_list|)
expr_stmt|;
name|JPanel
name|contentPanel
init|=
operator|new
name|JPanel
argument_list|(
operator|new
name|GridBagLayout
argument_list|()
argument_list|)
decl_stmt|;
name|GridBagConstraints
name|gbc2
init|=
operator|new
name|GridBagConstraints
argument_list|()
decl_stmt|;
name|gbc2
operator|.
name|gridx
operator|=
literal|0
expr_stmt|;
name|gbc2
operator|.
name|gridy
operator|=
literal|0
expr_stmt|;
name|gbc2
operator|.
name|gridwidth
operator|=
literal|2
expr_stmt|;
name|gbc2
operator|.
name|weightx
operator|=
literal|0.3
expr_stmt|;
name|gbc2
operator|.
name|weighty
operator|=
literal|1
expr_stmt|;
name|gbc2
operator|.
name|fill
operator|=
name|GridBagConstraints
operator|.
name|HORIZONTAL
expr_stmt|;
name|contentPanel
operator|.
name|add
argument_list|(
name|inputPanel
argument_list|,
name|gbc2
argument_list|)
expr_stmt|;
name|colorBar
operator|=
operator|new
name|JLabel
argument_list|()
expr_stmt|;
name|colorBar
operator|.
name|setOpaque
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|gbc2
operator|.
name|gridx
operator|=
literal|2
expr_stmt|;
name|gbc2
operator|.
name|weightx
operator|=
literal|0.7
expr_stmt|;
name|gbc2
operator|.
name|gridwidth
operator|=
literal|4
expr_stmt|;
name|gbc2
operator|.
name|gridheight
operator|=
literal|2
expr_stmt|;
name|gbc2
operator|.
name|fill
operator|=
name|GridBagConstraints
operator|.
name|BOTH
expr_stmt|;
name|contentPanel
operator|.
name|add
argument_list|(
name|colorBar
argument_list|,
name|gbc2
argument_list|)
expr_stmt|;
name|setColorBarBorder
argument_list|()
expr_stmt|;
name|panel
operator|=
operator|new
name|JPanel
argument_list|(
operator|new
name|GridBagLayout
argument_list|()
argument_list|)
expr_stmt|;
name|JLabel
name|colorantNameLabel
init|=
operator|new
name|JLabel
argument_list|(
literal|"Colorant: "
operator|+
name|separation
operator|.
name|getColorantName
argument_list|()
argument_list|)
decl_stmt|;
name|colorantNameLabel
operator|.
name|setFont
argument_list|(
name|boldFont
argument_list|)
expr_stmt|;
name|GridBagConstraints
name|maingbc
init|=
operator|new
name|GridBagConstraints
argument_list|()
decl_stmt|;
name|maingbc
operator|.
name|gridx
operator|=
literal|0
expr_stmt|;
name|maingbc
operator|.
name|gridy
operator|=
literal|0
expr_stmt|;
name|maingbc
operator|.
name|weightx
operator|=
literal|1
expr_stmt|;
name|maingbc
operator|.
name|weighty
operator|=
literal|0.03
expr_stmt|;
name|maingbc
operator|.
name|anchor
operator|=
name|GridBagConstraints
operator|.
name|FIRST_LINE_START
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|colorantNameLabel
argument_list|,
name|maingbc
argument_list|)
expr_stmt|;
name|maingbc
operator|.
name|gridx
operator|=
literal|0
expr_stmt|;
name|maingbc
operator|.
name|gridy
operator|=
literal|1
expr_stmt|;
name|maingbc
operator|.
name|weighty
operator|=
literal|0.97
expr_stmt|;
name|maingbc
operator|.
name|gridwidth
operator|=
literal|10
expr_stmt|;
name|maingbc
operator|.
name|fill
operator|=
name|GridBagConstraints
operator|.
name|HORIZONTAL
expr_stmt|;
name|panel
operator|.
name|add
argument_list|(
name|contentPanel
argument_list|,
name|maingbc
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|initValues
parameter_list|()
block|{
name|slider
operator|.
name|setValue
argument_list|(
name|getIntRepresentation
argument_list|(
name|tintValue
argument_list|)
argument_list|)
expr_stmt|;
name|tintField
operator|.
name|setText
argument_list|(
name|Float
operator|.
name|toString
argument_list|(
name|tintValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * return the main panel that hold all the UI elements.      * @return JPanel instance      */
specifier|public
name|JPanel
name|getPanel
parameter_list|()
block|{
return|return
name|panel
return|;
block|}
comment|/**      * input changed in slider.      * @param changeEvent      */
annotation|@
name|Override
specifier|public
name|void
name|stateChanged
parameter_list|(
name|ChangeEvent
name|changeEvent
parameter_list|)
block|{
name|int
name|value
init|=
name|slider
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|tintValue
operator|=
name|getFloatRepresentation
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|tintField
operator|.
name|setText
argument_list|(
name|Float
operator|.
name|toString
argument_list|(
name|tintValue
argument_list|)
argument_list|)
expr_stmt|;
name|updateColorBar
argument_list|()
expr_stmt|;
block|}
comment|/**      * input changed in text field.      * @param actionEvent      */
annotation|@
name|Override
specifier|public
name|void
name|actionPerformed
parameter_list|(
name|ActionEvent
name|actionEvent
parameter_list|)
block|{
name|String
name|input
init|=
name|tintField
operator|.
name|getText
argument_list|()
decl_stmt|;
try|try
block|{
name|tintValue
operator|=
name|Float
operator|.
name|parseFloat
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|slider
operator|.
name|setValue
argument_list|(
name|getIntRepresentation
argument_list|(
name|tintValue
argument_list|)
argument_list|)
expr_stmt|;
name|updateColorBar
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
name|tintField
operator|.
name|setText
argument_list|(
name|Float
operator|.
name|toString
argument_list|(
name|tintValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|updateColorBar
parameter_list|()
block|{
try|try
block|{
name|float
index|[]
name|rgbValues
init|=
name|separation
operator|.
name|toRGB
argument_list|(
operator|new
name|float
index|[]
block|{
name|tintValue
block|}
argument_list|)
decl_stmt|;
name|colorBar
operator|.
name|setBackground
argument_list|(
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
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Set a little border around colorbar. color of the border is the darkest of the colorant.      */
specifier|private
name|void
name|setColorBarBorder
parameter_list|()
block|{
try|try
block|{
name|float
index|[]
name|rgbValues
init|=
name|separation
operator|.
name|toRGB
argument_list|(
operator|new
name|float
index|[]
block|{
literal|1
block|}
argument_list|)
decl_stmt|;
name|Color
name|darkest
init|=
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
decl_stmt|;
name|colorBar
operator|.
name|setBorder
argument_list|(
operator|new
name|BevelBorder
argument_list|(
name|BevelBorder
operator|.
name|LOWERED
argument_list|,
name|darkest
argument_list|,
name|darkest
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|float
name|getFloatRepresentation
parameter_list|(
name|int
name|value
parameter_list|)
block|{
return|return
operator|(
name|float
operator|)
name|value
operator|/
literal|100
return|;
block|}
specifier|private
name|int
name|getIntRepresentation
parameter_list|(
name|float
name|value
parameter_list|)
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|value
operator|*
literal|100
argument_list|)
return|;
block|}
block|}
end_class

end_unit

