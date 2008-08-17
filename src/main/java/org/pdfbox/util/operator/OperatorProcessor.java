begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
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
name|org
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|PDFOperator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|PDFStreamEngine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|exceptions
operator|.
name|LoggingObject
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
comment|/**  *  *<p>Titre : OperatorProcessor</p>  *<p>Description : This class is the strategy abstract class  * in the strategy GOF pattern. After instancated, you must ever call * the setContext method to initiamise OPeratorProcessor</p>  *<p>Copyright : Copyright (c) 2004</p>  *<p>Société : DBGS</p>  * @author Huault : huault@free.fr  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|OperatorProcessor
extends|extends
name|LoggingObject
block|{
comment|/**      * The stream engine processing context.      */
specifier|protected
name|PDFStreamEngine
name|context
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor.      *      */
specifier|protected
name|OperatorProcessor
parameter_list|()
block|{     }
comment|/**      * Get the context for processing.      *      * @return The processing context.      */
specifier|protected
name|PDFStreamEngine
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
comment|/**      * Set the processing context.      *      * @param ctx The context for processing.      */
specifier|public
name|void
name|setContext
parameter_list|(
name|PDFStreamEngine
name|ctx
parameter_list|)
block|{
name|context
operator|=
name|ctx
expr_stmt|;
block|}
comment|/**      * process the operator.      * @param operator The operator that is being processed.      * @param arguments arguments needed by this operator.      *      * @throws IOException If there is an error processing the operator.      */
specifier|public
specifier|abstract
name|void
name|process
parameter_list|(
name|PDFOperator
name|operator
parameter_list|,
name|List
name|arguments
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_class

end_unit

