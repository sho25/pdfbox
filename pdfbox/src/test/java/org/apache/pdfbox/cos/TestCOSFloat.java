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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
comment|/**  * Tests {@link COSFloat}.  */
end_comment

begin_class
specifier|public
class|class
name|TestCOSFloat
extends|extends
name|TestCOSNumber
block|{
comment|// Use random number to ensure various float values are expressed in the test
specifier|private
name|Random
name|rnd
decl_stmt|;
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|rnd
operator|=
operator|new
name|Random
argument_list|()
expr_stmt|;
try|try
block|{
name|testCOSBase
operator|=
name|COSNumber
operator|.
name|get
argument_list|(
literal|"1.1"
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
literal|"Failed to create a COSNumber in setUp()"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Tests equals() - ensures that the Object.equals() contract is obeyed. These are tested over      * a range of arbitrary values to ensure Consistency, Reflexivity, Symmetry, Transitivity and      * non-nullity.      */
specifier|public
name|void
name|testEquals
parameter_list|()
block|{
comment|// Consistency
for|for
control|(
name|int
name|i
init|=
operator|-
literal|100000
init|;
name|i
operator|<
literal|300000
condition|;
name|i
operator|+=
literal|20000
control|)
block|{
name|float
name|num
init|=
name|i
operator|*
name|rnd
operator|.
name|nextFloat
argument_list|()
decl_stmt|;
name|COSFloat
name|test1
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|COSFloat
name|test2
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|COSFloat
name|test3
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
comment|// Reflexive (x == x)
name|assertTrue
argument_list|(
name|test1
operator|.
name|equals
argument_list|(
name|test1
argument_list|)
argument_list|)
expr_stmt|;
comment|// Symmetric is preserved ( x==y then y==x)
name|assertTrue
argument_list|(
name|test2
operator|.
name|equals
argument_list|(
name|test1
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|test1
operator|.
name|equals
argument_list|(
name|test2
argument_list|)
argument_list|)
expr_stmt|;
comment|// Transitive (if x==y&& y==z then x==z)
name|assertTrue
argument_list|(
name|test1
operator|.
name|equals
argument_list|(
name|test2
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|test2
operator|.
name|equals
argument_list|(
name|test3
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|test1
operator|.
name|equals
argument_list|(
name|test3
argument_list|)
argument_list|)
expr_stmt|;
comment|// Non-nullity
name|assertFalse
argument_list|(
name|test1
operator|.
name|equals
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|test2
operator|.
name|equals
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|test3
operator|.
name|equals
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|float
name|nf
init|=
name|Float
operator|.
name|intBitsToFloat
argument_list|(
name|Float
operator|.
name|floatToIntBits
argument_list|(
name|num
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|COSFloat
name|test4
init|=
operator|new
name|COSFloat
argument_list|(
name|nf
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|test4
operator|.
name|equals
argument_list|(
name|test1
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Tests hashCode() - ensures that the Object.hashCode() contract is obeyed over a range of      * arbitrary values.      */
specifier|public
name|void
name|testHashCode
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
operator|-
literal|100000
init|;
name|i
operator|<
literal|300000
condition|;
name|i
operator|+=
literal|20000
control|)
block|{
name|float
name|num
init|=
name|i
operator|*
name|rnd
operator|.
name|nextFloat
argument_list|()
decl_stmt|;
name|COSFloat
name|test1
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|COSFloat
name|test2
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|test1
operator|.
name|hashCode
argument_list|()
argument_list|,
name|test2
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|float
name|nf
init|=
name|Float
operator|.
name|intBitsToFloat
argument_list|(
name|Float
operator|.
name|floatToIntBits
argument_list|(
name|num
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|COSFloat
name|test3
init|=
operator|new
name|COSFloat
argument_list|(
name|nf
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|test3
operator|.
name|hashCode
argument_list|()
operator|==
name|test1
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|testFloatValue
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
operator|-
literal|100000
init|;
name|i
operator|<
literal|300000
condition|;
name|i
operator|+=
literal|20000
control|)
block|{
name|float
name|num
init|=
name|i
operator|*
name|rnd
operator|.
name|nextFloat
argument_list|()
decl_stmt|;
name|COSFloat
name|testFloat
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|num
argument_list|,
name|testFloat
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|testDoubleValue
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
operator|-
literal|100000
init|;
name|i
operator|<
literal|300000
condition|;
name|i
operator|+=
literal|20000
control|)
block|{
name|float
name|num
init|=
name|i
operator|*
name|rnd
operator|.
name|nextFloat
argument_list|()
decl_stmt|;
name|COSFloat
name|testFloat
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
comment|// compare the string representation instead of the numeric values
comment|// as the cast from float to double adds some more fraction digits
name|assertEquals
argument_list|(
name|Float
operator|.
name|toString
argument_list|(
name|num
argument_list|)
argument_list|,
name|Double
operator|.
name|toString
argument_list|(
name|testFloat
operator|.
name|doubleValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|testIntValue
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
operator|-
literal|100000
init|;
name|i
operator|<
literal|300000
condition|;
name|i
operator|+=
literal|20000
control|)
block|{
name|float
name|num
init|=
name|i
operator|*
name|rnd
operator|.
name|nextFloat
argument_list|()
decl_stmt|;
name|COSFloat
name|testFloat
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|(
name|int
operator|)
name|num
argument_list|,
name|testFloat
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|testLongValue
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
operator|-
literal|100000
init|;
name|i
operator|<
literal|300000
condition|;
name|i
operator|+=
literal|20000
control|)
block|{
name|float
name|num
init|=
name|i
operator|*
name|rnd
operator|.
name|nextFloat
argument_list|()
decl_stmt|;
name|COSFloat
name|testFloat
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
operator|(
name|long
operator|)
name|num
argument_list|,
name|testFloat
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|testAccept
parameter_list|()
block|{
name|ByteArrayOutputStream
name|outStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|COSWriter
name|visitor
init|=
operator|new
name|COSWriter
argument_list|(
name|outStream
argument_list|)
decl_stmt|;
name|float
name|num
init|=
literal|0
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
operator|-
literal|100000
init|;
name|i
operator|<
literal|300000
condition|;
name|i
operator|+=
literal|20000
control|)
block|{
name|num
operator|=
name|i
operator|*
name|rnd
operator|.
name|nextFloat
argument_list|()
expr_stmt|;
name|COSFloat
name|cosFloat
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|cosFloat
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|floatToString
argument_list|(
name|cosFloat
operator|.
name|floatValue
argument_list|()
argument_list|)
argument_list|,
name|outStream
operator|.
name|toString
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|testByteArrays
argument_list|(
name|floatToString
argument_list|(
name|num
argument_list|)
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|,
name|outStream
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|outStream
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Failed to write "
operator|+
name|num
operator|+
literal|" exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Tests writePDF() - this method takes an {@link OutputStream} and writes this object to it.      */
specifier|public
name|void
name|testWritePDF
parameter_list|()
block|{
name|ByteArrayOutputStream
name|outStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|float
name|num
init|=
literal|0
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
operator|-
literal|1000
init|;
name|i
operator|<
literal|3000
condition|;
name|i
operator|+=
literal|200
control|)
block|{
name|num
operator|=
name|i
operator|*
name|rnd
operator|.
name|nextFloat
argument_list|()
expr_stmt|;
name|COSFloat
name|cosFloat
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|cosFloat
operator|.
name|writePDF
argument_list|(
name|outStream
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|floatToString
argument_list|(
name|cosFloat
operator|.
name|floatValue
argument_list|()
argument_list|)
argument_list|,
name|outStream
operator|.
name|toString
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|testByteArrays
argument_list|(
name|floatToString
argument_list|(
name|num
argument_list|)
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|,
name|outStream
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
name|outStream
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
comment|// test a corner case as described in PDFBOX-1778
name|num
operator|=
literal|0.000000000000000000000000000000001f
expr_stmt|;
name|COSFloat
name|test
init|=
operator|new
name|COSFloat
argument_list|(
name|num
argument_list|)
decl_stmt|;
name|test
operator|.
name|writePDF
argument_list|(
name|outStream
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|floatToString
argument_list|(
name|num
argument_list|)
argument_list|,
name|outStream
operator|.
name|toString
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|outStream
operator|.
name|reset
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
literal|"Failed to write "
operator|+
name|num
operator|+
literal|" exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|floatToString
parameter_list|(
name|float
name|value
parameter_list|)
block|{
comment|// use a BigDecimal as intermediate state to avoid
comment|// a floating point string representation of the float value
return|return
name|removeTrailingNull
argument_list|(
operator|new
name|BigDecimal
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|value
argument_list|)
argument_list|)
operator|.
name|toPlainString
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|String
name|removeTrailingNull
parameter_list|(
name|String
name|value
parameter_list|)
block|{
comment|// remove fraction digit "0" only
if|if
condition|(
name|value
operator|.
name|indexOf
argument_list|(
literal|"."
argument_list|)
operator|>
operator|-
literal|1
operator|&&
operator|!
name|value
operator|.
name|endsWith
argument_list|(
literal|".0"
argument_list|)
condition|)
block|{
while|while
condition|(
name|value
operator|.
name|endsWith
argument_list|(
literal|"0"
argument_list|)
operator|&&
operator|!
name|value
operator|.
name|endsWith
argument_list|(
literal|".0"
argument_list|)
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|value
return|;
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
name|TestCOSFloat
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

