begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
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
name|javax
operator|.
name|activation
operator|.
name|DataSource
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
name|COSBase
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
name|XrefTrailerResolver
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
name|preflight
operator|.
name|ValidationResult
operator|.
name|ValidationError
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
name|preflight
operator|.
name|font
operator|.
name|container
operator|.
name|FontContainer
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
name|preflight
operator|.
name|graphic
operator|.
name|ICCProfileWrapper
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
name|preflight
operator|.
name|utils
operator|.
name|COSUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_class
specifier|public
class|class
name|PreflightContext
implements|implements
name|Closeable
block|{
comment|/**      * Contains the list of font name embedded in the PDF document.      */
specifier|protected
name|Map
argument_list|<
name|COSBase
argument_list|,
name|FontContainer
argument_list|>
name|fontContainers
init|=
operator|new
name|HashMap
argument_list|<
name|COSBase
argument_list|,
name|FontContainer
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * The PDFbox object representation of the PDF source.      */
specifier|protected
name|PreflightDocument
name|document
init|=
literal|null
decl_stmt|;
comment|/**      * The datasource to load the document from      */
specifier|protected
name|DataSource
name|source
init|=
literal|null
decl_stmt|;
comment|/**      * Contains all Xref/trailer objects and resolves them into single object using startxref reference.      */
specifier|private
name|XrefTrailerResolver
name|xrefTrailerResolver
decl_stmt|;
comment|/**      * This wrapper contains the ICCProfile used by the PDF file.      */
specifier|protected
name|ICCProfileWrapper
name|iccProfileWrapper
init|=
literal|null
decl_stmt|;
comment|/**      *       */
specifier|protected
name|boolean
name|iccProfileAlreadySearched
init|=
literal|false
decl_stmt|;
comment|/**      * MetaData of the current pdf file.      */
specifier|protected
name|XMPMetadata
name|metadata
init|=
literal|null
decl_stmt|;
specifier|protected
name|PreflightConfiguration
name|config
init|=
literal|null
decl_stmt|;
specifier|protected
name|PreflightPath
name|validationPath
init|=
operator|new
name|PreflightPath
argument_list|()
decl_stmt|;
specifier|protected
name|Integer
name|currentPageNumber
init|=
literal|null
decl_stmt|;
comment|/**      * Create the DocumentHandler using the DataSource which represent the PDF file to check.      *       * @param source      */
specifier|public
name|PreflightContext
parameter_list|(
name|DataSource
name|source
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
specifier|public
name|PreflightContext
parameter_list|(
name|DataSource
name|source
parameter_list|,
name|PreflightConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
block|}
comment|/**      * @return the metadata      */
specifier|public
name|XMPMetadata
name|getMetadata
parameter_list|()
block|{
return|return
name|metadata
return|;
block|}
comment|/**      * @param metadata      *            the metadata to set      */
specifier|public
name|void
name|setMetadata
parameter_list|(
name|XMPMetadata
name|metadata
parameter_list|)
block|{
name|this
operator|.
name|metadata
operator|=
name|metadata
expr_stmt|;
block|}
comment|/**      * @return the PDFBox object representation of the document      */
specifier|public
name|PreflightDocument
name|getDocument
parameter_list|()
block|{
return|return
name|document
return|;
block|}
specifier|public
name|XrefTrailerResolver
name|getXrefTrailerResolver
parameter_list|()
block|{
return|return
name|xrefTrailerResolver
return|;
block|}
specifier|public
name|void
name|setXrefTrailerResolver
parameter_list|(
name|XrefTrailerResolver
name|xrefTrailerResolver
parameter_list|)
block|{
name|this
operator|.
name|xrefTrailerResolver
operator|=
name|xrefTrailerResolver
expr_stmt|;
block|}
comment|/**      * Initialize the PDFBox object which present the PDF File.      *       * @param document      */
specifier|public
name|void
name|setDocument
parameter_list|(
name|PreflightDocument
name|document
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
block|}
comment|/**      *       * @return The datasource of the pdf document      */
specifier|public
name|DataSource
name|getSource
parameter_list|()
block|{
return|return
name|source
return|;
block|}
specifier|public
name|boolean
name|isComplete
parameter_list|()
block|{
return|return
operator|(
name|document
operator|!=
literal|null
operator|)
operator|&&
operator|(
name|source
operator|!=
literal|null
operator|)
return|;
block|}
comment|/**      * Add a FontContainer to allow TextObject validation.      *       * @param cBase the COSBase for the font container.      * @param fc the font container.      */
specifier|public
name|void
name|addFontContainer
parameter_list|(
name|COSBase
name|cBase
parameter_list|,
name|FontContainer
name|fc
parameter_list|)
block|{
name|this
operator|.
name|fontContainers
operator|.
name|put
argument_list|(
name|cBase
argument_list|,
name|fc
argument_list|)
expr_stmt|;
block|}
comment|/**      * Return the FontContainer identified by the COSBase. If the given object is missing from the      * {@link #fontContainers} map, the null value is returned.      *       * @param cBase the COSBase for the font container      * @return the font container.      */
specifier|public
name|FontContainer
name|getFontContainer
parameter_list|(
name|COSBase
name|cBase
parameter_list|)
block|{
return|return
name|this
operator|.
name|fontContainers
operator|.
name|get
argument_list|(
name|cBase
argument_list|)
return|;
block|}
comment|/**      * @return the iccProfileWrapper      */
specifier|public
name|ICCProfileWrapper
name|getIccProfileWrapper
parameter_list|()
block|{
return|return
name|iccProfileWrapper
return|;
block|}
comment|/**      * @param iccProfileWrapper      *            the iccProfileWrapper to set      */
specifier|public
name|void
name|setIccProfileWrapper
parameter_list|(
name|ICCProfileWrapper
name|iccProfileWrapper
parameter_list|)
block|{
name|this
operator|.
name|iccProfileWrapper
operator|=
name|iccProfileWrapper
expr_stmt|;
block|}
specifier|public
name|PreflightConfiguration
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
specifier|public
name|void
name|setConfig
parameter_list|(
name|PreflightConfiguration
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
comment|/**      * Close all opened resources      */
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|COSUtils
operator|.
name|closeDocumentQuietly
argument_list|(
name|document
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add the given error the PreflightDocument      *       * @param error      */
specifier|public
name|void
name|addValidationError
parameter_list|(
name|ValidationError
name|error
parameter_list|)
block|{
name|PreflightDocument
name|pfDoc
init|=
name|this
operator|.
name|document
decl_stmt|;
name|error
operator|.
name|setPageNumber
argument_list|(
name|currentPageNumber
argument_list|)
expr_stmt|;
name|pfDoc
operator|.
name|addValidationError
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add the given errors the PreflightDocument      *       * @param errors the list of validation errors.      */
specifier|public
name|void
name|addValidationErrors
parameter_list|(
name|List
argument_list|<
name|ValidationError
argument_list|>
name|errors
parameter_list|)
block|{
name|PreflightDocument
name|pfDoc
init|=
name|this
operator|.
name|document
decl_stmt|;
for|for
control|(
name|ValidationError
name|error
range|:
name|errors
control|)
block|{
name|pfDoc
operator|.
name|addValidationError
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|PreflightPath
name|getValidationPath
parameter_list|()
block|{
return|return
name|validationPath
return|;
block|}
specifier|public
name|void
name|setValidationPath
parameter_list|(
name|PreflightPath
name|validationPath
parameter_list|)
block|{
name|this
operator|.
name|validationPath
operator|=
name|validationPath
expr_stmt|;
block|}
specifier|public
name|boolean
name|isIccProfileAlreadySearched
parameter_list|()
block|{
return|return
name|iccProfileAlreadySearched
return|;
block|}
specifier|public
name|void
name|setIccProfileAlreadySearched
parameter_list|(
name|boolean
name|iccProfileAlreadySearched
parameter_list|)
block|{
name|this
operator|.
name|iccProfileAlreadySearched
operator|=
name|iccProfileAlreadySearched
expr_stmt|;
block|}
comment|/**      * Sets or resets the current page number.      *      * @param currentPageNumber zero based page number or null if none is known.      */
specifier|public
name|void
name|setCurrentPageNumber
parameter_list|(
name|Integer
name|currentPageNumber
parameter_list|)
block|{
name|this
operator|.
name|currentPageNumber
operator|=
name|currentPageNumber
expr_stmt|;
block|}
comment|/**      * Returns the current page number or null if none is known.      */
specifier|public
name|Integer
name|getCurrentPageNumber
parameter_list|()
block|{
return|return
name|currentPageNumber
return|;
block|}
block|}
end_class

end_unit

