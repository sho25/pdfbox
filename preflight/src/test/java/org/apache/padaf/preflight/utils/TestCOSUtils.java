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
name|utils
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

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
name|utils
operator|.
name|COSUtils
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
name|COSArray
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
name|COSDocument
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
name|COSFloat
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
name|COSInteger
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
name|COSObject
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
name|COSStream
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
name|COSString
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
name|io
operator|.
name|RandomAccess
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
name|PDDocument
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
name|persistence
operator|.
name|util
operator|.
name|COSObjectKey
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
name|TestCOSUtils
block|{
annotation|@
name|Test
specifier|public
name|void
name|testIsInteger
parameter_list|()
block|{
try|try
block|{
name|COSObject
name|co
init|=
operator|new
name|COSObject
argument_list|(
operator|new
name|COSInteger
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
name|co
operator|.
name|setGenerationNumber
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|co
operator|.
name|setObjectNumber
argument_list|(
operator|new
name|COSInteger
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|COSUtils
operator|.
name|isInteger
argument_list|(
name|co
argument_list|,
operator|new
name|IOCOSDocument
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|COSDocument
name|doc
init|=
operator|new
name|COSDocument
argument_list|()
decl_stmt|;
name|addToXref
argument_list|(
name|doc
argument_list|,
operator|new
name|COSObjectKey
argument_list|(
name|co
argument_list|)
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|COSUtils
operator|.
name|isInteger
argument_list|(
name|co
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsFloat
parameter_list|()
block|{
try|try
block|{
name|COSObject
name|co
init|=
operator|new
name|COSObject
argument_list|(
operator|new
name|COSFloat
argument_list|(
literal|10.0f
argument_list|)
argument_list|)
decl_stmt|;
name|co
operator|.
name|setGenerationNumber
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|co
operator|.
name|setObjectNumber
argument_list|(
operator|new
name|COSInteger
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|COSUtils
operator|.
name|isFloat
argument_list|(
name|co
argument_list|,
operator|new
name|IOCOSDocument
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|COSDocument
name|doc
init|=
operator|new
name|COSDocument
argument_list|()
decl_stmt|;
name|addToXref
argument_list|(
name|doc
argument_list|,
operator|new
name|COSObjectKey
argument_list|(
name|co
argument_list|)
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|COSUtils
operator|.
name|isFloat
argument_list|(
name|co
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsString
parameter_list|()
block|{
try|try
block|{
name|COSObject
name|co
init|=
operator|new
name|COSObject
argument_list|(
operator|new
name|COSString
argument_list|(
literal|""
argument_list|)
argument_list|)
decl_stmt|;
name|co
operator|.
name|setGenerationNumber
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|co
operator|.
name|setObjectNumber
argument_list|(
operator|new
name|COSInteger
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|COSUtils
operator|.
name|isString
argument_list|(
name|co
argument_list|,
operator|new
name|IOCOSDocument
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|COSDocument
name|doc
init|=
operator|new
name|COSDocument
argument_list|()
decl_stmt|;
name|addToXref
argument_list|(
name|doc
argument_list|,
operator|new
name|COSObjectKey
argument_list|(
name|co
argument_list|)
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|COSUtils
operator|.
name|isString
argument_list|(
name|co
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsStream
parameter_list|()
block|{
try|try
block|{
name|COSObject
name|co
init|=
operator|new
name|COSObject
argument_list|(
operator|new
name|COSStream
argument_list|(
literal|null
argument_list|)
argument_list|)
decl_stmt|;
name|co
operator|.
name|setGenerationNumber
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|co
operator|.
name|setObjectNumber
argument_list|(
operator|new
name|COSInteger
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|COSUtils
operator|.
name|isStream
argument_list|(
name|co
argument_list|,
operator|new
name|IOCOSDocument
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|COSDocument
name|doc
init|=
operator|new
name|COSDocument
argument_list|()
decl_stmt|;
name|addToXref
argument_list|(
name|doc
argument_list|,
operator|new
name|COSObjectKey
argument_list|(
name|co
argument_list|)
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|COSUtils
operator|.
name|isStream
argument_list|(
name|co
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsDictionary
parameter_list|()
block|{
try|try
block|{
name|COSObject
name|co
init|=
operator|new
name|COSObject
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
name|co
operator|.
name|setGenerationNumber
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|co
operator|.
name|setObjectNumber
argument_list|(
operator|new
name|COSInteger
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|COSUtils
operator|.
name|isDictionary
argument_list|(
name|co
argument_list|,
operator|new
name|IOCOSDocument
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|COSDocument
name|doc
init|=
operator|new
name|COSDocument
argument_list|()
decl_stmt|;
name|addToXref
argument_list|(
name|doc
argument_list|,
operator|new
name|COSObjectKey
argument_list|(
name|co
argument_list|)
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|COSUtils
operator|.
name|isDictionary
argument_list|(
name|co
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsArray
parameter_list|()
block|{
try|try
block|{
name|COSObject
name|co
init|=
operator|new
name|COSObject
argument_list|(
operator|new
name|COSArray
argument_list|()
argument_list|)
decl_stmt|;
name|co
operator|.
name|setGenerationNumber
argument_list|(
name|COSInteger
operator|.
name|ZERO
argument_list|)
expr_stmt|;
name|co
operator|.
name|setObjectNumber
argument_list|(
operator|new
name|COSInteger
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|COSUtils
operator|.
name|isArray
argument_list|(
name|co
argument_list|,
operator|new
name|IOCOSDocument
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|COSDocument
name|doc
init|=
operator|new
name|COSDocument
argument_list|()
decl_stmt|;
name|addToXref
argument_list|(
name|doc
argument_list|,
operator|new
name|COSObjectKey
argument_list|(
name|co
argument_list|)
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|COSUtils
operator|.
name|isArray
argument_list|(
name|co
argument_list|,
name|doc
argument_list|)
expr_stmt|;
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCloseCOSDocumentNull
parameter_list|()
block|{
name|COSUtils
operator|.
name|closeDocumentQuietly
argument_list|(
operator|(
name|COSDocument
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClosePDDocumentNull
parameter_list|()
block|{
name|COSUtils
operator|.
name|closeDocumentQuietly
argument_list|(
operator|(
name|PDDocument
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCloseCOSDocumentIO
parameter_list|()
block|{
try|try
block|{
name|COSUtils
operator|.
name|closeDocumentQuietly
argument_list|(
operator|new
name|IOCOSDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|addToXref
parameter_list|(
name|COSDocument
name|doc
parameter_list|,
name|COSObjectKey
name|key
parameter_list|,
name|long
name|value
parameter_list|)
block|{
name|Map
argument_list|<
name|COSObjectKey
argument_list|,
name|Long
argument_list|>
name|xrefTable
init|=
operator|new
name|HashMap
argument_list|<
name|COSObjectKey
argument_list|,
name|Long
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|xrefTable
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addXRefTable
argument_list|(
name|xrefTable
argument_list|)
expr_stmt|;
block|}
comment|/**    * Class used to check the catch block in COSUtils methods    */
specifier|private
class|class
name|IOCOSDocument
extends|extends
name|COSDocument
block|{
name|IOCOSDocument
parameter_list|()
throws|throws
name|IOException
block|{
name|super
argument_list|()
expr_stmt|;
block|}
name|IOCOSDocument
parameter_list|(
name|File
name|scratchDir
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|scratchDir
argument_list|)
expr_stmt|;
block|}
name|IOCOSDocument
parameter_list|(
name|RandomAccess
name|file
parameter_list|)
block|{
name|super
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Exception for code coverage"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|COSObject
name|getObjectFromPool
parameter_list|(
name|COSObjectKey
name|key
parameter_list|)
throws|throws
name|IOException
block|{
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Exception for code coverage"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

