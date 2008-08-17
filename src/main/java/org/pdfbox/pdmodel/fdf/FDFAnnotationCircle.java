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
name|io
operator|.
name|IOException
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
name|cos
operator|.
name|COSName
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

begin_comment
comment|/**  * This represents a Circle FDF annotation.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|FDFAnnotationCircle
extends|extends
name|FDFAnnotation
block|{
comment|/**      * COS Model value for SubType entry.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUBTYPE
init|=
literal|"Circle"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFAnnotationCircle
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|annot
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUBTYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor      *      * @param a An existing FDF Annotation.      */
specifier|public
name|FDFAnnotationCircle
parameter_list|(
name|COSDictionary
name|a
parameter_list|)
block|{
name|super
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      *  @param element An XFDF element.      *      *  @throws IOException If there is an error extracting information from the element.      */
specifier|public
name|FDFAnnotationCircle
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|annot
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUBTYPE
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

