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
comment|/**  * Scale of the image on printed pages.  *  * @author John Hewson  */
end_comment

begin_enum
specifier|public
enum|enum
name|Scaling
block|{
comment|/** Print the image at 100% scale. */
name|ACTUAL_SIZE
block|,
comment|/** Shrink the image to fit the page, if needed. */
name|SHRINK_TO_FIT
block|,
comment|/** Stretch the image to fill the page, if needed. */
name|STRETCH_TO_FIT
block|,
comment|/** Stretch or shrink the image to fill the page, as needed. */
name|SCALE_TO_FIT
block|}
end_enum

end_unit

