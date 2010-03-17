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
name|encoding
operator|.
name|conversion
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
name|Iterator
import|;
end_import

begin_comment
comment|/**  * This class represents PDF encoding name to Java charset name mapping.  *  * @author  Pin Xue (http://www.pinxue.net), Holly Lee (holly.lee (at) gmail.com)  * @version $Revision: 1.0 $  */
end_comment

begin_class
class|class
name|CJKEncodings
block|{
comment|// Mapping: PDF encoding name -> Java (IANA) charset name
specifier|private
specifier|static
name|HashMap
name|charsetMapping
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|CJKEncodings
parameter_list|()
block|{    }
static|static
block|{
comment|// Chinese (Simplified)
comment|// Microsoft Code Page 936 (lfCharSet 0x86), GB 2312-80 character set, EUC-CN encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"GB-EUC-H"
argument_list|,
literal|"GB2312"
argument_list|)
expr_stmt|;
comment|// Vertical version of GB-EUC-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"GB-EUC-V"
argument_list|,
literal|"GB2312"
argument_list|)
expr_stmt|;
comment|// Mac OS, GB 2312-80 character set, EUC-CN encoding, Script Manager code 19
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"GBpc-EUC-H"
argument_list|,
literal|"GB2312"
argument_list|)
expr_stmt|;
comment|// Vertical version of GBpc-EUC-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"GBpc-EUC-V"
argument_list|,
literal|"GB2312"
argument_list|)
expr_stmt|;
comment|// Microsoft Code Page 936 (lfCharSet 0x86), GBK character set, GBK encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"GBK-EUC-H"
argument_list|,
literal|"GBK"
argument_list|)
expr_stmt|;
comment|// Vertical version of GBK-EUC-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"GBK-EUC-V"
argument_list|,
literal|"GBK"
argument_list|)
expr_stmt|;
comment|// Same as GBK-EUC-H but replaces half-width Latin characters with proportional
comment|// forms and maps character code 0x24 to a dollar sign ($) instead of a yuan symbol (ââ´)
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"GBKp-EUC-H"
argument_list|,
literal|"GBK"
argument_list|)
expr_stmt|;
comment|// Vertical version of GBKp-EUC-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"GBKp-EUC-V"
argument_list|,
literal|"GBK"
argument_list|)
expr_stmt|;
comment|// GB 18030-2000 character set, mixed 1-, 2-, and 4-byte encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"GBK2K-H"
argument_list|,
literal|"GB18030"
argument_list|)
expr_stmt|;
comment|// Vertical version of GBK2K-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"GBK2K-V"
argument_list|,
literal|"GB18030"
argument_list|)
expr_stmt|;
comment|// Unicode (UCS-2) encoding for the Adobe-GB1 character collection
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniGB-UCS2-H"
argument_list|,
literal|"ISO-10646-UCS-2"
argument_list|)
expr_stmt|;
comment|// Vertical version of UniGB-UCS2-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniGB-UCS2-V"
argument_list|,
literal|"ISO-10646-UCS-2"
argument_list|)
expr_stmt|;
comment|// Unicode (UTF-16BE) encoding for the Adobe-GB1 character collection; contains mappings
comment|// for all characters in the GB18030-2000 character set
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniGB-UTF16-H"
argument_list|,
literal|"UTF-16BE"
argument_list|)
expr_stmt|;
comment|// Vertical version of UniGB-UTF16-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniGB-UTF16-V"
argument_list|,
literal|"UTF-16BE"
argument_list|)
expr_stmt|;
comment|// Chinese (Traditional)
comment|// Mac OS, Big Five character set, Big Five encoding, Script Manager code 2
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"B5pc-H"
argument_list|,
literal|"BIG5"
argument_list|)
expr_stmt|;
comment|// Vertical version of B5pc-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"B5pc-V"
argument_list|,
literal|"BIG5"
argument_list|)
expr_stmt|;
comment|// Hong Kong SCS, an extension to the Big Five character set and encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"HKscs-B5-H"
argument_list|,
literal|"Big5-HKSCS"
argument_list|)
expr_stmt|;
comment|// Vertical version of HKscs-B5-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"HKscs-B5-V"
argument_list|,
literal|"Big5-HKSCS"
argument_list|)
expr_stmt|;
comment|// Microsoft Code Page 950 (lfCharSet 0x88), Big Five character set with ETen extensions
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"ETen-B5-H"
argument_list|,
literal|"BIG5"
argument_list|)
expr_stmt|;
comment|// Vertical version of ETen-B5-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"ETen-B5-V"
argument_list|,
literal|"BIG5"
argument_list|)
expr_stmt|;
comment|// Same as ETen-B5-H but replaces half-width Latin characters with proportional forms
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"ETenms-B5-H"
argument_list|,
literal|"BIG5"
argument_list|)
expr_stmt|;
comment|// Vertical version of ETenms-B5-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"ETenms-B5-V"
argument_list|,
literal|"BIG5"
argument_list|)
expr_stmt|;
comment|// CNS 11643-1992 character set, EUC-TW encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"CNS-EUC-H"
argument_list|,
literal|"HZ"
argument_list|)
expr_stmt|;
comment|// Vertical version of CNS-EUC-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"CNS-EUC-V"
argument_list|,
literal|"HZ"
argument_list|)
expr_stmt|;
comment|// Unicode (UCS-2) encoding for the Adobe-CNS1 character collection
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniCNS-UCS2-H"
argument_list|,
literal|"ISO-10646-UCS-2"
argument_list|)
expr_stmt|;
comment|// Vertical version of UniCNS-UCS2-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniCNS-UCS2-V"
argument_list|,
literal|"ISO-10646-UCS-2"
argument_list|)
expr_stmt|;
comment|// Unicode (UTF-16BE) encoding for the Adobe-CNS1 character collection;
comment|// contains mappings for all the characters in the HKSCS-2001 character set and
comment|// contains both 2- and 4- byte character codes
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniCNS-UTF16-H"
argument_list|,
literal|"UTF-16BE"
argument_list|)
expr_stmt|;
comment|// Vertical version of UniCNS-UTF16-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniCNS-UTF16-V"
argument_list|,
literal|"UTF-16BE"
argument_list|)
expr_stmt|;
comment|//Japanese
comment|// Mac OS, JIS X 0208 character set with KanjiTalk6 extensions, Shift-JIS encoding, Script Manager code 1
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"83pv-RKSJ-H"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Microsoft Code Page 932 (lfCharSet 0x80), JIS X 0208 character set with NEC and IBM- extensions
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"90ms-RKSJ-H"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Vertical version of 90ms-RKSJ-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"90ms-RKSJ-V"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Same as 90ms-RKSJ-H but replaces half-width Latin characters with proportional forms
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"90msp-RKSJ-H"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Vertical version of 90msp-RKSJ-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"90msp-RKSJ-V"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Mac OS, JIS X 0208 character set with KanjiTalk7 extensions, Shift-JIS encoding, Script Manager code 1
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"90pv-RKSJ-H"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// JIS X 0208 character set with Fujitsu FMR extensions, Shift-JIS encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"Add-RKSJ-H"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Vertical version of Add-RKSJ-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"Add-RKSJ-V"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// JIS X 0208 character set, EUC-JP encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"EUC-H"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Vertical version of EUC-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"EUC-V"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// JIS C 6226 (JIS78) character set with NEC extensions, Shift-JIS encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"Ext-RKSJ-H"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Vertical version of Ext-RKSJ-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"Ext-RKSJ-V"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// JIS X 0208 character set, ISO-2022-JP encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"H"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Vertical version of H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"V"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Unicode (UCS-2) encoding for the Adobe-Japan1 character collection
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniJIS-UCS2-H"
argument_list|,
literal|"ISO-10646-UCS-2"
argument_list|)
expr_stmt|;
comment|// Vertical version of UniJIS-UCS2-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniJIS-UCS2-V"
argument_list|,
literal|"ISO-10646-UCS-2"
argument_list|)
expr_stmt|;
comment|// Same as UniJIS-UCS2-H but replaces proportional Latin characters with half-width forms
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniJIS-UCS2-HW-H"
argument_list|,
literal|"ISO-10646-UCS-2"
argument_list|)
expr_stmt|;
comment|// Vertical version of UniJIS-UCS2-HW-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniJIS-UCS2-HW-V"
argument_list|,
literal|"ISO-10646-UCS-2"
argument_list|)
expr_stmt|;
comment|// Unicode (UTF-16BE) encoding for the Adobe-Japan1 character collection;
comment|// contains mappings for all characters in the JIS X 0213:1000 character set
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniJIS-UTF16-H"
argument_list|,
literal|"UTF-16BE"
argument_list|)
expr_stmt|;
comment|// Vertical version of UniJIS-UTF16-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniJIS-UTF16-V"
argument_list|,
literal|"UTF-16BE"
argument_list|)
expr_stmt|;
comment|// JIS X 0208 character set, ISO-2022-JP encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"Identity-H"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|// Vertical version of H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"Identity-V"
argument_list|,
literal|"JIS"
argument_list|)
expr_stmt|;
comment|//Korean
comment|// KS X 1001:1992 character set, EUC-KR encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"KSC-EUC-H"
argument_list|,
literal|"KSC"
argument_list|)
expr_stmt|;
comment|// Vertical version of KSC-EUC-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"KSC-EUC-V"
argument_list|,
literal|"KSC"
argument_list|)
expr_stmt|;
comment|// Microsoft Code Page 949 (lfCharSet 0x81), KS X 1001:1992 character set
comment|// plus 8822.putitional hangul, Unified Hangul Code (UHC) encoding
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"KSCms-UHC-H"
argument_list|,
literal|"KSC"
argument_list|)
expr_stmt|;
comment|// Vertical version of KSCms-UHC-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"KSCms-UHC-V"
argument_list|,
literal|"KSC"
argument_list|)
expr_stmt|;
comment|// Same as KSCms-UHC-H but replaces proportional Latin characters with half-width forms
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"KSCms-UHC-HW-H"
argument_list|,
literal|"KSC"
argument_list|)
expr_stmt|;
comment|// Vertical version of KSCms-UHC-HW-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"KSCms-UHC-HW-V"
argument_list|,
literal|"KSC"
argument_list|)
expr_stmt|;
comment|// Mac OS, KS X 1001:1992 character set with Mac OS KH extensions, Script Manager Code 3
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"KSCpc-EUC-H"
argument_list|,
literal|"KSC"
argument_list|)
expr_stmt|;
comment|// Unicode (UCS-2) encoding for the Adobe-Korea1 character collection
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniKS-UCS2-H"
argument_list|,
literal|"ISO-10646-UCS-2"
argument_list|)
expr_stmt|;
comment|// Vertical version of UniKS-UCS2-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniKS-UCS2-V"
argument_list|,
literal|"ISO-10646-UCS-2"
argument_list|)
expr_stmt|;
comment|// Unicode (UTF-16BE) encoding for the Adobe-Korea1 character collection
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniKS-UTF16-H"
argument_list|,
literal|"UTF-16BE"
argument_list|)
expr_stmt|;
comment|// Vertical version of UniKS-UTF16-H
name|charsetMapping
operator|.
name|put
argument_list|(
literal|"UniKS-UTF16-V"
argument_list|,
literal|"UTF-16BE"
argument_list|)
expr_stmt|;
block|}
comment|/**     *  Get respective Java charset name from given PDF encoding name.     *     *  @param encoding   PDF encoding name     *  @return Java charset name, or null if not found     */
specifier|public
specifier|static
specifier|final
name|String
name|getCharset
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
if|if
condition|(
name|encoding
operator|.
name|startsWith
argument_list|(
literal|"COSName"
argument_list|)
condition|)
block|{
name|encoding
operator|=
name|encoding
operator|.
name|substring
argument_list|(
literal|8
argument_list|,
name|encoding
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
call|(
name|String
call|)
argument_list|(
name|charsetMapping
operator|.
name|get
argument_list|(
name|encoding
argument_list|)
argument_list|)
return|;
block|}
comment|/**     *  Return an iterator to iterate through all encodings.     */
specifier|public
specifier|static
specifier|final
name|Iterator
name|getEncodingIterator
parameter_list|()
block|{
return|return
name|charsetMapping
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
end_class

end_unit

