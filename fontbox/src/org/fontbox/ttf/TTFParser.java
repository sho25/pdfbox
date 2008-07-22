begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2005, www.fontbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of fontbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.fontbox.org  *  */
end_comment

begin_package
package|package
name|org
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_comment
comment|/**  * A true type font file parser.  *   * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|TTFParser
block|{
comment|/**      * A simple command line program to test parsing of a TTF file.<br/>      * usage: java org.pdfbox.ttf.TTFParser&lt;ttf-file&gt;      *       * @param args The command line arguments.      *       * @throws IOException If there is an error while parsing the font file.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: java org.pdfbox.ttf.TTFParser<ttf-file>"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|TTFParser
name|parser
init|=
operator|new
name|TTFParser
argument_list|()
decl_stmt|;
name|TrueTypeFont
name|font
init|=
name|parser
operator|.
name|parseTTF
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Font:"
operator|+
name|font
argument_list|)
expr_stmt|;
block|}
comment|/**      * Parse a file and get a true type font.      * @param ttfFile The TTF file.      * @return A true type font.      * @throws IOException If there is an error parsing the true type font.      */
specifier|public
name|TrueTypeFont
name|parseTTF
parameter_list|(
name|String
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
return|return
name|parseTTF
argument_list|(
name|raf
argument_list|)
return|;
block|}
comment|/**      * Parse a file and get a true type font.      * @param ttfFile The TTF file.      * @return A true type font.      * @throws IOException If there is an error parsing the true type font.      */
specifier|public
name|TrueTypeFont
name|parseTTF
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
return|return
name|parseTTF
argument_list|(
name|raf
argument_list|)
return|;
block|}
comment|/**      * Parse a file and get a true type font.      * @param ttfData The TTF data to parse.      * @return A true type font.      * @throws IOException If there is an error parsing the true type font.      */
specifier|public
name|TrueTypeFont
name|parseTTF
parameter_list|(
name|InputStream
name|ttfData
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|parseTTF
argument_list|(
operator|new
name|MemoryTTFDataStream
argument_list|(
name|ttfData
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Parse a file and get a true type font.      * @param raf The TTF file.      * @return A true type font.      * @throws IOException If there is an error parsing the true type font.      */
specifier|public
name|TrueTypeFont
name|parseTTF
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
operator|new
name|TrueTypeFont
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
name|raf
argument_list|)
decl_stmt|;
name|font
operator|.
name|addTable
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
name|List
name|initialized
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
comment|//need to initialize a couple tables in a certain order
name|HeaderTable
name|head
init|=
name|font
operator|.
name|getHeader
argument_list|()
decl_stmt|;
name|raf
operator|.
name|seek
argument_list|(
name|head
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|head
operator|.
name|initData
argument_list|(
name|font
argument_list|,
name|raf
argument_list|)
expr_stmt|;
name|initialized
operator|.
name|add
argument_list|(
name|head
argument_list|)
expr_stmt|;
name|HorizontalHeaderTable
name|hh
init|=
name|font
operator|.
name|getHorizontalHeader
argument_list|()
decl_stmt|;
name|raf
operator|.
name|seek
argument_list|(
name|hh
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|hh
operator|.
name|initData
argument_list|(
name|font
argument_list|,
name|raf
argument_list|)
expr_stmt|;
name|initialized
operator|.
name|add
argument_list|(
name|hh
argument_list|)
expr_stmt|;
name|MaximumProfileTable
name|maxp
init|=
name|font
operator|.
name|getMaximumProfile
argument_list|()
decl_stmt|;
name|raf
operator|.
name|seek
argument_list|(
name|maxp
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|maxp
operator|.
name|initData
argument_list|(
name|font
argument_list|,
name|raf
argument_list|)
expr_stmt|;
name|initialized
operator|.
name|add
argument_list|(
name|maxp
argument_list|)
expr_stmt|;
name|PostScriptTable
name|post
init|=
name|font
operator|.
name|getPostScript
argument_list|()
decl_stmt|;
name|raf
operator|.
name|seek
argument_list|(
name|post
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|post
operator|.
name|initData
argument_list|(
name|font
argument_list|,
name|raf
argument_list|)
expr_stmt|;
name|initialized
operator|.
name|add
argument_list|(
name|post
argument_list|)
expr_stmt|;
name|IndexToLocationTable
name|loc
init|=
name|font
operator|.
name|getIndexToLocation
argument_list|()
decl_stmt|;
name|raf
operator|.
name|seek
argument_list|(
name|loc
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|loc
operator|.
name|initData
argument_list|(
name|font
argument_list|,
name|raf
argument_list|)
expr_stmt|;
name|initialized
operator|.
name|add
argument_list|(
name|loc
argument_list|)
expr_stmt|;
name|Iterator
name|iter
init|=
name|font
operator|.
name|getTables
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|TTFTable
name|table
init|=
operator|(
name|TTFTable
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|initialized
operator|.
name|contains
argument_list|(
name|table
argument_list|)
condition|)
block|{
name|raf
operator|.
name|seek
argument_list|(
name|table
operator|.
name|getOffset
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|initData
argument_list|(
name|font
argument_list|,
name|raf
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|font
return|;
block|}
specifier|private
name|TTFTable
name|readTableDirectory
parameter_list|(
name|TTFDataStream
name|raf
parameter_list|)
throws|throws
name|IOException
block|{
name|TTFTable
name|retval
init|=
literal|null
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
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|CMAPTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|CMAPTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
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
name|retval
operator|=
operator|new
name|GlyphTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|HeaderTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|HeaderTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|HorizontalHeaderTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|HorizontalHeaderTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|HorizontalMetricsTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|HorizontalMetricsTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|IndexToLocationTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|IndexToLocationTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|MaximumProfileTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|MaximumProfileTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|NamingTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|NamingTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|OS2WindowsMetricsTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|OS2WindowsMetricsTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|PostScriptTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PostScriptTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
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
name|retval
operator|=
operator|new
name|GlyphTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
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
name|retval
operator|=
operator|new
name|GlyphTable
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|tag
operator|.
name|equals
argument_list|(
name|DigitalSignatureTable
operator|.
name|TAG
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|DigitalSignatureTable
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|//unknown table type but read it anyway.
name|retval
operator|=
operator|new
name|TTFTable
argument_list|()
expr_stmt|;
block|}
name|retval
operator|.
name|setTag
argument_list|(
name|tag
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setCheckSum
argument_list|(
name|raf
operator|.
name|readUnsignedInt
argument_list|()
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setOffset
argument_list|(
name|raf
operator|.
name|readUnsignedInt
argument_list|()
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setLength
argument_list|(
name|raf
operator|.
name|readUnsignedInt
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
block|}
end_class

end_unit

