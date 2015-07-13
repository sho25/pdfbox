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
name|tools
operator|.
name|pdfdebugger
operator|.
name|streampane
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|PDResources
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
name|PDStream
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
name|image
operator|.
name|PDImageXObject
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  *  * A class that provides the COSStream in different version and related informations.  */
end_comment

begin_class
class|class
name|Stream
block|{
specifier|public
specifier|static
specifier|final
name|String
name|UNFILTERED
init|=
literal|"unfiltered"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILTERED
init|=
literal|"Filtered"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|IMAGE
init|=
literal|"Image"
decl_stmt|;
specifier|private
specifier|final
name|COSStream
name|stream
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|isImage
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|filters
decl_stmt|;
comment|/**      * Constructor.      * @param cosStream COSStream instance.      */
name|Stream
parameter_list|(
name|COSStream
name|cosStream
parameter_list|)
block|{
name|this
operator|.
name|stream
operator|=
name|cosStream
expr_stmt|;
name|this
operator|.
name|isImage
operator|=
name|isImageStream
argument_list|(
name|cosStream
argument_list|)
expr_stmt|;
name|filters
operator|=
name|createFilterList
argument_list|(
name|cosStream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Return if this is stream is an Image XObject.      * @return true if this an image and false otherwise.      */
specifier|public
name|boolean
name|isImage
parameter_list|()
block|{
return|return
name|isImage
return|;
block|}
comment|/**      * Return the available filter list. Only "Unfiltered" is returned if there is no filter and in case of      * XObject image type stream "Image" is also included in the list.      * @return An array of String.      */
specifier|public
name|String
index|[]
name|getFilterList
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
name|filters
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|String
index|[]
name|filterArray
init|=
operator|new
name|String
index|[
name|set
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
return|return
name|set
operator|.
name|toArray
argument_list|(
name|filterArray
argument_list|)
return|;
block|}
comment|/**      * Returns a InputStream of a partially filtered stream.      *      * @param key is an instance of String which tells which version of stream should be returned.      * @return an InputStream.      */
specifier|public
name|InputStream
name|getStream
parameter_list|(
name|String
name|key
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|UNFILTERED
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|stream
operator|.
name|getUnfilteredStream
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|FILTERED
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|stream
operator|.
name|getFilteredStream
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|PDStream
argument_list|(
name|stream
argument_list|)
operator|.
name|getPartiallyFilteredStream
argument_list|(
name|filters
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Provide the image for stream. The stream must be image XObject.      * @param resources PDResources for the XObject.      * @return A BufferedImage.      */
specifier|public
name|BufferedImage
name|getImage
parameter_list|(
name|PDResources
name|resources
parameter_list|)
block|{
try|try
block|{
name|PDImageXObject
name|imageXObject
init|=
operator|new
name|PDImageXObject
argument_list|(
operator|new
name|PDStream
argument_list|(
name|stream
argument_list|)
argument_list|,
name|resources
argument_list|)
decl_stmt|;
return|return
name|imageXObject
operator|.
name|getImage
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|createFilterList
parameter_list|(
name|COSStream
name|stream
parameter_list|)
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|filterList
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|filterList
operator|.
name|put
argument_list|(
name|UNFILTERED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|PDStream
name|pdStream
init|=
operator|new
name|PDStream
argument_list|(
name|stream
argument_list|)
decl_stmt|;
if|if
condition|(
name|pdStream
operator|.
name|getFilters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|filtersSize
init|=
name|pdStream
operator|.
name|getFilters
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|filtersSize
operator|-
literal|1
init|;
name|i
operator|>=
literal|1
condition|;
name|i
operator|--
control|)
block|{
name|filterList
operator|.
name|put
argument_list|(
name|getPartialStreamCommand
argument_list|(
name|i
argument_list|)
argument_list|,
name|getStopFilterList
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|filterList
operator|.
name|put
argument_list|(
name|FILTERED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isImage
condition|)
block|{
name|filterList
operator|.
name|put
argument_list|(
name|IMAGE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|filterList
return|;
block|}
specifier|private
name|String
name|getPartialStreamCommand
parameter_list|(
specifier|final
name|int
name|indexOfStopFilter
parameter_list|)
block|{
name|List
argument_list|<
name|COSName
argument_list|>
name|avaiablrFilters
init|=
operator|new
name|PDStream
argument_list|(
name|stream
argument_list|)
operator|.
name|getFilters
argument_list|()
decl_stmt|;
name|StringBuilder
name|nameListBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|indexOfStopFilter
init|;
name|i
operator|<
name|avaiablrFilters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|nameListBuilder
operator|.
name|append
argument_list|(
name|avaiablrFilters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"& "
argument_list|)
expr_stmt|;
block|}
name|nameListBuilder
operator|.
name|delete
argument_list|(
name|nameListBuilder
operator|.
name|lastIndexOf
argument_list|(
literal|"&"
argument_list|)
argument_list|,
name|nameListBuilder
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|"Keep "
operator|+
name|nameListBuilder
operator|.
name|toString
argument_list|()
operator|+
literal|"..."
return|;
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getStopFilterList
parameter_list|(
specifier|final
name|int
name|stopFilterIndex
parameter_list|)
block|{
name|List
argument_list|<
name|COSName
argument_list|>
name|avaiablrFilters
init|=
operator|new
name|PDStream
argument_list|(
name|stream
argument_list|)
operator|.
name|getFilters
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|stopFilters
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|stopFilters
operator|.
name|add
argument_list|(
name|avaiablrFilters
operator|.
name|get
argument_list|(
name|stopFilterIndex
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|stopFilters
return|;
block|}
specifier|private
name|boolean
name|isImageStream
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
return|return
name|dic
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
operator|&&
name|dic
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|IMAGE
argument_list|)
return|;
block|}
block|}
end_class

end_unit

