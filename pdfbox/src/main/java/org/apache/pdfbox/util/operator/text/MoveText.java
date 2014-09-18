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
name|text
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
name|cos
operator|.
name|COSNumber
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
name|Matrix
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
name|OperatorProcessor
import|;
end_import

begin_comment
comment|/**  * Td: Move text position.  *  * @author Laurent Huault  */
end_comment

begin_class
specifier|public
class|class
name|MoveText
extends|extends
name|OperatorProcessor
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
name|arguments
parameter_list|)
block|{
name|COSNumber
name|x
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|COSNumber
name|y
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Matrix
name|td
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|td
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
name|x
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|td
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
name|y
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setTextLineMatrix
argument_list|(
name|td
operator|.
name|multiply
argument_list|(
name|context
operator|.
name|getTextLineMatrix
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|setTextMatrix
argument_list|(
name|context
operator|.
name|getTextLineMatrix
argument_list|()
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"Td"
return|;
block|}
block|}
end_class

end_unit

