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
name|graphic
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ICC_Profile
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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|cos
operator|.
name|COSNumber
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
name|color
operator|.
name|PDDeviceN
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
name|PDDeviceNAttributes
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
name|PDDeviceNProcess
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
name|PDICCBased
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
name|PDIndexed
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
name|PDSeparation
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
name|PreflightPath
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_ALTERNATE
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_CMYK
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_ICCBASED
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_INDEXED
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_MISSING
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_RGB
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_TOO_MANY_COMPONENTS_DEVICEN
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
name|ERROR_GRAPHIC_INVALID_PATTERN_COLOR_SPACE_FORBIDDEN
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
name|ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_TOO_RECENT
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
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
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
name|MAX_DEVICE_N_LIMIT
import|;
end_import

begin_comment
comment|/**  * This class doesn't define restrictions on ColorSpace. It checks only the consistency of the Color space with the  * DestOutputIntent.  */
end_comment

begin_class
specifier|public
class|class
name|StandardColorSpaceHelper
implements|implements
name|ColorSpaceHelper
block|{
comment|/**      * The context which contains useful information to process the validation.      */
specifier|protected
name|PreflightContext
name|context
init|=
literal|null
decl_stmt|;
comment|/**      * The ICCProfile contained in the DestOutputIntent      */
specifier|protected
name|ICCProfileWrapper
name|iccpw
init|=
literal|null
decl_stmt|;
comment|/**      * High level object which represents the colors space to check.      */
specifier|protected
name|PDColorSpace
name|pdcs
init|=
literal|null
decl_stmt|;
specifier|protected
name|StandardColorSpaceHelper
parameter_list|(
name|PreflightContext
name|_context
parameter_list|,
name|PDColorSpace
name|_cs
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|_context
expr_stmt|;
name|this
operator|.
name|pdcs
operator|=
name|_cs
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.pdfbox.preflight.graphic.color.ColorSpaceHelper#validate(java .util.List)      */
annotation|@
name|Override
specifier|public
specifier|final
name|void
name|validate
parameter_list|()
throws|throws
name|ValidationException
block|{
if|if
condition|(
name|pdcs
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to create a PDColorSpace with the value null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|iccpw
operator|=
name|ICCProfileWrapper
operator|.
name|getOrSearchICCProfile
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|processAllColorSpace
argument_list|(
name|pdcs
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method called by the validate method. According to the ColorSpace, a specific ColorSpace method is called.      *       * @param colorSpace the color space object to check.      */
specifier|protected
specifier|final
name|void
name|processAllColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
name|ColorSpaces
name|cs
init|=
name|ColorSpaces
operator|.
name|valueOf
argument_list|(
name|colorSpace
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|cs
condition|)
block|{
case|case
name|DeviceRGB
case|:
case|case
name|RGB
case|:
name|processRGBColorSpace
argument_list|(
name|colorSpace
argument_list|)
expr_stmt|;
break|break;
case|case
name|DeviceCMYK
case|:
case|case
name|CMYK
case|:
name|processCYMKColorSpace
argument_list|(
name|colorSpace
argument_list|)
expr_stmt|;
break|break;
case|case
name|CalRGB
case|:
case|case
name|CalGray
case|:
case|case
name|Lab
case|:
name|processCalibratedColorSpace
argument_list|(
name|colorSpace
argument_list|)
expr_stmt|;
break|break;
case|case
name|DeviceGray
case|:
case|case
name|G
case|:
name|processGrayColorSpace
argument_list|(
name|colorSpace
argument_list|)
expr_stmt|;
break|break;
case|case
name|ICCBased
case|:
name|processICCBasedColorSpace
argument_list|(
name|colorSpace
argument_list|)
expr_stmt|;
break|break;
case|case
name|DeviceN
case|:
name|processDeviceNColorSpace
argument_list|(
name|colorSpace
argument_list|)
expr_stmt|;
break|break;
case|case
name|Indexed
case|:
case|case
name|I
case|:
name|processIndexedColorSpace
argument_list|(
name|colorSpace
argument_list|)
expr_stmt|;
break|break;
case|case
name|Separation
case|:
name|processSeparationColorSpace
argument_list|(
name|colorSpace
argument_list|)
expr_stmt|;
break|break;
case|case
name|Pattern
case|:
name|processPatternColorSpace
argument_list|(
name|colorSpace
argument_list|)
expr_stmt|;
break|break;
default|default:
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_UNKNOWN_COLOR_SPACE
argument_list|,
name|cs
operator|.
name|getLabel
argument_list|()
operator|+
literal|" is unknown as ColorSpace"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Method called by the processAllColorSpace if the ColorSpace to check is DeviceRGB.      *       */
specifier|protected
name|void
name|processRGBColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
if|if
condition|(
operator|!
name|processDefaultColorSpace
argument_list|(
name|colorSpace
argument_list|)
condition|)
block|{
if|if
condition|(
name|iccpw
operator|==
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_MISSING
argument_list|,
literal|"DestOutputProfile is missing"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|iccpw
operator|.
name|isRGBColorSpace
argument_list|()
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_RGB
argument_list|,
literal|"DestOutputProfile isn't RGB ColorSpace"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Method called by the processAllColorSpace if the ColorSpace to check is DeviceCYMK.      *       */
specifier|protected
name|void
name|processCYMKColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
if|if
condition|(
operator|!
name|processDefaultColorSpace
argument_list|(
name|colorSpace
argument_list|)
condition|)
block|{
if|if
condition|(
name|iccpw
operator|==
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_MISSING
argument_list|,
literal|"DestOutputProfile is missing"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|iccpw
operator|.
name|isCMYKColorSpace
argument_list|()
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_CMYK
argument_list|,
literal|"DestOutputProfile isn't CMYK ColorSpace"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Method called by the processAllColorSpace if the ColorSpace to check is a Pattern.      * @param colorSpace       */
specifier|protected
name|void
name|processPatternColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
if|if
condition|(
name|iccpw
operator|==
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_MISSING
argument_list|,
literal|"DestOutputProfile is missing"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Method called by the processAllColorSpace if the ColorSpace to check is DeviceGray.      *       */
specifier|protected
name|void
name|processGrayColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
if|if
condition|(
operator|!
name|processDefaultColorSpace
argument_list|(
name|colorSpace
argument_list|)
operator|&&
name|iccpw
operator|==
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_MISSING
argument_list|,
literal|"DestOutputProfile is missing"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Method called by the processAllColorSpace if the ColorSpace to check is a calibrated color (CalGray, CalRGB, Lab).      * @param colorSpace       *       */
specifier|protected
name|void
name|processCalibratedColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
comment|// ---- OutputIntent isn't mandatory
block|}
comment|/**      * Method called by the processAllColorSpace if the ColorSpace to check is a ICCBased color space. Because this kind      * of ColorSpace can have alternate color space, the processAllColorSpace is called to check this alternate color      * space. (Pattern is forbidden as Alternate Color Space)      *       * @param colorSpace      *            the color space object to check.      */
specifier|protected
name|void
name|processICCBasedColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
name|PDICCBased
name|iccBased
init|=
operator|(
name|PDICCBased
operator|)
name|colorSpace
decl_stmt|;
try|try
block|{
name|ICC_Profile
name|iccp
decl_stmt|;
try|try
init|(
name|InputStream
name|is
init|=
name|iccBased
operator|.
name|getPDStream
argument_list|()
operator|.
name|createInputStream
argument_list|()
init|)
block|{
comment|// check that ICC profile loads (PDICCBased also does this, but catches the exception)
comment|// PDFBOX-2819: load ICC profile as a stream, not as a byte array because of java error
name|iccp
operator|=
name|ICC_Profile
operator|.
name|getInstance
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
name|PDColorSpace
name|altpdcs
init|=
name|iccBased
operator|.
name|getAlternateColorSpace
argument_list|()
decl_stmt|;
if|if
condition|(
name|altpdcs
operator|!=
literal|null
condition|)
block|{
name|ColorSpaces
name|altCsId
init|=
name|ColorSpaces
operator|.
name|valueOf
argument_list|(
name|altpdcs
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|altCsId
operator|==
name|ColorSpaces
operator|.
name|Pattern
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_PATTERN_COLOR_SPACE_FORBIDDEN
argument_list|,
literal|"Pattern is forbidden as AlternateColorSpace of a ICCBased"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/*                  * According to the ISO-19005-1:2005                  *                   * A conforming reader shall render ICCBased colour spaces as specified by the ICC specification,                  * and shall not use the Alternate colour space specified in an ICC profile stream dictionary                  *                   * We don't check the alternate ColorSpaces                  */
comment|// PDFBOX-4611, PDFBOX-4607: Yes we do because Adobe Reader chokes on it
comment|// and because VeraPDF and PDF-Tools do it.
if|if
condition|(
operator|!
name|validateICCProfileNEntry
argument_list|(
name|iccBased
operator|.
name|getPDStream
argument_list|()
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|iccp
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|validateICCProfileVersion
argument_list|(
name|iccp
argument_list|)
condition|)
block|{
return|return;
block|}
name|validateICCProfileAlternateEntry
argument_list|(
name|iccBased
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
decl||
name|ArrayIndexOutOfBoundsException
name|e
parameter_list|)
block|{
comment|// this is not a ICC_Profile
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_ICCBASED
argument_list|,
literal|"ICCBased color space is invalid: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE
argument_list|,
literal|"Unable to read ICCBase color space: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Method called by the processAllColorSpace if the ColorSpace to check is DeviceN. Because this kind of ColorSpace      * can have alternate color space, the processAllColorSpace is called to check this alternate color space. (There      * are no restrictions on the Alternate Color space)      *       * @param colorSpace      *            the color space object to check.      */
specifier|protected
name|void
name|processDeviceNColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
name|PDDeviceN
name|deviceN
init|=
operator|(
name|PDDeviceN
operator|)
name|colorSpace
decl_stmt|;
try|try
block|{
if|if
condition|(
name|iccpw
operator|==
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
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_MISSING
argument_list|,
literal|"DestOutputProfile is missing"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|COSBase
name|cosAlt
init|=
operator|(
operator|(
name|COSArray
operator|)
name|colorSpace
operator|.
name|getCOSObject
argument_list|()
operator|)
operator|.
name|getObject
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|PDColorSpace
name|altColor
init|=
name|PDColorSpace
operator|.
name|create
argument_list|(
name|cosAlt
argument_list|)
decl_stmt|;
if|if
condition|(
name|altColor
operator|!=
literal|null
condition|)
block|{
name|processAllColorSpace
argument_list|(
name|altColor
argument_list|)
expr_stmt|;
block|}
name|int
name|numberOfColorants
init|=
literal|0
decl_stmt|;
name|PDDeviceNAttributes
name|attr
init|=
name|deviceN
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|attr
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|PDSeparation
argument_list|>
name|colorants
init|=
name|attr
operator|.
name|getColorants
argument_list|()
decl_stmt|;
name|numberOfColorants
operator|=
name|colorants
operator|.
name|size
argument_list|()
expr_stmt|;
for|for
control|(
name|PDSeparation
name|col
range|:
name|colorants
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|col
operator|!=
literal|null
condition|)
block|{
name|processAllColorSpace
argument_list|(
name|col
argument_list|)
expr_stmt|;
block|}
block|}
name|PDDeviceNProcess
name|process
init|=
name|attr
operator|.
name|getProcess
argument_list|()
decl_stmt|;
if|if
condition|(
name|process
operator|!=
literal|null
condition|)
block|{
name|processAllColorSpace
argument_list|(
name|process
operator|.
name|getColorSpace
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|numberOfComponents
init|=
name|deviceN
operator|.
name|getNumberOfComponents
argument_list|()
decl_stmt|;
if|if
condition|(
name|numberOfColorants
operator|>
name|MAX_DEVICE_N_LIMIT
operator|||
name|numberOfComponents
operator|>
name|MAX_DEVICE_N_LIMIT
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_TOO_MANY_COMPONENTS_DEVICEN
argument_list|,
literal|"DeviceN has too many tint components or colorants"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE
argument_list|,
literal|"Unable to read DeviceN color space : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Method called by the processAllColorSpace if the ColorSpace to check is Indexed. Because this kind of ColorSpace      * can have a Base color space, the processAllColorSpace is called to check this base color space. (Indexed and      * Pattern can't be a Base color space)      *       * @param colorSpace      *            the color space object to check.      */
specifier|protected
name|void
name|processIndexedColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
name|PDIndexed
name|indexed
init|=
operator|(
name|PDIndexed
operator|)
name|colorSpace
decl_stmt|;
name|PDColorSpace
name|based
init|=
name|indexed
operator|.
name|getBaseColorSpace
argument_list|()
decl_stmt|;
name|ColorSpaces
name|cs
init|=
name|ColorSpaces
operator|.
name|valueOf
argument_list|(
name|based
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cs
operator|==
name|ColorSpaces
operator|.
name|Indexed
operator|||
name|cs
operator|==
name|ColorSpaces
operator|.
name|I
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_INDEXED
argument_list|,
literal|"Indexed color space can't be used as Base color space"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|cs
operator|==
name|ColorSpaces
operator|.
name|Pattern
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_INDEXED
argument_list|,
literal|"Pattern color space can't be used as Base color space"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|processAllColorSpace
argument_list|(
name|based
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method called by the processAllColorSpace if the ColorSpace to check is Separation. Because this kind of      * ColorSpace can have an alternate color space, the processAllColorSpace is called to check this alternate color      * space. (Indexed, Separation, DeviceN and Pattern can't be a Base color space)      *       * @param colorSpace      *            the color space object to check.      */
specifier|protected
name|void
name|processSeparationColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
try|try
block|{
name|COSBase
name|cosAlt
init|=
operator|(
operator|(
name|COSArray
operator|)
name|colorSpace
operator|.
name|getCOSObject
argument_list|()
operator|)
operator|.
name|getObject
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|PDColorSpace
name|altCol
init|=
name|PDColorSpace
operator|.
name|create
argument_list|(
name|cosAlt
argument_list|)
decl_stmt|;
if|if
condition|(
name|altCol
operator|!=
literal|null
condition|)
block|{
name|ColorSpaces
name|acs
init|=
name|ColorSpaces
operator|.
name|valueOf
argument_list|(
name|altCol
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|acs
condition|)
block|{
case|case
name|Separation
case|:
case|case
name|DeviceN
case|:
case|case
name|Pattern
case|:
case|case
name|Indexed
case|:
case|case
name|I
case|:
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE_ALTERNATE
argument_list|,
name|acs
operator|.
name|getLabel
argument_list|()
operator|+
literal|" color space can't be used as alternate color space"
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
name|processAllColorSpace
argument_list|(
name|altCol
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE
argument_list|,
literal|"Unable to read Separation color space : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Look up in the closest PDResources objects if there are a default ColorSpace. If there are, check that is a      * authorized ColorSpace.      *       * @param colorSpace      * @return true if the default colorspace is a right one, false otherwise.      */
specifier|protected
name|boolean
name|processDefaultColorSpace
parameter_list|(
name|PDColorSpace
name|colorSpace
parameter_list|)
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
comment|// get default color space
name|PreflightPath
name|vPath
init|=
name|context
operator|.
name|getValidationPath
argument_list|()
decl_stmt|;
name|PDResources
name|resources
init|=
name|vPath
operator|.
name|getClosestPathElement
argument_list|(
name|PDResources
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|PDColorSpace
name|defaultCS
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|colorSpace
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|ColorSpaces
operator|.
name|DeviceCMYK
operator|.
name|getLabel
argument_list|()
argument_list|)
operator|&&
name|resources
operator|.
name|hasColorSpace
argument_list|(
name|COSName
operator|.
name|DEFAULT_CMYK
argument_list|)
condition|)
block|{
name|defaultCS
operator|=
name|resources
operator|.
name|getColorSpace
argument_list|(
name|COSName
operator|.
name|DEFAULT_CMYK
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|ColorSpaces
operator|.
name|DeviceRGB
operator|.
name|getLabel
argument_list|()
argument_list|)
operator|&&
name|resources
operator|.
name|hasColorSpace
argument_list|(
name|COSName
operator|.
name|DEFAULT_RGB
argument_list|)
condition|)
block|{
name|defaultCS
operator|=
name|resources
operator|.
name|getColorSpace
argument_list|(
name|COSName
operator|.
name|DEFAULT_RGB
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|colorSpace
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|ColorSpaces
operator|.
name|DeviceGray
operator|.
name|getLabel
argument_list|()
argument_list|)
operator|&&
name|resources
operator|.
name|hasColorSpace
argument_list|(
name|COSName
operator|.
name|DEFAULT_GRAY
argument_list|)
condition|)
block|{
name|defaultCS
operator|=
name|resources
operator|.
name|getColorSpace
argument_list|(
name|COSName
operator|.
name|DEFAULT_GRAY
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_INVALID_COLOR_SPACE
argument_list|,
literal|"Unable to read default color space : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|defaultCS
operator|!=
literal|null
condition|)
block|{
comment|// defaultCS is valid if the number of errors hasn't changed
name|int
name|nbOfErrors
init|=
name|context
operator|.
name|getDocument
argument_list|()
operator|.
name|getValidationErrors
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|processAllColorSpace
argument_list|(
name|defaultCS
argument_list|)
expr_stmt|;
name|int
name|newNbOfErrors
init|=
name|context
operator|.
name|getDocument
argument_list|()
operator|.
name|getValidationErrors
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|result
operator|=
operator|(
name|nbOfErrors
operator|==
name|newNbOfErrors
operator|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
specifier|private
name|boolean
name|validateICCProfileVersion
parameter_list|(
name|ICC_Profile
name|iccp
parameter_list|)
block|{
name|PreflightConfiguration
name|config
init|=
name|context
operator|.
name|getConfig
argument_list|()
decl_stmt|;
comment|// check the ICC Profile version (6.2.2)
if|if
condition|(
name|iccp
operator|.
name|getMajorVersion
argument_list|()
operator|==
literal|2
condition|)
block|{
if|if
condition|(
name|iccp
operator|.
name|getMinorVersion
argument_list|()
operator|>
literal|0x40
condition|)
block|{
comment|// in PDF 1.4, max version is 02h.40h (meaning V 3.5)
comment|// see the ICCProfile specification (ICC.1:1998-09)page 13 - §6.1.3 :
comment|// The current profile version number is "2.4.0" (encoded as 02400000h")
name|ValidationError
name|error
init|=
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_TOO_RECENT
argument_list|,
literal|"Invalid version of the ICCProfile"
argument_list|)
decl_stmt|;
name|error
operator|.
name|setWarning
argument_list|(
name|config
operator|.
name|isLazyValidation
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addValidationError
argument_list|(
name|error
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// else OK
block|}
elseif|else
if|if
condition|(
name|iccp
operator|.
name|getMajorVersion
argument_list|()
operator|>
literal|2
condition|)
block|{
comment|// in PDF 1.4, max version is 02h.40h (meaning V 3.5)
comment|// see the ICCProfile specification (ICC.1:1998-09)page 13 - §6.1.3 :
comment|// The current profile version number is "2.4.0" (encoded as 02400000h"
name|ValidationError
name|error
init|=
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_TOO_RECENT
argument_list|,
literal|"Invalid version of the ICCProfile"
argument_list|)
decl_stmt|;
name|error
operator|.
name|setWarning
argument_list|(
name|config
operator|.
name|isLazyValidation
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addValidationError
argument_list|(
name|error
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// else seems less than 2, so correct
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|validateICCProfileNEntry
parameter_list|(
name|COSStream
name|stream
parameter_list|,
name|ICC_Profile
name|iccp
parameter_list|)
block|{
name|COSDictionary
name|streamDict
init|=
operator|(
name|COSDictionary
operator|)
name|stream
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|streamDict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|N
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
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"/N entry of ICC profile is mandatory"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|COSBase
name|nValue
init|=
name|streamDict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|nValue
operator|instanceof
name|COSNumber
operator|)
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"/N entry of ICC profile must be a number, but is "
operator|+
name|nValue
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|int
name|nNumberValue
init|=
operator|(
operator|(
name|COSNumber
operator|)
name|nValue
operator|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|nNumberValue
operator|!=
literal|1
operator|&&
name|nNumberValue
operator|!=
literal|3
operator|&&
name|nNumberValue
operator|!=
literal|4
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"/N entry of ICC profile must be 1, 3 or 4, but is "
operator|+
name|nNumberValue
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|iccp
operator|.
name|getNumComponents
argument_list|()
operator|!=
name|nNumberValue
condition|)
block|{
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"/N entry of ICC profile is "
operator|+
name|nNumberValue
operator|+
literal|" but the ICC profile has "
operator|+
name|iccp
operator|.
name|getNumComponents
argument_list|()
operator|+
literal|" components"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|validateICCProfileAlternateEntry
parameter_list|(
name|PDICCBased
name|iccBased
parameter_list|)
throws|throws
name|IOException
block|{
name|PDColorSpace
name|altCS
init|=
name|iccBased
operator|.
name|getAlternateColorSpace
argument_list|()
decl_stmt|;
if|if
condition|(
name|altCS
operator|!=
literal|null
operator|&&
name|altCS
operator|.
name|getNumberOfComponents
argument_list|()
operator|!=
name|iccBased
operator|.
name|getNumberOfComponents
argument_list|()
condition|)
block|{
comment|// https://github.com/veraPDF/veraPDF-library/issues/773
name|context
operator|.
name|addValidationError
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|ERROR_GRAPHIC_OUTPUT_INTENT_INVALID_ENTRY
argument_list|,
literal|"/N entry of ICC profile is different ("
operator|+
name|iccBased
operator|.
name|getNumberOfComponents
argument_list|()
operator|+
literal|") than alternate entry colorspace component count ("
operator|+
name|altCS
operator|.
name|getNumberOfComponents
argument_list|()
operator|+
literal|")"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

