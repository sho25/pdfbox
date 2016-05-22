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
name|java
operator|.
name|io
operator|.
name|Closeable
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|NumberFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|contentstream
operator|.
name|PDAbstractContentStream
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAppearanceStream
import|;
end_import

begin_comment
comment|/**  * Provides the ability to write to a page content stream.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDAppearanceContentStream
extends|extends
name|PDAbstractContentStream
implements|implements
name|Closeable
block|{
comment|/**      * This is to choose what to do with the stream: overwrite, append or prepend.      */
specifier|public
specifier|static
enum|enum
name|AppendMode
block|{
comment|/**          * Overwrite the existing page content streams.          */
name|OVERWRITE
block|,
comment|/**          * Append the content stream after all existing page content streams.          */
name|APPEND
block|,
comment|/**          * Insert before all other page content streams.          */
name|PREPEND
block|;
specifier|public
name|boolean
name|isOverwrite
parameter_list|()
block|{
return|return
name|this
operator|==
name|OVERWRITE
return|;
block|}
specifier|public
name|boolean
name|isPrepend
parameter_list|()
block|{
return|return
name|this
operator|==
name|PREPEND
return|;
block|}
block|}
comment|// number format
specifier|private
specifier|final
name|NumberFormat
name|formatDecimal
init|=
name|NumberFormat
operator|.
name|getNumberInstance
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
comment|/**      * Create a new appearance stream.      *      * @param doc The document the page is part of.      * @param appearance The appearance stream to write to.      * @throws IOException If there is an error writing to the page contents.      */
specifier|public
name|PDAppearanceContentStream
parameter_list|(
name|PDAppearanceStream
name|appearance
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|appearance
argument_list|,
name|appearance
operator|.
name|getStream
argument_list|()
operator|.
name|createOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a new appearance stream. Note that this is not actually a "page" content stream.      *      * @param doc The document the appearance is part of.      * @param appearance The appearance stream to add to.      * @param outputStream The appearances output stream to write to.      * @throws IOException If there is an error writing to the page contents.      */
specifier|public
name|PDAppearanceContentStream
parameter_list|(
name|PDAppearanceStream
name|appearance
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|appearance
argument_list|,
name|outputStream
argument_list|)
expr_stmt|;
name|setResources
argument_list|(
name|appearance
operator|.
name|getResources
argument_list|()
argument_list|)
expr_stmt|;
name|formatDecimal
operator|.
name|setMaximumFractionDigits
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|formatDecimal
operator|.
name|setGroupingUsed
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setStrokingColor
parameter_list|(
name|float
index|[]
name|components
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|float
name|value
range|:
name|components
control|)
block|{
name|writeOperand
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|int
name|numComponents
init|=
name|components
operator|.
name|length
decl_stmt|;
switch|switch
condition|(
name|numComponents
condition|)
block|{
case|case
literal|1
case|:
name|writeOperator
argument_list|(
literal|"G"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|writeOperator
argument_list|(
literal|"RG"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|4
case|:
name|writeOperator
argument_list|(
literal|"K"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

