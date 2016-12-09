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
name|ByteArrayOutputStream
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
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
name|common
operator|.
name|PDMetadata
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
name|font
operator|.
name|PDFont
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
name|font
operator|.
name|PDType0Font
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
name|color
operator|.
name|PDOutputIntent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|DublinCoreSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|PDFAIdentificationSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|BadFieldValueException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|xml
operator|.
name|XmpSerializer
import|;
end_import

begin_comment
comment|/**  * Creates a simple PDF/A document.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CreatePDFA
block|{
specifier|private
name|CreatePDFA
parameter_list|()
block|{     }
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
throws|,
name|TransformerException
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
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: "
operator|+
name|CreatePDFA
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<output-file><Message><ttf-file>"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|file
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
name|String
name|message
init|=
name|args
index|[
literal|1
index|]
decl_stmt|;
name|String
name|fontfile
init|=
name|args
index|[
literal|2
index|]
decl_stmt|;
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
try|try
block|{
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
comment|// load the font as this needs to be embedded
name|PDFont
name|font
init|=
name|PDType0Font
operator|.
name|load
argument_list|(
name|doc
argument_list|,
operator|new
name|File
argument_list|(
name|fontfile
argument_list|)
argument_list|)
decl_stmt|;
comment|// A PDF/A file needs to have the font embedded if the font is used for text rendering
comment|// in rendering modes other than text rendering mode 3
comment|// This requirement includes the PDF standard fonts, so don't use their static PDFType1Font classes such as
comment|// PDFType1Font.HELVETICA.
if|if
condition|(
operator|!
name|font
operator|.
name|isEmbedded
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"PDF/A compliance requires that all fonts used for"
operator|+
literal|" text rendering in rendering modes other than rendering mode 3 are embedded."
argument_list|)
throw|;
block|}
comment|// create a page with the message
name|PDPageContentStream
name|contents
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|)
decl_stmt|;
name|contents
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contents
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|contents
operator|.
name|newLineAtOffset
argument_list|(
literal|100
argument_list|,
literal|700
argument_list|)
expr_stmt|;
name|contents
operator|.
name|showText
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|contents
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contents
operator|.
name|saveGraphicsState
argument_list|()
expr_stmt|;
name|contents
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// add XMP metadata
name|XMPMetadata
name|xmp
init|=
name|XMPMetadata
operator|.
name|createXMPMetadata
argument_list|()
decl_stmt|;
try|try
block|{
name|DublinCoreSchema
name|dc
init|=
name|xmp
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
decl_stmt|;
name|dc
operator|.
name|setTitle
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|PDFAIdentificationSchema
name|id
init|=
name|xmp
operator|.
name|createAndAddPFAIdentificationSchema
argument_list|()
decl_stmt|;
name|id
operator|.
name|setPart
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|id
operator|.
name|setConformance
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|XmpSerializer
name|serializer
init|=
operator|new
name|XmpSerializer
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|serializer
operator|.
name|serialize
argument_list|(
name|xmp
argument_list|,
name|baos
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|PDMetadata
name|metadata
init|=
operator|new
name|PDMetadata
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|importXMPMetadata
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setMetadata
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadFieldValueException
name|e
parameter_list|)
block|{
comment|// won't happen here, as the provided value is valid
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// sRGB output intent
name|InputStream
name|colorProfile
init|=
name|CreatePDFA
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/pdfbox/resources/pdfa/sRGB.icc"
argument_list|)
decl_stmt|;
name|PDOutputIntent
name|intent
init|=
operator|new
name|PDOutputIntent
argument_list|(
name|doc
argument_list|,
name|colorProfile
argument_list|)
decl_stmt|;
name|intent
operator|.
name|setInfo
argument_list|(
literal|"sRGB IEC61966-2.1"
argument_list|)
expr_stmt|;
name|intent
operator|.
name|setOutputCondition
argument_list|(
literal|"sRGB IEC61966-2.1"
argument_list|)
expr_stmt|;
name|intent
operator|.
name|setOutputConditionIdentifier
argument_list|(
literal|"sRGB IEC61966-2.1"
argument_list|)
expr_stmt|;
name|intent
operator|.
name|setRegistryName
argument_list|(
literal|"http://www.color.org"
argument_list|)
expr_stmt|;
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|addOutputIntent
argument_list|(
name|intent
argument_list|)
expr_stmt|;
name|doc
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

