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
name|awt
operator|.
name|Point
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
import|;
end_import

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
name|awt
operator|.
name|geom
operator|.
name|Point2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|Rectangle2D
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|encoding
operator|.
name|StandardEncoding
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
name|type1
operator|.
name|Type1CharStringReader
import|;
end_import

begin_comment
comment|/**  * This class represents and renders a Type 1 CharString.  *  * @author Villu Ruusmann  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|Type1CharString
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
name|Type1CharString
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Type1CharStringReader
name|font
decl_stmt|;
specifier|private
specifier|final
name|String
name|fontName
decl_stmt|,
name|glyphName
decl_stmt|;
specifier|private
name|GeneralPath
name|path
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|width
init|=
literal|0
decl_stmt|;
specifier|private
name|Point2D
operator|.
name|Float
name|leftSideBearing
init|=
literal|null
decl_stmt|;
specifier|private
name|Point2D
operator|.
name|Float
name|current
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|isFlex
init|=
literal|false
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Point
operator|.
name|Float
argument_list|>
name|flexPoints
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|type1Sequence
decl_stmt|;
specifier|protected
name|int
name|commandCount
decl_stmt|;
comment|/**      * Constructs a new Type1CharString object.      *      * @param font Parent Type 1 CharString font.      * @param fontName Name of the font.      * @param glyphName Name of the glyph.      * @param sequence Type 1 char string sequence      */
specifier|public
name|Type1CharString
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
name|List
argument_list|<
name|Object
argument_list|>
name|sequence
parameter_list|)
block|{
name|this
argument_list|(
name|font
argument_list|,
name|fontName
argument_list|,
name|glyphName
argument_list|)
expr_stmt|;
name|type1Sequence
operator|=
name|sequence
expr_stmt|;
block|}
comment|/**      * Constructor for use in subclasses.      *      * @param font Parent Type 1 CharString font.      * @param fontName Name of the font.      * @param glyphName Name of the glyph.      */
specifier|protected
name|Type1CharString
parameter_list|(
name|Type1CharStringReader
name|font
parameter_list|,
name|String
name|fontName
parameter_list|,
name|String
name|glyphName
parameter_list|)
block|{
name|this
operator|.
name|font
operator|=
name|font
expr_stmt|;
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
name|this
operator|.
name|current
operator|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// todo: NEW name (or CID as hex)
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|glyphName
return|;
block|}
comment|/**      * Returns the bounds of the renderer path.      * @return the bounds as Rectangle2D      */
specifier|public
name|Rectangle2D
name|getBounds
parameter_list|()
block|{
synchronized|synchronized
init|(
name|LOG
init|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|render
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|path
operator|.
name|getBounds2D
argument_list|()
return|;
block|}
comment|/**      * Returns the advance width of the glyph.      * @return the width      */
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
synchronized|synchronized
init|(
name|LOG
init|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|render
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|width
return|;
block|}
comment|/**      * Returns the path of the character.      * @return the path      */
specifier|public
name|GeneralPath
name|getPath
parameter_list|()
block|{
synchronized|synchronized
init|(
name|LOG
init|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|render
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|path
return|;
block|}
comment|/**      * Returns the Type 1 char string sequence.      * @return the Type 1 sequence      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getType1Sequence
parameter_list|()
block|{
return|return
name|type1Sequence
return|;
block|}
comment|/**      * Renders the Type 1 char string sequence to a GeneralPath.      */
specifier|private
name|void
name|render
parameter_list|()
block|{
name|path
operator|=
operator|new
name|GeneralPath
argument_list|()
expr_stmt|;
name|leftSideBearing
operator|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|width
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
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Number
argument_list|>
name|handleCommand
parameter_list|(
name|List
argument_list|<
name|Number
argument_list|>
name|numbers
parameter_list|,
name|CharStringCommand
name|command
parameter_list|)
block|{
return|return
name|Type1CharString
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
name|type1Sequence
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|Number
argument_list|>
name|handleCommand
parameter_list|(
name|List
argument_list|<
name|Number
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
name|TYPE1_VOCABULARY
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
literal|"rmoveto"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|2
condition|)
block|{
if|if
condition|(
name|isFlex
condition|)
block|{
name|flexPoints
operator|.
name|add
argument_list|(
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rmoveTo
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
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|1
condition|)
block|{
if|if
condition|(
name|isFlex
condition|)
block|{
comment|// not in the Type 1 spec, but exists in some fonts
name|flexPoints
operator|.
name|add
argument_list|(
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
literal|0f
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rmoveTo
argument_list|(
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|1
condition|)
block|{
if|if
condition|(
name|isFlex
condition|)
block|{
comment|// not in the Type 1 spec, but exists in some fonts
name|flexPoints
operator|.
name|add
argument_list|(
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
literal|0f
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rmoveTo
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|2
condition|)
block|{
name|rlineTo
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
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|1
condition|)
block|{
name|rlineTo
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|1
condition|)
block|{
name|rlineTo
argument_list|(
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|6
condition|)
block|{
name|rrcurveTo
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
name|numbers
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"closepath"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|closepath
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"sbw"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|3
condition|)
block|{
name|leftSideBearing
operator|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|width
operator|=
name|numbers
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
name|current
operator|.
name|setLocation
argument_list|(
name|leftSideBearing
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"hsbw"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|2
condition|)
block|{
name|leftSideBearing
operator|=
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|floatValue
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|width
operator|=
name|numbers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
name|current
operator|.
name|setLocation
argument_list|(
name|leftSideBearing
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|4
condition|)
block|{
name|rrcurveTo
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
literal|0
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|4
condition|)
block|{
name|rrcurveTo
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
literal|0
argument_list|,
name|numbers
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"seac"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|5
condition|)
block|{
name|seac
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
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"setcurrentpoint"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|2
condition|)
block|{
name|setcurrentpoint
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
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"callothersubr"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|numbers
operator|.
name|size
argument_list|()
operator|>=
literal|1
condition|)
block|{
name|callothersubr
argument_list|(
name|numbers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"div"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|float
name|b
init|=
name|numbers
operator|.
name|get
argument_list|(
name|numbers
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|float
name|a
init|=
name|numbers
operator|.
name|get
argument_list|(
name|numbers
operator|.
name|size
argument_list|()
operator|-
literal|2
argument_list|)
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|float
name|result
init|=
name|a
operator|/
name|b
decl_stmt|;
name|List
argument_list|<
name|Number
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|numbers
argument_list|)
decl_stmt|;
name|list
operator|.
name|remove
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|list
operator|.
name|remove
argument_list|(
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|list
return|;
block|}
elseif|else
if|if
condition|(
literal|"hstem"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"vstem"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"hstem3"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"vstem3"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"dotsection"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// ignore hints
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
comment|// end
block|}
elseif|else
if|if
condition|(
literal|"return"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// indicates an invalid charstring
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unexpected charstring command: "
operator|+
name|command
operator|.
name|getKey
argument_list|()
operator|+
literal|" in glyph "
operator|+
name|glyphName
operator|+
literal|" of font "
operator|+
name|fontName
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
comment|// indicates a PDFBox bug
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unhandled command: "
operator|+
name|name
argument_list|)
throw|;
block|}
else|else
block|{
comment|// indicates an invalid charstring
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unknown charstring command: "
operator|+
name|command
operator|.
name|getKey
argument_list|()
operator|+
literal|" in glyph "
operator|+
name|glyphName
operator|+
literal|" of font "
operator|+
name|fontName
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
comment|/**      * Sets the current absolute point without performing a moveto.      * Used only with results from callothersubr      */
specifier|private
name|void
name|setcurrentpoint
parameter_list|(
name|Number
name|x
parameter_list|,
name|Number
name|y
parameter_list|)
block|{
name|current
operator|.
name|setLocation
argument_list|(
name|x
operator|.
name|floatValue
argument_list|()
argument_list|,
name|y
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Flex (via OtherSubrs)      * @param num OtherSubrs entry number      */
specifier|private
name|void
name|callothersubr
parameter_list|(
name|int
name|num
parameter_list|)
block|{
if|if
condition|(
name|num
operator|==
literal|0
condition|)
block|{
comment|// end flex
name|isFlex
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|flexPoints
operator|.
name|size
argument_list|()
operator|<
literal|7
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"flex without moveTo in font "
operator|+
name|fontName
operator|+
literal|", glyph "
operator|+
name|glyphName
operator|+
literal|", command "
operator|+
name|commandCount
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// reference point is relative to start point
name|Point
operator|.
name|Float
name|reference
init|=
name|flexPoints
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|reference
operator|.
name|setLocation
argument_list|(
name|current
operator|.
name|getX
argument_list|()
operator|+
name|reference
operator|.
name|getX
argument_list|()
argument_list|,
name|current
operator|.
name|getY
argument_list|()
operator|+
name|reference
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
comment|// first point is relative to reference point
name|Point
operator|.
name|Float
name|first
init|=
name|flexPoints
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|first
operator|.
name|setLocation
argument_list|(
name|reference
operator|.
name|getX
argument_list|()
operator|+
name|first
operator|.
name|getX
argument_list|()
argument_list|,
name|reference
operator|.
name|getY
argument_list|()
operator|+
name|first
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
comment|// make the first point relative to the start point
name|first
operator|.
name|setLocation
argument_list|(
name|first
operator|.
name|getX
argument_list|()
operator|-
name|current
operator|.
name|getX
argument_list|()
argument_list|,
name|first
operator|.
name|getY
argument_list|()
operator|-
name|current
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|rrcurveTo
argument_list|(
name|flexPoints
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getX
argument_list|()
argument_list|,
name|flexPoints
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getY
argument_list|()
argument_list|,
name|flexPoints
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getX
argument_list|()
argument_list|,
name|flexPoints
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getY
argument_list|()
argument_list|,
name|flexPoints
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getX
argument_list|()
argument_list|,
name|flexPoints
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|rrcurveTo
argument_list|(
name|flexPoints
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getX
argument_list|()
argument_list|,
name|flexPoints
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|getY
argument_list|()
argument_list|,
name|flexPoints
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|getX
argument_list|()
argument_list|,
name|flexPoints
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|getY
argument_list|()
argument_list|,
name|flexPoints
operator|.
name|get
argument_list|(
literal|6
argument_list|)
operator|.
name|getX
argument_list|()
argument_list|,
name|flexPoints
operator|.
name|get
argument_list|(
literal|6
argument_list|)
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|flexPoints
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|num
operator|==
literal|1
condition|)
block|{
comment|// begin flex
name|isFlex
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// indicates a PDFBox bug
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unexpected other subroutine: "
operator|+
name|num
argument_list|)
throw|;
block|}
block|}
comment|/**      * Relative moveto.      */
specifier|private
name|void
name|rmoveTo
parameter_list|(
name|Number
name|dx
parameter_list|,
name|Number
name|dy
parameter_list|)
block|{
name|float
name|x
init|=
operator|(
name|float
operator|)
name|current
operator|.
name|getX
argument_list|()
operator|+
name|dx
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|float
name|y
init|=
operator|(
name|float
operator|)
name|current
operator|.
name|getY
argument_list|()
operator|+
name|dy
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|path
operator|.
name|moveTo
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|current
operator|.
name|setLocation
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
comment|/**      * Relative lineto.      */
specifier|private
name|void
name|rlineTo
parameter_list|(
name|Number
name|dx
parameter_list|,
name|Number
name|dy
parameter_list|)
block|{
name|float
name|x
init|=
operator|(
name|float
operator|)
name|current
operator|.
name|getX
argument_list|()
operator|+
name|dx
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|float
name|y
init|=
operator|(
name|float
operator|)
name|current
operator|.
name|getY
argument_list|()
operator|+
name|dy
operator|.
name|floatValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|getCurrentPoint
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"rlineTo without initial moveTo in font "
operator|+
name|fontName
operator|+
literal|", glyph "
operator|+
name|glyphName
argument_list|)
expr_stmt|;
name|path
operator|.
name|moveTo
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|.
name|lineTo
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
name|current
operator|.
name|setLocation
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
comment|/**      * Relative curveto.      */
specifier|private
name|void
name|rrcurveTo
parameter_list|(
name|Number
name|dx1
parameter_list|,
name|Number
name|dy1
parameter_list|,
name|Number
name|dx2
parameter_list|,
name|Number
name|dy2
parameter_list|,
name|Number
name|dx3
parameter_list|,
name|Number
name|dy3
parameter_list|)
block|{
name|float
name|x1
init|=
operator|(
name|float
operator|)
name|current
operator|.
name|getX
argument_list|()
operator|+
name|dx1
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|float
name|y1
init|=
operator|(
name|float
operator|)
name|current
operator|.
name|getY
argument_list|()
operator|+
name|dy1
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|float
name|x2
init|=
name|x1
operator|+
name|dx2
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|float
name|y2
init|=
name|y1
operator|+
name|dy2
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|float
name|x3
init|=
name|x2
operator|+
name|dx3
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|float
name|y3
init|=
name|y2
operator|+
name|dy3
operator|.
name|floatValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|getCurrentPoint
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"rrcurveTo without initial moveTo in font "
operator|+
name|fontName
operator|+
literal|", glyph "
operator|+
name|glyphName
argument_list|)
expr_stmt|;
name|path
operator|.
name|moveTo
argument_list|(
name|x3
argument_list|,
name|y3
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|.
name|curveTo
argument_list|(
name|x1
argument_list|,
name|y1
argument_list|,
name|x2
argument_list|,
name|y2
argument_list|,
name|x3
argument_list|,
name|y3
argument_list|)
expr_stmt|;
block|}
name|current
operator|.
name|setLocation
argument_list|(
name|x3
argument_list|,
name|y3
argument_list|)
expr_stmt|;
block|}
comment|/**      * Close path.      */
specifier|private
name|void
name|closepath
parameter_list|()
block|{
if|if
condition|(
name|path
operator|.
name|getCurrentPoint
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"closepath without initial moveTo in font "
operator|+
name|fontName
operator|+
literal|", glyph "
operator|+
name|glyphName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|.
name|closePath
argument_list|()
expr_stmt|;
block|}
name|path
operator|.
name|moveTo
argument_list|(
name|current
operator|.
name|getX
argument_list|()
argument_list|,
name|current
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Standard Encoding Accented Character      *      * Makes an accented character from two other characters.      * @param asb      */
specifier|private
name|void
name|seac
parameter_list|(
name|Number
name|asb
parameter_list|,
name|Number
name|adx
parameter_list|,
name|Number
name|ady
parameter_list|,
name|Number
name|bchar
parameter_list|,
name|Number
name|achar
parameter_list|)
block|{
comment|// base character
name|String
name|baseName
init|=
name|StandardEncoding
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|(
name|bchar
operator|.
name|intValue
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Type1CharString
name|base
init|=
name|font
operator|.
name|getType1CharString
argument_list|(
name|baseName
argument_list|)
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|base
operator|.
name|getPath
argument_list|()
operator|.
name|getPathIterator
argument_list|(
literal|null
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"invalid seac character in glyph "
operator|+
name|glyphName
operator|+
literal|" of font "
operator|+
name|fontName
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// accent character
name|String
name|accentName
init|=
name|StandardEncoding
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|(
name|achar
operator|.
name|intValue
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Type1CharString
name|accent
init|=
name|font
operator|.
name|getType1CharString
argument_list|(
name|accentName
argument_list|)
decl_stmt|;
name|AffineTransform
name|at
init|=
name|AffineTransform
operator|.
name|getTranslateInstance
argument_list|(
name|leftSideBearing
operator|.
name|getX
argument_list|()
operator|+
name|adx
operator|.
name|floatValue
argument_list|()
operator|-
name|asb
operator|.
name|floatValue
argument_list|()
argument_list|,
name|leftSideBearing
operator|.
name|getY
argument_list|()
operator|+
name|ady
operator|.
name|floatValue
argument_list|()
argument_list|)
decl_stmt|;
name|path
operator|.
name|append
argument_list|(
name|accent
operator|.
name|getPath
argument_list|()
operator|.
name|getPathIterator
argument_list|(
name|at
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"invalid seac character in glyph "
operator|+
name|glyphName
operator|+
literal|" of font "
operator|+
name|fontName
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|type1Sequence
operator|.
name|toString
argument_list|()
operator|.
name|replace
argument_list|(
literal|"|"
argument_list|,
literal|"\n"
argument_list|)
operator|.
name|replace
argument_list|(
literal|","
argument_list|,
literal|" "
argument_list|)
return|;
block|}
block|}
end_class

end_unit

