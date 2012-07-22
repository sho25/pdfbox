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
name|COSName
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
name|COSStream
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
name|pdmodel
operator|.
name|PDResources
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
name|pdmodel
operator|.
name|graphics
operator|.
name|xobject
operator|.
name|PDXObject
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
name|pdmodel
operator|.
name|graphics
operator|.
name|xobject
operator|.
name|PDXObjectForm
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
name|PDFMarkedContentExtractor
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Invoke named XObject.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author Mario Ivankovits  *  * @version $Revision: 1.9 $  */
end_comment

begin_class
specifier|public
class|class
name|Invoke
extends|extends
name|OperatorProcessor
block|{
comment|/**      * process : Do - Invoke a named xobject.      *       * @param operator The operator that is being executed.      * @param arguments List      *      * @throws IOException If there is an error processing this operator.      */
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
throws|throws
name|IOException
block|{
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
name|xobjects
init|=
name|context
operator|.
name|getXObjects
argument_list|()
decl_stmt|;
name|PDXObject
name|xobject
init|=
operator|(
name|PDXObject
operator|)
name|xobjects
operator|.
name|get
argument_list|(
name|name
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|context
operator|instanceof
name|PDFMarkedContentExtractor
condition|)
block|{
operator|(
operator|(
name|PDFMarkedContentExtractor
operator|)
name|this
operator|.
name|context
operator|)
operator|.
name|xobject
argument_list|(
name|xobject
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xobject
operator|instanceof
name|PDXObjectForm
condition|)
block|{
name|PDXObjectForm
name|form
init|=
operator|(
name|PDXObjectForm
operator|)
name|xobject
decl_stmt|;
name|COSStream
name|formContentstream
init|=
name|form
operator|.
name|getCOSStream
argument_list|()
decl_stmt|;
comment|// find some optional resources, instead of using the current resources
name|PDResources
name|pdResources
init|=
name|form
operator|.
name|getResources
argument_list|()
decl_stmt|;
name|getContext
argument_list|()
operator|.
name|processSubStream
argument_list|(
name|context
operator|.
name|getCurrentPage
argument_list|()
argument_list|,
name|pdResources
argument_list|,
name|formContentstream
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

