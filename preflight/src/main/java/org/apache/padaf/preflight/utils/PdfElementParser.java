begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|utils
package|;
end_package

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
name|COSDocument
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
name|BaseParser
import|;
end_import

begin_comment
comment|/**  * This class is a tool to parse a byte array as a COS object (COSDIctionary)  */
end_comment

begin_class
specifier|public
class|class
name|PdfElementParser
extends|extends
name|BaseParser
block|{
comment|/**    * Create the PDFElementParser object.    *     * @param cd    *          a COSDocument which will be used to parse the byte array    * @param input    *          the byte array to parse    * @throws IOException    */
specifier|public
name|PdfElementParser
parameter_list|(
name|COSDocument
name|cd
parameter_list|,
name|byte
index|[]
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|this
operator|.
name|document
operator|=
name|cd
expr_stmt|;
block|}
comment|/**    * Parse the input byte array of the constructor call as a COSDictionary.    *     * @return a COSDictionary if the parsing succeed.    * @throws IOException    *           If the byte array isn't a COSDictionary or if there are an error    *           on the stream parsing    */
specifier|public
name|COSDictionary
name|parseAsDictionary
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|parseCOSDictionary
argument_list|()
return|;
block|}
comment|/**    * Return the COSDocument used to create this object.    *     * @return    */
specifier|public
name|COSDocument
name|getDocument
parameter_list|()
block|{
return|return
name|this
operator|.
name|document
return|;
block|}
block|}
end_class

end_unit

