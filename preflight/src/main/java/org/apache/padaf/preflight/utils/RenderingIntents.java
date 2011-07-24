begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|utils
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|RENDERING_INTENT_ABS_COLOR
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|RENDERING_INTENT_PERCEPTUAL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|RENDERING_INTENT_REL_COLOR
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|ValidationConstants
operator|.
name|RENDERING_INTENT_SATURATION
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
name|Collections
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

begin_comment
comment|/**  * This class contains a static list of RenderingIntent values to allow an easy  * RenderingIntent value validation. Here is the content of the RenderingIntent  * list :  *<UL>  *<li>Perceptual  *<li>Saturation  *<li>AbsoluteColorimetric  *<li>RelativeColorimetric  *</UL>  */
end_comment

begin_class
specifier|public
class|class
name|RenderingIntents
block|{
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|RENDERING_INTENTS
decl_stmt|;
static|static
block|{
name|ArrayList
argument_list|<
name|String
argument_list|>
name|al
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|al
operator|.
name|add
argument_list|(
name|RENDERING_INTENT_REL_COLOR
argument_list|)
expr_stmt|;
name|al
operator|.
name|add
argument_list|(
name|RENDERING_INTENT_ABS_COLOR
argument_list|)
expr_stmt|;
name|al
operator|.
name|add
argument_list|(
name|RENDERING_INTENT_PERCEPTUAL
argument_list|)
expr_stmt|;
name|al
operator|.
name|add
argument_list|(
name|RENDERING_INTENT_SATURATION
argument_list|)
expr_stmt|;
name|RENDERING_INTENTS
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|al
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|boolean
name|contains
parameter_list|(
name|Object
name|riArg
parameter_list|)
block|{
return|return
name|RENDERING_INTENTS
operator|.
name|contains
argument_list|(
name|riArg
argument_list|)
return|;
block|}
block|}
end_class

end_unit

