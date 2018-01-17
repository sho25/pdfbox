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
name|PDInkAppearanceHandler
import|;
end_import

begin_comment
comment|/**  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationInk
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
literal|"Ink"
decl_stmt|;
specifier|private
name|PDAppearanceHandler
name|inkAppearanceHandler
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDAnnotationInk
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
comment|/**      * Constructor.      *      * @param dict The annotations dictionary.      */
specifier|public
name|PDAnnotationInk
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|super
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
comment|//TODO setInkList, javadoc
specifier|public
name|float
index|[]
index|[]
name|getInkList
parameter_list|()
block|{
name|COSBase
name|base
init|=
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|INKLIST
argument_list|)
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|base
decl_stmt|;
name|float
index|[]
index|[]
name|inkList
init|=
operator|new
name|float
index|[
name|array
operator|.
name|size
argument_list|()
index|]
index|[]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
comment|//TODO check for class
name|COSArray
name|innerArray
init|=
operator|(
name|COSArray
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|inkList
index|[
name|i
index|]
operator|=
name|innerArray
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
block|}
return|return
name|inkList
return|;
block|}
comment|// Should never happen as this is a required item
return|return
literal|null
return|;
block|}
comment|/**      * Set a custom appearance handler for generating the annotations appearance streams.      *       * @param inkAppearanceHandler      */
specifier|public
name|void
name|setCustomInkAppearanceHandler
parameter_list|(
name|PDAppearanceHandler
name|inkAppearanceHandler
parameter_list|)
block|{
name|this
operator|.
name|inkAppearanceHandler
operator|=
name|inkAppearanceHandler
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|constructAppearances
parameter_list|()
block|{
if|if
condition|(
name|inkAppearanceHandler
operator|==
literal|null
condition|)
block|{
name|PDInkAppearanceHandler
name|appearanceHandler
init|=
operator|new
name|PDInkAppearanceHandler
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
name|inkAppearanceHandler
operator|.
name|generateAppearanceStreams
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

