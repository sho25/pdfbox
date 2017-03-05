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

begin_comment
comment|/**  * A vector outline font, e.g. not Type 3.  *  * @author John Hewson  */
end_comment

begin_interface
specifier|public
interface|interface
name|PDVectorFont
block|{
comment|/**      * Returns the glyph path for the given character code.      *      * @param code character code in a PDF. Not to be confused with unicode.      * @throws java.io.IOException if the font could not be read      */
name|GeneralPath
name|getPath
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the normalized glyph path for the given character code. The resulting path is      * normalized to the PostScript 1000 unit square, and fallback glyphs are returned      * where appropriate, e.g. for missing glyphs.      *        * @param code character code in a PDF. Not to be confused with unicode.      * @throws java.io.IOException if the font could not be read      */
name|GeneralPath
name|getNormalizedPath
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns true if this font contains a glyph for the given character code.      *      * @param code character code in a PDF. Not to be confused with unicode.      */
name|boolean
name|hasGlyph
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

