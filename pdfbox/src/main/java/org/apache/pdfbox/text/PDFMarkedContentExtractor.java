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
name|text
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
name|pdfbox
operator|.
name|cos
operator|.
name|COSDictionary
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
name|cos
operator|.
name|COSName
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
name|documentinterchange
operator|.
name|markedcontent
operator|.
name|PDMarkedContent
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
name|graphics
operator|.
name|PDXObject
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
name|contentstream
operator|.
name|operator
operator|.
name|markedcontent
operator|.
name|BeginMarkedContentSequence
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
name|contentstream
operator|.
name|operator
operator|.
name|markedcontent
operator|.
name|BeginMarkedContentSequenceWithProperties
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
name|contentstream
operator|.
name|operator
operator|.
name|markedcontent
operator|.
name|DrawObject
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
name|contentstream
operator|.
name|operator
operator|.
name|markedcontent
operator|.
name|EndMarkedContentSequence
import|;
end_import

begin_comment
comment|/**  * This is an stream engine to extract the marked content of a pdf.  *  * @author Johannes Koch  */
end_comment

begin_class
specifier|public
class|class
name|PDFMarkedContentExtractor
extends|extends
name|LegacyPDFStreamEngine
block|{
specifier|private
specifier|final
name|boolean
name|suppressDuplicateOverlappingText
init|=
literal|true
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|PDMarkedContent
argument_list|>
name|markedContents
init|=
operator|new
name|ArrayList
argument_list|<
name|PDMarkedContent
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Stack
argument_list|<
name|PDMarkedContent
argument_list|>
name|currentMarkedContents
init|=
operator|new
name|Stack
argument_list|<
name|PDMarkedContent
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|TextPosition
argument_list|>
argument_list|>
name|characterListMapping
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|TextPosition
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Instantiate a new PDFTextStripper object.      */
specifier|public
name|PDFMarkedContentExtractor
parameter_list|()
throws|throws
name|IOException
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor. Will apply encoding-specific conversions to the output text.      *      * @param encoding The encoding that the output will be written in.      */
specifier|public
name|PDFMarkedContentExtractor
parameter_list|(
name|String
name|encoding
parameter_list|)
throws|throws
name|IOException
block|{
name|addOperator
argument_list|(
operator|new
name|BeginMarkedContentSequenceWithProperties
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|BeginMarkedContentSequence
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|EndMarkedContentSequence
argument_list|()
argument_list|)
expr_stmt|;
name|addOperator
argument_list|(
operator|new
name|DrawObject
argument_list|()
argument_list|)
expr_stmt|;
comment|// todo: DP - Marked Content Point
comment|// todo: MP - Marked Content Point with Properties
block|}
comment|/**      * This will determine of two floating point numbers are within a specified variance.      *      * @param first The first number to compare to.      * @param second The second number to compare to.      * @param variance The allowed variance.      */
specifier|private
name|boolean
name|within
parameter_list|(
name|float
name|first
parameter_list|,
name|float
name|second
parameter_list|,
name|float
name|variance
parameter_list|)
block|{
return|return
name|second
operator|>
name|first
operator|-
name|variance
operator|&&
name|second
operator|<
name|first
operator|+
name|variance
return|;
block|}
specifier|public
name|void
name|beginMarkedContentSequence
parameter_list|(
name|COSName
name|tag
parameter_list|,
name|COSDictionary
name|properties
parameter_list|)
block|{
name|PDMarkedContent
name|markedContent
init|=
name|PDMarkedContent
operator|.
name|create
argument_list|(
name|tag
argument_list|,
name|properties
argument_list|)
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|currentMarkedContents
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|markedContents
operator|.
name|add
argument_list|(
name|markedContent
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PDMarkedContent
name|currentMarkedContent
init|=
name|this
operator|.
name|currentMarkedContents
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentMarkedContent
operator|!=
literal|null
condition|)
block|{
name|currentMarkedContent
operator|.
name|addMarkedContent
argument_list|(
name|markedContent
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|currentMarkedContents
operator|.
name|push
argument_list|(
name|markedContent
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|endMarkedContentSequence
parameter_list|()
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|currentMarkedContents
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|currentMarkedContents
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|xobject
parameter_list|(
name|PDXObject
name|xobject
parameter_list|)
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|currentMarkedContents
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|currentMarkedContents
operator|.
name|peek
argument_list|()
operator|.
name|addXObject
argument_list|(
name|xobject
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will process a TextPosition object and add the      * text to the list of characters on a page.  It takes care of      * overlapping text.      *      * @param text The text to process.      */
annotation|@
name|Override
specifier|protected
name|void
name|processTextPosition
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
name|boolean
name|showCharacter
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|suppressDuplicateOverlappingText
condition|)
block|{
name|showCharacter
operator|=
literal|false
expr_stmt|;
name|String
name|textCharacter
init|=
name|text
operator|.
name|getUnicode
argument_list|()
decl_stmt|;
name|float
name|textX
init|=
name|text
operator|.
name|getX
argument_list|()
decl_stmt|;
name|float
name|textY
init|=
name|text
operator|.
name|getY
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|TextPosition
argument_list|>
name|sameTextCharacters
init|=
name|this
operator|.
name|characterListMapping
operator|.
name|get
argument_list|(
name|textCharacter
argument_list|)
decl_stmt|;
if|if
condition|(
name|sameTextCharacters
operator|==
literal|null
condition|)
block|{
name|sameTextCharacters
operator|=
operator|new
name|ArrayList
argument_list|<
name|TextPosition
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|characterListMapping
operator|.
name|put
argument_list|(
name|textCharacter
argument_list|,
name|sameTextCharacters
argument_list|)
expr_stmt|;
block|}
comment|// RDD - Here we compute the value that represents the end of the rendered
comment|// text.  This value is used to determine whether subsequent text rendered
comment|// on the same line overwrites the current text.
comment|//
comment|// We subtract any positive padding to handle cases where extreme amounts
comment|// of padding are applied, then backed off (not sure why this is done, but there
comment|// are cases where the padding is on the order of 10x the character width, and
comment|// the TJ just backs up to compensate after each character).  Also, we subtract
comment|// an amount to allow for kerning (a percentage of the width of the last
comment|// character).
comment|//
name|boolean
name|suppressCharacter
init|=
literal|false
decl_stmt|;
name|float
name|tolerance
init|=
operator|(
name|text
operator|.
name|getWidth
argument_list|()
operator|/
name|textCharacter
operator|.
name|length
argument_list|()
operator|)
operator|/
literal|3.0f
decl_stmt|;
for|for
control|(
name|TextPosition
name|sameTextCharacter
range|:
name|sameTextCharacters
control|)
block|{
name|TextPosition
name|character
init|=
name|sameTextCharacter
decl_stmt|;
name|String
name|charCharacter
init|=
name|character
operator|.
name|getUnicode
argument_list|()
decl_stmt|;
name|float
name|charX
init|=
name|character
operator|.
name|getX
argument_list|()
decl_stmt|;
name|float
name|charY
init|=
name|character
operator|.
name|getY
argument_list|()
decl_stmt|;
comment|//only want to suppress
if|if
condition|(
name|charCharacter
operator|!=
literal|null
operator|&&
comment|//charCharacter.equals( textCharacter )&&
name|within
argument_list|(
name|charX
argument_list|,
name|textX
argument_list|,
name|tolerance
argument_list|)
operator|&&
name|within
argument_list|(
name|charY
argument_list|,
name|textY
argument_list|,
name|tolerance
argument_list|)
condition|)
block|{
name|suppressCharacter
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|suppressCharacter
condition|)
block|{
name|sameTextCharacters
operator|.
name|add
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|showCharacter
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|showCharacter
condition|)
block|{
name|List
argument_list|<
name|TextPosition
argument_list|>
name|textList
init|=
operator|new
name|ArrayList
argument_list|<
name|TextPosition
argument_list|>
argument_list|()
decl_stmt|;
comment|/* In the wild, some PDF encoded documents put diacritics (accents on              * top of characters) into a separate Tj element.  When displaying them              * graphically, the two chunks get overlayed.  With text output though,              * we need to do the overlay. This code recombines the diacritic with              * its associated character if the two are consecutive.              */
if|if
condition|(
name|textList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|textList
operator|.
name|add
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|/* test if we overlap the previous entry.                    * Note that we are making an assumption that we need to only look back                  * one TextPosition to find what we are overlapping.                    * This may not always be true. */
name|TextPosition
name|previousTextPosition
init|=
name|textList
operator|.
name|get
argument_list|(
name|textList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|text
operator|.
name|isDiacritic
argument_list|()
operator|&&
name|previousTextPosition
operator|.
name|contains
argument_list|(
name|text
argument_list|)
condition|)
block|{
name|previousTextPosition
operator|.
name|mergeDiacritic
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
comment|/* If the previous TextPosition was the diacritic, merge it into this                  * one and remove it from the list. */
elseif|else
if|if
condition|(
name|previousTextPosition
operator|.
name|isDiacritic
argument_list|()
operator|&&
name|text
operator|.
name|contains
argument_list|(
name|previousTextPosition
argument_list|)
condition|)
block|{
name|text
operator|.
name|mergeDiacritic
argument_list|(
name|previousTextPosition
argument_list|)
expr_stmt|;
name|textList
operator|.
name|remove
argument_list|(
name|textList
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|textList
operator|.
name|add
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|textList
operator|.
name|add
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|this
operator|.
name|currentMarkedContents
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|currentMarkedContents
operator|.
name|peek
argument_list|()
operator|.
name|addText
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|List
argument_list|<
name|PDMarkedContent
argument_list|>
name|getMarkedContents
parameter_list|()
block|{
return|return
name|this
operator|.
name|markedContents
return|;
block|}
block|}
end_class

end_unit

