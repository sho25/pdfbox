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
name|common
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSName
import|;
end_import

begin_comment
comment|/**  * Contains information for a page label range.  *   * @author Igor Podolskiy  *   * @see PDPageLabels  *   */
end_comment

begin_class
specifier|public
class|class
name|PDPageLabelRange
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|root
decl_stmt|;
comment|// Page label dictonary (PDF32000-1:2008 Section 12.4.2, Table 159)
specifier|private
specifier|static
specifier|final
name|COSName
name|KEY_START
init|=
name|COSName
operator|.
name|ST
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|COSName
name|KEY_PREFIX
init|=
name|COSName
operator|.
name|P
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|COSName
name|KEY_STYLE
init|=
name|COSName
operator|.
name|S
decl_stmt|;
comment|// Style entry values (PDF32000-1:2008 Section 12.4.2, Table 159)
comment|/**      * Decimal page numbering style (1, 2, 3, ...).      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_DECIMAL
init|=
literal|"D"
decl_stmt|;
comment|/**      * Roman numbers (upper case) numbering style (I, II, III, IV, ...).      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_ROMAN_UPPER
init|=
literal|"R"
decl_stmt|;
comment|/**      * Roman numbers (lower case) numbering style (i, ii, iii, iv, ...).      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_ROMAN_LOWER
init|=
literal|"r"
decl_stmt|;
comment|/**      * Letter (upper case) numbering style (A, B, ..., Z, AA, BB, ..., ZZ, AAA,      * ...).      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_LETTERS_UPPER
init|=
literal|"A"
decl_stmt|;
comment|/**      * Letter (lower case) numbering style (a, b, ..., z, aa, bb, ..., zz, aaa,      * ...).      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_LETTERS_LOWER
init|=
literal|"a"
decl_stmt|;
comment|/**      * Creates a new empty page label range object.      */
specifier|public
name|PDPageLabelRange
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new page label range object from the given dictionary.      *       * @param dict      *            the base dictionary for the new object.      */
specifier|public
name|PDPageLabelRange
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|root
operator|=
name|dict
expr_stmt|;
block|}
comment|/**      * Returns the underlying dictionary.      *       * @return the underlying dictionary.      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|root
return|;
block|}
comment|/**      * Returns the numbering style for this page range.      *       * @return one of the STYLE_* constants      */
specifier|public
name|String
name|getStyle
parameter_list|()
block|{
return|return
name|root
operator|.
name|getNameAsString
argument_list|(
name|KEY_STYLE
argument_list|)
return|;
block|}
comment|/**      * Sets the numbering style for this page range.      *       * @param style      *            one of the STYLE_* constants or {@code null} if no page      *            numbering is desired.      */
specifier|public
name|void
name|setStyle
parameter_list|(
name|String
name|style
parameter_list|)
block|{
if|if
condition|(
name|style
operator|!=
literal|null
condition|)
block|{
name|root
operator|.
name|setName
argument_list|(
name|KEY_STYLE
argument_list|,
name|style
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|root
operator|.
name|removeItem
argument_list|(
name|KEY_STYLE
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the start value for page numbering in this page range.      *       * @return a positive integer the start value for numbering.      */
specifier|public
name|int
name|getStart
parameter_list|()
block|{
return|return
name|root
operator|.
name|getInt
argument_list|(
name|KEY_START
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * Sets the start value for page numbering in this page range.      *       * @param start      *            a positive integer representing the start value.      * @throws IllegalArgumentException      *             if {@code start} is not a positive integer      */
specifier|public
name|void
name|setStart
parameter_list|(
name|int
name|start
parameter_list|)
block|{
if|if
condition|(
name|start
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The page numbering start value must be a positive integer"
argument_list|)
throw|;
block|}
name|root
operator|.
name|setInt
argument_list|(
name|KEY_START
argument_list|,
name|start
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the page label prefix for this page range.      *       * @return the page label prefix for this page range, or {@code null} if no      *         prefix has been defined.      */
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|root
operator|.
name|getString
argument_list|(
name|KEY_PREFIX
argument_list|)
return|;
block|}
comment|/**      * Sets the page label prefix for this page range.      *       * @param prefix      *            the page label prefix for this page range, or {@code null} to      *            unset the prefix.      */
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
name|root
operator|.
name|setString
argument_list|(
name|KEY_PREFIX
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|root
operator|.
name|removeItem
argument_list|(
name|KEY_PREFIX
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

