begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2015 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|debugger
operator|.
name|ui
package|;
end_package

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
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

begin_comment
comment|/**  *  * @author Tilman Hausherr  *  * Utility class for images.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ImageUtil
block|{
specifier|private
name|ImageUtil
parameter_list|()
block|{     }
comment|/**      * Return an image rotated by a multiple of 90°.      *      * @param image The image to rotate.      * @param rotation The rotation in degrees.      * @return The rotated image.      */
specifier|public
specifier|static
name|BufferedImage
name|getRotatedImage
parameter_list|(
name|BufferedImage
name|image
parameter_list|,
name|int
name|rotation
parameter_list|)
block|{
name|int
name|width
init|=
name|image
operator|.
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|image
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|double
name|x
init|=
literal|0
decl_stmt|;
name|double
name|y
init|=
literal|0
decl_stmt|;
name|BufferedImage
name|rotatedImage
decl_stmt|;
switch|switch
condition|(
name|rotation
operator|%
literal|360
condition|)
block|{
case|case
literal|90
case|:
name|x
operator|=
name|height
expr_stmt|;
name|rotatedImage
operator|=
operator|new
name|BufferedImage
argument_list|(
name|height
argument_list|,
name|width
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_RGB
argument_list|)
expr_stmt|;
break|break;
case|case
literal|270
case|:
name|y
operator|=
name|width
expr_stmt|;
name|rotatedImage
operator|=
operator|new
name|BufferedImage
argument_list|(
name|height
argument_list|,
name|width
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_RGB
argument_list|)
expr_stmt|;
break|break;
case|case
literal|180
case|:
name|x
operator|=
name|width
expr_stmt|;
name|y
operator|=
name|height
expr_stmt|;
name|rotatedImage
operator|=
operator|new
name|BufferedImage
argument_list|(
name|width
argument_list|,
name|height
argument_list|,
name|BufferedImage
operator|.
name|TYPE_INT_RGB
argument_list|)
expr_stmt|;
break|break;
default|default:
return|return
name|image
return|;
block|}
name|Graphics2D
name|g
init|=
operator|(
name|Graphics2D
operator|)
name|rotatedImage
operator|.
name|getGraphics
argument_list|()
decl_stmt|;
name|g
operator|.
name|translate
argument_list|(
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|g
operator|.
name|rotate
argument_list|(
name|Math
operator|.
name|toRadians
argument_list|(
name|rotation
argument_list|)
argument_list|)
expr_stmt|;
name|g
operator|.
name|drawImage
argument_list|(
name|image
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|g
operator|.
name|dispose
argument_list|()
expr_stmt|;
return|return
name|rotatedImage
return|;
block|}
block|}
end_class

end_unit

