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
name|DOCUMENT_DICTIONARY_KEY_OUTPUT_INTENTS
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
name|ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_INVALID
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
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|color
operator|.
name|ICC_ColorSpace
import|;
end_import

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
name|PDDocumentCatalog
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
name|PDStream
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
name|PreflightDocument
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
name|utils
operator|.
name|COSUtils
import|;
end_import

begin_comment
comment|/**  * This class embeds an instance of java.awt.color.ICC_Profile which represent the ICCProfile defined by the  * DestOutputItents key of the OutputIntents of the PDF.  */
end_comment

begin_class
specifier|public
class|class
name|ICCProfileWrapper
block|{
comment|/**      * The ICCProfile extracted from DestOutputItents      */
specifier|private
specifier|final
name|ICC_Profile
name|profile
decl_stmt|;
comment|/**      * The ICC ColorSpace created using the ICCProfile      */
specifier|private
specifier|final
name|ICC_ColorSpace
name|colorSpace
decl_stmt|;
specifier|public
name|ICCProfileWrapper
parameter_list|(
specifier|final
name|ICC_Profile
name|_profile
parameter_list|)
block|{
name|this
operator|.
name|profile
operator|=
name|_profile
expr_stmt|;
name|this
operator|.
name|colorSpace
operator|=
operator|new
name|ICC_ColorSpace
argument_list|(
name|_profile
argument_list|)
expr_stmt|;
block|}
comment|/**      * Call the ICC_ColorSpace.getType method and return the value.      *       * @return      */
specifier|public
name|int
name|getColorSpaceType
parameter_list|()
block|{
return|return
name|colorSpace
operator|.
name|getType
argument_list|()
return|;
block|}
comment|/**      * @return the profile      */
specifier|public
name|ICC_Profile
name|getProfile
parameter_list|()
block|{
return|return
name|profile
return|;
block|}
comment|/**      * Return true if the ColourSpace is RGB      *       * @return      */
specifier|public
name|boolean
name|isRGBColorSpace
parameter_list|()
block|{
return|return
name|ICC_ColorSpace
operator|.
name|TYPE_RGB
operator|==
name|colorSpace
operator|.
name|getType
argument_list|()
return|;
block|}
comment|/**      * Return true if the ColourSpace is CMYK      *       * @return      */
specifier|public
name|boolean
name|isCMYKColorSpace
parameter_list|()
block|{
return|return
name|ICC_ColorSpace
operator|.
name|TYPE_CMYK
operator|==
name|colorSpace
operator|.
name|getType
argument_list|()
return|;
block|}
comment|/**      * Return true if the ColourSpace is Gray scale      *       * @return      */
specifier|public
name|boolean
name|isGrayColorSpace
parameter_list|()
block|{
return|return
name|ICC_ColorSpace
operator|.
name|TYPE_GRAY
operator|==
name|colorSpace
operator|.
name|getType
argument_list|()
return|;
block|}
comment|/**      * This method read all outputIntent dictionary until on of them have a destOutputProfile stream. This stream is      * parsed and is used to create a IccProfileWrapper.      *       * @param context      * @return an instance of ICCProfileWrapper or null if there are no DestOutputProfile      * @throws ValidationException      *             if an IOException occurs during the DestOutputProfile parsing      */
specifier|private
specifier|static
name|ICCProfileWrapper
name|searchFirstICCProfile
parameter_list|(
name|PreflightContext
name|context
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PreflightDocument
name|document
init|=
name|context
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|PDDocumentCatalog
name|catalog
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
decl_stmt|;
name|COSBase
name|cBase
init|=
name|catalog
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|DOCUMENT_DICTIONARY_KEY_OUTPUT_INTENTS
argument_list|)
argument_list|)
decl_stmt|;
name|COSArray
name|outputIntents
init|=
name|COSUtils
operator|.
name|getAsArray
argument_list|(
name|cBase
argument_list|,
name|document
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|outputIntents
operator|!=
literal|null
operator|&&
name|i
operator|<
name|outputIntents
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|COSDictionary
name|outputIntentDict
init|=
name|COSUtils
operator|.
name|getAsDictionary
argument_list|(
name|outputIntents
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|document
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
name|COSBase
name|destOutputProfile
init|=
name|outputIntentDict
operator|.
name|getItem
argument_list|(
name|OUTPUT_INTENT_DICTIONARY_KEY_DEST_OUTPUT_PROFILE
argument_list|)
decl_stmt|;
if|if
condition|(
name|destOutputProfile
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|PDStream
name|stream
init|=
name|PDStream
operator|.
name|createFromCOS
argument_list|(
name|COSUtils
operator|.
name|getAsStream
argument_list|(
name|destOutputProfile
argument_list|,
name|document
operator|.
name|getDocument
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|ICC_Profile
name|iccp
init|=
name|ICC_Profile
operator|.
name|getInstance
argument_list|(
name|stream
operator|.
name|getByteArray
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|ICCProfileWrapper
argument_list|(
name|iccp
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
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
name|ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_INVALID
argument_list|,
literal|"DestOutputProfile isn't a valid ICCProfile. Caused by : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
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
name|ERROR_GRAPHIC_OUTPUT_INTENT_ICC_PROFILE_INVALID
argument_list|,
literal|"Unable to parse the ICCProfile. Caused by : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|ICCProfileWrapper
name|getOrSearchICCProfile
parameter_list|(
name|PreflightContext
name|context
parameter_list|)
throws|throws
name|ValidationException
block|{
name|ICCProfileWrapper
name|profileWrapper
init|=
name|context
operator|.
name|getIccProfileWrapper
argument_list|()
decl_stmt|;
if|if
condition|(
name|profileWrapper
operator|==
literal|null
operator|&&
operator|!
name|context
operator|.
name|isIccProfileAlreadySearched
argument_list|()
condition|)
block|{
name|profileWrapper
operator|=
name|searchFirstICCProfile
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|context
operator|.
name|setIccProfileAlreadySearched
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|profileWrapper
return|;
block|}
block|}
end_class

end_unit

