begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *   Licensed to the Apache Software Foundation (ASF) under one or more  *   contributor license agreements.  See the NOTICE file distributed with  *   this work for additional information regarding copyright ownership.  *   The ASF licenses this file to You under the Apache License, Version 2.0  *   (the "License"); you may not use this file except in compliance with  *   the License.  You may obtain a copy of the License at  *  *        http://www.apache.org/licenses/LICENSE-2.0  *  *   Unless required by applicable law or agreed to in writing, software  *   distributed under the License is distributed on an "AS IS" BASIS,  *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *   See the License for the specific language governing permissions and  *   limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|debugger
operator|.
name|flagbitspane
package|;
end_package

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
name|COSDictionary
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
name|COSName
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
name|encryption
operator|.
name|AccessPermission
import|;
end_import

begin_comment
comment|/**  * @author Tilman Hausherr  *  * A class that provides Encrypt flag bits.  */
end_comment

begin_class
specifier|public
class|class
name|EncryptFlag
extends|extends
name|Flag
block|{
specifier|private
specifier|final
name|COSDictionary
name|encryptDictionary
decl_stmt|;
comment|/**      * Constructor      * @param encryptDict COSDictionary instance.      */
name|EncryptFlag
parameter_list|(
name|COSDictionary
name|encryptDict
parameter_list|)
block|{
name|encryptDictionary
operator|=
name|encryptDict
expr_stmt|;
block|}
annotation|@
name|Override
name|String
name|getFlagType
parameter_list|()
block|{
return|return
literal|"Encrypt flag"
return|;
block|}
annotation|@
name|Override
name|String
name|getFlagValue
parameter_list|()
block|{
return|return
literal|"Flag value:"
operator|+
name|encryptDictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|P
argument_list|)
return|;
block|}
annotation|@
name|Override
name|Object
index|[]
index|[]
name|getFlagBits
parameter_list|()
block|{
name|int
name|p
init|=
name|encryptDictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|P
argument_list|)
decl_stmt|;
name|AccessPermission
name|ap
init|=
operator|new
name|AccessPermission
argument_list|(
name|encryptDictionary
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|P
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|Object
index|[]
index|[]
block|{
operator|new
name|Object
index|[]
block|{
literal|3
block|,
literal|"can print"
block|,
name|ap
operator|.
name|canPrint
argument_list|()
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|4
block|,
literal|"can modify"
block|,
name|ap
operator|.
name|canModify
argument_list|()
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|5
block|,
literal|"can extract content"
block|,
name|ap
operator|.
name|canExtractContent
argument_list|()
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|6
block|,
literal|"can modify annotations"
block|,
name|ap
operator|.
name|canModifyAnnotations
argument_list|()
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|9
block|,
literal|"can fill in form fields"
block|,
name|ap
operator|.
name|canFillInForm
argument_list|()
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|10
block|,
literal|"can extract for accessibility"
block|,
name|ap
operator|.
name|canExtractForAccessibility
argument_list|()
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|11
block|,
literal|"can assemble document"
block|,
name|ap
operator|.
name|canAssembleDocument
argument_list|()
block|}
block|,
operator|new
name|Object
index|[]
block|{
literal|12
block|,
literal|"can print degraded"
block|,
name|ap
operator|.
name|canPrintDegraded
argument_list|()
block|}
block|,         }
return|;
block|}
block|}
end_class

end_unit

