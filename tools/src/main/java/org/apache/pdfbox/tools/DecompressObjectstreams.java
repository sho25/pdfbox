begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed under the Apache License, Version 2.0 (the "License");  *  you may not use this file except in compliance with the License.  *  You may obtain a copy of the License at  *   *       http://www.apache.org/licenses/LICENSE-2.0  *   *  Unless required by applicable law or agreed to in writing, software  *  distributed under the License is distributed on an "AS IS" BASIS,  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *  See the License for the specific language governing permissions and  *  limitations under the License.  *  under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSName
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
name|cos
operator|.
name|COSObject
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
name|cos
operator|.
name|COSStream
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
name|pdfparser
operator|.
name|PDFObjectStreamParser
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
name|cos
operator|.
name|COSObjectKey
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
name|io
operator|.
name|IOUtils
import|;
end_import

begin_comment
comment|/**  * This program will just take all of the stream objects in a PDF and dereference  * them.  The streams will be gone in the resulting file and the objects will be  * present.  This is very helpful when trying to debug problems as it'll make  * it possible to easily look through a PDF using a text editor.  It also exposes  * problems which stem from objects inside object streams overwriting other  * objects.  * @author Adam Nichols  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|DecompressObjectstreams
block|{
comment|/**      * private constructor.      */
specifier|private
name|DecompressObjectstreams
parameter_list|()
block|{     }
comment|/**      * This is a very simple program, so everything is in the main method.      * @param args arguments to the program      */
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
operator|<
literal|1
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|String
name|inputFilename
init|=
name|args
index|[
literal|0
index|]
decl_stmt|;
name|String
name|outputFilename
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|outputFilename
operator|=
name|args
index|[
literal|1
index|]
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|inputFilename
operator|.
name|matches
argument_list|(
literal|".*\\.[pP][dD][fF]$"
argument_list|)
condition|)
block|{
name|outputFilename
operator|=
name|inputFilename
operator|.
name|replaceAll
argument_list|(
literal|"\\.[pP][dD][fF]$"
argument_list|,
literal|".unc.pdf"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outputFilename
operator|=
name|inputFilename
operator|+
literal|".unc.pdf"
expr_stmt|;
block|}
block|}
name|PDDocument
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|inputFilename
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|COSObject
name|objStream
range|:
name|doc
operator|.
name|getDocument
argument_list|()
operator|.
name|getObjectsByType
argument_list|(
name|COSName
operator|.
name|OBJ_STM
argument_list|)
control|)
block|{
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|objStream
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|PDFObjectStreamParser
name|sp
init|=
operator|new
name|PDFObjectStreamParser
argument_list|(
name|stream
argument_list|,
name|doc
operator|.
name|getDocument
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|COSObject
name|next
range|:
name|sp
operator|.
name|parse
argument_list|()
control|)
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
name|next
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|doc
operator|.
name|getDocument
argument_list|()
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|obj
operator|.
name|setObject
argument_list|(
name|next
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|doc
operator|.
name|getDocument
argument_list|()
operator|.
name|removeObject
argument_list|(
operator|new
name|COSObjectKey
argument_list|(
name|objStream
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|doc
operator|.
name|save
argument_list|(
name|outputFilename
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error processing file: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Explains how to use the program.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|String
name|message
init|=
literal|"Usage: java -cp pdfbox-app-x.y.z.jar "
operator|+
literal|"org.apache.pdfbox.tools.DecompressObjectstreams<inputfile> [<outputfile>]\n"
operator|+
literal|"\nOptions:\n"
operator|+
literal|"<inputfile>  : The PDF document to decompress\n"
operator|+
literal|"<outputfile> : The output filename (default is to replace .pdf with .unc.pdf)"
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

