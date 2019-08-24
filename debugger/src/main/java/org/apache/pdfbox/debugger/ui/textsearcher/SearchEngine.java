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
name|debugger
operator|.
name|ui
operator|.
name|textsearcher
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|BadLocationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|Highlighter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|JTextComponent
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

begin_comment
comment|/**  * @author Khyrul Bashar  * A class that search a word in the JTextComponent and if find highlights them.  */
end_comment

begin_class
class|class
name|SearchEngine
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
name|SearchEngine
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Document
name|document
decl_stmt|;
specifier|private
specifier|final
name|Highlighter
name|highlighter
decl_stmt|;
specifier|private
specifier|final
name|Highlighter
operator|.
name|HighlightPainter
name|painter
decl_stmt|;
comment|/**      * Constructor.      * @param textComponent JTextComponent that is to be searched.      * @param painter Highlighter.HighlightPainter instance to paint the highlights.      */
name|SearchEngine
parameter_list|(
name|JTextComponent
name|textComponent
parameter_list|,
name|Highlighter
operator|.
name|HighlightPainter
name|painter
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|textComponent
operator|.
name|getDocument
argument_list|()
expr_stmt|;
name|this
operator|.
name|highlighter
operator|=
name|textComponent
operator|.
name|getHighlighter
argument_list|()
expr_stmt|;
name|this
operator|.
name|painter
operator|=
name|painter
expr_stmt|;
block|}
comment|/**      * Search the word.      * @param searchKey String. Search word.      * @param isCaseSensitive boolean. If search is case sensitive.      * @return ArrayList<Highlighter.Highlight>.      */
specifier|public
name|List
argument_list|<
name|Highlighter
operator|.
name|Highlight
argument_list|>
name|search
parameter_list|(
name|String
name|searchKey
parameter_list|,
name|boolean
name|isCaseSensitive
parameter_list|)
block|{
name|List
argument_list|<
name|Highlighter
operator|.
name|Highlight
argument_list|>
name|highlights
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|searchKey
operator|!=
literal|null
condition|)
block|{
name|highlighter
operator|.
name|removeAllHighlights
argument_list|()
expr_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|searchKey
argument_list|)
condition|)
block|{
return|return
name|highlights
return|;
block|}
name|String
name|textContent
decl_stmt|;
try|try
block|{
name|textContent
operator|=
name|document
operator|.
name|getText
argument_list|(
literal|0
argument_list|,
name|document
operator|.
name|getLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|highlights
return|;
block|}
if|if
condition|(
operator|!
name|isCaseSensitive
condition|)
block|{
name|textContent
operator|=
name|textContent
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
name|searchKey
operator|=
name|searchKey
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
block|}
name|int
name|searchKeyLength
init|=
name|searchKey
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|startAt
init|=
literal|0
decl_stmt|;
name|int
name|resultantOffset
decl_stmt|;
name|int
name|indexOfHighLight
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|resultantOffset
operator|=
name|textContent
operator|.
name|indexOf
argument_list|(
name|searchKey
argument_list|,
name|startAt
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
try|try
block|{
name|highlighter
operator|.
name|addHighlight
argument_list|(
name|resultantOffset
argument_list|,
name|resultantOffset
operator|+
name|searchKeyLength
argument_list|,
name|painter
argument_list|)
expr_stmt|;
name|highlights
operator|.
name|add
argument_list|(
name|highlighter
operator|.
name|getHighlights
argument_list|()
index|[
name|indexOfHighLight
operator|++
index|]
argument_list|)
expr_stmt|;
name|startAt
operator|=
name|resultantOffset
operator|+
name|searchKeyLength
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BadLocationException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|highlights
return|;
block|}
block|}
end_class

end_unit

