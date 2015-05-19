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
name|form
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|util
operator|.
name|List
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
name|operator
operator|.
name|Operator
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
name|cos
operator|.
name|COSString
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
name|pdfparser
operator|.
name|PDFStreamParser
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
name|PDPageContentStream
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
name|PDResources
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
name|font
operator|.
name|PDFont
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
comment|/**  * Represents a default appearance string, as found in the /DA entry of free text annotations.  *   *<p>The default appearance string (DA) contains any graphics state or text state operators needed  * to establish the graphics state parameters, such as text size and colour, for displaying the  * field’s variable text. Only operators that are allowed within text objects shall occur in this  * string.  *   * Note: This class is not yet public, as its API is still unstable.  */
end_comment

begin_class
class|class
name|PDAppearanceString
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|tokens
decl_stmt|;
specifier|private
specifier|final
name|PDResources
name|defaultResources
decl_stmt|;
comment|/**      * Constructor for reading an existing DA string.      *       * @param defaultResources DR entry      * @param defaultAppearance DA entry      * @throws IOException If the DA could not be parsed      */
name|PDAppearanceString
parameter_list|(
name|COSString
name|defaultAppearance
parameter_list|,
name|PDResources
name|defaultResources
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|defaultAppearance
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"/DA is a required entry"
argument_list|)
throw|;
block|}
if|if
condition|(
name|defaultResources
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"/DR is a required entry"
argument_list|)
throw|;
block|}
name|ByteArrayInputStream
name|stream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|defaultAppearance
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|PDFStreamParser
name|parser
init|=
operator|new
name|PDFStreamParser
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|tokens
operator|=
name|parser
operator|.
name|getTokens
argument_list|()
expr_stmt|;
name|parser
operator|.
name|close
argument_list|()
expr_stmt|;
name|this
operator|.
name|defaultResources
operator|=
name|defaultResources
expr_stmt|;
block|}
comment|/**      * Returns the font size.      */
specifier|public
name|float
name|getFontSize
parameter_list|()
block|{
if|if
condition|(
operator|!
name|tokens
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// daString looks like "BMC /Helv 3.4 Tf EMC"
comment|// use the fontsize of the default existing apperance stream
name|int
name|fontIndex
init|=
name|tokens
operator|.
name|indexOf
argument_list|(
name|Operator
operator|.
name|getOperator
argument_list|(
literal|"Tf"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|fontIndex
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|tokens
operator|.
name|get
argument_list|(
name|fontIndex
operator|-
literal|1
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
block|}
comment|// default font size is 12 in Acrobat
return|return
literal|12
return|;
block|}
comment|/**      * w in an appearance stream represents the lineWidth.      *      * @return the linewidth      */
specifier|public
name|float
name|getLineWidth
parameter_list|()
block|{
name|float
name|retval
init|=
literal|0f
decl_stmt|;
if|if
condition|(
name|tokens
operator|!=
literal|null
condition|)
block|{
name|int
name|btIndex
init|=
name|tokens
operator|.
name|indexOf
argument_list|(
name|Operator
operator|.
name|getOperator
argument_list|(
literal|"BT"
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|wIndex
init|=
name|tokens
operator|.
name|indexOf
argument_list|(
name|Operator
operator|.
name|getOperator
argument_list|(
literal|"w"
argument_list|)
argument_list|)
decl_stmt|;
comment|// the w should only be used if it is before the first BT.
if|if
condition|(
name|wIndex
operator|>
literal|0
operator|&&
operator|(
name|wIndex
operator|<
name|btIndex
operator|||
name|btIndex
operator|==
operator|-
literal|1
operator|)
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|tokens
operator|.
name|get
argument_list|(
name|wIndex
operator|-
literal|1
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Returns the font.      *       * @throws IOException If the font could not be found.      */
specifier|public
name|PDFont
name|getFont
parameter_list|()
throws|throws
name|IOException
block|{
name|COSName
name|name
init|=
name|getFontResourceName
argument_list|()
decl_stmt|;
name|PDFont
name|font
init|=
name|defaultResources
operator|.
name|getFont
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// todo: handle cases where font == null with special mapping logic (see PDFBOX-2661)
if|if
condition|(
name|font
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not find font: /"
operator|+
name|name
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|font
return|;
block|}
comment|/**      * Returns the name of the font in the Resources.      */
specifier|private
name|COSName
name|getFontResourceName
parameter_list|()
block|{
name|int
name|setFontOperatorIndex
init|=
name|tokens
operator|.
name|indexOf
argument_list|(
name|Operator
operator|.
name|getOperator
argument_list|(
literal|"Tf"
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|COSName
operator|)
name|tokens
operator|.
name|get
argument_list|(
name|setFontOperatorIndex
operator|-
literal|2
argument_list|)
return|;
block|}
comment|/**      * Writes the DA string to the given content stream.      */
name|void
name|writeTo
parameter_list|(
name|PDPageContentStream
name|contents
parameter_list|,
name|float
name|zeroFontSize
parameter_list|)
throws|throws
name|IOException
block|{
name|float
name|fontSize
init|=
name|getFontSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|fontSize
operator|==
literal|0
condition|)
block|{
name|fontSize
operator|=
name|zeroFontSize
expr_stmt|;
block|}
name|contents
operator|.
name|setFont
argument_list|(
name|getFont
argument_list|()
argument_list|,
name|fontSize
argument_list|)
expr_stmt|;
comment|// todo: set more state...
block|}
comment|/**      * Copies any needed resources from the document’s DR dictionary into the stream’s Resources      * dictionary. Resources with the same name shall be left intact.      */
name|void
name|copyNeededResourcesTo
parameter_list|(
name|PDAppearanceStream
name|appearanceStream
parameter_list|)
throws|throws
name|IOException
block|{
comment|// make sure we have resources
name|PDResources
name|streamResources
init|=
name|appearanceStream
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|streamResources
operator|==
literal|null
condition|)
block|{
name|streamResources
operator|=
operator|new
name|PDResources
argument_list|()
expr_stmt|;
name|appearanceStream
operator|.
name|setResources
argument_list|(
name|streamResources
argument_list|)
expr_stmt|;
block|}
comment|// fonts
name|COSName
name|fontName
init|=
name|getFontResourceName
argument_list|()
decl_stmt|;
if|if
condition|(
name|streamResources
operator|.
name|getFont
argument_list|(
name|fontName
argument_list|)
operator|==
literal|null
condition|)
block|{
name|streamResources
operator|.
name|put
argument_list|(
name|fontName
argument_list|,
name|getFont
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// todo: other kinds of resource...
block|}
block|}
end_class

end_unit

