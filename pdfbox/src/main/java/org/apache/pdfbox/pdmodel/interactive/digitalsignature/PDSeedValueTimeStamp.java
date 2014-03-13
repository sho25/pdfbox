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

begin_comment
comment|/**  * If exist, it describe where the signature handler can request a RFC3161  * timestamp and if it is a must have for the signature.  *  * @author Thomas Chojecki  */
end_comment

begin_class
specifier|public
class|class
name|PDSeedValueTimeStamp
block|{
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDSeedValueTimeStamp
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|dictionary
operator|.
name|setDirect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dict The signature dictionary.      */
specifier|public
name|PDSeedValueTimeStamp
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|dictionary
operator|=
name|dict
expr_stmt|;
name|dictionary
operator|.
name|setDirect
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
return|;
block|}
comment|/**      * Convert this standard java object to a COS dictionary.      *      * @return The COS dictionary that matches this Java object.      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * Returns the URL.      *       * @return the URL      */
specifier|public
name|String
name|getURL
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|URL
argument_list|)
return|;
block|}
comment|/**      * Sets the URL.      * @param url the URL to be set as URL      */
specifier|public
name|void
name|setURL
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|dictionary
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|URL
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
comment|/**      * Indicates if a timestamp is required.      *       * @return true if a timestamp is required      */
specifier|public
name|boolean
name|isTimestampRequired
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|FT
argument_list|,
literal|0
argument_list|)
operator|!=
literal|0
return|;
block|}
comment|/**      * Sets if a timestamp is reuqired or not.      *       * @param flag true if a timestamp is required      */
specifier|public
name|void
name|setTimestampRequired
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|dictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|FT
argument_list|,
name|flag
condition|?
literal|1
else|:
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

