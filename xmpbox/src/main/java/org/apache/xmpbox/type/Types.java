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

begin_enum
specifier|public
enum|enum
name|Types
block|{
name|Structured
argument_list|(
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|,
name|DefinedType
argument_list|(
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
block|,
comment|// basic
name|Text
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
name|TextType
operator|.
name|class
argument_list|)
block|,
name|Date
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
name|DateType
operator|.
name|class
argument_list|)
block|,
name|Boolean
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
name|BooleanType
operator|.
name|class
argument_list|)
block|,
name|Integer
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
name|IntegerType
operator|.
name|class
argument_list|)
block|,
name|Real
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
name|RealType
operator|.
name|class
argument_list|)
block|,
name|GPSCoordinate
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|TextType
operator|.
name|class
argument_list|)
block|,
name|ProperName
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|ProperNameType
operator|.
name|class
argument_list|)
block|,
name|Locale
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|LocaleType
operator|.
name|class
argument_list|)
block|,
name|AgentName
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|AgentNameType
operator|.
name|class
argument_list|)
block|,
name|GUID
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|GUIDType
operator|.
name|class
argument_list|)
block|,
name|XPath
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|XPathType
operator|.
name|class
argument_list|)
block|,
name|Part
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|PartType
operator|.
name|class
argument_list|)
block|,
name|URL
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|URLType
operator|.
name|class
argument_list|)
block|,
name|URI
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|URIType
operator|.
name|class
argument_list|)
block|,
name|Choice
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|ChoiceType
operator|.
name|class
argument_list|)
block|,
name|MIMEType
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|MIMEType
operator|.
name|class
argument_list|)
block|,
name|LangAlt
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|TextType
operator|.
name|class
argument_list|)
block|,
name|RenditionClass
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|RenditionClassType
operator|.
name|class
argument_list|)
block|,
name|Rational
argument_list|(
literal|true
argument_list|,
name|Text
argument_list|,
name|RationalType
operator|.
name|class
argument_list|)
block|,
name|Layer
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|LayerType
operator|.
name|class
argument_list|)
block|,
name|Thumbnail
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|ThumbnailType
operator|.
name|class
argument_list|)
block|,
name|ResourceEvent
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|ResourceEventType
operator|.
name|class
argument_list|)
block|,
name|ResourceRef
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|ResourceRefType
operator|.
name|class
argument_list|)
block|,
name|Version
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|VersionType
operator|.
name|class
argument_list|)
block|,
name|PDFASchema
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|PDFASchemaType
operator|.
name|class
argument_list|)
block|,
name|PDFAField
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|PDFAFieldType
operator|.
name|class
argument_list|)
block|,
name|PDFAProperty
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|PDFAPropertyType
operator|.
name|class
argument_list|)
block|,
name|PDFAType
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|PDFATypeType
operator|.
name|class
argument_list|)
block|,
name|Job
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|JobType
operator|.
name|class
argument_list|)
block|,
name|OECF
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|OECFType
operator|.
name|class
argument_list|)
block|,
name|CFAPattern
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|CFAPatternType
operator|.
name|class
argument_list|)
block|,
name|DeviceSettings
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|DeviceSettingsType
operator|.
name|class
argument_list|)
block|,
name|Flash
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|FlashType
operator|.
name|class
argument_list|)
block|,
name|Dimensions
argument_list|(
literal|false
argument_list|,
name|Structured
argument_list|,
name|DimensionsType
operator|.
name|class
argument_list|)
block|;
comment|// For defined types
specifier|private
specifier|final
name|boolean
name|simple
decl_stmt|;
specifier|private
specifier|final
name|Types
name|basic
decl_stmt|;
specifier|private
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|AbstractField
argument_list|>
name|clz
decl_stmt|;
specifier|private
name|Types
parameter_list|(
name|boolean
name|s
parameter_list|,
name|Types
name|b
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|AbstractField
argument_list|>
name|c
parameter_list|)
block|{
name|this
operator|.
name|simple
operator|=
name|s
expr_stmt|;
name|this
operator|.
name|basic
operator|=
name|b
expr_stmt|;
name|this
operator|.
name|clz
operator|=
name|c
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSimple
parameter_list|()
block|{
return|return
name|simple
return|;
block|}
specifier|public
name|boolean
name|isBasic
parameter_list|()
block|{
return|return
name|basic
operator|==
literal|null
return|;
block|}
specifier|public
name|boolean
name|isStructured
parameter_list|()
block|{
return|return
name|basic
operator|==
name|Structured
return|;
block|}
specifier|public
name|boolean
name|isDefined
parameter_list|()
block|{
return|return
name|this
operator|==
name|DefinedType
return|;
block|}
specifier|public
name|Types
name|getBasic
parameter_list|()
block|{
return|return
name|basic
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
extends|extends
name|AbstractField
argument_list|>
name|getImplementingClass
parameter_list|()
block|{
return|return
name|clz
return|;
block|}
block|}
end_enum

end_unit

