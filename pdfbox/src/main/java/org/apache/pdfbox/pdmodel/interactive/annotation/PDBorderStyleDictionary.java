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
name|COSObjectable
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
name|PDLineDashPattern
import|;
end_import

begin_comment
comment|/**  * This class represents a PDF /BS entry the border style dictionary.  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDBorderStyleDictionary
implements|implements
name|COSObjectable
block|{
comment|/*      * The various values of the style for the border as defined in the PDF 1.6      * reference Table 8.13      */
comment|/**      * Constant for the name of a solid style.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_SOLID
init|=
literal|"S"
decl_stmt|;
comment|/**      * Constant for the name of a dashed style.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_DASHED
init|=
literal|"D"
decl_stmt|;
comment|/**      * Constant for the name of a beveled style.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_BEVELED
init|=
literal|"B"
decl_stmt|;
comment|/**      * Constant for the name of a inset style.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_INSET
init|=
literal|"I"
decl_stmt|;
comment|/**      * Constant for the name of a underline style.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_UNDERLINE
init|=
literal|"U"
decl_stmt|;
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDBorderStyleDictionary
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dict      *            a border style dictionary.      */
specifier|public
name|PDBorderStyleDictionary
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|dictionary
operator|=
name|dict
expr_stmt|;
block|}
comment|/**      * returns the dictionary.      *      * @return the dictionary      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * returns the dictionary.      *      * @return the dictionary      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * This will set the border width in points, 0 = no border.      *      * @param w      *            float the width in points      */
specifier|public
name|void
name|setWidth
parameter_list|(
name|float
name|w
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFloat
argument_list|(
literal|"W"
argument_list|,
name|w
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border width in points, 0 = no border.      *      * @return flaot the width of the border in points      */
specifier|public
name|float
name|getWidth
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFloat
argument_list|(
literal|"W"
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * This will set the border style, see the STYLE_* constants for valid values.      *      * @param s      *            the border style to use      */
specifier|public
name|void
name|setStyle
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setName
argument_list|(
literal|"S"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border style, see the STYLE_* constants for valid      * values.      *      * @return the style of the border      */
specifier|public
name|String
name|getStyle
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
literal|"S"
argument_list|,
name|STYLE_SOLID
argument_list|)
return|;
block|}
comment|/**      * This will set the dash style used for drawing the border.      *      * @param dashArray      *            the dash style to use      */
specifier|public
name|void
name|setDashStyle
parameter_list|(
name|COSArray
name|dashArray
parameter_list|)
block|{
name|COSArray
name|array
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dashArray
operator|!=
literal|null
condition|)
block|{
name|array
operator|=
name|dashArray
expr_stmt|;
block|}
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"D"
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the dash style used for drawing the border.      *      * @return the dash style of the border      */
specifier|public
name|PDLineDashPattern
name|getDashStyle
parameter_list|()
block|{
name|COSArray
name|d
init|=
operator|(
name|COSArray
operator|)
name|getDictionary
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"D"
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
name|d
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|d
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|THREE
argument_list|)
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"D"
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|PDLineDashPattern
argument_list|(
name|d
argument_list|,
literal|0
argument_list|)
return|;
block|}
block|}
end_class

end_unit

