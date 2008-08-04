begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2004, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|fdf
package|;
end_package

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
name|Writer
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
name|org
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
name|pdfbox
operator|.
name|cos
operator|.
name|COSString
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|COSArrayList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|filespecification
operator|.
name|PDFileSpecification
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|filespecification
operator|.
name|PDSimpleFileSpecification
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_comment
comment|/**  * This represents an FDF dictionary that is part of the FDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.9 $  */
end_comment

begin_class
specifier|public
class|class
name|FDFDictionary
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|fdf
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFDictionary
parameter_list|()
block|{
name|fdf
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param fdfDictionary The FDF documents catalog.      */
specifier|public
name|FDFDictionary
parameter_list|(
name|COSDictionary
name|fdfDictionary
parameter_list|)
block|{
name|fdf
operator|=
name|fdfDictionary
expr_stmt|;
block|}
comment|/**      * This will create an FDF dictionary from an XFDF XML document.      *       * @param fdfXML The XML document that contains the XFDF data.      * @throws IOException If there is an error reading from the dom.      */
specifier|public
name|FDFDictionary
parameter_list|(
name|Element
name|fdfXML
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|()
expr_stmt|;
name|NodeList
name|nodeList
init|=
name|fdfXML
operator|.
name|getChildNodes
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
name|nodeList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|child
init|=
operator|(
name|Element
operator|)
name|node
decl_stmt|;
if|if
condition|(
name|child
operator|.
name|getTagName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"f"
argument_list|)
condition|)
block|{
name|PDSimpleFileSpecification
name|fs
init|=
operator|new
name|PDSimpleFileSpecification
argument_list|()
decl_stmt|;
name|fs
operator|.
name|setFile
argument_list|(
name|child
operator|.
name|getAttribute
argument_list|(
literal|"href"
argument_list|)
argument_list|)
expr_stmt|;
name|setFile
argument_list|(
name|fs
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|child
operator|.
name|getTagName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"ids"
argument_list|)
condition|)
block|{
name|COSArray
name|ids
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|String
name|original
init|=
name|child
operator|.
name|getAttribute
argument_list|(
literal|"original"
argument_list|)
decl_stmt|;
name|String
name|modified
init|=
name|child
operator|.
name|getAttribute
argument_list|(
literal|"modified"
argument_list|)
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|COSString
operator|.
name|createFromHexString
argument_list|(
name|original
argument_list|)
argument_list|)
expr_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|COSString
operator|.
name|createFromHexString
argument_list|(
name|modified
argument_list|)
argument_list|)
expr_stmt|;
name|setID
argument_list|(
name|ids
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|child
operator|.
name|getTagName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"fields"
argument_list|)
condition|)
block|{
name|NodeList
name|fields
init|=
name|child
operator|.
name|getElementsByTagName
argument_list|(
literal|"field"
argument_list|)
decl_stmt|;
name|List
name|fieldList
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|f
init|=
literal|0
init|;
name|f
operator|<
name|fields
operator|.
name|getLength
argument_list|()
condition|;
name|f
operator|++
control|)
block|{
name|fieldList
operator|.
name|add
argument_list|(
operator|new
name|FDFField
argument_list|(
operator|(
name|Element
operator|)
name|fields
operator|.
name|item
argument_list|(
name|f
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setFields
argument_list|(
name|fieldList
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|child
operator|.
name|getTagName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"annots"
argument_list|)
condition|)
block|{
name|NodeList
name|annots
init|=
name|child
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|List
name|annotList
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|annots
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Node
name|annotNode
init|=
name|annots
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotNode
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|annot
init|=
operator|(
name|Element
operator|)
name|annotNode
decl_stmt|;
if|if
condition|(
name|annot
operator|.
name|getNodeName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"text"
argument_list|)
condition|)
block|{
name|annotList
operator|.
name|add
argument_list|(
operator|new
name|FDFAnnotationText
argument_list|(
name|annot
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
literal|"Error: Unknown annotation type '"
operator|+
name|annot
operator|.
name|getNodeName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
name|setAnnotations
argument_list|(
name|annotList
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * This will write this element as an XML document.      *       * @param output The stream to write the xml to.      *       * @throws IOException If there is an error writing the XML.      */
specifier|public
name|void
name|writeXML
parameter_list|(
name|Writer
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFileSpecification
name|fs
init|=
name|this
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|fs
operator|!=
literal|null
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
literal|"<f href=\""
operator|+
name|fs
operator|.
name|getFile
argument_list|()
operator|+
literal|"\" />\n"
argument_list|)
expr_stmt|;
block|}
name|COSArray
name|ids
init|=
name|this
operator|.
name|getID
argument_list|()
decl_stmt|;
if|if
condition|(
name|ids
operator|!=
literal|null
condition|)
block|{
name|COSString
name|original
init|=
operator|(
name|COSString
operator|)
name|ids
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|COSString
name|modified
init|=
operator|(
name|COSString
operator|)
name|ids
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|output
operator|.
name|write
argument_list|(
literal|"<ids original=\""
operator|+
name|original
operator|.
name|getHexString
argument_list|()
operator|+
literal|"\" "
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
literal|"modified=\""
operator|+
name|modified
operator|.
name|getHexString
argument_list|()
operator|+
literal|"\" />\n"
argument_list|)
expr_stmt|;
block|}
name|List
name|fields
init|=
name|getFields
argument_list|()
decl_stmt|;
if|if
condition|(
name|fields
operator|!=
literal|null
operator|&&
name|fields
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
literal|"<fields>\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fields
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
operator|(
operator|(
name|FDFField
operator|)
name|fields
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|writeXML
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|write
argument_list|(
literal|"</fields>\n"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|fdf
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|fdf
return|;
block|}
comment|/**      * The source file or target file: the PDF document file that      * this FDF file was exported from or is intended to be imported into.      *      * @return The F entry of the FDF dictionary.      *       * @throws IOException If there is an error creating the file spec.      */
specifier|public
name|PDFileSpecification
name|getFile
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDFileSpecification
operator|.
name|createFS
argument_list|(
name|fdf
operator|.
name|getDictionaryObject
argument_list|(
literal|"F"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the file specification.      *      * @param fs The file specification.      */
specifier|public
name|void
name|setFile
parameter_list|(
name|PDFileSpecification
name|fs
parameter_list|)
block|{
name|fdf
operator|.
name|setItem
argument_list|(
literal|"F"
argument_list|,
name|fs
argument_list|)
expr_stmt|;
block|}
comment|/**      * This is the FDF id.      *      * @return The FDF ID.      */
specifier|public
name|COSArray
name|getID
parameter_list|()
block|{
return|return
operator|(
name|COSArray
operator|)
name|fdf
operator|.
name|getDictionaryObject
argument_list|(
literal|"ID"
argument_list|)
return|;
block|}
comment|/**      * This will set the FDF id.      *      * @param id The new id for the FDF.      */
specifier|public
name|void
name|setID
parameter_list|(
name|COSArray
name|id
parameter_list|)
block|{
name|fdf
operator|.
name|setItem
argument_list|(
literal|"ID"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the list of FDF Fields.  This will return a list of FDFField      * objects.      *      * @return A list of FDF fields.      */
specifier|public
name|List
name|getFields
parameter_list|()
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|fieldArray
init|=
operator|(
name|COSArray
operator|)
name|fdf
operator|.
name|getDictionaryObject
argument_list|(
literal|"Fields"
argument_list|)
decl_stmt|;
if|if
condition|(
name|fieldArray
operator|!=
literal|null
condition|)
block|{
name|List
name|fields
init|=
operator|new
name|ArrayList
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
name|fieldArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|fields
operator|.
name|add
argument_list|(
operator|new
name|FDFField
argument_list|(
operator|(
name|COSDictionary
operator|)
name|fieldArray
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|fields
argument_list|,
name|fieldArray
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the list of fields.  This should be a list of FDFField objects.      *      * @param fields The list of fields.      */
specifier|public
name|void
name|setFields
parameter_list|(
name|List
name|fields
parameter_list|)
block|{
name|fdf
operator|.
name|setItem
argument_list|(
literal|"Fields"
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|fields
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the status string to be displayed as the result of an      * action.      *      * @return The status.      */
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|fdf
operator|.
name|getString
argument_list|(
literal|"Status"
argument_list|)
return|;
block|}
comment|/**      * This will set the status string.      *      * @param status The new status string.      */
specifier|public
name|void
name|setStatus
parameter_list|(
name|String
name|status
parameter_list|)
block|{
name|fdf
operator|.
name|setString
argument_list|(
literal|"Status"
argument_list|,
name|status
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the list of FDF Pages.  This will return a list of FDFPage objects.      *      * @return A list of FDF pages.      */
specifier|public
name|List
name|getPages
parameter_list|()
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|pageArray
init|=
operator|(
name|COSArray
operator|)
name|fdf
operator|.
name|getDictionaryObject
argument_list|(
literal|"Pages"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pageArray
operator|!=
literal|null
condition|)
block|{
name|List
name|pages
init|=
operator|new
name|ArrayList
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
name|pageArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|pages
operator|.
name|add
argument_list|(
operator|new
name|FDFPage
argument_list|(
operator|(
name|COSDictionary
operator|)
name|pageArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|pages
argument_list|,
name|pageArray
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the list of pages.  This should be a list of FDFPage objects.      *      *      * @param pages The list of pages.      */
specifier|public
name|void
name|setPages
parameter_list|(
name|List
name|pages
parameter_list|)
block|{
name|fdf
operator|.
name|setItem
argument_list|(
literal|"Pages"
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|pages
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * The encoding to be used for a FDF field.  The default is PDFDocEncoding      * and this method will never return null.      *      * @return The encoding value.      */
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
name|String
name|encoding
init|=
name|fdf
operator|.
name|getNameAsString
argument_list|(
literal|"Encoding"
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|encoding
operator|=
literal|"PDFDocEncoding"
expr_stmt|;
block|}
return|return
name|encoding
return|;
block|}
comment|/**      * This will set the encoding.      *      * @param encoding The new encoding.      */
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|fdf
operator|.
name|setName
argument_list|(
literal|"Encoding"
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the list of FDF Annotations.  This will return a list of FDFAnnotation objects      * or null if the entry is not set.      *      * @return A list of FDF annotations.      *       * @throws IOException If there is an error creating the annotation list.      */
specifier|public
name|List
name|getAnnotations
parameter_list|()
throws|throws
name|IOException
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|annotArray
init|=
operator|(
name|COSArray
operator|)
name|fdf
operator|.
name|getDictionaryObject
argument_list|(
literal|"Annots"
argument_list|)
decl_stmt|;
if|if
condition|(
name|annotArray
operator|!=
literal|null
condition|)
block|{
name|List
name|annots
init|=
operator|new
name|ArrayList
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
name|annotArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|annots
operator|.
name|add
argument_list|(
name|FDFAnnotation
operator|.
name|create
argument_list|(
operator|(
name|COSDictionary
operator|)
name|annotArray
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|annots
argument_list|,
name|annotArray
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the list of annotations.  This should be a list of FDFAnnotation objects.      *      *      * @param annots The list of annotations.      */
specifier|public
name|void
name|setAnnotations
parameter_list|(
name|List
name|annots
parameter_list|)
block|{
name|fdf
operator|.
name|setItem
argument_list|(
literal|"Annots"
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|annots
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the incremental updates since the PDF was last opened.      *      * @return The differences entry of the FDF dictionary.      */
specifier|public
name|COSStream
name|getDifferences
parameter_list|()
block|{
return|return
operator|(
name|COSStream
operator|)
name|fdf
operator|.
name|getDictionaryObject
argument_list|(
literal|"Differences"
argument_list|)
return|;
block|}
comment|/**      * This will set the differences stream.      *      * @param diff The new differences stream.      */
specifier|public
name|void
name|setDifferences
parameter_list|(
name|COSStream
name|diff
parameter_list|)
block|{
name|fdf
operator|.
name|setItem
argument_list|(
literal|"Differences"
argument_list|,
name|diff
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the target frame in the browser to open this document.      *      * @return The target frame.      */
specifier|public
name|String
name|getTarget
parameter_list|()
block|{
return|return
name|fdf
operator|.
name|getString
argument_list|(
literal|"Target"
argument_list|)
return|;
block|}
comment|/**      * This will set the target frame in the browser to open this document.      *      * @param target The new target frame.      */
specifier|public
name|void
name|setTarget
parameter_list|(
name|String
name|target
parameter_list|)
block|{
name|fdf
operator|.
name|setString
argument_list|(
literal|"Target"
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the list of embedded FDF entries, or null if the entry is null.      * This will return a list of PDFileSpecification objects.      *      * @return A list of embedded FDF files.      *       * @throws IOException If there is an error creating the file spec.      */
specifier|public
name|List
name|getEmbeddedFDFs
parameter_list|()
throws|throws
name|IOException
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|embeddedArray
init|=
operator|(
name|COSArray
operator|)
name|fdf
operator|.
name|getDictionaryObject
argument_list|(
literal|"EmbeddedFDFs"
argument_list|)
decl_stmt|;
if|if
condition|(
name|embeddedArray
operator|!=
literal|null
condition|)
block|{
name|List
name|embedded
init|=
operator|new
name|ArrayList
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
name|embeddedArray
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|embedded
operator|.
name|add
argument_list|(
name|PDFileSpecification
operator|.
name|createFS
argument_list|(
name|embeddedArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|embedded
argument_list|,
name|embeddedArray
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the list of embedded FDFs.  This should be a list of      * PDFileSpecification objects.      *      *      * @param embedded The list of embedded FDFs.      */
specifier|public
name|void
name|setEmbeddedFDFs
parameter_list|(
name|List
name|embedded
parameter_list|)
block|{
name|fdf
operator|.
name|setItem
argument_list|(
literal|"EmbeddedFDFs"
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|embedded
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the java script entry.      *      * @return The java script entry describing javascript commands.      */
specifier|public
name|FDFJavaScript
name|getJavaScript
parameter_list|()
block|{
name|FDFJavaScript
name|fs
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|fdf
operator|.
name|getDictionaryObject
argument_list|(
literal|"JavaScript"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dic
operator|!=
literal|null
condition|)
block|{
name|fs
operator|=
operator|new
name|FDFJavaScript
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|fs
return|;
block|}
comment|/**      * This will set the JavaScript entry.      *      * @param js The javascript entries.      */
specifier|public
name|void
name|setJavaScript
parameter_list|(
name|FDFJavaScript
name|js
parameter_list|)
block|{
name|fdf
operator|.
name|setItem
argument_list|(
literal|"JavaScript"
argument_list|,
name|js
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

