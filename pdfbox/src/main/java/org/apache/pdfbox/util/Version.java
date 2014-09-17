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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * Exposes PDFBox version.  */
end_comment

begin_class
specifier|public
class|class
name|Version
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PDFBOX_VERSION_PROPERTIES
init|=
literal|"org/apache/pdfbox/resources/pdfbox.properties"
decl_stmt|;
specifier|private
name|Version
parameter_list|()
block|{
comment|// static helper
block|}
comment|/**      * Returns the version of PDFBox.      */
specifier|public
specifier|static
name|String
name|getVersion
parameter_list|()
block|{
try|try
block|{
name|URL
name|url
init|=
name|Version
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|PDFBOX_VERSION_PROPERTIES
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|url
operator|.
name|openStream
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|properties
operator|.
name|getProperty
argument_list|(
literal|"pdfbox.version"
argument_list|,
literal|null
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|io
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

