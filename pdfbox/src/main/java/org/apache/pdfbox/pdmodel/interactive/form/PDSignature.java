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
name|form
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * A class for handling the PDF field as a signature.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.5 $  *   * @deprecated Use {@link PDSignatureField} instead (see PDFBOX-1513).  */
end_comment

begin_class
specifier|public
class|class
name|PDSignature
extends|extends
name|PDField
block|{
comment|/**      * @see PDField#PDField(PDAcroForm,COSDictionary)      *      * @param theAcroForm The acroForm for this field.      * @param field The dictionary for the signature.      */
specifier|public
name|PDSignature
parameter_list|(
name|PDAcroForm
name|theAcroForm
parameter_list|,
name|COSDictionary
name|field
parameter_list|)
block|{
name|super
argument_list|(
name|theAcroForm
argument_list|,
name|field
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"The usage of "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" is deprecated. Please use "
operator|+
name|PDSignatureField
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" instead (see PDFBOX-1513)"
argument_list|)
throw|;
block|}
comment|/**      * @see PDField#setValue(java.lang.String)      *      * @param value The new value for the field.      *      * @throws IOException If there is an error creating the appearance stream.      */
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Not yet implemented"
argument_list|)
throw|;
block|}
comment|/**      * @see PDField#setValue(java.lang.String)      *      * @return The string value of this field.      *      * @throws IOException If there is an error creating the appearance stream.      */
specifier|public
name|String
name|getValue
parameter_list|()
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Not yet implemented"
argument_list|)
throw|;
block|}
comment|/**      * Return a string rep of this object.      *      * @return A string rep of this object.      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"PDSignature"
return|;
block|}
block|}
end_class

end_unit

