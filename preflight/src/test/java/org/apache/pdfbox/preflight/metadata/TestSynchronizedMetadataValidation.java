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
name|metadata
package|;
end_package

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
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Assert
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
name|pdmodel
operator|.
name|PDDocumentInformation
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
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|ValidationResult
operator|.
name|ValidationError
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
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|AdobePDFSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|DublinCoreSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|schema
operator|.
name|XMPBasicSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_comment
comment|/**  * Test Class of SynchronizedMetaDataValidation (for 6-7-3 Isartor Tests)  *   * @author Germain Costenobel  *   */
end_comment

begin_class
specifier|public
class|class
name|TestSynchronizedMetadataValidation
block|{
specifier|protected
name|PDDocument
name|doc
decl_stmt|;
specifier|protected
name|PDDocumentInformation
name|dico
decl_stmt|;
specifier|protected
name|XMPMetadata
name|metadata
decl_stmt|;
specifier|protected
name|String
name|title
decl_stmt|,
name|author
decl_stmt|,
name|subject
decl_stmt|,
name|keywords
decl_stmt|,
name|creator
decl_stmt|,
name|producer
decl_stmt|;
specifier|protected
name|Calendar
name|creationDate
decl_stmt|,
name|modifyDate
decl_stmt|;
specifier|protected
specifier|static
name|SynchronizedMetaDataValidation
name|sync
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|ValidationError
argument_list|>
name|ve
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|initSynchronizedMetadataValidation
parameter_list|()
block|{
name|sync
operator|=
operator|new
name|SynchronizedMetaDataValidation
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|initNewDocumentInformation
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|doc
operator|=
operator|new
name|PDDocument
argument_list|()
expr_stmt|;
name|dico
operator|=
name|doc
operator|.
name|getDocumentInformation
argument_list|()
expr_stmt|;
name|metadata
operator|=
name|XMPMetadata
operator|.
name|createXMPMetadata
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Failed to create temporary test PDF/XMP Document"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Check detection of a null Document      *       * @throws ValidationException      */
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
name|TestNullDocument
parameter_list|()
throws|throws
name|ValidationException
block|{
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
literal|null
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check detection of null metadata      *       * @throws ValidationException      */
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
name|TestNullMetaData
parameter_list|()
throws|throws
name|ValidationException
block|{
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
name|doc
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check the detection of a PDF document without any information      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|TestDocumentWithoutInformation
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|ve
operator|=
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
name|doc
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
comment|// Test without any information
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ve
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Check the detection of a completely empty XMP document (without any schemas)      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testEmptyXMP
parameter_list|()
throws|throws
name|Exception
block|{
name|title
operator|=
literal|"TITLE"
expr_stmt|;
name|author
operator|=
literal|"AUTHOR(S)"
expr_stmt|;
name|subject
operator|=
literal|"SUBJECTS"
expr_stmt|;
name|keywords
operator|=
literal|"KEYWORD(S)"
expr_stmt|;
name|creator
operator|=
literal|"CREATOR"
expr_stmt|;
name|producer
operator|=
literal|"PRODUCER"
expr_stmt|;
name|creationDate
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|modifyDate
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
comment|// Writing info in Document Information dictionary
comment|// TITLE
name|dico
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
comment|// AUTHOR
name|dico
operator|.
name|setAuthor
argument_list|(
name|author
argument_list|)
expr_stmt|;
comment|// SUBJECT
name|dico
operator|.
name|setSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
comment|// KEYWORDS
name|dico
operator|.
name|setKeywords
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
comment|// CREATOR
name|dico
operator|.
name|setCreator
argument_list|(
name|creator
argument_list|)
expr_stmt|;
comment|// PRODUCER
name|dico
operator|.
name|setProducer
argument_list|(
name|producer
argument_list|)
expr_stmt|;
comment|// CREATION DATE
name|dico
operator|.
name|setCreationDate
argument_list|(
name|creationDate
argument_list|)
expr_stmt|;
comment|// MODIFY DATE
name|dico
operator|.
name|setModificationDate
argument_list|(
name|modifyDate
argument_list|)
expr_stmt|;
try|try
block|{
name|ve
operator|=
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
name|doc
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
comment|// Test Detection of an Empty XMP (without any schemas)
for|for
control|(
name|ValidationError
name|valid
range|:
name|ve
control|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_MISMATCH
argument_list|,
name|valid
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Check the detection of a XMP with empty common schemas      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testEmptyXMPSchemas
parameter_list|()
throws|throws
name|Exception
block|{
name|title
operator|=
literal|"TITLE"
expr_stmt|;
name|author
operator|=
literal|"AUTHOR(S)"
expr_stmt|;
name|subject
operator|=
literal|"SUBJECTS"
expr_stmt|;
name|keywords
operator|=
literal|"KEYWORD(S)"
expr_stmt|;
name|creator
operator|=
literal|"CREATOR"
expr_stmt|;
name|producer
operator|=
literal|"PRODUCER"
expr_stmt|;
name|creationDate
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|modifyDate
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
comment|// building temporary XMP metadata (but empty)
name|metadata
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
expr_stmt|;
name|metadata
operator|.
name|createAndAddAdobePDFSchema
argument_list|()
expr_stmt|;
name|metadata
operator|.
name|createAndAddXMPBasicSchema
argument_list|()
expr_stmt|;
comment|// Writing info in Document Information dictionary
comment|// TITLE
name|dico
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
comment|// AUTHOR
name|dico
operator|.
name|setAuthor
argument_list|(
name|author
argument_list|)
expr_stmt|;
comment|// SUBJECT
name|dico
operator|.
name|setSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
comment|// KEYWORDS
name|dico
operator|.
name|setKeywords
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
comment|// CREATOR
name|dico
operator|.
name|setCreator
argument_list|(
name|creator
argument_list|)
expr_stmt|;
comment|// PRODUCER
name|dico
operator|.
name|setProducer
argument_list|(
name|producer
argument_list|)
expr_stmt|;
comment|// CREATION DATE
name|dico
operator|.
name|setCreationDate
argument_list|(
name|creationDate
argument_list|)
expr_stmt|;
comment|// MODIFY DATE
name|dico
operator|.
name|setModificationDate
argument_list|(
name|modifyDate
argument_list|)
expr_stmt|;
try|try
block|{
name|ve
operator|=
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
name|doc
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
comment|// Test Detection of absent XMP values
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|ve
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Check detection of a null value in array (for Subject and author properties)      *       * @throws Exception      */
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testNullArrayValue
parameter_list|()
throws|throws
name|Exception
block|{
comment|// building temporary XMP metadata
name|DublinCoreSchema
name|dc
init|=
name|metadata
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
decl_stmt|;
comment|// AUTHOR
name|dico
operator|.
name|setAuthor
argument_list|(
literal|"dicoAuthor"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addCreator
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// SUBJECT
name|dico
operator|.
name|setSubject
argument_list|(
literal|"dicoSubj"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addSubject
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// Launching synchronization test
try|try
block|{
name|ve
operator|=
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
name|doc
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
comment|// Test unsychronized value
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|ve
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * in XMP, Subject and Author must be embedded in a single entry text array This function check the detection of      * multiple entries for these properties      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testBadSizeOfArrays
parameter_list|()
throws|throws
name|Exception
block|{
comment|// building temporary XMP metadata
name|DublinCoreSchema
name|dc
init|=
name|metadata
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
decl_stmt|;
name|AdobePDFSchema
name|pdf
init|=
name|metadata
operator|.
name|createAndAddAdobePDFSchema
argument_list|()
decl_stmt|;
name|XMPBasicSchema
name|xmp
init|=
name|metadata
operator|.
name|createAndAddXMPBasicSchema
argument_list|()
decl_stmt|;
comment|// Writing info in XMP and Document Information dictionary
comment|// TITLE
name|dico
operator|.
name|setTitle
argument_list|(
literal|"dicoTitle"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|setTitle
argument_list|(
literal|"x-default"
argument_list|,
literal|"XMPTitle"
argument_list|)
expr_stmt|;
comment|// AUTHOR
name|dico
operator|.
name|setAuthor
argument_list|(
literal|"dicoAuthor"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addCreator
argument_list|(
literal|"XMPAuthor"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addCreator
argument_list|(
literal|"2ndCreator"
argument_list|)
expr_stmt|;
comment|// SUBJECT
name|dico
operator|.
name|setSubject
argument_list|(
literal|"dicoSubj"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addSubject
argument_list|(
literal|"XMPSubj"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addSubject
argument_list|(
literal|"2ndSubj"
argument_list|)
expr_stmt|;
comment|// KEYWORDS
name|dico
operator|.
name|setKeywords
argument_list|(
literal|"DicoKeywords"
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setKeywords
argument_list|(
literal|"XMPkeywords"
argument_list|)
expr_stmt|;
comment|// CREATOR
name|dico
operator|.
name|setCreator
argument_list|(
literal|"DicoCreator"
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setCreatorTool
argument_list|(
literal|"XMPCreator"
argument_list|)
expr_stmt|;
comment|// PRODUCER
name|dico
operator|.
name|setProducer
argument_list|(
literal|"DicoProducer"
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setProducer
argument_list|(
literal|"XMPProducer"
argument_list|)
expr_stmt|;
comment|// CREATION DATE
name|dico
operator|.
name|setCreationDate
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|GregorianCalendar
name|XMPCreate
init|=
operator|new
name|GregorianCalendar
argument_list|(
literal|2008
argument_list|,
literal|11
argument_list|,
literal|05
argument_list|)
decl_stmt|;
name|xmp
operator|.
name|setCreateDate
argument_list|(
name|XMPCreate
argument_list|)
expr_stmt|;
comment|// MODIFY DATE
name|dico
operator|.
name|setModificationDate
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|GregorianCalendar
name|XMPModify
init|=
operator|new
name|GregorianCalendar
argument_list|(
literal|2009
argument_list|,
literal|10
argument_list|,
literal|15
argument_list|)
decl_stmt|;
name|xmp
operator|.
name|setModifyDate
argument_list|(
name|XMPModify
argument_list|)
expr_stmt|;
comment|// Launching synchronization test
try|try
block|{
name|ve
operator|=
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
name|doc
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
comment|// Test unsychronized value
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|ve
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Check the detection of unsynchronized information between Document Information dictionary and XMP      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testAllInfoUnsynchronized
parameter_list|()
throws|throws
name|Exception
block|{
comment|// building temporary XMP metadata
name|DublinCoreSchema
name|dc
init|=
name|metadata
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
decl_stmt|;
name|AdobePDFSchema
name|pdf
init|=
name|metadata
operator|.
name|createAndAddAdobePDFSchema
argument_list|()
decl_stmt|;
name|XMPBasicSchema
name|xmp
init|=
name|metadata
operator|.
name|createAndAddXMPBasicSchema
argument_list|()
decl_stmt|;
comment|// Writing info in XMP and Document Information dictionary
comment|// TITLE
name|dico
operator|.
name|setTitle
argument_list|(
literal|"dicoTitle"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|setTitle
argument_list|(
literal|"x-default"
argument_list|,
literal|"XMPTitle"
argument_list|)
expr_stmt|;
comment|// AUTHOR
name|dico
operator|.
name|setAuthor
argument_list|(
literal|"dicoAuthor"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addCreator
argument_list|(
literal|"XMPAuthor"
argument_list|)
expr_stmt|;
comment|// SUBJECT
name|dico
operator|.
name|setSubject
argument_list|(
literal|"dicoSubj"
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addSubject
argument_list|(
literal|"XMPSubj"
argument_list|)
expr_stmt|;
comment|// KEYWORDS
name|dico
operator|.
name|setKeywords
argument_list|(
literal|"DicoKeywords"
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setKeywords
argument_list|(
literal|"XMPkeywords"
argument_list|)
expr_stmt|;
comment|// CREATOR
name|dico
operator|.
name|setCreator
argument_list|(
literal|"DicoCreator"
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setCreatorTool
argument_list|(
literal|"XMPCreator"
argument_list|)
expr_stmt|;
comment|// PRODUCER
name|dico
operator|.
name|setProducer
argument_list|(
literal|"DicoProducer"
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setProducer
argument_list|(
literal|"XMPProducer"
argument_list|)
expr_stmt|;
comment|// CREATION DATE
name|dico
operator|.
name|setCreationDate
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|GregorianCalendar
name|XMPCreate
init|=
operator|new
name|GregorianCalendar
argument_list|(
literal|2008
argument_list|,
literal|11
argument_list|,
literal|05
argument_list|)
decl_stmt|;
name|xmp
operator|.
name|setCreateDate
argument_list|(
name|XMPCreate
argument_list|)
expr_stmt|;
comment|// MODIFY DATE
name|dico
operator|.
name|setModificationDate
argument_list|(
name|Calendar
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|GregorianCalendar
name|XMPModify
init|=
operator|new
name|GregorianCalendar
argument_list|(
literal|2009
argument_list|,
literal|10
argument_list|,
literal|15
argument_list|)
decl_stmt|;
name|xmp
operator|.
name|setModifyDate
argument_list|(
name|XMPModify
argument_list|)
expr_stmt|;
comment|// Launching synchronization test
try|try
block|{
name|ve
operator|=
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
name|doc
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
comment|// Test unsychronized value
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|ve
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Check reaction when metadata are well-formed      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testAllInfoSynhcronized
parameter_list|()
throws|throws
name|Exception
block|{
name|title
operator|=
literal|"TITLE"
expr_stmt|;
name|author
operator|=
literal|"AUTHOR(S)"
expr_stmt|;
name|subject
operator|=
literal|"SUBJECTS"
expr_stmt|;
name|keywords
operator|=
literal|"KEYWORD(S)"
expr_stmt|;
name|creator
operator|=
literal|"CREATOR"
expr_stmt|;
name|producer
operator|=
literal|"PRODUCER"
expr_stmt|;
name|creationDate
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|modifyDate
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
comment|// building temporary XMP metadata
name|DublinCoreSchema
name|dc
init|=
name|metadata
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
decl_stmt|;
name|XMPBasicSchema
name|xmp
init|=
name|metadata
operator|.
name|createAndAddXMPBasicSchema
argument_list|()
decl_stmt|;
name|AdobePDFSchema
name|pdf
init|=
name|metadata
operator|.
name|createAndAddAdobePDFSchema
argument_list|()
decl_stmt|;
comment|// Writing info in XMP and Document Information dictionary
comment|// TITLE
name|dico
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|dc
operator|.
name|setTitle
argument_list|(
literal|"x-default"
argument_list|,
name|title
argument_list|)
expr_stmt|;
comment|// AUTHOR
name|dico
operator|.
name|setAuthor
argument_list|(
name|author
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addCreator
argument_list|(
name|author
argument_list|)
expr_stmt|;
comment|// SUBJECT
name|dico
operator|.
name|setSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addDescription
argument_list|(
literal|"x-default"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
comment|// KEYWORDS
name|dico
operator|.
name|setKeywords
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setKeywords
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
comment|// CREATOR
name|dico
operator|.
name|setCreator
argument_list|(
name|creator
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setCreatorTool
argument_list|(
name|creator
argument_list|)
expr_stmt|;
comment|// PRODUCER
name|dico
operator|.
name|setProducer
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setProducer
argument_list|(
name|producer
argument_list|)
expr_stmt|;
comment|// CREATION DATE
name|dico
operator|.
name|setCreationDate
argument_list|(
name|creationDate
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setCreateDate
argument_list|(
name|creationDate
argument_list|)
expr_stmt|;
comment|// MODIFY DATE
name|dico
operator|.
name|setModificationDate
argument_list|(
name|modifyDate
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setModifyDate
argument_list|(
name|modifyDate
argument_list|)
expr_stmt|;
comment|// Launching synchronization test
try|try
block|{
name|ve
operator|=
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
name|doc
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
comment|// Checking all is synchronized
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ve
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Check if SchemaAccessException Generator is ok      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|checkSchemaAccessException
parameter_list|()
throws|throws
name|Exception
block|{
name|Throwable
name|cause
init|=
operator|new
name|Throwable
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertSame
argument_list|(
name|cause
argument_list|,
name|sync
operator|.
name|SchemaAccessException
argument_list|(
literal|"test"
argument_list|,
name|cause
argument_list|)
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check reaction when metadata are well-formed      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testBadPrefixSchemas
parameter_list|()
throws|throws
name|Exception
block|{
name|title
operator|=
literal|"TITLE"
expr_stmt|;
name|author
operator|=
literal|"AUTHOR(S)"
expr_stmt|;
name|subject
operator|=
literal|"SUBJECTS"
expr_stmt|;
name|keywords
operator|=
literal|"KEYWORD(S)"
expr_stmt|;
name|creator
operator|=
literal|"CREATOR"
expr_stmt|;
name|producer
operator|=
literal|"PRODUCER"
expr_stmt|;
name|creationDate
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|modifyDate
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
comment|// building temporary XMP metadata
name|DublinCoreSchema
name|dc
init|=
operator|new
name|DublinCoreSchema
argument_list|(
name|metadata
argument_list|,
literal|"dctest"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|addSchema
argument_list|(
name|dc
argument_list|)
expr_stmt|;
name|XMPBasicSchema
name|xmp
init|=
operator|new
name|XMPBasicSchema
argument_list|(
name|metadata
argument_list|,
literal|"xmptest"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|addSchema
argument_list|(
name|xmp
argument_list|)
expr_stmt|;
name|AdobePDFSchema
name|pdf
init|=
operator|new
name|AdobePDFSchema
argument_list|(
name|metadata
argument_list|,
literal|"pdftest"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|addSchema
argument_list|(
name|pdf
argument_list|)
expr_stmt|;
comment|// Writing info in XMP and Document Information dictionary
comment|// TITLE
name|dico
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|dc
operator|.
name|setTitle
argument_list|(
literal|"x-default"
argument_list|,
name|title
argument_list|)
expr_stmt|;
comment|// AUTHOR
name|dico
operator|.
name|setAuthor
argument_list|(
name|author
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addCreator
argument_list|(
name|author
argument_list|)
expr_stmt|;
comment|// SUBJECT
name|dico
operator|.
name|setSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addDescription
argument_list|(
literal|"x-default"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
comment|// KEYWORDS
name|dico
operator|.
name|setKeywords
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setKeywords
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
comment|// CREATOR
name|dico
operator|.
name|setCreator
argument_list|(
name|creator
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setCreatorTool
argument_list|(
name|creator
argument_list|)
expr_stmt|;
comment|// PRODUCER
name|dico
operator|.
name|setProducer
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setProducer
argument_list|(
name|producer
argument_list|)
expr_stmt|;
comment|// CREATION DATE
name|dico
operator|.
name|setCreationDate
argument_list|(
name|creationDate
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setCreateDate
argument_list|(
name|creationDate
argument_list|)
expr_stmt|;
comment|// MODIFY DATE
name|dico
operator|.
name|setModificationDate
argument_list|(
name|modifyDate
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setModifyDate
argument_list|(
name|modifyDate
argument_list|)
expr_stmt|;
comment|// Launching synchronization test
try|try
block|{
name|ve
operator|=
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
name|doc
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
for|for
control|(
name|ValidationError
name|valid
range|:
name|ve
control|)
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|PreflightConstants
operator|.
name|ERROR_METADATA_WRONG_NS_PREFIX
argument_list|,
name|valid
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|/**      * Check reaction when metadata are well-formed      *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testdoublePrefixSchemas
parameter_list|()
throws|throws
name|Exception
block|{
name|title
operator|=
literal|"TITLE"
expr_stmt|;
name|author
operator|=
literal|"AUTHOR(S)"
expr_stmt|;
name|subject
operator|=
literal|"SUBJECTS"
expr_stmt|;
name|keywords
operator|=
literal|"KEYWORD(S)"
expr_stmt|;
name|creator
operator|=
literal|"CREATOR"
expr_stmt|;
name|producer
operator|=
literal|"PRODUCER"
expr_stmt|;
name|creationDate
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
name|modifyDate
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|()
expr_stmt|;
comment|// building temporary XMP metadata
name|DublinCoreSchema
name|dc
init|=
name|metadata
operator|.
name|createAndAddDublinCoreSchema
argument_list|()
decl_stmt|;
name|DublinCoreSchema
name|dc2
init|=
operator|new
name|DublinCoreSchema
argument_list|(
name|metadata
argument_list|,
literal|"dctest"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|addSchema
argument_list|(
name|dc2
argument_list|)
expr_stmt|;
name|XMPBasicSchema
name|xmp
init|=
name|metadata
operator|.
name|createAndAddXMPBasicSchema
argument_list|()
decl_stmt|;
name|XMPBasicSchema
name|xmp2
init|=
operator|new
name|XMPBasicSchema
argument_list|(
name|metadata
argument_list|,
literal|"xmptest"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|addSchema
argument_list|(
name|xmp2
argument_list|)
expr_stmt|;
name|AdobePDFSchema
name|pdf
init|=
name|metadata
operator|.
name|createAndAddAdobePDFSchema
argument_list|()
decl_stmt|;
name|AdobePDFSchema
name|pdf2
init|=
operator|new
name|AdobePDFSchema
argument_list|(
name|metadata
argument_list|,
literal|"pdftest"
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|addSchema
argument_list|(
name|pdf2
argument_list|)
expr_stmt|;
comment|// write some temp info in 'false' schemas
name|dc2
operator|.
name|setCoverage
argument_list|(
literal|"tmpcover"
argument_list|)
expr_stmt|;
name|xmp2
operator|.
name|setCreatorTool
argument_list|(
literal|"tmpcreator"
argument_list|)
expr_stmt|;
name|pdf2
operator|.
name|setKeywords
argument_list|(
literal|"tmpkeys"
argument_list|)
expr_stmt|;
comment|// Writing info in XMP and Document Information dictionary
comment|// TITLE
name|dico
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|dc
operator|.
name|setTitle
argument_list|(
literal|"x-default"
argument_list|,
name|title
argument_list|)
expr_stmt|;
comment|// AUTHOR
name|dico
operator|.
name|setAuthor
argument_list|(
name|author
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addCreator
argument_list|(
name|author
argument_list|)
expr_stmt|;
comment|// SUBJECT
name|dico
operator|.
name|setSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
name|dc
operator|.
name|addDescription
argument_list|(
literal|"x-default"
argument_list|,
name|subject
argument_list|)
expr_stmt|;
comment|// KEYWORDS
name|dico
operator|.
name|setKeywords
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setKeywords
argument_list|(
name|keywords
argument_list|)
expr_stmt|;
comment|// CREATOR
name|dico
operator|.
name|setCreator
argument_list|(
name|creator
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setCreatorTool
argument_list|(
name|creator
argument_list|)
expr_stmt|;
comment|// PRODUCER
name|dico
operator|.
name|setProducer
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|pdf
operator|.
name|setProducer
argument_list|(
name|producer
argument_list|)
expr_stmt|;
comment|// CREATION DATE
name|dico
operator|.
name|setCreationDate
argument_list|(
name|creationDate
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setCreateDate
argument_list|(
name|creationDate
argument_list|)
expr_stmt|;
comment|// MODIFY DATE
name|dico
operator|.
name|setModificationDate
argument_list|(
name|modifyDate
argument_list|)
expr_stmt|;
name|xmp
operator|.
name|setModifyDate
argument_list|(
name|modifyDate
argument_list|)
expr_stmt|;
comment|// Launching synchronization test
try|try
block|{
name|ve
operator|=
name|sync
operator|.
name|validateMetadataSynchronization
argument_list|(
name|doc
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|ve
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|After
specifier|public
name|void
name|checkErrors
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
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
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Error while closing PDF Document"
argument_list|)
throw|;
block|}
comment|/*          * Iterator<ValidationError> it=ve.iterator(); while(it.hasNext()){ ValidationError tmp=it.next();          * System.out.println("Error:"+ tmp.getDetails()+"\n code: "+tmp.getErrorCode()); }          */
block|}
block|}
end_class

end_unit

