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
name|interactive
operator|.
name|annotation
operator|.
name|PDAppearanceStream
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
comment|/**      * Create a new appearance stream.      *      * @param appearance      *            The appearance stream to write to.      * @throws IOException      *             If there is an error writing to the page contents.      */
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
comment|/**      * Create a new appearance stream. Note that this is not actually a "page"      * content stream.      *      * @param appearance      *            The appearance stream to add to.      * @param outputStream      *            The appearances output stream to write to.      */
specifier|public
name|PDAppearanceContentStream
parameter_list|(
name|PDAppearanceStream
name|appearance
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
block|{
name|super
argument_list|(
literal|null
argument_list|,
name|outputStream
argument_list|,
name|appearance
operator|.
name|getResources
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the stroking color.      *       *<p>      * The command is only emitted if the color is not null and the number of      * components is gt 0.      *       * @see PDAbstractContentStream#setStrokingColor(PDColor)      */
specifier|public
name|boolean
name|setStrokingColorOnDemand
parameter_list|(
name|PDColor
name|color
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|color
operator|!=
literal|null
condition|)
block|{
name|float
index|[]
name|components
init|=
name|color
operator|.
name|getComponents
argument_list|()
decl_stmt|;
if|if
condition|(
name|components
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|setStrokingColor
argument_list|(
name|components
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Set the stroking color.      *       * @see PDAbstractContentStream#setStrokingColor(java.awt.Color)      * @param components      *            the color components dependent on the color space being used.      * @throws IOException      *             if an IO error occurs while writing to the stream.      */
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
break|break;
default|default:
break|break;
block|}
block|}
comment|/**      * Set the non stroking color.      *       *<p>      * The command is only emitted if the color is not null and the number of      * components is gt 0.      *       * @see PDAbstractContentStream#setNonStrokingColor(PDColor)      */
specifier|public
name|boolean
name|setNonStrokingColorOnDemand
parameter_list|(
name|PDColor
name|color
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|color
operator|!=
literal|null
condition|)
block|{
name|float
index|[]
name|components
init|=
name|color
operator|.
name|getComponents
argument_list|()
decl_stmt|;
if|if
condition|(
name|components
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|setNonStrokingColor
argument_list|(
name|components
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Set the non stroking color.      *       * @see PDAbstractContentStream#setNonStrokingColor(java.awt.Color)      * @param components      *            the color components dependent on the color space being used.      * @throws IOException      *             if an IO error occurs while writing to the stream.      */
specifier|public
name|void
name|setNonStrokingColor
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
literal|"g"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|3
case|:
name|writeOperator
argument_list|(
literal|"rg"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|4
case|:
name|writeOperator
argument_list|(
literal|"k"
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
specifier|public
name|void
name|setBorderLine
parameter_list|(
name|float
name|lineWidth
parameter_list|,
name|PDBorderStyleDictionary
name|bs
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Can't use PDBorderStyleDictionary.getDashStyle() as
comment|// this will return a default dash style if non is existing
if|if
condition|(
name|bs
operator|!=
literal|null
operator|&&
name|bs
operator|.
name|getCOSObject
argument_list|()
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
operator|&&
name|bs
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
name|setLineDashPattern
argument_list|(
name|bs
operator|.
name|getDashStyle
argument_list|()
operator|.
name|getDashArray
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
name|setLineWidthOnDemand
argument_list|(
name|lineWidth
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the line width. The command is only emitted if the lineWidth is      * different to 1.      *       * @param lineWidth the line width of the path.      * @throws java.io.IOException      * @see PDAbstractContentStream#setLineWidth(float)      */
specifier|public
name|void
name|setLineWidthOnDemand
parameter_list|(
name|float
name|lineWidth
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Acrobat doesn't write a line width command
comment|// for a line width of 1 as this is default.
comment|// Will do the same.
if|if
condition|(
operator|!
operator|(
name|Math
operator|.
name|abs
argument_list|(
name|lineWidth
operator|-
literal|1
argument_list|)
operator|<
literal|1e-6
operator|)
condition|)
block|{
name|setLineWidth
argument_list|(
name|lineWidth
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Draw a shape.      *      *<p>      * Dependent on the lineWidth and whether or not there is a background to be generated there are      * different commands to be used for draw a shape.      *      * @param lineWidth the line width of the path.      * @param hasStroke shall there be a stroking color.      * @param hasFill shall there be a fill color.      * @throws IOException if an IO error occurs while writing to the stream.      */
specifier|public
name|void
name|drawShape
parameter_list|(
name|float
name|lineWidth
parameter_list|,
name|boolean
name|hasStroke
parameter_list|,
name|boolean
name|hasFill
parameter_list|)
throws|throws
name|IOException
block|{
comment|// initial setting if stroking shall be done
name|boolean
name|resolvedHasStroke
init|=
name|hasStroke
decl_stmt|;
comment|// no stroking for very small lines
if|if
condition|(
name|lineWidth
operator|<
literal|1e-6
condition|)
block|{
name|resolvedHasStroke
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|hasFill
operator|&&
name|resolvedHasStroke
condition|)
block|{
name|fillAndStroke
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|resolvedHasStroke
condition|)
block|{
name|stroke
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hasFill
condition|)
block|{
name|fill
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|writeOperator
argument_list|(
literal|"n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

