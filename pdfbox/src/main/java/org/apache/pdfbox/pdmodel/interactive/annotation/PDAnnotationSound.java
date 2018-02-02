begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2018 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|ScratchFile
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
name|interactive
operator|.
name|annotation
operator|.
name|handlers
operator|.
name|PDAppearanceHandler
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
name|interactive
operator|.
name|annotation
operator|.
name|handlers
operator|.
name|PDSoundAppearanceHandler
import|;
end_import

begin_comment
comment|/**  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationSound
extends|extends
name|PDAnnotationMarkup
block|{
comment|/**      * The type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"Sound"
decl_stmt|;
specifier|private
name|PDAppearanceHandler
name|soundAppearanceHandler
decl_stmt|;
specifier|public
name|PDAnnotationSound
parameter_list|()
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a sound annotation from a COSDictionary, expected to be a correct object definition.      *      * @param field the PDF object to represent as a field.      */
specifier|public
name|PDAnnotationSound
parameter_list|(
name|COSDictionary
name|field
parameter_list|)
block|{
name|super
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set a custom appearance handler for generating the annotations appearance streams.      *       * @param soundAppearanceHandler      */
specifier|public
name|void
name|setCustomSoundAppearanceHandler
parameter_list|(
name|PDAppearanceHandler
name|soundAppearanceHandler
parameter_list|)
block|{
name|this
operator|.
name|soundAppearanceHandler
operator|=
name|soundAppearanceHandler
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|constructAppearances
parameter_list|(
name|ScratchFile
name|scratchFile
parameter_list|)
block|{
if|if
condition|(
name|soundAppearanceHandler
operator|==
literal|null
condition|)
block|{
name|PDSoundAppearanceHandler
name|appearanceHandler
init|=
operator|new
name|PDSoundAppearanceHandler
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|appearanceHandler
operator|.
name|generateAppearanceStreams
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|soundAppearanceHandler
operator|.
name|generateAppearanceStreams
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
