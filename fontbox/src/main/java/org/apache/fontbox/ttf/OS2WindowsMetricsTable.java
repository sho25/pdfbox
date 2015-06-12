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
name|ttf
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

begin_comment
comment|/**  * A table in a true type font.  *   * @author Ben Litchfield  *   */
end_comment

begin_class
specifier|public
class|class
name|OS2WindowsMetricsTable
extends|extends
name|TTFTable
block|{
comment|/**      * Weight class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WEIGHT_CLASS_THIN
init|=
literal|100
decl_stmt|;
comment|/**      * Weight class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WEIGHT_CLASS_ULTRA_LIGHT
init|=
literal|200
decl_stmt|;
comment|/**      * Weight class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WEIGHT_CLASS_LIGHT
init|=
literal|300
decl_stmt|;
comment|/**      * Weight class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WEIGHT_CLASS_NORMAL
init|=
literal|400
decl_stmt|;
comment|/**      * Weight class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WEIGHT_CLASS_MEDIUM
init|=
literal|500
decl_stmt|;
comment|/**      * Weight class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WEIGHT_CLASS_SEMI_BOLD
init|=
literal|600
decl_stmt|;
comment|/**      * Weight class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WEIGHT_CLASS_BOLD
init|=
literal|700
decl_stmt|;
comment|/**      * Weight class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WEIGHT_CLASS_EXTRA_BOLD
init|=
literal|800
decl_stmt|;
comment|/**      * Weight class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WEIGHT_CLASS_BLACK
init|=
literal|900
decl_stmt|;
comment|/**      * Width class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WIDTH_CLASS_ULTRA_CONDENSED
init|=
literal|1
decl_stmt|;
comment|/**      * Width class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WIDTH_CLASS_EXTRA_CONDENSED
init|=
literal|2
decl_stmt|;
comment|/**      * Width class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WIDTH_CLASS_CONDENSED
init|=
literal|3
decl_stmt|;
comment|/**      * Width class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WIDTH_CLASS_SEMI_CONDENSED
init|=
literal|4
decl_stmt|;
comment|/**      * Width class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WIDTH_CLASS_MEDIUM
init|=
literal|5
decl_stmt|;
comment|/**      * Width class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WIDTH_CLASS_SEMI_EXPANDED
init|=
literal|6
decl_stmt|;
comment|/**      * Width class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WIDTH_CLASS_EXPANDED
init|=
literal|7
decl_stmt|;
comment|/**      * Width class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WIDTH_CLASS_EXTRA_EXPANDED
init|=
literal|8
decl_stmt|;
comment|/**      * Width class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|WIDTH_CLASS_ULTRA_EXPANDED
init|=
literal|9
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_NO_CLASSIFICATION
init|=
literal|0
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_OLDSTYLE_SERIFS
init|=
literal|1
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_TRANSITIONAL_SERIFS
init|=
literal|2
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_MODERN_SERIFS
init|=
literal|3
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_CLAREDON_SERIFS
init|=
literal|4
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_SLAB_SERIFS
init|=
literal|5
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_FREEFORM_SERIFS
init|=
literal|7
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_SANS_SERIF
init|=
literal|8
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_ORNAMENTALS
init|=
literal|9
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_SCRIPTS
init|=
literal|10
decl_stmt|;
comment|/**      * Family class constant.      */
specifier|public
specifier|static
specifier|final
name|int
name|FAMILY_CLASS_SYMBOLIC
init|=
literal|12
decl_stmt|;
comment|/**      * Restricted License embedding: must not be modified, embedded or exchanged in any manner.      *      *<p>For Restricted License embedding to take effect, it must be the only level of embedding      * selected.      */
specifier|public
specifier|static
specifier|final
name|short
name|FSTYPE_RESTRICTED
init|=
literal|0x0001
decl_stmt|;
comment|/**      * Preview& Print embedding: the font may be embedded, and temporarily loaded on the      * remote system. No edits can be applied to the document.      */
specifier|public
specifier|static
specifier|final
name|short
name|FSTYPE_PREVIEW_AND_PRINT
init|=
literal|0x0004
decl_stmt|;
comment|/**      * Editable embedding: the font may be embedded but must only be installed temporarily on other      * systems. Documents may be editied and changes saved.      */
specifier|public
specifier|static
specifier|final
name|short
name|FSTYPE_EDITIBLE
init|=
literal|0x0004
decl_stmt|;
comment|/**      * No subsetting: the font must not be subsetted prior to embedding.      */
specifier|public
specifier|static
specifier|final
name|short
name|FSTYPE_NO_SUBSETTING
init|=
literal|0x0100
decl_stmt|;
comment|/**      * Bitmap embedding only: only bitmaps contained in the font may be embedded. No outline data      * may be embedded. Other embedding restrictions specified in bits 0-3 and 8 also apply.      */
specifier|public
specifier|static
specifier|final
name|short
name|FSTYPE_BITMAP_ONLY
init|=
literal|0x0200
decl_stmt|;
name|OS2WindowsMetricsTable
parameter_list|(
name|TrueTypeFont
name|font
parameter_list|)
block|{
name|super
argument_list|(
name|font
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return Returns the achVendId.      */
specifier|public
name|String
name|getAchVendId
parameter_list|()
block|{
return|return
name|achVendId
return|;
block|}
comment|/**      * @param achVendIdValue The achVendId to set.      */
specifier|public
name|void
name|setAchVendId
parameter_list|(
name|String
name|achVendIdValue
parameter_list|)
block|{
name|this
operator|.
name|achVendId
operator|=
name|achVendIdValue
expr_stmt|;
block|}
comment|/**      * @return Returns the averageCharWidth.      */
specifier|public
name|short
name|getAverageCharWidth
parameter_list|()
block|{
return|return
name|averageCharWidth
return|;
block|}
comment|/**      * @param averageCharWidthValue The averageCharWidth to set.      */
specifier|public
name|void
name|setAverageCharWidth
parameter_list|(
name|short
name|averageCharWidthValue
parameter_list|)
block|{
name|this
operator|.
name|averageCharWidth
operator|=
name|averageCharWidthValue
expr_stmt|;
block|}
comment|/**      * @return Returns the codePageRange1.      */
specifier|public
name|long
name|getCodePageRange1
parameter_list|()
block|{
return|return
name|codePageRange1
return|;
block|}
comment|/**      * @param codePageRange1Value The codePageRange1 to set.      */
specifier|public
name|void
name|setCodePageRange1
parameter_list|(
name|long
name|codePageRange1Value
parameter_list|)
block|{
name|this
operator|.
name|codePageRange1
operator|=
name|codePageRange1Value
expr_stmt|;
block|}
comment|/**      * @return Returns the codePageRange2.      */
specifier|public
name|long
name|getCodePageRange2
parameter_list|()
block|{
return|return
name|codePageRange2
return|;
block|}
comment|/**      * @param codePageRange2Value The codePageRange2 to set.      */
specifier|public
name|void
name|setCodePageRange2
parameter_list|(
name|long
name|codePageRange2Value
parameter_list|)
block|{
name|this
operator|.
name|codePageRange2
operator|=
name|codePageRange2Value
expr_stmt|;
block|}
comment|/**      * @return Returns the familyClass.      */
specifier|public
name|int
name|getFamilyClass
parameter_list|()
block|{
return|return
name|familyClass
return|;
block|}
comment|/**      * @param familyClassValue The familyClass to set.      */
specifier|public
name|void
name|setFamilyClass
parameter_list|(
name|int
name|familyClassValue
parameter_list|)
block|{
name|this
operator|.
name|familyClass
operator|=
name|familyClassValue
expr_stmt|;
block|}
comment|/**      * @return Returns the familySubClass.      */
specifier|public
name|int
name|getFamilySubClass
parameter_list|()
block|{
return|return
name|familySubClass
return|;
block|}
comment|/**      * @param familySubClassValue The familySubClass to set.      */
specifier|public
name|void
name|setFamilySubClass
parameter_list|(
name|int
name|familySubClassValue
parameter_list|)
block|{
name|this
operator|.
name|familySubClass
operator|=
name|familySubClassValue
expr_stmt|;
block|}
comment|/**      * @return Returns the firstCharIndex.      */
specifier|public
name|int
name|getFirstCharIndex
parameter_list|()
block|{
return|return
name|firstCharIndex
return|;
block|}
comment|/**      * @param firstCharIndexValue The firstCharIndex to set.      */
specifier|public
name|void
name|setFirstCharIndex
parameter_list|(
name|int
name|firstCharIndexValue
parameter_list|)
block|{
name|this
operator|.
name|firstCharIndex
operator|=
name|firstCharIndexValue
expr_stmt|;
block|}
comment|/**      * @return Returns the fsSelection.      */
specifier|public
name|int
name|getFsSelection
parameter_list|()
block|{
return|return
name|fsSelection
return|;
block|}
comment|/**      * @param fsSelectionValue The fsSelection to set.      */
specifier|public
name|void
name|setFsSelection
parameter_list|(
name|int
name|fsSelectionValue
parameter_list|)
block|{
name|this
operator|.
name|fsSelection
operator|=
name|fsSelectionValue
expr_stmt|;
block|}
comment|/**      * @return Returns the fsType.      */
specifier|public
name|short
name|getFsType
parameter_list|()
block|{
return|return
name|fsType
return|;
block|}
comment|/**      * @param fsTypeValue The fsType to set.      */
specifier|public
name|void
name|setFsType
parameter_list|(
name|short
name|fsTypeValue
parameter_list|)
block|{
name|this
operator|.
name|fsType
operator|=
name|fsTypeValue
expr_stmt|;
block|}
comment|/**      * @return Returns the lastCharIndex.      */
specifier|public
name|int
name|getLastCharIndex
parameter_list|()
block|{
return|return
name|lastCharIndex
return|;
block|}
comment|/**      * @param lastCharIndexValue The lastCharIndex to set.      */
specifier|public
name|void
name|setLastCharIndex
parameter_list|(
name|int
name|lastCharIndexValue
parameter_list|)
block|{
name|this
operator|.
name|lastCharIndex
operator|=
name|lastCharIndexValue
expr_stmt|;
block|}
comment|/**      * @return Returns the panose.      */
specifier|public
name|byte
index|[]
name|getPanose
parameter_list|()
block|{
return|return
name|panose
return|;
block|}
comment|/**      * @param panoseValue The panose to set.      */
specifier|public
name|void
name|setPanose
parameter_list|(
name|byte
index|[]
name|panoseValue
parameter_list|)
block|{
name|this
operator|.
name|panose
operator|=
name|panoseValue
expr_stmt|;
block|}
comment|/**      * @return Returns the strikeoutPosition.      */
specifier|public
name|short
name|getStrikeoutPosition
parameter_list|()
block|{
return|return
name|strikeoutPosition
return|;
block|}
comment|/**      * @param strikeoutPositionValue The strikeoutPosition to set.      */
specifier|public
name|void
name|setStrikeoutPosition
parameter_list|(
name|short
name|strikeoutPositionValue
parameter_list|)
block|{
name|this
operator|.
name|strikeoutPosition
operator|=
name|strikeoutPositionValue
expr_stmt|;
block|}
comment|/**      * @return Returns the strikeoutSize.      */
specifier|public
name|short
name|getStrikeoutSize
parameter_list|()
block|{
return|return
name|strikeoutSize
return|;
block|}
comment|/**      * @param strikeoutSizeValue The strikeoutSize to set.      */
specifier|public
name|void
name|setStrikeoutSize
parameter_list|(
name|short
name|strikeoutSizeValue
parameter_list|)
block|{
name|this
operator|.
name|strikeoutSize
operator|=
name|strikeoutSizeValue
expr_stmt|;
block|}
comment|/**      * @return Returns the subscriptXOffset.      */
specifier|public
name|short
name|getSubscriptXOffset
parameter_list|()
block|{
return|return
name|subscriptXOffset
return|;
block|}
comment|/**      * @param subscriptXOffsetValue The subscriptXOffset to set.      */
specifier|public
name|void
name|setSubscriptXOffset
parameter_list|(
name|short
name|subscriptXOffsetValue
parameter_list|)
block|{
name|this
operator|.
name|subscriptXOffset
operator|=
name|subscriptXOffsetValue
expr_stmt|;
block|}
comment|/**      * @return Returns the subscriptXSize.      */
specifier|public
name|short
name|getSubscriptXSize
parameter_list|()
block|{
return|return
name|subscriptXSize
return|;
block|}
comment|/**      * @param subscriptXSizeValue The subscriptXSize to set.      */
specifier|public
name|void
name|setSubscriptXSize
parameter_list|(
name|short
name|subscriptXSizeValue
parameter_list|)
block|{
name|this
operator|.
name|subscriptXSize
operator|=
name|subscriptXSizeValue
expr_stmt|;
block|}
comment|/**      * @return Returns the subscriptYOffset.      */
specifier|public
name|short
name|getSubscriptYOffset
parameter_list|()
block|{
return|return
name|subscriptYOffset
return|;
block|}
comment|/**      * @param subscriptYOffsetValue The subscriptYOffset to set.      */
specifier|public
name|void
name|setSubscriptYOffset
parameter_list|(
name|short
name|subscriptYOffsetValue
parameter_list|)
block|{
name|this
operator|.
name|subscriptYOffset
operator|=
name|subscriptYOffsetValue
expr_stmt|;
block|}
comment|/**      * @return Returns the subscriptYSize.      */
specifier|public
name|short
name|getSubscriptYSize
parameter_list|()
block|{
return|return
name|subscriptYSize
return|;
block|}
comment|/**      * @param subscriptYSizeValue The subscriptYSize to set.      */
specifier|public
name|void
name|setSubscriptYSize
parameter_list|(
name|short
name|subscriptYSizeValue
parameter_list|)
block|{
name|this
operator|.
name|subscriptYSize
operator|=
name|subscriptYSizeValue
expr_stmt|;
block|}
comment|/**      * @return Returns the superscriptXOffset.      */
specifier|public
name|short
name|getSuperscriptXOffset
parameter_list|()
block|{
return|return
name|superscriptXOffset
return|;
block|}
comment|/**      * @param superscriptXOffsetValue The superscriptXOffset to set.      */
specifier|public
name|void
name|setSuperscriptXOffset
parameter_list|(
name|short
name|superscriptXOffsetValue
parameter_list|)
block|{
name|this
operator|.
name|superscriptXOffset
operator|=
name|superscriptXOffsetValue
expr_stmt|;
block|}
comment|/**      * @return Returns the superscriptXSize.      */
specifier|public
name|short
name|getSuperscriptXSize
parameter_list|()
block|{
return|return
name|superscriptXSize
return|;
block|}
comment|/**      * @param superscriptXSizeValue The superscriptXSize to set.      */
specifier|public
name|void
name|setSuperscriptXSize
parameter_list|(
name|short
name|superscriptXSizeValue
parameter_list|)
block|{
name|this
operator|.
name|superscriptXSize
operator|=
name|superscriptXSizeValue
expr_stmt|;
block|}
comment|/**      * @return Returns the superscriptYOffset.      */
specifier|public
name|short
name|getSuperscriptYOffset
parameter_list|()
block|{
return|return
name|superscriptYOffset
return|;
block|}
comment|/**      * @param superscriptYOffsetValue The superscriptYOffset to set.      */
specifier|public
name|void
name|setSuperscriptYOffset
parameter_list|(
name|short
name|superscriptYOffsetValue
parameter_list|)
block|{
name|this
operator|.
name|superscriptYOffset
operator|=
name|superscriptYOffsetValue
expr_stmt|;
block|}
comment|/**      * @return Returns the superscriptYSize.      */
specifier|public
name|short
name|getSuperscriptYSize
parameter_list|()
block|{
return|return
name|superscriptYSize
return|;
block|}
comment|/**      * @param superscriptYSizeValue The superscriptYSize to set.      */
specifier|public
name|void
name|setSuperscriptYSize
parameter_list|(
name|short
name|superscriptYSizeValue
parameter_list|)
block|{
name|this
operator|.
name|superscriptYSize
operator|=
name|superscriptYSizeValue
expr_stmt|;
block|}
comment|/**      * @return Returns the typoLineGap.      */
specifier|public
name|int
name|getTypoLineGap
parameter_list|()
block|{
return|return
name|typoLineGap
return|;
block|}
comment|/**      * @param typeLineGapValue The typoLineGap to set.      */
specifier|public
name|void
name|setTypoLineGap
parameter_list|(
name|int
name|typeLineGapValue
parameter_list|)
block|{
name|this
operator|.
name|typoLineGap
operator|=
name|typeLineGapValue
expr_stmt|;
block|}
comment|/**      * @return Returns the typoAscender.      */
specifier|public
name|int
name|getTypoAscender
parameter_list|()
block|{
return|return
name|typoAscender
return|;
block|}
comment|/**      * @param typoAscenderValue The typoAscender to set.      */
specifier|public
name|void
name|setTypoAscender
parameter_list|(
name|int
name|typoAscenderValue
parameter_list|)
block|{
name|this
operator|.
name|typoAscender
operator|=
name|typoAscenderValue
expr_stmt|;
block|}
comment|/**      * @return Returns the typoDescender.      */
specifier|public
name|int
name|getTypoDescender
parameter_list|()
block|{
return|return
name|typoDescender
return|;
block|}
comment|/**      * @param typoDescenderValue The typoDescender to set.      */
specifier|public
name|void
name|setTypoDescender
parameter_list|(
name|int
name|typoDescenderValue
parameter_list|)
block|{
name|this
operator|.
name|typoDescender
operator|=
name|typoDescenderValue
expr_stmt|;
block|}
comment|/**      * @return Returns the unicodeRange1.      */
specifier|public
name|long
name|getUnicodeRange1
parameter_list|()
block|{
return|return
name|unicodeRange1
return|;
block|}
comment|/**      * @param unicodeRange1Value The unicodeRange1 to set.      */
specifier|public
name|void
name|setUnicodeRange1
parameter_list|(
name|long
name|unicodeRange1Value
parameter_list|)
block|{
name|this
operator|.
name|unicodeRange1
operator|=
name|unicodeRange1Value
expr_stmt|;
block|}
comment|/**      * @return Returns the unicodeRange2.      */
specifier|public
name|long
name|getUnicodeRange2
parameter_list|()
block|{
return|return
name|unicodeRange2
return|;
block|}
comment|/**      * @param unicodeRange2Value The unicodeRange2 to set.      */
specifier|public
name|void
name|setUnicodeRange2
parameter_list|(
name|long
name|unicodeRange2Value
parameter_list|)
block|{
name|this
operator|.
name|unicodeRange2
operator|=
name|unicodeRange2Value
expr_stmt|;
block|}
comment|/**      * @return Returns the unicodeRange3.      */
specifier|public
name|long
name|getUnicodeRange3
parameter_list|()
block|{
return|return
name|unicodeRange3
return|;
block|}
comment|/**      * @param unicodeRange3Value The unicodeRange3 to set.      */
specifier|public
name|void
name|setUnicodeRange3
parameter_list|(
name|long
name|unicodeRange3Value
parameter_list|)
block|{
name|this
operator|.
name|unicodeRange3
operator|=
name|unicodeRange3Value
expr_stmt|;
block|}
comment|/**      * @return Returns the unicodeRange4.      */
specifier|public
name|long
name|getUnicodeRange4
parameter_list|()
block|{
return|return
name|unicodeRange4
return|;
block|}
comment|/**      * @param unicodeRange4Value The unicodeRange4 to set.      */
specifier|public
name|void
name|setUnicodeRange4
parameter_list|(
name|long
name|unicodeRange4Value
parameter_list|)
block|{
name|this
operator|.
name|unicodeRange4
operator|=
name|unicodeRange4Value
expr_stmt|;
block|}
comment|/**      * @return Returns the version.      */
specifier|public
name|int
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
comment|/**      * @param versionValue The version to set.      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|int
name|versionValue
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|versionValue
expr_stmt|;
block|}
comment|/**      * @return Returns the weightClass.      */
specifier|public
name|int
name|getWeightClass
parameter_list|()
block|{
return|return
name|weightClass
return|;
block|}
comment|/**      * @param weightClassValue The weightClass to set.      */
specifier|public
name|void
name|setWeightClass
parameter_list|(
name|int
name|weightClassValue
parameter_list|)
block|{
name|this
operator|.
name|weightClass
operator|=
name|weightClassValue
expr_stmt|;
block|}
comment|/**      * @return Returns the widthClass.      */
specifier|public
name|int
name|getWidthClass
parameter_list|()
block|{
return|return
name|widthClass
return|;
block|}
comment|/**      * @param widthClassValue The widthClass to set.      */
specifier|public
name|void
name|setWidthClass
parameter_list|(
name|int
name|widthClassValue
parameter_list|)
block|{
name|this
operator|.
name|widthClass
operator|=
name|widthClassValue
expr_stmt|;
block|}
comment|/**      * @return Returns the winAscent.      */
specifier|public
name|int
name|getWinAscent
parameter_list|()
block|{
return|return
name|winAscent
return|;
block|}
comment|/**      * @param winAscentValue The winAscent to set.      */
specifier|public
name|void
name|setWinAscent
parameter_list|(
name|int
name|winAscentValue
parameter_list|)
block|{
name|this
operator|.
name|winAscent
operator|=
name|winAscentValue
expr_stmt|;
block|}
comment|/**      * @return Returns the winDescent.      */
specifier|public
name|int
name|getWinDescent
parameter_list|()
block|{
return|return
name|winDescent
return|;
block|}
comment|/**      * @param winDescentValue The winDescent to set.      */
specifier|public
name|void
name|setWinDescent
parameter_list|(
name|int
name|winDescentValue
parameter_list|)
block|{
name|this
operator|.
name|winDescent
operator|=
name|winDescentValue
expr_stmt|;
block|}
comment|/**      * Returns the sxHeight.      */
specifier|public
name|int
name|getHeight
parameter_list|()
block|{
return|return
name|sxHeight
return|;
block|}
comment|/**      * Returns the sCapHeight.      */
specifier|public
name|int
name|getCapHeight
parameter_list|()
block|{
return|return
name|sCapHeight
return|;
block|}
comment|/**      * Returns the usDefaultChar.      */
specifier|public
name|int
name|getDefaultChar
parameter_list|()
block|{
return|return
name|usDefaultChar
return|;
block|}
comment|/**      * Returns the usBreakChar.      */
specifier|public
name|int
name|getBreakChar
parameter_list|()
block|{
return|return
name|usBreakChar
return|;
block|}
comment|/**      * Returns the usMaxContext.      */
specifier|public
name|int
name|getMaxContext
parameter_list|()
block|{
return|return
name|usMaxContext
return|;
block|}
specifier|private
name|int
name|version
decl_stmt|;
specifier|private
name|short
name|averageCharWidth
decl_stmt|;
specifier|private
name|int
name|weightClass
decl_stmt|;
specifier|private
name|int
name|widthClass
decl_stmt|;
specifier|private
name|short
name|fsType
decl_stmt|;
specifier|private
name|short
name|subscriptXSize
decl_stmt|;
specifier|private
name|short
name|subscriptYSize
decl_stmt|;
specifier|private
name|short
name|subscriptXOffset
decl_stmt|;
specifier|private
name|short
name|subscriptYOffset
decl_stmt|;
specifier|private
name|short
name|superscriptXSize
decl_stmt|;
specifier|private
name|short
name|superscriptYSize
decl_stmt|;
specifier|private
name|short
name|superscriptXOffset
decl_stmt|;
specifier|private
name|short
name|superscriptYOffset
decl_stmt|;
specifier|private
name|short
name|strikeoutSize
decl_stmt|;
specifier|private
name|short
name|strikeoutPosition
decl_stmt|;
specifier|private
name|int
name|familyClass
decl_stmt|;
specifier|private
name|int
name|familySubClass
decl_stmt|;
specifier|private
name|byte
index|[]
name|panose
init|=
operator|new
name|byte
index|[
literal|10
index|]
decl_stmt|;
specifier|private
name|long
name|unicodeRange1
decl_stmt|;
specifier|private
name|long
name|unicodeRange2
decl_stmt|;
specifier|private
name|long
name|unicodeRange3
decl_stmt|;
specifier|private
name|long
name|unicodeRange4
decl_stmt|;
specifier|private
name|String
name|achVendId
init|=
literal|"XXXX"
decl_stmt|;
specifier|private
name|int
name|fsSelection
decl_stmt|;
specifier|private
name|int
name|firstCharIndex
decl_stmt|;
specifier|private
name|int
name|lastCharIndex
decl_stmt|;
specifier|private
name|int
name|typoAscender
decl_stmt|;
specifier|private
name|int
name|typoDescender
decl_stmt|;
specifier|private
name|int
name|typoLineGap
decl_stmt|;
specifier|private
name|int
name|winAscent
decl_stmt|;
specifier|private
name|int
name|winDescent
decl_stmt|;
specifier|private
name|long
name|codePageRange1
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|long
name|codePageRange2
init|=
operator|-
literal|1
decl_stmt|;
specifier|private
name|int
name|sxHeight
decl_stmt|;
specifier|private
name|int
name|sCapHeight
decl_stmt|;
specifier|private
name|int
name|usDefaultChar
decl_stmt|;
specifier|private
name|int
name|usBreakChar
decl_stmt|;
specifier|private
name|int
name|usMaxContext
decl_stmt|;
comment|/**      * A tag that identifies this table type.      */
specifier|public
specifier|static
specifier|final
name|String
name|TAG
init|=
literal|"OS/2"
decl_stmt|;
comment|/**      * This will read the required data from the stream.      *       * @param ttf The font that is being read.      * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|void
name|read
parameter_list|(
name|TrueTypeFont
name|ttf
parameter_list|,
name|TTFDataStream
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|version
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|averageCharWidth
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|weightClass
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|widthClass
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|fsType
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|subscriptXSize
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|subscriptYSize
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|subscriptXOffset
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|subscriptYOffset
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|superscriptXSize
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|superscriptYSize
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|superscriptXOffset
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|superscriptYOffset
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|strikeoutSize
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|strikeoutPosition
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|familyClass
operator|=
name|data
operator|.
name|readUnsignedByte
argument_list|()
expr_stmt|;
name|familySubClass
operator|=
name|data
operator|.
name|readUnsignedByte
argument_list|()
expr_stmt|;
name|panose
operator|=
name|data
operator|.
name|read
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|unicodeRange1
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|unicodeRange2
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|unicodeRange3
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|unicodeRange4
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|achVendId
operator|=
name|data
operator|.
name|readString
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|fsSelection
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|firstCharIndex
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|lastCharIndex
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|typoAscender
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|typoDescender
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|typoLineGap
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|winAscent
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|winDescent
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
if|if
condition|(
name|version
operator|>=
literal|1
condition|)
block|{
name|codePageRange1
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
name|codePageRange2
operator|=
name|data
operator|.
name|readUnsignedInt
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|version
operator|>=
literal|1.2
condition|)
block|{
name|sxHeight
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|sCapHeight
operator|=
name|data
operator|.
name|readSignedShort
argument_list|()
expr_stmt|;
name|usDefaultChar
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|usBreakChar
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|usMaxContext
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
block|}
name|initialized
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

end_unit

