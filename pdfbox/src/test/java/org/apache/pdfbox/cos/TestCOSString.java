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
name|cos
package|;
end_package

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
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|TestSuite
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
name|pdfwriter
operator|.
name|COSWriter
import|;
end_import

begin_comment
comment|/**  * This will test all of the filters in the PDFBox system.  *  * Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|TestCOSString
extends|extends
name|TestCOSBase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ESC_CHAR_STRING
init|=
literal|"( test#some) escaped< \\chars>!~1239857 "
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ESC_CHAR_STRING_PDF_FORMAT
init|=
literal|"\\( test#some\\) escaped< \\\\chars>!~1239857 "
decl_stmt|;
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
name|TestCOSString
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|testCOSBase
operator|=
operator|new
name|COSString
argument_list|(
literal|"test cos string"
argument_list|)
expr_stmt|;
block|}
comment|/**      * infamous main method.      *      * @param args The command line arguments.      */
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
name|TestCOSString
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
comment|/**      * Test setForceHexForm() and setForceLiteralForm() - tests these two methods do enforce the      * different String output forms within PDF.       */
specifier|public
name|void
name|testSetForceHexLiteralForm
parameter_list|()
block|{
name|String
name|inputString
init|=
literal|"Test with a text and a few numbers 1, 2 and 3"
decl_stmt|;
name|String
name|pdfHex
init|=
literal|"<"
operator|+
name|createHex
argument_list|(
name|inputString
argument_list|)
operator|+
literal|">"
decl_stmt|;
name|COSString
name|cosStr
init|=
operator|new
name|COSString
argument_list|(
name|inputString
argument_list|)
decl_stmt|;
name|cosStr
operator|.
name|setForceHexForm
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|writePDFTests
argument_list|(
name|pdfHex
argument_list|,
name|cosStr
argument_list|)
expr_stmt|;
name|COSString
name|escStr
init|=
operator|new
name|COSString
argument_list|(
name|ESC_CHAR_STRING
argument_list|)
decl_stmt|;
name|writePDFTests
argument_list|(
literal|"("
operator|+
name|ESC_CHAR_STRING_PDF_FORMAT
operator|+
literal|")"
argument_list|,
name|escStr
argument_list|)
expr_stmt|;
name|escStr
operator|.
name|setForceHexForm
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Escape characters not escaped in hex version
name|writePDFTests
argument_list|(
literal|"<"
operator|+
name|createHex
argument_list|(
name|ESC_CHAR_STRING
argument_list|)
operator|+
literal|">"
argument_list|,
name|escStr
argument_list|)
expr_stmt|;
block|}
comment|/**      * Helper method for testing writePDF().      *       * @param expected the String expected when writePDF() is invoked      * @param testSubj the test subject      */
specifier|private
name|void
name|writePDFTests
parameter_list|(
name|String
name|expected
parameter_list|,
name|COSString
name|testSubj
parameter_list|)
block|{
name|ByteArrayOutputStream
name|outStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|COSWriter
operator|.
name|writeString
argument_list|(
name|testSubj
argument_list|,
name|outStream
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
literal|"IOException: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expected
argument_list|,
name|outStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test parseHex() - tests that the proper String is created from a hex string input.      */
specifier|public
name|void
name|testFromHex
parameter_list|()
block|{
name|String
name|expected
init|=
literal|"Quick and simple test"
decl_stmt|;
name|String
name|hexForm
init|=
name|createHex
argument_list|(
name|expected
argument_list|)
decl_stmt|;
try|try
block|{
name|COSString
name|test1
init|=
name|COSString
operator|.
name|parseHex
argument_list|(
name|hexForm
argument_list|)
decl_stmt|;
name|writePDFTests
argument_list|(
literal|"("
operator|+
name|expected
operator|+
literal|")"
argument_list|,
name|test1
argument_list|)
expr_stmt|;
name|COSString
name|test2
init|=
name|COSString
operator|.
name|parseHex
argument_list|(
name|createHex
argument_list|(
name|ESC_CHAR_STRING
argument_list|)
argument_list|)
decl_stmt|;
name|writePDFTests
argument_list|(
literal|"("
operator|+
name|ESC_CHAR_STRING_PDF_FORMAT
operator|+
literal|")"
argument_list|,
name|test2
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
literal|"IOException thrown: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|COSString
operator|.
name|parseHex
argument_list|(
name|hexForm
operator|+
literal|"xx"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an IOException here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// PASS
block|}
block|}
specifier|private
name|String
name|createHex
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|str
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|c
argument_list|,
literal|16
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|toUpperCase
argument_list|()
return|;
block|}
comment|/**      * Tests getHex() - ensure the hex String returned is properly formatted.      */
specifier|public
name|void
name|testGetHex
parameter_list|()
block|{
name|String
name|expected
init|=
literal|"Test subject for testing getHex"
decl_stmt|;
name|COSString
name|test1
init|=
operator|new
name|COSString
argument_list|(
name|expected
argument_list|)
decl_stmt|;
name|String
name|hexForm
init|=
name|createHex
argument_list|(
name|expected
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|hexForm
argument_list|,
name|test1
operator|.
name|toHexString
argument_list|()
argument_list|)
expr_stmt|;
name|COSString
name|escCS
init|=
operator|new
name|COSString
argument_list|(
name|ESC_CHAR_STRING
argument_list|)
decl_stmt|;
comment|// Not sure whether the escaped characters should be escaped or not, presumably since
comment|// writePDF() gives you the proper formatted text, getHex() should ONLY convert to hex.
name|assertEquals
argument_list|(
name|createHex
argument_list|(
name|ESC_CHAR_STRING
argument_list|)
argument_list|,
name|escCS
operator|.
name|toHexString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test testGetString() - ensure getString() are returned in the correct format.      */
specifier|public
name|void
name|testGetString
parameter_list|()
block|{
try|try
block|{
name|String
name|testStr
init|=
literal|"Test subject for getString()"
decl_stmt|;
name|COSString
name|test1
init|=
operator|new
name|COSString
argument_list|(
name|testStr
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|testStr
argument_list|,
name|test1
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
name|COSString
name|hexStr
init|=
name|COSString
operator|.
name|parseHex
argument_list|(
name|createHex
argument_list|(
name|testStr
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|testStr
argument_list|,
name|hexStr
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
name|COSString
name|escapedString
init|=
operator|new
name|COSString
argument_list|(
name|ESC_CHAR_STRING
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|ESC_CHAR_STRING
argument_list|,
name|escapedString
operator|.
name|getString
argument_list|()
argument_list|)
expr_stmt|;
name|testStr
operator|=
literal|"Line1\nLine2\nLine3\n"
expr_stmt|;
name|COSString
name|lineFeedString
init|=
operator|new
name|COSString
argument_list|(
name|testStr
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|testStr
argument_list|,
name|lineFeedString
operator|.
name|getString
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
literal|"IOException thrown: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test getBytes() - again not much to test, just ensure the proper byte array is returned.      */
specifier|public
name|void
name|testGetBytes
parameter_list|()
block|{
name|COSString
name|str
init|=
operator|new
name|COSString
argument_list|(
name|ESC_CHAR_STRING
argument_list|)
decl_stmt|;
name|testByteArrays
argument_list|(
name|ESC_CHAR_STRING
operator|.
name|getBytes
argument_list|()
argument_list|,
name|str
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests writePDF() - tests that the string is in PDF format.      */
specifier|public
name|void
name|testWritePDF
parameter_list|()
block|{
comment|// This has been tested quite thorougly above but do a couple tests anyway
name|COSString
name|testSubj
init|=
operator|new
name|COSString
argument_list|(
name|ESC_CHAR_STRING
argument_list|)
decl_stmt|;
name|writePDFTests
argument_list|(
literal|"("
operator|+
name|ESC_CHAR_STRING_PDF_FORMAT
operator|+
literal|")"
argument_list|,
name|testSubj
argument_list|)
expr_stmt|;
name|String
name|textString
init|=
literal|"This is just an arbitrary piece of text for testing"
decl_stmt|;
name|COSString
name|testSubj2
init|=
operator|new
name|COSString
argument_list|(
name|textString
argument_list|)
decl_stmt|;
name|writePDFTests
argument_list|(
literal|"("
operator|+
name|textString
operator|+
literal|")"
argument_list|,
name|testSubj2
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will test all of the filters in the system.      *      * @throws IOException If there is an exception while encoding.      */
specifier|public
name|void
name|testUnicode
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|theString
init|=
literal|"\u4e16"
decl_stmt|;
name|COSString
name|string
init|=
operator|new
name|COSString
argument_list|(
name|theString
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|string
operator|.
name|getString
argument_list|()
operator|.
name|equals
argument_list|(
name|theString
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|textAscii
init|=
literal|"This is some regular text. It should all be expressable in ASCII"
decl_stmt|;
comment|/** En français où les choses sont accentués. En español, así */
name|String
name|text8Bit
init|=
literal|"En fran\u00e7ais o\u00f9 les choses sont accentu\u00e9s. En espa\u00f1ol, as\u00ed"
decl_stmt|;
comment|/** をクリックしてく */
name|String
name|textHighBits
init|=
literal|"\u3092\u30af\u30ea\u30c3\u30af\u3057\u3066\u304f"
decl_stmt|;
comment|// Testing the getString method
name|COSString
name|stringAscii
init|=
operator|new
name|COSString
argument_list|(
name|textAscii
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|stringAscii
operator|.
name|getString
argument_list|()
argument_list|,
name|textAscii
argument_list|)
expr_stmt|;
name|COSString
name|string8Bit
init|=
operator|new
name|COSString
argument_list|(
name|text8Bit
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|string8Bit
operator|.
name|getString
argument_list|()
argument_list|,
name|text8Bit
argument_list|)
expr_stmt|;
name|COSString
name|stringHighBits
init|=
operator|new
name|COSString
argument_list|(
name|textHighBits
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|stringHighBits
operator|.
name|getString
argument_list|()
argument_list|,
name|textHighBits
argument_list|)
expr_stmt|;
comment|// Testing the getBytes method
comment|// The first two strings should be stored as ISO-8859-1 because they only contain chars in the range 0..255
name|assertEquals
argument_list|(
name|textAscii
argument_list|,
operator|new
name|String
argument_list|(
name|stringAscii
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
comment|// likewise for the 8bit characters.
name|assertEquals
argument_list|(
name|text8Bit
argument_list|,
operator|new
name|String
argument_list|(
name|string8Bit
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
comment|// The japanese text contains high bits so must be stored as big endian UTF-16
name|assertEquals
argument_list|(
name|textHighBits
argument_list|,
operator|new
name|String
argument_list|(
name|stringHighBits
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|"UnicodeBig"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test the writePDF method to ensure that the Strings are correct when written into PDF.
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|COSWriter
operator|.
name|writeString
argument_list|(
name|stringAscii
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"("
operator|+
name|textAscii
operator|+
literal|")"
argument_list|,
operator|new
name|String
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"ASCII"
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|reset
argument_list|()
expr_stmt|;
name|COSWriter
operator|.
name|writeString
argument_list|(
name|string8Bit
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|StringBuffer
name|hex
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|text8Bit
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|hex
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toHexString
argument_list|(
name|c
argument_list|)
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"<"
operator|+
name|hex
operator|.
name|toString
argument_list|()
operator|+
literal|">"
argument_list|,
operator|new
name|String
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"ASCII"
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|reset
argument_list|()
expr_stmt|;
name|COSWriter
operator|.
name|writeString
argument_list|(
name|stringHighBits
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|hex
operator|=
operator|new
name|StringBuffer
argument_list|()
expr_stmt|;
name|hex
operator|.
name|append
argument_list|(
literal|"FEFF"
argument_list|)
expr_stmt|;
comment|// Byte Order Mark
for|for
control|(
name|char
name|c
range|:
name|textHighBits
operator|.
name|toCharArray
argument_list|()
control|)
block|{
name|hex
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toHexString
argument_list|(
name|c
argument_list|)
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"<"
operator|+
name|hex
operator|.
name|toString
argument_list|()
operator|+
literal|">"
argument_list|,
operator|new
name|String
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"ASCII"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|testAccept
parameter_list|()
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|outStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ICOSVisitor
name|visitor
init|=
operator|new
name|COSWriter
argument_list|(
name|outStream
argument_list|)
decl_stmt|;
name|COSString
name|testSubj
init|=
operator|new
name|COSString
argument_list|(
name|ESC_CHAR_STRING
argument_list|)
decl_stmt|;
name|testSubj
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"("
operator|+
name|ESC_CHAR_STRING_PDF_FORMAT
operator|+
literal|")"
argument_list|,
name|outStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|outStream
operator|.
name|reset
argument_list|()
expr_stmt|;
name|testSubj
operator|.
name|setForceHexForm
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|testSubj
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<"
operator|+
name|createHex
argument_list|(
name|ESC_CHAR_STRING
argument_list|)
operator|+
literal|">"
argument_list|,
name|outStream
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tests equals(Object) - ensure that the Object.equals() contract is obeyed.      */
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
comment|// Check all these several times for consistency
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
comment|// Reflexive
name|COSString
name|x1
init|=
operator|new
name|COSString
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|x1
operator|.
name|equals
argument_list|(
name|x1
argument_list|)
argument_list|)
expr_stmt|;
comment|// Symmetry i.e. if x == y then y == x
name|COSString
name|y1
init|=
operator|new
name|COSString
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|x1
operator|.
name|equals
argument_list|(
name|y1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|y1
operator|.
name|equals
argument_list|(
name|x1
argument_list|)
argument_list|)
expr_stmt|;
name|COSString
name|x2
init|=
operator|new
name|COSString
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|x2
operator|.
name|setForceHexForm
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// also if x != y then y != x
name|assertFalse
argument_list|(
name|x1
operator|.
name|equals
argument_list|(
name|x2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|x2
operator|.
name|equals
argument_list|(
name|x1
argument_list|)
argument_list|)
expr_stmt|;
comment|// Transitive if x == y&& y == z then x == z
name|COSString
name|z1
init|=
operator|new
name|COSString
argument_list|(
literal|"Test"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|x1
operator|.
name|equals
argument_list|(
name|y1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|y1
operator|.
name|equals
argument_list|(
name|z1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|x1
operator|.
name|equals
argument_list|(
name|z1
argument_list|)
argument_list|)
expr_stmt|;
comment|// Test the negative as well if x1 == y1&& y1 != x2 then x1 != x2
name|assertTrue
argument_list|(
name|x1
operator|.
name|equals
argument_list|(
name|y1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|y1
operator|.
name|equals
argument_list|(
name|x2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|x1
operator|.
name|equals
argument_list|(
name|x2
argument_list|)
argument_list|)
expr_stmt|;
comment|// Non-nullity
name|assertFalse
argument_list|(
name|x1
operator|==
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|y1
operator|==
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|z1
operator|==
literal|null
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|x2
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test hashCode() - tests that the Object.hashCode() contract is obeyed.      */
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
name|COSString
name|str1
init|=
operator|new
name|COSString
argument_list|(
literal|"Test1"
argument_list|)
decl_stmt|;
name|COSString
name|str2
init|=
operator|new
name|COSString
argument_list|(
literal|"Test2"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|str1
operator|.
name|hashCode
argument_list|()
operator|==
name|str2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|COSString
name|str3
init|=
operator|new
name|COSString
argument_list|(
literal|"Test1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|str1
operator|.
name|hashCode
argument_list|()
operator|==
name|str3
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|str3
operator|.
name|setForceHexForm
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|str1
operator|.
name|hashCode
argument_list|()
operator|==
name|str3
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test testCompareFromHexString() - tests that Strings created from hex      * compare correctly (PDFBOX-2401)      */
specifier|public
name|void
name|testCompareFromHexString
parameter_list|()
throws|throws
name|IOException
block|{
name|COSString
name|test1
init|=
name|COSString
operator|.
name|parseHex
argument_list|(
literal|"000000FF000000"
argument_list|)
decl_stmt|;
name|COSString
name|test2
init|=
name|COSString
operator|.
name|parseHex
argument_list|(
literal|"000000FF00FFFF"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|test1
argument_list|,
name|test1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|test2
argument_list|,
name|test2
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|test1
operator|.
name|toHexString
argument_list|()
operator|.
name|equals
argument_list|(
name|test2
operator|.
name|toHexString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|test1
operator|.
name|getBytes
argument_list|()
argument_list|,
name|test2
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|test1
operator|.
name|equals
argument_list|(
name|test2
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|test2
operator|.
name|equals
argument_list|(
name|test1
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|test1
operator|.
name|getString
argument_list|()
operator|.
name|equals
argument_list|(
name|test2
operator|.
name|getString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

