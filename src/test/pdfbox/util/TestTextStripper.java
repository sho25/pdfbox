begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *   *      http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|test
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
name|FileWriter
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
name|FileOutputStream
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
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|LineNumberReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|pdfbox
operator|.
name|util
operator|.
name|PDFTextStripper
import|;
end_import

begin_comment
comment|/**  * Test suite for PDFTextStripper.  *  * FILE SET VALIDATION  *  * This test suite is designed to test PDFTextStripper using a set of PDF  * files and known good output for each.  The default mode of testAll()  * is to process each *.pdf file in "test/input".  An output file is  * created in "test/output" with the same name as the PDF file, plus an  * additional ".txt" suffix.  *  * The output file is then tested against a known good result file from  * the input directory (again, with the same name as the tested PDF file,  * but with the additional ".txt" suffix).  *  * So for the file "test/input/hello.pdf", an output file will be generated  * named "test/output/hello.pdf.txt".  Then that file will be compared to  * the known good file "test/input/hello.pdf.txt", if it exists.  *  * Any errors are logged, and at the end of processing all *.pdf files, if  * there were any errors, the test fails.  The logging is at INFO, as the  * general goal is overall validation, and on failure, the indication of  * which file or files failed.  *  * When processing new PDF files, you may use testAll() to generate output,  * verify the output manually, then move the output file to the test input  * directory to use as the basis for future validations.  *  * SINGLE FILE VALIDATION  *  * To further research individual failures, the test.pdfbox.util.TextStripper.file  * system property may be set with the name of a single file in the "test/input"  * directory.  In this mode, testAll() will evaluate only that file, and will  * do so with DEBUG level logging.  You can set this property from ant by  * defining "file", as in:  *  *    ant testextract -Dfile=hello.pdf  *  * @author Robert Dickinson (bob@brutesquadlabs.com)  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.19 $  */
end_comment

