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
name|util
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
name|COSInteger
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
name|COSArrayList
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
name|graphics
operator|.
name|color
operator|.
name|PDColorSpaceFactory
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * This contains all of the image parameters for in inlined image.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|ImageParameters
block|{
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|ImageParameters
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param params The image parameters.      */
specifier|public
name|ImageParameters
parameter_list|(
name|COSDictionary
name|params
parameter_list|)
block|{
name|dictionary
operator|=
name|params
expr_stmt|;
block|}
comment|/**      * This will get the dictionary that stores the image parameters.      *      * @return The COS dictionary that stores the image parameters.      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
specifier|private
name|COSBase
name|getCOSObject
parameter_list|(
name|String
name|abbreviatedName
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|COSBase
name|retval
init|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|abbreviatedName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
specifier|private
name|int
name|getNumberOrNegativeOne
parameter_list|(
name|String
name|abbreviatedName
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|int
name|retval
init|=
operator|-
literal|1
decl_stmt|;
name|COSNumber
name|number
init|=
operator|(
name|COSNumber
operator|)
name|getCOSObject
argument_list|(
name|abbreviatedName
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|number
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|number
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * The bits per component of this image.  This will return -1 if one has not      * been set.      *      * @return The number of bits per component.      */
specifier|public
name|int
name|getBitsPerComponent
parameter_list|()
block|{
return|return
name|getNumberOrNegativeOne
argument_list|(
literal|"BPC"
argument_list|,
literal|"BitsPerComponent"
argument_list|)
return|;
block|}
comment|/**      * Set the number of bits per component.      *      * @param bpc The number of bits per component.      */
specifier|public
name|void
name|setBitsPerComponent
parameter_list|(
name|int
name|bpc
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"BPC"
argument_list|)
argument_list|,
operator|new
name|COSInteger
argument_list|(
name|bpc
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the color space or null if none exists.      *      * @return The color space for this image.      *      * @throws IOException If there is an error getting the colorspace.      */
specifier|public
name|PDColorSpace
name|getColorSpace
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getColorSpace
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * This will get the color space or null if none exists.      *      * @param colorSpaces The ColorSpace dictionary from the current resources, if any.      *      * @return The color space for this image.      *      * @throws IOException If there is an error getting the colorspace.      */
specifier|public
name|PDColorSpace
name|getColorSpace
parameter_list|(
name|Map
name|colorSpaces
parameter_list|)
throws|throws
name|IOException
block|{
name|COSBase
name|cs
init|=
name|getCOSObject
argument_list|(
literal|"CS"
argument_list|,
literal|"ColorSpace"
argument_list|)
decl_stmt|;
name|PDColorSpace
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|PDColorSpaceFactory
operator|.
name|createColorSpace
argument_list|(
name|cs
argument_list|,
name|colorSpaces
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the color space for this image.      *      * @param cs The color space for this image.      */
specifier|public
name|void
name|setColorSpace
parameter_list|(
name|PDColorSpace
name|cs
parameter_list|)
block|{
name|COSBase
name|base
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
name|base
operator|=
name|cs
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
name|getPDFName
argument_list|(
literal|"CS"
argument_list|)
argument_list|,
name|base
argument_list|)
expr_stmt|;
block|}
comment|/**      * The height of this image.  This will return -1 if one has not      * been set.      *      * @return The height.      */
specifier|public
name|int
name|getHeight
parameter_list|()
block|{
return|return
name|getNumberOrNegativeOne
argument_list|(
literal|"H"
argument_list|,
literal|"Height"
argument_list|)
return|;
block|}
comment|/**      * Set the height of the image.      *      * @param h The height of the image.      */
specifier|public
name|void
name|setHeight
parameter_list|(
name|int
name|h
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"H"
argument_list|)
argument_list|,
operator|new
name|COSInteger
argument_list|(
name|h
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * The width of this image.  This will return -1 if one has not      * been set.      *      * @return The width.      */
specifier|public
name|int
name|getWidth
parameter_list|()
block|{
return|return
name|getNumberOrNegativeOne
argument_list|(
literal|"W"
argument_list|,
literal|"Width"
argument_list|)
return|;
block|}
comment|/**      * Set the width of the image.      *      * @param w The width of the image.      */
specifier|public
name|void
name|setWidth
parameter_list|(
name|int
name|w
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"W"
argument_list|)
argument_list|,
operator|new
name|COSInteger
argument_list|(
name|w
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the list of filters that are associated with this stream.  Or      * null if there are none.      * @return A list of all encoding filters to apply to this stream.      */
specifier|public
name|List
name|getFilters
parameter_list|()
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSBase
name|filters
init|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"Filter"
block|,
literal|"F"
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|filters
operator|instanceof
name|COSName
condition|)
block|{
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|filters
decl_stmt|;
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|name
operator|.
name|getName
argument_list|()
argument_list|,
name|name
argument_list|,
name|dictionary
argument_list|,
literal|"Filter"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|instanceof
name|COSArray
condition|)
block|{
name|retval
operator|=
name|COSArrayList
operator|.
name|convertCOSNameCOSArrayToList
argument_list|(
operator|(
name|COSArray
operator|)
name|filters
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the filters that are part of this stream.      *      * @param filters The filters that are part of this stream.      */
specifier|public
name|void
name|setFilters
parameter_list|(
name|List
name|filters
parameter_list|)
block|{
name|COSBase
name|obj
init|=
name|COSArrayList
operator|.
name|convertStringListToCOSNameCOSArray
argument_list|(
name|filters
argument_list|)
decl_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
literal|"Filter"
argument_list|,
name|obj
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

