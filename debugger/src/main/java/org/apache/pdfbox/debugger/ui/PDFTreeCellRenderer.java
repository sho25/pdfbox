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
name|debugger
operator|.
name|ui
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
name|java
operator|.
name|awt
operator|.
name|Graphics
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

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
name|javax
operator|.
name|swing
operator|.
name|ImageIcon
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
name|COSBoolean
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
name|COSFloat
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
name|COSInteger
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
name|COSNull
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
name|COSObject
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
name|cos
operator|.
name|COSString
import|;
end_import

begin_comment
comment|/**  * A class to render tree cells for the pdfviewer.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDFTreeCellRenderer
extends|extends
name|DefaultTreeCellRenderer
block|{
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_ARRAY
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"array"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_BOOLEAN
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"boolean"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_DICT
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"dict"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_HEX
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"hex"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_INDIRECT
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"indirect"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_INTEGER
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"integer"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_NAME
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"name"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_NULL
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"null"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_REAL
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"real"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_STREAM_DICT
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"stream-dict"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_STRING
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"string"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_PDF
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"pdf"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|ImageIcon
name|ICON_PAGE
init|=
operator|new
name|ImageIcon
argument_list|(
name|getImageUrl
argument_list|(
literal|"page"
argument_list|)
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|URL
name|getImageUrl
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
name|fullName
init|=
literal|"/org/apache/pdfbox/debugger/"
operator|+
name|name
operator|+
literal|".png"
decl_stmt|;
return|return
name|PDFTreeCellRenderer
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|fullName
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|Component
name|component
init|=
name|super
operator|.
name|getTreeCellRendererComponent
argument_list|(
name|tree
argument_list|,
name|toTreeObject
argument_list|(
name|nodeValue
argument_list|)
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
decl_stmt|;
name|setIcon
argument_list|(
name|lookupIconWithOverlay
argument_list|(
name|nodeValue
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|component
return|;
block|}
specifier|private
name|Object
name|toTreeObject
parameter_list|(
name|Object
name|nodeValue
parameter_list|)
block|{
name|Object
name|result
init|=
name|nodeValue
decl_stmt|;
if|if
condition|(
name|nodeValue
operator|instanceof
name|MapEntry
operator|||
name|nodeValue
operator|instanceof
name|ArrayEntry
condition|)
block|{
name|String
name|key
decl_stmt|;
name|Object
name|object
decl_stmt|;
name|Object
name|value
decl_stmt|;
name|COSBase
name|item
decl_stmt|;
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
name|key
operator|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|object
operator|=
name|toTreeObject
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|value
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|item
operator|=
name|entry
operator|.
name|getItem
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ArrayEntry
name|entry
init|=
operator|(
name|ArrayEntry
operator|)
name|nodeValue
decl_stmt|;
name|key
operator|=
literal|""
operator|+
name|entry
operator|.
name|getIndex
argument_list|()
expr_stmt|;
name|object
operator|=
name|toTreeObject
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|value
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|item
operator|=
name|entry
operator|.
name|getItem
argument_list|()
expr_stmt|;
block|}
name|String
name|stringResult
init|=
name|key
decl_stmt|;
if|if
condition|(
name|object
operator|instanceof
name|String
operator|&&
operator|(
operator|(
name|String
operator|)
name|object
operator|)
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|stringResult
operator|+=
literal|":  "
operator|+
name|object
expr_stmt|;
if|if
condition|(
name|item
operator|instanceof
name|COSObject
condition|)
block|{
name|COSObject
name|indirect
init|=
operator|(
name|COSObject
operator|)
name|item
decl_stmt|;
name|stringResult
operator|+=
literal|" ["
operator|+
name|indirect
operator|.
name|getObjectNumber
argument_list|()
operator|+
literal|" "
operator|+
name|indirect
operator|.
name|getGenerationNumber
argument_list|()
operator|+
literal|" R]"
expr_stmt|;
block|}
name|stringResult
operator|+=
name|toTreePostfix
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
name|stringResult
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSBoolean
condition|)
block|{
name|result
operator|=
literal|""
operator|+
operator|(
operator|(
name|COSBoolean
operator|)
name|nodeValue
operator|)
operator|.
name|getValue
argument_list|()
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
name|result
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
name|result
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
name|String
name|text
init|=
operator|(
operator|(
name|COSString
operator|)
name|nodeValue
operator|)
operator|.
name|getString
argument_list|()
decl_stmt|;
comment|// display unprintable strings as hex
for|for
control|(
name|char
name|c
range|:
name|text
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
name|Character
operator|.
name|isISOControl
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|text
operator|=
literal|"<"
operator|+
operator|(
operator|(
name|COSString
operator|)
name|nodeValue
operator|)
operator|.
name|toHexString
argument_list|()
operator|+
literal|">"
expr_stmt|;
break|break;
block|}
block|}
name|result
operator|=
name|text
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
name|result
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
name|COSNull
operator|||
name|nodeValue
operator|==
literal|null
condition|)
block|{
name|result
operator|=
literal|""
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
name|COSName
operator|.
name|XREF
operator|.
name|equals
argument_list|(
name|dict
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
argument_list|)
condition|)
block|{
name|result
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
literal|"("
operator|+
name|dict
operator|.
name|size
argument_list|()
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
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|nodeValue
decl_stmt|;
name|result
operator|=
literal|"("
operator|+
name|array
operator|.
name|size
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|DocumentEntry
condition|)
block|{
name|result
operator|=
name|nodeValue
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
name|String
name|toTreePostfix
parameter_list|(
name|Object
name|nodeValue
parameter_list|)
block|{
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSDictionary
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
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
name|dict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
condition|)
block|{
name|COSName
name|type
init|=
name|dict
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"   /T:"
argument_list|)
operator|.
name|append
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
condition|)
block|{
name|COSName
name|subtype
init|=
name|dict
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"  /S:"
argument_list|)
operator|.
name|append
argument_list|(
name|subtype
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
specifier|private
name|ImageIcon
name|lookupIconWithOverlay
parameter_list|(
name|Object
name|nodeValue
parameter_list|)
block|{
name|ImageIcon
name|icon
init|=
name|lookupIcon
argument_list|(
name|nodeValue
argument_list|)
decl_stmt|;
name|boolean
name|isIndirect
init|=
literal|false
decl_stmt|;
name|boolean
name|isStream
init|=
literal|false
decl_stmt|;
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
if|if
condition|(
name|entry
operator|.
name|getItem
argument_list|()
operator|instanceof
name|COSObject
condition|)
block|{
name|isIndirect
operator|=
literal|true
expr_stmt|;
name|isStream
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|COSStream
expr_stmt|;
block|}
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
if|if
condition|(
name|entry
operator|.
name|getItem
argument_list|()
operator|instanceof
name|COSObject
condition|)
block|{
name|isIndirect
operator|=
literal|true
expr_stmt|;
name|isStream
operator|=
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|COSStream
expr_stmt|;
block|}
block|}
if|if
condition|(
name|isIndirect
operator|&&
operator|!
name|isStream
condition|)
block|{
name|OverlayIcon
name|overlay
init|=
operator|new
name|OverlayIcon
argument_list|(
name|icon
argument_list|)
decl_stmt|;
name|overlay
operator|.
name|add
argument_list|(
name|ICON_INDIRECT
argument_list|)
expr_stmt|;
return|return
name|overlay
return|;
block|}
return|return
name|icon
return|;
block|}
specifier|private
name|ImageIcon
name|lookupIcon
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
return|return
name|lookupIcon
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
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
return|return
name|lookupIcon
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSBoolean
condition|)
block|{
return|return
name|ICON_BOOLEAN
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSFloat
condition|)
block|{
return|return
name|ICON_REAL
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSInteger
condition|)
block|{
return|return
name|ICON_INTEGER
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSString
condition|)
block|{
name|String
name|text
init|=
operator|(
operator|(
name|COSString
operator|)
name|nodeValue
operator|)
operator|.
name|getString
argument_list|()
decl_stmt|;
comment|// display unprintable strings as hex
for|for
control|(
name|char
name|c
range|:
name|text
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
name|Character
operator|.
name|isISOControl
argument_list|(
name|c
argument_list|)
condition|)
block|{
return|return
name|ICON_HEX
return|;
block|}
block|}
return|return
name|ICON_STRING
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSName
condition|)
block|{
return|return
name|ICON_NAME
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSNull
operator|||
name|nodeValue
operator|==
literal|null
condition|)
block|{
return|return
name|ICON_NULL
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSStream
condition|)
block|{
return|return
name|ICON_STREAM_DICT
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
name|ICON_DICT
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|COSArray
condition|)
block|{
return|return
name|ICON_ARRAY
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|DocumentEntry
condition|)
block|{
return|return
name|ICON_PDF
return|;
block|}
elseif|else
if|if
condition|(
name|nodeValue
operator|instanceof
name|PageEntry
condition|)
block|{
return|return
name|ICON_PAGE
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * An ImageIcon which allows other ImageIcon overlays.      */
specifier|private
class|class
name|OverlayIcon
extends|extends
name|ImageIcon
block|{
specifier|private
specifier|final
name|ImageIcon
name|base
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|ImageIcon
argument_list|>
name|overlays
decl_stmt|;
name|OverlayIcon
parameter_list|(
name|ImageIcon
name|base
parameter_list|)
block|{
name|super
argument_list|(
name|base
operator|.
name|getImage
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
name|this
operator|.
name|overlays
operator|=
operator|new
name|ArrayList
argument_list|<
name|ImageIcon
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|void
name|add
parameter_list|(
name|ImageIcon
name|overlay
parameter_list|)
block|{
name|overlays
operator|.
name|add
argument_list|(
name|overlay
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|paintIcon
parameter_list|(
name|Component
name|c
parameter_list|,
name|Graphics
name|g
parameter_list|,
name|int
name|x
parameter_list|,
name|int
name|y
parameter_list|)
block|{
name|base
operator|.
name|paintIcon
argument_list|(
name|c
argument_list|,
name|g
argument_list|,
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
for|for
control|(
name|ImageIcon
name|icon
range|:
name|overlays
control|)
block|{
name|icon
operator|.
name|paintIcon
argument_list|(
name|c
argument_list|,
name|g
argument_list|,
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

