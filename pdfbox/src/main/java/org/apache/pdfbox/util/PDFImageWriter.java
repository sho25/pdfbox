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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|HeadlessException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Toolkit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
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
name|Properties
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

begin_comment
comment|/**  * This class writes single pages of a pdf to a file.  *   * @author<a href="mailto:DanielWilson@Users.SourceForge.net">Daniel Wilson</a>  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|PDFImageWriter
extends|extends
name|PDFStreamEngine
block|{
comment|/**      * Log instance.      */
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
name|PDFImageWriter
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Instantiate a new PDFImageWriter object.      */
specifier|public
name|PDFImageWriter
parameter_list|()
block|{     }
comment|/**      * Instantiate a new PDFImageWriter object. Loading all of the operator mappings from the properties object that is      * passed in.      *       * @param props      *            The properties containing the mapping of operators to PDFOperator classes.      *       * @throws IOException      *             If there is an error reading the properties.      */
specifier|public
name|PDFImageWriter
parameter_list|(
name|Properties
name|props
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
comment|/**      * Converts a given page range of a PDF document to bitmap images.      *       * @param document      *            the PDF document      * @param imageType      *            the target format (ex. "png")      * @param password      *            the password (needed if the PDF is encrypted)      * @param startPage      *            the start page (1 is the first page)      * @param endPage      *            the end page (set to Integer.MAX_VALUE for all pages)      * @param outputPrefix      *            used to construct the filename for the individual images      * @return true if the images were produced, false if there was an error      * @throws IOException      *             if an I/O error occurs      */
specifier|public
name|boolean
name|writeImage
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|String
name|imageType
parameter_list|,
name|String
name|password
parameter_list|,
name|int
name|startPage
parameter_list|,
name|int
name|endPage
parameter_list|,
name|String
name|outputPrefix
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|resolution
decl_stmt|;
try|try
block|{
name|resolution
operator|=
name|Toolkit
operator|.
name|getDefaultToolkit
argument_list|()
operator|.
name|getScreenResolution
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HeadlessException
name|e
parameter_list|)
block|{
name|resolution
operator|=
literal|96
expr_stmt|;
block|}
return|return
name|writeImage
argument_list|(
name|document
argument_list|,
name|imageType
argument_list|,
name|password
argument_list|,
name|startPage
argument_list|,
name|endPage
argument_list|,
name|outputPrefix
argument_list|,
literal|8
argument_list|,
name|resolution
argument_list|)
return|;
block|}
comment|/**      * Converts a given page range of a PDF document to bitmap images.      *       * @param document      *            the PDF document      * @param imageFormat      *            the target format (ex. "png")      * @param password      *            the password (needed if the PDF is encrypted)      * @param startPage      *            the start page (1 is the first page)      * @param endPage      *            the end page (set to Integer.MAX_VALUE for all pages)      * @param outputPrefix      *            used to construct the filename for the individual images      * @param imageType      *            the image type (see {@link BufferedImage}.TYPE_*)      * @param resolution      *            the resolution in dpi (dots per inch)      * @return true if the images were produced, false if there was an error      * @throws IOException      *             if an I/O error occurs      */
specifier|public
name|boolean
name|writeImage
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|String
name|imageFormat
parameter_list|,
name|String
name|password
parameter_list|,
name|int
name|startPage
parameter_list|,
name|int
name|endPage
parameter_list|,
name|String
name|outputPrefix
parameter_list|,
name|int
name|imageType
parameter_list|,
name|int
name|resolution
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|bSuccess
init|=
literal|true
decl_stmt|;
name|List
argument_list|<
name|PDPage
argument_list|>
name|pages
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
argument_list|()
decl_stmt|;
name|int
name|pagesSize
init|=
name|pages
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|startPage
operator|-
literal|1
init|;
name|i
operator|<
name|endPage
operator|&&
name|i
operator|<
name|pagesSize
condition|;
name|i
operator|++
control|)
block|{
name|PDPage
name|page
init|=
name|pages
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|BufferedImage
name|image
init|=
name|page
operator|.
name|convertToImage
argument_list|(
name|imageType
argument_list|,
name|resolution
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
name|outputPrefix
operator|+
operator|(
name|i
operator|+
literal|1
operator|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Writing: "
operator|+
name|fileName
operator|+
literal|"."
operator|+
name|imageFormat
argument_list|)
expr_stmt|;
name|bSuccess
operator|&=
name|ImageIOUtil
operator|.
name|writeImage
argument_list|(
name|image
argument_list|,
name|imageFormat
argument_list|,
name|fileName
argument_list|,
name|imageType
argument_list|,
name|resolution
argument_list|)
expr_stmt|;
block|}
return|return
name|bSuccess
return|;
block|}
block|}
end_class

end_unit

