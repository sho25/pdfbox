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

begin_comment
comment|/**  * This class represents a color space and the color value for that colorspace.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.7 $  */
end_comment

begin_class
specifier|public
class|class
name|PDColorState
extends|extends
name|LoggingObject
implements|implements
name|Cloneable
block|{
comment|/**      * The default color that can be set to replace all colors in      * {@link ICC_ColorSpace ICC color spaces}.      *      * @see #setIccOverrideColor(Color)      */
specifier|private
specifier|static
specifier|volatile
name|Color
name|iccOverrideColor
init|=
name|Color
operator|.
name|getColor
argument_list|(
literal|"org.apache.pdfbox.ICC_override_color"
argument_list|)
decl_stmt|;
comment|/**      * Sets the default color to replace all colors in      * {@link ICC_ColorSpace ICC color spaces}. This will work around      * a potential JVM crash caused by broken native ICC color manipulation      * code in the Sun class libraries.      *<p>      * The default override can be specified by setting the color code in      *<code>org.apache.pdfbox.ICC_override_color</code> system property      * (see {@link Color#getColor(String)}. If this system property is not      * specified, then the override is not enabled unless this method is      * explicitly called.      *      * @param color ICC override color,      *              or<code>null</code> to disable the override      * @see<a href="https://issues.apache.org/jira/browse/PDFBOX-511">PDFBOX-511</a>      * @since Apache PDFBox 0.8.1      */
specifier|public
specifier|static
name|void
name|setIccOverrideColor
parameter_list|(
name|Color
name|color
parameter_list|)
block|{
name|iccOverrideColor
operator|=
name|color
expr_stmt|;
block|}
specifier|private
name|PDColorSpace
name|colorSpace
init|=
operator|new
name|PDDeviceGray
argument_list|()
decl_stmt|;
specifier|private
name|COSArray
name|colorSpaceValue
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
comment|/**      * Cached Java AWT color based on the current color space and value.      * The value is cleared whenever the color space or value is set.      *      * @see #getJavaColor()      */
specifier|private
name|Color
name|color
init|=
literal|null
decl_stmt|;
comment|/**      * Default constructor.      *      */
specifier|public
name|PDColorState
parameter_list|()
block|{
name|setColorSpaceValue
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|Object
name|clone
parameter_list|()
block|{
name|PDColorState
name|retval
init|=
operator|new
name|PDColorState
argument_list|()
decl_stmt|;
name|retval
operator|.
name|colorSpace
operator|=
name|this
operator|.
name|colorSpace
expr_stmt|;
name|retval
operator|.
name|colorSpaceValue
operator|.
name|clear
argument_list|()
expr_stmt|;
name|retval
operator|.
name|colorSpaceValue
operator|.
name|addAll
argument_list|(
name|this
operator|.
name|colorSpaceValue
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Returns the Java AWT color based on the current color space and value.      *      * @return current Java AWT color      * @throws IOException if the current color can not be created      */
specifier|public
name|Color
name|getJavaColor
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|color
operator|==
literal|null
condition|)
block|{
name|color
operator|=
name|createColor
argument_list|()
expr_stmt|;
block|}
return|return
name|color
return|;
block|}
comment|/**      * Create the current color from the colorspace and values.      * @return The current awt color.      * @throws IOException If there is an error creating the color.      */
specifier|private
name|Color
name|createColor
parameter_list|()
throws|throws
name|IOException
block|{
name|float
index|[]
name|components
init|=
name|colorSpaceValue
operator|.
name|toFloatArray
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|colorSpace
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|PDDeviceRGB
operator|.
name|NAME
argument_list|)
operator|&&
name|components
operator|.
name|length
operator|==
literal|3
condition|)
block|{
comment|//for some reason, when using RGB and the RGB colorspace
comment|//the new Color doesn't maintain exactly the same values
comment|//I think some color conversion needs to take place first
comment|//for now we will just make rgb a special case.
return|return
operator|new
name|Color
argument_list|(
name|components
index|[
literal|0
index|]
argument_list|,
name|components
index|[
literal|1
index|]
argument_list|,
name|components
index|[
literal|2
index|]
argument_list|)
return|;
block|}
else|else
block|{
name|Color
name|override
init|=
name|iccOverrideColor
decl_stmt|;
name|ColorSpace
name|cs
init|=
name|colorSpace
operator|.
name|createColorSpace
argument_list|()
decl_stmt|;
if|if
condition|(
name|colorSpace
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
operator|&&
name|components
operator|.
name|length
operator|==
literal|1
condition|)
block|{
comment|//Use that component as a single-integer RGB value
return|return
operator|new
name|Color
argument_list|(
operator|(
name|int
operator|)
name|components
index|[
literal|0
index|]
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|cs
operator|instanceof
name|ICC_ColorSpace
operator|&&
name|override
operator|!=
literal|null
condition|)
block|{
name|logger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Using an ICC override color to avoid a potential"
operator|+
literal|" JVM crash (see PDFBOX-511)"
argument_list|)
expr_stmt|;
return|return
name|override
return|;
block|}
else|else
block|{
return|return
operator|new
name|Color
argument_list|(
name|cs
argument_list|,
name|components
argument_list|,
literal|1f
argument_list|)
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|IllegalArgumentException
name|exception
parameter_list|)
block|{
name|String
name|values
init|=
literal|"Color Values: "
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
name|components
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|values
operator|=
name|values
operator|+
name|components
index|[
name|i
index|]
operator|+
literal|"\t"
expr_stmt|;
block|}
name|logger
argument_list|()
operator|.
name|error
argument_list|(
name|exception
operator|+
literal|"\n"
operator|+
name|values
argument_list|,
name|exception
argument_list|)
expr_stmt|;
throw|throw
name|exception
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioexception
parameter_list|)
block|{
name|logger
argument_list|()
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
name|e
parameter_list|)
block|{
name|logger
argument_list|()
operator|.
name|error
argument_list|(
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to Create Color"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Constructor with an existing color set.  Default colorspace is PDDeviceGray.      *      * @param csValues The color space values.      */
specifier|public
name|PDColorState
parameter_list|(
name|COSArray
name|csValues
parameter_list|)
block|{
name|colorSpaceValue
operator|=
name|csValues
expr_stmt|;
block|}
comment|/**      * This will get the current colorspace.      *      * @return The current colorspace.      */
specifier|public
name|PDColorSpace
name|getColorSpace
parameter_list|()
block|{
return|return
name|colorSpace
return|;
block|}
comment|/**      * This will set the current colorspace.      *      * @param value The new colorspace.      */
specifier|public
name|void
name|setColorSpace
parameter_list|(
name|PDColorSpace
name|value
parameter_list|)
block|{
name|colorSpace
operator|=
name|value
expr_stmt|;
comment|// Clear color cache
name|color
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * This will get the color space values.  Either 1 for gray or 3 for RGB.      *      * @return The colorspace values.      */
specifier|public
name|float
index|[]
name|getColorSpaceValue
parameter_list|()
block|{
return|return
name|colorSpaceValue
operator|.
name|toFloatArray
argument_list|()
return|;
block|}
comment|/**      * This will get the color space values.  Either 1 for gray or 3 for RGB.      *      * @return The colorspace values.      */
specifier|public
name|COSArray
name|getCOSColorSpaceValue
parameter_list|()
block|{
return|return
name|colorSpaceValue
return|;
block|}
comment|/**      * This will update the colorspace values.      *      * @param value The new colorspace values.      */
specifier|public
name|void
name|setColorSpaceValue
parameter_list|(
name|float
index|[]
name|value
parameter_list|)
block|{
name|colorSpaceValue
operator|.
name|setFloatArray
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// Clear color cache
name|color
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

