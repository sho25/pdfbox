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
name|List
name|lineSubPaths
init|=
operator|new
name|ArrayList
argument_list|()
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
literal|"Resources/PageDrawer.properties"
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
comment|// Only if there is some content, we have to process it. Otherwise we are done here and we will produce an empty page
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
comment|//g.translate( 20, -20 );
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
comment|// Transformations should be done in order
comment|// 1 - Translate
comment|// 2 - Rotate
comment|// 3 - Scale
comment|// Refer to PDFReference p176 (or 188 in xpdf)
comment|/*AffineTransform transform = graphics.getTransform();         transform.setToTranslation( 0, page.findMediaBox().getHeight()/2 );         transform.setToRotation((double)p.getRotation());         transform.setTransform( 1, 0, 0, 1, 0, 0 );         transform.setToScale( 1, 1 );          AffineTransform rotation = graphics.getTransform();         rotation.rotate( (page.findRotation() * Math.PI) / 180d );         graphics.setTransform( rotation );*/
block|}
comment|/**      * You should override this method if you want to perform an action when a      * text is being processed.       *      * @param text The text to process       */
specifier|protected
name|void
name|processTextPosition
parameter_list|(
name|TextPosition
name|text
parameter_list|)
block|{
comment|//should use colorspaces for the font color but for now assume that
comment|//the font color is black
try|try
block|{
if|if
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
operator|==
name|PDTextState
operator|.
name|RENDERING_MODE_FILL_TEXT
condition|)
block|{
name|graphics
operator|.
name|setColor
argument_list|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColorSpace
argument_list|()
operator|.
name|createColor
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
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
operator|==
name|PDTextState
operator|.
name|RENDERING_MODE_STROKE_TEXT
condition|)
block|{
name|graphics
operator|.
name|setColor
argument_list|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColorSpace
argument_list|()
operator|.
name|createColor
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO: need to implement....
name|logger
argument_list|()
operator|.
name|warning
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
literal|" in PageDrawer.processTextPosition()"
argument_list|)
expr_stmt|;
name|logger
argument_list|()
operator|.
name|warning
argument_list|(
literal|"Using RenderingMode "
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
name|setColor
argument_list|(
name|this
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColorSpace
argument_list|()
operator|.
name|createColor
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
name|text
operator|.
name|getFontSize
argument_list|()
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
comment|/**      * Fix the y coordinate based on page rotation.      *      * @deprecated      * @param x The x coordinate.      * @param y The y coordinate.      * @return The updated y coordinate.      */
specifier|public
name|double
name|fixY
parameter_list|(
name|double
name|x
parameter_list|,
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
comment|/**      * Fix the y coordinate      *      * @param y The y coordinate.      * @return The updated y coordinate.      */
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
comment|/**      * Get the current list of line paths to be drawn.      *      * @return The current list of line paths to be drawn.      */
specifier|public
name|List
name|getLineSubPaths
parameter_list|()
block|{
return|return
name|lineSubPaths
return|;
block|}
comment|/**      * Set the list of line paths to draw.      *      * @param newLineSubPaths Set the list of line paths to draw.      */
specifier|public
name|void
name|setLineSubPaths
parameter_list|(
name|List
name|newLineSubPaths
parameter_list|)
block|{
name|lineSubPaths
operator|=
name|newLineSubPaths
expr_stmt|;
block|}
comment|/**      *      * Fill the path      *      * @param windingRule The winding rule this path will use.      */
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
name|setColor
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColorSpace
argument_list|()
operator|.
name|createColor
argument_list|()
argument_list|)
expr_stmt|;
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
name|List
name|subPaths
init|=
name|getLineSubPaths
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
name|subPaths
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|GeneralPath
name|subPath
init|=
operator|(
name|GeneralPath
operator|)
name|subPaths
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|subPath
operator|.
name|getCurrentPoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|//Sector9's suggestion in bug 1672556
name|subPath
operator|.
name|closePath
argument_list|()
expr_stmt|;
block|}
name|graphics
operator|.
name|fill
argument_list|(
name|subPath
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|StrokePath
parameter_list|()
throws|throws
name|IOException
block|{
name|graphics
operator|.
name|setColor
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColorSpace
argument_list|()
operator|.
name|createColor
argument_list|()
argument_list|)
expr_stmt|;
comment|//per Ben's 11/15 change in StrokePath.java
name|List
name|subPaths
init|=
name|getLineSubPaths
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
name|subPaths
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|GeneralPath
name|subPath
init|=
operator|(
name|GeneralPath
operator|)
name|subPaths
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|graphics
operator|.
name|draw
argument_list|(
name|subPath
argument_list|)
expr_stmt|;
block|}
name|subPaths
operator|.
name|clear
argument_list|()
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
comment|//If you need to do anything when a color changes, do it here ... or in an override of this function
specifier|public
name|void
name|ColorChanged
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
comment|/**      * use the current transformatrion matrix to transform a single point.      * @param x x-coordinate of the point to be transform      * @param x y-coordinate of the point to be transform      * @return the transformed coordinates as Point2D.Double      */
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
name|TransformedPoint
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
comment|//Use ScaledPoint rather than TransformedPoint in situations where most of the translation
comment|//need not be repeated.
comment|//Consider, for example, the second coordinate of a rectangle.
comment|/**      * @deprecated      */
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
name|ScaledPoint
parameter_list|(
name|double
name|x
parameter_list|,
name|double
name|y
parameter_list|,
name|double
name|scaleX
parameter_list|,
name|double
name|scaleY
parameter_list|)
block|{
name|double
name|finalX
init|=
literal|0.0
decl_stmt|;
name|double
name|finalY
init|=
literal|0.0
decl_stmt|;
if|if
condition|(
name|scaleX
operator|>
literal|0
condition|)
block|{
name|finalX
operator|=
name|x
operator|*
name|scaleX
expr_stmt|;
block|}
if|if
condition|(
name|scaleY
operator|>
literal|0
condition|)
block|{
name|finalY
operator|=
name|y
operator|*
name|scaleY
expr_stmt|;
block|}
return|return
operator|new
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|Point2D
operator|.
name|Double
argument_list|(
name|finalX
argument_list|,
name|finalY
argument_list|)
return|;
block|}
comment|/**      * @deprecated      */
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
name|ScaledPoint
parameter_list|(
name|double
name|x
parameter_list|,
name|double
name|y
parameter_list|)
block|{
name|double
name|scaleX
init|=
literal|0.0
decl_stmt|;
name|double
name|scaleY
init|=
literal|0.0
decl_stmt|;
comment|//Get the transformation matrix
name|Matrix
name|ctm
init|=
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentTransformationMatrix
argument_list|()
decl_stmt|;
name|AffineTransform
name|at
init|=
name|ctm
operator|.
name|createAffineTransform
argument_list|()
decl_stmt|;
name|scaleX
operator|=
name|at
operator|.
name|getScaleX
argument_list|()
expr_stmt|;
name|scaleY
operator|=
name|at
operator|.
name|getScaleY
argument_list|()
expr_stmt|;
return|return
name|ScaledPoint
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|scaleX
argument_list|,
name|scaleY
argument_list|)
return|;
block|}
comment|/**      *      * Fill the path      *      * @param windingRule The winding rule this path will use.      */
specifier|public
name|void
name|SetClippingPath
parameter_list|(
name|int
name|windingRule
parameter_list|)
throws|throws
name|IOException
block|{
name|graphics
operator|.
name|setColor
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColorSpace
argument_list|()
operator|.
name|createColor
argument_list|()
argument_list|)
expr_stmt|;
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
name|List
name|subPaths
init|=
name|getLineSubPaths
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
name|subPaths
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|GeneralPath
name|subPath
init|=
operator|(
name|GeneralPath
operator|)
name|subPaths
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|subPath
operator|.
name|getCurrentPoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|subPath
operator|.
name|closePath
argument_list|()
expr_stmt|;
block|}
block|}
name|graphics
operator|.
name|setClip
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
block|}
end_class

end_unit

