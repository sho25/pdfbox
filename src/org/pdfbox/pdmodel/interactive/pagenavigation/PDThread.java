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
name|pagenavigation
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
name|cos
operator|.
name|COSDictionary
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
name|PDDocumentInformation
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
comment|/**  * This a single thread in a PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.2 $  */
end_comment

begin_class
specifier|public
class|class
name|PDThread
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|thread
decl_stmt|;
comment|/**      * Constructor that is used for a preexisting dictionary.      *      * @param t The underlying dictionary.      */
specifier|public
name|PDThread
parameter_list|(
name|COSDictionary
name|t
parameter_list|)
block|{
name|thread
operator|=
name|t
expr_stmt|;
block|}
comment|/**      * Default constructor.      *      */
specifier|public
name|PDThread
parameter_list|()
block|{
name|thread
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|thread
operator|.
name|setName
argument_list|(
literal|"Type"
argument_list|,
literal|"Thread"
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the underlying dictionary that this object wraps.      *      * @return The underlying info dictionary.      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|thread
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|thread
return|;
block|}
comment|/**      * Get info about the thread, or null if there is nothing.      *      * @return The thread information.      */
specifier|public
name|PDDocumentInformation
name|getThreadInfo
parameter_list|()
block|{
name|PDDocumentInformation
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|info
init|=
operator|(
name|COSDictionary
operator|)
name|thread
operator|.
name|getDictionaryObject
argument_list|(
literal|"I"
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDDocumentInformation
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the thread info, can be null.      *      * @param info The info dictionary about this thread.      */
specifier|public
name|void
name|setThreadInfo
parameter_list|(
name|PDDocumentInformation
name|info
parameter_list|)
block|{
name|thread
operator|.
name|setItem
argument_list|(
literal|"I"
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the first bead in the thread, or null if it has not been set yet.  This      * is a required field for this object.      *      * @return The first bead in the thread.      */
specifier|public
name|PDThreadBead
name|getFirstBead
parameter_list|()
block|{
name|PDThreadBead
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|bead
init|=
operator|(
name|COSDictionary
operator|)
name|thread
operator|.
name|getDictionaryObject
argument_list|(
literal|"F"
argument_list|)
decl_stmt|;
if|if
condition|(
name|bead
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDThreadBead
argument_list|(
name|bead
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the first bead in the thread.  When this is set it will      * also set the thread property of the bead object.      *      * @param bead The first bead in the thread.      */
specifier|public
name|void
name|setFirstBead
parameter_list|(
name|PDThreadBead
name|bead
parameter_list|)
block|{
if|if
condition|(
name|bead
operator|!=
literal|null
condition|)
block|{
name|bead
operator|.
name|setThread
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|thread
operator|.
name|setItem
argument_list|(
literal|"F"
argument_list|,
name|bead
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

