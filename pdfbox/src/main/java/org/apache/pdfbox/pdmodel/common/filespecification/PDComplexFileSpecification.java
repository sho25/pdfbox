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

begin_comment
comment|/**  * This represents a file specification.  *  * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|PDComplexFileSpecification
extends|extends
name|PDFileSpecification
block|{
specifier|private
name|COSDictionary
name|fs
decl_stmt|;
specifier|private
name|COSDictionary
name|efDictionary
decl_stmt|;
comment|/**      * Default Constructor.      */
specifier|public
name|PDComplexFileSpecification
parameter_list|()
block|{
name|fs
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|fs
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|FILESPEC
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor. Creates empty COSDictionary if dict is null.      *      * @param dict The dictionary that fulfils this file specification.      */
specifier|public
name|PDComplexFileSpecification
parameter_list|(
name|COSDictionary
name|dict
parameter_list|)
block|{
if|if
condition|(
name|dict
operator|==
literal|null
condition|)
block|{
name|fs
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|fs
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|TYPE
argument_list|,
name|COSName
operator|.
name|FILESPEC
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fs
operator|=
name|dict
expr_stmt|;
block|}
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|fs
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|fs
return|;
block|}
specifier|private
name|COSDictionary
name|getEFDictionary
parameter_list|()
block|{
if|if
condition|(
name|efDictionary
operator|==
literal|null
operator|&&
name|fs
operator|!=
literal|null
condition|)
block|{
name|efDictionary
operator|=
operator|(
name|COSDictionary
operator|)
name|fs
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|EF
argument_list|)
expr_stmt|;
block|}
return|return
name|efDictionary
return|;
block|}
specifier|private
name|COSBase
name|getObjectFromEFDictionary
parameter_list|(
name|COSName
name|key
parameter_list|)
block|{
name|COSDictionary
name|ef
init|=
name|getEFDictionary
argument_list|()
decl_stmt|;
if|if
condition|(
name|ef
operator|!=
literal|null
condition|)
block|{
return|return
name|ef
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      *<p>Preferred method for getting the filename.      * It will determinate the recommended file name.</p>      *<p>First of all we try to get the unicode filename if it exist.      * If it doesn't exist we take a look at the DOS, MAC UNIX filenames.      * If no one exist the required F entry will be returned.</p>      *      * @return The preferred file name.      */
specifier|public
name|String
name|getFilename
parameter_list|()
block|{
name|String
name|filename
init|=
name|getFileUnicode
argument_list|()
decl_stmt|;
if|if
condition|(
name|filename
operator|==
literal|null
condition|)
block|{
name|filename
operator|=
name|getFileDos
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|filename
operator|==
literal|null
condition|)
block|{
name|filename
operator|=
name|getFileMac
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|filename
operator|==
literal|null
condition|)
block|{
name|filename
operator|=
name|getFileUnix
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|filename
operator|==
literal|null
condition|)
block|{
name|filename
operator|=
name|getFile
argument_list|()
expr_stmt|;
block|}
return|return
name|filename
return|;
block|}
comment|/**      * This will get the unicode file name.      *      * @return The file name.      */
specifier|public
name|String
name|getFileUnicode
parameter_list|()
block|{
return|return
name|fs
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|UF
argument_list|)
return|;
block|}
comment|/**      * This will set unicode file name.      *      * @param file The name of the file.      */
specifier|public
name|void
name|setFileUnicode
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|fs
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|UF
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the file name.      *      * @return The file name.      */
specifier|public
name|String
name|getFile
parameter_list|()
block|{
return|return
name|fs
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|F
argument_list|)
return|;
block|}
comment|/**      * This will set the file name.      *      * @param file The name of the file.      */
specifier|public
name|void
name|setFile
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|fs
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the name representing a Dos file.      *      * @return The file name.      */
specifier|public
name|String
name|getFileDos
parameter_list|()
block|{
return|return
name|fs
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|DOS
argument_list|)
return|;
block|}
comment|/**      * This will set name representing a dos file.      *      * @param file The name of the file.      */
specifier|public
name|void
name|setFileDos
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|fs
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DOS
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the name representing a Mac file.      *      * @return The file name.      */
specifier|public
name|String
name|getFileMac
parameter_list|()
block|{
return|return
name|fs
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|MAC
argument_list|)
return|;
block|}
comment|/**      * This will set name representing a Mac file.      *      * @param file The name of the file.      */
specifier|public
name|void
name|setFileMac
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|fs
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|MAC
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the name representing a Unix file.      *      * @return The file name.      */
specifier|public
name|String
name|getFileUnix
parameter_list|()
block|{
return|return
name|fs
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|UNIX
argument_list|)
return|;
block|}
comment|/**      * This will set name representing a Unix file.      *      * @param file The name of the file.      */
specifier|public
name|void
name|setFileUnix
parameter_list|(
name|String
name|file
parameter_list|)
block|{
name|fs
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|UNIX
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
comment|/**      * Tell if the underlying file is volatile and should not be cached by the      * reader application.  Default: false      *      * @param fileIsVolatile The new value for the volatility of the file.      */
specifier|public
name|void
name|setVolatile
parameter_list|(
name|boolean
name|fileIsVolatile
parameter_list|)
block|{
name|fs
operator|.
name|setBoolean
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
name|fileIsVolatile
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get if the file is volatile.  Default: false      *      * @return True if the file is volatile attribute is set.      */
specifier|public
name|boolean
name|isVolatile
parameter_list|()
block|{
return|return
name|fs
operator|.
name|getBoolean
argument_list|(
name|COSName
operator|.
name|V
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Get the embedded file.      *      * @return The embedded file for this file spec.      */
specifier|public
name|PDEmbeddedFile
name|getEmbeddedFile
parameter_list|()
block|{
name|PDEmbeddedFile
name|file
init|=
literal|null
decl_stmt|;
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|getObjectFromEFDictionary
argument_list|(
name|COSName
operator|.
name|F
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|file
operator|=
operator|new
name|PDEmbeddedFile
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
comment|/**      * Set the embedded file for this spec.      *      * @param file The file to be embedded.      */
specifier|public
name|void
name|setEmbeddedFile
parameter_list|(
name|PDEmbeddedFile
name|file
parameter_list|)
block|{
name|COSDictionary
name|ef
init|=
name|getEFDictionary
argument_list|()
decl_stmt|;
if|if
condition|(
name|ef
operator|==
literal|null
operator|&&
name|file
operator|!=
literal|null
condition|)
block|{
name|ef
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|fs
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|EF
argument_list|,
name|ef
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ef
operator|!=
literal|null
condition|)
block|{
name|ef
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the embedded dos file.      *      * @return The embedded dos file for this file spec.      */
specifier|public
name|PDEmbeddedFile
name|getEmbeddedFileDos
parameter_list|()
block|{
name|PDEmbeddedFile
name|file
init|=
literal|null
decl_stmt|;
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|getObjectFromEFDictionary
argument_list|(
name|COSName
operator|.
name|DOS
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|file
operator|=
operator|new
name|PDEmbeddedFile
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
comment|/**      * Set the embedded dos file for this spec.      *      * @param file The dos file to be embedded.      */
specifier|public
name|void
name|setEmbeddedFileDos
parameter_list|(
name|PDEmbeddedFile
name|file
parameter_list|)
block|{
name|COSDictionary
name|ef
init|=
name|getEFDictionary
argument_list|()
decl_stmt|;
if|if
condition|(
name|ef
operator|==
literal|null
operator|&&
name|file
operator|!=
literal|null
condition|)
block|{
name|ef
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|fs
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|EF
argument_list|,
name|ef
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ef
operator|!=
literal|null
condition|)
block|{
name|ef
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DOS
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the embedded Mac file.      *      * @return The embedded Mac file for this file spec.      */
specifier|public
name|PDEmbeddedFile
name|getEmbeddedFileMac
parameter_list|()
block|{
name|PDEmbeddedFile
name|file
init|=
literal|null
decl_stmt|;
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|getObjectFromEFDictionary
argument_list|(
name|COSName
operator|.
name|MAC
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|file
operator|=
operator|new
name|PDEmbeddedFile
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
comment|/**      * Set the embedded Mac file for this spec.      *      * @param file The Mac file to be embedded.      */
specifier|public
name|void
name|setEmbeddedFileMac
parameter_list|(
name|PDEmbeddedFile
name|file
parameter_list|)
block|{
name|COSDictionary
name|ef
init|=
name|getEFDictionary
argument_list|()
decl_stmt|;
if|if
condition|(
name|ef
operator|==
literal|null
operator|&&
name|file
operator|!=
literal|null
condition|)
block|{
name|ef
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|fs
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|EF
argument_list|,
name|ef
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ef
operator|!=
literal|null
condition|)
block|{
name|ef
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|MAC
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the embedded Unix file.      *      * @return The embedded file for this file spec.      */
specifier|public
name|PDEmbeddedFile
name|getEmbeddedFileUnix
parameter_list|()
block|{
name|PDEmbeddedFile
name|file
init|=
literal|null
decl_stmt|;
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|getObjectFromEFDictionary
argument_list|(
name|COSName
operator|.
name|UNIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|file
operator|=
operator|new
name|PDEmbeddedFile
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
comment|/**      * Set the embedded Unix file for this spec.      *      * @param file The Unix file to be embedded.      */
specifier|public
name|void
name|setEmbeddedFileUnix
parameter_list|(
name|PDEmbeddedFile
name|file
parameter_list|)
block|{
name|COSDictionary
name|ef
init|=
name|getEFDictionary
argument_list|()
decl_stmt|;
if|if
condition|(
name|ef
operator|==
literal|null
operator|&&
name|file
operator|!=
literal|null
condition|)
block|{
name|ef
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|fs
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|EF
argument_list|,
name|ef
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ef
operator|!=
literal|null
condition|)
block|{
name|ef
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|UNIX
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the embedded unicode file.      *      * @return The embedded unicode file for this file spec.      */
specifier|public
name|PDEmbeddedFile
name|getEmbeddedFileUnicode
parameter_list|()
block|{
name|PDEmbeddedFile
name|file
init|=
literal|null
decl_stmt|;
name|COSStream
name|stream
init|=
operator|(
name|COSStream
operator|)
name|getObjectFromEFDictionary
argument_list|(
name|COSName
operator|.
name|UF
argument_list|)
decl_stmt|;
if|if
condition|(
name|stream
operator|!=
literal|null
condition|)
block|{
name|file
operator|=
operator|new
name|PDEmbeddedFile
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
comment|/**      * Set the embedded Unicode file for this spec.      *      * @param file The Unicode file to be embedded.      */
specifier|public
name|void
name|setEmbeddedFileUnicode
parameter_list|(
name|PDEmbeddedFile
name|file
parameter_list|)
block|{
name|COSDictionary
name|ef
init|=
name|getEFDictionary
argument_list|()
decl_stmt|;
if|if
condition|(
name|ef
operator|==
literal|null
operator|&&
name|file
operator|!=
literal|null
condition|)
block|{
name|ef
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|fs
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|EF
argument_list|,
name|ef
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ef
operator|!=
literal|null
condition|)
block|{
name|ef
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|UF
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Set the file description.      *       * @param description The file description      */
specifier|public
name|void
name|setFileDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|fs
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DESC
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the description.      *      * @return The file description.      */
specifier|public
name|String
name|getFileDescription
parameter_list|()
block|{
return|return
name|fs
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|DESC
argument_list|)
return|;
block|}
block|}
end_class

end_unit

