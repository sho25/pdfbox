begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*     Licensed to the Apache Software Foundation (ASF) under one or more    contributor license agreements.  See the NOTICE file distributed with    this work for additional information regarding copyright ownership.    The ASF licenses this file to You under the Apache License, Version 2.0    (the "License"); you may not use this file except in compliance with    the License.  You may obtain a copy of the License at         http://www.apache.org/licenses/LICENSE-2.0     Unless required by applicable law or agreed to in writing, software    distributed under the License is distributed on an "AS IS" BASIS,    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.    See the License for the specific language governing permissions and    limitations under the License.   */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
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

begin_comment
comment|/**  * This class is based on code from Apache Batik a subproject of Apache XMLGraphics.  * see http://xmlgraphics.apache.org/batik/ for further details.  */
end_comment

begin_class
specifier|public
class|class
name|GlyfSimpleDescript
extends|extends
name|GlyfDescript
block|{
specifier|private
name|int
index|[]
name|endPtsOfContours
decl_stmt|;
specifier|private
name|byte
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
specifier|private
name|int
name|pointCount
decl_stmt|;
comment|/**      * Constructor.      *       * @param numberOfContours number of contours      * @param bais the stream to be read      * @throws IOException is thrown if something went wrong       */
specifier|public
name|GlyfSimpleDescript
parameter_list|(
name|short
name|numberOfContours
parameter_list|,
name|TTFDataStream
name|bais
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|numberOfContours
argument_list|,
name|bais
argument_list|)
expr_stmt|;
comment|// Simple glyph description
name|endPtsOfContours
operator|=
operator|new
name|int
index|[
name|numberOfContours
index|]
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfContours
condition|;
name|i
operator|++
control|)
block|{
name|endPtsOfContours
index|[
name|i
index|]
operator|=
name|bais
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
block|}
comment|// The last end point index reveals the total number of points
name|pointCount
operator|=
name|endPtsOfContours
index|[
name|numberOfContours
operator|-
literal|1
index|]
operator|+
literal|1
expr_stmt|;
name|flags
operator|=
operator|new
name|byte
index|[
name|pointCount
index|]
expr_stmt|;
name|xCoordinates
operator|=
operator|new
name|short
index|[
name|pointCount
index|]
expr_stmt|;
name|yCoordinates
operator|=
operator|new
name|short
index|[
name|pointCount
index|]
expr_stmt|;
name|int
name|instructionCount
init|=
name|bais
operator|.
name|readSignedShort
argument_list|()
decl_stmt|;
name|readInstructions
argument_list|(
name|bais
argument_list|,
name|instructionCount
argument_list|)
expr_stmt|;
name|readFlags
argument_list|(
name|pointCount
argument_list|,
name|bais
argument_list|)
expr_stmt|;
name|readCoords
argument_list|(
name|pointCount
argument_list|,
name|bais
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|getEndPtOfContours
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|endPtsOfContours
index|[
name|i
index|]
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|byte
name|getFlags
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|flags
index|[
name|i
index|]
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|short
name|getXCoordinate
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|xCoordinates
index|[
name|i
index|]
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|short
name|getYCoordinate
parameter_list|(
name|int
name|i
parameter_list|)
block|{
return|return
name|yCoordinates
index|[
name|i
index|]
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|boolean
name|isComposite
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|getPointCount
parameter_list|()
block|{
return|return
name|pointCount
return|;
block|}
comment|/**      * The table is stored as relative values, but we'll store them as absolutes.      */
specifier|private
name|void
name|readCoords
parameter_list|(
name|int
name|count
parameter_list|,
name|TTFDataStream
name|bais
parameter_list|)
throws|throws
name|IOException
block|{
name|short
name|x
init|=
literal|0
decl_stmt|;
name|short
name|y
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|(
name|flags
index|[
name|i
index|]
operator|&
name|X_DUAL
operator|)
operator|!=
literal|0
condition|)
block|{
if|if
condition|(
operator|(
name|flags
index|[
name|i
index|]
operator|&
name|X_SHORT_VECTOR
operator|)
operator|!=
literal|0
condition|)
block|{
name|x
operator|+=
operator|(
name|short
operator|)
name|bais
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|(
name|flags
index|[
name|i
index|]
operator|&
name|X_SHORT_VECTOR
operator|)
operator|!=
literal|0
condition|)
block|{
name|x
operator|+=
operator|(
name|short
operator|)
operator|-
operator|(
operator|(
name|short
operator|)
name|bais
operator|.
name|read
argument_list|()
operator|)
expr_stmt|;
block|}
else|else
block|{
name|x
operator|+=
name|bais
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
block|}
block|}
name|xCoordinates
index|[
name|i
index|]
operator|=
name|x
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
operator|(
name|flags
index|[
name|i
index|]
operator|&
name|Y_DUAL
operator|)
operator|!=
literal|0
condition|)
block|{
if|if
condition|(
operator|(
name|flags
index|[
name|i
index|]
operator|&
name|Y_SHORT_VECTOR
operator|)
operator|!=
literal|0
condition|)
block|{
name|y
operator|+=
operator|(
name|short
operator|)
name|bais
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|(
name|flags
index|[
name|i
index|]
operator|&
name|Y_SHORT_VECTOR
operator|)
operator|!=
literal|0
condition|)
block|{
name|y
operator|+=
operator|(
name|short
operator|)
operator|-
operator|(
operator|(
name|short
operator|)
name|bais
operator|.
name|read
argument_list|()
operator|)
expr_stmt|;
block|}
else|else
block|{
name|y
operator|+=
name|bais
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
block|}
block|}
name|yCoordinates
index|[
name|i
index|]
operator|=
name|y
expr_stmt|;
block|}
block|}
comment|/**      * The flags are run-length encoded.      */
specifier|private
name|void
name|readFlags
parameter_list|(
name|int
name|flagCount
parameter_list|,
name|TTFDataStream
name|bais
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|flagCount
condition|;
name|index
operator|++
control|)
block|{
name|flags
index|[
name|index
index|]
operator|=
operator|(
name|byte
operator|)
name|bais
operator|.
name|read
argument_list|()
expr_stmt|;
if|if
condition|(
operator|(
name|flags
index|[
name|index
index|]
operator|&
name|REPEAT
operator|)
operator|!=
literal|0
condition|)
block|{
name|int
name|repeats
init|=
name|bais
operator|.
name|read
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|repeats
condition|;
name|i
operator|++
control|)
block|{
name|flags
index|[
name|index
operator|+
name|i
index|]
operator|=
name|flags
index|[
name|index
index|]
expr_stmt|;
block|}
name|index
operator|+=
name|repeats
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ArrayIndexOutOfBoundsException
name|e
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"error: array index out of bounds"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

