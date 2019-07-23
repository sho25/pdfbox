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
name|documentinterchange
operator|.
name|logicalstructure
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
name|cos
operator|.
name|COSStream
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotation
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
name|annotation
operator|.
name|PDAnnotationUnknown
import|;
end_import

begin_comment
comment|/**  * An object reference.  *<p>  * This is described as "Entries in an object reference dictionary" in the PDF specification.  *   * @author Johannes Koch  */
end_comment

begin_class
specifier|public
class|class
name|PDObjectReference
implements|implements
name|COSObjectable
block|{
comment|/**      * Log instance.      */
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
name|PDObjectReference
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * TYPE of this object.      */
specifier|public
specifier|static
specifier|final
name|String
name|TYPE
init|=
literal|"OBJR"
decl_stmt|;
specifier|private
specifier|final
name|COSDictionary
name|dictionary
decl_stmt|;
comment|/**      * Default Constructor.      *      */
specifier|public
name|PDObjectReference
parameter_list|()
block|{
name|this
operator|.
name|dictionary
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|this
operator|.
name|dictionary
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for an existing object reference.      *      * @param theDictionary The existing dictionary.      */
specifier|public
name|PDObjectReference
parameter_list|(
name|COSDictionary
name|theDictionary
parameter_list|)
block|{
name|dictionary
operator|=
name|theDictionary
expr_stmt|;
block|}
comment|/**      * Returns the underlying dictionary.      *       * @return the dictionary      */
annotation|@
name|Override
specifier|public
name|COSDictionary
name|getCOSObject
parameter_list|()
block|{
return|return
name|this
operator|.
name|dictionary
return|;
block|}
comment|/**      * Gets a higher-level object for the referenced object.      * Currently this method may return a {@link PDAnnotation},      * a {@link PDXObject} or<code>null</code>.      *       * @return a higher-level object for the referenced object      */
specifier|public
name|COSObjectable
name|getReferencedObject
parameter_list|()
block|{
name|COSBase
name|obj
init|=
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|OBJ
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|COSDictionary
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
if|if
condition|(
name|obj
operator|instanceof
name|COSStream
condition|)
block|{
name|PDXObject
name|xobject
init|=
name|PDXObject
operator|.
name|createXObject
argument_list|(
name|obj
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|//<-- TODO: valid?
if|if
condition|(
name|xobject
operator|!=
literal|null
condition|)
block|{
return|return
name|xobject
return|;
block|}
block|}
name|COSDictionary
name|objDictionary
init|=
operator|(
name|COSDictionary
operator|)
name|obj
decl_stmt|;
name|PDAnnotation
name|annotation
init|=
name|PDAnnotation
operator|.
name|createAnnotation
argument_list|(
name|obj
argument_list|)
decl_stmt|;
comment|/*              * COSName.TYPE is optional, so if annotation is of type unknown and              * COSName.TYPE is not COSName.ANNOT it still may be an annotation.              * TODO shall we return the annotation object instead of null?              * what else can be the target of the object reference?              */
if|if
condition|(
operator|!
operator|(
name|annotation
operator|instanceof
name|PDAnnotationUnknown
operator|)
operator|||
name|COSName
operator|.
name|ANNOT
operator|.
name|equals
argument_list|(
name|objDictionary
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|annotation
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Couldn't get the referenced object - returning null instead"
argument_list|,
name|exception
argument_list|)
expr_stmt|;
comment|// this can only happen if the target is an XObject.
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Sets the referenced annotation.      *       * @param annotation the referenced annotation      */
specifier|public
name|void
name|setReferencedObject
parameter_list|(
name|PDAnnotation
name|annotation
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OBJ
argument_list|,
name|annotation
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the referenced XObject.      *       * @param xobject the referenced XObject      */
specifier|public
name|void
name|setReferencedObject
parameter_list|(
name|PDXObject
name|xobject
parameter_list|)
block|{
name|this
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|OBJ
argument_list|,
name|xobject
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

