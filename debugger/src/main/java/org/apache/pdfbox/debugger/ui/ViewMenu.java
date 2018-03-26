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
name|ui
package|;
end_package

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
name|javax
operator|.
name|swing
operator|.
name|JCheckBoxMenuItem
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JMenu
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JMenuItem
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
name|PDFDebugger
import|;
end_import

begin_class
specifier|public
class|class
name|ViewMenu
extends|extends
name|MenuBase
block|{
specifier|private
specifier|static
name|ViewMenu
name|instance
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SHOW_TEXT_STRIPPER
init|=
literal|"Show TextStripper TextPositions"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SHOW_TEXT_STRIPPER_BEADS
init|=
literal|"Show TextStripper Beads"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SHOW_FONT_BBOX
init|=
literal|"Show Approximate Text Bounds"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SHOW_GLYPH_BOUNDS
init|=
literal|"Show Glyph Bounds"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ALLOW_SUBSAMPLING
init|=
literal|"Allow subsampling"
decl_stmt|;
specifier|private
name|JMenuItem
name|viewModeItem
decl_stmt|;
specifier|private
name|JCheckBoxMenuItem
name|showTextStripper
decl_stmt|;
specifier|private
name|JCheckBoxMenuItem
name|showTextStripperBeads
decl_stmt|;
specifier|private
name|JCheckBoxMenuItem
name|showFontBBox
decl_stmt|;
specifier|private
name|JCheckBoxMenuItem
name|showGlyphBounds
decl_stmt|;
specifier|private
name|JCheckBoxMenuItem
name|allowSubsampling
decl_stmt|;
specifier|private
name|PDFDebugger
name|pdfDebugger
decl_stmt|;
comment|/**      * Constructor.      */
specifier|private
name|ViewMenu
parameter_list|(
name|PDFDebugger
name|pdfDebugger
parameter_list|)
block|{
name|this
operator|.
name|pdfDebugger
operator|=
name|pdfDebugger
expr_stmt|;
name|setMenu
argument_list|(
name|createViewMenu
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Provides the ViewMenu instance.      *      * @return ViewMenu instance.      */
specifier|public
specifier|static
name|ViewMenu
name|getInstance
parameter_list|(
name|PDFDebugger
name|pdfDebugger
parameter_list|)
block|{
if|if
condition|(
name|instance
operator|==
literal|null
condition|)
block|{
name|instance
operator|=
operator|new
name|ViewMenu
argument_list|(
name|pdfDebugger
argument_list|)
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
comment|/**      * Test if the one of the rendering options has been selected      *       * @param actionCommand the actioCommand of the menu event       * @return true if the actionCommand matches one of the rendering options      */
specifier|public
specifier|static
name|boolean
name|isRenderingOptions
parameter_list|(
name|String
name|actionCommand
parameter_list|)
block|{
return|return
name|SHOW_TEXT_STRIPPER
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
operator|||
name|SHOW_TEXT_STRIPPER_BEADS
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
operator|||
name|SHOW_FONT_BBOX
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
operator|||
name|SHOW_GLYPH_BOUNDS
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
operator|||
name|ALLOW_SUBSAMPLING
operator|.
name|equals
argument_list|(
name|actionCommand
argument_list|)
return|;
block|}
comment|/**      * State if the TextStripper TextPositions shall be shown.      *       * @return the selection state      */
specifier|public
specifier|static
name|boolean
name|isShowTextStripper
parameter_list|()
block|{
return|return
name|instance
operator|.
name|showTextStripper
operator|.
name|isSelected
argument_list|()
return|;
block|}
comment|/**      * State if the article beads shall be shown.      *       * @return the selection state      */
specifier|public
specifier|static
name|boolean
name|isShowTextStripperBeads
parameter_list|()
block|{
return|return
name|instance
operator|.
name|showTextStripperBeads
operator|.
name|isSelected
argument_list|()
return|;
block|}
comment|/**      * State if the fonts bounding box shall be shown.      *       * @return the selection state      */
specifier|public
specifier|static
name|boolean
name|isShowFontBBox
parameter_list|()
block|{
return|return
name|instance
operator|.
name|showFontBBox
operator|.
name|isSelected
argument_list|()
return|;
block|}
comment|/**      * State if the bounds of individual glyphs shall be shown.      *       * @return the selection state      */
specifier|public
specifier|static
name|boolean
name|isShowGlyphBounds
parameter_list|()
block|{
return|return
name|instance
operator|.
name|showGlyphBounds
operator|.
name|isSelected
argument_list|()
return|;
block|}
comment|/**      * State if subsampling for image rendering shall be used.      *       * @return the selection state      */
specifier|public
specifier|static
name|boolean
name|isAllowSubsampling
parameter_list|()
block|{
return|return
name|instance
operator|.
name|allowSubsampling
operator|.
name|isSelected
argument_list|()
return|;
block|}
specifier|private
name|JMenu
name|createViewMenu
parameter_list|()
block|{
name|JMenu
name|viewMenu
init|=
operator|new
name|JMenu
argument_list|(
literal|"View"
argument_list|)
decl_stmt|;
name|viewMenu
operator|.
name|setMnemonic
argument_list|(
literal|'V'
argument_list|)
expr_stmt|;
if|if
condition|(
name|pdfDebugger
operator|.
name|isPageMode
argument_list|()
condition|)
block|{
name|viewModeItem
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Show Internal Structure"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|viewModeItem
operator|=
operator|new
name|JMenuItem
argument_list|(
literal|"Show Pages"
argument_list|)
expr_stmt|;
block|}
name|viewModeItem
operator|.
name|addActionListener
argument_list|(
operator|new
name|ActionListener
argument_list|()
block|{
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
if|if
condition|(
name|pdfDebugger
operator|.
name|isPageMode
argument_list|()
condition|)
block|{
name|viewModeItem
operator|.
name|setText
argument_list|(
literal|"Show Pages"
argument_list|)
expr_stmt|;
name|pdfDebugger
operator|.
name|setPageMode
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|viewModeItem
operator|.
name|setText
argument_list|(
literal|"Show Internal Structure"
argument_list|)
expr_stmt|;
name|pdfDebugger
operator|.
name|setPageMode
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pdfDebugger
operator|.
name|hasDocument
argument_list|()
condition|)
block|{
name|pdfDebugger
operator|.
name|initTree
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|add
argument_list|(
name|viewModeItem
argument_list|)
expr_stmt|;
name|ZoomMenu
name|zoomMenu
init|=
name|ZoomMenu
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|zoomMenu
operator|.
name|setEnableMenu
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|add
argument_list|(
name|zoomMenu
operator|.
name|getMenu
argument_list|()
argument_list|)
expr_stmt|;
name|RotationMenu
name|rotationMenu
init|=
name|RotationMenu
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|rotationMenu
operator|.
name|setEnableMenu
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|add
argument_list|(
name|rotationMenu
operator|.
name|getMenu
argument_list|()
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|showTextStripper
operator|=
operator|new
name|JCheckBoxMenuItem
argument_list|(
name|SHOW_TEXT_STRIPPER
argument_list|)
expr_stmt|;
name|showTextStripper
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|add
argument_list|(
name|showTextStripper
argument_list|)
expr_stmt|;
name|showTextStripperBeads
operator|=
operator|new
name|JCheckBoxMenuItem
argument_list|(
name|SHOW_TEXT_STRIPPER_BEADS
argument_list|)
expr_stmt|;
name|showTextStripperBeads
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|add
argument_list|(
name|showTextStripperBeads
argument_list|)
expr_stmt|;
name|showFontBBox
operator|=
operator|new
name|JCheckBoxMenuItem
argument_list|(
name|SHOW_FONT_BBOX
argument_list|)
expr_stmt|;
name|showFontBBox
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|add
argument_list|(
name|showFontBBox
argument_list|)
expr_stmt|;
name|showGlyphBounds
operator|=
operator|new
name|JCheckBoxMenuItem
argument_list|(
name|SHOW_GLYPH_BOUNDS
argument_list|)
expr_stmt|;
name|showGlyphBounds
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|add
argument_list|(
name|showGlyphBounds
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|addSeparator
argument_list|()
expr_stmt|;
name|allowSubsampling
operator|=
operator|new
name|JCheckBoxMenuItem
argument_list|(
name|ALLOW_SUBSAMPLING
argument_list|)
expr_stmt|;
name|allowSubsampling
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|viewMenu
operator|.
name|add
argument_list|(
name|allowSubsampling
argument_list|)
expr_stmt|;
return|return
name|viewMenu
return|;
block|}
block|}
end_class

end_unit
