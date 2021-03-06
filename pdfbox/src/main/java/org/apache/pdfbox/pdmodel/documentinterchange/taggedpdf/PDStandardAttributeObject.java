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
name|documentinterchange
operator|.
name|taggedpdf
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
name|documentinterchange
operator|.
name|logicalstructure
operator|.
name|PDAttributeObject
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
name|graphics
operator|.
name|color
operator|.
name|PDGamma
import|;
end_import

begin_comment
comment|/**  * A standard attribute object.  *   * @author Johannes Koch  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDStandardAttributeObject
extends|extends
name|PDAttributeObject
block|{
comment|/**      * An "unspecified" default float value.      */
specifier|protected
specifier|static
specifier|final
name|float
name|UNSPECIFIED
init|=
operator|-
literal|1.f
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDStandardAttributeObject
parameter_list|()
block|{     }
comment|/**      * Creates a new standard attribute object with a given dictionary.      *       * @param dictionary the dictionary      */
specifier|public
name|PDStandardAttributeObject
parameter_list|(
name|COSDictionary
name|dictionary
parameter_list|)
block|{
name|super
argument_list|(
name|dictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * Is the attribute with the given name specified in this attribute object?      *       * @param name the attribute name      * @return<code>true</code> if the attribute is specified,      *<code>false</code> otherwise      */
specifier|public
name|boolean
name|isSpecified
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
operator|!=
literal|null
return|;
block|}
comment|/**      * Gets a string attribute value.      *       * @param name the attribute name      * @return the string attribute value      */
specifier|protected
name|String
name|getString
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Sets a string attribute value.      *       * @param name the attribute name      * @param value the string attribute value      */
specifier|protected
name|void
name|setString
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|COSBase
name|oldBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setString
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|COSBase
name|newBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|potentiallyNotifyChanged
argument_list|(
name|oldBase
argument_list|,
name|newBase
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets an array of strings.      *       * @param name the attribute name      * @return the array of strings      */
specifier|protected
name|String
index|[]
name|getArrayOfString
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|COSBase
name|v
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
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
name|v
decl_stmt|;
name|String
index|[]
name|strings
init|=
operator|new
name|String
index|[
name|array
operator|.
name|size
argument_list|()
index|]
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
name|strings
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|COSName
operator|)
name|array
operator|.
name|getObject
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|strings
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets an array of strings.      *       * @param name the attribute name      * @param values the array of strings      */
specifier|protected
name|void
name|setArrayOfString
parameter_list|(
name|String
name|name
parameter_list|,
name|String
index|[]
name|values
parameter_list|)
block|{
name|COSBase
name|oldBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|name
argument_list|,
name|array
argument_list|)
expr_stmt|;
name|COSBase
name|newBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|potentiallyNotifyChanged
argument_list|(
name|oldBase
argument_list|,
name|newBase
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets a name value.      *       * @param name the attribute name      * @return the name value      */
specifier|protected
name|String
name|getName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Gets a name value.      *       * @param name the attribute name      * @param defaultValue the default value      * @return the name value      */
specifier|protected
name|String
name|getName
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
comment|/**      * Gets a name value or array of name values.      *       * @param name the attribute name      * @param defaultValue the default value      * @return a String or array of Strings      */
specifier|protected
name|Object
name|getNameOrArrayOfName
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|COSBase
name|v
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
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
name|v
decl_stmt|;
name|String
index|[]
name|names
init|=
operator|new
name|String
index|[
name|array
operator|.
name|size
argument_list|()
index|]
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
name|item
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
name|item
operator|instanceof
name|COSName
condition|)
block|{
name|names
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|COSName
operator|)
name|item
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|names
return|;
block|}
if|if
condition|(
name|v
operator|instanceof
name|COSName
condition|)
block|{
return|return
operator|(
operator|(
name|COSName
operator|)
name|v
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
name|defaultValue
return|;
block|}
comment|/**      * Sets a name value.      *       * @param name the attribute name      * @param value the name value      */
specifier|protected
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|COSBase
name|oldBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|COSBase
name|newBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|potentiallyNotifyChanged
argument_list|(
name|oldBase
argument_list|,
name|newBase
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets an array of name values.      *       * @param name the attribute name      * @param values the array of name values      */
specifier|protected
name|void
name|setArrayOfName
parameter_list|(
name|String
name|name
parameter_list|,
name|String
index|[]
name|values
parameter_list|)
block|{
name|COSBase
name|oldBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
name|array
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|name
argument_list|,
name|array
argument_list|)
expr_stmt|;
name|COSBase
name|newBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|potentiallyNotifyChanged
argument_list|(
name|oldBase
argument_list|,
name|newBase
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets a number or a name value.      *       * @param name the attribute name      * @param defaultValue the default name      * @return a Float or a String      */
specifier|protected
name|Object
name|getNumberOrName
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
name|COSBase
name|value
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|COSNumber
condition|)
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|value
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|COSName
condition|)
block|{
return|return
operator|(
operator|(
name|COSName
operator|)
name|value
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
return|return
name|defaultValue
return|;
block|}
comment|/**      * Gets an integer.      *       * @param name the attribute name      * @param defaultValue the default value      * @return the integer      */
specifier|protected
name|int
name|getInteger
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getInt
argument_list|(
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
comment|/**      * Sets an integer.      *       * @param name the attribute name      * @param value the integer      */
specifier|protected
name|void
name|setInteger
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
name|COSBase
name|oldBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|COSBase
name|newBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|potentiallyNotifyChanged
argument_list|(
name|oldBase
argument_list|,
name|newBase
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets a number value.      *       * @param name the attribute name      * @param defaultValue the default value      * @return the number value      */
specifier|protected
name|float
name|getNumber
parameter_list|(
name|String
name|name
parameter_list|,
name|float
name|defaultValue
parameter_list|)
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getFloat
argument_list|(
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
comment|/**      * Gets a number value.      *       * @param name the attribute name      * @return the number value      */
specifier|protected
name|float
name|getNumber
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getFloat
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Gets a number or an array of numbers.      *       * @param name the attribute name      * @param defaultValue the default value      * @return a Float or an array of floats      */
specifier|protected
name|Object
name|getNumberOrArrayOfNumber
parameter_list|(
name|String
name|name
parameter_list|,
name|float
name|defaultValue
parameter_list|)
block|{
name|COSBase
name|v
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|v
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
name|v
decl_stmt|;
name|float
index|[]
name|values
init|=
operator|new
name|float
index|[
name|array
operator|.
name|size
argument_list|()
index|]
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
name|item
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
name|item
operator|instanceof
name|COSNumber
condition|)
block|{
name|values
index|[
name|i
index|]
operator|=
operator|(
operator|(
name|COSNumber
operator|)
name|item
operator|)
operator|.
name|floatValue
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|values
return|;
block|}
if|if
condition|(
name|v
operator|instanceof
name|COSNumber
condition|)
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|v
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
if|if
condition|(
name|Float
operator|.
name|compare
argument_list|(
name|defaultValue
argument_list|,
name|UNSPECIFIED
argument_list|)
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|defaultValue
return|;
block|}
comment|/**      * Sets a float number.      *       * @param name the attribute name      * @param value the float number      */
specifier|protected
name|void
name|setNumber
parameter_list|(
name|String
name|name
parameter_list|,
name|float
name|value
parameter_list|)
block|{
name|COSBase
name|oldBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setFloat
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|COSBase
name|newBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|potentiallyNotifyChanged
argument_list|(
name|oldBase
argument_list|,
name|newBase
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets an integer number.      *       * @param name the attribute name      * @param value the integer number      */
specifier|protected
name|void
name|setNumber
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
name|COSBase
name|oldBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setInt
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|COSBase
name|newBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|potentiallyNotifyChanged
argument_list|(
name|oldBase
argument_list|,
name|newBase
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets an array of float numbers.      *       * @param name the attribute name      * @param values the float numbers      */
specifier|protected
name|void
name|setArrayOfNumber
parameter_list|(
name|String
name|name
parameter_list|,
name|float
index|[]
name|values
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|float
name|value
range|:
name|values
control|)
block|{
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|COSBase
name|oldBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|name
argument_list|,
name|array
argument_list|)
expr_stmt|;
name|COSBase
name|newBase
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|potentiallyNotifyChanged
argument_list|(
name|oldBase
argument_list|,
name|newBase
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets a colour.      *       * @param name the attribute name      * @return the colour      */
specifier|protected
name|PDGamma
name|getColor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|COSArray
name|c
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDGamma
argument_list|(
name|c
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Gets a single colour or four colours.      *       * @param name the attribute name      * @return the single ({@link PDGamma}) or a ({@link PDFourColours})      */
specifier|protected
name|Object
name|getColorOrFourColors
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|==
literal|3
condition|)
block|{
comment|// only one colour
return|return
operator|new
name|PDGamma
argument_list|(
name|array
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|array
operator|.
name|size
argument_list|()
operator|==
literal|4
condition|)
block|{
return|return
operator|new
name|PDFourColours
argument_list|(
name|array
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets a colour.      *       * @param name the attribute name      * @param value the colour      */
specifier|protected
name|void
name|setColor
parameter_list|(
name|String
name|name
parameter_list|,
name|PDGamma
name|value
parameter_list|)
block|{
name|COSBase
name|oldValue
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|COSBase
name|newValue
init|=
name|value
operator|==
literal|null
condition|?
literal|null
else|:
name|value
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|this
operator|.
name|potentiallyNotifyChanged
argument_list|(
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets four colours.      *       * @param name the attribute name      * @param value the four colours      */
specifier|protected
name|void
name|setFourColors
parameter_list|(
name|String
name|name
parameter_list|,
name|PDFourColours
name|value
parameter_list|)
block|{
name|COSBase
name|oldValue
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|COSBase
name|newValue
init|=
name|value
operator|==
literal|null
condition|?
literal|null
else|:
name|value
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|this
operator|.
name|potentiallyNotifyChanged
argument_list|(
name|oldValue
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

