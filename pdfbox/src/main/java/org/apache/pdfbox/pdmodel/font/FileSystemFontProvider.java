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
name|font
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|fontbox
operator|.
name|FontBoxFont
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cff
operator|.
name|CFFCIDFont
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|cff
operator|.
name|CFFFont
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|NamingTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|OTFParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|OpenTypeFont
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|TTFParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|TrueTypeCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|ttf
operator|.
name|TrueTypeFont
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|type1
operator|.
name|Type1Font
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|fontbox
operator|.
name|util
operator|.
name|autodetect
operator|.
name|FontFileFinder
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
name|util
operator|.
name|Charsets
import|;
end_import

begin_comment
comment|/**  * A FontProvider which searches for fonts on the local filesystem.  *  * @author John Hewson  */
end_comment

begin_class
specifier|final
class|class
name|FileSystemFontProvider
extends|extends
name|FontProvider
block|{
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
name|FileSystemFontProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|FSFontInfo
argument_list|>
name|fontInfoList
init|=
operator|new
name|ArrayList
argument_list|<
name|FSFontInfo
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|FontCache
name|cache
decl_stmt|;
specifier|private
class|class
name|FSFontInfo
extends|extends
name|FontInfo
block|{
specifier|private
specifier|final
name|String
name|postScriptName
decl_stmt|;
specifier|private
specifier|final
name|FontFormat
name|format
decl_stmt|;
specifier|private
specifier|final
name|PDCIDSystemInfo
name|cidSystemInfo
decl_stmt|;
specifier|private
specifier|final
name|int
name|usWeightClass
decl_stmt|;
specifier|private
specifier|final
name|int
name|sFamilyClass
decl_stmt|;
specifier|private
specifier|final
name|int
name|ulCodePageRange1
decl_stmt|;
specifier|private
specifier|final
name|int
name|ulCodePageRange2
decl_stmt|;
specifier|private
specifier|final
name|int
name|macStyle
decl_stmt|;
specifier|private
specifier|final
name|PDPanoseClassification
name|panose
decl_stmt|;
specifier|private
specifier|final
name|File
name|file
decl_stmt|;
specifier|private
name|FSFontInfo
parameter_list|(
name|File
name|file
parameter_list|,
name|FontFormat
name|format
parameter_list|,
name|String
name|postScriptName
parameter_list|,
name|PDCIDSystemInfo
name|cidSystemInfo
parameter_list|,
name|int
name|usWeightClass
parameter_list|,
name|int
name|sFamilyClass
parameter_list|,
name|int
name|ulCodePageRange1
parameter_list|,
name|int
name|ulCodePageRange2
parameter_list|,
name|int
name|macStyle
parameter_list|,
name|byte
index|[]
name|panose
parameter_list|)
block|{
name|this
operator|.
name|file
operator|=
name|file
expr_stmt|;
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
name|this
operator|.
name|postScriptName
operator|=
name|postScriptName
expr_stmt|;
name|this
operator|.
name|cidSystemInfo
operator|=
name|cidSystemInfo
expr_stmt|;
name|this
operator|.
name|usWeightClass
operator|=
name|usWeightClass
expr_stmt|;
name|this
operator|.
name|sFamilyClass
operator|=
name|sFamilyClass
expr_stmt|;
name|this
operator|.
name|ulCodePageRange1
operator|=
name|ulCodePageRange1
expr_stmt|;
name|this
operator|.
name|ulCodePageRange2
operator|=
name|ulCodePageRange2
expr_stmt|;
name|this
operator|.
name|macStyle
operator|=
name|macStyle
expr_stmt|;
name|this
operator|.
name|panose
operator|=
name|panose
operator|!=
literal|null
condition|?
operator|new
name|PDPanoseClassification
argument_list|(
name|panose
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPostScriptName
parameter_list|()
block|{
return|return
name|postScriptName
return|;
block|}
annotation|@
name|Override
specifier|public
name|FontFormat
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDCIDSystemInfo
name|getCIDSystemInfo
parameter_list|()
block|{
return|return
name|cidSystemInfo
return|;
block|}
annotation|@
name|Override
specifier|public
name|FontBoxFont
name|getFont
parameter_list|()
block|{
name|FontBoxFont
name|cached
init|=
name|cache
operator|.
name|getFont
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|cached
operator|!=
literal|null
condition|)
block|{
return|return
name|cached
return|;
block|}
else|else
block|{
name|FontBoxFont
name|font
decl_stmt|;
switch|switch
condition|(
name|format
condition|)
block|{
case|case
name|PFB
case|:
name|font
operator|=
name|getType1Font
argument_list|(
name|postScriptName
argument_list|,
name|file
argument_list|)
expr_stmt|;
break|break;
case|case
name|TTF
case|:
name|font
operator|=
name|getTrueTypeFont
argument_list|(
name|postScriptName
argument_list|,
name|file
argument_list|)
expr_stmt|;
break|break;
case|case
name|OTF
case|:
name|font
operator|=
name|getOTFFont
argument_list|(
name|postScriptName
argument_list|,
name|file
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"can't happen"
argument_list|)
throw|;
block|}
name|cache
operator|.
name|addFont
argument_list|(
name|this
argument_list|,
name|font
argument_list|)
expr_stmt|;
return|return
name|font
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|int
name|getFamilyClass
parameter_list|()
block|{
return|return
name|sFamilyClass
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getWeightClass
parameter_list|()
block|{
return|return
name|usWeightClass
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getCodePageRange1
parameter_list|()
block|{
return|return
name|ulCodePageRange1
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getCodePageRange2
parameter_list|()
block|{
return|return
name|ulCodePageRange2
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|getMacStyle
parameter_list|()
block|{
return|return
name|macStyle
return|;
block|}
annotation|@
name|Override
specifier|public
name|PDPanoseClassification
name|getPanose
parameter_list|()
block|{
return|return
name|panose
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
literal|" "
operator|+
name|file
return|;
block|}
block|}
comment|/**      * Constructor.      */
name|FileSystemFontProvider
parameter_list|(
name|FontCache
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Will search the local system for fonts"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|File
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|FontFileFinder
name|fontFileFinder
init|=
operator|new
name|FontFileFinder
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|URI
argument_list|>
name|fonts
init|=
name|fontFileFinder
operator|.
name|find
argument_list|()
decl_stmt|;
for|for
control|(
name|URI
name|font
range|:
name|fonts
control|)
block|{
name|files
operator|.
name|add
argument_list|(
operator|new
name|File
argument_list|(
name|font
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Found "
operator|+
name|files
operator|.
name|size
argument_list|()
operator|+
literal|" fonts on the local system"
argument_list|)
expr_stmt|;
block|}
comment|// todo: loading all of these fonts is slow, can we cache this?
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
try|try
block|{
if|if
condition|(
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".ttf"
argument_list|)
operator|||
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".otf"
argument_list|)
condition|)
block|{
name|addTrueTypeFont
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".ttc"
argument_list|)
operator|||
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".otc"
argument_list|)
condition|)
block|{
name|addTrueTypeCollection
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|file
operator|.
name|getPath
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".pfb"
argument_list|)
condition|)
block|{
name|addType1Font
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Error parsing font "
operator|+
name|file
operator|.
name|getPath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Adds a TTC or OTC to the file cache. To reduce memory, the parsed font is not cached.      */
specifier|private
name|void
name|addTrueTypeCollection
parameter_list|(
name|File
name|ttcFile
parameter_list|)
throws|throws
name|IOException
block|{
name|TrueTypeCollection
name|ttc
init|=
literal|null
decl_stmt|;
try|try
block|{
name|ttc
operator|=
operator|new
name|TrueTypeCollection
argument_list|(
name|ttcFile
argument_list|)
expr_stmt|;
for|for
control|(
name|TrueTypeFont
name|ttf
range|:
name|ttc
operator|.
name|getFonts
argument_list|()
control|)
block|{
name|addTrueTypeFontImpl
argument_list|(
name|ttf
argument_list|,
name|ttcFile
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
comment|// TTF parser is buggy
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not load font file: "
operator|+
name|ttcFile
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not load font file: "
operator|+
name|ttcFile
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|ttc
operator|!=
literal|null
condition|)
block|{
name|ttc
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Adds an OTF or TTF font to the file cache. To reduce memory, the parsed font is not cached.      */
specifier|private
name|void
name|addTrueTypeFont
parameter_list|(
name|File
name|ttfFile
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
if|if
condition|(
name|ttfFile
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".otf"
argument_list|)
condition|)
block|{
name|OTFParser
name|parser
init|=
operator|new
name|OTFParser
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|OpenTypeFont
name|otf
init|=
name|parser
operator|.
name|parse
argument_list|(
name|ttfFile
argument_list|)
decl_stmt|;
name|addTrueTypeFontImpl
argument_list|(
name|otf
argument_list|,
name|ttfFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|TTFParser
name|parser
init|=
operator|new
name|TTFParser
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|TrueTypeFont
name|ttf
init|=
name|parser
operator|.
name|parse
argument_list|(
name|ttfFile
argument_list|)
decl_stmt|;
name|addTrueTypeFontImpl
argument_list|(
name|ttf
argument_list|,
name|ttfFile
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
comment|// TTF parser is buggy
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not load font file: "
operator|+
name|ttfFile
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not load font file: "
operator|+
name|ttfFile
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds an OTF or TTF font to the file cache. To reduce memory, the parsed font is not cached.      */
specifier|private
name|void
name|addTrueTypeFontImpl
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
comment|// read PostScript name, if any
if|if
condition|(
name|ttf
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|sFamilyClass
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|usWeightClass
init|=
operator|-
literal|1
decl_stmt|;
name|int
name|ulCodePageRange1
init|=
literal|0
decl_stmt|;
name|int
name|ulCodePageRange2
init|=
literal|0
decl_stmt|;
name|byte
index|[]
name|panose
init|=
literal|null
decl_stmt|;
comment|// Apple's AAT fonts don't have an OS/2 table
if|if
condition|(
name|ttf
operator|.
name|getOS2Windows
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sFamilyClass
operator|=
name|ttf
operator|.
name|getOS2Windows
argument_list|()
operator|.
name|getFamilyClass
argument_list|()
expr_stmt|;
name|usWeightClass
operator|=
name|ttf
operator|.
name|getOS2Windows
argument_list|()
operator|.
name|getWeightClass
argument_list|()
expr_stmt|;
name|ulCodePageRange1
operator|=
operator|(
name|int
operator|)
name|ttf
operator|.
name|getOS2Windows
argument_list|()
operator|.
name|getCodePageRange1
argument_list|()
expr_stmt|;
name|ulCodePageRange2
operator|=
operator|(
name|int
operator|)
name|ttf
operator|.
name|getOS2Windows
argument_list|()
operator|.
name|getCodePageRange2
argument_list|()
expr_stmt|;
name|panose
operator|=
name|ttf
operator|.
name|getOS2Windows
argument_list|()
operator|.
name|getPanose
argument_list|()
expr_stmt|;
block|}
comment|// ignore bitmap fonts
if|if
condition|(
name|ttf
operator|.
name|getHeader
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|int
name|macStyle
init|=
name|ttf
operator|.
name|getHeader
argument_list|()
operator|.
name|getMacStyle
argument_list|()
decl_stmt|;
name|String
name|format
decl_stmt|;
if|if
condition|(
name|ttf
operator|instanceof
name|OpenTypeFont
operator|&&
operator|(
operator|(
name|OpenTypeFont
operator|)
name|ttf
operator|)
operator|.
name|isPostScript
argument_list|()
condition|)
block|{
name|format
operator|=
literal|"OTF"
expr_stmt|;
name|CFFFont
name|cff
init|=
operator|(
operator|(
name|OpenTypeFont
operator|)
name|ttf
operator|)
operator|.
name|getCFF
argument_list|()
operator|.
name|getFont
argument_list|()
decl_stmt|;
name|PDCIDSystemInfo
name|ros
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cff
operator|instanceof
name|CFFCIDFont
condition|)
block|{
name|CFFCIDFont
name|cidFont
init|=
operator|(
name|CFFCIDFont
operator|)
name|cff
decl_stmt|;
name|String
name|registry
init|=
name|cidFont
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|String
name|ordering
init|=
name|cidFont
operator|.
name|getOrdering
argument_list|()
decl_stmt|;
name|int
name|supplement
init|=
name|cidFont
operator|.
name|getSupplement
argument_list|()
decl_stmt|;
name|ros
operator|=
operator|new
name|PDCIDSystemInfo
argument_list|(
name|registry
argument_list|,
name|ordering
argument_list|,
name|supplement
argument_list|)
expr_stmt|;
block|}
name|fontInfoList
operator|.
name|add
argument_list|(
operator|new
name|FSFontInfo
argument_list|(
name|file
argument_list|,
name|FontFormat
operator|.
name|OTF
argument_list|,
name|ttf
operator|.
name|getName
argument_list|()
argument_list|,
name|ros
argument_list|,
name|usWeightClass
argument_list|,
name|sFamilyClass
argument_list|,
name|ulCodePageRange1
argument_list|,
name|ulCodePageRange2
argument_list|,
name|macStyle
argument_list|,
name|panose
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PDCIDSystemInfo
name|ros
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ttf
operator|.
name|getTableMap
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"gcid"
argument_list|)
condition|)
block|{
comment|// Apple's AAT fonts have a "gcid" table with CID info
name|byte
index|[]
name|bytes
init|=
name|ttf
operator|.
name|getTableBytes
argument_list|(
name|ttf
operator|.
name|getTableMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"gcid"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|registryName
init|=
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
literal|10
argument_list|,
literal|64
argument_list|,
name|Charsets
operator|.
name|US_ASCII
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|orderName
init|=
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
literal|76
argument_list|,
literal|64
argument_list|,
name|Charsets
operator|.
name|US_ASCII
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
name|int
name|supplementVersion
init|=
name|bytes
index|[
literal|140
index|]
operator|<<
literal|8
operator|&
name|bytes
index|[
literal|141
index|]
decl_stmt|;
name|ros
operator|=
operator|new
name|PDCIDSystemInfo
argument_list|(
name|registryName
argument_list|,
name|orderName
argument_list|,
name|supplementVersion
argument_list|)
expr_stmt|;
block|}
name|format
operator|=
literal|"TTF"
expr_stmt|;
name|fontInfoList
operator|.
name|add
argument_list|(
operator|new
name|FSFontInfo
argument_list|(
name|file
argument_list|,
name|FontFormat
operator|.
name|TTF
argument_list|,
name|ttf
operator|.
name|getName
argument_list|()
argument_list|,
name|ros
argument_list|,
name|usWeightClass
argument_list|,
name|sFamilyClass
argument_list|,
name|ulCodePageRange1
argument_list|,
name|ulCodePageRange2
argument_list|,
name|macStyle
argument_list|,
name|panose
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|NamingTable
name|name
init|=
name|ttf
operator|.
name|getNaming
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
name|format
operator|+
literal|": '"
operator|+
name|name
operator|.
name|getPostScriptName
argument_list|()
operator|+
literal|"' / '"
operator|+
name|name
operator|.
name|getFontFamily
argument_list|()
operator|+
literal|"' / '"
operator|+
name|name
operator|.
name|getFontSubFamily
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Missing 'name' entry for PostScript name in font "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not load font file: "
operator|+
name|file
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|ttf
operator|!=
literal|null
condition|)
block|{
name|ttf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Adds a Type 1 font to the file cache. To reduce memory, the parsed font is not cached.      */
specifier|private
name|void
name|addType1Font
parameter_list|(
name|File
name|pfbFile
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|input
init|=
operator|new
name|FileInputStream
argument_list|(
name|pfbFile
argument_list|)
decl_stmt|;
try|try
block|{
name|Type1Font
name|type1
init|=
name|Type1Font
operator|.
name|createWithPFB
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|fontInfoList
operator|.
name|add
argument_list|(
operator|new
name|FSFontInfo
argument_list|(
name|pfbFile
argument_list|,
name|FontFormat
operator|.
name|PFB
argument_list|,
name|type1
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|,
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
operator|-
literal|1
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"PFB: '"
operator|+
name|type1
operator|.
name|getName
argument_list|()
operator|+
literal|"' / '"
operator|+
name|type1
operator|.
name|getFamilyName
argument_list|()
operator|+
literal|"' / '"
operator|+
name|type1
operator|.
name|getWeight
argument_list|()
operator|+
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not load font file: "
operator|+
name|pfbFile
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|TrueTypeFont
name|getTrueTypeFont
parameter_list|(
name|String
name|postScriptName
parameter_list|,
name|File
name|file
parameter_list|)
block|{
try|try
block|{
name|TrueTypeFont
name|ttf
init|=
name|readTrueTypeFont
argument_list|(
name|postScriptName
argument_list|,
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loaded "
operator|+
name|postScriptName
operator|+
literal|" from "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
return|return
name|ttf
return|;
block|}
catch|catch
parameter_list|(
name|NullPointerException
name|e
parameter_list|)
comment|// TTF parser is buggy
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not load font file: "
operator|+
name|file
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not load font file: "
operator|+
name|file
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|TrueTypeFont
name|readTrueTypeFont
parameter_list|(
name|String
name|postScriptName
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|file
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".ttc"
argument_list|)
condition|)
block|{
name|TrueTypeCollection
name|ttc
init|=
operator|new
name|TrueTypeCollection
argument_list|(
name|file
argument_list|)
decl_stmt|;
for|for
control|(
name|TrueTypeFont
name|ttf
range|:
name|ttc
operator|.
name|getFonts
argument_list|()
control|)
block|{
if|if
condition|(
name|ttf
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|postScriptName
argument_list|)
condition|)
block|{
return|return
name|ttf
return|;
block|}
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Font "
operator|+
name|postScriptName
operator|+
literal|" not found in "
operator|+
name|file
argument_list|)
throw|;
block|}
else|else
block|{
name|TTFParser
name|ttfParser
init|=
operator|new
name|TTFParser
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|ttfParser
operator|.
name|parse
argument_list|(
name|file
argument_list|)
return|;
block|}
block|}
specifier|private
name|OpenTypeFont
name|getOTFFont
parameter_list|(
name|String
name|postScriptName
parameter_list|,
name|File
name|file
parameter_list|)
block|{
try|try
block|{
comment|// todo JH: we don't yet support loading CFF fonts from OTC collections 
name|OTFParser
name|parser
init|=
operator|new
name|OTFParser
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|OpenTypeFont
name|otf
init|=
name|parser
operator|.
name|parse
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loaded "
operator|+
name|postScriptName
operator|+
literal|" from "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
return|return
name|otf
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not load font file: "
operator|+
name|file
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Type1Font
name|getType1Font
parameter_list|(
name|String
name|postScriptName
parameter_list|,
name|File
name|file
parameter_list|)
block|{
name|InputStream
name|input
init|=
literal|null
decl_stmt|;
try|try
block|{
name|input
operator|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|Type1Font
name|type1
init|=
name|Type1Font
operator|.
name|createWithPFB
argument_list|(
name|input
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loaded "
operator|+
name|postScriptName
operator|+
literal|" from "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
return|return
name|type1
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Could not load font file: "
operator|+
name|file
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toDebugString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|FSFontInfo
name|info
range|:
name|fontInfoList
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|info
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|info
operator|.
name|getPostScriptName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|info
operator|.
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|FontInfo
argument_list|>
name|getFontInfo
parameter_list|()
block|{
return|return
name|fontInfoList
return|;
block|}
block|}
end_class

end_unit

