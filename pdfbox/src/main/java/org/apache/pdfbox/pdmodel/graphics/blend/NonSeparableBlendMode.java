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
comment|/**  * Non-separable blend mode (supports blend function).  *  * @author Kühn& Weyh Software, GmbH  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|NonSeparableBlendMode
extends|extends
name|BlendMode
block|{
name|NonSeparableBlendMode
parameter_list|()
block|{     }
specifier|public
specifier|abstract
name|void
name|blend
parameter_list|(
name|float
index|[]
name|srcValues
parameter_list|,
name|float
index|[]
name|dstValues
parameter_list|,
name|float
index|[]
name|result
parameter_list|)
function_decl|;
block|}
end_class

end_unit

