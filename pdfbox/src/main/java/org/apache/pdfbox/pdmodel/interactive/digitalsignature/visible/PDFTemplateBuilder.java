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
name|IOException
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
comment|/**  * That class builds visible signature template which will be added in our PDF document.  * @author Vakhtang Koroghlishvili  */
end_comment

begin_interface
specifier|public
interface|interface
name|PDFTemplateBuilder
block|{
comment|/**      * In order to create Affine Transform, using parameters.      * @param params      */
name|void
name|createAffineTransform
parameter_list|(
name|byte
index|[]
name|params
parameter_list|)
function_decl|;
comment|/**      * Creates specified size page.      *       * @param properties      */
name|void
name|createPage
parameter_list|(
name|PDVisibleSignDesigner
name|properties
parameter_list|)
function_decl|;
comment|/**      * Creates template using page.      *       * @param page      * @throws IOException      */
name|void
name|createTemplate
parameter_list|(
name|PDPage
name|page
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Creates Acro forms in the template.      *       * @param template      */
name|void
name|createAcroForm
parameter_list|(
name|PDDocument
name|template
parameter_list|)
function_decl|;
comment|/**      * Creates signature fields.      *       * @param acroForm      * @throws IOException      */
name|void
name|createSignatureField
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Creates the signature with the given name and assign it to the signature field parameter and      * assign the page parameter to the widget.      *      * @param pdSignatureField      * @param page      * @param signerName the name of the person or authority signing the document. According to the      * PDF specification, this value should be used only when it is not possible to extract the name      * from the signature.      * @throws IOException      */
name|void
name|createSignature
parameter_list|(
name|PDSignatureField
name|pdSignatureField
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|String
name|signerName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Create AcroForm Dictionary.      *       * @param acroForm      * @param signatureField      * @throws IOException      */
name|void
name|createAcroFormDictionary
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|,
name|PDSignatureField
name|signatureField
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Creates SignatureRectangle.      *       * @param signatureField      * @param properties      * @throws IOException      */
name|void
name|createSignatureRectangle
parameter_list|(
name|PDSignatureField
name|signatureField
parameter_list|,
name|PDVisibleSignDesigner
name|properties
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Creates procSetArray of PDF,Text,ImageB,ImageC,ImageI.      */
name|void
name|createProcSetArray
parameter_list|()
function_decl|;
comment|/**      * Creates signature image.      * @param template      * @param image      * @throws IOException      */
name|void
name|createSignatureImage
parameter_list|(
name|PDDocument
name|template
parameter_list|,
name|BufferedImage
name|image
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      *       * @param params      */
name|void
name|createFormatterRectangle
parameter_list|(
name|byte
index|[]
name|params
parameter_list|)
function_decl|;
comment|/**      *       * @param template      */
name|void
name|createHolderFormStream
parameter_list|(
name|PDDocument
name|template
parameter_list|)
function_decl|;
comment|/**      * Creates resources of form      */
name|void
name|createHolderFormResources
parameter_list|()
function_decl|;
comment|/**      * Creates Form      *       * @param holderFormResources      * @param holderFormStream      * @param formrect      */
name|void
name|createHolderForm
parameter_list|(
name|PDResources
name|holderFormResources
parameter_list|,
name|PDStream
name|holderFormStream
parameter_list|,
name|PDRectangle
name|formrect
parameter_list|)
function_decl|;
comment|/**      * Creates appearance dictionary      *       * @param holderForml      * @param signatureField      * @throws IOException      */
name|void
name|createAppearanceDictionary
parameter_list|(
name|PDFormXObject
name|holderForml
parameter_list|,
name|PDSignatureField
name|signatureField
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      *       * @param template      */
name|void
name|createInnerFormStream
parameter_list|(
name|PDDocument
name|template
parameter_list|)
function_decl|;
comment|/**      * Creates InnerForm      */
name|void
name|createInnerFormResource
parameter_list|()
function_decl|;
comment|/**      *       * @param innerFormResources      * @param innerFormStream      * @param formrect      */
name|void
name|createInnerForm
parameter_list|(
name|PDResources
name|innerFormResources
parameter_list|,
name|PDStream
name|innerFormStream
parameter_list|,
name|PDRectangle
name|formrect
parameter_list|)
function_decl|;
comment|/**      *       * @param innerForm      * @param holderFormResources      */
name|void
name|insertInnerFormToHolderResources
parameter_list|(
name|PDFormXObject
name|innerForm
parameter_list|,
name|PDResources
name|holderFormResources
parameter_list|)
function_decl|;
comment|/**      *       * @param template      */
name|void
name|createImageFormStream
parameter_list|(
name|PDDocument
name|template
parameter_list|)
function_decl|;
comment|/**      * Create resource of image form      */
name|void
name|createImageFormResources
parameter_list|()
function_decl|;
comment|/**      * Creates Image form      *       * @param imageFormResources      * @param innerFormResource      * @param imageFormStream      * @param formrect      * @param affineTransform      * @param img      * @throws IOException      */
name|void
name|createImageForm
parameter_list|(
name|PDResources
name|imageFormResources
parameter_list|,
name|PDResources
name|innerFormResource
parameter_list|,
name|PDStream
name|imageFormStream
parameter_list|,
name|PDRectangle
name|formrect
parameter_list|,
name|AffineTransform
name|affineTransform
parameter_list|,
name|PDImageXObject
name|img
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Inject procSetArray      *       * @param innerForm      * @param page      * @param innerFormResources      * @param imageFormResources      * @param holderFormResources      * @param procSet      */
name|void
name|injectProcSetArray
parameter_list|(
name|PDFormXObject
name|innerForm
parameter_list|,
name|PDPage
name|page
parameter_list|,
name|PDResources
name|innerFormResources
parameter_list|,
name|PDResources
name|imageFormResources
parameter_list|,
name|PDResources
name|holderFormResources
parameter_list|,
name|COSArray
name|procSet
parameter_list|)
function_decl|;
comment|/**      * injects appearance streams      *       * @param holderFormStream      * @param innerFormStream      * @param imageFormStream      * @param imageObjectName      * @param imageName      * @param innerFormName      * @param properties      * @throws IOException      */
name|void
name|injectAppearanceStreams
parameter_list|(
name|PDStream
name|holderFormStream
parameter_list|,
name|PDStream
name|innerFormStream
parameter_list|,
name|PDStream
name|imageFormStream
parameter_list|,
name|COSName
name|imageObjectName
parameter_list|,
name|COSName
name|imageName
parameter_list|,
name|COSName
name|innerFormName
parameter_list|,
name|PDVisibleSignDesigner
name|properties
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * just to create visible signature      *       * @param template      */
name|void
name|createVisualSignature
parameter_list|(
name|PDDocument
name|template
parameter_list|)
function_decl|;
comment|/**      * adds Widget Dictionary      *       * @param signatureField      * @param holderFormResources      * @throws IOException      */
name|void
name|createWidgetDictionary
parameter_list|(
name|PDSignatureField
name|signatureField
parameter_list|,
name|PDResources
name|holderFormResources
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      *       * @return - PDF template Structure      */
name|PDFTemplateStructure
name|getStructure
parameter_list|()
function_decl|;
comment|/**      * Closes template      *       * @param template      * @throws IOException      */
name|void
name|closeTemplate
parameter_list|(
name|PDDocument
name|template
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

