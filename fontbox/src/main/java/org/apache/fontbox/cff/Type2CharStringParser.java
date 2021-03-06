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
name|Locale
import|;
end_import

begin_comment
comment|/**  * This class represents a converter for a mapping into a Type2-sequence.  * @author Villu Ruusmann  */
end_comment

begin_class
specifier|public
class|class
name|Type2CharStringParser
block|{
specifier|private
name|int
name|hstemCount
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|vstemCount
init|=
literal|0
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|sequence
init|=
literal|null
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
specifier|final
name|String
name|fontName
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
specifier|final
name|String
name|glyphName
decl_stmt|;
comment|/**      * Constructs a new Type1CharStringParser object for a Type 1-equivalent font.      *      * @param fontName font name      * @param glyphName glyph name      */
specifier|public
name|Type2CharStringParser
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
comment|/**      * Constructs a new Type1CharStringParser object for a CID-Keyed font.      *      * @param fontName font name      * @param cid CID      */
specifier|public
name|Type2CharStringParser
parameter_list|(
name|String
name|fontName
parameter_list|,
name|int
name|cid
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
name|String
operator|.
name|format
argument_list|(
name|Locale
operator|.
name|US
argument_list|,
literal|"%04x"
argument_list|,
name|cid
argument_list|)
expr_stmt|;
comment|// for debugging only
block|}
comment|/**      * The given byte array will be parsed and converted to a Type2 sequence.      * @param bytes the given mapping as byte array      * @param globalSubrIndex array containing all global subroutines      * @param localSubrIndex array containing all local subroutines      *       * @return the Type2 sequence      * @throws IOException if an error occurs during reading      */
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
name|byte
index|[]
index|[]
name|globalSubrIndex
parameter_list|,
name|byte
index|[]
index|[]
name|localSubrIndex
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|parse
argument_list|(
name|bytes
argument_list|,
name|globalSubrIndex
argument_list|,
name|localSubrIndex
argument_list|,
literal|true
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
name|byte
index|[]
index|[]
name|globalSubrIndex
parameter_list|,
name|byte
index|[]
index|[]
name|localSubrIndex
parameter_list|,
name|boolean
name|init
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|init
condition|)
block|{
name|hstemCount
operator|=
literal|0
expr_stmt|;
name|vstemCount
operator|=
literal|0
expr_stmt|;
name|sequence
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|DataInput
name|input
init|=
operator|new
name|DataInput
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|boolean
name|localSubroutineIndexProvided
init|=
name|localSubrIndex
operator|!=
literal|null
operator|&&
name|localSubrIndex
operator|.
name|length
operator|>
literal|0
decl_stmt|;
name|boolean
name|globalSubroutineIndexProvided
init|=
name|globalSubrIndex
operator|!=
literal|null
operator|&&
name|globalSubrIndex
operator|.
name|length
operator|>
literal|0
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
literal|10
operator|&&
name|localSubroutineIndexProvided
condition|)
block|{
comment|// process subr command
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
comment|//get subrbias
name|int
name|bias
init|=
literal|0
decl_stmt|;
name|int
name|nSubrs
init|=
name|localSubrIndex
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|nSubrs
operator|<
literal|1240
condition|)
block|{
name|bias
operator|=
literal|107
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nSubrs
operator|<
literal|33900
condition|)
block|{
name|bias
operator|=
literal|1131
expr_stmt|;
block|}
else|else
block|{
name|bias
operator|=
literal|32768
expr_stmt|;
block|}
name|int
name|subrNumber
init|=
name|bias
operator|+
name|operand
decl_stmt|;
if|if
condition|(
name|subrNumber
operator|<
name|localSubrIndex
operator|.
name|length
condition|)
block|{
name|byte
index|[]
name|subrBytes
init|=
name|localSubrIndex
index|[
name|subrNumber
index|]
decl_stmt|;
name|parse
argument_list|(
name|subrBytes
argument_list|,
name|globalSubrIndex
argument_list|,
name|localSubrIndex
argument_list|,
literal|false
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
literal|11
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
literal|29
operator|&&
name|globalSubroutineIndexProvided
condition|)
block|{
comment|// process globalsubr command
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
comment|//get subrbias
name|int
name|bias
decl_stmt|;
name|int
name|nSubrs
init|=
name|globalSubrIndex
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|nSubrs
operator|<
literal|1240
condition|)
block|{
name|bias
operator|=
literal|107
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nSubrs
operator|<
literal|33900
condition|)
block|{
name|bias
operator|=
literal|1131
expr_stmt|;
block|}
else|else
block|{
name|bias
operator|=
literal|32768
expr_stmt|;
block|}
name|int
name|subrNumber
init|=
name|bias
operator|+
name|operand
decl_stmt|;
if|if
condition|(
name|subrNumber
operator|<
name|globalSubrIndex
operator|.
name|length
condition|)
block|{
name|byte
index|[]
name|subrBytes
init|=
name|globalSubrIndex
index|[
name|subrNumber
index|]
decl_stmt|;
name|parse
argument_list|(
name|subrBytes
argument_list|,
name|globalSubrIndex
argument_list|,
name|localSubrIndex
argument_list|,
literal|false
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
literal|11
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
operator|>=
literal|0
operator|&&
name|b0
operator|<=
literal|27
condition|)
block|{
name|sequence
operator|.
name|add
argument_list|(
name|readCommand
argument_list|(
name|b0
argument_list|,
name|input
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|b0
operator|==
literal|28
condition|)
block|{
name|sequence
operator|.
name|add
argument_list|(
name|readNumber
argument_list|(
name|b0
argument_list|,
name|input
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|b0
operator|>=
literal|29
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
name|b0
argument_list|,
name|input
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
name|b0
argument_list|,
name|input
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
specifier|private
name|CharStringCommand
name|readCommand
parameter_list|(
name|int
name|b0
parameter_list|,
name|DataInput
name|input
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|b0
operator|==
literal|1
operator|||
name|b0
operator|==
literal|18
condition|)
block|{
name|hstemCount
operator|+=
name|peekNumbers
argument_list|()
operator|.
name|size
argument_list|()
operator|/
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|b0
operator|==
literal|3
operator|||
name|b0
operator|==
literal|19
operator|||
name|b0
operator|==
literal|20
operator|||
name|b0
operator|==
literal|23
condition|)
block|{
name|vstemCount
operator|+=
name|peekNumbers
argument_list|()
operator|.
name|size
argument_list|()
operator|/
literal|2
expr_stmt|;
block|}
comment|// End if
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
elseif|else
if|if
condition|(
name|b0
operator|==
literal|19
operator|||
name|b0
operator|==
literal|20
condition|)
block|{
name|int
index|[]
name|value
init|=
operator|new
name|int
index|[
literal|1
operator|+
name|getMaskLength
argument_list|()
index|]
decl_stmt|;
name|value
index|[
literal|0
index|]
operator|=
name|b0
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
name|value
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|value
index|[
name|i
index|]
operator|=
name|input
operator|.
name|readUnsignedByte
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|CharStringCommand
argument_list|(
name|value
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
name|Number
name|readNumber
parameter_list|(
name|int
name|b0
parameter_list|,
name|DataInput
name|input
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|b0
operator|==
literal|28
condition|)
block|{
return|return
operator|(
name|int
operator|)
name|input
operator|.
name|readShort
argument_list|()
return|;
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
name|short
name|value
init|=
name|input
operator|.
name|readShort
argument_list|()
decl_stmt|;
comment|// The lower bytes are representing the digits after the decimal point
name|double
name|fraction
init|=
name|input
operator|.
name|readUnsignedShort
argument_list|()
operator|/
literal|65535d
decl_stmt|;
return|return
name|value
operator|+
name|fraction
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
specifier|private
name|int
name|getMaskLength
parameter_list|()
block|{
name|int
name|hintCount
init|=
name|hstemCount
operator|+
name|vstemCount
decl_stmt|;
name|int
name|length
init|=
name|hintCount
operator|/
literal|8
decl_stmt|;
if|if
condition|(
name|hintCount
operator|%
literal|8
operator|>
literal|0
condition|)
block|{
name|length
operator|++
expr_stmt|;
block|}
return|return
name|length
return|;
block|}
specifier|private
name|List
argument_list|<
name|Number
argument_list|>
name|peekNumbers
parameter_list|()
block|{
name|List
argument_list|<
name|Number
argument_list|>
name|numbers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>
operator|-
literal|1
condition|;
name|i
operator|--
control|)
block|{
name|Object
name|object
init|=
name|sequence
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|Number
operator|)
condition|)
block|{
return|return
name|numbers
return|;
block|}
name|numbers
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|(
name|Number
operator|)
name|object
argument_list|)
expr_stmt|;
block|}
return|return
name|numbers
return|;
block|}
block|}
end_class

end_unit

