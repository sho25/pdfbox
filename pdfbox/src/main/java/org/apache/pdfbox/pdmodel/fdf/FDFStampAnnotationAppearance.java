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
name|fdf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|util
operator|.
name|Hex
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_comment
comment|/**  * Parse the XML for the /AP entry tree.  *  * @author Andrew Hung  */
end_comment

begin_class
class|class
name|FDFStampAnnotationAppearance
implements|implements
name|COSObjectable
block|{
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
name|FDFStampAnnotationAppearance
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Default constructor      */
name|FDFStampAnnotationAppearance
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
comment|// the N entry is required.
name|dictionary
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|N
argument_list|,
operator|new
name|COSStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for reading.      *      * @param dictionary The annotations dictionary.      */
name|FDFStampAnnotationAppearance
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|this
operator|.
name|dictionary
operator|=
name|dictionary
expr_stmt|;
block|}
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
comment|/**      * This will create an Appearance dictionary from an appearance XML document.      *      * @param fdfXML The XML document that contains the appearance data.      */
name|FDFStampAnnotationAppearance
parameter_list|(
name|Element
name|appearanceXML
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Build dictionary for Appearance based on the appearanceXML"
argument_list|)
expr_stmt|;
name|NodeList
name|nodeList
init|=
name|appearanceXML
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|Node
name|node
init|=
literal|null
decl_stmt|;
name|Element
name|child
init|=
literal|null
decl_stmt|;
name|String
name|parentAttrKey
init|=
name|appearanceXML
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Appearance Root - tag: "
operator|+
name|appearanceXML
operator|.
name|getTagName
argument_list|()
operator|+
literal|", name: "
operator|+
name|appearanceXML
operator|.
name|getNodeName
argument_list|()
operator|+
literal|", key: "
operator|+
name|parentAttrKey
operator|+
literal|", children: "
operator|+
name|nodeList
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
comment|// Currently only handles Appearance dictionary (AP key on the root)
if|if
condition|(
literal|"AP"
operator|.
name|equals
argument_list|(
name|appearanceXML
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
argument_list|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nodeList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|node
operator|=
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|child
operator|=
operator|(
name|Element
operator|)
name|node
expr_stmt|;
if|if
condition|(
literal|"STREAM"
operator|.
name|equalsIgnoreCase
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Process "
operator|+
name|child
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
operator|+
literal|" item in the dictionary after processing the "
operator|+
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
expr_stmt|;
name|dictionary
operator|.
name|setItem
argument_list|(
name|child
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
argument_list|,
name|parseStreamElement
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Set "
operator|+
name|child
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|parentAttrKey
operator|+
literal|" => Not handling element: "
operator|+
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|parentAttrKey
operator|+
literal|" => Not handling element: "
operator|+
name|appearanceXML
operator|.
name|getTagName
argument_list|()
operator|+
literal|" with key: "
operator|+
name|appearanceXML
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|COSStream
name|parseStreamElement
parameter_list|(
name|Element
name|streamEl
parameter_list|)
throws|throws
name|IOException
block|{
name|COSStream
name|stream
init|=
operator|new
name|COSStream
argument_list|()
decl_stmt|;
name|NodeList
name|nodeList
init|=
name|streamEl
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|Node
name|node
decl_stmt|;
name|Element
name|child
decl_stmt|;
name|String
name|childAttrKey
decl_stmt|;
name|String
name|childAttrVal
decl_stmt|;
name|String
name|parentAttrKey
init|=
name|streamEl
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
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
name|nodeList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|node
operator|=
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|child
operator|=
operator|(
name|Element
operator|)
name|node
expr_stmt|;
name|childAttrKey
operator|=
name|child
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
expr_stmt|;
name|childAttrVal
operator|=
name|child
operator|.
name|getAttribute
argument_list|(
literal|"VAL"
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => reading child: "
operator|+
name|child
operator|.
name|getTagName
argument_list|()
operator|+
literal|" with key: "
operator|+
name|childAttrKey
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"INT"
operator|.
name|equalsIgnoreCase
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
literal|"Length"
operator|.
name|equals
argument_list|(
name|childAttrKey
argument_list|)
condition|)
block|{
name|stream
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|childAttrKey
argument_list|)
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|childAttrVal
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Set "
operator|+
name|childAttrKey
operator|+
literal|": "
operator|+
name|childAttrVal
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"NAME"
operator|.
name|equalsIgnoreCase
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
name|stream
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|childAttrKey
argument_list|)
argument_list|,
name|childAttrVal
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Set "
operator|+
name|childAttrKey
operator|+
literal|": "
operator|+
name|childAttrVal
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"BOOL"
operator|.
name|equalsIgnoreCase
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
name|stream
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|childAttrKey
argument_list|)
argument_list|,
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|childAttrVal
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Set Interpolate: "
operator|+
name|childAttrVal
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"ARRAY"
operator|.
name|equalsIgnoreCase
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|childAttrKey
argument_list|)
argument_list|,
name|parseArrayElement
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Set "
operator|+
name|childAttrKey
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"DICT"
operator|.
name|equalsIgnoreCase
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|childAttrKey
argument_list|)
argument_list|,
name|parseDictElement
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Set "
operator|+
name|childAttrKey
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"STREAM"
operator|.
name|equalsIgnoreCase
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
name|stream
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|childAttrKey
argument_list|)
argument_list|,
name|parseStreamElement
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Set "
operator|+
name|childAttrKey
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"DATA"
operator|.
name|equalsIgnoreCase
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Handling DATA with encoding: "
operator|+
name|child
operator|.
name|getAttribute
argument_list|(
literal|"ENCODING"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"HEX"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getAttribute
argument_list|(
literal|"ENCODING"
argument_list|)
argument_list|)
condition|)
block|{
name|OutputStream
name|os
init|=
literal|null
decl_stmt|;
try|try
block|{
name|os
operator|=
name|stream
operator|.
name|createRawOutputStream
argument_list|()
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|Hex
operator|.
name|decodeHex
argument_list|(
name|child
operator|.
name|getTextContent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Data was streamed"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|parentAttrKey
operator|+
literal|" => Not handling element DATA encoding: "
operator|+
name|child
operator|.
name|getAttribute
argument_list|(
literal|"ENCODING"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|parentAttrKey
operator|+
literal|" => Not handling child element: "
operator|+
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|stream
return|;
block|}
specifier|private
name|COSArray
name|parseArrayElement
parameter_list|(
name|Element
name|arrayEl
parameter_list|)
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Parse "
operator|+
name|arrayEl
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
operator|+
literal|" Array"
argument_list|)
expr_stmt|;
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|NodeList
name|nodeList
init|=
name|arrayEl
operator|.
name|getElementsByTagName
argument_list|(
literal|"FIXED"
argument_list|)
decl_stmt|;
name|Node
name|node
decl_stmt|;
name|Element
name|el
decl_stmt|;
name|String
name|elAttrKey
init|=
name|arrayEl
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"BBox"
operator|.
name|equals
argument_list|(
name|elAttrKey
argument_list|)
condition|)
block|{
if|if
condition|(
name|nodeList
operator|.
name|getLength
argument_list|()
operator|<
literal|4
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"BBox does not have enough coordinates, only has: "
operator|+
name|nodeList
operator|.
name|getLength
argument_list|()
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"Matrix"
operator|.
name|equals
argument_list|(
name|elAttrKey
argument_list|)
condition|)
block|{
if|if
condition|(
name|nodeList
operator|.
name|getLength
argument_list|()
operator|<
literal|6
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Matrix does not have enough coordinates, only has: "
operator|+
name|nodeList
operator|.
name|getLength
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"There are "
operator|+
name|nodeList
operator|.
name|getLength
argument_list|()
operator|+
literal|" FIXED elements"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nodeList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|node
operator|=
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|el
operator|=
operator|(
name|Element
operator|)
name|node
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|elAttrKey
operator|+
literal|" value("
operator|+
name|i
operator|+
literal|"): "
operator|+
name|el
operator|.
name|getAttribute
argument_list|(
literal|"VAL"
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|el
operator|.
name|getAttribute
argument_list|(
literal|"VAL"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|array
return|;
block|}
specifier|private
name|COSDictionary
name|parseDictElement
parameter_list|(
name|Element
name|dictEl
parameter_list|)
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Parse "
operator|+
name|dictEl
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
operator|+
literal|" Dictionary"
argument_list|)
expr_stmt|;
name|COSDictionary
name|dict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|NodeList
name|nodeList
init|=
name|dictEl
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|Node
name|node
decl_stmt|;
name|Element
name|child
decl_stmt|;
name|String
name|childAttrKey
decl_stmt|;
name|String
name|childAttrVal
decl_stmt|;
name|String
name|parentAttrKey
init|=
name|dictEl
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
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
name|nodeList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|node
operator|=
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|child
operator|=
operator|(
name|Element
operator|)
name|node
expr_stmt|;
name|childAttrKey
operator|=
name|child
operator|.
name|getAttribute
argument_list|(
literal|"KEY"
argument_list|)
expr_stmt|;
name|childAttrVal
operator|=
name|child
operator|.
name|getAttribute
argument_list|(
literal|"VAL"
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"DICT"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Handling DICT element with key: "
operator|+
name|childAttrKey
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|childAttrKey
argument_list|)
argument_list|,
name|parseDictElement
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Set "
operator|+
name|childAttrKey
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"STREAM"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Handling STREAM element with key: "
operator|+
name|childAttrKey
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|childAttrKey
argument_list|)
argument_list|,
name|parseStreamElement
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"NAME"
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Handling NAME element with key: "
operator|+
name|childAttrKey
argument_list|)
expr_stmt|;
name|dict
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|childAttrKey
argument_list|)
argument_list|,
name|childAttrVal
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
name|parentAttrKey
operator|+
literal|" => Set "
operator|+
name|childAttrKey
operator|+
literal|": "
operator|+
name|childAttrVal
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|parentAttrKey
operator|+
literal|" => NOT handling child element: "
operator|+
name|child
operator|.
name|getTagName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|dict
return|;
block|}
block|}
end_class

end_unit

