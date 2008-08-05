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
name|util
package|;
end_package

begin_comment
comment|/**  * This class deals with some logging that is not handled by the log4j replacement.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|ErrorLogger
block|{
comment|/**      * Utility class, should not be instantiated.      *      */
specifier|private
name|ErrorLogger
parameter_list|()
block|{     }
comment|/**      * Log an error message.  This is only used for log4j replacement and      * should never be used when writing code.      *      * @param errorMessage The error message.      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|String
name|errorMessage
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
block|}
comment|/**      * Log an error message.  This is only used for log4j replacement and      * should never be used when writing code.      *      * @param errorMessage The error message.      * @param t The exception.      */
specifier|public
specifier|static
name|void
name|log
parameter_list|(
name|String
name|errorMessage
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

