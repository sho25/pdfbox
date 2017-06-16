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
name|rendering
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
name|HashMap
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
name|pdmodel
operator|.
name|font
operator|.
name|PDFontLike
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
name|PDVectorFont
import|;
end_import

begin_comment
comment|/**  * A simple glyph outline cache.  *  * @author John Hewson  */
end_comment

begin_class
specifier|final
class|class
name|GlyphCache
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
name|GlyphCache
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|PDVectorFont
name|font
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|GeneralPath
argument_list|>
name|cache
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|GlyphCache
parameter_list|(
name|PDVectorFont
name|font
parameter_list|)
block|{
name|this
operator|.
name|font
operator|=
name|font
expr_stmt|;
block|}
specifier|public
name|GeneralPath
name|getPathForCharacterCode
parameter_list|(
name|int
name|code
parameter_list|)
block|{
name|GeneralPath
name|path
init|=
name|cache
operator|.
name|get
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
return|return
name|path
return|;
block|}
try|try
block|{
if|if
condition|(
operator|!
name|font
operator|.
name|hasGlyph
argument_list|(
name|code
argument_list|)
condition|)
block|{
name|String
name|fontName
init|=
operator|(
operator|(
name|PDFontLike
operator|)
name|font
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|font
operator|instanceof
name|PDType0Font
condition|)
block|{
name|int
name|cid
init|=
operator|(
operator|(
name|PDType0Font
operator|)
name|font
operator|)
operator|.
name|codeToCID
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|String
name|cidHex
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%04x"
argument_list|,
name|cid
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"No glyph for "
operator|+
name|code
operator|+
literal|" (CID "
operator|+
name|cidHex
operator|+
literal|") in font "
operator|+
name|fontName
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"No glyph for "
operator|+
name|code
operator|+
literal|" in "
operator|+
name|font
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" "
operator|+
name|fontName
operator|+
literal|" (embedded or system font used: "
operator|+
operator|(
operator|(
name|PDSimpleFont
operator|)
name|font
operator|)
operator|.
name|getFontBoxFont
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No glyph for "
operator|+
name|code
operator|+
literal|" in font "
operator|+
name|fontName
argument_list|)
expr_stmt|;
block|}
block|}
name|path
operator|=
name|font
operator|.
name|getNormalizedPath
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|code
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return
name|path
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// todo: escalate this error?
name|LOG
operator|.
name|error
argument_list|(
literal|"Glyph rendering failed"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
operator|new
name|GeneralPath
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

