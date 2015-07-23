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
name|tools
operator|.
name|pdfdebugger
operator|.
name|streampane
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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|Style
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|StyleConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|StyleContext
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  */
end_comment

begin_class
specifier|final
class|class
name|OperatorMarker
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Style
argument_list|>
name|operatorStyleMap
decl_stmt|;
name|OperatorMarker
parameter_list|()
block|{
name|operatorStyleMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Style
argument_list|>
argument_list|()
expr_stmt|;
name|initOperatorStyles
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initOperatorStyles
parameter_list|()
block|{
name|StyleContext
name|styleContext
init|=
name|StyleContext
operator|.
name|getDefaultStyleContext
argument_list|()
decl_stmt|;
name|Style
name|common
init|=
name|styleContext
operator|.
name|addStyle
argument_list|(
literal|"common"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|StyleConstants
operator|.
name|setBold
argument_list|(
name|common
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Style
name|textObjectStyle
init|=
name|styleContext
operator|.
name|addStyle
argument_list|(
literal|"text_object"
argument_list|,
name|common
argument_list|)
decl_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|textObjectStyle
argument_list|,
operator|new
name|Color
argument_list|(
literal|0
argument_list|,
literal|100
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Style
name|graphicsStyle
init|=
name|styleContext
operator|.
name|addStyle
argument_list|(
literal|"graphics"
argument_list|,
name|common
argument_list|)
decl_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|graphicsStyle
argument_list|,
operator|new
name|Color
argument_list|(
literal|255
argument_list|,
literal|68
argument_list|,
literal|68
argument_list|)
argument_list|)
expr_stmt|;
name|Style
name|concatStyle
init|=
name|styleContext
operator|.
name|addStyle
argument_list|(
literal|"cm"
argument_list|,
name|common
argument_list|)
decl_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|concatStyle
argument_list|,
operator|new
name|Color
argument_list|(
literal|1
argument_list|,
literal|169
argument_list|,
literal|219
argument_list|)
argument_list|)
expr_stmt|;
name|Style
name|inlineImage
init|=
name|styleContext
operator|.
name|addStyle
argument_list|(
literal|"inline_image"
argument_list|,
name|common
argument_list|)
decl_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|inlineImage
argument_list|,
operator|new
name|Color
argument_list|(
literal|71
argument_list|,
literal|117
argument_list|,
literal|163
argument_list|)
argument_list|)
expr_stmt|;
name|Style
name|imageData
init|=
name|styleContext
operator|.
name|addStyle
argument_list|(
literal|"ID"
argument_list|,
name|common
argument_list|)
decl_stmt|;
name|StyleConstants
operator|.
name|setForeground
argument_list|(
name|imageData
argument_list|,
operator|new
name|Color
argument_list|(
literal|255
argument_list|,
literal|165
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|String
name|BEGIN_TEXT_OBJECT
init|=
literal|"BT"
decl_stmt|;
specifier|final
name|String
name|END_TEXT_OBJECT
init|=
literal|"ET"
decl_stmt|;
specifier|final
name|String
name|SAVE_GRAPHICS_STATE
init|=
literal|"q"
decl_stmt|;
specifier|final
name|String
name|RESTORE_GRAPHICS_STATE
init|=
literal|"Q"
decl_stmt|;
specifier|final
name|String
name|CONCAT
init|=
literal|"cm"
decl_stmt|;
specifier|final
name|String
name|INLINE_IMAGE_BEGIN
init|=
literal|"BI"
decl_stmt|;
specifier|final
name|String
name|IMAGE_DATA
init|=
literal|"ID"
decl_stmt|;
specifier|final
name|String
name|INLINE_IMAGE_END
init|=
literal|"EI"
decl_stmt|;
name|operatorStyleMap
operator|.
name|put
argument_list|(
name|BEGIN_TEXT_OBJECT
argument_list|,
name|textObjectStyle
argument_list|)
expr_stmt|;
name|operatorStyleMap
operator|.
name|put
argument_list|(
name|END_TEXT_OBJECT
argument_list|,
name|textObjectStyle
argument_list|)
expr_stmt|;
name|operatorStyleMap
operator|.
name|put
argument_list|(
name|SAVE_GRAPHICS_STATE
argument_list|,
name|graphicsStyle
argument_list|)
expr_stmt|;
name|operatorStyleMap
operator|.
name|put
argument_list|(
name|RESTORE_GRAPHICS_STATE
argument_list|,
name|graphicsStyle
argument_list|)
expr_stmt|;
name|operatorStyleMap
operator|.
name|put
argument_list|(
name|CONCAT
argument_list|,
name|concatStyle
argument_list|)
expr_stmt|;
name|operatorStyleMap
operator|.
name|put
argument_list|(
name|INLINE_IMAGE_BEGIN
argument_list|,
name|inlineImage
argument_list|)
expr_stmt|;
name|operatorStyleMap
operator|.
name|put
argument_list|(
name|IMAGE_DATA
argument_list|,
name|imageData
argument_list|)
expr_stmt|;
name|operatorStyleMap
operator|.
name|put
argument_list|(
name|INLINE_IMAGE_END
argument_list|,
name|inlineImage
argument_list|)
expr_stmt|;
block|}
name|Style
name|getStyle
parameter_list|(
name|String
name|operator
parameter_list|)
block|{
if|if
condition|(
name|operatorStyleMap
operator|.
name|containsKey
argument_list|(
name|operator
argument_list|)
condition|)
block|{
return|return
name|operatorStyleMap
operator|.
name|get
argument_list|(
name|operator
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

