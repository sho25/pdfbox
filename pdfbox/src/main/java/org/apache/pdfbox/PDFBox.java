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
package|;
end_package

begin_comment
comment|/**  * Simple wrapper around all the command line utilities included in PDFBox.  * Used as the main class in the runnable standalone PDFBox jar.  *  * @see<a href="https://issues.apache.org/jira/browse/PDFBOX-687">PDFBOX-687</a>  */
end_comment

begin_class
specifier|public
class|class
name|PDFBox
block|{
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
if|if
condition|(
name|args
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|String
name|command
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
name|String
index|[]
name|arguments
init|=
operator|new
name|String
index|[
name|args
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|args
argument_list|,
literal|1
argument_list|,
name|arguments
argument_list|,
literal|0
argument_list|,
name|arguments
operator|.
name|length
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"ConvertColorspace"
argument_list|)
condition|)
block|{
name|ConvertColorspace
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"Decrypt"
argument_list|)
condition|)
block|{
name|Decrypt
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"Encrypt"
argument_list|)
condition|)
block|{
name|Encrypt
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"ExtractText"
argument_list|)
condition|)
block|{
name|ExtractText
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"Overlay"
argument_list|)
condition|)
block|{
name|Overlay
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"PrintPDF"
argument_list|)
condition|)
block|{
name|PrintPDF
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"PDFDebugger"
argument_list|)
condition|)
block|{
name|PDFDebugger
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"PDFMerger"
argument_list|)
condition|)
block|{
name|PDFMerger
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"PDFReader"
argument_list|)
condition|)
block|{
name|PDFReader
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"PDFReader"
argument_list|)
condition|)
block|{
name|PDFReader
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"PDFSplit"
argument_list|)
condition|)
block|{
name|PDFSplit
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"PDFToImage"
argument_list|)
condition|)
block|{
name|PDFToImage
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|command
operator|.
name|equals
argument_list|(
literal|"TextToPDF"
argument_list|)
condition|)
block|{
name|TextToPDF
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|command
operator|+
literal|" failed with the following exception:"
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"usage: java pdfbox-app-x.y.z.jar<command><args..>"
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
block|}
end_class

end_unit

