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
comment|/**  * A signature build dictionary as specified in the PDF Signature Build Dictionary Specification.  *  * The signature build properties dictionary provides signature properties for the software  * application that was used to create the signature.  *  * @see<a href="http://partners.adobe.com/public/developer/en/acrobat/Acrobat_Signature_BuiladDict.pdf">  * http://partners.adobe.com/public/developer/en/acrobat/Acrobat_Signature_BuiladDict.pdf</a>  *  * @author Thomas Chojecki  */
end_comment

begin_class
specifier|public
class|class
name|PDPropBuild
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDPropBuild
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
comment|// the specification claim to use direct objects
block|}
comment|/**      * Constructor.      *      * @param dict The signature dictionary.      */
specifier|public
name|PDPropBuild
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
comment|// the specification claim to use direct objects
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
comment|/**      * A build data dictionary for the signature handler that was      * used to create the parent signature.      *      * @return the Filter as PDPropBuildFilter object      */
specifier|public
name|PDPropBuildDataDict
name|getFilter
parameter_list|()
block|{
name|PDPropBuildDataDict
name|filter
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|filterDic
init|=
operator|(
name|COSDictionary
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|)
decl_stmt|;
if|if
condition|(
name|filterDic
operator|!=
literal|null
condition|)
block|{
name|filter
operator|=
operator|new
name|PDPropBuildDataDict
argument_list|(
name|filterDic
argument_list|)
expr_stmt|;
block|}
return|return
name|filter
return|;
block|}
comment|/**      * Set the build data dictionary for the signature handler.      * This entry is optional but is highly recommended for the signatures.      *      * @param filter is the PDPropBuildFilter      */
specifier|public
name|void
name|setPDPropBuildFilter
parameter_list|(
name|PDPropBuildDataDict
name|filter
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
comment|/**      * A build data dictionary for the PubSec software module      * that was used to create the parent signature.      *      * @return the PubSec as PDPropBuildPubSec object      */
specifier|public
name|PDPropBuildDataDict
name|getPubSec
parameter_list|()
block|{
name|PDPropBuildDataDict
name|pubSec
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|pubSecDic
init|=
operator|(
name|COSDictionary
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PUB_SEC
argument_list|)
decl_stmt|;
if|if
condition|(
name|pubSecDic
operator|!=
literal|null
condition|)
block|{
name|pubSec
operator|=
operator|new
name|PDPropBuildDataDict
argument_list|(
name|pubSecDic
argument_list|)
expr_stmt|;
block|}
return|return
name|pubSec
return|;
block|}
comment|/**      * Set the build data dictionary for the PubSec Software module.      *      * @param pubSec is the PDPropBuildPubSec      */
specifier|public
name|void
name|setPDPropBuildPubSec
parameter_list|(
name|PDPropBuildDataDict
name|pubSec
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PUB_SEC
argument_list|,
name|pubSec
argument_list|)
expr_stmt|;
block|}
comment|/**      * A build data dictionary for the viewing application software      * module that was used to create the parent signature.      *      * @return the App as PDPropBuildApp object      */
specifier|public
name|PDPropBuildDataDict
name|getApp
parameter_list|()
block|{
name|PDPropBuildDataDict
name|app
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|appDic
init|=
operator|(
name|COSDictionary
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|APP
argument_list|)
decl_stmt|;
if|if
condition|(
name|appDic
operator|!=
literal|null
condition|)
block|{
name|app
operator|=
operator|new
name|PDPropBuildDataDict
argument_list|(
name|appDic
argument_list|)
expr_stmt|;
block|}
return|return
name|app
return|;
block|}
comment|/**      * Set the build data dictionary for the viewing application      * software module.      *      * @param app is the PDPropBuildApp      */
specifier|public
name|void
name|setPDPropBuildApp
parameter_list|(
name|PDPropBuildDataDict
name|app
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|APP
argument_list|,
name|app
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

