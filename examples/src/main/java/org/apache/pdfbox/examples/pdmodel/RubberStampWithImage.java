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
name|exceptions
operator|.
name|COSVisitorException
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
name|common
operator|.
name|PDStream
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
name|xobject
operator|.
name|PDJpeg
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
name|xobject
operator|.
name|PDXObjectForm
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
name|xobject
operator|.
name|PDXObjectImage
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
name|MapUtil
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
name|Locale
import|;
end_import

begin_comment
comment|/**  * This is an example on how to add a rubber stamp with an image to pages of a PDF document.  *  * @version $Revision: 1.0 $  */
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
name|args
index|[
literal|0
index|]
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
name|List
name|allpages
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getPages
argument_list|()
operator|.
name|getAllKids
argument_list|(
name|allpages
argument_list|)
expr_stmt|;
name|int
name|numberOfPages
init|=
name|allpages
operator|.
name|size
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
name|numberOfPages
condition|;
name|i
operator|++
control|)
block|{
name|PDPage
name|apage
init|=
operator|(
name|PDPage
operator|)
name|allpages
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|List
name|annotations
init|=
name|apage
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
literal|100
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
comment|// Create a PDXObjectImage with the given jpg
name|FileInputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
name|args
index|[
literal|2
index|]
argument_list|)
decl_stmt|;
name|PDJpeg
name|mypic
init|=
operator|new
name|PDJpeg
argument_list|(
name|document
argument_list|,
name|fin
argument_list|)
decl_stmt|;
comment|//Define and set the target rectangle
name|PDRectangle
name|myrect
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|myrect
operator|.
name|setUpperRightX
argument_list|(
literal|275
argument_list|)
expr_stmt|;
name|myrect
operator|.
name|setUpperRightY
argument_list|(
literal|575
argument_list|)
expr_stmt|;
name|myrect
operator|.
name|setLowerLeftX
argument_list|(
literal|250
argument_list|)
expr_stmt|;
name|myrect
operator|.
name|setLowerLeftY
argument_list|(
literal|550
argument_list|)
expr_stmt|;
comment|// Create a PDXObjectForm
name|PDStream
name|formstream
init|=
operator|new
name|PDStream
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
name|formstream
operator|.
name|createOutputStream
argument_list|()
decl_stmt|;
name|PDXObjectForm
name|form
init|=
operator|new
name|PDXObjectForm
argument_list|(
name|formstream
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
name|myrect
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
name|drawXObject
argument_list|(
name|mypic
argument_list|,
name|form
operator|.
name|getResources
argument_list|()
argument_list|,
name|os
argument_list|,
literal|250
argument_list|,
literal|550
argument_list|,
literal|25
argument_list|,
literal|25
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
name|myrect
argument_list|)
expr_stmt|;
comment|//Add the new RubberStamp to the document
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
catch|catch
parameter_list|(
name|COSVisitorException
name|exception
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"An error occured during saving the document."
argument_list|)
expr_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Exception:"
operator|+
name|exception
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
name|PDXObjectImage
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
name|String
name|xObjectPrefix
init|=
literal|"Im"
decl_stmt|;
name|String
name|objMapping
init|=
name|MapUtil
operator|.
name|getNextUniqueKey
argument_list|(
name|resources
operator|.
name|getImages
argument_list|()
argument_list|,
name|xObjectPrefix
argument_list|)
decl_stmt|;
name|resources
operator|.
name|getXObjects
argument_list|()
operator|.
name|put
argument_list|(
name|objMapping
argument_list|,
name|xobject
argument_list|)
expr_stmt|;
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
name|objMapping
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
comment|/**      * This creates an instance of RubberStampWithImage.      *      * @param args The command line arguments.      *      * @throws Exception If there is an error parsing the document.      */
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
literal|"<input-pdf><output-pdf><jpeg-filename>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

