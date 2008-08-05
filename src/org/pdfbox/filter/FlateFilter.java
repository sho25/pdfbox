begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|filter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|DeflaterOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|InflaterInputStream
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|cos
operator|.
name|COSDictionary
import|;
end_import

begin_comment
comment|/**  * This is the used for the FlateDecode filter.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author Marcel Kammer  * @version $Revision: 1.12 $  */
end_comment

begin_class
specifier|public
class|class
name|FlateFilter
implements|implements
name|Filter
block|{
specifier|private
specifier|static
specifier|final
name|int
name|BUFFER_SIZE
init|=
literal|2048
decl_stmt|;
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|decode
parameter_list|(
name|InputStream
name|compressedData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
name|COSBase
name|baseObj
init|=
name|options
operator|.
name|getDictionaryObject
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"DecodeParms"
block|,
literal|"DP"
block|}
argument_list|)
decl_stmt|;
name|COSDictionary
name|dict
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|baseObj
operator|instanceof
name|COSDictionary
condition|)
block|{
name|dict
operator|=
operator|(
name|COSDictionary
operator|)
name|baseObj
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|baseObj
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|paramArray
init|=
operator|(
name|COSArray
operator|)
name|baseObj
decl_stmt|;
if|if
condition|(
name|filterIndex
operator|<
name|paramArray
operator|.
name|size
argument_list|()
condition|)
block|{
name|dict
operator|=
operator|(
name|COSDictionary
operator|)
name|paramArray
operator|.
name|getObject
argument_list|(
name|filterIndex
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|baseObj
operator|==
literal|null
condition|)
block|{
comment|//do nothing
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Expected COSArray or COSDictionary and not "
operator|+
name|baseObj
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|int
name|predictor
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|colors
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|bitsPerPixel
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|columns
init|=
operator|-
literal|1
decl_stmt|;
name|InflaterInputStream
name|decompressor
init|=
literal|null
decl_stmt|;
name|ByteArrayInputStream
name|bais
init|=
literal|null
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dict
operator|!=
literal|null
condition|)
block|{
name|predictor
operator|=
name|dict
operator|.
name|getInt
argument_list|(
literal|"Predictor"
argument_list|)
expr_stmt|;
name|colors
operator|=
name|dict
operator|.
name|getInt
argument_list|(
literal|"Colors"
argument_list|)
expr_stmt|;
name|bitsPerPixel
operator|=
name|options
operator|.
name|getInt
argument_list|(
literal|"BitsPerComponent"
argument_list|)
expr_stmt|;
name|columns
operator|=
name|dict
operator|.
name|getInt
argument_list|(
literal|"Columns"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// Decompress data to temporary ByteArrayOutputStream
name|decompressor
operator|=
operator|new
name|InflaterInputStream
argument_list|(
name|compressedData
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|BUFFER_SIZE
index|]
decl_stmt|;
name|int
name|amountRead
decl_stmt|;
comment|// Decode data using given predictor
if|if
condition|(
name|predictor
operator|==
operator|-
literal|1
operator|||
name|predictor
operator|==
literal|1
operator|||
name|predictor
operator|==
literal|10
condition|)
block|{
comment|// decoding not needed
while|while
condition|(
operator|(
name|amountRead
operator|=
name|decompressor
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|BUFFER_SIZE
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|result
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|colors
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Could not read 'colors' attribute to decompress flate stream."
argument_list|)
throw|;
block|}
if|if
condition|(
name|bitsPerPixel
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Could not read 'bitsPerPixel' attribute to decompress flate stream."
argument_list|)
throw|;
block|}
if|if
condition|(
name|columns
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Could not read 'columns' attribute to decompress flate stream."
argument_list|)
throw|;
block|}
name|baos
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|decompressor
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|BUFFER_SIZE
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|baos
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
name|baos
operator|.
name|flush
argument_list|()
expr_stmt|;
comment|// Copy data to ByteArrayInputStream for reading
name|bais
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|baos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|baos
operator|.
name|close
argument_list|()
expr_stmt|;
name|baos
operator|=
literal|null
expr_stmt|;
name|byte
index|[]
name|decodedData
init|=
name|decodePredictor
argument_list|(
name|predictor
argument_list|,
name|colors
argument_list|,
name|bitsPerPixel
argument_list|,
name|columns
argument_list|,
name|bais
argument_list|)
decl_stmt|;
name|bais
operator|.
name|close
argument_list|()
expr_stmt|;
name|bais
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|decodedData
argument_list|)
expr_stmt|;
comment|// write decoded data to result
while|while
condition|(
operator|(
name|amountRead
operator|=
name|bais
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|result
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
name|bais
operator|.
name|close
argument_list|()
expr_stmt|;
name|bais
operator|=
literal|null
expr_stmt|;
block|}
name|result
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|decompressor
operator|!=
literal|null
condition|)
block|{
name|decompressor
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|bais
operator|!=
literal|null
condition|)
block|{
name|bais
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|baos
operator|!=
literal|null
condition|)
block|{
name|baos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|byte
index|[]
name|decodePredictor
parameter_list|(
name|int
name|predictor
parameter_list|,
name|int
name|colors
parameter_list|,
name|int
name|bitsPerComponent
parameter_list|,
name|int
name|columns
parameter_list|,
name|InputStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|2048
index|]
decl_stmt|;
if|if
condition|(
name|predictor
operator|==
literal|1
operator|||
name|predictor
operator|==
literal|10
condition|)
block|{
comment|// No prediction or PNG NONE
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|i
operator|=
name|data
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|baos
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// calculate sizes
name|int
name|bpp
init|=
operator|(
name|colors
operator|*
name|bitsPerComponent
operator|+
literal|7
operator|)
operator|/
literal|8
decl_stmt|;
name|int
name|rowlength
init|=
operator|(
name|columns
operator|*
name|colors
operator|*
name|bitsPerComponent
operator|+
literal|7
operator|)
operator|/
literal|8
operator|+
name|bpp
decl_stmt|;
name|byte
index|[]
name|actline
init|=
operator|new
name|byte
index|[
name|rowlength
index|]
decl_stmt|;
name|byte
index|[]
name|lastline
init|=
operator|new
name|byte
index|[
name|rowlength
index|]
decl_stmt|;
comment|// Initialize lastline with
comment|// Zeros according to
comment|// PNG-specification
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|int
name|linepredictor
init|=
name|predictor
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
if|if
condition|(
name|predictor
operator|==
literal|15
condition|)
block|{
name|linepredictor
operator|=
name|data
operator|.
name|read
argument_list|()
expr_stmt|;
comment|// read per line predictor
if|if
condition|(
name|linepredictor
operator|==
operator|-
literal|1
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
comment|// reached EOF
break|break;
block|}
else|else
block|{
name|linepredictor
operator|+=
literal|10
expr_stmt|;
comment|// add 10 to tread value 1 as 11
block|}
comment|// (instead of PRED NONE) and 2
comment|// as 12 (instead of PRED TIFF)
block|}
comment|// read line
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|offset
init|=
name|bpp
decl_stmt|;
while|while
condition|(
name|offset
operator|<
name|rowlength
operator|&&
operator|(
operator|(
name|i
operator|=
name|data
operator|.
name|read
argument_list|(
name|actline
argument_list|,
name|offset
argument_list|,
name|rowlength
operator|-
name|offset
argument_list|)
operator|)
operator|!=
operator|-
literal|1
operator|)
condition|)
block|{
name|offset
operator|+=
name|i
expr_stmt|;
block|}
comment|// Do prediction as specified in PNG-Specification 1.2
switch|switch
condition|(
name|linepredictor
condition|)
block|{
case|case
literal|2
case|:
comment|// PRED TIFF SUB
comment|/**                          * @todo decode tiff                          */
throw|throw
operator|new
name|IOException
argument_list|(
literal|"TIFF-Predictor not supported"
argument_list|)
throw|;
case|case
literal|11
case|:
comment|// PRED SUB
for|for
control|(
name|int
name|p
init|=
name|bpp
init|;
name|p
operator|<
name|rowlength
condition|;
name|p
operator|++
control|)
block|{
name|int
name|sub
init|=
name|actline
index|[
name|p
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|left
init|=
name|actline
index|[
name|p
operator|-
name|bpp
index|]
operator|&
literal|0xff
decl_stmt|;
name|actline
index|[
name|p
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|sub
operator|+
name|left
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
literal|12
case|:
comment|// PRED UP
for|for
control|(
name|int
name|p
init|=
name|bpp
init|;
name|p
operator|<
name|rowlength
condition|;
name|p
operator|++
control|)
block|{
name|int
name|up
init|=
name|actline
index|[
name|p
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|prior
init|=
name|lastline
index|[
name|p
index|]
operator|&
literal|0xff
decl_stmt|;
name|actline
index|[
name|p
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|up
operator|+
name|prior
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
literal|13
case|:
comment|// PRED AVG
for|for
control|(
name|int
name|p
init|=
name|bpp
init|;
name|p
operator|<
name|rowlength
condition|;
name|p
operator|++
control|)
block|{
name|int
name|avg
init|=
name|actline
index|[
name|p
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|left
init|=
name|actline
index|[
name|p
operator|-
name|bpp
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|up
init|=
name|lastline
index|[
name|p
index|]
operator|&
literal|0xff
decl_stmt|;
name|actline
index|[
name|p
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|avg
operator|+
operator|(
operator|(
name|left
operator|+
name|up
operator|)
operator|/
literal|2
operator|)
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
literal|14
case|:
comment|// PRED PAETH
for|for
control|(
name|int
name|p
init|=
name|bpp
init|;
name|p
operator|<
name|rowlength
condition|;
name|p
operator|++
control|)
block|{
name|int
name|paeth
init|=
name|actline
index|[
name|p
index|]
operator|&
literal|0xff
decl_stmt|;
name|int
name|a
init|=
name|actline
index|[
name|p
operator|-
name|bpp
index|]
operator|&
literal|0xff
decl_stmt|;
comment|// left
name|int
name|b
init|=
name|lastline
index|[
name|p
index|]
operator|&
literal|0xff
decl_stmt|;
comment|// upper
name|int
name|c
init|=
name|lastline
index|[
name|p
operator|-
name|bpp
index|]
operator|&
literal|0xff
decl_stmt|;
comment|// upperleft
name|int
name|value
init|=
name|a
operator|+
name|b
operator|-
name|c
decl_stmt|;
name|int
name|absa
init|=
name|Math
operator|.
name|abs
argument_list|(
name|value
operator|-
name|a
argument_list|)
decl_stmt|;
name|int
name|absb
init|=
name|Math
operator|.
name|abs
argument_list|(
name|value
operator|-
name|b
argument_list|)
decl_stmt|;
name|int
name|absc
init|=
name|Math
operator|.
name|abs
argument_list|(
name|value
operator|-
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
name|absa
operator|<=
name|absb
operator|&&
name|absa
operator|<=
name|absc
condition|)
block|{
name|actline
index|[
name|p
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|paeth
operator|+
name|absa
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|absb
operator|<=
name|absc
condition|)
block|{
name|actline
index|[
name|p
index|]
operator|+=
call|(
name|byte
call|)
argument_list|(
name|paeth
operator|+
name|absb
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|actline
index|[
name|p
index|]
operator|+=
call|(
name|byte
call|)
argument_list|(
name|paeth
operator|+
name|absc
argument_list|)
expr_stmt|;
block|}
block|}
break|break;
default|default:
break|break;
block|}
name|lastline
operator|=
name|actline
expr_stmt|;
name|baos
operator|.
name|write
argument_list|(
name|actline
argument_list|,
name|bpp
argument_list|,
name|actline
operator|.
name|length
operator|-
name|bpp
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|encode
parameter_list|(
name|InputStream
name|rawData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
block|{
name|DeflaterOutputStream
name|out
init|=
operator|new
name|DeflaterOutputStream
argument_list|(
name|result
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|BUFFER_SIZE
index|]
decl_stmt|;
name|int
name|amountRead
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|rawData
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|BUFFER_SIZE
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|result
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

