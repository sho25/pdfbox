begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2018 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
operator|.
name|handlers
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
name|COSNumber
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
name|color
operator|.
name|PDColor
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
name|color
operator|.
name|PDDeviceGray
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
name|PDAnnotation
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
name|PDBorderStyleDictionary
import|;
end_import

begin_comment
comment|/**  * Class to collect all sort of border info about annotations.  *   * @author Tilman Hausherr  */
end_comment

begin_class
class|class
name|AnnotationBorder
block|{
name|float
index|[]
name|dashArray
init|=
literal|null
decl_stmt|;
name|boolean
name|underline
init|=
literal|false
decl_stmt|;
name|float
name|width
init|=
literal|0
decl_stmt|;
name|PDColor
name|color
decl_stmt|;
comment|// return border info. BorderStyle must be provided as parameter because
comment|// method is not available in the base class
specifier|static
name|AnnotationBorder
name|getAnnotationBorder
parameter_list|(
name|PDAnnotation
name|annotation
parameter_list|,
name|PDBorderStyleDictionary
name|borderStyle
parameter_list|)
block|{
name|AnnotationBorder
name|ab
init|=
operator|new
name|AnnotationBorder
argument_list|()
decl_stmt|;
if|if
condition|(
name|borderStyle
operator|==
literal|null
condition|)
block|{
name|COSArray
name|border
init|=
name|annotation
operator|.
name|getBorder
argument_list|()
decl_stmt|;
if|if
condition|(
name|border
operator|.
name|size
argument_list|()
operator|>=
literal|3
operator|&&
name|border
operator|.
name|getObject
argument_list|(
literal|2
argument_list|)
operator|instanceof
name|COSNumber
condition|)
block|{
name|ab
operator|.
name|width
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|border
operator|.
name|getObject
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|border
operator|.
name|size
argument_list|()
operator|>
literal|3
condition|)
block|{
name|COSBase
name|base3
init|=
name|border
operator|.
name|getObject
argument_list|(
literal|3
argument_list|)
decl_stmt|;
if|if
condition|(
name|base3
operator|instanceof
name|COSArray
condition|)
block|{
name|ab
operator|.
name|dashArray
operator|=
operator|(
operator|(
name|COSArray
operator|)
name|base3
operator|)
operator|.
name|toFloatArray
argument_list|()
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|ab
operator|.
name|width
operator|=
name|borderStyle
operator|.
name|getWidth
argument_list|()
expr_stmt|;
if|if
condition|(
name|borderStyle
operator|.
name|getStyle
argument_list|()
operator|.
name|equals
argument_list|(
name|PDBorderStyleDictionary
operator|.
name|STYLE_DASHED
argument_list|)
condition|)
block|{
name|ab
operator|.
name|dashArray
operator|=
name|borderStyle
operator|.
name|getDashStyle
argument_list|()
operator|.
name|getDashArray
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|borderStyle
operator|.
name|getStyle
argument_list|()
operator|.
name|equals
argument_list|(
name|PDBorderStyleDictionary
operator|.
name|STYLE_UNDERLINE
argument_list|)
condition|)
block|{
name|ab
operator|.
name|underline
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|ab
operator|.
name|color
operator|=
name|annotation
operator|.
name|getColor
argument_list|()
expr_stmt|;
if|if
condition|(
name|ab
operator|.
name|color
operator|==
literal|null
condition|)
block|{
comment|// spec is unclear, but black seems to be the right thing to do
name|ab
operator|.
name|color
operator|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|}
argument_list|,
name|PDDeviceGray
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ab
operator|.
name|dashArray
operator|!=
literal|null
condition|)
block|{
name|boolean
name|allZero
init|=
literal|true
decl_stmt|;
for|for
control|(
name|float
name|f
range|:
name|ab
operator|.
name|dashArray
control|)
block|{
if|if
condition|(
name|f
operator|!=
literal|0
condition|)
block|{
name|allZero
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|allZero
condition|)
block|{
name|ab
operator|.
name|dashArray
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|ab
return|;
block|}
block|}
end_class

end_unit

