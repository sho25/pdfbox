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
name|cos
operator|.
name|COSDocument
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
name|VisualSignatureParser
import|;
end_import

begin_class
specifier|public
class|class
name|SignatureOptions
block|{
specifier|private
name|COSDocument
name|visualSignature
decl_stmt|;
specifier|private
name|int
name|pageNo
decl_stmt|;
specifier|public
name|void
name|setPage
parameter_list|(
name|int
name|pageNo
parameter_list|)
block|{
name|this
operator|.
name|pageNo
operator|=
name|pageNo
expr_stmt|;
block|}
specifier|public
name|int
name|getPage
parameter_list|()
block|{
return|return
name|pageNo
return|;
block|}
specifier|public
name|void
name|setVisualSignature
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|VisualSignatureParser
name|visParser
init|=
operator|new
name|VisualSignatureParser
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|visParser
operator|.
name|parse
argument_list|()
expr_stmt|;
name|visualSignature
operator|=
name|visParser
operator|.
name|getDocument
argument_list|()
expr_stmt|;
block|}
specifier|public
name|COSDocument
name|getVisualSignature
parameter_list|()
block|{
return|return
name|visualSignature
return|;
block|}
block|}
end_class

end_unit

