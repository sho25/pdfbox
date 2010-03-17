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
name|fontbox
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|IOException
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
comment|/**  * This class will handle loading resource files(AFM/CMAP).  This was originally  * written for PDFBox but FontBox uses it as well.  For now each project will  * have their own version.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|ResourceLoader
block|{
comment|/**      * private constructor for utility class.      */
specifier|private
name|ResourceLoader
parameter_list|()
block|{
comment|//private utility class
block|}
comment|/**      * This will attempt to load the resource given the resource name.      *      * @param resourceName The resource to try and load.      *      * @return The resource as a stream or null if it could not be found.      *       * @throws IOException If there is an error while attempting to load the resource.      */
specifier|public
specifier|static
name|InputStream
name|loadResource
parameter_list|(
name|String
name|resourceName
parameter_list|)
throws|throws
name|IOException
block|{
name|ClassLoader
name|loader
init|=
name|ResourceLoader
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
name|loader
operator|.
name|getResourceAsStream
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
block|}
comment|//see sourceforge bug 863053, this is a fix for a user that
comment|//needed to have PDFBox loaded by the bootstrap classloader
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|loader
operator|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
expr_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|is
operator|=
name|loader
operator|.
name|getResourceAsStream
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|is
return|;
block|}
comment|/**      * This will attempt to load the resource given the resource name.      *      * @param resourceName The resource to try and load.      *      * @return The resource as a stream or null if it could not be found.      *       * @throws IOException If there is an error loading the properties.      */
specifier|public
specifier|static
name|Properties
name|loadProperties
parameter_list|(
name|String
name|resourceName
parameter_list|)
throws|throws
name|IOException
block|{
name|Properties
name|properties
init|=
literal|null
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|loadResource
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|properties
return|;
block|}
comment|/**      * This will attempt to load the resource given the resource name.      *      * @param resourceName The resource to try and load.      * @param defaults A stream of default properties.      *      * @return The resource as a stream or null if it could not be found.      *       * @throws IOException If there is an error loading the properties.      */
specifier|public
specifier|static
name|Properties
name|loadProperties
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|Properties
name|defaults
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|loadResource
argument_list|(
name|resourceName
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|defaults
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|defaults
return|;
block|}
block|}
end_class

end_unit

