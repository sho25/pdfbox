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
name|digitalsignature
operator|.
name|visible
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
name|AffineTransform
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
name|BufferedInputStream
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
name|FileInputStream
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
name|pdfparser
operator|.
name|PDFParser
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

begin_comment
comment|/**  * Class for visible signature design properties. Setters use param() instead of setParam() to allow  * chaining.  *  * @author Vakhtang Koroghlishvili  */
end_comment

begin_class
specifier|public
class|class
name|PDVisibleSignDesigner
block|{
specifier|private
name|Float
name|imageWidth
decl_stmt|;
specifier|private
name|Float
name|imageHeight
decl_stmt|;
specifier|private
name|float
name|xAxis
decl_stmt|;
specifier|private
name|float
name|yAxis
decl_stmt|;
specifier|private
name|float
name|pageHeight
decl_stmt|;
specifier|private
name|float
name|pageWidth
decl_stmt|;
specifier|private
name|BufferedImage
name|image
decl_stmt|;
specifier|private
name|String
name|signatureFieldName
init|=
literal|"sig"
decl_stmt|;
specifier|private
name|int
index|[]
name|formatterRectangleParameters
init|=
block|{
literal|0
block|,
literal|0
block|,
literal|100
block|,
literal|50
block|}
decl_stmt|;
specifier|private
name|AffineTransform
name|affineTransform
init|=
operator|new
name|AffineTransform
argument_list|()
decl_stmt|;
specifier|private
name|float
name|imageSizeInPercents
decl_stmt|;
specifier|private
name|int
name|rotation
init|=
literal|0
decl_stmt|;
comment|/**      * Constructor.      *      * @param filename Path of the PDF file      * @param imageStream image as a stream      * @param page The 1-based page number for which the page size should be calculated.      * @throws IOException      */
specifier|public
name|PDVisibleSignDesigner
parameter_list|(
name|String
name|filename
parameter_list|,
name|InputStream
name|imageStream
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
comment|// set visible signature image Input stream
name|readImageStream
argument_list|(
name|imageStream
argument_list|)
expr_stmt|;
comment|// calculate height and width of document page
name|calculatePageSizeFromFile
argument_list|(
name|filename
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param documentStream Original PDF document as stream      * @param imageStream Image as a stream      * @param page The 1-based page number for which the page size should be calculated.      * @throws IOException      */
specifier|public
name|PDVisibleSignDesigner
parameter_list|(
name|InputStream
name|documentStream
parameter_list|,
name|InputStream
name|imageStream
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
comment|// set visible signature image Input stream
name|readImageStream
argument_list|(
name|imageStream
argument_list|)
expr_stmt|;
comment|// calculate height and width of document page
name|calculatePageSizeFromStream
argument_list|(
name|documentStream
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param document Already created PDDocument of your PDF document.      * @param imageStream Image as a stream.      * @param page The 1-based page number for which the page size should be calculated.      * @throws IOException If we can't read, flush, or can't close stream.      */
specifier|public
name|PDVisibleSignDesigner
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|InputStream
name|imageStream
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|readImageStream
argument_list|(
name|imageStream
argument_list|)
expr_stmt|;
name|calculatePageSize
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param filename Path of the PDF file      * @param image      * @param page The 1-based page number for which the page size should be calculated.      * @throws IOException      */
specifier|public
name|PDVisibleSignDesigner
parameter_list|(
name|String
name|filename
parameter_list|,
name|BufferedImage
name|image
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
comment|// set visible signature image
name|setImage
argument_list|(
name|image
argument_list|)
expr_stmt|;
comment|// calculate height and width of document page
name|calculatePageSizeFromFile
argument_list|(
name|filename
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param documentStream Original PDF document as stream      * @param image      * @param page The 1-based page number for which the page size should be calculated.      * @throws IOException      */
specifier|public
name|PDVisibleSignDesigner
parameter_list|(
name|InputStream
name|documentStream
parameter_list|,
name|BufferedImage
name|image
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
comment|// set visible signature image
name|setImage
argument_list|(
name|image
argument_list|)
expr_stmt|;
comment|// calculate height and width of document page
name|calculatePageSizeFromStream
argument_list|(
name|documentStream
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param document Already created PDDocument of your PDF document.      * @param image      * @param page The 1-based page number for which the page size should be calculated.      */
specifier|public
name|PDVisibleSignDesigner
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|BufferedImage
name|image
parameter_list|,
name|int
name|page
parameter_list|)
block|{
name|setImage
argument_list|(
name|image
argument_list|)
expr_stmt|;
name|calculatePageSize
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor usable for signing existing signature fields.      *      * @param imageStream image as a stream      * @throws IOException      */
specifier|public
name|PDVisibleSignDesigner
parameter_list|(
name|InputStream
name|imageStream
parameter_list|)
throws|throws
name|IOException
block|{
comment|// set visible signature image Input stream
name|readImageStream
argument_list|(
name|imageStream
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|calculatePageSizeFromFile
parameter_list|(
name|String
name|filename
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|document
init|=
name|PDFParser
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|filename
argument_list|)
argument_list|)
init|)
block|{
comment|// calculate height and width of document page
name|calculatePageSize
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|calculatePageSizeFromStream
parameter_list|(
name|InputStream
name|documentStream
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|document
init|=
name|PDFParser
operator|.
name|load
argument_list|(
name|documentStream
argument_list|)
init|)
block|{
comment|// calculate height and width of document page
name|calculatePageSize
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Each page of document can be different sizes. This method calculates the page size based on      * the page media box.      *       * @param document      * @param page The 1-based page number for which the page size should be calculated.      * @throws IllegalArgumentException if the page argument is lower than 0.      */
specifier|private
name|void
name|calculatePageSize
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|int
name|page
parameter_list|)
block|{
if|if
condition|(
name|page
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"First page of pdf is 1, not "
operator|+
name|page
argument_list|)
throw|;
block|}
name|PDPage
name|firstPage
init|=
name|document
operator|.
name|getPage
argument_list|(
name|page
operator|-
literal|1
argument_list|)
decl_stmt|;
name|PDRectangle
name|mediaBox
init|=
name|firstPage
operator|.
name|getMediaBox
argument_list|()
decl_stmt|;
name|pageHeight
argument_list|(
name|mediaBox
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|pageWidth
operator|=
name|mediaBox
operator|.
name|getWidth
argument_list|()
expr_stmt|;
name|imageSizeInPercents
operator|=
literal|100
expr_stmt|;
name|rotation
operator|=
name|firstPage
operator|.
name|getRotation
argument_list|()
operator|%
literal|360
expr_stmt|;
block|}
comment|/**      * Adjust signature for page rotation. This is optional, call this after all x and y coordinates      * have been set if you want the signature to be positioned regardless of page orientation.      *      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|adjustForRotation
parameter_list|()
block|{
switch|switch
condition|(
name|rotation
condition|)
block|{
case|case
literal|90
case|:
comment|// https://stackoverflow.com/a/34359956/535646
name|float
name|temp
init|=
name|yAxis
decl_stmt|;
name|yAxis
operator|=
name|pageHeight
operator|-
name|xAxis
operator|-
name|imageWidth
expr_stmt|;
name|xAxis
operator|=
name|temp
expr_stmt|;
name|affineTransform
operator|=
operator|new
name|AffineTransform
argument_list|(
literal|0
argument_list|,
name|imageHeight
operator|/
name|imageWidth
argument_list|,
operator|-
name|imageWidth
operator|/
name|imageHeight
argument_list|,
literal|0
argument_list|,
name|imageWidth
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|temp
operator|=
name|imageHeight
expr_stmt|;
name|imageHeight
operator|=
name|imageWidth
expr_stmt|;
name|imageWidth
operator|=
name|temp
expr_stmt|;
break|break;
case|case
literal|180
case|:
name|float
name|newX
init|=
name|pageWidth
operator|-
name|xAxis
operator|-
name|imageWidth
decl_stmt|;
name|float
name|newY
init|=
name|pageHeight
operator|-
name|yAxis
operator|-
name|imageHeight
decl_stmt|;
name|xAxis
operator|=
name|newX
expr_stmt|;
name|yAxis
operator|=
name|newY
expr_stmt|;
name|affineTransform
operator|=
operator|new
name|AffineTransform
argument_list|(
operator|-
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
operator|-
literal|1
argument_list|,
name|imageWidth
argument_list|,
name|imageHeight
argument_list|)
expr_stmt|;
break|break;
case|case
literal|270
case|:
name|temp
operator|=
name|xAxis
expr_stmt|;
name|xAxis
operator|=
name|pageWidth
operator|-
name|yAxis
operator|-
name|imageHeight
expr_stmt|;
name|yAxis
operator|=
name|temp
expr_stmt|;
name|affineTransform
operator|=
operator|new
name|AffineTransform
argument_list|(
literal|0
argument_list|,
operator|-
name|imageHeight
operator|/
name|imageWidth
argument_list|,
name|imageWidth
operator|/
name|imageHeight
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|imageHeight
argument_list|)
expr_stmt|;
name|temp
operator|=
name|imageHeight
expr_stmt|;
name|imageHeight
operator|=
name|imageWidth
expr_stmt|;
name|imageWidth
operator|=
name|temp
expr_stmt|;
break|break;
case|case
literal|0
case|:
default|default:
break|break;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Set the image for the signature.      *      * @param path Path of the image file.      * @return Visible Signature Configuration Object      * @throws IOException      */
specifier|public
name|PDVisibleSignDesigner
name|signatureImage
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|in
init|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|path
argument_list|)
argument_list|)
init|)
block|{
name|readImageStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Zoom signature image with some percent.      *       * @param percent increase (positive value) or decrease (negative value) image with x percent.      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|zoom
parameter_list|(
name|float
name|percent
parameter_list|)
block|{
name|imageHeight
operator|+=
operator|(
name|imageHeight
operator|*
name|percent
operator|)
operator|/
literal|100
expr_stmt|;
name|imageWidth
operator|+=
operator|(
name|imageWidth
operator|*
name|percent
operator|)
operator|/
literal|100
expr_stmt|;
name|formatterRectangleParameters
index|[
literal|2
index|]
operator|=
operator|(
name|int
operator|)
name|imageWidth
operator|.
name|floatValue
argument_list|()
expr_stmt|;
name|formatterRectangleParameters
index|[
literal|3
index|]
operator|=
operator|(
name|int
operator|)
name|imageHeight
operator|.
name|floatValue
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *      * @param x - x coordinate      * @param y - y coordinate      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|coordinates
parameter_list|(
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|)
block|{
name|xAxis
argument_list|(
name|x
argument_list|)
expr_stmt|;
name|yAxis
argument_list|(
name|y
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *      * @return xAxis - gets x coordinates      */
specifier|public
name|float
name|getxAxis
parameter_list|()
block|{
return|return
name|xAxis
return|;
block|}
comment|/**      *      * @param xAxis  - x coordinate       * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|xAxis
parameter_list|(
name|float
name|xAxis
parameter_list|)
block|{
name|this
operator|.
name|xAxis
operator|=
name|xAxis
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *      * @return yAxis      */
specifier|public
name|float
name|getyAxis
parameter_list|()
block|{
return|return
name|yAxis
return|;
block|}
comment|/**      *      * @param yAxis      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|yAxis
parameter_list|(
name|float
name|yAxis
parameter_list|)
block|{
name|this
operator|.
name|yAxis
operator|=
name|yAxis
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *       * @return signature image width      */
specifier|public
name|float
name|getWidth
parameter_list|()
block|{
return|return
name|imageWidth
return|;
block|}
comment|/**      *       * @param width signature image width      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|width
parameter_list|(
name|float
name|width
parameter_list|)
block|{
name|this
operator|.
name|imageWidth
operator|=
name|width
expr_stmt|;
name|this
operator|.
name|formatterRectangleParameters
index|[
literal|2
index|]
operator|=
operator|(
name|int
operator|)
name|width
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *       * @return signature image height      */
specifier|public
name|float
name|getHeight
parameter_list|()
block|{
return|return
name|imageHeight
return|;
block|}
comment|/**      *       * @param height signature image height      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|height
parameter_list|(
name|float
name|height
parameter_list|)
block|{
name|this
operator|.
name|imageHeight
operator|=
name|height
expr_stmt|;
name|this
operator|.
name|formatterRectangleParameters
index|[
literal|3
index|]
operator|=
operator|(
name|int
operator|)
name|height
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *       * @return template height      */
specifier|protected
name|float
name|getTemplateHeight
parameter_list|()
block|{
return|return
name|getPageHeight
argument_list|()
return|;
block|}
comment|/**      *       * @param templateHeight      * @return Visible Signature Configuration Object      */
specifier|private
name|PDVisibleSignDesigner
name|pageHeight
parameter_list|(
name|float
name|templateHeight
parameter_list|)
block|{
name|this
operator|.
name|pageHeight
operator|=
name|templateHeight
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *       * @return signature field name      */
specifier|public
name|String
name|getSignatureFieldName
parameter_list|()
block|{
return|return
name|signatureFieldName
return|;
block|}
comment|/**      *       * @param signatureFieldName      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|signatureFieldName
parameter_list|(
name|String
name|signatureFieldName
parameter_list|)
block|{
name|this
operator|.
name|signatureFieldName
operator|=
name|signatureFieldName
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *       * @return image Image      */
specifier|public
name|BufferedImage
name|getImage
parameter_list|()
block|{
return|return
name|image
return|;
block|}
comment|/**      * Read the image stream of the signature and set height and width.      *      * @param stream stream of your visible signature image      * @throws IOException If we can't read, flush, or close stream of image      */
specifier|private
name|void
name|readImageStream
parameter_list|(
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
block|{
name|ImageIO
operator|.
name|setUseCache
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setImage
argument_list|(
name|ImageIO
operator|.
name|read
argument_list|(
name|stream
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set image and its height and width.      *      * @param image      */
specifier|private
name|void
name|setImage
parameter_list|(
name|BufferedImage
name|image
parameter_list|)
block|{
name|this
operator|.
name|image
operator|=
name|image
expr_stmt|;
name|imageHeight
operator|=
operator|(
name|float
operator|)
name|image
operator|.
name|getHeight
argument_list|()
expr_stmt|;
name|imageWidth
operator|=
operator|(
name|float
operator|)
name|image
operator|.
name|getWidth
argument_list|()
expr_stmt|;
name|formatterRectangleParameters
index|[
literal|2
index|]
operator|=
name|image
operator|.
name|getWidth
argument_list|()
expr_stmt|;
name|formatterRectangleParameters
index|[
literal|3
index|]
operator|=
name|image
operator|.
name|getHeight
argument_list|()
expr_stmt|;
block|}
comment|/**      * @return Affine Transform parameters for PDF Matrix      */
specifier|public
name|AffineTransform
name|getTransform
parameter_list|()
block|{
return|return
name|affineTransform
return|;
block|}
comment|/**      *       * @param affineTransform      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|transform
parameter_list|(
name|AffineTransform
name|affineTransform
parameter_list|)
block|{
name|this
operator|.
name|affineTransform
operator|=
operator|new
name|AffineTransform
argument_list|(
name|affineTransform
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *       * @return formatter PDRectangle parameters      */
specifier|public
name|int
index|[]
name|getFormatterRectangleParameters
parameter_list|()
block|{
return|return
name|formatterRectangleParameters
return|;
block|}
comment|/**      * Sets formatter PDRectangle      *       * @param formatterRectangleParameters      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|formatterRectangleParameters
parameter_list|(
name|int
index|[]
name|formatterRectangleParameters
parameter_list|)
block|{
name|this
operator|.
name|formatterRectangleParameters
operator|=
name|formatterRectangleParameters
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *       * @return page width      */
specifier|public
name|float
name|getPageWidth
parameter_list|()
block|{
return|return
name|pageWidth
return|;
block|}
comment|/**      *       * @param pageWidth pageWidth      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|pageWidth
parameter_list|(
name|float
name|pageWidth
parameter_list|)
block|{
name|this
operator|.
name|pageWidth
operator|=
name|pageWidth
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *       * @return page height      */
specifier|public
name|float
name|getPageHeight
parameter_list|()
block|{
return|return
name|pageHeight
return|;
block|}
comment|/**      * get image size in percents      * @return the image size in percent      */
specifier|public
name|float
name|getImageSizeInPercents
parameter_list|()
block|{
return|return
name|imageSizeInPercents
return|;
block|}
comment|/**     *      * @param imageSizeInPercents     */
specifier|public
name|void
name|imageSizeInPercents
parameter_list|(
name|float
name|imageSizeInPercents
parameter_list|)
block|{
name|this
operator|.
name|imageSizeInPercents
operator|=
name|imageSizeInPercents
expr_stmt|;
block|}
comment|/**      * returns visible signature text      * @return the visible signature's text      */
specifier|public
name|String
name|getSignatureText
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"That method is not yet implemented"
argument_list|)
throw|;
block|}
comment|/**      *       * @param signatureText - adds the text on visible signature      * @return the signature design      */
specifier|public
name|PDVisibleSignDesigner
name|signatureText
parameter_list|(
name|String
name|signatureText
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"That method is not yet implemented"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

