begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|COSBase
import|;
end_import

begin_comment
comment|/**  * The identity function.  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PDFunctionTypeIdentity
extends|extends
name|PDFunction
block|{
specifier|public
name|PDFunctionTypeIdentity
parameter_list|(
name|COSBase
name|function
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getFunctionType
parameter_list|()
block|{
comment|// shouldn't be called
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
comment|//TODO this is a violation of the interface segregation principle
block|}
annotation|@
name|Override
specifier|public
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
block|{
return|return
name|input
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
literal|"FunctionTypeIdentity"
return|;
block|}
block|}
end_class

end_unit

