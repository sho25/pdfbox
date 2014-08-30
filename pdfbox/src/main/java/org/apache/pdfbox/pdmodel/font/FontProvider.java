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
name|cff
operator|.
name|CFFFont
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|type1
operator|.
name|Type1Font
import|;
end_import

begin_comment
comment|/**  * External font service provider interface. Implementations are expected to be thread safe.  *  * @author John Hewson  */
end_comment

begin_interface
specifier|public
interface|interface
name|FontProvider
block|{
comment|/**      * Returns a TrueType which corresponds to the given PostScript name. If there is no      * suitable font, then this method will return null.      *      * @param postScriptName PostScript font name      */
specifier|public
name|TrueTypeFont
name|getTrueTypeFont
parameter_list|(
name|String
name|postScriptName
parameter_list|)
function_decl|;
comment|/**      * Returns a CFF font which corresponds to the given PostScript name. If there is no      * suitable font, then this method will return null.      *      * @param postScriptName PostScript font name      */
specifier|public
name|CFFFont
name|getCFFFont
parameter_list|(
name|String
name|postScriptName
parameter_list|)
function_decl|;
comment|/**      * Returns a Type 1 which corresponds to the given PostScript name. If there is no      * suitable font, then this method will return null.      *      * @param postScriptName PostScript font name      */
specifier|public
name|Type1Font
name|getType1Font
parameter_list|(
name|String
name|postScriptName
parameter_list|)
function_decl|;
comment|/**      * Returns a string containing debugging information. This will be written to the log if no      * suitable fonts are found and no fallback fonts are available. May be null.      */
specifier|public
name|String
name|toDebugString
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

