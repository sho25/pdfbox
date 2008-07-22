begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2005, www.fontbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of fontbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.fontbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|fontbox
operator|.
name|ttf
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
name|org
operator|.
name|fontbox
operator|.
name|util
operator|.
name|BoundingBox
import|;
end_import

begin_comment
comment|/**  * A glyph data record in the glyf table.  *   * @author Ben Litchfield (ben@benlitchfield.com)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|GlyphData
block|{
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_ON_CURVE
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_SHORT_X
init|=
literal|1
operator|<<
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_SHORT_Y
init|=
literal|1
operator|<<
literal|2
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_X_MAGIC
init|=
literal|1
operator|<<
literal|3
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|FLAG_Y_MAGIC
init|=
literal|1
operator|<<
literal|4
decl_stmt|;
specifier|private
name|BoundingBox
name|boundingBox
init|=
operator|new
name|BoundingBox
argument_list|()
decl_stmt|;
specifier|private
name|short
name|numberOfContours
decl_stmt|;
specifier|private
name|int
index|[]
name|endPointsOfContours
decl_stmt|;
specifier|private
name|byte
index|[]
name|instructions
decl_stmt|;
specifier|private
name|int
index|[]
name|flags
decl_stmt|;
specifier|private
name|short
index|[]
name|xCoordinates
decl_stmt|;
specifier|private
name|short
index|[]
name|yCoordinates
decl_stmt|;
comment|/**      * This will read the required data from the stream.      *       * @param ttf The font that is being read.      * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|void
name|initData
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|,
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|numberOfContours
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|boundingBox
operator|.
name|setLowerLeftX
argument_list|(
name|data
operator|.
name|readSignedShort
argument_list|()
argument_list|)
expr_stmt|;
name|boundingBox
operator|.
name|setLowerLeftY
argument_list|(
name|data
operator|.
name|readSignedShort
argument_list|()
argument_list|)
expr_stmt|;
name|boundingBox
operator|.
name|setUpperRightX
argument_list|(
name|data
operator|.
name|readSignedShort
argument_list|()
argument_list|)
expr_stmt|;
name|boundingBox
operator|.
name|setUpperRightY
argument_list|(
name|data
operator|.
name|readSignedShort
argument_list|()
argument_list|)
expr_stmt|;
comment|/**if( numberOfContours> 0 )         {             endPointsOfContours = new int[ numberOfContours ];             for( int i=0; i<numberOfContours; i++ )             {                 endPointsOfContours[i] = data.readUnsignedShort();             }             int instructionLength = data.readUnsignedShort();             instructions = data.read( instructionLength );                          //BJL It is possible to read some more information here but PDFBox             //does not need it at this time so just ignore it.                          //not sure if the length of the flags is the number of contours??             //flags = new int[numberOfContours];             //first read the flags, and just so the TTF can save a couples bytes             //we need to check some bit masks to see if there are more bytes or not.             //int currentFlagIndex = 0;             //int currentFlag =                                    }*/
block|}
comment|/**      * @return Returns the boundingBox.      */
specifier|public
name|BoundingBox
name|getBoundingBox
parameter_list|()
block|{
return|return
name|boundingBox
return|;
block|}
comment|/**      * @param boundingBoxValue The boundingBox to set.      */
specifier|public
name|void
name|setBoundingBox
parameter_list|(
name|BoundingBox
name|boundingBoxValue
parameter_list|)
block|{
name|this
operator|.
name|boundingBox
operator|=
name|boundingBoxValue
expr_stmt|;
block|}
comment|/**      * @return Returns the numberOfContours.      */
specifier|public
name|short
name|getNumberOfContours
parameter_list|()
block|{
return|return
name|numberOfContours
return|;
block|}
comment|/**      * @param numberOfContoursValue The numberOfContours to set.      */
specifier|public
name|void
name|setNumberOfContours
parameter_list|(
name|short
name|numberOfContoursValue
parameter_list|)
block|{
name|this
operator|.
name|numberOfContours
operator|=
name|numberOfContoursValue
expr_stmt|;
block|}
block|}
end_class

end_unit

