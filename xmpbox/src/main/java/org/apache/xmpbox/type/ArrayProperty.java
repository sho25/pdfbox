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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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

begin_comment
comment|/**  * Object representation of a Complex XMP Property (Represents Ordered, Unordered and Alternative Arrays builder)  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|ArrayProperty
extends|extends
name|AbstractComplexProperty
block|{
specifier|private
specifier|final
name|Cardinality
name|arrayType
decl_stmt|;
specifier|private
specifier|final
name|String
name|namespace
decl_stmt|;
specifier|private
specifier|final
name|String
name|prefix
decl_stmt|;
comment|/**      * Constructor of a complex property      *       * @param metadata      *            The metadata to attach to this property      * @param namespace      *            The namespace URI to associate to this property      * @param prefix      *            The prefix to set for this property      * @param propertyName      *            The local Name of this property      * @param type      *            type of complexProperty (Bag, Seq, Alt)      */
specifier|public
name|ArrayProperty
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|prefix
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Cardinality
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|propertyName
argument_list|)
expr_stmt|;
name|this
operator|.
name|arrayType
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
block|}
specifier|public
name|Cardinality
name|getArrayType
parameter_list|()
block|{
return|return
name|arrayType
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getElementsAsString
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|retval
decl_stmt|;
name|retval
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|Iterator
argument_list|<
name|AbstractField
argument_list|>
name|it
init|=
name|getContainer
argument_list|()
operator|.
name|getAllProperties
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|AbstractSimpleProperty
name|tmp
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|tmp
operator|=
operator|(
name|AbstractSimpleProperty
operator|)
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
name|retval
operator|.
name|add
argument_list|(
name|tmp
operator|.
name|getStringValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|retval
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Get the namespace URI of this entity      *       * @return the namespace URI      */
annotation|@
name|Override
specifier|public
specifier|final
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
comment|/**      * Get the prefix of this entity      *       * @return the prefix specified      */
annotation|@
name|Override
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
block|}
end_class

end_unit

