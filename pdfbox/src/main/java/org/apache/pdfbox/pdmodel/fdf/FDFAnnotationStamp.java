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
name|fdf
package|;
end_package

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
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpressionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|util
operator|.
name|Hex
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
name|Document
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_comment
comment|/**  * This represents a Stamp FDF annotation.  *  * @author Ben Litchfield  * @author Andrew Hung  */
end_comment

begin_class
specifier|public
class|class
name|FDFAnnotationStamp
extends|extends
name|FDFAnnotation
block|{
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
name|FDFAnnotationStamp
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * COS Model value for SubType entry.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUBTYPE
init|=
literal|"Stamp"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFAnnotationStamp
parameter_list|()
block|{
name|annot
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUBTYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param a An existing FDF Annotation.      */
specifier|public
name|FDFAnnotationStamp
parameter_list|(
name|COSDictionary
name|a
parameter_list|)
block|{
name|super
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param element An XFDF element.      *      * @throws IOException If there is an error extracting information from the element.      */
specifier|public
name|FDFAnnotationStamp
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|annot
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUBTYPE
argument_list|)
expr_stmt|;
comment|// PDFBOX-4437: Initialize the Stamp appearance from the XFDF
comment|// https://www.immagic.com/eLibrary/ARCHIVES/TECH/ADOBE/A070914X.pdf
comment|// appearance is only defined for stamps
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
comment|// Set the Appearance to the annotation
name|LOG
operator|.
name|debug
argument_list|(
literal|"Get the DOM Document for the stamp appearance"
argument_list|)
expr_stmt|;
name|String
name|base64EncodedAppearance
decl_stmt|;
try|try
block|{
name|base64EncodedAppearance
operator|=
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"appearance"
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XPathExpressionException
name|e
parameter_list|)
block|{
comment|// should not happen
name|LOG
operator|.
name|error
argument_list|(
literal|"Error while evaluating XPath expression for appearance: "
operator|+
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
name|byte
index|[]
name|decodedAppearanceXML
decl_stmt|;
try|try
block|{
name|decodedAppearanceXML
operator|=
name|Hex
operator|.
name|decodeBase64
argument_list|(
name|base64EncodedAppearance
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Bad base64 encoded appearance ignored"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|base64EncodedAppearance
operator|!=
literal|null
operator|&&
operator|!
name|base64EncodedAppearance
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Document
name|stampAppearance
init|=
name|getStampAppearanceDocument
argument_list|(
name|decodedAppearanceXML
argument_list|)
decl_stmt|;
name|Element
name|appearanceEl
init|=
name|stampAppearance
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
comment|// Is the root node have tag as DICT, error otherwise
if|if
condition|(
operator|!
name|appearanceEl
operator|.
name|getNodeName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"dict"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error while reading stamp document, "
operator|+
literal|"root should be 'dict' and not '"
operator|+
name|appearanceEl
operator|.
name|getNodeName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Generate and set the appearance dictionary to the stamp annotation"
argument_list|)
expr_stmt|;
name|annot
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AP
argument_list|,
operator|new
name|FDFStampAnnotationAppearance
argument_list|(
name|appearanceEl
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Parse the<param>xmlString</param> to DOM Document tree from XML content      */
specifier|private
name|Document
name|getStampAppearanceDocument
parameter_list|(
name|byte
index|[]
name|xml
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
comment|// Obtain DOM Document instance and create DocumentBuilder with default configuration
name|DocumentBuilder
name|builder
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
comment|// Parse the content to Document object
return|return
name|builder
operator|.
name|parse
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|xml
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error while converting appearance xml to document: "
operator|+
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error while converting appearance xml to document: "
operator|+
name|ex
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

