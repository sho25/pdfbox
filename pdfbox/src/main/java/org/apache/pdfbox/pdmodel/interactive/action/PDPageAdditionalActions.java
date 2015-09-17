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
comment|/**  * This class represents a page object's dictionary of actions  * that occur due to events.  *  * @author Ben Litchfield  * @author Panagiotis Toumasis  */
end_comment

begin_class
specifier|public
class|class
name|PDPageAdditionalActions
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
name|PDPageAdditionalActions
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
name|PDPageAdditionalActions
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
comment|/**      * This will get an action to be performed when the page      * is opened. This action is independent of any that may be      * defined by the OpenAction entry in the document catalog,      * and is executed after such an action.      *      * @return The O entry of page object's additional actions dictionary.      */
specifier|public
name|PDAction
name|getO
parameter_list|()
block|{
name|COSDictionary
name|o
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|O
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|PDActionFactory
operator|.
name|createAction
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set an action to be performed when the page      * is opened. This action is independent of any that may be      * defined by the OpenAction entry in the document catalog,      * and is executed after such an action.      *      * @param o The action to be performed.      */
specifier|public
name|void
name|setO
parameter_list|(
name|PDAction
name|o
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|O
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get an action to be performed when the page      * is closed. This action applies to the page being closed,      * and is executed before any other page opened.      *      * @return The C entry of page object's additional actions dictionary.      */
specifier|public
name|PDAction
name|getC
parameter_list|()
block|{
name|COSDictionary
name|c
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
literal|"C"
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
name|PDActionFactory
operator|.
name|createAction
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set an action to be performed when the page      * is closed. This action applies to the page being closed,      * and is executed before any other page opened.      *      * @param c The action to be performed.      */
specifier|public
name|void
name|setC
parameter_list|(
name|PDAction
name|c
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
literal|"C"
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

