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
name|xmpbox
operator|.
name|xml
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|XmpConstants
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
name|XmpParsingException
operator|.
name|ErrorType
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
name|Attr
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

begin_class
specifier|public
specifier|final
class|class
name|DomHelper
block|{
specifier|private
name|DomHelper
parameter_list|()
block|{     }
specifier|public
specifier|static
name|Element
name|getUniqueElementChild
parameter_list|(
name|Element
name|description
parameter_list|)
throws|throws
name|XmpParsingException
block|{
name|NodeList
name|nl
init|=
name|description
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
operator|-
literal|1
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
name|nl
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
operator|instanceof
name|Element
condition|)
block|{
if|if
condition|(
name|pos
operator|>=
literal|0
condition|)
block|{
comment|// invalid : found two child elements
throw|throw
operator|new
name|XmpParsingException
argument_list|(
name|ErrorType
operator|.
name|Undefined
argument_list|,
literal|"Found two child elements in "
operator|+
name|description
argument_list|)
throw|;
block|}
else|else
block|{
name|pos
operator|=
name|i
expr_stmt|;
block|}
block|}
block|}
return|return
operator|(
name|Element
operator|)
name|nl
operator|.
name|item
argument_list|(
name|pos
argument_list|)
return|;
block|}
comment|/**      * Return the first child element of the element parameter. If there is no child, null is returned      *       * @param description      * @return the first child element. Might be null.      */
specifier|public
specifier|static
name|Element
name|getFirstChildElement
parameter_list|(
name|Element
name|description
parameter_list|)
block|{
name|NodeList
name|nl
init|=
name|description
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
name|nl
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
operator|instanceof
name|Element
condition|)
block|{
return|return
operator|(
name|Element
operator|)
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|Element
argument_list|>
name|getElementChildren
parameter_list|(
name|Element
name|description
parameter_list|)
block|{
name|NodeList
name|nl
init|=
name|description
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|nl
operator|.
name|getLength
argument_list|()
argument_list|)
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
name|nl
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
operator|instanceof
name|Element
condition|)
block|{
name|ret
operator|.
name|add
argument_list|(
operator|(
name|Element
operator|)
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|QName
name|getQName
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
operator|new
name|QName
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|,
name|element
operator|.
name|getPrefix
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isRdfDescription
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
operator|(
name|XmpConstants
operator|.
name|DEFAULT_RDF_PREFIX
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getPrefix
argument_list|()
argument_list|)
operator|&&
name|XmpConstants
operator|.
name|DESCRIPTION_NAME
operator|.
name|equals
argument_list|(
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isParseTypeResource
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|Attr
name|parseType
init|=
name|element
operator|.
name|getAttributeNodeNS
argument_list|(
name|XmpConstants
operator|.
name|RDF_NAMESPACE
argument_list|,
name|XmpConstants
operator|.
name|PARSE_TYPE
argument_list|)
decl_stmt|;
return|return
name|parseType
operator|!=
literal|null
operator|&&
name|XmpConstants
operator|.
name|RESOURCE_NAME
operator|.
name|equals
argument_list|(
name|parseType
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

