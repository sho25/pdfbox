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
name|action
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
name|COSDictionary
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
name|pdmodel
operator|.
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
operator|.
name|PDDestination
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
name|interactive
operator|.
name|documentnavigation
operator|.
name|destination
operator|.
name|PDPageDestination
import|;
end_import

begin_comment
comment|/**  * This represents a go-to action that can be executed in a PDF document.  *  * @author Ben Litchfield  * @author Panagiotis Toumasis  */
end_comment

begin_class
specifier|public
class|class
name|PDActionGoTo
extends|extends
name|PDAction
block|{
comment|/**      * This type of action this object represents.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"GoTo"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|PDActionGoTo
parameter_list|()
block|{
name|setSubType
argument_list|(
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param a The action dictionary.      */
specifier|public
name|PDActionGoTo
parameter_list|(
name|COSDictionary
name|a
parameter_list|)
block|{
name|super
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the destination to jump to.      *      * @return The D entry of the specific go-to action dictionary.      *      * @throws IOException If there is an error creating the destination.      */
specifier|public
name|PDDestination
name|getDestination
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDDestination
operator|.
name|create
argument_list|(
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|D
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will set the destination to jump to.      *      * @param d The destination.      *       * @throws IllegalArgumentException if the destination is not a page dictionary object.      */
specifier|public
name|void
name|setDestination
parameter_list|(
name|PDDestination
name|d
parameter_list|)
block|{
if|if
condition|(
name|d
operator|instanceof
name|PDPageDestination
condition|)
block|{
name|PDPageDestination
name|pageDest
init|=
operator|(
name|PDPageDestination
operator|)
name|d
decl_stmt|;
name|COSArray
name|destArray
init|=
name|pageDest
operator|.
name|getCOSObject
argument_list|()
decl_stmt|;
if|if
condition|(
name|destArray
operator|.
name|size
argument_list|()
operator|>=
literal|1
condition|)
block|{
name|COSBase
name|page
init|=
name|destArray
operator|.
name|getObject
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|page
operator|instanceof
name|COSDictionary
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Destination of a GoTo action must be "
operator|+
literal|"a page dictionary object"
argument_list|)
throw|;
block|}
block|}
block|}
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|D
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

