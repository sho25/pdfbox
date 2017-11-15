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
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|cos
operator|.
name|COSDocument
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
name|pdfwriter
operator|.
name|COSWriter
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
name|interactive
operator|.
name|form
operator|.
name|PDAcroForm
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
name|form
operator|.
name|PDSignatureField
import|;
end_import

begin_comment
comment|/**  * Class to build PDF template.  *  * @author Vakhtang Koroghlishvili  */
end_comment

begin_class
specifier|public
class|class
name|PDFTemplateCreator
block|{
specifier|private
specifier|final
name|PDFTemplateBuilder
name|pdfBuilder
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDFTemplateCreator
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor.      *       * @param templateBuilder      */
specifier|public
name|PDFTemplateCreator
parameter_list|(
name|PDFTemplateBuilder
name|templateBuilder
parameter_list|)
block|{
name|pdfBuilder
operator|=
name|templateBuilder
expr_stmt|;
block|}
comment|/**      * Returns the PDFTemplateStructure object.      *       * @return      */
specifier|public
name|PDFTemplateStructure
name|getPdfStructure
parameter_list|()
block|{
return|return
name|pdfBuilder
operator|.
name|getStructure
argument_list|()
return|;
block|}
comment|/**      * Build a PDF with a visible signature step by step, and return it as a stream.      *      * @param properties      * @return InputStream      * @throws IOException      */
specifier|public
name|InputStream
name|buildPDF
parameter_list|(
name|PDVisibleSignDesigner
name|properties
parameter_list|)
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"pdf building has been started"
argument_list|)
expr_stmt|;
name|PDFTemplateStructure
name|pdfStructure
init|=
name|pdfBuilder
operator|.
name|getStructure
argument_list|()
decl_stmt|;
comment|// we create array of [Text, ImageB, ImageC, ImageI]
name|pdfBuilder
operator|.
name|createProcSetArray
argument_list|()
expr_stmt|;
comment|//create page
name|pdfBuilder
operator|.
name|createPage
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|PDPage
name|page
init|=
name|pdfStructure
operator|.
name|getPage
argument_list|()
decl_stmt|;
comment|//create template
name|pdfBuilder
operator|.
name|createTemplate
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDDocument
name|template
init|=
name|pdfStructure
operator|.
name|getTemplate
argument_list|()
decl_stmt|;
comment|//create /AcroForm
name|pdfBuilder
operator|.
name|createAcroForm
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|PDAcroForm
name|acroForm
init|=
name|pdfStructure
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
comment|// AcroForm contains signature fields
name|pdfBuilder
operator|.
name|createSignatureField
argument_list|(
name|acroForm
argument_list|)
expr_stmt|;
name|PDSignatureField
name|pdSignatureField
init|=
name|pdfStructure
operator|.
name|getSignatureField
argument_list|()
decl_stmt|;
comment|// create signature
comment|//TODO
comment|// The line below has no effect with the CreateVisibleSignature example.
comment|// The signature field is needed as a "holder" for the /AP tree,
comment|// but the /P and /V PDSignatureField entries are ignored by PDDocument.addSignature
name|pdfBuilder
operator|.
name|createSignature
argument_list|(
name|pdSignatureField
argument_list|,
name|page
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|// that is /AcroForm/DR entry
name|pdfBuilder
operator|.
name|createAcroFormDictionary
argument_list|(
name|acroForm
argument_list|,
name|pdSignatureField
argument_list|)
expr_stmt|;
comment|// create AffineTransform
name|pdfBuilder
operator|.
name|createAffineTransform
argument_list|(
name|properties
operator|.
name|getTransform
argument_list|()
argument_list|)
expr_stmt|;
name|AffineTransform
name|transform
init|=
name|pdfStructure
operator|.
name|getAffineTransform
argument_list|()
decl_stmt|;
comment|// rectangle, formatter, image. /AcroForm/DR/XObject contains that form
name|pdfBuilder
operator|.
name|createSignatureRectangle
argument_list|(
name|pdSignatureField
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|pdfBuilder
operator|.
name|createFormatterRectangle
argument_list|(
name|properties
operator|.
name|getFormatterRectangleParameters
argument_list|()
argument_list|)
expr_stmt|;
name|PDRectangle
name|formatter
init|=
name|pdfStructure
operator|.
name|getFormatterRectangle
argument_list|()
decl_stmt|;
name|pdfBuilder
operator|.
name|createSignatureImage
argument_list|(
name|template
argument_list|,
name|properties
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
comment|// create form stream, form and  resource.
name|pdfBuilder
operator|.
name|createHolderFormStream
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|PDStream
name|holderFormStream
init|=
name|pdfStructure
operator|.
name|getHolderFormStream
argument_list|()
decl_stmt|;
name|pdfBuilder
operator|.
name|createHolderFormResources
argument_list|()
expr_stmt|;
name|PDResources
name|holderFormResources
init|=
name|pdfStructure
operator|.
name|getHolderFormResources
argument_list|()
decl_stmt|;
name|pdfBuilder
operator|.
name|createHolderForm
argument_list|(
name|holderFormResources
argument_list|,
name|holderFormStream
argument_list|,
name|formatter
argument_list|)
expr_stmt|;
comment|// that is /AP entry the appearance dictionary.
name|pdfBuilder
operator|.
name|createAppearanceDictionary
argument_list|(
name|pdfStructure
operator|.
name|getHolderForm
argument_list|()
argument_list|,
name|pdSignatureField
argument_list|)
expr_stmt|;
comment|// inner form stream, form and resource (hlder form containts inner form)
name|pdfBuilder
operator|.
name|createInnerFormStream
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|pdfBuilder
operator|.
name|createInnerFormResource
argument_list|()
expr_stmt|;
name|PDResources
name|innerFormResource
init|=
name|pdfStructure
operator|.
name|getInnerFormResources
argument_list|()
decl_stmt|;
name|pdfBuilder
operator|.
name|createInnerForm
argument_list|(
name|innerFormResource
argument_list|,
name|pdfStructure
operator|.
name|getInnerFormStream
argument_list|()
argument_list|,
name|formatter
argument_list|)
expr_stmt|;
name|PDFormXObject
name|innerForm
init|=
name|pdfStructure
operator|.
name|getInnerForm
argument_list|()
decl_stmt|;
comment|// inner form must be in the holder form as we wrote
name|pdfBuilder
operator|.
name|insertInnerFormToHolderResources
argument_list|(
name|innerForm
argument_list|,
name|holderFormResources
argument_list|)
expr_stmt|;
comment|//  Image form is in this structure: /AcroForm/DR/FRM/Resources/XObject/n2
name|pdfBuilder
operator|.
name|createImageFormStream
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|PDStream
name|imageFormStream
init|=
name|pdfStructure
operator|.
name|getImageFormStream
argument_list|()
decl_stmt|;
name|pdfBuilder
operator|.
name|createImageFormResources
argument_list|()
expr_stmt|;
name|PDResources
name|imageFormResources
init|=
name|pdfStructure
operator|.
name|getImageFormResources
argument_list|()
decl_stmt|;
name|pdfBuilder
operator|.
name|createImageForm
argument_list|(
name|imageFormResources
argument_list|,
name|innerFormResource
argument_list|,
name|imageFormStream
argument_list|,
name|formatter
argument_list|,
name|transform
argument_list|,
name|pdfStructure
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
name|pdfBuilder
operator|.
name|createBackgroundLayerForm
argument_list|(
name|innerFormResource
argument_list|,
name|formatter
argument_list|)
expr_stmt|;
comment|// now inject procSetArray
name|pdfBuilder
operator|.
name|injectProcSetArray
argument_list|(
name|innerForm
argument_list|,
name|page
argument_list|,
name|innerFormResource
argument_list|,
name|imageFormResources
argument_list|,
name|holderFormResources
argument_list|,
name|pdfStructure
operator|.
name|getProcSet
argument_list|()
argument_list|)
expr_stmt|;
name|COSName
name|imageFormName
init|=
name|pdfStructure
operator|.
name|getImageFormName
argument_list|()
decl_stmt|;
name|COSName
name|imageName
init|=
name|pdfStructure
operator|.
name|getImageName
argument_list|()
decl_stmt|;
name|COSName
name|innerFormName
init|=
name|pdfStructure
operator|.
name|getInnerFormName
argument_list|()
decl_stmt|;
comment|// now create Streams of AP
name|pdfBuilder
operator|.
name|injectAppearanceStreams
argument_list|(
name|holderFormStream
argument_list|,
name|imageFormStream
argument_list|,
name|imageFormStream
argument_list|,
name|imageFormName
argument_list|,
name|imageName
argument_list|,
name|innerFormName
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|pdfBuilder
operator|.
name|createVisualSignature
argument_list|(
name|template
argument_list|)
expr_stmt|;
name|pdfBuilder
operator|.
name|createWidgetDictionary
argument_list|(
name|pdSignatureField
argument_list|,
name|holderFormResources
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|getVisualSignatureAsStream
argument_list|(
name|pdfStructure
operator|.
name|getVisualSignature
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"stream returning started, size= "
operator|+
name|in
operator|.
name|available
argument_list|()
argument_list|)
expr_stmt|;
comment|// we must close the document
name|template
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// return result of the stream
return|return
name|in
return|;
block|}
specifier|private
name|InputStream
name|getVisualSignatureAsStream
parameter_list|(
name|COSDocument
name|visualSignature
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
init|(
name|COSWriter
name|writer
init|=
operator|new
name|COSWriter
argument_list|(
name|baos
argument_list|)
init|)
block|{
name|writer
operator|.
name|write
argument_list|(
name|visualSignature
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

