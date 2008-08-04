begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|filter
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
name|pdfbox
operator|.
name|cos
operator|.
name|COSDictionary
import|;
end_import

begin_comment
comment|/**  * This is the interface that will be used to apply filters to a byte stream.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.7 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|Filter
block|{
comment|/**      * This will decode some compressed data.      *      * @param compressedData The compressed byte stream.      * @param result The place to write the uncompressed byte stream.      * @param options The options to use to encode the data.      * @param filterIndex The index to the filter being decoded.      *      * @throws IOException If there is an error decompressing the stream.      */
specifier|public
name|void
name|decode
parameter_list|(
name|InputStream
name|compressedData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * This will encode some data.      *      * @param rawData The raw data to encode.      * @param result The place to write to encoded results to.      * @param options The options to use to encode the data.      * @param filterIndex The index to the filter being encoded.      *      * @throws IOException If there is an error compressing the stream.      */
specifier|public
name|void
name|encode
parameter_list|(
name|InputStream
name|rawData
parameter_list|,
name|OutputStream
name|result
parameter_list|,
name|COSDictionary
name|options
parameter_list|,
name|int
name|filterIndex
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

