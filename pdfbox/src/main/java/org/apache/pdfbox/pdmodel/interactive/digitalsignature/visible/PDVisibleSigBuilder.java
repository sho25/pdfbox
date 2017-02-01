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
name|List
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
name|LosslessFactory
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
name|PDAnnotationWidget
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
name|pdmodel
operator|.
name|interactive
operator|.
name|digitalsignature
operator|.
name|PDSignature
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
name|PDField
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
comment|/**  * Implementation of {@link PDFTemplateBuilder}. This builds the signature PDF but doesn't keep the  * elements, these are kept in its PDF template structure.  *  * @author Vakhtang Koroghlishvili  */
end_comment

begin_class
specifier|public
class|class
name|PDVisibleSigBuilder
implements|implements
name|PDFTemplateBuilder
block|{
specifier|private
specifier|final
name|PDFTemplateStructure
name|pdfStructure
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
name|PDVisibleSigBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor, creates PDF template structure.      */
specifier|public
name|PDVisibleSigBuilder
parameter_list|()
block|{
name|pdfStructure
operator|=
operator|new
name|PDFTemplateStructure
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"PDF Structure has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createPage
parameter_list|(
name|PDVisibleSignDesigner
name|properties
parameter_list|)
block|{
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|(
operator|new
name|PDRectangle
argument_list|(
name|properties
operator|.
name|getPageWidth
argument_list|()
argument_list|,
name|properties
operator|.
name|getPageHeight
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|pdfStructure
operator|.
name|setPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"PDF page has been created"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a PDDocument and adds the page parameter to it and keeps this as a template in the      * PDF template Structure.      *      * @param page      * @throws IOException      */
annotation|@
name|Override
specifier|public
name|void
name|createTemplate
parameter_list|(
name|PDPage
name|page
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDocument
name|template
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|template
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setTemplate
argument_list|(
name|template
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createAcroForm
parameter_list|(
name|PDDocument
name|template
parameter_list|)
block|{
name|PDAcroForm
name|theAcroForm
init|=
operator|new
name|PDAcroForm
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|template
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setAcroForm
argument_list|(
name|theAcroForm
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setAcroForm
argument_list|(
name|theAcroForm
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"AcroForm has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|PDFTemplateStructure
name|getStructure
parameter_list|()
block|{
return|return
name|pdfStructure
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createSignatureField
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|)
throws|throws
name|IOException
block|{
name|PDSignatureField
name|sf
init|=
operator|new
name|PDSignatureField
argument_list|(
name|acroForm
argument_list|)
decl_stmt|;
name|pdfStructure
operator|.
name|setSignatureField
argument_list|(
name|sf
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Signature field has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|PDSignature
name|pdSignature
init|=
operator|new
name|PDSignature
argument_list|()
decl_stmt|;
name|PDAnnotationWidget
name|widget
init|=
name|pdSignatureField
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|pdSignatureField
operator|.
name|setValue
argument_list|(
name|pdSignature
argument_list|)
expr_stmt|;
name|widget
operator|.
name|setPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|page
operator|.
name|getAnnotations
argument_list|()
operator|.
name|add
argument_list|(
name|widget
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|signerName
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|pdSignature
operator|.
name|setName
argument_list|(
name|signerName
argument_list|)
expr_stmt|;
block|}
name|pdfStructure
operator|.
name|setPdSignature
argument_list|(
name|pdSignature
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"PDSignature has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|PDField
argument_list|>
name|acroFormFields
init|=
name|acroForm
operator|.
name|getFields
argument_list|()
decl_stmt|;
name|COSDictionary
name|acroFormDict
init|=
name|acroForm
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|acroForm
operator|.
name|setSignaturesExist
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|acroForm
operator|.
name|setAppendOnly
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|acroFormDict
operator|.
name|setDirect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|acroFormFields
operator|.
name|add
argument_list|(
name|signatureField
argument_list|)
expr_stmt|;
name|acroForm
operator|.
name|setDefaultAppearance
argument_list|(
literal|"/sylfaen 0 Tf 0 g"
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setAcroFormFields
argument_list|(
name|acroFormFields
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setAcroFormDictionary
argument_list|(
name|acroFormDict
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"AcroForm dictionary has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|PDRectangle
name|rect
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|rect
operator|.
name|setUpperRightX
argument_list|(
name|properties
operator|.
name|getxAxis
argument_list|()
operator|+
name|properties
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightY
argument_list|(
name|properties
operator|.
name|getTemplateHeight
argument_list|()
operator|-
name|properties
operator|.
name|getyAxis
argument_list|()
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setLowerLeftY
argument_list|(
name|properties
operator|.
name|getTemplateHeight
argument_list|()
operator|-
name|properties
operator|.
name|getyAxis
argument_list|()
operator|-
name|properties
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setLowerLeftX
argument_list|(
name|properties
operator|.
name|getxAxis
argument_list|()
argument_list|)
expr_stmt|;
name|signatureField
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setRectangle
argument_list|(
name|rect
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setSignatureRectangle
argument_list|(
name|rect
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Signature rectangle has been created"
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc }      *      * @deprecated use {@link #createAffineTransform(java.awt.geom.AffineTransform) }      */
annotation|@
name|Override
annotation|@
name|Deprecated
specifier|public
name|void
name|createAffineTransform
parameter_list|(
name|byte
index|[]
name|params
parameter_list|)
block|{
name|AffineTransform
name|transform
init|=
operator|new
name|AffineTransform
argument_list|(
name|params
index|[
literal|0
index|]
argument_list|,
name|params
index|[
literal|1
index|]
argument_list|,
name|params
index|[
literal|2
index|]
argument_list|,
name|params
index|[
literal|3
index|]
argument_list|,
name|params
index|[
literal|4
index|]
argument_list|,
name|params
index|[
literal|5
index|]
argument_list|)
decl_stmt|;
name|pdfStructure
operator|.
name|setAffineTransform
argument_list|(
name|transform
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Matrix has been added"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createAffineTransform
parameter_list|(
name|AffineTransform
name|affineTransform
parameter_list|)
block|{
name|pdfStructure
operator|.
name|setAffineTransform
argument_list|(
name|affineTransform
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Matrix has been added"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createProcSetArray
parameter_list|()
block|{
name|COSArray
name|procSetArr
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|procSetArr
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"PDF"
argument_list|)
argument_list|)
expr_stmt|;
name|procSetArr
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Text"
argument_list|)
argument_list|)
expr_stmt|;
name|procSetArr
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ImageB"
argument_list|)
argument_list|)
expr_stmt|;
name|procSetArr
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ImageC"
argument_list|)
argument_list|)
expr_stmt|;
name|procSetArr
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ImageI"
argument_list|)
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setProcSet
argument_list|(
name|procSetArr
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"ProcSet array has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|pdfStructure
operator|.
name|setImage
argument_list|(
name|LosslessFactory
operator|.
name|createFromImage
argument_list|(
name|template
argument_list|,
name|image
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Visible Signature Image has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createFormatterRectangle
parameter_list|(
name|byte
index|[]
name|params
parameter_list|)
block|{
name|PDRectangle
name|formatterRectangle
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|formatterRectangle
operator|.
name|setUpperRightX
argument_list|(
name|params
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|formatterRectangle
operator|.
name|setUpperRightY
argument_list|(
name|params
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|formatterRectangle
operator|.
name|setLowerLeftX
argument_list|(
name|params
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|formatterRectangle
operator|.
name|setLowerLeftY
argument_list|(
name|params
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setFormatterRectangle
argument_list|(
name|formatterRectangle
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Formatter rectangle has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createHolderFormStream
parameter_list|(
name|PDDocument
name|template
parameter_list|)
block|{
name|PDStream
name|holderForm
init|=
operator|new
name|PDStream
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|pdfStructure
operator|.
name|setHolderFormStream
argument_list|(
name|holderForm
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Holder form stream has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createHolderFormResources
parameter_list|()
block|{
name|PDResources
name|holderFormResources
init|=
operator|new
name|PDResources
argument_list|()
decl_stmt|;
name|pdfStructure
operator|.
name|setHolderFormResources
argument_list|(
name|holderFormResources
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Holder form resources have been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|PDFormXObject
name|holderForm
init|=
operator|new
name|PDFormXObject
argument_list|(
name|holderFormStream
argument_list|)
decl_stmt|;
name|holderForm
operator|.
name|setResources
argument_list|(
name|holderFormResources
argument_list|)
expr_stmt|;
name|holderForm
operator|.
name|setBBox
argument_list|(
name|formrect
argument_list|)
expr_stmt|;
name|holderForm
operator|.
name|setFormType
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setHolderForm
argument_list|(
name|holderForm
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Holder form has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|PDAppearanceDictionary
name|appearance
init|=
operator|new
name|PDAppearanceDictionary
argument_list|()
decl_stmt|;
name|appearance
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setDirect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|PDAppearanceStream
name|appearanceStream
init|=
operator|new
name|PDAppearanceStream
argument_list|(
name|holderForml
operator|.
name|getCOSObject
argument_list|()
argument_list|)
decl_stmt|;
name|appearance
operator|.
name|setNormalAppearance
argument_list|(
name|appearanceStream
argument_list|)
expr_stmt|;
name|signatureField
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|setAppearance
argument_list|(
name|appearance
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setAppearanceDictionary
argument_list|(
name|appearance
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"PDF appearance dictionary has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createInnerFormStream
parameter_list|(
name|PDDocument
name|template
parameter_list|)
block|{
name|PDStream
name|innerFormStream
init|=
operator|new
name|PDStream
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|pdfStructure
operator|.
name|setInnterFormStream
argument_list|(
name|innerFormStream
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Stream of another form (inner form - it will be inside holder form) "
operator|+
literal|"has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createInnerFormResource
parameter_list|()
block|{
name|PDResources
name|innerFormResources
init|=
operator|new
name|PDResources
argument_list|()
decl_stmt|;
name|pdfStructure
operator|.
name|setInnerFormResources
argument_list|(
name|innerFormResources
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Resources of another form (inner form - it will be inside holder form)"
operator|+
literal|"have been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|PDFormXObject
name|innerForm
init|=
operator|new
name|PDFormXObject
argument_list|(
name|innerFormStream
argument_list|)
decl_stmt|;
name|innerForm
operator|.
name|setResources
argument_list|(
name|innerFormResources
argument_list|)
expr_stmt|;
name|innerForm
operator|.
name|setBBox
argument_list|(
name|formrect
argument_list|)
expr_stmt|;
name|innerForm
operator|.
name|setFormType
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setInnerForm
argument_list|(
name|innerForm
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Another form (inner form - it will be inside holder form) has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|insertInnerFormToHolderResources
parameter_list|(
name|PDFormXObject
name|innerForm
parameter_list|,
name|PDResources
name|holderFormResources
parameter_list|)
block|{
name|COSName
name|innerFormName
init|=
name|holderFormResources
operator|.
name|add
argument_list|(
name|innerForm
argument_list|,
literal|"FRM"
argument_list|)
decl_stmt|;
name|pdfStructure
operator|.
name|setInnerFormName
argument_list|(
name|innerFormName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Now inserted inner form inside holder form"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createImageFormStream
parameter_list|(
name|PDDocument
name|template
parameter_list|)
block|{
name|PDStream
name|imageFormStream
init|=
operator|new
name|PDStream
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|pdfStructure
operator|.
name|setImageFormStream
argument_list|(
name|imageFormStream
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Created image form stream"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createImageFormResources
parameter_list|()
block|{
name|PDResources
name|imageFormResources
init|=
operator|new
name|PDResources
argument_list|()
decl_stmt|;
name|pdfStructure
operator|.
name|setImageFormResources
argument_list|(
name|imageFormResources
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Created image form resources"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
name|at
parameter_list|,
name|PDImageXObject
name|img
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFormXObject
name|imageForm
init|=
operator|new
name|PDFormXObject
argument_list|(
name|imageFormStream
argument_list|)
decl_stmt|;
name|imageForm
operator|.
name|setBBox
argument_list|(
name|formrect
argument_list|)
expr_stmt|;
name|imageForm
operator|.
name|setMatrix
argument_list|(
name|at
argument_list|)
expr_stmt|;
name|imageForm
operator|.
name|setResources
argument_list|(
name|imageFormResources
argument_list|)
expr_stmt|;
name|imageForm
operator|.
name|setFormType
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|imageFormResources
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setDirect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|COSName
name|imageFormName
init|=
name|innerFormResource
operator|.
name|add
argument_list|(
name|imageForm
argument_list|,
literal|"n"
argument_list|)
decl_stmt|;
name|COSName
name|imageName
init|=
name|imageFormResources
operator|.
name|add
argument_list|(
name|img
argument_list|,
literal|"img"
argument_list|)
decl_stmt|;
name|pdfStructure
operator|.
name|setImageForm
argument_list|(
name|imageForm
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setImageFormName
argument_list|(
name|imageFormName
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setImageName
argument_list|(
name|imageName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Created image form"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|innerForm
operator|.
name|getResources
argument_list|()
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PROC_SET
argument_list|,
name|procSet
argument_list|)
expr_stmt|;
name|page
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PROC_SET
argument_list|,
name|procSet
argument_list|)
expr_stmt|;
name|innerFormResources
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PROC_SET
argument_list|,
name|procSet
argument_list|)
expr_stmt|;
name|imageFormResources
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PROC_SET
argument_list|,
name|procSet
argument_list|)
expr_stmt|;
name|holderFormResources
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PROC_SET
argument_list|,
name|procSet
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Inserted ProcSet to PDF"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
comment|// 100 means that document width is 100% via the rectangle. if rectangle
comment|// is 500px, images 100% is 500px.
comment|// String imgFormContent = "q "+imageWidthSize+ " 0 0 50 0 0 cm /" +
comment|// imageName + " Do Q\n" + builder.toString();
name|String
name|imgFormContent
init|=
literal|"q "
operator|+
literal|100
operator|+
literal|" 0 0 50 0 0 cm /"
operator|+
name|imageName
operator|.
name|getName
argument_list|()
operator|+
literal|" Do Q\n"
decl_stmt|;
name|String
name|holderFormContent
init|=
literal|"q 1 0 0 1 0 0 cm /"
operator|+
name|innerFormName
operator|.
name|getName
argument_list|()
operator|+
literal|" Do Q\n"
decl_stmt|;
name|String
name|innerFormContent
init|=
literal|"q 1 0 0 1 0 0 cm /"
operator|+
name|imageObjectName
operator|.
name|getName
argument_list|()
operator|+
literal|" Do Q\n"
decl_stmt|;
name|appendRawCommands
argument_list|(
name|pdfStructure
operator|.
name|getHolderFormStream
argument_list|()
operator|.
name|createOutputStream
argument_list|()
argument_list|,
name|holderFormContent
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|pdfStructure
operator|.
name|getInnerFormStream
argument_list|()
operator|.
name|createOutputStream
argument_list|()
argument_list|,
name|innerFormContent
argument_list|)
expr_stmt|;
name|appendRawCommands
argument_list|(
name|pdfStructure
operator|.
name|getImageFormStream
argument_list|()
operator|.
name|createOutputStream
argument_list|()
argument_list|,
name|imgFormContent
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Injected appearance stream to pdf"
argument_list|)
expr_stmt|;
block|}
specifier|public
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
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|createVisualSignature
parameter_list|(
name|PDDocument
name|template
parameter_list|)
block|{
name|pdfStructure
operator|.
name|setVisualSignature
argument_list|(
name|template
operator|.
name|getDocument
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Visible signature has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
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
block|{
name|COSDictionary
name|widgetDict
init|=
name|signatureField
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|widgetDict
operator|.
name|setNeedToBeUpdated
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|widgetDict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DR
argument_list|,
name|holderFormResources
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|pdfStructure
operator|.
name|setWidgetDictionary
argument_list|(
name|widgetDict
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"WidgetDictionary has been created"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|closeTemplate
parameter_list|(
name|PDDocument
name|template
parameter_list|)
throws|throws
name|IOException
block|{
name|template
operator|.
name|close
argument_list|()
expr_stmt|;
name|pdfStructure
operator|.
name|getTemplate
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

