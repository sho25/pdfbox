begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2015 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|font
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_comment
comment|/**  * Utility class for Unicode fallback.  *  * @author Philip Helger  */
end_comment

begin_class
specifier|final
class|class
name|UniUtil
block|{
specifier|private
name|UniUtil
parameter_list|()
block|{     }
comment|// faster than String.format("uni%04X", codePoint)
specifier|static
name|String
name|getUniNameOfCodePoint
parameter_list|(
name|int
name|codePoint
parameter_list|)
block|{
name|String
name|hex
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|codePoint
argument_list|,
literal|16
argument_list|)
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|hex
operator|.
name|length
argument_list|()
condition|)
block|{
case|case
literal|1
case|:
return|return
literal|"uni000"
operator|+
name|hex
return|;
case|case
literal|2
case|:
return|return
literal|"uni00"
operator|+
name|hex
return|;
case|case
literal|3
case|:
return|return
literal|"uni0"
operator|+
name|hex
return|;
default|default:
return|return
literal|"uni"
operator|+
name|hex
return|;
block|}
block|}
block|}
end_class

end_unit

