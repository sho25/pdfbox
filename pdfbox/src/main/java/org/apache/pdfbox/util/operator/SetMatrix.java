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

begin_comment
comment|/**  * @author Huault : huault@free.fr  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|SetMatrix
extends|extends
name|OperatorProcessor
block|{
comment|/**      * Tm Set text matrix and text line matrix.      * @param operator The operator that is being executed.      * @param arguments List      */
specifier|public
name|void
name|process
parameter_list|(
name|PDFOperator
name|operator
parameter_list|,
name|List
argument_list|<
name|COSBase
argument_list|>
name|arguments
parameter_list|)
block|{
comment|//Set text matrix and text line matrix
name|COSNumber
name|a
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
name|b
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
name|COSNumber
name|c
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|COSNumber
name|d
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|COSNumber
name|e
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|COSNumber
name|f
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|Matrix
name|textMatrix
init|=
operator|new
name|Matrix
argument_list|()
decl_stmt|;
name|textMatrix
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|a
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|textMatrix
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|b
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|textMatrix
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
name|c
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|textMatrix
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
name|d
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|textMatrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
name|e
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|textMatrix
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
name|f
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setTextMatrix
argument_list|(
name|textMatrix
argument_list|)
expr_stmt|;
name|context
operator|.
name|setTextLineMatrix
argument_list|(
name|textMatrix
operator|.
name|clone
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

