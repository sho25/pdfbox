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
name|digitalsignature
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_comment
comment|/**  * Interface for external signature creation scenarios. It contains method for retrieving PDF data  * to be sign and setting created CMS signature to the PDF.  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ExternalSigningSupport
block|{
comment|/**      * Get PDF content to be signed. Obtained InputStream must be closed after use.      *      * @return content stream      */
name|InputStream
name|getContent
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Set CMS signature bytes to PDF.      *      * @param signature CMS signature as byte array      *      * @throws IOException if exception occured during PDF writing      */
name|void
name|setSignature
parameter_list|(
name|byte
index|[]
name|signature
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit
