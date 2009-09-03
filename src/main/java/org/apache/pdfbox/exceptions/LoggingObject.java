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
name|exceptions
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Implementation of base object to help with error-handling.  *  * @author<a href="mailto:DanielWilson@users.sourceforge.net">Daniel Wilson</a>  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|LoggingObject
block|{
specifier|private
specifier|static
name|Log
name|logger
decl_stmt|;
comment|//dwilson 3/15/07
comment|/**      * Returns the main logger instance.      * @return the logger instance      */
specifier|protected
name|Log
name|logger
parameter_list|()
comment|//dwilson 3/15/07
block|{
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|logger
operator|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|logger
return|;
block|}
block|}
end_class

end_unit

