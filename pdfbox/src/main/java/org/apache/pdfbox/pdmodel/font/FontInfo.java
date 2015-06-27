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

begin_comment
comment|/**  * Information about a font on the system.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|FontInfo
block|{
comment|/**      * Returns the PostScript name of the font.      */
specifier|public
specifier|abstract
name|String
name|getPostScriptName
parameter_list|()
function_decl|;
comment|/**      * Returns the font's format.      */
specifier|public
specifier|abstract
name|FontFormat
name|getFormat
parameter_list|()
function_decl|;
comment|/**      * Returns the CIDSystemInfo associated with the font, if any.      */
specifier|public
specifier|abstract
name|PDCIDSystemInfo
name|getCIDSystemInfo
parameter_list|()
function_decl|;
comment|/**      * Returns a new FontBox font instance for the font. Implementors of this method must not      * cache the return value of this method unless doing so via the current {@link FontCache}.      */
specifier|public
specifier|abstract
name|FontBoxFont
name|getFont
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getPostScriptName
argument_list|()
operator|+
literal|" ("
operator|+
name|getFormat
argument_list|()
operator|+
literal|")"
return|;
block|}
block|}
end_class

end_unit

