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

begin_comment
comment|/**  * A wrapper for a COS dictionary including Type information.  *  * @author<a href="mailto:Johannes%20Koch%20%3Ckoch@apache.org%3E">Johannes Koch</a>  * @version $Revision: $  *  */
end_comment

begin_class
specifier|public
class|class
name|PDTypedDictionaryWrapper
extends|extends
name|PDDictionaryWrapper
block|{
comment|/**      * Creates a new instance with a given type.      *       * @param type the type (Type)      */
specifier|public
name|PDTypedDictionaryWrapper
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new instance with a given COS dictionary.      *       * @param dictionary the dictionary      */
specifier|public
name|PDTypedDictionaryWrapper
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|super
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the type.      *       * @return the type      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|this
operator|.
name|getCOSDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
return|;
block|}
comment|// There is no setType(String) method because changing the Type would most
comment|// probably also change the type of PD object.
block|}
end_class

end_unit

