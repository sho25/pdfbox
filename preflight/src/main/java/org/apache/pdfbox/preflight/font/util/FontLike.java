begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *  ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|font
operator|.
name|util
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
name|PDCIDFont
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Wrapper over font-like objects.  */
end_comment

begin_class
specifier|public
class|class
name|FontLike
block|{
specifier|private
name|PDFont
name|font
decl_stmt|;
specifier|private
name|PDCIDFont
name|cidFont
decl_stmt|;
comment|/**      * Constructor.      *      * @param font A font.      */
specifier|public
name|FontLike
parameter_list|(
name|PDFont
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
comment|/**      * Constructor.      *      * @param cidFont A CIDFont, this is not actually a font.      */
specifier|public
name|FontLike
parameter_list|(
name|PDCIDFont
name|cidFont
parameter_list|)
block|{
name|this
operator|.
name|cidFont
operator|=
name|cidFont
expr_stmt|;
block|}
comment|/**      * The PostScript name of the font.      */
specifier|public
name|String
name|getBaseFont
parameter_list|()
block|{
if|if
condition|(
name|font
operator|!=
literal|null
condition|)
block|{
return|return
name|font
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|cidFont
operator|.
name|getBaseFont
argument_list|()
return|;
block|}
block|}
comment|/**      * Returns the width of the given character.      *      * @param code character code      */
specifier|public
name|float
name|getWidth
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|font
operator|!=
literal|null
condition|)
block|{
return|return
name|font
operator|.
name|getWidth
argument_list|(
name|code
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|cidFont
operator|.
name|getWidth
argument_list|(
name|code
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

