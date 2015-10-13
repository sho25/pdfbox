begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|utils
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
name|COSDocument
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
name|COSFloat
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
name|COSInteger
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
name|COSNumber
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
name|cos
operator|.
name|COSString
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
name|cos
operator|.
name|COSObjectKey
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|COSUtils
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|COSUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|COSUtils
parameter_list|()
block|{     }
comment|/**      * return true if the elt is a COSDictionary or a reference to a COSDictionary      *       * @param elt the object to check.      * @param doc the document.      * @return true if the object is a COSDictionary or a reference to it.      */
specifier|public
specifier|static
name|boolean
name|isDictionary
parameter_list|(
name|COSBase
name|elt
parameter_list|,
name|COSDocument
name|doc
parameter_list|)
block|{
if|if
condition|(
name|elt
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|elt
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|doc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSDictionary
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
operator|(
name|elt
operator|instanceof
name|COSDictionary
operator|)
return|;
block|}
comment|/**      * return true if the elt is a COSString or a COSName or a reference to it.      *       * @param elt the object to check.      * @param doc the document.      * @return true if the object is a COSString or a COSName or a reference to it.      */
specifier|public
specifier|static
name|boolean
name|isString
parameter_list|(
name|COSBase
name|elt
parameter_list|,
name|COSDocument
name|doc
parameter_list|)
block|{
if|if
condition|(
name|elt
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|elt
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|doc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|obj
operator|!=
literal|null
operator|&&
operator|(
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSString
operator|||
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSName
operator|)
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
operator|(
name|elt
operator|instanceof
name|COSString
operator|||
name|elt
operator|instanceof
name|COSName
operator|)
return|;
block|}
comment|/**      * return true if the elt is a COSStream or a reference to a COSStream      *       * @param elt the object to check.      * @param doc the document.      * @return true if the object is a COSStream or a reference to it.      */
specifier|public
specifier|static
name|boolean
name|isStream
parameter_list|(
name|COSBase
name|elt
parameter_list|,
name|COSDocument
name|doc
parameter_list|)
block|{
if|if
condition|(
name|elt
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|elt
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|doc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSStream
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
operator|(
name|elt
operator|instanceof
name|COSStream
operator|)
return|;
block|}
comment|/**      * return true if the elt is a COSInteger or a reference to a COSInteger      *       * @param elt the object to check.      * @param doc the document.      * @return true if the object is a COSInteger or a reference to it.      */
specifier|public
specifier|static
name|boolean
name|isInteger
parameter_list|(
name|COSBase
name|elt
parameter_list|,
name|COSDocument
name|doc
parameter_list|)
block|{
if|if
condition|(
name|elt
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|elt
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|doc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSInteger
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
operator|(
name|elt
operator|instanceof
name|COSInteger
operator|)
return|;
block|}
comment|/**      * return true if elt is COSInteger or COSFloat      *       * @param elt the object to check.      * @param doc the document.      * @return true if the object is a COSInteger, COSFloat or a reference to it.      */
specifier|public
specifier|static
name|boolean
name|isNumeric
parameter_list|(
name|COSBase
name|elt
parameter_list|,
name|COSDocument
name|doc
parameter_list|)
block|{
return|return
name|isInteger
argument_list|(
name|elt
argument_list|,
name|doc
argument_list|)
operator|||
name|isFloat
argument_list|(
name|elt
argument_list|,
name|doc
argument_list|)
return|;
block|}
comment|/**      * return true if the elt is a COSFloat or a reference to a COSFloat      *       * @param elt the object to check.      * @param doc the document.      * @return true if the object is a COSFloat or a reference to it.      */
specifier|public
specifier|static
name|boolean
name|isFloat
parameter_list|(
name|COSBase
name|elt
parameter_list|,
name|COSDocument
name|doc
parameter_list|)
block|{
if|if
condition|(
name|elt
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|elt
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|doc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSFloat
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
operator|(
name|elt
operator|instanceof
name|COSFloat
operator|)
return|;
block|}
comment|/**      * return true if the elt is a COSArray or a reference to a COSArray      *       * @param elt the object to check.      * @param doc the document.      * @return true if the object is a COSArray or a reference to it.      */
specifier|public
specifier|static
name|boolean
name|isArray
parameter_list|(
name|COSBase
name|elt
parameter_list|,
name|COSDocument
name|doc
parameter_list|)
block|{
if|if
condition|(
name|elt
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|elt
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|doc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
operator|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSArray
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
operator|(
name|elt
operator|instanceof
name|COSArray
operator|)
return|;
block|}
comment|/**      * Return the COSBase object as COSArray if the COSBase object is an instance of COSArray or a reference to a      * COSArray object. In other cases, this method returns null;      *       * @param cbase the object to get.      * @param cDoc the document.      * @return the object as COSArray if the object is a COSArray or a reference to it. Returns null otherwise.      */
specifier|public
specifier|static
name|COSArray
name|getAsArray
parameter_list|(
name|COSBase
name|cbase
parameter_list|,
name|COSDocument
name|cDoc
parameter_list|)
block|{
if|if
condition|(
name|cbase
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|cbase
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|cDoc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|(
name|COSArray
operator|)
name|obj
operator|.
name|getObject
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|cbase
operator|instanceof
name|COSArray
condition|)
block|{
return|return
operator|(
name|COSArray
operator|)
name|cbase
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Return the COSBase object as String if the COSBase object is an instance of COSString or      * COSName or a reference to it.      *      * @param cbase the object to get.      * @param cDoc the document.      * @return the object as String if the object is a COSString or COSName or reference to it.      * Returns null otherwise.      */
specifier|public
specifier|static
name|String
name|getAsString
parameter_list|(
name|COSBase
name|cbase
parameter_list|,
name|COSDocument
name|cDoc
parameter_list|)
block|{
if|if
condition|(
name|cbase
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|cbase
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|cDoc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSString
condition|)
block|{
return|return
operator|(
operator|(
name|COSString
operator|)
name|obj
operator|.
name|getObject
argument_list|()
operator|)
operator|.
name|getString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSName
condition|)
block|{
return|return
operator|(
operator|(
name|COSName
operator|)
name|obj
operator|.
name|getObject
argument_list|()
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|cbase
operator|instanceof
name|COSString
condition|)
block|{
return|return
operator|(
operator|(
name|COSString
operator|)
name|cbase
operator|)
operator|.
name|getString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|cbase
operator|instanceof
name|COSName
condition|)
block|{
return|return
operator|(
operator|(
name|COSName
operator|)
name|cbase
operator|)
operator|.
name|getName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Return the COSBase object as COSDictionary if the COSBase object is an instance of COSDictionary or a reference      * to a COSDictionary object. In other cases, this method returns null;      *       * @param cbase the object to get.      * @param cDoc the document.      * @return the object as COSDictionary if the object is a COSDictionary or a reference to it. Returns null otherwise.      */
specifier|public
specifier|static
name|COSDictionary
name|getAsDictionary
parameter_list|(
name|COSBase
name|cbase
parameter_list|,
name|COSDocument
name|cDoc
parameter_list|)
block|{
if|if
condition|(
name|cbase
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|cbase
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|cDoc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|(
name|COSDictionary
operator|)
name|obj
operator|.
name|getObject
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|cbase
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|(
name|COSDictionary
operator|)
name|cbase
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Return the COSBase object as COSStream if the COSBase object is an instance of COSStream or a reference to a      * COSStream object. In other cases, this method returns null;      *       * @param cbase the object to get.      * @param cDoc the document.      * @return the object as COSStream if the object is a COSStream or a reference to it. Returns null otherwise.      */
specifier|public
specifier|static
name|COSStream
name|getAsStream
parameter_list|(
name|COSBase
name|cbase
parameter_list|,
name|COSDocument
name|cDoc
parameter_list|)
block|{
if|if
condition|(
name|cbase
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|cbase
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|cDoc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSStream
condition|)
block|{
return|return
operator|(
name|COSStream
operator|)
name|obj
operator|.
name|getObject
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|cbase
operator|instanceof
name|COSStream
condition|)
block|{
return|return
operator|(
name|COSStream
operator|)
name|cbase
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Return the COSBase object as Float if the COSBase object is an instance of COSFloat or a reference to a COSFloat      * object. In other cases, this method returns null;      *       * @param cbase the object to get.      * @param cDoc the document.      * @return the object as Float if the object is a COSFloat or a reference to it. Returns null otherwise.      */
specifier|public
specifier|static
name|Float
name|getAsFloat
parameter_list|(
name|COSBase
name|cbase
parameter_list|,
name|COSDocument
name|cDoc
parameter_list|)
block|{
if|if
condition|(
name|cbase
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|cbase
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|cDoc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSNumber
condition|)
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|obj
operator|.
name|getObject
argument_list|()
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|cbase
operator|instanceof
name|COSNumber
condition|)
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|cbase
operator|)
operator|.
name|floatValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Return the COSBase object as Integer if the COSBase object is an instance of COSInteger or a reference to a      * COSInteger object. In other cases, this method returns null;      *       * @param cbase the object to get.      * @param cDoc the document.      * @return the object as Integer if the object is a COSInteger or a reference to it. Returns null otherwise.      */
specifier|public
specifier|static
name|Integer
name|getAsInteger
parameter_list|(
name|COSBase
name|cbase
parameter_list|,
name|COSDocument
name|cDoc
parameter_list|)
block|{
if|if
condition|(
name|cbase
operator|instanceof
name|COSObject
condition|)
block|{
try|try
block|{
name|COSObjectKey
name|key
init|=
operator|new
name|COSObjectKey
argument_list|(
operator|(
name|COSObject
operator|)
name|cbase
argument_list|)
decl_stmt|;
name|COSObject
name|obj
init|=
name|cDoc
operator|.
name|getObjectFromPool
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|.
name|getObject
argument_list|()
operator|instanceof
name|COSNumber
condition|)
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|obj
operator|.
name|getObject
argument_list|()
operator|)
operator|.
name|intValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|cbase
operator|instanceof
name|COSNumber
condition|)
block|{
return|return
operator|(
operator|(
name|COSNumber
operator|)
name|cbase
operator|)
operator|.
name|intValue
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Close the given Document. If the close method of the document throws an      * exception, it is logged using a commons logger (Level : WARN)      *      * @param document the document.      */
specifier|public
specifier|static
name|void
name|closeDocumentQuietly
parameter_list|(
name|COSDocument
name|document
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Error occured during the close of a COSDocument : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Close the given Document. If the close method of the document throws an      * exception, it is logged using a commons logger (Level : WARN)       *       * @param document the document.      */
specifier|public
specifier|static
name|void
name|closeDocumentQuietly
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
if|if
condition|(
name|document
operator|!=
literal|null
condition|)
block|{
name|closeDocumentQuietly
argument_list|(
name|document
operator|.
name|getDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

