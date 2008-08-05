begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This represents an FDF template that is part of the FDF page.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|FDFTemplate
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|template
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFTemplate
parameter_list|()
block|{
name|template
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param t The FDF page template.      */
specifier|public
name|FDFTemplate
parameter_list|(
name|COSDictionary
name|t
parameter_list|)
block|{
name|template
operator|=
name|t
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|template
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|template
return|;
block|}
comment|/**      * This is the template reference.      *      * @return The template reference.      */
specifier|public
name|FDFNamedPageReference
name|getTemplateReference
parameter_list|()
block|{
name|FDFNamedPageReference
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
name|template
operator|.
name|getDictionaryObject
argument_list|(
literal|"TRef"
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
name|FDFNamedPageReference
argument_list|(
name|dict
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the template reference.      *      * @param tRef The template reference.      */
specifier|public
name|void
name|setTemplateReference
parameter_list|(
name|FDFNamedPageReference
name|tRef
parameter_list|)
block|{
name|template
operator|.
name|setItem
argument_list|(
literal|"TRef"
argument_list|,
name|tRef
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get a list of fields that are part of this template.      *      * @return A list of fields.      */
specifier|public
name|List
name|getFields
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
name|template
operator|.
name|getDictionaryObject
argument_list|(
literal|"Fields"
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
name|fields
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
name|fields
operator|.
name|add
argument_list|(
operator|new
name|FDFField
argument_list|(
operator|(
name|COSDictionary
operator|)
name|array
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
name|fields
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set a list of fields for this template.      *      * @param fields The list of fields to set for this template.      */
specifier|public
name|void
name|setFields
parameter_list|(
name|List
name|fields
parameter_list|)
block|{
name|template
operator|.
name|setItem
argument_list|(
literal|"Fields"
argument_list|,
name|COSArrayList
operator|.
name|converterToCOSArray
argument_list|(
name|fields
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * A flag telling if the fields imported from the template may be renamed if there are conflicts.      *      * @return A flag telling if the fields can be renamed.      */
specifier|public
name|boolean
name|shouldRename
parameter_list|()
block|{
return|return
name|template
operator|.
name|getBoolean
argument_list|(
literal|"Rename"
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * This will set if the fields can be renamed.      *      * @param value The flag value.      */
specifier|public
name|void
name|setRename
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|template
operator|.
name|setBoolean
argument_list|(
literal|"Rename"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

