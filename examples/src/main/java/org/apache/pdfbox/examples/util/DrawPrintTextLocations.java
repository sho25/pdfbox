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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|BasicStroke
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Shape
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
name|Rectangle2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

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
name|File
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
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
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
name|util
operator|.
name|BoundingBox
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
name|font
operator|.
name|PDCIDFontType2
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
name|PDSimpleFont
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
name|PDTrueTypeFont
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
name|PDType0Font
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
name|PDType3CharProc
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
name|PDType3Font
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
name|PDVectorFont
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
name|pagenavigation
operator|.
name|PDThreadBead
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
name|rendering
operator|.
name|PDFRenderer
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
name|text
operator|.
name|PDFTextStripper
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
name|text
operator|.
name|TextPosition
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
name|Matrix
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
name|Vector
import|;
end_import

begin_comment
comment|/**  * This is an example on how to get some x/y coordinates of text and to show them in a rendered  * image.  *  * @author Ben Litchfield  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|DrawPrintTextLocations
extends|extends
name|PDFTextStripper
block|{
specifier|private
name|BufferedImage
name|image
decl_stmt|;
specifier|private
name|AffineTransform
name|flipAT
decl_stmt|;
specifier|private
name|AffineTransform
name|rotateAT
decl_stmt|;
specifier|private
name|AffineTransform
name|transAT
decl_stmt|;
specifier|private
specifier|final
name|String
name|filename
decl_stmt|;
specifier|static
specifier|final
name|int
name|SCALE
init|=
literal|4
decl_stmt|;
specifier|private
name|Graphics2D
name|g2d
decl_stmt|;
specifier|private
specifier|final
name|PDDocument
name|document
decl_stmt|;
comment|/**      * Instantiate a new PDFTextStripper object.      *      * @param document      * @param filename      * @throws IOException If there is an error loading the properties.      */
specifier|public
name|DrawPrintTextLocations
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
name|this
operator|.
name|filename
operator|=
name|filename
expr_stmt|;
block|}
comment|/**      * This will print the documents data.      *      * @param args The command line arguments.      *      * @throws IOException If there is an error parsing the document.      */
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
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|DrawPrintTextLocations
name|stripper
init|=
operator|new
name|DrawPrintTextLocations
argument_list|(
name|document
argument_list|,
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|stripper
operator|.
name|setSortByPosition
argument_list|(
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|page
init|=
literal|0
init|;
name|page
operator|<
name|document
operator|.
name|getNumberOfPages
argument_list|()
condition|;
operator|++
name|page
control|)
block|{
name|stripper
operator|.
name|stripPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
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
name|super
operator|.
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
name|displacement
argument_list|)
expr_stmt|;
comment|// in cyan:
comment|// show actual glyph bounds. This must be done here and not in writeString(),
comment|// because writeString processes only the glyphs with unicode,
comment|// see e.g. the file in PDFBOX-3274
name|Shape
name|cyanShape
init|=
name|calculateGlyphBounds
argument_list|(
name|textRenderingMatrix
argument_list|,
name|font
argument_list|,
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|cyanShape
operator|!=
literal|null
condition|)
block|{
name|cyanShape
operator|=
name|flipAT
operator|.
name|createTransformedShape
argument_list|(
name|cyanShape
argument_list|)
expr_stmt|;
name|cyanShape
operator|=
name|rotateAT
operator|.
name|createTransformedShape
argument_list|(
name|cyanShape
argument_list|)
expr_stmt|;
name|cyanShape
operator|=
name|transAT
operator|.
name|createTransformedShape
argument_list|(
name|cyanShape
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|CYAN
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|draw
argument_list|(
name|cyanShape
argument_list|)
expr_stmt|;
block|}
block|}
comment|// this calculates the real individual glyph bounds
specifier|private
name|Shape
name|calculateGlyphBounds
parameter_list|(
name|Matrix
name|textRenderingMatrix
parameter_list|,
name|PDFont
name|font
parameter_list|,
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
name|GeneralPath
name|path
init|=
literal|null
decl_stmt|;
name|AffineTransform
name|at
init|=
name|textRenderingMatrix
operator|.
name|createAffineTransform
argument_list|()
decl_stmt|;
name|at
operator|.
name|concatenate
argument_list|(
name|font
operator|.
name|getFontMatrix
argument_list|()
operator|.
name|createAffineTransform
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|font
operator|instanceof
name|PDType3Font
condition|)
block|{
name|PDType3Font
name|t3Font
init|=
operator|(
name|PDType3Font
operator|)
name|font
decl_stmt|;
name|PDType3CharProc
name|charProc
init|=
name|t3Font
operator|.
name|getCharProc
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|charProc
operator|!=
literal|null
condition|)
block|{
name|PDRectangle
name|glyphBBox
init|=
name|charProc
operator|.
name|getGlyphBBox
argument_list|()
decl_stmt|;
if|if
condition|(
name|glyphBBox
operator|!=
literal|null
condition|)
block|{
name|path
operator|=
name|glyphBBox
operator|.
name|toGeneralPath
argument_list|()
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|font
operator|instanceof
name|PDVectorFont
condition|)
block|{
name|PDVectorFont
name|vectorFont
init|=
operator|(
name|PDVectorFont
operator|)
name|font
decl_stmt|;
name|path
operator|=
name|vectorFont
operator|.
name|getPath
argument_list|(
name|code
argument_list|)
expr_stmt|;
if|if
condition|(
name|font
operator|instanceof
name|PDTrueTypeFont
condition|)
block|{
name|PDTrueTypeFont
name|ttFont
init|=
operator|(
name|PDTrueTypeFont
operator|)
name|font
decl_stmt|;
name|int
name|unitsPerEm
init|=
name|ttFont
operator|.
name|getTrueTypeFont
argument_list|()
operator|.
name|getHeader
argument_list|()
operator|.
name|getUnitsPerEm
argument_list|()
decl_stmt|;
name|at
operator|.
name|scale
argument_list|(
literal|1000d
operator|/
name|unitsPerEm
argument_list|,
literal|1000d
operator|/
name|unitsPerEm
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|font
operator|instanceof
name|PDType0Font
condition|)
block|{
name|PDType0Font
name|t0font
init|=
operator|(
name|PDType0Font
operator|)
name|font
decl_stmt|;
if|if
condition|(
name|t0font
operator|.
name|getDescendantFont
argument_list|()
operator|instanceof
name|PDCIDFontType2
condition|)
block|{
name|int
name|unitsPerEm
init|=
operator|(
operator|(
name|PDCIDFontType2
operator|)
name|t0font
operator|.
name|getDescendantFont
argument_list|()
operator|)
operator|.
name|getTrueTypeFont
argument_list|()
operator|.
name|getHeader
argument_list|()
operator|.
name|getUnitsPerEm
argument_list|()
decl_stmt|;
name|at
operator|.
name|scale
argument_list|(
literal|1000d
operator|/
name|unitsPerEm
argument_list|,
literal|1000d
operator|/
name|unitsPerEm
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|font
operator|instanceof
name|PDSimpleFont
condition|)
block|{
name|PDSimpleFont
name|simpleFont
init|=
operator|(
name|PDSimpleFont
operator|)
name|font
decl_stmt|;
comment|// these two lines do not always work, e.g. for the TT fonts in file 032431.pdf
comment|// which is why PDVectorFont is tried first.
name|String
name|name
init|=
name|simpleFont
operator|.
name|getEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
name|path
operator|=
name|simpleFont
operator|.
name|getPath
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// shouldn't happen, please open issue in JIRA
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Unknown font class: "
operator|+
name|font
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|at
operator|.
name|createTransformedShape
argument_list|(
name|path
operator|.
name|getBounds2D
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|void
name|stripPage
parameter_list|(
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFRenderer
name|pdfRenderer
init|=
operator|new
name|PDFRenderer
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|image
operator|=
name|pdfRenderer
operator|.
name|renderImage
argument_list|(
name|page
argument_list|,
name|SCALE
argument_list|)
expr_stmt|;
name|PDPage
name|pdPage
init|=
name|document
operator|.
name|getPage
argument_list|(
name|page
argument_list|)
decl_stmt|;
name|PDRectangle
name|cropBox
init|=
name|pdPage
operator|.
name|getCropBox
argument_list|()
decl_stmt|;
comment|// flip y-axis
name|flipAT
operator|=
operator|new
name|AffineTransform
argument_list|()
expr_stmt|;
name|flipAT
operator|.
name|translate
argument_list|(
literal|0
argument_list|,
name|pdPage
operator|.
name|getBBox
argument_list|()
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|flipAT
operator|.
name|scale
argument_list|(
literal|1
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// page may be rotated
name|rotateAT
operator|=
operator|new
name|AffineTransform
argument_list|()
expr_stmt|;
name|int
name|rotation
init|=
name|pdPage
operator|.
name|getRotation
argument_list|()
decl_stmt|;
if|if
condition|(
name|rotation
operator|!=
literal|0
condition|)
block|{
name|PDRectangle
name|mediaBox
init|=
name|pdPage
operator|.
name|getMediaBox
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|rotation
condition|)
block|{
case|case
literal|90
case|:
name|rotateAT
operator|.
name|translate
argument_list|(
name|mediaBox
operator|.
name|getHeight
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
literal|270
case|:
name|rotateAT
operator|.
name|translate
argument_list|(
literal|0
argument_list|,
name|mediaBox
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
literal|180
case|:
name|rotateAT
operator|.
name|translate
argument_list|(
name|mediaBox
operator|.
name|getWidth
argument_list|()
argument_list|,
name|mediaBox
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
name|rotateAT
operator|.
name|rotate
argument_list|(
name|Math
operator|.
name|toRadians
argument_list|(
name|rotation
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// cropbox
name|transAT
operator|=
name|AffineTransform
operator|.
name|getTranslateInstance
argument_list|(
operator|-
name|cropBox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|cropBox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
expr_stmt|;
name|g2d
operator|=
name|image
operator|.
name|createGraphics
argument_list|()
expr_stmt|;
name|g2d
operator|.
name|setStroke
argument_list|(
operator|new
name|BasicStroke
argument_list|(
literal|0.1f
argument_list|)
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|scale
argument_list|(
name|SCALE
argument_list|,
name|SCALE
argument_list|)
expr_stmt|;
name|setStartPage
argument_list|(
name|page
operator|+
literal|1
argument_list|)
expr_stmt|;
name|setEndPage
argument_list|(
name|page
operator|+
literal|1
argument_list|)
expr_stmt|;
name|Writer
name|dummy
init|=
operator|new
name|OutputStreamWriter
argument_list|(
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
decl_stmt|;
name|writeText
argument_list|(
name|document
argument_list|,
name|dummy
argument_list|)
expr_stmt|;
comment|// beads in green
name|g2d
operator|.
name|setStroke
argument_list|(
operator|new
name|BasicStroke
argument_list|(
literal|0.4f
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PDThreadBead
argument_list|>
name|pageArticles
init|=
name|pdPage
operator|.
name|getThreadBeads
argument_list|()
decl_stmt|;
for|for
control|(
name|PDThreadBead
name|bead
range|:
name|pageArticles
control|)
block|{
name|PDRectangle
name|r
init|=
name|bead
operator|.
name|getRectangle
argument_list|()
decl_stmt|;
name|Shape
name|s
init|=
name|r
operator|.
name|toGeneralPath
argument_list|()
operator|.
name|createTransformedShape
argument_list|(
name|transAT
argument_list|)
decl_stmt|;
name|s
operator|=
name|flipAT
operator|.
name|createTransformedShape
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|s
operator|=
name|rotateAT
operator|.
name|createTransformedShape
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|green
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|draw
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|g2d
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|String
name|imageFilename
init|=
name|filename
decl_stmt|;
name|int
name|pt
init|=
name|imageFilename
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
name|imageFilename
operator|=
name|imageFilename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pt
argument_list|)
operator|+
literal|"-marked-"
operator|+
operator|(
name|page
operator|+
literal|1
operator|)
operator|+
literal|".png"
expr_stmt|;
name|ImageIO
operator|.
name|write
argument_list|(
name|image
argument_list|,
literal|"png"
argument_list|,
operator|new
name|File
argument_list|(
name|imageFilename
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Override the default functionality of PDFTextStripper.      */
annotation|@
name|Override
specifier|protected
name|void
name|writeString
parameter_list|(
name|String
name|string
parameter_list|,
name|List
argument_list|<
name|TextPosition
argument_list|>
name|textPositions
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|TextPosition
name|text
range|:
name|textPositions
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"String["
operator|+
name|text
operator|.
name|getXDirAdj
argument_list|()
operator|+
literal|","
operator|+
name|text
operator|.
name|getYDirAdj
argument_list|()
operator|+
literal|" fs="
operator|+
name|text
operator|.
name|getFontSize
argument_list|()
operator|+
literal|" xscale="
operator|+
name|text
operator|.
name|getXScale
argument_list|()
operator|+
literal|" height="
operator|+
name|text
operator|.
name|getHeightDir
argument_list|()
operator|+
literal|" space="
operator|+
name|text
operator|.
name|getWidthOfSpace
argument_list|()
operator|+
literal|" width="
operator|+
name|text
operator|.
name|getWidthDirAdj
argument_list|()
operator|+
literal|"]"
operator|+
name|text
operator|.
name|getUnicode
argument_list|()
argument_list|)
expr_stmt|;
comment|// in red:
comment|// show rectangles with the "height" (not a real height, but used for text extraction
comment|// heuristics, it is 1/2 of the bounding box height and starts at y=0)
name|Rectangle2D
operator|.
name|Float
name|rect
init|=
operator|new
name|Rectangle2D
operator|.
name|Float
argument_list|(
name|text
operator|.
name|getXDirAdj
argument_list|()
argument_list|,
operator|(
name|text
operator|.
name|getYDirAdj
argument_list|()
operator|-
name|text
operator|.
name|getHeightDir
argument_list|()
operator|)
argument_list|,
name|text
operator|.
name|getWidthDirAdj
argument_list|()
argument_list|,
name|text
operator|.
name|getHeightDir
argument_list|()
argument_list|)
decl_stmt|;
name|Shape
name|sh
init|=
name|rotateAT
operator|.
name|createTransformedShape
argument_list|(
name|rect
argument_list|)
decl_stmt|;
name|g2d
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|red
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|draw
argument_list|(
name|sh
argument_list|)
expr_stmt|;
comment|// in blue:
comment|// show rectangle with the real vertical bounds, based on the font bounding box y values
comment|// usually, the height is identical to what you see when marking text in Adobe Reader
name|PDFont
name|font
init|=
name|text
operator|.
name|getFont
argument_list|()
decl_stmt|;
name|BoundingBox
name|bbox
init|=
name|font
operator|.
name|getBoundingBox
argument_list|()
decl_stmt|;
comment|// advance width, bbox height (glyph space)
name|float
name|xadvance
init|=
name|font
operator|.
name|getWidth
argument_list|(
name|text
operator|.
name|getCharacterCodes
argument_list|()
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
comment|// todo: should iterate all chars
name|rect
operator|=
operator|new
name|Rectangle2D
operator|.
name|Float
argument_list|(
literal|0
argument_list|,
name|bbox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|,
name|xadvance
argument_list|,
name|bbox
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
comment|// glyph space -> user space
comment|// note: text.getTextMatrix() is *not* the Text Matrix, it's the Text Rendering Matrix
name|AffineTransform
name|at
init|=
name|text
operator|.
name|getTextMatrix
argument_list|()
operator|.
name|createAffineTransform
argument_list|()
decl_stmt|;
if|if
condition|(
name|font
operator|instanceof
name|PDType3Font
condition|)
block|{
comment|// bbox and font matrix are unscaled
name|at
operator|.
name|concatenate
argument_list|(
name|font
operator|.
name|getFontMatrix
argument_list|()
operator|.
name|createAffineTransform
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// bbox and font matrix are already scaled to 1000
name|at
operator|.
name|scale
argument_list|(
literal|1
operator|/
literal|1000f
argument_list|,
literal|1
operator|/
literal|1000f
argument_list|)
expr_stmt|;
block|}
name|Shape
name|s
init|=
name|at
operator|.
name|createTransformedShape
argument_list|(
name|rect
argument_list|)
decl_stmt|;
name|s
operator|=
name|flipAT
operator|.
name|createTransformedShape
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|s
operator|=
name|rotateAT
operator|.
name|createTransformedShape
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|blue
argument_list|)
expr_stmt|;
name|g2d
operator|.
name|draw
argument_list|(
name|s
argument_list|)
expr_stmt|;
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
literal|"Usage: java "
operator|+
name|DrawPrintTextLocations
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<input-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

