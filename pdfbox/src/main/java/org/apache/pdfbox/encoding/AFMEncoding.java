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
name|encoding
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|afm
operator|.
name|CharMetric
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|afm
operator|.
name|FontMetric
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

begin_comment
comment|/**  * This will handle the encoding from an AFM font.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  *   */
end_comment

begin_class
specifier|public
class|class
name|AFMEncoding
extends|extends
name|Encoding
block|{
comment|/**      * Constructor.      *      * @param fontInfo The font metric information.      */
specifier|public
name|AFMEncoding
parameter_list|(
name|FontMetric
name|fontInfo
parameter_list|)
block|{
name|Iterator
argument_list|<
name|CharMetric
argument_list|>
name|characters
init|=
name|fontInfo
operator|.
name|getCharMetrics
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|characters
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|CharMetric
name|nextMetric
init|=
name|characters
operator|.
name|next
argument_list|()
decl_stmt|;
name|addCharacterEncoding
argument_list|(
name|nextMetric
operator|.
name|getCharacterCode
argument_list|()
argument_list|,
name|nextMetric
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

