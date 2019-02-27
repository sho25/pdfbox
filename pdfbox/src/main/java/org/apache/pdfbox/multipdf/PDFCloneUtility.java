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
name|multipdf
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|COSArray
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
name|COSObject
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
name|io
operator|.
name|IOUtils
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

begin_comment
comment|/**  * Utility class used to clone PDF objects. It keeps track of objects it has  * already cloned.  *  */
end_comment

begin_class
class|class
name|PDFCloneUtility
block|{
specifier|private
specifier|final
name|PDDocument
name|destination
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|COSBase
argument_list|>
name|clonedVersion
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|COSBase
argument_list|>
name|clonedValues
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**      * Creates a new instance for the given target document.      * @param dest the destination PDF document that will receive the clones      */
name|PDFCloneUtility
parameter_list|(
name|PDDocument
name|dest
parameter_list|)
block|{
name|this
operator|.
name|destination
operator|=
name|dest
expr_stmt|;
block|}
comment|/**      * Returns the destination PDF document this cloner instance is set up for.      * @return the destination PDF document      */
name|PDDocument
name|getDestination
parameter_list|()
block|{
return|return
name|this
operator|.
name|destination
return|;
block|}
comment|/**      * Deep-clones the given object for inclusion into a different PDF document identified by      * the destination parameter.      * @param base the initial object as the root of the deep-clone operation      * @return the cloned instance of the base object      * @throws IOException if an I/O error occurs      */
name|COSBase
name|cloneForNewDocument
parameter_list|(
name|Object
name|base
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
return|return
literal|null
return|;
block|}
name|COSBase
name|retval
init|=
name|clonedVersion
operator|.
name|get
argument_list|(
name|base
argument_list|)
decl_stmt|;
if|if
condition|(
name|retval
operator|!=
literal|null
condition|)
block|{
comment|//we are done, it has already been converted.
return|return
name|retval
return|;
block|}
if|if
condition|(
name|base
operator|instanceof
name|COSBase
operator|&&
name|clonedValues
operator|.
name|contains
argument_list|(
name|base
argument_list|)
condition|)
block|{
comment|// Don't clone a clone
return|return
operator|(
name|COSBase
operator|)
name|base
return|;
block|}
if|if
condition|(
name|base
operator|instanceof
name|List
condition|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|base
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|list
control|)
block|{
name|array
operator|.
name|add
argument_list|(
name|cloneForNewDocument
argument_list|(
name|obj
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
name|array
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSObjectable
operator|&&
operator|!
operator|(
name|base
operator|instanceof
name|COSBase
operator|)
condition|)
block|{
name|retval
operator|=
name|cloneForNewDocument
argument_list|(
operator|(
operator|(
name|COSObjectable
operator|)
name|base
operator|)
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSObject
condition|)
block|{
name|COSObject
name|object
init|=
operator|(
name|COSObject
operator|)
name|base
decl_stmt|;
name|retval
operator|=
name|cloneForNewDocument
argument_list|(
name|object
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|newArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|base
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|newArray
operator|.
name|add
argument_list|(
name|cloneForNewDocument
argument_list|(
name|array
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
name|newArray
expr_stmt|;
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSStream
condition|)
block|{
name|COSStream
name|originalStream
init|=
operator|(
name|COSStream
operator|)
name|base
decl_stmt|;
name|COSStream
name|stream
init|=
name|destination
operator|.
name|getDocument
argument_list|()
operator|.
name|createCOSStream
argument_list|()
decl_stmt|;
try|try
init|(
name|OutputStream
name|output
init|=
name|stream
operator|.
name|createRawOutputStream
argument_list|()
init|;                    InputStream input = originalStream.createRawInputStream()
block|)
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|input
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|stream
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
name|entry
range|:
name|originalStream
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|stream
operator|.
name|setItem
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|cloneForNewDocument
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
name|stream
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
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|base
decl_stmt|;
name|retval
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|retval
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
name|entry
range|:
name|dic
operator|.
name|entrySet
argument_list|()
control|)
block|{
operator|(
operator|(
name|COSDictionary
operator|)
name|retval
operator|)
operator|.
name|setItem
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|cloneForNewDocument
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|retval
operator|=
operator|(
name|COSBase
operator|)
name|base
expr_stmt|;
block|}
name|clonedVersion
operator|.
name|put
parameter_list|(
name|base
parameter_list|,
name|retval
parameter_list|)
constructor_decl|;
name|clonedValues
operator|.
name|add
parameter_list|(
name|retval
parameter_list|)
constructor_decl|;
return|return
name|retval
return|;
block|}
end_class

begin_comment
comment|/**        * Merges two objects of the same type by deep-cloning its members.        *<br>        * Base and target must be instances of the same class.        * @param base the base object to be cloned        * @param target the merge target        * @throws IOException if an I/O error occurs        */
end_comment

begin_function
name|void
name|cloneMerge
parameter_list|(
specifier|final
name|COSObjectable
name|base
parameter_list|,
name|COSObjectable
name|target
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
return|return;
block|}
name|COSBase
name|retval
init|=
name|clonedVersion
operator|.
name|get
argument_list|(
name|base
argument_list|)
decl_stmt|;
if|if
condition|(
name|retval
operator|!=
literal|null
condition|)
block|{
return|return;
comment|//we are done, it has already been converted. // ### Is that correct for cloneMerge???
block|}
comment|//TODO what when clone-merging a clone? Does it ever happen?
if|if
condition|(
operator|!
operator|(
name|base
operator|instanceof
name|COSBase
operator|)
condition|)
block|{
name|cloneMerge
argument_list|(
name|base
operator|.
name|getCOSObject
argument_list|()
argument_list|,
name|target
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSObject
condition|)
block|{
if|if
condition|(
name|target
operator|instanceof
name|COSObject
condition|)
block|{
name|cloneMerge
argument_list|(
operator|(
operator|(
name|COSObject
operator|)
name|base
operator|)
operator|.
name|getObject
argument_list|()
argument_list|,
operator|(
operator|(
name|COSObject
operator|)
name|target
operator|)
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|target
operator|instanceof
name|COSDictionary
operator|||
name|target
operator|instanceof
name|COSArray
condition|)
block|{
name|cloneMerge
argument_list|(
operator|(
operator|(
name|COSObject
operator|)
name|base
operator|)
operator|.
name|getObject
argument_list|()
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|base
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
operator|(
operator|(
name|COSArray
operator|)
name|target
operator|)
operator|.
name|add
argument_list|(
name|cloneForNewDocument
argument_list|(
name|array
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|retval
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSStream
condition|)
block|{
comment|// does that make sense???
name|COSStream
name|originalStream
init|=
operator|(
name|COSStream
operator|)
name|base
decl_stmt|;
name|COSStream
name|stream
init|=
name|destination
operator|.
name|getDocument
argument_list|()
operator|.
name|createCOSStream
argument_list|()
decl_stmt|;
try|try
init|(
name|OutputStream
name|output
init|=
name|stream
operator|.
name|createOutputStream
argument_list|(
name|originalStream
operator|.
name|getFilters
argument_list|()
argument_list|)
init|)
block|{
name|IOUtils
operator|.
name|copy
argument_list|(
name|originalStream
operator|.
name|createInputStream
argument_list|()
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|stream
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
name|entry
range|:
name|originalStream
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|stream
operator|.
name|setItem
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|cloneForNewDocument
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
name|stream
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
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|base
decl_stmt|;
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|retval
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
name|entry
range|:
name|dic
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|COSName
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|COSBase
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
operator|(
name|COSDictionary
operator|)
name|target
operator|)
operator|.
name|getItem
argument_list|(
name|key
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|cloneMerge
argument_list|(
name|value
argument_list|,
operator|(
operator|(
name|COSDictionary
operator|)
name|target
operator|)
operator|.
name|getItem
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|(
operator|(
name|COSDictionary
operator|)
name|target
operator|)
operator|.
name|setItem
argument_list|(
name|key
argument_list|,
name|cloneForNewDocument
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|retval
operator|=
operator|(
name|COSBase
operator|)
name|base
expr_stmt|;
block|}
name|clonedVersion
operator|.
name|put
argument_list|(
name|base
argument_list|,
name|retval
argument_list|)
expr_stmt|;
name|clonedValues
operator|.
name|add
argument_list|(
name|retval
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

