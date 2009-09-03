begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
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
name|util
operator|.
name|List
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
name|pdfviewer
operator|.
name|PageDrawer
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
name|util
operator|.
name|Matrix
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
name|util
operator|.
name|PDFOperator
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
name|util
operator|.
name|operator
operator|.
name|OperatorProcessor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|BasicStroke
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Graphics2D
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
comment|/**  * Implementation of content stream operator for page drawer.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|StrokePath
extends|extends
name|OperatorProcessor
block|{
comment|/**      * S stroke the path.      * @param operator The operator that is being executed.      * @param arguments List      *      * @throws IOException If an error occurs while processing the font.      */
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
throws|throws
name|IOException
block|{
comment|///dwilson 3/19/07 refactor
try|try
block|{
name|PageDrawer
name|drawer
init|=
operator|(
name|PageDrawer
operator|)
name|context
decl_stmt|;
name|float
name|lineWidth
init|=
operator|(
name|float
operator|)
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getLineWidth
argument_list|()
decl_stmt|;
name|Matrix
name|ctm
init|=
name|context
operator|.
name|getGraphicsState
argument_list|()
operator|.
name|getCurrentTransformationMatrix
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctm
operator|!=
literal|null
operator|&&
name|ctm
operator|.
name|getXScale
argument_list|()
operator|>
literal|0
condition|)
block|{
name|lineWidth
operator|=
name|lineWidth
operator|*
name|ctm
operator|.
name|getXScale
argument_list|()
expr_stmt|;
block|}
name|Graphics2D
name|graphics
init|=
operator|(
operator|(
name|PageDrawer
operator|)
name|context
operator|)
operator|.
name|getGraphics
argument_list|()
decl_stmt|;
name|BasicStroke
name|stroke
init|=
operator|(
name|BasicStroke
operator|)
name|graphics
operator|.
name|getStroke
argument_list|()
decl_stmt|;
if|if
condition|(
name|stroke
operator|==
literal|null
condition|)
block|{
name|graphics
operator|.
name|setStroke
argument_list|(
operator|new
name|BasicStroke
argument_list|(
name|lineWidth
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|graphics
operator|.
name|setStroke
argument_list|(
operator|new
name|BasicStroke
argument_list|(
name|lineWidth
argument_list|,
name|stroke
operator|.
name|getEndCap
argument_list|()
argument_list|,
name|stroke
operator|.
name|getLineJoin
argument_list|()
argument_list|,
name|stroke
operator|.
name|getMiterLimit
argument_list|()
argument_list|,
name|stroke
operator|.
name|getDashArray
argument_list|()
argument_list|,
name|stroke
operator|.
name|getDashPhase
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|drawer
operator|.
name|strokePath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|logger
argument_list|()
operator|.
name|warn
argument_list|(
name|exception
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

