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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|COSDictionaryMap
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * This class represents a PDF /AP entry the appearance dictionary.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|PDAppearanceDictionary
implements|implements
name|COSObjectable
block|{
comment|/**      * Log instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|PDAppearanceDictionary
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDAppearanceDictionary
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
comment|//the N entry is required.
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|N
argument_list|,
operator|new
name|COSDictionary
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dict The annotations dictionary.      */
specifier|public
name|PDAppearanceDictionary
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|dictionary
operator|=
name|dict
expr_stmt|;
block|}
comment|/**      * returns the dictionary.      * @return the dictionary      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * returns the dictionary.      * @return the dictionary      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * This will return a list of appearances.  In the case where there is      * only one appearance the map will contain one entry whose key is the string      * "default".      *      * @return A list of key(java.lang.String) value(PDAppearanceStream) pairs      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|getNormalAppearance
parameter_list|()
block|{
name|COSBase
name|ap
init|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|N
argument_list|)
decl_stmt|;
if|if
condition|(
name|ap
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|ap
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|aux
init|=
operator|(
name|COSStream
operator|)
name|ap
decl_stmt|;
name|ap
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
operator|(
operator|(
name|COSDictionary
operator|)
name|ap
operator|)
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DEFAULT
argument_list|,
name|aux
argument_list|)
expr_stmt|;
block|}
name|COSDictionary
name|map
init|=
operator|(
name|COSDictionary
operator|)
name|ap
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|actuals
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|retval
init|=
operator|new
name|COSDictionaryMap
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
argument_list|(
name|actuals
argument_list|,
name|map
argument_list|)
decl_stmt|;
for|for
control|(
name|COSName
name|asName
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|stream
init|=
name|map
operator|.
name|getDictionaryObject
argument_list|(
name|asName
argument_list|)
decl_stmt|;
comment|// PDFBOX-1599: this is just a workaround. The given PDF provides "null" as stream
comment|// which leads to a COSName("null") value and finally to a ClassCastExcpetion
if|if
condition|(
name|stream
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|as
init|=
operator|(
name|COSStream
operator|)
name|stream
decl_stmt|;
name|actuals
operator|.
name|put
argument_list|(
name|asName
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|PDAppearanceStream
argument_list|(
name|as
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"non-conformance workaround: ignore null value for appearance stream."
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a list of appearances.  If you would like to set the single      * appearance then you should use the key "default", and when the PDF is written      * back to the filesystem then there will only be one stream.      *      * @param appearanceMap The updated map with the appearance.      */
specifier|public
name|void
name|setNormalAppearance
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|appearanceMap
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|N
argument_list|,
name|COSDictionaryMap
operator|.
name|convert
argument_list|(
name|appearanceMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the normal appearance when there is only one appearance      * to be shown.      *      * @param ap The appearance stream to show.      */
specifier|public
name|void
name|setNormalAppearance
parameter_list|(
name|PDAppearanceStream
name|ap
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|N
argument_list|,
name|ap
operator|.
name|getStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return a list of appearances.  In the case where there is      * only one appearance the map will contain one entry whose key is the string      * "default".  If there is no rollover appearance then the normal appearance      * will be returned.  Which means that this method will never return null.      *      * @return A list of key(java.lang.String) value(PDAppearanceStream) pairs      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|getRolloverAppearance
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
name|COSBase
name|ap
init|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|R
argument_list|)
decl_stmt|;
if|if
condition|(
name|ap
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
name|getNormalAppearance
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|ap
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|aux
init|=
operator|(
name|COSStream
operator|)
name|ap
decl_stmt|;
name|ap
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
operator|(
operator|(
name|COSDictionary
operator|)
name|ap
operator|)
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DEFAULT
argument_list|,
name|aux
argument_list|)
expr_stmt|;
block|}
name|COSDictionary
name|map
init|=
operator|(
name|COSDictionary
operator|)
name|ap
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|actuals
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|COSDictionaryMap
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
argument_list|(
name|actuals
argument_list|,
name|map
argument_list|)
expr_stmt|;
for|for
control|(
name|COSName
name|asName
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|stream
init|=
name|map
operator|.
name|getDictionaryObject
argument_list|(
name|asName
argument_list|)
decl_stmt|;
comment|// PDFBOX-1599: this is just a workaround. The given PDF provides "null" as stream
comment|// which leads to a COSName("null") value and finally to a ClassCastExcpetion
if|if
condition|(
name|stream
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|as
init|=
operator|(
name|COSStream
operator|)
name|stream
decl_stmt|;
name|actuals
operator|.
name|put
argument_list|(
name|asName
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|PDAppearanceStream
argument_list|(
name|as
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"non-conformance workaround: ignore null value for appearance stream."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a list of appearances.  If you would like to set the single      * appearance then you should use the key "default", and when the PDF is written      * back to the filesystem then there will only be one stream.      *      * @param appearanceMap The updated map with the appearance.      */
specifier|public
name|void
name|setRolloverAppearance
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|appearanceMap
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|R
argument_list|,
name|COSDictionaryMap
operator|.
name|convert
argument_list|(
name|appearanceMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the rollover appearance when there is rollover appearance      * to be shown.      *      * @param ap The appearance stream to show.      */
specifier|public
name|void
name|setRolloverAppearance
parameter_list|(
name|PDAppearanceStream
name|ap
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|R
argument_list|,
name|ap
operator|.
name|getStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return a list of appearances.  In the case where there is      * only one appearance the map will contain one entry whose key is the string      * "default".  If there is no rollover appearance then the normal appearance      * will be returned.  Which means that this method will never return null.      *      * @return A list of key(java.lang.String) value(PDAppearanceStream) pairs      */
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|getDownAppearance
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
name|COSBase
name|ap
init|=
name|dictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
decl_stmt|;
if|if
condition|(
name|ap
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
name|getNormalAppearance
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|ap
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|aux
init|=
operator|(
name|COSStream
operator|)
name|ap
decl_stmt|;
name|ap
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
operator|(
operator|(
name|COSDictionary
operator|)
name|ap
operator|)
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DEFAULT
argument_list|,
name|aux
argument_list|)
expr_stmt|;
block|}
name|COSDictionary
name|map
init|=
operator|(
name|COSDictionary
operator|)
name|ap
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|actuals
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|COSDictionaryMap
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
argument_list|(
name|actuals
argument_list|,
name|map
argument_list|)
expr_stmt|;
for|for
control|(
name|COSName
name|asName
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|COSBase
name|stream
init|=
name|map
operator|.
name|getDictionaryObject
argument_list|(
name|asName
argument_list|)
decl_stmt|;
comment|// PDFBOX-1599: this is just a workaround. The given PDF provides "null" as stream
comment|// which leads to a COSName("null") value and finally to a ClassCastExcpetion
if|if
condition|(
name|stream
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|as
init|=
operator|(
name|COSStream
operator|)
name|stream
decl_stmt|;
name|actuals
operator|.
name|put
argument_list|(
name|asName
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|PDAppearanceStream
argument_list|(
name|as
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"non-conformance workaround: ignore null value for appearance stream."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a list of appearances.  If you would like to set the single      * appearance then you should use the key "default", and when the PDF is written      * back to the filesystem then there will only be one stream.      *      * @param appearanceMap The updated map with the appearance.      */
specifier|public
name|void
name|setDownAppearance
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|PDAppearanceStream
argument_list|>
name|appearanceMap
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|COSDictionaryMap
operator|.
name|convert
argument_list|(
name|appearanceMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the down appearance when there is down appearance      * to be shown.      *      * @param ap The appearance stream to show.      */
specifier|public
name|void
name|setDownAppearance
parameter_list|(
name|PDAppearanceStream
name|ap
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|ap
operator|.
name|getStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

