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
name|graphics
operator|.
name|optionalcontent
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
name|documentinterchange
operator|.
name|markedcontent
operator|.
name|PDPropertyList
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
name|rendering
operator|.
name|RenderDestination
import|;
end_import

begin_comment
comment|/**  * An optional content group (OCG).  */
end_comment

begin_class
specifier|public
class|class
name|PDOptionalContentGroup
extends|extends
name|PDPropertyList
block|{
comment|/**      * Creates a new optional content group (OCG).      * @param name the name of the content group      */
specifier|public
name|PDOptionalContentGroup
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|dict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|OCG
argument_list|)
expr_stmt|;
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new instance based on a given {@link COSDictionary}.      * @param dict the dictionary      */
specifier|public
name|PDOptionalContentGroup
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|super
argument_list|(
name|dict
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dict
operator|.
name|getItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
operator|.
name|equals
argument_list|(
name|COSName
operator|.
name|OCG
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Provided dictionary is not of type '"
operator|+
name|COSName
operator|.
name|OCG
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Enumeration for the renderState dictionary entry on the "Export", "View"      * and "Print" dictionary.      */
specifier|public
enum|enum
name|RenderState
block|{
comment|/** The "ON" value. */
name|ON
parameter_list|(
name|COSName
operator|.
name|ON
parameter_list|)
operator|,
comment|/** The "OFF" value. */
constructor|OFF(COSName.OFF
block|)
enum|;
specifier|private
specifier|final
name|COSName
name|name
decl_stmt|;
specifier|private
name|RenderState
parameter_list|(
name|COSName
name|value
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|value
expr_stmt|;
block|}
comment|/**          * Returns the base state represented by the given {@link COSName}.          *          * @param state the state name          * @return the state enum value          */
specifier|public
specifier|static
name|RenderState
name|valueOf
parameter_list|(
name|COSName
name|state
parameter_list|)
block|{
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|RenderState
operator|.
name|valueOf
argument_list|(
name|state
operator|.
name|getName
argument_list|()
operator|.
name|toUpperCase
argument_list|()
argument_list|)
return|;
block|}
comment|/**          * Returns the PDF name for the state.          *          * @return the name of the state          */
specifier|public
name|COSName
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
block|}
end_class

begin_comment
comment|/**      * Returns the name of the optional content group.      * @return the name      */
end_comment

begin_function
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|dict
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
return|;
block|}
end_function

begin_comment
comment|/**      * Sets the name of the optional content group.      * @param name the name      */
end_comment

begin_function
specifier|public
specifier|final
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|dict
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
end_function

begin_comment
comment|//TODO Add support for "Intent"
end_comment

begin_comment
comment|/**      * @param destination to be rendered      * @return state or null if undefined      */
end_comment

begin_function
specifier|public
name|RenderState
name|getRenderState
parameter_list|(
name|RenderDestination
name|destination
parameter_list|)
block|{
name|COSName
name|state
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|usage
init|=
operator|(
name|COSDictionary
operator|)
name|dict
operator|.
name|getDictionaryObject
argument_list|(
literal|"Usage"
argument_list|)
decl_stmt|;
if|if
condition|(
name|usage
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|RenderDestination
operator|.
name|PRINT
operator|.
name|equals
argument_list|(
name|destination
argument_list|)
condition|)
block|{
name|COSDictionary
name|print
init|=
operator|(
name|COSDictionary
operator|)
name|usage
operator|.
name|getDictionaryObject
argument_list|(
literal|"Print"
argument_list|)
decl_stmt|;
name|state
operator|=
name|print
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
name|COSName
operator|)
name|print
operator|.
name|getDictionaryObject
argument_list|(
literal|"PrintState"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|RenderDestination
operator|.
name|VIEW
operator|.
name|equals
argument_list|(
name|destination
argument_list|)
condition|)
block|{
name|COSDictionary
name|view
init|=
operator|(
name|COSDictionary
operator|)
name|usage
operator|.
name|getDictionaryObject
argument_list|(
literal|"View"
argument_list|)
decl_stmt|;
name|state
operator|=
name|view
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
name|COSName
operator|)
name|view
operator|.
name|getDictionaryObject
argument_list|(
literal|"ViewState"
argument_list|)
expr_stmt|;
block|}
comment|// Fallback to export
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|export
init|=
operator|(
name|COSDictionary
operator|)
name|usage
operator|.
name|getDictionaryObject
argument_list|(
literal|"Export"
argument_list|)
decl_stmt|;
name|state
operator|=
name|export
operator|==
literal|null
condition|?
literal|null
else|:
operator|(
name|COSName
operator|)
name|export
operator|.
name|getDictionaryObject
argument_list|(
literal|"ExportState"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|state
operator|==
literal|null
condition|?
literal|null
else|:
name|RenderState
operator|.
name|valueOf
argument_list|(
name|state
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
literal|" ("
operator|+
name|getName
argument_list|()
operator|+
literal|")"
return|;
block|}
end_function

unit|}
end_unit

