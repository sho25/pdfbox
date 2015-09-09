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
name|font
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|contentstream
operator|.
name|PDContentStream
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
name|PDResources
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
name|COSObjectable
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
name|PDRectangle
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
name|PDStream
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * A Type 3 character procedure. This is a standalone PDF content stream.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDType3CharProc
implements|implements
name|COSObjectable
implements|,
name|PDContentStream
block|{
specifier|private
specifier|final
name|PDType3Font
name|font
decl_stmt|;
specifier|private
specifier|final
name|COSStream
name|charStream
decl_stmt|;
specifier|public
name|PDType3CharProc
parameter_list|(
name|PDType3Font
name|font
parameter_list|,
name|COSStream
name|charStream
parameter_list|)
block|{
name|this
operator|.
name|font
operator|=
name|font
expr_stmt|;
name|this
operator|.
name|charStream
operator|=
name|charStream
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|COSStream
name|getCOSObject
parameter_list|()
block|{
return|return
name|charStream
return|;
block|}
specifier|public
name|PDType3Font
name|getFont
parameter_list|()
block|{
return|return
name|font
return|;
block|}
specifier|public
name|PDStream
name|getContentStream
parameter_list|()
block|{
return|return
operator|new
name|PDStream
argument_list|(
name|charStream
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getContents
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|charStream
operator|.
name|createInputStream
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDResources
name|getResources
parameter_list|()
block|{
return|return
name|font
operator|.
name|getResources
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDRectangle
name|getBBox
parameter_list|()
block|{
return|return
name|font
operator|.
name|getFontBBox
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Matrix
name|getMatrix
parameter_list|()
block|{
return|return
name|font
operator|.
name|getFontMatrix
argument_list|()
return|;
block|}
comment|// todo: add methods for getting the character's width from the stream
block|}
end_class

end_unit

