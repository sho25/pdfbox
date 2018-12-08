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
name|java
operator|.
name|awt
operator|.
name|print
operator|.
name|PrinterException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|print
operator|.
name|PrinterJob
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|print
operator|.
name|PrintService
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
name|printing
operator|.
name|Orientation
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
name|printing
operator|.
name|PDFPageable
import|;
end_import

begin_comment
comment|/**  * This is a command line program that will print a PDF document.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PrintPDF
block|{
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"-password"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SILENT
init|=
literal|"-silentPrint"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PRINTER_NAME
init|=
literal|"-printerName"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ORIENTATION
init|=
literal|"-orientation"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BORDER
init|=
literal|"-border"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DPI
init|=
literal|"-dpi"
decl_stmt|;
comment|/**      * private constructor.      */
specifier|private
name|PrintPDF
parameter_list|()
block|{
comment|// static class
block|}
comment|/**      * Infamous main method.      *       * @param args Command line arguments, should be one and a reference to a file.      * @throws PrinterException if the specified service cannot support the Pageable and Printable interfaces.      * @throws IOException if there is an error parsing the file.      */
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
name|PrinterException
throws|,
name|IOException
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
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|pdfFile
init|=
literal|null
decl_stmt|;
name|boolean
name|silentPrint
init|=
literal|false
decl_stmt|;
name|String
name|printerName
init|=
literal|null
decl_stmt|;
name|Orientation
name|orientation
init|=
name|Orientation
operator|.
name|AUTO
decl_stmt|;
name|boolean
name|showPageBorder
init|=
literal|false
decl_stmt|;
name|int
name|dpi
init|=
literal|0
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Orientation
argument_list|>
name|orientationMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|orientationMap
operator|.
name|put
argument_list|(
literal|"auto"
argument_list|,
name|Orientation
operator|.
name|AUTO
argument_list|)
expr_stmt|;
name|orientationMap
operator|.
name|put
argument_list|(
literal|"landscape"
argument_list|,
name|Orientation
operator|.
name|LANDSCAPE
argument_list|)
expr_stmt|;
name|orientationMap
operator|.
name|put
argument_list|(
literal|"portrait"
argument_list|,
name|Orientation
operator|.
name|PORTRAIT
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
name|args
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
switch|switch
condition|(
name|args
index|[
name|i
index|]
condition|)
block|{
case|case
name|PASSWORD
case|:
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|password
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
break|break;
case|case
name|PRINTER_NAME
case|:
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|printerName
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
break|break;
case|case
name|SILENT
case|:
name|silentPrint
operator|=
literal|true
expr_stmt|;
break|break;
case|case
name|ORIENTATION
case|:
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|orientation
operator|=
name|orientationMap
operator|.
name|get
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|orientation
operator|==
literal|null
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
break|break;
case|case
name|BORDER
case|:
name|showPageBorder
operator|=
literal|true
expr_stmt|;
break|break;
case|case
name|DPI
case|:
name|i
operator|++
expr_stmt|;
if|if
condition|(
name|i
operator|>=
name|args
operator|.
name|length
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|dpi
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
expr_stmt|;
break|break;
default|default:
name|pdfFile
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|pdfFile
operator|==
literal|null
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
try|try
init|(
name|PDDocument
name|document
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|pdfFile
argument_list|)
argument_list|,
name|password
argument_list|)
init|)
block|{
name|PrinterJob
name|printJob
init|=
name|PrinterJob
operator|.
name|getPrinterJob
argument_list|()
decl_stmt|;
name|printJob
operator|.
name|setJobName
argument_list|(
operator|new
name|File
argument_list|(
name|pdfFile
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|printerName
operator|!=
literal|null
condition|)
block|{
name|PrintService
index|[]
name|printService
init|=
name|PrinterJob
operator|.
name|lookupPrintServices
argument_list|()
decl_stmt|;
name|boolean
name|printerFound
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
operator|!
name|printerFound
operator|&&
name|i
operator|<
name|printService
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|printService
index|[
name|i
index|]
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
name|printerName
argument_list|)
condition|)
block|{
name|printJob
operator|.
name|setPrintService
argument_list|(
name|printService
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|printerFound
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
name|printJob
operator|.
name|setPageable
argument_list|(
operator|new
name|PDFPageable
argument_list|(
name|document
argument_list|,
name|orientation
argument_list|,
name|showPageBorder
argument_list|,
name|dpi
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|silentPrint
operator|||
name|printJob
operator|.
name|printDialog
argument_list|()
condition|)
block|{
name|printJob
operator|.
name|print
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This will print the usage requirements and exit.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|String
name|message
init|=
literal|"Usage: java -jar pdfbox-app-x.y.z.jar PrintPDF [options]<inputfile>\n"
operator|+
literal|"\nOptions:\n"
operator|+
literal|"  -password<password>                : Password to decrypt document\n"
operator|+
literal|"  -printerName<name>                  : Print to specific printer\n"
operator|+
literal|"  -orientation auto|portrait|landscape : Print using orientation\n"
operator|+
literal|"                                           (default: auto)\n"
operator|+
literal|"  -border                              : Print with border\n"
operator|+
literal|"  -dpi                                 : Render into intermediate image with\n"
operator|+
literal|"                                           specific dpi and then print\n"
operator|+
literal|"  -silentPrint                         : Print without printer dialog box\n"
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

