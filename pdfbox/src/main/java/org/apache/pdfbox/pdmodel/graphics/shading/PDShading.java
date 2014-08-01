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
name|pdmodel
operator|.
name|common
operator|.
name|PDRectangle
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
name|graphics
operator|.
name|color
operator|.
name|PDColorSpace
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
comment|/**  * A Shading Resource.  * @author Andreas Lehmkühler  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDShading
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
specifier|private
name|COSArray
name|background
init|=
literal|null
decl_stmt|;
specifier|private
name|PDRectangle
name|bBox
init|=
literal|null
decl_stmt|;
specifier|private
name|PDColorSpace
name|colorSpace
init|=
literal|null
decl_stmt|;
specifier|private
name|PDFunction
name|function
init|=
literal|null
decl_stmt|;
specifier|private
name|PDFunction
index|[]
name|functionArray
init|=
literal|null
decl_stmt|;
comment|/** shading type 1 = function based shading. */
specifier|public
specifier|static
specifier|final
name|int
name|SHADING_TYPE1
init|=
literal|1
decl_stmt|;
comment|/** shading type 2 = axial shading. */
specifier|public
specifier|static
specifier|final
name|int
name|SHADING_TYPE2
init|=
literal|2
decl_stmt|;
comment|/** shading type 3 = radial shading. */
specifier|public
specifier|static
specifier|final
name|int
name|SHADING_TYPE3
init|=
literal|3
decl_stmt|;
comment|/** shading type 4 = Free-Form Gouraud-Shaded Triangle Meshes. */
specifier|public
specifier|static
specifier|final
name|int
name|SHADING_TYPE4
init|=
literal|4
decl_stmt|;
comment|/** shading type 5 = Lattice-Form Gouraud-Shaded Triangle Meshes. */
specifier|public
specifier|static
specifier|final
name|int
name|SHADING_TYPE5
init|=
literal|5
decl_stmt|;
comment|/** shading type 6 = Coons Patch Meshes. */
specifier|public
specifier|static
specifier|final
name|int
name|SHADING_TYPE6
init|=
literal|6
decl_stmt|;
comment|/** shading type 7 = Tensor-Product Patch Meshes. */
specifier|public
specifier|static
specifier|final
name|int
name|SHADING_TYPE7
init|=
literal|7
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDShading
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor using the given shading dictionary.      * @param shadingDictionary the dictionary for this shading      */
specifier|public
name|PDShading
parameter_list|(
name|COSDictionary
name|shadingDictionary
parameter_list|)
block|{
name|dictionary
operator|=
name|shadingDictionary
expr_stmt|;
block|}
comment|/**      * This will get the underlying dictionary.      * @return the dictionary for this shading      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      * @return the cos object that matches this Java object      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * This will return the type.      * @return the type of object that this is      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|COSName
operator|.
name|SHADING
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * This will set the shading type.      * @param shadingType the new shading type      */
specifier|public
name|void
name|setShadingType
parameter_list|(
name|int
name|shadingType
parameter_list|)
block|{
name|dictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|SHADING_TYPE
argument_list|,
name|shadingType
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the shading type.      * @return the shading typ      */
specifier|public
specifier|abstract
name|int
name|getShadingType
parameter_list|()
function_decl|;
comment|/**      * This will set the background.      * @param newBackground the new background      */
specifier|public
name|void
name|setBackground
parameter_list|(
name|COSArray
name|newBackground
parameter_list|)
block|{
name|background
operator|=
name|newBackground
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BACKGROUND
argument_list|,
name|newBackground
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the background.      * @return the background      */
specifier|public
name|COSArray
name|getBackground
parameter_list|()
block|{
if|if
condition|(
name|background
operator|==
literal|null
condition|)
block|{
name|background
operator|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BACKGROUND
argument_list|)
expr_stmt|;
block|}
return|return
name|background
return|;
block|}
comment|/**      * An array of four numbers in the form coordinate system (see below),      * giving the coordinates of the left, bottom, right, and top edges, respectively,      * of the shading's bounding box.      * @return the BBox of the form      */
specifier|public
name|PDRectangle
name|getBBox
parameter_list|()
block|{
if|if
condition|(
name|bBox
operator|==
literal|null
condition|)
block|{
name|COSArray
name|array
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
name|BBOX
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|bBox
operator|=
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|bBox
return|;
block|}
comment|/**      * This will set the BBox (bounding box) for this Shading.      * @param newBBox the new BBox      */
specifier|public
name|void
name|setBBox
parameter_list|(
name|PDRectangle
name|newBBox
parameter_list|)
block|{
name|bBox
operator|=
name|newBBox
expr_stmt|;
if|if
condition|(
name|bBox
operator|==
literal|null
condition|)
block|{
name|dictionary
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|BBOX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BBOX
argument_list|,
name|bBox
operator|.
name|getCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will set the AntiAlias value.      * @param antiAlias the new AntiAlias value      */
specifier|public
name|void
name|setAntiAlias
parameter_list|(
name|boolean
name|antiAlias
parameter_list|)
block|{
name|dictionary
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|ANTI_ALIAS
argument_list|,
name|antiAlias
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the AntiAlias value.      * @return the AntiAlias value      */
specifier|public
name|boolean
name|getAntiAlias
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|ANTI_ALIAS
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * This will get the color space or null if none exists.      * @return the color space for the shading      * @throws IOException if there is an error getting the color space      */
specifier|public
name|PDColorSpace
name|getColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|colorSpace
operator|==
literal|null
condition|)
block|{
name|COSBase
name|colorSpaceDictionary
init|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CS
argument_list|,
name|COSName
operator|.
name|COLORSPACE
argument_list|)
decl_stmt|;
name|colorSpace
operator|=
name|PDColorSpace
operator|.
name|create
argument_list|(
name|colorSpaceDictionary
argument_list|)
expr_stmt|;
block|}
return|return
name|colorSpace
return|;
block|}
comment|/**      * This will set the color space for the shading.      * @param colorSpace the color space      */
specifier|public
name|void
name|setColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
name|this
operator|.
name|colorSpace
operator|=
name|colorSpace
expr_stmt|;
if|if
condition|(
name|colorSpace
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
name|COLORSPACE
argument_list|,
name|colorSpace
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dictionary
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|COLORSPACE
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create the correct PD Model shading based on the COS base shading.      * @param resourceDictionary the COS shading dictionary      * @return the newly created shading resources object      * @throws IOException if we are unable to create the PDShading object      */
specifier|public
specifier|static
name|PDShading
name|create
parameter_list|(
name|COSDictionary
name|resourceDictionary
parameter_list|)
throws|throws
name|IOException
block|{
name|PDShading
name|shading
init|=
literal|null
decl_stmt|;
name|int
name|shadingType
init|=
name|resourceDictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|SHADING_TYPE
argument_list|,
literal|0
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|shadingType
condition|)
block|{
case|case
name|SHADING_TYPE1
case|:
name|shading
operator|=
operator|new
name|PDShadingType1
argument_list|(
name|resourceDictionary
argument_list|)
expr_stmt|;
break|break;
case|case
name|SHADING_TYPE2
case|:
name|shading
operator|=
operator|new
name|PDShadingType2
argument_list|(
name|resourceDictionary
argument_list|)
expr_stmt|;
break|break;
case|case
name|SHADING_TYPE3
case|:
name|shading
operator|=
operator|new
name|PDShadingType3
argument_list|(
name|resourceDictionary
argument_list|)
expr_stmt|;
break|break;
case|case
name|SHADING_TYPE4
case|:
name|shading
operator|=
operator|new
name|PDShadingType4
argument_list|(
name|resourceDictionary
argument_list|)
expr_stmt|;
break|break;
case|case
name|SHADING_TYPE5
case|:
name|shading
operator|=
operator|new
name|PDShadingType5
argument_list|(
name|resourceDictionary
argument_list|)
expr_stmt|;
break|break;
case|case
name|SHADING_TYPE6
case|:
name|shading
operator|=
operator|new
name|PDShadingType6
argument_list|(
name|resourceDictionary
argument_list|)
expr_stmt|;
break|break;
case|case
name|SHADING_TYPE7
case|:
name|shading
operator|=
operator|new
name|PDShadingType7
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
literal|"Error: Unknown shading type "
operator|+
name|shadingType
argument_list|)
throw|;
block|}
return|return
name|shading
return|;
block|}
comment|/**      * This will set the function for the color conversion.      * @param newFunction the new function      */
specifier|public
name|void
name|setFunction
parameter_list|(
name|PDFunction
name|newFunction
parameter_list|)
block|{
name|functionArray
operator|=
literal|null
expr_stmt|;
name|function
operator|=
name|newFunction
expr_stmt|;
if|if
condition|(
name|newFunction
operator|==
literal|null
condition|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|FUNCTION
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FUNCTION
argument_list|,
name|newFunction
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will set the functions COSArray for the color conversion.      * @param newFunctions the new COSArray containing all functions      */
specifier|public
name|void
name|setFunction
parameter_list|(
name|COSArray
name|newFunctions
parameter_list|)
block|{
name|functionArray
operator|=
literal|null
expr_stmt|;
name|function
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|newFunctions
operator|==
literal|null
condition|)
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|FUNCTION
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getCOSDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FUNCTION
argument_list|,
name|newFunctions
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will return the function used to convert the color values.      * @return the function      * @exception IOException if we are unable to create the PDFunction object      */
specifier|public
name|PDFunction
name|getFunction
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|function
operator|==
literal|null
condition|)
block|{
name|COSBase
name|dictionaryFunctionObject
init|=
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FUNCTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|dictionaryFunctionObject
operator|!=
literal|null
condition|)
name|function
operator|=
name|PDFunction
operator|.
name|create
argument_list|(
name|dictionaryFunctionObject
argument_list|)
expr_stmt|;
block|}
return|return
name|function
return|;
block|}
comment|/**      * Provide the function(s) of the shading dictionary as array.      * @return an array containing the function(s)       * @throws IOException if something went wrong      */
specifier|private
name|PDFunction
index|[]
name|getFunctionsArray
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|functionArray
operator|==
literal|null
condition|)
block|{
name|COSBase
name|functionObject
init|=
name|getCOSDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FUNCTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|functionObject
operator|instanceof
name|COSDictionary
condition|)
block|{
name|functionArray
operator|=
operator|new
name|PDFunction
index|[
literal|1
index|]
expr_stmt|;
name|functionArray
index|[
literal|0
index|]
operator|=
name|PDFunction
operator|.
name|create
argument_list|(
name|functionObject
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|COSArray
name|functionCOSArray
init|=
operator|(
name|COSArray
operator|)
name|functionObject
decl_stmt|;
name|int
name|numberOfFunctions
init|=
name|functionCOSArray
operator|.
name|size
argument_list|()
decl_stmt|;
name|functionArray
operator|=
operator|new
name|PDFunction
index|[
name|numberOfFunctions
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
name|numberOfFunctions
condition|;
name|i
operator|++
control|)
block|{
name|functionArray
index|[
name|i
index|]
operator|=
name|PDFunction
operator|.
name|create
argument_list|(
name|functionCOSArray
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|functionArray
return|;
block|}
comment|/**      * Convert the input value using the functions of the shading dictionary.      * @param inputValue the input value      * @return the output values      * @throws IOException thrown if something went wrong      */
specifier|public
name|float
index|[]
name|evalFunction
parameter_list|(
name|float
name|inputValue
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|evalFunction
argument_list|(
operator|new
name|float
index|[]
block|{
name|inputValue
block|}
argument_list|)
return|;
block|}
comment|/**      * Convert the input values using the functions of the shading dictionary.      * @param input the input values      * @return the output values      * @throws IOException thrown if something went wrong      */
specifier|public
name|float
index|[]
name|evalFunction
parameter_list|(
name|float
index|[]
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFunction
index|[]
name|functions
init|=
name|getFunctionsArray
argument_list|()
decl_stmt|;
name|int
name|numberOfFunctions
init|=
name|functions
operator|.
name|length
decl_stmt|;
name|float
index|[]
name|returnValues
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|numberOfFunctions
operator|==
literal|1
condition|)
block|{
name|returnValues
operator|=
name|functions
index|[
literal|0
index|]
operator|.
name|eval
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|returnValues
operator|=
operator|new
name|float
index|[
name|numberOfFunctions
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
name|numberOfFunctions
condition|;
name|i
operator|++
control|)
block|{
name|float
index|[]
name|newValue
init|=
name|functions
index|[
name|i
index|]
operator|.
name|eval
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|returnValues
index|[
name|i
index|]
operator|=
name|newValue
index|[
literal|0
index|]
expr_stmt|;
block|}
block|}
comment|// From the PDF spec:
comment|// "If the value returned by the function for a given colour component
comment|// is out of range, it shall be adjusted to the nearest valid value."
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|returnValues
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|returnValues
index|[
name|i
index|]
operator|<
literal|0
condition|)
block|{
name|returnValues
index|[
name|i
index|]
operator|=
literal|0
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|returnValues
index|[
name|i
index|]
operator|>
literal|1
condition|)
block|{
name|returnValues
index|[
name|i
index|]
operator|=
literal|1
expr_stmt|;
block|}
block|}
return|return
name|returnValues
return|;
block|}
comment|/**      * Returns an AWT paint which corresponds to this shading      * @param matrix the pattern matrix      * @param pageHeight the height of the current page      * @return an AWT Paint instance      */
specifier|public
specifier|abstract
name|Paint
name|toPaint
parameter_list|(
name|Matrix
name|matrix
parameter_list|,
name|int
name|pageHeight
parameter_list|)
function_decl|;
block|}
end_class

end_unit

