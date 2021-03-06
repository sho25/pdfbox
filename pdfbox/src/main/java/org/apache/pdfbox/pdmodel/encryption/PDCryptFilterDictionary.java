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
name|encryption
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
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This class is a specialized view of the crypt filter dictionary of a PDF document.  * It contains a low level dictionary (COSDictionary) and provides the methods to  * manage its fields.  *  */
end_comment

begin_class
specifier|public
class|class
name|PDCryptFilterDictionary
implements|implements
name|COSObjectable
block|{
comment|/**      * COS crypt filter dictionary.      */
specifier|protected
name|COSDictionary
name|cryptFilterDictionary
init|=
literal|null
decl_stmt|;
comment|/**      * creates a new empty crypt filter dictionary.      */
specifier|public
name|PDCryptFilterDictionary
parameter_list|()
block|{
name|cryptFilterDictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * creates a new crypt filter dictionary from the low level dictionary provided.      * @param d the low level dictionary that will be managed by the newly created object      */
specifier|public
name|PDCryptFilterDictionary
parameter_list|(
name|COSDictionary
name|d
parameter_list|)
block|{
name|cryptFilterDictionary
operator|=
name|d
expr_stmt|;
block|}
comment|/**      * This will get the dictionary associated with this crypt filter dictionary.      *      * @return The COS dictionary that this object wraps.      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|cryptFilterDictionary
return|;
block|}
comment|/**      * This will set the number of bits to use for the crypt filter algorithm.      *      * @param length The new key length.      */
specifier|public
name|void
name|setLength
parameter_list|(
name|int
name|length
parameter_list|)
block|{
name|cryptFilterDictionary
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the Length entry of the crypt filter dictionary.<br><br>      * The length in<b>bits</b> for the crypt filter algorithm. This will return a multiple of 8.      *      * @return The length in bits for the encryption algorithm      */
specifier|public
name|int
name|getLength
parameter_list|()
block|{
return|return
name|cryptFilterDictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|LENGTH
argument_list|,
literal|40
argument_list|)
return|;
block|}
comment|/**      * This will set the crypt filter method.       * Allowed values are: NONE, V2, AESV2, AESV3      *      * @param cfm name of the crypt filter method.      *      */
specifier|public
name|void
name|setCryptFilterMethod
parameter_list|(
name|COSName
name|cfm
parameter_list|)
block|{
name|cryptFilterDictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CFM
argument_list|,
name|cfm
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return the crypt filter method.       * Allowed values are: NONE, V2, AESV2, AESV3      *      * @return the name of the crypt filter method.      */
specifier|public
name|COSName
name|getCryptFilterMethod
parameter_list|()
block|{
return|return
operator|(
name|COSName
operator|)
name|cryptFilterDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CFM
argument_list|)
return|;
block|}
block|}
end_class

end_unit

