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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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
name|io
operator|.
name|IOUtils
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
name|PDResources
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
name|common
operator|.
name|PDStream
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
name|PDCIDFont
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
name|PDCIDFontType2
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
name|PDFontDescriptor
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
name|PDTrueTypeFont
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
name|PDType0Font
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
name|graphics
operator|.
name|PDXObject
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
name|graphics
operator|.
name|form
operator|.
name|PDFormXObject
import|;
end_import

begin_comment
comment|/**  * This will extract all true type-fonts of a pdf.  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ExtractTTFFonts
block|{
specifier|private
name|int
name|fontCounter
init|=
literal|1
decl_stmt|;
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
name|PREFIX
init|=
literal|"-prefix"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ADDKEY
init|=
literal|"-addkey"
decl_stmt|;
specifier|private
name|ExtractTTFFonts
parameter_list|()
block|{     }
comment|/**      * This is the entry point for the application.      *       * @param args The command-line arguments.      *       * @throws IOException If there is an error decrypting the document.      */
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
name|ExtractTTFFonts
name|extractor
init|=
operator|new
name|ExtractTTFFonts
argument_list|()
decl_stmt|;
name|extractor
operator|.
name|extractFonts
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|extractFonts
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
argument_list|<
literal|1
operator|||
name|args
operator|.
name|length
argument_list|>
literal|4
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|String
name|pdfFile
init|=
literal|null
decl_stmt|;
name|String
name|password
init|=
literal|""
decl_stmt|;
name|String
name|prefix
init|=
literal|null
decl_stmt|;
name|boolean
name|addKey
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
name|PREFIX
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
name|prefix
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
break|break;
case|case
name|ADDKEY
case|:
name|addKey
operator|=
literal|true
expr_stmt|;
break|break;
default|default:
if|if
condition|(
name|pdfFile
operator|==
literal|null
condition|)
block|{
name|pdfFile
operator|=
name|args
index|[
name|i
index|]
expr_stmt|;
block|}
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
else|else
block|{
if|if
condition|(
name|prefix
operator|==
literal|null
operator|&&
name|pdfFile
operator|.
name|length
argument_list|()
operator|>
literal|4
condition|)
block|{
name|prefix
operator|=
name|pdfFile
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pdfFile
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
block|}
name|PDDocument
name|document
init|=
literal|null
decl_stmt|;
try|try
block|{
name|document
operator|=
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
expr_stmt|;
for|for
control|(
name|PDPage
name|page
range|:
name|document
operator|.
name|getPages
argument_list|()
control|)
block|{
name|PDResources
name|resources
init|=
name|page
operator|.
name|getResources
argument_list|()
decl_stmt|;
comment|// extract all fonts which are part of the page resources
name|processResources
argument_list|(
name|resources
argument_list|,
name|prefix
argument_list|,
name|addKey
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|void
name|processResources
parameter_list|(
name|PDResources
name|resources
parameter_list|,
name|String
name|prefix
parameter_list|,
name|boolean
name|addKey
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|resources
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|COSName
name|key
range|:
name|resources
operator|.
name|getFontNames
argument_list|()
control|)
block|{
name|PDFont
name|font
init|=
name|resources
operator|.
name|getFont
argument_list|(
name|key
argument_list|)
decl_stmt|;
comment|// write the font
if|if
condition|(
name|font
operator|instanceof
name|PDTrueTypeFont
condition|)
block|{
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|addKey
condition|)
block|{
name|name
operator|=
name|getUniqueFileName
argument_list|(
name|prefix
operator|+
literal|"_"
operator|+
name|key
argument_list|,
literal|"ttf"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|getUniqueFileName
argument_list|(
name|prefix
argument_list|,
literal|"ttf"
argument_list|)
expr_stmt|;
block|}
name|writeFont
argument_list|(
name|font
operator|.
name|getFontDescriptor
argument_list|()
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|font
operator|instanceof
name|PDType0Font
condition|)
block|{
name|PDCIDFont
name|descendantFont
init|=
operator|(
operator|(
name|PDType0Font
operator|)
name|font
operator|)
operator|.
name|getDescendantFont
argument_list|()
decl_stmt|;
if|if
condition|(
name|descendantFont
operator|instanceof
name|PDCIDFontType2
condition|)
block|{
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|addKey
condition|)
block|{
name|name
operator|=
name|getUniqueFileName
argument_list|(
name|prefix
operator|+
literal|"_"
operator|+
name|key
argument_list|,
literal|"ttf"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|getUniqueFileName
argument_list|(
name|prefix
argument_list|,
literal|"ttf"
argument_list|)
expr_stmt|;
block|}
name|writeFont
argument_list|(
name|descendantFont
operator|.
name|getFontDescriptor
argument_list|()
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|COSName
name|name
range|:
name|resources
operator|.
name|getXObjectNames
argument_list|()
control|)
block|{
name|PDXObject
name|xobject
init|=
name|resources
operator|.
name|getXObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|xobject
operator|instanceof
name|PDFormXObject
condition|)
block|{
name|PDFormXObject
name|xObjectForm
init|=
operator|(
name|PDFormXObject
operator|)
name|xobject
decl_stmt|;
name|PDResources
name|formResources
init|=
name|xObjectForm
operator|.
name|getResources
argument_list|()
decl_stmt|;
name|processResources
argument_list|(
name|formResources
argument_list|,
name|prefix
argument_list|,
name|addKey
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|writeFont
parameter_list|(
name|PDFontDescriptor
name|fd
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|fd
operator|!=
literal|null
condition|)
block|{
name|PDStream
name|ff2Stream
init|=
name|fd
operator|.
name|getFontFile2
argument_list|()
decl_stmt|;
if|if
condition|(
name|ff2Stream
operator|!=
literal|null
condition|)
block|{
name|FileOutputStream
name|fos
init|=
literal|null
decl_stmt|;
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Writing font:"
operator|+
name|name
argument_list|)
expr_stmt|;
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
operator|new
name|File
argument_list|(
name|name
operator|+
literal|".ttf"
argument_list|)
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|ff2Stream
operator|.
name|createInputStream
argument_list|()
argument_list|,
name|fos
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|fos
operator|!=
literal|null
condition|)
block|{
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|String
name|getUniqueFileName
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|suffix
parameter_list|)
block|{
name|String
name|uniqueName
init|=
literal|null
decl_stmt|;
name|File
name|f
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|f
operator|==
literal|null
operator|||
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
name|uniqueName
operator|=
name|prefix
operator|+
literal|"-"
operator|+
name|fontCounter
expr_stmt|;
name|f
operator|=
operator|new
name|File
argument_list|(
name|uniqueName
operator|+
literal|"."
operator|+
name|suffix
argument_list|)
expr_stmt|;
name|fontCounter
operator|++
expr_stmt|;
block|}
return|return
name|uniqueName
return|;
block|}
comment|/**      * This will print the usage requirements and exit.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Usage: java "
operator|+
name|ExtractTTFFonts
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" [OPTIONS]<PDF file>\n"
operator|+
literal|"  -password<password>        Password to decrypt document\n"
operator|+
literal|"  -prefix<font-prefix>       Font prefix(default to pdf name)\n"
operator|+
literal|"  -addkey                      add the internal font key to the file name\n"
operator|+
literal|"<PDF file>                   The PDF document to use\n"
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

