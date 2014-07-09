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
name|Writer
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
name|COSNumber
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
name|PDTextStream
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
name|interactive
operator|.
name|action
operator|.
name|PDActionFactory
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
name|interactive
operator|.
name|action
operator|.
name|PDAdditionalActions
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
name|interactive
operator|.
name|action
operator|.
name|PDAction
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAppearanceDictionary
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
comment|/**  * This represents an FDF field that is part of the FDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|FDFField
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|field
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFField
parameter_list|()
block|{
name|field
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param f The FDF field.      */
specifier|public
name|FDFField
parameter_list|(
name|COSDictionary
name|f
parameter_list|)
block|{
name|field
operator|=
name|f
expr_stmt|;
block|}
comment|/**      * This will create an FDF field from an XFDF XML document.      *      * @param fieldXML The XML document that contains the XFDF data.      * @throws IOException If there is an error reading from the dom.      */
specifier|public
name|FDFField
parameter_list|(
name|Element
name|fieldXML
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|setPartialFieldName
argument_list|(
name|fieldXML
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|NodeList
name|nodeList
init|=
name|fieldXML
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|FDFField
argument_list|>
name|kids
init|=
operator|new
name|ArrayList
argument_list|<
name|FDFField
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
name|nodeList
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|nodeList
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|Element
name|child
init|=
operator|(
name|Element
operator|)
name|node
decl_stmt|;
if|if
condition|(
name|child
operator|.
name|getTagName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"value"
argument_list|)
condition|)
block|{
name|setValue
argument_list|(
name|XMLUtil
operator|.
name|getNodeValue
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|child
operator|.
name|getTagName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"value-richtext"
argument_list|)
condition|)
block|{
name|setRichText
argument_list|(
operator|new
name|PDTextStream
argument_list|(
name|XMLUtil
operator|.
name|getNodeValue
argument_list|(
name|child
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|child
operator|.
name|getTagName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"field"
argument_list|)
condition|)
block|{
name|kids
operator|.
name|add
argument_list|(
operator|new
name|FDFField
argument_list|(
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|kids
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|setKids
argument_list|(
name|kids
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will write this element as an XML document.      *      * @param output The stream to write the xml to.      *      * @throws IOException If there is an error writing the XML.      */
specifier|public
name|void
name|writeXML
parameter_list|(
name|Writer
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|output
operator|.
name|write
argument_list|(
literal|"<field name=\""
operator|+
name|getPartialFieldName
argument_list|()
operator|+
literal|"\">\n"
argument_list|)
expr_stmt|;
name|Object
name|value
init|=
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
literal|"<value>"
operator|+
name|value
operator|+
literal|"</value>\n"
argument_list|)
expr_stmt|;
block|}
name|PDTextStream
name|rt
init|=
name|getRichText
argument_list|()
decl_stmt|;
if|if
condition|(
name|rt
operator|!=
literal|null
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
literal|"<value-richtext>"
operator|+
name|rt
operator|.
name|getAsString
argument_list|()
operator|+
literal|"</value-richtext>\n"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|FDFField
argument_list|>
name|kids
init|=
name|getKids
argument_list|()
decl_stmt|;
if|if
condition|(
name|kids
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|FDFField
name|kid
range|:
name|kids
control|)
block|{
operator|(
operator|(
name|FDFField
operator|)
name|kid
operator|)
operator|.
name|writeXML
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
name|output
operator|.
name|write
argument_list|(
literal|"</field>\n"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|field
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|field
return|;
block|}
comment|/**      * This will get the list of kids.  This will return a list of FDFField objects.      * This will return null if the underlying list is null.      *      * @return The list of kids.      */
specifier|public
name|List
argument_list|<
name|FDFField
argument_list|>
name|getKids
parameter_list|()
block|{
name|COSArray
name|kids
init|=
operator|(
name|COSArray
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|FDFField
argument_list|>
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|kids
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|FDFField
argument_list|>
name|actuals
init|=
operator|new
name|ArrayList
argument_list|<
name|FDFField
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
name|kids
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|actuals
operator|.
name|add
argument_list|(
operator|new
name|FDFField
argument_list|(
operator|(
name|COSDictionary
operator|)
name|kids
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|actuals
argument_list|,
name|kids
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the list of kids.      *      * @param kids A list of FDFField objects.      */
specifier|public
name|void
name|setKids
parameter_list|(
name|List
argument_list|<
name|FDFField
argument_list|>
name|kids
parameter_list|)
block|{
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|KIDS
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|kids
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the "T" entry in the field dictionary.  A partial field      * name.  Where the fully qualified field name is a concatenation of      * the parent's fully qualified field name and "." as a separator.  For example<br/>      * Address.State<br />      * Address.City<br />      *      * @return The partial field name.      */
specifier|public
name|String
name|getPartialFieldName
parameter_list|()
block|{
return|return
name|field
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|T
argument_list|)
return|;
block|}
comment|/**      * This will set the partial field name.      *      * @param partial The partial field name.      */
specifier|public
name|void
name|setPartialFieldName
parameter_list|(
name|String
name|partial
parameter_list|)
block|{
name|field
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|T
argument_list|,
name|partial
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the value for the field.  This will return type will either be<br />      * String : Checkboxes, Radio Button<br />      * java.util.List of strings: Choice Field      * PDTextStream: Textfields      *      * @return The value of the field.      *      * @throws IOException If there is an error getting the value.      */
specifier|public
name|Object
name|getValue
parameter_list|()
throws|throws
name|IOException
block|{
name|Object
name|retval
init|=
literal|null
decl_stmt|;
name|COSBase
name|value
init|=
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|V
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|COSName
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSName
operator|)
name|value
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|COSArray
condition|)
block|{
name|retval
operator|=
name|COSArrayList
operator|.
name|convertCOSStringCOSArrayToList
argument_list|(
operator|(
name|COSArray
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|COSString
operator|||
name|value
operator|instanceof
name|COSStream
condition|)
block|{
name|retval
operator|=
name|PDTextStream
operator|.
name|createTextStream
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|//Ok, value is null so do nothing
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error:Unknown type for field import"
operator|+
name|value
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * You should pass in a string, or a java.util.List of strings to set the      * value.      *      * @param value The value that should populate when imported.      *      * @throws IOException If there is an error setting the value.      */
specifier|public
name|void
name|setValue
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|COSBase
name|cos
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|List
condition|)
block|{
name|cos
operator|=
name|COSArrayList
operator|.
name|convertStringListToCOSStringCOSArray
argument_list|(
operator|(
name|List
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|cos
operator|=
name|COSName
operator|.
name|getPDFName
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|COSObjectable
condition|)
block|{
name|cos
operator|=
operator|(
operator|(
name|COSObjectable
operator|)
name|value
operator|)
operator|.
name|getCOSObject
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
comment|//do nothing and let cos remain null as well.
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error:Unknown type for field import"
operator|+
name|value
argument_list|)
throw|;
block|}
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|cos
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the Ff entry of the cos dictionary.  If it it not present then      * this method will return null.      *      * @return The field flags.      */
specifier|public
name|Integer
name|getFieldFlags
parameter_list|()
block|{
name|Integer
name|retval
init|=
literal|null
decl_stmt|;
name|COSNumber
name|ff
init|=
operator|(
name|COSNumber
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|FF
argument_list|)
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|Integer
argument_list|(
name|ff
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the field flags that are associated with this field.  The Ff entry      * in the FDF field dictionary.      *      * @param ff The new value for the field flags.      */
specifier|public
name|void
name|setFieldFlags
parameter_list|(
name|Integer
name|ff
parameter_list|)
block|{
name|COSInteger
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|COSInteger
operator|.
name|get
argument_list|(
name|ff
argument_list|)
expr_stmt|;
block|}
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the field flags that are associated with this field.  The Ff entry      * in the FDF field dictionary.      *      * @param ff The new value for the field flags.      */
specifier|public
name|void
name|setFieldFlags
parameter_list|(
name|int
name|ff
parameter_list|)
block|{
name|field
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|FF
argument_list|,
name|ff
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the SetFf entry of the cos dictionary.  If it it not present then      * this method will return null.      *      * @return The field flags.      */
specifier|public
name|Integer
name|getSetFieldFlags
parameter_list|()
block|{
name|Integer
name|retval
init|=
literal|null
decl_stmt|;
name|COSNumber
name|ff
init|=
operator|(
name|COSNumber
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SET_FF
argument_list|)
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|Integer
argument_list|(
name|ff
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the field flags that are associated with this field.  The SetFf entry      * in the FDF field dictionary.      *      * @param ff The new value for the "set field flags".      */
specifier|public
name|void
name|setSetFieldFlags
parameter_list|(
name|Integer
name|ff
parameter_list|)
block|{
name|COSInteger
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|COSInteger
operator|.
name|get
argument_list|(
name|ff
argument_list|)
expr_stmt|;
block|}
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SET_FF
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the field flags that are associated with this field.  The SetFf entry      * in the FDF field dictionary.      *      * @param ff The new value for the "set field flags".      */
specifier|public
name|void
name|setSetFieldFlags
parameter_list|(
name|int
name|ff
parameter_list|)
block|{
name|field
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|SET_FF
argument_list|,
name|ff
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the ClrFf entry of the cos dictionary.  If it it not present then      * this method will return null.      *      * @return The field flags.      */
specifier|public
name|Integer
name|getClearFieldFlags
parameter_list|()
block|{
name|Integer
name|retval
init|=
literal|null
decl_stmt|;
name|COSNumber
name|ff
init|=
operator|(
name|COSNumber
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CLR_FF
argument_list|)
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|Integer
argument_list|(
name|ff
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the field flags that are associated with this field.  The ClrFf entry      * in the FDF field dictionary.      *      * @param ff The new value for the "clear field flags".      */
specifier|public
name|void
name|setClearFieldFlags
parameter_list|(
name|Integer
name|ff
parameter_list|)
block|{
name|COSInteger
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|COSInteger
operator|.
name|get
argument_list|(
name|ff
argument_list|)
expr_stmt|;
block|}
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CLR_FF
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the field flags that are associated with this field.  The ClrFf entry      * in the FDF field dictionary.      *      * @param ff The new value for the "clear field flags".      */
specifier|public
name|void
name|setClearFieldFlags
parameter_list|(
name|int
name|ff
parameter_list|)
block|{
name|field
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|CLR_FF
argument_list|,
name|ff
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the F entry of the cos dictionary.  If it it not present then      * this method will return null.      *      * @return The widget field flags.      */
specifier|public
name|Integer
name|getWidgetFieldFlags
parameter_list|()
block|{
name|Integer
name|retval
init|=
literal|null
decl_stmt|;
name|COSNumber
name|f
init|=
operator|(
name|COSNumber
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
literal|"F"
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|Integer
argument_list|(
name|f
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the widget field flags that are associated with this field.  The F entry      * in the FDF field dictionary.      *      * @param f The new value for the field flags.      */
specifier|public
name|void
name|setWidgetFieldFlags
parameter_list|(
name|Integer
name|f
parameter_list|)
block|{
name|COSInteger
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|COSInteger
operator|.
name|get
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the field flags that are associated with this field.  The F entry      * in the FDF field dictionary.      *      * @param f The new value for the field flags.      */
specifier|public
name|void
name|setWidgetFieldFlags
parameter_list|(
name|int
name|f
parameter_list|)
block|{
name|field
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|f
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the SetF entry of the cos dictionary.  If it it not present then      * this method will return null.      *      * @return The field flags.      */
specifier|public
name|Integer
name|getSetWidgetFieldFlags
parameter_list|()
block|{
name|Integer
name|retval
init|=
literal|null
decl_stmt|;
name|COSNumber
name|ff
init|=
operator|(
name|COSNumber
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SET_F
argument_list|)
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|Integer
argument_list|(
name|ff
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the widget field flags that are associated with this field.  The SetF entry      * in the FDF field dictionary.      *      * @param ff The new value for the "set widget field flags".      */
specifier|public
name|void
name|setSetWidgetFieldFlags
parameter_list|(
name|Integer
name|ff
parameter_list|)
block|{
name|COSInteger
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|COSInteger
operator|.
name|get
argument_list|(
name|ff
argument_list|)
expr_stmt|;
block|}
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SET_F
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the widget field flags that are associated with this field.  The SetF entry      * in the FDF field dictionary.      *      * @param ff The new value for the "set widget field flags".      */
specifier|public
name|void
name|setSetWidgetFieldFlags
parameter_list|(
name|int
name|ff
parameter_list|)
block|{
name|field
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|SET_F
argument_list|,
name|ff
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the ClrF entry of the cos dictionary.  If it it not present then      * this method will return null.      *      * @return The widget field flags.      */
specifier|public
name|Integer
name|getClearWidgetFieldFlags
parameter_list|()
block|{
name|Integer
name|retval
init|=
literal|null
decl_stmt|;
name|COSNumber
name|ff
init|=
operator|(
name|COSNumber
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CLR_F
argument_list|)
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|Integer
argument_list|(
name|ff
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the field flags that are associated with this field.  The ClrF entry      * in the FDF field dictionary.      *      * @param ff The new value for the "clear widget field flags".      */
specifier|public
name|void
name|setClearWidgetFieldFlags
parameter_list|(
name|Integer
name|ff
parameter_list|)
block|{
name|COSInteger
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ff
operator|!=
literal|null
condition|)
block|{
name|value
operator|=
name|COSInteger
operator|.
name|get
argument_list|(
name|ff
argument_list|)
expr_stmt|;
block|}
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CLR_F
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the field flags that are associated with this field.  The ClrF entry      * in the FDF field dictionary.      *      * @param ff The new value for the "clear field flags".      */
specifier|public
name|void
name|setClearWidgetFieldFlags
parameter_list|(
name|int
name|ff
parameter_list|)
block|{
name|field
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|CLR_F
argument_list|,
name|ff
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the appearance dictionary that specifies the appearance of      * a pushbutton field.      *      * @return The AP entry of this dictionary.      */
specifier|public
name|PDAppearanceDictionary
name|getAppearanceDictionary
parameter_list|()
block|{
name|PDAppearanceDictionary
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AP
argument_list|)
decl_stmt|;
if|if
condition|(
name|dict
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDAppearanceDictionary
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the appearance dictionary.      *      * @param ap The apperance dictionary.      */
specifier|public
name|void
name|setAppearanceDictionary
parameter_list|(
name|PDAppearanceDictionary
name|ap
parameter_list|)
block|{
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AP
argument_list|,
name|ap
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get named page references..      *      * @return The named page references.      */
specifier|public
name|FDFNamedPageReference
name|getAppearanceStreamReference
parameter_list|()
block|{
name|FDFNamedPageReference
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|ref
init|=
operator|(
name|COSDictionary
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AP_REF
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|FDFNamedPageReference
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the named page references.      *      * @param ref The named page references.      */
specifier|public
name|void
name|setAppearanceStreamReference
parameter_list|(
name|FDFNamedPageReference
name|ref
parameter_list|)
block|{
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AP_REF
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the icon fit that is associated with this field.      *      * @return The IF entry.      */
specifier|public
name|FDFIconFit
name|getIconFit
parameter_list|()
block|{
name|FDFIconFit
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
literal|"IF"
argument_list|)
decl_stmt|;
if|if
condition|(
name|dic
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|FDFIconFit
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the icon fit entry.      *      * @param fit The icon fit object.      */
specifier|public
name|void
name|setIconFit
parameter_list|(
name|FDFIconFit
name|fit
parameter_list|)
block|{
name|field
operator|.
name|setItem
argument_list|(
literal|"IF"
argument_list|,
name|fit
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will return a list of options for a choice field.  The value in the      * list will be 1 of 2 types.  java.lang.String or FDFOptionElement.      *      * @return A list of all options.      */
specifier|public
name|List
name|getOptions
parameter_list|()
block|{
name|List
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|List
name|objects
init|=
operator|new
name|ArrayList
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
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|COSBase
name|next
init|=
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|instanceof
name|COSString
condition|)
block|{
name|objects
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSString
operator|)
name|next
operator|)
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|COSArray
name|value
init|=
operator|(
name|COSArray
operator|)
name|next
decl_stmt|;
name|objects
operator|.
name|add
argument_list|(
operator|new
name|FDFOptionElement
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|retval
operator|=
operator|new
name|COSArrayList
argument_list|(
name|objects
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the options for the choice field.  The objects in the list      * should either be java.lang.String or FDFOptionElement.      *      * @param options The options to set.      */
specifier|public
name|void
name|setOptions
parameter_list|(
name|List
name|options
parameter_list|)
block|{
name|COSArray
name|value
init|=
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|options
argument_list|)
decl_stmt|;
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OPT
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the action that is associated with this field.      *      * @return The A entry in the field dictionary.      */
specifier|public
name|PDAction
name|getAction
parameter_list|()
block|{
return|return
name|PDActionFactory
operator|.
name|createAction
argument_list|(
operator|(
name|COSDictionary
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|A
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the action that is associated with this field.      *      * @param a The new action.      */
specifier|public
name|void
name|setAction
parameter_list|(
name|PDAction
name|a
parameter_list|)
block|{
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|A
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a list of additional actions that will get executed based      * on events.      *      * @return The AA entry in this field dictionary.      */
specifier|public
name|PDAdditionalActions
name|getAdditionalActions
parameter_list|()
block|{
name|PDAdditionalActions
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|dict
init|=
operator|(
name|COSDictionary
operator|)
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|AA
argument_list|)
decl_stmt|;
if|if
condition|(
name|dict
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDAdditionalActions
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the additional actions that are associated with this field.      *      * @param aa The additional actions.      */
specifier|public
name|void
name|setAdditionalActions
parameter_list|(
name|PDAdditionalActions
name|aa
parameter_list|)
block|{
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|AA
argument_list|,
name|aa
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the rich text that is associated with this field.      *      * @return The rich text XHTML stream.      */
specifier|public
name|PDTextStream
name|getRichText
parameter_list|()
block|{
name|COSBase
name|rv
init|=
name|field
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RV
argument_list|)
decl_stmt|;
return|return
name|PDTextStream
operator|.
name|createTextStream
argument_list|(
name|rv
argument_list|)
return|;
block|}
comment|/**      * This will set the rich text value.      *      * @param rv The rich text value for the stream.      */
specifier|public
name|void
name|setRichText
parameter_list|(
name|PDTextStream
name|rv
parameter_list|)
block|{
name|field
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RV
argument_list|,
name|rv
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

