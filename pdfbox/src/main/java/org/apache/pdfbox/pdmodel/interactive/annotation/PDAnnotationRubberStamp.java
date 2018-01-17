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
name|interactive
operator|.
name|annotation
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

begin_comment
comment|/**  * This is the class that represents a rubber stamp annotation. Introduced in PDF 1.3 specification  *  * @author Paul King  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationRubberStamp
extends|extends
name|PDAnnotationMarkup
block|{
comment|/*      * The various values of the rubber stamp as defined in the PDF 1.6 reference Table 8.28      */
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_APPROVED
init|=
literal|"Approved"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_EXPERIMENTAL
init|=
literal|"Experimental"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_NOT_APPROVED
init|=
literal|"NotApproved"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_AS_IS
init|=
literal|"AsIs"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_EXPIRED
init|=
literal|"Expired"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_NOT_FOR_PUBLIC_RELEASE
init|=
literal|"NotForPublicRelease"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_FOR_PUBLIC_RELEASE
init|=
literal|"ForPublicRelease"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_DRAFT
init|=
literal|"Draft"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_FOR_COMMENT
init|=
literal|"ForComment"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_TOP_SECRET
init|=
literal|"TopSecret"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_DEPARTMENTAL
init|=
literal|"Departmental"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_CONFIDENTIAL
init|=
literal|"Confidential"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_FINAL
init|=
literal|"Final"
decl_stmt|;
comment|/**      * Constant for the name of a rubber stamp.      */
specifier|public
specifier|static
specifier|final
name|String
name|NAME_SOLD
init|=
literal|"Sold"
decl_stmt|;
comment|/**      * The type of annotation.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"Stamp"
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDAnnotationRubberStamp
parameter_list|()
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Rubber Stamp annotation from a COSDictionary, expected to be a correct object definition.      *      * @param field the PDF object to represent as a field.      */
specifier|public
name|PDAnnotationRubberStamp
parameter_list|(
name|COSDictionary
name|field
parameter_list|)
block|{
name|super
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the name (and hence appearance, AP taking precedence) For this annotation. See the NAME_XXX      * constants for valid values.      *      * @param name The name of the rubber stamp.      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the name (and hence appearance, AP taking precedence) For this annotation. The default is      * DRAFT.      *      * @return The name of this rubber stamp, see the NAME_XXX constants.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getCOSObject
argument_list|()
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|,
name|NAME_DRAFT
argument_list|)
return|;
block|}
block|}
end_class

end_unit

