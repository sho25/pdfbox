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
name|Map
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
name|PDDocumentCatalog
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
name|PDPageContentStream
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
name|optionalcontent
operator|.
name|PDOptionalContentGroup
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
name|optionalcontent
operator|.
name|PDOptionalContentProperties
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

begin_comment
comment|/**  * This class allows to import pages as Form XObjects into a PDF file and use them to create  * layers (optional content groups).  *  */
end_comment

begin_class
specifier|public
class|class
name|LayerUtility
block|{
specifier|private
specifier|static
specifier|final
name|boolean
name|DEBUG
init|=
literal|true
decl_stmt|;
specifier|private
specifier|final
name|PDDocument
name|targetDoc
decl_stmt|;
specifier|private
specifier|final
name|PDFCloneUtility
name|cloner
decl_stmt|;
comment|/**      * Creates a new instance.      * @param document the PDF document to modify      */
specifier|public
name|LayerUtility
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
name|this
operator|.
name|targetDoc
operator|=
name|document
expr_stmt|;
name|this
operator|.
name|cloner
operator|=
operator|new
name|PDFCloneUtility
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the PDF document we work on.      * @return the PDF document      */
specifier|public
name|PDDocument
name|getDocument
parameter_list|()
block|{
return|return
name|this
operator|.
name|targetDoc
return|;
block|}
comment|/**      * Some applications may not wrap their page content in a save/restore (q/Q) pair which can      * lead to problems with coordinate system transformations when content is appended. This      * method lets you add a q/Q pair around the existing page's content.      * @param page the page      * @throws IOException if an I/O error occurs      */
specifier|public
name|void
name|wrapInSaveRestore
parameter_list|(
name|PDPage
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|COSStream
name|saveGraphicsStateStream
init|=
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
operator|.
name|createCOSStream
argument_list|()
decl_stmt|;
name|OutputStream
name|saveStream
init|=
name|saveGraphicsStateStream
operator|.
name|createOutputStream
argument_list|()
decl_stmt|;
name|saveStream
operator|.
name|write
argument_list|(
literal|"q\n"
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|saveStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|COSStream
name|restoreGraphicsStateStream
init|=
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
operator|.
name|createCOSStream
argument_list|()
decl_stmt|;
name|OutputStream
name|restoreStream
init|=
name|restoreGraphicsStateStream
operator|.
name|createOutputStream
argument_list|()
decl_stmt|;
name|restoreStream
operator|.
name|write
argument_list|(
literal|"Q\n"
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|restoreStream
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//Wrap the existing page's content in a save/restore pair (q/Q) to have a controlled
comment|//environment to add additional content.
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
if|if
condition|(
name|contents
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|contentsStream
init|=
operator|(
name|COSStream
operator|)
name|contents
decl_stmt|;
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
name|saveGraphicsStateStream
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|contentsStream
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|restoreGraphicsStateStream
argument_list|)
expr_stmt|;
name|pageDictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CONTENTS
argument_list|,
name|array
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
name|COSArray
name|contentsArray
init|=
operator|(
name|COSArray
operator|)
name|contents
decl_stmt|;
name|contentsArray
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|saveGraphicsStateStream
argument_list|)
expr_stmt|;
name|contentsArray
operator|.
name|add
argument_list|(
name|restoreGraphicsStateStream
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Contents are unknown type: "
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
comment|/**      * Imports a page from some PDF file as a Form XObject so it can be placed on another page      * in the target document.      * @param sourceDoc the source PDF document that contains the page to be copied      * @param pageNumber the page number of the page to be copied      * @return a Form XObject containing the original page's content      * @throws IOException if an I/O error occurs      */
specifier|public
name|PDFormXObject
name|importPageAsForm
parameter_list|(
name|PDDocument
name|sourceDoc
parameter_list|,
name|int
name|pageNumber
parameter_list|)
throws|throws
name|IOException
block|{
name|PDPage
name|page
init|=
name|sourceDoc
operator|.
name|getPage
argument_list|(
name|pageNumber
argument_list|)
decl_stmt|;
return|return
name|importPageAsForm
argument_list|(
name|sourceDoc
argument_list|,
name|page
argument_list|)
return|;
block|}
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|PAGE_TO_FORM_FILTER
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"Group"
block|,
literal|"LastModified"
block|,
literal|"Metadata"
block|}
argument_list|)
argument_list|)
decl_stmt|;
comment|/**      * Imports a page from some PDF file as a Form XObject so it can be placed on another page      * in the target document.      * @param sourceDoc the source PDF document that contains the page to be copied      * @param page the page in the source PDF document to be copied      * @return a Form XObject containing the original page's content      * @throws IOException if an I/O error occurs      */
specifier|public
name|PDFormXObject
name|importPageAsForm
parameter_list|(
name|PDDocument
name|sourceDoc
parameter_list|,
name|PDPage
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|PDStream
name|newStream
init|=
operator|new
name|PDStream
argument_list|(
name|targetDoc
argument_list|,
name|page
operator|.
name|getContents
argument_list|()
argument_list|,
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
decl_stmt|;
name|PDFormXObject
name|form
init|=
operator|new
name|PDFormXObject
argument_list|(
name|newStream
argument_list|)
decl_stmt|;
comment|//Copy resources
name|PDResources
name|pageRes
init|=
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
name|PDResources
name|formRes
init|=
operator|new
name|PDResources
argument_list|()
decl_stmt|;
name|cloner
operator|.
name|cloneMerge
argument_list|(
name|pageRes
argument_list|,
name|formRes
argument_list|)
expr_stmt|;
name|form
operator|.
name|setResources
argument_list|(
name|formRes
argument_list|)
expr_stmt|;
comment|//Transfer some values from page to form
name|transferDict
argument_list|(
name|page
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|form
operator|.
name|getCOSStream
argument_list|()
argument_list|,
name|PAGE_TO_FORM_FILTER
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Matrix
name|matrix
init|=
name|form
operator|.
name|getMatrix
argument_list|()
decl_stmt|;
name|AffineTransform
name|at
init|=
name|matrix
operator|.
name|createAffineTransform
argument_list|()
decl_stmt|;
name|PDRectangle
name|mediaBox
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
decl_stmt|;
name|PDRectangle
name|cropBox
init|=
name|page
operator|.
name|getCropBox
argument_list|()
decl_stmt|;
name|PDRectangle
name|viewBox
init|=
operator|(
name|cropBox
operator|!=
literal|null
condition|?
name|cropBox
else|:
name|mediaBox
operator|)
decl_stmt|;
comment|//Handle the /Rotation entry on the page dict
name|int
name|rotation
init|=
name|page
operator|.
name|getRotation
argument_list|()
decl_stmt|;
comment|//Transform to FOP's user space
comment|//at.scale(1 / viewBox.getWidth(), 1 / viewBox.getHeight());
name|at
operator|.
name|translate
argument_list|(
name|mediaBox
operator|.
name|getLowerLeftX
argument_list|()
operator|-
name|viewBox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
name|mediaBox
operator|.
name|getLowerLeftY
argument_list|()
operator|-
name|viewBox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|rotation
condition|)
block|{
case|case
literal|90
case|:
name|at
operator|.
name|scale
argument_list|(
name|viewBox
operator|.
name|getWidth
argument_list|()
operator|/
name|viewBox
operator|.
name|getHeight
argument_list|()
argument_list|,
name|viewBox
operator|.
name|getHeight
argument_list|()
operator|/
name|viewBox
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|at
operator|.
name|translate
argument_list|(
literal|0
argument_list|,
name|viewBox
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|at
operator|.
name|rotate
argument_list|(
operator|-
name|Math
operator|.
name|PI
operator|/
literal|2.0
argument_list|)
expr_stmt|;
break|break;
case|case
literal|180
case|:
name|at
operator|.
name|translate
argument_list|(
name|viewBox
operator|.
name|getWidth
argument_list|()
argument_list|,
name|viewBox
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|at
operator|.
name|rotate
argument_list|(
operator|-
name|Math
operator|.
name|PI
argument_list|)
expr_stmt|;
break|break;
case|case
literal|270
case|:
name|at
operator|.
name|scale
argument_list|(
name|viewBox
operator|.
name|getWidth
argument_list|()
operator|/
name|viewBox
operator|.
name|getHeight
argument_list|()
argument_list|,
name|viewBox
operator|.
name|getHeight
argument_list|()
operator|/
name|viewBox
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|at
operator|.
name|translate
argument_list|(
name|viewBox
operator|.
name|getHeight
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|at
operator|.
name|rotate
argument_list|(
operator|-
name|Math
operator|.
name|PI
operator|*
literal|1.5
argument_list|)
expr_stmt|;
default|default:
comment|//no additional transformations necessary
block|}
comment|//Compensate for Crop Boxes not starting at 0,0
name|at
operator|.
name|translate
argument_list|(
operator|-
name|viewBox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
operator|-
name|viewBox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|at
operator|.
name|isIdentity
argument_list|()
condition|)
block|{
name|form
operator|.
name|setMatrix
argument_list|(
name|at
argument_list|)
expr_stmt|;
block|}
name|BoundingBox
name|bbox
init|=
operator|new
name|BoundingBox
argument_list|()
decl_stmt|;
name|bbox
operator|.
name|setLowerLeftX
argument_list|(
name|viewBox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|)
expr_stmt|;
name|bbox
operator|.
name|setLowerLeftY
argument_list|(
name|viewBox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
expr_stmt|;
name|bbox
operator|.
name|setUpperRightX
argument_list|(
name|viewBox
operator|.
name|getUpperRightX
argument_list|()
argument_list|)
expr_stmt|;
name|bbox
operator|.
name|setUpperRightY
argument_list|(
name|viewBox
operator|.
name|getUpperRightY
argument_list|()
argument_list|)
expr_stmt|;
name|form
operator|.
name|setBBox
argument_list|(
operator|new
name|PDRectangle
argument_list|(
name|bbox
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|form
return|;
block|}
comment|/**      * Places the given form over the existing content of the indicated page (like an overlay).      * The form is enveloped in a marked content section to indicate that it's part of an      * optional content group (OCG), here used as a layer. This optional group is returned and      * can be enabled and disabled through methods on {@link PDOptionalContentProperties}.      * @param targetPage the target page      * @param form the form to place      * @param transform the transformation matrix that controls the placement      * @param layerName the name for the layer/OCG to produce      * @return the optional content group that was generated for the form usage      * @throws IOException if an I/O error occurs      */
specifier|public
name|PDOptionalContentGroup
name|appendFormAsLayer
parameter_list|(
name|PDPage
name|targetPage
parameter_list|,
name|PDFormXObject
name|form
parameter_list|,
name|AffineTransform
name|transform
parameter_list|,
name|String
name|layerName
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocumentCatalog
name|catalog
init|=
name|targetDoc
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|PDOptionalContentProperties
name|ocprops
init|=
name|catalog
operator|.
name|getOCProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|ocprops
operator|==
literal|null
condition|)
block|{
name|ocprops
operator|=
operator|new
name|PDOptionalContentProperties
argument_list|()
expr_stmt|;
name|catalog
operator|.
name|setOCProperties
argument_list|(
name|ocprops
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ocprops
operator|.
name|hasGroup
argument_list|(
name|layerName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Optional group (layer) already exists: "
operator|+
name|layerName
argument_list|)
throw|;
block|}
name|PDOptionalContentGroup
name|layer
init|=
operator|new
name|PDOptionalContentGroup
argument_list|(
name|layerName
argument_list|)
decl_stmt|;
name|ocprops
operator|.
name|addGroup
argument_list|(
name|layer
argument_list|)
expr_stmt|;
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|targetDoc
argument_list|,
name|targetPage
argument_list|,
literal|true
argument_list|,
operator|!
name|DEBUG
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|beginMarkedContent
argument_list|(
name|COSName
operator|.
name|OC
argument_list|,
name|layer
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|saveGraphicsState
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|transform
argument_list|(
operator|new
name|Matrix
argument_list|(
name|transform
argument_list|)
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawForm
argument_list|(
name|form
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|restoreGraphicsState
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|endMarkedContent
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|layer
return|;
block|}
specifier|private
name|void
name|transferDict
parameter_list|(
name|COSDictionary
name|orgDict
parameter_list|,
name|COSDictionary
name|targetDict
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|filter
parameter_list|,
name|boolean
name|inclusive
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
name|entry
range|:
name|orgDict
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|COSName
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|inclusive
operator|&&
operator|!
name|filter
operator|.
name|contains
argument_list|(
name|key
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
elseif|else
if|if
condition|(
operator|!
name|inclusive
operator|&&
name|filter
operator|.
name|contains
argument_list|(
name|key
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|targetDict
operator|.
name|setItem
argument_list|(
name|key
argument_list|,
name|cloner
operator|.
name|cloneForNewDocument
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

