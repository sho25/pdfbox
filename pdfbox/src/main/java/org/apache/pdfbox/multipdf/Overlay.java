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
name|multipdf
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
name|InputStream
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
name|math
operator|.
name|BigDecimal
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
name|HashMap
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
name|cos
operator|.
name|COSArray
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
name|COSBase
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
name|cos
operator|.
name|COSObject
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

begin_comment
comment|/**  * Adds an overlay to an existing PDF document.  *   * Based on code contributed by Balazs Jerk.  *   */
end_comment

begin_class
specifier|public
class|class
name|Overlay
block|{
comment|/**      * Possible location of the overlayed pages: foreground or background.      */
specifier|public
enum|enum
name|Position
block|{
name|FOREGROUND
block|,
name|BACKGROUND
block|}
empty_stmt|;
specifier|private
name|LayoutPage
name|defaultOverlayPage
decl_stmt|;
specifier|private
name|LayoutPage
name|firstPageOverlayPage
decl_stmt|;
specifier|private
name|LayoutPage
name|lastPageOverlayPage
decl_stmt|;
specifier|private
name|LayoutPage
name|oddPageOverlayPage
decl_stmt|;
specifier|private
name|LayoutPage
name|evenPageOverlayPage
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|PDDocument
argument_list|>
name|specificPageOverlay
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|PDDocument
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|LayoutPage
argument_list|>
name|specificPageOverlayPage
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|LayoutPage
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Position
name|position
init|=
name|Position
operator|.
name|BACKGROUND
decl_stmt|;
specifier|private
name|String
name|inputFileName
init|=
literal|null
decl_stmt|;
specifier|private
name|PDDocument
name|inputPDFDocument
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|outputFilename
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|defaultOverlayFilename
init|=
literal|null
decl_stmt|;
specifier|private
name|PDDocument
name|defaultOverlay
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|firstPageOverlayFilename
init|=
literal|null
decl_stmt|;
specifier|private
name|PDDocument
name|firstPageOverlay
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|lastPageOverlayFilename
init|=
literal|null
decl_stmt|;
specifier|private
name|PDDocument
name|lastPageOverlay
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|allPagesOverlayFilename
init|=
literal|null
decl_stmt|;
specifier|private
name|PDDocument
name|allPagesOverlay
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|oddPageOverlayFilename
init|=
literal|null
decl_stmt|;
specifier|private
name|PDDocument
name|oddPageOverlay
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|evenPageOverlayFilename
init|=
literal|null
decl_stmt|;
specifier|private
name|PDDocument
name|evenPageOverlay
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|numberOfOverlayPages
init|=
literal|0
decl_stmt|;
specifier|private
name|boolean
name|useAllOverlayPages
init|=
literal|false
decl_stmt|;
comment|/**      * This will add overlays to a documents.      *       * @param specificPageOverlayFile map of overlay files for specific pages      * @throws IOException if something went wrong      */
specifier|public
name|void
name|overlay
parameter_list|(
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|specificPageOverlayFile
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|loadPDFs
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|e
range|:
name|specificPageOverlayFile
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|PDDocument
name|doc
init|=
name|loadPDF
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|specificPageOverlay
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|specificPageOverlayPage
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|getLayoutPage
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|processPages
argument_list|(
name|inputPDFDocument
argument_list|)
expr_stmt|;
name|inputPDFDocument
operator|.
name|save
argument_list|(
name|outputFilename
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|inputPDFDocument
operator|!=
literal|null
condition|)
block|{
name|inputPDFDocument
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|defaultOverlay
operator|!=
literal|null
condition|)
block|{
name|defaultOverlay
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|firstPageOverlay
operator|!=
literal|null
condition|)
block|{
name|firstPageOverlay
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|lastPageOverlay
operator|!=
literal|null
condition|)
block|{
name|lastPageOverlay
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|allPagesOverlay
operator|!=
literal|null
condition|)
block|{
name|allPagesOverlay
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|oddPageOverlay
operator|!=
literal|null
condition|)
block|{
name|oddPageOverlay
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|evenPageOverlay
operator|!=
literal|null
condition|)
block|{
name|evenPageOverlay
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|PDDocument
argument_list|>
name|e
range|:
name|specificPageOverlay
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|specificPageOverlay
operator|.
name|clear
argument_list|()
expr_stmt|;
name|specificPageOverlayPage
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|loadPDFs
parameter_list|()
throws|throws
name|IOException
block|{
comment|// input PDF
if|if
condition|(
name|inputFileName
operator|!=
literal|null
condition|)
block|{
name|inputPDFDocument
operator|=
name|loadPDF
argument_list|(
name|inputFileName
argument_list|)
expr_stmt|;
block|}
comment|// default overlay PDF
if|if
condition|(
name|defaultOverlayFilename
operator|!=
literal|null
condition|)
block|{
name|defaultOverlay
operator|=
name|loadPDF
argument_list|(
name|defaultOverlayFilename
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|defaultOverlay
operator|!=
literal|null
condition|)
block|{
name|defaultOverlayPage
operator|=
name|getLayoutPage
argument_list|(
name|defaultOverlay
argument_list|)
expr_stmt|;
block|}
comment|// first page overlay PDF
if|if
condition|(
name|firstPageOverlayFilename
operator|!=
literal|null
condition|)
block|{
name|firstPageOverlay
operator|=
name|loadPDF
argument_list|(
name|firstPageOverlayFilename
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|firstPageOverlay
operator|!=
literal|null
condition|)
block|{
name|firstPageOverlayPage
operator|=
name|getLayoutPage
argument_list|(
name|firstPageOverlay
argument_list|)
expr_stmt|;
block|}
comment|// last page overlay PDF
if|if
condition|(
name|lastPageOverlayFilename
operator|!=
literal|null
condition|)
block|{
name|lastPageOverlay
operator|=
name|loadPDF
argument_list|(
name|lastPageOverlayFilename
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|lastPageOverlay
operator|!=
literal|null
condition|)
block|{
name|lastPageOverlayPage
operator|=
name|getLayoutPage
argument_list|(
name|lastPageOverlay
argument_list|)
expr_stmt|;
block|}
comment|// odd pages overlay PDF
if|if
condition|(
name|oddPageOverlayFilename
operator|!=
literal|null
condition|)
block|{
name|oddPageOverlay
operator|=
name|loadPDF
argument_list|(
name|oddPageOverlayFilename
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|oddPageOverlay
operator|!=
literal|null
condition|)
block|{
name|oddPageOverlayPage
operator|=
name|getLayoutPage
argument_list|(
name|oddPageOverlay
argument_list|)
expr_stmt|;
block|}
comment|// even pages overlay PDF
if|if
condition|(
name|evenPageOverlayFilename
operator|!=
literal|null
condition|)
block|{
name|evenPageOverlay
operator|=
name|loadPDF
argument_list|(
name|evenPageOverlayFilename
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|evenPageOverlay
operator|!=
literal|null
condition|)
block|{
name|evenPageOverlayPage
operator|=
name|getLayoutPage
argument_list|(
name|evenPageOverlay
argument_list|)
expr_stmt|;
block|}
comment|// all pages overlay PDF
if|if
condition|(
name|allPagesOverlayFilename
operator|!=
literal|null
condition|)
block|{
name|allPagesOverlay
operator|=
name|loadPDF
argument_list|(
name|allPagesOverlayFilename
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|allPagesOverlay
operator|!=
literal|null
condition|)
block|{
name|specificPageOverlayPage
operator|=
name|getLayoutPages
argument_list|(
name|allPagesOverlay
argument_list|)
expr_stmt|;
name|useAllOverlayPages
operator|=
literal|true
expr_stmt|;
name|numberOfOverlayPages
operator|=
name|specificPageOverlayPage
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|PDDocument
name|loadPDF
parameter_list|(
name|String
name|pdfName
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|pdfName
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Stores the overlay page information.      */
specifier|private
specifier|static
specifier|final
class|class
name|LayoutPage
block|{
specifier|private
specifier|final
name|PDRectangle
name|overlayMediaBox
decl_stmt|;
specifier|private
specifier|final
name|COSStream
name|overlayContentStream
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|overlayResources
decl_stmt|;
specifier|private
name|LayoutPage
parameter_list|(
name|PDRectangle
name|mediaBox
parameter_list|,
name|COSStream
name|contentStream
parameter_list|,
name|COSDictionary
name|resources
parameter_list|)
block|{
name|overlayMediaBox
operator|=
name|mediaBox
expr_stmt|;
name|overlayContentStream
operator|=
name|contentStream
expr_stmt|;
name|overlayResources
operator|=
name|resources
expr_stmt|;
block|}
block|}
specifier|private
name|LayoutPage
name|getLayoutPage
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
name|PDPage
name|page
init|=
name|doc
operator|.
name|getPage
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|COSBase
name|contents
init|=
name|page
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|)
decl_stmt|;
name|PDResources
name|resources
init|=
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
name|resources
operator|=
operator|new
name|PDResources
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|LayoutPage
argument_list|(
name|page
operator|.
name|getMediaBox
argument_list|()
argument_list|,
name|createContentStream
argument_list|(
name|contents
argument_list|)
argument_list|,
name|resources
operator|.
name|getCOSObject
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|LayoutPage
argument_list|>
name|getLayoutPages
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|numberOfPages
init|=
name|doc
operator|.
name|getNumberOfPages
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|LayoutPage
argument_list|>
name|layoutPages
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Overlay
operator|.
name|LayoutPage
argument_list|>
argument_list|(
name|numberOfPages
argument_list|)
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
name|page
init|=
name|doc
operator|.
name|getPage
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|COSBase
name|contents
init|=
name|page
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|)
decl_stmt|;
name|PDResources
name|resources
init|=
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
name|resources
operator|=
operator|new
name|PDResources
argument_list|()
expr_stmt|;
block|}
name|layoutPages
operator|.
name|put
argument_list|(
name|i
argument_list|,
operator|new
name|LayoutPage
argument_list|(
name|page
operator|.
name|getMediaBox
argument_list|()
argument_list|,
name|createContentStream
argument_list|(
name|contents
argument_list|)
argument_list|,
name|resources
operator|.
name|getCOSObject
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|layoutPages
return|;
block|}
specifier|private
name|COSStream
name|createContentStream
parameter_list|(
name|COSBase
name|contents
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|COSStream
argument_list|>
name|contentStreams
init|=
name|createContentStreamList
argument_list|(
name|contents
argument_list|)
decl_stmt|;
comment|// concatenate streams
name|COSStream
name|concatStream
init|=
operator|new
name|COSStream
argument_list|()
decl_stmt|;
name|OutputStream
name|out
init|=
name|concatStream
operator|.
name|createOutputStream
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
decl_stmt|;
for|for
control|(
name|COSStream
name|contentStream
range|:
name|contentStreams
control|)
block|{
name|InputStream
name|in
init|=
name|contentStream
operator|.
name|createInputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|2048
index|]
decl_stmt|;
name|int
name|n
decl_stmt|;
while|while
condition|(
operator|(
name|n
operator|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|concatStream
return|;
block|}
specifier|private
name|List
argument_list|<
name|COSStream
argument_list|>
name|createContentStreamList
parameter_list|(
name|COSBase
name|contents
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|COSStream
argument_list|>
name|contentStreams
init|=
operator|new
name|ArrayList
argument_list|<
name|COSStream
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|contents
operator|instanceof
name|COSStream
condition|)
block|{
name|contentStreams
operator|.
name|add
argument_list|(
operator|(
name|COSStream
operator|)
name|contents
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|contents
operator|instanceof
name|COSArray
condition|)
block|{
for|for
control|(
name|COSBase
name|item
range|:
operator|(
name|COSArray
operator|)
name|contents
control|)
block|{
name|contentStreams
operator|.
name|addAll
argument_list|(
name|createContentStreamList
argument_list|(
name|item
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|contents
operator|instanceof
name|COSObject
condition|)
block|{
name|contentStreams
operator|.
name|addAll
argument_list|(
name|createContentStreamList
argument_list|(
operator|(
operator|(
name|COSObject
operator|)
name|contents
operator|)
operator|.
name|getObject
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Contents are unknown type:"
operator|+
name|contents
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|contentStreams
return|;
block|}
specifier|private
name|void
name|processPages
parameter_list|(
name|PDDocument
name|document
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|pageCount
init|=
literal|0
decl_stmt|;
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
name|COSDictionary
name|pageDictionary
init|=
name|page
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|COSBase
name|contents
init|=
name|pageDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|)
decl_stmt|;
name|COSArray
name|contentArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|position
condition|)
block|{
case|case
name|FOREGROUND
case|:
comment|// save state
name|contentArray
operator|.
name|add
argument_list|(
name|createStream
argument_list|(
literal|"q\n"
argument_list|)
argument_list|)
expr_stmt|;
name|addOriginalContent
argument_list|(
name|contents
argument_list|,
name|contentArray
argument_list|)
expr_stmt|;
comment|// restore state
name|contentArray
operator|.
name|add
argument_list|(
name|createStream
argument_list|(
literal|"Q\n"
argument_list|)
argument_list|)
expr_stmt|;
comment|// overlay content
name|overlayPage
argument_list|(
name|contentArray
argument_list|,
name|page
argument_list|,
name|pageCount
operator|+
literal|1
argument_list|,
name|document
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|BACKGROUND
case|:
comment|// overlay content
name|overlayPage
argument_list|(
name|contentArray
argument_list|,
name|page
argument_list|,
name|pageCount
operator|+
literal|1
argument_list|,
name|document
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|addOriginalContent
argument_list|(
name|contents
argument_list|,
name|contentArray
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown type of position:"
operator|+
name|position
argument_list|)
throw|;
block|}
name|pageDictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|,
name|contentArray
argument_list|)
expr_stmt|;
name|pageCount
operator|++
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addOriginalContent
parameter_list|(
name|COSBase
name|contents
parameter_list|,
name|COSArray
name|contentArray
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|contents
operator|instanceof
name|COSStream
condition|)
block|{
name|contentArray
operator|.
name|add
argument_list|(
name|contents
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|contents
operator|instanceof
name|COSArray
condition|)
block|{
name|contentArray
operator|.
name|addAll
argument_list|(
operator|(
name|COSArray
operator|)
name|contents
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown content type:"
operator|+
name|contents
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|overlayPage
parameter_list|(
name|COSArray
name|array
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|int
name|pageNumber
parameter_list|,
name|int
name|numberOfPages
parameter_list|)
throws|throws
name|IOException
block|{
name|LayoutPage
name|layoutPage
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|useAllOverlayPages
operator|&&
name|specificPageOverlayPage
operator|.
name|containsKey
argument_list|(
name|pageNumber
argument_list|)
condition|)
block|{
name|layoutPage
operator|=
name|specificPageOverlayPage
operator|.
name|get
argument_list|(
name|pageNumber
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|pageNumber
operator|==
literal|1
operator|)
operator|&&
operator|(
name|firstPageOverlayPage
operator|!=
literal|null
operator|)
condition|)
block|{
name|layoutPage
operator|=
name|firstPageOverlayPage
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|pageNumber
operator|==
name|numberOfPages
operator|)
operator|&&
operator|(
name|lastPageOverlayPage
operator|!=
literal|null
operator|)
condition|)
block|{
name|layoutPage
operator|=
name|lastPageOverlayPage
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|pageNumber
operator|%
literal|2
operator|==
literal|1
operator|)
operator|&&
operator|(
name|oddPageOverlayPage
operator|!=
literal|null
operator|)
condition|)
block|{
name|layoutPage
operator|=
name|oddPageOverlayPage
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|pageNumber
operator|%
literal|2
operator|==
literal|0
operator|)
operator|&&
operator|(
name|evenPageOverlayPage
operator|!=
literal|null
operator|)
condition|)
block|{
name|layoutPage
operator|=
name|evenPageOverlayPage
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|defaultOverlayPage
operator|!=
literal|null
condition|)
block|{
name|layoutPage
operator|=
name|defaultOverlayPage
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|useAllOverlayPages
condition|)
block|{
name|int
name|usePageNum
init|=
operator|(
name|pageNumber
operator|-
literal|1
operator|)
operator|%
name|numberOfOverlayPages
decl_stmt|;
name|layoutPage
operator|=
name|specificPageOverlayPage
operator|.
name|get
argument_list|(
name|usePageNum
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|layoutPage
operator|!=
literal|null
condition|)
block|{
name|PDResources
name|resources
init|=
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
name|resources
operator|=
operator|new
name|PDResources
argument_list|()
expr_stmt|;
name|page
operator|.
name|setResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
name|COSName
name|xObjectId
init|=
name|createOverlayXObject
argument_list|(
name|page
argument_list|,
name|layoutPage
argument_list|,
name|layoutPage
operator|.
name|overlayContentStream
argument_list|)
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|createOverlayStream
argument_list|(
name|page
argument_list|,
name|layoutPage
argument_list|,
name|xObjectId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|COSName
name|createOverlayXObject
parameter_list|(
name|PDPage
name|page
parameter_list|,
name|LayoutPage
name|layoutPage
parameter_list|,
name|COSStream
name|contentStream
parameter_list|)
block|{
name|PDFormXObject
name|xobjForm
init|=
operator|new
name|PDFormXObject
argument_list|(
name|contentStream
argument_list|)
decl_stmt|;
name|xobjForm
operator|.
name|setResources
argument_list|(
operator|new
name|PDResources
argument_list|(
name|layoutPage
operator|.
name|overlayResources
argument_list|)
argument_list|)
expr_stmt|;
name|xobjForm
operator|.
name|setFormType
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|xobjForm
operator|.
name|setBBox
argument_list|(
name|layoutPage
operator|.
name|overlayMediaBox
operator|.
name|createRetranslatedRectangle
argument_list|()
argument_list|)
expr_stmt|;
name|xobjForm
operator|.
name|setMatrix
argument_list|(
operator|new
name|AffineTransform
argument_list|()
argument_list|)
expr_stmt|;
name|PDResources
name|resources
init|=
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
return|return
name|resources
operator|.
name|add
argument_list|(
name|xobjForm
argument_list|,
literal|"OL"
argument_list|)
return|;
block|}
specifier|private
name|COSStream
name|createOverlayStream
parameter_list|(
name|PDPage
name|page
parameter_list|,
name|LayoutPage
name|layoutPage
parameter_list|,
name|COSName
name|xObjectId
parameter_list|)
throws|throws
name|IOException
block|{
comment|// create a new content stream that executes the XObject content
name|PDRectangle
name|pageMediaBox
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
decl_stmt|;
name|float
name|hShift
init|=
operator|(
name|pageMediaBox
operator|.
name|getWidth
argument_list|()
operator|-
name|layoutPage
operator|.
name|overlayMediaBox
operator|.
name|getWidth
argument_list|()
operator|)
operator|/
literal|2.0f
decl_stmt|;
name|float
name|vShift
init|=
operator|(
name|pageMediaBox
operator|.
name|getHeight
argument_list|()
operator|-
name|layoutPage
operator|.
name|overlayMediaBox
operator|.
name|getHeight
argument_list|()
operator|)
operator|/
literal|2.0f
decl_stmt|;
name|StringBuilder
name|overlayStream
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|overlayStream
operator|.
name|append
argument_list|(
literal|"q\nq 1 0 0 1 "
argument_list|)
expr_stmt|;
name|overlayStream
operator|.
name|append
argument_list|(
name|float2String
argument_list|(
name|hShift
argument_list|)
argument_list|)
expr_stmt|;
name|overlayStream
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|overlayStream
operator|.
name|append
argument_list|(
name|float2String
argument_list|(
name|vShift
argument_list|)
argument_list|)
expr_stmt|;
name|overlayStream
operator|.
name|append
argument_list|(
literal|" cm /"
argument_list|)
expr_stmt|;
name|overlayStream
operator|.
name|append
argument_list|(
name|xObjectId
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|overlayStream
operator|.
name|append
argument_list|(
literal|" Do Q\nQ\n"
argument_list|)
expr_stmt|;
return|return
name|createStream
argument_list|(
name|overlayStream
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|float2String
parameter_list|(
name|float
name|floatValue
parameter_list|)
block|{
comment|// use a BigDecimal as intermediate state to avoid
comment|// a floating point string representation of the float value
name|BigDecimal
name|value
init|=
operator|new
name|BigDecimal
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|floatValue
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|stringValue
init|=
name|value
operator|.
name|toPlainString
argument_list|()
decl_stmt|;
comment|// remove fraction digit "0" only
if|if
condition|(
name|stringValue
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
operator|>
operator|-
literal|1
operator|&&
operator|!
name|stringValue
operator|.
name|endsWith
argument_list|(
literal|".0"
argument_list|)
condition|)
block|{
while|while
condition|(
name|stringValue
operator|.
name|endsWith
argument_list|(
literal|"0"
argument_list|)
operator|&&
operator|!
name|stringValue
operator|.
name|endsWith
argument_list|(
literal|".0"
argument_list|)
condition|)
block|{
name|stringValue
operator|=
name|stringValue
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|stringValue
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|stringValue
return|;
block|}
specifier|private
name|COSStream
name|createStream
parameter_list|(
name|String
name|content
parameter_list|)
throws|throws
name|IOException
block|{
name|COSStream
name|stream
init|=
operator|new
name|COSStream
argument_list|()
decl_stmt|;
name|OutputStream
name|out
init|=
name|stream
operator|.
name|createOutputStream
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|content
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|stream
return|;
block|}
comment|/**      * Sets the overlay position.      *       * @param overlayPosition the overlay position      */
specifier|public
name|void
name|setOverlayPosition
parameter_list|(
name|Position
name|overlayPosition
parameter_list|)
block|{
name|position
operator|=
name|overlayPosition
expr_stmt|;
block|}
comment|/**      * Sets the file to be overlayed.      *       * @param inputFile the file to be overlayed      */
specifier|public
name|void
name|setInputFile
parameter_list|(
name|String
name|inputFile
parameter_list|)
block|{
name|inputFileName
operator|=
name|inputFile
expr_stmt|;
block|}
comment|/**      * Sets the PDF to be overlayed.      *       * @param inputPDF the PDF to be overlayed      */
specifier|public
name|void
name|setInputPDF
parameter_list|(
name|PDDocument
name|inputPDF
parameter_list|)
block|{
name|inputPDFDocument
operator|=
name|inputPDF
expr_stmt|;
block|}
comment|/**      * Returns the input file.      *       * @return the input file      */
specifier|public
name|String
name|getInputFile
parameter_list|()
block|{
return|return
name|inputFileName
return|;
block|}
comment|/**      * Sets the output file.      *       * @param outputFile the output file      */
specifier|public
name|void
name|setOutputFile
parameter_list|(
name|String
name|outputFile
parameter_list|)
block|{
name|outputFilename
operator|=
name|outputFile
expr_stmt|;
block|}
comment|/**      * Returns the output file.      *       * @return the output file      */
specifier|public
name|String
name|getOutputFile
parameter_list|()
block|{
return|return
name|outputFilename
return|;
block|}
comment|/**      * Sets the default overlay file.      *       * @param defaultOverlayFile the default overlay file      */
specifier|public
name|void
name|setDefaultOverlayFile
parameter_list|(
name|String
name|defaultOverlayFile
parameter_list|)
block|{
name|defaultOverlayFilename
operator|=
name|defaultOverlayFile
expr_stmt|;
block|}
comment|/**      * Sets the default overlay PDF.      *       * @param defaultOverlayPDF the default overlay PDF      */
specifier|public
name|void
name|setDefaultOverlayPDF
parameter_list|(
name|PDDocument
name|defaultOverlayPDF
parameter_list|)
block|{
name|defaultOverlay
operator|=
name|defaultOverlayPDF
expr_stmt|;
block|}
comment|/**      * Returns the default overlay file.      *       * @return the default overlay file      */
specifier|public
name|String
name|getDefaultOverlayFile
parameter_list|()
block|{
return|return
name|defaultOverlayFilename
return|;
block|}
comment|/**      * Sets the first page overlay file.      *       * @param firstPageOverlayFile the first page overlay file      */
specifier|public
name|void
name|setFirstPageOverlayFile
parameter_list|(
name|String
name|firstPageOverlayFile
parameter_list|)
block|{
name|firstPageOverlayFilename
operator|=
name|firstPageOverlayFile
expr_stmt|;
block|}
comment|/**      * Sets the first page overlay PDF.      *       * @param firstPageOverlayPDF the first page overlay PDF      */
specifier|public
name|void
name|setFirstPageOverlayPDF
parameter_list|(
name|PDDocument
name|firstPageOverlayPDF
parameter_list|)
block|{
name|firstPageOverlay
operator|=
name|firstPageOverlayPDF
expr_stmt|;
block|}
comment|/**      * Sets the last page overlay file.      *       * @param lastPageOverlayFile the last page overlay file      */
specifier|public
name|void
name|setLastPageOverlayFile
parameter_list|(
name|String
name|lastPageOverlayFile
parameter_list|)
block|{
name|lastPageOverlayFilename
operator|=
name|lastPageOverlayFile
expr_stmt|;
block|}
comment|/**      * Sets the last page overlay PDF.      *       * @param lastPageOverlayPDF the last page overlay PDF      */
specifier|public
name|void
name|setLastPageOverlayPDF
parameter_list|(
name|PDDocument
name|lastPageOverlayPDF
parameter_list|)
block|{
name|lastPageOverlay
operator|=
name|lastPageOverlayPDF
expr_stmt|;
block|}
comment|/**      * Sets the all pages overlay file.      *       * @param allPagesOverlayFile the all pages overlay file      */
specifier|public
name|void
name|setAllPagesOverlayFile
parameter_list|(
name|String
name|allPagesOverlayFile
parameter_list|)
block|{
name|allPagesOverlayFilename
operator|=
name|allPagesOverlayFile
expr_stmt|;
block|}
comment|/**      * Sets the all pages overlay PDF.      *       * @param allPagesOverlayPDF the all pages overlay PDF      */
specifier|public
name|void
name|setAllPagesOverlayPDF
parameter_list|(
name|PDDocument
name|allPagesOverlayPDF
parameter_list|)
block|{
name|allPagesOverlay
operator|=
name|allPagesOverlayPDF
expr_stmt|;
block|}
comment|/**      * Sets the odd page overlay file.      *       * @param oddPageOverlayFile the odd page overlay file      */
specifier|public
name|void
name|setOddPageOverlayFile
parameter_list|(
name|String
name|oddPageOverlayFile
parameter_list|)
block|{
name|oddPageOverlayFilename
operator|=
name|oddPageOverlayFile
expr_stmt|;
block|}
comment|/**      * Sets the odd page overlay PDF.      *       * @param oddPageOverlayPDF the odd page overlay PDF      */
specifier|public
name|void
name|setOddPageOverlayPDF
parameter_list|(
name|PDDocument
name|oddPageOverlayPDF
parameter_list|)
block|{
name|oddPageOverlay
operator|=
name|oddPageOverlayPDF
expr_stmt|;
block|}
comment|/**      * Sets the even page overlay file.      *       * @param evenPageOverlayFile the even page overlay file      */
specifier|public
name|void
name|setEvenPageOverlayFile
parameter_list|(
name|String
name|evenPageOverlayFile
parameter_list|)
block|{
name|evenPageOverlayFilename
operator|=
name|evenPageOverlayFile
expr_stmt|;
block|}
comment|/**      * Sets the even page overlay PDF.      *       * @param evenPageOverlayPDF the even page overlay PDF      */
specifier|public
name|void
name|setEvenPageOverlayPDF
parameter_list|(
name|PDDocument
name|evenPageOverlayPDF
parameter_list|)
block|{
name|evenPageOverlay
operator|=
name|evenPageOverlayPDF
expr_stmt|;
block|}
block|}
end_class

end_unit

