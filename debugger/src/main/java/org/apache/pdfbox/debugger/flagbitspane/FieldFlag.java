begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *   Licensed to the Apache Software Foundation (ASF) under one or more  *   contributor license agreements.  See the NOTICE file distributed with  *   this work for additional information regarding copyright ownership.  *   The ASF licenses this file to You under the Apache License, Version 2.0  *   (the "License"); you may not use this file except in compliance with  *   the License.  You may obtain a copy of the License at  *  *        http://www.apache.org/licenses/LICENSE-2.0  *  *   Unless required by applicable law or agreed to in writing, software  *   distributed under the License is distributed on an "AS IS" BASIS,  *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *   See the License for the specific language governing permissions and  *   limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|debugger
operator|.
name|flagbitspane
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

begin_comment
comment|/**  * @author Khyrul Bashar  * A class that provides field flag bits.  */
end_comment

begin_class
class|class
name|FieldFlag
extends|extends
name|Flag
block|{
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor      * @param dictionary COSDictionary instance      */
name|FieldFlag
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|dictionary
operator|=
name|dictionary
expr_stmt|;
block|}
annotation|@
name|Override
name|String
name|getFlagType
parameter_list|()
block|{
name|COSName
name|fieldType
init|=
name|dictionary
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|FT
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|TX
operator|.
name|equals
argument_list|(
name|fieldType
argument_list|)
condition|)
block|{
return|return
literal|"Text field flag"
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|BTN
operator|.
name|equals
argument_list|(
name|fieldType
argument_list|)
condition|)
block|{
return|return
literal|"Button field flag"
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|CH
operator|.
name|equals
argument_list|(
name|fieldType
argument_list|)
condition|)
block|{
return|return
literal|"Choice field flag"
return|;
block|}
return|return
literal|"Field flag"
return|;
block|}
annotation|@
name|Override
name|String
name|getFlagValue
parameter_list|()
block|{
return|return
literal|"Flag value: "
operator|+
name|dictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|FF
argument_list|)
return|;
block|}
annotation|@
name|Override
name|Object
index|[]
index|[]
name|getFlagBits
parameter_list|()
block|{
name|int
name|flagValue
init|=
name|dictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|FF
argument_list|)
decl_stmt|;
name|COSName
name|fieldType
init|=
name|dictionary
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|FT
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|TX
operator|.
name|equals
argument_list|(
name|fieldType
argument_list|)
condition|)
block|{
return|return
name|getTextFieldFlagBits
argument_list|(
name|flagValue
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|BTN
operator|.
name|equals
argument_list|(
name|fieldType
argument_list|)
condition|)
block|{
return|return
name|getButtonFieldFlagBits
argument_list|(
name|flagValue
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|CH
operator|.
name|equals
argument_list|(
name|fieldType
argument_list|)
condition|)
block|{
return|return
name|getChoiceFieldFlagBits
argument_list|(
name|flagValue
argument_list|)
return|;
block|}
return|return
name|getFieldFlagBits
argument_list|(
name|flagValue
argument_list|)
return|;
block|}
specifier|private
name|Object
index|[]
index|[]
name|getTextFieldFlagBits
parameter_list|(
specifier|final
name|int
name|flagValue
parameter_list|)
block|{
return|return
operator|new
name|Object
index|[]
index|[]
block|{
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|"ReadOnly"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|1
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|2
block|,
literal|"Required"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|2
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|3
block|,
literal|"NoExport"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|3
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|13
block|,
literal|"Multiline"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|13
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|14
block|,
literal|"Password"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|14
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|21
block|,
literal|"FileSelect"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|21
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|23
block|,
literal|"DoNotSpellCheck"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|23
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|24
block|,
literal|"DoNotScroll"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|24
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|25
block|,
literal|"Comb"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|25
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|26
block|,
literal|"RichText"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|26
argument_list|)
block|}
block|}
return|;
block|}
specifier|private
name|Object
index|[]
index|[]
name|getButtonFieldFlagBits
parameter_list|(
specifier|final
name|int
name|flagValue
parameter_list|)
block|{
return|return
operator|new
name|Object
index|[]
index|[]
block|{
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|"ReadOnly"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|1
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|2
block|,
literal|"Required"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|2
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|3
block|,
literal|"NoExport"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|3
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|15
block|,
literal|"NoToggleToOff"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|15
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|16
block|,
literal|"Radio"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|16
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|17
block|,
literal|"Pushbutton"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|17
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|26
block|,
literal|"RadiosInUnison"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|26
argument_list|)
block|}
block|}
return|;
block|}
specifier|private
name|Object
index|[]
index|[]
name|getChoiceFieldFlagBits
parameter_list|(
specifier|final
name|int
name|flagValue
parameter_list|)
block|{
return|return
operator|new
name|Object
index|[]
index|[]
block|{
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|"ReadOnly"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|1
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|2
block|,
literal|"Required"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|2
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|3
block|,
literal|"NoExport"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|3
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|18
block|,
literal|"Combo"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|18
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|19
block|,
literal|"Edit"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|19
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|20
block|,
literal|"Sort"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|20
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|22
block|,
literal|"MultiSelect"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|22
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|23
block|,
literal|"DoNotSpellCheck"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|23
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|27
block|,
literal|"CommitOnSelChange"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|27
argument_list|)
block|}
block|}
return|;
block|}
specifier|private
name|Object
index|[]
index|[]
name|getFieldFlagBits
parameter_list|(
specifier|final
name|int
name|flagValue
parameter_list|)
block|{
return|return
operator|new
name|Object
index|[]
index|[]
block|{
operator|new
name|Object
index|[]
block|{
literal|1
block|,
literal|"ReadOnly"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|1
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|2
block|,
literal|"Required"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|2
argument_list|)
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|3
block|,
literal|"NoExport"
block|,
name|isFlagBitSet
argument_list|(
name|flagValue
argument_list|,
literal|3
argument_list|)
block|}
block|}
return|;
block|}
comment|/**      * Check the corresponding flag bit if set or not      * @param flagValue the flag integer      * @param bitPosition bit position to check      * @return if set return true else false      */
specifier|private
name|Boolean
name|isFlagBitSet
parameter_list|(
name|int
name|flagValue
parameter_list|,
name|int
name|bitPosition
parameter_list|)
block|{
name|int
name|binaryFormat
init|=
literal|1
operator|<<
operator|(
name|bitPosition
operator|-
literal|1
operator|)
decl_stmt|;
return|return
operator|(
name|flagValue
operator|&
name|binaryFormat
operator|)
operator|==
name|binaryFormat
return|;
block|}
block|}
end_class

end_unit

