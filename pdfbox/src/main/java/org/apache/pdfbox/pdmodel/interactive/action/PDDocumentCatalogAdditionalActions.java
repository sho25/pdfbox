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
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This class represents a document catalog's dictionary of actions  * that occur due to events.  *  * @author Ben Litchfield  * @author Panagiotis Toumasis  */
end_comment

begin_class
specifier|public
class|class
name|PDDocumentCatalogAdditionalActions
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
name|PDDocumentCatalogAdditionalActions
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
name|PDDocumentCatalogAdditionalActions
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
comment|/**      * This will get a JavaScript action to be performed      * before closing a document.      * The name WC stands for "will close".      *      * @return The WC entry of document catalog's additional actions dictionary.      */
specifier|public
name|PDAction
name|getWC
parameter_list|()
block|{
name|COSDictionary
name|wc
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
literal|"WC"
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|wc
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
name|wc
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a JavaScript action to be performed      * before closing a document.      * The name WC stands for "will close".      *      * @param wc The action to be performed.      */
specifier|public
name|void
name|setWC
parameter_list|(
name|PDAction
name|wc
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
literal|"WC"
argument_list|,
name|wc
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a JavaScript action to be performed      * before saving a document.      * The name WS stands for "will save".      *      * @return The WS entry of document catalog's additional actions dictionary.      */
specifier|public
name|PDAction
name|getWS
parameter_list|()
block|{
name|COSDictionary
name|ws
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
literal|"WS"
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ws
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
name|ws
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a JavaScript action to be performed      * before saving a document.      * The name WS stands for "will save".      *      * @param ws The action to be performed.      */
specifier|public
name|void
name|setWS
parameter_list|(
name|PDAction
name|ws
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
literal|"WS"
argument_list|,
name|ws
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a JavaScript action to be performed      * after saving a document.      * The name DS stands for "did save".      *      * @return The DS entry of document catalog's additional actions dictionary.      */
specifier|public
name|PDAction
name|getDS
parameter_list|()
block|{
name|COSDictionary
name|ds
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
literal|"DS"
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ds
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
name|ds
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a JavaScript action to be performed      * after saving a document.      * The name DS stands for "did save".      *      * @param ds The action to be performed.      */
specifier|public
name|void
name|setDS
parameter_list|(
name|PDAction
name|ds
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
literal|"DS"
argument_list|,
name|ds
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a JavaScript action to be performed      * before printing a document.      * The name WP stands for "will print".      *      * @return The WP entry of document catalog's additional actions dictionary.      */
specifier|public
name|PDAction
name|getWP
parameter_list|()
block|{
name|COSDictionary
name|wp
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
literal|"WP"
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|wp
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
name|wp
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a JavaScript action to be performed      * before printing a document.      * The name WP stands for "will print".      *      * @param wp The action to be performed.      */
specifier|public
name|void
name|setWP
parameter_list|(
name|PDAction
name|wp
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
literal|"WP"
argument_list|,
name|wp
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a JavaScript action to be performed      * after printing a document.      * The name DP stands for "did print".      *      * @return The DP entry of document catalog's additional actions dictionary.      */
specifier|public
name|PDAction
name|getDP
parameter_list|()
block|{
name|COSDictionary
name|dp
init|=
operator|(
name|COSDictionary
operator|)
name|actions
operator|.
name|getDictionaryObject
argument_list|(
literal|"DP"
argument_list|)
decl_stmt|;
name|PDAction
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dp
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
name|dp
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a JavaScript action to be performed      * after printing a document.      * The name DP stands for "did print".      *      * @param dp The action to be performed.      */
specifier|public
name|void
name|setDP
parameter_list|(
name|PDAction
name|dp
parameter_list|)
block|{
name|actions
operator|.
name|setItem
argument_list|(
literal|"DP"
argument_list|,
name|dp
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

