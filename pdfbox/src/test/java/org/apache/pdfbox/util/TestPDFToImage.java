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
name|util
package|;
end_package

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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FilenameFilter
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
name|Test
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
name|junit
operator|.
name|framework
operator|.
name|TestSuite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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

begin_comment
comment|/**  * Test suite for PDFTextStripper.  *  * FILE SET VALIDATION  *  * This test suite is designed to test PDFToImage using a set of PDF  * files and known good output for each.  The default mode of testAll()  * is to process each *.pdf file in "src/test/resources/input/rendering".  An output file is  * created in "target/test-output/rendering" with the same name as the PDF file, plus an  * additional page number and ".png" suffix.  *  * The output file is then tested against a known good result file from  * the input directory (again, with the same name as the tested PDF file,  * but with the additional page number and ".png" suffix).  *  * Currently, testing against known output is simply a byte-for-byte comparison  *  *In the future, testing against the known output may be accomplished using PerceptualDiff  *  http://sourceforge.net/projects/pdiff  *  *  * @author<a href="mailto:DanielWilson@Users.Sourceforge.net">Daniel Wilson</a>  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|TestPDFToImage
extends|extends
name|TestCase
block|{
comment|/**      * Logger instance.      */
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|TestPDFToImage
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|bFail
init|=
literal|false
decl_stmt|;
specifier|private
name|PDFImageWriter
name|writer
init|=
literal|null
decl_stmt|;
specifier|private
name|File
name|mcurFile
init|=
literal|null
decl_stmt|;
comment|/**      * Test class constructor.      *      * @param name The name of the test class.      *      * @throws IOException If there is an error creating the test.      */
specifier|public
name|TestPDFToImage
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|writer
operator|=
operator|new
name|PDFImageWriter
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test suite setup.      */
specifier|public
name|void
name|setUp
parameter_list|()
block|{
comment|// If you want to test a single file using DEBUG logging, from an IDE,
comment|// you can do something like this:
comment|//
comment|// System.setProperty("org.apache.pdfbox.util.TextStripper.file", "FVS318Ref.pdf");
block|}
comment|/**      * Validate text extraction on a single file.      *      * @param file The file to validate      * @param bLogResult Whether to log the extracted text      * @param inDir Name of the input directory      * @param outDir Name of the output directory      * @throws Exception when there is an exception      */
specifier|public
name|void
name|doTestFile
parameter_list|(
name|File
name|file
parameter_list|,
name|boolean
name|bLogResult
parameter_list|,
name|String
name|inDir
parameter_list|,
name|String
name|outDir
parameter_list|)
throws|throws
name|Exception
block|{
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Preparing to convert "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeImage
argument_list|(
name|document
argument_list|,
literal|"png"
argument_list|,
literal|""
argument_list|,
literal|1
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|outDir
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|"-"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|bFail
operator|=
literal|true
expr_stmt|;
name|log
operator|.
name|error
argument_list|(
literal|"Error converting file "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|//Now check the resulting files ... did we get identical PNG(s)?
try|try
block|{
name|mcurFile
operator|=
name|file
expr_stmt|;
name|File
index|[]
name|outFiles
init|=
operator|new
name|File
argument_list|(
name|outDir
argument_list|)
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".png"
argument_list|)
operator|&&
name|name
operator|.
name|startsWith
argument_list|(
name|mcurFile
operator|.
name|getName
argument_list|()
argument_list|,
literal|0
argument_list|)
operator|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<
name|outFiles
operator|.
name|length
condition|;
name|n
operator|++
control|)
block|{
name|File
name|inFile
init|=
operator|new
name|File
argument_list|(
name|inDir
operator|+
literal|'/'
operator|+
name|outFiles
index|[
name|n
index|]
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|inFile
operator|.
name|exists
argument_list|()
operator|||
operator|!
name|filesAreIdentical
argument_list|(
name|outFiles
index|[
name|n
index|]
argument_list|,
name|inFile
argument_list|)
condition|)
block|{
name|this
operator|.
name|bFail
operator|=
literal|true
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"*** TEST FAILURE *** Input and output not identical for file: "
operator|+
name|inFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|this
operator|.
name|bFail
operator|=
literal|true
expr_stmt|;
name|log
operator|.
name|error
argument_list|(
literal|"Error comparing file output for "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test to validate image rendering of file set.      *      * @throws Exception when there is an exception      */
specifier|public
name|void
name|testRenderImage
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|filename
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.pdfbox.util.TextStripper.file"
argument_list|)
decl_stmt|;
name|String
name|inDir
init|=
literal|"src/test/resources/input/rendering"
decl_stmt|;
name|String
name|outDir
init|=
literal|"target/test-output/rendering/"
decl_stmt|;
name|String
name|inDirExt
init|=
literal|"target/test-input-ext/rendering"
decl_stmt|;
name|String
name|outDirExt
init|=
literal|"target/test-output-ext/rendering"
decl_stmt|;
if|if
condition|(
operator|(
name|filename
operator|==
literal|null
operator|)
operator|||
operator|(
name|filename
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|)
condition|)
block|{
name|File
index|[]
name|testFiles
init|=
operator|new
name|File
argument_list|(
name|inDir
argument_list|)
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".pdf"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".ai"
argument_list|)
operator|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<
name|testFiles
operator|.
name|length
condition|;
name|n
operator|++
control|)
block|{
name|doTestFile
argument_list|(
name|testFiles
index|[
name|n
index|]
argument_list|,
literal|false
argument_list|,
name|inDir
argument_list|,
name|outDir
argument_list|)
expr_stmt|;
block|}
name|testFiles
operator|=
operator|new
name|File
argument_list|(
name|inDirExt
argument_list|)
operator|.
name|listFiles
argument_list|(
operator|new
name|FilenameFilter
argument_list|()
block|{
specifier|public
name|boolean
name|accept
parameter_list|(
name|File
name|dir
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".pdf"
argument_list|)
operator|||
name|name
operator|.
name|endsWith
argument_list|(
literal|".ai"
argument_list|)
operator|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|testFiles
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|n
init|=
literal|0
init|;
name|n
operator|<
name|testFiles
operator|.
name|length
condition|;
name|n
operator|++
control|)
block|{
name|doTestFile
argument_list|(
name|testFiles
index|[
name|n
index|]
argument_list|,
literal|false
argument_list|,
name|inDirExt
argument_list|,
name|outDirExt
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|doTestFile
argument_list|(
operator|new
name|File
argument_list|(
name|inDir
argument_list|,
name|filename
argument_list|)
argument_list|,
literal|true
argument_list|,
name|inDir
argument_list|,
name|outDir
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|bFail
condition|)
block|{
name|fail
argument_list|(
literal|"One or more failures, see test log for details"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Set the tests in the suite for this test class.      *      * @return the Suite.      */
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
return|return
operator|new
name|TestSuite
argument_list|(
name|TestPDFToImage
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Command line execution.      *      * @param args Command line arguments.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|String
index|[]
name|arg
init|=
block|{
name|TestPDFToImage
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
name|junit
operator|.
name|textui
operator|.
name|TestRunner
operator|.
name|main
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|filesAreIdentical
parameter_list|(
name|File
name|left
parameter_list|,
name|File
name|right
parameter_list|)
throws|throws
name|IOException
block|{
comment|//http://forum.java.sun.com/thread.jspa?threadID=688105&messageID=4003259
comment|/* -- I reworked ASSERT's into IF statement -- dwilson         assert left != null;         assert right != null;         assert left.exists();         assert right.exists();         */
if|if
condition|(
name|left
operator|!=
literal|null
operator|&&
name|right
operator|!=
literal|null
operator|&&
name|left
operator|.
name|exists
argument_list|()
operator|&&
name|right
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
name|left
operator|.
name|length
argument_list|()
operator|!=
name|right
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|FileInputStream
name|lin
init|=
operator|new
name|FileInputStream
argument_list|(
name|left
argument_list|)
decl_stmt|;
name|FileInputStream
name|rin
init|=
operator|new
name|FileInputStream
argument_list|(
name|right
argument_list|)
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|lbuffer
init|=
operator|new
name|byte
index|[
literal|4096
index|]
decl_stmt|;
name|byte
index|[]
name|rbuffer
init|=
operator|new
name|byte
index|[
name|lbuffer
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|lcount
init|=
literal|0
init|;
operator|(
name|lcount
operator|=
name|lin
operator|.
name|read
argument_list|(
name|lbuffer
argument_list|)
operator|)
operator|>
literal|0
condition|;
control|)
block|{
name|int
name|bytesRead
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|rcount
init|=
literal|0
init|;
operator|(
name|rcount
operator|=
name|rin
operator|.
name|read
argument_list|(
name|rbuffer
argument_list|,
name|bytesRead
argument_list|,
name|lcount
operator|-
name|bytesRead
argument_list|)
operator|)
operator|>
literal|0
condition|;
control|)
block|{
name|bytesRead
operator|+=
name|rcount
expr_stmt|;
block|}
for|for
control|(
name|int
name|byteIndex
init|=
literal|0
init|;
name|byteIndex
operator|<
name|lcount
condition|;
name|byteIndex
operator|++
control|)
block|{
if|if
condition|(
name|lbuffer
index|[
name|byteIndex
index|]
operator|!=
name|rbuffer
index|[
name|byteIndex
index|]
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
finally|finally
block|{
name|lin
operator|.
name|close
argument_list|()
expr_stmt|;
name|rin
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

