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
name|cos
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

begin_comment
comment|/**  * This class represents a PDF object.  *  * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|COSObject
extends|extends
name|COSBase
implements|implements
name|COSUpdateInfo
block|{
specifier|private
name|COSBase
name|baseObject
decl_stmt|;
specifier|private
name|long
name|objectNumber
decl_stmt|;
specifier|private
name|int
name|generationNumber
decl_stmt|;
specifier|private
name|boolean
name|needToBeUpdated
decl_stmt|;
specifier|private
name|ICOSParser
name|parser
decl_stmt|;
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
name|COSObject
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Constructor.      *      * @param object The object that this encapsulates.      *      */
specifier|public
name|COSObject
parameter_list|(
name|COSBase
name|object
parameter_list|)
block|{
name|this
argument_list|(
name|object
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param object The object that this encapsulates.      * @param parser The parser to be used to load the object on demand      *      */
specifier|public
name|COSObject
parameter_list|(
name|COSBase
name|object
parameter_list|,
name|ICOSParser
name|parser
parameter_list|)
block|{
name|baseObject
operator|=
name|object
expr_stmt|;
name|this
operator|.
name|parser
operator|=
name|parser
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param key The object number of the encapsulated object.      * @param parser The parser to be used to load the object on demand      *      */
specifier|public
name|COSObject
parameter_list|(
name|COSObjectKey
name|key
parameter_list|,
name|ICOSParser
name|parser
parameter_list|)
block|{
name|this
operator|.
name|parser
operator|=
name|parser
expr_stmt|;
name|objectNumber
operator|=
name|key
operator|.
name|getNumber
argument_list|()
expr_stmt|;
name|generationNumber
operator|=
name|key
operator|.
name|getGeneration
argument_list|()
expr_stmt|;
block|}
comment|/**      * Indicates if the referenced object is present or not.      *       * @return true if the indirect object is dereferenced      */
specifier|public
name|boolean
name|isObjectNull
parameter_list|()
block|{
return|return
name|baseObject
operator|==
literal|null
return|;
block|}
comment|/**      * This will get the object that this object encapsulates.      *      * @return The encapsulated object.      */
specifier|public
name|COSBase
name|getObject
parameter_list|()
block|{
if|if
condition|(
operator|(
name|baseObject
operator|==
literal|null
operator|||
name|baseObject
operator|instanceof
name|COSNull
operator|)
operator|&&
name|parser
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|baseObject
operator|=
name|parser
operator|.
name|dereferenceCOSObject
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|baseObject
operator|!=
literal|null
condition|)
block|{
comment|// remove parser to avoid endless recursions
name|parser
operator|=
literal|null
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// remove parser to avoid endless recursions
name|parser
operator|=
literal|null
expr_stmt|;
name|LOG
operator|.
name|error
argument_list|(
literal|"Can't dereference "
operator|+
name|this
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|baseObject
return|;
block|}
specifier|public
specifier|final
name|void
name|setToNull
parameter_list|()
block|{
name|baseObject
operator|=
name|COSNull
operator|.
name|NULL
expr_stmt|;
name|parser
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"COSObject{"
operator|+
name|Long
operator|.
name|toString
argument_list|(
name|objectNumber
argument_list|)
operator|+
literal|", "
operator|+
name|Integer
operator|.
name|toString
argument_list|(
name|generationNumber
argument_list|)
operator|+
literal|"}"
return|;
block|}
comment|/**       * Getter for property objectNumber.      * @return Value of property objectNumber.      */
specifier|public
name|long
name|getObjectNumber
parameter_list|()
block|{
return|return
name|objectNumber
return|;
block|}
comment|/**       * Getter for property generationNumber.      * @return Value of property generationNumber.      */
specifier|public
name|int
name|getGenerationNumber
parameter_list|()
block|{
return|return
name|generationNumber
return|;
block|}
comment|/**      * visitor pattern double dispatch method.      *      * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws IOException If an error occurs while visiting this object.      */
annotation|@
name|Override
specifier|public
name|Object
name|accept
parameter_list|(
name|ICOSVisitor
name|visitor
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getObject
argument_list|()
operator|!=
literal|null
condition|?
name|getObject
argument_list|()
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
else|:
name|COSNull
operator|.
name|NULL
operator|.
name|accept
argument_list|(
name|visitor
argument_list|)
return|;
block|}
comment|/**      * Get the update state for the COSWriter.      *       * @return the update state.      */
annotation|@
name|Override
specifier|public
name|boolean
name|isNeedToBeUpdated
parameter_list|()
block|{
return|return
name|needToBeUpdated
return|;
block|}
comment|/**      * Set the update state of the dictionary for the COSWriter.      *       * @param flag the update state.      */
annotation|@
name|Override
specifier|public
name|void
name|setNeedToBeUpdated
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|needToBeUpdated
operator|=
name|flag
expr_stmt|;
block|}
block|}
end_class

end_unit

