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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

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
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSBase
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
name|filespecification
operator|.
name|PDFileSpecification
import|;
end_import

begin_comment
comment|/**  * A PDStream represents a stream in a PDF document.  Streams are tied to a single  * PDF document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDMemoryStream
extends|extends
name|PDStream
block|{
specifier|private
specifier|final
name|byte
index|[]
name|data
decl_stmt|;
comment|/**      * This will create a new PDMemoryStream object.      *      * @param buffer The data for this in memory stream.      */
specifier|public
name|PDMemoryStream
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|)
block|{
name|data
operator|=
name|buffer
expr_stmt|;
block|}
comment|/**      * If there are not compression filters on the current stream then this      * will add a compression filter, flate compression for example.      */
annotation|@
name|Override
specifier|public
name|void
name|addCompression
parameter_list|()
block|{
comment|//no compression to add
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not supported for memory stream"
argument_list|)
throw|;
block|}
comment|/**      * This will get a stream that can be written to.      *      * @return An output stream to write data to.      *      * @throws IOException If an IO error occurs during writing.      */
annotation|@
name|Override
specifier|public
name|OutputStream
name|createOutputStream
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not supported for memory stream"
argument_list|)
throw|;
block|}
comment|/**      * This will get a stream that can be read from.      *      * @return An input stream that can be read from.      *      * @throws IOException If an IO error occurs during reading.      */
annotation|@
name|Override
specifier|public
name|InputStream
name|createInputStream
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
return|;
block|}
comment|/**      * This will get a stream with some filters applied but not others.  This is useful      * when doing images, ie filters = [flate,dct], we want to remove flate but leave dct      *      * @param stopFilters A list of filters to stop decoding at.      * @return A stream with decoded data.      * @throws IOException If there is an error processing the stream.      */
annotation|@
name|Override
specifier|public
name|InputStream
name|getPartiallyFilteredStream
parameter_list|(
name|List
name|stopFilters
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|createInputStream
argument_list|()
return|;
block|}
comment|/**      * Get the cos stream associated with this object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSStream
name|getStream
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not supported for memory stream"
argument_list|)
throw|;
block|}
comment|/**      * This will get the length of the filtered/compressed stream.  This is readonly in the      * PD Model and will be managed by this class.      *      * @return The length of the filtered stream.      */
annotation|@
name|Override
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|data
operator|.
name|length
return|;
block|}
comment|/**      * This will get the list of filters that are associated with this stream.  Or      * null if there are none.      * @return A list of all encoding filters to apply to this stream.      */
annotation|@
name|Override
specifier|public
name|List
name|getFilters
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * This will set the filters that are part of this stream.      *      * @param filters The filters that are part of this stream.      */
annotation|@
name|Override
specifier|public
name|void
name|setFilters
parameter_list|(
name|List
name|filters
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"not supported for memory stream"
argument_list|)
throw|;
block|}
comment|/**      * Get the list of decode parameters.  Each entry in the list will refer to      * an entry in the filters list.      *      * @return The list of decode parameters.      *      * @throws IOException if there is an error retrieving the parameters.      */
specifier|public
name|List
name|getDecodeParams
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
comment|/**      * This will set the list of decode params.      *      * @param decodeParams The list of decode params.      */
specifier|public
name|void
name|setDecodeParams
parameter_list|(
name|List
name|decodeParams
parameter_list|)
block|{
comment|//do nothing
block|}
comment|/**      * This will get the file specification for this stream.  This is only      * required for external files.      *      * @return The file specification.      */
annotation|@
name|Override
specifier|public
name|PDFileSpecification
name|getFile
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Set the file specification.      * @param f The file specification.      */
annotation|@
name|Override
specifier|public
name|void
name|setFile
parameter_list|(
name|PDFileSpecification
name|f
parameter_list|)
block|{
comment|//do nothing.
block|}
comment|/**      * This will get the list of filters that are associated with this stream.  Or      * null if there are none.      * @return A list of all encoding filters to apply to this stream.      */
annotation|@
name|Override
specifier|public
name|List
name|getFileFilters
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * This will set the filters that are part of this stream.      *      * @param filters The filters that are part of this stream.      */
annotation|@
name|Override
specifier|public
name|void
name|setFileFilters
parameter_list|(
name|List
name|filters
parameter_list|)
block|{
comment|//do nothing.
block|}
comment|/**      * Get the list of decode parameters.  Each entry in the list will refer to      * an entry in the filters list.      *      * @return The list of decode parameters.      *      * @throws IOException if there is an error retrieving the parameters.      */
annotation|@
name|Override
specifier|public
name|List
name|getFileDecodeParams
parameter_list|()
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
comment|/**      * This will set the list of decode params.      *      * @param decodeParams The list of decode params.      */
annotation|@
name|Override
specifier|public
name|void
name|setFileDecodeParams
parameter_list|(
name|List
name|decodeParams
parameter_list|)
block|{
comment|//do nothing
block|}
comment|/**      * This will copy the stream into a byte array.      *      * @return The byte array of the filteredStream      * @throws IOException When getFilteredStream did not work      */
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|getByteArray
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|data
return|;
block|}
comment|/**      * Get the metadata that is part of the document catalog.  This will      * return null if there is no meta data for this object.      *      * @return The metadata for this object.      */
annotation|@
name|Override
specifier|public
name|PDMetadata
name|getMetadata
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Set the metadata for this object.  This can be null.      *      * @param meta The meta data for this object.      */
annotation|@
name|Override
specifier|public
name|void
name|setMetadata
parameter_list|(
name|PDMetadata
name|meta
parameter_list|)
block|{
comment|//do nothing
block|}
block|}
end_class

end_unit

