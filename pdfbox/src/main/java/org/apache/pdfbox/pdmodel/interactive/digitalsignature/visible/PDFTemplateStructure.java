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
name|PDTerminalField
import|;
end_import

begin_comment
comment|/**  * Structure of PDF document with visible signature.  *   * @author Vakhtang Koroghlishvili  */
end_comment

begin_class
specifier|public
class|class
name|PDFTemplateStructure
block|{
specifier|private
name|PDPage
name|page
decl_stmt|;
specifier|private
name|PDDocument
name|template
decl_stmt|;
specifier|private
name|PDAcroForm
name|acroForm
decl_stmt|;
specifier|private
name|PDSignatureField
name|signatureField
decl_stmt|;
specifier|private
name|PDSignature
name|pdSignature
decl_stmt|;
specifier|private
name|COSDictionary
name|acroFormDictionary
decl_stmt|;
specifier|private
name|PDRectangle
name|singatureRectangle
decl_stmt|;
specifier|private
name|AffineTransform
name|affineTransform
decl_stmt|;
specifier|private
name|COSArray
name|procSet
decl_stmt|;
specifier|private
name|PDImageXObject
name|image
decl_stmt|;
specifier|private
name|PDRectangle
name|formaterRectangle
decl_stmt|;
specifier|private
name|PDStream
name|holderFormStream
decl_stmt|;
specifier|private
name|PDResources
name|holderFormResources
decl_stmt|;
specifier|private
name|PDFormXObject
name|holderForm
decl_stmt|;
specifier|private
name|PDAppearanceDictionary
name|appearanceDictionary
decl_stmt|;
specifier|private
name|PDStream
name|innterFormStream
decl_stmt|;
specifier|private
name|PDResources
name|innerFormResources
decl_stmt|;
specifier|private
name|PDFormXObject
name|innerForm
decl_stmt|;
specifier|private
name|PDStream
name|imageFormStream
decl_stmt|;
specifier|private
name|PDResources
name|imageFormResources
decl_stmt|;
specifier|private
name|List
argument_list|<
name|PDField
argument_list|>
name|acroFormFields
decl_stmt|;
specifier|private
name|COSName
name|innerFormName
decl_stmt|;
specifier|private
name|COSName
name|imageFormName
decl_stmt|;
specifier|private
name|COSName
name|imageName
decl_stmt|;
specifier|private
name|COSDocument
name|visualSignature
decl_stmt|;
specifier|private
name|PDFormXObject
name|imageForm
decl_stmt|;
specifier|private
name|COSDictionary
name|widgetDictionary
decl_stmt|;
comment|/**      * Returns document page.      * @return the page      */
specifier|public
name|PDPage
name|getPage
parameter_list|()
block|{
return|return
name|page
return|;
block|}
comment|/**      * Sets document page      * @param page      */
specifier|public
name|void
name|setPage
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|this
operator|.
name|page
operator|=
name|page
expr_stmt|;
block|}
comment|/**      * Gets PDDocument template.      * This represents a digital signature      *  that can be attached to a document      * @return the template      */
specifier|public
name|PDDocument
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
comment|/**      * Wets PDDocument template.      * This represents a digital signature      * that can be attached to a document      * @param template      */
specifier|public
name|void
name|setTemplate
parameter_list|(
name|PDDocument
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
comment|/**      * Gets AcroForm      * @return the AcroForm      */
specifier|public
name|PDAcroForm
name|getAcroForm
parameter_list|()
block|{
return|return
name|acroForm
return|;
block|}
comment|/**      * Sets AcroForm      * @param acroForm      */
specifier|public
name|void
name|setAcroForm
parameter_list|(
name|PDAcroForm
name|acroForm
parameter_list|)
block|{
name|this
operator|.
name|acroForm
operator|=
name|acroForm
expr_stmt|;
block|}
comment|/**      * Gets Signature field      * @return the signature field      */
specifier|public
name|PDSignatureField
name|getSignatureField
parameter_list|()
block|{
return|return
name|signatureField
return|;
block|}
comment|/**      * Sets signature field      * @param signatureField      */
specifier|public
name|void
name|setSignatureField
parameter_list|(
name|PDSignatureField
name|signatureField
parameter_list|)
block|{
name|this
operator|.
name|signatureField
operator|=
name|signatureField
expr_stmt|;
block|}
comment|/**      * Gets PDSignature      * @return the signature      */
specifier|public
name|PDSignature
name|getPdSignature
parameter_list|()
block|{
return|return
name|pdSignature
return|;
block|}
comment|/**      * Sets PDSignatureField      * @param pdSignature      */
specifier|public
name|void
name|setPdSignature
parameter_list|(
name|PDSignature
name|pdSignature
parameter_list|)
block|{
name|this
operator|.
name|pdSignature
operator|=
name|pdSignature
expr_stmt|;
block|}
comment|/**      * Gets Dictionary of AcroForm. Thats<b> /DR</b>      * entry in the AcroForm      * @return the AcroForm's dictionary       */
specifier|public
name|COSDictionary
name|getAcroFormDictionary
parameter_list|()
block|{
return|return
name|acroFormDictionary
return|;
block|}
comment|/**      * Acroform have its Dictionary, so we here set      * the Dictionary  which is in this location:      *<b> AcroForm/DR<b>      * @param acroFormDictionary      */
specifier|public
name|void
name|setAcroFormDictionary
parameter_list|(
name|COSDictionary
name|acroFormDictionary
parameter_list|)
block|{
name|this
operator|.
name|acroFormDictionary
operator|=
name|acroFormDictionary
expr_stmt|;
block|}
comment|/**      * Gets SignatureRectangle      * @return the rectangle for the signature      */
specifier|public
name|PDRectangle
name|getSingatureRectangle
parameter_list|()
block|{
return|return
name|singatureRectangle
return|;
block|}
comment|/**      * Sets SignatureRectangle      * @param singatureRectangle      */
specifier|public
name|void
name|setSignatureRectangle
parameter_list|(
name|PDRectangle
name|singatureRectangle
parameter_list|)
block|{
name|this
operator|.
name|singatureRectangle
operator|=
name|singatureRectangle
expr_stmt|;
block|}
comment|/**      * Gets AffineTransform      * @return the AffineTransform      */
specifier|public
name|AffineTransform
name|getAffineTransform
parameter_list|()
block|{
return|return
name|affineTransform
return|;
block|}
comment|/**      * Sets AffineTransform      * @param affineTransform      */
specifier|public
name|void
name|setAffineTransform
parameter_list|(
name|AffineTransform
name|affineTransform
parameter_list|)
block|{
name|this
operator|.
name|affineTransform
operator|=
name|affineTransform
expr_stmt|;
block|}
comment|/**      * Gets ProcSet Array      * @return the PorocSet array      */
specifier|public
name|COSArray
name|getProcSet
parameter_list|()
block|{
return|return
name|procSet
return|;
block|}
comment|/**      * Sets ProcSet Array      * @param procSet      */
specifier|public
name|void
name|setProcSet
parameter_list|(
name|COSArray
name|procSet
parameter_list|)
block|{
name|this
operator|.
name|procSet
operator|=
name|procSet
expr_stmt|;
block|}
comment|/**      * Gets the image of visible signature      * @return the image making up the visible signature      */
specifier|public
name|PDImageXObject
name|getImage
parameter_list|()
block|{
return|return
name|image
return|;
block|}
comment|/**      * Sets the image of visible signature      * @param image Image XObject      */
specifier|public
name|void
name|setImage
parameter_list|(
name|PDImageXObject
name|image
parameter_list|)
block|{
name|this
operator|.
name|image
operator|=
name|image
expr_stmt|;
block|}
comment|/**      * Gets formatter rectangle      * @return the formatter rectangle      */
specifier|public
name|PDRectangle
name|getFormaterRectangle
parameter_list|()
block|{
return|return
name|formaterRectangle
return|;
block|}
comment|/**      * Sets formatter rectangle      * @param formaterRectangle      */
specifier|public
name|void
name|setFormaterRectangle
parameter_list|(
name|PDRectangle
name|formaterRectangle
parameter_list|)
block|{
name|this
operator|.
name|formaterRectangle
operator|=
name|formaterRectangle
expr_stmt|;
block|}
comment|/**      * Sets HolderFormStream      * @return the holder form stream      */
specifier|public
name|PDStream
name|getHolderFormStream
parameter_list|()
block|{
return|return
name|holderFormStream
return|;
block|}
comment|/**      * Sets stream of holder form Stream       * @param holderFormStream      */
specifier|public
name|void
name|setHolderFormStream
parameter_list|(
name|PDStream
name|holderFormStream
parameter_list|)
block|{
name|this
operator|.
name|holderFormStream
operator|=
name|holderFormStream
expr_stmt|;
block|}
comment|/**      * Gets Holder form.      * That form is here<b> AcroForm/DR/XObject/{holder form name}</b>      * By default, name stars with FRM. We also add number of form      * to the name.      * @return the holder form      */
specifier|public
name|PDFormXObject
name|getHolderForm
parameter_list|()
block|{
return|return
name|holderForm
return|;
block|}
comment|/**      * In the structure, form will be contained by XObject in the<b>AcroForm/DR/</b>      * @param holderForm      */
specifier|public
name|void
name|setHolderForm
parameter_list|(
name|PDFormXObject
name|holderForm
parameter_list|)
block|{
name|this
operator|.
name|holderForm
operator|=
name|holderForm
expr_stmt|;
block|}
comment|/**      * Gets Holder form resources      * @return the holder form's resources      */
specifier|public
name|PDResources
name|getHolderFormResources
parameter_list|()
block|{
return|return
name|holderFormResources
return|;
block|}
comment|/**      * Sets holder form resources      * @param holderFormResources      */
specifier|public
name|void
name|setHolderFormResources
parameter_list|(
name|PDResources
name|holderFormResources
parameter_list|)
block|{
name|this
operator|.
name|holderFormResources
operator|=
name|holderFormResources
expr_stmt|;
block|}
comment|/**      * Gets AppearanceDictionary      * That is<b>/AP</b> entry the appearance dictionary.      * @return the Appearance Dictionary      */
specifier|public
name|PDAppearanceDictionary
name|getAppearanceDictionary
parameter_list|()
block|{
return|return
name|appearanceDictionary
return|;
block|}
comment|/**      * Sets AppearanceDictionary      * That is<b>/AP</b> entry the appearance dictionary.      * @param appearanceDictionary      */
specifier|public
name|void
name|setAppearanceDictionary
parameter_list|(
name|PDAppearanceDictionary
name|appearanceDictionary
parameter_list|)
block|{
name|this
operator|.
name|appearanceDictionary
operator|=
name|appearanceDictionary
expr_stmt|;
block|}
comment|/**      * Gets Inner form Stream.      * @return the inner form stream      */
specifier|public
name|PDStream
name|getInnterFormStream
parameter_list|()
block|{
return|return
name|innterFormStream
return|;
block|}
comment|/**      * Sets inner form stream      * @param innterFormStream      */
specifier|public
name|void
name|setInnterFormStream
parameter_list|(
name|PDStream
name|innterFormStream
parameter_list|)
block|{
name|this
operator|.
name|innterFormStream
operator|=
name|innterFormStream
expr_stmt|;
block|}
comment|/**      * Gets inner form Resource      * @return the inner form's resources      */
specifier|public
name|PDResources
name|getInnerFormResources
parameter_list|()
block|{
return|return
name|innerFormResources
return|;
block|}
comment|/**      * Sets inner form resource      * @param innerFormResources      */
specifier|public
name|void
name|setInnerFormResources
parameter_list|(
name|PDResources
name|innerFormResources
parameter_list|)
block|{
name|this
operator|.
name|innerFormResources
operator|=
name|innerFormResources
expr_stmt|;
block|}
comment|/**      * Gets inner form that is in this location:      *<b> AcroForm/DR/XObject/{holder form name}/Resources/XObject/{inner name}</b>      * By default inner form name starts with "n". Then we add number of form      * to the name.      * @return the inner form      */
specifier|public
name|PDFormXObject
name|getInnerForm
parameter_list|()
block|{
return|return
name|innerForm
return|;
block|}
comment|/**      * sets inner form to this location:      *<b> AcroForm/DR/XObject/{holder form name}/Resources/XObject/{destination}</b>      * @param innerForm      */
specifier|public
name|void
name|setInnerForm
parameter_list|(
name|PDFormXObject
name|innerForm
parameter_list|)
block|{
name|this
operator|.
name|innerForm
operator|=
name|innerForm
expr_stmt|;
block|}
comment|/**      * Gets name of inner form      * @return the inner forms's name      */
specifier|public
name|COSName
name|getInnerFormName
parameter_list|()
block|{
return|return
name|innerFormName
return|;
block|}
comment|/**      * Sets inner form name      * @param innerFormName      */
specifier|public
name|void
name|setInnerFormName
parameter_list|(
name|COSName
name|innerFormName
parameter_list|)
block|{
name|this
operator|.
name|innerFormName
operator|=
name|innerFormName
expr_stmt|;
block|}
comment|/**      * Gets Image form stream      * @return the image form's stream      */
specifier|public
name|PDStream
name|getImageFormStream
parameter_list|()
block|{
return|return
name|imageFormStream
return|;
block|}
comment|/**      * Sets image form stream      * @param imageFormStream      */
specifier|public
name|void
name|setImageFormStream
parameter_list|(
name|PDStream
name|imageFormStream
parameter_list|)
block|{
name|this
operator|.
name|imageFormStream
operator|=
name|imageFormStream
expr_stmt|;
block|}
comment|/**      * Gets image form resources      * @return the image form's resources      */
specifier|public
name|PDResources
name|getImageFormResources
parameter_list|()
block|{
return|return
name|imageFormResources
return|;
block|}
comment|/**      * Sets image form resource      * @param imageFormResources      */
specifier|public
name|void
name|setImageFormResources
parameter_list|(
name|PDResources
name|imageFormResources
parameter_list|)
block|{
name|this
operator|.
name|imageFormResources
operator|=
name|imageFormResources
expr_stmt|;
block|}
comment|/**      * Gets Image form. Image form is in this structure:       *<b>/AcroForm/DR/{holder form}/Resources/XObject /{inner form}</b>      * /Resources/XObject/{image form name}.      * @return the image form      */
specifier|public
name|PDFormXObject
name|getImageForm
parameter_list|()
block|{
return|return
name|imageForm
return|;
block|}
comment|/**      * Sets Image form. Image form will be in this structure:       *<b>/AcroForm/DR/{holder form}/Resources/XObject /{inner form}      * /Resources/XObject/{image form name}.</b> By default we start      *  image form name with "img". Then we  add number of image       *  form to the form name.      * Sets image form      * @param imageForm      */
specifier|public
name|void
name|setImageForm
parameter_list|(
name|PDFormXObject
name|imageForm
parameter_list|)
block|{
name|this
operator|.
name|imageForm
operator|=
name|imageForm
expr_stmt|;
block|}
comment|/**      * Gets image form name      * @return the image form's name      */
specifier|public
name|COSName
name|getImageFormName
parameter_list|()
block|{
return|return
name|imageFormName
return|;
block|}
comment|/**      * Sets image form name      * @param imageFormName      */
specifier|public
name|void
name|setImageFormName
parameter_list|(
name|COSName
name|imageFormName
parameter_list|)
block|{
name|this
operator|.
name|imageFormName
operator|=
name|imageFormName
expr_stmt|;
block|}
comment|/**      * Gets visible signature image name      * @return the visible signature's image name      */
specifier|public
name|COSName
name|getImageName
parameter_list|()
block|{
return|return
name|imageName
return|;
block|}
comment|/**      * Sets visible signature image name      * @param imageName      */
specifier|public
name|void
name|setImageName
parameter_list|(
name|COSName
name|imageName
parameter_list|)
block|{
name|this
operator|.
name|imageName
operator|=
name|imageName
expr_stmt|;
block|}
comment|/**      * Gets COSDocument of visible Signature.      * @see org.apache.pdfbox.cos.COSDocument      * @return the visual signature      */
specifier|public
name|COSDocument
name|getVisualSignature
parameter_list|()
block|{
return|return
name|visualSignature
return|;
block|}
comment|/**      *       * Sets COSDocument of visible Signature.      * @see org.apache.pdfbox.cos.COSDocument      * @param visualSignature      */
specifier|public
name|void
name|setVisualSignature
parameter_list|(
name|COSDocument
name|visualSignature
parameter_list|)
block|{
name|this
operator|.
name|visualSignature
operator|=
name|visualSignature
expr_stmt|;
block|}
comment|/**      * Gets acroFormFields      * @return the AcroForm fields      */
specifier|public
name|List
argument_list|<
name|PDField
argument_list|>
name|getAcroFormFields
parameter_list|()
block|{
return|return
name|acroFormFields
return|;
block|}
comment|/**      * Sets acroFormFields      * @param acroFormFields      */
specifier|public
name|void
name|setAcroFormFields
parameter_list|(
name|List
argument_list|<
name|PDField
argument_list|>
name|acroFormFields
parameter_list|)
block|{
name|this
operator|.
name|acroFormFields
operator|=
name|acroFormFields
expr_stmt|;
block|}
comment|/**     * Gets AP of the created template     * @return the templates Appearance Stream     * @throws IOException     */
specifier|public
name|ByteArrayInputStream
name|getTemplateAppearanceStream
parameter_list|()
throws|throws
name|IOException
block|{
name|COSDocument
name|visualSignature
init|=
name|getVisualSignature
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|memoryOut
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|COSWriter
name|memoryWriter
init|=
operator|new
name|COSWriter
argument_list|(
name|memoryOut
argument_list|)
decl_stmt|;
name|memoryWriter
operator|.
name|write
argument_list|(
name|visualSignature
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|input
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|memoryOut
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|getTemplate
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|input
return|;
block|}
comment|/**      * Gets Widget Dictionary.      * {@link PDTerminalField}      * @see PDTerminalField#getWidget()      * @return the widget dictionary      */
specifier|public
name|COSDictionary
name|getWidgetDictionary
parameter_list|()
block|{
return|return
name|widgetDictionary
return|;
block|}
comment|/**      * Sets Widget Dictionary.      * {@link PDTerminalField}      * @see PDTerminalField#getWidget()      * @param widgetDictionary      */
specifier|public
name|void
name|setWidgetDictionary
parameter_list|(
name|COSDictionary
name|widgetDictionary
parameter_list|)
block|{
name|this
operator|.
name|widgetDictionary
operator|=
name|widgetDictionary
expr_stmt|;
block|}
block|}
end_class

end_unit

