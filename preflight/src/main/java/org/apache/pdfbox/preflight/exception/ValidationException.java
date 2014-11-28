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
name|exception
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

begin_class
specifier|public
class|class
name|ValidationException
extends|extends
name|IOException
block|{
comment|/**      * serial version number      */
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|1616141241190424669L
decl_stmt|;
specifier|protected
name|Integer
name|pageNumber
init|=
literal|null
decl_stmt|;
specifier|public
name|ValidationException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|,
name|Integer
name|pageNumber
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|pageNumber
operator|=
name|pageNumber
expr_stmt|;
block|}
specifier|public
name|ValidationException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ValidationException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ValidationException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|initCause
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the page number related to the exception, or null if not known.      */
specifier|public
name|Integer
name|getPageNumber
parameter_list|()
block|{
return|return
name|pageNumber
return|;
block|}
block|}
end_class

end_unit

