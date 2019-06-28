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
name|PDVectorFont
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
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  * A class that shows the glyph table along with unicode characters for SimpleFont.  */
end_comment

begin_class
class|class
name|SimpleFont
extends|extends
name|FontPane
block|{
specifier|public
specifier|static
specifier|final
name|String
name|NO_GLYPH
init|=
literal|"None"
decl_stmt|;
specifier|private
specifier|final
name|FontEncodingView
name|view
decl_stmt|;
specifier|private
name|int
name|totalAvailableGlyph
init|=
literal|0
decl_stmt|;
comment|/**      * Constructor.      * @param font PDSimpleFont instance.      * @throws IOException If fails to parse unicode characters.      */
name|SimpleFont
parameter_list|(
name|PDSimpleFont
name|font
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
index|[]
index|[]
name|tableData
init|=
name|getGlyphs
argument_list|(
name|font
argument_list|)
decl_stmt|;
name|double
index|[]
name|yBounds
init|=
name|getYBounds
argument_list|(
name|tableData
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|attributes
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"Font"
argument_list|,
name|font
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"Encoding"
argument_list|,
name|getEncodingName
argument_list|(
name|font
argument_list|)
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"Glyphs"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|totalAvailableGlyph
argument_list|)
argument_list|)
expr_stmt|;
name|attributes
operator|.
name|put
argument_list|(
literal|"Standard 14"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|font
operator|.
name|isStandard14
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|=
operator|new
name|FontEncodingView
argument_list|(
name|tableData
argument_list|,
name|attributes
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"Code"
block|,
literal|"Glyph Name"
block|,
literal|"Unicode Character"
block|,
literal|"Glyph"
block|}
argument_list|,
name|yBounds
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Object
index|[]
index|[]
name|getGlyphs
parameter_list|(
name|PDSimpleFont
name|font
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
index|[]
index|[]
name|glyphs
init|=
operator|new
name|Object
index|[
literal|256
index|]
index|[
literal|4
index|]
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<=
literal|255
condition|;
name|index
operator|++
control|)
block|{
name|glyphs
index|[
name|index
index|]
index|[
literal|0
index|]
operator|=
name|index
expr_stmt|;
if|if
condition|(
name|font
operator|.
name|getEncoding
argument_list|()
operator|.
name|contains
argument_list|(
name|index
argument_list|)
operator|||
name|font
operator|.
name|toUnicode
argument_list|(
name|index
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|glyphName
init|=
name|font
operator|.
name|getEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|index
argument_list|)
decl_stmt|;
name|glyphs
index|[
name|index
index|]
index|[
literal|1
index|]
operator|=
name|glyphName
expr_stmt|;
name|glyphs
index|[
name|index
index|]
index|[
literal|2
index|]
operator|=
name|font
operator|.
name|toUnicode
argument_list|(
name|index
argument_list|)
expr_stmt|;
if|if
condition|(
name|font
operator|instanceof
name|PDVectorFont
condition|)
block|{
comment|// using names didn't work with the file from PDFBOX-3445
name|glyphs
index|[
name|index
index|]
index|[
literal|3
index|]
operator|=
operator|(
operator|(
name|PDVectorFont
operator|)
name|font
operator|)
operator|.
name|getPath
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|glyphs
index|[
name|index
index|]
index|[
literal|3
index|]
operator|=
name|font
operator|.
name|getPath
argument_list|(
name|glyphName
argument_list|)
expr_stmt|;
block|}
name|totalAvailableGlyph
operator|++
expr_stmt|;
block|}
else|else
block|{
name|glyphs
index|[
name|index
index|]
index|[
literal|1
index|]
operator|=
name|NO_GLYPH
expr_stmt|;
name|glyphs
index|[
name|index
index|]
index|[
literal|2
index|]
operator|=
name|NO_GLYPH
expr_stmt|;
name|glyphs
index|[
name|index
index|]
index|[
literal|3
index|]
operator|=
name|font
operator|.
name|getPath
argument_list|(
literal|".notdef"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|glyphs
return|;
block|}
specifier|private
name|String
name|getEncodingName
parameter_list|(
name|PDSimpleFont
name|font
parameter_list|)
block|{
return|return
name|font
operator|.
name|getEncoding
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|JPanel
name|getPanel
parameter_list|()
block|{
return|return
name|view
operator|.
name|getPanel
argument_list|()
return|;
block|}
block|}
end_class

end_unit

