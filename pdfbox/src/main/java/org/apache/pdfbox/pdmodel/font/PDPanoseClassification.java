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
name|pdmodel
operator|.
name|font
package|;
end_package

begin_comment
comment|/**  * Represents a 10-byte PANOSE classification.  *  * @link http://www.monotype.com/services/pan2  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|PDPanoseClassification
block|{
specifier|private
specifier|final
name|byte
index|[]
name|bytes
decl_stmt|;
name|PDPanoseClassification
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|this
operator|.
name|bytes
operator|=
name|bytes
expr_stmt|;
block|}
specifier|public
name|int
name|getFamilyKind
parameter_list|()
block|{
return|return
name|bytes
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|int
name|getSerifStyle
parameter_list|()
block|{
return|return
name|bytes
index|[
literal|1
index|]
return|;
block|}
specifier|public
name|int
name|getWeight
parameter_list|()
block|{
return|return
name|bytes
index|[
literal|2
index|]
return|;
block|}
specifier|public
name|int
name|getProportion
parameter_list|()
block|{
return|return
name|bytes
index|[
literal|3
index|]
return|;
block|}
specifier|public
name|int
name|getContrast
parameter_list|()
block|{
return|return
name|bytes
index|[
literal|4
index|]
return|;
block|}
specifier|public
name|int
name|getStrokeVariation
parameter_list|()
block|{
return|return
name|bytes
index|[
literal|5
index|]
return|;
block|}
specifier|public
name|int
name|getArmStyle
parameter_list|()
block|{
return|return
name|bytes
index|[
literal|6
index|]
return|;
block|}
specifier|public
name|int
name|getLetterform
parameter_list|()
block|{
return|return
name|bytes
index|[
literal|7
index|]
return|;
block|}
specifier|public
name|int
name|getMidline
parameter_list|()
block|{
return|return
name|bytes
index|[
literal|8
index|]
return|;
block|}
specifier|public
name|int
name|getXHeight
parameter_list|()
block|{
return|return
name|bytes
index|[
literal|9
index|]
return|;
block|}
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|()
block|{
return|return
name|bytes
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"{ FamilyType = "
operator|+
name|getFamilyKind
argument_list|()
operator|+
literal|", "
operator|+
literal|"SerifStyle = "
operator|+
name|getSerifStyle
argument_list|()
operator|+
literal|", "
operator|+
literal|"Weight = "
operator|+
name|getWeight
argument_list|()
operator|+
literal|", "
operator|+
literal|"Proportion = "
operator|+
name|getProportion
argument_list|()
operator|+
literal|", "
operator|+
literal|"Contrast = "
operator|+
name|getContrast
argument_list|()
operator|+
literal|", "
operator|+
literal|"StrokeVariation = "
operator|+
name|getStrokeVariation
argument_list|()
operator|+
literal|", "
operator|+
literal|"ArmStyle = "
operator|+
name|getArmStyle
argument_list|()
operator|+
literal|", "
operator|+
literal|"Letterform = "
operator|+
name|getLetterform
argument_list|()
operator|+
literal|", "
operator|+
literal|"Midline = "
operator|+
name|getMidline
argument_list|()
operator|+
literal|", "
operator|+
literal|"XHeight = "
operator|+
name|getXHeight
argument_list|()
operator|+
literal|"}"
return|;
block|}
block|}
end_class

end_unit

