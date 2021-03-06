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
name|awt
operator|.
name|geom
operator|.
name|Rectangle2D
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|apache
operator|.
name|pdfbox
operator|.
name|Loader
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
name|common
operator|.
name|PDRectangle
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
name|interactive
operator|.
name|action
operator|.
name|PDAction
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
name|interactive
operator|.
name|action
operator|.
name|PDActionURI
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotation
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
name|text
operator|.
name|PDFTextStripperByArea
import|;
end_import

begin_comment
comment|/**  * This is an example of how to access a URL in a PDF document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PrintURLs
block|{
comment|/**      * Constructor.      */
specifier|private
name|PrintURLs
parameter_list|()
block|{
comment|//utility class
block|}
comment|/**      * This will output all URLs and the texts in the annotation rectangle of a document.      *<br>      * see usage() for commandline      *      * @param args Command line arguments.      *      * @throws IOException If there is an error extracting the URLs.      */
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
operator|!=
literal|1
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|pageNum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|PDPage
name|page
range|:
name|doc
operator|.
name|getPages
argument_list|()
control|)
block|{
name|pageNum
operator|++
expr_stmt|;
name|PDFTextStripperByArea
name|stripper
init|=
operator|new
name|PDFTextStripperByArea
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PDAnnotation
argument_list|>
name|annotations
init|=
name|page
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
comment|//first setup text extraction regions
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|annotations
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|PDAnnotation
name|annot
init|=
name|annotations
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
if|if
condition|(
name|getActionURI
argument_list|(
name|annot
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|PDRectangle
name|rect
init|=
name|annot
operator|.
name|getRectangle
argument_list|()
decl_stmt|;
comment|//need to reposition link rectangle to match text space
name|float
name|x
init|=
name|rect
operator|.
name|getLowerLeftX
argument_list|()
decl_stmt|;
name|float
name|y
init|=
name|rect
operator|.
name|getUpperRightY
argument_list|()
decl_stmt|;
name|float
name|width
init|=
name|rect
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|float
name|height
init|=
name|rect
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|rotation
init|=
name|page
operator|.
name|getRotation
argument_list|()
decl_stmt|;
if|if
condition|(
name|rotation
operator|==
literal|0
condition|)
block|{
name|PDRectangle
name|pageSize
init|=
name|page
operator|.
name|getMediaBox
argument_list|()
decl_stmt|;
comment|// area stripper uses java coordinates, not PDF coordinates
name|y
operator|=
name|pageSize
operator|.
name|getHeight
argument_list|()
operator|-
name|y
expr_stmt|;
block|}
else|else
block|{
comment|// do nothing
comment|// please send us a sample file
block|}
name|Rectangle2D
operator|.
name|Float
name|awtRect
init|=
operator|new
name|Rectangle2D
operator|.
name|Float
argument_list|(
name|x
argument_list|,
name|y
argument_list|,
name|width
argument_list|,
name|height
argument_list|)
decl_stmt|;
name|stripper
operator|.
name|addRegion
argument_list|(
literal|""
operator|+
name|j
argument_list|,
name|awtRect
argument_list|)
expr_stmt|;
block|}
block|}
name|stripper
operator|.
name|extractRegions
argument_list|(
name|page
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|annotations
operator|.
name|size
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|PDAnnotation
name|annot
init|=
name|annotations
operator|.
name|get
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|PDActionURI
name|uri
init|=
name|getActionURI
argument_list|(
name|annot
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|String
name|urlText
init|=
name|stripper
operator|.
name|getTextForRegion
argument_list|(
literal|""
operator|+
name|j
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Page "
operator|+
name|pageNum
operator|+
literal|":'"
operator|+
name|urlText
operator|.
name|trim
argument_list|()
operator|+
literal|"'="
operator|+
name|uri
operator|.
name|getURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
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
name|PDActionURI
name|getActionURI
parameter_list|(
name|PDAnnotation
name|annot
parameter_list|)
block|{
comment|// use reflection to catch all annotation types that have getAction()
comment|// If you can't use reflection, then check for classes
comment|// PDAnnotationLink and PDAnnotationWidget, and call getAction() and check for a
comment|// PDActionURI result type
try|try
block|{
name|Method
name|actionMethod
init|=
name|annot
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getAction"
argument_list|)
decl_stmt|;
if|if
condition|(
name|actionMethod
operator|.
name|getReturnType
argument_list|()
operator|.
name|equals
argument_list|(
name|PDAction
operator|.
name|class
argument_list|)
condition|)
block|{
name|PDAction
name|action
init|=
operator|(
name|PDAction
operator|)
name|actionMethod
operator|.
name|invoke
argument_list|(
name|annot
argument_list|)
decl_stmt|;
if|if
condition|(
name|action
operator|instanceof
name|PDActionURI
condition|)
block|{
return|return
operator|(
name|PDActionURI
operator|)
name|action
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
decl||
name|IllegalAccessException
decl||
name|InvocationTargetException
name|e
parameter_list|)
block|{         }
return|return
literal|null
return|;
block|}
comment|/**      * This will print out a message telling how to use this example.      */
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
literal|"usage: "
operator|+
name|PrintURLs
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"<input-file>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

