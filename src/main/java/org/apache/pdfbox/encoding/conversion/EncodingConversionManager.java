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
name|encoding
operator|.
name|conversion
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_comment
comment|/**  *  EncodingConversionManager maintains relationship between PDF encoding name  *  and respective EncodingConverter instance. Those PDF encoding name like  *  GBK-EUC-H should be converted to java charset name before constructing a  *  java string instance  *    *  @author  Pin Xue (http://www.pinxue.net), Holly Lee (holly.lee (at) gmail.com)  *  @version $Revision: 1.0 $  */
end_comment

begin_class
specifier|public
class|class
name|EncodingConversionManager
block|{
comment|/**         *  Mapping from PDF encoding name to EncodingConverter instance.         */
specifier|private
specifier|static
name|HashMap
name|encodingMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|EncodingConversionManager
parameter_list|()
block|{        }
comment|/**         *  Initialize the encodingMap before anything calls us.         */
static|static
block|{
comment|// Add CJK encodings to map
name|Iterator
name|it
init|=
name|CJKEncodings
operator|.
name|getEncodingIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|encodingName
init|=
call|(
name|String
call|)
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|encodingMap
operator|.
name|put
argument_list|(
name|encodingName
argument_list|,
operator|new
name|CJKConverter
argument_list|(
name|encodingName
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// If there is any other encoding conversions, please add it here.
block|}
comment|/**         *  Get converter from given encoding name. If no converted defined,         *  a null is returned.         *           *  @param encoding search for a converter for the given encoding name         *  @return the converter for the given encoding name         */
specifier|public
specifier|static
specifier|final
name|EncodingConverter
name|getConverter
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
return|return
call|(
name|EncodingConverter
call|)
argument_list|(
name|encodingMap
operator|.
name|get
argument_list|(
name|encoding
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

