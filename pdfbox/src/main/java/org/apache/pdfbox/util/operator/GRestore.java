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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
comment|/**  * Process the Q operator.  *   * @author Huault : huault@free.fr  *   */
end_comment

begin_class
specifier|public
class|class
name|GRestore
extends|extends
name|OperatorProcessor
block|{
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|GRestore
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * {@inheritDoc}      */
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
if|if
condition|(
name|context
operator|.
name|getGraphicsStackSize
argument_list|()
operator|>
literal|0
condition|)
block|{
name|context
operator|.
name|restoreGraphicsState
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// this shouldn't happen but it does, see PDFBOX-161
comment|// TODO make this self healing mechanism optional for preflight??
name|LOG
operator|.
name|debug
argument_list|(
literal|"GRestore: no graphics state left to be restored."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

