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
name|pdmodel
operator|.
name|graphics
operator|.
name|color
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ColorSpace
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
name|OutputStream
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
name|cos
operator|.
name|COSArray
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
name|cos
operator|.
name|COSBase
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
name|cos
operator|.
name|COSFloat
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
name|cos
operator|.
name|COSName
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
name|PDDocument
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
name|common
operator|.
name|PDStream
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
name|exceptions
operator|.
name|LoggingObject
import|;
end_import

begin_comment
comment|/**  * This class represents a color space in a pdf document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.11 $  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDColorSpaceFactory
extends|extends
name|LoggingObject
block|{
comment|/**      * Private constructor for utility classes.      */
specifier|private
name|PDColorSpaceFactory
parameter_list|()
block|{
comment|//utility class should not be implemented
block|}
comment|/**      * This will create the correct color space given the name.      *      * @param colorSpace The color space object.      *      * @return The color space.      *      * @throws IOException If the color space name is unknown.      */
specifier|public
specifier|static
name|PDColorSpace
name|createColorSpace
parameter_list|(
name|COSBase
name|colorSpace
parameter_list|)
throws|throws
name|IOException
block|{
name|PDColorSpace
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|colorSpace
operator|instanceof
name|COSName
condition|)
block|{
name|retval
operator|=
name|createColorSpace
argument_list|(
operator|(
operator|(
name|COSName
operator|)
name|colorSpace
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|colorSpace
decl_stmt|;
name|COSName
name|type
init|=
operator|(
name|COSName
operator|)
name|array
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|PDCalGray
operator|.
name|NAME
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDCalGray
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|PDCalRGB
operator|.
name|NAME
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDCalRGB
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|PDDeviceN
operator|.
name|NAME
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDDeviceN
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|PDIndexed
operator|.
name|NAME
argument_list|)
operator|||
name|type
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|PDIndexed
operator|.
name|ABBREVIATED_NAME
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDIndexed
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|PDLab
operator|.
name|NAME
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDLab
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|PDSeparation
operator|.
name|NAME
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDSeparation
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|PDICCBased
operator|.
name|NAME
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDICCBased
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|PDPattern
operator|.
name|NAME
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDPattern
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown colorspace array type:"
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown colorspace type:"
operator|+
name|colorSpace
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will create the correct color space given the name.      *      * @param colorSpaceName The name of the colorspace.      *      * @return The color space.      *      * @throws IOException If the color space name is unknown.      */
specifier|public
specifier|static
name|PDColorSpace
name|createColorSpace
parameter_list|(
name|String
name|colorSpaceName
parameter_list|)
throws|throws
name|IOException
block|{
name|PDColorSpace
name|cs
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|colorSpaceName
operator|.
name|equals
argument_list|(
name|PDDeviceCMYK
operator|.
name|NAME
argument_list|)
operator|||
name|colorSpaceName
operator|.
name|equals
argument_list|(
name|PDDeviceCMYK
operator|.
name|ABBREVIATED_NAME
argument_list|)
condition|)
block|{
name|cs
operator|=
name|PDDeviceCMYK
operator|.
name|INSTANCE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpaceName
operator|.
name|equals
argument_list|(
name|PDDeviceRGB
operator|.
name|NAME
argument_list|)
operator|||
name|colorSpaceName
operator|.
name|equals
argument_list|(
name|PDDeviceRGB
operator|.
name|ABBREVIATED_NAME
argument_list|)
condition|)
block|{
name|cs
operator|=
name|PDDeviceRGB
operator|.
name|INSTANCE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpaceName
operator|.
name|equals
argument_list|(
name|PDDeviceGray
operator|.
name|NAME
argument_list|)
operator|||
name|colorSpaceName
operator|.
name|equals
argument_list|(
name|PDDeviceGray
operator|.
name|ABBREVIATED_NAME
argument_list|)
condition|)
block|{
name|cs
operator|=
operator|new
name|PDDeviceGray
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpaceName
operator|.
name|equals
argument_list|(
name|PDLab
operator|.
name|NAME
argument_list|)
condition|)
block|{
name|cs
operator|=
operator|new
name|PDLab
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpaceName
operator|.
name|equals
argument_list|(
name|PDPattern
operator|.
name|NAME
argument_list|)
condition|)
block|{
name|cs
operator|=
operator|new
name|PDPattern
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Unknown colorspace '"
operator|+
name|colorSpaceName
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
name|cs
return|;
block|}
comment|/**      * This will create the correct color space from a java colorspace.      *      * @param doc The doc to potentiall write information to.      * @param cs The awt colorspace.      *      * @return The color space.      *      * @throws IOException If the color space name is unknown.      */
specifier|public
specifier|static
name|PDColorSpace
name|createColorSpace
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|ColorSpace
name|cs
parameter_list|)
throws|throws
name|IOException
block|{
name|PDColorSpace
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cs
operator|.
name|isCS_sRGB
argument_list|()
condition|)
block|{
name|retval
operator|=
name|PDDeviceRGB
operator|.
name|INSTANCE
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cs
operator|instanceof
name|ICC_ColorSpace
condition|)
block|{
name|ICC_ColorSpace
name|ics
init|=
operator|(
name|ICC_ColorSpace
operator|)
name|cs
decl_stmt|;
name|PDICCBased
name|pdCS
init|=
operator|new
name|PDICCBased
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|retval
operator|=
name|pdCS
expr_stmt|;
name|COSArray
name|ranges
init|=
operator|new
name|COSArray
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
name|cs
operator|.
name|getNumComponents
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ranges
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|ics
operator|.
name|getMinValue
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|ranges
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|ics
operator|.
name|getMaxValue
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|PDStream
name|iccData
init|=
name|pdCS
operator|.
name|getPDStream
argument_list|()
decl_stmt|;
name|OutputStream
name|output
init|=
literal|null
decl_stmt|;
try|try
block|{
name|output
operator|=
name|iccData
operator|.
name|createOutputStream
argument_list|()
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|ics
operator|.
name|getProfile
argument_list|()
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
name|output
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|pdCS
operator|.
name|setNumberOfComponents
argument_list|(
name|cs
operator|.
name|getNumComponents
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not yet implemented:"
operator|+
name|cs
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
block|}
end_class

end_unit

