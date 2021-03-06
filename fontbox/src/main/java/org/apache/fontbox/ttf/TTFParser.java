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
comment|/**  * TrueType font file parser.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|TTFParser
block|{
specifier|private
name|boolean
name|isEmbedded
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|parseOnDemandOnly
init|=
literal|false
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|TTFParser
parameter_list|()
block|{
name|this
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *        * @param isEmbedded true if the font is embedded in PDF      */
specifier|public
name|TTFParser
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
comment|/**      *  Constructor.      *        * @param isEmbedded true if the font is embedded in PDF      * @param parseOnDemand true if the tables of the font should be parsed on demand      */
specifier|public
name|TTFParser
parameter_list|(
name|boolean
name|isEmbedded
parameter_list|,
name|boolean
name|parseOnDemand
parameter_list|)
block|{
name|this
operator|.
name|isEmbedded
operator|=
name|isEmbedded
expr_stmt|;
name|parseOnDemandOnly
operator|=
name|parseOnDemand
expr_stmt|;
block|}
comment|/**      * Parse a file and return a TrueType font.      *      * @param ttfFile The TrueType font filename.      * @return A TrueType font.      * @throws IOException If there is an error parsing the TrueType font.      */
specifier|public
name|TrueTypeFont
name|parse
parameter_list|(
name|String
name|ttfFile
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|parse
argument_list|(
operator|new
name|File
argument_list|(
name|ttfFile
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Parse a file and return a TrueType font.      *      * @param ttfFile The TrueType font file.      * @return A TrueType font.      * @throws IOException If there is an error parsing the TrueType font.      */
specifier|public
name|TrueTypeFont
name|parse
parameter_list|(
name|File
name|ttfFile
parameter_list|)
throws|throws
name|IOException
block|{
name|RAFDataStream
name|raf
init|=
operator|new
name|RAFDataStream
argument_list|(
name|ttfFile
argument_list|,
literal|"r"
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|parse
argument_list|(
name|raf
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// close only on error (file is still being accessed later)
name|raf
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
comment|/**      * Parse an input stream and return a TrueType font.      *      * @param inputStream The TTF data stream to parse from. It will be closed before returning.      * @return A TrueType font.      * @throws IOException If there is an error parsing the TrueType font.      */
specifier|public
name|TrueTypeFont
name|parse
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|parse
argument_list|(
operator|new
name|MemoryTTFDataStream
argument_list|(
name|inputStream
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Parse an input stream and return a TrueType font that is to be embedded.      *      * @param inputStream The TTF data stream to parse from. It will be closed before returning.      * @return A TrueType font.      * @throws IOException If there is an error parsing the TrueType font.      */
specifier|public
name|TrueTypeFont
name|parseEmbedded
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|isEmbedded
operator|=
literal|true
expr_stmt|;
return|return
name|parse
argument_list|(
operator|new
name|MemoryTTFDataStream
argument_list|(
name|inputStream
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Parse a file and get a true type font.      *      * @param raf The TTF file.      * @return A TrueType font.      * @throws IOException If there is an error parsing the TrueType font.      */
name|TrueTypeFont
name|parse
parameter_list|(
name|TTFDataStream
name|raf
parameter_list|)
throws|throws
name|IOException
block|{
name|TrueTypeFont
name|font
init|=
name|newFont
argument_list|(
name|raf
argument_list|)
decl_stmt|;
name|font
operator|.
name|setVersion
argument_list|(
name|raf
operator|.
name|read32Fixed
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|numberOfTables
init|=
name|raf
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|searchRange
init|=
name|raf
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|entrySelector
init|=
name|raf
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
name|int
name|rangeShift
init|=
name|raf
operator|.
name|readUnsignedShort
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfTables
condition|;
name|i
operator|++
control|)
block|{
name|TTFTable
name|table
init|=
name|readTableDirectory
argument_list|(
name|font
argument_list|,
name|raf
argument_list|)
decl_stmt|;
comment|// skip tables with zero length
if|if
condition|(
name|table
operator|!=
literal|null
condition|)
block|{
name|font
operator|.
name|addTable
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
comment|// parse tables if wanted
if|if
condition|(
operator|!
name|parseOnDemandOnly
condition|)
block|{
name|parseTables
argument_list|(
name|font
argument_list|)
expr_stmt|;
block|}
return|return
name|font
return|;
block|}
name|TrueTypeFont
name|newFont
parameter_list|(
name|TTFDataStream
name|raf
parameter_list|)
block|{
return|return
operator|new
name|TrueTypeFont
argument_list|(
name|raf
argument_list|)
return|;
block|}
comment|/**      * Parse all tables and check if all needed tables are present.      *      * @param font the TrueTypeFont instance holding the parsed data.      * @throws IOException If there is an error parsing the TrueType font.      */
specifier|private
name|void
name|parseTables
parameter_list|(
name|TrueTypeFont
name|font
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|TTFTable
name|table
range|:
name|font
operator|.
name|getTables
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|table
operator|.
name|getInitialized
argument_list|()
condition|)
block|{
name|font
operator|.
name|readTable
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|isPostScript
init|=
name|allowCFF
argument_list|()
operator|&&
name|font
operator|.
name|tables
operator|.
name|containsKey
argument_list|(
name|CFFTable
operator|.
name|TAG
argument_list|)
decl_stmt|;
name|HeaderTable
name|head
init|=
name|font
operator|.
name|getHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|head
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"head is mandatory"
argument_list|)
throw|;
block|}
name|HorizontalHeaderTable
name|hh
init|=
name|font
operator|.
name|getHorizontalHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|hh
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"hhead is mandatory"
argument_list|)
throw|;
block|}
name|MaximumProfileTable
name|maxp
init|=
name|font
operator|.
name|getMaximumProfile
argument_list|()
decl_stmt|;
if|if
condition|(
name|maxp
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"maxp is mandatory"
argument_list|)
throw|;
block|}
name|PostScriptTable
name|post
init|=
name|font
operator|.
name|getPostScript
argument_list|()
decl_stmt|;
if|if
condition|(
name|post
operator|==
literal|null
operator|&&
operator|!
name|isEmbedded
condition|)
block|{
comment|// in an embedded font this table is optional
throw|throw
operator|new
name|IOException
argument_list|(
literal|"post is mandatory"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|isPostScript
condition|)
block|{
name|IndexToLocationTable
name|loc
init|=
name|font
operator|.
name|getIndexToLocation
argument_list|()
decl_stmt|;
if|if
condition|(
name|loc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"loca is mandatory"
argument_list|)
throw|;
block|}
if|if
condition|(
name|font
operator|.
name|getGlyph
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"glyf is mandatory"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|font
operator|.
name|getNaming
argument_list|()
operator|==
literal|null
operator|&&
operator|!
name|isEmbedded
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"name is mandatory"
argument_list|)
throw|;
block|}
if|if
condition|(
name|font
operator|.
name|getHorizontalMetrics
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"hmtx is mandatory"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|isEmbedded
operator|&&
name|font
operator|.
name|getCmap
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"cmap is mandatory"
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|boolean
name|allowCFF
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|private
name|TTFTable
name|readTableDirectory
parameter_list|(
name|TrueTypeFont
name|font
parameter_list|,
name|TTFDataStream
name|raf
parameter_list|)
throws|throws
name|IOException
block|{
name|TTFTable
name|table
decl_stmt|;
name|String
name|tag
init|=
name|raf
operator|.
name|readString
argument_list|(
literal|4
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|tag
condition|)
block|{
case|case
name|CmapTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|CmapTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|GlyphTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|GlyphTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|HeaderTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|HeaderTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|HorizontalHeaderTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|HorizontalHeaderTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|HorizontalMetricsTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|HorizontalMetricsTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|IndexToLocationTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|IndexToLocationTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|MaximumProfileTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|MaximumProfileTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|NamingTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|NamingTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|OS2WindowsMetricsTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|OS2WindowsMetricsTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|PostScriptTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|PostScriptTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|DigitalSignatureTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|DigitalSignatureTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|KerningTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|KerningTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|VerticalHeaderTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|VerticalHeaderTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|VerticalMetricsTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|VerticalMetricsTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|VerticalOriginTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|VerticalOriginTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
case|case
name|GlyphSubstitutionTable
operator|.
name|TAG
case|:
name|table
operator|=
operator|new
name|GlyphSubstitutionTable
argument_list|(
name|font
argument_list|)
expr_stmt|;
break|break;
default|default:
name|table
operator|=
name|readTable
argument_list|(
name|font
argument_list|,
name|tag
argument_list|)
expr_stmt|;
break|break;
block|}
name|table
operator|.
name|setTag
argument_list|(
name|tag
argument_list|)
expr_stmt|;
name|table
operator|.
name|setCheckSum
argument_list|(
name|raf
operator|.
name|readUnsignedInt
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setOffset
argument_list|(
name|raf
operator|.
name|readUnsignedInt
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setLength
argument_list|(
name|raf
operator|.
name|readUnsignedInt
argument_list|()
argument_list|)
expr_stmt|;
comment|// skip tables with zero length (except glyf)
if|if
condition|(
name|table
operator|.
name|getLength
argument_list|()
operator|==
literal|0
operator|&&
operator|!
name|tag
operator|.
name|equals
argument_list|(
name|GlyphTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|table
return|;
block|}
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
comment|// unknown table type but read it anyway.
return|return
operator|new
name|TTFTable
argument_list|(
name|font
argument_list|)
return|;
block|}
block|}
end_class

end_unit

