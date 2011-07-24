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
name|padaf
operator|.
name|xmpbox
operator|.
name|parser
package|;
end_package

begin_comment
comment|/**  * Exception thrown when problems occurs in Schema Treatment  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|XmpSchemaException
extends|extends
name|Exception
block|{
comment|/** 	 * serial version uid 	 */
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|980712488563404867L
decl_stmt|;
comment|/** 	 * Create an instance of XmpSchemaException 	 *  	 * @param message 	 *            a description of the encountered problem 	 */
specifier|public
name|XmpSchemaException
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
comment|/** 	 * Create an instance of XmpSchemaException 	 *  	 * @param message 	 *            a description of the encountered problem 	 * @param cause 	 *            the cause of the exception 	 */
specifier|public
name|XmpSchemaException
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
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

