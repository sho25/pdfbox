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
name|ERROR_GRAPHIC_TRANSPARENCY_SMASK
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

begin_comment
comment|/**  * This class processes commons validations of XObjects.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractXObjValidator
implements|implements
name|XObjectValidator
block|{
comment|/**      * The XObject to validate as a COSStream.      */
specifier|protected
name|COSStream
name|xobject
init|=
literal|null
decl_stmt|;
comment|/**      * The validation context which contains useful information to process validation.      */
specifier|protected
name|PreflightContext
name|context
init|=
literal|null
decl_stmt|;
specifier|public
name|AbstractXObjValidator
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSStream
name|xobj
parameter_list|)
block|{
name|this
operator|.
name|xobject
operator|=
name|xobj
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
comment|/**      * This method checks the SMask entry in the XObject dictionary. According to the PDF Reference, a SMask in a      * XObject is a Stream. So if it is not null, it should be an error but a SMask with the name None is authorized in      * the PDF/A Specification 6.4. If the validation fails (SMask not null and different from None), the error list is      * updated with the error code ERROR_GRAPHIC_TRANSPARENCY_SMASK (2.2.2).      *       */
specifier|protected
name|void
name|checkSMask
parameter_list|()
block|{
name|COSBase
name|smask
init|=
name|xobject
operator|.
name|getCOSDictionary
argument_list|(
name|COSName
operator|.
name|SMASK
argument_list|)
decl_stmt|;
if|if
condition|(
name|smask
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|smask
operator|instanceof
name|COSName
operator|&&
name|COSName
operator|.
name|NONE
operator|.
name|equals
argument_list|(
name|smask
argument_list|)
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
name|ERROR_GRAPHIC_TRANSPARENCY_SMASK
argument_list|,
literal|"Soft Mask must be null or None ["
operator|+
name|xobject
operator|.
name|toString
argument_list|()
operator|+
literal|"]"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * According the ISO 190005:1-2005 specification, a XObject can't have an OPI entry in its dictionary. If the      * XObject has a OPI entry, the error list is updated with the error code ERROR_GRAPHIC_UNEXPECTED_KEY (2.3).      *       */
specifier|protected
name|void
name|checkOPI
parameter_list|()
block|{
comment|// 6.2.4 and 6.2.5 no OPI
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
literal|"OPI"
argument_list|)
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
literal|"Unexpected 'OPI' Key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * According the ISO 190005:1-2005 specification, a XObject can't have an Ref entry in its dictionary. If the      * XObject has a Ref entry, the error list is updated with the error code ERROR_GRAPHIC_UNEXPECTED_KEY (2.3).      *       */
specifier|protected
name|void
name|checkReferenceXObject
parameter_list|()
block|{
comment|// 6.2.6 No reference XObject
if|if
condition|(
name|this
operator|.
name|xobject
operator|.
name|getItem
argument_list|(
literal|"Ref"
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
literal|"No reference XObject allowed in PDF/A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * According the ISO 190005:1-2005 specification, PostScript XObjects are forbidden. If the XObject is a PostScript      * XObject, the error list is updated with the error code ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY (2.3.2).      *       * To know whether the object is a Postscript XObject, "Subtype" and "Subtype2" entries are checked.      */
specifier|protected
name|void
name|checkPostscriptXObject
parameter_list|()
block|{
comment|// 6.2.7 No PostScript XObjects
name|COSName
name|subtype
init|=
name|this
operator|.
name|xobject
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|PS
operator|.
name|equals
argument_list|(
name|subtype
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
literal|"No Postscript XObject allowed in PDF/A"
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
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
literal|"Subtype2"
argument_list|)
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
name|ERROR_GRAPHIC_UNEXPECTED_VALUE_FOR_KEY
argument_list|,
literal|"No Postscript XObject allowed in PDF/A (Subtype2)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This method checks if required fields are present.      *       */
specifier|protected
specifier|abstract
name|void
name|checkMandatoryFields
parameter_list|()
function_decl|;
comment|/*      * (non-Javadoc)      *       * @see org.apache.pdfbox.preflight.graphic.XObjectValidator#validate()      */
annotation|@
name|Override
specifier|public
name|void
name|validate
parameter_list|()
throws|throws
name|ValidationException
block|{
name|checkMandatoryFields
argument_list|()
expr_stmt|;
name|checkOPI
argument_list|()
expr_stmt|;
name|checkSMask
argument_list|()
expr_stmt|;
name|checkReferenceXObject
argument_list|()
expr_stmt|;
name|checkPostscriptXObject
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

