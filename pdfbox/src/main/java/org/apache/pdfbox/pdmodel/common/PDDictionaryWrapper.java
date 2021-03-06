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
name|common
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

begin_comment
comment|/**  * A wrapper for a COS dictionary.  *  * @author Johannes Koch  *  */
end_comment

begin_class
specifier|public
class|class
name|PDDictionaryWrapper
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Default constructor      */
specifier|public
name|PDDictionaryWrapper
parameter_list|()
block|{
name|this
operator|.
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates a new instance with a given COS dictionary.      *       * @param dictionary the dictionary      */
specifier|public
name|PDDictionaryWrapper
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|dictionary
operator|=
name|dictionary
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|this
operator|.
name|dictionary
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|PDDictionaryWrapper
condition|)
block|{
return|return
name|this
operator|.
name|dictionary
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|PDDictionaryWrapper
operator|)
name|obj
operator|)
operator|.
name|dictionary
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|this
operator|.
name|dictionary
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

