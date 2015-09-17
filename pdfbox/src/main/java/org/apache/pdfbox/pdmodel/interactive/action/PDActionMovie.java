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

begin_comment
comment|/**  * @author Timur Kamalov  */
end_comment

begin_class
specifier|public
class|class
name|PDActionMovie
extends|extends
name|PDAction
block|{
comment|/**      * This type of action this object represents.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"Movie"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDActionMovie
parameter_list|()
block|{
name|action
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|setSubType
argument_list|(
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param a The action dictionary.      */
specifier|public
name|PDActionMovie
parameter_list|(
name|COSDictionary
name|a
parameter_list|)
block|{
name|super
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the type of action that the actions dictionary describes. It must be Movie for      * a Movie action.      *      * @return The S entry of the specific Movie action dictionary.      */
specifier|public
name|String
name|getS
parameter_list|()
block|{
return|return
name|action
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|S
argument_list|)
return|;
block|}
comment|/**      * This will set the type of action that the actions dictionary describes. It must be Movie for      * a Movie action.      *      * @param s The Movie action.      */
specifier|public
name|void
name|setS
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|action
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|S
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

