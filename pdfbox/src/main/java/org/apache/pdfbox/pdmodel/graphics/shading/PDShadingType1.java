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
name|shading
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Paint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * Resources for a function based shading.  */
end_comment

begin_class
specifier|public
class|class
name|PDShadingType1
extends|extends
name|PDShading
block|{
specifier|private
name|COSArray
name|domain
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor using the given shading dictionary.      *      * @param shadingDictionary the dictionary for this shading      */
specifier|public
name|PDShadingType1
parameter_list|(
name|COSDictionary
name|shadingDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|shadingDictionary
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getShadingType
parameter_list|()
block|{
return|return
name|PDShading
operator|.
name|SHADING_TYPE1
return|;
block|}
comment|/**      * This will get the optional Matrix of a function based shading.      *      * @return the matrix      */
specifier|public
name|Matrix
name|getMatrix
parameter_list|()
block|{
name|Matrix
name|matrix
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|MATRIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|matrix
operator|=
operator|new
name|Matrix
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
comment|// todo: else return identity matrix?
return|return
name|matrix
return|;
block|}
comment|/**      * Sets the optional Matrix entry for the function based shading.      *      * @param transform the transformation matrix      */
specifier|public
name|void
name|setMatrix
parameter_list|(
name|AffineTransform
name|transform
parameter_list|)
block|{
name|COSArray
name|matrix
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|double
index|[]
name|values
init|=
operator|new
name|double
index|[
literal|6
index|]
decl_stmt|;
name|transform
operator|.
name|getMatrix
argument_list|(
name|values
argument_list|)
expr_stmt|;
for|for
control|(
name|double
name|v
range|:
name|values
control|)
block|{
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
operator|(
name|float
operator|)
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|MATRIX
argument_list|,
name|matrix
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the optional Domain values of a function based shading.      *      * @return the domain values      */
specifier|public
name|COSArray
name|getDomain
parameter_list|()
block|{
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
name|domain
operator|=
operator|(
name|COSArray
operator|)
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DOMAIN
argument_list|)
expr_stmt|;
block|}
return|return
name|domain
return|;
block|}
comment|/**      * Sets the optional Domain entry for the function based shading.      *      * @param newDomain the domain array      */
specifier|public
name|void
name|setDomain
parameter_list|(
name|COSArray
name|newDomain
parameter_list|)
block|{
name|domain
operator|=
name|newDomain
expr_stmt|;
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DOMAIN
argument_list|,
name|newDomain
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Paint
name|toPaint
parameter_list|(
name|Matrix
name|matrix
parameter_list|)
block|{
return|return
operator|new
name|Type1ShadingPaint
argument_list|(
name|this
argument_list|,
name|matrix
argument_list|)
return|;
block|}
block|}
end_class

end_unit

