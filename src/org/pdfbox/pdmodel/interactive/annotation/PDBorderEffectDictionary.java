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
name|pdmodel
operator|.
name|interactive
operator|.
name|annotation
package|;
end_package

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSDictionary
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This class represents a PDF /BE entry the border effect dictionary.  *  * @author Paul King  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|PDBorderEffectDictionary
implements|implements
name|COSObjectable
block|{
comment|/*      * The various values of the effect applied to the border as defined in the      * PDF 1.6 reference Table 8.14      */
comment|/**      * Constant for the name for no effect.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_SOLID
init|=
literal|"S"
decl_stmt|;
comment|/**      * Constant for the name of a cloudy effect.      */
specifier|public
specifier|static
specifier|final
name|String
name|STYLE_CLOUDY
init|=
literal|"C"
decl_stmt|;
specifier|private
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|PDBorderEffectDictionary
parameter_list|()
block|{
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dict      *            a border style dictionary.      */
specifier|public
name|PDBorderEffectDictionary
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
name|dictionary
operator|=
name|dict
expr_stmt|;
block|}
comment|/**      * returns the dictionary.      *      * @return the dictionary      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * returns the dictionary.      *      * @return the dictionary      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|dictionary
return|;
block|}
comment|/**      * This will set the intensity of the applied effect.      *      * @param i      *            the intensity of the effect values 0 to 2      */
specifier|public
name|void
name|setIntensity
parameter_list|(
name|float
name|i
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setFloat
argument_list|(
literal|"I"
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the intensity of the applied effect.      *      * @return the intensity value 0 to 2      */
specifier|public
name|float
name|getIntensity
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getFloat
argument_list|(
literal|"I"
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the border effect, see the STYLE_* constants for valid values.      *      * @param s      *            the border effect to use      */
specifier|public
name|void
name|setStyle
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|getDictionary
argument_list|()
operator|.
name|setName
argument_list|(
literal|"S"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the border effect, see the STYLE_* constants for valid      * values.      *      * @return the effect of the border      */
specifier|public
name|String
name|getStyle
parameter_list|()
block|{
return|return
name|getDictionary
argument_list|()
operator|.
name|getNameAsString
argument_list|(
literal|"S"
argument_list|,
name|STYLE_SOLID
argument_list|)
return|;
block|}
block|}
end_class

end_unit

