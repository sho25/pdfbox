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
package|;
end_package

begin_comment
comment|/**  *  * @author adam  */
end_comment

begin_class
specifier|public
class|class
name|XrefEntry
block|{
specifier|private
name|int
name|objectNumber
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|byteOffset
init|=
literal|0
decl_stmt|;
specifier|private
name|int
name|generation
init|=
literal|0
decl_stmt|;
specifier|private
name|boolean
name|inUse
init|=
literal|true
decl_stmt|;
specifier|public
name|XrefEntry
parameter_list|()
block|{     }
specifier|public
name|XrefEntry
parameter_list|(
name|int
name|objectNumber
parameter_list|,
name|int
name|byteOffset
parameter_list|,
name|int
name|generation
parameter_list|,
name|String
name|inUse
parameter_list|)
block|{
name|this
operator|.
name|objectNumber
operator|=
name|objectNumber
expr_stmt|;
name|this
operator|.
name|byteOffset
operator|=
name|byteOffset
expr_stmt|;
name|this
operator|.
name|generation
operator|=
name|generation
expr_stmt|;
name|this
operator|.
name|inUse
operator|=
literal|"n"
operator|.
name|equals
argument_list|(
name|inUse
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getByteOffset
parameter_list|()
block|{
return|return
name|byteOffset
return|;
block|}
block|}
end_class

end_unit

