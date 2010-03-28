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
name|COSDictionary
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
name|common
operator|.
name|PDRange
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
comment|/**  * This class represents a Lab color space.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|PDLab
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
literal|"Lab"
decl_stmt|;
specifier|private
name|COSArray
name|array
decl_stmt|;
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDLab
parameter_list|()
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|LAB
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor with array.      *      * @param lab The underlying color space.      */
specifier|public
name|PDLab
parameter_list|(
name|COSArray
name|lab
parameter_list|)
block|{
name|array
operator|=
name|lab
expr_stmt|;
name|dictionary
operator|=
operator|(
name|COSDictionary
operator|)
name|array
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|array
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
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not implemented"
argument_list|)
throw|;
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
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not implemented"
argument_list|)
throw|;
block|}
comment|/**      * This will get the number of components that this color space is made up of.      *      * @return The number of components in this color space.      *      * @throws IOException If there is an error getting the number of color components.      */
specifier|public
name|int
name|getNumberOfComponents
parameter_list|()
throws|throws
name|IOException
block|{
comment|//BJL
comment|//hmm is this correct, I am not 100% sure.
return|return
literal|3
return|;
block|}
comment|/**      * This will return the whitepoint tristimulus.  As this is a required field      * this will never return null.  A default of 1,1,1 will be returned if the      * pdf does not have any values yet.      *      * @return The whitepoint tristimulus.      */
specifier|public
name|PDTristimulus
name|getWhitepoint
parameter_list|()
block|{
name|COSArray
name|wp
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|WHITE_POINT
argument_list|)
decl_stmt|;
if|if
condition|(
name|wp
operator|==
literal|null
condition|)
block|{
name|wp
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|wp
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|1.0f
argument_list|)
argument_list|)
expr_stmt|;
name|wp
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|1.0f
argument_list|)
argument_list|)
expr_stmt|;
name|wp
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|1.0f
argument_list|)
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|WHITE_POINT
argument_list|,
name|wp
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PDTristimulus
argument_list|(
name|wp
argument_list|)
return|;
block|}
comment|/**      * This will set the whitepoint tristimulus.  As this is a required field      * this null should not be passed into this function.      *      * @param wp The whitepoint tristimulus.      */
specifier|public
name|void
name|setWhitepoint
parameter_list|(
name|PDTristimulus
name|wp
parameter_list|)
block|{
name|COSBase
name|wpArray
init|=
name|wp
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|wpArray
operator|!=
literal|null
condition|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|WHITE_POINT
argument_list|,
name|wpArray
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will return the BlackPoint tristimulus.  This is an optional field but      * has defaults so this will never return null.      * A default of 0,0,0 will be returned if the pdf does not have any values yet.      *      * @return The blackpoint tristimulus.      */
specifier|public
name|PDTristimulus
name|getBlackPoint
parameter_list|()
block|{
name|COSArray
name|bp
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BLACK_POINT
argument_list|)
decl_stmt|;
if|if
condition|(
name|bp
operator|==
literal|null
condition|)
block|{
name|bp
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|bp
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0.0f
argument_list|)
argument_list|)
expr_stmt|;
name|bp
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0.0f
argument_list|)
argument_list|)
expr_stmt|;
name|bp
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0.0f
argument_list|)
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BLACK_POINT
argument_list|,
name|bp
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PDTristimulus
argument_list|(
name|bp
argument_list|)
return|;
block|}
comment|/**      * This will set the BlackPoint tristimulus.  As this is a required field      * this null should not be passed into this function.      *      * @param bp The BlackPoint tristimulus.      */
specifier|public
name|void
name|setBlackPoint
parameter_list|(
name|PDTristimulus
name|bp
parameter_list|)
block|{
name|COSBase
name|bpArray
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bp
operator|!=
literal|null
condition|)
block|{
name|bpArray
operator|=
name|bp
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BLACK_POINT
argument_list|,
name|bpArray
argument_list|)
expr_stmt|;
block|}
specifier|private
name|COSArray
name|getRangeArray
parameter_list|()
block|{
name|COSArray
name|range
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|)
decl_stmt|;
if|if
condition|(
name|range
operator|==
literal|null
condition|)
block|{
name|range
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|,
name|array
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
operator|-
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
operator|-
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|range
return|;
block|}
comment|/**      * This will get the valid range for the a component.  If none is found      * then the default will be returned, which is -100 to 100.      *      * @return The a range.      */
specifier|public
name|PDRange
name|getARange
parameter_list|()
block|{
name|COSArray
name|range
init|=
name|getRangeArray
argument_list|()
decl_stmt|;
return|return
operator|new
name|PDRange
argument_list|(
name|range
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the a range for this color space.      *      * @param range The new range for the a component.      */
specifier|public
name|void
name|setARange
parameter_list|(
name|PDRange
name|range
parameter_list|)
block|{
name|COSArray
name|rangeArray
init|=
literal|null
decl_stmt|;
comment|//if null then reset to defaults
if|if
condition|(
name|range
operator|==
literal|null
condition|)
block|{
name|rangeArray
operator|=
name|getRangeArray
argument_list|()
expr_stmt|;
name|rangeArray
operator|.
name|set
argument_list|(
literal|0
argument_list|,
operator|new
name|COSFloat
argument_list|(
operator|-
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|rangeArray
operator|.
name|set
argument_list|(
literal|1
argument_list|,
operator|new
name|COSFloat
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rangeArray
operator|=
name|range
operator|.
name|getCOSArray
argument_list|()
expr_stmt|;
block|}
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|,
name|rangeArray
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the valid range for the b component.  If none is found      * then the default will be returned, which is -100 to 100.      *      * @return The b range.      */
specifier|public
name|PDRange
name|getBRange
parameter_list|()
block|{
name|COSArray
name|range
init|=
name|getRangeArray
argument_list|()
decl_stmt|;
return|return
operator|new
name|PDRange
argument_list|(
name|range
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * This will set the b range for this color space.      *      * @param range The new range for the b component.      */
specifier|public
name|void
name|setBRange
parameter_list|(
name|PDRange
name|range
parameter_list|)
block|{
name|COSArray
name|rangeArray
init|=
literal|null
decl_stmt|;
comment|//if null then reset to defaults
if|if
condition|(
name|range
operator|==
literal|null
condition|)
block|{
name|rangeArray
operator|=
name|getRangeArray
argument_list|()
expr_stmt|;
name|rangeArray
operator|.
name|set
argument_list|(
literal|2
argument_list|,
operator|new
name|COSFloat
argument_list|(
operator|-
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|rangeArray
operator|.
name|set
argument_list|(
literal|3
argument_list|,
operator|new
name|COSFloat
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rangeArray
operator|=
name|range
operator|.
name|getCOSArray
argument_list|()
expr_stmt|;
block|}
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|,
name|rangeArray
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

