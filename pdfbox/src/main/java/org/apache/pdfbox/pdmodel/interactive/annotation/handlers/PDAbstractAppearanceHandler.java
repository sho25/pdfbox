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
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
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
name|PDRectangle
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
name|PDColor
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
name|state
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
name|PDAnnotationSquareCircle
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
name|PDAppearanceContentStream
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
name|PDAppearanceDictionary
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
name|PDAppearanceEntry
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
name|PDAppearanceStream
import|;
end_import

begin_comment
comment|/**  * Generic handler to generate the fields appearance.  *   * Individual handler will provide specific implementations for different field  * types.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDAbstractAppearanceHandler
implements|implements
name|PDAppearanceHandler
block|{
specifier|private
specifier|final
name|PDAnnotation
name|annotation
decl_stmt|;
specifier|private
name|PDAppearanceEntry
name|appearanceEntry
decl_stmt|;
specifier|private
name|PDAppearanceContentStream
name|contentStream
decl_stmt|;
specifier|public
name|PDAbstractAppearanceHandler
parameter_list|(
name|PDAnnotation
name|annotation
parameter_list|)
block|{
name|this
operator|.
name|annotation
operator|=
name|annotation
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|abstract
name|void
name|generateNormalAppearance
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
specifier|abstract
name|void
name|generateRolloverAppearance
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
specifier|abstract
name|void
name|generateDownAppearance
parameter_list|()
function_decl|;
name|PDAnnotation
name|getAnnotation
parameter_list|()
block|{
return|return
name|annotation
return|;
block|}
name|PDColor
name|getColor
parameter_list|()
block|{
return|return
name|annotation
operator|.
name|getColor
argument_list|()
return|;
block|}
name|PDRectangle
name|getRectangle
parameter_list|()
block|{
return|return
name|annotation
operator|.
name|getRectangle
argument_list|()
return|;
block|}
comment|/**      * Get the annotations appearance dictionary.      *       *<p>      * This will get the annotations appearance dictionary. If this is not      * existent an empty appearance dictionary will be created.      *       * @return the annotations appearance dictionary      */
name|PDAppearanceDictionary
name|getAppearance
parameter_list|()
block|{
name|PDAppearanceDictionary
name|appearanceDictionary
init|=
name|annotation
operator|.
name|getAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|appearanceDictionary
operator|==
literal|null
condition|)
block|{
name|appearanceDictionary
operator|=
operator|new
name|PDAppearanceDictionary
argument_list|()
expr_stmt|;
name|annotation
operator|.
name|setAppearance
argument_list|(
name|appearanceDictionary
argument_list|)
expr_stmt|;
block|}
return|return
name|appearanceDictionary
return|;
block|}
comment|/**      * Get the annotations normal appearance content stream.      *       *<p>      * This will get the annotations normal appearance content stream,      * to 'draw' to.      *       * @return the appearance entry representing the normal appearance.      * @throws IOException       */
name|PDAppearanceContentStream
name|getNormalAppearanceAsContentStream
parameter_list|()
throws|throws
name|IOException
block|{
name|appearanceEntry
operator|=
name|getNormalAppearance
argument_list|()
expr_stmt|;
name|contentStream
operator|=
name|getAppearanceEntryAsContentStream
argument_list|(
name|appearanceEntry
argument_list|)
expr_stmt|;
return|return
name|contentStream
return|;
block|}
comment|/**      * Get the annotations down appearance.      *       *<p>      * This will get the annotations down appearance. If this is not existent an      * empty appearance entry will be created.      *       * @return the appearance entry representing the down appearance.      */
name|PDAppearanceEntry
name|getDownAppearance
parameter_list|()
block|{
name|PDAppearanceDictionary
name|appearanceDictionary
init|=
name|getAppearance
argument_list|()
decl_stmt|;
name|PDAppearanceEntry
name|appearanceEntry
init|=
name|appearanceDictionary
operator|.
name|getDownAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|appearanceEntry
operator|.
name|isSubDictionary
argument_list|()
condition|)
block|{
comment|//TODO replace with "document.getDocument().createCOSStream()"
name|appearanceEntry
operator|=
operator|new
name|PDAppearanceEntry
argument_list|(
operator|new
name|COSStream
argument_list|()
argument_list|)
expr_stmt|;
name|appearanceDictionary
operator|.
name|setDownAppearance
argument_list|(
name|appearanceEntry
argument_list|)
expr_stmt|;
block|}
return|return
name|appearanceEntry
return|;
block|}
comment|/**      * Get the annotations rollover appearance.      *       *<p>      * This will get the annotations rollover appearance. If this is not      * existent an empty appearance entry will be created.      *       * @return the appearance entry representing the rollover appearance.      */
name|PDAppearanceEntry
name|getRolloverAppearance
parameter_list|()
block|{
name|PDAppearanceDictionary
name|appearanceDictionary
init|=
name|getAppearance
argument_list|()
decl_stmt|;
name|PDAppearanceEntry
name|appearanceEntry
init|=
name|appearanceDictionary
operator|.
name|getRolloverAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|appearanceEntry
operator|.
name|isSubDictionary
argument_list|()
condition|)
block|{
name|appearanceEntry
operator|=
operator|new
name|PDAppearanceEntry
argument_list|(
operator|new
name|COSStream
argument_list|()
argument_list|)
expr_stmt|;
name|appearanceDictionary
operator|.
name|setRolloverAppearance
argument_list|(
name|appearanceEntry
argument_list|)
expr_stmt|;
block|}
return|return
name|appearanceEntry
return|;
block|}
comment|/**      * Set the differences rectangle.      */
name|void
name|setRectDifference
parameter_list|(
name|float
name|lineWidth
parameter_list|)
block|{
if|if
condition|(
name|annotation
operator|instanceof
name|PDAnnotationSquareCircle
operator|&&
name|lineWidth
operator|>
literal|0
condition|)
block|{
name|PDRectangle
name|differences
init|=
operator|new
name|PDRectangle
argument_list|(
name|lineWidth
operator|/
literal|2
argument_list|,
name|lineWidth
operator|/
literal|2
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
operator|(
operator|(
name|PDAnnotationSquareCircle
operator|)
name|annotation
operator|)
operator|.
name|setRectDifference
argument_list|(
name|differences
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get a padded rectangle.      *       *<p>Creates a new rectangle with padding applied to each side.      * .      * @param rectangle the rectangle.      * @param padding the padding to apply.      * @return the padded rectangle.      */
name|PDRectangle
name|getPaddedRectangle
parameter_list|(
name|PDRectangle
name|rectangle
parameter_list|,
name|float
name|padding
parameter_list|)
block|{
return|return
operator|new
name|PDRectangle
argument_list|(
name|rectangle
operator|.
name|getLowerLeftX
argument_list|()
operator|+
name|padding
argument_list|,
name|rectangle
operator|.
name|getLowerLeftY
argument_list|()
operator|+
name|padding
argument_list|,
name|rectangle
operator|.
name|getWidth
argument_list|()
operator|-
literal|2
operator|*
name|padding
argument_list|,
name|rectangle
operator|.
name|getHeight
argument_list|()
operator|-
literal|2
operator|*
name|padding
argument_list|)
return|;
block|}
name|void
name|handleOpacity
parameter_list|(
name|float
name|opacity
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|opacity
operator|<
literal|1
condition|)
block|{
name|PDExtendedGraphicsState
name|gs
init|=
operator|new
name|PDExtendedGraphicsState
argument_list|()
decl_stmt|;
name|gs
operator|.
name|setStrokingAlphaConstant
argument_list|(
name|opacity
argument_list|)
expr_stmt|;
name|gs
operator|.
name|setNonStrokingAlphaConstant
argument_list|(
name|opacity
argument_list|)
expr_stmt|;
name|PDAppearanceStream
name|appearanceStream
init|=
name|appearanceEntry
operator|.
name|getAppearanceStream
argument_list|()
decl_stmt|;
name|PDResources
name|resources
init|=
name|appearanceStream
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
name|resources
operator|=
operator|new
name|PDResources
argument_list|()
expr_stmt|;
name|appearanceStream
operator|.
name|setResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|setResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
block|}
name|contentStream
operator|.
name|setGraphicsStateParameters
argument_list|(
name|gs
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the annotations normal appearance.      *       *<p>      * This will get the annotations normal appearance. If this is not existent      * an empty appearance entry will be created.      *       * @return the appearance entry representing the normal appearance.      */
specifier|private
name|PDAppearanceEntry
name|getNormalAppearance
parameter_list|()
block|{
name|PDAppearanceDictionary
name|appearanceDictionary
init|=
name|getAppearance
argument_list|()
decl_stmt|;
name|PDAppearanceEntry
name|appearanceEntry
init|=
name|appearanceDictionary
operator|.
name|getNormalAppearance
argument_list|()
decl_stmt|;
if|if
condition|(
name|appearanceEntry
operator|.
name|isSubDictionary
argument_list|()
condition|)
block|{
name|appearanceEntry
operator|=
operator|new
name|PDAppearanceEntry
argument_list|(
operator|new
name|COSStream
argument_list|()
argument_list|)
expr_stmt|;
name|appearanceDictionary
operator|.
name|setNormalAppearance
argument_list|(
name|appearanceEntry
argument_list|)
expr_stmt|;
block|}
return|return
name|appearanceEntry
return|;
block|}
specifier|private
name|PDAppearanceContentStream
name|getAppearanceEntryAsContentStream
parameter_list|(
name|PDAppearanceEntry
name|appearanceEntry
parameter_list|)
throws|throws
name|IOException
block|{
name|PDAppearanceStream
name|appearanceStream
init|=
name|appearanceEntry
operator|.
name|getAppearanceStream
argument_list|()
decl_stmt|;
name|setTransformationMatrix
argument_list|(
name|appearanceStream
argument_list|)
expr_stmt|;
return|return
operator|new
name|PDAppearanceContentStream
argument_list|(
name|appearanceStream
argument_list|)
return|;
block|}
specifier|private
name|void
name|setTransformationMatrix
parameter_list|(
name|PDAppearanceStream
name|appearanceStream
parameter_list|)
block|{
name|PDRectangle
name|bbox
init|=
name|getRectangle
argument_list|()
decl_stmt|;
name|appearanceStream
operator|.
name|setBBox
argument_list|(
name|bbox
argument_list|)
expr_stmt|;
name|AffineTransform
name|transform
init|=
name|AffineTransform
operator|.
name|getTranslateInstance
argument_list|(
operator|-
name|bbox
operator|.
name|getLowerLeftX
argument_list|()
argument_list|,
operator|-
name|bbox
operator|.
name|getLowerLeftY
argument_list|()
argument_list|)
decl_stmt|;
name|appearanceStream
operator|.
name|setMatrix
argument_list|(
name|transform
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

