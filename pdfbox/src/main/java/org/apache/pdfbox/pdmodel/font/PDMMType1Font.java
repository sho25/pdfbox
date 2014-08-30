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
name|font
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Type 1 Multiple Master Font.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDMMType1Font
extends|extends
name|PDType1Font
block|{
comment|/**      * Creates an MMType1Font from a Font dictionary in a PDF.      *      * @param fontDictionary font dictionary      */
specifier|public
name|PDMMType1Font
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

