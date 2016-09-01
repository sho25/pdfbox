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
name|form
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
name|text
operator|.
name|AttributedString
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|BreakIterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|AttributedCharacterIterator
operator|.
name|Attribute
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
name|font
operator|.
name|PDFont
import|;
end_import

begin_comment
comment|/**  * A block of text.  *<p>  * A block of text can contain multiple paragraphs which will  * be treated individually within the block placement.  *</p>  *   */
end_comment

begin_class
class|class
name|PlainText
block|{
specifier|private
specifier|static
specifier|final
name|float
name|FONTSCALE
init|=
literal|1000f
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|Paragraph
argument_list|>
name|paragraphs
decl_stmt|;
comment|/**      * Construct the text block from a single value.      *       * Constructs the text block from a single value splitting      * into individual {@link Paragraph} when a new line character is       * encountered.      *       * @param textValue the text block string.      */
name|PlainText
parameter_list|(
name|String
name|textValue
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|textValue
operator|.
name|replaceAll
argument_list|(
literal|"\t"
argument_list|,
literal|" "
argument_list|)
operator|.
name|split
argument_list|(
literal|"\\r\\n|\\n|\\r|\\u2028|\\u2029"
argument_list|)
argument_list|)
decl_stmt|;
name|paragraphs
operator|=
operator|new
name|ArrayList
argument_list|<
name|Paragraph
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
comment|// Acrobat prints a space for an empty paragraph
if|if
condition|(
name|part
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|part
operator|=
literal|" "
expr_stmt|;
block|}
name|paragraphs
operator|.
name|add
argument_list|(
operator|new
name|Paragraph
argument_list|(
name|part
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Construct the text block from a list of values.      *       * Constructs the text block from a list of values treating each      * entry as an individual {@link Paragraph}.      *       * @param listValue the text block string.      */
name|PlainText
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|listValue
parameter_list|)
block|{
name|paragraphs
operator|=
operator|new
name|ArrayList
argument_list|<
name|Paragraph
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|part
range|:
name|listValue
control|)
block|{
name|paragraphs
operator|.
name|add
argument_list|(
operator|new
name|Paragraph
argument_list|(
name|part
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the list of paragraphs.      *       * @return the paragraphs.      */
name|List
argument_list|<
name|Paragraph
argument_list|>
name|getParagraphs
parameter_list|()
block|{
return|return
name|paragraphs
return|;
block|}
comment|/**      * Attribute keys and attribute values used for text handling.      *       * This is similar to {@link java.awt.font.TextAttribute} but      * handled individually as to avoid a dependency on awt.      *       */
specifier|static
class|class
name|TextAttribute
extends|extends
name|Attribute
block|{
comment|/**          * UID for serializing.          */
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3138885145941283005L
decl_stmt|;
comment|/**          * Attribute width of the text.          */
specifier|public
specifier|static
specifier|final
name|Attribute
name|WIDTH
init|=
operator|new
name|TextAttribute
argument_list|(
literal|"width"
argument_list|)
decl_stmt|;
specifier|protected
name|TextAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A block of text to be formatted as a whole.      *<p>      * A block of text can contain multiple paragraphs which will      * be treated individually within the block placement.      *</p>      *       */
specifier|static
class|class
name|Paragraph
block|{
specifier|private
specifier|final
name|String
name|textContent
decl_stmt|;
name|Paragraph
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|textContent
operator|=
name|text
expr_stmt|;
block|}
comment|/**          * Get the paragraph text.          *           * @return the text.          */
name|String
name|getText
parameter_list|()
block|{
return|return
name|textContent
return|;
block|}
comment|/**          * Break the paragraph into individual lines.          *           * @param font the font used for rendering the text.          * @param fontSize the fontSize used for rendering the text.          * @param width the width of the box holding the content.          * @return the individual lines.          * @throws IOException          */
name|List
argument_list|<
name|Line
argument_list|>
name|getLines
parameter_list|(
name|PDFont
name|font
parameter_list|,
name|float
name|fontSize
parameter_list|,
name|float
name|width
parameter_list|)
throws|throws
name|IOException
block|{
name|BreakIterator
name|iterator
init|=
name|BreakIterator
operator|.
name|getLineInstance
argument_list|()
decl_stmt|;
name|iterator
operator|.
name|setText
argument_list|(
name|textContent
argument_list|)
expr_stmt|;
specifier|final
name|float
name|scale
init|=
name|fontSize
operator|/
name|FONTSCALE
decl_stmt|;
name|int
name|start
init|=
name|iterator
operator|.
name|first
argument_list|()
decl_stmt|;
name|int
name|end
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|float
name|lineWidth
init|=
literal|0
decl_stmt|;
name|List
argument_list|<
name|Line
argument_list|>
name|textLines
init|=
operator|new
name|ArrayList
argument_list|<
name|Line
argument_list|>
argument_list|()
decl_stmt|;
name|Line
name|textLine
init|=
operator|new
name|Line
argument_list|()
decl_stmt|;
while|while
condition|(
name|end
operator|!=
name|BreakIterator
operator|.
name|DONE
condition|)
block|{
name|String
name|word
init|=
name|textContent
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
decl_stmt|;
name|float
name|wordWidth
init|=
name|font
operator|.
name|getStringWidth
argument_list|(
name|word
argument_list|)
operator|*
name|scale
decl_stmt|;
name|lineWidth
operator|=
name|lineWidth
operator|+
name|wordWidth
expr_stmt|;
comment|// check if the last word would fit without the whitespace ending it
if|if
condition|(
name|lineWidth
operator|>=
name|width
operator|&&
name|Character
operator|.
name|isWhitespace
argument_list|(
name|word
operator|.
name|charAt
argument_list|(
name|word
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
name|float
name|whitespaceWidth
init|=
name|font
operator|.
name|getStringWidth
argument_list|(
name|word
operator|.
name|substring
argument_list|(
name|word
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
operator|*
name|scale
decl_stmt|;
name|lineWidth
operator|=
name|lineWidth
operator|-
name|whitespaceWidth
expr_stmt|;
block|}
if|if
condition|(
name|lineWidth
operator|>=
name|width
condition|)
block|{
name|textLine
operator|.
name|setWidth
argument_list|(
name|textLine
operator|.
name|calculateWidth
argument_list|(
name|font
argument_list|,
name|fontSize
argument_list|)
argument_list|)
expr_stmt|;
name|textLines
operator|.
name|add
argument_list|(
name|textLine
argument_list|)
expr_stmt|;
name|textLine
operator|=
operator|new
name|Line
argument_list|()
expr_stmt|;
name|lineWidth
operator|=
name|font
operator|.
name|getStringWidth
argument_list|(
name|word
argument_list|)
operator|*
name|scale
expr_stmt|;
block|}
name|AttributedString
name|as
init|=
operator|new
name|AttributedString
argument_list|(
name|word
argument_list|)
decl_stmt|;
name|as
operator|.
name|addAttribute
argument_list|(
name|TextAttribute
operator|.
name|WIDTH
argument_list|,
name|wordWidth
argument_list|)
expr_stmt|;
name|Word
name|wordInstance
init|=
operator|new
name|Word
argument_list|(
name|word
argument_list|)
decl_stmt|;
name|wordInstance
operator|.
name|setAttributes
argument_list|(
name|as
argument_list|)
expr_stmt|;
name|textLine
operator|.
name|addWord
argument_list|(
name|wordInstance
argument_list|)
expr_stmt|;
name|start
operator|=
name|end
expr_stmt|;
name|end
operator|=
name|iterator
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
name|textLine
operator|.
name|setWidth
argument_list|(
name|textLine
operator|.
name|calculateWidth
argument_list|(
name|font
argument_list|,
name|fontSize
argument_list|)
argument_list|)
expr_stmt|;
name|textLines
operator|.
name|add
argument_list|(
name|textLine
argument_list|)
expr_stmt|;
return|return
name|textLines
return|;
block|}
block|}
comment|/**      * An individual line of text.      */
specifier|static
class|class
name|Line
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|Word
argument_list|>
name|words
init|=
operator|new
name|ArrayList
argument_list|<
name|Word
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|float
name|lineWidth
decl_stmt|;
name|float
name|getWidth
parameter_list|()
block|{
return|return
name|lineWidth
return|;
block|}
name|void
name|setWidth
parameter_list|(
name|float
name|width
parameter_list|)
block|{
name|lineWidth
operator|=
name|width
expr_stmt|;
block|}
name|float
name|calculateWidth
parameter_list|(
name|PDFont
name|font
parameter_list|,
name|float
name|fontSize
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|float
name|scale
init|=
name|fontSize
operator|/
name|FONTSCALE
decl_stmt|;
name|float
name|calculatedWidth
init|=
literal|0f
decl_stmt|;
for|for
control|(
name|Word
name|word
range|:
name|words
control|)
block|{
name|calculatedWidth
operator|=
name|calculatedWidth
operator|+
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
name|String
name|text
init|=
name|word
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|words
operator|.
name|indexOf
argument_list|(
name|word
argument_list|)
operator|==
name|words
operator|.
name|size
argument_list|()
operator|-
literal|1
operator|&&
name|Character
operator|.
name|isWhitespace
argument_list|(
name|text
operator|.
name|charAt
argument_list|(
name|text
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
condition|)
block|{
name|float
name|whitespaceWidth
init|=
name|font
operator|.
name|getStringWidth
argument_list|(
name|text
operator|.
name|substring
argument_list|(
name|text
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
operator|*
name|scale
decl_stmt|;
name|calculatedWidth
operator|=
name|calculatedWidth
operator|-
name|whitespaceWidth
expr_stmt|;
block|}
block|}
return|return
name|calculatedWidth
return|;
block|}
name|List
argument_list|<
name|Word
argument_list|>
name|getWords
parameter_list|()
block|{
return|return
name|words
return|;
block|}
name|float
name|getInterWordSpacing
parameter_list|(
name|float
name|width
parameter_list|)
block|{
return|return
operator|(
name|width
operator|-
name|lineWidth
operator|)
operator|/
operator|(
name|words
operator|.
name|size
argument_list|()
operator|-
literal|1
operator|)
return|;
block|}
name|void
name|addWord
parameter_list|(
name|Word
name|word
parameter_list|)
block|{
name|words
operator|.
name|add
argument_list|(
name|word
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * An individual word.      *       * A word is defined as a string which must be kept      * on the same line.      */
specifier|static
class|class
name|Word
block|{
specifier|private
name|AttributedString
name|attributedString
decl_stmt|;
specifier|private
specifier|final
name|String
name|textContent
decl_stmt|;
name|Word
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|textContent
operator|=
name|text
expr_stmt|;
block|}
name|String
name|getText
parameter_list|()
block|{
return|return
name|textContent
return|;
block|}
name|AttributedString
name|getAttributes
parameter_list|()
block|{
return|return
name|attributedString
return|;
block|}
name|void
name|setAttributes
parameter_list|(
name|AttributedString
name|as
parameter_list|)
block|{
name|this
operator|.
name|attributedString
operator|=
name|as
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

