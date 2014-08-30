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
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|type1
operator|.
name|Type1CharStringReader
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
comment|/**  * Represents a Type 2 CharString by converting it into an equivalent Type 1 CharString.  *   * @author Villu Ruusmann  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|Type2CharString
extends|extends
name|Type1CharString
block|{
specifier|private
name|int
name|defWidthX
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|nominalWidthX
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|pathCount
init|=
literal|0
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|type2sequence
decl_stmt|;
specifier|private
specifier|final
name|int
name|gid
decl_stmt|;
comment|/**      * Constructor.      * @param font Parent CFF font      * @param fontName font name      * @param glyphName glyph name (or CID as hex string)      * @param gid GID      * @param sequence Type 2 char string sequence      * @param defaultWidthX default width      * @param nomWidthX nominal width      */
specifier|public
name|Type2CharString
parameter_list|(
name|Type1CharStringReader
name|font
parameter_list|,
name|String
name|fontName
parameter_list|,
name|String
name|glyphName
parameter_list|,
name|int
name|gid
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|sequence
parameter_list|,
name|int
name|defaultWidthX
parameter_list|,
name|int
name|nomWidthX
parameter_list|)
block|{
name|super
argument_list|(
name|font
argument_list|,
name|fontName
argument_list|,
name|glyphName
argument_list|)
expr_stmt|;
name|this
operator|.
name|gid
operator|=
name|gid
expr_stmt|;
name|type2sequence
operator|=
name|sequence
expr_stmt|;
name|defWidthX
operator|=
name|defaultWidthX
expr_stmt|;
name|nominalWidthX
operator|=
name|nomWidthX
expr_stmt|;
name|convertType1ToType2
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
block|}
comment|/**      * Return the GID (glyph id) of this charstring.      */
specifier|public
name|int
name|getGID
parameter_list|()
block|{
return|return
name|gid
return|;
block|}
comment|/**      * Returns the advance width of this glyph.      */
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
name|int
name|width
init|=
name|super
operator|.
name|getWidth
argument_list|()
decl_stmt|;
if|if
condition|(
name|width
operator|==
literal|0
condition|)
block|{
return|return
name|defWidthX
return|;
block|}
else|else
block|{
return|return
name|width
return|;
block|}
block|}
comment|/**      * Returns the Type 2 charstring sequence.      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getType2Sequence
parameter_list|()
block|{
return|return
name|type2sequence
return|;
block|}
comment|/**      * Converts a sequence of Type 2 commands into a sequence of Type 1 commands.      * @param sequence the Type 2 char string sequence      */
specifier|private
name|void
name|convertType1ToType2
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|sequence
parameter_list|)
block|{
name|type1Sequence
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|pathCount
operator|=
literal|0
expr_stmt|;
name|CharStringHandler
name|handler
init|=
operator|new
name|CharStringHandler
argument_list|()
block|{
specifier|public
name|List
argument_list|<
name|Integer
argument_list|>
name|handleCommand
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|numbers
parameter_list|,
name|CharStringCommand
name|command
parameter_list|)
block|{
return|return
name|Type2CharString
operator|.
name|this
operator|.
name|handleCommand
argument_list|(
name|numbers
argument_list|,
name|command
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|handler
operator|.
name|handleSequence
argument_list|(
name|sequence
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
name|value
operator|=
block|{
literal|"unchecked"
block|}
argument_list|)
specifier|private
name|List
argument_list|<
name|Integer
argument_list|>
name|handleCommand
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|numbers
parameter_list|,
name|CharStringCommand
name|command
parameter_list|)
block|{
name|commandCount
operator|++
expr_stmt|;
name|String
name|name
init|=
name|CharStringCommand
operator|.
name|TYPE2_VOCABULARY
operator|.
name|get
argument_list|(
name|command
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"hstem"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|numbers
operator|=
name|clearStack
argument_list|(
name|numbers
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|%
literal|2
operator|!=
literal|0
argument_list|)
expr_stmt|;
name|expandStemHints
argument_list|(
name|numbers
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"vstem"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|numbers
operator|=
name|clearStack
argument_list|(
name|numbers
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|%
literal|2
operator|!=
literal|0
argument_list|)
expr_stmt|;
name|expandStemHints
argument_list|(
name|numbers
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"vmoveto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|numbers
operator|=
name|clearStack
argument_list|(
name|numbers
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|>
literal|1
argument_list|)
expr_stmt|;
name|markPath
argument_list|()
expr_stmt|;
name|addCommand
argument_list|(
name|numbers
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"rlineto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|addCommandList
argument_list|(
name|split
argument_list|(
name|numbers
argument_list|,
literal|2
argument_list|)
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"hlineto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|drawAlternatingLine
argument_list|(
name|numbers
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"vlineto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|drawAlternatingLine
argument_list|(
name|numbers
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"rrcurveto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|addCommandList
argument_list|(
name|split
argument_list|(
name|numbers
argument_list|,
literal|6
argument_list|)
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"endchar"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|numbers
operator|=
name|clearStack
argument_list|(
name|numbers
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|==
literal|5
operator|||
name|numbers
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|closePath
argument_list|()
expr_stmt|;
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|==
literal|4
condition|)
block|{
comment|// deprecated "seac" operator
name|numbers
operator|.
name|add
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|addCommand
argument_list|(
name|numbers
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|12
argument_list|,
literal|6
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addCommand
argument_list|(
name|numbers
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"rmoveto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|numbers
operator|=
name|clearStack
argument_list|(
name|numbers
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|>
literal|2
argument_list|)
expr_stmt|;
name|markPath
argument_list|()
expr_stmt|;
name|addCommand
argument_list|(
name|numbers
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"hmoveto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|numbers
operator|=
name|clearStack
argument_list|(
name|numbers
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|>
literal|1
argument_list|)
expr_stmt|;
name|markPath
argument_list|()
expr_stmt|;
name|addCommand
argument_list|(
name|numbers
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"vhcurveto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|drawAlternatingCurve
argument_list|(
name|numbers
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"hvcurveto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|drawAlternatingCurve
argument_list|(
name|numbers
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"hflex"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|first
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|second
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|,
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|,
operator|-
name|numbers
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|6
argument_list|)
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|addCommandList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|first
argument_list|,
name|second
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"flex"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|first
init|=
name|numbers
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|second
init|=
name|numbers
operator|.
name|subList
argument_list|(
literal|6
argument_list|,
literal|12
argument_list|)
decl_stmt|;
name|addCommandList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|first
argument_list|,
name|second
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"hflex1"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|first
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|second
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|,
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|6
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|7
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|8
argument_list|)
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|addCommandList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|first
argument_list|,
name|second
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"flex1"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|int
name|dx
init|=
literal|0
decl_stmt|;
name|int
name|dy
init|=
literal|0
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
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|dx
operator|+=
name|numbers
operator|.
name|get
argument_list|(
name|i
operator|*
literal|2
argument_list|)
expr_stmt|;
name|dy
operator|+=
name|numbers
operator|.
name|get
argument_list|(
name|i
operator|*
literal|2
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Integer
argument_list|>
name|first
init|=
name|numbers
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
literal|6
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|second
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|6
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|7
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|8
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|9
argument_list|)
argument_list|,
operator|(
name|Math
operator|.
name|abs
argument_list|(
name|dx
argument_list|)
operator|>
name|Math
operator|.
name|abs
argument_list|(
name|dy
argument_list|)
condition|?
name|numbers
operator|.
name|get
argument_list|(
literal|10
argument_list|)
else|:
operator|-
name|dx
operator|)
argument_list|,
operator|(
name|Math
operator|.
name|abs
argument_list|(
name|dx
argument_list|)
operator|>
name|Math
operator|.
name|abs
argument_list|(
name|dy
argument_list|)
condition|?
operator|-
name|dy
else|:
name|numbers
operator|.
name|get
argument_list|(
literal|10
argument_list|)
operator|)
argument_list|)
decl_stmt|;
name|addCommandList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|first
argument_list|,
name|second
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"hstemhm"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|numbers
operator|=
name|clearStack
argument_list|(
name|numbers
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|%
literal|2
operator|!=
literal|0
argument_list|)
expr_stmt|;
name|expandStemHints
argument_list|(
name|numbers
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"hintmask"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"cntrmask"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|numbers
operator|=
name|clearStack
argument_list|(
name|numbers
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|%
literal|2
operator|!=
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|expandStemHints
argument_list|(
name|numbers
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"vstemhm"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|numbers
operator|=
name|clearStack
argument_list|(
name|numbers
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|%
literal|2
operator|!=
literal|0
argument_list|)
expr_stmt|;
name|expandStemHints
argument_list|(
name|numbers
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"rcurveline"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|addCommandList
argument_list|(
name|split
argument_list|(
name|numbers
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|-
literal|2
argument_list|)
argument_list|,
literal|6
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
name|addCommand
argument_list|(
name|numbers
operator|.
name|subList
argument_list|(
name|numbers
operator|.
name|size
argument_list|()
operator|-
literal|2
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"rlinecurve"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|addCommandList
argument_list|(
name|split
argument_list|(
name|numbers
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
operator|-
literal|6
argument_list|)
argument_list|,
literal|2
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
name|addCommand
argument_list|(
name|numbers
operator|.
name|subList
argument_list|(
name|numbers
operator|.
name|size
argument_list|()
operator|-
literal|6
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"vvcurveto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|drawCurve
argument_list|(
name|numbers
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"hhcurveto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|drawCurve
argument_list|(
name|numbers
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addCommand
argument_list|(
name|numbers
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|List
argument_list|<
name|Integer
argument_list|>
name|clearStack
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|numbers
parameter_list|,
name|boolean
name|flag
parameter_list|)
block|{
if|if
condition|(
name|type1Sequence
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|flag
condition|)
block|{
name|addCommand
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|+
name|nominalWidthX
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|13
argument_list|)
argument_list|)
expr_stmt|;
name|numbers
operator|=
name|numbers
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addCommand
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|0
argument_list|,
name|defWidthX
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|13
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|numbers
return|;
block|}
specifier|private
name|void
name|expandStemHints
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|numbers
parameter_list|,
name|boolean
name|horizontal
parameter_list|)
block|{
comment|// TODO
block|}
specifier|private
name|void
name|markPath
parameter_list|()
block|{
if|if
condition|(
name|pathCount
operator|>
literal|0
condition|)
block|{
name|closePath
argument_list|()
expr_stmt|;
block|}
name|pathCount
operator|++
expr_stmt|;
block|}
specifier|private
name|void
name|closePath
parameter_list|()
block|{
name|CharStringCommand
name|command
init|=
name|pathCount
operator|>
literal|0
condition|?
operator|(
name|CharStringCommand
operator|)
name|type1Sequence
operator|.
name|get
argument_list|(
name|type1Sequence
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
else|:
literal|null
decl_stmt|;
name|CharStringCommand
name|closepathCommand
init|=
operator|new
name|CharStringCommand
argument_list|(
literal|9
argument_list|)
decl_stmt|;
if|if
condition|(
name|command
operator|!=
literal|null
operator|&&
operator|!
name|closepathCommand
operator|.
name|equals
argument_list|(
name|command
argument_list|)
condition|)
block|{
name|addCommand
argument_list|(
name|Collections
operator|.
expr|<
name|Integer
operator|>
name|emptyList
argument_list|()
argument_list|,
name|closepathCommand
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|drawAlternatingLine
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|numbers
parameter_list|,
name|boolean
name|horizontal
parameter_list|)
block|{
while|while
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|addCommand
argument_list|(
name|numbers
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
name|horizontal
condition|?
literal|6
else|:
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|numbers
operator|=
name|numbers
operator|.
name|subList
argument_list|(
literal|1
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|horizontal
operator|=
operator|!
name|horizontal
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|drawAlternatingCurve
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|numbers
parameter_list|,
name|boolean
name|horizontal
parameter_list|)
block|{
while|while
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|boolean
name|last
init|=
name|numbers
operator|.
name|size
argument_list|()
operator|==
literal|5
decl_stmt|;
if|if
condition|(
name|horizontal
condition|)
block|{
name|addCommand
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|last
condition|?
name|numbers
operator|.
name|get
argument_list|(
literal|4
argument_list|)
else|:
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addCommand
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|,
name|last
condition|?
name|numbers
operator|.
name|get
argument_list|(
literal|4
argument_list|)
else|:
literal|0
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|numbers
operator|=
name|numbers
operator|.
name|subList
argument_list|(
name|last
condition|?
literal|5
else|:
literal|4
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|horizontal
operator|=
operator|!
name|horizontal
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|drawCurve
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|numbers
parameter_list|,
name|boolean
name|horizontal
parameter_list|)
block|{
while|while
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|boolean
name|first
init|=
name|numbers
operator|.
name|size
argument_list|()
operator|%
literal|4
operator|==
literal|1
decl_stmt|;
if|if
condition|(
name|horizontal
condition|)
block|{
name|addCommand
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
name|first
condition|?
literal|1
else|:
literal|0
argument_list|)
argument_list|,
name|first
condition|?
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
name|first
condition|?
literal|2
else|:
literal|1
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
name|first
condition|?
literal|3
else|:
literal|2
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
name|first
condition|?
literal|4
else|:
literal|3
argument_list|)
argument_list|,
literal|0
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addCommand
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|first
condition|?
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
name|first
condition|?
literal|1
else|:
literal|0
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
name|first
condition|?
literal|2
else|:
literal|1
argument_list|)
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
name|first
condition|?
literal|3
else|:
literal|2
argument_list|)
argument_list|,
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
name|first
condition|?
literal|4
else|:
literal|3
argument_list|)
argument_list|)
argument_list|,
operator|new
name|CharStringCommand
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|numbers
operator|=
name|numbers
operator|.
name|subList
argument_list|(
name|first
condition|?
literal|5
else|:
literal|4
argument_list|,
name|numbers
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addCommandList
parameter_list|(
name|List
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|numbers
parameter_list|,
name|CharStringCommand
name|command
parameter_list|)
block|{
for|for
control|(
name|List
argument_list|<
name|Integer
argument_list|>
name|ns
range|:
name|numbers
control|)
block|{
name|addCommand
argument_list|(
name|ns
argument_list|,
name|command
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addCommand
parameter_list|(
name|List
argument_list|<
name|Integer
argument_list|>
name|numbers
parameter_list|,
name|CharStringCommand
name|command
parameter_list|)
block|{
name|type1Sequence
operator|.
name|addAll
argument_list|(
name|numbers
argument_list|)
expr_stmt|;
name|type1Sequence
operator|.
name|add
argument_list|(
name|command
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
parameter_list|<
name|E
parameter_list|>
name|List
argument_list|<
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
name|split
parameter_list|(
name|List
argument_list|<
name|E
argument_list|>
name|list
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|List
argument_list|<
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|List
argument_list|<
name|E
argument_list|>
argument_list|>
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
name|list
operator|.
name|size
argument_list|()
operator|/
name|size
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|list
operator|.
name|subList
argument_list|(
name|i
operator|*
name|size
argument_list|,
operator|(
name|i
operator|+
literal|1
operator|)
operator|*
name|size
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

