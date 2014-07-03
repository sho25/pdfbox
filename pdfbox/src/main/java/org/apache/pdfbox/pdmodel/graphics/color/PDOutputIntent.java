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
name|graphics
operator|.
name|color
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
name|COSDictionary
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
name|PDStream
import|;
end_import

begin_comment
comment|/**  * An Output Intent describes the colour reproduction characteristics of a possible output  * device or production condition.  * Output intents provide a means for matching the colour characteristics of a PDF document with  * those of a target output device or production environment in which the document will be printed.  *  * @author Guillaume Bailleul  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDOutputIntent
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
specifier|public
name|PDOutputIntent
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|colorProfile
parameter_list|)
throws|throws
name|IOException
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|OUTPUT_INTENT
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|S
argument_list|,
name|COSName
operator|.
name|GTS_PDFA1
argument_list|)
expr_stmt|;
name|PDStream
name|destOutputIntent
init|=
name|configureOutputProfile
argument_list|(
name|doc
argument_list|,
name|colorProfile
argument_list|)
decl_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DEST_OUTPUT_PROFILE
argument_list|,
name|destOutputIntent
argument_list|)
expr_stmt|;
block|}
specifier|public
name|PDOutputIntent
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|dictionary
operator|=
name|dictionary
expr_stmt|;
block|}
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
specifier|public
name|COSStream
name|getDestOutputIntent
parameter_list|()
block|{
return|return
operator|(
name|COSStream
operator|)
name|dictionary
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|DEST_OUTPUT_PROFILE
argument_list|)
return|;
block|}
specifier|public
name|String
name|getInfo
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|INFO
argument_list|)
return|;
block|}
specifier|public
name|void
name|setInfo
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|INFO
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getOutputCondition
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|OUTPUT_CONDITION
argument_list|)
return|;
block|}
specifier|public
name|void
name|setOutputCondition
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|OUTPUT_CONDITION
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getOutputConditionIdentifier
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|OUTPUT_CONDITION_IDENTIFIER
argument_list|)
return|;
block|}
specifier|public
name|void
name|setOutputConditionIdentifier
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|OUTPUT_CONDITION_IDENTIFIER
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getRegistryName
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|REGISTRY_NAME
argument_list|)
return|;
block|}
specifier|public
name|void
name|setRegistryName
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|REGISTRY_NAME
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|private
name|PDStream
name|configureOutputProfile
parameter_list|(
name|PDDocument
name|doc
parameter_list|,
name|InputStream
name|colorProfile
parameter_list|)
throws|throws
name|IOException
block|{
name|PDStream
name|stream
init|=
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|,
name|colorProfile
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|stream
operator|.
name|getStream
argument_list|()
operator|.
name|setFilters
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
argument_list|)
expr_stmt|;
name|stream
operator|.
name|getStream
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
name|stream
operator|.
name|getByteArray
argument_list|()
operator|.
name|length
argument_list|)
expr_stmt|;
name|stream
operator|.
name|getStream
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|N
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|stream
operator|.
name|addCompression
argument_list|()
expr_stmt|;
return|return
name|stream
return|;
block|}
block|}
end_class

end_unit

