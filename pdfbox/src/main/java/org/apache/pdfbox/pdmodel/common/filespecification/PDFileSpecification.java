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
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This represents a file specification.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDFileSpecification
implements|implements
name|COSObjectable
block|{
comment|/**      * A file specification can either be a COSString or a COSDictionary.  This      * will create the file specification either way.      *      * @param base The cos object that describes the fs.      *      * @return The file specification for the COSBase object.      *      * @throws IOException If there is an error creating the file spec.      */
specifier|public
specifier|static
name|PDFileSpecification
name|createFS
parameter_list|(
name|COSBase
name|base
parameter_list|)
throws|throws
name|IOException
block|{
name|PDFileSpecification
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
comment|//then simply return null
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
name|PDSimpleFileSpecification
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
name|COSDictionary
condition|)
block|{
name|retval
operator|=
operator|new
name|PDComplexFileSpecification
argument_list|(
operator|(
name|COSDictionary
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
literal|"Error: Unknown file specification "
operator|+
name|base
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will get the file name.      *      * @return The file name.      */
specifier|public
specifier|abstract
name|String
name|getFile
parameter_list|()
function_decl|;
comment|/**      * This will set the file name.      *      * @param file The name of the file.      */
specifier|public
specifier|abstract
name|void
name|setFile
parameter_list|(
name|String
name|file
parameter_list|)
function_decl|;
block|}
end_class

end_unit

