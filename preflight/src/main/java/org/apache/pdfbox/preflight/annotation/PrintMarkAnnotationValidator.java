begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
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
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationUnknown
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
name|preflight
operator|.
name|PreflightContext
import|;
end_import

begin_comment
comment|/**  * Validation class for the PopupAnnotation  */
end_comment

begin_class
specifier|public
class|class
name|PrintMarkAnnotationValidator
extends|extends
name|AnnotationValidator
block|{
comment|/**      * PDFBox object which wraps the annotation dictionary      */
specifier|protected
name|PDAnnotationUnknown
name|pdUnk
init|=
literal|null
decl_stmt|;
specifier|public
name|PrintMarkAnnotationValidator
parameter_list|(
name|PreflightContext
name|ctx
parameter_list|,
name|COSDictionary
name|annotDictionary
parameter_list|)
block|{
name|super
argument_list|(
name|ctx
argument_list|,
name|annotDictionary
argument_list|)
expr_stmt|;
name|this
operator|.
name|pdUnk
operator|=
operator|new
name|PDAnnotationUnknown
argument_list|(
name|annotDictionary
argument_list|)
expr_stmt|;
name|this
operator|.
name|pdAnnot
operator|=
name|this
operator|.
name|pdUnk
expr_stmt|;
block|}
block|}
end_class

end_unit

