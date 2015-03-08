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
name|common
operator|.
name|function
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
name|common
operator|.
name|COSObjectable
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
name|PDRange
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

begin_comment
comment|/**  * This class represents a function in a PDF document.  *  * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDFunction
implements|implements
name|COSObjectable
block|{
specifier|private
name|PDStream
name|functionStream
init|=
literal|null
decl_stmt|;
specifier|private
name|COSDictionary
name|functionDictionary
init|=
literal|null
decl_stmt|;
specifier|private
name|COSArray
name|domain
init|=
literal|null
decl_stmt|;
specifier|private
name|COSArray
name|range
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|numberOfInputValues
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|int
name|numberOfOutputValues
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * Constructor.      *      * @param function The function stream.      *       */
specifier|public
name|PDFunction
parameter_list|(
name|COSBase
name|function
parameter_list|)
block|{
if|if
condition|(
name|function
operator|instanceof
name|COSStream
condition|)
block|{
name|functionStream
operator|=
operator|new
name|PDStream
argument_list|(
operator|(
name|COSStream
operator|)
name|function
argument_list|)
expr_stmt|;
name|functionStream
operator|.
name|getStream
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|FUNCTION
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|function
operator|instanceof
name|COSDictionary
condition|)
block|{
name|functionDictionary
operator|=
operator|(
name|COSDictionary
operator|)
name|function
expr_stmt|;
block|}
block|}
comment|/**      * Returns the function type.      *       * Possible values are:      *       * 0 - Sampled function      * 2 - Exponential interpolation function      * 3 - Stitching function      * 4 - PostScript calculator function      *       * @return the function type.      */
specifier|public
specifier|abstract
name|int
name|getFunctionType
parameter_list|()
function_decl|;
comment|/**      * Returns the COSObject.      *      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
if|if
condition|(
name|functionStream
operator|!=
literal|null
condition|)
block|{
return|return
name|functionStream
operator|.
name|getCOSObject
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|functionDictionary
return|;
block|}
block|}
comment|/**      * Returns the stream.      * @return The stream for this object.      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
if|if
condition|(
name|functionStream
operator|!=
literal|null
condition|)
block|{
return|return
name|functionStream
operator|.
name|getStream
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|functionDictionary
return|;
block|}
block|}
comment|/**      * Returns the underlying PDStream.      * @return The stream.      */
specifier|protected
name|PDStream
name|getPDStream
parameter_list|()
block|{
return|return
name|functionStream
return|;
block|}
comment|/**      * Create the correct PD Model function based on the COS base function.      *      * @param function The COS function dictionary.      *      * @return The PDModel Function object.      *      * @throws IOException If we are unable to create the PDFunction object.      */
specifier|public
specifier|static
name|PDFunction
name|create
parameter_list|(
name|COSBase
name|function
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|function
operator|==
name|COSName
operator|.
name|IDENTITY
condition|)
block|{
return|return
operator|new
name|PDFunctionTypeIdentity
argument_list|(
literal|null
argument_list|)
return|;
block|}
name|PDFunction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|function
operator|instanceof
name|COSObject
condition|)
block|{
name|function
operator|=
operator|(
operator|(
name|COSObject
operator|)
name|function
operator|)
operator|.
name|getObject
argument_list|()
expr_stmt|;
block|}
name|COSDictionary
name|functionDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|function
decl_stmt|;
name|int
name|functionType
init|=
name|functionDictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|FUNCTION_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|functionType
operator|==
literal|0
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFunctionType0
argument_list|(
name|functionDictionary
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|functionType
operator|==
literal|2
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFunctionType2
argument_list|(
name|functionDictionary
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|functionType
operator|==
literal|3
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFunctionType3
argument_list|(
name|functionDictionary
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|functionType
operator|==
literal|4
condition|)
block|{
name|retval
operator|=
operator|new
name|PDFunctionType4
argument_list|(
name|functionDictionary
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Unknown function type "
operator|+
name|functionType
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the number of output parameters that      * have a range specified.  A range for output parameters      * is optional so this may return zero for a function      * that does have output parameters, this will simply return the      * number that have the rnage specified.      *      * @return The number of input parameters that have a range      * specified.      */
specifier|public
name|int
name|getNumberOfOutputParameters
parameter_list|()
block|{
if|if
condition|(
name|numberOfOutputValues
operator|==
operator|-
literal|1
condition|)
block|{
name|COSArray
name|rangeValues
init|=
name|getRangeValues
argument_list|()
decl_stmt|;
name|numberOfOutputValues
operator|=
name|rangeValues
operator|.
name|size
argument_list|()
operator|/
literal|2
expr_stmt|;
block|}
return|return
name|numberOfOutputValues
return|;
block|}
comment|/**      * This will get the range for a certain output parameters.  This is will never      * return null.  If it is not present then the range 0 to 0 will      * be returned.      *      * @param n The output parameter number to get the range for.      *      * @return The range for this component.      */
specifier|public
name|PDRange
name|getRangeForOutput
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|COSArray
name|rangeValues
init|=
name|getRangeValues
argument_list|()
decl_stmt|;
return|return
operator|new
name|PDRange
argument_list|(
name|rangeValues
argument_list|,
name|n
argument_list|)
return|;
block|}
comment|/**      * This will set the range values.      *      * @param rangeValues The new range values.      */
specifier|public
name|void
name|setRangeValues
parameter_list|(
name|COSArray
name|rangeValues
parameter_list|)
block|{
name|range
operator|=
name|rangeValues
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|,
name|rangeValues
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the number of input parameters that      * have a domain specified.      *      * @return The number of input parameters that have a domain      * specified.      */
specifier|public
name|int
name|getNumberOfInputParameters
parameter_list|()
block|{
if|if
condition|(
name|numberOfInputValues
operator|==
operator|-
literal|1
condition|)
block|{
name|COSArray
name|array
init|=
name|getDomainValues
argument_list|()
decl_stmt|;
name|numberOfInputValues
operator|=
name|array
operator|.
name|size
argument_list|()
operator|/
literal|2
expr_stmt|;
block|}
return|return
name|numberOfInputValues
return|;
block|}
comment|/**      * This will get the range for a certain input parameter.  This is will never      * return null.  If it is not present then the range 0 to 0 will      * be returned.      *      * @param n The parameter number to get the domain for.      *      * @return The domain range for this component.      */
specifier|public
name|PDRange
name|getDomainForInput
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|COSArray
name|domainValues
init|=
name|getDomainValues
argument_list|()
decl_stmt|;
return|return
operator|new
name|PDRange
argument_list|(
name|domainValues
argument_list|,
name|n
argument_list|)
return|;
block|}
comment|/**      * This will set the domain values.      *      * @param domainValues The new domain values.      */
specifier|public
name|void
name|setDomainValues
parameter_list|(
name|COSArray
name|domainValues
parameter_list|)
block|{
name|domain
operator|=
name|domainValues
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DOMAIN
argument_list|,
name|domainValues
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated Replaced by {@link #eval(float[] input)}      */
specifier|public
name|COSArray
name|eval
parameter_list|(
name|COSArray
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|float
index|[]
name|outputValues
init|=
name|eval
argument_list|(
name|input
operator|.
name|toFloatArray
argument_list|()
argument_list|)
decl_stmt|;
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|array
operator|.
name|setFloatArray
argument_list|(
name|outputValues
argument_list|)
expr_stmt|;
return|return
name|array
return|;
block|}
comment|/**      * Evaluates the function at the given input.      * ReturnValue = f(input)      *      * @param input The array of input values for the function.       * In many cases will be an array of a single value, but not always.      *       * @return The of outputs the function returns based on those inputs.       * In many cases will be an array of a single value, but not always.      *       * @throws IOException an IOExcpetion is thrown if something went wrong processing the function.        */
specifier|public
specifier|abstract
name|float
index|[]
name|eval
parameter_list|(
name|float
index|[]
name|input
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns all ranges for the output values as COSArray .      * Required for type 0 and type 4 functions      * @return the ranges array.       */
specifier|protected
name|COSArray
name|getRangeValues
parameter_list|()
block|{
if|if
condition|(
name|range
operator|==
literal|null
condition|)
block|{
name|range
operator|=
operator|(
name|COSArray
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|)
expr_stmt|;
block|}
return|return
name|range
return|;
block|}
comment|/**      * Returns all domains for the input values as COSArray.      * Required for all function types.      * @return the domains array.       */
specifier|private
name|COSArray
name|getDomainValues
parameter_list|()
block|{
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
name|domain
operator|=
operator|(
name|COSArray
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DOMAIN
argument_list|)
expr_stmt|;
block|}
return|return
name|domain
return|;
block|}
comment|/**      * Clip the given input values to the ranges.      *       * @param inputValues the input values      * @return the clipped values      */
specifier|protected
name|float
index|[]
name|clipToRange
parameter_list|(
name|float
index|[]
name|inputValues
parameter_list|)
block|{
name|COSArray
name|rangesArray
init|=
name|getRangeValues
argument_list|()
decl_stmt|;
name|float
index|[]
name|result
decl_stmt|;
if|if
condition|(
name|rangesArray
operator|!=
literal|null
condition|)
block|{
name|float
index|[]
name|rangeValues
init|=
name|rangesArray
operator|.
name|toFloatArray
argument_list|()
decl_stmt|;
name|int
name|numberOfRanges
init|=
name|rangeValues
operator|.
name|length
operator|/
literal|2
decl_stmt|;
name|result
operator|=
operator|new
name|float
index|[
name|numberOfRanges
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfRanges
condition|;
name|i
operator|++
control|)
block|{
name|int
name|index
init|=
name|i
operator|<<
literal|1
decl_stmt|;
name|result
index|[
name|i
index|]
operator|=
name|clipToRange
argument_list|(
name|inputValues
index|[
name|i
index|]
argument_list|,
name|rangeValues
index|[
name|index
index|]
argument_list|,
name|rangeValues
index|[
name|index
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|result
operator|=
name|inputValues
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Clip the given input value to the given range.      *       * @param x the input value      * @param rangeMin the min value of the range      * @param rangeMax the max value of the range       * @return the clipped value      */
specifier|protected
name|float
name|clipToRange
parameter_list|(
name|float
name|x
parameter_list|,
name|float
name|rangeMin
parameter_list|,
name|float
name|rangeMax
parameter_list|)
block|{
if|if
condition|(
name|x
operator|<
name|rangeMin
condition|)
block|{
return|return
name|rangeMin
return|;
block|}
elseif|else
if|if
condition|(
name|x
operator|>
name|rangeMax
condition|)
block|{
return|return
name|rangeMax
return|;
block|}
return|return
name|x
return|;
block|}
comment|/**      * For a given value of x, interpolate calculates the y value       * on the line defined by the two points (xRangeMin , xRangeMax )       * and (yRangeMin , yRangeMax ).      *       * @param x the to be interpolated value.      * @param xRangeMin the min value of the x range      * @param xRangeMax the max value of the x range      * @param yRangeMin the min value of the y range      * @param yRangeMax the max value of the y range      * @return the interpolated y value      */
specifier|protected
name|float
name|interpolate
parameter_list|(
name|float
name|x
parameter_list|,
name|float
name|xRangeMin
parameter_list|,
name|float
name|xRangeMax
parameter_list|,
name|float
name|yRangeMin
parameter_list|,
name|float
name|yRangeMax
parameter_list|)
block|{
return|return
name|yRangeMin
operator|+
operator|(
operator|(
name|x
operator|-
name|xRangeMin
operator|)
operator|*
operator|(
name|yRangeMax
operator|-
name|yRangeMin
operator|)
operator|/
operator|(
name|xRangeMax
operator|-
name|xRangeMin
operator|)
operator|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"FunctionType"
operator|+
name|getFunctionType
argument_list|()
return|;
block|}
block|}
end_class

end_unit