begin_class
specifier|public
class|class
name|TestTextStripper
extends|extends
name|TestCase
block|{
specifier|private
name|boolean
name|bFail
init|=
literal|false
decl_stmt|;
specifier|private
name|PDFTextStripper
name|stripper
init|=
literal|null
decl_stmt|;
specifier|private
name|PrintWriter
name|log
init|=
literal|null
decl_stmt|;
comment|/**      * Test class constructor.      *      * @param name The name of the test class.      *       * @throws IOException If there is an error creating the test.      */
specifier|public
name|TestTextStripper
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
name|stripper
operator|=
operator|new
name|PDFTextStripper
argument_list|()
expr_stmt|;
name|stripper
operator|.
name|setLineSeparator
argument_list|(
literal|"\n"
argument_list|)
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
comment|// System.setProperty("test.pdfbox.util.TextStripper.file", "FVS318Ref.pdf");
block|}
comment|/**      * Determine whether two strings are equal, where two null strings are      * considered equal.      *      * @param expected Excpected string      * @param actual Actual String      * @return<code>true</code> is the strings are both null,      * or if their contents are the same, otherwise<code>false</code>.      */
specifier|private
name|boolean
name|stringsEqual
parameter_list|(
name|PrintWriter
name|log
parameter_list|,
name|String
name|expected
parameter_list|,
name|String
name|actual
parameter_list|)
block|{
name|boolean
name|equals
init|=
literal|true
decl_stmt|;
if|if
condition|(
operator|(
name|expected
operator|==
literal|null
operator|)
operator|&&
operator|(
name|actual
operator|==
literal|null
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|expected
operator|!=
literal|null
operator|&&
name|actual
operator|!=
literal|null
condition|)
block|{
name|expected
operator|=
name|expected
operator|.
name|trim
argument_list|()
expr_stmt|;
name|actual
operator|=
name|actual
operator|.
name|trim
argument_list|()
expr_stmt|;
name|char
index|[]
name|expectedArray
init|=
name|expected
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|char
index|[]
name|actualArray
init|=
name|actual
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|int
name|expectedIndex
init|=
literal|0
decl_stmt|;
name|int
name|actualIndex
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|expectedIndex
operator|<
name|expectedArray
operator|.
name|length
operator|&&
name|actualIndex
operator|<
name|actualArray
operator|.
name|length
condition|)
block|{
if|if
condition|(
name|expectedArray
index|[
name|expectedIndex
index|]
operator|!=
name|actualArray
index|[
name|actualIndex
index|]
condition|)
block|{
name|equals
operator|=
literal|false
expr_stmt|;
name|log
operator|.
name|println
argument_list|(
literal|"Lines differ at index"
operator|+
literal|" expected:"
operator|+
name|expectedIndex
operator|+
literal|"-"
operator|+
operator|(
name|int
operator|)
name|expectedArray
index|[
name|expectedIndex
index|]
operator|+
literal|" actual:"
operator|+
name|actualIndex
operator|+
literal|"-"
operator|+
operator|(
name|int
operator|)
name|actualArray
index|[
name|actualIndex
index|]
argument_list|)
expr_stmt|;
break|break;
block|}
name|expectedIndex
operator|=
name|skipWhitespace
argument_list|(
name|expectedArray
argument_list|,
name|expectedIndex
argument_list|)
expr_stmt|;
name|actualIndex
operator|=
name|skipWhitespace
argument_list|(
name|actualArray
argument_list|,
name|actualIndex
argument_list|)
expr_stmt|;
name|expectedIndex
operator|++
expr_stmt|;
name|actualIndex
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|equals
condition|)
block|{
if|if
condition|(
name|expectedIndex
operator|!=
name|expectedArray
operator|.
name|length
condition|)
block|{
name|equals
operator|=
literal|false
expr_stmt|;
name|log
operator|.
name|println
argument_list|(
literal|"Expected line is longer at:"
operator|+
name|expectedIndex
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|actualIndex
operator|!=
name|actualArray
operator|.
name|length
condition|)
block|{
name|equals
operator|=
literal|false
expr_stmt|;
name|log
operator|.
name|println
argument_list|(
literal|"Actual line is longer at:"
operator|+
name|actualIndex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
operator|(
name|expected
operator|==
literal|null
operator|&&
name|actual
operator|!=
literal|null
operator|&&
name|actual
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|)
operator|||
operator|(
name|actual
operator|==
literal|null
operator|&&
name|expected
operator|!=
literal|null
operator|&&
name|expected
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
operator|)
condition|)
block|{
comment|//basically there are some cases where pdfbox will put an extra line
comment|//at the end of the file, who cares, this is not enough to report
comment|// a failure
name|equals
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|equals
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|equals
return|;
block|}
comment|/**      * If the current index is whitespace then skip any subsequent whitespace.      */
specifier|private
name|int
name|skipWhitespace
parameter_list|(
name|char
index|[]
name|array
parameter_list|,
name|int
name|index
parameter_list|)
block|{
comment|//if we are at a space character then skip all space
comment|//characters, but when all done rollback 1 because stringsEqual
comment|//will roll forward 1
if|if
condition|(
name|array
index|[
name|index
index|]
operator|==
literal|' '
operator|||
name|array
index|[
name|index
index|]
operator|>
literal|256
condition|)
block|{
while|while
condition|(
name|index
operator|<
name|array
operator|.
name|length
operator|&&
operator|(
name|array
index|[
name|index
index|]
operator|==
literal|' '
operator|||
name|array
index|[
name|index
index|]
operator|>
literal|256
operator|)
condition|)
block|{
name|index
operator|++
expr_stmt|;
block|}
name|index
operator|--
expr_stmt|;
block|}
return|return
name|index
return|;
block|}
comment|/**      * Validate text extraction on a single file.      *      * @param file The file to validate      * @param bLogResult Whether to log the extracted text      * @throws Exception when there is an exception      */
specifier|public
name|void
name|doTestFile
parameter_list|(
name|File
name|file
parameter_list|,
name|boolean
name|bLogResult
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|println
argument_list|(
literal|"Preparing to parse "
operator|+
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
literal|null
decl_stmt|;
name|Writer
name|writer
init|=
literal|null
decl_stmt|;
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
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
name|File
name|outFile
init|=
operator|new
name|File
argument_list|(
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
argument_list|,
literal|"output/"
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".txt"
argument_list|)
decl_stmt|;
name|os
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|0xFF
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|0xFE
argument_list|)
expr_stmt|;
name|writer
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|,
literal|"UTF-16LE"
argument_list|)
expr_stmt|;
name|stripper
operator|.
name|writeText
argument_list|(
name|document
argument_list|,
name|writer
argument_list|)
expr_stmt|;
if|if
condition|(
name|bLogResult
condition|)
block|{
name|log
operator|.
name|println
argument_list|(
literal|"Text for "
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|":\r\n"
operator|+
name|stripper
operator|.
name|getText
argument_list|(
name|document
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
argument_list|,
literal|"input/"
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".txt"
argument_list|)
decl_stmt|;
name|File
name|actualFile
init|=
operator|new
name|File
argument_list|(
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|getParentFile
argument_list|()
argument_list|,
literal|"output/"
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|".txt"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|expectedFile
operator|.
name|exists
argument_list|()
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
name|println
argument_list|(
literal|"FAILURE: Input verification file: "
operator|+
name|expectedFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" did not exist"
argument_list|)
expr_stmt|;
return|return;
block|}
name|LineNumberReader
name|expectedReader
init|=
operator|new
name|LineNumberReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|expectedFile
argument_list|)
argument_list|,
literal|"UTF-16LE"
argument_list|)
argument_list|)
decl_stmt|;
name|LineNumberReader
name|actualReader
init|=
operator|new
name|LineNumberReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|actualFile
argument_list|)
argument_list|,
literal|"UTF-16LE"
argument_list|)
argument_list|)
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|String
name|expectedLine
init|=
name|expectedReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|expectedLine
operator|!=
literal|null
operator|&&
name|expectedLine
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|expectedLine
operator|=
name|expectedReader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|String
name|actualLine
init|=
name|actualReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|actualLine
operator|!=
literal|null
operator|&&
name|actualLine
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|actualLine
operator|=
name|actualReader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|stringsEqual
argument_list|(
name|log
argument_list|,
name|expectedLine
argument_list|,
name|actualLine
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
name|println
argument_list|(
literal|"FAILURE: Line mismatch for file "
operator|+
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|" at expected line: "
operator|+
name|expectedReader
operator|.
name|getLineNumber
argument_list|()
operator|+
literal|" at actual line: "
operator|+
name|actualReader
operator|.
name|getLineNumber
argument_list|()
operator|+
literal|"\r\n  expected line was: \""
operator|+
name|expectedLine
operator|+
literal|"\""
operator|+
literal|"\r\n  actual line was:   \""
operator|+
name|actualLine
operator|+
literal|"\""
argument_list|)
expr_stmt|;
comment|//lets report all lines, even though this might produce some verbose logging
comment|//break;
block|}
if|if
condition|(
name|expectedLine
operator|==
literal|null
operator|||
name|actualLine
operator|==
literal|null
condition|)
block|{
break|break;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|os
operator|!=
literal|null
condition|)
block|{
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Test to validate text extraction of file set.      *      * @throws Exception when there is an exception      */
specifier|public
name|void
name|testExtract
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
literal|"test.pdfbox.util.TextStripper.file"
argument_list|)
decl_stmt|;
name|File
name|testDir
init|=
operator|new
name|File
argument_list|(
literal|"test/input"
argument_list|)
decl_stmt|;
try|try
block|{
name|log
operator|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|FileWriter
argument_list|(
literal|"textextract.log"
argument_list|)
argument_list|)
expr_stmt|;
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
name|testDir
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
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|doTestFile
argument_list|(
operator|new
name|File
argument_list|(
name|testDir
argument_list|,
name|filename
argument_list|)
argument_list|,
literal|true
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
finally|finally
block|{
if|if
condition|(
name|log
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
name|TestTextStripper
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Command line execution.      *       * @param args Command line arguments.      */
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
name|TestTextStripper
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
block|}
end_class

end_unit

