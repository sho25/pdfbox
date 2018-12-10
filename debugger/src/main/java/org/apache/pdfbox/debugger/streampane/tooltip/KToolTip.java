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
name|debugger
operator|.
name|streampane
operator|.
name|tooltip
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ICC_ColorSpace
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ICC_Profile
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
name|io
operator|.
name|InputStream
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|graphics
operator|.
name|color
operator|.
name|PDDeviceCMYK
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  *  A class that provide tooltip for K and k.  */
end_comment

begin_class
specifier|final
class|class
name|KToolTip
extends|extends
name|ColorToolTip
block|{
comment|/**      * Constructor.      * @param rowText String instance.      */
name|KToolTip
parameter_list|(
name|String
name|rowText
parameter_list|)
block|{
name|createMarkUp
argument_list|(
name|rowText
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createMarkUp
parameter_list|(
name|String
name|rowText
parameter_list|)
block|{
name|float
index|[]
name|colorValues
init|=
name|extractColorValues
argument_list|(
name|rowText
argument_list|)
decl_stmt|;
if|if
condition|(
name|colorValues
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|float
index|[]
name|rgbValues
init|=
name|getICCColorSpace
argument_list|()
operator|.
name|toRGB
argument_list|(
name|colorValues
argument_list|)
decl_stmt|;
name|setToolTipText
argument_list|(
name|getMarkUp
argument_list|(
name|colorHexValue
argument_list|(
operator|new
name|Color
argument_list|(
name|rgbValues
index|[
literal|0
index|]
argument_list|,
name|rgbValues
index|[
literal|1
index|]
argument_list|,
name|rgbValues
index|[
literal|2
index|]
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|ICC_ColorSpace
name|getICCColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
comment|// loads the ICC color profile for CMYK
name|ICC_Profile
name|iccProfile
init|=
name|getICCProfile
argument_list|()
decl_stmt|;
if|if
condition|(
name|iccProfile
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Default CMYK color profile could not be loaded"
argument_list|)
throw|;
block|}
return|return
operator|new
name|ICC_ColorSpace
argument_list|(
name|iccProfile
argument_list|)
return|;
block|}
name|ICC_Profile
name|getICCProfile
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Adobe Acrobat uses "U.S. Web Coated (SWOP) v2" as the default
comment|// CMYK profile, however it is not available under an open license.
comment|// Instead, the "ISO Coated v2 300% (basICColor)" is used, which
comment|// is an open alternative to the "ISO Coated v2 300% (ECI)" profile.
name|String
name|name
init|=
literal|"/org/apache/pdfbox/resources/icc/ISOcoated_v2_300_bas.icc"
decl_stmt|;
name|URL
name|url
init|=
name|PDDeviceCMYK
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error loading resource: "
operator|+
name|name
argument_list|)
throw|;
block|}
try|try
init|(
name|InputStream
name|input
init|=
name|url
operator|.
name|openStream
argument_list|()
init|)
block|{
return|return
name|ICC_Profile
operator|.
name|getInstance
argument_list|(
name|input
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

