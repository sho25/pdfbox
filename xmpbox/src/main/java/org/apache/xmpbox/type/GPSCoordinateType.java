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
name|xmpbox
operator|.
name|type
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_class
specifier|public
class|class
name|GPSCoordinateType
extends|extends
name|TextType
block|{
specifier|public
name|GPSCoordinateType
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|namespaceURI
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|namespaceURI
argument_list|,
name|prefix
argument_list|,
name|propertyName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

