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
name|annotation
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
name|PDGamma
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
name|PDXObjectForm
import|;
end_import

begin_comment
comment|/**  * This class represents an appearance characteristics dictionary.  *  * @version $Revision: 1.0 $   *  */
end_comment

begin_class
specifier|public
class|class
name|PDAppearanceCharacteristicsDictionary
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor.      *       * @param dict dictionary      */
specifier|public
name|PDAppearanceCharacteristicsDictionary
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|this
operator|.
name|dictionary
operator|=
name|dict
expr_stmt|;
block|}
comment|/**      * returns the dictionary.      * @return the dictionary      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|this
operator|.
name|dictionary
return|;
block|}
comment|/**      * {@inheritDoc}      *       */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|this
operator|.
name|dictionary
return|;
block|}
comment|/**      * This will retrieve the rotation of the annotation widget.      * It must be a multiple of 90. Default is 0       * @return the rotation      */
specifier|public
name|int
name|getRotation
parameter_list|()
block|{
return|return
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|R
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the rotation.      *       * @param rotation the rotation as a multiple of 90      */
specifier|public
name|void
name|setRotation
parameter_list|(
name|int
name|rotation
parameter_list|)
block|{
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|R
argument_list|,
name|rotation
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border color.      *       * @return the border color.      */
specifier|public
name|PDGamma
name|getBorderColour
parameter_list|()
block|{
name|COSBase
name|c
init|=
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"BC"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|new
name|PDGamma
argument_list|(
operator|(
name|COSArray
operator|)
name|c
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the border color.      *       * @param c the border color      */
specifier|public
name|void
name|setBorderColour
parameter_list|(
name|PDGamma
name|c
parameter_list|)
block|{
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"BC"
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the background color.      *       * @return the background color.      */
specifier|public
name|PDGamma
name|getBackground
parameter_list|()
block|{
name|COSBase
name|c
init|=
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"BG"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|new
name|PDGamma
argument_list|(
operator|(
name|COSArray
operator|)
name|c
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the background color.      *       * @param c the background color      */
specifier|public
name|void
name|setBackground
parameter_list|(
name|PDGamma
name|c
parameter_list|)
block|{
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"BG"
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the normal caption.      *       * @return the normal caption.      */
specifier|public
name|String
name|getNormalCaption
parameter_list|()
block|{
return|return
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getString
argument_list|(
literal|"CA"
argument_list|)
return|;
block|}
comment|/**      * This will set the normal caption.      *       * @param caption the normal caption      */
specifier|public
name|void
name|setNormalCaption
parameter_list|(
name|String
name|caption
parameter_list|)
block|{
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
literal|"CA"
argument_list|,
name|caption
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the rollover caption.      *       * @return the rollover caption.      */
specifier|public
name|String
name|getRolloverCaption
parameter_list|()
block|{
return|return
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getString
argument_list|(
literal|"RC"
argument_list|)
return|;
block|}
comment|/**      * This will set the rollover caption.      *       * @param caption the rollover caption      */
specifier|public
name|void
name|setRolloverCaption
parameter_list|(
name|String
name|caption
parameter_list|)
block|{
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
literal|"RC"
argument_list|,
name|caption
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the alternate caption.      *       * @return the alternate caption.      */
specifier|public
name|String
name|getAlternateCaption
parameter_list|()
block|{
return|return
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getString
argument_list|(
literal|"AC"
argument_list|)
return|;
block|}
comment|/**      * This will set the alternate caption.      *       * @param caption the alternate caption      */
specifier|public
name|void
name|setAlternateCaption
parameter_list|(
name|String
name|caption
parameter_list|)
block|{
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|setString
argument_list|(
literal|"AC"
argument_list|,
name|caption
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the normal icon.      *       * @return the normal icon.      */
specifier|public
name|PDXObjectForm
name|getNormalIcon
parameter_list|()
block|{
name|COSBase
name|i
init|=
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"I"
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|instanceof
name|COSStream
condition|)
block|{
return|return
operator|new
name|PDXObjectForm
argument_list|(
operator|(
name|COSStream
operator|)
name|i
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will retrieve the rollover icon.      *       * @return the rollover icon      */
specifier|public
name|PDXObjectForm
name|getRolloverIcon
parameter_list|()
block|{
name|COSBase
name|i
init|=
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"RI"
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|instanceof
name|COSStream
condition|)
block|{
return|return
operator|new
name|PDXObjectForm
argument_list|(
operator|(
name|COSStream
operator|)
name|i
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will retrieve the alternate icon.      *       * @return the alternate icon.      */
specifier|public
name|PDXObjectForm
name|getAlternateIcon
parameter_list|()
block|{
name|COSBase
name|i
init|=
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"IX"
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|instanceof
name|COSStream
condition|)
block|{
return|return
operator|new
name|PDXObjectForm
argument_list|(
operator|(
name|COSStream
operator|)
name|i
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

