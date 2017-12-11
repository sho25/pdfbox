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

begin_comment
comment|/**  * Specifies access to glyph description classes, simple and composite.  *   * This class is based on code from Apache Batik a subproject of Apache XMLGraphics.  * see http://xmlgraphics.apache.org/batik/ for further details.  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|GlyphDescription
block|{
comment|/**       * Returns the index of the ending point of the given contour.      *       * @param i the number of the contour      * @return the index of the ending point of the given contour      */
name|int
name|getEndPtOfContours
parameter_list|(
name|int
name|i
parameter_list|)
function_decl|;
comment|/**      * Returns the flags of the given point. To decode these bit flags, use the static elements of      * {@link GlyfDescript}. See also "Outline flags" in      *<a href="https://developer.apple.com/fonts/TrueType-Reference-Manual/RM06/Chap6glyf.html">The      * 'glyf' table</a> in the TrueType Reference Manual.      *      * @param i the given point      * @return the flags value for the given point      */
name|byte
name|getFlags
parameter_list|(
name|int
name|i
parameter_list|)
function_decl|;
comment|/**      * Returns the x coordinate of the given point.      * @param i the given point      * @return the x coordinate value for the given point      */
name|short
name|getXCoordinate
parameter_list|(
name|int
name|i
parameter_list|)
function_decl|;
comment|/**      * Returns the y coordinate of the given point.      * @param i the given point      * @return the y coordinate value for the given point      */
name|short
name|getYCoordinate
parameter_list|(
name|int
name|i
parameter_list|)
function_decl|;
comment|/**      * Returns whether this point is a composite or not.      * @return true if this point is a composite      */
name|boolean
name|isComposite
parameter_list|()
function_decl|;
comment|/**      * Returns the number of points.      * @return the number of points      */
name|int
name|getPointCount
parameter_list|()
function_decl|;
comment|/**      * Returns the number of contours.      * @return the number of contours      */
name|int
name|getContourCount
parameter_list|()
function_decl|;
comment|/**      * Resolve all parts of an composite glyph.      */
name|void
name|resolve
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

