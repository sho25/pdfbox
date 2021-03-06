begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License), Version 2.0  * (the "License")), you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing), software  * distributed under the License is distributed on an "AS IS" BASIS),  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND), either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
package|;
end_package

begin_comment
comment|/**  * A name object specifying the page layout shall be used when the document is opened.  *  * @author John Hewson  */
end_comment

begin_enum
specifier|public
enum|enum
name|PageLayout
block|{
comment|/** Display one page at a time. */
name|SINGLE_PAGE
argument_list|(
literal|"SinglePage"
argument_list|)
block|,
comment|/**  Display the pages in one column. */
name|ONE_COLUMN
argument_list|(
literal|"OneColumn"
argument_list|)
block|,
comment|/** Display the pages in two columns), with odd numbered pages on the left. */
name|TWO_COLUMN_LEFT
argument_list|(
literal|"TwoColumnLeft"
argument_list|)
block|,
comment|/** Display the pages in two columns), with odd numbered pages on the right.  */
name|TWO_COLUMN_RIGHT
argument_list|(
literal|"TwoColumnRight"
argument_list|)
block|,
comment|/** Display the pages two at a time), with odd-numbered pages on the left. */
name|TWO_PAGE_LEFT
argument_list|(
literal|"TwoPageLeft"
argument_list|)
block|,
comment|/** Display the pages two at a time), with odd-numbered pages on the right. */
name|TWO_PAGE_RIGHT
argument_list|(
literal|"TwoPageRight"
argument_list|)
block|;
specifier|public
specifier|static
name|PageLayout
name|fromString
parameter_list|(
name|String
name|value
parameter_list|)
block|{
for|for
control|(
name|PageLayout
name|instance
range|:
name|PageLayout
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|instance
operator|.
name|value
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|instance
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|value
argument_list|)
throw|;
block|}
specifier|private
specifier|final
name|String
name|value
decl_stmt|;
name|PageLayout
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Returns the string value, as used in a PDF file.      */
specifier|public
name|String
name|stringValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
block|}
end_enum

end_unit

