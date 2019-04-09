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
name|util
package|;
end_package

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
name|FactoryConfigurationError
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
name|SAXException
import|;
end_import

begin_comment
comment|/**  * This class with handle some simple XML operations.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|XMLUtil
block|{
comment|/**      * Utility class, should not be instantiated.      *      */
specifier|private
name|XMLUtil
parameter_list|()
block|{     }
comment|/**      * This will parse an XML stream and create a DOM document.      *      * @param is The stream to get the XML from.      * @return The DOM document.      * @throws IOException It there is an error creating the dom.      */
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
return|return
name|parse
argument_list|(
name|is
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * This will parse an XML stream and create a DOM document.      *      * @param is The stream to get the XML from.      * @param nsAware activates namespace awareness of the parser      * @return The DOM document.      * @throws IOException It there is an error creating the dom.      */
specifier|public
specifier|static
name|Document
name|parse
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|boolean
name|nsAware
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
name|builderFactory
operator|.
name|setFeature
argument_list|(
literal|"http://apache.org/xml/features/disallow-doctype-decl"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|builderFactory
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/external-general-entities"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|builderFactory
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/external-parameter-entities"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|builderFactory
operator|.
name|setFeature
argument_list|(
literal|"http://apache.org/xml/features/nonvalidating/load-external-dtd"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|builderFactory
operator|.
name|setXIncludeAware
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|builderFactory
operator|.
name|setExpandEntityReferences
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|builderFactory
operator|.
name|setNamespaceAware
argument_list|(
name|nsAware
argument_list|)
expr_stmt|;
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
name|FactoryConfigurationError
decl||
name|ParserConfigurationException
decl||
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * This will get the text value of an element.      *      * @param node The node to get the text value for.      * @return The text of the node.      */
specifier|public
specifier|static
name|String
name|getNodeValue
parameter_list|(
name|Element
name|node
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|NodeList
name|children
init|=
name|node
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|int
name|numNodes
init|=
name|children
operator|.
name|getLength
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
name|numNodes
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
name|sb
operator|.
name|append
argument_list|(
name|next
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit
