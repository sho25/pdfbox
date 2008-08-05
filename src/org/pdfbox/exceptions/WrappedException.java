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
name|exceptions
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_comment
comment|/**  * An exception that that holds a sub exception.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|WrappedException
extends|extends
name|Exception
block|{
specifier|private
name|Exception
name|wrapped
init|=
literal|null
decl_stmt|;
comment|/**      * constructor comment.      *      * @param e The root exception that caused this exception.      */
specifier|public
name|WrappedException
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|wrapped
operator|=
name|e
expr_stmt|;
block|}
comment|/**      * Gets the wrapped exception message.      *      * @return A message indicating the exception.      */
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|wrapped
operator|.
name|getMessage
argument_list|()
return|;
block|}
comment|/**      * Prints this throwable and its backtrace to the specified print stream.      *      * @param s<code>PrintStream</code> to use for output      */
specifier|public
name|void
name|printStackTrace
parameter_list|(
name|PrintStream
name|s
parameter_list|)
block|{
name|super
operator|.
name|printStackTrace
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|wrapped
operator|.
name|printStackTrace
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

