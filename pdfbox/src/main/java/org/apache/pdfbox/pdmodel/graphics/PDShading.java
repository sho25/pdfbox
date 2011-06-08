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
name|COSBoolean
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
name|pdmodel
operator|.
name|graphics
operator|.
name|color
operator|.
name|PDColorSpaceFactory
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * This class represents a Shading Pattern color space.  *  See section 4.6.3 of the PDF 1.7 specification.  *  * @author<a href="mailto:Daniel.Wilson@BlackLocustSoftware.com">Daniel wilson</a>  * @version $Revision: 1.0 $  */
end_comment

begin_class
specifier|public
class|class
name|PDShading
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|DictShading
decl_stmt|;
specifier|private
name|COSName
name|shadingname
decl_stmt|;
specifier|private
name|COSArray
name|domain
init|=
literal|null
decl_stmt|;
specifier|private
name|COSArray
name|extend
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
name|PDColorSpace
name|colorspace
init|=
literal|null
decl_stmt|;
comment|/**      * The name of this object.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"Shading"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDShading
parameter_list|()
block|{
name|DictShading
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
comment|//DictShading.add( COSName.getPDFName( NAME ) );
block|}
comment|/**      * Constructor.      *      * @param shading The shading dictionary.      */
specifier|public
name|PDShading
parameter_list|(
name|COSName
name|name
parameter_list|,
name|COSDictionary
name|shading
parameter_list|)
block|{
name|DictShading
operator|=
name|shading
expr_stmt|;
name|shadingname
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * This will return the name of the object.      *      * @return The name of the object.      */
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
name|COSName
operator|.
name|SHADING
return|;
block|}
comment|/**     * This will return the name of this particular shading dictionary     *     * @return The name of the shading dictionary     */
specifier|public
name|COSName
name|getShadingName
parameter_list|()
block|{
return|return
name|shadingname
return|;
block|}
comment|/**     * This will return the ShadingType -- an integer between 1 and 7 that specifies the gradient type.     * Required in all Shading Dictionaries.     *     * @return The Shading Type     */
specifier|public
name|int
name|getShadingType
parameter_list|()
block|{
return|return
name|DictShading
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|SHADING_TYPE
argument_list|)
return|;
block|}
comment|/**     * This will return the Color Space.     * Required in all Shading Dictionaries.     *     * @return The Color Space of the shading dictionary     */
specifier|public
name|PDColorSpace
name|getColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|colorspace
operator|==
literal|null
condition|)
block|{
name|colorspace
operator|=
name|PDColorSpaceFactory
operator|.
name|createColorSpace
argument_list|(
name|DictShading
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|COLORSPACE
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|colorspace
return|;
block|}
comment|/**     * This will return a boolean flag indicating whether to antialias the shading pattern.     *     * @return The antialias flag, defaulting to False     */
specifier|public
name|boolean
name|getAntiAlias
parameter_list|()
block|{
return|return
name|DictShading
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
comment|/**     * Returns the coordinate array used by several of the gradient types. Interpretation depends on the ShadingType.     *     * @return The coordinate array.     */
specifier|public
name|COSArray
name|getCoords
parameter_list|()
block|{
return|return
call|(
name|COSArray
call|)
argument_list|(
name|DictShading
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|COORDS
argument_list|)
argument_list|)
return|;
block|}
comment|/**     * Returns the function used by several of the gradient types. Interpretation depends on the ShadingType.     *     * @return The gradient function.     */
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
name|function
operator|=
name|PDFunction
operator|.
name|create
argument_list|(
name|DictShading
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FUNCTION
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|function
return|;
block|}
comment|/**     * Returns the Domain array used by several of the gradient types. Interpretation depends on the ShadingType.     *     * @return The Domain array.     */
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
call|(
name|COSArray
call|)
argument_list|(
name|DictShading
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DOMAIN
argument_list|)
argument_list|)
expr_stmt|;
comment|// use default values
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
name|domain
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|domain
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
name|domain
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
block|}
block|}
return|return
name|domain
return|;
block|}
comment|/**     * Returns the Extend array used by several of the gradient types. Interpretation depends on the ShadingType.     * Default is {false, false}.     *     * @return The Extend array.     */
specifier|public
name|COSArray
name|getExtend
parameter_list|()
block|{
if|if
condition|(
name|extend
operator|==
literal|null
condition|)
block|{
name|extend
operator|=
call|(
name|COSArray
call|)
argument_list|(
name|DictShading
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|EXTEND
argument_list|)
argument_list|)
expr_stmt|;
comment|// use default values
if|if
condition|(
name|extend
operator|==
literal|null
condition|)
block|{
name|extend
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|extend
operator|.
name|add
argument_list|(
name|COSBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|extend
operator|.
name|add
argument_list|(
name|COSBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|extend
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|sColorSpace
decl_stmt|;
name|String
name|sFunction
decl_stmt|;
try|try
block|{
name|sColorSpace
operator|=
name|getColorSpace
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|sColorSpace
operator|=
literal|"Failure retrieving ColorSpace: "
operator|+
name|e
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|sFunction
operator|=
name|getFunction
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|sFunction
operator|=
literal|"n/a"
expr_stmt|;
block|}
name|String
name|s
init|=
literal|"Shading "
operator|+
name|shadingname
operator|+
literal|"\n"
operator|+
literal|"\tShadingType: "
operator|+
name|getShadingType
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"\tColorSpace: "
operator|+
name|sColorSpace
operator|+
literal|"\n"
operator|+
literal|"\tAntiAlias: "
operator|+
name|getAntiAlias
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"\tCoords: "
operator|+
operator|(
name|getCoords
argument_list|()
operator|!=
literal|null
condition|?
name|getCoords
argument_list|()
operator|.
name|toString
argument_list|()
else|:
literal|""
operator|)
operator|+
literal|"\n"
operator|+
literal|"\tDomain: "
operator|+
name|getDomain
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"\tFunction: "
operator|+
name|sFunction
operator|+
literal|"\n"
operator|+
literal|"\tExtend: "
operator|+
name|getExtend
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|"\n"
operator|+
literal|"\tRaw Value:\n"
operator|+
name|DictShading
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|s
return|;
block|}
block|}
end_class

end_unit

