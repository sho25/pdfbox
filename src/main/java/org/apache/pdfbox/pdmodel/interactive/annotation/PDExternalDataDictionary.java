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
name|annotation
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
comment|/**  * This class represents an external data dictionary.  *   * @version $Revision: 1.0 $  *   */
end_comment

begin_class
specifier|public
class|class
name|PDExternalDataDictionary
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|dataDictionary
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDExternalDataDictionary
parameter_list|()
block|{
name|this
operator|.
name|dataDictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|this
operator|.
name|dataDictionary
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
literal|"ExData"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *       *  @param dictionary Dictionary      */
specifier|public
name|PDExternalDataDictionary
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|dataDictionary
operator|=
name|dictionary
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|this
operator|.
name|dataDictionary
return|;
block|}
comment|/**      * returns the dictionary.      *      * @return the dictionary      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|this
operator|.
name|dataDictionary
return|;
block|}
comment|/**      * returns the type of the external data dictionary.      * It must be "ExData", if present      * @return the type of the external data dictionary      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
literal|"ExData"
argument_list|)
return|;
block|}
comment|/**      * returns the subtype of the external data dictionary.      * @return the subtype of the external data dictionary      */
specifier|public
name|String
name|getSubtype
parameter_list|()
block|{
return|return
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
return|;
block|}
comment|/**      * This will set the subtype of the external data dictionary.      * @param subtype the subtype of the external data dictionary      */
specifier|public
name|void
name|setSubtype
parameter_list|(
name|String
name|subtype
parameter_list|)
block|{
name|this
operator|.
name|getDictionary
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|subtype
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

