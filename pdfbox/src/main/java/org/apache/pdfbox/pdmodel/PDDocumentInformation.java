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
name|Calendar
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
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_comment
comment|/**  * This is the document metadata.  Each getXXX method will return the entry if  * it exists or null if it does not exist.  If you pass in null for the setXXX  * method then it will clear the value.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.12 $  */
end_comment

begin_class
specifier|public
class|class
name|PDDocumentInformation
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|info
decl_stmt|;
comment|/**      * Default Constructor.      */
specifier|public
name|PDDocumentInformation
parameter_list|()
block|{
name|info
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor that is used for a preexisting dictionary.      *      * @param dic The underlying dictionary.      */
specifier|public
name|PDDocumentInformation
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
name|info
operator|=
name|dic
expr_stmt|;
block|}
comment|/**      * This will get the underlying dictionary that this object wraps.      *      * @return The underlying info dictionary.      */
specifier|public
name|COSDictionary
name|getDictionary
parameter_list|()
block|{
return|return
name|info
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|info
return|;
block|}
comment|/**      * This will get the title of the document.  This will return null if no title exists.      *      * @return The title of the document.      */
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|info
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|TITLE
argument_list|)
return|;
block|}
comment|/**      * This will set the title of the document.      *      * @param title The new title for the document.      */
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|info
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|TITLE
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the author of the document.  This will return null if no author exists.      *      * @return The author of the document.      */
specifier|public
name|String
name|getAuthor
parameter_list|()
block|{
return|return
name|info
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|AUTHOR
argument_list|)
return|;
block|}
comment|/**      * This will set the author of the document.      *      * @param author The new author for the document.      */
specifier|public
name|void
name|setAuthor
parameter_list|(
name|String
name|author
parameter_list|)
block|{
name|info
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|AUTHOR
argument_list|,
name|author
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the subject of the document.  This will return null if no subject exists.      *      * @return The subject of the document.      */
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|info
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|SUBJECT
argument_list|)
return|;
block|}
comment|/**      * This will set the subject of the document.      *      * @param subject The new subject for the document.      */
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|info
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|SUBJECT
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the keywords of the document.  This will return null if no keywords exists.      *      * @return The keywords of the document.      */
specifier|public
name|String
name|getKeywords
parameter_list|()
block|{
return|return
name|info
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|KEYWORDS
argument_list|)
return|;
block|}
comment|/**      * This will set the keywords of the document.      *      * @param keywords The new keywords for the document.      */
specifier|public
name|void
name|setKeywords
parameter_list|(
name|String
name|keywords
parameter_list|)
block|{
name|info
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|KEYWORDS
argument_list|,
name|keywords
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the creator of the document.  This will return null if no creator exists.      *      * @return The creator of the document.      */
specifier|public
name|String
name|getCreator
parameter_list|()
block|{
return|return
name|info
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|CREATOR
argument_list|)
return|;
block|}
comment|/**      * This will set the creator of the document.      *      * @param creator The new creator for the document.      */
specifier|public
name|void
name|setCreator
parameter_list|(
name|String
name|creator
parameter_list|)
block|{
name|info
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|CREATOR
argument_list|,
name|creator
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the producer of the document.  This will return null if no producer exists.      *      * @return The producer of the document.      */
specifier|public
name|String
name|getProducer
parameter_list|()
block|{
return|return
name|info
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|PRODUCER
argument_list|)
return|;
block|}
comment|/**      * This will set the producer of the document.      *      * @param producer The new producer for the document.      */
specifier|public
name|void
name|setProducer
parameter_list|(
name|String
name|producer
parameter_list|)
block|{
name|info
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|PRODUCER
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the creation date of the document.  This will return null if no creation date exists.      *      * @return The creation date of the document.      *      * @throws IOException If there is an error creating the date.      */
specifier|public
name|Calendar
name|getCreationDate
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|info
operator|.
name|getDate
argument_list|(
name|COSName
operator|.
name|CREATION_DATE
argument_list|)
return|;
block|}
comment|/**      * This will set the creation date of the document.      *      * @param date The new creation date for the document.      */
specifier|public
name|void
name|setCreationDate
parameter_list|(
name|Calendar
name|date
parameter_list|)
block|{
name|info
operator|.
name|setDate
argument_list|(
name|COSName
operator|.
name|CREATION_DATE
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the modification date of the document.  This will return null if no modification date exists.      *      * @return The modification date of the document.      *      * @throws IOException If there is an error creating the date.      */
specifier|public
name|Calendar
name|getModificationDate
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|info
operator|.
name|getDate
argument_list|(
name|COSName
operator|.
name|MOD_DATE
argument_list|)
return|;
block|}
comment|/**      * This will set the modification date of the document.      *      * @param date The new modification date for the document.      */
specifier|public
name|void
name|setModificationDate
parameter_list|(
name|Calendar
name|date
parameter_list|)
block|{
name|info
operator|.
name|setDate
argument_list|(
name|COSName
operator|.
name|MOD_DATE
argument_list|,
name|date
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the trapped value for the document.      * This will return null if one is not found.      *      * @return The trapped value for the document.      */
specifier|public
name|String
name|getTrapped
parameter_list|()
block|{
return|return
name|info
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|TRAPPED
argument_list|)
return|;
block|}
comment|/**      *  This will get the value of a custom metadata information field for the document.      *  This will return null if one is not found.      *      * @param fieldName Name of custom metadata field from pdf document.      *      * @return String Value of metadata field      *      * @author  Gerardo Ortiz      */
specifier|public
name|String
name|getCustomMetadataValue
parameter_list|(
name|String
name|fieldName
parameter_list|)
block|{
return|return
name|info
operator|.
name|getString
argument_list|(
name|fieldName
argument_list|)
return|;
block|}
comment|/**      * Set the custom metadata value.      *      * @param fieldName The name of the custom metadata field.      * @param fieldValue The value to the custom metadata field.      */
specifier|public
name|void
name|setCustomMetadataValue
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|String
name|fieldValue
parameter_list|)
block|{
name|info
operator|.
name|setString
argument_list|(
name|fieldName
argument_list|,
name|fieldValue
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will set the trapped of the document.  This will be      * 'True', 'False', or 'Unknown'.      *      * @param value The new trapped value for the document.      */
specifier|public
name|void
name|setTrapped
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|value
operator|.
name|equals
argument_list|(
literal|"True"
argument_list|)
operator|&&
operator|!
name|value
operator|.
name|equals
argument_list|(
literal|"False"
argument_list|)
operator|&&
operator|!
name|value
operator|.
name|equals
argument_list|(
literal|"Unknown"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Valid values for trapped are "
operator|+
literal|"'True', 'False', or 'Unknown'"
argument_list|)
throw|;
block|}
name|info
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|TRAPPED
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

