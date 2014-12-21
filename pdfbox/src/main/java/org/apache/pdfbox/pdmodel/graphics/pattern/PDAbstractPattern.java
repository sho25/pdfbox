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
name|pattern
package|;
end_package

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
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
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
comment|/**  * A Pattern dictionary from a page's resources.  * @author Andreas Lehmkühler  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDAbstractPattern
implements|implements
name|COSObjectable
block|{
comment|/** Tiling pattern type. */
specifier|public
specifier|static
specifier|final
name|int
name|TYPE_TILING_PATTERN
init|=
literal|1
decl_stmt|;
comment|/** Shading pattern type. */
specifier|public
specifier|static
specifier|final
name|int
name|TYPE_SHADING_PATTERN
init|=
literal|2
decl_stmt|;
comment|/**      * Create the correct PD Model pattern based on the COS base pattern.      * @param resourceDictionary the COS pattern dictionary      * @return the newly created pattern resources object      * @throws IOException If we are unable to create the PDPattern object.      */
specifier|public
specifier|static
name|PDAbstractPattern
name|create
parameter_list|(
name|COSDictionary
name|resourceDictionary
parameter_list|)
throws|throws
name|IOException
block|{
name|PDAbstractPattern
name|pattern
decl_stmt|;
name|int
name|patternType
init|=
name|resourceDictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|PATTERN_TYPE
argument_list|,
literal|0
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|patternType
condition|)
block|{
case|case
name|TYPE_TILING_PATTERN
case|:
name|pattern
operator|=
operator|new
name|PDTilingPattern
argument_list|(
name|resourceDictionary
argument_list|)
expr_stmt|;
break|break;
case|case
name|TYPE_SHADING_PATTERN
case|:
name|pattern
operator|=
operator|new
name|PDShadingPattern
argument_list|(
name|resourceDictionary
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Unknown pattern type "
operator|+
name|patternType
argument_list|)
throw|;
block|}
return|return
name|pattern
return|;
block|}
specifier|private
specifier|final
name|COSDictionary
name|patternDictionary
decl_stmt|;
comment|/**      * Creates a new Pattern dictionary.      */
specifier|public
name|PDAbstractPattern
parameter_list|()
block|{
name|patternDictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|patternDictionary
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|PATTERN
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new Pattern dictionary from the given COS dictionary.      * @param resourceDictionary The COSDictionary for this pattern resource.      */
specifier|public
name|PDAbstractPattern
parameter_list|(
name|COSDictionary
name|resourceDictionary
parameter_list|)
block|{
name|patternDictionary
operator|=
name|resourceDictionary
expr_stmt|;
block|}
comment|/**      * This will get the underlying dictionary.      * @return The dictionary for these pattern resources.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|patternDictionary
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|patternDictionary
return|;
block|}
comment|/**      * Sets the filter entry of the encryption dictionary.      * @param filter The filter name.      */
specifier|public
name|void
name|setFilter
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
name|patternDictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
name|filter
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the name of the filter.      * @return The filter name contained in this encryption dictionary.      */
specifier|public
name|String
name|getFilter
parameter_list|()
block|{
return|return
name|patternDictionary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|)
return|;
block|}
comment|/**      * This will set the length of the content stream.      * @param length The new stream length.      */
specifier|public
name|void
name|setLength
parameter_list|(
name|int
name|length
parameter_list|)
block|{
name|patternDictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the length of the content stream.      * @return The length of the content stream      */
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|patternDictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the paint type.      * @param paintType The new paint type.      */
specifier|public
name|void
name|setPaintType
parameter_list|(
name|int
name|paintType
parameter_list|)
block|{
name|patternDictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|PAINT_TYPE
argument_list|,
name|paintType
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the paint type.      * @return The type of object that this is.      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|COSName
operator|.
name|PATTERN
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * This will set the pattern type.      * @param patternType The new pattern type.      */
specifier|public
name|void
name|setPatternType
parameter_list|(
name|int
name|patternType
parameter_list|)
block|{
name|patternDictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|PATTERN_TYPE
argument_list|,
name|patternType
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the pattern type.      * @return The pattern type      */
specifier|public
specifier|abstract
name|int
name|getPatternType
parameter_list|()
function_decl|;
comment|/**      * Returns the pattern matrix, or the identity matrix is none is available.      */
specifier|public
name|Matrix
name|getMatrix
parameter_list|()
block|{
name|Matrix
name|matrix
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
argument_list|()
expr_stmt|;
name|matrix
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|matrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// default value is the identity matrix
name|matrix
operator|=
operator|new
name|Matrix
argument_list|()
expr_stmt|;
block|}
return|return
name|matrix
return|;
block|}
comment|/**      * Sets the optional Matrix entry for the Pattern.      * @param transform the transformation matrix      */
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
block|}
end_class

end_unit

