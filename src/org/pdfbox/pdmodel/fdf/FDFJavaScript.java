begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2004, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|fdf
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
name|COSArrayList
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
name|PDTextStream
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
name|PDNamedTextStream
import|;
end_import

begin_comment
comment|/**  * This represents an FDF JavaScript dictionary that is part of the FDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|FDFJavaScript
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|js
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFJavaScript
parameter_list|()
block|{
name|js
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param javaScript The FDF java script.      */
specifier|public
name|FDFJavaScript
parameter_list|(
name|COSDictionary
name|javaScript
parameter_list|)
block|{
name|js
operator|=
name|javaScript
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|js
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|js
return|;
block|}
comment|/**      * This will get the javascript that is executed before the import.      *      * @return Some javascript code.      */
specifier|public
name|PDTextStream
name|getBefore
parameter_list|()
block|{
return|return
name|PDTextStream
operator|.
name|createTextStream
argument_list|(
name|js
operator|.
name|getDictionaryObject
argument_list|(
literal|"Before"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the javascript code the will get execute before the import.      *      * @param before A reference to some javascript code.      */
specifier|public
name|void
name|setBefore
parameter_list|(
name|PDTextStream
name|before
parameter_list|)
block|{
name|js
operator|.
name|setItem
argument_list|(
literal|"Before"
argument_list|,
name|before
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the javascript that is executed after the import.      *      * @return Some javascript code.      */
specifier|public
name|PDTextStream
name|getAfter
parameter_list|()
block|{
return|return
name|PDTextStream
operator|.
name|createTextStream
argument_list|(
name|js
operator|.
name|getDictionaryObject
argument_list|(
literal|"After"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the javascript code the will get execute after the import.      *      * @param after A reference to some javascript code.      */
specifier|public
name|void
name|setAfter
parameter_list|(
name|PDTextStream
name|after
parameter_list|)
block|{
name|js
operator|.
name|setItem
argument_list|(
literal|"After"
argument_list|,
name|after
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return a list of PDNamedTextStream objects.  This is the "Doc"      * entry of the pdf document.  These will be added to the PDF documents      * javascript name tree.  This will not return null.      *      * @return A list of all named javascript entries.      */
specifier|public
name|List
name|getNamedJavaScripts
parameter_list|()
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|js
operator|.
name|getDictionaryObject
argument_list|(
literal|"Doc"
argument_list|)
decl_stmt|;
name|List
name|namedStreams
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
if|if
condition|(
name|array
operator|==
literal|null
condition|)
block|{
name|array
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|js
operator|.
name|setItem
argument_list|(
literal|"Doc"
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|array
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|i
operator|++
expr_stmt|;
name|COSBase
name|stream
init|=
name|array
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|PDNamedTextStream
name|namedStream
init|=
operator|new
name|PDNamedTextStream
argument_list|(
name|name
argument_list|,
name|stream
argument_list|)
decl_stmt|;
name|namedStreams
operator|.
name|add
argument_list|(
name|namedStream
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|COSArrayList
argument_list|(
name|namedStreams
argument_list|,
name|array
argument_list|)
return|;
block|}
comment|/**      * This should be a list of PDNamedTextStream objects.      *      * @param namedStreams The named streams.      */
specifier|public
name|void
name|setNamedJavaScripts
parameter_list|(
name|List
name|namedStreams
parameter_list|)
block|{
name|COSArray
name|array
init|=
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|namedStreams
argument_list|)
decl_stmt|;
name|js
operator|.
name|setItem
argument_list|(
literal|"Doc"
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

