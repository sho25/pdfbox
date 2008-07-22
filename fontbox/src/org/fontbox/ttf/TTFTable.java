begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

