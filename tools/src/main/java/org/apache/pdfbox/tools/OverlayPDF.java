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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|multipdf
operator|.
name|Overlay
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
name|multipdf
operator|.
name|Overlay
operator|.
name|Position
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

begin_comment
comment|/**  *   * Adds an overlay to an existing PDF document.  *    * Based on code contributed by Balazs Jerk.   *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|OverlayPDF
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|OverlayPDF
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Command line options
specifier|private
specifier|static
specifier|final
name|String
name|POSITION
init|=
literal|"-position"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ODD
init|=
literal|"-odd"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EVEN
init|=
literal|"-even"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|FIRST
init|=
literal|"-first"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|LAST
init|=
literal|"-last"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PAGE
init|=
literal|"-page"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USEALLPAGES
init|=
literal|"-useAllPages"
decl_stmt|;
specifier|private
name|OverlayPDF
parameter_list|()
block|{     }
comment|/**      * This will overlay a document and write out the results.      *      * @param args command line arguments      * @throws IOException if something went wrong      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
specifier|final
name|String
index|[]
name|args
parameter_list|)
throws|throws
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
name|outputFilename
init|=
literal|null
decl_stmt|;
name|Overlay
name|overlayer
init|=
operator|new
name|Overlay
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|specificPageOverlayFile
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// input arguments
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
name|String
name|arg
init|=
name|args
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|i
operator|==
literal|0
condition|)
block|{
name|overlayer
operator|.
name|setInputFile
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|i
operator|==
operator|(
name|args
operator|.
name|length
operator|-
literal|1
operator|)
condition|)
block|{
name|outputFilename
operator|=
name|arg
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
name|POSITION
argument_list|)
operator|&&
operator|(
operator|(
name|i
operator|+
literal|1
operator|)
operator|<
name|args
operator|.
name|length
operator|)
condition|)
block|{
if|if
condition|(
name|Position
operator|.
name|FOREGROUND
operator|.
name|toString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|args
index|[
name|i
operator|+
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|overlayer
operator|.
name|setOverlayPosition
argument_list|(
name|Position
operator|.
name|FOREGROUND
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Position
operator|.
name|BACKGROUND
operator|.
name|toString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|args
index|[
name|i
operator|+
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|overlayer
operator|.
name|setOverlayPosition
argument_list|(
name|Position
operator|.
name|BACKGROUND
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|i
operator|+=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
name|ODD
argument_list|)
operator|&&
operator|(
operator|(
name|i
operator|+
literal|1
operator|)
operator|<
name|args
operator|.
name|length
operator|)
condition|)
block|{
name|overlayer
operator|.
name|setOddPageOverlayFile
argument_list|(
name|args
index|[
name|i
operator|+
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
name|EVEN
argument_list|)
operator|&&
operator|(
operator|(
name|i
operator|+
literal|1
operator|)
operator|<
name|args
operator|.
name|length
operator|)
condition|)
block|{
name|overlayer
operator|.
name|setEvenPageOverlayFile
argument_list|(
name|args
index|[
name|i
operator|+
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
name|FIRST
argument_list|)
operator|&&
operator|(
operator|(
name|i
operator|+
literal|1
operator|)
operator|<
name|args
operator|.
name|length
operator|)
condition|)
block|{
name|overlayer
operator|.
name|setFirstPageOverlayFile
argument_list|(
name|args
index|[
name|i
operator|+
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
name|LAST
argument_list|)
operator|&&
operator|(
operator|(
name|i
operator|+
literal|1
operator|)
operator|<
name|args
operator|.
name|length
operator|)
condition|)
block|{
name|overlayer
operator|.
name|setLastPageOverlayFile
argument_list|(
name|args
index|[
name|i
operator|+
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
name|USEALLPAGES
argument_list|)
operator|&&
operator|(
operator|(
name|i
operator|+
literal|1
operator|)
operator|<
name|args
operator|.
name|length
operator|)
condition|)
block|{
name|overlayer
operator|.
name|setAllPagesOverlayFile
argument_list|(
name|args
index|[
name|i
operator|+
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
name|PAGE
argument_list|)
operator|&&
operator|(
operator|(
name|i
operator|+
literal|2
operator|)
operator|<
name|args
operator|.
name|length
operator|)
operator|&&
operator|(
name|isInteger
argument_list|(
name|args
index|[
name|i
operator|+
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|specificPageOverlayFile
operator|.
name|put
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|i
operator|+
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|,
name|args
index|[
name|i
operator|+
literal|2
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|i
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|overlayer
operator|.
name|getDefaultOverlayFile
argument_list|()
operator|==
literal|null
condition|)
block|{
name|overlayer
operator|.
name|setDefaultOverlayFile
argument_list|(
name|arg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|overlayer
operator|.
name|getInputFile
argument_list|()
operator|==
literal|null
operator|||
name|outputFilename
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
name|result
init|=
name|overlayer
operator|.
name|overlay
argument_list|(
name|specificPageOverlayFile
argument_list|)
init|)
block|{
name|result
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
name|LOG
operator|.
name|error
argument_list|(
literal|"Overlay failed: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
comment|// close the input files AFTER saving the resulting file as some
comment|// streams are shared among the input and the output files
name|overlayer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|String
name|message
init|=
literal|"Usage: java -jar pdfbox-app-x.y.z.jar OverlayPDF<inputfile> [options]<outputfile>\n"
operator|+
literal|"\nOptions:\n"
operator|+
literal|"<inputfile>                                  : input file\n"
operator|+
literal|"<defaultOverlay.pdf>                         : default overlay file\n"
operator|+
literal|"  -odd<oddPageOverlay.pdf>                    : overlay file used for odd pages\n"
operator|+
literal|"  -even<evenPageOverlay.pdf>                  : overlay file used for even pages\n"
operator|+
literal|"  -first<firstPageOverlay.pdf>                : overlay file used for the first page\n"
operator|+
literal|"  -last<lastPageOverlay.pdf>                  : overlay file used for the last page\n"
operator|+
literal|"  -useAllPages<allPagesOverlay.pdf>           : overlay file used for overlay, all pages"
operator|+
literal|" are used by simply repeating them\n"
operator|+
literal|"  -page<pageNumber><specificPageOverlay.pdf> : overlay file used for "
operator|+
literal|"the given page number, may occur more than once\n"
operator|+
literal|"  -position foreground|background              : where to put the overlay "
operator|+
literal|"file: foreground or background\n"
operator|+
literal|"<outputfile>                                 : output file"
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
specifier|private
specifier|static
name|boolean
name|isInteger
parameter_list|(
name|String
name|str
parameter_list|)
block|{
try|try
block|{
name|Integer
operator|.
name|parseInt
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nfe
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

