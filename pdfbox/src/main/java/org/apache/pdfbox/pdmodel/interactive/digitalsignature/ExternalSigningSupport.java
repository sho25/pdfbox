begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
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
name|digitalsignature
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_comment
comment|/**  * Interface for external signature creation scenarios. It contains method for retrieving PDF data  * to be sign and setting created CMS signature to the PDF.  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ExternalSigningSupport
block|{
comment|/**      * Get PDF content to be signed. Obtained InputStream must be closed after use.      *      * @return content stream      */
name|InputStream
name|getContent
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Set CMS signature bytes to PDF.      *      * @param signature CMS signature as byte array      *      * @throws IOException if exception occured during PDF writing      */
name|void
name|setSignature
parameter_list|(
name|byte
index|[]
name|signature
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

