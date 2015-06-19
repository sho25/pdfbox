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
name|fontbox
operator|.
name|afm
operator|.
name|AFMParser
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
name|afm
operator|.
name|FontMetrics
import|;
end_import

begin_comment
comment|/**  * The "Standard 14" PDF fonts, also known as the "base 14" fonts.  * There are 14 font files, but Acrobat uses additional names for compatibility, e.g. Arial.  *  * @author John Hewson  */
end_comment

begin_class
specifier|final
class|class
name|Standard14Fonts
block|{
specifier|private
name|Standard14Fonts
parameter_list|()
block|{     }
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|STANDARD_14_NAMES
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|STANDARD_14_MAPPING
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|FontMetrics
argument_list|>
name|STANDARD14_AFM_MAP
decl_stmt|;
static|static
block|{
try|try
block|{
name|STANDARD14_AFM_MAP
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|FontMetrics
argument_list|>
argument_list|()
expr_stmt|;
name|addAFM
argument_list|(
literal|"Courier-Bold"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Courier-BoldOblique"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Courier"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Courier-Oblique"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Helvetica"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Helvetica-Bold"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Helvetica-BoldOblique"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Helvetica-Oblique"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Symbol"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Times-Bold"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Times-BoldItalic"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Times-Italic"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Times-Roman"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"ZapfDingbats"
argument_list|)
expr_stmt|;
comment|// alternative names from Adobe Supplement to the ISO 32000
name|addAFM
argument_list|(
literal|"CourierCourierNew"
argument_list|,
literal|"Courier"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"CourierNew"
argument_list|,
literal|"Courier"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"CourierNew,Italic"
argument_list|,
literal|"Courier-Oblique"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"CourierNew,Bold"
argument_list|,
literal|"Courier-Bold"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"CourierNew,BoldItalic"
argument_list|,
literal|"Courier-BoldOblique"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Arial"
argument_list|,
literal|"Helvetica"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Arial,Italic"
argument_list|,
literal|"Helvetica-Oblique"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Arial,Bold"
argument_list|,
literal|"Helvetica-Bold"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Arial,BoldItalic"
argument_list|,
literal|"Helvetica-BoldOblique"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"TimesNewRoman"
argument_list|,
literal|"Times-Roman"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"TimesNewRoman,Italic"
argument_list|,
literal|"Times-Italic"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"TimesNewRoman,Bold"
argument_list|,
literal|"Times-Bold"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"TimesNewRoman,BoldItalic"
argument_list|,
literal|"Times-BoldItalic"
argument_list|)
expr_stmt|;
comment|// Acrobat treats these fonts as "standard 14" too (at least Acrobat preflight says so)
name|addAFM
argument_list|(
literal|"Symbol,Italic"
argument_list|,
literal|"Symbol"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Symbol,Bold"
argument_list|,
literal|"Symbol"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Symbol,BoldItalic"
argument_list|,
literal|"Symbol"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Times"
argument_list|,
literal|"Times-Roman"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Times,Italic"
argument_list|,
literal|"Times-Italic"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Times,Bold"
argument_list|,
literal|"Times-Bold"
argument_list|)
expr_stmt|;
name|addAFM
argument_list|(
literal|"Times,BoldItalic"
argument_list|,
literal|"Times-BoldItalic"
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
specifier|private
specifier|static
name|void
name|addAFM
parameter_list|(
name|String
name|fontName
parameter_list|)
throws|throws
name|IOException
block|{
name|addAFM
argument_list|(
name|fontName
argument_list|,
name|fontName
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|addAFM
parameter_list|(
name|String
name|fontName
parameter_list|,
name|String
name|afmName
parameter_list|)
throws|throws
name|IOException
block|{
name|STANDARD_14_NAMES
operator|.
name|add
argument_list|(
name|fontName
argument_list|)
expr_stmt|;
name|STANDARD_14_MAPPING
operator|.
name|put
argument_list|(
name|fontName
argument_list|,
name|afmName
argument_list|)
expr_stmt|;
if|if
condition|(
name|STANDARD14_AFM_MAP
operator|.
name|containsKey
argument_list|(
name|afmName
argument_list|)
condition|)
block|{
name|STANDARD14_AFM_MAP
operator|.
name|put
argument_list|(
name|fontName
argument_list|,
name|STANDARD14_AFM_MAP
operator|.
name|get
argument_list|(
name|afmName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|resourceName
init|=
literal|"org/apache/pdfbox/resources/afm/"
operator|+
name|afmName
operator|+
literal|".afm"
decl_stmt|;
name|URL
name|url
init|=
name|PDType1Font
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|afmStream
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
try|try
block|{
name|AFMParser
name|parser
init|=
operator|new
name|AFMParser
argument_list|(
name|afmStream
argument_list|)
decl_stmt|;
name|FontMetrics
name|metric
init|=
name|parser
operator|.
name|parse
argument_list|()
decl_stmt|;
name|STANDARD14_AFM_MAP
operator|.
name|put
argument_list|(
name|fontName
argument_list|,
name|metric
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|afmStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|resourceName
operator|+
literal|" not found"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns the AFM for the given font.      * @param baseName base name of font      */
specifier|public
specifier|static
name|FontMetrics
name|getAFM
parameter_list|(
name|String
name|baseName
parameter_list|)
block|{
return|return
name|STANDARD14_AFM_MAP
operator|.
name|get
argument_list|(
name|baseName
argument_list|)
return|;
block|}
comment|/**      * Returns true if the given font name a Standard 14 font.      * @param baseName base name of font      */
specifier|public
specifier|static
name|boolean
name|containsName
parameter_list|(
name|String
name|baseName
parameter_list|)
block|{
return|return
name|STANDARD_14_NAMES
operator|.
name|contains
argument_list|(
name|baseName
argument_list|)
return|;
block|}
comment|/**      * Returns the set of Standard 14 font names, including additional names.      */
specifier|public
specifier|static
name|Set
argument_list|<
name|String
argument_list|>
name|getNames
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|STANDARD_14_NAMES
argument_list|)
return|;
block|}
comment|/**      * Returns the name of the actual font which the given font name maps to.      * @param baseName base name of font      */
specifier|public
specifier|static
name|String
name|getMappedFontName
parameter_list|(
name|String
name|baseName
parameter_list|)
block|{
return|return
name|STANDARD_14_MAPPING
operator|.
name|get
argument_list|(
name|baseName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

