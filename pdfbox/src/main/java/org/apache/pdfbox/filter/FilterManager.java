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
name|apache
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
argument_list|<
name|COSName
argument_list|,
name|Filter
argument_list|>
name|filters
init|=
operator|new
name|HashMap
argument_list|<
name|COSName
argument_list|,
name|Filter
argument_list|>
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
name|Filter
name|cryptFilter
init|=
operator|new
name|CryptFilter
argument_list|()
decl_stmt|;
name|Filter
name|jpxFilter
init|=
operator|new
name|JPXFilter
argument_list|()
decl_stmt|;
name|Filter
name|jbig2Filter
init|=
operator|new
name|JBIG2Filter
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
name|addFilter
argument_list|(
name|COSName
operator|.
name|CRYPT
argument_list|,
name|cryptFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|JPX_DECODE
argument_list|,
name|jpxFilter
argument_list|)
expr_stmt|;
name|addFilter
argument_list|(
name|COSName
operator|.
name|JBIG2_DECODE
argument_list|,
name|jbig2Filter
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get all of the filters that are available in the system.      *      * @return All available filters in the system.      */
specifier|public
name|Collection
argument_list|<
name|Filter
argument_list|>
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

