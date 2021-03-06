begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *  ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|parser
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|ValidationResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|NodeList
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
name|XPathConstants
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

begin_class
specifier|public
class|class
name|TestXmlResultParser
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ERROR_CODE
init|=
literal|"000"
decl_stmt|;
specifier|protected
name|XmlResultParser
name|parser
init|=
operator|new
name|XmlResultParser
argument_list|()
decl_stmt|;
specifier|protected
name|Document
name|document
decl_stmt|;
specifier|protected
name|Element
name|preflight
decl_stmt|;
specifier|protected
name|XPath
name|xpath
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
name|document
operator|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|newDocument
argument_list|()
expr_stmt|;
name|preflight
operator|=
name|parser
operator|.
name|generateResponseSkeleton
argument_list|(
name|document
argument_list|,
literal|"myname"
argument_list|,
literal|14
argument_list|)
expr_stmt|;
name|xpath
operator|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testOneError
parameter_list|()
throws|throws
name|Exception
block|{
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|result
operator|.
name|addError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
literal|"7"
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|.
name|createResponseWithError
argument_list|(
name|document
argument_list|,
literal|"pdftype"
argument_list|,
name|result
argument_list|,
name|preflight
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"errors[@count='1']"
argument_list|,
name|preflight
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
name|NodeList
name|nl
init|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"errors/error[@count='1']"
argument_list|,
name|preflight
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|nl
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTwoError
parameter_list|()
throws|throws
name|Exception
block|{
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|result
operator|.
name|addError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
literal|"7"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|addError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|.
name|createResponseWithError
argument_list|(
name|document
argument_list|,
literal|"pdftype"
argument_list|,
name|result
argument_list|,
name|preflight
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"errors[@count='2']"
argument_list|,
name|preflight
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
name|NodeList
name|nl
init|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"errors/error[@count='1']"
argument_list|,
name|preflight
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|nl
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSameErrorTwice
parameter_list|()
throws|throws
name|Exception
block|{
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|result
operator|.
name|addError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|addError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|.
name|createResponseWithError
argument_list|(
name|document
argument_list|,
literal|"pdftype"
argument_list|,
name|result
argument_list|,
name|preflight
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"errors[@count='2']"
argument_list|,
name|preflight
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"errors/error[@count='2']"
argument_list|,
name|preflight
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
name|Element
name|code
init|=
operator|(
name|Element
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"errors/error[@count='2']/code"
argument_list|,
name|preflight
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|ERROR_CODE
argument_list|,
name|code
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSameCodeWithDifferentMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|ValidationResult
name|result
init|=
operator|new
name|ValidationResult
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|result
operator|.
name|addError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_CODE
argument_list|,
literal|"message 1"
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|addError
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ERROR_CODE
argument_list|,
literal|"message 2"
argument_list|)
argument_list|)
expr_stmt|;
name|parser
operator|.
name|createResponseWithError
argument_list|(
name|document
argument_list|,
literal|"pdftype"
argument_list|,
name|result
argument_list|,
name|preflight
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"errors[@count='2']"
argument_list|,
name|preflight
argument_list|,
name|XPathConstants
operator|.
name|NODE
argument_list|)
argument_list|)
expr_stmt|;
name|NodeList
name|nl
init|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"errors/error[@count='1']"
argument_list|,
name|preflight
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|nl
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//    private void dump (Element element) throws Exception {
comment|//        Transformer transformer = TransformerFactory.newInstance().newTransformer();
comment|//        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
comment|//        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
comment|//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
comment|//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
comment|//        transformer.transform(new DOMSource(element), new StreamResult(System.out));
comment|//        System.out.flush();
comment|//    }
block|}
end_class

end_unit

