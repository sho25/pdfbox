begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|operator
package|;
end_package

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
name|pdfbox
operator|.
name|util
operator|.
name|PDFOperator
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

begin_comment
comment|/**  *<p>Titre : PDFEngine Modification.</p>  *<p>Description : Structal modification of the PDFEngine class :  * the long sequence of conditions in processOperator is remplaced by  * this strategy pattern</p>  *<p>Copyright : Copyright (c) 2004</p>  *<p>Soci�t� : DBGS</p>  * @author Huault : huault@free.fr  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|SetTextFont
extends|extends
name|OperatorProcessor
block|{
comment|/**      * Tf selectfont Set text font and size.      * @param operator The operator that is being executed.      * @param arguments List      * @throws IOException If an error occurs while processing the font.      */
specifier|public
name|void
name|process
parameter_list|(
name|PDFOperator
name|operator
parameter_list|,
name|List
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
comment|//there are some documents that are incorrectly structured and
comment|//arguments are in the wrong spot, so we will silently ignore them
comment|//if there are no arguments
if|if
condition|(
name|arguments
operator|.
name|size
argument_list|()
operator|>=
literal|2
condition|)
block|{
comment|//set font and size
name|COSName
name|fontName
init|=
operator|(
name|COSName
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|float
name|fontSize
init|=
operator|(
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
decl_stmt|;
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|setFontSize
argument_list|(
name|fontSize
argument_list|)
expr_stmt|;
comment|//old way
comment|//graphicsState.getTextState().getFont() = (COSObject)stream.getDictionaryObject( fontName );
comment|//if( graphicsState.getTextState().getFont() == null )
comment|//{
comment|//    graphicsState.getTextState().getFont() = (COSObject)graphicsState.getTextState().getFont()
comment|//                                           Dictionary.getItem( fontName );
comment|//}
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|setFont
argument_list|(
operator|(
name|PDFont
operator|)
name|context
operator|.
name|getFonts
argument_list|()
operator|.
name|get
argument_list|(
name|fontName
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getTextState
argument_list|()
operator|.
name|getFont
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Could not find font("
operator|+
name|fontName
operator|+
literal|") in map="
operator|+
name|context
operator|.
name|getFonts
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

