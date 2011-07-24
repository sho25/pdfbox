begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|graphics
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ERROR_GRAPHIC_MISSING_FIELD
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ERROR_GRAPHIC_UNEXPECTED_KEY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|XOBJECT_DICTIONARY_KEY_COLOR_SPACE
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
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|DocumentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationResult
operator|.
name|ValidationError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|graphics
operator|.
name|color
operator|.
name|ColorSpaceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|graphics
operator|.
name|color
operator|.
name|ColorSpaceHelperFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|graphics
operator|.
name|color
operator|.
name|ColorSpaceHelperFactory
operator|.
name|ColorSpaceRestriction
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|utils
operator|.
name|COSUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|utils
operator|.
name|RenderingIntents
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
name|COSBoolean
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

begin_comment
comment|/**  * This class validates XObject with the Image subtype.  */
end_comment

begin_class
specifier|public
class|class
name|XObjImageValidator
extends|extends
name|AbstractXObjValidator
block|{
specifier|public
name|XObjImageValidator
parameter_list|(
name|DocumentHandler
name|handler
parameter_list|,
name|COSStream
name|xobj
parameter_list|)
block|{
name|super
argument_list|(
name|handler
argument_list|,
name|xobj
argument_list|)
expr_stmt|;
block|}
comment|/*    * (non-Javadoc)    *     * @seenet.awl.edoc.pdfa.validation.graphics.AbstractXObjValidator#    * checkMandatoryFields(java.util.List)    */
annotation|@
name|Override
specifier|protected
name|boolean
name|checkMandatoryFields
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|result
parameter_list|)
block|{
name|boolean
name|res
init|=
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Width"
argument_list|)
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|res
operator|=
name|res
operator|&&
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Height"
argument_list|)
argument_list|)
operator|!=
literal|null
expr_stmt|;
comment|// type and subtype checked before to create the Validator.
if|if
condition|(
operator|!
name|res
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_MISSING_FIELD
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|res
return|;
block|}
comment|/*    * 6.2.4 no Alternates    */
specifier|protected
name|void
name|checkAlternates
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|result
parameter_list|)
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Alternates"
argument_list|)
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_GRAPHIC_UNEXPECTED_KEY
argument_list|,
literal|"Unexpected 'Alternates' Key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*    * 6.2.4 if interpolates, value = false    */
specifier|protected
name|void
name|checkInterpolate
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|result
parameter_list|)
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Interpolate"
argument_list|)
argument_list|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|xobject
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Interpolate"
argument_list|)
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
argument_list|,
literal|"Unexpected 'true' value for 'Interpolate' Key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/*    * 6.2.4 Intent has specific values    */
specifier|protected
name|void
name|checkIntent
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|result
parameter_list|)
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Intent"
argument_list|)
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|s
init|=
name|this
operator|.
name|xobject
operator|.
name|getNameAsString
argument_list|(
literal|"Intent"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|RenderingIntents
operator|.
name|contains
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
argument_list|,
literal|"Unexpected value '"
operator|+
name|s
operator|+
literal|"' for Intent key in image"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/*    * According to the PDF Reference file, there are some specific rules on following fields    * ColorSpace, Mask, ImageMask and BitsPerComponent.    * If ImageMask is set to true, ColorSpace and Mask entries are forbidden.     */
specifier|protected
name|void
name|checkColorSpaceAndImageMask
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|result
parameter_list|)
throws|throws
name|ValidationException
block|{
name|COSBase
name|csImg
init|=
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|XOBJECT_DICTIONARY_KEY_COLOR_SPACE
argument_list|)
argument_list|)
decl_stmt|;
name|COSBase
name|bitsPerComp
init|=
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"BitsPerComponent"
argument_list|)
argument_list|)
decl_stmt|;
name|COSBase
name|mask
init|=
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Mask"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|isImageMaskTrue
argument_list|()
condition|)
block|{
if|if
condition|(
name|csImg
operator|!=
literal|null
operator|||
name|mask
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_KEY
argument_list|,
literal|"ImageMask entry is true, ColorSpace and Mask are forbidden."
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Integer
name|bitsPerCompValue
init|=
name|COSUtils
operator|.
name|getAsInteger
argument_list|(
name|bitsPerComp
argument_list|,
name|cosDocument
argument_list|)
decl_stmt|;
if|if
condition|(
name|bitsPerCompValue
operator|!=
literal|1
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
argument_list|,
literal|"ImageMask entry is true, BitsPerComponent must be 1."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ColorSpaceHelper
name|csh
init|=
name|ColorSpaceHelperFactory
operator|.
name|getColorSpaceHelper
argument_list|(
name|csImg
argument_list|,
name|handler
argument_list|,
name|ColorSpaceRestriction
operator|.
name|NO_PATTERN
argument_list|)
decl_stmt|;
name|csh
operator|.
name|validate
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isImageMaskTrue
parameter_list|()
block|{
name|COSBase
name|imgMask
init|=
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ImageMask"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|imgMask
operator|!=
literal|null
operator|&&
name|imgMask
operator|instanceof
name|COSBoolean
condition|)
block|{
return|return
operator|(
operator|(
name|COSBoolean
operator|)
name|imgMask
operator|)
operator|.
name|getValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/*    * (non-Javadoc)    *     * @see net.awl.edoc.pdfa.validation.graphics.AbstractXObjValidator#validate()    */
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|validate
parameter_list|()
throws|throws
name|ValidationException
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|result
init|=
name|super
operator|.
name|validate
argument_list|()
decl_stmt|;
name|checkAlternates
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|checkInterpolate
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|checkIntent
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|checkColorSpaceAndImageMask
argument_list|(
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

