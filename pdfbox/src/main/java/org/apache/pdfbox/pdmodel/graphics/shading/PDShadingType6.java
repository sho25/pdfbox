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
name|graphics
operator|.
name|shading
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Paint
import|;
end_import

begin_comment
comment|/**  * Resources for a shading type 6 (Coons Patch Mesh).  */
end_comment

begin_class
specifier|public
class|class
name|PDShadingType6
extends|extends
name|PDShadingType4
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDShadingType6
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor using the given shading dictionary.      * @param shadingDictionary the dictionary for this shading      */
specifier|public
name|PDShadingType6
parameter_list|(
name|COSDictionary
name|shadingDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|shadingDictionary
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getShadingType
parameter_list|()
block|{
return|return
name|PDShading
operator|.
name|SHADING_TYPE6
return|;
block|}
annotation|@
name|Override
specifier|public
name|Paint
name|toPaint
parameter_list|(
name|Matrix
name|matrix
parameter_list|,
name|int
name|pageHeight
parameter_list|)
block|{
comment|// TODO ...
name|LOG
operator|.
name|debug
argument_list|(
literal|"Type 6 Shading not implemented"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Color
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
return|;
comment|// transparent
block|}
block|}
end_class

end_unit

