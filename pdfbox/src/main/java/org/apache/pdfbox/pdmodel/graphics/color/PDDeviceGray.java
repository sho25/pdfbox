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
name|awt
operator|.
name|image
operator|.
name|ComponentColorModel
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
name|DataBuffer
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
name|IndexColorModel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Transparency
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

begin_comment
comment|/**  * This class represents a Gray color space.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  *   */
end_comment

begin_class
specifier|public
class|class
name|PDDeviceGray
extends|extends
name|PDColorSpace
block|{
comment|/**      * The name of this color space.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"DeviceGray"
decl_stmt|;
comment|/**      * The abbreviated name of this color space.      */
specifier|public
specifier|static
specifier|final
name|String
name|ABBREVIATED_NAME
init|=
literal|"G"
decl_stmt|;
comment|/**      * This will return the name of the color space.      *      * @return The name of the color space.      */
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
literal|1
return|;
block|}
comment|/**      * Create a Java colorspace for this colorspace.      *      * @return A color space that can be used for Java AWT operations.      */
specifier|protected
name|ColorSpace
name|createColorSpace
parameter_list|()
block|{
return|return
name|ColorSpace
operator|.
name|getInstance
argument_list|(
name|ColorSpace
operator|.
name|CS_GRAY
argument_list|)
return|;
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
name|ColorModel
name|colorModel
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bpc
operator|==
literal|8
condition|)
block|{
name|ColorSpace
name|cs
init|=
name|ColorSpace
operator|.
name|getInstance
argument_list|(
name|ColorSpace
operator|.
name|CS_GRAY
argument_list|)
decl_stmt|;
name|int
index|[]
name|nBits
init|=
block|{
name|bpc
block|}
decl_stmt|;
name|colorModel
operator|=
operator|new
name|ComponentColorModel
argument_list|(
name|cs
argument_list|,
name|nBits
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
name|Transparency
operator|.
name|OPAQUE
argument_list|,
name|DataBuffer
operator|.
name|TYPE_BYTE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|numEntries
init|=
literal|1
operator|<<
name|bpc
decl_stmt|;
comment|// calculate all possible values
name|byte
index|[]
name|indexedValues
init|=
operator|new
name|byte
index|[
name|numEntries
index|]
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
name|numEntries
condition|;
name|i
operator|++
control|)
block|{
name|indexedValues
index|[
name|i
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|i
operator|*
literal|255
operator|/
operator|(
name|numEntries
operator|-
literal|1
operator|)
argument_list|)
expr_stmt|;
block|}
name|colorModel
operator|=
operator|new
name|IndexColorModel
argument_list|(
name|bpc
argument_list|,
name|numEntries
argument_list|,
name|indexedValues
argument_list|,
name|indexedValues
argument_list|,
name|indexedValues
argument_list|)
expr_stmt|;
block|}
return|return
name|colorModel
return|;
block|}
block|}
end_class

end_unit

