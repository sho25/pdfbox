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
name|ByteArrayOutputStream
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
name|OutputStream
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
name|contentstream
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
name|pdfwriter
operator|.
name|ContentStreamWriter
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
name|PDPageContentStream
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
name|interactive
operator|.
name|action
operator|.
name|PDFormFieldAdditionalActions
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
name|PDAnnotationWidget
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
name|PDAppearanceDictionary
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
name|PDAppearanceEntry
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
name|PDAppearanceStream
import|;
end_import

begin_comment
comment|/**  * Create the AcroForms field appearance helper.  *   * @author Stephan Gerhard  * @author Ben Litchfield  */
end_comment

begin_class
class|class
name|AppearanceGeneratorHelper
block|{
specifier|private
specifier|static
specifier|final
name|Operator
name|BMC
init|=
name|Operator
operator|.
name|getOperator
argument_list|(
literal|"BMC"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Operator
name|EMC
init|=
name|Operator
operator|.
name|getOperator
argument_list|(
literal|"EMC"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|PDVariableText
name|field
decl_stmt|;
specifier|private
specifier|final
name|PDAppearanceString
name|defaultAppearance
decl_stmt|;
specifier|private
name|String
name|value
decl_stmt|;
comment|/**      * Constructs a COSAppearance from the given field.      *      * @param field the field which you wish to control the appearance of      * @throws IOException       */
name|AppearanceGeneratorHelper
parameter_list|(
name|PDVariableText
name|field
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|field
operator|=
name|field
expr_stmt|;
name|this
operator|.
name|defaultAppearance
operator|=
name|field
operator|.
name|getDefaultAppearanceString
argument_list|()
expr_stmt|;
block|}
comment|/**      * This is the public method for setting the appearance stream.      *      * @param apValue the String value which the appearance should represent      * @throws IOException If there is an error creating the stream.      */
specifier|public
name|void
name|setAppearanceValue
parameter_list|(
name|String
name|apValue
parameter_list|)
throws|throws
name|IOException
block|{
name|value
operator|=
name|apValue
expr_stmt|;
for|for
control|(
name|PDAnnotationWidget
name|widget
range|:
name|field
operator|.
name|getWidgets
argument_list|()
control|)
block|{
name|PDFormFieldAdditionalActions
name|actions
init|=
name|field
operator|.
name|getActions
argument_list|()
decl_stmt|;
comment|// in case all tests fail the field will be formatted by acrobat
comment|// when it is opened. See FreedomExpressions.pdf for an example of this.
if|if
condition|(
name|actions
operator|==
literal|null
operator|||
name|actions
operator|.
name|getF
argument_list|()
operator|==
literal|null
operator|||
name|widget
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AP
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|PDAppearanceDictionary
name|appearanceDict
init|=
name|widget
operator|.
name|getAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|appearanceDict
operator|==
literal|null
condition|)
block|{
name|appearanceDict
operator|=
operator|new
name|PDAppearanceDictionary
argument_list|()
expr_stmt|;
name|widget
operator|.
name|setAppearance
argument_list|(
name|appearanceDict
argument_list|)
expr_stmt|;
block|}
name|PDAppearanceEntry
name|appearance
init|=
name|appearanceDict
operator|.
name|getNormalAppearance
argument_list|()
decl_stmt|;
comment|// TODO support appearances other than "normal"
name|PDAppearanceStream
name|appearanceStream
decl_stmt|;
if|if
condition|(
name|appearance
operator|.
name|isStream
argument_list|()
condition|)
block|{
name|appearanceStream
operator|=
name|appearance
operator|.
name|getAppearanceStream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|appearanceStream
operator|=
operator|new
name|PDAppearanceStream
argument_list|(
name|field
operator|.
name|getAcroForm
argument_list|()
operator|.
name|getDocument
argument_list|()
argument_list|)
expr_stmt|;
name|appearanceStream
operator|.
name|setBBox
argument_list|(
name|widget
operator|.
name|getRectangle
argument_list|()
operator|.
name|createRetranslatedRectangle
argument_list|()
argument_list|)
expr_stmt|;
name|appearanceDict
operator|.
name|setNormalAppearance
argument_list|(
name|appearanceStream
argument_list|)
expr_stmt|;
comment|// TODO support appearances other than "normal"
block|}
name|setAppearanceContent
argument_list|(
name|widget
argument_list|,
name|appearanceStream
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Parses an appearance stream into tokens.      */
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|tokenize
parameter_list|(
name|PDAppearanceStream
name|appearanceStream
parameter_list|)
throws|throws
name|IOException
block|{
name|COSStream
name|stream
init|=
name|appearanceStream
operator|.
name|getCOSStream
argument_list|()
decl_stmt|;
name|PDFStreamParser
name|parser
init|=
operator|new
name|PDFStreamParser
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|tokens
init|=
name|parser
operator|.
name|getTokens
argument_list|()
decl_stmt|;
return|return
name|tokens
return|;
block|}
comment|/**      * Constructs and sets new contents for given appearance stream.      */
specifier|private
name|void
name|setAppearanceContent
parameter_list|(
name|PDAnnotationWidget
name|widget
parameter_list|,
name|PDAppearanceStream
name|appearanceStream
parameter_list|)
throws|throws
name|IOException
block|{
comment|// first copy any needed resources from the document’s DR dictionary into
comment|// the stream’s Resources dictionary
name|defaultAppearance
operator|.
name|copyNeededResourcesTo
argument_list|(
name|appearanceStream
argument_list|)
expr_stmt|;
comment|// then replace the existing contents of the appearance stream from /Tx BMC
comment|// to the matching EMC
name|ByteArrayOutputStream
name|output
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ContentStreamWriter
name|writer
init|=
operator|new
name|ContentStreamWriter
argument_list|(
name|output
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|tokens
init|=
name|tokenize
argument_list|(
name|appearanceStream
argument_list|)
decl_stmt|;
name|int
name|bmcIndex
init|=
name|tokens
operator|.
name|indexOf
argument_list|(
name|Operator
operator|.
name|getOperator
argument_list|(
literal|"BMC"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|bmcIndex
operator|==
operator|-
literal|1
condition|)
block|{
comment|// append to existing stream
name|writer
operator|.
name|writeTokens
argument_list|(
name|tokens
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeTokens
argument_list|(
name|COSName
operator|.
name|TX
argument_list|,
name|BMC
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// prepend content before BMC
name|writer
operator|.
name|writeTokens
argument_list|(
name|tokens
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|bmcIndex
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// insert field contents
name|insertGeneratedAppearance
argument_list|(
name|widget
argument_list|,
name|appearanceStream
argument_list|,
name|output
argument_list|)
expr_stmt|;
name|int
name|emcIndex
init|=
name|tokens
operator|.
name|indexOf
argument_list|(
name|Operator
operator|.
name|getOperator
argument_list|(
literal|"EMC"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|emcIndex
operator|==
operator|-
literal|1
condition|)
block|{
comment|// append EMC
name|writer
operator|.
name|writeTokens
argument_list|(
name|EMC
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// append contents after EMC
name|writer
operator|.
name|writeTokens
argument_list|(
name|tokens
operator|.
name|subList
argument_list|(
name|emcIndex
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
name|writeToStream
argument_list|(
name|output
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|appearanceStream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Generate and insert text content and clipping around it.         */
specifier|private
name|void
name|insertGeneratedAppearance
parameter_list|(
name|PDAnnotationWidget
name|widget
parameter_list|,
name|PDAppearanceStream
name|appearanceStream
parameter_list|,
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|PDPageContentStream
name|contents
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|field
operator|.
name|getAcroForm
argument_list|()
operator|.
name|getDocument
argument_list|()
argument_list|,
name|appearanceStream
argument_list|,
name|output
argument_list|)
decl_stmt|;
comment|// Acrobat calculates the left and right padding dependent on the offset of the border edge
comment|// This calculation works for forms having been generated by Acrobat.
comment|// The minimum distance is always 1f even if there is no rectangle being drawn around.
name|float
name|borderWidth
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|widget
operator|.
name|getBorderStyle
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|borderWidth
operator|=
name|widget
operator|.
name|getBorderStyle
argument_list|()
operator|.
name|getWidth
argument_list|()
expr_stmt|;
block|}
name|PDRectangle
name|bbox
init|=
name|resolveBoundingBox
argument_list|(
name|widget
argument_list|,
name|appearanceStream
argument_list|)
decl_stmt|;
name|PDRectangle
name|clipRect
init|=
name|applyPadding
argument_list|(
name|bbox
argument_list|,
name|Math
operator|.
name|max
argument_list|(
literal|1f
argument_list|,
name|borderWidth
argument_list|)
argument_list|)
decl_stmt|;
name|PDRectangle
name|contentRect
init|=
name|applyPadding
argument_list|(
name|clipRect
argument_list|,
name|Math
operator|.
name|max
argument_list|(
literal|1f
argument_list|,
name|borderWidth
argument_list|)
argument_list|)
decl_stmt|;
name|contents
operator|.
name|saveGraphicsState
argument_list|()
expr_stmt|;
comment|// add a clipping path to avoid overlapping with the border
if|if
condition|(
name|borderWidth
operator|>
literal|0
condition|)
block|{
name|contents
operator|.
name|addRect
argument_list|(
name|clipRect
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|clipRect
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|clipRect
operator|.
name|getWidth
argument_list|()
argument_list|,
name|clipRect
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|contents
operator|.
name|clip
argument_list|()
expr_stmt|;
block|}
comment|// start the text output
name|contents
operator|.
name|beginText
argument_list|()
expr_stmt|;
comment|// get the font
name|PDFont
name|font
init|=
name|field
operator|.
name|getDefaultAppearanceString
argument_list|()
operator|.
name|getFont
argument_list|()
decl_stmt|;
comment|// calculate the fontSize (because 0 = autosize)
name|float
name|fontSize
init|=
name|calculateFontSize
argument_list|(
name|font
argument_list|,
name|contentRect
argument_list|)
decl_stmt|;
comment|// write the /DA string
name|field
operator|.
name|getDefaultAppearanceString
argument_list|()
operator|.
name|writeTo
argument_list|(
name|contents
argument_list|,
name|fontSize
argument_list|)
expr_stmt|;
comment|// calculate the y-position of the baseline
name|float
name|y
decl_stmt|;
if|if
condition|(
name|field
operator|instanceof
name|PDTextField
operator|&&
operator|(
operator|(
name|PDTextField
operator|)
name|field
operator|)
operator|.
name|isMultiline
argument_list|()
condition|)
block|{
name|float
name|height
init|=
name|font
operator|.
name|getBoundingBox
argument_list|()
operator|.
name|getHeight
argument_list|()
operator|/
literal|1000
operator|*
name|fontSize
decl_stmt|;
name|y
operator|=
name|contentRect
operator|.
name|getUpperRightY
argument_list|()
operator|-
name|height
expr_stmt|;
block|}
else|else
block|{
name|float
name|minY
init|=
name|font
operator|.
name|getBoundingBox
argument_list|()
operator|.
name|getLowerLeftY
argument_list|()
operator|/
literal|1000
operator|*
name|fontSize
decl_stmt|;
name|y
operator|=
name|Math
operator|.
name|max
argument_list|(
name|bbox
operator|.
name|getHeight
argument_list|()
operator|/
literal|2f
operator|+
name|minY
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|// show the text
name|float
name|x
init|=
name|contentRect
operator|.
name|getLowerLeftX
argument_list|()
decl_stmt|;
name|PlainText
name|textContent
init|=
operator|new
name|PlainText
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|AppearanceStyle
name|appearanceStyle
init|=
operator|new
name|AppearanceStyle
argument_list|()
decl_stmt|;
name|appearanceStyle
operator|.
name|setFont
argument_list|(
name|font
argument_list|)
expr_stmt|;
name|appearanceStyle
operator|.
name|setFontSize
argument_list|(
name|fontSize
argument_list|)
expr_stmt|;
comment|// Adobe Acrobat uses the font's bounding box for the leading between the lines
name|appearanceStyle
operator|.
name|setLeading
argument_list|(
name|font
operator|.
name|getBoundingBox
argument_list|()
operator|.
name|getHeight
argument_list|()
operator|/
literal|1000
operator|*
name|fontSize
argument_list|)
expr_stmt|;
name|PlainTextFormatter
name|formatter
init|=
operator|new
name|PlainTextFormatter
operator|.
name|Builder
argument_list|(
name|contents
argument_list|)
operator|.
name|style
argument_list|(
name|appearanceStyle
argument_list|)
operator|.
name|text
argument_list|(
name|textContent
argument_list|)
operator|.
name|width
argument_list|(
name|contentRect
operator|.
name|getWidth
argument_list|()
argument_list|)
operator|.
name|wrapLines
argument_list|(
literal|true
argument_list|)
operator|.
name|initialOffset
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
operator|.
name|textAlign
argument_list|(
name|field
operator|.
name|getQ
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|formatter
operator|.
name|format
argument_list|()
expr_stmt|;
name|contents
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contents
operator|.
name|restoreGraphicsState
argument_list|()
expr_stmt|;
name|contents
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|boolean
name|isMultiLine
parameter_list|()
block|{
return|return
name|field
operator|instanceof
name|PDTextField
operator|&&
operator|(
operator|(
name|PDTextField
operator|)
name|field
operator|)
operator|.
name|isMultiline
argument_list|()
return|;
block|}
comment|/**      * Writes the stream to the actual stream in the COSStream.      *      * @throws IOException If there is an error writing to the stream      */
specifier|private
name|void
name|writeToStream
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|PDAppearanceStream
name|appearanceStream
parameter_list|)
throws|throws
name|IOException
block|{
name|OutputStream
name|out
init|=
name|appearanceStream
operator|.
name|getCOSStream
argument_list|()
operator|.
name|createUnfilteredStream
argument_list|()
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * My "not so great" method for calculating the fontsize. It does not work superb, but it      * handles ok.      *       * @return the calculated font-size      * @throws IOException If there is an error getting the font information.      */
specifier|private
name|float
name|calculateFontSize
parameter_list|(
name|PDFont
name|font
parameter_list|,
name|PDRectangle
name|contentRect
parameter_list|)
throws|throws
name|IOException
block|{
name|float
name|fontSize
init|=
name|defaultAppearance
operator|.
name|getFontSize
argument_list|()
decl_stmt|;
comment|// zero is special, it means the text is auto-sized
if|if
condition|(
name|fontSize
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|isMultiLine
argument_list|()
condition|)
block|{
comment|// Acrobat defaults to 12 for multiline text with size 0
return|return
literal|12f
return|;
block|}
else|else
block|{
comment|// fit width
name|float
name|width
init|=
name|font
operator|.
name|getStringWidth
argument_list|(
name|value
argument_list|)
operator|/
literal|1000
decl_stmt|;
name|float
name|widthBasedFontSize
init|=
name|contentRect
operator|.
name|getWidth
argument_list|()
operator|/
name|width
decl_stmt|;
comment|// fit height
name|float
name|height
init|=
operator|(
name|font
operator|.
name|getFontDescriptor
argument_list|()
operator|.
name|getAscent
argument_list|()
operator|+
operator|-
name|font
operator|.
name|getFontDescriptor
argument_list|()
operator|.
name|getDescent
argument_list|()
operator|)
operator|/
literal|1000
decl_stmt|;
if|if
condition|(
name|height
operator|<=
literal|0
condition|)
block|{
name|height
operator|=
name|font
operator|.
name|getBoundingBox
argument_list|()
operator|.
name|getHeight
argument_list|()
operator|/
literal|1000
expr_stmt|;
block|}
name|float
name|heightBasedFontSize
init|=
name|contentRect
operator|.
name|getHeight
argument_list|()
operator|/
name|height
decl_stmt|;
return|return
name|Math
operator|.
name|min
argument_list|(
name|heightBasedFontSize
argument_list|,
name|widthBasedFontSize
argument_list|)
return|;
block|}
block|}
return|return
name|fontSize
return|;
block|}
comment|/**      * Resolve the bounding box.      *       * @param fieldWidget the annotation widget.      * @param appearanceStream the annotations appearance stream.      * @return the resolved boundingBox.      */
specifier|private
name|PDRectangle
name|resolveBoundingBox
parameter_list|(
name|PDAnnotationWidget
name|fieldWidget
parameter_list|,
name|PDAppearanceStream
name|appearanceStream
parameter_list|)
block|{
name|PDRectangle
name|boundingBox
init|=
name|appearanceStream
operator|.
name|getBBox
argument_list|()
decl_stmt|;
if|if
condition|(
name|boundingBox
operator|==
literal|null
condition|)
block|{
name|boundingBox
operator|=
name|fieldWidget
operator|.
name|getRectangle
argument_list|()
operator|.
name|createRetranslatedRectangle
argument_list|()
expr_stmt|;
block|}
return|return
name|boundingBox
return|;
block|}
comment|/**      * Apply padding to a box.      *       * @param box box      * @return the padded box.      */
specifier|private
name|PDRectangle
name|applyPadding
parameter_list|(
name|PDRectangle
name|box
parameter_list|,
name|float
name|padding
parameter_list|)
block|{
return|return
operator|new
name|PDRectangle
argument_list|(
name|box
operator|.
name|getLowerLeftX
argument_list|()
operator|+
name|padding
argument_list|,
name|box
operator|.
name|getLowerLeftY
argument_list|()
operator|+
name|padding
argument_list|,
name|box
operator|.
name|getWidth
argument_list|()
operator|-
literal|2
operator|*
name|padding
argument_list|,
name|box
operator|.
name|getHeight
argument_list|()
operator|-
literal|2
operator|*
name|padding
argument_list|)
return|;
block|}
block|}
end_class

end_unit

