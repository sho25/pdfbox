begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|cos
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
name|OutputStream
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
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|exceptions
operator|.
name|COSVisitorException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|persistence
operator|.
name|util
operator|.
name|COSHEXTable
import|;
end_import

begin_comment
comment|/**  * This class represents a PDF named object.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.42 $  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|COSName
extends|extends
name|COSBase
implements|implements
name|Comparable
block|{
comment|/**      * Note: This is synchronized because a HashMap must be synchronized if accessed by      * multiple threads.      */
specifier|private
specifier|static
name|Map
name|nameMap
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|HashMap
argument_list|(
literal|8192
argument_list|)
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|A
init|=
operator|new
name|COSName
argument_list|(
literal|"A"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|AA
init|=
operator|new
name|COSName
argument_list|(
literal|"AA"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|ACRO_FORM
init|=
operator|new
name|COSName
argument_list|(
literal|"AcroForm"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|ANNOTS
init|=
operator|new
name|COSName
argument_list|(
literal|"Annots"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|ART_BOX
init|=
operator|new
name|COSName
argument_list|(
literal|"ArtBox"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|ASCII85_DECODE
init|=
operator|new
name|COSName
argument_list|(
literal|"ASCII85Decode"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|ASCII85_DECODE_ABBREVIATION
init|=
operator|new
name|COSName
argument_list|(
literal|"A85"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|ASCII_HEX_DECODE
init|=
operator|new
name|COSName
argument_list|(
literal|"ASCIIHexDecode"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|ASCII_HEX_DECODE_ABBREVIATION
init|=
operator|new
name|COSName
argument_list|(
literal|"AHx"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|AP
init|=
operator|new
name|COSName
argument_list|(
literal|"AP"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|B
init|=
operator|new
name|COSName
argument_list|(
literal|"B"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|BASE_ENCODING
init|=
operator|new
name|COSName
argument_list|(
literal|"BaseEncoding"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|BASE_FONT
init|=
operator|new
name|COSName
argument_list|(
literal|"BaseFont"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|BBOX
init|=
operator|new
name|COSName
argument_list|(
literal|"BBox"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|BLEED_BOX
init|=
operator|new
name|COSName
argument_list|(
literal|"BleedBox"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|CATALOG
init|=
operator|new
name|COSName
argument_list|(
literal|"Catalog"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|CALGRAY
init|=
operator|new
name|COSName
argument_list|(
literal|"CalGray"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|CALRGB
init|=
operator|new
name|COSName
argument_list|(
literal|"CalRGB"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|CCITTFAX_DECODE
init|=
operator|new
name|COSName
argument_list|(
literal|"CCITTFaxDecode"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|CCITTFAX_DECODE_ABBREVIATION
init|=
operator|new
name|COSName
argument_list|(
literal|"CCF"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|COLORSPACE
init|=
operator|new
name|COSName
argument_list|(
literal|"ColorSpace"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|CONTENTS
init|=
operator|new
name|COSName
argument_list|(
literal|"Contents"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|COUNT
init|=
operator|new
name|COSName
argument_list|(
literal|"Count"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|CROP_BOX
init|=
operator|new
name|COSName
argument_list|(
literal|"CropBox"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|DCT_DECODE
init|=
operator|new
name|COSName
argument_list|(
literal|"DCTDecode"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|DCT_DECODE_ABBREVIATION
init|=
operator|new
name|COSName
argument_list|(
literal|"DCT"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|DESCENDANT_FONTS
init|=
operator|new
name|COSName
argument_list|(
literal|"DescendantFonts"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|DEST
init|=
operator|new
name|COSName
argument_list|(
literal|"Dest"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|DEVICECMYK
init|=
operator|new
name|COSName
argument_list|(
literal|"DeviceCMYK"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|DEVICEGRAY
init|=
operator|new
name|COSName
argument_list|(
literal|"DeviceGray"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|DEVICEN
init|=
operator|new
name|COSName
argument_list|(
literal|"DeviceN"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|DEVICERGB
init|=
operator|new
name|COSName
argument_list|(
literal|"DeviceRGB"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|DIFFERENCES
init|=
operator|new
name|COSName
argument_list|(
literal|"Differences"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|DV
init|=
operator|new
name|COSName
argument_list|(
literal|"DV"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|ENCODING
init|=
operator|new
name|COSName
argument_list|(
literal|"Encoding"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|ENCODING_90MS_RKSJ_H
init|=
operator|new
name|COSName
argument_list|(
literal|"90ms-RKSJ-H"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|ENCODING_90MS_RKSJ_V
init|=
operator|new
name|COSName
argument_list|(
literal|"90ms-RKSJ-V"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|ENCODING_ETEN_B5_H
init|=
operator|new
name|COSName
argument_list|(
literal|"ETen?B5?H"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|ENCODING_ETEN_B5_V
init|=
operator|new
name|COSName
argument_list|(
literal|"ETen?B5?V"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|FIELDS
init|=
operator|new
name|COSName
argument_list|(
literal|"Fields"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|FILTER
init|=
operator|new
name|COSName
argument_list|(
literal|"Filter"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|FIRST_CHAR
init|=
operator|new
name|COSName
argument_list|(
literal|"FirstChar"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|FLATE_DECODE
init|=
operator|new
name|COSName
argument_list|(
literal|"FlateDecode"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|FLATE_DECODE_ABBREVIATION
init|=
operator|new
name|COSName
argument_list|(
literal|"Fl"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|FONT
init|=
operator|new
name|COSName
argument_list|(
literal|"Font"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|FONT_FILE
init|=
operator|new
name|COSName
argument_list|(
literal|"FontFile"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|FONT_FILE2
init|=
operator|new
name|COSName
argument_list|(
literal|"FontFile2"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|FONT_FILE3
init|=
operator|new
name|COSName
argument_list|(
literal|"FontFile3"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|FONT_DESC
init|=
operator|new
name|COSName
argument_list|(
literal|"FontDescriptor"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|FONT_MATRIX
init|=
operator|new
name|COSName
argument_list|(
literal|"FontMatrix"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|FORMTYPE
init|=
operator|new
name|COSName
argument_list|(
literal|"FormType"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|FRM
init|=
operator|new
name|COSName
argument_list|(
literal|"FRM"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|H
init|=
operator|new
name|COSName
argument_list|(
literal|"H"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|HEIGHT
init|=
operator|new
name|COSName
argument_list|(
literal|"Height"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|ICCBASED
init|=
operator|new
name|COSName
argument_list|(
literal|"ICCBased"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|IDENTITY_H
init|=
operator|new
name|COSName
argument_list|(
literal|"Identity-H"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|IMAGE
init|=
operator|new
name|COSName
argument_list|(
literal|"Image"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|INDEXED
init|=
operator|new
name|COSName
argument_list|(
literal|"Indexed"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|INFO
init|=
operator|new
name|COSName
argument_list|(
literal|"Info"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|JPX_DECODE
init|=
operator|new
name|COSName
argument_list|(
literal|"JPXDecode"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|KIDS
init|=
operator|new
name|COSName
argument_list|(
literal|"Kids"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|LAB
init|=
operator|new
name|COSName
argument_list|(
literal|"Lab"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|LAST_CHAR
init|=
operator|new
name|COSName
argument_list|(
literal|"LastChar"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|LENGTH
init|=
operator|new
name|COSName
argument_list|(
literal|"Length"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|LENGTH1
init|=
operator|new
name|COSName
argument_list|(
literal|"Length1"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|LZW_DECODE
init|=
operator|new
name|COSName
argument_list|(
literal|"LZWDecode"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|LZW_DECODE_ABBREVIATION
init|=
operator|new
name|COSName
argument_list|(
literal|"LZW"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|MAC_ROMAN_ENCODING
init|=
operator|new
name|COSName
argument_list|(
literal|"MacRomanEncoding"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|MATRIX
init|=
operator|new
name|COSName
argument_list|(
literal|"Matrix"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|MEDIA_BOX
init|=
operator|new
name|COSName
argument_list|(
literal|"MediaBox"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|METADATA
init|=
operator|new
name|COSName
argument_list|(
literal|"Metadata"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|N
init|=
operator|new
name|COSName
argument_list|(
literal|"N"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|NAME
init|=
operator|new
name|COSName
argument_list|(
literal|"Name"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|P
init|=
operator|new
name|COSName
argument_list|(
literal|"P"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|PAGE
init|=
operator|new
name|COSName
argument_list|(
literal|"Page"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|PAGES
init|=
operator|new
name|COSName
argument_list|(
literal|"Pages"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|PARENT
init|=
operator|new
name|COSName
argument_list|(
literal|"Parent"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|PATTERN
init|=
operator|new
name|COSName
argument_list|(
literal|"Pattern"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|PDF_DOC_ENCODING
init|=
operator|new
name|COSName
argument_list|(
literal|"PDFDocEncoding"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|PREV
init|=
operator|new
name|COSName
argument_list|(
literal|"Prev"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|R
init|=
operator|new
name|COSName
argument_list|(
literal|"R"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|RESOURCES
init|=
operator|new
name|COSName
argument_list|(
literal|"Resources"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|ROOT
init|=
operator|new
name|COSName
argument_list|(
literal|"Root"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|ROTATE
init|=
operator|new
name|COSName
argument_list|(
literal|"Rotate"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|RUN_LENGTH_DECODE
init|=
operator|new
name|COSName
argument_list|(
literal|"RunLengthDecode"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|RUN_LENGTH_DECODE_ABBREVIATION
init|=
operator|new
name|COSName
argument_list|(
literal|"RL"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|SEPARATION
init|=
operator|new
name|COSName
argument_list|(
literal|"Separation"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|STANDARD_ENCODING
init|=
operator|new
name|COSName
argument_list|(
literal|"StandardEncoding"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|SUBTYPE
init|=
operator|new
name|COSName
argument_list|(
literal|"Subtype"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|TRIM_BOX
init|=
operator|new
name|COSName
argument_list|(
literal|"TrimBox"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|TRUE_TYPE
init|=
operator|new
name|COSName
argument_list|(
literal|"TrueType"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|TO_UNICODE
init|=
operator|new
name|COSName
argument_list|(
literal|"ToUnicode"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|TYPE
init|=
operator|new
name|COSName
argument_list|(
literal|"Type"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|TYPE0
init|=
operator|new
name|COSName
argument_list|(
literal|"Type0"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|V
init|=
operator|new
name|COSName
argument_list|(
literal|"V"
argument_list|)
decl_stmt|;
comment|/**      * A common COSName value.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|VERSION
init|=
operator|new
name|COSName
argument_list|(
literal|"Version"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|WIDTHS
init|=
operator|new
name|COSName
argument_list|(
literal|"Widths"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|WIN_ANSI_ENCODING
init|=
operator|new
name|COSName
argument_list|(
literal|"WinAnsiEncoding"
argument_list|)
decl_stmt|;
comment|/**     * A common COSName value.     */
specifier|public
specifier|static
specifier|final
name|COSName
name|XOBJECT
init|=
operator|new
name|COSName
argument_list|(
literal|"XObject"
argument_list|)
decl_stmt|;
comment|/**      * The prefix to a PDF name.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|NAME_PREFIX
init|=
operator|new
name|byte
index|[]
block|{
literal|47
block|}
decl_stmt|;
comment|// The / character
comment|/**      * The escape character for a name.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|NAME_ESCAPE
init|=
operator|new
name|byte
index|[]
block|{
literal|35
block|}
decl_stmt|;
comment|//The # character
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|int
name|hashCode
decl_stmt|;
comment|/**      * This will get a COSName object with that name.      *      * @param aName The name of the object.      *      * @return A COSName with the specified name.      */
specifier|public
specifier|static
specifier|final
name|COSName
name|getPDFName
parameter_list|(
name|String
name|aName
parameter_list|)
block|{
name|COSName
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|aName
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
operator|(
name|COSName
operator|)
name|nameMap
operator|.
name|get
argument_list|(
name|aName
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
comment|//name is added to map in the constructor
name|name
operator|=
operator|new
name|COSName
argument_list|(
name|aName
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|name
return|;
block|}
comment|/**      * Private constructor.  This will limit the number of COSName objects.      * that are created.      *      * @param aName The name of the COSName object.      */
specifier|private
name|COSName
parameter_list|(
name|String
name|aName
parameter_list|)
block|{
name|name
operator|=
name|aName
expr_stmt|;
name|nameMap
operator|.
name|put
argument_list|(
name|aName
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|hashCode
operator|=
name|name
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will get the name of this COSName object.      *      * @return The name of the object.      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"COSName{"
operator|+
name|name
operator|+
literal|"}"
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|boolean
name|retval
init|=
name|this
operator|==
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|retval
operator|&&
name|o
operator|instanceof
name|COSName
condition|)
block|{
name|COSName
name|other
init|=
operator|(
name|COSName
operator|)
name|o
decl_stmt|;
name|retval
operator|=
name|name
operator|==
name|other
operator|.
name|name
operator|||
name|name
operator|.
name|equals
argument_list|(
name|other
operator|.
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|hashCode
return|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|int
name|compareTo
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
name|COSName
name|other
init|=
operator|(
name|COSName
operator|)
name|o
decl_stmt|;
return|return
name|this
operator|.
name|name
operator|.
name|compareTo
argument_list|(
name|other
operator|.
name|name
argument_list|)
return|;
block|}
comment|/**      * visitor pattern double dispatch method.      *      * @param visitor The object to notify when visiting this object.      * @return any object, depending on the visitor implementation, or null      * @throws COSVisitorException If an error occurs while visiting this object.      */
specifier|public
name|Object
name|accept
parameter_list|(
name|ICOSVisitor
name|visitor
parameter_list|)
throws|throws
name|COSVisitorException
block|{
return|return
name|visitor
operator|.
name|visitFromName
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * This will output this string as a PDF object.      *      * @param output The stream to write to.      * @throws IOException If there is an error writing to the stream.      */
specifier|public
name|void
name|writePDF
parameter_list|(
name|OutputStream
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|output
operator|.
name|write
argument_list|(
name|NAME_PREFIX
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|getName
argument_list|()
operator|.
name|getBytes
argument_list|()
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
name|bytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|int
name|current
init|=
operator|(
operator|(
name|bytes
index|[
name|i
index|]
operator|+
literal|256
operator|)
operator|%
literal|256
operator|)
decl_stmt|;
if|if
condition|(
name|current
operator|<=
literal|32
operator|||
name|current
operator|>=
literal|127
operator|||
name|current
operator|==
literal|'('
operator|||
name|current
operator|==
literal|')'
operator|||
name|current
operator|==
literal|'['
operator|||
name|current
operator|==
literal|']'
operator|||
name|current
operator|==
literal|'/'
operator|||
name|current
operator|==
literal|'%'
operator|||
name|current
operator|==
literal|'<'
operator|||
name|current
operator|==
literal|'>'
operator|||
name|current
operator|==
name|NAME_ESCAPE
index|[
literal|0
index|]
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
name|NAME_ESCAPE
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|COSHEXTable
operator|.
name|TABLE
index|[
name|current
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|output
operator|.
name|write
argument_list|(
name|current
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**       * Not usually needed except if resources need to be reclaimed in a ong       * running process.       * Patch provided by flester@GMail.com       * incorporated 5/23/08, Danielwilson@users.SourceForge.net       */
specifier|public
specifier|static
specifier|synchronized
name|void
name|clearResources
parameter_list|()
block|{
comment|// Clear them all
name|nameMap
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// Add the statics back in
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
name|f
index|[]
init|=
name|COSName
operator|.
name|class
operator|.
name|getFields
argument_list|()
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
operator|&&
name|f
operator|.
name|length
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|f
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|Object
name|obj
init|=
name|f
index|[
name|i
index|]
operator|.
name|get
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
operator|&&
name|obj
operator|instanceof
name|COSName
condition|)
block|{
name|COSName
name|cosname
init|=
operator|(
name|COSName
operator|)
name|obj
decl_stmt|;
name|nameMap
operator|.
name|put
argument_list|(
name|cosname
operator|.
name|getName
argument_list|()
argument_list|,
name|cosname
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ignore
parameter_list|)
block|{}
block|}
block|}
block|}
block|}
end_class

end_unit

