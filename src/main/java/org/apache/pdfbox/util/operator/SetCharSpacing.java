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
name|PDFOperator
import|;
end_import

begin_comment
comment|/**  *  *<p>Titre : PDFEngine Modification.</p>  *<p>Description : Structal modification of the PDFEngine class :  * the long sequence of conditions in processOperator is remplaced by  * this strategy pattern</p>  *<p>Copyright : Copyright (c) 2004</p>  *<p>Société : DBGS</p>  * @author Huault : huault@free.fr  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|SetCharSpacing
extends|extends
name|OperatorProcessor
block|{
comment|/**      * process : Tc Set character spacing.      * @param operator The operator that is being executed.      * @param arguments List      */
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
block|{
comment|//set character spacing
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|//There are some documents which are incorrectly structured, and have
comment|//a wrong number of arguments to this, so we will assume the last argument
comment|//in the list
name|Object
name|charSpacing
init|=
name|arguments
operator|.
name|get
argument_list|(
name|arguments
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|charSpacing
operator|instanceof
name|COSNumber
condition|)
block|{
name|COSNumber
name|characterSpacing
init|=
operator|(
name|COSNumber
operator|)
name|charSpacing
decl_stmt|;
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|setCharacterSpacing
argument_list|(
name|characterSpacing
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

