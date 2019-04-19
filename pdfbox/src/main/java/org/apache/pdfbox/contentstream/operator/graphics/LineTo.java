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
name|contentstream
operator|.
name|operator
operator|.
name|graphics
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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|Point2D
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|contentstream
operator|.
name|operator
operator|.
name|MissingOperandException
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
name|COSBase
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
name|COSNumber
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
name|contentstream
operator|.
name|operator
operator|.
name|Operator
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
name|contentstream
operator|.
name|operator
operator|.
name|OperatorName
import|;
end_import

begin_comment
comment|/**  * l Append straight line segment to path.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|LineTo
extends|extends
name|GraphicsOperatorProcessor
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|LineTo
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Operator
name|operator
parameter_list|,
name|List
argument_list|<
name|COSBase
argument_list|>
name|operands
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|operands
operator|.
name|size
argument_list|()
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|MissingOperandException
argument_list|(
name|operator
argument_list|,
name|operands
argument_list|)
throw|;
block|}
name|COSBase
name|base0
init|=
name|operands
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|base0
operator|instanceof
name|COSNumber
operator|)
condition|)
block|{
return|return;
block|}
name|COSBase
name|base1
init|=
name|operands
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|base1
operator|instanceof
name|COSNumber
operator|)
condition|)
block|{
return|return;
block|}
comment|// append straight line segment from the current point to the point
name|COSNumber
name|x
init|=
operator|(
name|COSNumber
operator|)
name|base0
decl_stmt|;
name|COSNumber
name|y
init|=
operator|(
name|COSNumber
operator|)
name|base1
decl_stmt|;
name|Point2D
operator|.
name|Float
name|pos
init|=
name|context
operator|.
name|transformedPoint
argument_list|(
name|x
operator|.
name|floatValue
argument_list|()
argument_list|,
name|y
operator|.
name|floatValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|getCurrentPoint
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"LineTo ("
operator|+
name|pos
operator|.
name|x
operator|+
literal|","
operator|+
name|pos
operator|.
name|y
operator|+
literal|") without initial MoveTo"
argument_list|)
expr_stmt|;
name|context
operator|.
name|moveTo
argument_list|(
name|pos
operator|.
name|x
argument_list|,
name|pos
operator|.
name|y
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|lineTo
argument_list|(
name|pos
operator|.
name|x
argument_list|,
name|pos
operator|.
name|y
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|OperatorName
operator|.
name|LINE_TO
return|;
block|}
block|}
end_class

end_unit

