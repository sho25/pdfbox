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
name|graphics
operator|.
name|image
operator|.
name|PDImage
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

begin_comment
comment|/**  * PDFStreamEngine subclass for advanced processing of graphics.  * This class should be subclasses by end users looking to hook into graphics operations.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDFGraphicsStreamEngine
extends|extends
name|PDFStreamEngine
block|{
comment|// may be null, for example if the stream is a tiling pattern
specifier|private
specifier|final
name|PDPage
name|page
decl_stmt|;
comment|/**      * Default constructor, loads properties from file.      *      * @throws java.io.IOException If there is an error loading properties from the file.      */
specifier|protected
name|PDFGraphicsStreamEngine
parameter_list|(
name|PDPage
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|ResourceLoader
operator|.
name|loadProperties
argument_list|(
literal|"org/apache/pdfbox/resources/PDFGraphicsStreamEngine.properties"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|page
operator|=
name|page
expr_stmt|;
block|}
comment|/**      * Returns the page.      */
specifier|protected
specifier|final
name|PDPage
name|getPage
parameter_list|()
block|{
return|return
name|page
return|;
block|}
comment|/**      * Append a rectangle to the current path.      */
specifier|public
specifier|abstract
name|void
name|appendRectangle
parameter_list|(
name|Point2D
name|p0
parameter_list|,
name|Point2D
name|p1
parameter_list|,
name|Point2D
name|p2
parameter_list|,
name|Point2D
name|p3
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Draw the image.      *      * @param pdImage The image to draw.      */
specifier|public
specifier|abstract
name|void
name|drawImage
parameter_list|(
name|PDImage
name|pdImage
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Modify the current clipping path by intersecting it with the current path.      * The clipping path will not be updated until the succeeding painting operator is called.      *      * @param windingRule The winding rule which will be used for clipping.      */
specifier|public
specifier|abstract
name|void
name|clip
parameter_list|(
name|int
name|windingRule
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Starts a new path at (x,y).      */
specifier|public
specifier|abstract
name|void
name|moveTo
parameter_list|(
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Draws a line from the current point to (x,y).      */
specifier|public
specifier|abstract
name|void
name|lineTo
parameter_list|(
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Draws a curve from the current point to (x3,y3) using (x1,y1) and (x2,y2) as control points.      */
specifier|public
specifier|abstract
name|void
name|curveTo
parameter_list|(
name|float
name|x1
parameter_list|,
name|float
name|y1
parameter_list|,
name|float
name|x2
parameter_list|,
name|float
name|y2
parameter_list|,
name|float
name|x3
parameter_list|,
name|float
name|y3
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the current point of the current path.      */
specifier|public
specifier|abstract
name|Point2D
name|getCurrentPoint
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Closes the current path.      */
specifier|public
specifier|abstract
name|void
name|closePath
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Ends the current path without filling or stroking it. The clipping path is updated here.      */
specifier|public
specifier|abstract
name|void
name|endPath
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Stroke the path.      *      * @throws IOException If there is an IO error while stroking the path.      */
specifier|public
specifier|abstract
name|void
name|strokePath
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Fill the path.      *      * @param windingRule The winding rule this path will use.      */
specifier|public
specifier|abstract
name|void
name|fillPath
parameter_list|(
name|int
name|windingRule
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Fills and then strokes the path.      *      * @param windingRule The winding rule this path will use.      */
specifier|public
specifier|abstract
name|void
name|fillAndStrokePath
parameter_list|(
name|int
name|windingRule
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Fill with Shading.      *      * @param shadingName The name of the Shading Dictionary to use for this fill instruction.      */
specifier|public
specifier|abstract
name|void
name|shadingFill
parameter_list|(
name|COSName
name|shadingName
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_class

end_unit

