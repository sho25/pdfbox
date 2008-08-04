begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2004, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
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
name|Iterator
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
name|getPDFName
argument_list|(
literal|"N"
argument_list|)
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
name|getPDFName
argument_list|(
literal|"N"
argument_list|)
argument_list|)
decl_stmt|;
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
name|getPDFName
argument_list|(
literal|"default"
argument_list|)
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
name|actuals
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|Map
name|retval
init|=
operator|new
name|COSDictionaryMap
argument_list|(
name|actuals
argument_list|,
name|map
argument_list|)
decl_stmt|;
name|Iterator
name|asNames
init|=
name|map
operator|.
name|keyList
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|asNames
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSName
name|asName
init|=
operator|(
name|COSName
operator|)
name|asNames
operator|.
name|next
argument_list|()
decl_stmt|;
name|COSStream
name|as
init|=
operator|(
name|COSStream
operator|)
name|map
operator|.
name|getDictionaryObject
argument_list|(
name|asName
argument_list|)
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
name|appearanceMap
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"N"
argument_list|)
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
name|getPDFName
argument_list|(
literal|"N"
argument_list|)
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
name|getRolloverAppearance
parameter_list|()
block|{
name|Map
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
name|getPDFName
argument_list|(
literal|"R"
argument_list|)
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
name|getPDFName
argument_list|(
literal|"default"
argument_list|)
argument_list|,
name|ap
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
name|actuals
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|COSDictionaryMap
argument_list|(
name|actuals
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|Iterator
name|asNames
init|=
name|map
operator|.
name|keyList
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|asNames
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSName
name|asName
init|=
operator|(
name|COSName
operator|)
name|asNames
operator|.
name|next
argument_list|()
decl_stmt|;
name|COSStream
name|as
init|=
operator|(
name|COSStream
operator|)
name|map
operator|.
name|getDictionaryObject
argument_list|(
name|asName
argument_list|)
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
name|appearanceMap
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"R"
argument_list|)
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
comment|/**      * This will return a list of appearances.  In the case where there is      * only one appearance the map will contain one entry whose key is the string      * "default".  If there is no rollover appearance then the normal appearance      * will be returned.  Which means that this method will never return null.      *      * @return A list of key(java.lang.String) value(PDAppearanceStream) pairs      */
specifier|public
name|Map
name|getDownAppearance
parameter_list|()
block|{
name|Map
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
name|getPDFName
argument_list|(
literal|"D"
argument_list|)
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
name|getPDFName
argument_list|(
literal|"default"
argument_list|)
argument_list|,
name|ap
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
name|actuals
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|retval
operator|=
operator|new
name|COSDictionaryMap
argument_list|(
name|actuals
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|Iterator
name|asNames
init|=
name|map
operator|.
name|keyList
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|asNames
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|COSName
name|asName
init|=
operator|(
name|COSName
operator|)
name|asNames
operator|.
name|next
argument_list|()
decl_stmt|;
name|COSStream
name|as
init|=
operator|(
name|COSStream
operator|)
name|map
operator|.
name|getDictionaryObject
argument_list|(
name|asName
argument_list|)
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
name|appearanceMap
parameter_list|)
block|{
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"D"
argument_list|)
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
block|}
end_class

end_unit

