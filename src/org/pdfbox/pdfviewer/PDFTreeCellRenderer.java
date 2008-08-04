begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003-2006, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdfviewer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JTree
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|tree
operator|.
name|DefaultTreeCellRenderer
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
name|cos
operator|.
name|COSNull
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
name|COSFloat
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
name|COSInteger
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
name|cos
operator|.
name|COSString
import|;
end_import

begin_comment
comment|/**  * A class to render tree cells for the pdfviewer.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.6 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFTreeCellRenderer
extends|extends
name|DefaultTreeCellRenderer
block|{
comment|/**      * {@inheritDoc}      */
specifier|public
name|Component
name|getTreeCellRendererComponent
parameter_list|(
name|JTree
name|tree
parameter_list|,
name|Object
name|nodeValue
parameter_list|,
name|boolean
name|isSelected
parameter_list|,
name|boolean
name|expanded
parameter_list|,
name|boolean
name|leaf
parameter_list|,
name|int
name|row
parameter_list|,
name|boolean
name|componentHasFocus
parameter_list|)
block|{
name|nodeValue
operator|=
name|convertToTreeObject
argument_list|(
name|nodeValue
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|getTreeCellRendererComponent
argument_list|(
name|tree
argument_list|,
name|nodeValue
argument_list|,
name|isSelected
argument_list|,
name|expanded
argument_list|,
name|leaf
argument_list|,
name|row
argument_list|,
name|componentHasFocus
argument_list|)
return|;
block|}
specifier|private
name|Object
name|convertToTreeObject
parameter_list|(
name|Object
name|nodeValue
parameter_list|)
block|{
if|if
condition|(
name|nodeValue
operator|instanceof
name|MapEntry
condition|)
block|{
name|MapEntry
name|entry
init|=
operator|(
name|MapEntry
operator|)
name|nodeValue
decl_stmt|;
name|COSName
name|key
init|=
operator|(
name|COSName
operator|)
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|COSBase
name|value
init|=
operator|(
name|COSBase
operator|)
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|nodeValue
operator|=
name|key
operator|.
name|getName
argument_list|()
operator|+
literal|":"
operator|+
name|convertToTreeObject
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSFloat
condition|)
block|{
name|nodeValue
operator|=
literal|""
operator|+
operator|(
operator|(
name|COSFloat
operator|)
name|nodeValue
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSInteger
condition|)
block|{
name|nodeValue
operator|=
literal|""
operator|+
operator|(
operator|(
name|COSInteger
operator|)
name|nodeValue
operator|)
operator|.
name|intValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSString
condition|)
block|{
name|nodeValue
operator|=
operator|(
operator|(
name|COSString
operator|)
name|nodeValue
operator|)
operator|.
name|getString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSName
condition|)
block|{
name|nodeValue
operator|=
operator|(
operator|(
name|COSName
operator|)
name|nodeValue
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|ArrayEntry
condition|)
block|{
name|ArrayEntry
name|entry
init|=
operator|(
name|ArrayEntry
operator|)
name|nodeValue
decl_stmt|;
name|nodeValue
operator|=
literal|"["
operator|+
name|entry
operator|.
name|getIndex
argument_list|()
operator|+
literal|"]"
operator|+
name|convertToTreeObject
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSNull
condition|)
block|{
name|nodeValue
operator|=
literal|"null"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|nodeValue
decl_stmt|;
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSStream
condition|)
block|{
name|nodeValue
operator|=
literal|"Stream"
expr_stmt|;
block|}
else|else
block|{
name|nodeValue
operator|=
literal|"Dictionary"
expr_stmt|;
block|}
name|COSName
name|type
init|=
operator|(
name|COSName
operator|)
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|nodeValue
operator|=
name|nodeValue
operator|+
literal|"("
operator|+
name|type
operator|.
name|getName
argument_list|()
expr_stmt|;
name|COSName
name|subType
init|=
operator|(
name|COSName
operator|)
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|subType
operator|!=
literal|null
condition|)
block|{
name|nodeValue
operator|=
name|nodeValue
operator|+
literal|":"
operator|+
name|subType
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|nodeValue
operator|=
name|nodeValue
operator|+
literal|")"
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSArray
condition|)
block|{
name|nodeValue
operator|=
literal|"Array"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSString
condition|)
block|{
name|nodeValue
operator|=
operator|(
operator|(
name|COSString
operator|)
name|nodeValue
operator|)
operator|.
name|getString
argument_list|()
expr_stmt|;
block|}
return|return
name|nodeValue
return|;
block|}
block|}
end_class

end_unit

