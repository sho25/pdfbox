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
name|util
operator|.
name|operator
operator|.
name|graphics
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|util
operator|.
name|operator
operator|.
name|Operator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * b* Close, fill and stroke the path with even-odd winding rule.  *  * @author Andreas Lehmkühler  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|CloseFillEvenOddAndStrokePath
extends|extends
name|GraphicsOperatorProcessor
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Operator
name|operator
parameter_list|,
name|List
argument_list|<
name|COSBase
argument_list|>
name|operands
parameter_list|)
throws|throws
name|IOException
block|{
name|context
operator|.
name|processOperator
argument_list|(
literal|"h"
argument_list|,
name|operands
argument_list|)
expr_stmt|;
comment|// ClosePath
name|context
operator|.
name|processOperator
argument_list|(
literal|"B*"
argument_list|,
name|operands
argument_list|)
expr_stmt|;
comment|// FillEvenOddAndStroke
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"b*"
return|;
block|}
block|}
end_class

end_unit

