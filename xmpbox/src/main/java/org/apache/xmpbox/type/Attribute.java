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

begin_comment
comment|/**  * Simple representation of an attribute  *   * @author a183132  *   */
end_comment

begin_class
specifier|public
class|class
name|Attribute
block|{
specifier|private
name|String
name|nsURI
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|value
decl_stmt|;
comment|/**      * Constructor of a new Attribute      *       * @param nsURI      *            namespaceURI of this attribute (could be null)      * @param localName      *            localName of this attribute      * @param value      *            value given to this attribute      */
specifier|public
name|Attribute
parameter_list|(
name|String
name|nsURI
parameter_list|,
name|String
name|localName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|nsURI
operator|=
name|nsURI
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|localName
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Get the localName of this attribute      *       * @return local name of this attribute      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Set the localName of this attribute      *       * @param lname      *            the local name to set      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|lname
parameter_list|)
block|{
name|name
operator|=
name|lname
expr_stmt|;
block|}
comment|/**      * Get the namespace URI of this attribute      *       * @return the namespace URI associated to this attribute (could be null)      */
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|nsURI
return|;
block|}
comment|/**      * Set the namespace URI of this attribute      *       * @param nsURI      *            the namespace URI to set      */
specifier|public
name|void
name|setNsURI
parameter_list|(
name|String
name|nsURI
parameter_list|)
block|{
name|this
operator|.
name|nsURI
operator|=
name|nsURI
expr_stmt|;
block|}
comment|/**      * Get value of this attribute      *       * @return value of this attribute      */
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * Set value of this attribute      *       * @param value      *            the value to set for this attribute      */
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|80
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"[attr:{"
argument_list|)
operator|.
name|append
argument_list|(
name|nsURI
argument_list|)
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

