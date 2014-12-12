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
name|URL
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|cff
operator|.
name|CFFParser
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
name|CFFType1Font
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
name|Type1Equivalent
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
name|pdfbox
operator|.
name|io
operator|.
name|IOUtils
import|;
end_import

begin_comment
comment|/**  * External font service, locates non-embedded fonts via a pluggable FontProvider.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ExternalFonts
block|{
specifier|private
name|ExternalFonts
parameter_list|()
block|{}
comment|// lazy thread safe singleton
specifier|private
specifier|static
class|class
name|DefaultFontProvider
block|{
specifier|private
specifier|static
specifier|final
name|FontProvider
name|INSTANCE
init|=
operator|new
name|FileSystemFontProvider
argument_list|()
decl_stmt|;
block|}
specifier|private
specifier|static
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ExternalFonts
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|FontProvider
name|fontProvider
decl_stmt|;
comment|/** fallback fonts, used as as a last resort */
specifier|private
specifier|static
specifier|final
name|TrueTypeFont
name|ttfFallbackFont
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|CFFCIDFont
name|cidFallbackFont
decl_stmt|;
static|static
block|{
try|try
block|{
comment|// ttf
name|String
name|ttfName
init|=
literal|"org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf"
decl_stmt|;
name|URL
name|url
init|=
name|ExternalFonts
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|ttfName
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error loading resource: "
operator|+
name|ttfName
argument_list|)
throw|;
block|}
name|InputStream
name|ttfStream
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|TTFParser
name|ttfParser
init|=
operator|new
name|TTFParser
argument_list|()
decl_stmt|;
name|ttfFallbackFont
operator|=
name|ttfParser
operator|.
name|parse
argument_list|(
name|ttfStream
argument_list|)
expr_stmt|;
comment|// cff
name|String
name|cffName
init|=
literal|"org/apache/pdfbox/resources/otf/AdobeBlank.otf"
decl_stmt|;
name|url
operator|=
name|ExternalFonts
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|cffName
argument_list|)
expr_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error loading resource: "
operator|+
name|ttfName
argument_list|)
throw|;
block|}
name|InputStream
name|cffStream
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|cffStream
argument_list|)
decl_stmt|;
name|CFFParser
name|cffParser
init|=
operator|new
name|CFFParser
argument_list|()
decl_stmt|;
name|cidFallbackFont
operator|=
operator|(
name|CFFCIDFont
operator|)
name|cffParser
operator|.
name|parse
argument_list|(
name|bytes
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Sets the font service provider.      */
specifier|public
specifier|static
name|void
name|setProvider
parameter_list|(
name|FontProvider
name|fontProvider
parameter_list|)
block|{
name|ExternalFonts
operator|.
name|fontProvider
operator|=
name|fontProvider
expr_stmt|;
block|}
comment|/**      * Returns the font service provider. Defaults to using FileSystemFontProvider.      */
specifier|public
specifier|static
name|FontProvider
name|getProvider
parameter_list|()
block|{
if|if
condition|(
name|fontProvider
operator|==
literal|null
condition|)
block|{
name|fontProvider
operator|=
name|DefaultFontProvider
operator|.
name|INSTANCE
expr_stmt|;
block|}
return|return
name|fontProvider
return|;
block|}
comment|/** Map of PostScript name substitutes, in priority order. */
specifier|private
specifier|final
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|substitutes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
comment|// substitutes for standard 14 fonts
name|substitutes
operator|.
name|put
argument_list|(
literal|"Courier"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"CourierNew"
argument_list|,
literal|"CourierNewPSMT"
argument_list|,
literal|"LiberationMono"
argument_list|,
literal|"NimbusMonL-Regu"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Courier-Bold"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"CourierNewPS-BoldMT"
argument_list|,
literal|"CourierNew-Bold"
argument_list|,
literal|"LiberationMono-Bold"
argument_list|,
literal|"NimbusMonL-Bold"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Courier-Oblique"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"CourierNewPS-ItalicMT"
argument_list|,
literal|"CourierNew-Italic"
argument_list|,
literal|"LiberationMono-Italic"
argument_list|,
literal|"NimbusMonL-ReguObli"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Courier-BoldOblique"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"CourierNewPS-BoldItalicMT"
argument_list|,
literal|"CourierNew-BoldItalic"
argument_list|,
literal|"LiberationMono-BoldItalic"
argument_list|,
literal|"NimbusMonL-BoldObli"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Helvetica"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"ArialMT"
argument_list|,
literal|"Arial"
argument_list|,
literal|"LiberationSans"
argument_list|,
literal|"NimbusSanL-Regu"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Helvetica-Bold"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Arial-BoldMT"
argument_list|,
literal|"Arial-Bold"
argument_list|,
literal|"LiberationSans-Bold"
argument_list|,
literal|"NimbusSanL-Bold"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Helvetica-Oblique"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Arial-ItalicMT"
argument_list|,
literal|"Arial-Italic"
argument_list|,
literal|"Helvetica-Italic"
argument_list|,
literal|"LiberationSans-Italic"
argument_list|,
literal|"NimbusSanL-ReguItal"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Helvetica-BoldOblique"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Arial-BoldItalicMT"
argument_list|,
literal|"Helvetica-BoldItalic"
argument_list|,
literal|"LiberationSans-BoldItalic"
argument_list|,
literal|"NimbusSanL-BoldItal"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Times-Roman"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"TimesNewRomanPSMT"
argument_list|,
literal|"TimesNewRoman"
argument_list|,
literal|"TimesNewRomanPS"
argument_list|,
literal|"LiberationSerif"
argument_list|,
literal|"NimbusRomNo9L-Regu"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Times-Bold"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"TimesNewRomanPS-BoldMT"
argument_list|,
literal|"TimesNewRomanPS-Bold"
argument_list|,
literal|"TimesNewRoman-Bold"
argument_list|,
literal|"LiberationSerif-Bold"
argument_list|,
literal|"NimbusRomNo9L-Medi"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Times-Italic"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"TimesNewRomanPS-ItalicMT"
argument_list|,
literal|"TimesNewRomanPS-Italic"
argument_list|,
literal|"TimesNewRoman-Italic"
argument_list|,
literal|"LiberationSerif-Italic"
argument_list|,
literal|"NimbusRomNo9L-ReguItal"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Times-BoldItalic"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"TimesNewRomanPS-BoldItalicMT"
argument_list|,
literal|"TimesNewRomanPS-BoldItalic"
argument_list|,
literal|"TimesNewRoman-BoldItalic"
argument_list|,
literal|"LiberationSerif-BoldItalic"
argument_list|,
literal|"NimbusRomNo9L-MediItal"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"Symbol"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"SymbolMT"
argument_list|,
literal|"StandardSymL"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"ZapfDingbats"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"ZapfDingbatsITC"
argument_list|,
literal|"Dingbats"
argument_list|)
argument_list|)
expr_stmt|;
comment|// extra substitute mechanism for CJK CIDFonts when all we know is the ROS
name|substitutes
operator|.
name|put
argument_list|(
literal|"$Adobe-CNS1"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"AdobeMingStd-Light"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"$Adobe-Japan1"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"KozMinPr6N-Regular"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"$Adobe-Korea1"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"AdobeGothicStd-Bold"
argument_list|)
argument_list|)
expr_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
literal|"$Adobe-GB1"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"AdobeHeitiStd-Regular"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Acrobat also uses alternative names for Standard 14 fonts, which we map to those above
comment|// these include names such as "Arial" and "TimesNewRoman"
for|for
control|(
name|String
name|baseName
range|:
name|Standard14Fonts
operator|.
name|getNames
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|substitutes
operator|.
name|containsKey
argument_list|(
name|baseName
argument_list|)
condition|)
block|{
name|String
name|mappedName
init|=
name|Standard14Fonts
operator|.
name|getMappedFontName
argument_list|(
name|baseName
argument_list|)
decl_stmt|;
name|substitutes
operator|.
name|put
argument_list|(
name|baseName
argument_list|,
name|copySubstitutes
argument_list|(
name|mappedName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Copies a list of font substitutes, adding the original font at the start of the list.      */
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|copySubstitutes
parameter_list|(
name|String
name|postScriptName
parameter_list|)
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|substitutes
operator|.
name|get
argument_list|(
name|postScriptName
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Adds a top-priority substitute for the given font.      *      * @param match PostScript name of the font to match      * @param replace PostScript name of the font to use as a replacement      */
specifier|public
specifier|static
name|void
name|addSubstitute
parameter_list|(
name|String
name|match
parameter_list|,
name|String
name|replace
parameter_list|)
block|{
if|if
condition|(
operator|!
name|substitutes
operator|.
name|containsKey
argument_list|(
name|match
argument_list|)
condition|)
block|{
name|substitutes
operator|.
name|put
argument_list|(
name|match
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|substitutes
operator|.
name|get
argument_list|(
name|match
argument_list|)
operator|.
name|add
argument_list|(
name|replace
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the substitutes for a given font.      */
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getSubstitutes
parameter_list|(
name|String
name|postScriptName
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|subs
init|=
name|substitutes
operator|.
name|get
argument_list|(
name|postScriptName
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|""
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|subs
operator|!=
literal|null
condition|)
block|{
return|return
name|subs
return|;
block|}
else|else
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
comment|/**      * Windows name (ArialNarrow,Bold) to PostScript name (ArialNarrow-Bold)      */
specifier|private
specifier|static
name|String
name|windowsToPs
parameter_list|(
name|String
name|windowsName
parameter_list|)
block|{
return|return
name|windowsName
operator|.
name|replaceAll
argument_list|(
literal|","
argument_list|,
literal|"-"
argument_list|)
return|;
block|}
comment|/**      * Finds a CFF CID-Keyed font with the given PostScript name, or a suitable substitute, or null.      *      * @param registryOrdering the CID system registry and ordering e.g. "Adobe-Japan1", if any      * @param fontDescriptor the font descriptor, if any      */
specifier|public
specifier|static
name|CFFCIDFont
name|getCFFCIDFontFallback
parameter_list|(
name|String
name|registryOrdering
parameter_list|,
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
block|{
comment|// try ROS substitutes
comment|// todo: this is a fairly primitive mechanism and could be improved
if|if
condition|(
name|registryOrdering
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|substituteName
range|:
name|getSubstitutes
argument_list|(
literal|"$"
operator|+
name|registryOrdering
argument_list|)
control|)
block|{
name|CFFFont
name|cff
init|=
name|getProvider
argument_list|()
operator|.
name|getCFFFont
argument_list|(
name|substituteName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cff
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|cff
operator|instanceof
name|CFFCIDFont
condition|)
block|{
return|return
operator|(
name|CFFCIDFont
operator|)
name|cff
return|;
block|}
block|}
block|}
block|}
return|return
name|cidFallbackFont
return|;
block|}
comment|/**      * Returns the fallback font, used for rendering when no other fonts are available,      * we attempt to find a good fallback based on the font descriptor.      */
specifier|public
specifier|static
name|Type1Equivalent
name|getType1FallbackFont
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
block|{
name|String
name|fontName
init|=
name|getFallbackFontName
argument_list|(
name|fontDescriptor
argument_list|)
decl_stmt|;
name|Type1Equivalent
name|type1Equivalent
init|=
name|getType1EquivalentFont
argument_list|(
name|fontName
argument_list|)
decl_stmt|;
if|if
condition|(
name|type1Equivalent
operator|==
literal|null
condition|)
block|{
name|String
name|message
init|=
name|fontProvider
operator|.
name|toDebugString
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
comment|// if we couldn't get a PFB font by now then there's no point continuing
name|log
operator|.
name|error
argument_list|(
literal|"No fallback font for '"
operator|+
name|fontName
operator|+
literal|"', dumping debug information:"
argument_list|)
expr_stmt|;
name|log
operator|.
name|error
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No fonts available on the system for "
operator|+
name|fontName
argument_list|)
throw|;
block|}
return|return
name|type1Equivalent
return|;
block|}
comment|/**      * Returns the fallback font, used for rendering when no other fonts are available,      * we attempt to find a good fallback based on the font descriptor.      */
specifier|public
specifier|static
name|TrueTypeFont
name|getTrueTypeFallbackFont
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
block|{
name|String
name|fontName
init|=
name|getFallbackFontName
argument_list|(
name|fontDescriptor
argument_list|)
decl_stmt|;
name|TrueTypeFont
name|ttf
init|=
name|getTrueTypeFont
argument_list|(
name|fontName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttf
operator|==
literal|null
condition|)
block|{
comment|// we have to return something here as TTFs aren't strictly required on the system
name|log
operator|.
name|error
argument_list|(
literal|"No TTF fallback font for '"
operator|+
name|fontName
operator|+
literal|"'"
argument_list|)
expr_stmt|;
return|return
name|ttfFallbackFont
return|;
block|}
return|return
name|ttf
return|;
block|}
comment|/**      * Attempts to find a good fallback based on the font descriptor.      */
specifier|private
specifier|static
name|String
name|getFallbackFontName
parameter_list|(
name|PDFontDescriptor
name|fontDescriptor
parameter_list|)
block|{
name|String
name|fontName
decl_stmt|;
if|if
condition|(
name|fontDescriptor
operator|!=
literal|null
condition|)
block|{
comment|// heuristic detection of bold
name|boolean
name|isBold
init|=
literal|false
decl_stmt|;
name|String
name|name
init|=
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|String
name|lower
init|=
name|fontDescriptor
operator|.
name|getFontName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
decl_stmt|;
name|isBold
operator|=
name|lower
operator|.
name|contains
argument_list|(
literal|"bold"
argument_list|)
operator|||
name|lower
operator|.
name|contains
argument_list|(
literal|"black"
argument_list|)
operator|||
name|lower
operator|.
name|contains
argument_list|(
literal|"heavy"
argument_list|)
expr_stmt|;
block|}
comment|// font descriptor flags should describe the style
if|if
condition|(
name|fontDescriptor
operator|.
name|isFixedPitch
argument_list|()
condition|)
block|{
name|fontName
operator|=
literal|"Courier"
expr_stmt|;
if|if
condition|(
name|isBold
operator|&&
name|fontDescriptor
operator|.
name|isItalic
argument_list|()
condition|)
block|{
name|fontName
operator|+=
literal|"-BoldOblique"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isBold
condition|)
block|{
name|fontName
operator|+=
literal|"-Bold"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fontDescriptor
operator|.
name|isItalic
argument_list|()
condition|)
block|{
name|fontName
operator|+=
literal|"-Oblique"
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|fontDescriptor
operator|.
name|isSerif
argument_list|()
condition|)
block|{
name|fontName
operator|=
literal|"Times"
expr_stmt|;
if|if
condition|(
name|isBold
operator|&&
name|fontDescriptor
operator|.
name|isItalic
argument_list|()
condition|)
block|{
name|fontName
operator|+=
literal|"-BoldItalic"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isBold
condition|)
block|{
name|fontName
operator|+=
literal|"-Bold"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fontDescriptor
operator|.
name|isItalic
argument_list|()
condition|)
block|{
name|fontName
operator|+=
literal|"-Italic"
expr_stmt|;
block|}
else|else
block|{
name|fontName
operator|+=
literal|"-Roman"
expr_stmt|;
block|}
block|}
else|else
block|{
name|fontName
operator|=
literal|"Helvetica"
expr_stmt|;
if|if
condition|(
name|isBold
operator|&&
name|fontDescriptor
operator|.
name|isItalic
argument_list|()
condition|)
block|{
name|fontName
operator|+=
literal|"-BoldOblique"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isBold
condition|)
block|{
name|fontName
operator|+=
literal|"-Bold"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fontDescriptor
operator|.
name|isItalic
argument_list|()
condition|)
block|{
name|fontName
operator|+=
literal|"-Oblique"
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// if there is no FontDescriptor then we just fall back to Times Roman
name|fontName
operator|=
literal|"Times-Roman"
expr_stmt|;
block|}
return|return
name|fontName
return|;
block|}
comment|/**      * Finds a TrueType font with the given PostScript name, or a suitable substitute, or null.      *      * @param postScriptName PostScript font name      */
specifier|public
specifier|static
name|TrueTypeFont
name|getTrueTypeFont
parameter_list|(
name|String
name|postScriptName
parameter_list|)
block|{
comment|// first ask the font provider for the font
name|TrueTypeFont
name|ttf
init|=
name|getProvider
argument_list|()
operator|.
name|getTrueTypeFont
argument_list|(
name|postScriptName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttf
operator|==
literal|null
condition|)
block|{
comment|// then try substitutes
for|for
control|(
name|String
name|substituteName
range|:
name|getSubstitutes
argument_list|(
name|postScriptName
argument_list|)
control|)
block|{
name|ttf
operator|=
name|getProvider
argument_list|()
operator|.
name|getTrueTypeFont
argument_list|(
name|substituteName
argument_list|)
expr_stmt|;
if|if
condition|(
name|ttf
operator|!=
literal|null
condition|)
block|{
return|return
name|ttf
return|;
block|}
block|}
comment|// then Windows name
name|ttf
operator|=
name|getProvider
argument_list|()
operator|.
name|getTrueTypeFont
argument_list|(
name|windowsToPs
argument_list|(
name|postScriptName
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ttf
return|;
block|}
comment|/**      * Finds a TrueType font with the given PostScript name, or a suitable substitute, or null.      *      * @param postScriptName PostScript font name      */
specifier|public
specifier|static
name|Type1Font
name|getType1Font
parameter_list|(
name|String
name|postScriptName
parameter_list|)
block|{
comment|// first ask the font provider for the font
name|Type1Font
name|t1
init|=
name|getProvider
argument_list|()
operator|.
name|getType1Font
argument_list|(
name|postScriptName
argument_list|)
decl_stmt|;
if|if
condition|(
name|t1
operator|==
literal|null
condition|)
block|{
comment|// then try substitutes
for|for
control|(
name|String
name|substituteName
range|:
name|getSubstitutes
argument_list|(
name|postScriptName
argument_list|)
control|)
block|{
name|t1
operator|=
name|getProvider
argument_list|()
operator|.
name|getType1Font
argument_list|(
name|substituteName
argument_list|)
expr_stmt|;
if|if
condition|(
name|t1
operator|!=
literal|null
condition|)
block|{
return|return
name|t1
return|;
block|}
block|}
comment|// then Windows name
name|t1
operator|=
name|getProvider
argument_list|()
operator|.
name|getType1Font
argument_list|(
name|windowsToPs
argument_list|(
name|postScriptName
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|t1
return|;
block|}
comment|/**      * Finds a CFF Type 1 font with the given PostScript name, or a suitable substitute, or null.      *      * @param postScriptName PostScript font name      */
specifier|public
specifier|static
name|CFFType1Font
name|getCFFType1Font
parameter_list|(
name|String
name|postScriptName
parameter_list|)
block|{
name|CFFFont
name|cff
init|=
name|getCFFFont
argument_list|(
name|postScriptName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cff
operator|instanceof
name|CFFType1Font
condition|)
block|{
return|return
operator|(
name|CFFType1Font
operator|)
name|cff
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Finds a CFF CID-Keyed font with the given PostScript name, or a suitable substitute, or null.      *      * @param postScriptName PostScript font name      */
specifier|public
specifier|static
name|CFFCIDFont
name|getCFFCIDFont
parameter_list|(
name|String
name|postScriptName
parameter_list|)
block|{
name|CFFFont
name|cff
init|=
name|getCFFFont
argument_list|(
name|postScriptName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cff
operator|instanceof
name|CFFCIDFont
condition|)
block|{
return|return
operator|(
name|CFFCIDFont
operator|)
name|cff
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Finds a CFF font with the given PostScript name, or a suitable substitute, or null.      *      * @param postScriptName PostScript font name      */
specifier|private
specifier|static
name|CFFFont
name|getCFFFont
parameter_list|(
name|String
name|postScriptName
parameter_list|)
block|{
comment|// first ask the font provider for the font
name|CFFFont
name|cff
init|=
name|getProvider
argument_list|()
operator|.
name|getCFFFont
argument_list|(
name|postScriptName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cff
operator|==
literal|null
condition|)
block|{
comment|// then try substitutes
for|for
control|(
name|String
name|substituteName
range|:
name|getSubstitutes
argument_list|(
name|postScriptName
argument_list|)
control|)
block|{
name|cff
operator|=
name|getProvider
argument_list|()
operator|.
name|getCFFFont
argument_list|(
name|substituteName
argument_list|)
expr_stmt|;
if|if
condition|(
name|cff
operator|!=
literal|null
condition|)
block|{
return|return
name|cff
return|;
block|}
block|}
comment|// then Windows name
name|cff
operator|=
name|getProvider
argument_list|()
operator|.
name|getCFFFont
argument_list|(
name|windowsToPs
argument_list|(
name|postScriptName
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|cff
return|;
block|}
comment|/**      * Finds a Type 1-equivalent font with the given PostScript name, or a suitable substitute,      * or null. This allows a Type 1 font to be substituted with a PFB, TTF or OTF.      *      * @param postScriptName PostScript font name      */
specifier|public
specifier|static
name|Type1Equivalent
name|getType1EquivalentFont
parameter_list|(
name|String
name|postScriptName
parameter_list|)
block|{
name|Type1Font
name|t1
init|=
name|getType1Font
argument_list|(
name|postScriptName
argument_list|)
decl_stmt|;
if|if
condition|(
name|t1
operator|!=
literal|null
condition|)
block|{
return|return
name|t1
return|;
block|}
name|CFFType1Font
name|cff
init|=
name|getCFFType1Font
argument_list|(
name|postScriptName
argument_list|)
decl_stmt|;
if|if
condition|(
name|cff
operator|!=
literal|null
condition|)
block|{
return|return
name|cff
return|;
block|}
name|TrueTypeFont
name|ttf
init|=
name|getTrueTypeFont
argument_list|(
name|postScriptName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ttf
operator|!=
literal|null
condition|)
block|{
return|return
name|ttf
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

