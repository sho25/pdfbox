begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2015 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|fontencodingpane
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
name|javax
operator|.
name|swing
operator|.
name|JPanel
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
name|COSDictionary
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
name|COSName
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
name|PDResources
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
name|font
operator|.
name|PDCIDFontType2
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
name|font
operator|.
name|PDFont
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
name|font
operator|.
name|PDSimpleFont
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
name|font
operator|.
name|PDType0Font
import|;
end_import

begin_interface
interface|interface
name|FontPane
block|{
name|JPanel
name|getPanel
parameter_list|()
function_decl|;
block|}
end_interface

begin_comment
comment|/**  * @author Khyrul Bashar  *  * A class that shows the glyph table or CIDToGID map depending on the font type. PDSimple and  * PDType0Font are supported.  */
end_comment

begin_class
specifier|public
class|class
name|FontEncodingPaneController
block|{
specifier|private
name|FontPane
name|fontPane
decl_stmt|;
comment|/**      * Constructor.      * @param fontName COSName instance, Font name in the fonts dictionary.      * @param dictionary COSDictionary instance for resources which resides the font.      */
specifier|public
name|FontEncodingPaneController
parameter_list|(
name|COSName
name|fontName
parameter_list|,
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|PDResources
name|resources
init|=
operator|new
name|PDResources
argument_list|(
name|dictionary
argument_list|)
decl_stmt|;
try|try
block|{
name|PDFont
name|font
init|=
name|resources
operator|.
name|getFont
argument_list|(
name|fontName
argument_list|)
decl_stmt|;
if|if
condition|(
name|font
operator|instanceof
name|PDSimpleFont
condition|)
block|{
name|fontPane
operator|=
operator|new
name|SimpleFont
argument_list|(
operator|(
name|PDSimpleFont
operator|)
name|font
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|font
operator|instanceof
name|PDType0Font
operator|&&
operator|(
operator|(
name|PDType0Font
operator|)
name|font
operator|)
operator|.
name|getDescendantFont
argument_list|()
operator|instanceof
name|PDCIDFontType2
condition|)
block|{
name|fontPane
operator|=
operator|new
name|Type0Font
argument_list|(
call|(
name|PDCIDFontType2
call|)
argument_list|(
operator|(
name|PDType0Font
operator|)
name|font
argument_list|)
operator|.
name|getDescendantFont
argument_list|()
argument_list|,
name|font
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Return a pane to display details of a font.      *       * @return a pane for font information, or null if that font type is not supported.      */
specifier|public
name|JPanel
name|getPane
parameter_list|()
block|{
if|if
condition|(
name|fontPane
operator|!=
literal|null
condition|)
block|{
return|return
name|fontPane
operator|.
name|getPanel
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

