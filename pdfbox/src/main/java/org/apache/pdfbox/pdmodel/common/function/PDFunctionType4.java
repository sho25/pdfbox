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
operator|.
name|function
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
name|pdmodel
operator|.
name|PDDocument
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
name|PDStream
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * This class represents a type 4 function in a PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFunctionType4
extends|extends
name|PDStreamFunction
block|{
comment|/**      * Constructor to create a new blank type 4 function.      *      * @param doc The document that the function will be part of.      */
specifier|protected
name|PDFunctionType4
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
block|{
name|super
argument_list|(
name|doc
argument_list|,
literal|4
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param functionDictionary The prepopulated function dictionary.      */
specifier|public
name|PDFunctionType4
parameter_list|(
name|PDStream
name|functionDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|functionDictionary
argument_list|)
expr_stmt|;
block|}
comment|/**     * {@inheritDoc}     */
specifier|public
name|COSArray
name|Eval
parameter_list|(
name|COSArray
name|input
parameter_list|)
throws|throws
name|IOException
block|{
comment|//Implementation here will require evaluation of PostScript functions.
comment|//See section 3.9.4 of the PDF Reference.
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Not Implemented"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

