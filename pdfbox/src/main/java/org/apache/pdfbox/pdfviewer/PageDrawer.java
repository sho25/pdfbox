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
name|pdfviewer
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
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics
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
name|geom
operator|.
name|Area
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|RenderingHints
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
name|Image
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
name|common
operator|.
name|PDMatrix
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
name|pdmodel
operator|.
name|graphics
operator|.
name|PDShading
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
name|PDAppearanceStream
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
name|text
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
name|PDFStreamEngine
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
name|ResourceLoader
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
name|COSDictionary
import|;
end_import

begin_comment
comment|/**  * This will paint a page in a PDF document to a graphics context.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.22 $  */
end_comment

begin_class
specifier|public
class|class
name|PageDrawer
extends|extends
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
name|PageDrawer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Graphics2D
name|graphics
decl_stmt|;
specifier|protected
name|Dimension
name|pageSize
decl_stmt|;
specifier|protected
name|PDPage
name|page
decl_stmt|;
specifier|private
name|GeneralPath
name|linePath
init|=
operator|new
name|GeneralPath
argument_list|()
decl_stmt|;
comment|/**      * Default constructor, loads properties from file.      *      * @throws IOException If there is an error loading properties from the file.      */
specifier|public
name|PageDrawer
parameter_list|()
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|ResourceLoader
operator|.
name|loadProperties
argument_list|(
literal|"org/apache/pdfbox/resources/PageDrawer.properties"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will draw the page to the requested context.      *      * @param g The graphics context to draw onto.      * @param p The page to draw.      * @param pageDimension The size of the page to draw.      *      * @throws IOException If there is an IO error while drawing the page.      */
specifier|public
name|void
name|drawPage
parameter_list|(
name|Graphics
name|g
parameter_list|,
name|PDPage
name|p
parameter_list|,
name|Dimension
name|pageDimension
parameter_list|)
throws|throws
name|IOException
block|{
name|graphics
operator|=
operator|(
name|Graphics2D
operator|)
name|g
expr_stmt|;
name|page
operator|=
name|p
expr_stmt|;
name|pageSize
operator|=
name|pageDimension
expr_stmt|;
name|graphics
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_ANTIALIASING
argument_list|,
name|RenderingHints
operator|.
name|VALUE_ANTIALIAS_ON
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_FRACTIONALMETRICS
argument_list|,
name|RenderingHints
operator|.
name|VALUE_FRACTIONALMETRICS_ON
argument_list|)
expr_stmt|;
comment|// Only if there is some content, we have to process it.
comment|// Otherwise we are done here and we will produce an empty page
if|if
condition|(
name|page
operator|.
name|getContents
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|PDResources
name|resources
init|=
name|page
operator|.
name|findResources
argument_list|()
decl_stmt|;
name|processStream
argument_list|(
name|page
argument_list|,
name|resources
argument_list|,
name|page
operator|.
name|getContents
argument_list|()
operator|.
name|getStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
name|annotations
init|=
name|page
operator|.
name|getAnnotations
argument_list|()
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
name|annotations
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PDAnnotation
name|annot
init|=
operator|(
name|PDAnnotation
operator|)
name|annotations
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|PDRectangle
name|rect
init|=
name|annot
operator|.
name|getRectangle
argument_list|()
decl_stmt|;
name|String
name|appearanceName
init|=
name|annot
operator|.
name|getAppearanceStream
argument_list|()
decl_stmt|;
name|PDAppearanceDictionary
name|appearDictionary
init|=
name|annot
operator|.
name|getAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|appearDictionary
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|appearanceName
operator|==
literal|null
condition|)
block|{
name|appearanceName
operator|=
literal|"default"
expr_stmt|;
block|}
name|Map
name|appearanceMap
init|=
name|appearDictionary
operator|.
name|getNormalAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|appearanceMap
operator|!=
literal|null
condition|)
block|{
name|PDAppearanceStream
name|appearance
init|=
operator|(
name|PDAppearanceStream
operator|)
name|appearanceMap
operator|.
name|get
argument_list|(
name|appearanceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|appearance
operator|!=
literal|null
condition|)
block|{
name|g
operator|.
name|translate
argument_list|(
operator|(
name|int
operator|)
name|rect
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
operator|(
name|int
operator|)
operator|-
name|rect
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
expr_stmt|;
name|processSubStream
argument_list|(
name|page
argument_list|,
name|appearance
operator|.
name|getResources
argument_list|()
argument_list|,
name|appearance
operator|.
name|getStream
argument_list|()
argument_list|)
expr_stmt|;
name|g
operator|.
name|translate
argument_list|(
operator|(
name|int
operator|)
operator|-
name|rect
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
operator|(
name|int
operator|)
operator|+
name|rect
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
comment|/**      * You should override this method if you want to perform an action when a      * text is being processed.      *      * @param text The text to process      */
specifier|protected
name|void
name|processTextPosition
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
try|try
block|{
switch|switch
condition|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|getRenderingMode
argument_list|()
condition|)
block|{
case|case
name|PDTextState
operator|.
name|RENDERING_MODE_FILL_TEXT
case|:
name|graphics
operator|.
name|setComposite
argument_list|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokeJavaComposite
argument_list|()
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setColor
argument_list|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
operator|.
name|getJavaColor
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDTextState
operator|.
name|RENDERING_MODE_STROKE_TEXT
case|:
name|graphics
operator|.
name|setComposite
argument_list|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getStrokeJavaComposite
argument_list|()
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setColor
argument_list|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColor
argument_list|()
operator|.
name|getJavaColor
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDTextState
operator|.
name|RENDERING_MODE_NEITHER_FILL_NOR_STROKE_TEXT
case|:
comment|//basic support for text rendering mode "invisible"
name|Color
name|nsc
init|=
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColor
argument_list|()
operator|.
name|getJavaColor
argument_list|()
decl_stmt|;
name|float
index|[]
name|components
init|=
block|{
name|Color
operator|.
name|black
operator|.
name|getRed
argument_list|()
block|,
name|Color
operator|.
name|black
operator|.
name|getGreen
argument_list|()
block|,
name|Color
operator|.
name|black
operator|.
name|getBlue
argument_list|()
block|}
decl_stmt|;
name|Color
name|c
init|=
operator|new
name|Color
argument_list|(
name|nsc
operator|.
name|getColorSpace
argument_list|()
argument_list|,
name|components
argument_list|,
literal|0f
argument_list|)
decl_stmt|;
name|graphics
operator|.
name|setComposite
argument_list|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getStrokeJavaComposite
argument_list|()
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setColor
argument_list|(
name|c
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// TODO : need to implement....
name|log
operator|.
name|debug
argument_list|(
literal|"Unsupported RenderingMode "
operator|+
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|getRenderingMode
argument_list|()
operator|+
literal|" in PageDrawer.processTextPosition()."
operator|+
literal|" Using RenderingMode "
operator|+
name|PDTextState
operator|.
name|RENDERING_MODE_FILL_TEXT
operator|+
literal|" instead"
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setComposite
argument_list|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokeJavaComposite
argument_list|()
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setColor
argument_list|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
operator|.
name|getJavaColor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|PDFont
name|font
init|=
name|text
operator|.
name|getFont
argument_list|()
decl_stmt|;
name|Matrix
name|textPos
init|=
name|text
operator|.
name|getTextPos
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
name|float
name|x
init|=
name|textPos
operator|.
name|getXPosition
argument_list|()
decl_stmt|;
comment|// the 0,0-reference has to be moved from the lower left (PDF) to the upper left (AWT-graphics)
name|float
name|y
init|=
name|pageSize
operator|.
name|height
operator|-
name|textPos
operator|.
name|getYPosition
argument_list|()
decl_stmt|;
comment|// Set translation to 0,0. We only need the scaling and shearing
name|textPos
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
name|textPos
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// because of the moved 0,0-reference, we have to shear in the opposite direction
name|textPos
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
operator|(
operator|-
literal|1
operator|)
operator|*
name|textPos
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|textPos
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
operator|(
operator|-
literal|1
operator|)
operator|*
name|textPos
operator|.
name|getValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|AffineTransform
name|at
init|=
name|textPos
operator|.
name|createAffineTransform
argument_list|()
decl_stmt|;
name|PDMatrix
name|fontMatrix
init|=
name|font
operator|.
name|getFontMatrix
argument_list|()
decl_stmt|;
name|at
operator|.
name|scale
argument_list|(
name|fontMatrix
operator|.
name|getValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
operator|*
literal|1000f
argument_list|,
name|fontMatrix
operator|.
name|getValue
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
operator|*
literal|1000f
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setClip
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentClippingPath
argument_list|()
argument_list|)
expr_stmt|;
comment|// the fontSize is no longer needed as it is already part of the transformation
comment|// we should remove it from the parameter list in the long run
name|font
operator|.
name|drawString
argument_list|(
name|text
operator|.
name|getCharacter
argument_list|()
argument_list|,
name|graphics
argument_list|,
literal|1
argument_list|,
name|at
argument_list|,
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
name|io
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Get the graphics that we are currently drawing on.      *      * @return The graphics we are drawing on.      */
specifier|public
name|Graphics2D
name|getGraphics
parameter_list|()
block|{
return|return
name|graphics
return|;
block|}
comment|/**      * Get the page that is currently being drawn.      *      * @return The page that is being drawn.      */
specifier|public
name|PDPage
name|getPage
parameter_list|()
block|{
return|return
name|page
return|;
block|}
comment|/**      * Get the size of the page that is currently being drawn.      *      * @return The size of the page that is being drawn.      */
specifier|public
name|Dimension
name|getPageSize
parameter_list|()
block|{
return|return
name|pageSize
return|;
block|}
comment|/**      * Fix the y coordinate.      *      * @param y The y coordinate.      * @return The updated y coordinate.      */
specifier|public
name|double
name|fixY
parameter_list|(
name|double
name|y
parameter_list|)
block|{
return|return
name|pageSize
operator|.
name|getHeight
argument_list|()
operator|-
name|y
return|;
block|}
comment|/**      * Get the current line path to be drawn.      *      * @return The current line path to be drawn.      */
specifier|public
name|GeneralPath
name|getLinePath
parameter_list|()
block|{
return|return
name|linePath
return|;
block|}
comment|/**      * Set the line path to draw.      *      * @param newLinePath Set the line path to draw.      */
specifier|public
name|void
name|setLinePath
parameter_list|(
name|GeneralPath
name|newLinePath
parameter_list|)
block|{
if|if
condition|(
name|linePath
operator|==
literal|null
operator|||
name|linePath
operator|.
name|getCurrentPoint
argument_list|()
operator|==
literal|null
condition|)
block|{
name|linePath
operator|=
name|newLinePath
expr_stmt|;
block|}
else|else
block|{
name|linePath
operator|.
name|append
argument_list|(
name|newLinePath
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Fill the path.      *      * @param windingRule The winding rule this path will use.      *       * @throws IOException If there is an IO error while filling the path.      */
specifier|public
name|void
name|fillPath
parameter_list|(
name|int
name|windingRule
parameter_list|)
throws|throws
name|IOException
block|{
name|graphics
operator|.
name|setComposite
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokeJavaComposite
argument_list|()
argument_list|)
expr_stmt|;
name|Color
name|nonStrokingColor
init|=
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
operator|.
name|getJavaColor
argument_list|()
decl_stmt|;
if|if
condition|(
name|nonStrokingColor
operator|!=
literal|null
condition|)
block|{
name|graphics
operator|.
name|setColor
argument_list|(
name|nonStrokingColor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"ColorSpace "
operator|+
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
operator|.
name|getColorSpace
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" doesn't provide a non-stroking color, using white instead!"
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|WHITE
argument_list|)
expr_stmt|;
block|}
name|getLinePath
argument_list|()
operator|.
name|setWindingRule
argument_list|(
name|windingRule
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_ANTIALIASING
argument_list|,
name|RenderingHints
operator|.
name|VALUE_ANTIALIAS_OFF
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setClip
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentClippingPath
argument_list|()
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|fill
argument_list|(
name|getLinePath
argument_list|()
argument_list|)
expr_stmt|;
name|getLinePath
argument_list|()
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will set the current stroke.      *      * @param newStroke The current stroke.      *       */
specifier|public
name|void
name|setStroke
parameter_list|(
name|BasicStroke
name|newStroke
parameter_list|)
block|{
name|getGraphics
argument_list|()
operator|.
name|setStroke
argument_list|(
name|newStroke
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the current stroke.      *      * @return The current stroke.      *       */
specifier|public
name|BasicStroke
name|getStroke
parameter_list|()
block|{
return|return
operator|(
name|BasicStroke
operator|)
name|getGraphics
argument_list|()
operator|.
name|getStroke
argument_list|()
return|;
block|}
comment|/**      * Stroke the path.      *      * @throws IOException If there is an IO error while stroking the path.      */
specifier|public
name|void
name|strokePath
parameter_list|()
throws|throws
name|IOException
block|{
name|graphics
operator|.
name|setComposite
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getStrokeJavaComposite
argument_list|()
argument_list|)
expr_stmt|;
name|Color
name|strokingColor
init|=
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColor
argument_list|()
operator|.
name|getJavaColor
argument_list|()
decl_stmt|;
if|if
condition|(
name|strokingColor
operator|!=
literal|null
condition|)
block|{
name|graphics
operator|.
name|setColor
argument_list|(
name|strokingColor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|info
argument_list|(
literal|"ColorSpace "
operator|+
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColor
argument_list|()
operator|.
name|getColorSpace
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" doesn't provide a stroking color, using black instead!"
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setColor
argument_list|(
name|Color
operator|.
name|BLACK
argument_list|)
expr_stmt|;
block|}
name|graphics
operator|.
name|setRenderingHint
argument_list|(
name|RenderingHints
operator|.
name|KEY_ANTIALIASING
argument_list|,
name|RenderingHints
operator|.
name|VALUE_ANTIALIAS_OFF
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setClip
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentClippingPath
argument_list|()
argument_list|)
expr_stmt|;
name|GeneralPath
name|path
init|=
name|getLinePath
argument_list|()
decl_stmt|;
name|graphics
operator|.
name|draw
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|path
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
comment|/**      * Called when the color changed.      * @param bStroking true for the stroking color, false for the non-stroking color      * @throws IOException if an I/O error occurs      */
annotation|@
name|Deprecated
specifier|public
name|void
name|colorChanged
parameter_list|(
name|boolean
name|bStroking
parameter_list|)
throws|throws
name|IOException
block|{
comment|//logger().info("changing " + (bStroking ? "" : "non") + "stroking color");
block|}
comment|//This code generalizes the code Jim Lynch wrote for AppendRectangleToPath
comment|/**      * use the current transformation matrix to transform a single point.      * @param x x-coordinate of the point to be transform      * @param y y-coordinate of the point to be transform      * @return the transformed coordinates as Point2D.Double      */
specifier|public
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
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
name|position
index|[
literal|1
index|]
operator|=
name|fixY
argument_list|(
name|position
index|[
literal|1
index|]
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
comment|/**      * Set the clipping Path.      *      * @param windingRule The winding rule this path will use.      *       */
specifier|public
name|void
name|setClippingPath
parameter_list|(
name|int
name|windingRule
parameter_list|)
block|{
name|PDGraphicsState
name|graphicsState
init|=
name|getGraphicsState
argument_list|()
decl_stmt|;
name|GeneralPath
name|clippingPath
init|=
operator|(
name|GeneralPath
operator|)
name|getLinePath
argument_list|()
operator|.
name|clone
argument_list|()
decl_stmt|;
name|clippingPath
operator|.
name|setWindingRule
argument_list|(
name|windingRule
argument_list|)
expr_stmt|;
comment|// If there is already set a clipping path, we have to intersect the new with the existing one
if|if
condition|(
name|graphicsState
operator|.
name|getCurrentClippingPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Area
name|currentArea
init|=
operator|new
name|Area
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentClippingPath
argument_list|()
argument_list|)
decl_stmt|;
name|Area
name|newArea
init|=
operator|new
name|Area
argument_list|(
name|clippingPath
argument_list|)
decl_stmt|;
name|currentArea
operator|.
name|intersect
argument_list|(
name|newArea
argument_list|)
expr_stmt|;
name|graphicsState
operator|.
name|setCurrentClippingPath
argument_list|(
name|currentArea
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|graphicsState
operator|.
name|setCurrentClippingPath
argument_list|(
name|clippingPath
argument_list|)
expr_stmt|;
block|}
name|getLinePath
argument_list|()
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
comment|/**      * Draw the AWT image. Called by Invoke.      * Moved into PageDrawer so that Invoke doesn't have to reach in here for Graphics as that breaks extensibility.      *      * @param awtImage The image to draw.      * @param at The transformation to use when drawing.      *       */
specifier|public
name|void
name|drawImage
parameter_list|(
name|Image
name|awtImage
parameter_list|,
name|AffineTransform
name|at
parameter_list|)
block|{
name|graphics
operator|.
name|setComposite
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getStrokeJavaComposite
argument_list|()
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|setClip
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentClippingPath
argument_list|()
argument_list|)
expr_stmt|;
name|graphics
operator|.
name|drawImage
argument_list|(
name|awtImage
argument_list|,
name|at
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fill with Shading.  Called by SHFill operator.      *      * @param ShadingName  The name of the Shading Dictionary to use for this fill instruction.      *      * @throws IOException If there is an IO error while shade-filling the path/clipping area.      */
specifier|public
name|void
name|SHFill
parameter_list|(
name|COSName
name|ShadingName
parameter_list|)
throws|throws
name|IOException
block|{
name|PDShading
name|Shading
init|=
name|FindShadingDictionary
argument_list|(
name|ShadingName
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Shading = "
operator|+
name|Shading
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|Shading
operator|.
name|getShadingType
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
name|SHFill_Function
argument_list|(
name|Shading
argument_list|)
expr_stmt|;
break|break;
case|case
literal|2
case|:
name|SHFill_Axial
argument_list|(
name|Shading
argument_list|)
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|SHFill_Radial
argument_list|(
name|Shading
argument_list|)
expr_stmt|;
break|break;
case|case
literal|4
case|:
name|SHFill_FreeGourad
argument_list|(
name|Shading
argument_list|)
expr_stmt|;
break|break;
case|case
literal|5
case|:
name|SHFill_LatticeGourad
argument_list|(
name|Shading
argument_list|)
expr_stmt|;
break|break;
case|case
literal|6
case|:
name|SHFill_CoonsPatch
argument_list|(
name|Shading
argument_list|)
expr_stmt|;
break|break;
case|case
literal|7
case|:
name|SHFill_TensorPatch
argument_list|(
name|Shading
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid ShadingType "
operator|+
name|Shading
operator|.
name|getShadingType
argument_list|()
operator|+
literal|" for Shading "
operator|+
name|ShadingName
argument_list|)
throw|;
block|}
block|}
comment|/**      * Find the appropriate Shading Dictionary. This is its own private function as it is really not appropriate to override when deriving from PageDrawer.      *      * @param ShadingName  The name of the Shading Dictionary to use for this fill instruction.      *      * @returns The PDShading object      * @throws IOException If there is an IO error while attempting to find the appropriate PDShading object.      */
specifier|private
name|PDShading
name|FindShadingDictionary
parameter_list|(
name|COSName
name|ShadingName
parameter_list|)
throws|throws
name|IOException
block|{
name|PDResources
name|resources
init|=
operator|(
name|PDResources
operator|)
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
name|COSDictionary
name|AllShadings
init|=
call|(
name|COSDictionary
call|)
argument_list|(
name|resources
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SHADING
argument_list|)
argument_list|)
decl_stmt|;
name|PDShading
name|Shading
init|=
operator|new
name|PDShading
argument_list|(
name|ShadingName
argument_list|,
call|(
name|COSDictionary
call|)
argument_list|(
name|AllShadings
operator|.
name|getDictionaryObject
argument_list|(
name|ShadingName
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Shading
return|;
block|}
comment|/**      * Fill with a Function-based gradient / shading.        * If extending the class, override this and its siblings, not the public SHFill method.      *      * @param Shading  The Shading Dictionary to use for this fill instruction.      *      * @throws IOException If there is an IO error while shade-filling the path/clipping area.      */
specifier|protected
name|void
name|SHFill_Function
parameter_list|(
name|PDShading
name|Shading
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not Implemented"
argument_list|)
throw|;
block|}
comment|/**      * Fill with an Axial Shading.        * If extending the class, override this and its siblings, not the public SHFill method.      *      * @param Shading  The Shading Dictionary to use for this fill instruction.      *      * @throws IOException If there is an IO error while shade-filling the path/clipping area.      */
specifier|protected
name|void
name|SHFill_Axial
parameter_list|(
name|PDShading
name|Shading
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not Implemented"
argument_list|)
throw|;
block|}
comment|/**      * Fill with a Radial gradient / shading.        * If extending the class, override this and its siblings, not the public SHFill method.      *      * @param Shading  The Shading Dictionary to use for this fill instruction.      *      * @throws IOException If there is an IO error while shade-filling the path/clipping area.      */
specifier|protected
name|void
name|SHFill_Radial
parameter_list|(
name|PDShading
name|Shading
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not Implemented"
argument_list|)
throw|;
block|}
comment|/**      * Fill with a Free-form Gourad-shaded triangle mesh.      * If extending the class, override this and its siblings, not the public SHFill method.      *      * @param Shading  The Shading Dictionary to use for this fill instruction.      *      * @throws IOException If there is an IO error while shade-filling the path/clipping area.      */
specifier|protected
name|void
name|SHFill_FreeGourad
parameter_list|(
name|PDShading
name|Shading
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not Implemented"
argument_list|)
throw|;
block|}
comment|/**      * Fill with a Lattice-form Gourad-shaded triangle mesh.      * If extending the class, override this and its siblings, not the public SHFill method.      *      * @param Shading  The Shading Dictionary to use for this fill instruction.      *      * @throws IOException If there is an IO error while shade-filling the path/clipping area.      */
specifier|protected
name|void
name|SHFill_LatticeGourad
parameter_list|(
name|PDShading
name|Shading
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not Implemented"
argument_list|)
throw|;
block|}
comment|/**      * Fill with a Coons patch mesh      * If extending the class, override this and its siblings, not the public SHFill method.      *      * @param Shading  The Shading Dictionary to use for this fill instruction.      *      * @throws IOException If there is an IO error while shade-filling the path/clipping area.      */
specifier|protected
name|void
name|SHFill_CoonsPatch
parameter_list|(
name|PDShading
name|Shading
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not Implemented"
argument_list|)
throw|;
block|}
comment|/**      * Fill with a Tensor-product patch mesh.      * If extending the class, override this and its siblings, not the public SHFill method.      *      * @param Shading  The Shading Dictionary to use for this fill instruction.      *      * @throws IOException If there is an IO error while shade-filling the path/clipping area.      */
specifier|protected
name|void
name|SHFill_TensorPatch
parameter_list|(
name|PDShading
name|Shading
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not Implemented"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

