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
name|IOException
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
name|pdmodel
operator|.
name|font
operator|.
name|encoding
operator|.
name|DictionaryEncoding
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
name|font
operator|.
name|encoding
operator|.
name|Encoding
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
name|font
operator|.
name|encoding
operator|.
name|GlyphList
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
name|font
operator|.
name|encoding
operator|.
name|MacRomanEncoding
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
name|font
operator|.
name|encoding
operator|.
name|StandardEncoding
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
name|font
operator|.
name|encoding
operator|.
name|WinAnsiEncoding
import|;
end_import

begin_comment
comment|/**  * A simple font. Simple fonts use a PostScript encoding vector.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|PDSimpleFont
extends|extends
name|PDFont
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
name|PDSimpleFont
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Encoding
name|encoding
decl_stmt|;
specifier|protected
name|GlyphList
name|glyphList
decl_stmt|;
specifier|private
name|Boolean
name|isSymbolic
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|Integer
argument_list|>
name|noUnicode
init|=
operator|new
name|HashSet
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
comment|// for logging
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|invertedEncoding
decl_stmt|;
comment|// for writing
comment|/**      * Constructor for embedding.      */
name|PDSimpleFont
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor for Standard 14.      */
name|PDSimpleFont
parameter_list|(
name|String
name|baseFont
parameter_list|)
block|{
name|super
argument_list|(
name|baseFont
argument_list|)
expr_stmt|;
name|this
operator|.
name|encoding
operator|=
name|WinAnsiEncoding
operator|.
name|INSTANCE
expr_stmt|;
comment|// assign the glyph list based on the font
if|if
condition|(
literal|"ZapfDingbats"
operator|.
name|equals
argument_list|(
name|baseFont
argument_list|)
condition|)
block|{
name|glyphList
operator|=
name|GlyphList
operator|.
name|getZapfDingbats
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|glyphList
operator|=
name|GlyphList
operator|.
name|getAdobeGlyphList
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Constructor.      *      * @param fontDictionary Font dictionary.      */
name|PDSimpleFont
parameter_list|(
name|COSDictionary
name|fontDictionary
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|fontDictionary
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reads the Encoding from the Font dictionary or the embedded or substituted font file.      * Must be called at the end of any subclass constructors.      *      * @throws IOException if the font file could not be read      */
specifier|protected
specifier|final
name|void
name|readEncoding
parameter_list|()
throws|throws
name|IOException
block|{
name|COSBase
name|encoding
init|=
name|dict
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|encoding
operator|instanceof
name|COSName
condition|)
block|{
name|COSName
name|encodingName
init|=
operator|(
name|COSName
operator|)
name|encoding
decl_stmt|;
name|this
operator|.
name|encoding
operator|=
name|Encoding
operator|.
name|getInstance
argument_list|(
name|encodingName
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|encoding
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Unknown encoding: "
operator|+
name|encodingName
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|encoding
operator|=
name|readEncodingFromFont
argument_list|()
expr_stmt|;
comment|// fallback
block|}
block|}
elseif|else
if|if
condition|(
name|encoding
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|encodingDict
init|=
operator|(
name|COSDictionary
operator|)
name|encoding
decl_stmt|;
name|Encoding
name|builtIn
init|=
literal|null
decl_stmt|;
name|Boolean
name|symbolic
init|=
name|getSymbolicFlag
argument_list|()
decl_stmt|;
name|boolean
name|isFlaggedAsSymbolic
init|=
name|symbolic
operator|!=
literal|null
operator|&&
name|symbolic
decl_stmt|;
if|if
condition|(
operator|!
name|encodingDict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|BASE_ENCODING
argument_list|)
operator|&&
name|isFlaggedAsSymbolic
condition|)
block|{
name|builtIn
operator|=
name|readEncodingFromFont
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|symbolic
operator|==
literal|null
condition|)
block|{
name|symbolic
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|builtIn
operator|==
literal|null
operator|&&
operator|!
name|encodingDict
operator|.
name|containsKey
argument_list|(
name|COSName
operator|.
name|BASE_ENCODING
argument_list|)
operator|&&
name|symbolic
condition|)
block|{
comment|// TTF built-in encoding is handled by PDTrueTypeFont#codeToGID
name|this
operator|.
name|encoding
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|encoding
operator|=
operator|new
name|DictionaryEncoding
argument_list|(
name|encodingDict
argument_list|,
operator|!
name|symbolic
argument_list|,
name|builtIn
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|this
operator|.
name|encoding
operator|=
name|readEncodingFromFont
argument_list|()
expr_stmt|;
block|}
comment|// TTFs may have null encoding, but if it's non-symbolic then we have Standard Encoding
if|if
condition|(
name|this
operator|.
name|encoding
operator|==
literal|null
operator|&&
name|getSymbolicFlag
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|getSymbolicFlag
argument_list|()
condition|)
block|{
name|this
operator|.
name|encoding
operator|=
name|StandardEncoding
operator|.
name|INSTANCE
expr_stmt|;
block|}
comment|// normalise the standard 14 name, e.g "Symbol,Italic" -> "Symbol"
name|String
name|standard14Name
init|=
name|Standard14Fonts
operator|.
name|getMappedFontName
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
comment|// TTFs may have null encoding, but if it's standard 14 then we know it's Standard Encoding
if|if
condition|(
name|this
operator|.
name|encoding
operator|==
literal|null
operator|&&
name|isStandard14
argument_list|()
operator|&&
operator|!
name|standard14Name
operator|.
name|equals
argument_list|(
literal|"Symbol"
argument_list|)
operator|&&
operator|!
name|standard14Name
operator|.
name|equals
argument_list|(
literal|"ZapfDingbats"
argument_list|)
condition|)
block|{
name|this
operator|.
name|encoding
operator|=
name|StandardEncoding
operator|.
name|INSTANCE
expr_stmt|;
block|}
comment|// assign the glyph list based on the font
if|if
condition|(
literal|"ZapfDingbats"
operator|.
name|equals
argument_list|(
name|standard14Name
argument_list|)
condition|)
block|{
name|glyphList
operator|=
name|GlyphList
operator|.
name|getZapfDingbats
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// StandardEncoding and Symbol are in the AGL
name|glyphList
operator|=
name|GlyphList
operator|.
name|getAdobeGlyphList
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Called by readEncoding() if the encoding needs to be extracted from the font file.      *      * @throws IOException if the font file could not be read.      */
specifier|protected
specifier|abstract
name|Encoding
name|readEncodingFromFont
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the Encoding vector.      */
specifier|public
name|Encoding
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
comment|/**      * Returns the Encoding vector.      */
specifier|public
name|GlyphList
name|getGlyphList
parameter_list|()
block|{
return|return
name|glyphList
return|;
block|}
comment|/**      * Inverts the font's Encoding. Any duplicate (Name -> Code) mappings will be lost.      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|getInvertedEncoding
parameter_list|()
block|{
if|if
condition|(
name|invertedEncoding
operator|!=
literal|null
condition|)
block|{
return|return
name|invertedEncoding
return|;
block|}
name|invertedEncoding
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|codeToName
init|=
name|encoding
operator|.
name|getCodeToNameMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|codeToName
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|invertedEncoding
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|invertedEncoding
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|invertedEncoding
return|;
block|}
comment|/**      * Returns true the font is a symbolic (that is, it does not use the Adobe Standard Roman      * character set).      */
specifier|public
specifier|final
name|boolean
name|isSymbolic
parameter_list|()
block|{
if|if
condition|(
name|isSymbolic
operator|==
literal|null
condition|)
block|{
name|Boolean
name|result
init|=
name|isFontSymbolic
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|isSymbolic
operator|=
name|result
expr_stmt|;
block|}
else|else
block|{
comment|// unless we can prove that the font is symbolic, we assume that it is not
name|isSymbolic
operator|=
literal|true
expr_stmt|;
block|}
block|}
return|return
name|isSymbolic
return|;
block|}
comment|/**      * Internal implementation of isSymbolic, allowing for the fact that the result may be      * indeterminate.      */
specifier|protected
name|Boolean
name|isFontSymbolic
parameter_list|()
block|{
name|Boolean
name|result
init|=
name|getSymbolicFlag
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
return|return
name|result
return|;
block|}
elseif|else
if|if
condition|(
name|isStandard14
argument_list|()
condition|)
block|{
name|String
name|mappedName
init|=
name|Standard14Fonts
operator|.
name|getMappedFontName
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|mappedName
operator|.
name|equals
argument_list|(
literal|"Symbol"
argument_list|)
operator|||
name|mappedName
operator|.
name|equals
argument_list|(
literal|"ZapfDingbats"
argument_list|)
return|;
block|}
else|else
block|{
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
comment|// sanity check, should never happen
if|if
condition|(
operator|!
operator|(
name|this
operator|instanceof
name|PDTrueTypeFont
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"PDFBox bug: encoding should not be null!"
argument_list|)
throw|;
block|}
comment|// TTF without its non-symbolic flag set must be symbolic
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|encoding
operator|instanceof
name|WinAnsiEncoding
operator|||
name|encoding
operator|instanceof
name|MacRomanEncoding
operator|||
name|encoding
operator|instanceof
name|StandardEncoding
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|encoding
operator|instanceof
name|DictionaryEncoding
condition|)
block|{
comment|// each name in Differences array must also be in the latin character set
for|for
control|(
name|String
name|name
range|:
operator|(
operator|(
name|DictionaryEncoding
operator|)
name|encoding
operator|)
operator|.
name|getDifferences
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
literal|".notdef"
argument_list|)
condition|)
block|{
comment|// skip
block|}
elseif|else
if|if
condition|(
operator|!
operator|(
name|WinAnsiEncoding
operator|.
name|INSTANCE
operator|.
name|contains
argument_list|(
name|name
argument_list|)
operator|&&
name|MacRomanEncoding
operator|.
name|INSTANCE
operator|.
name|contains
argument_list|(
name|name
argument_list|)
operator|&&
name|StandardEncoding
operator|.
name|INSTANCE
operator|.
name|contains
argument_list|(
name|name
argument_list|)
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
else|else
block|{
comment|// we don't know
return|return
literal|null
return|;
block|}
block|}
block|}
comment|/**      * Returns the value of the symbolic flag,  allowing for the fact that the result may be      * indeterminate.      */
specifier|protected
specifier|final
name|Boolean
name|getSymbolicFlag
parameter_list|()
block|{
if|if
condition|(
name|getFontDescriptor
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// fixme: isSymbolic() defaults to false if the flag is missing so we can't trust this
return|return
name|getFontDescriptor
argument_list|()
operator|.
name|isSymbolic
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toUnicode
parameter_list|(
name|int
name|code
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toUnicode
argument_list|(
name|code
argument_list|,
name|GlyphList
operator|.
name|getAdobeGlyphList
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toUnicode
parameter_list|(
name|int
name|code
parameter_list|,
name|GlyphList
name|customGlyphList
parameter_list|)
throws|throws
name|IOException
block|{
comment|// allow the glyph list to be overridden for the purpose of extracting Unicode
comment|// we only do this when the font's glyph list is the AGL, to avoid breaking Zapf Dingbats
name|GlyphList
name|unicodeGlyphList
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|glyphList
operator|==
name|GlyphList
operator|.
name|getAdobeGlyphList
argument_list|()
condition|)
block|{
name|unicodeGlyphList
operator|=
name|customGlyphList
expr_stmt|;
block|}
else|else
block|{
name|unicodeGlyphList
operator|=
name|this
operator|.
name|glyphList
expr_stmt|;
block|}
comment|// first try to use a ToUnicode CMap
name|String
name|unicode
init|=
name|super
operator|.
name|toUnicode
argument_list|(
name|code
argument_list|)
decl_stmt|;
if|if
condition|(
name|unicode
operator|!=
literal|null
condition|)
block|{
return|return
name|unicode
return|;
block|}
comment|// if the font is a "simple font" and uses MacRoman/MacExpert/WinAnsi[Encoding]
comment|// or has Differences with names from only Adobe Standard and/or Symbol, then:
comment|//
comment|//    a) Map the character codes to names
comment|//    b) Look up the name in the Adobe Glyph List to obtain the Unicode value
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|encoding
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|encoding
operator|.
name|getName
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|unicode
operator|=
name|unicodeGlyphList
operator|.
name|toUnicode
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|unicode
operator|!=
literal|null
condition|)
block|{
return|return
name|unicode
return|;
block|}
block|}
comment|// if no value has been produced, there is no way to obtain Unicode for the character.
if|if
condition|(
name|LOG
operator|.
name|isWarnEnabled
argument_list|()
operator|&&
operator|!
name|noUnicode
operator|.
name|contains
argument_list|(
name|code
argument_list|)
condition|)
block|{
comment|// we keep track of which warnings have been issued, so we don't log multiple times
name|noUnicode
operator|.
name|add
argument_list|(
name|code
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No Unicode mapping for "
operator|+
name|name
operator|+
literal|" ("
operator|+
name|code
operator|+
literal|") in font "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No Unicode mapping for character code "
operator|+
name|code
operator|+
literal|" in font "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isVertical
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|protected
specifier|final
name|float
name|getStandard14Width
parameter_list|(
name|int
name|code
parameter_list|)
block|{
if|if
condition|(
name|getStandard14AFM
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|nameInAFM
init|=
name|getEncoding
argument_list|()
operator|.
name|getName
argument_list|(
name|code
argument_list|)
decl_stmt|;
comment|// the Adobe AFMs don't include .notdef, but Acrobat uses 250, test with PDFBOX-2334
if|if
condition|(
name|nameInAFM
operator|.
name|equals
argument_list|(
literal|".notdef"
argument_list|)
condition|)
block|{
return|return
literal|250f
return|;
block|}
return|return
name|getStandard14AFM
argument_list|()
operator|.
name|getCharacterWidth
argument_list|(
name|nameInAFM
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No AFM"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isStandard14
parameter_list|()
block|{
comment|// this logic is based on Acrobat's behaviour, see see PDFBOX-2372
comment|// the Encoding entry cannot have Differences if we want "standard 14" font handling
if|if
condition|(
name|getEncoding
argument_list|()
operator|instanceof
name|DictionaryEncoding
condition|)
block|{
name|DictionaryEncoding
name|dictionary
init|=
operator|(
name|DictionaryEncoding
operator|)
name|getEncoding
argument_list|()
decl_stmt|;
if|if
condition|(
name|dictionary
operator|.
name|getDifferences
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// we also require that the differences are actually different, see PDFBOX-1900 with
comment|// the file from PDFBOX-2192 on Windows
name|Encoding
name|baseEncoding
init|=
name|dictionary
operator|.
name|getBaseEncoding
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|dictionary
operator|.
name|getDifferences
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
name|baseEncoding
operator|.
name|getName
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
return|return
name|super
operator|.
name|isStandard14
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addToSubset
parameter_list|(
name|int
name|codePoint
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|subset
parameter_list|()
throws|throws
name|IOException
block|{
comment|// only TTF subsetting via PDType0Font is currently supported
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|willBeSubset
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

