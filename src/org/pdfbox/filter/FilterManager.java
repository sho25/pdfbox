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
name|util
operator|.
name|Collection
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
comment|/**  * This will contain manage all the different types of filters that are available.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.13 $  */
end_comment

begin_class
specifier|public
class|class
name|FilterManager
block|{
specifier|private
name|Map
name|filters
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|FilterManager
parameter_list|()
block|{
name|Filter
name|flateFilter
init|=
operator|new
name|FlateFilter
argument_list|()
decl_stmt|;
name|Filter
name|dctFilter
init|=
operator|new
name|DCTFilter
argument_list|()
decl_stmt|;
name|Filter
name|ccittFaxFilter
init|=
operator|new
name|CCITTFaxDecodeFilter
argument_list|()
decl_stmt|;
name|Filter
name|lzwFilter
init|=
operator|new
name|LZWFilter
argument_list|()
decl_stmt|;
name|Filter
name|asciiHexFilter
init|=
operator|new
name|ASCIIHexFilter
argument_list|()
decl_stmt|;
name|Filter
name|ascii85Filter
init|=
operator|new
name|ASCII85Filter
argument_list|()
decl_stmt|;
name|Filter
name|runLengthFilter
init|=
operator|new
name|RunLengthDecodeFilter
argument_list|()
decl_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
argument_list|,
name|flateFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE_ABBREVIATION
argument_list|,
name|flateFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|DCT_DECODE
argument_list|,
name|dctFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|DCT_DECODE_ABBREVIATION
argument_list|,
name|dctFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|CCITTFAX_DECODE
argument_list|,
name|ccittFaxFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|CCITTFAX_DECODE_ABBREVIATION
argument_list|,
name|ccittFaxFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|LZW_DECODE
argument_list|,
name|lzwFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|LZW_DECODE_ABBREVIATION
argument_list|,
name|lzwFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|ASCII_HEX_DECODE
argument_list|,
name|asciiHexFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|ASCII_HEX_DECODE_ABBREVIATION
argument_list|,
name|asciiHexFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|ASCII85_DECODE
argument_list|,
name|ascii85Filter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|ASCII85_DECODE_ABBREVIATION
argument_list|,
name|ascii85Filter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|RUN_LENGTH_DECODE
argument_list|,
name|runLengthFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|RUN_LENGTH_DECODE_ABBREVIATION
argument_list|,
name|runLengthFilter
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get all of the filters that are available in the system.      *      * @return All available filters in the system.      */
specifier|public
name|Collection
name|getFilters
parameter_list|()
block|{
return|return
name|filters
operator|.
name|values
argument_list|()
return|;
block|}
comment|/**      * This will add an available filter.      *      * @param filterName The name of the filter.      * @param filter The filter to use.      */
specifier|public
name|void
name|addFilter
parameter_list|(
name|COSName
name|filterName
parameter_list|,
name|Filter
name|filter
parameter_list|)
block|{
name|filters
operator|.
name|put
argument_list|(
name|filterName
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a filter by name.      *      * @param filterName The name of the filter to retrieve.      *      * @return The filter that matches the name.      *      * @throws IOException If the filter could not be found.      */
specifier|public
name|Filter
name|getFilter
parameter_list|(
name|COSName
name|filterName
parameter_list|)
throws|throws
name|IOException
block|{
name|Filter
name|filter
init|=
operator|(
name|Filter
operator|)
name|filters
operator|.
name|get
argument_list|(
name|filterName
argument_list|)
decl_stmt|;
if|if
condition|(
name|filter
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown stream filter:"
operator|+
name|filterName
argument_list|)
throw|;
block|}
return|return
name|filter
return|;
block|}
comment|/**      * This will get a filter by name.      *      * @param filterName The name of the filter to retrieve.      *      * @return The filter that matches the name.      *      * @throws IOException If the filter could not be found.      */
specifier|public
name|Filter
name|getFilter
parameter_list|(
name|String
name|filterName
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getFilter
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|filterName
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

