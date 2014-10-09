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
name|pdmodel
operator|.
name|font
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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|Type1Equivalent
import|;
end_import

begin_comment
comment|/**  * A Type 1-equivalent font in a PDF, i.e. a font which can access glyphs by their PostScript name.  * May be a PFB, CFF, or TTF.  *  * @author John Hewson  */
end_comment

begin_interface
specifier|public
interface|interface
name|PDType1Equivalent
extends|extends
name|PDFontLike
block|{
comment|/**      * Returns the name of this font.      */
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Returns the glyph name for the given character code.      *      * @param code character code      * @return PostScript glyph name      */
specifier|public
name|String
name|codeToName
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the glyph path for the given character code.      * @param name PostScript glyph name      * @throws java.io.IOException if the font could not be read      */
specifier|public
name|GeneralPath
name|getPath
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the embedded or system font for rendering. This font is a Type 1-equivalent, but      * may not be a Type 1 font, it could be a CFF font or TTF font. If there is no suitable font      * then the fallback font will be returned: this method never returns null.      */
specifier|public
name|Type1Equivalent
name|getType1Equivalent
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

