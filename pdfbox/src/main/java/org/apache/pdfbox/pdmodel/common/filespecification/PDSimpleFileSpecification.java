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
name|common
operator|.
name|filespecification
package|;
end_package

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
name|COSString
import|;
end_import

begin_comment
comment|/**  * A file specification that is just a string.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDSimpleFileSpecification
extends|extends
name|PDFileSpecification
block|{
specifier|private
name|COSString
name|file
decl_stmt|;
comment|/**      * Constructor.      *      */
specifier|public
name|PDSimpleFileSpecification
parameter_list|()
block|{
name|file
operator|=
operator|new
name|COSString
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param fileName The file that this spec represents.      */
specifier|public
name|PDSimpleFileSpecification
parameter_list|(
name|COSString
name|fileName
parameter_list|)
block|{
name|file
operator|=
name|fileName
expr_stmt|;
block|}
comment|/**      * This will get the file name.      *      * @return The file name.      */
annotation|@
name|Override
specifier|public
name|String
name|getFile
parameter_list|()
block|{
return|return
name|file
operator|.
name|getString
argument_list|()
return|;
block|}
comment|/**      * This will set the file name.      *      * @param fileName The name of the file.      */
annotation|@
name|Override
specifier|public
name|void
name|setFile
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|file
operator|=
operator|new
name|COSString
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|file
return|;
block|}
block|}
end_class

end_unit

