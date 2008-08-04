begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2005, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|examples
operator|.
name|pdmodel
package|;
end_package

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDDocument
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|edit
operator|.
name|PDPageContentStream
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|font
operator|.
name|PDType1Font
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|graphics
operator|.
name|color
operator|.
name|PDGamma
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|action
operator|.
name|type
operator|.
name|PDActionURI
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationLine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationSquareCircle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationTextMarkup
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationLink
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
operator|.
name|PDBorderStyleDictionary
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
comment|/**  * This is an example on how to add annotations to pages of a PDF document.  *  * @author Paul King  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|Annotation
block|{
specifier|private
name|Annotation
parameter_list|()
block|{
comment|//utility class, should not be instantiated.
block|}
comment|/**      * This will create a doucument showing various annotations.      *      * @param args The command line arguments.      *      * @throws Exception If there is an error parsing the document.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|1
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
try|try
block|{
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|List
name|annotations
init|=
name|page
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
comment|// Setup some basic reusable objects/constants
comment|// Annotations themselves can only be used once!
name|float
name|inch
init|=
literal|72
decl_stmt|;
name|PDGamma
name|colourRed
init|=
operator|new
name|PDGamma
argument_list|()
decl_stmt|;
name|colourRed
operator|.
name|setR
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|PDGamma
name|colourBlue
init|=
operator|new
name|PDGamma
argument_list|()
decl_stmt|;
name|colourBlue
operator|.
name|setB
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|PDGamma
name|colourBlack
init|=
operator|new
name|PDGamma
argument_list|()
decl_stmt|;
name|PDBorderStyleDictionary
name|borderThick
init|=
operator|new
name|PDBorderStyleDictionary
argument_list|()
decl_stmt|;
name|borderThick
operator|.
name|setWidth
argument_list|(
name|inch
operator|/
literal|12
argument_list|)
expr_stmt|;
comment|// 12th inch
name|PDBorderStyleDictionary
name|borderThin
init|=
operator|new
name|PDBorderStyleDictionary
argument_list|()
decl_stmt|;
name|borderThin
operator|.
name|setWidth
argument_list|(
name|inch
operator|/
literal|72
argument_list|)
expr_stmt|;
comment|// 1 point
name|PDBorderStyleDictionary
name|borderULine
init|=
operator|new
name|PDBorderStyleDictionary
argument_list|()
decl_stmt|;
name|borderULine
operator|.
name|setStyle
argument_list|(
name|PDBorderStyleDictionary
operator|.
name|STYLE_UNDERLINE
argument_list|)
expr_stmt|;
name|borderULine
operator|.
name|setWidth
argument_list|(
name|inch
operator|/
literal|72
argument_list|)
expr_stmt|;
comment|// 1 point
name|float
name|pw
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
operator|.
name|getUpperRightX
argument_list|()
decl_stmt|;
name|float
name|ph
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
operator|.
name|getUpperRightY
argument_list|()
decl_stmt|;
comment|// First add some text, two lines we'll add some annotations to this later
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA_BOLD
decl_stmt|;
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|18
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
name|inch
argument_list|,
name|ph
operator|-
name|inch
operator|-
literal|18
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
literal|"PDFBox"
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|0
argument_list|,
operator|-
operator|(
name|inch
operator|/
literal|2
operator|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
literal|"Click Here"
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Now add the markup annotation, a highlight to PDFBox text
name|PDAnnotationTextMarkup
name|txtMark
init|=
operator|new
name|PDAnnotationTextMarkup
argument_list|(
name|PDAnnotationTextMarkup
operator|.
name|SUB_TYPE_HIGHLIGHT
argument_list|)
decl_stmt|;
name|txtMark
operator|.
name|setColour
argument_list|(
name|colourBlue
argument_list|)
expr_stmt|;
name|txtMark
operator|.
name|setConstantOpacity
argument_list|(
operator|(
name|float
operator|)
literal|0.2
argument_list|)
expr_stmt|;
comment|// Make the highlight 20% transparent
comment|// Set the rectangle containing the markup
name|float
name|textWidth
init|=
operator|(
name|font
operator|.
name|getStringWidth
argument_list|(
literal|"PDFBox"
argument_list|)
operator|/
literal|1000
operator|)
operator|*
literal|18
decl_stmt|;
name|PDRectangle
name|position
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|position
operator|.
name|setLowerLeftX
argument_list|(
name|inch
argument_list|)
expr_stmt|;
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
name|inch
operator|-
literal|18
argument_list|)
expr_stmt|;
name|position
operator|.
name|setUpperRightX
argument_list|(
literal|72
operator|+
name|textWidth
argument_list|)
expr_stmt|;
name|position
operator|.
name|setUpperRightY
argument_list|(
name|ph
operator|-
name|inch
argument_list|)
expr_stmt|;
name|txtMark
operator|.
name|setRectangle
argument_list|(
name|position
argument_list|)
expr_stmt|;
comment|// work out the points forming the four corners of the annotations
comment|// set out in anti clockwise form (Completely wraps the text)
comment|// OK, the below doesn't match that description.
comment|// It's what acrobat 7 does and displays properly!
name|float
index|[]
name|quads
init|=
operator|new
name|float
index|[
literal|8
index|]
decl_stmt|;
name|quads
index|[
literal|0
index|]
operator|=
name|position
operator|.
name|getLowerLeftX
argument_list|()
expr_stmt|;
comment|// x1
name|quads
index|[
literal|1
index|]
operator|=
name|position
operator|.
name|getUpperRightY
argument_list|()
operator|-
literal|2
expr_stmt|;
comment|// y1
name|quads
index|[
literal|2
index|]
operator|=
name|position
operator|.
name|getUpperRightX
argument_list|()
expr_stmt|;
comment|// x2
name|quads
index|[
literal|3
index|]
operator|=
name|quads
index|[
literal|1
index|]
expr_stmt|;
comment|// y2
name|quads
index|[
literal|4
index|]
operator|=
name|quads
index|[
literal|0
index|]
expr_stmt|;
comment|// x3
name|quads
index|[
literal|5
index|]
operator|=
name|position
operator|.
name|getLowerLeftY
argument_list|()
operator|-
literal|2
expr_stmt|;
comment|// y3
name|quads
index|[
literal|6
index|]
operator|=
name|quads
index|[
literal|2
index|]
expr_stmt|;
comment|// x4
name|quads
index|[
literal|7
index|]
operator|=
name|quads
index|[
literal|5
index|]
expr_stmt|;
comment|// y5
name|txtMark
operator|.
name|setQuadPoints
argument_list|(
name|quads
argument_list|)
expr_stmt|;
name|txtMark
operator|.
name|setContents
argument_list|(
literal|"Highlighted since it's important"
argument_list|)
expr_stmt|;
name|annotations
operator|.
name|add
argument_list|(
name|txtMark
argument_list|)
expr_stmt|;
comment|// Now add the link annotation, so the clickme works
name|PDAnnotationLink
name|txtLink
init|=
operator|new
name|PDAnnotationLink
argument_list|()
decl_stmt|;
name|txtLink
operator|.
name|setBorderStyle
argument_list|(
name|borderULine
argument_list|)
expr_stmt|;
comment|// Set the rectangle containing the link
name|textWidth
operator|=
operator|(
name|font
operator|.
name|getStringWidth
argument_list|(
literal|"Click Here"
argument_list|)
operator|/
literal|1000
operator|)
operator|*
literal|18
expr_stmt|;
name|position
operator|=
operator|new
name|PDRectangle
argument_list|()
expr_stmt|;
name|position
operator|.
name|setLowerLeftX
argument_list|(
name|inch
argument_list|)
expr_stmt|;
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
call|(
name|float
call|)
argument_list|(
literal|1.5
operator|*
name|inch
argument_list|)
operator|-
literal|20
argument_list|)
expr_stmt|;
comment|// down a couple of points
name|position
operator|.
name|setUpperRightX
argument_list|(
literal|72
operator|+
name|textWidth
argument_list|)
expr_stmt|;
name|position
operator|.
name|setUpperRightY
argument_list|(
name|ph
operator|-
call|(
name|float
call|)
argument_list|(
literal|1.5
operator|*
name|inch
argument_list|)
argument_list|)
expr_stmt|;
name|txtLink
operator|.
name|setRectangle
argument_list|(
name|position
argument_list|)
expr_stmt|;
comment|// add an action
name|PDActionURI
name|action
init|=
operator|new
name|PDActionURI
argument_list|()
decl_stmt|;
name|action
operator|.
name|setURI
argument_list|(
literal|"http://www.pdfbox.org"
argument_list|)
expr_stmt|;
name|txtLink
operator|.
name|setAction
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|annotations
operator|.
name|add
argument_list|(
name|txtLink
argument_list|)
expr_stmt|;
comment|// Now draw a few more annotations
name|PDAnnotationSquareCircle
name|aCircle
init|=
operator|new
name|PDAnnotationSquareCircle
argument_list|(
name|PDAnnotationSquareCircle
operator|.
name|SUB_TYPE_CIRCLE
argument_list|)
decl_stmt|;
name|aCircle
operator|.
name|setContents
argument_list|(
literal|"Circle Annotation"
argument_list|)
expr_stmt|;
name|aCircle
operator|.
name|setInteriorColour
argument_list|(
name|colourRed
argument_list|)
expr_stmt|;
comment|// Fill in circle in red
name|aCircle
operator|.
name|setColour
argument_list|(
name|colourBlue
argument_list|)
expr_stmt|;
comment|// The border itself will be blue
name|aCircle
operator|.
name|setBorderStyle
argument_list|(
name|borderThin
argument_list|)
expr_stmt|;
comment|// Place the annotation on the page, we'll make this 1" round
comment|// 3" down, 1" in on the page
name|position
operator|=
operator|new
name|PDRectangle
argument_list|()
expr_stmt|;
name|position
operator|.
name|setLowerLeftX
argument_list|(
name|inch
argument_list|)
expr_stmt|;
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
operator|(
literal|3
operator|*
name|inch
operator|)
operator|-
name|inch
argument_list|)
expr_stmt|;
comment|// 1" height, 3" down
name|position
operator|.
name|setUpperRightX
argument_list|(
literal|2
operator|*
name|inch
argument_list|)
expr_stmt|;
comment|// 1" in, 1" width
name|position
operator|.
name|setUpperRightY
argument_list|(
name|ph
operator|-
operator|(
literal|3
operator|*
name|inch
operator|)
argument_list|)
expr_stmt|;
comment|// 3" down
name|aCircle
operator|.
name|setRectangle
argument_list|(
name|position
argument_list|)
expr_stmt|;
comment|//  add to the annotations on the page
name|annotations
operator|.
name|add
argument_list|(
name|aCircle
argument_list|)
expr_stmt|;
comment|// Now a square annotation
name|PDAnnotationSquareCircle
name|aSquare
init|=
operator|new
name|PDAnnotationSquareCircle
argument_list|(
name|PDAnnotationSquareCircle
operator|.
name|SUB_TYPE_SQUARE
argument_list|)
decl_stmt|;
name|aSquare
operator|.
name|setContents
argument_list|(
literal|"Square Annotation"
argument_list|)
expr_stmt|;
name|aSquare
operator|.
name|setColour
argument_list|(
name|colourRed
argument_list|)
expr_stmt|;
comment|// Outline in red, not setting a fill
name|aSquare
operator|.
name|setBorderStyle
argument_list|(
name|borderThick
argument_list|)
expr_stmt|;
comment|// Place the annotation on the page, we'll make this 1" (72points) square
comment|// 3.5" down, 1" in from the right on the page
name|position
operator|=
operator|new
name|PDRectangle
argument_list|()
expr_stmt|;
comment|// Reuse the variable, but note it's a new object!
name|position
operator|.
name|setLowerLeftX
argument_list|(
name|pw
operator|-
operator|(
literal|2
operator|*
name|inch
operator|)
argument_list|)
expr_stmt|;
comment|// 1" in from right, 1" wide
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
call|(
name|float
call|)
argument_list|(
literal|3.5
operator|*
name|inch
argument_list|)
operator|-
name|inch
argument_list|)
expr_stmt|;
comment|// 1" height, 3.5" down
name|position
operator|.
name|setUpperRightX
argument_list|(
name|pw
operator|-
name|inch
argument_list|)
expr_stmt|;
comment|// 1" in from right
name|position
operator|.
name|setUpperRightY
argument_list|(
name|ph
operator|-
call|(
name|float
call|)
argument_list|(
literal|3.5
operator|*
name|inch
argument_list|)
argument_list|)
expr_stmt|;
comment|// 3.5" down
name|aSquare
operator|.
name|setRectangle
argument_list|(
name|position
argument_list|)
expr_stmt|;
comment|//  add to the annotations on the page
name|annotations
operator|.
name|add
argument_list|(
name|aSquare
argument_list|)
expr_stmt|;
comment|// Now we want to draw a line between the two, one end with an open arrow
name|PDAnnotationLine
name|aLine
init|=
operator|new
name|PDAnnotationLine
argument_list|()
decl_stmt|;
name|aLine
operator|.
name|setEndPointEndingStyle
argument_list|(
name|PDAnnotationLine
operator|.
name|LE_OPEN_ARROW
argument_list|)
expr_stmt|;
name|aLine
operator|.
name|setContents
argument_list|(
literal|"Circle->Square"
argument_list|)
expr_stmt|;
name|aLine
operator|.
name|setCaption
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Make the contents a caption on the line
comment|// Set the rectangle containing the line
name|position
operator|=
operator|new
name|PDRectangle
argument_list|()
expr_stmt|;
comment|// Reuse the variable, but note it's a new object!
name|position
operator|.
name|setLowerLeftX
argument_list|(
literal|2
operator|*
name|inch
argument_list|)
expr_stmt|;
comment|// 1" in + width of circle
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
call|(
name|float
call|)
argument_list|(
literal|3.5
operator|*
name|inch
argument_list|)
operator|-
name|inch
argument_list|)
expr_stmt|;
comment|// 1" height, 3.5" down
name|position
operator|.
name|setUpperRightX
argument_list|(
name|pw
operator|-
name|inch
operator|-
name|inch
argument_list|)
expr_stmt|;
comment|// 1" in from right, and width of square
name|position
operator|.
name|setUpperRightY
argument_list|(
name|ph
operator|-
operator|(
literal|3
operator|*
name|inch
operator|)
argument_list|)
expr_stmt|;
comment|// 3" down (top of circle)
name|aLine
operator|.
name|setRectangle
argument_list|(
name|position
argument_list|)
expr_stmt|;
comment|// Now set the line position itself
name|float
index|[]
name|linepos
init|=
operator|new
name|float
index|[
literal|4
index|]
decl_stmt|;
name|linepos
index|[
literal|0
index|]
operator|=
literal|2
operator|*
name|inch
expr_stmt|;
comment|// x1 = rhs of circle
name|linepos
index|[
literal|1
index|]
operator|=
name|ph
operator|-
call|(
name|float
call|)
argument_list|(
literal|3.5
operator|*
name|inch
argument_list|)
expr_stmt|;
comment|// y1 halfway down circle
name|linepos
index|[
literal|2
index|]
operator|=
name|pw
operator|-
operator|(
literal|2
operator|*
name|inch
operator|)
expr_stmt|;
comment|// x2 = lhs of square
name|linepos
index|[
literal|3
index|]
operator|=
name|ph
operator|-
operator|(
literal|4
operator|*
name|inch
operator|)
expr_stmt|;
comment|// y2 halfway down square
name|aLine
operator|.
name|setLine
argument_list|(
name|linepos
argument_list|)
expr_stmt|;
name|aLine
operator|.
name|setBorderStyle
argument_list|(
name|borderThick
argument_list|)
expr_stmt|;
name|aLine
operator|.
name|setColour
argument_list|(
name|colourBlack
argument_list|)
expr_stmt|;
comment|// add to the annotations on the page
name|annotations
operator|.
name|add
argument_list|(
name|aLine
argument_list|)
expr_stmt|;
comment|// Finally all done
name|document
operator|.
name|save
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will print the usage for this document.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Usage: java org.pdfbox.examples.pdmodel.Annotation<output-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

