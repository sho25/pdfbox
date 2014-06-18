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
name|COSArray
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
name|COSInteger
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
name|COSArrayList
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * A line dash pattern for stroking paths.  * Instances of PDLineDashPattern are immutable.  * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDLineDashPattern
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|final
name|int
name|phase
decl_stmt|;
specifier|private
specifier|final
name|float
index|[]
name|array
decl_stmt|;
comment|/**      * Creates a new line dash pattern, with no dashes and a phase of 0.      */
specifier|public
name|PDLineDashPattern
parameter_list|()
block|{
name|array
operator|=
operator|new
name|float
index|[]
block|{ }
expr_stmt|;
name|phase
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * Creates a new line dash pattern from a dash array and phase.      * @param array the dash array      * @param phase the phase      */
specifier|public
name|PDLineDashPattern
parameter_list|(
name|COSArray
name|array
parameter_list|,
name|int
name|phase
parameter_list|)
block|{
name|this
operator|.
name|array
operator|=
name|array
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
name|this
operator|.
name|phase
operator|=
name|phase
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
name|COSArray
name|cos
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|cos
operator|.
name|add
argument_list|(
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|array
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|cos
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
name|phase
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cos
return|;
block|}
comment|/**      * Returns the dash phase.      * This specifies the distance into the dash pattern at which to start the dash.      * @return the dash phase      */
specifier|public
name|int
name|getPhase
parameter_list|()
block|{
return|return
name|phase
return|;
block|}
comment|/**      * Returns the dash array.      * @return the dash array      */
specifier|public
name|float
index|[]
name|getDashArray
parameter_list|()
block|{
return|return
name|array
operator|.
name|clone
argument_list|()
return|;
block|}
block|}
end_class

end_unit

