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
name|cff
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
name|ArrayList
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
name|Stack
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * This class represents a converter for a mapping into a Type 1 sequence.  *  * @see "Adobe Type 1 Font Format, Adobe Systems (1999)"  *  * @author Villu Ruusmann  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|Type1CharStringParser
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|Type1CharStringParser
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// 1-byte commands
specifier|static
specifier|final
name|int
name|RETURN
init|=
literal|11
decl_stmt|;
specifier|static
specifier|final
name|int
name|CALLSUBR
init|=
literal|10
decl_stmt|;
comment|// 2-byte commands
specifier|static
specifier|final
name|int
name|TWO_BYTE
init|=
literal|12
decl_stmt|;
specifier|static
specifier|final
name|int
name|CALLOTHERSUBR
init|=
literal|16
decl_stmt|;
specifier|static
specifier|final
name|int
name|POP
init|=
literal|17
decl_stmt|;
specifier|private
specifier|final
name|String
name|fontName
decl_stmt|,
name|glyphName
decl_stmt|;
comment|/**      * Constructs a new Type1CharStringParser object.      *      * @param fontName font name      * @param glyphName glyph name      */
specifier|public
name|Type1CharStringParser
parameter_list|(
name|String
name|fontName
parameter_list|,
name|String
name|glyphName
parameter_list|)
block|{
name|this
operator|.
name|fontName
operator|=
name|fontName
expr_stmt|;
name|this
operator|.
name|glyphName
operator|=
name|glyphName
expr_stmt|;
block|}
comment|/**      * The given byte array will be parsed and converted to a Type1 sequence.      *      * @param bytes the given mapping as byte array      * @param subrs list of local subroutines      * @return the Type1 sequence      * @throws IOException if an error occurs during reading      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|parse
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|subrs
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|parse
argument_list|(
name|bytes
argument_list|,
name|subrs
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|parse
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|,
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|subrs
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|sequence
parameter_list|)
throws|throws
name|IOException
block|{
name|DataInput
name|input
init|=
operator|new
name|DataInput
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
while|while
condition|(
name|input
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|int
name|b0
init|=
name|input
operator|.
name|readUnsignedByte
argument_list|()
decl_stmt|;
if|if
condition|(
name|b0
operator|==
name|CALLSUBR
condition|)
block|{
comment|// callsubr command
name|Integer
name|operand
init|=
operator|(
name|Integer
operator|)
name|sequence
operator|.
name|remove
argument_list|(
name|sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|operand
operator|<
name|subrs
operator|.
name|size
argument_list|()
condition|)
block|{
name|byte
index|[]
name|subrBytes
init|=
name|subrs
operator|.
name|get
argument_list|(
name|operand
argument_list|)
decl_stmt|;
name|parse
argument_list|(
name|subrBytes
argument_list|,
name|subrs
argument_list|,
name|sequence
argument_list|)
expr_stmt|;
name|Object
name|lastItem
init|=
name|sequence
operator|.
name|get
argument_list|(
name|sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastItem
operator|instanceof
name|CharStringCommand
operator|&&
operator|(
operator|(
name|CharStringCommand
operator|)
name|lastItem
operator|)
operator|.
name|getKey
argument_list|()
operator|.
name|getValue
argument_list|()
index|[
literal|0
index|]
operator|==
name|RETURN
condition|)
block|{
name|sequence
operator|.
name|remove
argument_list|(
name|sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// remove "return" command
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|b0
operator|==
name|TWO_BYTE
operator|&&
name|input
operator|.
name|peekUnsignedByte
argument_list|(
literal|0
argument_list|)
operator|==
name|CALLOTHERSUBR
condition|)
block|{
comment|// callothersubr command (needed in order to expand Subrs)
name|input
operator|.
name|readByte
argument_list|()
expr_stmt|;
name|Integer
name|othersubrNum
init|=
operator|(
name|Integer
operator|)
name|sequence
operator|.
name|remove
argument_list|(
name|sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|Integer
name|numArgs
init|=
operator|(
name|Integer
operator|)
name|sequence
operator|.
name|remove
argument_list|(
name|sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
comment|// othersubrs 0-3 have their own semantics
name|Stack
argument_list|<
name|Integer
argument_list|>
name|results
init|=
operator|new
name|Stack
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|othersubrNum
operator|==
literal|0
condition|)
block|{
name|results
operator|.
name|push
argument_list|(
name|removeInteger
argument_list|(
name|sequence
argument_list|)
argument_list|)
expr_stmt|;
name|results
operator|.
name|push
argument_list|(
name|removeInteger
argument_list|(
name|sequence
argument_list|)
argument_list|)
expr_stmt|;
name|sequence
operator|.
name|remove
argument_list|(
name|sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// end flex
name|sequence
operator|.
name|add
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|sequence
operator|.
name|add
argument_list|(
operator|new
name|CharStringCommand
argument_list|(
name|TWO_BYTE
argument_list|,
name|CALLOTHERSUBR
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|othersubrNum
operator|==
literal|1
condition|)
block|{
comment|// begin flex
name|sequence
operator|.
name|add
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sequence
operator|.
name|add
argument_list|(
operator|new
name|CharStringCommand
argument_list|(
name|TWO_BYTE
argument_list|,
name|CALLOTHERSUBR
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|othersubrNum
operator|==
literal|3
condition|)
block|{
comment|// allows hint replacement
name|results
operator|.
name|push
argument_list|(
name|removeInteger
argument_list|(
name|sequence
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// all remaining othersubrs use this fallback mechanism
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numArgs
condition|;
name|i
operator|++
control|)
block|{
name|Integer
name|arg
init|=
name|removeInteger
argument_list|(
name|sequence
argument_list|)
decl_stmt|;
name|results
operator|.
name|push
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
block|}
comment|// pop must follow immediately
while|while
condition|(
name|input
operator|.
name|peekUnsignedByte
argument_list|(
literal|0
argument_list|)
operator|==
name|TWO_BYTE
operator|&&
name|input
operator|.
name|peekUnsignedByte
argument_list|(
literal|1
argument_list|)
operator|==
name|POP
condition|)
block|{
name|input
operator|.
name|readByte
argument_list|()
expr_stmt|;
comment|// B0_POP
name|input
operator|.
name|readByte
argument_list|()
expr_stmt|;
comment|// B1_POP
name|Integer
name|val
init|=
name|results
operator|.
name|pop
argument_list|()
decl_stmt|;
name|sequence
operator|.
name|add
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|results
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Value left on the PostScript stack in glyph "
operator|+
name|glyphName
operator|+
literal|" of font "
operator|+
name|fontName
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|b0
operator|>=
literal|0
operator|&&
name|b0
operator|<=
literal|31
condition|)
block|{
name|sequence
operator|.
name|add
argument_list|(
name|readCommand
argument_list|(
name|input
argument_list|,
name|b0
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|b0
operator|>=
literal|32
operator|&&
name|b0
operator|<=
literal|255
condition|)
block|{
name|sequence
operator|.
name|add
argument_list|(
name|readNumber
argument_list|(
name|input
argument_list|,
name|b0
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
block|}
return|return
name|sequence
return|;
block|}
comment|// this method is a workaround for the fact that Type1CharStringParser assumes that subrs and
comment|// othersubrs can be unrolled without executing the 'div' operator, which isn't true
specifier|private
specifier|static
name|Integer
name|removeInteger
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|sequence
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|item
init|=
name|sequence
operator|.
name|remove
argument_list|(
name|sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|item
operator|instanceof
name|Integer
condition|)
block|{
return|return
operator|(
name|Integer
operator|)
name|item
return|;
block|}
name|CharStringCommand
name|command
init|=
operator|(
name|CharStringCommand
operator|)
name|item
decl_stmt|;
comment|// div
if|if
condition|(
name|command
operator|.
name|getKey
argument_list|()
operator|.
name|getValue
argument_list|()
index|[
literal|0
index|]
operator|==
literal|12
operator|&&
name|command
operator|.
name|getKey
argument_list|()
operator|.
name|getValue
argument_list|()
index|[
literal|0
index|]
operator|==
literal|12
condition|)
block|{
name|int
name|a
init|=
operator|(
name|Integer
operator|)
name|sequence
operator|.
name|remove
argument_list|(
name|sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|int
name|b
init|=
operator|(
name|Integer
operator|)
name|sequence
operator|.
name|remove
argument_list|(
name|sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|b
operator|/
name|a
return|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unexpected char string command: "
operator|+
name|command
operator|.
name|getKey
argument_list|()
argument_list|)
throw|;
block|}
specifier|private
specifier|static
name|CharStringCommand
name|readCommand
parameter_list|(
name|DataInput
name|input
parameter_list|,
name|int
name|b0
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|b0
operator|==
literal|12
condition|)
block|{
name|int
name|b1
init|=
name|input
operator|.
name|readUnsignedByte
argument_list|()
decl_stmt|;
return|return
operator|new
name|CharStringCommand
argument_list|(
name|b0
argument_list|,
name|b1
argument_list|)
return|;
block|}
return|return
operator|new
name|CharStringCommand
argument_list|(
name|b0
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Integer
name|readNumber
parameter_list|(
name|DataInput
name|input
parameter_list|,
name|int
name|b0
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|b0
operator|>=
literal|32
operator|&&
name|b0
operator|<=
literal|246
condition|)
block|{
return|return
name|b0
operator|-
literal|139
return|;
block|}
elseif|else
if|if
condition|(
name|b0
operator|>=
literal|247
operator|&&
name|b0
operator|<=
literal|250
condition|)
block|{
name|int
name|b1
init|=
name|input
operator|.
name|readUnsignedByte
argument_list|()
decl_stmt|;
return|return
operator|(
name|b0
operator|-
literal|247
operator|)
operator|*
literal|256
operator|+
name|b1
operator|+
literal|108
return|;
block|}
elseif|else
if|if
condition|(
name|b0
operator|>=
literal|251
operator|&&
name|b0
operator|<=
literal|254
condition|)
block|{
name|int
name|b1
init|=
name|input
operator|.
name|readUnsignedByte
argument_list|()
decl_stmt|;
return|return
operator|-
operator|(
name|b0
operator|-
literal|251
operator|)
operator|*
literal|256
operator|-
name|b1
operator|-
literal|108
return|;
block|}
elseif|else
if|if
condition|(
name|b0
operator|==
literal|255
condition|)
block|{
name|int
name|b1
init|=
name|input
operator|.
name|readUnsignedByte
argument_list|()
decl_stmt|;
name|int
name|b2
init|=
name|input
operator|.
name|readUnsignedByte
argument_list|()
decl_stmt|;
name|int
name|b3
init|=
name|input
operator|.
name|readUnsignedByte
argument_list|()
decl_stmt|;
name|int
name|b4
init|=
name|input
operator|.
name|readUnsignedByte
argument_list|()
decl_stmt|;
return|return
name|b1
operator|<<
literal|24
operator||
name|b2
operator|<<
literal|16
operator||
name|b3
operator|<<
literal|8
operator||
name|b4
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

