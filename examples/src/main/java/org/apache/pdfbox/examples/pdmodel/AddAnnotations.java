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
name|examples
operator|.
name|pdmodel
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
name|io
operator|.
name|ScratchFile
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
name|PDDocument
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
name|font
operator|.
name|PDType1Font
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
name|color
operator|.
name|PDColor
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
name|color
operator|.
name|PDDeviceRGB
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
name|PDActionGoTo
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
name|PDActionURI
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
name|PDAnnotation
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
name|PDAnnotationCircle
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
name|PDAnnotationFreeText
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
name|PDAnnotationHighlight
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
name|PDAnnotationLine
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
name|PDAnnotationLink
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
name|PDAnnotationSquare
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
name|PDBorderStyleDictionary
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDPageDestination
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDPageFitWidthDestination
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
name|form
operator|.
name|PDAcroForm
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
name|form
operator|.
name|PDVariableText
import|;
end_import

begin_comment
comment|/**  * Add annotations to pages of a PDF document.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|AddAnnotations
block|{
specifier|static
specifier|final
name|float
name|INCH
init|=
literal|72
decl_stmt|;
specifier|private
name|AddAnnotations
parameter_list|()
block|{     }
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
name|IOException
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Usage: "
operator|+
name|AddAnnotations
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<output-pdf>"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
try|try
init|(
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
init|)
block|{
name|PDPage
name|page1
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|PDPage
name|page2
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|PDPage
name|page3
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page1
argument_list|)
expr_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page2
argument_list|)
expr_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page3
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|annotations
init|=
name|page1
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
comment|// Some basic reusable objects/constants
comment|// Annotations themselves can only be used once!
name|PDColor
name|red
init|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|1
block|,
literal|0
block|,
literal|0
block|}
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|PDColor
name|blue
init|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|1
block|}
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|PDColor
name|black
init|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
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
name|INCH
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
name|INCH
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
name|INCH
operator|/
literal|72
argument_list|)
expr_stmt|;
comment|// 1 point
name|float
name|pw
init|=
name|page1
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
name|page1
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
try|try
init|(
name|PDPageContentStream
name|contents
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page1
argument_list|)
init|)
block|{
name|contents
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contents
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|18
argument_list|)
expr_stmt|;
name|contents
operator|.
name|newLineAtOffset
argument_list|(
name|INCH
argument_list|,
name|ph
operator|-
name|INCH
operator|-
literal|18
argument_list|)
expr_stmt|;
name|contents
operator|.
name|showText
argument_list|(
literal|"PDFBox"
argument_list|)
expr_stmt|;
name|contents
operator|.
name|newLineAtOffset
argument_list|(
literal|0
argument_list|,
operator|-
operator|(
name|INCH
operator|/
literal|2
operator|)
argument_list|)
expr_stmt|;
name|contents
operator|.
name|showText
argument_list|(
literal|"External URL"
argument_list|)
expr_stmt|;
name|contents
operator|.
name|newLineAtOffset
argument_list|(
literal|0
argument_list|,
operator|-
operator|(
name|INCH
operator|/
literal|2
operator|)
argument_list|)
expr_stmt|;
name|contents
operator|.
name|showText
argument_list|(
literal|"Jump to page three"
argument_list|)
expr_stmt|;
name|contents
operator|.
name|endText
argument_list|()
expr_stmt|;
block|}
comment|// Now add the markup annotation, a highlight to PDFBox text
name|PDAnnotationHighlight
name|txtHighlight
init|=
operator|new
name|PDAnnotationHighlight
argument_list|()
decl_stmt|;
name|txtHighlight
operator|.
name|setColor
argument_list|(
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|1
block|,
literal|1
block|}
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
argument_list|)
expr_stmt|;
comment|// 20% transparent
name|txtHighlight
operator|.
name|setConstantOpacity
argument_list|(
operator|(
name|float
operator|)
literal|0.2
argument_list|)
expr_stmt|;
comment|// Set the rectangle containing the markup
name|float
name|textWidth
init|=
name|font
operator|.
name|getStringWidth
argument_list|(
literal|"PDFBox"
argument_list|)
operator|/
literal|1000
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
name|INCH
argument_list|)
expr_stmt|;
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
name|INCH
operator|-
literal|18
argument_list|)
expr_stmt|;
name|position
operator|.
name|setUpperRightX
argument_list|(
name|INCH
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
name|INCH
argument_list|)
expr_stmt|;
name|txtHighlight
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
name|txtHighlight
operator|.
name|setQuadPoints
argument_list|(
name|quads
argument_list|)
expr_stmt|;
name|txtHighlight
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
name|txtHighlight
argument_list|)
expr_stmt|;
comment|// Now add the link annotation, so the click on "External URL" works
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
name|font
operator|.
name|getStringWidth
argument_list|(
literal|"External URL"
argument_list|)
operator|/
literal|1000
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
name|INCH
argument_list|)
expr_stmt|;
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
literal|1.5f
operator|*
name|INCH
operator|-
literal|20
argument_list|)
expr_stmt|;
comment|// down a couple of points
name|position
operator|.
name|setUpperRightX
argument_list|(
name|INCH
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
literal|1.5f
operator|*
name|INCH
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
literal|"http://pdfbox.apache.org"
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
name|PDAnnotationCircle
name|aCircle
init|=
operator|new
name|PDAnnotationCircle
argument_list|()
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
name|setInteriorColor
argument_list|(
name|red
argument_list|)
expr_stmt|;
comment|// Fill in circle in red
name|aCircle
operator|.
name|setColor
argument_list|(
name|blue
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
name|INCH
argument_list|)
expr_stmt|;
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
literal|3
operator|*
name|INCH
operator|-
name|INCH
argument_list|)
expr_stmt|;
comment|// 1" height, 3" down
name|position
operator|.
name|setUpperRightX
argument_list|(
literal|2
operator|*
name|INCH
argument_list|)
expr_stmt|;
comment|// 1" in, 1" width
name|position
operator|.
name|setUpperRightY
argument_list|(
name|ph
operator|-
literal|3
operator|*
name|INCH
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
name|annotations
operator|.
name|add
argument_list|(
name|aCircle
argument_list|)
expr_stmt|;
comment|// Now a square annotation
name|PDAnnotationSquare
name|aSquare
init|=
operator|new
name|PDAnnotationSquare
argument_list|()
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
name|setColor
argument_list|(
name|red
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
comment|// Place the annotation on the page, we'll make this 1" (72 points) square
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
literal|2
operator|*
name|INCH
argument_list|)
expr_stmt|;
comment|// 1" in from right, 1" wide
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
literal|3.5f
operator|*
name|INCH
operator|-
name|INCH
argument_list|)
expr_stmt|;
comment|// 1" height, 3.5" down
name|position
operator|.
name|setUpperRightX
argument_list|(
name|pw
operator|-
name|INCH
argument_list|)
expr_stmt|;
comment|// 1" in from right
name|position
operator|.
name|setUpperRightY
argument_list|(
name|ph
operator|-
literal|3.5f
operator|*
name|INCH
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
name|INCH
argument_list|)
expr_stmt|;
comment|// 1" in + width of circle
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
literal|3.5f
operator|*
name|INCH
operator|-
name|INCH
argument_list|)
expr_stmt|;
comment|// 1" height, 3.5" down
name|position
operator|.
name|setUpperRightX
argument_list|(
name|pw
operator|-
name|INCH
operator|-
name|INCH
argument_list|)
expr_stmt|;
comment|// 1" in from right, and width of square
name|position
operator|.
name|setUpperRightY
argument_list|(
name|ph
operator|-
literal|3
operator|*
name|INCH
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
name|INCH
expr_stmt|;
comment|// x1 = rhs of circle
name|linepos
index|[
literal|1
index|]
operator|=
name|ph
operator|-
literal|3.5f
operator|*
name|INCH
expr_stmt|;
comment|// y1 halfway down circle
name|linepos
index|[
literal|2
index|]
operator|=
name|pw
operator|-
literal|2
operator|*
name|INCH
expr_stmt|;
comment|// x2 = lhs of square
name|linepos
index|[
literal|3
index|]
operator|=
name|ph
operator|-
literal|4
operator|*
name|INCH
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
name|setColor
argument_list|(
name|black
argument_list|)
expr_stmt|;
name|annotations
operator|.
name|add
argument_list|(
name|aLine
argument_list|)
expr_stmt|;
comment|// Now add the link annotation, so the click on "Jump to page three" works
name|PDAnnotationLink
name|pageLink
init|=
operator|new
name|PDAnnotationLink
argument_list|()
decl_stmt|;
name|pageLink
operator|.
name|setBorderStyle
argument_list|(
name|borderULine
argument_list|)
expr_stmt|;
comment|// Set the rectangle containing the link
name|textWidth
operator|=
name|font
operator|.
name|getStringWidth
argument_list|(
literal|"Jump to page three"
argument_list|)
operator|/
literal|1000
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
name|INCH
argument_list|)
expr_stmt|;
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
literal|2
operator|*
name|INCH
operator|-
literal|20
argument_list|)
expr_stmt|;
comment|// down a couple of points
name|position
operator|.
name|setUpperRightX
argument_list|(
name|INCH
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
literal|2
operator|*
name|INCH
argument_list|)
expr_stmt|;
name|pageLink
operator|.
name|setRectangle
argument_list|(
name|position
argument_list|)
expr_stmt|;
comment|// add the GoTo action
name|PDActionGoTo
name|actionGoto
init|=
operator|new
name|PDActionGoTo
argument_list|()
decl_stmt|;
comment|// see javadoc for other types of PDPageDestination
name|PDPageDestination
name|dest
init|=
operator|new
name|PDPageFitWidthDestination
argument_list|()
decl_stmt|;
comment|// do not use setPageNumber(), this is for external destinations only
name|dest
operator|.
name|setPage
argument_list|(
name|page3
argument_list|)
expr_stmt|;
name|actionGoto
operator|.
name|setDestination
argument_list|(
name|dest
argument_list|)
expr_stmt|;
name|pageLink
operator|.
name|setAction
argument_list|(
name|actionGoto
argument_list|)
expr_stmt|;
name|annotations
operator|.
name|add
argument_list|(
name|pageLink
argument_list|)
expr_stmt|;
name|PDAnnotationFreeText
name|freeTextAnnotation
init|=
operator|new
name|PDAnnotationFreeText
argument_list|()
decl_stmt|;
name|PDColor
name|yellow
init|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|1
block|,
literal|1
block|,
literal|0
block|}
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
comment|// this sets background only (contradicts PDF specification)
name|freeTextAnnotation
operator|.
name|setColor
argument_list|(
name|yellow
argument_list|)
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
literal|1
operator|*
name|INCH
argument_list|)
expr_stmt|;
name|position
operator|.
name|setLowerLeftY
argument_list|(
name|ph
operator|-
literal|5f
operator|*
name|INCH
operator|-
literal|3
operator|*
name|INCH
argument_list|)
expr_stmt|;
name|position
operator|.
name|setUpperRightX
argument_list|(
name|pw
operator|-
name|INCH
argument_list|)
expr_stmt|;
name|position
operator|.
name|setUpperRightY
argument_list|(
name|ph
operator|-
literal|5f
operator|*
name|INCH
argument_list|)
expr_stmt|;
name|freeTextAnnotation
operator|.
name|setRectangle
argument_list|(
name|position
argument_list|)
expr_stmt|;
name|freeTextAnnotation
operator|.
name|setTitlePopup
argument_list|(
literal|"Sophia Lorem"
argument_list|)
expr_stmt|;
name|freeTextAnnotation
operator|.
name|setSubject
argument_list|(
literal|"Lorem ipsum"
argument_list|)
expr_stmt|;
name|freeTextAnnotation
operator|.
name|setContents
argument_list|(
literal|"Lorem ipsum dolor sit amet, consetetur sadipscing elitr,"
operator|+
literal|" sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam "
operator|+
literal|"erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea "
operator|+
literal|"rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum "
operator|+
literal|"dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, "
operator|+
literal|"sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam "
operator|+
literal|"erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea "
operator|+
literal|"rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum "
operator|+
literal|"dolor sit amet."
argument_list|)
expr_stmt|;
comment|// Text and border in blue RGB color, "Helv" font, 20 point
name|freeTextAnnotation
operator|.
name|setDefaultAppearance
argument_list|(
literal|"0 0 1 rg /Helv 20 Tf"
argument_list|)
expr_stmt|;
comment|// Quadding does not have any effect?!
name|freeTextAnnotation
operator|.
name|setQ
argument_list|(
name|PDVariableText
operator|.
name|QUADDING_RIGHT
argument_list|)
expr_stmt|;
name|annotations
operator|.
name|add
argument_list|(
name|freeTextAnnotation
argument_list|)
expr_stmt|;
comment|// add the "Helv" font to the default resources
name|PDAcroForm
name|acroForm
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
if|if
condition|(
name|acroForm
operator|==
literal|null
condition|)
block|{
name|acroForm
operator|=
operator|new
name|PDAcroForm
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setAcroForm
argument_list|(
name|acroForm
argument_list|)
expr_stmt|;
block|}
name|PDResources
name|dr
init|=
name|acroForm
operator|.
name|getDefaultResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|dr
operator|==
literal|null
condition|)
block|{
name|dr
operator|=
operator|new
name|PDResources
argument_list|()
expr_stmt|;
name|acroForm
operator|.
name|setDefaultResources
argument_list|(
name|dr
argument_list|)
expr_stmt|;
block|}
name|dr
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Helv"
argument_list|)
argument_list|,
name|PDType1Font
operator|.
name|HELVETICA
argument_list|)
expr_stmt|;
comment|// Create the appearance streams.
comment|// Adobe Reader will always display annotations without appearance streams nicely,
comment|// but other applications may not.
for|for
control|(
name|PDAnnotation
name|ann
range|:
name|annotations
control|)
block|{
name|ann
operator|.
name|constructAppearances
argument_list|()
expr_stmt|;
block|}
name|showPageNo
argument_list|(
name|document
argument_list|,
name|page1
argument_list|,
literal|"Page 1"
argument_list|)
expr_stmt|;
name|showPageNo
argument_list|(
name|document
argument_list|,
name|page2
argument_list|,
literal|"Page 2"
argument_list|)
expr_stmt|;
name|showPageNo
argument_list|(
name|document
argument_list|,
name|page3
argument_list|,
literal|"Page 3"
argument_list|)
expr_stmt|;
comment|// save the PDF
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
block|}
specifier|private
specifier|static
name|void
name|showPageNo
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|String
name|pageText
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|fontSize
init|=
literal|10
decl_stmt|;
try|try
init|(
name|PDPageContentStream
name|contents
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|,
name|PDPageContentStream
operator|.
name|AppendMode
operator|.
name|PREPEND
argument_list|,
literal|true
argument_list|)
init|)
block|{
name|float
name|pageWidth
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|float
name|pageHeight
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA
decl_stmt|;
name|contents
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
name|fontSize
argument_list|)
expr_stmt|;
name|float
name|textWidth
init|=
name|font
operator|.
name|getStringWidth
argument_list|(
name|pageText
argument_list|)
operator|/
literal|1000
operator|*
name|fontSize
decl_stmt|;
name|contents
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contents
operator|.
name|newLineAtOffset
argument_list|(
name|pageWidth
operator|/
literal|2
operator|-
name|textWidth
operator|/
literal|2
argument_list|,
name|pageHeight
operator|-
name|INCH
operator|/
literal|2
argument_list|)
expr_stmt|;
name|contents
operator|.
name|showText
argument_list|(
name|pageText
argument_list|)
expr_stmt|;
name|contents
operator|.
name|endText
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

