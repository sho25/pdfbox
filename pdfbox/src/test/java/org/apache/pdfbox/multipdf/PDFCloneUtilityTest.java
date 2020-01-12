begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|multipdf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|pdfparser
operator|.
name|PDFParser
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
name|PDPage
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
name|PDPageContentStream
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
name|PDPageContentStream
operator|.
name|AppendMode
import|;
end_import

begin_comment
comment|/**  * Test suite for PDFCloneUtility, see PDFBOX-2052.  *  * @author Cornelis Hoeflake  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|PDFCloneUtilityTest
extends|extends
name|TestCase
block|{
comment|/**      * original (minimal) test from PDFBOX-2052.      *       * @throws IOException       */
specifier|public
name|void
name|testClonePDFWithCosArrayStream
parameter_list|()
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|srcDoc
init|=
operator|new
name|PDDocument
argument_list|()
init|;
name|PDDocument
name|dstDoc
operator|=
operator|new
name|PDDocument
argument_list|()
init|)
block|{
name|PDPage
name|pdPage
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|srcDoc
operator|.
name|addPage
argument_list|(
name|pdPage
argument_list|)
expr_stmt|;
operator|new
name|PDPageContentStream
argument_list|(
name|srcDoc
argument_list|,
name|pdPage
argument_list|,
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|true
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
operator|new
name|PDPageContentStream
argument_list|(
name|srcDoc
argument_list|,
name|pdPage
argument_list|,
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|true
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
operator|new
name|PDFCloneUtility
argument_list|(
name|dstDoc
argument_list|)
operator|.
name|cloneForNewDocument
argument_list|(
name|pdPage
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * broader test that saves to a real PDF document.      *       * @throws IOException       */
specifier|public
name|void
name|testClonePDFWithCosArrayStream2
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|String
name|TESTDIR
init|=
literal|"target/test-output/clone/"
decl_stmt|;
specifier|final
name|String
name|CLONESRC
init|=
literal|"clone-src.pdf"
decl_stmt|;
specifier|final
name|String
name|CLONEDST
init|=
literal|"clone-dst.pdf"
decl_stmt|;
operator|new
name|File
argument_list|(
name|TESTDIR
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|PDDocument
name|srcDoc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|PDPage
name|pdPage
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|srcDoc
operator|.
name|addPage
argument_list|(
name|pdPage
argument_list|)
expr_stmt|;
try|try
init|(
name|PDPageContentStream
name|pdPageContentStream1
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|srcDoc
argument_list|,
name|pdPage
argument_list|,
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|false
argument_list|)
init|)
block|{
name|pdPageContentStream1
operator|.
name|setNonStrokingColor
argument_list|(
name|Color
operator|.
name|black
argument_list|)
expr_stmt|;
name|pdPageContentStream1
operator|.
name|addRect
argument_list|(
literal|100
argument_list|,
literal|600
argument_list|,
literal|300
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|pdPageContentStream1
operator|.
name|fill
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|PDPageContentStream
name|pdPageContentStream2
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|srcDoc
argument_list|,
name|pdPage
argument_list|,
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|false
argument_list|)
init|)
block|{
name|pdPageContentStream2
operator|.
name|setNonStrokingColor
argument_list|(
name|Color
operator|.
name|red
argument_list|)
expr_stmt|;
name|pdPageContentStream2
operator|.
name|addRect
argument_list|(
literal|100
argument_list|,
literal|500
argument_list|,
literal|300
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|pdPageContentStream2
operator|.
name|fill
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|PDPageContentStream
name|pdPageContentStream3
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|srcDoc
argument_list|,
name|pdPage
argument_list|,
name|AppendMode
operator|.
name|APPEND
argument_list|,
literal|false
argument_list|)
init|)
block|{
name|pdPageContentStream3
operator|.
name|setNonStrokingColor
argument_list|(
name|Color
operator|.
name|yellow
argument_list|)
expr_stmt|;
name|pdPageContentStream3
operator|.
name|addRect
argument_list|(
literal|100
argument_list|,
literal|400
argument_list|,
literal|300
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|pdPageContentStream3
operator|.
name|fill
argument_list|()
expr_stmt|;
block|}
name|srcDoc
operator|.
name|save
argument_list|(
name|TESTDIR
operator|+
name|CLONESRC
argument_list|)
expr_stmt|;
name|PDFMergerUtility
name|merger
init|=
operator|new
name|PDFMergerUtility
argument_list|()
decl_stmt|;
name|PDDocument
name|dstDoc
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
comment|// this calls PDFCloneUtility.cloneForNewDocument(),
comment|// which would fail before the fix in PDFBOX-2052
name|merger
operator|.
name|appendDocument
argument_list|(
name|dstDoc
argument_list|,
name|srcDoc
argument_list|)
expr_stmt|;
comment|// save and reload PDF, so that one can see that the files are legit
name|dstDoc
operator|.
name|save
argument_list|(
name|TESTDIR
operator|+
name|CLONEDST
argument_list|)
expr_stmt|;
name|PDFParser
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|TESTDIR
operator|+
name|CLONESRC
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDFParser
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|TESTDIR
operator|+
name|CLONESRC
argument_list|)
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDFParser
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|TESTDIR
operator|+
name|CLONEDST
argument_list|)
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
name|PDFParser
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|TESTDIR
operator|+
name|CLONEDST
argument_list|)
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

