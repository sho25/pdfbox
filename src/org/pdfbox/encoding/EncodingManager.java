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
name|encoding
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

begin_comment
comment|/**  * This class will handle getting the appropriate encodings.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.9 $  */
end_comment

begin_class
specifier|public
class|class
name|EncodingManager
block|{
specifier|private
specifier|static
specifier|final
name|Map
name|ENCODINGS
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
static|static
block|{
name|ENCODINGS
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|MAC_ROMAN_ENCODING
argument_list|,
operator|new
name|MacRomanEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|ENCODINGS
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|PDF_DOC_ENCODING
argument_list|,
operator|new
name|PdfDocEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|ENCODINGS
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|STANDARD_ENCODING
argument_list|,
operator|new
name|StandardEncoding
argument_list|()
argument_list|)
expr_stmt|;
name|ENCODINGS
operator|.
name|put
argument_list|(
name|COSName
operator|.
name|WIN_ANSI_ENCODING
argument_list|,
operator|new
name|WinAnsiEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the standard encoding.      *      * @return The standard encoding.      */
specifier|public
name|Encoding
name|getStandardEncoding
parameter_list|()
block|{
return|return
operator|(
name|Encoding
operator|)
name|ENCODINGS
operator|.
name|get
argument_list|(
name|COSName
operator|.
name|STANDARD_ENCODING
argument_list|)
return|;
block|}
comment|/**      * This will get an encoding by name.      *      * @param name The name of the encoding to get.      *      * @return The encoding that matches the name.      *      * @throws IOException If there is not encoding with that name.      */
specifier|public
name|Encoding
name|getEncoding
parameter_list|(
name|COSName
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|Encoding
name|encoding
init|=
operator|(
name|Encoding
operator|)
name|ENCODINGS
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown encoding for '"
operator|+
name|name
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
name|encoding
return|;
block|}
block|}
end_class

end_unit

