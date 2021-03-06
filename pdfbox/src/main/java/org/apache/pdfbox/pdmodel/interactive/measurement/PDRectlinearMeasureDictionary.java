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
name|interactive
operator|.
name|measurement
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

begin_comment
comment|/**  * This class represents a rectlinear measure dictionary.  *   */
end_comment

begin_class
specifier|public
class|class
name|PDRectlinearMeasureDictionary
extends|extends
name|PDMeasureDictionary
block|{
comment|/**      * The subtype of the rectlinear measure dictionary.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUBTYPE
init|=
literal|"RL"
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDRectlinearMeasureDictionary
parameter_list|()
block|{
name|this
operator|.
name|setSubtype
argument_list|(
name|SUBTYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param dictionary the corresponding dictionary      */
specifier|public
name|PDRectlinearMeasureDictionary
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|super
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the scale ration.      *       * @return the scale ratio.      */
specifier|public
name|String
name|getScaleRatio
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|R
argument_list|)
return|;
block|}
comment|/**      * This will set the scale ration.      *       * @param scaleRatio the scale ratio.      */
specifier|public
name|void
name|setScaleRatio
parameter_list|(
name|String
name|scaleRatio
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|R
argument_list|,
name|scaleRatio
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the changes along the x-axis.      *       * @return changes along the x-axis      */
specifier|public
name|PDNumberFormatDictionary
index|[]
name|getChangeXs
parameter_list|()
block|{
name|COSArray
name|x
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
if|if
condition|(
name|x
operator|!=
literal|null
condition|)
block|{
name|PDNumberFormatDictionary
index|[]
name|retval
init|=
operator|new
name|PDNumberFormatDictionary
index|[
name|x
operator|.
name|size
argument_list|()
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
name|x
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|x
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|retval
index|[
name|i
index|]
operator|=
operator|new
name|PDNumberFormatDictionary
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the changes along the x-axis.      *       * @param changeXs changes along the x-axis      */
specifier|public
name|void
name|setChangeXs
parameter_list|(
name|PDNumberFormatDictionary
index|[]
name|changeXs
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|PDNumberFormatDictionary
name|changeX
range|:
name|changeXs
control|)
block|{
name|array
operator|.
name|add
argument_list|(
name|changeX
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"X"
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the changes along the y-axis.      *       * @return changes along the y-axis      */
specifier|public
name|PDNumberFormatDictionary
index|[]
name|getChangeYs
parameter_list|()
block|{
name|COSArray
name|y
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"Y"
argument_list|)
decl_stmt|;
if|if
condition|(
name|y
operator|!=
literal|null
condition|)
block|{
name|PDNumberFormatDictionary
index|[]
name|retval
init|=
operator|new
name|PDNumberFormatDictionary
index|[
name|y
operator|.
name|size
argument_list|()
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
name|y
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|y
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|retval
index|[
name|i
index|]
operator|=
operator|new
name|PDNumberFormatDictionary
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the changes along the y-axis.      *       * @param changeYs changes along the y-axis      */
specifier|public
name|void
name|setChangeYs
parameter_list|(
name|PDNumberFormatDictionary
index|[]
name|changeYs
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|PDNumberFormatDictionary
name|changeY
range|:
name|changeYs
control|)
block|{
name|array
operator|.
name|add
argument_list|(
name|changeY
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"Y"
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the distances.      *       * @return distances      */
specifier|public
name|PDNumberFormatDictionary
index|[]
name|getDistances
parameter_list|()
block|{
name|COSArray
name|d
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"D"
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|!=
literal|null
condition|)
block|{
name|PDNumberFormatDictionary
index|[]
name|retval
init|=
operator|new
name|PDNumberFormatDictionary
index|[
name|d
operator|.
name|size
argument_list|()
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
name|d
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|d
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|retval
index|[
name|i
index|]
operator|=
operator|new
name|PDNumberFormatDictionary
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the distances.      *       * @param distances distances      */
specifier|public
name|void
name|setDistances
parameter_list|(
name|PDNumberFormatDictionary
index|[]
name|distances
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|PDNumberFormatDictionary
name|distance
range|:
name|distances
control|)
block|{
name|array
operator|.
name|add
argument_list|(
name|distance
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"D"
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the areas.      *       * @return areas      */
specifier|public
name|PDNumberFormatDictionary
index|[]
name|getAreas
parameter_list|()
block|{
name|COSArray
name|a
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|A
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
condition|)
block|{
name|PDNumberFormatDictionary
index|[]
name|retval
init|=
operator|new
name|PDNumberFormatDictionary
index|[
name|a
operator|.
name|size
argument_list|()
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
name|a
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|a
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|retval
index|[
name|i
index|]
operator|=
operator|new
name|PDNumberFormatDictionary
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the areas.      *       * @param areas areas      */
specifier|public
name|void
name|setAreas
parameter_list|(
name|PDNumberFormatDictionary
index|[]
name|areas
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|PDNumberFormatDictionary
name|area
range|:
name|areas
control|)
block|{
name|array
operator|.
name|add
argument_list|(
name|area
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|A
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the angles.      *       * @return angles      */
specifier|public
name|PDNumberFormatDictionary
index|[]
name|getAngles
parameter_list|()
block|{
name|COSArray
name|t
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"T"
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|PDNumberFormatDictionary
index|[]
name|retval
init|=
operator|new
name|PDNumberFormatDictionary
index|[
name|t
operator|.
name|size
argument_list|()
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
name|t
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|t
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|retval
index|[
name|i
index|]
operator|=
operator|new
name|PDNumberFormatDictionary
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the angles.      *       * @param angles angles      */
specifier|public
name|void
name|setAngles
parameter_list|(
name|PDNumberFormatDictionary
index|[]
name|angles
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|PDNumberFormatDictionary
name|angle
range|:
name|angles
control|)
block|{
name|array
operator|.
name|add
argument_list|(
name|angle
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"T"
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the sloaps of a line.      *       * @return the sloaps of a line      */
specifier|public
name|PDNumberFormatDictionary
index|[]
name|getLineSloaps
parameter_list|()
block|{
name|COSArray
name|s
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"S"
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|PDNumberFormatDictionary
index|[]
name|retval
init|=
operator|new
name|PDNumberFormatDictionary
index|[
name|s
operator|.
name|size
argument_list|()
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
name|s
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|s
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|retval
index|[
name|i
index|]
operator|=
operator|new
name|PDNumberFormatDictionary
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the sloaps of a line.      *       * @param lineSloaps the sloaps of a line      */
specifier|public
name|void
name|setLineSloaps
parameter_list|(
name|PDNumberFormatDictionary
index|[]
name|lineSloaps
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|PDNumberFormatDictionary
name|lineSloap
range|:
name|lineSloaps
control|)
block|{
name|array
operator|.
name|add
argument_list|(
name|lineSloap
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"S"
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the origin of the coordinate system.      *       * @return the origin      */
specifier|public
name|float
index|[]
name|getCoordSystemOrigin
parameter_list|()
block|{
name|COSArray
name|o
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"O"
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
return|return
name|o
operator|.
name|toFloatArray
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the origin of the coordinate system.      *       * @param coordSystemOrigin the origin      */
specifier|public
name|void
name|setCoordSystemOrigin
parameter_list|(
name|float
index|[]
name|coordSystemOrigin
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|array
operator|.
name|setFloatArray
argument_list|(
name|coordSystemOrigin
argument_list|)
expr_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"O"
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the CYX factor.      *       * @return CYX factor      */
specifier|public
name|float
name|getCYX
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getFloat
argument_list|(
literal|"CYX"
argument_list|)
return|;
block|}
comment|/**      * This will set the CYX factor.      *       * @param cyx CYX factor      */
specifier|public
name|void
name|setCYX
parameter_list|(
name|float
name|cyx
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setFloat
argument_list|(
literal|"CYX"
argument_list|,
name|cyx
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

