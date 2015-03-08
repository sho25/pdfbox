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
name|cos
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

begin_comment
comment|/**  * An interface for visiting a PDF document at the type (COS) level.  *  * @author Michael Traut  */
end_comment

begin_interface
specifier|public
interface|interface
name|ICOSVisitor
block|{
comment|/**      * Notification of visit to Array object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws IOException If there is an error while visiting this object.      */
name|Object
name|visitFromArray
parameter_list|(
name|COSArray
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Notification of visit to boolean object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws IOException If there is an error while visiting this object.      */
name|Object
name|visitFromBoolean
parameter_list|(
name|COSBoolean
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Notification of visit to dictionary object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws IOException If there is an error while visiting this object.      */
name|Object
name|visitFromDictionary
parameter_list|(
name|COSDictionary
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Notification of visit to document object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws IOException If there is an error while visiting this object.      */
name|Object
name|visitFromDocument
parameter_list|(
name|COSDocument
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Notification of visit to float object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws IOException If there is an error while visiting this object.      */
name|Object
name|visitFromFloat
parameter_list|(
name|COSFloat
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Notification of visit to integer object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws IOException If there is an error while visiting this object.      */
name|Object
name|visitFromInt
parameter_list|(
name|COSInteger
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Notification of visit to name object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws IOException If there is an error while visiting this object.      */
name|Object
name|visitFromName
parameter_list|(
name|COSName
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Notification of visit to null object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws IOException If there is an error while visiting this object.      */
name|Object
name|visitFromNull
parameter_list|(
name|COSNull
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Notification of visit to stream object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws IOException If there is an error while visiting this object.      */
name|Object
name|visitFromStream
parameter_list|(
name|COSStream
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Notification of visit to string object.      *      * @param obj The Object that is being visited.      * @return any Object depending on the visitor implementation, or null      * @throws IOException If there is an error while visiting this object.      */
name|Object
name|visitFromString
parameter_list|(
name|COSString
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

