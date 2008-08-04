begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
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

begin_comment
comment|/**  * This is the class that represents a rubber stamp annotation.  * Introduced in PDF 1.3 specification  *   * @author Paul King  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PDAnnotationRubberStamp
extends|extends
name|PDAnnotationMarkup
block|{
comment|/*      * The various values of the rubber stamp as defined in       * the PDF 1.6 reference Table 8.28      */
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
name|super
argument_list|()
expr_stmt|;
name|getDictionary
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|COSName
operator|.
name|getPDFName
argument_list|(
name|SUB_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Rubber Stamp annotation from a COSDictionary, expected to be      * a correct object definition.      *      * @param field the PDF objet to represent as a field.      */
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
comment|/**      * This will set the name (and hence appearance, AP taking precedence)      * For this annotation.   See the NAME_XXX constants for valid values.      *      * @param name The name of the rubber stamp.      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|getDictionary
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
comment|/**      * This will retrieve the name (and hence appearance, AP taking precedence)      * For this annotation.  The default is DRAFT.      *       * @return The name of this rubber stamp, see the NAME_XXX constants.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getDictionary
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

