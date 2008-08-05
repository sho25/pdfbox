begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
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
name|org
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
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
name|pdfbox
operator|.
name|exceptions
operator|.
name|LoggingObject
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
name|image
operator|.
name|ColorModel
import|;
end_import

begin_comment
comment|/**  * This class represents a color space in a pdf document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDColorSpace
extends|extends
name|LoggingObject
implements|implements
name|COSObjectable
block|{
comment|/**      * This will return the name of the color space.      *      * @return The name of the color space.      */
specifier|public
specifier|abstract
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * This will get the number of components that this color space is made up of.      *      * @return The number of components in this color space.      *      * @throws IOException If there is an error getting the number of color components.      */
specifier|public
specifier|abstract
name|int
name|getNumberOfComponents
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|COSName
operator|.
name|getPDFName
argument_list|(
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Create a Java colorspace for this colorspace.      *      * @return A color space that can be used for Java AWT operations.      *      * @throws IOException If there is an error creating the color space.      */
specifier|public
specifier|abstract
name|ColorSpace
name|createColorSpace
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Create a Java color model for this colorspace.      *      * @param bpc The number of bits per component.      *      * @return A color model that can be used for Java AWT operations.      *      * @throws IOException If there is an error creating the color model.      */
specifier|public
specifier|abstract
name|ColorModel
name|createColorModel
parameter_list|(
name|int
name|bpc
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_class

end_unit

