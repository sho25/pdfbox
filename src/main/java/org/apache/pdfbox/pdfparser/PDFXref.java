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
name|pdfparser
package|;
end_package

begin_comment
comment|/**  * This class represents a PDF xref.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFXref
block|{
specifier|private
name|long
name|count
decl_stmt|;
specifier|private
name|long
name|start
decl_stmt|;
comment|/**      * constructor.      *      * @param startValue The start attribute.      * @param countValue The count attribute.      */
specifier|public
name|PDFXref
parameter_list|(
name|long
name|startValue
parameter_list|,
name|long
name|countValue
parameter_list|)
block|{
name|setStart
argument_list|(
name|startValue
argument_list|)
expr_stmt|;
name|setCount
argument_list|(
name|countValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the count attribute.      *      * @return The count.      */
specifier|public
name|long
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
comment|/**      * This will get the start attribute.      *      * @return The start.      */
specifier|public
name|long
name|getStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
comment|/**      * This will set the count attribute.      *      * @param newCount The new count.      */
specifier|private
name|void
name|setCount
parameter_list|(
name|long
name|newCount
parameter_list|)
block|{
name|count
operator|=
name|newCount
expr_stmt|;
block|}
comment|/**      * This will set the start attribute.      *      * @param newStart The new start attribute.      */
specifier|private
name|void
name|setStart
parameter_list|(
name|long
name|newStart
parameter_list|)
block|{
name|start
operator|=
name|newStart
expr_stmt|;
block|}
block|}
end_class

end_unit

