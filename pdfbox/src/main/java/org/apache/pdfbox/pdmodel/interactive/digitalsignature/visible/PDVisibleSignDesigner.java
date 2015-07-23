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
comment|/**  * Builder for visible signature design.  * Uses use param() instead of setParam()  *  * @author Vakhtang Koroghlishvili  */
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
comment|// default
specifier|private
name|byte
index|[]
name|formaterRectangleParams
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
comment|// default
specifier|private
name|byte
index|[]
name|AffineTransformParams
init|=
block|{
literal|1
block|,
literal|0
block|,
literal|0
block|,
literal|1
block|,
literal|0
block|,
literal|0
block|}
decl_stmt|;
comment|// default
specifier|private
name|float
name|imageSizeInPercents
decl_stmt|;
comment|/**      * Constructor.      *      * @param filename Path of the PDF file      * @param jpegStream JPEG image as a stream      * @param page The 1-based page number for which the page size should be calculated.      * @throws IOException      */
specifier|public
name|PDVisibleSignDesigner
parameter_list|(
name|String
name|filename
parameter_list|,
name|InputStream
name|jpegStream
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|filename
argument_list|)
argument_list|,
name|jpegStream
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param documentStream Original PDF document as stream      * @param jpegStream JPEG image as a stream      * @param page The 1-based page number for which the page size should be calculated.      * @throws IOException      */
specifier|public
name|PDVisibleSignDesigner
parameter_list|(
name|InputStream
name|documentStream
parameter_list|,
name|InputStream
name|jpegStream
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
comment|// set visible singature image Input stream
name|signatureImageStream
argument_list|(
name|jpegStream
argument_list|)
expr_stmt|;
comment|// create PD document
name|PDDocument
name|document
init|=
name|PDDocument
operator|.
name|load
argument_list|(
name|documentStream
argument_list|)
decl_stmt|;
comment|// calculate height and width of document page
name|calculatePageSize
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
expr_stmt|;
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param doc - Already created PDDocument of your PDF document      * @param jpegStream      * @param page The 1-based page number for which the page size should be calculated.      * @throws IOException - If we can't read, flush, or can't close stream      */
specifier|public
name|PDVisibleSignDesigner
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|jpegStream
parameter_list|,
name|int
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|signatureImageStream
argument_list|(
name|jpegStream
argument_list|)
expr_stmt|;
name|calculatePageSize
argument_list|(
name|doc
argument_list|,
name|page
argument_list|)
expr_stmt|;
block|}
comment|/**      * Each page of document can be different sizes. This method calculates the page size based on      * the page media box.      *       * @param document      * @param page The 1-based page number for which the page size should be calculated.      */
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
name|float
name|x
init|=
name|this
operator|.
name|pageWidth
decl_stmt|;
name|float
name|y
init|=
literal|0
decl_stmt|;
name|pageWidth
operator|=
name|this
operator|.
name|pageWidth
operator|+
name|y
expr_stmt|;
name|float
name|tPercent
init|=
operator|(
literal|100
operator|*
name|y
operator|/
operator|(
name|x
operator|+
name|y
operator|)
operator|)
decl_stmt|;
name|imageSizeInPercents
operator|=
literal|100
operator|-
name|tPercent
expr_stmt|;
block|}
comment|/**      *      * @param path  of image location      * @return image Stream      * @throws IOException      */
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
name|InputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
return|return
name|signatureImageStream
argument_list|(
name|fin
argument_list|)
return|;
block|}
comment|/**      * zoom signature image with some percent.      *       * @param percent increase image with x percent.      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|zoom
parameter_list|(
name|float
name|percent
parameter_list|)
block|{
name|imageHeight
operator|=
name|imageHeight
operator|+
operator|(
name|imageHeight
operator|*
name|percent
operator|)
operator|/
literal|100
expr_stmt|;
name|imageWidth
operator|=
name|imageWidth
operator|+
operator|(
name|imageWidth
operator|*
name|percent
operator|)
operator|/
literal|100
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
comment|/**      *       * @param height signature image Height      * @return Visible Signature Configuration Object      */
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
comment|/**      *       * @param stream stream of your visible signature image      * @return Visible Signature Configuration Object      * @throws IOException If we can't read, flush, or close stream of image      */
specifier|private
name|PDVisibleSignDesigner
name|signatureImageStream
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
name|image
operator|=
name|ImageIO
operator|.
name|read
argument_list|(
name|stream
argument_list|)
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
return|return
name|this
return|;
block|}
comment|/**      *       * @return Affine Transform parameters of for PDF Matrix      */
specifier|public
name|byte
index|[]
name|getAffineTransformParams
parameter_list|()
block|{
return|return
name|AffineTransformParams
return|;
block|}
comment|/**      *       * @param affineTransformParams      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|affineTransformParams
parameter_list|(
name|byte
index|[]
name|affineTransformParams
parameter_list|)
block|{
name|AffineTransformParams
operator|=
name|affineTransformParams
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *       * @return formatter PDRectanle parameters      */
specifier|public
name|byte
index|[]
name|getFormaterRectangleParams
parameter_list|()
block|{
return|return
name|formaterRectangleParams
return|;
block|}
comment|/**      * sets formatter PDRectangle;      *       * @param formaterRectangleParams      * @return Visible Signature Configuration Object      */
specifier|public
name|PDVisibleSignDesigner
name|formaterRectangleParams
parameter_list|(
name|byte
index|[]
name|formaterRectangleParams
parameter_list|)
block|{
name|this
operator|.
name|formaterRectangleParams
operator|=
name|formaterRectangleParams
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

