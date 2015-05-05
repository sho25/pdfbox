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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|COSArray
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
name|pdmodel
operator|.
name|common
operator|.
name|COSArrayList
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
comment|/**  * This represents a pdf signature seed value dictionary.  *  * @author Thomas Chojecki  */
end_comment

begin_class
specifier|public
class|class
name|PDSeedValue
implements|implements
name|COSObjectable
block|{
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_FILTER
init|=
literal|1
decl_stmt|;
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_SUBFILTER
init|=
literal|1
operator|<<
literal|1
decl_stmt|;
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_V
init|=
literal|1
operator|<<
literal|2
decl_stmt|;
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_REASON
init|=
literal|1
operator|<<
literal|3
decl_stmt|;
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_LEGAL_ATTESTATION
init|=
literal|1
operator|<<
literal|4
decl_stmt|;
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_ADD_REV_INFO
init|=
literal|1
operator|<<
literal|5
decl_stmt|;
comment|/**      * A Ff flag.      */
specifier|public
specifier|static
specifier|final
name|int
name|FLAG_DIGEST_METHOD
init|=
literal|1
operator|<<
literal|6
decl_stmt|;
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDSeedValue
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
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|SV
argument_list|)
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
name|PDSeedValue
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
comment|/**      * Convert this standard java object to a COS dictionary.      *      * @return The COS dictionary that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      *      * @return true if the Filter is required      */
specifier|public
name|boolean
name|isFilterRequired
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_FILTER
argument_list|)
return|;
block|}
comment|/**      * set true if the filter shall be required.      *       * @param flag if true, the specified Filter shall be used when signing.      */
specifier|public
name|void
name|setFilterRequired
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_FILTER
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
comment|/**      *      * @return true if the SubFilter is required      */
specifier|public
name|boolean
name|isSubFilterRequired
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_SUBFILTER
argument_list|)
return|;
block|}
comment|/**      * set true if the subfilter shall be required.      *       * @param flag if true, the first supported SubFilter in the array shall be used when signing.      */
specifier|public
name|void
name|setSubFilterRequired
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_SUBFILTER
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
comment|/**     *     * @return true if the DigestMethod is required     */
specifier|public
name|boolean
name|isDigestMethodRequired
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_DIGEST_METHOD
argument_list|)
return|;
block|}
comment|/**      * set true if the DigestMethod shall be required.      *       * @param flag if true, one digest from the array shall be used.      */
specifier|public
name|void
name|setDigestMethodRequired
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_DIGEST_METHOD
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
comment|/**     *     * @return true if the V entry is required     */
specifier|public
name|boolean
name|isVRequired
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_V
argument_list|)
return|;
block|}
comment|/**      * set true if the V entry shall be required.      *       * @param flag if true, the V entry shall be used.      */
specifier|public
name|void
name|setVRequired
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_V
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
comment|/**     *     * @return true if the Reason is required     */
specifier|public
name|boolean
name|isReasonRequired
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_REASON
argument_list|)
return|;
block|}
comment|/**      * set true if the Reason shall be required.      *       * @param flag if true, the Reason entry shall be used.      */
specifier|public
name|void
name|setReasonRequired
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_REASON
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
comment|/**     *     * @return true if the LegalAttestation is required     */
specifier|public
name|boolean
name|isLegalAttestationRequired
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_LEGAL_ATTESTATION
argument_list|)
return|;
block|}
comment|/**      * set true if the LegalAttestation shall be required.      *       * @param flag if true, the LegalAttestation entry shall be used.      */
specifier|public
name|void
name|setLegalAttestationRequired
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_LEGAL_ATTESTATION
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
comment|/**     *     * @return true if the AddRevInfo is required     */
specifier|public
name|boolean
name|isAddRevInfoRequired
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_ADD_REV_INFO
argument_list|)
return|;
block|}
comment|/**      * set true if the AddRevInfo shall be required.      *       * @param flag if true, the AddRevInfo shall be used.      */
specifier|public
name|void
name|setAddRevInfoRequired
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|FLAG_ADD_REV_INFO
argument_list|,
name|flag
argument_list|)
expr_stmt|;
block|}
comment|/**      * If<b>Filter</b> is not null and the {@link #isFilterRequired()} indicates this entry is a      * required constraint, then the signature handler specified by this entry shall be used when      * signing; otherwise, signing shall not take place. If {@link #isFilterRequired()} indicates      * that this is an optional constraint, this handler may be used if it is available. If it is      * not available, a different handler may be used instead.      *      * @return the filter that shall be used by the signature handler      */
specifier|public
name|String
name|getFilter
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|FILTER
argument_list|)
return|;
block|}
comment|/**      * (Optional) The signature handler that shall be used to sign the signature field.      *      * @param filter is the filter that shall be used by the signature handler      */
specifier|public
name|void
name|setFilter
parameter_list|(
name|COSName
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
comment|/**      * If<b>SubFilter</b> is not null and the {@link #isSubFilterRequired()} indicates this      * entry is a required constraint, then the first matching encodings shall be used when      * signing; otherwise, signing shall not take place. If {@link #isSubFilterRequired()}      * indicates that this is an optional constraint, then the first matching encoding shall      * be used if it is available. If it is not available, a different encoding may be used      * instead.      *      * @return the subfilter that shall be used by the signature handler      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSubFilter
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|fields
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SUB_FILTER
argument_list|)
decl_stmt|;
if|if
condition|(
name|fields
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|actuals
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fields
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|element
init|=
name|fields
operator|.
name|getName
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|actuals
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|actuals
argument_list|,
name|fields
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * (Optional) An array of names indicating encodings to use when signing. The first name      * in the array that matches an encoding supported by the signature handler shall be the      * encoding that is actually used for signing.      *      * @param subfilter is the name that shall be used for encoding      */
specifier|public
name|void
name|setSubFilter
parameter_list|(
name|List
argument_list|<
name|COSName
argument_list|>
name|subfilter
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUB_FILTER
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|subfilter
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * An array of names indicating acceptable digest algorithms to use when      * signing. The value shall be one of<b>SHA1</b>,<b>SHA256</b>,<b>SHA384</b>,      *<b>SHA512</b>,<b>RIPEMD160</b>. The default value is implementation-specific.      *      * @return the digest method that shall be used by the signature handler      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getDigestMethod
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|fields
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DIGEST_METHOD
argument_list|)
decl_stmt|;
if|if
condition|(
name|fields
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|actuals
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fields
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|element
init|=
name|fields
operator|.
name|getName
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|actuals
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|actuals
argument_list|,
name|fields
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      *<p>(Optional, PDF 1.7) An array of names indicating acceptable digest      * algorithms to use when signing. The value shall be one of<b>SHA1</b>,      *<b>SHA256</b>,<b>SHA384</b>,<b>SHA512</b>,<b>RIPEMD160</b>. The default      * value is implementation-specific.</p>      *      *<p>This property is only applicable if the digital credential signing contains RSA      * public/privat keys</p>      *      * @param digestMethod is a list of possible names of the digests, that should be      * used for signing.      */
specifier|public
name|void
name|setDigestMethod
parameter_list|(
name|List
argument_list|<
name|COSName
argument_list|>
name|digestMethod
parameter_list|)
block|{
comment|// integrity check
for|for
control|(
name|COSName
name|cosName
range|:
name|digestMethod
control|)
block|{
if|if
condition|(
operator|!
operator|(
name|cosName
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|DIGEST_SHA1
argument_list|)
operator|||
name|cosName
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|DIGEST_SHA256
argument_list|)
operator|||
name|cosName
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|DIGEST_SHA384
argument_list|)
operator|||
name|cosName
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|DIGEST_SHA512
argument_list|)
operator|||
name|cosName
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|DIGEST_RIPEMD160
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Specified digest "
operator|+
name|cosName
operator|.
name|getName
argument_list|()
operator|+
literal|" isn't allowed."
argument_list|)
throw|;
block|}
block|}
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DIGEST_METHOD
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|digestMethod
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * The minimum required capability of the signature field seed value      * dictionary parser. A value of 1 specifies that the parser shall be able to      * recognize all seed value dictionary entries in a PDF 1.5 file. A value of 2      * specifies that it shall be able to recognize all seed value dictionary entries      * specified.      *      * @return the minimum required capability of the signature field seed value      * dictionary parser      */
specifier|public
name|float
name|getV
parameter_list|()
block|{
return|return
name|dictionary
operator|.
name|getFloat
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
return|;
block|}
comment|/**      * (Optional) The minimum required capability of the signature field seed value      * dictionary parser. A value of 1 specifies that the parser shall be able to      * recognize all seed value dictionary entries in a PDF 1.5 file. A value of 2      * specifies that it shall be able to recognize all seed value dictionary entries      * specified.      *      * @param minimumRequiredCapability is the minimum required capability of the      * signature field seed value dictionary parser      */
specifier|public
name|void
name|setV
parameter_list|(
name|float
name|minimumRequiredCapability
parameter_list|)
block|{
name|dictionary
operator|.
name|setFloat
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|minimumRequiredCapability
argument_list|)
expr_stmt|;
block|}
comment|/**      * If the Reasons array is provided and {@link #isReasonRequired()} indicates that      * Reasons is a required constraint, one of the reasons in the array shall be used      * for the signature dictionary; otherwise signing shall not take place. If the      * {@link #isReasonRequired()} indicates Reasons is an optional constraint, one of      * the reasons in the array may be chose or a custom reason can be provided.      *      * @return the reasons that should be used by the signature handler      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getReasons
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|fields
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|REASONS
argument_list|)
decl_stmt|;
if|if
condition|(
name|fields
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|actuals
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fields
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|element
init|=
name|fields
operator|.
name|getString
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|actuals
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|actuals
argument_list|,
name|fields
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * (Optional) An array of text strings that specifying possible reasons for signing      * a document. If specified, the reasons supplied in this entry replace those used      * by conforming products.      *      * @param reasons is a list of possible text string that specifying possible reasons      */
specifier|public
name|void
name|setReasonsd
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|reasons
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|REASONS
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|reasons
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      *<p>(Optional; PDF 1.6) A dictionary containing a single entry whose key is P      * and whose value is an integer between 0 and 3. A value of 0 defines the      * signatures as an author signature. The value 1 through 3 shall be used for      * certification signatures and correspond to the value of P in a DocMDP transform      * parameters dictionary.</p>      *      *<p>If this MDP key is not present or the MDP dictionary does not contain a P      * entry, no rules shall be defined regarding the type of signature or its      * permissions.</p>      *      * @return the mdp dictionary as PDSeedValueMDP      */
specifier|public
name|PDSeedValueMDP
name|getMDP
parameter_list|()
block|{
name|COSDictionary
name|dict
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
name|MDP
argument_list|)
decl_stmt|;
name|PDSeedValueMDP
name|mdp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dict
operator|!=
literal|null
condition|)
block|{
name|mdp
operator|=
operator|new
name|PDSeedValueMDP
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
return|return
name|mdp
return|;
block|}
comment|/**      *<p>(Optional; PDF 1.6) A dictionary containing a single entry whose key is P      * and whose value is an integer between 0 and 3. A value of 0 defines the      * signatures as an author signature. The value 1 through 3 shall be used for      * certification signatures and correspond to the value of P in a DocMDP transform      * parameters dictionary.</p>      *      *<p>If this MDP key is not present or the MDP dictionary does not contain a P      * entry, no rules shall be defined regarding the type of signature or its      * permissions.</p>      *      * @param mdp dictionary      */
specifier|public
name|void
name|setMPD
parameter_list|(
name|PDSeedValueMDP
name|mdp
parameter_list|)
block|{
if|if
condition|(
name|mdp
operator|!=
literal|null
condition|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|MDP
argument_list|,
name|mdp
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *<p>(Optional; PDF 1.6) A time stamp dictionary containing two entries. URL which      * is a ASCII string specifying the URL to a rfc3161 conform timestamp server and Ff      * to indicate if a timestamp is required or optional.</p>      *      * @return the timestamp dictionary as PDSeedValueTimeStamp      */
specifier|public
name|PDSeedValueTimeStamp
name|getTimeStamp
parameter_list|()
block|{
name|COSDictionary
name|dict
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
name|TIME_STAMP
argument_list|)
decl_stmt|;
name|PDSeedValueTimeStamp
name|timestamp
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|dict
operator|!=
literal|null
condition|)
block|{
name|timestamp
operator|=
operator|new
name|PDSeedValueTimeStamp
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
return|return
name|timestamp
return|;
block|}
comment|/**      *<p>(Optional; PDF 1.6) A time stamp dictionary containing two entries. URL which      * is a ASCII string specifying the URL to a rfc3161 conform timestamp server and Ff      * to indicate if a timestamp is required or optional.</p>      *      * @param timestamp dictionary      */
specifier|public
name|void
name|setTimeStamp
parameter_list|(
name|PDSeedValueTimeStamp
name|timestamp
parameter_list|)
block|{
if|if
condition|(
name|timestamp
operator|!=
literal|null
condition|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TIME_STAMP
argument_list|,
name|timestamp
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * (Optional, PDF 1.6) An array of text strings that specifying possible legal      * attestations.      *      * @return the reasons that should be used by the signature handler      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getLegalAttestation
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|fields
init|=
operator|(
name|COSArray
operator|)
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|LEGAL_ATTESTATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|fields
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|actuals
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fields
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|element
init|=
name|fields
operator|.
name|getString
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|actuals
operator|.
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|actuals
argument_list|,
name|fields
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * (Optional, PDF 1.6) An array of text strings that specifying possible legal      * attestations.      *      * @param legalAttestation is a list of possible text string that specifying possible      * legal attestations.      */
specifier|public
name|void
name|setLegalAttestation
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|legalAttestation
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|LEGAL_ATTESTATION
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|legalAttestation
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

