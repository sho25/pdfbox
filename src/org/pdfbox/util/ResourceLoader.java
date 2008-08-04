begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2006, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  */
end_comment

begin_package
package|package
name|org
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
comment|/**  * This class will handle loading resource files(AFM/CMAP).  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.9 $  */
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
comment|/**      * This will attempt to load the resource given the resource name.      *      * @param resourceName The resource to try and load.      * @param failIfNotFound Throw an error message if the properties were not found.      *      * @return The resource as a stream or null if it could not be found.      *       * @throws IOException If there is an error loading the properties.      */
specifier|public
specifier|static
name|Properties
name|loadProperties
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|boolean
name|failIfNotFound
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
else|else
block|{
if|if
condition|(
name|failIfNotFound
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: could not find resource '"
operator|+
name|resourceName
operator|+
literal|"' on classpath."
argument_list|)
throw|;
block|}
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

