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
name|ByteArrayInputStream
import|;
end_import

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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
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
name|BuildPDFAExtensionSchemaDescriptionException
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
name|PropertyDescription
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|XStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|thoughtworks
operator|.
name|xstream
operator|.
name|io
operator|.
name|xml
operator|.
name|DomDriver
import|;
end_import

begin_comment
comment|/**  * Manage XML property description file Allow user to create XML property  * description description file or retrieve List of PropertyDescription from an  * XML File If file data are specified in class schema representation, building  * of Description schema (which must included in PDF/A Extension) will use these  * information.  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|XMLPropertiesDescriptionManager
block|{
specifier|protected
name|List
argument_list|<
name|PropertyDescription
argument_list|>
name|propDescs
decl_stmt|;
specifier|protected
name|XStream
name|xstream
decl_stmt|;
comment|/** 	 * Create new XMLPropertiesDescriptionManager 	 */
specifier|public
name|XMLPropertiesDescriptionManager
parameter_list|()
block|{
name|propDescs
operator|=
operator|new
name|ArrayList
argument_list|<
name|PropertyDescription
argument_list|>
argument_list|()
expr_stmt|;
name|xstream
operator|=
operator|new
name|XStream
argument_list|(
operator|new
name|DomDriver
argument_list|()
argument_list|)
expr_stmt|;
name|xstream
operator|.
name|alias
argument_list|(
literal|"PropertiesDescriptions"
argument_list|,
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
name|xstream
operator|.
name|alias
argument_list|(
literal|"PropertyDescription"
argument_list|,
name|PropertyDescription
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Add a description for the named property 	 *  	 * @param name 	 *            Name of property 	 * @param description 	 *            Description which will be used 	 */
specifier|public
name|void
name|addPropertyDescription
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|propDescs
operator|.
name|add
argument_list|(
operator|new
name|PropertyDescription
argument_list|(
name|name
argument_list|,
name|description
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Save as XML data, all information defined before to the OutputStream 	 *  	 * @param os 	 *            The stream where write data 	 */
specifier|public
name|void
name|toXML
parameter_list|(
name|OutputStream
name|os
parameter_list|)
block|{
name|xstream
operator|.
name|toXML
argument_list|(
name|propDescs
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Load Properties Description from a well-formed XML File 	 *  	 * @param classSchem 	 *            Description Schema where properties description are used 	 * @param path 	 *            Relative Path (file loading search in same class Schema 	 *            representation folder) 	 * @throws BuildPDFAExtensionSchemaDescriptionException 	 *             When problems to get or treat data in XML description file 	 */
specifier|public
name|void
name|loadListFromXML
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|XMPSchema
argument_list|>
name|classSchem
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|BuildPDFAExtensionSchemaDescriptionException
block|{
name|InputStream
name|fis
init|=
name|classSchem
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|fis
operator|==
literal|null
condition|)
block|{
comment|// resource not found
throw|throw
operator|new
name|BuildPDFAExtensionSchemaDescriptionException
argument_list|(
literal|"Failed to find specified XML properties descriptions resource"
argument_list|)
throw|;
block|}
name|loadListFromXML
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Load Properties Description from XML Stream 	 *  	 * @param is 	 *            Stream where read data 	 * @throws BuildPDFAExtensionSchemaDescriptionException 	 *             When problems to get or treat data in XML description file 	 */
specifier|public
name|void
name|loadListFromXML
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|BuildPDFAExtensionSchemaDescriptionException
block|{
try|try
block|{
name|Object
name|o
init|=
name|xstream
operator|.
name|fromXML
argument_list|(
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|List
argument_list|<
name|?
argument_list|>
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|o
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|o
operator|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|PropertyDescription
condition|)
block|{
name|propDescs
operator|=
operator|(
name|List
argument_list|<
name|PropertyDescription
argument_list|>
operator|)
name|o
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|BuildPDFAExtensionSchemaDescriptionException
argument_list|(
literal|"Failed to get correct properties descriptions from specified XML stream"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|BuildPDFAExtensionSchemaDescriptionException
argument_list|(
literal|"Failed to find a properties description into the specified XML stream"
argument_list|)
throw|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BuildPDFAExtensionSchemaDescriptionException
argument_list|(
literal|"Failed to get correct properties descriptions from specified XML stream"
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|/** 	 * Get all Properties Descriptions defined 	 *  	 * @return List of PropertyDescription 	 */
end_comment

begin_function
specifier|public
name|List
argument_list|<
name|PropertyDescription
argument_list|>
name|getPropertiesDescriptionList
parameter_list|()
block|{
return|return
name|propDescs
return|;
block|}
end_function

unit|}
end_unit

