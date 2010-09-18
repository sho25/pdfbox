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
name|cmap
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
name|FileInputStream
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
name|PushbackInputStream
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|util
operator|.
name|ResourceLoader
import|;
end_import

begin_comment
comment|/**  * This will parser a CMap stream.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.9 $  */
end_comment

begin_class
specifier|public
class|class
name|CMapParser
block|{
specifier|private
specifier|static
specifier|final
name|String
name|BEGIN_CODESPACE_RANGE
init|=
literal|"begincodespacerange"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BEGIN_BASE_FONT_CHAR
init|=
literal|"beginbfchar"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BEGIN_BASE_FONT_RANGE
init|=
literal|"beginbfrange"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BEGIN_CID_CHAR
init|=
literal|"begincidchar"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BEGIN_CID_RANGE
init|=
literal|"begincidrange"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USECMAP
init|=
literal|"usecmap"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WMODE
init|=
literal|"WMode"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CMAP_NAME
init|=
literal|"CMapName"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CMAP_VERSION
init|=
literal|"CMapVersion"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CMAP_TYPE
init|=
literal|"CMapType"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REGISTRY
init|=
literal|"Registry"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ORDERING
init|=
literal|"Ordering"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SUPPLEMENT
init|=
literal|"Supplement"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MARK_END_OF_DICTIONARY
init|=
literal|">>"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MARK_END_OF_ARRAY
init|=
literal|"]"
decl_stmt|;
specifier|private
name|byte
index|[]
name|tokenParserByteBuffer
init|=
operator|new
name|byte
index|[
literal|512
index|]
decl_stmt|;
comment|/**      * Creates a new instance of CMapParser.      */
specifier|public
name|CMapParser
parameter_list|()
block|{     }
comment|/**      * Parse a CMAP file on the file system.      *       * @param file The file to parse.      *       * @return A parsed CMAP file.      *       * @throws IOException If there is an issue while parsing the CMAP.      */
specifier|public
name|CMap
name|parse
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|rootDir
init|=
name|file
operator|.
name|getParent
argument_list|()
operator|+
name|File
operator|.
name|separator
decl_stmt|;
name|FileInputStream
name|input
init|=
literal|null
decl_stmt|;
try|try
block|{
name|input
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|parse
argument_list|(
name|rootDir
argument_list|,
name|input
argument_list|)
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|input
operator|!=
literal|null
condition|)
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will parse the stream and create a cmap object.      *      * @param resourceRoot The root path to the cmap file.  This will be used      *                     to find referenced cmap files.  It can be null.      * @param input The CMAP stream to parse.      *       * @return The parsed stream as a java object.      *      * @throws IOException If there is an error parsing the stream.      */
specifier|public
name|CMap
name|parse
parameter_list|(
name|String
name|resourceRoot
parameter_list|,
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|PushbackInputStream
name|cmapStream
init|=
operator|new
name|PushbackInputStream
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|CMap
name|result
init|=
operator|new
name|CMap
argument_list|()
decl_stmt|;
name|Object
name|previousToken
init|=
literal|null
decl_stmt|;
name|Object
name|token
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|token
operator|=
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|token
operator|instanceof
name|Operator
condition|)
block|{
name|Operator
name|op
init|=
operator|(
name|Operator
operator|)
name|token
decl_stmt|;
if|if
condition|(
name|op
operator|.
name|op
operator|.
name|equals
argument_list|(
name|USECMAP
argument_list|)
condition|)
block|{
name|LiteralName
name|useCmapName
init|=
operator|(
name|LiteralName
operator|)
name|previousToken
decl_stmt|;
name|InputStream
name|useStream
init|=
name|ResourceLoader
operator|.
name|loadResource
argument_list|(
name|resourceRoot
operator|+
name|useCmapName
operator|.
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|useStream
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Could not find referenced cmap stream "
operator|+
name|useCmapName
operator|.
name|name
argument_list|)
throw|;
block|}
name|CMap
name|useCMap
init|=
name|parse
argument_list|(
name|resourceRoot
argument_list|,
name|useStream
argument_list|)
decl_stmt|;
name|result
operator|.
name|useCmap
argument_list|(
name|useCMap
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|op
operator|.
name|op
operator|.
name|equals
argument_list|(
name|BEGIN_CODESPACE_RANGE
argument_list|)
condition|)
block|{
name|Number
name|cosCount
init|=
operator|(
name|Number
operator|)
name|previousToken
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|cosCount
operator|.
name|intValue
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|byte
index|[]
name|startRange
init|=
operator|(
name|byte
index|[]
operator|)
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|byte
index|[]
name|endRange
init|=
operator|(
name|byte
index|[]
operator|)
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|CodespaceRange
name|range
init|=
operator|new
name|CodespaceRange
argument_list|()
decl_stmt|;
name|range
operator|.
name|setStart
argument_list|(
name|startRange
argument_list|)
expr_stmt|;
name|range
operator|.
name|setEnd
argument_list|(
name|endRange
argument_list|)
expr_stmt|;
name|result
operator|.
name|addCodespaceRange
argument_list|(
name|range
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|op
operator|.
name|op
operator|.
name|equals
argument_list|(
name|BEGIN_BASE_FONT_CHAR
argument_list|)
condition|)
block|{
name|Number
name|cosCount
init|=
operator|(
name|Number
operator|)
name|previousToken
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|cosCount
operator|.
name|intValue
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|byte
index|[]
name|inputCode
init|=
operator|(
name|byte
index|[]
operator|)
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|Object
name|nextToken
init|=
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|nextToken
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|(
name|byte
index|[]
operator|)
name|nextToken
decl_stmt|;
name|String
name|value
init|=
name|createStringFromBytes
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|result
operator|.
name|addMapping
argument_list|(
name|inputCode
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nextToken
operator|instanceof
name|LiteralName
condition|)
block|{
name|result
operator|.
name|addMapping
argument_list|(
name|inputCode
argument_list|,
operator|(
operator|(
name|LiteralName
operator|)
name|nextToken
operator|)
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error parsing CMap beginbfchar, expected{COSString "
operator|+
literal|"or COSName} and not "
operator|+
name|nextToken
argument_list|)
throw|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|op
operator|.
name|op
operator|.
name|equals
argument_list|(
name|BEGIN_BASE_FONT_RANGE
argument_list|)
condition|)
block|{
name|Number
name|cosCount
init|=
operator|(
name|Number
operator|)
name|previousToken
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|cosCount
operator|.
name|intValue
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|byte
index|[]
name|startCode
init|=
operator|(
name|byte
index|[]
operator|)
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|byte
index|[]
name|endCode
init|=
operator|(
name|byte
index|[]
operator|)
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|Object
name|nextToken
init|=
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|array
init|=
literal|null
decl_stmt|;
name|byte
index|[]
name|tokenBytes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|nextToken
operator|instanceof
name|List
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|array
operator|=
operator|(
name|List
argument_list|<
name|byte
index|[]
argument_list|>
operator|)
name|nextToken
expr_stmt|;
name|tokenBytes
operator|=
name|array
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tokenBytes
operator|=
operator|(
name|byte
index|[]
operator|)
name|nextToken
expr_stmt|;
block|}
name|String
name|value
init|=
literal|null
decl_stmt|;
name|int
name|arrayIndex
init|=
literal|0
decl_stmt|;
name|boolean
name|done
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
if|if
condition|(
name|compare
argument_list|(
name|startCode
argument_list|,
name|endCode
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
block|}
name|value
operator|=
name|createStringFromBytes
argument_list|(
name|tokenBytes
argument_list|)
expr_stmt|;
name|result
operator|.
name|addMapping
argument_list|(
name|startCode
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|increment
argument_list|(
name|startCode
argument_list|)
expr_stmt|;
if|if
condition|(
name|array
operator|==
literal|null
condition|)
block|{
name|increment
argument_list|(
name|tokenBytes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|arrayIndex
operator|++
expr_stmt|;
if|if
condition|(
name|arrayIndex
operator|<
name|array
operator|.
name|size
argument_list|()
condition|)
block|{
name|tokenBytes
operator|=
operator|(
name|byte
index|[]
operator|)
name|array
operator|.
name|get
argument_list|(
name|arrayIndex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|op
operator|.
name|op
operator|.
name|equals
argument_list|(
name|BEGIN_CID_CHAR
argument_list|)
condition|)
block|{
name|Number
name|cosCount
init|=
operator|(
name|Number
operator|)
name|previousToken
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|cosCount
operator|.
name|intValue
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|byte
index|[]
name|inputCode
init|=
operator|(
name|byte
index|[]
operator|)
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|int
name|mappedCode
init|=
operator|(
name|Integer
operator|)
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|String
name|mappedStr
init|=
name|createStringFromBytes
argument_list|(
name|inputCode
argument_list|)
decl_stmt|;
name|result
operator|.
name|addCIDMapping
argument_list|(
name|mappedCode
argument_list|,
name|mappedStr
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|op
operator|.
name|op
operator|.
name|equals
argument_list|(
name|BEGIN_CID_RANGE
argument_list|)
condition|)
block|{
name|int
name|numberOfLines
init|=
operator|(
name|Integer
operator|)
name|previousToken
decl_stmt|;
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<
name|numberOfLines
condition|;
name|n
operator|++
control|)
block|{
name|byte
index|[]
name|startCode
init|=
operator|(
name|byte
index|[]
operator|)
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|int
name|start
init|=
name|createIntFromBytes
argument_list|(
name|startCode
argument_list|)
decl_stmt|;
name|byte
index|[]
name|endCode
init|=
operator|(
name|byte
index|[]
operator|)
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|int
name|end
init|=
name|createIntFromBytes
argument_list|(
name|endCode
argument_list|)
decl_stmt|;
name|int
name|mappedCode
init|=
operator|(
name|Integer
operator|)
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
name|int
name|endOfMappings
init|=
name|mappedCode
operator|+
name|end
operator|-
name|start
decl_stmt|;
while|while
condition|(
name|mappedCode
operator|<=
name|endOfMappings
condition|)
block|{
name|String
name|mappedStr
init|=
name|createStringFromBytes
argument_list|(
name|startCode
argument_list|)
decl_stmt|;
name|result
operator|.
name|addCIDMapping
argument_list|(
name|mappedCode
operator|++
argument_list|,
name|mappedStr
argument_list|)
expr_stmt|;
name|increment
argument_list|(
name|startCode
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|token
operator|instanceof
name|LiteralName
condition|)
block|{
name|LiteralName
name|literal
init|=
operator|(
name|LiteralName
operator|)
name|token
decl_stmt|;
if|if
condition|(
name|WMODE
operator|.
name|equals
argument_list|(
name|literal
operator|.
name|name
argument_list|)
condition|)
block|{
name|Object
name|next
init|=
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|Integer
condition|)
block|{
name|result
operator|.
name|setWMode
argument_list|(
operator|(
name|Integer
operator|)
name|next
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|CMAP_NAME
operator|.
name|equals
argument_list|(
name|literal
operator|.
name|name
argument_list|)
condition|)
block|{
name|Object
name|next
init|=
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|LiteralName
condition|)
block|{
name|result
operator|.
name|setName
argument_list|(
operator|(
operator|(
name|LiteralName
operator|)
name|next
operator|)
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|CMAP_VERSION
operator|.
name|equals
argument_list|(
name|literal
operator|.
name|name
argument_list|)
condition|)
block|{
name|Object
name|next
init|=
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|String
condition|)
block|{
name|result
operator|.
name|setVersion
argument_list|(
operator|(
name|String
operator|)
name|next
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|CMAP_TYPE
operator|.
name|equals
argument_list|(
name|literal
operator|.
name|name
argument_list|)
condition|)
block|{
name|Object
name|next
init|=
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|Integer
condition|)
block|{
name|result
operator|.
name|setType
argument_list|(
operator|(
name|Integer
operator|)
name|next
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|REGISTRY
operator|.
name|equals
argument_list|(
name|literal
operator|.
name|name
argument_list|)
condition|)
block|{
name|Object
name|next
init|=
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|String
condition|)
block|{
name|result
operator|.
name|setRegistry
argument_list|(
operator|(
name|String
operator|)
name|next
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|ORDERING
operator|.
name|equals
argument_list|(
name|literal
operator|.
name|name
argument_list|)
condition|)
block|{
name|Object
name|next
init|=
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|String
condition|)
block|{
name|result
operator|.
name|setOrdering
argument_list|(
operator|(
name|String
operator|)
name|next
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|SUPPLEMENT
operator|.
name|equals
argument_list|(
name|literal
operator|.
name|name
argument_list|)
condition|)
block|{
name|Object
name|next
init|=
name|parseNextToken
argument_list|(
name|cmapStream
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|Integer
condition|)
block|{
name|result
operator|.
name|setSupplement
argument_list|(
operator|(
name|Integer
operator|)
name|next
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|previousToken
operator|=
name|token
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
name|Object
name|parseNextToken
parameter_list|(
name|PushbackInputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|retval
init|=
literal|null
decl_stmt|;
name|int
name|nextByte
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
comment|//skip whitespace
while|while
condition|(
name|nextByte
operator|==
literal|0x09
operator|||
name|nextByte
operator|==
literal|0x20
operator|||
name|nextByte
operator|==
literal|0x0D
operator|||
name|nextByte
operator|==
literal|0x0A
condition|)
block|{
name|nextByte
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
switch|switch
condition|(
name|nextByte
condition|)
block|{
case|case
literal|'%'
case|:
block|{
comment|//header operations, for now return the entire line
comment|//may need to smarter in the future
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|nextByte
argument_list|)
expr_stmt|;
name|readUntilEndOfLine
argument_list|(
name|is
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|retval
operator|=
name|buffer
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
literal|'('
case|:
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|stringByte
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
while|while
condition|(
name|stringByte
operator|!=
operator|-
literal|1
operator|&&
name|stringByte
operator|!=
literal|')'
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|stringByte
argument_list|)
expr_stmt|;
name|stringByte
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|retval
operator|=
name|buffer
operator|.
name|toString
argument_list|()
expr_stmt|;
break|break;
block|}
case|case
literal|'>'
case|:
block|{
name|int
name|secondCloseBrace
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|secondCloseBrace
operator|==
literal|'>'
condition|)
block|{
name|retval
operator|=
name|MARK_END_OF_DICTIONARY
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: expected the end of a dictionary."
argument_list|)
throw|;
block|}
break|break;
block|}
case|case
literal|']'
case|:
block|{
name|retval
operator|=
name|MARK_END_OF_ARRAY
expr_stmt|;
break|break;
block|}
case|case
literal|'['
case|:
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Object
name|nextToken
init|=
name|parseNextToken
argument_list|(
name|is
argument_list|)
decl_stmt|;
while|while
condition|(
name|nextToken
operator|!=
literal|null
operator|&&
name|nextToken
operator|!=
name|MARK_END_OF_ARRAY
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|nextToken
argument_list|)
expr_stmt|;
name|nextToken
operator|=
name|parseNextToken
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
name|list
expr_stmt|;
break|break;
block|}
case|case
literal|'<'
case|:
block|{
name|int
name|theNextByte
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|theNextByte
operator|==
literal|'<'
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|//we are reading a dictionary
name|Object
name|key
init|=
name|parseNextToken
argument_list|(
name|is
argument_list|)
decl_stmt|;
while|while
condition|(
name|key
operator|instanceof
name|LiteralName
operator|&&
name|key
operator|!=
name|MARK_END_OF_DICTIONARY
condition|)
block|{
name|Object
name|value
init|=
name|parseNextToken
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
operator|(
operator|(
name|LiteralName
operator|)
name|key
operator|)
operator|.
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|key
operator|=
name|parseNextToken
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
name|result
expr_stmt|;
block|}
else|else
block|{
comment|//won't read more than 512 bytes
name|int
name|multiplyer
init|=
literal|16
decl_stmt|;
name|int
name|bufferIndex
init|=
operator|-
literal|1
decl_stmt|;
while|while
condition|(
name|theNextByte
operator|!=
operator|-
literal|1
operator|&&
name|theNextByte
operator|!=
literal|'>'
condition|)
block|{
name|int
name|intValue
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|theNextByte
operator|>=
literal|'0'
operator|&&
name|theNextByte
operator|<=
literal|'9'
condition|)
block|{
name|intValue
operator|=
name|theNextByte
operator|-
literal|'0'
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|theNextByte
operator|>=
literal|'A'
operator|&&
name|theNextByte
operator|<=
literal|'F'
condition|)
block|{
name|intValue
operator|=
literal|10
operator|+
name|theNextByte
operator|-
literal|'A'
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|theNextByte
operator|>=
literal|'a'
operator|&&
name|theNextByte
operator|<=
literal|'f'
condition|)
block|{
name|intValue
operator|=
literal|10
operator|+
name|theNextByte
operator|-
literal|'a'
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|theNextByte
operator|==
literal|0x20
condition|)
block|{
comment|// skipping whitespaces
name|theNextByte
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
continue|continue;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: expected hex character and not "
operator|+
operator|(
name|char
operator|)
name|theNextByte
operator|+
literal|":"
operator|+
name|theNextByte
argument_list|)
throw|;
block|}
name|intValue
operator|*=
name|multiplyer
expr_stmt|;
if|if
condition|(
name|multiplyer
operator|==
literal|16
condition|)
block|{
name|bufferIndex
operator|++
expr_stmt|;
name|tokenParserByteBuffer
index|[
name|bufferIndex
index|]
operator|=
literal|0
expr_stmt|;
name|multiplyer
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
name|multiplyer
operator|=
literal|16
expr_stmt|;
block|}
name|tokenParserByteBuffer
index|[
name|bufferIndex
index|]
operator|+=
name|intValue
expr_stmt|;
name|theNextByte
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|byte
index|[]
name|finalResult
init|=
operator|new
name|byte
index|[
name|bufferIndex
operator|+
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|tokenParserByteBuffer
argument_list|,
literal|0
argument_list|,
name|finalResult
argument_list|,
literal|0
argument_list|,
name|bufferIndex
operator|+
literal|1
argument_list|)
expr_stmt|;
name|retval
operator|=
name|finalResult
expr_stmt|;
block|}
break|break;
block|}
case|case
literal|'/'
case|:
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|int
name|stringByte
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|isWhitespaceOrEOF
argument_list|(
name|stringByte
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|stringByte
argument_list|)
expr_stmt|;
name|stringByte
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|LiteralName
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
case|case
operator|-
literal|1
case|:
block|{
comment|//EOF return null;
break|break;
block|}
case|case
literal|'0'
case|:
case|case
literal|'1'
case|:
case|case
literal|'2'
case|:
case|case
literal|'3'
case|:
case|case
literal|'4'
case|:
case|case
literal|'5'
case|:
case|case
literal|'6'
case|:
case|case
literal|'7'
case|:
case|case
literal|'8'
case|:
case|case
literal|'9'
case|:
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|nextByte
argument_list|)
expr_stmt|;
name|nextByte
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|isWhitespaceOrEOF
argument_list|(
name|nextByte
argument_list|)
operator|&&
operator|(
name|Character
operator|.
name|isDigit
argument_list|(
operator|(
name|char
operator|)
name|nextByte
argument_list|)
operator|||
name|nextByte
operator|==
literal|'.'
operator|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|nextByte
argument_list|)
expr_stmt|;
name|nextByte
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|is
operator|.
name|unread
argument_list|(
name|nextByte
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|retval
operator|=
operator|new
name|Double
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|Integer
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
default|default:
block|{
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|nextByte
argument_list|)
expr_stmt|;
name|nextByte
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|isWhitespaceOrEOF
argument_list|(
name|nextByte
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|nextByte
argument_list|)
expr_stmt|;
name|nextByte
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|Operator
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
return|return
name|retval
return|;
block|}
specifier|private
name|void
name|readUntilEndOfLine
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|StringBuffer
name|buf
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|nextByte
init|=
name|is
operator|.
name|read
argument_list|()
decl_stmt|;
while|while
condition|(
name|nextByte
operator|!=
operator|-
literal|1
operator|&&
name|nextByte
operator|!=
literal|0x0D
operator|&&
name|nextByte
operator|!=
literal|0x0A
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|nextByte
argument_list|)
expr_stmt|;
name|nextByte
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isWhitespaceOrEOF
parameter_list|(
name|int
name|aByte
parameter_list|)
block|{
return|return
name|aByte
operator|==
operator|-
literal|1
operator|||
name|aByte
operator|==
literal|0x20
operator|||
name|aByte
operator|==
literal|0x0D
operator|||
name|aByte
operator|==
literal|0x0A
return|;
block|}
specifier|private
name|void
name|increment
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|increment
argument_list|(
name|data
argument_list|,
name|data
operator|.
name|length
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|increment
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|int
name|position
parameter_list|)
block|{
if|if
condition|(
name|position
operator|>
literal|0
operator|&&
operator|(
name|data
index|[
name|position
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
operator|==
literal|255
condition|)
block|{
name|data
index|[
name|position
index|]
operator|=
literal|0
expr_stmt|;
name|increment
argument_list|(
name|data
argument_list|,
name|position
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|data
index|[
name|position
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|data
index|[
name|position
index|]
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|int
name|createIntFromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|int
name|intValue
init|=
operator|(
name|bytes
index|[
literal|0
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
decl_stmt|;
if|if
condition|(
name|bytes
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|intValue
operator|<<=
literal|8
expr_stmt|;
name|intValue
operator|+=
operator|(
name|bytes
index|[
literal|1
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
expr_stmt|;
block|}
return|return
name|intValue
return|;
block|}
specifier|private
name|String
name|createStringFromBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bytes
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|retval
operator|=
operator|new
name|String
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
literal|"UTF-16BE"
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
specifier|private
name|int
name|compare
parameter_list|(
name|byte
index|[]
name|first
parameter_list|,
name|byte
index|[]
name|second
parameter_list|)
block|{
name|int
name|retval
init|=
literal|1
decl_stmt|;
name|int
name|firstLength
init|=
name|first
operator|.
name|length
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
name|firstLength
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|first
index|[
name|i
index|]
operator|==
name|second
index|[
name|i
index|]
condition|)
block|{
continue|continue;
block|}
elseif|else
if|if
condition|(
operator|(
operator|(
name|first
index|[
name|i
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
operator|)
operator|<
operator|(
operator|(
name|second
index|[
name|i
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
operator|)
condition|)
block|{
name|retval
operator|=
operator|-
literal|1
expr_stmt|;
break|break;
block|}
else|else
block|{
name|retval
operator|=
literal|1
expr_stmt|;
break|break;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Internal class.      */
specifier|private
class|class
name|LiteralName
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|LiteralName
parameter_list|(
name|String
name|theName
parameter_list|)
block|{
name|name
operator|=
name|theName
expr_stmt|;
block|}
block|}
comment|/**      * Internal class.      */
specifier|private
class|class
name|Operator
block|{
specifier|private
name|String
name|op
decl_stmt|;
specifier|private
name|Operator
parameter_list|(
name|String
name|theOp
parameter_list|)
block|{
name|op
operator|=
name|theOp
expr_stmt|;
block|}
block|}
comment|/**      * A simple class to test parsing of cmap files.      *       * @param args Some command line arguments.      *       * @throws Exception If there is an error parsing the file.      */
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
name|Exception
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
literal|"usage: java org.pdfbox.cmapparser.CMapParser<CMAP File>"
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
name|CMapParser
name|parser
init|=
operator|new
name|CMapParser
argument_list|(  )
decl_stmt|;
name|File
name|cmapFile
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|CMap
name|result
init|=
name|parser
operator|.
name|parse
argument_list|(
name|cmapFile
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Result:"
operator|+
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

