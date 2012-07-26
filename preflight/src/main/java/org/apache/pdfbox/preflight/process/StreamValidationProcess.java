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
name|process
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
name|ERROR_SYNTAX_STREAM_FX_KEYS
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
name|ERROR_SYNTAX_STREAM_INVALID_FILTER
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
name|ERROR_SYNTAX_STREAM_LENGTH_INVALID
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
name|ERROR_SYNTAX_STREAM_LENGTH_MISSING
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
name|List
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
name|io
operator|.
name|IOUtils
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
name|PDDocument
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
name|persistence
operator|.
name|util
operator|.
name|COSObjectKey
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
name|pdfbox
operator|.
name|preflight
operator|.
name|utils
operator|.
name|FilterHelper
import|;
end_import

begin_class
specifier|public
class|class
name|StreamValidationProcess
extends|extends
name|AbstractProcess
block|{
specifier|public
name|void
name|validate
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|)
throws|throws
name|ValidationException
block|{
name|PDDocument
name|pdfDoc
init|=
name|ctx
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|COSDocument
name|cDoc
init|=
name|pdfDoc
operator|.
name|getDocument
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|lCOSObj
init|=
name|cDoc
operator|.
name|getObjects
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|lCOSObj
control|)
block|{
name|COSObject
name|cObj
init|=
operator|(
name|COSObject
operator|)
name|o
decl_stmt|;
comment|/* 			 *  If this object represents a Stream, the Dictionary must contain the Length key 			 */
name|COSBase
name|cBase
init|=
name|cObj
operator|.
name|getObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|cBase
operator|instanceof
name|COSStream
condition|)
block|{
name|validateStreamObject
argument_list|(
name|ctx
argument_list|,
name|cObj
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|validateStreamObject
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSObject
name|cObj
parameter_list|)
throws|throws
name|ValidationException
block|{
name|COSStream
name|streamObj
init|=
operator|(
name|COSStream
operator|)
name|cObj
operator|.
name|getObject
argument_list|()
decl_stmt|;
comment|// ---- Check dictionary entries
comment|// ---- Only the Length entry is mandatory
comment|// ---- In a PDF/A file, F, FFilter and FDecodeParms are forbidden
name|checkDictionaryEntries
argument_list|(
name|context
argument_list|,
name|streamObj
argument_list|)
expr_stmt|;
comment|// ---- check stream length
name|checkStreamLength
argument_list|(
name|context
argument_list|,
name|cObj
argument_list|)
expr_stmt|;
comment|// ---- Check the Filter value(s)
name|checkFilters
argument_list|(
name|streamObj
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * This method checks if one of declared Filter is LZWdecode. If LZW is found, 	 * the result list is updated with an error code. 	 *  	 * @param stream 	 * @param handler 	 * @param result 	 */
specifier|protected
name|void
name|checkFilters
parameter_list|(
name|COSStream
name|stream
parameter_list|,
name|PreflightContext
name|context
parameter_list|)
block|{
name|COSBase
name|bFilter
init|=
name|stream
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|)
decl_stmt|;
if|if
condition|(
name|bFilter
operator|!=
literal|null
condition|)
block|{
name|COSDocument
name|cosDocument
init|=
name|context
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
decl_stmt|;
if|if
condition|(
name|COSUtils
operator|.
name|isArray
argument_list|(
name|bFilter
argument_list|,
name|cosDocument
argument_list|)
condition|)
block|{
name|COSArray
name|afName
init|=
operator|(
name|COSArray
operator|)
name|bFilter
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
name|afName
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|FilterHelper
operator|.
name|isAuthorizedFilter
argument_list|(
name|context
argument_list|,
name|afName
operator|.
name|getString
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|bFilter
operator|instanceof
name|COSName
condition|)
block|{
name|String
name|fName
init|=
operator|(
operator|(
name|COSName
operator|)
name|bFilter
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|FilterHelper
operator|.
name|isAuthorizedFilter
argument_list|(
name|context
argument_list|,
name|fName
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// ---- The filter type is invalid
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_STREAM_INVALID_FILTER
argument_list|,
literal|"Filter should be a Name or an Array"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|//  else Filter entry is optional
block|}
specifier|private
name|boolean
name|readUntilStream
parameter_list|(
name|InputStream
name|ra
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|search
init|=
literal|true
decl_stmt|;
comment|//    String stream = "";
name|boolean
name|maybe
init|=
literal|false
decl_stmt|;
name|int
name|lastChar
init|=
operator|-
literal|1
decl_stmt|;
do|do
block|{
name|int
name|c
init|=
name|ra
operator|.
name|read
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'s'
case|:
comment|//      stream = "s";
name|maybe
operator|=
literal|true
expr_stmt|;
name|lastChar
operator|=
name|c
expr_stmt|;
break|break;
case|case
literal|'t'
case|:
comment|//      if (maybe&& stream.endsWith("s")) {
if|if
condition|(
name|maybe
operator|&&
name|lastChar
operator|==
literal|'s'
condition|)
block|{
comment|//          stream = stream + "t";
name|lastChar
operator|=
name|c
expr_stmt|;
block|}
else|else
block|{
name|maybe
operator|=
literal|false
expr_stmt|;
name|lastChar
operator|=
operator|-
literal|1
expr_stmt|;
block|}
break|break;
case|case
literal|'r'
case|:
comment|// if (maybe&& stream.endsWith("t")) {
if|if
condition|(
name|maybe
operator|&&
name|lastChar
operator|==
literal|'t'
condition|)
block|{
comment|//        stream = stream + "r";
name|lastChar
operator|=
name|c
expr_stmt|;
block|}
else|else
block|{
name|maybe
operator|=
literal|false
expr_stmt|;
name|lastChar
operator|=
operator|-
literal|1
expr_stmt|;
block|}
break|break;
case|case
literal|'e'
case|:
comment|//      if (maybe&& stream.endsWith("r")) {
if|if
condition|(
name|maybe
operator|&&
name|lastChar
operator|==
literal|'r'
condition|)
block|{
name|lastChar
operator|=
name|c
expr_stmt|;
comment|//        stream = stream + "e";
block|}
else|else
block|{
name|maybe
operator|=
literal|false
expr_stmt|;
block|}
break|break;
case|case
literal|'a'
case|:
comment|//        if (maybe&& stream.endsWith("e")) {
if|if
condition|(
name|maybe
operator|&&
name|lastChar
operator|==
literal|'e'
condition|)
block|{
name|lastChar
operator|=
name|c
expr_stmt|;
comment|//        stream = stream + "a";
block|}
else|else
block|{
name|maybe
operator|=
literal|false
expr_stmt|;
block|}
break|break;
case|case
literal|'m'
case|:
comment|//        if (maybe&& stream.endsWith("a")) {
if|if
condition|(
name|maybe
operator|&&
name|lastChar
operator|==
literal|'a'
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
name|maybe
operator|=
literal|false
expr_stmt|;
block|}
break|break;
case|case
operator|-
literal|1
case|:
name|search
operator|=
literal|false
expr_stmt|;
break|break;
default|default:
name|maybe
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
do|while
condition|(
name|search
condition|)
do|;
return|return
literal|false
return|;
block|}
specifier|protected
name|void
name|checkStreamLength
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSObject
name|cObj
parameter_list|)
throws|throws
name|ValidationException
block|{
name|COSStream
name|streamObj
init|=
operator|(
name|COSStream
operator|)
name|cObj
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|int
name|length
init|=
name|streamObj
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|)
decl_stmt|;
name|InputStream
name|ra
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ra
operator|=
name|context
operator|.
name|getSource
argument_list|()
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|Long
name|offset
init|=
name|context
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocument
argument_list|()
operator|.
name|getXrefTable
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|COSObjectKey
argument_list|(
name|cObj
argument_list|)
argument_list|)
decl_stmt|;
comment|// ---- go to the beginning of the object
name|long
name|skipped
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|offset
operator|!=
literal|null
condition|)
block|{
while|while
condition|(
name|skipped
operator|!=
name|offset
condition|)
block|{
name|long
name|curSkip
init|=
name|ra
operator|.
name|skip
argument_list|(
name|offset
operator|-
name|skipped
argument_list|)
decl_stmt|;
if|if
condition|(
name|curSkip
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to skip bytes in the PDFFile to check stream length"
argument_list|)
throw|;
block|}
name|skipped
operator|+=
name|curSkip
expr_stmt|;
block|}
comment|// ---- go to the stream key word
if|if
condition|(
name|readUntilStream
argument_list|(
name|ra
argument_list|)
condition|)
block|{
name|int
name|c
init|=
name|ra
operator|.
name|read
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|==
literal|'\r'
condition|)
block|{
name|ra
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
comment|// else c is '\n' no more character to read
comment|// ---- Here is the true beginning of the Stream Content.
comment|// ---- Read the given length of bytes and check the 10 next bytes
comment|// ---- to see if there are endstream.
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|nbBytesToRead
init|=
name|length
decl_stmt|;
do|do
block|{
name|int
name|cr
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|nbBytesToRead
operator|>
literal|1024
condition|)
block|{
name|cr
operator|=
name|ra
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
literal|1024
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cr
operator|=
name|ra
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|nbBytesToRead
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cr
operator|==
operator|-
literal|1
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_STREAM_LENGTH_INVALID
argument_list|,
literal|"Stream length is invalide"
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
name|nbBytesToRead
operator|=
name|nbBytesToRead
operator|-
name|cr
expr_stmt|;
block|}
block|}
do|while
condition|(
name|nbBytesToRead
operator|>
literal|0
condition|)
do|;
name|int
name|len
init|=
literal|"endstream"
operator|.
name|length
argument_list|()
operator|+
literal|2
decl_stmt|;
name|byte
index|[]
name|buffer2
init|=
operator|new
name|byte
index|[
name|len
index|]
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
name|len
condition|;
operator|++
name|i
control|)
block|{
name|buffer2
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
name|ra
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
comment|// ---- check the content of 10 last characters
name|String
name|endStream
init|=
operator|new
name|String
argument_list|(
name|buffer2
argument_list|)
decl_stmt|;
if|if
condition|(
name|buffer2
index|[
literal|0
index|]
operator|==
literal|'\r'
operator|&&
name|buffer2
index|[
literal|1
index|]
operator|==
literal|'\n'
condition|)
block|{
if|if
condition|(
operator|!
name|endStream
operator|.
name|contains
argument_list|(
literal|"endstream"
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_STREAM_LENGTH_INVALID
argument_list|,
literal|"Stream length is invalide"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|buffer2
index|[
literal|0
index|]
operator|==
literal|'\r'
operator|&&
name|buffer2
index|[
literal|1
index|]
operator|==
literal|'e'
condition|)
block|{
if|if
condition|(
operator|!
name|endStream
operator|.
name|contains
argument_list|(
literal|"endstream"
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_STREAM_LENGTH_INVALID
argument_list|,
literal|"Stream length is invalide"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|buffer2
index|[
literal|0
index|]
operator|==
literal|'\n'
operator|&&
name|buffer2
index|[
literal|1
index|]
operator|==
literal|'e'
condition|)
block|{
if|if
condition|(
operator|!
name|endStream
operator|.
name|contains
argument_list|(
literal|"endstream"
argument_list|)
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_STREAM_LENGTH_INVALID
argument_list|,
literal|"Stream length is invalide"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_STREAM_LENGTH_INVALID
argument_list|,
literal|"Stream length is invalide"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_STREAM_LENGTH_INVALID
argument_list|,
literal|"Stream length is invalide"
argument_list|)
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
throw|throw
operator|new
name|ValidationException
argument_list|(
literal|"Unable to read a stream to validate it due to : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|ra
operator|!=
literal|null
condition|)
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|ra
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** 	 * Check dictionary entries. Only the Length entry is mandatory. In a PDF/A 	 * file, F, FFilter and FDecodeParms are forbidden 	 *  	 * @param streamObj 	 * @param result 	 */
specifier|protected
name|void
name|checkDictionaryEntries
parameter_list|(
name|PreflightContext
name|context
parameter_list|,
name|COSStream
name|streamObj
parameter_list|)
block|{
name|boolean
name|len
init|=
name|streamObj
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|)
decl_stmt|;
name|boolean
name|f
init|=
name|streamObj
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|F
argument_list|)
decl_stmt|;
name|boolean
name|ffilter
init|=
name|streamObj
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|F_FILTER
argument_list|)
decl_stmt|;
name|boolean
name|fdecParams
init|=
name|streamObj
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|F_DECODE_PARMS
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|len
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_STREAM_LENGTH_MISSING
argument_list|,
literal|"Stream length is missing"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|f
operator|||
name|ffilter
operator|||
name|fdecParams
condition|)
block|{
name|addValidationError
argument_list|(
name|context
argument_list|,
operator|new
name|ValidationError
argument_list|(
name|ERROR_SYNTAX_STREAM_FX_KEYS
argument_list|,
literal|"F, FFilter or FDecodeParms keys are present in the stream dictionary"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

