begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*     Licensed to the Apache Software Foundation (ASF) under one or more    contributor license agreements.  See the NOTICE file distributed with    this work for additional information regarding copyright ownership.    The ASF licenses this file to You under the Apache License, Version 2.0    (the "License"); you may not use this file except in compliance with    the License.  You may obtain a copy of the License at         http://www.apache.org/licenses/LICENSE-2.0     Unless required by applicable law or agreed to in writing, software    distributed under the License is distributed on an "AS IS" BASIS,    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    See the License for the specific language governing permissions and    limitations under the License.   */
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

begin_comment
comment|/**  * This interface is implemented by several font specific classes which is called to get the  * general path of a single glyph of the represented font most likely to render it.  */
end_comment

begin_interface
interface|interface
name|Glyph2D
block|{
comment|/**      * Returns the path describing the glyph for the given character code.      *       * @param code the character code      *       * @return the GeneralPath for the given character code      */
name|GeneralPath
name|getPathForCharacterCode
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Remove all cached resources.      */
name|void
name|dispose
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

