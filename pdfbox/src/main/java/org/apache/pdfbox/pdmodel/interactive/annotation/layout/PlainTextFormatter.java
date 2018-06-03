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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
operator|.
name|layout
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDAppearanceContentStream
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
name|interactive
operator|.
name|annotation
operator|.
name|layout
operator|.
name|PlainText
operator|.
name|Line
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
name|interactive
operator|.
name|annotation
operator|.
name|layout
operator|.
name|PlainText
operator|.
name|Paragraph
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
name|interactive
operator|.
name|annotation
operator|.
name|layout
operator|.
name|PlainText
operator|.
name|TextAttribute
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
name|interactive
operator|.
name|annotation
operator|.
name|layout
operator|.
name|PlainText
operator|.
name|Word
import|;
end_import

begin_comment
comment|/**  * TextFormatter to handle plain text formatting.  *   * The text formatter will take a single value or an array of values which  * are treated as paragraphs.  */
end_comment

begin_class
class|class
name|PlainTextFormatter
block|{
enum|enum
name|TextAlign
block|{
name|LEFT
argument_list|(
literal|0
argument_list|)
block|,
name|CENTER
argument_list|(
literal|1
argument_list|)
block|,
name|RIGHT
argument_list|(
literal|2
argument_list|)
block|,
name|JUSTIFY
argument_list|(
literal|4
argument_list|)
block|;
specifier|private
specifier|final
name|int
name|alignment
decl_stmt|;
specifier|private
name|TextAlign
parameter_list|(
name|int
name|alignment
parameter_list|)
block|{
name|this
operator|.
name|alignment
operator|=
name|alignment
expr_stmt|;
block|}
name|int
name|getTextAlign
parameter_list|()
block|{
return|return
name|alignment
return|;
block|}
specifier|public
specifier|static
name|TextAlign
name|valueOf
parameter_list|(
name|int
name|alignment
parameter_list|)
block|{
for|for
control|(
name|TextAlign
name|textAlignment
range|:
name|TextAlign
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|textAlignment
operator|.
name|getTextAlign
argument_list|()
operator|==
name|alignment
condition|)
block|{
return|return
name|textAlignment
return|;
block|}
block|}
return|return
name|TextAlign
operator|.
name|LEFT
return|;
block|}
block|}
comment|/**      * The scaling factor for font units to PDF units      */
specifier|private
specifier|static
specifier|final
name|int
name|FONTSCALE
init|=
literal|1000
decl_stmt|;
specifier|private
specifier|final
name|AppearanceStyle
name|appearanceStyle
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|wrapLines
decl_stmt|;
specifier|private
specifier|final
name|float
name|width
decl_stmt|;
specifier|private
specifier|final
name|PDAppearanceContentStream
name|contents
decl_stmt|;
specifier|private
specifier|final
name|PlainText
name|textContent
decl_stmt|;
specifier|private
specifier|final
name|TextAlign
name|textAlignment
decl_stmt|;
specifier|private
name|float
name|horizontalOffset
decl_stmt|;
specifier|private
name|float
name|verticalOffset
decl_stmt|;
specifier|static
class|class
name|Builder
block|{
comment|// required parameters
specifier|private
name|PDAppearanceContentStream
name|contents
decl_stmt|;
comment|// optional parameters
specifier|private
name|AppearanceStyle
name|appearanceStyle
decl_stmt|;
specifier|private
name|boolean
name|wrapLines
init|=
literal|false
decl_stmt|;
specifier|private
name|float
name|width
init|=
literal|0f
decl_stmt|;
specifier|private
name|PlainText
name|textContent
decl_stmt|;
specifier|private
name|TextAlign
name|textAlignment
init|=
name|TextAlign
operator|.
name|LEFT
decl_stmt|;
comment|// initial offset from where to start the position of the first line
specifier|private
name|float
name|horizontalOffset
init|=
literal|0f
decl_stmt|;
specifier|private
name|float
name|verticalOffset
init|=
literal|0f
decl_stmt|;
name|Builder
parameter_list|(
name|PDAppearanceContentStream
name|contents
parameter_list|)
block|{
name|this
operator|.
name|contents
operator|=
name|contents
expr_stmt|;
block|}
name|Builder
name|style
parameter_list|(
name|AppearanceStyle
name|appearanceStyle
parameter_list|)
block|{
name|this
operator|.
name|appearanceStyle
operator|=
name|appearanceStyle
expr_stmt|;
return|return
name|this
return|;
block|}
name|Builder
name|wrapLines
parameter_list|(
name|boolean
name|wrapLines
parameter_list|)
block|{
name|this
operator|.
name|wrapLines
operator|=
name|wrapLines
expr_stmt|;
return|return
name|this
return|;
block|}
name|Builder
name|width
parameter_list|(
name|float
name|width
parameter_list|)
block|{
name|this
operator|.
name|width
operator|=
name|width
expr_stmt|;
return|return
name|this
return|;
block|}
name|Builder
name|textAlign
parameter_list|(
name|int
name|alignment
parameter_list|)
block|{
name|this
operator|.
name|textAlignment
operator|=
name|TextAlign
operator|.
name|valueOf
argument_list|(
name|alignment
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
name|Builder
name|textAlign
parameter_list|(
name|TextAlign
name|alignment
parameter_list|)
block|{
name|this
operator|.
name|textAlignment
operator|=
name|alignment
expr_stmt|;
return|return
name|this
return|;
block|}
name|Builder
name|text
parameter_list|(
name|PlainText
name|textContent
parameter_list|)
block|{
name|this
operator|.
name|textContent
operator|=
name|textContent
expr_stmt|;
return|return
name|this
return|;
block|}
name|Builder
name|initialOffset
parameter_list|(
name|float
name|horizontalOffset
parameter_list|,
name|float
name|verticalOffset
parameter_list|)
block|{
name|this
operator|.
name|horizontalOffset
operator|=
name|horizontalOffset
expr_stmt|;
name|this
operator|.
name|verticalOffset
operator|=
name|verticalOffset
expr_stmt|;
return|return
name|this
return|;
block|}
name|PlainTextFormatter
name|build
parameter_list|()
block|{
return|return
operator|new
name|PlainTextFormatter
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
specifier|private
name|PlainTextFormatter
parameter_list|(
name|Builder
name|builder
parameter_list|)
block|{
name|appearanceStyle
operator|=
name|builder
operator|.
name|appearanceStyle
expr_stmt|;
name|wrapLines
operator|=
name|builder
operator|.
name|wrapLines
expr_stmt|;
name|width
operator|=
name|builder
operator|.
name|width
expr_stmt|;
name|contents
operator|=
name|builder
operator|.
name|contents
expr_stmt|;
name|textContent
operator|=
name|builder
operator|.
name|textContent
expr_stmt|;
name|textAlignment
operator|=
name|builder
operator|.
name|textAlignment
expr_stmt|;
name|horizontalOffset
operator|=
name|builder
operator|.
name|horizontalOffset
expr_stmt|;
name|verticalOffset
operator|=
name|builder
operator|.
name|verticalOffset
expr_stmt|;
block|}
comment|/**      * Format the text block.      *       * @throws IOException if there is an error writing to the stream.      */
specifier|public
name|void
name|format
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|textContent
operator|!=
literal|null
operator|&&
operator|!
name|textContent
operator|.
name|getParagraphs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|boolean
name|isFirstParagraph
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Paragraph
name|paragraph
range|:
name|textContent
operator|.
name|getParagraphs
argument_list|()
control|)
block|{
if|if
condition|(
name|wrapLines
condition|)
block|{
name|List
argument_list|<
name|Line
argument_list|>
name|lines
init|=
name|paragraph
operator|.
name|getLines
argument_list|(
name|appearanceStyle
operator|.
name|getFont
argument_list|()
argument_list|,
name|appearanceStyle
operator|.
name|getFontSize
argument_list|()
argument_list|,
name|width
argument_list|)
decl_stmt|;
name|processLines
argument_list|(
name|lines
argument_list|,
name|isFirstParagraph
argument_list|)
expr_stmt|;
name|isFirstParagraph
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|float
name|startOffset
init|=
literal|0f
decl_stmt|;
name|float
name|lineWidth
init|=
name|appearanceStyle
operator|.
name|getFont
argument_list|()
operator|.
name|getStringWidth
argument_list|(
name|paragraph
operator|.
name|getText
argument_list|()
argument_list|)
operator|*
name|appearanceStyle
operator|.
name|getFontSize
argument_list|()
operator|/
name|FONTSCALE
decl_stmt|;
if|if
condition|(
name|lineWidth
operator|<
name|width
condition|)
block|{
switch|switch
condition|(
name|textAlignment
condition|)
block|{
case|case
name|CENTER
case|:
name|startOffset
operator|=
operator|(
name|width
operator|-
name|lineWidth
operator|)
operator|/
literal|2
expr_stmt|;
break|break;
case|case
name|RIGHT
case|:
name|startOffset
operator|=
name|width
operator|-
name|lineWidth
expr_stmt|;
break|break;
case|case
name|JUSTIFY
case|:
default|default:
name|startOffset
operator|=
literal|0f
expr_stmt|;
block|}
block|}
name|contents
operator|.
name|newLineAtOffset
argument_list|(
name|horizontalOffset
operator|+
name|startOffset
argument_list|,
name|verticalOffset
argument_list|)
expr_stmt|;
name|contents
operator|.
name|showText
argument_list|(
name|paragraph
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Process lines for output.       *      * Process lines for an individual paragraph and generate the       * commands for the content stream to show the text.      *       * @param lines the lines to process.      * @throws IOException if there is an error writing to the stream.      */
specifier|private
name|void
name|processLines
parameter_list|(
name|List
argument_list|<
name|Line
argument_list|>
name|lines
parameter_list|,
name|boolean
name|isFirstParagraph
parameter_list|)
throws|throws
name|IOException
block|{
name|float
name|wordWidth
init|=
literal|0f
decl_stmt|;
name|float
name|lastPos
init|=
literal|0f
decl_stmt|;
name|float
name|startOffset
init|=
literal|0f
decl_stmt|;
name|float
name|interWordSpacing
init|=
literal|0f
decl_stmt|;
for|for
control|(
name|Line
name|line
range|:
name|lines
control|)
block|{
switch|switch
condition|(
name|textAlignment
condition|)
block|{
case|case
name|CENTER
case|:
name|startOffset
operator|=
operator|(
name|width
operator|-
name|line
operator|.
name|getWidth
argument_list|()
operator|)
operator|/
literal|2
expr_stmt|;
break|break;
case|case
name|RIGHT
case|:
name|startOffset
operator|=
name|width
operator|-
name|line
operator|.
name|getWidth
argument_list|()
expr_stmt|;
break|break;
case|case
name|JUSTIFY
case|:
if|if
condition|(
name|lines
operator|.
name|indexOf
argument_list|(
name|line
argument_list|)
operator|!=
name|lines
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|)
block|{
name|interWordSpacing
operator|=
name|line
operator|.
name|getInterWordSpacing
argument_list|(
name|width
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
name|startOffset
operator|=
literal|0f
expr_stmt|;
block|}
name|float
name|offset
init|=
operator|-
name|lastPos
operator|+
name|startOffset
operator|+
name|horizontalOffset
decl_stmt|;
if|if
condition|(
name|lines
operator|.
name|indexOf
argument_list|(
name|line
argument_list|)
operator|==
literal|0
operator|&&
name|isFirstParagraph
condition|)
block|{
name|contents
operator|.
name|newLineAtOffset
argument_list|(
name|offset
argument_list|,
name|verticalOffset
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// keep the last position
name|verticalOffset
operator|=
name|verticalOffset
operator|-
name|appearanceStyle
operator|.
name|getLeading
argument_list|()
expr_stmt|;
name|contents
operator|.
name|newLineAtOffset
argument_list|(
name|offset
argument_list|,
operator|-
name|appearanceStyle
operator|.
name|getLeading
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|lastPos
operator|+=
name|offset
expr_stmt|;
name|List
argument_list|<
name|Word
argument_list|>
name|words
init|=
name|line
operator|.
name|getWords
argument_list|()
decl_stmt|;
for|for
control|(
name|Word
name|word
range|:
name|words
control|)
block|{
name|contents
operator|.
name|showText
argument_list|(
name|word
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|wordWidth
operator|=
operator|(
name|Float
operator|)
name|word
operator|.
name|getAttributes
argument_list|()
operator|.
name|getIterator
argument_list|()
operator|.
name|getAttribute
argument_list|(
name|TextAttribute
operator|.
name|WIDTH
argument_list|)
expr_stmt|;
if|if
condition|(
name|words
operator|.
name|indexOf
argument_list|(
name|word
argument_list|)
operator|!=
name|words
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|)
block|{
name|contents
operator|.
name|newLineAtOffset
argument_list|(
name|wordWidth
operator|+
name|interWordSpacing
argument_list|,
literal|0f
argument_list|)
expr_stmt|;
name|lastPos
operator|=
name|lastPos
operator|+
name|wordWidth
operator|+
name|interWordSpacing
expr_stmt|;
block|}
block|}
block|}
name|horizontalOffset
operator|=
name|horizontalOffset
operator|-
name|lastPos
expr_stmt|;
block|}
block|}
end_class

end_unit
