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

begin_comment
comment|/**  * This represents an object that can be used in a Field's Opt entry to represent an available option and a default  * appearance string.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|FDFOptionElement
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|final
name|COSArray
name|option
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFOptionElement
parameter_list|()
block|{
name|option
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|option
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|option
operator|.
name|add
argument_list|(
operator|new
name|COSString
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param o The option element.      */
specifier|public
name|FDFOptionElement
parameter_list|(
name|COSArray
name|o
parameter_list|)
block|{
name|option
operator|=
name|o
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|option
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSArray
name|getCOSArray
parameter_list|()
block|{
return|return
name|option
return|;
block|}
comment|/**      * This will get the string of one of the available options. A required element.      *      * @return An available option.      */
specifier|public
name|String
name|getOption
parameter_list|()
block|{
return|return
operator|(
operator|(
name|COSString
operator|)
name|option
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|getString
argument_list|()
return|;
block|}
comment|/**      * This will set the string for an available option.      *      * @param opt One of the available options.      */
specifier|public
name|void
name|setOption
parameter_list|(
name|String
name|opt
parameter_list|)
block|{
name|option
operator|.
name|set
argument_list|(
literal|0
argument_list|,
operator|new
name|COSString
argument_list|(
name|opt
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the string of default appearance string. A required element.      *      * @return A default appearance string.      */
specifier|public
name|String
name|getDefaultAppearanceString
parameter_list|()
block|{
return|return
operator|(
operator|(
name|COSString
operator|)
name|option
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|getString
argument_list|()
return|;
block|}
comment|/**      * This will set the default appearance string.      *      * @param da The default appearance string.      */
specifier|public
name|void
name|setDefaultAppearanceString
parameter_list|(
name|String
name|da
parameter_list|)
block|{
name|option
operator|.
name|set
argument_list|(
literal|1
argument_list|,
operator|new
name|COSString
argument_list|(
name|da
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

