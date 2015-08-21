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
name|hexviewer
package|;
end_package

begin_comment
comment|/**  * @author Khyrul Bashar  *  * This the event class for byte value changed from the Hex pane.  */
end_comment

begin_class
class|class
name|HexChangedEvent
block|{
specifier|private
specifier|final
name|byte
name|newValue
decl_stmt|;
specifier|private
specifier|final
name|int
name|byteIndex
decl_stmt|;
comment|/**      * Constructor.      * @param newValue byte. The new byte value for the index.      * @param byteIndex int. Index for the changed byte.      */
name|HexChangedEvent
parameter_list|(
name|byte
name|newValue
parameter_list|,
name|int
name|byteIndex
parameter_list|)
block|{
name|this
operator|.
name|newValue
operator|=
name|newValue
expr_stmt|;
name|this
operator|.
name|byteIndex
operator|=
name|byteIndex
expr_stmt|;
block|}
specifier|public
name|byte
name|getNewValue
parameter_list|()
block|{
return|return
name|newValue
return|;
block|}
specifier|public
name|int
name|getByteIndex
parameter_list|()
block|{
return|return
name|byteIndex
return|;
block|}
block|}
end_class

end_unit

