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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|NumberFormat
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
name|Locale
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
name|graphics
operator|.
name|form
operator|.
name|PDFormXObject
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
name|PDAnnotationRubberStamp
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

begin_comment
comment|/**  * This is an example on how to add a rubber stamp with an image to pages of a PDF document.  */
end_comment

begin_class
specifier|public
class|class
name|RubberStampWithImage
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SAVE_GRAPHICS_STATE
init|=
literal|"q\n"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RESTORE_GRAPHICS_STATE
init|=
literal|"Q\n"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONCATENATE_MATRIX
init|=
literal|"cm\n"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|XOBJECT_DO
init|=
literal|"Do\n"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SPACE
init|=
literal|" "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|NumberFormat
name|formatDecimal
init|=
name|NumberFormat
operator|.
name|getNumberInstance
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
comment|/**      * Add a rubber stamp with an jpg image to every page of the given document.      * @param args the command line arguments      * @throws IOException an exception is thrown if something went wrong      */
specifier|public
name|void
name|doIt
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
literal|3
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
if|if
condition|(
name|document
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Encrypted documents are not supported for this example"
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|document
operator|.
name|getNumberOfPages
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PDPage
name|page
init|=
name|document
operator|.
name|getPage
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|annotations
init|=
name|page
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
name|PDAnnotationRubberStamp
name|rubberStamp
init|=
operator|new
name|PDAnnotationRubberStamp
argument_list|()
decl_stmt|;
name|rubberStamp
operator|.
name|setName
argument_list|(
name|PDAnnotationRubberStamp
operator|.
name|NAME_TOP_SECRET
argument_list|)
expr_stmt|;
name|rubberStamp
operator|.
name|setRectangle
argument_list|(
operator|new
name|PDRectangle
argument_list|(
literal|200
argument_list|,
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|rubberStamp
operator|.
name|setContents
argument_list|(
literal|"A top secret note"
argument_list|)
expr_stmt|;
comment|// create a PDXObjectImage with the given image file
comment|// if you already have the image in a BufferedImage,
comment|// call LosslessFactory.createFromImage() instead
name|PDImageXObject
name|ximage
init|=
name|PDImageXObject
operator|.
name|createFromFile
argument_list|(
name|args
index|[
literal|2
index|]
argument_list|,
name|document
argument_list|)
decl_stmt|;
comment|// define and set the target rectangle
name|int
name|lowerLeftX
init|=
literal|250
decl_stmt|;
name|int
name|lowerLeftY
init|=
literal|550
decl_stmt|;
name|int
name|formWidth
init|=
literal|150
decl_stmt|;
name|int
name|formHeight
init|=
literal|25
decl_stmt|;
name|int
name|imgWidth
init|=
literal|50
decl_stmt|;
name|int
name|imgHeight
init|=
literal|25
decl_stmt|;
name|PDRectangle
name|rect
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|rect
operator|.
name|setLowerLeftX
argument_list|(
name|lowerLeftX
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setLowerLeftY
argument_list|(
name|lowerLeftY
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightX
argument_list|(
name|lowerLeftX
operator|+
name|formWidth
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightY
argument_list|(
name|lowerLeftY
operator|+
name|formHeight
argument_list|)
expr_stmt|;
comment|// Create a PDFormXObject
name|PDFormXObject
name|form
init|=
operator|new
name|PDFormXObject
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|form
operator|.
name|setResources
argument_list|(
operator|new
name|PDResources
argument_list|()
argument_list|)
expr_stmt|;
name|form
operator|.
name|setBBox
argument_list|(
name|rect
argument_list|)
expr_stmt|;
name|form
operator|.
name|setFormType
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// adjust the image to the target rectangle and add it to the stream
name|OutputStream
name|os
init|=
name|form
operator|.
name|getStream
argument_list|()
operator|.
name|createOutputStream
argument_list|()
decl_stmt|;
name|drawXObject
argument_list|(
name|ximage
argument_list|,
name|form
operator|.
name|getResources
argument_list|()
argument_list|,
name|os
argument_list|,
name|lowerLeftX
argument_list|,
name|lowerLeftY
argument_list|,
name|imgWidth
argument_list|,
name|imgHeight
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDAppearanceStream
name|myDic
init|=
operator|new
name|PDAppearanceStream
argument_list|(
name|form
operator|.
name|getCOSStream
argument_list|()
argument_list|)
decl_stmt|;
name|PDAppearanceDictionary
name|appearance
init|=
operator|new
name|PDAppearanceDictionary
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
name|appearance
operator|.
name|setNormalAppearance
argument_list|(
name|myDic
argument_list|)
expr_stmt|;
name|rubberStamp
operator|.
name|setAppearance
argument_list|(
name|appearance
argument_list|)
expr_stmt|;
name|rubberStamp
operator|.
name|setRectangle
argument_list|(
name|rect
argument_list|)
expr_stmt|;
comment|// add the new RubberStamp to the document
name|annotations
operator|.
name|add
argument_list|(
name|rubberStamp
argument_list|)
expr_stmt|;
block|}
name|document
operator|.
name|save
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
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
specifier|private
name|void
name|drawXObject
parameter_list|(
name|PDImageXObject
name|xobject
parameter_list|,
name|PDResources
name|resources
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|,
name|float
name|width
parameter_list|,
name|float
name|height
parameter_list|)
throws|throws
name|IOException
block|{
comment|// This is similar to PDPageContentStream.drawXObject()
name|COSName
name|xObjectId
init|=
name|resources
operator|.
name|add
argument_list|(
name|xobject
argument_list|)
decl_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|SAVE_GRAPHICS_STATE
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|formatDecimal
operator|.
name|format
argument_list|(
name|width
argument_list|)
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|SPACE
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|formatDecimal
operator|.
name|format
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|SPACE
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|formatDecimal
operator|.
name|format
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|SPACE
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|formatDecimal
operator|.
name|format
argument_list|(
name|height
argument_list|)
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|SPACE
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|formatDecimal
operator|.
name|format
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|SPACE
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|formatDecimal
operator|.
name|format
argument_list|(
name|y
argument_list|)
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|SPACE
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|CONCATENATE_MATRIX
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|SPACE
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|xObjectId
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|SPACE
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|XOBJECT_DO
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|SPACE
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|os
argument_list|,
name|RESTORE_GRAPHICS_STATE
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|appendRawCommands
parameter_list|(
name|OutputStream
name|os
parameter_list|,
name|String
name|commands
parameter_list|)
throws|throws
name|IOException
block|{
name|os
operator|.
name|write
argument_list|(
name|commands
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This creates an instance of RubberStampWithImage.      *      * @param args The command line arguments.      *      * @throws IOException If there is an error parsing the document.      */
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
name|RubberStampWithImage
name|rubberStamp
init|=
operator|new
name|RubberStampWithImage
argument_list|()
decl_stmt|;
name|rubberStamp
operator|.
name|doIt
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will print the usage for this example.      */
specifier|private
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
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"<input-pdf><output-pdf><image-filename>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

