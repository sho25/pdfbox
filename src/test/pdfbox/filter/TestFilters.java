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
name|filter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|filter
operator|.
name|DCTFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|filter
operator|.
name|CCITTFaxDecodeFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|filter
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|filter
operator|.
name|FilterManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|filter
operator|.
name|RunLengthDecodeFilter
import|;
end_import

begin_comment
comment|/**  * This will test all of the filters in the PDFBox system.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.8 $  */
end_comment

begin_class
specifier|public
class|class
name|TestFilters
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|int
name|BUFFER_SIZE
init|=
literal|2048
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|COSDictionary
name|EMPTY_DICTIONARY
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
comment|/**      * Constructor.      *      * @param name The name of the test to run.      */
specifier|public
name|TestFilters
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the suite of test that this class holds.      *      * @return All of the tests that this class holds.      */
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
name|TestFilters
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * This will test all of the filters in the system.      *      * @throws IOException If there is an exception while encoding.      */
specifier|public
name|void
name|testFilters
parameter_list|()
throws|throws
name|IOException
block|{
name|FilterManager
name|manager
init|=
operator|new
name|FilterManager
argument_list|()
decl_stmt|;
name|Collection
name|filters
init|=
name|manager
operator|.
name|getFilters
argument_list|()
decl_stmt|;
name|Iterator
name|filterIter
init|=
name|filters
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|filterIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|Filter
name|filter
init|=
operator|(
name|Filter
operator|)
name|filterIter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|filter
operator|instanceof
name|DCTFilter
operator|||
name|filter
operator|instanceof
name|CCITTFaxDecodeFilter
operator|||
name|filter
operator|instanceof
name|RunLengthDecodeFilter
operator|)
condition|)
block|{
name|checkFilter
argument_list|(
operator|new
name|File
argument_list|(
literal|"classes"
argument_list|)
argument_list|,
name|filter
argument_list|)
expr_stmt|;
name|long
name|stop
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Time for filter "
operator|+
name|filter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"="
operator|+
operator|(
name|stop
operator|-
name|start
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will check the filter.      *      * @param file The file or directory to test.      * @param filter The filter to check.      *      * @throws IOException If there is an exception while encoding.      */
specifier|private
name|void
name|checkFilter
parameter_list|(
name|File
name|file
parameter_list|,
name|Filter
name|filter
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|subFiles
init|=
name|file
operator|.
name|listFiles
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|subFiles
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|checkFilter
argument_list|(
name|subFiles
index|[
name|i
index|]
argument_list|,
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|ByteArrayOutputStream
name|output
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|encoded
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|decoded
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|FileInputStream
name|fin
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|int
name|amountRead
init|=
literal|0
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|BUFFER_SIZE
index|]
decl_stmt|;
while|while
condition|(
operator|(
name|amountRead
operator|=
name|fin
operator|.
name|read
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|BUFFER_SIZE
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|amountRead
argument_list|)
expr_stmt|;
block|}
name|fin
operator|.
name|close
argument_list|()
expr_stmt|;
name|byte
index|[]
name|original
init|=
name|output
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|filter
operator|.
name|encode
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|original
argument_list|)
argument_list|,
name|encoded
argument_list|,
name|EMPTY_DICTIONARY
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|filter
operator|.
name|decode
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|encoded
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
name|decoded
argument_list|,
name|EMPTY_DICTIONARY
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cmpArray
argument_list|(
name|original
argument_list|,
name|decoded
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|filter
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will compare a couple of arrays and fail if they do not match.      *      * @param firstArray The first array.      * @param secondArray The second array.      * @param filter The filter that did the encoding.      * @param file The file that was encoded.      */
specifier|private
name|void
name|cmpArray
parameter_list|(
name|byte
index|[]
name|firstArray
parameter_list|,
name|byte
index|[]
name|secondArray
parameter_list|,
name|Filter
name|filter
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|String
name|fileMsg
init|=
name|filter
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|firstArray
operator|.
name|length
operator|!=
name|secondArray
operator|.
name|length
condition|)
block|{
name|fail
argument_list|(
literal|"The array lengths do not match for "
operator|+
name|fileMsg
operator|+
literal|", firstArray length was: "
operator|+
name|firstArray
operator|.
name|length
operator|+
literal|", secondArray length was: "
operator|+
name|secondArray
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|firstArray
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|firstArray
index|[
name|i
index|]
operator|!=
name|secondArray
index|[
name|i
index|]
condition|)
block|{
name|fail
argument_list|(
literal|"Array data does not match "
operator|+
name|fileMsg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

