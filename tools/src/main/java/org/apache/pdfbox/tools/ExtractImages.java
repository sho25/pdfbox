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
name|tools
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
name|Point2D
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|Arrays
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
name|List
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|Loader
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
name|PDFGraphicsStreamEngine
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
name|io
operator|.
name|IOUtils
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
name|encryption
operator|.
name|AccessPermission
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
name|PDDeviceGray
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
name|graphics
operator|.
name|color
operator|.
name|PDPattern
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
name|PDTransparencyGroup
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
name|PDImageXObject
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
name|pattern
operator|.
name|PDAbstractPattern
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
name|pattern
operator|.
name|PDTilingPattern
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
name|PDSoftMask
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
name|RenderingMode
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
name|tools
operator|.
name|imageio
operator|.
name|ImageIOUtil
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
comment|/**  * Extracts the images from a PDF file.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ExtractImages
block|{
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"squid:S2068"
block|}
argument_list|)
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"-password"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PREFIX
init|=
literal|"-prefix"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DIRECTJPEG
init|=
literal|"-directJPEG"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|JPEG
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|COSName
operator|.
name|DCT_DECODE
operator|.
name|getName
argument_list|()
argument_list|,
name|COSName
operator|.
name|DCT_DECODE_ABBREVIATION
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|useDirectJPEG
decl_stmt|;
specifier|private
name|String
name|prefix
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|COSStream
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|int
name|imageCounter
init|=
literal|1
decl_stmt|;
specifier|private
name|ExtractImages
parameter_list|()
block|{     }
comment|/**      * Entry point for the application.      *      * @param args The command-line arguments.      * @throws IOException if there is an error reading the file or extracting the images.      */
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
comment|// suppress the Dock icon on OS X
name|System
operator|.
name|setProperty
argument_list|(
literal|"apple.awt.UIElement"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|ExtractImages
name|extractor
init|=
operator|new
name|ExtractImages
argument_list|()
decl_stmt|;
name|extractor
operator|.
name|run
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|run
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
argument_list|<
literal|1
operator|||
name|args
operator|.
name|length
argument_list|>
literal|4
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|pdfFile
init|=
literal|null
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"squid:S2068"
block|}
argument_list|)
name|String
name|password
init|=
literal|""
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
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
switch|switch
condition|(
name|args
index|[
name|i
index|]
condition|)
block|{
case|case
name|PASSWORD
case|:
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|password
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
break|break;
case|case
name|PREFIX
case|:
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|prefix
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
break|break;
case|case
name|DIRECTJPEG
case|:
name|useDirectJPEG
operator|=
literal|true
expr_stmt|;
break|break;
default|default:
if|if
condition|(
name|pdfFile
operator|==
literal|null
condition|)
block|{
name|pdfFile
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|pdfFile
operator|==
literal|null
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|prefix
operator|==
literal|null
operator|&&
name|pdfFile
operator|.
name|length
argument_list|()
operator|>
literal|4
condition|)
block|{
name|prefix
operator|=
name|pdfFile
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pdfFile
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
name|extract
argument_list|(
name|pdfFile
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Print the usage requirements and exit.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|String
name|message
init|=
literal|"Usage: java "
operator|+
name|ExtractImages
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" [options]<inputfile>\n"
operator|+
literal|"\nOptions:\n"
operator|+
literal|"  -password<password>   : Password to decrypt document\n"
operator|+
literal|"  -prefix<image-prefix> : Image prefix (default to pdf name)\n"
operator|+
literal|"  -directJPEG            : Forces the direct extraction of JPEG/JPX images "
operator|+
literal|"                           regardless of colorspace or masking\n"
operator|+
literal|"<inputfile>            : The PDF document to use\n"
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|message
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
specifier|private
name|void
name|extract
parameter_list|(
name|String
name|pdfFile
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|document
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|pdfFile
argument_list|)
argument_list|,
name|password
argument_list|)
init|)
block|{
name|AccessPermission
name|ap
init|=
name|document
operator|.
name|getCurrentAccessPermission
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ap
operator|.
name|canExtractContent
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"You do not have permission to extract images"
argument_list|)
throw|;
block|}
for|for
control|(
name|PDPage
name|page
range|:
name|document
operator|.
name|getPages
argument_list|()
control|)
block|{
name|ImageGraphicsEngine
name|extractor
init|=
operator|new
name|ImageGraphicsEngine
argument_list|(
name|page
argument_list|)
decl_stmt|;
name|extractor
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
class|class
name|ImageGraphicsEngine
extends|extends
name|PDFGraphicsStreamEngine
block|{
specifier|protected
name|ImageGraphicsEngine
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|super
argument_list|(
name|page
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|IOException
block|{
name|PDPage
name|page
init|=
name|getPage
argument_list|()
decl_stmt|;
name|processPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDResources
name|res
init|=
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|res
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|COSName
name|name
range|:
name|res
operator|.
name|getExtGStateNames
argument_list|()
control|)
block|{
name|PDExtendedGraphicsState
name|extGState
init|=
name|res
operator|.
name|getExtGState
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|extGState
operator|==
literal|null
condition|)
block|{
comment|// can happen if key exists but no value
continue|continue;
block|}
name|PDSoftMask
name|softMask
init|=
name|extGState
operator|.
name|getSoftMask
argument_list|()
decl_stmt|;
if|if
condition|(
name|softMask
operator|!=
literal|null
condition|)
block|{
name|PDTransparencyGroup
name|group
init|=
name|softMask
operator|.
name|getGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
comment|// PDFBOX-4327: without this line NPEs will occur
name|res
operator|.
name|getExtGState
argument_list|(
name|name
argument_list|)
operator|.
name|copyIntoGraphicsState
argument_list|(
name|getGraphicsState
argument_list|()
argument_list|)
expr_stmt|;
name|processSoftMask
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|drawImage
parameter_list|(
name|PDImage
name|pdImage
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|pdImage
operator|instanceof
name|PDImageXObject
condition|)
block|{
if|if
condition|(
name|pdImage
operator|.
name|isStencil
argument_list|()
condition|)
block|{
name|processColor
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|PDImageXObject
name|xobject
init|=
operator|(
name|PDImageXObject
operator|)
name|pdImage
decl_stmt|;
if|if
condition|(
name|seen
operator|.
name|contains
argument_list|(
name|xobject
operator|.
name|getCOSObject
argument_list|()
argument_list|)
condition|)
block|{
comment|// skip duplicate image
return|return;
block|}
name|seen
operator|.
name|add
argument_list|(
name|xobject
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// save image
name|String
name|name
init|=
name|prefix
operator|+
literal|"-"
operator|+
name|imageCounter
decl_stmt|;
name|imageCounter
operator|++
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Writing image: "
operator|+
name|name
argument_list|)
expr_stmt|;
name|write2file
argument_list|(
name|pdImage
argument_list|,
name|name
argument_list|,
name|useDirectJPEG
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{          }
annotation|@
name|Override
specifier|public
name|void
name|clip
parameter_list|(
name|int
name|windingRule
parameter_list|)
throws|throws
name|IOException
block|{          }
annotation|@
name|Override
specifier|public
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
block|{          }
annotation|@
name|Override
specifier|public
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
block|{          }
annotation|@
name|Override
specifier|public
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
block|{          }
annotation|@
name|Override
specifier|public
name|Point2D
name|getCurrentPoint
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|Point2D
operator|.
name|Float
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|closePath
parameter_list|()
throws|throws
name|IOException
block|{          }
annotation|@
name|Override
specifier|public
name|void
name|endPath
parameter_list|()
throws|throws
name|IOException
block|{          }
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
name|RenderingMode
name|renderingMode
init|=
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|getRenderingMode
argument_list|()
decl_stmt|;
if|if
condition|(
name|renderingMode
operator|.
name|isFill
argument_list|()
condition|)
block|{
name|processColor
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|renderingMode
operator|.
name|isStroke
argument_list|()
condition|)
block|{
name|processColor
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|strokePath
parameter_list|()
throws|throws
name|IOException
block|{
name|processColor
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getStrokingColor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|processColor
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|fillAndStrokePath
parameter_list|(
name|int
name|windingRule
parameter_list|)
throws|throws
name|IOException
block|{
name|processColor
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getNonStrokingColor
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|shadingFill
parameter_list|(
name|COSName
name|shadingName
parameter_list|)
throws|throws
name|IOException
block|{          }
comment|// find out if it is a tiling pattern, then process that one
specifier|private
name|void
name|processColor
parameter_list|(
name|PDColor
name|color
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|color
operator|.
name|getColorSpace
argument_list|()
operator|instanceof
name|PDPattern
condition|)
block|{
name|PDPattern
name|pattern
init|=
operator|(
name|PDPattern
operator|)
name|color
operator|.
name|getColorSpace
argument_list|()
decl_stmt|;
name|PDAbstractPattern
name|abstractPattern
init|=
name|pattern
operator|.
name|getPattern
argument_list|(
name|color
argument_list|)
decl_stmt|;
if|if
condition|(
name|abstractPattern
operator|instanceof
name|PDTilingPattern
condition|)
block|{
name|processTilingPattern
argument_list|(
operator|(
name|PDTilingPattern
operator|)
name|abstractPattern
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Writes the image to a file with the filename prefix + an appropriate suffix, like "Image.jpg".      * The suffix is automatically set depending on the image compression in the PDF.      * @param pdImage the image.      * @param prefix the filename prefix.      * @param directJPEG if true, force saving JPEG/JPX streams as they are in the PDF file.       * @throws IOException When something is wrong with the corresponding file.      */
specifier|private
name|void
name|write2file
parameter_list|(
name|PDImage
name|pdImage
parameter_list|,
name|String
name|prefix
parameter_list|,
name|boolean
name|directJPEG
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|suffix
init|=
name|pdImage
operator|.
name|getSuffix
argument_list|()
decl_stmt|;
if|if
condition|(
name|suffix
operator|==
literal|null
operator|||
literal|"jb2"
operator|.
name|equals
argument_list|(
name|suffix
argument_list|)
condition|)
block|{
name|suffix
operator|=
literal|"png"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"jpx"
operator|.
name|equals
argument_list|(
name|suffix
argument_list|)
condition|)
block|{
comment|// use jp2 suffix for file because jpx not known by windows
name|suffix
operator|=
literal|"jp2"
expr_stmt|;
block|}
if|if
condition|(
name|hasMasks
argument_list|(
name|pdImage
argument_list|)
condition|)
block|{
comment|// TIKA-3040, PDFBOX-4771: can't save ARGB as JPEG
name|suffix
operator|=
literal|"png"
expr_stmt|;
block|}
try|try
init|(
name|FileOutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|prefix
operator|+
literal|"."
operator|+
name|suffix
argument_list|)
init|)
block|{
if|if
condition|(
literal|"jpg"
operator|.
name|equals
argument_list|(
name|suffix
argument_list|)
condition|)
block|{
name|String
name|colorSpaceName
init|=
name|pdImage
operator|.
name|getColorSpace
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|directJPEG
operator|||
operator|(
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|colorSpaceName
argument_list|)
operator|||
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|colorSpaceName
argument_list|)
operator|)
condition|)
block|{
comment|// RGB or Gray colorspace: get and write the unmodified JPEG stream
name|InputStream
name|data
init|=
name|pdImage
operator|.
name|createInputStream
argument_list|(
name|JPEG
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|data
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// for CMYK and other "unusual" colorspaces, the JPEG will be converted
name|BufferedImage
name|image
init|=
name|pdImage
operator|.
name|getImage
argument_list|()
decl_stmt|;
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image
argument_list|,
name|suffix
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
literal|"jp2"
operator|.
name|equals
argument_list|(
name|suffix
argument_list|)
condition|)
block|{
name|String
name|colorSpaceName
init|=
name|pdImage
operator|.
name|getColorSpace
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|directJPEG
operator|||
operator|(
name|PDDeviceGray
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|colorSpaceName
argument_list|)
operator|||
name|PDDeviceRGB
operator|.
name|INSTANCE
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|colorSpaceName
argument_list|)
operator|)
condition|)
block|{
comment|// RGB or Gray colorspace: get and write the unmodified JPEG2000 stream
name|InputStream
name|data
init|=
name|pdImage
operator|.
name|createInputStream
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|COSName
operator|.
name|JPX_DECODE
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|data
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// for CMYK and other "unusual" colorspaces, the image will be converted
name|BufferedImage
name|image
init|=
name|pdImage
operator|.
name|getImage
argument_list|()
decl_stmt|;
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image
argument_list|,
literal|"jpeg2000"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
literal|"tiff"
operator|.
name|equals
argument_list|(
name|suffix
argument_list|)
operator|&&
name|pdImage
operator|.
name|getColorSpace
argument_list|()
operator|.
name|equals
argument_list|(
name|PDDeviceGray
operator|.
name|INSTANCE
argument_list|)
condition|)
block|{
name|BufferedImage
name|image
init|=
name|pdImage
operator|.
name|getImage
argument_list|()
decl_stmt|;
if|if
condition|(
name|image
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// CCITT compressed images can have a different colorspace, but this one is B/W
comment|// This is a bitonal image, so copy to TYPE_BYTE_BINARY
comment|// so that a G4 compressed TIFF image is created by ImageIOUtil.writeImage()
name|int
name|w
init|=
name|image
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|h
init|=
name|image
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|BufferedImage
name|bitonalImage
init|=
operator|new
name|BufferedImage
argument_list|(
name|w
argument_list|,
name|h
argument_list|,
name|BufferedImage
operator|.
name|TYPE_BYTE_BINARY
argument_list|)
decl_stmt|;
comment|// copy image the old fashioned way - ColorConvertOp is slower!
for|for
control|(
name|int
name|y
init|=
literal|0
init|;
name|y
operator|<
name|h
condition|;
name|y
operator|++
control|)
block|{
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|w
condition|;
name|x
operator|++
control|)
block|{
name|bitonalImage
operator|.
name|setRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|image
operator|.
name|getRGB
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|bitonalImage
argument_list|,
name|suffix
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|BufferedImage
name|image
init|=
name|pdImage
operator|.
name|getImage
argument_list|()
decl_stmt|;
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image
argument_list|,
name|suffix
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|hasMasks
parameter_list|(
name|PDImage
name|pdImage
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|pdImage
operator|instanceof
name|PDImageXObject
condition|)
block|{
name|PDImageXObject
name|ximg
init|=
operator|(
name|PDImageXObject
operator|)
name|pdImage
decl_stmt|;
return|return
name|ximg
operator|.
name|getMask
argument_list|()
operator|!=
literal|null
operator|||
name|ximg
operator|.
name|getSoftMask
argument_list|()
operator|!=
literal|null
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

