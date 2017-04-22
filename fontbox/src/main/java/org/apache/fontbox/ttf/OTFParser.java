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
name|java
operator|.
name|io
operator|.
name|File
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_comment
comment|/**  * OpenType font file parser.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|OTFParser
extends|extends
name|TTFParser
block|{
comment|/**      * Constructor.      */
specifier|public
name|OTFParser
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param isEmbedded true if the font is embedded in PDF      */
specifier|public
name|OTFParser
parameter_list|(
name|boolean
name|isEmbedded
parameter_list|)
block|{
name|this
argument_list|(
name|isEmbedded
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      *  Constructor.      *      * @param isEmbedded true if the font is embedded in PDF      * @param parseOnDemand true if the tables of the font should be parsed on demand      */
specifier|public
name|OTFParser
parameter_list|(
name|boolean
name|isEmbedded
parameter_list|,
name|boolean
name|parseOnDemand
parameter_list|)
block|{
name|super
argument_list|(
name|isEmbedded
argument_list|,
name|parseOnDemand
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|OpenTypeFont
name|parse
parameter_list|(
name|String
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|(
name|OpenTypeFont
operator|)
name|super
operator|.
name|parse
argument_list|(
name|file
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|OpenTypeFont
name|parse
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|(
name|OpenTypeFont
operator|)
name|super
operator|.
name|parse
argument_list|(
name|file
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|OpenTypeFont
name|parse
parameter_list|(
name|InputStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|(
name|OpenTypeFont
operator|)
name|super
operator|.
name|parse
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Override
name|OpenTypeFont
name|parse
parameter_list|(
name|TTFDataStream
name|raf
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|(
name|OpenTypeFont
operator|)
name|super
operator|.
name|parse
argument_list|(
name|raf
argument_list|)
return|;
block|}
annotation|@
name|Override
name|OpenTypeFont
name|newFont
parameter_list|(
name|TTFDataStream
name|raf
parameter_list|)
block|{
return|return
operator|new
name|OpenTypeFont
argument_list|(
name|raf
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|TTFTable
name|readTable
parameter_list|(
name|TrueTypeFont
name|font
parameter_list|,
name|String
name|tag
parameter_list|)
block|{
comment|// todo: this is a stub, a full implementation is needed
switch|switch
condition|(
name|tag
condition|)
block|{
case|case
literal|"BASE"
case|:
case|case
literal|"GDEF"
case|:
case|case
literal|"GPOS"
case|:
case|case
literal|"GSUB"
case|:
case|case
literal|"JSTF"
case|:
return|return
operator|new
name|OTLTable
argument_list|(
name|font
argument_list|)
return|;
case|case
literal|"CFF "
case|:
return|return
operator|new
name|CFFTable
argument_list|(
name|font
argument_list|)
return|;
default|default:
return|return
name|super
operator|.
name|readTable
argument_list|(
name|font
argument_list|,
name|tag
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|boolean
name|allowCFF
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

