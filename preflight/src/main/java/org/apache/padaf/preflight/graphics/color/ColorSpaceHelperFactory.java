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
name|padaf
operator|.
name|preflight
operator|.
name|graphics
operator|.
name|color
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|padaf
operator|.
name|preflight
operator|.
name|DocumentHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
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
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|graphics
operator|.
name|color
operator|.
name|PDColorSpace
import|;
end_import

begin_comment
comment|/**  * This factory create the right Helper according to the owner of the ColorSpace  * entry.  */
end_comment

begin_class
specifier|public
class|class
name|ColorSpaceHelperFactory
block|{
comment|/**    * Return an instance of ColorSpaceHelper according to the    * ColorSpaceRestiction value.    *<UL>    *<li>ColorSpaceRestiction.NO_PATTERN : returns NoPatternColorSpaceHelper    *<li>ColorSpaceRestiction.ONLY_DEVICE : returns DeviceColorSpaceHelper    *<li>default : returns StandardColorSpaceHelper    *</UL>    *     * @param csObj    *          the COSBase which represents the ColorSpace (COSName or COSArray)    * @param handler    *          the DocumentHandler to access useful data    * @param csr    *          the color space restriction    * @return    */
specifier|public
specifier|static
name|ColorSpaceHelper
name|getColorSpaceHelper
parameter_list|(
name|COSBase
name|csObj
parameter_list|,
name|DocumentHandler
name|handler
parameter_list|,
name|ColorSpaceRestriction
name|csr
parameter_list|)
block|{
switch|switch
condition|(
name|csr
condition|)
block|{
case|case
name|NO_PATTERN
case|:
return|return
operator|new
name|NoPatternColorSpaceHelper
argument_list|(
name|csObj
argument_list|,
name|handler
argument_list|)
return|;
case|case
name|ONLY_DEVICE
case|:
return|return
operator|new
name|DeviceColorSpaceHelper
argument_list|(
name|csObj
argument_list|,
name|handler
argument_list|)
return|;
default|default:
return|return
operator|new
name|StandardColorSpaceHelper
argument_list|(
name|csObj
argument_list|,
name|handler
argument_list|)
return|;
block|}
block|}
comment|/**    * Return an instance of ColorSpaceHelper according to the    * ColorSpaceRestiction value.    *<UL>    *<li>ColorSpaceRestiction.NO_PATTERN : returns NoPatternColorSpaceHelper    *<li>ColorSpaceRestiction.ONLY_DEVICE : returns DeviceColorSpaceHelper    *<li>default : returns StandardColorSpaceHelper    *</UL>    *     * @param cs    *          the High level PDFBox object which represents the ColorSpace    * @param handler    *          the DocumentHandler to access useful data    * @param csr    *          the color space restriction    * @return    */
specifier|public
specifier|static
name|ColorSpaceHelper
name|getColorSpaceHelper
parameter_list|(
name|PDColorSpace
name|cs
parameter_list|,
name|DocumentHandler
name|handler
parameter_list|,
name|ColorSpaceRestriction
name|csr
parameter_list|)
block|{
switch|switch
condition|(
name|csr
condition|)
block|{
case|case
name|NO_PATTERN
case|:
return|return
operator|new
name|NoPatternColorSpaceHelper
argument_list|(
name|cs
argument_list|,
name|handler
argument_list|)
return|;
case|case
name|ONLY_DEVICE
case|:
return|return
operator|new
name|DeviceColorSpaceHelper
argument_list|(
name|cs
argument_list|,
name|handler
argument_list|)
return|;
default|default:
return|return
operator|new
name|StandardColorSpaceHelper
argument_list|(
name|cs
argument_list|,
name|handler
argument_list|)
return|;
block|}
block|}
comment|/**    * Enum used as argument of methods of this factory to return the right    * Helper.    */
specifier|public
enum|enum
name|ColorSpaceRestriction
block|{
name|NO_RESTRICTION
block|,
name|NO_PATTERN
block|,
name|ONLY_DEVICE
block|;   }
block|}
end_class

end_unit

