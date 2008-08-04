begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|cos
package|;
end_package

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|filter
operator|.
name|FilterManager
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

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|exceptions
operator|.
name|COSVisitorException
import|;
end_import

begin_comment
comment|/**  * The base object that all objects in the PDF document will extend.  *  * @author<a href="ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.14 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|COSBase
implements|implements
name|COSObjectable
block|{
comment|/**      * Constructor.      */
specifier|public
name|COSBase
parameter_list|()
block|{     }
comment|/**      * This will get the filter manager to use to filter streams.      *      * @return The filter manager.      */
specifier|public
name|FilterManager
name|getFilterManager
parameter_list|()
block|{
comment|/**          * @todo move this to PDFdocument or something better          */
return|return
operator|new
name|FilterManager
argument_list|()
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|this
return|;
block|}
comment|/**      * visitor pattern double dispatch method.      *      * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws COSVisitorException If an error occurs while visiting this object.      */
specifier|public
specifier|abstract
name|Object
name|accept
parameter_list|(
name|ICOSVisitor
name|visitor
parameter_list|)
throws|throws
name|COSVisitorException
function_decl|;
block|}
end_class

end_unit

