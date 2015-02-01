begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|xmpbox
package|;
end_package

begin_comment
comment|/**  * Several constants used in XMP.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|XmpConstants
block|{
comment|/**      * The RDF namespace URI reference.      */
specifier|public
specifier|static
specifier|final
name|String
name|RDF_NAMESPACE
init|=
literal|"http://www.w3.org/1999/02/22-rdf-syntax-ns#"
decl_stmt|;
comment|/**      * The default xpacket header begin attribute.      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_XPACKET_BEGIN
init|=
literal|"\uFEFF"
decl_stmt|;
comment|/**      * The default xpacket header id attribute.      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_XPACKET_ID
init|=
literal|"W5M0MpCehiHzreSzNTczkc9d"
decl_stmt|;
comment|/**      * The default xpacket header encoding attribute.      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_XPACKET_ENCODING
init|=
literal|"UTF-8"
decl_stmt|;
comment|/**      * The default xpacket data (XMP Data).      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_XPACKET_BYTES
init|=
literal|null
decl_stmt|;
comment|/**      * The default xpacket trailer end attribute.      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_XPACKET_END
init|=
literal|"w"
decl_stmt|;
comment|/**      * The default namespace prefix for RDF.      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_RDF_PREFIX
init|=
literal|"rdf"
decl_stmt|;
comment|/**      * The default local name for RDF.      */
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_RDF_LOCAL_NAME
init|=
literal|"RDF"
decl_stmt|;
comment|/**      * The list element name.      */
specifier|public
specifier|static
specifier|final
name|String
name|LIST_NAME
init|=
literal|"li"
decl_stmt|;
comment|/**      * The language attribute name.      */
specifier|public
specifier|static
specifier|final
name|String
name|LANG_NAME
init|=
literal|"lang"
decl_stmt|;
comment|/**      * The about attribute name.      */
specifier|public
specifier|static
specifier|final
name|String
name|ABOUT_NAME
init|=
literal|"about"
decl_stmt|;
comment|/**      * The Description element name.      */
specifier|public
specifier|static
specifier|final
name|String
name|DESCRIPTION_NAME
init|=
literal|"Description"
decl_stmt|;
comment|/**      * The resource attribute name.      */
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_NAME
init|=
literal|"Resource"
decl_stmt|;
comment|/**      * The parse type attribute name.      */
specifier|public
specifier|static
specifier|final
name|String
name|PARSE_TYPE
init|=
literal|"parseType"
decl_stmt|;
comment|/**      * The default language code.      */
specifier|public
specifier|static
specifier|final
name|String
name|X_DEFAULT
init|=
literal|"x-default"
decl_stmt|;
specifier|private
name|XmpConstants
parameter_list|()
block|{
comment|// hide constructor
block|}
block|}
end_class

end_unit

