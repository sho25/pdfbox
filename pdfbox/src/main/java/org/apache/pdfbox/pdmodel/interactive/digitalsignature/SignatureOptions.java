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
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|InputStream
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
name|io
operator|.
name|RandomAccessBufferedFileInputStream
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
name|PDFParser
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
name|digitalsignature
operator|.
name|visible
operator|.
name|PDVisibleSigProperties
import|;
end_import

begin_comment
comment|/**  * TODO description needed  */
end_comment

begin_class
specifier|public
class|class
name|SignatureOptions
implements|implements
name|Closeable
block|{
specifier|private
name|COSDocument
name|visualSignature
decl_stmt|;
specifier|private
name|int
name|preferedSignatureSize
decl_stmt|;
specifier|private
name|int
name|pageNo
decl_stmt|;
comment|/**      * Creates the default signature options.      */
specifier|public
name|SignatureOptions
parameter_list|()
block|{
name|pageNo
operator|=
literal|0
expr_stmt|;
block|}
comment|/**      * Set the 0-based page number.      *       * @param pageNo the page number      */
specifier|public
name|void
name|setPage
parameter_list|(
name|int
name|pageNo
parameter_list|)
block|{
name|this
operator|.
name|pageNo
operator|=
name|pageNo
expr_stmt|;
block|}
comment|/**      * Get the 0-based page number.      *       * @return the page number      */
specifier|public
name|int
name|getPage
parameter_list|()
block|{
return|return
name|pageNo
return|;
block|}
comment|/**      * Reads the visual signature from the given file.      *        * @param file the file containing the visual signature      * @throws IOException when something went wrong during parsing       */
specifier|public
name|void
name|setVisualSignature
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFParser
name|parser
init|=
operator|new
name|PDFParser
argument_list|(
operator|new
name|RandomAccessBufferedFileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|visualSignature
operator|=
name|parser
operator|.
name|getDocument
argument_list|()
expr_stmt|;
block|}
comment|/**      * Reads the visual signature from the given input stream.      *        * @param is the input stream containing the visual signature      * @throws IOException when something went wrong during parsing       */
specifier|public
name|void
name|setVisualSignature
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFParser
name|parser
init|=
operator|new
name|PDFParser
argument_list|(
operator|new
name|RandomAccessBufferedFileInputStream
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|visualSignature
operator|=
name|parser
operator|.
name|getDocument
argument_list|()
expr_stmt|;
block|}
comment|/**      * Reads the visual signature from the given visual signature properties      *        * @param visSignatureProperties the<code>PDVisibleSigProperties</code> object containing the visual signature      *       * @throws IOException when something went wrong during parsing      */
specifier|public
name|void
name|setVisualSignature
parameter_list|(
name|PDVisibleSigProperties
name|visSignatureProperties
parameter_list|)
throws|throws
name|IOException
block|{
name|setVisualSignature
argument_list|(
name|visSignatureProperties
operator|.
name|getVisibleSignature
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the visual signature.      *       * @return the visual signature      */
specifier|public
name|COSDocument
name|getVisualSignature
parameter_list|()
block|{
return|return
name|visualSignature
return|;
block|}
comment|/**      * Get the preferred size of the signature.      *       * @return the preferred size      */
specifier|public
name|int
name|getPreferedSignatureSize
parameter_list|()
block|{
return|return
name|preferedSignatureSize
return|;
block|}
comment|/**      * Set the preferred size of the signature.      *       * @param size the size of the signature      */
specifier|public
name|void
name|setPreferedSignatureSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
name|preferedSignatureSize
operator|=
name|size
expr_stmt|;
block|}
block|}
comment|/**      * Closes the visual signature COSDocument, if any.      *      * @throws IOException if the document could not be closed      */
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|visualSignature
operator|!=
literal|null
condition|)
block|{
name|visualSignature
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

