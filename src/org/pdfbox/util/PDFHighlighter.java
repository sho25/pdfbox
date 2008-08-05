begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|util
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
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|org
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|PDPage
import|;
end_import

begin_comment
comment|/**  * Highlighting of words in a PDF document with an XML file.  *  * @author slagraulet (slagraulet@cardiweb.com)  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.7 $  *  * @see<a href="http://partners.adobe.com/public/developer/en/pdf/HighlightFileFormat.pdf">  *      Adobe Highlight File Format</a>  */
end_comment

begin_class
specifier|public
class|class
name|PDFHighlighter
extends|extends
name|PDFTextStripper
block|{
specifier|private
name|Writer
name|highlighterOutput
init|=
literal|null
decl_stmt|;
comment|//private Color highlightColor = Color.YELLOW;
specifier|private
name|String
index|[]
name|searchedWords
decl_stmt|;
specifier|private
name|ByteArrayOutputStream
name|textOS
init|=
literal|null
decl_stmt|;
specifier|private
name|Writer
name|textWriter
init|=
literal|null
decl_stmt|;
comment|/**      * Default constructor.      *      * @throws IOException If there is an error constructing this class.      */
specifier|public
name|PDFHighlighter
parameter_list|()
throws|throws
name|IOException
block|{
name|super
argument_list|()
expr_stmt|;
name|super
operator|.
name|setLineSeparator
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|super
operator|.
name|setPageSeparator
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|super
operator|.
name|setWordSeparator
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|super
operator|.
name|setShouldSeparateByBeads
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|super
operator|.
name|setSuppressDuplicateOverlappingText
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Generate an XML highlight string based on the PDF.      *      * @param pdDocument The PDF to find words in.      * @param highlightWord The word to search for.      * @param xmlOutput The resulting output xml file.      *      * @throws IOException If there is an error reading from the PDF, or writing to the XML.      */
specifier|public
name|void
name|generateXMLHighlight
parameter_list|(
name|PDDocument
name|pdDocument
parameter_list|,
name|String
name|highlightWord
parameter_list|,
name|Writer
name|xmlOutput
parameter_list|)
throws|throws
name|IOException
block|{
name|generateXMLHighlight
argument_list|(
name|pdDocument
argument_list|,
operator|new
name|String
index|[]
block|{
name|highlightWord
block|}
argument_list|,
name|xmlOutput
argument_list|)
expr_stmt|;
block|}
comment|/**      * Generate an XML highlight string based on the PDF.      *      * @param pdDocument The PDF to find words in.      * @param sWords The words to search for.      * @param xmlOutput The resulting output xml file.      *      * @throws IOException If there is an error reading from the PDF, or writing to the XML.      */
specifier|public
name|void
name|generateXMLHighlight
parameter_list|(
name|PDDocument
name|pdDocument
parameter_list|,
name|String
index|[]
name|sWords
parameter_list|,
name|Writer
name|xmlOutput
parameter_list|)
throws|throws
name|IOException
block|{
name|highlighterOutput
operator|=
name|xmlOutput
expr_stmt|;
name|searchedWords
operator|=
name|sWords
expr_stmt|;
name|highlighterOutput
operator|.
name|write
argument_list|(
literal|"<XML>\n<Body units=characters "
operator|+
comment|//color and mode are not implemented by the highlight spec
comment|//so don't include them for now
comment|//" color=#" + getHighlightColorAsString() +
comment|//" mode=active " + */
literal|" version=2>\n<Highlight>\n"
argument_list|)
expr_stmt|;
name|textOS
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
name|textWriter
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
name|textOS
argument_list|,
literal|"UTF-16"
argument_list|)
expr_stmt|;
name|writeText
argument_list|(
name|pdDocument
argument_list|,
name|textWriter
argument_list|)
expr_stmt|;
name|highlighterOutput
operator|.
name|write
argument_list|(
literal|"</Highlight>\n</Body>\n</XML>"
argument_list|)
expr_stmt|;
name|highlighterOutput
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|protected
name|void
name|endPage
parameter_list|(
name|PDPage
name|pdPage
parameter_list|)
throws|throws
name|IOException
block|{
name|textWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|String
name|page
init|=
operator|new
name|String
argument_list|(
name|textOS
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"UTF-16"
argument_list|)
decl_stmt|;
name|textOS
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|//page = page.replaceAll( "\n", "" );
comment|//page = page.replaceAll( "\r", "" );
comment|//page = CCRStringUtil.stripChar(page, '\n');
comment|//page = CCRStringUtil.stripChar(page, '\r');
comment|// Traitement des listes � puces (caract�res sp�ciaux)
if|if
condition|(
name|page
operator|.
name|indexOf
argument_list|(
literal|"a"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|page
operator|=
name|page
operator|.
name|replaceAll
argument_list|(
literal|"a[0-9]{1,3}"
argument_list|,
literal|"."
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|searchedWords
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|searchedWords
index|[
name|i
index|]
argument_list|,
name|Pattern
operator|.
name|CASE_INSENSITIVE
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|page
argument_list|)
decl_stmt|;
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|int
name|begin
init|=
name|matcher
operator|.
name|start
argument_list|()
decl_stmt|;
name|int
name|end
init|=
name|matcher
operator|.
name|end
argument_list|()
decl_stmt|;
name|highlighterOutput
operator|.
name|write
argument_list|(
literal|"<loc "
operator|+
literal|"pg="
operator|+
operator|(
name|getCurrentPageNo
argument_list|()
operator|-
literal|1
operator|)
operator|+
literal|" pos="
operator|+
name|begin
operator|+
literal|" len="
operator|+
operator|(
name|end
operator|-
name|begin
operator|)
operator|+
literal|">\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Command line application.      *      * @param args The command line arguments to the application.      *      * @throws IOException If there is an error generating the highlight file.      */
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
name|PDFHighlighter
name|xmlExtractor
init|=
operator|new
name|PDFHighlighter
argument_list|()
decl_stmt|;
name|PDDocument
name|doc
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<
literal|2
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
name|String
index|[]
name|highlightStrings
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
name|highlightStrings
argument_list|,
literal|0
argument_list|,
name|highlightStrings
operator|.
name|length
argument_list|)
expr_stmt|;
name|doc
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|xmlExtractor
operator|.
name|generateXMLHighlight
argument_list|(
name|doc
argument_list|,
name|highlightStrings
argument_list|,
operator|new
name|OutputStreamWriter
argument_list|(
name|System
operator|.
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|doc
operator|!=
literal|null
condition|)
block|{
name|doc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
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
literal|"usage: java "
operator|+
name|PDFHighlighter
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<pdf file> word1 word2 word3 ..."
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
comment|/**      * Get the color to highlight the strings with.  Default is Color.YELLOW.      *      * @return The color to highlight strings with.      */
comment|/*public Color getHighlightColor()     {         return highlightColor;     }**/
comment|/**      * Get the color to highlight the strings with.  Default is Color.YELLOW.      *      * @param color The color to highlight strings with.      */
comment|/*public void setHighlightColor(Color color)     {         this.highlightColor = color;     }**/
comment|/**      * Set the highlight color using HTML like rgb string.  The string must be 6 characters long.      *      * @param color The color to use for highlighting.  Should be in the format of "FF0000".      */
comment|/*public void setHighlightColor( String color )     {         highlightColor = Color.decode( color );     }**/
comment|/**      * Get the highlight color as an HTML like string.  This will return a string of six characters.      *      * @return The current highlight color.  For example FF0000      */
comment|/*public String getHighlightColorAsString()     {         //BJL: kudos to anyone that has a cleaner way of doing this!         String red = Integer.toHexString( highlightColor.getRed() );         String green = Integer.toHexString( highlightColor.getGreen() );         String blue = Integer.toHexString( highlightColor.getBlue() );          return (red.length()< 2 ? "0" + red : red) +                (green.length()< 2 ? "0" + green : green) +                (blue.length()< 2 ? "0" + blue : blue);     }**/
block|}
end_class

end_unit

