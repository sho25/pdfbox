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
name|fontbox
operator|.
name|cff
package|;
end_package

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
name|Map
import|;
end_import

begin_comment
comment|/**  * A CFF charset. A charset is an array of SIDs/CIDs for all glyphs in the font.  *  * todo: split this into two? CFFCharsetType1 and CFFCharsetCID ?  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|CFFCharset
block|{
specifier|private
specifier|final
name|boolean
name|isCIDFont
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|sidOrCidToGid
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|gidToSid
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|nameToSid
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
comment|// inverse
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
name|gidToCid
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|gidToName
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Package-private constructor for use by subclasses.      *      * @param isCIDFont true if the parent font is a CIDFont      */
name|CFFCharset
parameter_list|(
name|boolean
name|isCIDFont
parameter_list|)
block|{
name|this
operator|.
name|isCIDFont
operator|=
name|isCIDFont
expr_stmt|;
block|}
comment|/**      * Adds a new GID/SID/name combination to the charset.      *      * @param gid GID      * @param sid SID      */
specifier|public
name|void
name|addSID
parameter_list|(
name|int
name|gid
parameter_list|,
name|int
name|sid
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|isCIDFont
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not a Type 1-equivalent font"
argument_list|)
throw|;
block|}
name|sidOrCidToGid
operator|.
name|put
argument_list|(
name|sid
argument_list|,
name|gid
argument_list|)
expr_stmt|;
name|gidToSid
operator|.
name|put
argument_list|(
name|gid
argument_list|,
name|sid
argument_list|)
expr_stmt|;
name|nameToSid
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|sid
argument_list|)
expr_stmt|;
name|gidToName
operator|.
name|put
argument_list|(
name|gid
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a new GID/CID combination to the charset.      *      * @param gid GID      * @param cid CID      */
specifier|public
name|void
name|addCID
parameter_list|(
name|int
name|gid
parameter_list|,
name|int
name|cid
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isCIDFont
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not a CIDFont"
argument_list|)
throw|;
block|}
name|sidOrCidToGid
operator|.
name|put
argument_list|(
name|cid
argument_list|,
name|gid
argument_list|)
expr_stmt|;
name|gidToCid
operator|.
name|put
argument_list|(
name|gid
argument_list|,
name|cid
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the SID for a given GID. SIDs are internal to the font and are not public.      *      * @param sid SID      * @return GID      */
name|int
name|getSIDForGID
parameter_list|(
name|int
name|sid
parameter_list|)
block|{
if|if
condition|(
name|isCIDFont
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not a Type 1-equivalent font"
argument_list|)
throw|;
block|}
name|Integer
name|gid
init|=
name|gidToSid
operator|.
name|get
argument_list|(
name|sid
argument_list|)
decl_stmt|;
if|if
condition|(
name|gid
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|gid
return|;
block|}
comment|/**      * Returns the GID for the given SID. SIDs are internal to the font and are not public.      *      * @param sid SID      * @return GID      */
name|int
name|getGIDForSID
parameter_list|(
name|int
name|sid
parameter_list|)
block|{
if|if
condition|(
name|isCIDFont
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not a Type 1-equivalent font"
argument_list|)
throw|;
block|}
name|Integer
name|gid
init|=
name|sidOrCidToGid
operator|.
name|get
argument_list|(
name|sid
argument_list|)
decl_stmt|;
if|if
condition|(
name|gid
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|gid
return|;
block|}
comment|/**      * Returns the GID for a given CID. Returns 0 if the CID is missing.      *      * @param cid CID      * @return GID      */
specifier|public
name|int
name|getGIDForCID
parameter_list|(
name|int
name|cid
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isCIDFont
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not a CIDFont"
argument_list|)
throw|;
block|}
name|Integer
name|gid
init|=
name|sidOrCidToGid
operator|.
name|get
argument_list|(
name|cid
argument_list|)
decl_stmt|;
if|if
condition|(
name|gid
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|gid
return|;
block|}
comment|/**      * Returns the SID for a given PostScript name, you would think this is not needed,      * but some fonts have glyphs beyond their encoding with charset SID names.      *      * @param name PostScript glyph name      * @return SID      */
name|int
name|getSID
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|isCIDFont
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not a Type 1-equivalent font"
argument_list|)
throw|;
block|}
name|Integer
name|sid
init|=
name|nameToSid
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|sid
operator|==
literal|null
condition|)
block|{
return|return
literal|0
return|;
block|}
return|return
name|sid
return|;
block|}
comment|/**      * Returns the PostScript glyph name for the given GID.      *      * @param gid GID      * @return PostScript glyph name      */
specifier|public
name|String
name|getNameForGID
parameter_list|(
name|int
name|gid
parameter_list|)
block|{
if|if
condition|(
name|isCIDFont
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not a Type 1-equivalent font"
argument_list|)
throw|;
block|}
return|return
name|gidToName
operator|.
name|get
argument_list|(
name|gid
argument_list|)
return|;
block|}
comment|/**      * Returns the CID for the given GID.      *      * @param gid GID      * @return CID      */
specifier|public
name|int
name|getCIDForGID
parameter_list|(
name|int
name|gid
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isCIDFont
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not a CIDFont"
argument_list|)
throw|;
block|}
name|Integer
name|cid
init|=
name|gidToCid
operator|.
name|get
argument_list|(
name|gid
argument_list|)
decl_stmt|;
if|if
condition|(
name|cid
operator|!=
literal|null
condition|)
block|{
return|return
name|cid
return|;
block|}
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

