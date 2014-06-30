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
name|util
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
name|encryption
operator|.
name|InvalidPasswordException
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
name|image
operator|.
name|PDImageXObject
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
name|util
operator|.
name|Matrix
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
name|util
operator|.
name|operator
operator|.
name|PDFOperator
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
name|util
operator|.
name|PDFStreamEngine
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
name|util
operator|.
name|ResourceLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
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
name|List
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
name|pdfbox
operator|.
name|pdmodel
operator|.
name|encryption
operator|.
name|StandardDecryptionMaterial
import|;
end_import

begin_comment
comment|/**  * This is an example on how to get the x/y coordinates of image locations.  *  * Usage: java org.apache.pdfbox.examples.util.PrintImageLocations&lt;input-pdf&gt;  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.5 $  */
end_comment

begin_class
specifier|public
class|class
name|PrintImageLocations
extends|extends
name|PDFStreamEngine
block|{
specifier|private
specifier|static
specifier|final
name|String
name|INVOKE_OPERATOR
init|=
literal|"Do"
decl_stmt|;
comment|/**      * Default constructor.      *      * @throws IOException If there is an error loading text stripper properties.      */
specifier|public
name|PrintImageLocations
parameter_list|()
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|ResourceLoader
operator|.
name|loadProperties
argument_list|(
literal|"org/apache/pdfbox/resources/PDFTextStripper.properties"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will print the documents data.      *      * @param args The command line arguments.      *      * @throws Exception If there is an error parsing the document.      */
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
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|document
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
try|try
block|{
name|StandardDecryptionMaterial
name|sdm
init|=
operator|new
name|StandardDecryptionMaterial
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|document
operator|.
name|openProtection
argument_list|(
name|sdm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidPasswordException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error: Document is encrypted with a password."
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
name|PrintImageLocations
name|printer
init|=
operator|new
name|PrintImageLocations
argument_list|()
decl_stmt|;
name|List
name|allPages
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
argument_list|()
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
name|allPages
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PDPage
name|page
init|=
operator|(
name|PDPage
operator|)
name|allPages
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Processing page: "
operator|+
name|i
argument_list|)
expr_stmt|;
name|printer
operator|.
name|processStream
argument_list|(
name|page
operator|.
name|findResources
argument_list|()
argument_list|,
name|page
operator|.
name|getContents
argument_list|()
operator|.
name|getStream
argument_list|()
argument_list|,
name|page
operator|.
name|findCropBox
argument_list|()
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
comment|/**      * This is used to handle an operation.      *      * @param operator The operation to perform.      * @param arguments The list of arguments.      *      * @throws IOException If there is an error processing the operation.      */
specifier|protected
name|void
name|processOperator
parameter_list|(
name|PDFOperator
name|operator
parameter_list|,
name|List
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|operation
init|=
name|operator
operator|.
name|getOperation
argument_list|()
decl_stmt|;
if|if
condition|(
name|INVOKE_OPERATOR
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|COSName
name|objectName
init|=
operator|(
name|COSName
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
name|xobjects
init|=
name|getResources
argument_list|()
operator|.
name|getXObjects
argument_list|()
decl_stmt|;
name|PDXObject
name|xobject
init|=
operator|(
name|PDXObject
operator|)
name|xobjects
operator|.
name|get
argument_list|(
name|objectName
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|xobject
operator|instanceof
name|PDImageXObject
condition|)
block|{
name|PDImageXObject
name|image
init|=
operator|(
name|PDImageXObject
operator|)
name|xobject
decl_stmt|;
name|int
name|imageWidth
init|=
name|image
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|imageHeight
init|=
name|image
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"*******************************************************************"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Found image ["
operator|+
name|objectName
operator|.
name|getName
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|Matrix
name|ctmNew
init|=
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentTransformationMatrix
argument_list|()
decl_stmt|;
name|AffineTransform
name|imageTransform
init|=
name|ctmNew
operator|.
name|createAffineTransform
argument_list|()
decl_stmt|;
name|imageTransform
operator|.
name|scale
argument_list|(
literal|1.0
operator|/
name|imageWidth
argument_list|,
operator|-
literal|1.0
operator|/
name|imageHeight
argument_list|)
expr_stmt|;
name|imageTransform
operator|.
name|translate
argument_list|(
literal|0
argument_list|,
operator|-
name|imageHeight
argument_list|)
expr_stmt|;
name|double
name|imageXScale
init|=
name|imageTransform
operator|.
name|getScaleX
argument_list|()
decl_stmt|;
name|double
name|imageYScale
init|=
name|imageTransform
operator|.
name|getScaleY
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"position = "
operator|+
name|ctmNew
operator|.
name|getXPosition
argument_list|()
operator|+
literal|", "
operator|+
name|ctmNew
operator|.
name|getYPosition
argument_list|()
argument_list|)
expr_stmt|;
comment|// size in pixel
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"size = "
operator|+
name|imageWidth
operator|+
literal|"px, "
operator|+
name|imageHeight
operator|+
literal|"px"
argument_list|)
expr_stmt|;
comment|// size in page units
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"size = "
operator|+
name|imageXScale
operator|+
literal|", "
operator|+
name|imageYScale
argument_list|)
expr_stmt|;
comment|// size in inches
name|imageXScale
operator|/=
literal|72
expr_stmt|;
name|imageYScale
operator|/=
literal|72
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"size = "
operator|+
name|imageXScale
operator|+
literal|"in, "
operator|+
name|imageYScale
operator|+
literal|"in"
argument_list|)
expr_stmt|;
comment|// size in millimeter
name|imageXScale
operator|*=
literal|25.4
expr_stmt|;
name|imageYScale
operator|*=
literal|25.4
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"size = "
operator|+
name|imageXScale
operator|+
literal|"mm, "
operator|+
name|imageYScale
operator|+
literal|"mm"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xobject
operator|instanceof
name|PDFormXObject
condition|)
block|{
comment|// save the graphics state
name|saveGraphicsState
argument_list|()
expr_stmt|;
name|PDFormXObject
name|form
init|=
operator|(
name|PDFormXObject
operator|)
name|xobject
decl_stmt|;
name|COSStream
name|invoke
init|=
operator|(
name|COSStream
operator|)
name|form
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
name|PDResources
name|pdResources
init|=
name|form
operator|.
name|getResources
argument_list|()
decl_stmt|;
comment|// if there is an optional form matrix, we have to map the form space to the user space
name|Matrix
name|matrix
init|=
name|form
operator|.
name|getMatrix
argument_list|()
decl_stmt|;
if|if
condition|(
name|matrix
operator|!=
literal|null
condition|)
block|{
name|Matrix
name|xobjectCTM
init|=
name|matrix
operator|.
name|multiply
argument_list|(
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentTransformationMatrix
argument_list|()
argument_list|)
decl_stmt|;
name|getGraphicsState
argument_list|()
operator|.
name|setCurrentTransformationMatrix
argument_list|(
name|xobjectCTM
argument_list|)
expr_stmt|;
block|}
name|processSubStream
argument_list|(
name|pdResources
argument_list|,
name|invoke
argument_list|)
expr_stmt|;
comment|// restore the graphics state
name|restoreGraphicsState
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|super
operator|.
name|processOperator
argument_list|(
name|operator
argument_list|,
name|arguments
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will print the usage for this document.      */
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
literal|"Usage: java org.apache.pdfbox.examples.pdmodel.PrintImageLocations<input-pdf>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

