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
name|contentstream
operator|.
name|operator
package|;
end_package

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
name|contentstream
operator|.
name|PDFStreamEngine
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
comment|/**  * Processes a PDF operator.  *  * @author Laurent Huault  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|OperatorProcessor
block|{
comment|/** The processing context. */
specifier|protected
name|PDFStreamEngine
name|context
decl_stmt|;
comment|/**      * Creates a new OperatorProcessor.      */
specifier|protected
name|OperatorProcessor
parameter_list|()
block|{     }
comment|/**      * Returns the processing context.      * @return the processing context      */
specifier|protected
specifier|final
name|PDFStreamEngine
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
comment|/**      * Sets the processing context.      * @param context the processing context.      */
specifier|public
name|void
name|setContext
parameter_list|(
name|PDFStreamEngine
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
comment|/**      * Process the operator.      * @param operator the operator to process      * @param operands the operands to use when processing      * @throws IOException if the operator cannot be processed      */
specifier|public
specifier|abstract
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
function_decl|;
comment|/**      * Returns the name of this operator, e.g. "BI".      */
specifier|public
specifier|abstract
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Check whether all operands list elements are an instance of a specific class.      *      * @param operands The operands list.      * @param clazz The expected class.      * @return the boolean      */
specifier|public
name|boolean
name|checkArrayTypesClass
parameter_list|(
name|List
argument_list|<
name|COSBase
argument_list|>
name|operands
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
for|for
control|(
name|COSBase
name|base
range|:
name|operands
control|)
block|{
if|if
condition|(
operator|!
name|clazz
operator|.
name|isInstance
argument_list|(
name|base
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

