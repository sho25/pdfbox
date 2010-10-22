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
package|;
end_package

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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|cos
operator|.
name|COSStream
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
name|COSDictionaryMap
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
name|font
operator|.
name|PDFontFactory
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
name|PDExtendedGraphicsState
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
name|xobject
operator|.
name|PDXObject
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
name|xobject
operator|.
name|PDXObjectImage
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
name|markedcontent
operator|.
name|PDPropertyList
import|;
end_import

begin_comment
comment|/**  * This represents a set of resources available at the page/pages/stream level.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.16 $  */
end_comment

begin_class
specifier|public
class|class
name|PDResources
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|resources
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDResources
parameter_list|()
block|{
name|resources
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Prepopulated resources.      *      * @param resourceDictionary The cos dictionary for this resource.      */
specifier|public
name|PDResources
parameter_list|(
name|COSDictionary
name|resourceDictionary
parameter_list|)
block|{
name|resources
operator|=
name|resourceDictionary
expr_stmt|;
block|}
comment|/**      * This will get the underlying dictionary.      *      * @return The dictionary for these resources.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|resources
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|resources
return|;
block|}
comment|/**      * This will get the map of fonts.  This will never return null.  The keys are string      * and the values are PDFont objects.      *      * @param fontCache A map of existing PDFont objects to reuse.      * @return The map of fonts.      *      * @throws IOException If there is an error getting the fonts.      */
specifier|public
name|Map
name|getFonts
parameter_list|(
name|Map
name|fontCache
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|fonts
init|=
operator|(
name|COSDictionary
operator|)
name|resources
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FONT
argument_list|)
decl_stmt|;
if|if
condition|(
name|fonts
operator|==
literal|null
condition|)
block|{
name|fonts
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|resources
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FONT
argument_list|,
name|fonts
argument_list|)
expr_stmt|;
block|}
name|Map
name|actuals
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|COSDictionaryMap
argument_list|(
name|actuals
argument_list|,
name|fonts
argument_list|)
expr_stmt|;
for|for
control|(
name|COSName
name|fontName
range|:
name|fonts
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|font
init|=
name|fonts
operator|.
name|getDictionaryObject
argument_list|(
name|fontName
argument_list|)
decl_stmt|;
comment|//data-000174.pdf contains a font that is a COSArray, looks to be an error in the
comment|//PDF, we will just ignore entries that are not dictionaries.
if|if
condition|(
name|font
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|fontDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|font
decl_stmt|;
name|actuals
operator|.
name|put
argument_list|(
name|fontName
operator|.
name|getName
argument_list|()
argument_list|,
name|PDFontFactory
operator|.
name|createFont
argument_list|(
name|fontDictionary
argument_list|,
name|fontCache
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the map of fonts.  This will never return null.  The keys are string      * and the values are PDFont objects.      *      * @return The map of fonts.      *      * @throws IOException If there is an error getting the fonts.      */
specifier|public
name|Map
name|getFonts
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getFonts
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**      * This will get the map of PDXObjects that are in the resource dictionary.      *      * @return The map of xobjects.      *      * @throws IOException If there is an error creating the xobjects.      */
specifier|public
name|Map
name|getXObjects
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|xobjects
init|=
operator|(
name|COSDictionary
operator|)
name|resources
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|XOBJECT
argument_list|)
decl_stmt|;
if|if
condition|(
name|xobjects
operator|==
literal|null
condition|)
block|{
name|xobjects
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|resources
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|XOBJECT
argument_list|,
name|xobjects
argument_list|)
expr_stmt|;
block|}
name|Map
name|actuals
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|COSDictionaryMap
argument_list|(
name|actuals
argument_list|,
name|xobjects
argument_list|)
expr_stmt|;
for|for
control|(
name|COSName
name|objName
range|:
name|xobjects
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|cosObject
init|=
name|xobjects
operator|.
name|getDictionaryObject
argument_list|(
name|objName
argument_list|)
decl_stmt|;
name|PDXObject
name|xobject
init|=
name|PDXObject
operator|.
name|createXObject
argument_list|(
name|cosObject
argument_list|)
decl_stmt|;
if|if
condition|(
name|xobject
operator|!=
literal|null
condition|)
block|{
name|actuals
operator|.
name|put
argument_list|(
name|objName
operator|.
name|getName
argument_list|()
argument_list|,
name|xobject
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the map of images.  An empty map will be returned if there      * are no underlying images.      * So far the keys are COSName of the image      * and the value is the corresponding PDXObjectImage.      *      * @author By BM      * @return The map of images.      * @throws IOException If there is an error writing the picture.      */
specifier|public
name|Map
name|getImages
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|images
init|=
operator|(
name|COSDictionary
operator|)
name|resources
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|XOBJECT
argument_list|)
decl_stmt|;
if|if
condition|(
name|images
operator|==
literal|null
condition|)
block|{
name|images
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|resources
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|XOBJECT
argument_list|,
name|images
argument_list|)
expr_stmt|;
block|}
name|Map
name|actuals
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|COSDictionaryMap
argument_list|(
name|actuals
argument_list|,
name|images
argument_list|)
expr_stmt|;
for|for
control|(
name|COSName
name|imageName
range|:
name|images
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSStream
name|image
init|=
call|(
name|COSStream
call|)
argument_list|(
name|images
operator|.
name|getDictionaryObject
argument_list|(
name|imageName
argument_list|)
argument_list|)
decl_stmt|;
name|COSName
name|subType
init|=
operator|(
name|COSName
operator|)
name|image
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|subType
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|IMAGE
argument_list|)
condition|)
block|{
name|PDXObjectImage
name|ximage
init|=
operator|(
name|PDXObjectImage
operator|)
name|PDXObject
operator|.
name|createXObject
argument_list|(
name|image
argument_list|)
decl_stmt|;
if|if
condition|(
name|ximage
operator|!=
literal|null
condition|)
block|{
name|actuals
operator|.
name|put
argument_list|(
name|imageName
operator|.
name|getName
argument_list|()
argument_list|,
name|ximage
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the map of fonts.      *      * @param fonts The new map of fonts.      */
specifier|public
name|void
name|setFonts
parameter_list|(
name|Map
name|fonts
parameter_list|)
block|{
name|resources
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FONT
argument_list|,
name|COSDictionaryMap
operator|.
name|convert
argument_list|(
name|fonts
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the map of colorspaces.  This will return null if the underlying      * resources dictionary does not have a colorspace dictionary.  The keys are string      * and the values are PDColorSpace objects.      *      * @return The map of colorspaces.      *      * @throws IOException If there is an error getting the colorspaces.      */
specifier|public
name|Map
name|getColorSpaces
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|colorspaces
init|=
operator|(
name|COSDictionary
operator|)
name|resources
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ColorSpace"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|colorspaces
operator|!=
literal|null
condition|)
block|{
name|Map
name|actuals
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|COSDictionaryMap
argument_list|(
name|actuals
argument_list|,
name|colorspaces
argument_list|)
expr_stmt|;
for|for
control|(
name|COSName
name|csName
range|:
name|colorspaces
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|cs
init|=
name|colorspaces
operator|.
name|getDictionaryObject
argument_list|(
name|csName
argument_list|)
decl_stmt|;
name|actuals
operator|.
name|put
argument_list|(
name|csName
operator|.
name|getName
argument_list|()
argument_list|,
name|PDColorSpaceFactory
operator|.
name|createColorSpace
argument_list|(
name|cs
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the map of colorspaces.      *      * @param colorspaces The new map of colorspaces.      */
specifier|public
name|void
name|setColorSpaces
parameter_list|(
name|Map
name|colorspaces
parameter_list|)
block|{
name|resources
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|COLORSPACE
argument_list|,
name|COSDictionaryMap
operator|.
name|convert
argument_list|(
name|colorspaces
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the map of graphic states.  This will return null if the underlying      * resources dictionary does not have a graphics dictionary.  The keys are the graphic state      * name as a String and the values are PDExtendedGraphicsState objects.      *      * @return The map of extended graphic state objects.      */
specifier|public
name|Map
name|getGraphicsStates
parameter_list|()
block|{
name|Map
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|states
init|=
operator|(
name|COSDictionary
operator|)
name|resources
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|EXT_G_STATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|states
operator|!=
literal|null
condition|)
block|{
name|Map
name|actuals
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|COSDictionaryMap
argument_list|(
name|actuals
argument_list|,
name|states
argument_list|)
expr_stmt|;
for|for
control|(
name|COSName
name|name
range|:
name|states
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSDictionary
name|dictionary
init|=
operator|(
name|COSDictionary
operator|)
name|states
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|actuals
operator|.
name|put
argument_list|(
name|name
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|PDExtendedGraphicsState
argument_list|(
name|dictionary
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the map of graphics states.      *      * @param states The new map of states.      */
specifier|public
name|void
name|setGraphicsStates
parameter_list|(
name|Map
name|states
parameter_list|)
block|{
name|Iterator
name|iter
init|=
name|states
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|COSDictionary
name|dic
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|name
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|PDExtendedGraphicsState
name|state
init|=
operator|(
name|PDExtendedGraphicsState
operator|)
name|states
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|dic
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|name
argument_list|)
argument_list|,
name|state
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resources
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|EXT_G_STATE
argument_list|,
name|dic
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the dictionary mapping resource names to property list dictionaries for marked      * content.      * @return the property list      */
specifier|public
name|PDPropertyList
name|getProperties
parameter_list|()
block|{
name|PDPropertyList
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|props
init|=
operator|(
name|COSDictionary
operator|)
name|resources
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PROPERTIES
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDPropertyList
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Sets the dictionary mapping resource names to property list dictionaries for marked      * content.      * @param props the property list      */
specifier|public
name|void
name|setProperties
parameter_list|(
name|PDPropertyList
name|props
parameter_list|)
block|{
name|resources
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PROPERTIES
argument_list|,
name|props
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

