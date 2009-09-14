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
name|util
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
name|Properties
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
name|java
operator|.
name|util
operator|.
name|Vector
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
name|pdfbox
operator|.
name|cos
operator|.
name|COSObject
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
name|COSStream
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
name|exceptions
operator|.
name|WrappedIOException
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
name|PDPage
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
name|PDResources
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
name|PDGraphicsState
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
name|util
operator|.
name|operator
operator|.
name|OperatorProcessor
import|;
end_import

begin_comment
comment|/**  * This class will run through a PDF content stream and execute certain operations  * and provide a callback interface for clients that want to do things with the stream.  * See the PDFTextStripper class for an example of how to use this class.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.38 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFStreamEngine
block|{
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDFStreamEngine
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Vector
name|unsupportedOperators
init|=
operator|new
name|Vector
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|SPACE_BYTES
init|=
block|{
operator|(
name|byte
operator|)
literal|32
block|}
decl_stmt|;
specifier|private
name|PDGraphicsState
name|graphicsState
init|=
literal|null
decl_stmt|;
specifier|private
name|Matrix
name|textMatrix
init|=
literal|null
decl_stmt|;
specifier|private
name|Matrix
name|textLineMatrix
init|=
literal|null
decl_stmt|;
specifier|private
name|Stack
name|graphicsStack
init|=
operator|new
name|Stack
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|operators
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|Stack
name|streamResourcesStack
init|=
operator|new
name|Stack
argument_list|()
decl_stmt|;
specifier|private
name|PDPage
name|page
decl_stmt|;
specifier|private
name|Map
name|documentFontCache
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|int
name|validCharCnt
decl_stmt|;
specifier|private
name|int
name|totalCharCnt
decl_stmt|;
comment|/**      * This is a simple internal class used by the Stream engine to handle the      * resources stack.      */
specifier|private
specifier|static
class|class
name|StreamResources
block|{
specifier|private
name|Map
name|fonts
decl_stmt|;
specifier|private
name|Map
name|colorSpaces
decl_stmt|;
specifier|private
name|Map
name|xobjects
decl_stmt|;
specifier|private
name|Map
name|graphicsStates
decl_stmt|;
specifier|private
name|PDResources
name|resources
decl_stmt|;
specifier|private
name|StreamResources
parameter_list|()
block|{}
empty_stmt|;
block|}
comment|/**      * Constructor.      */
specifier|public
name|PDFStreamEngine
parameter_list|()
block|{
comment|//default constructor
name|validCharCnt
operator|=
literal|0
expr_stmt|;
name|totalCharCnt
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * Constructor with engine properties.  The property keys are all      * PDF operators, the values are class names used to execute those      * operators.      *      * @param properties The engine properties.      *      * @throws IOException If there is an error setting the engine properties.      */
specifier|public
name|PDFStreamEngine
parameter_list|(
name|Properties
name|properties
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"properties cannot be null"
argument_list|)
throw|;
block|}
try|try
block|{
name|Iterator
name|keys
init|=
name|properties
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|keys
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|operator
init|=
operator|(
name|String
operator|)
name|keys
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|operatorClass
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|operator
argument_list|)
decl_stmt|;
name|OperatorProcessor
name|op
init|=
operator|(
name|OperatorProcessor
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|operatorClass
argument_list|)
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|registerOperatorProcessor
argument_list|(
name|operator
argument_list|,
name|op
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WrappedIOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|validCharCnt
operator|=
literal|0
expr_stmt|;
name|totalCharCnt
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * Register a custom operator processor with the engine.      *      * @param operator The operator as a string.      * @param op Processor instance.      */
specifier|public
name|void
name|registerOperatorProcessor
parameter_list|(
name|String
name|operator
parameter_list|,
name|OperatorProcessor
name|op
parameter_list|)
block|{
name|op
operator|.
name|setContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|operators
operator|.
name|put
argument_list|(
name|operator
argument_list|,
name|op
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method must be called between processing documents.  The      * PDFStreamEngine caches information for the document between pages      * and this will release the cached information.  This only needs      * to be called if processing a new document.      *      */
specifier|public
name|void
name|resetEngine
parameter_list|()
block|{
name|documentFontCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|validCharCnt
operator|=
literal|0
expr_stmt|;
name|totalCharCnt
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * This will process the contents of the stream.      *      * @param aPage The page.      * @param resources The location to retrieve resources.      * @param cosStream the Stream to execute.      *      *      * @throws IOException if there is an error accessing the stream.      */
specifier|public
name|void
name|processStream
parameter_list|(
name|PDPage
name|aPage
parameter_list|,
name|PDResources
name|resources
parameter_list|,
name|COSStream
name|cosStream
parameter_list|)
throws|throws
name|IOException
block|{
name|graphicsState
operator|=
operator|new
name|PDGraphicsState
argument_list|()
expr_stmt|;
name|textMatrix
operator|=
literal|null
expr_stmt|;
name|textLineMatrix
operator|=
literal|null
expr_stmt|;
name|graphicsStack
operator|.
name|clear
argument_list|()
expr_stmt|;
name|streamResourcesStack
operator|.
name|clear
argument_list|()
expr_stmt|;
name|processSubStream
argument_list|(
name|aPage
argument_list|,
name|resources
argument_list|,
name|cosStream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Process a sub stream of the current stream.      *      * @param aPage The page used for drawing.      * @param resources The resources used when processing the stream.      * @param cosStream The stream to process.      *      * @throws IOException If there is an exception while processing the stream.      */
specifier|public
name|void
name|processSubStream
parameter_list|(
name|PDPage
name|aPage
parameter_list|,
name|PDResources
name|resources
parameter_list|,
name|COSStream
name|cosStream
parameter_list|)
throws|throws
name|IOException
block|{
name|page
operator|=
name|aPage
expr_stmt|;
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|StreamResources
name|sr
init|=
operator|new
name|StreamResources
argument_list|()
decl_stmt|;
name|sr
operator|.
name|fonts
operator|=
name|resources
operator|.
name|getFonts
argument_list|(
name|documentFontCache
argument_list|)
expr_stmt|;
name|sr
operator|.
name|colorSpaces
operator|=
name|resources
operator|.
name|getColorSpaces
argument_list|()
expr_stmt|;
name|sr
operator|.
name|xobjects
operator|=
name|resources
operator|.
name|getXObjects
argument_list|()
expr_stmt|;
name|sr
operator|.
name|graphicsStates
operator|=
name|resources
operator|.
name|getGraphicsStates
argument_list|()
expr_stmt|;
name|sr
operator|.
name|resources
operator|=
name|resources
expr_stmt|;
name|streamResourcesStack
operator|.
name|push
argument_list|(
name|sr
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|List
name|arguments
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|List
name|tokens
init|=
name|cosStream
operator|.
name|getStreamTokens
argument_list|()
decl_stmt|;
if|if
condition|(
name|tokens
operator|!=
literal|null
condition|)
block|{
name|Iterator
name|iter
init|=
name|tokens
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
name|Object
name|next
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|COSObject
condition|)
block|{
name|arguments
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSObject
operator|)
name|next
operator|)
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|next
operator|instanceof
name|PDFOperator
condition|)
block|{
name|processOperator
argument_list|(
operator|(
name|PDFOperator
operator|)
name|next
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
name|arguments
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|arguments
operator|.
name|add
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"token: "
operator|+
name|next
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|streamResourcesStack
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * A method provided as an event interface to allow a subclass to perform      * some specific functionality when text needs to be processed.      *      * @param text The text to be processed.      */
specifier|protected
name|void
name|processTextPosition
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
comment|//subclasses can override to provide specific functionality.
block|}
comment|/**      * Process encoded text from the PDF Stream.       * You should override this method if you want to perform an action when       * encoded text is being processed.      *      * @param string The encoded text      *      * @throws IOException If there is an error processing the string      */
specifier|public
name|void
name|processEncodedText
parameter_list|(
name|byte
index|[]
name|string
parameter_list|)
throws|throws
name|IOException
block|{
comment|/* Note on variable names.  There are three different units being used          * in this code.  Character sizes are given in glyph units, text locations          * are initially given in text units, and we want to save the data in           * display units. The variable names should end with Text or Disp to           * represent if the values are in text or disp units (no glyph units are saved).          */
specifier|final
name|float
name|fontSizeText
init|=
name|graphicsState
operator|.
name|getTextState
argument_list|()
operator|.
name|getFontSize
argument_list|()
decl_stmt|;
specifier|final
name|float
name|horizontalScalingText
init|=
name|graphicsState
operator|.
name|getTextState
argument_list|()
operator|.
name|getHorizontalScalingPercent
argument_list|()
operator|/
literal|100f
decl_stmt|;
comment|//float verticalScalingText = horizontalScaling;//not sure if this is right but what else to do???
specifier|final
name|float
name|riseText
init|=
name|graphicsState
operator|.
name|getTextState
argument_list|()
operator|.
name|getRise
argument_list|()
decl_stmt|;
specifier|final
name|float
name|wordSpacingText
init|=
name|graphicsState
operator|.
name|getTextState
argument_list|()
operator|.
name|getWordSpacing
argument_list|()
decl_stmt|;
specifier|final
name|float
name|characterSpacingText
init|=
name|graphicsState
operator|.
name|getTextState
argument_list|()
operator|.
name|getCharacterSpacing
argument_list|()
decl_stmt|;
comment|//We won't know the actual number of characters until
comment|//we process the byte data(could be two bytes each) but
comment|//it won't ever be more than string.length*2(there are some cases
comment|//were a single byte will result in two output characters "fi"
specifier|final
name|PDFont
name|font
init|=
name|graphicsState
operator|.
name|getTextState
argument_list|()
operator|.
name|getFont
argument_list|()
decl_stmt|;
comment|//This will typically be 1000 but in the case of a type3 font
comment|//this might be a different number
specifier|final
name|float
name|glyphSpaceToTextSpaceFactor
init|=
literal|1f
operator|/
name|font
operator|.
name|getFontMatrix
argument_list|()
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
comment|// lets see what the space displacement should be
name|float
name|spaceWidthText
init|=
operator|(
name|font
operator|.
name|getFontWidth
argument_list|(
name|SPACE_BYTES
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
operator|/
name|glyphSpaceToTextSpaceFactor
operator|)
decl_stmt|;
if|if
condition|(
name|spaceWidthText
operator|==
literal|0
condition|)
block|{
name|spaceWidthText
operator|=
operator|(
name|font
operator|.
name|getAverageFontWidth
argument_list|()
operator|/
name|glyphSpaceToTextSpaceFactor
operator|)
expr_stmt|;
comment|//The average space width appears to be higher than necessary
comment|//so lets make it a little bit smaller.
name|spaceWidthText
operator|*=
literal|.80f
expr_stmt|;
block|}
comment|/* Convert textMatrix to display units */
name|Matrix
name|initialMatrix
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|initialMatrix
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|initialMatrix
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|initialMatrix
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|initialMatrix
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|initialMatrix
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|initialMatrix
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|initialMatrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|initialMatrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
name|riseText
argument_list|)
expr_stmt|;
name|initialMatrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
specifier|final
name|Matrix
name|ctm
init|=
name|graphicsState
operator|.
name|getCurrentTransformationMatrix
argument_list|()
decl_stmt|;
specifier|final
name|Matrix
name|textMatrixStDisp
init|=
name|initialMatrix
operator|.
name|multiply
argument_list|(
name|textMatrix
argument_list|)
operator|.
name|multiply
argument_list|(
name|ctm
argument_list|)
decl_stmt|;
specifier|final
name|float
name|xScaleDisp
init|=
name|textMatrixStDisp
operator|.
name|getXScale
argument_list|()
decl_stmt|;
specifier|final
name|float
name|yScaleDisp
init|=
name|textMatrixStDisp
operator|.
name|getYScale
argument_list|()
decl_stmt|;
specifier|final
name|float
name|spaceWidthDisp
init|=
name|spaceWidthText
operator|*
name|xScaleDisp
operator|*
name|fontSizeText
decl_stmt|;
specifier|final
name|float
name|wordSpacingDisp
init|=
name|wordSpacingText
operator|*
name|xScaleDisp
operator|*
name|fontSizeText
decl_stmt|;
name|float
name|maxVerticalDisplacementText
init|=
literal|0
decl_stmt|;
name|float
index|[]
name|individualWidthsText
init|=
operator|new
name|float
index|[
literal|2048
index|]
decl_stmt|;
name|StringBuffer
name|stringResult
init|=
operator|new
name|StringBuffer
argument_list|(
name|string
operator|.
name|length
argument_list|)
decl_stmt|;
name|int
name|codeLength
init|=
literal|1
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
name|string
operator|.
name|length
condition|;
name|i
operator|+=
name|codeLength
control|)
block|{
comment|// Decode the value to a Unicode character
name|codeLength
operator|=
literal|1
expr_stmt|;
name|String
name|c
init|=
name|font
operator|.
name|encode
argument_list|(
name|string
argument_list|,
name|i
argument_list|,
name|codeLength
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
operator|&&
name|i
operator|+
literal|1
operator|<
name|string
operator|.
name|length
condition|)
block|{
comment|//maybe a multibyte encoding
name|codeLength
operator|++
expr_stmt|;
name|c
operator|=
name|font
operator|.
name|encode
argument_list|(
name|string
argument_list|,
name|i
argument_list|,
name|codeLength
argument_list|)
expr_stmt|;
block|}
comment|//todo, handle horizontal displacement
comment|// get the width and height of this character in text units
name|float
name|characterHorizontalDisplacementText
init|=
operator|(
name|font
operator|.
name|getFontWidth
argument_list|(
name|string
argument_list|,
name|i
argument_list|,
name|codeLength
argument_list|)
operator|/
name|glyphSpaceToTextSpaceFactor
operator|)
decl_stmt|;
name|maxVerticalDisplacementText
operator|=
name|Math
operator|.
name|max
argument_list|(
name|maxVerticalDisplacementText
argument_list|,
name|font
operator|.
name|getFontHeight
argument_list|(
name|string
argument_list|,
name|i
argument_list|,
name|codeLength
argument_list|)
operator|/
name|glyphSpaceToTextSpaceFactor
argument_list|)
expr_stmt|;
comment|// PDF Spec - 5.5.2 Word Spacing
comment|//
comment|// Word spacing works the same was as character spacing, but applies
comment|// only to the space character, code 32.
comment|//
comment|// Note: Word spacing is applied to every occurrence of the single-byte
comment|// character code 32 in a string.  This can occur when using a simple
comment|// font or a composite font that defines code 32 as a single-byte code.
comment|// It does not apply to occurrences of the byte value 32 in multiple-byte
comment|// codes.
comment|//
comment|// RDD - My interpretation of this is that only character code 32's that
comment|// encode to spaces should have word spacing applied.  Cases have been
comment|// observed where a font has a space character with a character code
comment|// other than 32, and where word spacing (Tw) was used.  In these cases,
comment|// applying word spacing to either the non-32 space or to the character
comment|// code 32 non-space resulted in errors consistent with this interpretation.
comment|//
name|float
name|spacingText
init|=
name|characterSpacingText
decl_stmt|;
if|if
condition|(
operator|(
name|string
index|[
name|i
index|]
operator|==
literal|0x20
operator|)
operator|&&
name|c
operator|!=
literal|null
operator|&&
name|c
operator|.
name|equals
argument_list|(
literal|" "
argument_list|)
condition|)
block|{
name|spacingText
operator|+=
name|wordSpacingText
expr_stmt|;
block|}
comment|// get the X location before we update the text matrix
name|float
name|xPosBeforeText
init|=
name|initialMatrix
operator|.
name|multiply
argument_list|(
name|textMatrix
argument_list|)
operator|.
name|multiply
argument_list|(
name|ctm
argument_list|)
operator|.
name|getXPosition
argument_list|()
decl_stmt|;
comment|/* The text matrix gets updated after each glyph is placed.  The updated              * version will have the X and Y coordinates for the next glyph.              */
comment|//The adjustment will always be zero.  The adjustment as shown in the
comment|//TJ operator will be handled separately.
name|float
name|adjustment
init|=
literal|0
decl_stmt|;
comment|// TODO : tx should be set for horizontal text and ty for vertical text
comment|// which seems to be specified in the font (not the direction in the matrix).
name|float
name|tx
init|=
operator|(
operator|(
name|characterHorizontalDisplacementText
operator|-
name|adjustment
operator|/
name|glyphSpaceToTextSpaceFactor
operator|)
operator|*
name|fontSizeText
operator|+
name|spacingText
operator|)
operator|*
name|horizontalScalingText
decl_stmt|;
name|float
name|ty
init|=
literal|0
decl_stmt|;
name|Matrix
name|td
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|td
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
name|tx
argument_list|)
expr_stmt|;
name|td
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
name|ty
argument_list|)
expr_stmt|;
name|textMatrix
operator|=
name|td
operator|.
name|multiply
argument_list|(
name|textMatrix
argument_list|)
expr_stmt|;
comment|// determine the width of this character
comment|// XXX: Note that if we handled vertical text, we should be using Y here
name|float
name|widthText
init|=
name|initialMatrix
operator|.
name|multiply
argument_list|(
name|textMatrix
argument_list|)
operator|.
name|multiply
argument_list|(
name|ctm
argument_list|)
operator|.
name|getXPosition
argument_list|()
operator|-
name|xPosBeforeText
decl_stmt|;
comment|//there are several cases where one character code will
comment|//output multiple characters.  For example "fi" or a
comment|//glyphname that has no mapping like "visiblespace"
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
comment|// assume each character is the same size
name|float
name|widthOfEachCharacterForCode
init|=
name|widthText
operator|/
name|c
operator|.
name|length
argument_list|()
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
name|c
operator|.
name|length
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|stringResult
operator|.
name|length
argument_list|()
operator|+
name|j
operator|<
name|individualWidthsText
operator|.
name|length
condition|)
block|{
if|if
condition|(
name|c
operator|.
name|equals
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
comment|//System.out.println( "stringResult.length()+j=" + (widthOfEachCharacterForCode));
block|}
name|individualWidthsText
index|[
name|stringResult
operator|.
name|length
argument_list|()
operator|+
name|j
index|]
operator|=
name|widthOfEachCharacterForCode
expr_stmt|;
block|}
block|}
name|validCharCnt
operator|+=
name|c
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// PDFBOX-373: Replace a null entry with "?" so it is
comment|// not printed as "(null)"
name|c
operator|=
literal|"?"
expr_stmt|;
block|}
name|totalCharCnt
operator|+=
name|c
operator|.
name|length
argument_list|()
expr_stmt|;
name|stringResult
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|String
name|resultingString
init|=
name|stringResult
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|individualWidthsText
operator|.
name|length
operator|!=
name|resultingString
operator|.
name|length
argument_list|()
condition|)
block|{
name|float
index|[]
name|tmp
init|=
operator|new
name|float
index|[
name|resultingString
operator|.
name|length
argument_list|()
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|individualWidthsText
argument_list|,
literal|0
argument_list|,
name|tmp
argument_list|,
literal|0
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|individualWidthsText
operator|.
name|length
argument_list|,
name|resultingString
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|individualWidthsText
operator|=
name|tmp
expr_stmt|;
if|if
condition|(
name|resultingString
operator|.
name|equals
argument_list|(
literal|"- "
argument_list|)
condition|)
block|{
comment|//System.out.println( "EQUALS " + individualWidths[0] );
block|}
block|}
name|float
name|totalVerticalDisplacementDisp
init|=
name|maxVerticalDisplacementText
operator|*
name|fontSizeText
operator|*
name|yScaleDisp
decl_stmt|;
comment|// convert textMatrix at the end of the string to display units
name|Matrix
name|textMatrixEndDisp
init|=
name|initialMatrix
operator|.
name|multiply
argument_list|(
name|textMatrix
argument_list|)
operator|.
name|multiply
argument_list|(
name|ctm
argument_list|)
decl_stmt|;
comment|// process the decoded text
name|processTextPosition
argument_list|(
operator|new
name|TextPosition
argument_list|(
name|page
argument_list|,
name|textMatrixStDisp
argument_list|,
name|textMatrixEndDisp
argument_list|,
name|totalVerticalDisplacementDisp
argument_list|,
name|individualWidthsText
argument_list|,
name|spaceWidthDisp
argument_list|,
name|stringResult
operator|.
name|toString
argument_list|()
argument_list|,
name|font
argument_list|,
name|fontSizeText
argument_list|,
call|(
name|int
call|)
argument_list|(
name|fontSizeText
operator|*
name|textMatrix
operator|.
name|getXScale
argument_list|()
argument_list|)
argument_list|,
name|wordSpacingDisp
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This is used to handle an operation.      *      * @param operation The operation to perform.      * @param arguments The list of arguments.      *      * @throws IOException If there is an error processing the operation.      */
specifier|public
name|void
name|processOperator
parameter_list|(
name|String
name|operation
parameter_list|,
name|List
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|PDFOperator
name|oper
init|=
name|PDFOperator
operator|.
name|getOperator
argument_list|(
name|operation
argument_list|)
decl_stmt|;
name|processOperator
argument_list|(
name|oper
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This is used to handle an operation.      *      * @param operator The operation to perform.      * @param arguments The list of arguments.      *      * @throws IOException If there is an error processing the operation.      */
specifier|protected
name|void
name|processOperator
parameter_list|(
name|PDFOperator
name|operator
parameter_list|,
name|List
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|String
name|operation
init|=
name|operator
operator|.
name|getOperation
argument_list|()
decl_stmt|;
name|OperatorProcessor
name|processor
init|=
operator|(
name|OperatorProcessor
operator|)
name|operators
operator|.
name|get
argument_list|(
name|operation
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|setContext
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|operator
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|unsupportedOperators
operator|.
name|contains
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"unsupported/disabled operation: "
operator|+
name|operation
argument_list|)
expr_stmt|;
name|unsupportedOperators
operator|.
name|add
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @return Returns the colorSpaces.      */
specifier|public
name|Map
name|getColorSpaces
parameter_list|()
block|{
return|return
operator|(
operator|(
name|StreamResources
operator|)
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|)
operator|.
name|colorSpaces
return|;
block|}
comment|/**      * @return Returns the colorSpaces.      */
specifier|public
name|Map
name|getXObjects
parameter_list|()
block|{
return|return
operator|(
operator|(
name|StreamResources
operator|)
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|)
operator|.
name|xobjects
return|;
block|}
comment|/**      * @param value The colorSpaces to set.      */
specifier|public
name|void
name|setColorSpaces
parameter_list|(
name|Map
name|value
parameter_list|)
block|{
operator|(
operator|(
name|StreamResources
operator|)
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|)
operator|.
name|colorSpaces
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * @return Returns the fonts.      */
specifier|public
name|Map
name|getFonts
parameter_list|()
block|{
return|return
operator|(
operator|(
name|StreamResources
operator|)
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|)
operator|.
name|fonts
return|;
block|}
comment|/**      * @param value The fonts to set.      */
specifier|public
name|void
name|setFonts
parameter_list|(
name|Map
name|value
parameter_list|)
block|{
operator|(
operator|(
name|StreamResources
operator|)
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|)
operator|.
name|fonts
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * @return Returns the graphicsStack.      */
specifier|public
name|Stack
name|getGraphicsStack
parameter_list|()
block|{
return|return
name|graphicsStack
return|;
block|}
comment|/**      * @param value The graphicsStack to set.      */
specifier|public
name|void
name|setGraphicsStack
parameter_list|(
name|Stack
name|value
parameter_list|)
block|{
name|graphicsStack
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * @return Returns the graphicsState.      */
specifier|public
name|PDGraphicsState
name|getGraphicsState
parameter_list|()
block|{
return|return
name|graphicsState
return|;
block|}
comment|/**      * @param value The graphicsState to set.      */
specifier|public
name|void
name|setGraphicsState
parameter_list|(
name|PDGraphicsState
name|value
parameter_list|)
block|{
name|graphicsState
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * @return Returns the graphicsStates.      */
specifier|public
name|Map
name|getGraphicsStates
parameter_list|()
block|{
return|return
operator|(
operator|(
name|StreamResources
operator|)
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|)
operator|.
name|graphicsStates
return|;
block|}
comment|/**      * @param value The graphicsStates to set.      */
specifier|public
name|void
name|setGraphicsStates
parameter_list|(
name|Map
name|value
parameter_list|)
block|{
operator|(
operator|(
name|StreamResources
operator|)
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|)
operator|.
name|graphicsStates
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * @return Returns the textLineMatrix.      */
specifier|public
name|Matrix
name|getTextLineMatrix
parameter_list|()
block|{
return|return
name|textLineMatrix
return|;
block|}
comment|/**      * @param value The textLineMatrix to set.      */
specifier|public
name|void
name|setTextLineMatrix
parameter_list|(
name|Matrix
name|value
parameter_list|)
block|{
name|textLineMatrix
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * @return Returns the textMatrix.      */
specifier|public
name|Matrix
name|getTextMatrix
parameter_list|()
block|{
return|return
name|textMatrix
return|;
block|}
comment|/**      * @param value The textMatrix to set.      */
specifier|public
name|void
name|setTextMatrix
parameter_list|(
name|Matrix
name|value
parameter_list|)
block|{
name|textMatrix
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * @return Returns the resources.      */
specifier|public
name|PDResources
name|getResources
parameter_list|()
block|{
return|return
operator|(
operator|(
name|StreamResources
operator|)
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|)
operator|.
name|resources
return|;
block|}
comment|/**      * Get the current page that is being processed.      *      * @return The page being processed.      */
specifier|public
name|PDPage
name|getCurrentPage
parameter_list|()
block|{
return|return
name|page
return|;
block|}
comment|/**       * Get the total number of valid characters in the doc       * that could be decoded in processEncodedText().       * @return The number of valid characters.       */
specifier|public
name|int
name|getValidCharCnt
parameter_list|()
block|{
return|return
name|validCharCnt
return|;
block|}
comment|/**      * Get the total number of characters in the doc      * (including ones that could not be mapped).        * @return The number of characters.       */
specifier|public
name|int
name|getTotalCharCnt
parameter_list|()
block|{
return|return
name|totalCharCnt
return|;
block|}
block|}
end_class

end_unit

