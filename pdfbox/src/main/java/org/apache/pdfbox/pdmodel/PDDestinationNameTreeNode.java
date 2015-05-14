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
name|pdmodel
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
name|COSDictionary
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
name|pdmodel
operator|.
name|common
operator|.
name|PDNameTreeNode
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
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
operator|.
name|PDDestination
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
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
operator|.
name|PDPageDestination
import|;
end_import

begin_comment
comment|/**  * This class holds all of the name trees that are available at the document level.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDDestinationNameTreeNode
extends|extends
name|PDNameTreeNode
argument_list|<
name|PDPageDestination
argument_list|>
block|{
comment|/**      * Constructor.      */
specifier|public
name|PDDestinationNameTreeNode
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dic The COS dictionary.      */
specifier|public
name|PDDestinationNameTreeNode
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
name|super
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|PDPageDestination
name|convertCOSToPD
parameter_list|(
name|COSBase
name|base
parameter_list|)
throws|throws
name|IOException
block|{
name|COSBase
name|destination
init|=
name|base
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSDictionary
condition|)
block|{
comment|//the destination is sometimes stored in the D dictionary
comment|//entry instead of being directly an array, so just dereference
comment|//it for now
name|destination
operator|=
operator|(
operator|(
name|COSDictionary
operator|)
name|base
operator|)
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|PDPageDestination
operator|)
name|PDDestination
operator|.
name|create
argument_list|(
name|destination
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|PDNameTreeNode
name|createChildNode
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
return|return
operator|new
name|PDDestinationNameTreeNode
argument_list|(
name|dic
argument_list|)
return|;
block|}
block|}
end_class

end_unit

