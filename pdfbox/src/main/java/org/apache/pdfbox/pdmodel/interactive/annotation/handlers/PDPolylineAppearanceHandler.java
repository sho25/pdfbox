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
operator|.
name|handlers
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
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotation
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
name|PDAnnotationPolyline
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
name|PDBorderStyleDictionary
import|;
end_import

begin_comment
comment|/**  * Handler to generate the polygon annotations appearance.  *  */
end_comment

begin_class
specifier|public
class|class
name|PDPolylineAppearanceHandler
extends|extends
name|PDAbstractAppearanceHandler
block|{
specifier|public
name|PDPolylineAppearanceHandler
parameter_list|(
name|PDAnnotation
name|annotation
parameter_list|)
block|{
name|super
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateAppearanceStreams
parameter_list|()
block|{
name|generateNormalAppearance
argument_list|()
expr_stmt|;
name|generateRolloverAppearance
argument_list|()
expr_stmt|;
name|generateDownAppearance
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateNormalAppearance
parameter_list|()
block|{
comment|//TODO
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateRolloverAppearance
parameter_list|()
block|{
comment|// No rollover appearance generated for a polygon annotation
block|}
annotation|@
name|Override
specifier|public
name|void
name|generateDownAppearance
parameter_list|()
block|{
comment|// No down appearance generated for a polygon annotation
block|}
comment|//TODO DRY, this code is from polygonAppearanceHandler so it's double
comment|/**      * Get the line with of the border.      *       * Get the width of the line used to draw a border around the annotation.      * This may either be specified by the annotation dictionaries Border      * setting or by the W entry in the BS border style dictionary. If both are      * missing the default width is 1.      *       * @return the line width      */
comment|// TODO: according to the PDF spec the use of the BS entry is annotation
comment|// specific
comment|// so we will leave that to be implemented by individual handlers.
comment|// If at the end all annotations support the BS entry this can be handled
comment|// here and removed from the individual handlers.
name|float
name|getLineWidth
parameter_list|()
block|{
name|PDAnnotationPolyline
name|annotation
init|=
operator|(
name|PDAnnotationPolyline
operator|)
name|getAnnotation
argument_list|()
decl_stmt|;
name|PDBorderStyleDictionary
name|bs
init|=
name|annotation
operator|.
name|getBorderStyle
argument_list|()
decl_stmt|;
if|if
condition|(
name|bs
operator|!=
literal|null
condition|)
block|{
return|return
name|bs
operator|.
name|getWidth
argument_list|()
return|;
block|}
else|else
block|{
name|COSArray
name|borderCharacteristics
init|=
name|annotation
operator|.
name|getBorder
argument_list|()
decl_stmt|;
if|if
condition|(
name|borderCharacteristics
operator|.
name|size
argument_list|()
operator|>=
literal|3
condition|)
block|{
return|return
name|borderCharacteristics
operator|.
name|getInt
argument_list|(
literal|3
argument_list|)
return|;
block|}
block|}
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

