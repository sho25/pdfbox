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
name|pdmodel
operator|.
name|graphics
operator|.
name|PDXObject
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
name|graphics
operator|.
name|form
operator|.
name|PDFormXObject
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
name|graphics
operator|.
name|form
operator|.
name|PDTransparencyGroup
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
name|PDResources
import|;
end_import

begin_comment
comment|/**  * Do: Draws an XObject.  *  * @author Ben Litchfield  * @author Mario Ivankovits  */
end_comment

begin_class
specifier|public
class|class
name|DrawObject
extends|extends
name|OperatorProcessor
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
name|DrawObject
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
name|arguments
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|arguments
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MissingOperandException
argument_list|(
name|operator
argument_list|,
name|arguments
argument_list|)
throw|;
block|}
name|COSBase
name|base0
init|=
name|arguments
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
name|COSName
operator|)
condition|)
block|{
return|return;
block|}
name|COSName
name|name
init|=
operator|(
name|COSName
operator|)
name|base0
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|getResources
argument_list|()
operator|.
name|isImageXObject
argument_list|(
name|name
argument_list|)
condition|)
block|{
comment|// we're done here, don't decode images when doing text extraction
return|return;
block|}
name|PDXObject
name|xobject
init|=
name|context
operator|.
name|getResources
argument_list|()
operator|.
name|getXObject
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|xobject
operator|instanceof
name|PDFormXObject
condition|)
block|{
name|PDFormXObject
name|form
init|=
operator|(
name|PDFormXObject
operator|)
name|xobject
decl_stmt|;
name|PDResources
name|formResources
init|=
name|form
operator|.
name|getResources
argument_list|()
decl_stmt|;
if|if
condition|(
name|formResources
operator|!=
literal|null
operator|&&
name|context
operator|.
name|getResources
argument_list|()
operator|.
name|getCOSObject
argument_list|()
operator|==
name|formResources
operator|.
name|getCOSObject
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"avoiding recursion with XObject '"
operator|+
name|name
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|form
operator|instanceof
name|PDTransparencyGroup
condition|)
block|{
name|context
operator|.
name|showTransparencyGroup
argument_list|(
operator|(
name|PDTransparencyGroup
operator|)
name|form
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|showForm
argument_list|(
name|form
argument_list|)
expr_stmt|;
block|}
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
name|DRAW_OBJECT
return|;
block|}
block|}
end_class

end_unit

