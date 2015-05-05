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
name|measurement
package|;
end_package

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
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This class represents a number format dictionary.  *   */
end_comment

begin_class
specifier|public
class|class
name|PDNumberFormatDictionary
implements|implements
name|COSObjectable
block|{
comment|/**      * The type of the dictionary.      */
specifier|public
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"NumberFormat"
decl_stmt|;
comment|/**      * Constant indicating that the label specified by U is a suffix to the value.      */
specifier|public
specifier|static
specifier|final
name|String
name|LABEL_SUFFIX_TO_VALUE
init|=
literal|"S"
decl_stmt|;
comment|/**      * Constant indicating that the label specified by U is a postfix to the value.      */
specifier|public
specifier|static
specifier|final
name|String
name|LABEL_PREFIX_TO_VALUE
init|=
literal|"P"
decl_stmt|;
comment|/**      * Constant for showing a fractional value as decimal to the precision specified by the D entry.      */
specifier|public
specifier|static
specifier|final
name|String
name|FRACTIONAL_DISPLAY_DECIMAL
init|=
literal|"D"
decl_stmt|;
comment|/**      * Constant for showing a fractional value as a fraction with denominator specified by the D entry.      */
specifier|public
specifier|static
specifier|final
name|String
name|FRACTIONAL_DISPLAY_FRACTION
init|=
literal|"F"
decl_stmt|;
comment|/**      * Constant for showing a fractional value without fractional part; round to the nearest whole unit.      */
specifier|public
specifier|static
specifier|final
name|String
name|FRACTIONAL_DISPLAY_ROUND
init|=
literal|"R"
decl_stmt|;
comment|/**      * Constant for showing a fractional value without fractional part; truncate to achieve whole units.      */
specifier|public
specifier|static
specifier|final
name|String
name|FRACTIONAL_DISPLAY_TRUNCATE
init|=
literal|"T"
decl_stmt|;
specifier|private
name|COSDictionary
name|numberFormatDictionary
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDNumberFormatDictionary
parameter_list|()
block|{
name|this
operator|.
name|numberFormatDictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|this
operator|.
name|numberFormatDictionary
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       * @param dictionary the corresponding dictionary      */
specifier|public
name|PDNumberFormatDictionary
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|numberFormatDictionary
operator|=
name|dictionary
expr_stmt|;
block|}
comment|/**      * This will return the dictionary.      *       * @return the number format dictionary      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|this
operator|.
name|numberFormatDictionary
return|;
block|}
comment|/**      * This will return the type of the number format dictionary.      * It must be "NumberFormat"      *       * @return the type      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|TYPE
return|;
block|}
comment|/**      * This will return the label for the units.      *       * @return the label for the units      */
specifier|public
name|String
name|getUnits
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
literal|"U"
argument_list|)
return|;
block|}
comment|/**      * This will set the label for the units.      *       * @param units the label for the units      */
specifier|public
name|void
name|setUnits
parameter_list|(
name|String
name|units
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
literal|"U"
argument_list|,
name|units
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the conversion factor.      *       * @return the conversion factor      */
specifier|public
name|float
name|getConversionFactor
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getFloat
argument_list|(
literal|"C"
argument_list|)
return|;
block|}
comment|/**      * This will set the conversion factor.      *       * @param conversionFactor the conversion factor      */
specifier|public
name|void
name|setConversionFactor
parameter_list|(
name|float
name|conversionFactor
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setFloat
argument_list|(
literal|"C"
argument_list|,
name|conversionFactor
argument_list|)
expr_stmt|;
block|}
comment|/**       * This will return the value for the manner to display a fractional value.      *        * @return the manner to display a fractional value      */
specifier|public
name|String
name|getFractionalDisplay
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
literal|"F"
argument_list|,
name|FRACTIONAL_DISPLAY_DECIMAL
argument_list|)
return|;
block|}
comment|/**       * This will set the value for the manner to display a fractional value.      * Allowed values are "D", "F", "R" and "T"      * @param fractionalDisplay the manner to display a fractional value      */
specifier|public
name|void
name|setFractionalDisplay
parameter_list|(
name|String
name|fractionalDisplay
parameter_list|)
block|{
if|if
condition|(
operator|(
name|fractionalDisplay
operator|==
literal|null
operator|)
operator|||
name|FRACTIONAL_DISPLAY_DECIMAL
operator|.
name|equals
argument_list|(
name|fractionalDisplay
argument_list|)
operator|||
name|FRACTIONAL_DISPLAY_FRACTION
operator|.
name|equals
argument_list|(
name|fractionalDisplay
argument_list|)
operator|||
name|FRACTIONAL_DISPLAY_ROUND
operator|.
name|equals
argument_list|(
name|fractionalDisplay
argument_list|)
operator|||
name|FRACTIONAL_DISPLAY_TRUNCATE
operator|.
name|equals
argument_list|(
name|fractionalDisplay
argument_list|)
condition|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
literal|"F"
argument_list|,
name|fractionalDisplay
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value must be \"D\", \"F\", \"R\", or \"T\", (or null)."
argument_list|)
throw|;
block|}
block|}
comment|/**      * This will return the precision or denominator of a fractional amount.      *       * @return the precision or denominator      */
specifier|public
name|int
name|getDenominator
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
literal|"D"
argument_list|)
return|;
block|}
comment|/**      * This will set the precision or denominator of a fractional amount.      *       * @param denominator the precision or denominator      */
specifier|public
name|void
name|setDenominator
parameter_list|(
name|int
name|denominator
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
literal|"D"
argument_list|,
name|denominator
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the value indication if the denominator of the fractional value is reduced/truncated .      *       * @return fd      */
specifier|public
name|boolean
name|isFD
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getBoolean
argument_list|(
literal|"FD"
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * This will set the value indication if the denominator of the fractional value is reduced/truncated .      * The denominator may not be reduced/truncated if true      * @param fd fd      */
specifier|public
name|void
name|setFD
parameter_list|(
name|boolean
name|fd
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setBoolean
argument_list|(
literal|"FD"
argument_list|,
name|fd
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the text to be used between orders of thousands in display of numerical values.      *       * @return thousands separator      */
specifier|public
name|String
name|getThousandsSeparator
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
literal|"RT"
argument_list|,
literal|","
argument_list|)
return|;
block|}
comment|/**      * This will set the text to be used between orders of thousands in display of numerical values.      *       * @param thousandsSeparator thousands separator      */
specifier|public
name|void
name|setThousandsSeparator
parameter_list|(
name|String
name|thousandsSeparator
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
literal|"RT"
argument_list|,
name|thousandsSeparator
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the text to be used as the decimal point in displaying numerical values.      *       * @return decimal separator      */
specifier|public
name|String
name|getDecimalSeparator
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
literal|"RD"
argument_list|,
literal|"."
argument_list|)
return|;
block|}
comment|/**      * This will set the text to be used as the decimal point in displaying numerical values.      *       * @param decimalSeparator decimal separator      */
specifier|public
name|void
name|setDecimalSeparator
parameter_list|(
name|String
name|decimalSeparator
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
literal|"RD"
argument_list|,
name|decimalSeparator
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the text to be concatenated to the left of the label specified by U.      * @return label prefix      */
specifier|public
name|String
name|getLabelPrefixString
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
literal|"PS"
argument_list|,
literal|" "
argument_list|)
return|;
block|}
comment|/**      * This will set the text to be concatenated to the left of the label specified by U.      * @param labelPrefixString label prefix      */
specifier|public
name|void
name|setLabelPrefixString
parameter_list|(
name|String
name|labelPrefixString
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
literal|"PS"
argument_list|,
name|labelPrefixString
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the text to be concatenated after the label specified by U.      *       * @return label suffix      */
specifier|public
name|String
name|getLabelSuffixString
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
literal|"SS"
argument_list|,
literal|" "
argument_list|)
return|;
block|}
comment|/**      * This will set the text to be concatenated after the label specified by U.      *       * @param labelSuffixString label suffix      */
specifier|public
name|void
name|setLabelSuffixString
parameter_list|(
name|String
name|labelSuffixString
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
literal|"SS"
argument_list|,
name|labelSuffixString
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return a value indicating the ordering of the label specified by U to the calculated unit value.      *       * @return label position       */
specifier|public
name|String
name|getLabelPositionToValue
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
literal|"O"
argument_list|,
name|LABEL_SUFFIX_TO_VALUE
argument_list|)
return|;
block|}
comment|/**      * This will set the value indicating the ordering of the label specified by U to the calculated unit value.      * Possible values are "S" and "P"      *       * @param labelPositionToValue label position       */
specifier|public
name|void
name|setLabelPositionToValue
parameter_list|(
name|String
name|labelPositionToValue
parameter_list|)
block|{
if|if
condition|(
operator|(
name|labelPositionToValue
operator|==
literal|null
operator|)
operator|||
name|LABEL_PREFIX_TO_VALUE
operator|.
name|equals
argument_list|(
name|labelPositionToValue
argument_list|)
operator|||
name|LABEL_SUFFIX_TO_VALUE
operator|.
name|equals
argument_list|(
name|labelPositionToValue
argument_list|)
condition|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
literal|"O"
argument_list|,
name|labelPositionToValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Value must be \"S\", or \"P\" (or null)."
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

