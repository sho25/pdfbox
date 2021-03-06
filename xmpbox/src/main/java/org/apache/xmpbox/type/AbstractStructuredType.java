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
name|type
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractStructuredType
extends|extends
name|AbstractComplexProperty
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|STRUCTURE_ARRAY_NAME
init|=
literal|"li"
decl_stmt|;
specifier|private
name|String
name|namespace
decl_stmt|;
specifier|private
name|String
name|preferedPrefix
decl_stmt|;
specifier|private
name|String
name|prefix
decl_stmt|;
specifier|public
name|AbstractStructuredType
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|this
argument_list|(
name|metadata
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractStructuredType
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|namespaceURI
parameter_list|)
block|{
name|this
argument_list|(
name|metadata
argument_list|,
name|namespaceURI
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|StructuredType
name|st
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|StructuredType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|st
operator|!=
literal|null
condition|)
block|{
comment|// init with annotation
name|this
operator|.
name|namespace
operator|=
name|st
operator|.
name|namespace
argument_list|()
expr_stmt|;
name|this
operator|.
name|preferedPrefix
operator|=
name|st
operator|.
name|preferedPrefix
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|" StructuredType annotation cannot be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|prefix
operator|=
name|this
operator|.
name|preferedPrefix
expr_stmt|;
block|}
specifier|public
name|AbstractStructuredType
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|fieldPrefix
parameter_list|,
name|String
name|propertyName
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|propertyName
argument_list|)
expr_stmt|;
name|StructuredType
name|st
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|StructuredType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|st
operator|!=
literal|null
condition|)
block|{
comment|// init with annotation
name|this
operator|.
name|namespace
operator|=
name|st
operator|.
name|namespace
argument_list|()
expr_stmt|;
name|this
operator|.
name|preferedPrefix
operator|=
name|st
operator|.
name|preferedPrefix
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// init with parameters
if|if
condition|(
name|namespaceURI
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Both StructuredType annotation and namespace parameter cannot be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|namespace
operator|=
name|namespaceURI
expr_stmt|;
name|this
operator|.
name|preferedPrefix
operator|=
name|fieldPrefix
expr_stmt|;
block|}
name|this
operator|.
name|prefix
operator|=
name|fieldPrefix
operator|==
literal|null
condition|?
name|this
operator|.
name|preferedPrefix
else|:
name|fieldPrefix
expr_stmt|;
block|}
comment|/**      * Get the namespace URI of this entity      *       * @return the namespace URI      */
specifier|public
specifier|final
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
specifier|final
name|void
name|setNamespace
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|ns
expr_stmt|;
block|}
comment|/**      * Get the prefix of this entity      *       * @return the prefix specified      */
specifier|public
specifier|final
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
specifier|public
specifier|final
name|void
name|setPrefix
parameter_list|(
name|String
name|pf
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|pf
expr_stmt|;
block|}
specifier|public
specifier|final
name|String
name|getPreferedPrefix
parameter_list|()
block|{
return|return
name|preferedPrefix
return|;
block|}
specifier|protected
name|void
name|addSimpleProperty
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|TypeMapping
name|tm
init|=
name|getMetadata
argument_list|()
operator|.
name|getTypeMapping
argument_list|()
decl_stmt|;
name|AbstractSimpleProperty
name|asp
init|=
name|tm
operator|.
name|instanciateSimpleField
argument_list|(
name|getClass
argument_list|()
argument_list|,
literal|null
argument_list|,
name|getPrefix
argument_list|()
argument_list|,
name|propertyName
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|addProperty
argument_list|(
name|asp
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|getPropertyValueAsString
parameter_list|(
name|String
name|fieldName
parameter_list|)
block|{
name|AbstractSimpleProperty
name|absProp
init|=
operator|(
name|AbstractSimpleProperty
operator|)
name|getProperty
argument_list|(
name|fieldName
argument_list|)
decl_stmt|;
if|if
condition|(
name|absProp
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|absProp
operator|.
name|getStringValue
argument_list|()
return|;
block|}
block|}
specifier|protected
name|Calendar
name|getDatePropertyAsCalendar
parameter_list|(
name|String
name|fieldName
parameter_list|)
block|{
name|DateType
name|absProp
init|=
operator|(
name|DateType
operator|)
name|getFirstEquivalentProperty
argument_list|(
name|fieldName
argument_list|,
name|DateType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|absProp
operator|!=
literal|null
condition|)
block|{
return|return
name|absProp
operator|.
name|getValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|TextType
name|createTextType
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|getMetadata
argument_list|()
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createText
argument_list|(
name|getNamespace
argument_list|()
argument_list|,
name|getPrefix
argument_list|()
argument_list|,
name|propertyName
argument_list|,
name|value
argument_list|)
return|;
block|}
specifier|public
name|ArrayProperty
name|createArrayProperty
parameter_list|(
name|String
name|propertyName
parameter_list|,
name|Cardinality
name|type
parameter_list|)
block|{
return|return
name|getMetadata
argument_list|()
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|createArrayProperty
argument_list|(
name|getNamespace
argument_list|()
argument_list|,
name|getPrefix
argument_list|()
argument_list|,
name|propertyName
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

