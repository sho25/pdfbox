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
name|interactive
operator|.
name|measurement
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

begin_comment
comment|/**  * This class represents a viewport dictionary.  *   */
end_comment

begin_class
specifier|public
class|class
name|PDViewportDictionary
implements|implements
name|COSObjectable
block|{
comment|/**      * The type of this object.      */
specifier|public
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"Viewport"
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|viewportDictionary
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDViewportDictionary
parameter_list|()
block|{
name|this
operator|.
name|viewportDictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param dictionary the dictionary      */
specifier|public
name|PDViewportDictionary
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|viewportDictionary
operator|=
name|dictionary
expr_stmt|;
block|}
comment|/**      * This will return the corresponding dictionary.      *       * @return the viewport dictionary      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|this
operator|.
name|viewportDictionary
return|;
block|}
comment|/**      * Returns the type of the viewport dictionary.      * It must be "Viewport"      * @return the type of the external data dictionary      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|TYPE
return|;
block|}
comment|/**      * This will retrieve the rectangle specifying the location of the viewport.      *       * @return the location      */
specifier|public
name|PDRectangle
name|getBBox
parameter_list|()
block|{
name|COSBase
name|bbox
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
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
name|bbox
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|new
name|PDRectangle
argument_list|(
operator|(
name|COSArray
operator|)
name|bbox
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the rectangle specifying the location of the viewport.      *       * @param rectangle the rectangle specifying the location.      */
specifier|public
name|void
name|setBBox
parameter_list|(
name|PDRectangle
name|rectangle
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BBOX
argument_list|,
name|rectangle
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the name of the viewport.      *       * @return the name of the viewport      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
return|;
block|}
comment|/**     * This will set the name of the viewport.     *       * @param name the name of the viewport     */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the measure dictionary.      *       * @return the measure dictionary      */
specifier|public
name|PDMeasureDictionary
name|getMeasure
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|MEASURE
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|new
name|PDMeasureDictionary
argument_list|(
operator|(
name|COSDictionary
operator|)
name|base
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the measure dictionary.      *       * @param measure the measure dictionary      */
specifier|public
name|void
name|setMeasure
parameter_list|(
name|PDMeasureDictionary
name|measure
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|MEASURE
argument_list|,
name|measure
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

