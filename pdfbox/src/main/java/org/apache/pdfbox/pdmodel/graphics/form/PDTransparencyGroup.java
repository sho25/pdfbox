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
name|form
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
name|COSStream
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
name|PDDocument
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
name|ResourceCache
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
name|PDStream
import|;
end_import

begin_comment
comment|/**  * A transparency group.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|PDTransparencyGroup
extends|extends
name|PDFormXObject
block|{
comment|/**      * Creates a Transparency Group for reading.      * @param stream The XObject stream      */
specifier|public
name|PDTransparencyGroup
parameter_list|(
name|PDStream
name|stream
parameter_list|)
block|{
name|super
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Transparency Group for reading.      * @param stream The XObject stream      */
specifier|public
name|PDTransparencyGroup
parameter_list|(
name|COSStream
name|stream
parameter_list|,
name|ResourceCache
name|cache
parameter_list|)
block|{
name|super
argument_list|(
name|stream
argument_list|,
name|cache
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Transparency Group for writing, in the given document.      * @param document The current document      */
specifier|public
name|PDTransparencyGroup
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
name|super
argument_list|(
name|document
argument_list|)
expr_stmt|;
comment|// todo: set mandatory fields
block|}
block|}
end_class

end_unit

