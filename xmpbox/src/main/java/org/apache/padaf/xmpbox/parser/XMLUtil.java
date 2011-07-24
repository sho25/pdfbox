begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|parser
package|;
end_package

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
name|io
operator|.
name|StringWriter
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
name|transform
operator|.
name|OutputKeys
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
name|Result
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
name|Transformer
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
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
name|dom
operator|.
name|DOMSource
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
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|Elementable
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

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Text
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
name|InputSource
import|;
end_import

begin_comment
comment|/**  * This class with handle some simple XML operations.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author<a href="mailto:chris@oezbek.net">Christopher Oezbek</a>  *   * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|XMLUtil
block|{
comment|/** 	 * Utility class, should not be instantiated. 	 *  	 */
specifier|private
name|XMLUtil
parameter_list|()
block|{ 	}
comment|/** 	 * This will parse an XML stream and create a DOM document. 	 *  	 * @param is 	 *            The stream to get the XML from. 	 * @return The DOM document. 	 * @throws IOException 	 *             It there is an error creating the dom. 	 */
specifier|public
specifier|static
name|Document
name|parse
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|DocumentBuilderFactory
name|builderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|DocumentBuilder
name|builder
init|=
name|builderFactory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
return|return
name|builder
operator|.
name|parse
argument_list|(
name|is
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IOException
name|thrown
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
name|thrown
throw|;
block|}
block|}
comment|/** 	 * This will parse an InputSource and create a DOM document. 	 *  	 * @param is 	 *            The stream to get the XML from. 	 * @return The DOM document. 	 * @throws IOException 	 *             It there is an error creating the dom. 	 */
specifier|public
specifier|static
name|Document
name|parse
parameter_list|(
name|InputSource
name|is
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|DocumentBuilderFactory
name|builderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|DocumentBuilder
name|builder
init|=
name|builderFactory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
return|return
name|builder
operator|.
name|parse
argument_list|(
name|is
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IOException
name|thrown
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
name|thrown
throw|;
block|}
block|}
comment|/** 	 * This will parse an XML stream and create a DOM document. 	 *  	 * @param fileName 	 *            The file to get the XML from. 	 * @return The DOM document. 	 * @throws IOException 	 *             It there is an error creating the dom. 	 */
specifier|public
specifier|static
name|Document
name|parse
parameter_list|(
name|String
name|fileName
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|DocumentBuilderFactory
name|builderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|DocumentBuilder
name|builder
init|=
name|builderFactory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
return|return
name|builder
operator|.
name|parse
argument_list|(
name|fileName
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IOException
name|thrown
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
name|thrown
throw|;
block|}
block|}
comment|/** 	 * Create a new blank XML document. 	 *  	 * @return The new blank XML document. 	 *  	 * @throws IOException 	 *             If there is an error creating the XML document. 	 */
specifier|public
specifier|static
name|Document
name|newDocument
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|DocumentBuilderFactory
name|builderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|DocumentBuilder
name|builder
init|=
name|builderFactory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
return|return
name|builder
operator|.
name|newDocument
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IOException
name|thrown
init|=
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
name|thrown
throw|;
block|}
block|}
comment|/** 	 * Get the first instance of an element by name. 	 *  	 * @param parent 	 *            The parent to get the element from. 	 * @param elementName 	 *            The name of the element to look for. 	 * @return The element or null if it is not found. 	 */
specifier|public
specifier|static
name|Element
name|getElement
parameter_list|(
name|Element
name|parent
parameter_list|,
name|String
name|elementName
parameter_list|)
block|{
name|Element
name|retval
init|=
literal|null
decl_stmt|;
name|NodeList
name|children
init|=
name|parent
operator|.
name|getElementsByTagName
argument_list|(
name|elementName
argument_list|)
decl_stmt|;
if|if
condition|(
name|children
operator|.
name|getLength
argument_list|()
operator|>
literal|0
condition|)
block|{
name|retval
operator|=
operator|(
name|Element
operator|)
name|children
operator|.
name|item
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/** 	 * Get the integer value of a subnode. 	 *  	 * @param parent 	 *            The parent element that holds the values. 	 * @param nodeName 	 *            The name of the node that holds the integer value. 	 *  	 * @return The integer value of the node. 	 */
specifier|public
specifier|static
name|Integer
name|getIntValue
parameter_list|(
name|Element
name|parent
parameter_list|,
name|String
name|nodeName
parameter_list|)
block|{
name|String
name|intVal
init|=
name|XMLUtil
operator|.
name|getStringValue
argument_list|(
name|XMLUtil
operator|.
name|getElement
argument_list|(
name|parent
argument_list|,
name|nodeName
argument_list|)
argument_list|)
decl_stmt|;
name|Integer
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|intVal
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|Integer
argument_list|(
name|intVal
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/** 	 * Set the integer value of an element. 	 *  	 * @param parent 	 *            The parent element that will hold this subelement. 	 * @param nodeName 	 *            The name of the subelement. 	 * @param intValue 	 *            The value to set. 	 */
specifier|public
specifier|static
name|void
name|setIntValue
parameter_list|(
name|Element
name|parent
parameter_list|,
name|String
name|nodeName
parameter_list|,
name|Integer
name|intValue
parameter_list|)
block|{
name|Element
name|currentValue
init|=
name|getElement
argument_list|(
name|parent
argument_list|,
name|nodeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|intValue
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|currentValue
operator|!=
literal|null
condition|)
block|{
name|parent
operator|.
name|removeChild
argument_list|(
name|currentValue
argument_list|)
expr_stmt|;
block|}
comment|// else it doesn't exist so we don't need to remove it.
block|}
else|else
block|{
if|if
condition|(
name|currentValue
operator|==
literal|null
condition|)
block|{
name|currentValue
operator|=
name|parent
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElement
argument_list|(
name|nodeName
argument_list|)
expr_stmt|;
name|parent
operator|.
name|appendChild
argument_list|(
name|currentValue
argument_list|)
expr_stmt|;
block|}
name|XMLUtil
operator|.
name|setStringValue
argument_list|(
name|currentValue
argument_list|,
name|intValue
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Get the value of a subnode. 	 *  	 * @param parent 	 *            The parent element that holds the values. 	 * @param nodeName 	 *            The name of the node that holds the value. 	 *  	 * @return The value of the sub node. 	 */
specifier|public
specifier|static
name|String
name|getStringValue
parameter_list|(
name|Element
name|parent
parameter_list|,
name|String
name|nodeName
parameter_list|)
block|{
return|return
name|XMLUtil
operator|.
name|getStringValue
argument_list|(
name|XMLUtil
operator|.
name|getElement
argument_list|(
name|parent
argument_list|,
name|nodeName
argument_list|)
argument_list|)
return|;
block|}
comment|/** 	 * Set the value of an element. 	 *  	 * @param parent 	 *            The parent element that will hold this subelement. 	 * @param nodeName 	 *            The name of the subelement. 	 * @param nodeValue 	 *            The value to set. 	 */
specifier|public
specifier|static
name|void
name|setStringValue
parameter_list|(
name|Element
name|parent
parameter_list|,
name|String
name|nodeName
parameter_list|,
name|String
name|nodeValue
parameter_list|)
block|{
name|Element
name|currentValue
init|=
name|getElement
argument_list|(
name|parent
argument_list|,
name|nodeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodeValue
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|currentValue
operator|!=
literal|null
condition|)
block|{
name|parent
operator|.
name|removeChild
argument_list|(
name|currentValue
argument_list|)
expr_stmt|;
block|}
comment|// else it doesn't exist so we don't need to remove it.
block|}
else|else
block|{
if|if
condition|(
name|currentValue
operator|==
literal|null
condition|)
block|{
name|currentValue
operator|=
name|parent
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElement
argument_list|(
name|nodeName
argument_list|)
expr_stmt|;
name|parent
operator|.
name|appendChild
argument_list|(
name|currentValue
argument_list|)
expr_stmt|;
block|}
name|XMLUtil
operator|.
name|setStringValue
argument_list|(
name|currentValue
argument_list|,
name|nodeValue
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * This will get the text value of an element. 	 *  	 * @param node 	 *            The node to get the text value for. 	 * @return The text of the node. 	 */
specifier|public
specifier|static
name|String
name|getStringValue
parameter_list|(
name|Element
name|node
parameter_list|)
block|{
name|String
name|retval
init|=
literal|""
decl_stmt|;
name|NodeList
name|children
init|=
name|node
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
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|next
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|Text
condition|)
block|{
name|retval
operator|=
name|next
operator|.
name|getNodeValue
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/** 	 * This will set the text value of an element. 	 *  	 * @param node 	 *            The node to get the text value for. 	 * @param value 	 *            The new value to set the node to. 	 */
specifier|public
specifier|static
name|void
name|setStringValue
parameter_list|(
name|Element
name|node
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|NodeList
name|children
init|=
name|node
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
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|next
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|Text
condition|)
block|{
name|node
operator|.
name|removeChild
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
block|}
name|node
operator|.
name|appendChild
argument_list|(
name|node
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createTextNode
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Set an XML element document. 	 *  	 * @param parent 	 *            The parent document to set the value in. 	 * @param name 	 *            The name of the XML element to set. 	 * @param node 	 *            The node to set or clear. 	 */
specifier|public
specifier|static
name|void
name|setElementableValue
parameter_list|(
name|Element
name|parent
parameter_list|,
name|String
name|name
parameter_list|,
name|Elementable
name|node
parameter_list|)
block|{
name|NodeList
name|nodes
init|=
name|parent
operator|.
name|getElementsByTagName
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nodes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|parent
operator|.
name|removeChild
argument_list|(
name|nodes
operator|.
name|item
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|nodes
operator|.
name|getLength
argument_list|()
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|parent
operator|.
name|hasChildNodes
argument_list|()
condition|)
block|{
name|Node
name|firstChild
init|=
name|parent
operator|.
name|getChildNodes
argument_list|()
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|parent
operator|.
name|insertBefore
argument_list|(
name|node
operator|.
name|getElement
argument_list|()
argument_list|,
name|firstChild
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parent
operator|.
name|appendChild
argument_list|(
name|node
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Node
name|oldNode
init|=
name|nodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|parent
operator|.
name|replaceChild
argument_list|(
name|node
operator|.
name|getElement
argument_list|()
argument_list|,
name|oldNode
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** 	 * Save the XML document to a file. 	 *  	 * @param doc 	 *            The XML document to save. 	 * @param file 	 *            The file to save the document to. 	 * @param encoding 	 *            The encoding to save the file as. 	 *  	 * @throws TransformerException 	 *             If there is an error while saving the XML. 	 */
specifier|public
specifier|static
name|void
name|save
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|file
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|TransformerException
block|{
name|Transformer
name|transformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
comment|// initialize StreamResult with File object to save to file
name|Result
name|result
init|=
operator|new
name|StreamResult
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Save the XML document to an output stream. 	 *  	 * @param doc 	 *            The XML document to save. 	 * @param outStream 	 *            The stream to save the document to. 	 * @param encoding 	 *            The encoding to save the file as. 	 *  	 * @throws TransformerException 	 *             If there is an error while saving the XML. 	 */
specifier|public
specifier|static
name|void
name|save
parameter_list|(
name|Node
name|doc
parameter_list|,
name|OutputStream
name|outStream
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|TransformerException
block|{
name|Transformer
name|transformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
comment|// initialize StreamResult with File object to save to file
name|Result
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|outStream
argument_list|)
decl_stmt|;
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Convert the document to an array of bytes. 	 *  	 * @param doc 	 *            The XML document. 	 * @param encoding 	 *            The encoding of the output data. 	 *  	 * @return The XML document as an array of bytes. 	 *  	 * @throws TransformerException 	 *             If there is an error transforming to text. 	 */
specifier|public
specifier|static
name|byte
index|[]
name|asByteArray
parameter_list|(
name|Document
name|doc
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|TransformerException
block|{
name|Transformer
name|transformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|Result
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|writer
argument_list|)
decl_stmt|;
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|getBuffer
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
return|;
block|}
block|}
end_class

end_unit

