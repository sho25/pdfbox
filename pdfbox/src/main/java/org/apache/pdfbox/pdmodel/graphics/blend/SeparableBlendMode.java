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
name|graphics
operator|.
name|blend
package|;
end_package

begin_comment
comment|/**  * Separable blend mode (support blendChannel)  *  * @author Kühn& Weyh Software, GmbH  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|SeparableBlendMode
extends|extends
name|BlendMode
block|{
name|SeparableBlendMode
parameter_list|()
block|{     }
specifier|public
specifier|abstract
name|float
name|blendChannel
parameter_list|(
name|float
name|srcValue
parameter_list|,
name|float
name|dstValue
parameter_list|)
function_decl|;
block|}
end_class

end_unit

