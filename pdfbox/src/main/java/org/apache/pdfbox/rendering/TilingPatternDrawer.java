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
name|rendering
package|;
end_package

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
comment|/**  * Tiling pattern drawer.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|TilingPatternDrawer
extends|extends
name|PageDrawer
block|{
comment|/**      * Tiling pattern constructor, loads properties from file.      *      * @param renderer renderer to render the page.      *       * @throws java.io.IOException If there is an error loading properties from the file.      */
specifier|public
name|TilingPatternDrawer
parameter_list|(
name|PDFRenderer
name|renderer
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|renderer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

