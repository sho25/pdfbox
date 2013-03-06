begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
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
name|container
package|;
end_package

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
name|List
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
name|cff
operator|.
name|CFFFont
operator|.
name|Mapping
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
operator|.
name|GlyphException
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
name|preflight
operator|.
name|font
operator|.
name|util
operator|.
name|Type1
import|;
end_import

begin_class
specifier|public
class|class
name|Type1Container
extends|extends
name|FontContainer
block|{
comment|/**      * Represent the missingWidth value of the FontDescriptor dictionary. According to the PDF Reference, if this value      * is missing, the default one is 0.      */
specifier|private
name|float
name|defaultGlyphWidth
init|=
literal|0
decl_stmt|;
comment|/**      * true if information come from the FontFile1 Stream, false if they come from the FontFile3      */
specifier|protected
name|boolean
name|isFontFile1
init|=
literal|true
decl_stmt|;
specifier|protected
name|Type1
name|type1Font
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|CFFFont
argument_list|>
name|lCFonts
decl_stmt|;
specifier|public
name|Type1Container
parameter_list|(
name|PDFont
name|font
parameter_list|)
block|{
name|super
argument_list|(
name|font
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|float
name|getFontProgramWidth
parameter_list|(
name|int
name|cid
parameter_list|)
block|{
name|float
name|widthResult
init|=
operator|-
literal|1
decl_stmt|;
try|try
block|{
if|if
condition|(
name|isFontFile1
condition|)
block|{
if|if
condition|(
name|type1Font
operator|!=
literal|null
condition|)
block|{
name|widthResult
operator|=
name|this
operator|.
name|type1Font
operator|.
name|getWidthOfCID
argument_list|(
name|cid
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|/*                  * Retrieves the SID with the Character Name in the encoding map Need more PDF with a Type1C subfont to                  * valid this implementation                  */
name|String
name|name
init|=
name|this
operator|.
name|font
operator|.
name|getFontEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|cid
argument_list|)
decl_stmt|;
for|for
control|(
name|CFFFont
name|cff
range|:
name|lCFonts
control|)
block|{
name|int
name|SID
init|=
name|cff
operator|.
name|getEncoding
argument_list|()
operator|.
name|getSID
argument_list|(
name|cid
argument_list|)
decl_stmt|;
for|for
control|(
name|Mapping
name|m
range|:
name|cff
operator|.
name|getMappings
argument_list|()
control|)
block|{
if|if
condition|(
name|m
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|SID
operator|=
name|m
operator|.
name|getSID
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
name|widthResult
operator|=
name|cff
operator|.
name|getWidth
argument_list|(
name|SID
argument_list|)
expr_stmt|;
if|if
condition|(
name|widthResult
operator|!=
name|defaultGlyphWidth
condition|)
block|{
break|break;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|GlyphException
name|e
parameter_list|)
block|{
name|widthResult
operator|=
operator|-
literal|1
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|widthResult
operator|=
operator|-
literal|1
expr_stmt|;
comment|// TODO validation exception
block|}
return|return
name|widthResult
return|;
block|}
specifier|public
name|void
name|setType1Font
parameter_list|(
name|Type1
name|type1Font
parameter_list|)
block|{
name|this
operator|.
name|type1Font
operator|=
name|type1Font
expr_stmt|;
block|}
specifier|public
name|void
name|setFontFile1
parameter_list|(
name|boolean
name|isFontFile1
parameter_list|)
block|{
name|this
operator|.
name|isFontFile1
operator|=
name|isFontFile1
expr_stmt|;
block|}
specifier|public
name|void
name|setCFFFontObjects
parameter_list|(
name|List
argument_list|<
name|CFFFont
argument_list|>
name|lCFonts
parameter_list|)
block|{
name|this
operator|.
name|lCFonts
operator|=
name|lCFonts
expr_stmt|;
block|}
block|}
end_class

end_unit

