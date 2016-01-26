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
name|PDDocument
import|;
end_import

begin_comment
comment|/**  * A PDStream represents a stream in a PDF document.  Streams are tied to a single  * PDF document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDObjectStream
extends|extends
name|PDStream
block|{
comment|/**      * Constructor.      *      * @param str The stream parameter.      */
specifier|public
name|PDObjectStream
parameter_list|(
name|COSStream
name|str
parameter_list|)
block|{
name|super
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will create a new PDObjectStream object.      *      * @param document The document that the stream will be part of.      * @return A new stream object.      */
specifier|public
specifier|static
name|PDObjectStream
name|createStream
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
name|COSStream
name|cosStream
init|=
name|document
operator|.
name|getDocument
argument_list|()
operator|.
name|createCOSStream
argument_list|()
decl_stmt|;
name|PDObjectStream
name|strm
init|=
operator|new
name|PDObjectStream
argument_list|(
name|cosStream
argument_list|)
decl_stmt|;
name|strm
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|OBJ_STM
argument_list|)
expr_stmt|;
return|return
name|strm
return|;
block|}
comment|/**      * Get the type of this object, should always return "ObjStm".      *      * @return The type of this object.      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
return|;
block|}
comment|/**      * Get the number of compressed object.      *      * @return The number of compressed objects.      */
specifier|public
name|int
name|getNumberOfObjects
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|N
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * Set the number of objects.      *      * @param n The new number of objects.      */
specifier|public
name|void
name|setNumberOfObjects
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|N
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
comment|/**      * The byte offset (in the decoded stream) of the first compressed object.      *      * @return The byte offset to the first object.      */
specifier|public
name|int
name|getFirstByteOffset
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|FIRST
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * The byte offset (in the decoded stream) of the first compressed object.      *      * @param n The byte offset to the first object.      */
specifier|public
name|void
name|setFirstByteOffset
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|FIRST
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
comment|/**      * A reference to an object stream, of which the current object stream is      * considered an extension.      *      * @return The object that this stream is an extension.      */
specifier|public
name|PDObjectStream
name|getExtends
parameter_list|()
block|{
name|PDObjectStream
name|retval
init|=
literal|null
decl_stmt|;
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|EXTENDS
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDObjectStream
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * A reference to an object stream, of which the current object stream is      * considered an extension.      *      * @param stream The object stream extension.      */
specifier|public
name|void
name|setExtends
parameter_list|(
name|PDObjectStream
name|stream
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|EXTENDS
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

