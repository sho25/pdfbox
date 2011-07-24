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
name|helpers
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|ValidatorConfig
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
name|PdfElementParser
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
name|COSDocument
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
name|COSObject
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
name|COSString
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
name|PDDocument
import|;
end_import

begin_comment
comment|/**  * @author eric  *   */
end_comment

begin_class
specifier|public
class|class
name|TrailerValidationHelper
extends|extends
name|AbstractValidationHelper
block|{
specifier|public
name|TrailerValidationHelper
parameter_list|(
name|ValidatorConfig
name|cfg
parameter_list|)
throws|throws
name|ValidationException
block|{
name|super
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
block|}
comment|/*    * (non-Javadoc)    *     * @see    * net.awl.edoc.pdfa.validation.helpers.AbstractValidationHelper#validate(    * net.awl.edoc.pdfa.validation.DocumentHandler)    */
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ValidationError
argument_list|>
name|innerValidate
parameter_list|(
name|DocumentHandler
name|handler
parameter_list|)
throws|throws
name|ValidationException
block|{
name|List
argument_list|<
name|ValidationError
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|ValidationError
argument_list|>
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|PDDocument
name|pdfDoc
init|=
name|handler
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|COSDictionary
name|linearizedDict
init|=
name|isLinearizedPdf
argument_list|(
name|pdfDoc
argument_list|)
decl_stmt|;
if|if
condition|(
name|linearizedDict
operator|!=
literal|null
condition|)
block|{
comment|// it is a linearized PDF, check the linearized dictionary
name|checkLinearizedDictionnary
argument_list|(
name|linearizedDict
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// if the pdf is a linearized pdf. the first trailer must be checked
comment|// and it must have the same ID than the last trailer.
name|List
argument_list|<
name|String
argument_list|>
name|lTrailers
init|=
name|handler
operator|.
name|getPdfExtractor
argument_list|()
operator|.
name|getAllTrailers
argument_list|()
decl_stmt|;
name|String
name|firstTrailer
init|=
name|lTrailers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|lastTrailer
init|=
name|lTrailers
operator|.
name|get
argument_list|(
name|lTrailers
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|COSDictionary
name|first
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|last
init|=
literal|null
decl_stmt|;
name|COSDocument
name|cd
init|=
literal|null
decl_stmt|;
try|try
block|{
name|cd
operator|=
operator|new
name|COSDocument
argument_list|()
expr_stmt|;
name|PdfElementParser
name|parser1
init|=
operator|new
name|PdfElementParser
argument_list|(
name|cd
argument_list|,
name|firstTrailer
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|first
operator|=
name|parser1
operator|.
name|parseAsDictionary
argument_list|()
expr_stmt|;
name|PdfElementParser
name|parser2
init|=
operator|new
name|PdfElementParser
argument_list|(
name|cd
argument_list|,
name|lastTrailer
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|last
operator|=
name|parser2
operator|.
name|parseAsDictionary
argument_list|()
expr_stmt|;
name|checkMainTrailer
argument_list|(
name|pdfDoc
operator|.
name|getDocument
argument_list|()
argument_list|,
name|first
argument_list|,
name|result
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|compareIds
argument_list|(
name|first
argument_list|,
name|last
argument_list|,
name|pdfDoc
operator|.
name|getDocument
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER_ID_CONSISTENCY
argument_list|,
literal|"ID is different in the first and the last trailer"
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
name|result
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER
argument_list|,
literal|"Unable to parse trailers of the linearized PDF"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|COSUtils
operator|.
name|closeDocumentQuietly
argument_list|(
name|cd
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// If the PDF isn't a linearized one, only the last trailer must be
comment|// checked
name|List
argument_list|<
name|String
argument_list|>
name|lTrailers
init|=
name|handler
operator|.
name|getPdfExtractor
argument_list|()
operator|.
name|getAllTrailers
argument_list|()
decl_stmt|;
name|String
name|lastTrailer
init|=
name|lTrailers
operator|.
name|get
argument_list|(
name|lTrailers
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|COSDocument
name|cd
init|=
literal|null
decl_stmt|;
try|try
block|{
name|cd
operator|=
operator|new
name|COSDocument
argument_list|()
expr_stmt|;
name|PdfElementParser
name|parser
init|=
operator|new
name|PdfElementParser
argument_list|(
name|cd
argument_list|,
name|lastTrailer
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|COSDictionary
name|trailer
init|=
name|parser
operator|.
name|parseAsDictionary
argument_list|()
decl_stmt|;
name|checkMainTrailer
argument_list|(
name|pdfDoc
operator|.
name|getDocument
argument_list|()
argument_list|,
name|trailer
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|result
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER
argument_list|,
literal|"The trailer dictionary is missing"
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|cd
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|COSUtils
operator|.
name|closeDocumentQuietly
argument_list|(
name|cd
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**    * Return true if the ID of the first dictionary is the same as the id of the    * last dictionary Return false otherwise.    *     * @param first    * @param last    * @return    */
specifier|protected
name|boolean
name|compareIds
parameter_list|(
name|COSDictionary
name|first
parameter_list|,
name|COSDictionary
name|last
parameter_list|,
name|COSDocument
name|doc
parameter_list|)
block|{
name|COSBase
name|idFirst
init|=
name|first
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|TRAILER_DICTIONARY_KEY_ID
argument_list|)
argument_list|)
decl_stmt|;
name|COSBase
name|idLast
init|=
name|last
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|TRAILER_DICTIONARY_KEY_ID
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|idFirst
operator|==
literal|null
operator|||
name|idLast
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// ---- cast two COSBase to COSArray.
name|COSArray
name|af
init|=
name|COSUtils
operator|.
name|getAsArray
argument_list|(
name|idFirst
argument_list|,
name|doc
argument_list|)
decl_stmt|;
name|COSArray
name|al
init|=
name|COSUtils
operator|.
name|getAsArray
argument_list|(
name|idLast
argument_list|,
name|doc
argument_list|)
decl_stmt|;
comment|// ---- if one COSArray is null, the PDF/A isn't valid
if|if
condition|(
operator|(
name|af
operator|==
literal|null
operator|)
operator|||
operator|(
name|al
operator|==
literal|null
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// ---- compare both arrays
name|boolean
name|isEqual
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Object
name|of
range|:
name|af
operator|.
name|toList
argument_list|()
control|)
block|{
name|boolean
name|oneIsEquals
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|ol
range|:
name|al
operator|.
name|toList
argument_list|()
control|)
block|{
comment|// ---- according to PDF Reference 1-4, ID is an array containing two
comment|// strings
if|if
condition|(
operator|!
name|oneIsEquals
condition|)
name|oneIsEquals
operator|=
operator|(
operator|(
name|COSString
operator|)
name|ol
operator|)
operator|.
name|getString
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|COSString
operator|)
name|of
operator|)
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|isEqual
operator|=
name|isEqual
operator|&&
name|oneIsEquals
expr_stmt|;
block|}
return|return
name|isEqual
return|;
block|}
comment|/**    * check if all keys are authorized in a trailer dictionary and if the type is    * valid.    *     * @param trailer    * @param lErrors    */
specifier|protected
name|void
name|checkMainTrailer
parameter_list|(
name|COSDocument
name|doc
parameter_list|,
name|COSDictionary
name|trailer
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lErrors
parameter_list|)
block|{
name|boolean
name|id
init|=
literal|false
decl_stmt|;
name|boolean
name|root
init|=
literal|false
decl_stmt|;
name|boolean
name|size
init|=
literal|false
decl_stmt|;
name|boolean
name|prev
init|=
literal|false
decl_stmt|;
name|boolean
name|info
init|=
literal|false
decl_stmt|;
name|boolean
name|encrypt
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|trailer
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|key
operator|instanceof
name|COSName
operator|)
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_DICTIONARY_KEY_INVALID
argument_list|,
literal|"Invalid key in The trailer dictionary"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|cosName
init|=
operator|(
operator|(
name|COSName
operator|)
name|key
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|TRAILER_DICTIONARY_KEY_ENCRYPT
argument_list|)
condition|)
block|{
name|encrypt
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|TRAILER_DICTIONARY_KEY_SIZE
argument_list|)
condition|)
block|{
name|size
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|TRAILER_DICTIONARY_KEY_PREV
argument_list|)
condition|)
block|{
name|prev
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|TRAILER_DICTIONARY_KEY_ROOT
argument_list|)
condition|)
block|{
name|root
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|TRAILER_DICTIONARY_KEY_INFO
argument_list|)
condition|)
block|{
name|info
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|TRAILER_DICTIONARY_KEY_ID
argument_list|)
condition|)
block|{
name|id
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// ---- PDF/A Trailer dictionary must contain the ID key
if|if
condition|(
operator|!
name|id
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER_MISSING_ID
argument_list|,
literal|"The trailer dictionary doesn't contain ID"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|COSBase
name|trailerId
init|=
name|trailer
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|TRAILER_DICTIONARY_KEY_ID
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSUtils
operator|.
name|isArray
argument_list|(
name|trailerId
argument_list|,
name|doc
argument_list|)
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER_TYPE_INVALID
argument_list|,
literal|"The trailer dictionary contains an id but it isn't an array"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ---- PDF/A Trailer dictionary mustn't contain the Encrypt key
if|if
condition|(
name|encrypt
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER_ENCRYPT
argument_list|,
literal|"The trailer dictionary contains Encrypt"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// ---- PDF Trailer dictionary must contain the Size key
if|if
condition|(
operator|!
name|size
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER_MISSING_SIZE
argument_list|,
literal|"The trailer dictionary doesn't contain Size"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|COSBase
name|trailerSize
init|=
name|trailer
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|TRAILER_DICTIONARY_KEY_SIZE
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSUtils
operator|.
name|isInteger
argument_list|(
name|trailerSize
argument_list|,
name|doc
argument_list|)
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER_TYPE_INVALID
argument_list|,
literal|"The trailer dictionary contains a size but it isn't an integer"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ---- PDF Trailer dictionary must contain the Root key
if|if
condition|(
operator|!
name|root
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER_MISSING_ROOT
argument_list|,
literal|"The trailer dictionary doesn't contain Root"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|COSBase
name|trailerRoot
init|=
name|trailer
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|TRAILER_DICTIONARY_KEY_ROOT
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSUtils
operator|.
name|isDictionary
argument_list|(
name|trailerRoot
argument_list|,
name|doc
argument_list|)
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER_TYPE_INVALID
argument_list|,
literal|"The trailer dictionary contains a root but it isn't a dictionary"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ---- PDF Trailer dictionary may contain the Prev key
if|if
condition|(
name|prev
condition|)
block|{
name|COSBase
name|trailerPrev
init|=
name|trailer
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|TRAILER_DICTIONARY_KEY_PREV
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSUtils
operator|.
name|isInteger
argument_list|(
name|trailerPrev
argument_list|,
name|doc
argument_list|)
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER_TYPE_INVALID
argument_list|,
literal|"The trailer dictionary contains a prev but it isn't an integer"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// ---- PDF Trailer dictionary may contain the Info key
if|if
condition|(
name|info
condition|)
block|{
name|COSBase
name|trailerInfo
init|=
name|trailer
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|TRAILER_DICTIONARY_KEY_INFO
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|COSUtils
operator|.
name|isDictionary
argument_list|(
name|trailerInfo
argument_list|,
name|doc
argument_list|)
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_TRAILER_TYPE_INVALID
argument_list|,
literal|"The trailer dictionary contains an info but it isn't a dictionary"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**    * According to the PDF Reference, A linearized PDF contain a dictionary as    * first object (linearized dictionary) and only this one in the first    * section.    *     * @param document    * @return    */
specifier|protected
name|COSDictionary
name|isLinearizedPdf
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
comment|// ---- Get Ref to obj
name|COSDocument
name|cDoc
init|=
name|document
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|lObj
init|=
name|cDoc
operator|.
name|getObjects
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|object
range|:
name|lObj
control|)
block|{
name|COSBase
name|curObj
init|=
operator|(
operator|(
name|COSObject
operator|)
name|object
operator|)
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|curObj
operator|instanceof
name|COSDictionary
operator|&&
operator|(
operator|(
name|COSDictionary
operator|)
name|curObj
operator|)
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|DICTIONARY_KEY_LINEARIZED
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|(
name|COSDictionary
operator|)
name|curObj
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**    * Check if mandatory keys of linearized dictionary are present.    *     * @param lErrors    */
specifier|protected
name|void
name|checkLinearizedDictionnary
parameter_list|(
name|COSDictionary
name|linearizedDict
parameter_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
name|lErrors
parameter_list|)
block|{
comment|// ---- check if all keys are authorized in a linearized dictionary
comment|// ---- Linearized dictionary must contain the lhoent keys
name|boolean
name|l
init|=
literal|false
decl_stmt|;
name|boolean
name|h
init|=
literal|false
decl_stmt|;
name|boolean
name|o
init|=
literal|false
decl_stmt|;
name|boolean
name|e
init|=
literal|false
decl_stmt|;
name|boolean
name|n
init|=
literal|false
decl_stmt|;
name|boolean
name|t
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|linearizedDict
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|key
operator|instanceof
name|COSName
operator|)
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_DICTIONARY_KEY_INVALID
argument_list|,
literal|"Invalid key in The Linearized dictionary"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|cosName
init|=
operator|(
operator|(
name|COSName
operator|)
name|key
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|DICTIONARY_KEY_LINEARIZED_L
argument_list|)
condition|)
block|{
name|l
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|DICTIONARY_KEY_LINEARIZED_H
argument_list|)
condition|)
block|{
name|h
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|DICTIONARY_KEY_LINEARIZED_O
argument_list|)
condition|)
block|{
name|o
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|DICTIONARY_KEY_LINEARIZED_E
argument_list|)
condition|)
block|{
name|e
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|DICTIONARY_KEY_LINEARIZED_N
argument_list|)
condition|)
block|{
name|n
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cosName
operator|.
name|equals
argument_list|(
name|DICTIONARY_KEY_LINEARIZED_T
argument_list|)
condition|)
block|{
name|t
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
operator|(
name|l
operator|&&
name|h
operator|&&
name|o
operator|&&
name|e
operator|&&
name|t
operator|&&
name|n
operator|)
condition|)
block|{
name|lErrors
operator|.
name|add
argument_list|(
operator|new
name|ValidationResult
operator|.
name|ValidationError
argument_list|(
name|ValidationConstants
operator|.
name|ERROR_SYNTAX_DICT_INVALID
argument_list|,
literal|"Invalid key in The Linearized dictionary"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
block|}
end_class

end_unit

