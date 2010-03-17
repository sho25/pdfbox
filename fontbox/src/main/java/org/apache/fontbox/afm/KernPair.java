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
name|fontbox
operator|.
name|afm
package|;
end_package

begin_comment
comment|/**  * This represents some kern pair data.  *  * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|KernPair
block|{
specifier|private
name|String
name|firstKernCharacter
decl_stmt|;
specifier|private
name|String
name|secondKernCharacter
decl_stmt|;
specifier|private
name|float
name|x
decl_stmt|;
specifier|private
name|float
name|y
decl_stmt|;
comment|/** Getter for property firstKernCharacter.      * @return Value of property firstKernCharacter.      */
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|getFirstKernCharacter
parameter_list|()
block|{
return|return
name|firstKernCharacter
return|;
block|}
comment|/** Setter for property firstKernCharacter.      * @param firstKernCharacterValue New value of property firstKernCharacter.      */
specifier|public
name|void
name|setFirstKernCharacter
parameter_list|(
name|String
name|firstKernCharacterValue
parameter_list|)
block|{
name|firstKernCharacter
operator|=
name|firstKernCharacterValue
expr_stmt|;
block|}
comment|/** Getter for property secondKernCharacter.      * @return Value of property secondKernCharacter.      */
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|getSecondKernCharacter
parameter_list|()
block|{
return|return
name|secondKernCharacter
return|;
block|}
comment|/** Setter for property secondKernCharacter.      * @param secondKernCharacterValue New value of property secondKernCharacter.      */
specifier|public
name|void
name|setSecondKernCharacter
parameter_list|(
name|String
name|secondKernCharacterValue
parameter_list|)
block|{
name|secondKernCharacter
operator|=
name|secondKernCharacterValue
expr_stmt|;
block|}
comment|/** Getter for property x.      * @return Value of property x.      */
specifier|public
name|float
name|getX
parameter_list|()
block|{
return|return
name|x
return|;
block|}
comment|/** Setter for property x.      * @param xValue New value of property x.      */
specifier|public
name|void
name|setX
parameter_list|(
name|float
name|xValue
parameter_list|)
block|{
name|x
operator|=
name|xValue
expr_stmt|;
block|}
comment|/** Getter for property y.      * @return Value of property y.      */
specifier|public
name|float
name|getY
parameter_list|()
block|{
return|return
name|y
return|;
block|}
comment|/** Setter for property y.      * @param yValue New value of property y.      */
specifier|public
name|void
name|setY
parameter_list|(
name|float
name|yValue
parameter_list|)
block|{
name|y
operator|=
name|yValue
expr_stmt|;
block|}
block|}
end_class

end_unit

