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
name|preflight
operator|.
name|exception
operator|.
name|ValidationException
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
name|process
operator|.
name|BookmarkValidationProcess
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
name|process
operator|.
name|EmptyValidationProcess
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
name|process
operator|.
name|ValidationProcess
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
name|process
operator|.
name|reflect
operator|.
name|ResourcesValidationProcess
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|TestPreflightConfiguration
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetValidationProcess
parameter_list|()
throws|throws
name|Exception
block|{
name|PreflightConfiguration
name|confg
init|=
name|PreflightConfiguration
operator|.
name|createPdfA1BConfiguration
argument_list|()
decl_stmt|;
name|ValidationProcess
name|vp
init|=
name|confg
operator|.
name|getInstanceOfProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|BOOKMARK_PROCESS
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|vp
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|vp
operator|instanceof
name|BookmarkValidationProcess
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetValidationPageProcess
parameter_list|()
throws|throws
name|Exception
block|{
name|PreflightConfiguration
name|confg
init|=
name|PreflightConfiguration
operator|.
name|createPdfA1BConfiguration
argument_list|()
decl_stmt|;
name|ValidationProcess
name|vp
init|=
name|confg
operator|.
name|getInstanceOfProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|RESOURCES_PROCESS
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|vp
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|vp
operator|instanceof
name|ResourcesValidationProcess
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetValidationProcess_noError
parameter_list|()
throws|throws
name|Exception
block|{
name|PreflightConfiguration
name|confg
init|=
name|PreflightConfiguration
operator|.
name|createPdfA1BConfiguration
argument_list|()
decl_stmt|;
name|confg
operator|.
name|setErrorOnMissingProcess
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|confg
operator|.
name|removeProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|BOOKMARK_PROCESS
argument_list|)
expr_stmt|;
name|ValidationProcess
name|vp
init|=
name|confg
operator|.
name|getInstanceOfProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|BOOKMARK_PROCESS
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|vp
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|vp
operator|instanceof
name|EmptyValidationProcess
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetValidationPageProcess_noError
parameter_list|()
throws|throws
name|Exception
block|{
name|PreflightConfiguration
name|confg
init|=
name|PreflightConfiguration
operator|.
name|createPdfA1BConfiguration
argument_list|()
decl_stmt|;
name|confg
operator|.
name|setErrorOnMissingProcess
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|confg
operator|.
name|removePageProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|RESOURCES_PROCESS
argument_list|)
expr_stmt|;
name|ValidationProcess
name|vp
init|=
name|confg
operator|.
name|getInstanceOfProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|RESOURCES_PROCESS
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|vp
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|vp
operator|instanceof
name|EmptyValidationProcess
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ValidationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testGetMissingValidationProcess
parameter_list|()
throws|throws
name|Exception
block|{
name|PreflightConfiguration
name|confg
init|=
name|PreflightConfiguration
operator|.
name|createPdfA1BConfiguration
argument_list|()
decl_stmt|;
name|confg
operator|.
name|removeProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|BOOKMARK_PROCESS
argument_list|)
expr_stmt|;
name|confg
operator|.
name|getInstanceOfProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|BOOKMARK_PROCESS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ValidationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testGetMissingValidationPageProcess
parameter_list|()
throws|throws
name|Exception
block|{
name|PreflightConfiguration
name|confg
init|=
name|PreflightConfiguration
operator|.
name|createPdfA1BConfiguration
argument_list|()
decl_stmt|;
name|confg
operator|.
name|removePageProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|RESOURCES_PROCESS
argument_list|)
expr_stmt|;
name|confg
operator|.
name|getInstanceOfProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|RESOURCES_PROCESS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ValidationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testGetMissingValidationProcess2
parameter_list|()
throws|throws
name|Exception
block|{
name|PreflightConfiguration
name|confg
init|=
name|PreflightConfiguration
operator|.
name|createPdfA1BConfiguration
argument_list|()
decl_stmt|;
name|confg
operator|.
name|replaceProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|BOOKMARK_PROCESS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|confg
operator|.
name|getInstanceOfProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|BOOKMARK_PROCESS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|ValidationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testGetMissingValidationPageProcess2
parameter_list|()
throws|throws
name|Exception
block|{
name|PreflightConfiguration
name|confg
init|=
name|PreflightConfiguration
operator|.
name|createPdfA1BConfiguration
argument_list|()
decl_stmt|;
name|confg
operator|.
name|replacePageProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|RESOURCES_PROCESS
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|confg
operator|.
name|getInstanceOfProcess
argument_list|(
name|PreflightConfiguration
operator|.
name|RESOURCES_PROCESS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|fail
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

