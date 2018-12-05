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
name|graphics
operator|.
name|state
package|;
end_package

begin_comment
comment|/**  * Rendering intent.  *  * @author John Hewson  */
end_comment

begin_enum
specifier|public
enum|enum
name|RenderingIntent
block|{
comment|/**      * Absolute Colorimetric.      */
name|ABSOLUTE_COLORIMETRIC
argument_list|(
literal|"AbsoluteColorimetric"
argument_list|)
block|,
comment|/**      * Relative Colorimetric.      */
name|RELATIVE_COLORIMETRIC
argument_list|(
literal|"RelativeColorimetric"
argument_list|)
block|,
comment|/**      * Saturation.      */
name|SATURATION
argument_list|(
literal|"Saturation"
argument_list|)
block|,
comment|/**      * Perceptual.      */
name|PERCEPTUAL
argument_list|(
literal|"Perceptual"
argument_list|)
block|;
specifier|public
specifier|static
name|RenderingIntent
name|fromString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
for|for
control|(
name|RenderingIntent
name|instance
range|:
name|RenderingIntent
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|instance
operator|.
name|value
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|instance
return|;
block|}
block|}
comment|// "If a conforming reader does not recognize the specified name,
comment|// it shall use the RelativeColorimetric intent by default."
return|return
name|RELATIVE_COLORIMETRIC
return|;
block|}
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
name|RenderingIntent
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Returns the string value, as used in a PDF file.      */
specifier|public
name|String
name|stringValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
end_enum

end_unit

