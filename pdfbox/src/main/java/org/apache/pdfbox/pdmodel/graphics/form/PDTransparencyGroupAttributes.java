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
name|form
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
name|graphics
operator|.
name|color
operator|.
name|PDColorSpace
import|;
end_import

begin_comment
comment|/**  * Transparency group attributes.  *   * @author Kühn&amp; Weyh Software GmbH  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDTransparencyGroupAttributes
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
specifier|private
name|PDColorSpace
name|colorSpace
decl_stmt|;
comment|/**      * Creates a group object with /Transparency subtype entry.      */
specifier|public
name|PDTransparencyGroupAttributes
parameter_list|()
block|{
name|dictionary
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
name|S
argument_list|,
name|COSName
operator|.
name|TRANSPARENCY
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a group object from a given dictionary      * @param dic {@link COSDictionary} object      */
specifier|public
name|PDTransparencyGroupAttributes
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
name|dictionary
operator|=
name|dic
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * Returns the group color space or null if it isn't defined.      *      * @return the group color space.      * @throws IOException      */
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
comment|/**      * Returns the group color space or null if it isn't defined.      *      * @param resources useful for its cache. Can be null.      * @return the group color space.      * @throws IOException      */
specifier|public
name|PDColorSpace
name|getColorSpace
parameter_list|(
name|PDResources
name|resources
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|colorSpace
operator|==
literal|null
operator|&&
name|getCOSObject
argument_list|()
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|CS
argument_list|)
condition|)
block|{
name|colorSpace
operator|=
name|PDColorSpace
operator|.
name|create
argument_list|(
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CS
argument_list|)
argument_list|,
name|resources
argument_list|)
expr_stmt|;
block|}
return|return
name|colorSpace
return|;
block|}
comment|/**      * Returns true if this group is isolated. Isolated groups begin with the fully transparent      * image, non-isolated begin with the current backdrop.      */
specifier|public
name|boolean
name|isIsolated
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|I
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Returns true if this group is a knockout. A knockout group blends with original backdrop,      * a non-knockout group blends with the current backdrop.      */
specifier|public
name|boolean
name|isKnockout
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|K
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

