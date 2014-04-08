begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|rendering
operator|.
name|printing
package|;
end_package

begin_comment
comment|/**  * Orientation of printed pages.  *  * @author John Hewson  */
end_comment

begin_enum
specifier|public
enum|enum
name|Orientation
block|{
comment|/** Automatically select the orientation of each page based on its aspect ratio.  */
name|AUTO
block|,
comment|/** Print all pages as landscape. */
name|LANDSCAPE
block|,
comment|/** Print all pages as portrait. */
name|PORTRAIT
block|}
end_enum

end_unit

