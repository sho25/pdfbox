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
name|font
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
name|GeneralPath
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

begin_comment
comment|/**  * A Type 1-equivalent font in a PDF, i.e. a font which can access glyphs by their PostScript name.  * May be a PFB, CFF, or TTF.  *  * @author John Hewson  */
end_comment

begin_interface
specifier|public
interface|interface
name|PDType1Equivalent
block|{
comment|/**      * Returns the name of this font.      */
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Returns true if the font contains a glyph with the given name.      */
specifier|public
name|boolean
name|hasGlyph
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the glyph name for the given character code.      *      * @param code character code      * @return PostScript glyph name      */
specifier|public
name|String
name|codeToName
parameter_list|(
name|int
name|code
parameter_list|)
function_decl|;
comment|/**      * Returns the glyph path for the given character code.      * @param name PostScript glyph name      * @throws java.io.IOException if the font could not be read      */
specifier|public
name|GeneralPath
name|getPath
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

