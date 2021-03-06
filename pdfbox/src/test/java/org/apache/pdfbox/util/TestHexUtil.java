begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2016 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  *  * @author Michael Doswald  */
end_comment

begin_class
specifier|public
class|class
name|TestHexUtil
extends|extends
name|TestCase
block|{
comment|/**      * Test conversion from short to char[]      */
specifier|public
name|void
name|testGetCharsFromShortWithoutPassingInABuffer
parameter_list|()
block|{
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|}
argument_list|,
name|Hex
operator|.
name|getChars
argument_list|(
operator|(
name|short
operator|)
literal|0x0000
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'0'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'F'
block|}
argument_list|,
name|Hex
operator|.
name|getChars
argument_list|(
operator|(
name|short
operator|)
literal|0x000F
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'A'
block|,
literal|'B'
block|,
literal|'C'
block|,
literal|'D'
block|}
argument_list|,
name|Hex
operator|.
name|getChars
argument_list|(
operator|(
name|short
operator|)
literal|0xABCD
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'B'
block|,
literal|'A'
block|,
literal|'B'
block|,
literal|'E'
block|}
argument_list|,
name|Hex
operator|.
name|getChars
argument_list|(
operator|(
name|short
operator|)
literal|0xCAFEBABE
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Check conversion from String to a char[] which contains the UTF16-BE encoded      * bytes of the string as hex digits      *      */
specifier|public
name|void
name|testGetCharsUTF16BE
parameter_list|()
block|{
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'0'
block|,
literal|'0'
block|,
literal|'6'
block|,
literal|'1'
block|,
literal|'0'
block|,
literal|'0'
block|,
literal|'6'
block|,
literal|'2'
block|}
argument_list|,
name|Hex
operator|.
name|getCharsUTF16BE
argument_list|(
literal|"ab"
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|char
index|[]
block|{
literal|'5'
block|,
literal|'E'
block|,
literal|'2'
block|,
literal|'E'
block|,
literal|'5'
block|,
literal|'2'
block|,
literal|'A'
block|,
literal|'9'
block|}
argument_list|,
name|Hex
operator|.
name|getCharsUTF16BE
argument_list|(
literal|"帮助"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertArrayEquals
parameter_list|(
name|char
index|[]
name|expected
parameter_list|,
name|char
index|[]
name|actual
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Length of char array not equal"
argument_list|,
name|expected
operator|.
name|length
argument_list|,
name|actual
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|idx
init|=
literal|0
init|;
name|idx
operator|<
name|expected
operator|.
name|length
condition|;
name|idx
operator|++
control|)
block|{
if|if
condition|(
name|expected
index|[
name|idx
index|]
operator|!=
name|actual
index|[
name|idx
index|]
condition|)
block|{
name|fail
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Character at index %d not equal. Expected '%c' but got '%c'"
argument_list|,
name|idx
argument_list|,
name|expected
index|[
name|idx
index|]
argument_list|,
name|actual
index|[
name|idx
index|]
argument_list|)
argument_list|)
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
name|TestHexUtil
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
name|TestHexUtil
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

