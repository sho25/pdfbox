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
name|filespecification
operator|.
name|PDFileSpecification
import|;
end_import

begin_comment
comment|/**  * This represents an FDF named page reference that is part of the FDF field.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|FDFNamedPageReference
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|final
name|COSDictionary
name|ref
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFNamedPageReference
parameter_list|()
block|{
name|ref
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param r The FDF named page reference dictionary.      */
specifier|public
name|FDFNamedPageReference
parameter_list|(
name|COSDictionary
name|r
parameter_list|)
block|{
name|ref
operator|=
name|r
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
comment|/**      * This will get the name of the referenced page. A required parameter.      *      * @return The name of the referenced page.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|ref
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|NAME
argument_list|)
return|;
block|}
comment|/**      * This will set the name of the referenced page.      *      * @param name The referenced page name.      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|ref
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
comment|/**      * This will get the file specification of this reference. An optional parameter.      *      * @return The F entry for this dictionary.      *      * @throws IOException If there is an error creating the file spec.      */
specifier|public
name|PDFileSpecification
name|getFileSpecification
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDFileSpecification
operator|.
name|createFS
argument_list|(
name|ref
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|F
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the file specification for this named page reference.      *      * @param fs The file specification to set.      */
specifier|public
name|void
name|setFileSpecification
parameter_list|(
name|PDFileSpecification
name|fs
parameter_list|)
block|{
name|ref
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|fs
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

