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
name|image
operator|.
name|ColorModel
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
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
name|cos
operator|.
name|COSNull
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
name|COSArrayList
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
name|function
operator|.
name|PDFunction
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
name|function
operator|.
name|PDFunctionType4
import|;
end_import

begin_comment
comment|/**  * This class represents a DeviceN color space.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|PDDeviceN
extends|extends
name|PDColorSpace
block|{
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDDeviceN
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The name of this color space.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"DeviceN"
decl_stmt|;
specifier|private
name|COSArray
name|array
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDDeviceN
parameter_list|()
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|DEVICEN
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param separation The array containing all separation information.      */
specifier|public
name|PDDeviceN
parameter_list|(
name|COSArray
name|separation
parameter_list|)
block|{
name|array
operator|=
name|separation
expr_stmt|;
block|}
comment|/**      * This will return the name of the color space.  For a PDSeparation object      * this will always return "Separation"      *      * @return The name of the color space.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|NAME
return|;
block|}
comment|/**      * This will get the number of components that this color space is made up of.      *      * @return The number of components in this color space.      *      * @throws IOException If there is an error getting the number of color components.      */
specifier|public
name|int
name|getNumberOfComponents
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getColorantNames
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Create a Java colorspace for this colorspace.      *      * @return A color space that can be used for Java AWT operations.      *      * @throws IOException If there is an error creating the color space.      */
specifier|protected
name|ColorSpace
name|createColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|PDColorSpace
name|alt
init|=
name|getAlternateColorSpace
argument_list|()
decl_stmt|;
return|return
name|alt
operator|.
name|getJavaColorSpace
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioexception
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|ioexception
argument_list|,
name|ioexception
argument_list|)
expr_stmt|;
throw|throw
name|ioexception
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|exception
argument_list|,
name|exception
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to Create ColorSpace"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create a Java color model for this colorspace.      *      * @param bpc The number of bits per component.      *      * @return A color model that can be used for Java AWT operations.      *      * @throws IOException If there is an error creating the color model.      */
specifier|public
name|ColorModel
name|createColorModel
parameter_list|(
name|int
name|bpc
parameter_list|)
throws|throws
name|IOException
block|{
name|log
operator|.
name|info
argument_list|(
literal|"About to create ColorModel for "
operator|+
name|getAlternateColorSpace
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|getAlternateColorSpace
argument_list|()
operator|.
name|createColorModel
argument_list|(
name|bpc
argument_list|)
return|;
block|}
comment|/**      * This will get the colorant names.  A list of string objects.      *      * @return A list of colorants      */
specifier|public
name|List
argument_list|<
name|COSBase
argument_list|>
name|getColorantNames
parameter_list|()
block|{
name|COSArray
name|names
init|=
operator|(
name|COSArray
operator|)
name|array
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
return|return
name|COSArrayList
operator|.
name|convertCOSNameCOSArrayToList
argument_list|(
name|names
argument_list|)
return|;
block|}
comment|/**      * This will set the list of colorants.      *      * @param names The list of colorant names.      */
specifier|public
name|void
name|setColorantNames
parameter_list|(
name|List
argument_list|<
name|COSBase
argument_list|>
name|names
parameter_list|)
block|{
name|COSArray
name|namesArray
init|=
name|COSArrayList
operator|.
name|convertStringListToCOSNameCOSArray
argument_list|(
name|names
argument_list|)
decl_stmt|;
name|array
operator|.
name|set
argument_list|(
literal|1
argument_list|,
name|namesArray
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the alternate color space for this separation.      *      * @return The alternate color space.      *      * @throws IOException If there is an error getting the alternate color space.      */
specifier|public
name|PDColorSpace
name|getAlternateColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|alternate
init|=
name|array
operator|.
name|getObject
argument_list|(
literal|2
argument_list|)
decl_stmt|;
return|return
name|PDColorSpaceFactory
operator|.
name|createColorSpace
argument_list|(
name|alternate
argument_list|)
return|;
block|}
comment|/**      * This will set the alternate color space.      *      * @param cs The alternate color space.      */
specifier|public
name|void
name|setAlternateColorSpace
parameter_list|(
name|PDColorSpace
name|cs
parameter_list|)
block|{
name|COSBase
name|space
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
name|space
operator|=
name|cs
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
name|array
operator|.
name|set
argument_list|(
literal|2
argument_list|,
name|space
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the tint transform function.      *      * @return The tint transform function.      *      * @throws IOException if there is an error creating the function.      */
specifier|public
name|PDFunction
name|getTintTransform
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDFunction
operator|.
name|create
argument_list|(
name|array
operator|.
name|getObject
argument_list|(
literal|3
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the tint transform function.      *      * @param tint The tint transform function.      */
specifier|public
name|void
name|setTintTransform
parameter_list|(
name|PDFunction
name|tint
parameter_list|)
block|{
name|array
operator|.
name|set
argument_list|(
literal|3
argument_list|,
name|tint
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the attributes that are associated with the deviceN      * color space.      *      * @return The DeviceN attributes.      */
specifier|public
name|PDDeviceNAttributes
name|getAttributes
parameter_list|()
block|{
name|PDDeviceNAttributes
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|<
literal|5
condition|)
block|{
name|retval
operator|=
operator|new
name|PDDeviceNAttributes
argument_list|()
expr_stmt|;
name|setAttributes
argument_list|(
name|retval
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the color space attributes.  If null is passed in then      * all attribute will be removed.      *      * @param attributes The color space attributes.      */
specifier|public
name|void
name|setAttributes
parameter_list|(
name|PDDeviceNAttributes
name|attributes
parameter_list|)
block|{
if|if
condition|(
name|attributes
operator|==
literal|null
condition|)
block|{
name|array
operator|.
name|remove
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//make sure array is large enough
while|while
condition|(
name|array
operator|.
name|size
argument_list|()
operator|<
literal|5
condition|)
block|{
name|array
operator|.
name|add
argument_list|(
name|COSNull
operator|.
name|NULL
argument_list|)
expr_stmt|;
block|}
name|array
operator|.
name|set
argument_list|(
literal|4
argument_list|,
name|attributes
operator|.
name|getCOSDictionary
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the components of the color in the alternate colorspace for the given tint value.      * @return COSArray with the color components      * @throws IOException If the tint function is not supported      */
specifier|public
name|COSArray
name|calculateColorValues
parameter_list|(
name|List
argument_list|<
name|COSBase
argument_list|>
name|tintValues
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFunction
name|tintTransform
init|=
name|getTintTransform
argument_list|()
decl_stmt|;
if|if
condition|(
name|tintTransform
operator|instanceof
name|PDFunctionType4
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unsupported tint transformation type: "
operator|+
name|tintTransform
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" in "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|".getColorValues()"
operator|+
literal|" using color black instead."
argument_list|)
expr_stmt|;
name|int
name|numberOfComponents
init|=
name|getAlternateColorSpace
argument_list|()
operator|.
name|getNumberOfComponents
argument_list|()
decl_stmt|;
comment|// To get black as color:
comment|// 0.0f is used for the single value(s) if the colorspace is gray or RGB based
comment|// 1.0f is used for the single value if the colorspace is CMYK based
name|float
name|colorValue
init|=
name|numberOfComponents
operator|==
literal|4
condition|?
literal|1.0f
else|:
literal|0.0f
decl_stmt|;
name|COSArray
name|retval
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
name|numberOfComponents
condition|;
name|i
operator|++
control|)
block|{
name|retval
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|colorValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
else|else
block|{
name|COSArray
name|tint
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|tint
operator|.
name|addAll
argument_list|(
name|tintValues
argument_list|)
expr_stmt|;
return|return
name|tintTransform
operator|.
name|eval
argument_list|(
name|tint
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

