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
name|graphics
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
name|PDDocument
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
name|ResourceCache
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
name|common
operator|.
name|PDStream
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
name|graphics
operator|.
name|image
operator|.
name|PDImageXObject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * An external object, or "XObject".  *  * @author Ben Litchfield  * @author John Hewson  */
end_comment

begin_class
specifier|public
class|class
name|PDXObject
implements|implements
name|COSObjectable
block|{
specifier|private
specifier|final
name|PDStream
name|stream
decl_stmt|;
comment|/**      * Creates a new XObject instance of the appropriate type for the COS stream.      *      * @param base The stream which is wrapped by this XObject.      * @return A new XObject instance.      * @throws java.io.IOException if there is an error creating the XObject.      */
specifier|public
specifier|static
name|PDXObject
name|createXObject
parameter_list|(
name|COSBase
name|base
parameter_list|,
name|PDResources
name|resources
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|base
operator|==
literal|null
condition|)
block|{
comment|// TODO throw an exception?
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSStream
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unexpected object type: "
operator|+
name|base
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|base
decl_stmt|;
name|String
name|subtype
init|=
name|stream
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|COSName
operator|.
name|IMAGE
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDImageXObject
argument_list|(
operator|new
name|PDStream
argument_list|(
name|stream
argument_list|)
argument_list|,
name|resources
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|FORM
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
name|ResourceCache
name|cache
init|=
name|resources
operator|!=
literal|null
condition|?
name|resources
operator|.
name|getResourceCache
argument_list|()
else|:
literal|null
decl_stmt|;
name|COSDictionary
name|group
init|=
operator|(
name|COSDictionary
operator|)
name|stream
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|GROUP
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
operator|&&
name|COSName
operator|.
name|TRANSPARENCY
operator|.
name|equals
argument_list|(
name|group
operator|.
name|getCOSName
argument_list|(
name|COSName
operator|.
name|S
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDTransparencyGroup
argument_list|(
name|stream
argument_list|,
name|cache
argument_list|)
return|;
block|}
return|return
operator|new
name|PDFormXObject
argument_list|(
name|stream
argument_list|,
name|cache
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|COSName
operator|.
name|PS
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|subtype
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDPostScriptXObject
argument_list|(
name|stream
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Invalid XObject Subtype: "
operator|+
name|subtype
argument_list|)
throw|;
block|}
block|}
comment|/**      * Creates a new XObject from the given stream and subtype.      * @param stream The stream to read.      */
specifier|protected
name|PDXObject
parameter_list|(
name|COSStream
name|stream
parameter_list|,
name|COSName
name|subtype
parameter_list|)
block|{
name|this
operator|.
name|stream
operator|=
operator|new
name|PDStream
argument_list|(
name|stream
argument_list|)
expr_stmt|;
comment|// could be used for writing:
name|stream
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|XOBJECT
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|subtype
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new XObject from the given stream and subtype.      * @param stream The stream to read.      */
specifier|protected
name|PDXObject
parameter_list|(
name|PDStream
name|stream
parameter_list|,
name|COSName
name|subtype
parameter_list|)
block|{
name|this
operator|.
name|stream
operator|=
name|stream
expr_stmt|;
comment|// could be used for writing:
name|stream
operator|.
name|getStream
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|XOBJECT
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|getStream
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|subtype
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new XObject of the given subtype for writing.      * @param document The document in which to create the XObject.      * @param subtype The subtype of the new XObject.      */
specifier|protected
name|PDXObject
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|COSName
name|subtype
parameter_list|)
block|{
name|stream
operator|=
operator|new
name|PDStream
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|stream
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|XOBJECT
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|stream
operator|.
name|getCOSObject
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|subtype
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the stream.      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
specifier|final
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|stream
operator|.
name|getCOSObject
argument_list|()
return|;
block|}
comment|/**      * Returns the stream.      * @return The stream for this object.      * @deprecated use {@link #getCOSObject() }      */
annotation|@
name|Deprecated
specifier|public
specifier|final
name|COSStream
name|getCOSStream
parameter_list|()
block|{
return|return
name|stream
operator|.
name|getCOSObject
argument_list|()
return|;
block|}
comment|/**      * Returns the stream.      * @return The stream for this object.      * @deprecated Use {@link #getStream()} instead.      */
annotation|@
name|Deprecated
specifier|public
specifier|final
name|PDStream
name|getPDStream
parameter_list|()
block|{
return|return
name|stream
return|;
block|}
comment|/**      * Returns the stream.      * @return The stream for this object.      */
specifier|public
specifier|final
name|PDStream
name|getStream
parameter_list|()
block|{
return|return
name|stream
return|;
block|}
block|}
end_class

end_unit

