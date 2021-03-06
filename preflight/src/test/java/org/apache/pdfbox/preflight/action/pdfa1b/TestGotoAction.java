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
name|pdfbox
operator|.
name|preflight
operator|.
name|action
operator|.
name|pdfa1b
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
name|interactive
operator|.
name|action
operator|.
name|PDActionGoTo
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
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
operator|.
name|PDDestination
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
name|preflight
operator|.
name|PreflightConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|TestGotoAction
extends|extends
name|AbstractTestAction
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGoto_OK
parameter_list|()
throws|throws
name|Exception
block|{
name|PDActionGoTo
name|gotoAction
init|=
operator|new
name|PDActionGoTo
argument_list|()
decl_stmt|;
name|gotoAction
operator|.
name|setDestination
argument_list|(
operator|new
name|PDDestination
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"ADest"
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|valid
argument_list|(
name|gotoAction
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGoto_KO_invalidContent
parameter_list|()
throws|throws
name|Exception
block|{
name|PDActionGoTo
name|gotoAction
init|=
operator|new
name|PDActionGoTo
argument_list|()
decl_stmt|;
name|gotoAction
operator|.
name|setDestination
argument_list|(
operator|new
name|PDDestination
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
operator|new
name|COSDictionary
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|valid
argument_list|(
name|gotoAction
argument_list|,
literal|false
argument_list|,
name|PreflightConstants
operator|.
name|ERROR_SYNTAX_DICT_INVALID
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGoto_KO_missingD
parameter_list|()
throws|throws
name|Exception
block|{
name|PDActionGoTo
name|gotoAction
init|=
operator|new
name|PDActionGoTo
argument_list|()
decl_stmt|;
name|valid
argument_list|(
name|gotoAction
argument_list|,
literal|false
argument_list|,
name|PreflightConstants
operator|.
name|ERROR_ACTION_MISING_KEY
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

