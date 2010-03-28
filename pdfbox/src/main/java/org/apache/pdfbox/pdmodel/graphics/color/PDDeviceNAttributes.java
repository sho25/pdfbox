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

begin_comment
comment|/**  * This class represents attributes for a DeviceN color space.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PDDeviceNAttributes
block|{
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDDeviceNAttributes
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param attributes A dictionary that has all of the attributes.      */
specifier|public
name|PDDeviceNAttributes
parameter_list|(
name|COSDictionary
name|attributes
parameter_list|)
block|{
name|dictionary
operator|=
name|attributes
expr_stmt|;
block|}
comment|/**      * This will get the underlying cos dictionary.      *      * @return The dictionary that this object wraps.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * This will get a map of colorants.  See the PDF Reference for more details about      * this attribute.  The map will contain a java.lang.String as the key, a colorant name,      * and a PDColorSpace as the value.      *      * @return The colorant map.      *      * @throws IOException If there is an error getting the colorspaces.      */
specifier|public
name|Map
name|getColorants
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
name|actuals
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|COSDictionary
name|colorants
init|=
operator|(
name|COSDictionary
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|COLORANTS
argument_list|)
decl_stmt|;
if|if
condition|(
name|colorants
operator|==
literal|null
condition|)
block|{
name|colorants
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|COLORANTS
argument_list|,
name|colorants
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|COSName
name|name
range|:
name|colorants
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|value
init|=
name|colorants
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
name|PDColorSpaceFactory
operator|.
name|createColorSpace
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|COSDictionaryMap
argument_list|(
name|actuals
argument_list|,
name|colorants
argument_list|)
return|;
block|}
comment|/**      * This will replace the existing colorant attribute.  The key should be strings      * and the values should be PDColorSpaces.      *      * @param colorants The map of colorants.      */
specifier|public
name|void
name|setColorants
parameter_list|(
name|Map
name|colorants
parameter_list|)
block|{
name|COSDictionary
name|colorantDict
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|colorants
operator|!=
literal|null
condition|)
block|{
name|colorantDict
operator|=
name|COSDictionaryMap
operator|.
name|convert
argument_list|(
name|colorants
argument_list|)
expr_stmt|;
block|}
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|COLORANTS
argument_list|,
name|colorantDict
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

