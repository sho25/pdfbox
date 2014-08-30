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
name|io
operator|.
name|ByteArrayInputStream
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|HashSet
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
name|Set
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
name|COSBase
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
name|pdfparser
operator|.
name|PDFStreamParser
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
name|common
operator|.
name|PDRectangle
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
name|font
operator|.
name|PDFontFactory
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
name|form
operator|.
name|PDFormXObject
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
name|state
operator|.
name|PDExtendedGraphicsState
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
name|state
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
name|pdmodel
operator|.
name|graphics
operator|.
name|state
operator|.
name|PDTextState
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
name|Operator
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
comment|/**  * Processes a PDF content stream and executes certain operations.  * Provides a callback interface for clients that want to do things with the stream.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDFStreamEngine
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
name|PDFStreamEngine
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|unsupportedOperators
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|OperatorProcessor
argument_list|>
name|operators
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|OperatorProcessor
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Matrix
name|textMatrix
decl_stmt|;
specifier|private
name|Matrix
name|textLineMatrix
decl_stmt|;
specifier|protected
name|Matrix
name|subStreamMatrix
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Stack
argument_list|<
name|PDGraphicsState
argument_list|>
name|graphicsStack
init|=
operator|new
name|Stack
argument_list|<
name|PDGraphicsState
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Stack
argument_list|<
name|PDResources
argument_list|>
name|streamResourcesStack
init|=
operator|new
name|Stack
argument_list|<
name|PDResources
argument_list|>
argument_list|()
decl_stmt|;
comment|// skip malformed or otherwise unparseable input where possible
specifier|private
name|boolean
name|forceParsing
decl_stmt|;
comment|/**      * Creates a new PDFStreamEngine.      */
specifier|public
name|PDFStreamEngine
parameter_list|()
block|{     }
comment|/**      * Constructor with engine properties. The property keys are all PDF operators, the values are      * class names used to execute those operators. An empty value means that the operator will be      * silently ignored.      *       * @param properties The engine properties.      */
specifier|public
name|PDFStreamEngine
parameter_list|(
name|Properties
name|properties
parameter_list|)
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
name|Enumeration
argument_list|<
name|?
argument_list|>
name|names
init|=
name|properties
operator|.
name|propertyNames
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|name
range|:
name|Collections
operator|.
name|list
argument_list|(
name|names
argument_list|)
control|)
block|{
name|String
name|operator
init|=
name|name
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|processorClassName
init|=
name|properties
operator|.
name|getProperty
argument_list|(
name|operator
argument_list|)
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|processorClassName
argument_list|)
condition|)
block|{
name|unsupportedOperators
operator|.
name|add
argument_list|(
name|operator
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|Class
operator|.
name|forName
argument_list|(
name|processorClassName
argument_list|)
decl_stmt|;
name|OperatorProcessor
name|processor
init|=
operator|(
name|OperatorProcessor
operator|)
name|cls
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|registerOperatorProcessor
argument_list|(
name|operator
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
comment|// should not happen
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
comment|// should not happen
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
comment|// should not happen
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
comment|/**      * Indicates if force parsing is activated.      *       * @return true if force parsing is active      */
specifier|public
name|boolean
name|isForceParsing
parameter_list|()
block|{
return|return
name|forceParsing
return|;
block|}
comment|/**      * Enable/Disable force parsing.      *       * @param forceParsingValue true activates force parsing      */
specifier|public
name|void
name|setForceParsing
parameter_list|(
name|boolean
name|forceParsingValue
parameter_list|)
block|{
name|forceParsing
operator|=
name|forceParsingValue
expr_stmt|;
block|}
comment|/**      * Register a custom operator processor with the engine.      *       * @param operator The operator as a string.      * @param op Processor instance.      */
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
comment|/**      * Initialises a stream for processing.      *      * @param drawingSize the size of the page      */
specifier|protected
name|void
name|initStream
parameter_list|(
name|PDRectangle
name|drawingSize
parameter_list|)
block|{
name|graphicsStack
operator|.
name|clear
argument_list|()
expr_stmt|;
name|graphicsStack
operator|.
name|push
argument_list|(
operator|new
name|PDGraphicsState
argument_list|(
name|drawingSize
argument_list|)
argument_list|)
expr_stmt|;
name|textMatrix
operator|=
literal|null
expr_stmt|;
name|textLineMatrix
operator|=
literal|null
expr_stmt|;
name|streamResourcesStack
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will initialise and process the contents of the stream.      *       * @param resources the location to retrieve resources      * @param cosStream the Stream to execute      * @param drawingSize the size of the page      * @throws IOException if there is an error accessing the stream      */
specifier|public
name|void
name|processStream
parameter_list|(
name|PDResources
name|resources
parameter_list|,
name|COSStream
name|cosStream
parameter_list|,
name|PDRectangle
name|drawingSize
parameter_list|)
throws|throws
name|IOException
block|{
name|initStream
argument_list|(
name|drawingSize
argument_list|)
expr_stmt|;
name|processSubStream
argument_list|(
name|resources
argument_list|,
name|cosStream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Shows a form from the content stream.      *      * @param form form XObject      * @throws IOException if the form cannot be processed      */
specifier|public
name|void
name|showForm
parameter_list|(
name|PDFormXObject
name|form
parameter_list|)
throws|throws
name|IOException
block|{
name|processSubStream
argument_list|(
name|form
operator|.
name|getResources
argument_list|()
argument_list|,
name|form
operator|.
name|getCOSStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Shows a transparency group from the content stream.      *      * @param form transparency group (form) XObject      * @throws IOException if the transparency group cannot be processed      */
specifier|public
name|void
name|showTransparencyGroup
parameter_list|(
name|PDFormXObject
name|form
parameter_list|)
throws|throws
name|IOException
block|{
name|showForm
argument_list|(
name|form
argument_list|)
expr_stmt|;
block|}
comment|/**      * Process a sub stream of the current stream.      *       * @param resources the resources used when processing the stream      * @param cosStream the stream to process      * @throws IOException if there is an exception while processing the stream      */
specifier|public
name|void
name|processSubStream
parameter_list|(
name|PDResources
name|resources
parameter_list|,
name|COSStream
name|cosStream
parameter_list|)
throws|throws
name|IOException
block|{
comment|// sanity check
if|if
condition|(
name|graphicsStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Call to processSubStream() before processStream() "
operator|+
literal|"or initStream()"
argument_list|)
throw|;
block|}
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|streamResourcesStack
operator|.
name|push
argument_list|(
name|resources
argument_list|)
expr_stmt|;
try|try
block|{
name|processSubStream
argument_list|(
name|cosStream
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|streamResourcesStack
operator|.
name|pop
argument_list|()
operator|.
name|clearCache
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|processSubStream
argument_list|(
name|cosStream
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|processSubStream
parameter_list|(
name|COSStream
name|cosStream
parameter_list|)
throws|throws
name|IOException
block|{
name|Matrix
name|oldSubStreamMatrix
init|=
name|subStreamMatrix
decl_stmt|;
name|subStreamMatrix
operator|=
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentTransformationMatrix
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
init|=
operator|new
name|ArrayList
argument_list|<
name|COSBase
argument_list|>
argument_list|()
decl_stmt|;
name|PDFStreamParser
name|parser
init|=
operator|new
name|PDFStreamParser
argument_list|(
name|cosStream
argument_list|,
name|forceParsing
argument_list|)
decl_stmt|;
try|try
block|{
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
init|=
name|parser
operator|.
name|getTokenIterator
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
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"processing substream token: "
operator|+
name|next
argument_list|)
expr_stmt|;
block|}
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
name|Operator
condition|)
block|{
name|processOperator
argument_list|(
operator|(
name|Operator
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
argument_list|<
name|COSBase
argument_list|>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|arguments
operator|.
name|add
argument_list|(
operator|(
name|COSBase
operator|)
name|next
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|parser
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|subStreamMatrix
operator|=
name|oldSubStreamMatrix
expr_stmt|;
block|}
comment|/**      * Called when the BT operator is encountered. This method is for overriding in subclasses, the      * default implementation does nothing.      *      * @throws IOException if there was an error processing the text      */
specifier|public
name|void
name|beginText
parameter_list|()
throws|throws
name|IOException
block|{
comment|// overridden in subclasses
block|}
comment|/**      * Called when the ET operator is encountered. This method is for overriding in subclasses, the      * default implementation does nothing.      *      * @throws IOException if there was an error processing the text      */
specifier|public
name|void
name|endText
parameter_list|()
throws|throws
name|IOException
block|{
comment|// overridden in subclasses
block|}
comment|/**      * Called when a string of text is to be shown.      *      * @param string the encoded text      * @throws IOException if there was an error showing the text      */
specifier|public
name|void
name|showText
parameter_list|(
name|byte
index|[]
name|string
parameter_list|)
throws|throws
name|IOException
block|{
name|showText
argument_list|(
name|string
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called when a string of text with spacing adjustments is to be shown.      *      * @param strings list of the encoded text      * @param adjustments spacing adjustment for each string      * @throws IOException if there was an error showing the text      */
specifier|public
name|void
name|showAdjustedText
parameter_list|(
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|strings
parameter_list|,
name|List
argument_list|<
name|Float
argument_list|>
name|adjustments
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|,
name|len
init|=
name|strings
operator|.
name|size
argument_list|()
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|showText
argument_list|(
name|strings
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|adjustments
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Process text from the PDF Stream. You should override this method if you want to      * perform an action when encoded text is being processed.      *      * @param string the encoded text      * @param adjustment a position adjustment from a TJ array to be applied after the glyph      * @throws IOException if there is an error processing the string      */
specifier|protected
name|void
name|showText
parameter_list|(
name|byte
index|[]
name|string
parameter_list|,
name|float
name|adjustment
parameter_list|)
throws|throws
name|IOException
block|{
name|PDGraphicsState
name|state
init|=
name|getGraphicsState
argument_list|()
decl_stmt|;
name|PDTextState
name|textState
init|=
name|state
operator|.
name|getTextState
argument_list|()
decl_stmt|;
comment|// get the current font
name|PDFont
name|font
init|=
name|textState
operator|.
name|getFont
argument_list|()
decl_stmt|;
if|if
condition|(
name|font
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No current font, will use default"
argument_list|)
expr_stmt|;
name|font
operator|=
name|PDFontFactory
operator|.
name|createDefaultFont
argument_list|()
expr_stmt|;
block|}
name|float
name|fontSize
init|=
name|textState
operator|.
name|getFontSize
argument_list|()
decl_stmt|;
name|float
name|horizontalScaling
init|=
name|textState
operator|.
name|getHorizontalScaling
argument_list|()
operator|/
literal|100f
decl_stmt|;
name|float
name|charSpacing
init|=
name|textState
operator|.
name|getCharacterSpacing
argument_list|()
decl_stmt|;
comment|// put the text state parameters into matrix form
name|Matrix
name|parameters
init|=
operator|new
name|Matrix
argument_list|(
name|fontSize
operator|*
name|horizontalScaling
argument_list|,
literal|0
argument_list|,
comment|// 0
literal|0
argument_list|,
name|fontSize
argument_list|,
comment|// 0
literal|0
argument_list|,
name|textState
operator|.
name|getRise
argument_list|()
argument_list|)
decl_stmt|;
comment|// 1
comment|// read the stream until it is empty
name|InputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|string
argument_list|)
decl_stmt|;
while|while
condition|(
name|in
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// decode a character
name|int
name|before
init|=
name|in
operator|.
name|available
argument_list|()
decl_stmt|;
name|int
name|code
init|=
name|font
operator|.
name|readCode
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|int
name|codeLength
init|=
name|before
operator|-
name|in
operator|.
name|available
argument_list|()
decl_stmt|;
name|String
name|unicode
init|=
name|font
operator|.
name|toUnicode
argument_list|(
name|code
argument_list|)
decl_stmt|;
comment|// Word spacing shall be applied to every occurrence of the single-byte character code
comment|// 32 in a string when using a simple font or a composite font that defines code 32 as
comment|// a single-byte code.
name|float
name|wordSpacing
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|codeLength
operator|==
literal|1
condition|)
block|{
if|if
condition|(
name|code
operator|==
literal|32
condition|)
block|{
name|wordSpacing
operator|+=
name|textState
operator|.
name|getWordSpacing
argument_list|()
expr_stmt|;
block|}
block|}
comment|// text rendering matrix (text space -> device space)
name|Matrix
name|ctm
init|=
name|state
operator|.
name|getCurrentTransformationMatrix
argument_list|()
decl_stmt|;
name|Matrix
name|textRenderingMatrix
init|=
name|parameters
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
comment|// get glyph's position vector if this is vertical text
comment|// changes to vertical text should be tested with PDFBOX-2294 and PDFBOX-1422
if|if
condition|(
name|font
operator|.
name|isVertical
argument_list|()
condition|)
block|{
comment|// position vector, in text space
name|Vector
name|v
init|=
name|font
operator|.
name|getPositionVector
argument_list|(
name|code
argument_list|)
decl_stmt|;
comment|// apply the position vector to the horizontal origin to get the vertical origin
name|textRenderingMatrix
operator|.
name|translate
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
comment|// get glyph's horizontal and vertical displacements, in text space
name|Vector
name|w
init|=
name|font
operator|.
name|getDisplacement
argument_list|(
name|code
argument_list|)
decl_stmt|;
comment|// process the decoded glyph
name|showGlyph
argument_list|(
name|textRenderingMatrix
argument_list|,
name|font
argument_list|,
name|code
argument_list|,
name|unicode
argument_list|,
name|w
argument_list|)
expr_stmt|;
comment|// TJ adjustment after final glyph
name|float
name|tj
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|in
operator|.
name|available
argument_list|()
operator|==
literal|0
condition|)
block|{
name|tj
operator|=
name|adjustment
expr_stmt|;
block|}
comment|// calculate the combined displacements
name|float
name|tx
decl_stmt|,
name|ty
decl_stmt|;
if|if
condition|(
name|font
operator|.
name|isVertical
argument_list|()
condition|)
block|{
name|tx
operator|=
literal|0
expr_stmt|;
name|ty
operator|=
operator|(
name|w
operator|.
name|getY
argument_list|()
operator|-
name|tj
operator|/
literal|1000
operator|)
operator|*
name|fontSize
operator|+
name|charSpacing
operator|+
name|wordSpacing
expr_stmt|;
block|}
else|else
block|{
name|tx
operator|=
operator|(
operator|(
name|w
operator|.
name|getX
argument_list|()
operator|-
name|tj
operator|/
literal|1000
operator|)
operator|*
name|fontSize
operator|+
name|charSpacing
operator|+
name|wordSpacing
operator|)
operator|*
name|horizontalScaling
expr_stmt|;
name|ty
operator|=
literal|0
expr_stmt|;
block|}
comment|// update the text matrix
name|textMatrix
operator|.
name|concatenate
argument_list|(
name|Matrix
operator|.
name|getTranslatingInstance
argument_list|(
name|tx
argument_list|,
name|ty
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Called when a glyph is to be processed.This method is intended for overriding in subclasses,      * the default implementation does nothing.      *      * @param textRenderingMatrix the current text rendering matrix, T<sub>rm</sub>      * @param font the current font      * @param code internal PDF character code for the glyph      * @param unicode the Unicode text for this glyph, or null if the PDF does provide it      * @param displacement the displacement (i.e. advance) of the glyph in text space      * @throws IOException if the glyph cannot be processed      */
specifier|protected
name|void
name|showGlyph
parameter_list|(
name|Matrix
name|textRenderingMatrix
parameter_list|,
name|PDFont
name|font
parameter_list|,
name|int
name|code
parameter_list|,
name|String
name|unicode
parameter_list|,
name|Vector
name|displacement
parameter_list|)
throws|throws
name|IOException
block|{
comment|// overridden in subclasses
block|}
comment|/**      * This is used to handle an operation.      *       * @param operation The operation to perform.      * @param arguments The list of arguments.      * @throws IOException If there is an error processing the operation.      */
specifier|public
name|void
name|processOperator
parameter_list|(
name|String
name|operation
parameter_list|,
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|Operator
name|operator
init|=
name|Operator
operator|.
name|getOperator
argument_list|(
name|operation
argument_list|)
decl_stmt|;
name|processOperator
argument_list|(
name|operator
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
name|LOG
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
comment|/**      * This is used to handle an operation.      *       * @param operator The operation to perform.      * @param arguments The list of arguments.      * @throws IOException If there is an error processing the operation.      */
specifier|protected
name|void
name|processOperator
parameter_list|(
name|Operator
name|operator
parameter_list|,
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
parameter_list|)
throws|throws
name|IOException
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"unsupported/disabled operation: "
operator|+
name|operation
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * @return Returns the XObjects.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
name|getXObjects
parameter_list|()
block|{
return|return
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|.
name|getXObjects
argument_list|()
return|;
block|}
comment|/**      * @return Returns the fonts.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|getFonts
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|streamResourcesStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
return|return
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|.
name|getFonts
argument_list|()
return|;
block|}
comment|/**      * @param value The fonts to set.      */
specifier|public
name|void
name|setFonts
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|value
parameter_list|)
block|{
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|.
name|setFonts
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pushes the current graphics state to the stack.      */
specifier|public
name|void
name|saveGraphicsState
parameter_list|()
block|{
name|graphicsStack
operator|.
name|push
argument_list|(
name|graphicsStack
operator|.
name|peek
argument_list|()
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pops the current graphics state from the stack.      */
specifier|public
name|void
name|restoreGraphicsState
parameter_list|()
block|{
name|graphicsStack
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return Returns the size of the graphicsStack.      */
specifier|public
name|int
name|getGraphicsStackSize
parameter_list|()
block|{
return|return
name|graphicsStack
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * @return Returns the graphicsState.      */
specifier|public
name|PDGraphicsState
name|getGraphicsState
parameter_list|()
block|{
return|return
name|graphicsStack
operator|.
name|peek
argument_list|()
return|;
block|}
comment|/**      * @return Returns the graphicsStates.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDExtendedGraphicsState
argument_list|>
name|getGraphicsStates
parameter_list|()
block|{
return|return
name|streamResourcesStack
operator|.
name|peek
argument_list|()
operator|.
name|getGraphicsStates
argument_list|()
return|;
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
comment|/**      * @return Returns the subStreamMatrix.      */
specifier|protected
name|Matrix
name|getSubStreamMatrix
parameter_list|()
block|{
return|return
name|subStreamMatrix
return|;
block|}
comment|/**      * @return Returns the resources.      */
specifier|public
name|PDResources
name|getResources
parameter_list|()
block|{
return|return
name|streamResourcesStack
operator|.
name|peek
argument_list|()
return|;
block|}
comment|/**      * use the current transformation matrix to transformPoint a single point.      *      * @param x x-coordinate of the point to be transformPoint      * @param y y-coordinate of the point to be transformPoint      * @return the transformed coordinates as Point2D.Double      */
specifier|public
name|Point2D
operator|.
name|Double
name|transformedPoint
parameter_list|(
name|double
name|x
parameter_list|,
name|double
name|y
parameter_list|)
block|{
name|double
index|[]
name|position
init|=
block|{
name|x
block|,
name|y
block|}
decl_stmt|;
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentTransformationMatrix
argument_list|()
operator|.
name|createAffineTransform
argument_list|()
operator|.
name|transform
argument_list|(
name|position
argument_list|,
literal|0
argument_list|,
name|position
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|)
expr_stmt|;
return|return
operator|new
name|Point2D
operator|.
name|Double
argument_list|(
name|position
index|[
literal|0
index|]
argument_list|,
name|position
index|[
literal|1
index|]
argument_list|)
return|;
block|}
comment|/**      * use the current transformation matrix to transformPoint a PDRectangle.      *       * @param rect the PDRectangle to transformPoint      * @return the transformed coordinates as a GeneralPath      */
specifier|public
name|GeneralPath
name|transformedPDRectanglePath
parameter_list|(
name|PDRectangle
name|rect
parameter_list|)
block|{
name|float
name|x1
init|=
name|rect
operator|.
name|getLowerLeftX
argument_list|()
decl_stmt|;
name|float
name|y1
init|=
name|rect
operator|.
name|getLowerLeftY
argument_list|()
decl_stmt|;
name|float
name|x2
init|=
name|rect
operator|.
name|getUpperRightX
argument_list|()
decl_stmt|;
name|float
name|y2
init|=
name|rect
operator|.
name|getUpperRightY
argument_list|()
decl_stmt|;
name|Point2D
name|p0
init|=
name|transformedPoint
argument_list|(
name|x1
argument_list|,
name|y1
argument_list|)
decl_stmt|;
name|Point2D
name|p1
init|=
name|transformedPoint
argument_list|(
name|x2
argument_list|,
name|y1
argument_list|)
decl_stmt|;
name|Point2D
name|p2
init|=
name|transformedPoint
argument_list|(
name|x2
argument_list|,
name|y2
argument_list|)
decl_stmt|;
name|Point2D
name|p3
init|=
name|transformedPoint
argument_list|(
name|x1
argument_list|,
name|y2
argument_list|)
decl_stmt|;
name|GeneralPath
name|path
init|=
operator|new
name|GeneralPath
argument_list|()
decl_stmt|;
name|path
operator|.
name|moveTo
argument_list|(
operator|(
name|float
operator|)
name|p0
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|p0
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|path
operator|.
name|lineTo
argument_list|(
operator|(
name|float
operator|)
name|p1
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|p1
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|path
operator|.
name|lineTo
argument_list|(
operator|(
name|float
operator|)
name|p2
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|p2
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|path
operator|.
name|lineTo
argument_list|(
operator|(
name|float
operator|)
name|p3
operator|.
name|getX
argument_list|()
argument_list|,
operator|(
name|float
operator|)
name|p3
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
name|path
operator|.
name|closePath
argument_list|()
expr_stmt|;
return|return
name|path
return|;
block|}
comment|// transforms a width using the CTM
specifier|protected
name|float
name|transformWidth
parameter_list|(
name|float
name|width
parameter_list|)
block|{
name|Matrix
name|ctm
init|=
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentTransformationMatrix
argument_list|()
decl_stmt|;
name|float
name|x
init|=
name|ctm
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
operator|+
name|ctm
operator|.
name|getValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|float
name|y
init|=
name|ctm
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|+
name|ctm
operator|.
name|getValue
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
return|return
name|width
operator|*
operator|(
name|float
operator|)
name|Math
operator|.
name|sqrt
argument_list|(
operator|(
name|x
operator|*
name|x
operator|+
name|y
operator|*
name|y
operator|)
operator|*
literal|0.5
argument_list|)
return|;
block|}
block|}
end_class

end_unit

