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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|FontBoxFont
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
name|TrueTypeFont
import|;
end_import

begin_comment
comment|/**  * Font mapper, locates non-embedded fonts. If you implement this then you're responsible for  * caching the fonts. SoftReference&lt;FontBoxFont&gt; is recommended.  *  * @author John Hewson  */
end_comment

begin_interface
specifier|public
interface|interface
name|FontMapper
block|{
comment|/**      * Finds a TrueType font with the given PostScript name, or a suitable substitute, or null.      *      * @param fontDescriptor FontDescriptor      */
name|FontMapping
argument_list|<
name|TrueTypeFont
argument_list|>
name|getTrueTypeFont
parameter_list|(
name|String
name|baseFont
parameter_list|,
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
function_decl|;
comment|/**      * Finds a font with the given PostScript name, or a suitable substitute, or null. This allows      * any font to be substituted with a PFB, TTF or OTF.      *      * @param fontDescriptor the FontDescriptor of the font to find      */
name|FontMapping
argument_list|<
name|FontBoxFont
argument_list|>
name|getFontBoxFont
parameter_list|(
name|String
name|baseFont
parameter_list|,
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
function_decl|;
comment|/**      * Finds a CFF CID-Keyed font with the given PostScript name, or a suitable substitute, or null.      * This method can also map CJK fonts via their CIDSystemInfo (ROS).      *       * @param fontDescriptor FontDescriptor      * @param cidSystemInfo the CID system info, e.g. "Adobe-Japan1", if any.      */
name|CIDFontMapping
name|getCIDFont
parameter_list|(
name|String
name|baseFont
parameter_list|,
name|PDFontDescriptor
name|fontDescriptor
parameter_list|,
name|PDCIDSystemInfo
name|cidSystemInfo
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

