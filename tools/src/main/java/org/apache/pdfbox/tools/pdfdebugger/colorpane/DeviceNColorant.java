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
name|tools
operator|.
name|pdfdebugger
operator|.
name|colorpane
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

begin_comment
comment|/**  * @author Khyrul Bashar.  */
end_comment

begin_class
specifier|public
class|class
name|DeviceNColorant
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|Color
name|maximum
decl_stmt|;
specifier|private
name|Color
name|minimum
decl_stmt|;
comment|/**      * Constructor      */
specifier|public
name|DeviceNColorant
parameter_list|()
block|{     }
comment|/**      * Constructor      * @param colorantName String instance      * @param maxi maximum color      * @param mini minimum color      */
specifier|public
name|DeviceNColorant
parameter_list|(
name|String
name|colorantName
parameter_list|,
name|Color
name|maxi
parameter_list|,
name|Color
name|mini
parameter_list|)
block|{
name|name
operator|=
name|colorantName
expr_stmt|;
name|maximum
operator|=
name|maxi
expr_stmt|;
name|minimum
operator|=
name|mini
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Color
name|getMaximum
parameter_list|()
block|{
return|return
name|maximum
return|;
block|}
specifier|public
name|void
name|setMaximum
parameter_list|(
name|Color
name|maximum
parameter_list|)
block|{
name|this
operator|.
name|maximum
operator|=
name|maximum
expr_stmt|;
block|}
specifier|public
name|Color
name|getMinimum
parameter_list|()
block|{
return|return
name|minimum
return|;
block|}
specifier|public
name|void
name|setMinimum
parameter_list|(
name|Color
name|minimum
parameter_list|)
block|{
name|this
operator|.
name|minimum
operator|=
name|minimum
expr_stmt|;
block|}
block|}
end_class

end_unit

