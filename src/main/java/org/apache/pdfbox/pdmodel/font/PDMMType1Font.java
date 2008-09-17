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
name|font
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
comment|/**  * This is implementation of the Multiple Master Type1 Font.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|PDMMType1Font
extends|extends
name|PDSimpleFont
block|{
comment|/**      * Constructor.      */
specifier|public
name|PDMMType1Font
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|font
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
literal|"MMType1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param fontDictionary The font dictionary according to the PDF specification.      */
specifier|public
name|PDMMType1Font
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

