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
name|form
package|;
end_package

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
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This class represents an XML Forms Architecture Data packet.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PDXFA
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSBase
name|xfa
decl_stmt|;
comment|/**      * Constructor.      *      * @param xfaBase The xfa resource.      */
specifier|public
name|PDXFA
parameter_list|(
name|COSBase
name|xfaBase
parameter_list|)
block|{
name|xfa
operator|=
name|xfaBase
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|xfa
return|;
block|}
block|}
end_class

end_unit

