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
name|COSStream
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
name|PDMatrix
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * This is implementation of the Type3 Font.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.8 $  */
end_comment

begin_class
specifier|public
class|class
name|PDType3Font
extends|extends
name|PDSimpleFont
block|{
comment|//A map of character code to java.awt.Image for the glyph
specifier|private
name|Map
argument_list|<
name|Character
argument_list|,
name|Image
argument_list|>
name|images
init|=
operator|new
name|HashMap
argument_list|<
name|Character
argument_list|,
name|Image
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDType3Font
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|font
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|TYPE3
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param fontDictionary The font dictionary according to the PDF specification.      */
specifier|public
name|PDType3Font
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * Type3 fonts have their glyphs defined as a content stream.  This      * will create the image that represents that character      *      * @throws IOException If there is an error creating the image.      */
specifier|private
name|Image
name|createImageIfNecessary
parameter_list|(
name|char
name|character
parameter_list|)
throws|throws
name|IOException
block|{
name|Character
name|c
init|=
operator|new
name|Character
argument_list|(
name|character
argument_list|)
decl_stmt|;
name|Image
name|retval
init|=
operator|(
name|Image
operator|)
name|images
operator|.
name|get
argument_list|(
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
name|retval
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|charProcs
init|=
operator|(
name|COSDictionary
operator|)
name|font
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CHAR_PROCS
argument_list|)
decl_stmt|;
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|charProcs
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|""
operator|+
name|character
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|Type3StreamParser
name|parser
init|=
operator|new
name|Type3StreamParser
argument_list|()
decl_stmt|;
name|retval
operator|=
name|parser
operator|.
name|createImage
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|images
operator|.
name|put
argument_list|(
name|c
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//stream should not be null!!
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|void
name|drawString
parameter_list|(
name|String
name|string
parameter_list|,
name|int
index|[]
name|codePoints
parameter_list|,
name|Graphics
name|g
parameter_list|,
name|float
name|fontSize
parameter_list|,
name|AffineTransform
name|at
parameter_list|,
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|string
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
comment|//todo need to use image observers and such
name|char
name|c
init|=
name|string
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Image
name|image
init|=
name|createImageIfNecessary
argument_list|(
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
name|int
name|newWidth
init|=
call|(
name|int
call|)
argument_list|(
literal|.12
operator|*
name|image
operator|.
name|getWidth
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|newHeight
init|=
call|(
name|int
call|)
argument_list|(
literal|.12
operator|*
name|image
operator|.
name|getHeight
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|newWidth
operator|>
literal|0
operator|&&
name|newHeight
operator|>
literal|0
condition|)
block|{
name|image
operator|=
name|image
operator|.
name|getScaledInstance
argument_list|(
name|newWidth
argument_list|,
name|newHeight
argument_list|,
name|Image
operator|.
name|SCALE_SMOOTH
argument_list|)
expr_stmt|;
name|g
operator|.
name|drawImage
argument_list|(
name|image
argument_list|,
operator|(
name|int
operator|)
name|x
argument_list|,
operator|(
name|int
operator|)
name|y
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|x
operator|+=
name|newWidth
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Set the font matrix for this type3 font.      *      * @param matrix The font matrix for this type3 font.      */
specifier|public
name|void
name|setFontMatrix
parameter_list|(
name|PDMatrix
name|matrix
parameter_list|)
block|{
name|font
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FONT_MATRIX
argument_list|,
name|matrix
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

