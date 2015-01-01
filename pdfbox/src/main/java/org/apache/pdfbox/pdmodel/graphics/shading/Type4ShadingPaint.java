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
name|pdmodel
operator|.
name|graphics
operator|.
name|shading
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Paint
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|PaintContext
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Rectangle
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|RenderingHints
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
name|AffineTransform
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
name|Rectangle2D
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|ColorModel
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * AWT PaintContext for Gouraud Triangle Mesh (Type 4) shading.  */
end_comment

begin_class
class|class
name|Type4ShadingPaint
implements|implements
name|Paint
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
name|Type4ShadingPaint
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|PDShadingType4
name|shading
decl_stmt|;
specifier|private
name|Matrix
name|matrix
decl_stmt|;
comment|/**      * Constructor.      *      * @param shading the shading resources      * @param matrix the pattern matrix concatenated with that of the parent content stream      */
name|Type4ShadingPaint
parameter_list|(
name|PDShadingType4
name|shading
parameter_list|,
name|Matrix
name|matrix
parameter_list|)
block|{
name|this
operator|.
name|shading
operator|=
name|shading
expr_stmt|;
name|this
operator|.
name|matrix
operator|=
name|matrix
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getTransparency
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|PaintContext
name|createContext
parameter_list|(
name|ColorModel
name|cm
parameter_list|,
name|Rectangle
name|deviceBounds
parameter_list|,
name|Rectangle2D
name|userBounds
parameter_list|,
name|AffineTransform
name|xform
parameter_list|,
name|RenderingHints
name|hints
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|Type4ShadingContext
argument_list|(
name|shading
argument_list|,
name|cm
argument_list|,
name|xform
argument_list|,
name|matrix
argument_list|,
name|deviceBounds
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"An error occurred while painting"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
operator|new
name|Color
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
operator|.
name|createContext
argument_list|(
name|cm
argument_list|,
name|deviceBounds
argument_list|,
name|userBounds
argument_list|,
name|xform
argument_list|,
name|hints
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

