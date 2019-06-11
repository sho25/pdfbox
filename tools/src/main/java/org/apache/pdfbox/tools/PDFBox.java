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
name|tools
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|debugger
operator|.
name|PDFDebugger
import|;
end_import

begin_comment
comment|/**  * Simple wrapper around all the command line utilities included in PDFBox.  * Used as the main class in the runnable standalone PDFBox jar.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PDFBox
block|{
specifier|private
name|PDFBox
parameter_list|()
block|{     }
comment|/**      * Main method.      *       * @param args command line arguments      * @throws java.lang.Exception      */
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
name|Exception
block|{
comment|// suppress the Dock icon on OS X
name|System
operator|.
name|setProperty
argument_list|(
literal|"apple.awt.UIElement"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
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
name|boolean
name|exitAfterCallingMain
init|=
literal|true
decl_stmt|;
switch|switch
condition|(
name|command
condition|)
block|{
case|case
literal|"Decrypt"
case|:
name|Decrypt
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"Encrypt"
case|:
name|Encrypt
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"ExtractText"
case|:
name|ExtractText
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"ExtractImages"
case|:
name|ExtractImages
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"OverlayPDF"
case|:
name|OverlayPDF
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"PrintPDF"
case|:
name|PrintPDF
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"PDFDebugger"
case|:
case|case
literal|"PDFReader"
case|:
name|PDFDebugger
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
name|exitAfterCallingMain
operator|=
literal|false
expr_stmt|;
break|break;
case|case
literal|"PDFMerger"
case|:
name|PDFMerger
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"PDFSplit"
case|:
name|PDFSplit
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"PDFToImage"
case|:
name|PDFToImage
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"ImageToPDF"
case|:
name|ImageToPDF
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"TextToPDF"
case|:
name|TextToPDF
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"WriteDecodedDoc"
case|:
name|WriteDecodedDoc
operator|.
name|main
argument_list|(
name|arguments
argument_list|)
expr_stmt|;
break|break;
default|default:
name|showMessageAndExit
argument_list|()
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|exitAfterCallingMain
condition|)
block|{
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|showMessageAndExit
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|showMessageAndExit
parameter_list|()
block|{
name|String
name|message
init|=
literal|"PDFBox version: \""
operator|+
name|Version
operator|.
name|getVersion
argument_list|()
operator|+
literal|"\""
operator|+
literal|"\nUsage: java -jar pdfbox-app-x.y.z.jar<command><args..>\n"
operator|+
literal|"\nPossible commands are:\n"
operator|+
literal|"  Decrypt\n"
operator|+
literal|"  Encrypt\n"
operator|+
literal|"  ExtractText\n"
operator|+
literal|"  ExtractImages\n"
operator|+
literal|"  ImageToPDF\n"
operator|+
literal|"  OverlayPDF\n"
operator|+
literal|"  PrintPDF\n"
operator|+
literal|"  PDFDebugger\n"
operator|+
literal|"  PDFMerger\n"
operator|+
literal|"  PDFReader\n"
operator|+
literal|"  PDFSplit\n"
operator|+
literal|"  PDFToImage\n"
operator|+
literal|"  TextToPDF\n"
operator|+
literal|"  WriteDecodedDoc"
decl_stmt|;
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|message
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

