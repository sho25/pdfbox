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
name|type
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|XMPMetadata
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
name|parser
operator|.
name|PropMapping
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
name|schema
operator|.
name|PropertyAttributesAnnotation
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
name|schema
operator|.
name|XMPSchema
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
name|TypeDescription
operator|.
name|BasicType
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|TypeMapping
block|{
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|TypeDescription
argument_list|>
name|BASIC_TYPES
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractField
argument_list|>
argument_list|,
name|TypeDescription
argument_list|>
name|BASIC_CLASSES
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|TypeDescription
argument_list|>
name|DERIVED_TYPES
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractField
argument_list|>
argument_list|,
name|TypeDescription
argument_list|>
name|DERIVED_CLASSES
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|TypeDescription
argument_list|>
name|STRUCTURED_TYPES
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractField
argument_list|>
argument_list|,
name|TypeDescription
argument_list|>
name|STRUCTURED_CLASSES
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|TypeDescription
argument_list|>
name|STRUCTURED_NAMESPACES
decl_stmt|;
specifier|private
specifier|static
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|structuredTypes
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// no public constructor
specifier|private
name|TypeMapping
parameter_list|()
block|{ 		 	}
specifier|private
specifier|static
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|simplePropertyConstParams
init|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|XMPMetadata
operator|.
name|class
operator|,
name|String
operator|.
name|class
operator|,
name|String
operator|.
name|class
operator|,
name|String
operator|.
name|class
operator|,
name|Object
operator|.
name|class
block|}
empty_stmt|;
static|static
block|{
comment|// basic
name|BASIC_TYPES
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|TypeDescription
argument_list|>
argument_list|()
expr_stmt|;
name|BASIC_CLASSES
operator|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractField
argument_list|>
argument_list|,
name|TypeDescription
argument_list|>
argument_list|()
expr_stmt|;
name|addToBasicMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Text"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|TextType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToBasicMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Date"
argument_list|,
name|BasicType
operator|.
name|Date
argument_list|,
name|DateType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToBasicMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Boolean"
argument_list|,
name|BasicType
operator|.
name|Boolean
argument_list|,
name|BooleanType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToBasicMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Integer"
argument_list|,
name|BasicType
operator|.
name|Integer
argument_list|,
name|IntegerType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToBasicMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Real"
argument_list|,
name|BasicType
operator|.
name|Real
argument_list|,
name|RealType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// derived
name|DERIVED_TYPES
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|TypeDescription
argument_list|>
argument_list|()
expr_stmt|;
name|DERIVED_CLASSES
operator|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractField
argument_list|>
argument_list|,
name|TypeDescription
argument_list|>
argument_list|()
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"AgentName"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|AgentNameType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Choice"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|ChoiceType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"GUID"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|GUIDType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Lang Alt"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|TextType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Locale"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|LocaleType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"MIMEType"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|MIMEType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"ProperName"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|ProperNameType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"RenditionClass"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|RenditionClassType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"URL"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|URLType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"URI"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|URIType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"XPath"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|XPathType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToDerivedMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Part"
argument_list|,
name|BasicType
operator|.
name|Text
argument_list|,
name|PartType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// structured types
name|STRUCTURED_TYPES
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|TypeDescription
argument_list|>
argument_list|()
expr_stmt|;
name|STRUCTURED_CLASSES
operator|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|AbstractField
argument_list|>
argument_list|,
name|TypeDescription
argument_list|>
argument_list|()
expr_stmt|;
name|STRUCTURED_NAMESPACES
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|TypeDescription
argument_list|>
argument_list|()
expr_stmt|;
name|addToStructuredMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Thumbnail"
argument_list|,
literal|null
argument_list|,
name|ThumbnailType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToStructuredMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Layer"
argument_list|,
literal|null
argument_list|,
name|LayerType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToStructuredMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"ResourceEvent"
argument_list|,
literal|null
argument_list|,
name|ResourceEventType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToStructuredMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Job"
argument_list|,
literal|null
argument_list|,
name|JobType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToStructuredMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"ResourceRef"
argument_list|,
literal|null
argument_list|,
name|ResourceRefType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|addToStructuredMaps
argument_list|(
operator|new
name|TypeDescription
argument_list|(
literal|"Version"
argument_list|,
literal|null
argument_list|,
name|VersionType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|// PDF/A structured types
comment|//        addToStructuredMaps(new TypeDescription("PDFAField",null,PDFAFieldType.class));
comment|//        addToStructuredMaps(new TypeDescription("PDFAProperty",null,PDFAPropertyType.class));
comment|//        addToStructuredMaps(new TypeDescription("PDFAType",null,PDFATypeType.class));
comment|//        addToStructuredMaps(new TypeDescription("PDFASchema",null,PDFASchemaType.class));
block|}
specifier|private
specifier|static
name|void
name|addToBasicMaps
parameter_list|(
name|TypeDescription
name|td
parameter_list|)
block|{
name|BASIC_TYPES
operator|.
name|put
argument_list|(
name|td
operator|.
name|getType
argument_list|()
argument_list|,
name|td
argument_list|)
expr_stmt|;
name|BASIC_CLASSES
operator|.
name|put
argument_list|(
name|td
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|td
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|addToDerivedMaps
parameter_list|(
name|TypeDescription
name|td
parameter_list|)
block|{
name|DERIVED_TYPES
operator|.
name|put
argument_list|(
name|td
operator|.
name|getType
argument_list|()
argument_list|,
name|td
argument_list|)
expr_stmt|;
name|DERIVED_CLASSES
operator|.
name|put
argument_list|(
name|td
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|td
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|addToStructuredMaps
parameter_list|(
name|TypeDescription
name|td
parameter_list|)
block|{
name|STRUCTURED_TYPES
operator|.
name|put
argument_list|(
name|td
operator|.
name|getType
argument_list|()
argument_list|,
name|td
argument_list|)
expr_stmt|;
name|STRUCTURED_CLASSES
operator|.
name|put
argument_list|(
name|td
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|td
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|ns
init|=
operator|(
name|String
operator|)
name|td
operator|.
name|getTypeClass
argument_list|()
operator|.
name|getField
argument_list|(
literal|"ELEMENT_NS"
argument_list|)
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|STRUCTURED_NAMESPACES
operator|.
name|put
argument_list|(
name|ns
argument_list|,
name|td
argument_list|)
expr_stmt|;
name|PropMapping
name|pm
init|=
name|initializePropMapping
argument_list|(
name|ns
argument_list|,
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|AbstractStructuredType
argument_list|>
operator|)
name|td
operator|.
name|getTypeClass
argument_list|()
argument_list|)
decl_stmt|;
name|td
operator|.
name|setProperties
argument_list|(
name|pm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to init structured maps for "
operator|+
name|td
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to init structured maps for "
operator|+
name|td
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to init structured maps for "
operator|+
name|td
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to init structured maps for "
operator|+
name|td
operator|.
name|getTypeClass
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|)
block|{
comment|// search in basic
name|TypeDescription
name|td
init|=
name|BASIC_CLASSES
operator|.
name|get
argument_list|(
name|clz
argument_list|)
decl_stmt|;
comment|// search in derived
if|if
condition|(
name|td
operator|==
literal|null
condition|)
block|{
name|td
operator|=
name|DERIVED_CLASSES
operator|.
name|get
argument_list|(
name|clz
argument_list|)
expr_stmt|;
block|}
comment|// search in structured
if|if
condition|(
name|td
operator|==
literal|null
condition|)
block|{
name|td
operator|=
name|STRUCTURED_CLASSES
operator|.
name|get
argument_list|(
name|clz
argument_list|)
expr_stmt|;
block|}
comment|// return type if exists
return|return
operator|(
name|td
operator|!=
literal|null
operator|)
condition|?
name|td
operator|.
name|getType
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Return the type description linked the specified paramater. If the type      * parameter is an array, the TypeDescription of the elements of the array      * will be returned      *       * @param type      * @return      */
specifier|public
specifier|static
name|TypeDescription
name|getTypeDescription
parameter_list|(
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|BASIC_TYPES
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|BASIC_TYPES
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|DERIVED_TYPES
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|DERIVED_TYPES
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|STRUCTURED_TYPES
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|STRUCTURED_TYPES
operator|.
name|get
argument_list|(
name|type
argument_list|)
return|;
block|}
else|else
block|{
name|int
name|pos
init|=
name|type
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|>
literal|0
condition|)
block|{
return|return
name|getTypeDescription
argument_list|(
name|type
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
comment|// unknown type
return|return
literal|null
return|;
block|}
block|}
block|}
specifier|public
specifier|static
name|AbstractSimpleProperty
name|instanciateSimpleProperty
parameter_list|(
name|XMPMetadata
name|xmp
parameter_list|,
name|String
name|nsuri
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|,
name|String
name|type
parameter_list|)
block|{
comment|// constructor parameters
name|Object
index|[]
name|params
init|=
operator|new
name|Object
index|[]
block|{
name|xmp
block|,
name|nsuri
block|,
name|prefix
block|,
name|name
block|,
name|value
block|}
decl_stmt|;
comment|// type
try|try
block|{
name|TypeDescription
name|description
init|=
name|getTypeDescription
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|AbstractSimpleProperty
argument_list|>
name|clz
init|=
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|AbstractSimpleProperty
argument_list|>
operator|)
name|description
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
name|Constructor
argument_list|<
name|?
extends|extends
name|AbstractSimpleProperty
argument_list|>
name|cons
init|=
name|clz
operator|.
name|getConstructor
argument_list|(
name|simplePropertyConstParams
argument_list|)
decl_stmt|;
return|return
name|cons
operator|.
name|newInstance
argument_list|(
name|params
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodError
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to instanciate property"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to instanciate property"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to instanciate property"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to instanciate property"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to instanciate property"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to instanciate property"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to instanciate property"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|AbstractSimpleProperty
name|instanciateSimpleField
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|,
name|XMPMetadata
name|xmp
parameter_list|,
name|String
name|nsuri
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fields
init|=
name|getStructuredTypeFields
argument_list|(
name|clz
argument_list|)
decl_stmt|;
name|String
name|fieldName
init|=
name|fields
operator|.
name|get
argument_list|(
name|propertyName
argument_list|)
decl_stmt|;
try|try
block|{
name|Field
name|f
init|=
name|clz
operator|.
name|getField
argument_list|(
name|fieldName
argument_list|)
decl_stmt|;
name|PropertyType
name|pt
init|=
name|f
operator|.
name|getAnnotation
argument_list|(
name|PropertyType
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|simpleType
init|=
name|pt
operator|.
name|propertyType
argument_list|()
decl_stmt|;
return|return
name|TypeMapping
operator|.
name|instanciateSimpleProperty
argument_list|(
name|xmp
argument_list|,
name|nsuri
argument_list|,
name|prefix
argument_list|,
name|propertyName
argument_list|,
name|value
argument_list|,
name|simpleType
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|SecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to instanciate"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchFieldException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to instanciate"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getStructuredTypeFields
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clz
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
name|structuredTypes
operator|.
name|get
argument_list|(
name|clz
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|Field
index|[]
name|fields
init|=
name|clz
operator|.
name|getFields
argument_list|()
decl_stmt|;
for|for
control|(
name|Field
name|field
range|:
name|fields
control|)
block|{
name|PropertyType
name|pt
init|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|PropertyType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|pt
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|field
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|field
operator|.
name|get
argument_list|(
literal|null
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|value
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot parse this class"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
name|structuredTypes
operator|.
name|put
argument_list|(
name|clz
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
specifier|static
name|TypeDescription
name|getStructuredTypeName
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|STRUCTURED_NAMESPACES
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
return|;
block|}
comment|/**      * Check if a namespace used reference a complex basic types (like      * Thumbnails)      *       * @param namespace      *            The namespace URI to check      * @return True if namespace URI is a reference for a complex basic type      */
specifier|public
specifier|static
name|boolean
name|isStructuredTypeNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
comment|//        return STRUCTURED_TYPES.containsKey(namespace);
comment|// TODO why was STRUCTURED_TYPE
return|return
name|STRUCTURED_NAMESPACES
operator|.
name|containsKey
argument_list|(
name|namespace
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isArrayOfSimpleType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|int
name|pos
init|=
name|type
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|<
literal|0
condition|)
block|{
comment|// not array
return|return
literal|false
return|;
block|}
else|else
block|{
name|String
name|second
init|=
name|type
operator|.
name|substring
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
decl_stmt|;
return|return
name|isSimpleType
argument_list|(
name|second
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
name|String
name|getArrayType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|int
name|pos
init|=
name|type
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|<
literal|0
condition|)
block|{
comment|// not array
return|return
literal|null
return|;
block|}
else|else
block|{
name|String
name|first
init|=
name|type
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
decl_stmt|;
if|if
condition|(
name|first
operator|.
name|equalsIgnoreCase
argument_list|(
name|ArrayProperty
operator|.
name|UNORDERED_ARRAY
argument_list|)
condition|)
block|{
return|return
name|ArrayProperty
operator|.
name|UNORDERED_ARRAY
return|;
block|}
elseif|else
if|if
condition|(
name|first
operator|.
name|equalsIgnoreCase
argument_list|(
name|ArrayProperty
operator|.
name|ORDERED_ARRAY
argument_list|)
condition|)
block|{
return|return
name|ArrayProperty
operator|.
name|ORDERED_ARRAY
return|;
block|}
elseif|else
if|if
condition|(
name|first
operator|.
name|equalsIgnoreCase
argument_list|(
name|ArrayProperty
operator|.
name|ALTERNATIVE_ARRAY
argument_list|)
condition|)
block|{
return|return
name|ArrayProperty
operator|.
name|ALTERNATIVE_ARRAY
return|;
block|}
else|else
block|{
comment|// else not an array
return|return
literal|null
return|;
block|}
block|}
block|}
specifier|public
specifier|static
name|boolean
name|isSimpleType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
operator|(
name|BASIC_TYPES
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
operator|||
name|DERIVED_TYPES
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
operator|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isStructuredType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
name|STRUCTURED_TYPES
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|PropMapping
name|initializePropMapping
parameter_list|(
name|String
name|ns
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|AbstractStructuredType
argument_list|>
name|classSchem
parameter_list|)
block|{
name|PropertyType
name|propType
decl_stmt|;
name|PropertyAttributesAnnotation
name|propAtt
decl_stmt|;
name|Field
index|[]
name|fields
decl_stmt|;
name|PropMapping
name|propMap
init|=
operator|new
name|PropMapping
argument_list|(
name|ns
argument_list|)
decl_stmt|;
name|fields
operator|=
name|classSchem
operator|.
name|getFields
argument_list|()
expr_stmt|;
name|String
name|propName
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Field
name|field
range|:
name|fields
control|)
block|{
if|if
condition|(
name|field
operator|.
name|isAnnotationPresent
argument_list|(
name|PropertyType
operator|.
name|class
argument_list|)
condition|)
block|{
try|try
block|{
name|propName
operator|=
operator|(
name|String
operator|)
name|field
operator|.
name|get
argument_list|(
name|propName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"couldn't read one type declaration, please check accessibility and declaration of fields annoted in "
operator|+
name|classSchem
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|propType
operator|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|PropertyType
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|field
operator|.
name|isAnnotationPresent
argument_list|(
name|PropertyAttributesAnnotation
operator|.
name|class
argument_list|)
condition|)
block|{
name|propMap
operator|.
name|addNewProperty
argument_list|(
name|propName
argument_list|,
name|propType
operator|.
name|propertyType
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// XXX Case where a special annotation is used to specify
comment|// attributes
comment|// NOT IMPLEMENTED YET, JUST TO GIVE A CLUE TO MAKE THIS
name|propAtt
operator|=
name|field
operator|.
name|getAnnotation
argument_list|(
name|PropertyAttributesAnnotation
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|attributes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|att
range|:
name|propAtt
operator|.
name|expectedAttributes
argument_list|()
control|)
block|{
name|attributes
operator|.
name|add
argument_list|(
name|att
argument_list|)
expr_stmt|;
block|}
name|propMap
operator|.
name|addNewProperty
argument_list|(
name|propName
argument_list|,
name|propType
operator|.
name|propertyType
argument_list|()
argument_list|,
name|attributes
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|propMap
return|;
block|}
block|}
end_class

end_unit

