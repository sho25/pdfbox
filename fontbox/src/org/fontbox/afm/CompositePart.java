begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|fontbox
operator|.
name|afm
package|;
end_package

begin_comment
comment|/**  * This class represents a part of composite character data.  *  * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|CompositePart
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|int
name|xDisplacement
decl_stmt|;
specifier|private
name|int
name|yDisplacement
decl_stmt|;
comment|/** Getter for property name.      * @return Value of property name.      */
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/** Setter for property name.      * @param nameValue New value of property name.      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|nameValue
parameter_list|)
block|{
name|name
operator|=
name|nameValue
expr_stmt|;
block|}
comment|/** Getter for property xDisplacement.      * @return Value of property xDisplacement.      */
specifier|public
name|int
name|getXDisplacement
parameter_list|()
block|{
return|return
name|xDisplacement
return|;
block|}
comment|/** Setter for property xDisplacement.      * @param xDisp New value of property xDisplacement.      */
specifier|public
name|void
name|setXDisplacement
parameter_list|(
name|int
name|xDisp
parameter_list|)
block|{
name|xDisplacement
operator|=
name|xDisp
expr_stmt|;
block|}
comment|/** Getter for property yDisplacement.      * @return Value of property yDisplacement.      */
specifier|public
name|int
name|getYDisplacement
parameter_list|()
block|{
return|return
name|yDisplacement
return|;
block|}
comment|/** Setter for property yDisplacement.      * @param yDisp New value of property yDisplacement.      */
specifier|public
name|void
name|setYDisplacement
parameter_list|(
name|int
name|yDisp
parameter_list|)
block|{
name|yDisplacement
operator|=
name|yDisp
expr_stmt|;
block|}
block|}
end_class

end_unit

