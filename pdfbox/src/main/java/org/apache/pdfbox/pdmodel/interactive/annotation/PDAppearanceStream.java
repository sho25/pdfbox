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
name|annotation
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
name|common
operator|.
name|PDStream
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
name|graphics
operator|.
name|form
operator|.
name|PDFormXObject
import|;
end_import

begin_comment
comment|/**  * An appearance stream is a form XObject, a self-contained content stream that shall be rendered inside the annotation  * rectangle.  *  * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|PDAppearanceStream
extends|extends
name|PDFormXObject
block|{
comment|/**      * Creates a Form XObject for reading.      *       * @param stream The XObject stream      */
specifier|public
name|PDAppearanceStream
parameter_list|(
name|COSStream
name|stream
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|PDStream
argument_list|(
name|stream
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Form Image XObject for writing, in the given document.      *       * @param document The current document      */
specifier|public
name|PDAppearanceStream
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
block|}
block|}
end_class

end_unit

