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

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Paint
import|;
end_import

begin_comment
comment|/**  * Resources for a shading type 4 (Free-Form Gouraud-Shaded Triangle Mesh).  */
end_comment

begin_class
specifier|public
class|class
name|PDShadingType4
extends|extends
name|PDTriangleBasedShadingType
block|{
comment|/**      * Constructor using the given shading dictionary.      * @param shadingDictionary the dictionary for this shading      */
specifier|public
name|PDShadingType4
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
name|SHADING_TYPE4
return|;
block|}
comment|/**      * The bits per flag of this shading. This will return -1 if one has not been set.      * @return The number of bits per flag.      */
specifier|public
name|int
name|getBitsPerFlag
parameter_list|()
block|{
return|return
name|getCOSDictionary
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|BITS_PER_FLAG
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
comment|/**      * Set the number of bits per flag.      * @param bitsPerFlag the number of bits per flag      */
specifier|public
name|void
name|setBitsPerFlag
parameter_list|(
name|int
name|bitsPerFlag
parameter_list|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|BITS_PER_FLAG
argument_list|,
name|bitsPerFlag
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
parameter_list|,
name|int
name|pageHeight
parameter_list|)
block|{
return|return
operator|new
name|Type4ShadingPaint
argument_list|(
name|this
argument_list|,
name|matrix
argument_list|,
name|pageHeight
argument_list|)
return|;
block|}
block|}
end_class

end_unit

