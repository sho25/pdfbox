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
name|debugger
operator|.
name|streampane
operator|.
name|tooltip
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
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

begin_comment
comment|/**  * @author Khyrul Bashar  * An abstract class for tooltips of color operators.  */
end_comment

begin_class
specifier|abstract
class|class
name|ColorToolTip
implements|implements
name|ToolTip
block|{
specifier|private
name|String
name|toolTipText
decl_stmt|;
comment|/**      * provides the Hex value for a Color instance.      * @param color      * @return      */
specifier|static
name|String
name|colorHexValue
parameter_list|(
name|Color
name|color
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%02x"
argument_list|,
name|color
operator|.
name|getRed
argument_list|()
argument_list|)
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"%02x"
argument_list|,
name|color
operator|.
name|getGreen
argument_list|()
argument_list|)
operator|+
name|String
operator|.
name|format
argument_list|(
literal|"%02x"
argument_list|,
name|color
operator|.
name|getBlue
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Extract Color values from the row for which tooltip is going to be shown.      * @param rowtext String instance,      * @return float array containing color values.      */
name|float
index|[]
name|extractColorValues
parameter_list|(
name|String
name|rowtext
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|words
init|=
name|ToolTipController
operator|.
name|getWords
argument_list|(
name|rowtext
argument_list|)
decl_stmt|;
name|words
operator|.
name|remove
argument_list|(
name|words
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|float
index|[]
name|values
init|=
operator|new
name|float
index|[
name|words
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|int
name|index
init|=
literal|0
decl_stmt|;
try|try
block|{
for|for
control|(
name|String
name|word
range|:
name|words
control|)
block|{
name|values
index|[
name|index
operator|++
index|]
operator|=
name|Float
operator|.
name|parseFloat
argument_list|(
name|word
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|values
return|;
block|}
comment|/**      * Create a html string that actually shows a colored rect.      * @param hexValue      * @return String instance, In html format.      */
name|String
name|getMarkUp
parameter_list|(
name|String
name|hexValue
parameter_list|)
block|{
return|return
literal|"<html>\n"
operator|+
literal|"<body bgcolor=#ffffff>\n"
operator|+
literal|"<div style=\"width:50px;height:20px;border:1px; background-color:#"
operator|+
name|hexValue
operator|+
literal|";\"></div></body>\n"
operator|+
literal|"</html>"
return|;
block|}
specifier|public
name|void
name|setToolTipText
parameter_list|(
name|String
name|toolTip
parameter_list|)
block|{
name|this
operator|.
name|toolTipText
operator|=
name|toolTip
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getToolTipText
parameter_list|()
block|{
return|return
name|toolTipText
return|;
block|}
block|}
end_class

end_unit

