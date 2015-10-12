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
name|state
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
name|form
operator|.
name|PDFormXObject
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
name|form
operator|.
name|PDTransparencyGroup
import|;
end_import

begin_comment
comment|/**  * Soft mask.  *  * @author Kühn& Weyh Software, GmbH  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDSoftMask
implements|implements
name|COSObjectable
block|{
comment|/**      * Creates a new soft mask.      *      * @param dictionary SMask      */
specifier|public
specifier|static
name|PDSoftMask
name|create
parameter_list|(
name|COSBase
name|dictionary
parameter_list|)
block|{
if|if
condition|(
name|dictionary
operator|instanceof
name|COSName
condition|)
block|{
if|if
condition|(
name|COSName
operator|.
name|NONE
operator|.
name|equals
argument_list|(
name|dictionary
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Invalid SMask "
operator|+
name|dictionary
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|dictionary
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|new
name|PDSoftMask
argument_list|(
operator|(
name|COSDictionary
operator|)
name|dictionary
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Invalid SMask "
operator|+
name|dictionary
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
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
name|PDSoftMask
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
specifier|private
name|COSName
name|subType
init|=
literal|null
decl_stmt|;
specifier|private
name|PDTransparencyGroup
name|group
init|=
literal|null
decl_stmt|;
specifier|private
name|COSArray
name|backdropColor
init|=
literal|null
decl_stmt|;
specifier|private
name|PDFunction
name|transferFunction
init|=
literal|null
decl_stmt|;
comment|/**      * Creates a new soft mask.      */
specifier|public
name|PDSoftMask
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|dictionary
operator|=
name|dictionary
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
comment|/**      * Returns the subtype of the soft mask (Alpha, Luminosity) - S entry      */
specifier|public
name|COSName
name|getSubType
parameter_list|()
block|{
if|if
condition|(
name|subType
operator|==
literal|null
condition|)
block|{
name|subType
operator|=
operator|(
name|COSName
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|S
argument_list|)
expr_stmt|;
block|}
return|return
name|subType
return|;
block|}
comment|/**      * Returns the G entry of the soft mask object      *       * @return form containing the transparency group      * @throws IOException      */
specifier|public
name|PDTransparencyGroup
name|getGroup
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
name|COSBase
name|cosGroup
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|G
argument_list|)
decl_stmt|;
if|if
condition|(
name|cosGroup
operator|!=
literal|null
condition|)
block|{
name|group
operator|=
operator|(
name|PDTransparencyGroup
operator|)
name|PDXObject
operator|.
name|createXObject
argument_list|(
name|cosGroup
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|group
return|;
block|}
comment|/**      * Returns the backdrop color.      */
specifier|public
name|COSArray
name|getBackdropColor
parameter_list|()
block|{
if|if
condition|(
name|backdropColor
operator|==
literal|null
condition|)
block|{
name|backdropColor
operator|=
operator|(
name|COSArray
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BC
argument_list|)
expr_stmt|;
block|}
return|return
name|backdropColor
return|;
block|}
comment|/**      * Returns the transfer function.      * @throws IOException If we are unable to create the PDFunction object.      */
specifier|public
name|PDFunction
name|getTransferFunction
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|transferFunction
operator|==
literal|null
condition|)
block|{
name|COSBase
name|cosTF
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|TR
argument_list|)
decl_stmt|;
if|if
condition|(
name|cosTF
operator|!=
literal|null
condition|)
block|{
name|transferFunction
operator|=
name|PDFunction
operator|.
name|create
argument_list|(
name|cosTF
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|transferFunction
return|;
block|}
block|}
end_class

end_unit

