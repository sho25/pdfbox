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
name|pdfbox
operator|.
name|preflight
operator|.
name|xobject
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_GRAPHIC_INVALID_UNKNOWN_COLOR_SPACE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
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
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
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
name|pdfbox
operator|.
name|preflight
operator|.
name|PreflightConstants
operator|.
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
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
name|COSInteger
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
name|graphics
operator|.
name|color
operator|.
name|PDColorSpace
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
name|image
operator|.
name|PDImageXObject
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
name|preflight
operator|.
name|PreflightConfiguration
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
name|preflight
operator|.
name|PreflightContext
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
name|pdfbox
operator|.
name|preflight
operator|.
name|exception
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
name|pdfbox
operator|.
name|preflight
operator|.
name|graphic
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
name|pdfbox
operator|.
name|preflight
operator|.
name|graphic
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
name|pdfbox
operator|.
name|preflight
operator|.
name|graphic
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
name|pdfbox
operator|.
name|preflight
operator|.
name|utils
operator|.
name|RenderingIntents
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
specifier|private
specifier|static
specifier|final
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|XObjImageValidator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|PDImageXObject
name|xImage
init|=
literal|null
decl_stmt|;
specifier|public
name|XObjImageValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|PDImageXObject
name|xobj
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|xobj
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|xImage
operator|=
name|xobj
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|checkMandatoryFields
parameter_list|()
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
name|WIDTH
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
name|HEIGHT
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
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_MISSING_FIELD
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * 6.2.4 no Alternates      */
specifier|protected
name|void
name|checkAlternates
parameter_list|()
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
literal|"Alternates"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_KEY
argument_list|,
literal|"Unexpected 'Alternates' Key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * 6.2.4 if interpolates, value = false      */
specifier|protected
name|void
name|checkInterpolate
parameter_list|()
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
name|INTERPOLATE
argument_list|)
operator|!=
literal|null
operator|&&
name|this
operator|.
name|xobject
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|INTERPOLATE
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
argument_list|,
literal|"Unexpected 'true' value for 'Interpolate' Key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * 6.2.4 Intent has specific values      */
specifier|protected
name|void
name|checkIntent
parameter_list|()
block|{
name|COSName
name|renderingIntent
init|=
name|xobject
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|INTENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|renderingIntent
operator|!=
literal|null
operator|&&
operator|!
name|RenderingIntents
operator|.
name|contains
argument_list|(
name|renderingIntent
argument_list|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
argument_list|,
literal|"Unexpected value '"
operator|+
name|renderingIntent
operator|.
name|getName
argument_list|()
operator|+
literal|"' for Intent key in image"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*      * According to the PDF Reference file, there are some specific rules on following fields ColorSpace, Mask,      * ImageMask and BitsPerComponent. If ImageMask is set to true, ColorSpace and Mask entries are forbidden.      */
specifier|protected
name|void
name|checkColorSpaceAndImageMask
parameter_list|()
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
name|COLORSPACE
argument_list|)
decl_stmt|;
name|COSBase
name|bitsPerComp
init|=
name|this
operator|.
name|xobject
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BITS_PER_COMPONENT
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
name|MASK
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
name|context
operator|.
name|addValidationError
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
if|if
condition|(
name|bitsPerComp
operator|instanceof
name|COSInteger
operator|&&
operator|(
operator|(
name|COSInteger
operator|)
name|bitsPerComp
operator|)
operator|.
name|intValue
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
argument_list|,
literal|"ImageMask entry is true, BitsPerComponent must be absent or 1."
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
try|try
block|{
name|PreflightConfiguration
name|config
init|=
name|context
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|ColorSpaceHelperFactory
name|csFact
init|=
name|config
operator|.
name|getColorSpaceHelperFact
argument_list|()
decl_stmt|;
name|PDColorSpace
name|pdCS
init|=
name|PDColorSpace
operator|.
name|create
argument_list|(
name|csImg
argument_list|)
decl_stmt|;
name|ColorSpaceHelper
name|csh
init|=
name|csFact
operator|.
name|getColorSpaceHelper
argument_list|(
name|context
argument_list|,
name|pdCS
argument_list|,
name|ColorSpaceRestriction
operator|.
name|NO_PATTERN
argument_list|)
decl_stmt|;
name|csh
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Couldn't create PDColorSpace "
operator|+
name|csImg
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_UNKNOWN_COLOR_SPACE
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"ImageMask"
argument_list|)
decl_stmt|;
if|if
condition|(
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
comment|/*      * (non-Javadoc)      *       * @see org.apache.pdfbox.preflight.graphic.AbstractXObjValidator#validate()      */
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|()
throws|throws
name|ValidationException
block|{
name|super
operator|.
name|validate
argument_list|()
expr_stmt|;
name|checkAlternates
argument_list|()
expr_stmt|;
name|checkInterpolate
argument_list|()
expr_stmt|;
name|checkIntent
argument_list|()
expr_stmt|;
name|checkColorSpaceAndImageMask
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

