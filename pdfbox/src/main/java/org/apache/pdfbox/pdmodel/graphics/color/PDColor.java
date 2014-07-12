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
name|COSNumber
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
name|COSString
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
name|Arrays
import|;
end_import

begin_comment
comment|/**  * A color value, consisting of one or more color components, or for pattern color spaces,  * a name and optional color components.  * Color values are not associated with any given color space.  *  * Instances of PDColor are immutable.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDColor
block|{
comment|/** The color black in the DeviceGray color space. */
specifier|public
specifier|static
name|PDColor
name|DEVICE_GRAY_BLACK
init|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|}
argument_list|)
decl_stmt|;
comment|/** A pattern which leaves no marks on the page. */
specifier|public
specifier|static
name|PDColor
name|EMPTY_PATTERN
init|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{ }
argument_list|,
literal|null
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|float
index|[]
name|components
decl_stmt|;
specifier|private
specifier|final
name|String
name|patternName
decl_stmt|;
comment|/**      * Creates a PDColor containing the given color value.      * @param array a COS array containing the color value      */
specifier|public
name|PDColor
parameter_list|(
name|COSArray
name|array
parameter_list|)
block|{
if|if
condition|(
name|array
operator|.
name|get
argument_list|(
name|array
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|instanceof
name|COSName
condition|)
block|{
comment|// color components (optional)
name|components
operator|=
operator|new
name|float
index|[
name|array
operator|.
name|size
argument_list|()
operator|-
literal|1
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|components
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
comment|// pattern name (required)
name|patternName
operator|=
operator|(
operator|(
name|COSName
operator|)
name|array
operator|.
name|get
argument_list|(
name|array
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// color components only
name|components
operator|=
operator|new
name|float
index|[
name|array
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|components
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
name|patternName
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Creates a PDColor containing the given color component values.      * @param components array of color component values      */
specifier|public
name|PDColor
parameter_list|(
name|float
index|[]
name|components
parameter_list|)
block|{
name|this
operator|.
name|components
operator|=
name|components
operator|.
name|clone
argument_list|()
expr_stmt|;
name|this
operator|.
name|patternName
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Creates a PDColor containing the given pattern name.      * @param patternName the name of a pattern in a pattern dictionary      */
specifier|public
name|PDColor
parameter_list|(
name|String
name|patternName
parameter_list|)
block|{
name|this
operator|.
name|components
operator|=
operator|new
name|float
index|[
literal|0
index|]
expr_stmt|;
name|this
operator|.
name|patternName
operator|=
name|patternName
expr_stmt|;
block|}
comment|/**      * Creates a PDColor containing the given color component values and pattern name.      * @param components array of color component values      * @param patternName the name of a pattern in a pattern dictionary      */
specifier|public
name|PDColor
parameter_list|(
name|float
index|[]
name|components
parameter_list|,
name|String
name|patternName
parameter_list|)
block|{
name|this
operator|.
name|components
operator|=
name|components
operator|.
name|clone
argument_list|()
expr_stmt|;
name|this
operator|.
name|patternName
operator|=
name|patternName
expr_stmt|;
block|}
comment|/**      * Returns the components of this color value.      * @return the components of this color value      */
specifier|public
name|float
index|[]
name|getComponents
parameter_list|()
block|{
return|return
name|components
operator|.
name|clone
argument_list|()
return|;
block|}
comment|/**      * Returns the pattern name from this color value.      * @return the pattern name from this color value      */
specifier|public
name|String
name|getPatternName
parameter_list|()
block|{
return|return
name|patternName
return|;
block|}
comment|/**      * Returns true if this color value is a pattern.      * @return true if this color value is a pattern      */
specifier|public
name|boolean
name|isPattern
parameter_list|()
block|{
return|return
name|patternName
operator|!=
literal|null
return|;
block|}
comment|// todo: toRGB() which returns a packed int
comment|// todo: getColorSpace() getter
comment|// STARTING POINT:
comment|// todo: JavaDoc [TODO: WHAT TO DO IF THIS IS A PATTERN? THROW ERROR?]
specifier|public
name|int
name|toRGB
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
throws|throws
name|IOException
comment|// todo: store colorSpace locally (Also, fallback color if exception?)
block|{
name|float
index|[]
name|floats
init|=
name|colorSpace
operator|.
name|toRGB
argument_list|(
name|components
argument_list|)
decl_stmt|;
name|int
name|r
init|=
name|Math
operator|.
name|round
argument_list|(
name|floats
index|[
literal|0
index|]
operator|*
literal|255
argument_list|)
decl_stmt|;
name|int
name|g
init|=
name|Math
operator|.
name|round
argument_list|(
name|floats
index|[
literal|1
index|]
operator|*
literal|255
argument_list|)
decl_stmt|;
name|int
name|b
init|=
name|Math
operator|.
name|round
argument_list|(
name|floats
index|[
literal|2
index|]
operator|*
literal|255
argument_list|)
decl_stmt|;
name|int
name|rgb
init|=
name|r
decl_stmt|;
name|rgb
operator|=
operator|(
name|rgb
operator|<<
literal|8
operator|)
operator|+
name|g
expr_stmt|;
name|rgb
operator|=
operator|(
name|rgb
operator|<<
literal|8
operator|)
operator|+
name|b
expr_stmt|;
return|return
name|rgb
return|;
block|}
comment|/**      * Returns this color value as a COS array      * @return the color value as a COS array      */
specifier|public
name|COSArray
name|toCOSArray
parameter_list|()
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
name|components
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
name|patternName
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"PDColor{components="
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|components
argument_list|)
operator|+
literal|", patternName="
operator|+
name|patternName
operator|+
literal|"}"
return|;
block|}
block|}
end_class

end_unit

