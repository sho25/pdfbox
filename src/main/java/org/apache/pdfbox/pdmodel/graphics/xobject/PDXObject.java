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
operator|.
name|xobject
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
name|PDMetadata
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
name|exceptions
operator|.
name|LoggingObject
import|;
end_import

begin_comment
comment|/**  * The base class for all XObjects in the PDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author mathiak  * @author Marcel Kammer  * @version $Revision: 1.14 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDXObject
extends|extends
name|LoggingObject
implements|implements
name|COSObjectable
block|{
specifier|private
name|PDStream
name|xobject
decl_stmt|;
comment|/**      * Standard constuctor.      *      * @param xobj The XObject dictionary.      */
specifier|public
name|PDXObject
parameter_list|(
name|COSStream
name|xobj
parameter_list|)
block|{
name|xobject
operator|=
operator|new
name|PDStream
argument_list|(
name|xobj
argument_list|)
expr_stmt|;
name|xobject
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
literal|"XObject"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Standard constuctor.      *      * @param xobj The XObject dictionary.      */
specifier|public
name|PDXObject
parameter_list|(
name|PDStream
name|xobj
parameter_list|)
block|{
name|xobject
operator|=
name|xobj
expr_stmt|;
name|xobject
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
literal|"XObject"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Standard constuctor.      *      * @param doc The doc to store the object contents.      */
specifier|public
name|PDXObject
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
block|{
name|xobject
operator|=
operator|new
name|PDStream
argument_list|(
name|doc
argument_list|)
expr_stmt|;
name|xobject
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
literal|"XObject"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the stream.      *      * {@inheritDoc}      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|xobject
operator|.
name|getCOSObject
argument_list|()
return|;
block|}
comment|/**      * Returns the stream.      * @return The stream for this object.      */
specifier|public
name|COSStream
name|getCOSStream
parameter_list|()
block|{
return|return
name|xobject
operator|.
name|getStream
argument_list|()
return|;
block|}
comment|/**      * Returns the stream.      * @return The stream for this object.      */
specifier|public
name|PDStream
name|getPDStream
parameter_list|()
block|{
return|return
name|xobject
return|;
block|}
comment|/**      * Create the correct xobject from the cos base.      *      * @param xobject The cos level xobject to create.      *      * @return a pdmodel xobject      * @throws IOException If there is an error creating the xobject.      */
specifier|public
specifier|static
name|PDXObject
name|createXObject
parameter_list|(
name|COSBase
name|xobject
parameter_list|)
throws|throws
name|IOException
block|{
name|PDXObject
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|xobject
operator|==
literal|null
condition|)
block|{
name|retval
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xobject
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|xstream
init|=
operator|(
name|COSStream
operator|)
name|xobject
decl_stmt|;
name|String
name|subtype
init|=
name|xstream
operator|.
name|getNameAsString
argument_list|(
literal|"Subtype"
argument_list|)
decl_stmt|;
if|if
condition|(
name|subtype
operator|.
name|equals
argument_list|(
name|PDXObjectImage
operator|.
name|SUB_TYPE
argument_list|)
condition|)
block|{
name|PDStream
name|image
init|=
operator|new
name|PDStream
argument_list|(
name|xstream
argument_list|)
decl_stmt|;
comment|// See if filters are DCT or JPX otherwise treat as Bitmap-like
comment|// There might be a problem with several filters, but that's ToDo until
comment|// I find an example
name|List
name|filters
init|=
name|image
operator|.
name|getFilters
argument_list|()
decl_stmt|;
if|if
condition|(
name|filters
operator|!=
literal|null
operator|&&
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|DCT_DECODE
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDJpeg
argument_list|(
name|image
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|!=
literal|null
operator|&&
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|CCITTFAX_DECODE
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|PDCcitt
argument_list|(
name|image
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|filters
operator|!=
literal|null
operator|&&
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|JPX_DECODE
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|//throw new IOException( "JPXDecode has not been implemented for images" );
comment|//JPX Decode is not really supported right now, but if we are just doing
comment|//text extraction then we don't want to throw an exception, so for now
comment|//just return a PDPixelMap, which will break later on if it is
comment|//actually used, but for text extraction it is not used.
return|return
operator|new
name|PDPixelMap
argument_list|(
name|image
argument_list|)
return|;
block|}
comment|/*else if( filters != null&& filters.contains(COSName.FLATE_DECODE.getName()))                 {             retval = new PDPixelMap(image);         }*/
else|else
block|{
name|retval
operator|=
operator|new
name|PDPixelMap
argument_list|(
name|image
argument_list|)
expr_stmt|;
comment|//throw new IOException ("Default branch: filters = " + filters.toString());
block|}
block|}
elseif|else
if|if
condition|(
name|subtype
operator|.
name|equals
argument_list|(
name|PDXObjectForm
operator|.
name|SUB_TYPE
argument_list|)
condition|)
block|{
name|retval
operator|=
operator|new
name|PDXObjectForm
argument_list|(
name|xstream
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown xobject subtype '"
operator|+
name|subtype
operator|+
literal|"'"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown xobject type:"
operator|+
name|xobject
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Get the metadata that is part of the document catalog.  This will      * return null if there is no meta data for this object.      *      * @return The metadata for this object.      */
specifier|public
name|PDMetadata
name|getMetadata
parameter_list|()
block|{
name|PDMetadata
name|retval
init|=
literal|null
decl_stmt|;
name|COSStream
name|mdStream
init|=
operator|(
name|COSStream
operator|)
name|xobject
operator|.
name|getStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"Metadata"
argument_list|)
decl_stmt|;
if|if
condition|(
name|mdStream
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDMetadata
argument_list|(
name|mdStream
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Set the metadata for this object.  This can be null.      *      * @param meta The meta data for this object.      */
specifier|public
name|void
name|setMetadata
parameter_list|(
name|PDMetadata
name|meta
parameter_list|)
block|{
name|xobject
operator|.
name|getStream
argument_list|()
operator|.
name|setItem
argument_list|(
literal|"Metadata"
argument_list|,
name|meta
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

