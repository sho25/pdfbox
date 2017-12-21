begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2017 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cff
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
name|FileInputStream
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
name|InputStream
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
name|org
operator|.
name|junit
operator|.
name|Test
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
name|*
import|;
end_import

begin_comment
comment|/**  *  * @author Petr Slaby  */
end_comment

begin_class
specifier|public
class|class
name|CFFParserTest
block|{
comment|/**      * PDFBOX-4038: Test whether BlueValues and other delta encoded lists are read correctly. The      * test file is from FOP-2432.      *      * @throws IOException       */
annotation|@
name|Test
specifier|public
name|void
name|testDeltaLists
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|CFFFont
argument_list|>
name|fonts
init|=
name|readFont
argument_list|(
operator|new
name|FileInputStream
argument_list|(
literal|"target/pdfs/SourceSansProBold.otf"
argument_list|)
argument_list|)
decl_stmt|;
name|CFFType1Font
name|font
init|=
operator|(
name|CFFType1Font
operator|)
name|fonts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Number
argument_list|>
name|blues
init|=
operator|(
name|List
argument_list|<
name|Number
argument_list|>
operator|)
name|font
operator|.
name|getPrivateDict
argument_list|()
operator|.
name|get
argument_list|(
literal|"BlueValues"
argument_list|)
decl_stmt|;
comment|// Expected values found for this font
name|assertNumberList
argument_list|(
literal|"Blue values are different than expected: "
operator|+
name|blues
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|int
index|[]
block|{
operator|-
literal|12
block|,
literal|0
block|,
literal|496
block|,
literal|508
block|,
literal|578
block|,
literal|590
block|,
literal|635
block|,
literal|647
block|,
literal|652
block|,
literal|664
block|,
literal|701
block|,
literal|713
block|}
argument_list|,
name|blues
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Number
argument_list|>
name|otherBlues
init|=
operator|(
name|List
argument_list|<
name|Number
argument_list|>
operator|)
name|font
operator|.
name|getPrivateDict
argument_list|()
operator|.
name|get
argument_list|(
literal|"OtherBlues"
argument_list|)
decl_stmt|;
name|assertNumberList
argument_list|(
literal|"Other blues are different than expected: "
operator|+
name|otherBlues
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|int
index|[]
block|{
operator|-
literal|196
block|,
operator|-
literal|184
block|}
argument_list|,
name|otherBlues
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Number
argument_list|>
name|familyBlues
init|=
operator|(
name|List
argument_list|<
name|Number
argument_list|>
operator|)
name|font
operator|.
name|getPrivateDict
argument_list|()
operator|.
name|get
argument_list|(
literal|"FamilyBlues"
argument_list|)
decl_stmt|;
name|assertNumberList
argument_list|(
literal|"Other blues are different than expected: "
operator|+
name|familyBlues
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|int
index|[]
block|{
operator|-
literal|12
block|,
literal|0
block|,
literal|486
block|,
literal|498
block|,
literal|574
block|,
literal|586
block|,
literal|638
block|,
literal|650
block|,
literal|656
block|,
literal|668
block|,
literal|712
block|,
literal|724
block|}
argument_list|,
name|familyBlues
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Number
argument_list|>
name|familyOtherBlues
init|=
operator|(
name|List
argument_list|<
name|Number
argument_list|>
operator|)
name|font
operator|.
name|getPrivateDict
argument_list|()
operator|.
name|get
argument_list|(
literal|"FamilyOtherBlues"
argument_list|)
decl_stmt|;
name|assertNumberList
argument_list|(
literal|"Other blues are different than expected: "
operator|+
name|familyOtherBlues
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|int
index|[]
block|{
operator|-
literal|217
block|,
operator|-
literal|205
block|}
argument_list|,
name|familyOtherBlues
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Number
argument_list|>
name|stemSnapH
init|=
operator|(
name|List
argument_list|<
name|Number
argument_list|>
operator|)
name|font
operator|.
name|getPrivateDict
argument_list|()
operator|.
name|get
argument_list|(
literal|"StemSnapH"
argument_list|)
decl_stmt|;
name|assertNumberList
argument_list|(
literal|"StemSnapH values are different than expected: "
operator|+
name|stemSnapH
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|int
index|[]
block|{
literal|115
block|}
argument_list|,
name|stemSnapH
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Number
argument_list|>
name|stemSnapV
init|=
operator|(
name|List
argument_list|<
name|Number
argument_list|>
operator|)
name|font
operator|.
name|getPrivateDict
argument_list|()
operator|.
name|get
argument_list|(
literal|"StemSnapV"
argument_list|)
decl_stmt|;
name|assertNumberList
argument_list|(
literal|"StemSnapV values are different than expected: "
operator|+
name|stemSnapV
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|int
index|[]
block|{
literal|146
block|,
literal|150
block|}
argument_list|,
name|stemSnapV
argument_list|)
expr_stmt|;
block|}
specifier|private
name|List
argument_list|<
name|CFFFont
argument_list|>
name|readFont
parameter_list|(
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|content
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
name|int
name|len
decl_stmt|;
while|while
condition|(
operator|(
name|len
operator|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|>
operator|-
literal|1
condition|)
block|{
name|content
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
name|CFFParser
name|parser
init|=
operator|new
name|CFFParser
argument_list|()
decl_stmt|;
return|return
name|parser
operator|.
name|parse
argument_list|(
name|content
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|void
name|assertNumberList
parameter_list|(
name|String
name|message
parameter_list|,
name|int
index|[]
name|expected
parameter_list|,
name|List
argument_list|<
name|Number
argument_list|>
name|found
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|message
argument_list|,
name|expected
operator|.
name|length
argument_list|,
name|found
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expected
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|message
argument_list|,
name|expected
index|[
name|i
index|]
argument_list|,
name|found
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
