begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2005, www.fontbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of fontbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.fontbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|fontbox
operator|.
name|ttf
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

begin_comment
comment|/**  * A table in a true type font.  *   * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|TTFTable
block|{
specifier|private
name|String
name|tag
decl_stmt|;
specifier|private
name|long
name|checkSum
decl_stmt|;
specifier|private
name|long
name|offset
decl_stmt|;
specifier|private
name|long
name|length
decl_stmt|;
comment|/**      * @return Returns the checkSum.      */
specifier|public
name|long
name|getCheckSum
parameter_list|()
block|{
return|return
name|checkSum
return|;
block|}
comment|/**      * @param checkSumValue The checkSum to set.      */
specifier|public
name|void
name|setCheckSum
parameter_list|(
name|long
name|checkSumValue
parameter_list|)
block|{
name|this
operator|.
name|checkSum
operator|=
name|checkSumValue
expr_stmt|;
block|}
comment|/**      * @return Returns the length.      */
specifier|public
name|long
name|getLength
parameter_list|()
block|{
return|return
name|length
return|;
block|}
comment|/**      * @param lengthValue The length to set.      */
specifier|public
name|void
name|setLength
parameter_list|(
name|long
name|lengthValue
parameter_list|)
block|{
name|this
operator|.
name|length
operator|=
name|lengthValue
expr_stmt|;
block|}
comment|/**      * @return Returns the offset.      */
specifier|public
name|long
name|getOffset
parameter_list|()
block|{
return|return
name|offset
return|;
block|}
comment|/**      * @param offsetValue The offset to set.      */
specifier|public
name|void
name|setOffset
parameter_list|(
name|long
name|offsetValue
parameter_list|)
block|{
name|this
operator|.
name|offset
operator|=
name|offsetValue
expr_stmt|;
block|}
comment|/**      * @return Returns the tag.      */
specifier|public
name|String
name|getTag
parameter_list|()
block|{
return|return
name|tag
return|;
block|}
comment|/**      * @param tagValue The tag to set.      */
specifier|public
name|void
name|setTag
parameter_list|(
name|String
name|tagValue
parameter_list|)
block|{
name|this
operator|.
name|tag
operator|=
name|tagValue
expr_stmt|;
block|}
comment|/**      * This will read the required data from the stream.      *       * @param ttf The font that is being read.      * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|void
name|initData
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|,
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{     }
block|}
end_class

end_unit

