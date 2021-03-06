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
name|debugger
operator|.
name|fontencodingpane
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|GeneralPath
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|Rectangle2D
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|PDType3Font
import|;
end_import

begin_class
specifier|abstract
class|class
name|FontPane
block|{
specifier|abstract
name|JPanel
name|getPanel
parameter_list|()
function_decl|;
comment|/**      * Calculate vertical bounds common to all rendered glyphs.      *      * @param tableData      * @param glyphIndex the table index that has glyphs.      * @return an array with two elements: min lower bound (but max 0), and max upper bound (but min      * 0).      */
name|double
index|[]
name|getYBounds
parameter_list|(
name|Object
index|[]
index|[]
name|tableData
parameter_list|,
name|int
name|glyphIndex
parameter_list|)
block|{
name|double
name|minY
init|=
literal|0
decl_stmt|;
name|double
name|maxY
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
index|[]
name|aTableData
range|:
name|tableData
control|)
block|{
name|GeneralPath
name|path
init|=
operator|(
name|GeneralPath
operator|)
name|aTableData
index|[
name|glyphIndex
index|]
decl_stmt|;
name|Rectangle2D
name|bounds2D
init|=
name|path
operator|.
name|getBounds2D
argument_list|()
decl_stmt|;
if|if
condition|(
name|bounds2D
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|minY
operator|=
name|Math
operator|.
name|min
argument_list|(
name|minY
argument_list|,
name|bounds2D
operator|.
name|getMinY
argument_list|()
argument_list|)
expr_stmt|;
name|maxY
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxY
argument_list|,
name|bounds2D
operator|.
name|getMaxY
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|double
index|[]
block|{
name|minY
block|,
name|maxY
block|}
return|;
block|}
block|}
end_class

begin_comment
comment|/**  * @author Khyrul Bashar  *  * A class that shows the glyph table or CIDToGID map depending on the font type. PDSimple and  * PDType0Font are supported.  */
end_comment

begin_class
specifier|public
class|class
name|FontEncodingPaneController
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FontEncodingPaneController
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|PDType3Font
condition|)
block|{
name|fontPane
operator|=
operator|new
name|Type3Font
argument_list|(
operator|(
name|PDType3Font
operator|)
name|font
argument_list|,
name|resources
argument_list|)
expr_stmt|;
block|}
elseif|else
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
condition|)
block|{
name|fontPane
operator|=
operator|new
name|Type0Font
argument_list|(
operator|(
operator|(
name|PDType0Font
operator|)
name|font
operator|)
operator|.
name|getDescendantFont
argument_list|()
argument_list|,
operator|(
name|PDType0Font
operator|)
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
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
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

