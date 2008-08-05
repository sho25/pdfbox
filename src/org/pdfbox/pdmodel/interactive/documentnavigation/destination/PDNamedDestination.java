begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
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
name|pdfbox
operator|.
name|cos
operator|.
name|COSString
import|;
end_import

begin_comment
comment|/**  * This represents a destination to a page by referencing it with a name.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|PDNamedDestination
extends|extends
name|PDDestination
block|{
specifier|private
name|COSBase
name|namedDestination
decl_stmt|;
comment|/**      * Constructor.      *      * @param dest The named destination.      */
specifier|public
name|PDNamedDestination
parameter_list|(
name|COSString
name|dest
parameter_list|)
block|{
name|namedDestination
operator|=
name|dest
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param dest The named destination.      */
specifier|public
name|PDNamedDestination
parameter_list|(
name|COSName
name|dest
parameter_list|)
block|{
name|namedDestination
operator|=
name|dest
expr_stmt|;
block|}
comment|/**      * Default constructor.      */
specifier|public
name|PDNamedDestination
parameter_list|()
block|{
comment|//default, so do nothing
block|}
comment|/**      * Default constructor.      *      * @param dest The named destination.      */
specifier|public
name|PDNamedDestination
parameter_list|(
name|String
name|dest
parameter_list|)
block|{
name|namedDestination
operator|=
operator|new
name|COSString
argument_list|(
name|dest
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|namedDestination
return|;
block|}
comment|/**      * This will get the name of the destination.      *      * @return The name of the destination.      */
specifier|public
name|String
name|getNamedDestination
parameter_list|()
block|{
name|String
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|namedDestination
operator|instanceof
name|COSString
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSString
operator|)
name|namedDestination
operator|)
operator|.
name|getString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|namedDestination
operator|instanceof
name|COSName
condition|)
block|{
name|retval
operator|=
operator|(
operator|(
name|COSName
operator|)
name|namedDestination
operator|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the named destination.      *      * @param dest The new named destination.      *      * @throws IOException If there is an error setting the named destination.      */
specifier|public
name|void
name|setNamedDestination
parameter_list|(
name|String
name|dest
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|namedDestination
operator|instanceof
name|COSString
condition|)
block|{
name|COSString
name|string
init|=
operator|(
operator|(
name|COSString
operator|)
name|namedDestination
operator|)
decl_stmt|;
name|string
operator|.
name|reset
argument_list|()
expr_stmt|;
name|string
operator|.
name|append
argument_list|(
name|dest
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dest
operator|==
literal|null
condition|)
block|{
name|namedDestination
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|namedDestination
operator|=
operator|new
name|COSString
argument_list|(
name|dest
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

