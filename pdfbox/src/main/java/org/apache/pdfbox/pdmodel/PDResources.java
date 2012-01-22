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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|PDFont
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
operator|.
name|PDPatternResources
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
name|shading
operator|.
name|PDShadingResources
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
name|MapUtil
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
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|fonts
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|PDFont
argument_list|,
name|String
argument_list|>
name|fontMappings
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PDColorSpace
argument_list|>
name|colorspaces
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
name|xobjects
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|PDXObject
argument_list|,
name|String
argument_list|>
name|xobjectMappings
init|=
literal|null
decl_stmt|;
specifier|private
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDXObjectImage
argument_list|>
name|images
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PDExtendedGraphicsState
argument_list|>
name|graphicsStates
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PDPatternResources
argument_list|>
name|patterns
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|PDShadingResources
argument_list|>
name|shadings
init|=
literal|null
decl_stmt|;
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDResources
operator|.
name|class
argument_list|)
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
comment|/**      * Calling this will release all cached information.      *       */
specifier|public
name|void
name|clear
parameter_list|()
block|{
if|if
condition|(
name|fonts
operator|!=
literal|null
condition|)
block|{
name|fonts
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|colorspaces
operator|!=
literal|null
condition|)
block|{
name|colorspaces
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|xobjects
operator|!=
literal|null
condition|)
block|{
name|xobjects
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|images
operator|!=
literal|null
condition|)
block|{
name|images
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|graphicsStates
operator|!=
literal|null
condition|)
block|{
name|graphicsStates
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|patterns
operator|!=
literal|null
condition|)
block|{
name|patterns
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|shadings
operator|!=
literal|null
condition|)
block|{
name|shadings
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|resources
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * This will get the map of fonts.  This will never return null.  The keys are string      * and the values are PDFont objects.      *      * @param fontCache A map of existing PDFont objects to reuse.      * @return The map of fonts.      *      * @throws IOException If there is an error getting the fonts.      *       * @deprecated due to some side effects font caching is no longer supported, use {@link #getFonts()} instead      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|getFonts
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|fontCache
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getFonts
argument_list|()
return|;
block|}
comment|/**      * This will get the map of fonts. This will never return null.      *      * @return The map of fonts.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|getFonts
parameter_list|()
block|{
if|if
condition|(
name|fonts
operator|==
literal|null
condition|)
block|{
comment|// at least an empty map will be returned
comment|// TODO we should return null instead of an empty map
name|fonts
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
argument_list|()
expr_stmt|;
name|COSDictionary
name|fontsDictionary
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
name|fontsDictionary
operator|==
literal|null
condition|)
block|{
name|fontsDictionary
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
name|fontsDictionary
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|COSName
name|fontName
range|:
name|fontsDictionary
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|font
init|=
name|fontsDictionary
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
name|PDFont
name|newFont
init|=
literal|null
decl_stmt|;
try|try
block|{
name|newFont
operator|=
name|PDFontFactory
operator|.
name|createFont
argument_list|(
operator|(
name|COSDictionary
operator|)
name|font
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"error while creating a font"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|newFont
operator|!=
literal|null
condition|)
block|{
name|fonts
operator|.
name|put
argument_list|(
name|fontName
operator|.
name|getName
argument_list|()
argument_list|,
name|newFont
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|fonts
return|;
block|}
comment|/**      * This will get the map of PDXObjects that are in the resource dictionary.      * This will never return null.      *      * @return The map of xobjects.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
name|getXObjects
parameter_list|()
block|{
if|if
condition|(
name|xobjects
operator|==
literal|null
condition|)
block|{
comment|// at least an empty map will be returned
comment|// TODO we should return null instead of an empty map
name|xobjects
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
argument_list|()
expr_stmt|;
name|COSDictionary
name|xobjectsDictionary
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
name|xobjectsDictionary
operator|==
literal|null
condition|)
block|{
name|xobjectsDictionary
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
name|xobjectsDictionary
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xobjects
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|COSName
name|objName
range|:
name|xobjectsDictionary
operator|.
name|keySet
argument_list|()
control|)
block|{
name|PDXObject
name|xobject
init|=
literal|null
decl_stmt|;
try|try
block|{
name|xobject
operator|=
name|PDXObject
operator|.
name|createXObject
argument_list|(
name|xobjectsDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|objName
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"error while creating a xobject"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xobject
operator|!=
literal|null
condition|)
block|{
name|xobjects
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
block|}
block|}
return|return
name|xobjects
return|;
block|}
comment|/**      * This will get the map of images.  An empty map will be returned if there      * are no underlying images.      * So far the keys are COSName of the image      * and the value is the corresponding PDXObjectImage.      *      * @author By BM      * @return The map of images.      * @throws IOException If there is an error writing the picture.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObjectImage
argument_list|>
name|getImages
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|images
operator|==
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
name|allXObjects
init|=
name|getXObjects
argument_list|()
decl_stmt|;
name|images
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDXObjectImage
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|imageName
range|:
name|allXObjects
operator|.
name|keySet
argument_list|()
control|)
block|{
name|PDXObject
name|xobject
init|=
name|images
operator|.
name|get
argument_list|(
name|imageName
argument_list|)
decl_stmt|;
if|if
condition|(
name|xobject
operator|instanceof
name|PDXObjectImage
condition|)
block|{
name|images
operator|.
name|put
argument_list|(
name|imageName
argument_list|,
operator|(
name|PDXObjectImage
operator|)
name|xobject
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|images
return|;
block|}
comment|/**      * This will set the map of fonts.      *      * @param fontsValue The new map of fonts.      */
specifier|public
name|void
name|setFonts
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
name|fontsValue
parameter_list|)
block|{
name|fonts
operator|=
name|fontsValue
expr_stmt|;
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
name|fontsValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the map of xobjects.      *      * @param xobjectsValue The new map of xobjects.      */
specifier|public
name|void
name|setXObjects
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
name|xobjectsValue
parameter_list|)
block|{
name|xobjects
operator|=
name|xobjectsValue
expr_stmt|;
name|resources
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|XOBJECT
argument_list|,
name|COSDictionaryMap
operator|.
name|convert
argument_list|(
name|xobjectsValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the map of colorspaces.  This will return null if the underlying      * resources dictionary does not have a colorspace dictionary.  The keys are string      * and the values are PDColorSpace objects.      *      * @return The map of colorspaces.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDColorSpace
argument_list|>
name|getColorSpaces
parameter_list|()
block|{
if|if
condition|(
name|colorspaces
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|csDictionary
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
name|COLORSPACE
argument_list|)
decl_stmt|;
if|if
condition|(
name|csDictionary
operator|!=
literal|null
condition|)
block|{
name|colorspaces
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDColorSpace
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|COSName
name|csName
range|:
name|csDictionary
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|cs
init|=
name|csDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|csName
argument_list|)
decl_stmt|;
name|PDColorSpace
name|colorspace
init|=
literal|null
decl_stmt|;
try|try
block|{
name|colorspace
operator|=
name|PDColorSpaceFactory
operator|.
name|createColorSpace
argument_list|(
name|cs
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"error while creating a colorspace"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|colorspace
operator|!=
literal|null
condition|)
block|{
name|colorspaces
operator|.
name|put
argument_list|(
name|csName
operator|.
name|getName
argument_list|()
argument_list|,
name|colorspace
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|colorspaces
return|;
block|}
comment|/**      * This will set the map of colorspaces.      *      * @param csValue The new map of colorspaces.      */
specifier|public
name|void
name|setColorSpaces
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDColorSpace
argument_list|>
name|csValue
parameter_list|)
block|{
name|colorspaces
operator|=
name|csValue
expr_stmt|;
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
name|csValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the map of graphic states.  This will return null if the underlying      * resources dictionary does not have a graphics dictionary.  The keys are the graphic state      * name as a String and the values are PDExtendedGraphicsState objects.      *      * @return The map of extended graphic state objects.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDExtendedGraphicsState
argument_list|>
name|getGraphicsStates
parameter_list|()
block|{
if|if
condition|(
name|graphicsStates
operator|==
literal|null
condition|)
block|{
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
name|graphicsStates
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDExtendedGraphicsState
argument_list|>
argument_list|()
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
name|graphicsStates
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
block|}
return|return
name|graphicsStates
return|;
block|}
comment|/**      * This will set the map of graphics states.      *      * @param states The new map of states.      */
specifier|public
name|void
name|setGraphicsStates
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDExtendedGraphicsState
argument_list|>
name|states
parameter_list|)
block|{
name|graphicsStates
operator|=
name|states
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
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
comment|/**      * This will get the map of patterns.  This will return null if the underlying      * resources dictionary does not have a patterns dictionary. The keys are the pattern      * name as a String and the values are PDPatternResources objects.      *      * @return The map of pattern resources objects.      *       * @throws IOException If there is an error getting the pattern resources.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDPatternResources
argument_list|>
name|getPatterns
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|patterns
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|patternsDictionary
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
name|PATTERN
argument_list|)
decl_stmt|;
if|if
condition|(
name|patternsDictionary
operator|!=
literal|null
condition|)
block|{
name|patterns
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDPatternResources
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|COSName
name|name
range|:
name|patternsDictionary
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
name|patternsDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|patterns
operator|.
name|put
argument_list|(
name|name
operator|.
name|getName
argument_list|()
argument_list|,
name|PDPatternResources
operator|.
name|create
argument_list|(
name|dictionary
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|patterns
return|;
block|}
comment|/**      * This will set the map of patterns.      *      * @param patternsValue The new map of patterns.      */
specifier|public
name|void
name|setPatterns
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDPatternResources
argument_list|>
name|patternsValue
parameter_list|)
block|{
name|patterns
operator|=
name|patternsValue
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iter
init|=
name|patternsValue
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
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|PDPatternResources
name|pattern
init|=
name|patternsValue
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
name|pattern
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
name|PATTERN
argument_list|,
name|dic
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the map of shadings.  This will return null if the underlying      * resources dictionary does not have a shading dictionary. The keys are the shading      * name as a String and the values are PDShadingResources objects.      *      * @return The map of shading resources objects.      *       * @throws IOException If there is an error getting the shading resources.      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDShadingResources
argument_list|>
name|getShadings
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|shadings
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|shadingsDictionary
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
name|SHADING
argument_list|)
decl_stmt|;
if|if
condition|(
name|shadingsDictionary
operator|!=
literal|null
condition|)
block|{
name|shadings
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDShadingResources
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|COSName
name|name
range|:
name|shadingsDictionary
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
name|shadingsDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|shadings
operator|.
name|put
argument_list|(
name|name
operator|.
name|getName
argument_list|()
argument_list|,
name|PDShadingResources
operator|.
name|create
argument_list|(
name|dictionary
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|shadings
return|;
block|}
comment|/**      * This will set the map of shadings.      *      * @param shadingsValue The new map of shadings.      */
specifier|public
name|void
name|setShadings
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDShadingResources
argument_list|>
name|shadingsValue
parameter_list|)
block|{
name|shadings
operator|=
name|shadingsValue
expr_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|iter
init|=
name|shadingsValue
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
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|PDShadingResources
name|shading
init|=
name|shadingsValue
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
name|shading
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
name|SHADING
argument_list|,
name|dic
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds the given font to the resources of the current the page.      *       * @param font the font to be added      * @return the font name to be used within the content stream.      */
specifier|public
name|String
name|addFont
parameter_list|(
name|PDFont
name|font
parameter_list|)
block|{
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
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDFont
argument_list|>
argument_list|()
expr_stmt|;
name|fontMappings
operator|=
name|reverseMap
argument_list|(
name|fonts
argument_list|,
name|PDFont
operator|.
name|class
argument_list|)
expr_stmt|;
name|setFonts
argument_list|(
name|fonts
argument_list|)
expr_stmt|;
block|}
name|String
name|fontMapping
init|=
name|fontMappings
operator|.
name|get
argument_list|(
name|font
argument_list|)
decl_stmt|;
if|if
condition|(
name|fontMapping
operator|==
literal|null
condition|)
block|{
name|fontMapping
operator|=
name|MapUtil
operator|.
name|getNextUniqueKey
argument_list|(
name|fonts
argument_list|,
literal|"F"
argument_list|)
expr_stmt|;
name|fontMappings
operator|.
name|put
argument_list|(
name|font
argument_list|,
name|fontMapping
argument_list|)
expr_stmt|;
name|fonts
operator|.
name|put
argument_list|(
name|fontMapping
argument_list|,
name|font
argument_list|)
expr_stmt|;
name|addFontToDictionary
argument_list|(
name|font
argument_list|,
name|fontMapping
argument_list|)
expr_stmt|;
block|}
return|return
name|fontMapping
return|;
block|}
specifier|private
name|void
name|addFontToDictionary
parameter_list|(
name|PDFont
name|font
parameter_list|,
name|String
name|fontName
parameter_list|)
block|{
name|COSDictionary
name|fontsDictionary
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
name|fontsDictionary
operator|.
name|setItem
argument_list|(
name|fontName
argument_list|,
name|font
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds the given XObject to the resources of the current the page.      *       * @param xobject the XObject to be added      * @param prefix the prefix to be used for the name      *       * @return the XObject name to be used within the content stream.      */
specifier|public
name|String
name|addXObject
parameter_list|(
name|PDXObject
name|xobject
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
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
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
argument_list|()
expr_stmt|;
name|xobjectMappings
operator|=
name|reverseMap
argument_list|(
name|xobjects
argument_list|,
name|PDXObject
operator|.
name|class
argument_list|)
expr_stmt|;
name|setXObjects
argument_list|(
name|xobjects
argument_list|)
expr_stmt|;
block|}
name|String
name|objMapping
init|=
name|xobjectMappings
operator|.
name|get
argument_list|(
name|xobject
argument_list|)
decl_stmt|;
if|if
condition|(
name|objMapping
operator|==
literal|null
condition|)
block|{
name|objMapping
operator|=
name|MapUtil
operator|.
name|getNextUniqueKey
argument_list|(
name|xobjects
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
name|xobjectMappings
operator|.
name|put
argument_list|(
name|xobject
argument_list|,
name|objMapping
argument_list|)
expr_stmt|;
name|xobjects
operator|.
name|put
argument_list|(
name|objMapping
argument_list|,
name|xobject
argument_list|)
expr_stmt|;
name|addXObjectToDictionary
argument_list|(
name|xobject
argument_list|,
name|objMapping
argument_list|)
expr_stmt|;
block|}
return|return
name|objMapping
return|;
block|}
specifier|private
name|void
name|addXObjectToDictionary
parameter_list|(
name|PDXObject
name|xobject
parameter_list|,
name|String
name|xobjectName
parameter_list|)
block|{
name|COSDictionary
name|fontsDictionary
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
name|fontsDictionary
operator|.
name|setItem
argument_list|(
name|xobjectName
argument_list|,
name|xobject
argument_list|)
expr_stmt|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
name|reverseMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|map
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|keyClass
parameter_list|)
block|{
name|Map
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
name|reversed
init|=
operator|new
name|java
operator|.
name|util
operator|.
name|HashMap
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|reversed
operator|.
name|put
argument_list|(
name|keyClass
operator|.
name|cast
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|,
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|reversed
return|;
block|}
block|}
end_class

end_unit

