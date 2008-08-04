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
name|exceptions
package|;
end_package

begin_comment
comment|/**  * An exception that indicates that something has gone wrong during a  * cryptography operation.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
specifier|public
class|class
name|CryptographyException
extends|extends
name|Exception
block|{
specifier|private
name|Exception
name|embedded
decl_stmt|;
comment|/**      * Constructor.      *      * @param msg A msg to go with this exception.      */
specifier|public
name|CryptographyException
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param e The root exception that caused this exception.      */
specifier|public
name|CryptographyException
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|super
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|setEmbedded
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the exception that caused this exception.      *      * @return The embedded exception if one exists.      */
specifier|public
name|Exception
name|getEmbedded
parameter_list|()
block|{
return|return
name|embedded
return|;
block|}
comment|/**      * This will set the exception that caused this exception.      *      * @param e The sub exception.      */
specifier|private
name|void
name|setEmbedded
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|embedded
operator|=
name|e
expr_stmt|;
block|}
block|}
end_class

end_unit

