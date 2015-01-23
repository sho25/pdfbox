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
name|fontbox
operator|.
name|ttf
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|encoding
operator|.
name|Encoding
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
name|util
operator|.
name|BoundingBox
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

begin_comment
comment|/**  * A Type 1-equivalent font, i.e. a font which can access glyphs by their PostScript name.  * This is currently a minimal interface and could be expanded if needed.  *  * @author John Hewson  */
end_comment

begin_interface
specifier|public
interface|interface
name|Type1Equivalent
block|{
comment|/**      * The PostScript name of the font.      */
name|String
name|getName
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the Type 1 CharString for the character with the given name.      *      * @return glyph path      * @throws IOException if the path could not be read      */
name|GeneralPath
name|getPath
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the advance width for the character with the given name.      *      * @return glyph advance width      * @throws IOException if the path could not be read      */
name|float
name|getWidth
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns true if the font contains the given glyph.      * @param name PostScript glyph name      */
name|boolean
name|hasGlyph
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the PostScript Encoding vector for the font.      */
name|Encoding
name|getEncoding
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the font's bounding box in PostScript units.      */
name|BoundingBox
name|getFontBBox
parameter_list|()
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

