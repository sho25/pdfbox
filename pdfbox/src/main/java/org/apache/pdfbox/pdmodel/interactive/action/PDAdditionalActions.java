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
operator|.
name|interactive
operator|.
name|action
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
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This represents a dictionary of actions that occur due to events.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDAdditionalActions
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|final
name|COSDictionary
name|actions
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDAdditionalActions
parameter_list|()
block|{
name|actions
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param a The action dictionary.      */
specifier|public
name|PDAdditionalActions
parameter_list|(
name|COSDictionary
name|a
parameter_list|)
block|{
name|actions
operator|=
name|a
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|actions
return|;
block|}
comment|/**      * Get the F action.      *      * @return The F action.      */
specifier|public
name|PDAction
name|getF
parameter_list|()
block|{
return|return
name|PDActionFactory
operator|.
name|createAction
argument_list|(
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|F
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Set the F action.      *      * @param action Get the F action.      */
specifier|public
name|void
name|setF
parameter_list|(
name|PDAction
name|action
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

