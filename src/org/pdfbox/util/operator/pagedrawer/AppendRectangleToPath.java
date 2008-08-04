begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2005, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|operator
operator|.
name|pagedrawer
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
name|Rectangle2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSNumber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdfviewer
operator|.
name|PageDrawer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|PDFOperator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|operator
operator|.
name|OperatorProcessor
import|;
end_import

begin_comment
comment|/**  * Implementation of content stream operator for page drawer.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|AppendRectangleToPath
extends|extends
name|OperatorProcessor
block|{
comment|/**      * process : re : append rectangle to path.      * @param operator The operator that is being executed.      * @param arguments List      */
specifier|public
name|void
name|process
parameter_list|(
name|PDFOperator
name|operator
parameter_list|,
name|List
name|arguments
parameter_list|)
block|{
name|PageDrawer
name|drawer
init|=
operator|(
name|PageDrawer
operator|)
name|context
decl_stmt|;
name|COSNumber
name|x
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|COSNumber
name|y
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|COSNumber
name|w
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|COSNumber
name|h
init|=
operator|(
name|COSNumber
operator|)
name|arguments
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|double
name|finalY
init|=
name|drawer
operator|.
name|fixY
argument_list|(
name|x
operator|.
name|doubleValue
argument_list|()
argument_list|,
name|y
operator|.
name|doubleValue
argument_list|()
argument_list|)
operator|-
name|h
operator|.
name|doubleValue
argument_list|()
decl_stmt|;
name|double
name|finalH
init|=
name|h
operator|.
name|doubleValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|finalH
operator|<
literal|0
condition|)
block|{
name|finalY
operator|+=
name|finalH
expr_stmt|;
name|finalH
operator|=
name|Math
operator|.
name|abs
argument_list|(
name|finalH
argument_list|)
expr_stmt|;
block|}
name|Rectangle2D
name|rect
init|=
operator|new
name|Rectangle2D
operator|.
name|Double
argument_list|(
name|x
operator|.
name|doubleValue
argument_list|()
argument_list|,
name|finalY
argument_list|,
name|w
operator|.
name|doubleValue
argument_list|()
operator|+
literal|1
argument_list|,
name|finalH
operator|+
literal|1
argument_list|)
decl_stmt|;
name|drawer
operator|.
name|getLinePath
argument_list|()
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|//System.out.println( "Bounds before=" + drawer.getLinePath().getBounds() );
name|drawer
operator|.
name|getLinePath
argument_list|()
operator|.
name|append
argument_list|(
name|rect
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|//graphics.drawRect((int)x.doubleValue(), (int)(pageSize.getHeight() - y.doubleValue()),
comment|//                  (int)w.doubleValue(),(int)h.doubleValue() );
comment|//System.out.println( "<re x=\"" + x.getValue() + "\" y=\"" + y.getValue() + "\" width=\"" +
comment|//                                 width.getValue() + "\" height=\"" + height.getValue() + "\">" );
block|}
block|}
end_class

end_unit

