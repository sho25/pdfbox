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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|exceptions
operator|.
name|SignatureException
import|;
end_import

begin_comment
comment|/**  * Providing an interface for accessing necessary functions for signing a pdf document.  *   * @author<a href="mailto:mail@thomas-chojecki.de">Thomas Chojecki</a>  * @version $  */
end_comment

begin_interface
specifier|public
interface|interface
name|SignatureInterface
block|{
comment|/**    * Creates a cms signature for the given content    *     * @param content is the content as a (Filter)InputStream    * @return signature as a byte array    */
specifier|public
name|byte
index|[]
name|sign
parameter_list|(
name|InputStream
name|content
parameter_list|)
throws|throws
name|SignatureException
throws|,
name|IOException
function_decl|;
block|}
end_interface

end_unit

