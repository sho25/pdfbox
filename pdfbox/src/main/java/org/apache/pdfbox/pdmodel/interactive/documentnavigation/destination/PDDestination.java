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
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
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
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSArray
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
name|COSName
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
name|COSString
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
name|pdmodel
operator|.
name|common
operator|.
name|PDDestinationOrAction
import|;
end_import

begin_comment
comment|/**  * This represents a destination in a PDF document.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDDestination
implements|implements
name|PDDestinationOrAction
block|{
comment|/**      * This will create a new destination depending on the type of COSBase      * that is passed in.      *      * @param base The base level object.      *      * @return A new destination.      *      * @throws IOException If the base cannot be converted to a Destination.      */
specifier|public
specifier|static
name|PDDestination
name|create
parameter_list|(
name|COSBase
name|base
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDestination
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|base
operator|==
literal|null
condition|)
block|{
comment|//this is ok, just return null.
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSArray
operator|&&
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|size
argument_list|()
operator|>
literal|1
operator|&&
operator|(
operator|(
name|COSArray
operator|)
name|base
operator|)
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|COSName
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|base
decl_stmt|;
name|COSName
name|type
init|=
operator|(
name|COSName
operator|)
name|array
operator|.
name|getObject
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|typeString
init|=
name|type
operator|.
name|getName
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|typeString
condition|)
block|{
case|case
name|PDPageFitDestination
operator|.
name|TYPE
case|:
case|case
name|PDPageFitDestination
operator|.
name|TYPE_BOUNDED
case|:
name|retval
operator|=
operator|new
name|PDPageFitDestination
argument_list|(
name|array
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDPageFitHeightDestination
operator|.
name|TYPE
case|:
case|case
name|PDPageFitHeightDestination
operator|.
name|TYPE_BOUNDED
case|:
name|retval
operator|=
operator|new
name|PDPageFitHeightDestination
argument_list|(
name|array
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDPageFitRectangleDestination
operator|.
name|TYPE
case|:
name|retval
operator|=
operator|new
name|PDPageFitRectangleDestination
argument_list|(
name|array
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDPageFitWidthDestination
operator|.
name|TYPE
case|:
case|case
name|PDPageFitWidthDestination
operator|.
name|TYPE_BOUNDED
case|:
name|retval
operator|=
operator|new
name|PDPageFitWidthDestination
argument_list|(
name|array
argument_list|)
expr_stmt|;
break|break;
case|case
name|PDPageXYZDestination
operator|.
name|TYPE
case|:
name|retval
operator|=
operator|new
name|PDPageXYZDestination
argument_list|(
name|array
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown destination type: "
operator|+
name|type
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSString
condition|)
block|{
name|retval
operator|=
operator|new
name|PDNamedDestination
argument_list|(
operator|(
name|COSString
operator|)
name|base
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSName
condition|)
block|{
name|retval
operator|=
operator|new
name|PDNamedDestination
argument_list|(
operator|(
name|COSName
operator|)
name|base
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: can't convert to Destination "
operator|+
name|base
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
block|}
end_class

end_unit

