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
name|examples
operator|.
name|pdmodel
package|;
end_package

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
name|font
operator|.
name|PDFont
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
name|font
operator|.
name|PDType1Font
import|;
end_import

begin_comment
comment|/**  * Creates a simple document with a Type 1 font (.pfb).  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|HelloWorldType1
block|{
specifier|private
name|HelloWorldType1
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|3
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: "
operator|+
name|HelloWorldType1
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<output-file><Message><pfb-file>"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|file
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
name|String
name|message
init|=
name|args
index|[
literal|1
index|]
decl_stmt|;
name|String
name|pfbPath
init|=
name|args
index|[
literal|2
index|]
decl_stmt|;
try|try
init|(
name|PDDocument
name|doc
init|=
operator|new
name|PDDocument
argument_list|()
init|)
block|{
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDFont
name|font
init|=
operator|new
name|PDType1Font
argument_list|(
name|doc
argument_list|,
operator|new
name|FileInputStream
argument_list|(
name|pfbPath
argument_list|)
argument_list|)
decl_stmt|;
try|try
init|(
name|PDPageContentStream
name|contents
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|)
init|)
block|{
name|contents
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contents
operator|.
name|setFont
argument_list|(
name|font
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|contents
operator|.
name|newLineAtOffset
argument_list|(
literal|100
argument_list|,
literal|700
argument_list|)
expr_stmt|;
name|contents
operator|.
name|showText
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|contents
operator|.
name|endText
argument_list|()
expr_stmt|;
block|}
name|doc
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|file
operator|+
literal|" created!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

