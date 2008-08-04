begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|org
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSFloat
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
name|PDFOperator
import|;
end_import

begin_comment
comment|/**  *  *<p>Titre : PDFEngine Modification.</p>  *<p>Description : Structal modification of the PDFEngine class : the long sequence of   *    conditions in processOperator is remplaced by this strategy pattern</p>  *<p>Copyright : Copyright (c) 2004</p>  *<p>Société : DBGS</p>  * @author Huault : huault@free.fr  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|NextLine
extends|extends
name|OperatorProcessor
block|{
comment|/**      * process : T* Move to start of next text line.      * @param operator The operator that is being executed.      * @param arguments List      *      * @throws IOException If there is an error during processing.      */
specifier|public
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
block|{
comment|//move to start of next text line
name|ArrayList
name|args
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|args
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|0.0f
argument_list|)
argument_list|)
expr_stmt|;
comment|// this must be -leading instead of just leading as written in the
comment|// specification (p.369) the acrobat reader seems to implement it the same way
name|args
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
operator|-
literal|1
operator|*
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|getLeading
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// use Td instead of repeating code
name|context
operator|.
name|processOperator
argument_list|(
literal|"Td"
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

