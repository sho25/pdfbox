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
comment|/**  * A name record in the name table.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|NameRecord
block|{
comment|// platform ids
specifier|public
specifier|static
specifier|final
name|int
name|PLATFORM_UNICODE
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PLATFORM_MACINTOSH
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PLATFORM_ISO
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|PLATFORM_WINDOWS
init|=
literal|3
decl_stmt|;
comment|// Unicode encoding ids
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_UNICODE_1_0
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_UNICODE_1_1
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_UNICODE_2_0_BMP
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_UNICODE_2_0_FULL
init|=
literal|4
decl_stmt|;
comment|// Unicode encoding ids
specifier|public
specifier|static
specifier|final
name|int
name|LANGUGAE_UNICODE
init|=
literal|0
decl_stmt|;
comment|// Windows encoding ids
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WINDOWS_SYMBOL
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WINDOWS_UNICODE_BMP
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_WINDOWS_UNICODE_UCS4
init|=
literal|10
decl_stmt|;
comment|// Windows language ids
specifier|public
specifier|static
specifier|final
name|int
name|LANGUGAE_WINDOWS_EN_US
init|=
literal|0x0409
decl_stmt|;
comment|// Macintosh encoding ids
specifier|public
specifier|static
specifier|final
name|int
name|ENCODING_MACINTOSH_ROMAN
init|=
literal|0
decl_stmt|;
comment|// Macintosh language ids
specifier|public
specifier|static
specifier|final
name|int
name|LANGUGAE_MACINTOSH_ENGLISH
init|=
literal|0
decl_stmt|;
comment|// name ids
specifier|public
specifier|static
specifier|final
name|int
name|NAME_COPYRIGHT
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NAME_FONT_FAMILY_NAME
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NAME_FONT_SUB_FAMILY_NAME
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NAME_UNIQUE_FONT_ID
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NAME_FULL_FONT_NAME
init|=
literal|4
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NAME_VERSION
init|=
literal|5
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NAME_POSTSCRIPT_NAME
init|=
literal|6
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|NAME_TRADEMARK
init|=
literal|7
decl_stmt|;
specifier|private
name|int
name|platformId
decl_stmt|;
specifier|private
name|int
name|platformEncodingId
decl_stmt|;
specifier|private
name|int
name|languageId
decl_stmt|;
specifier|private
name|int
name|nameId
decl_stmt|;
specifier|private
name|int
name|stringLength
decl_stmt|;
specifier|private
name|int
name|stringOffset
decl_stmt|;
specifier|private
name|String
name|string
decl_stmt|;
comment|/**      * @return Returns the stringLength.      */
specifier|public
name|int
name|getStringLength
parameter_list|()
block|{
return|return
name|stringLength
return|;
block|}
comment|/**      * @param stringLengthValue The stringLength to set.      */
specifier|public
name|void
name|setStringLength
parameter_list|(
name|int
name|stringLengthValue
parameter_list|)
block|{
name|this
operator|.
name|stringLength
operator|=
name|stringLengthValue
expr_stmt|;
block|}
comment|/**      * @return Returns the stringOffset.      */
specifier|public
name|int
name|getStringOffset
parameter_list|()
block|{
return|return
name|stringOffset
return|;
block|}
comment|/**      * @param stringOffsetValue The stringOffset to set.      */
specifier|public
name|void
name|setStringOffset
parameter_list|(
name|int
name|stringOffsetValue
parameter_list|)
block|{
name|this
operator|.
name|stringOffset
operator|=
name|stringOffsetValue
expr_stmt|;
block|}
comment|/**      * @return Returns the languageId.      */
specifier|public
name|int
name|getLanguageId
parameter_list|()
block|{
return|return
name|languageId
return|;
block|}
comment|/**      * @param languageIdValue The languageId to set.      */
specifier|public
name|void
name|setLanguageId
parameter_list|(
name|int
name|languageIdValue
parameter_list|)
block|{
name|this
operator|.
name|languageId
operator|=
name|languageIdValue
expr_stmt|;
block|}
comment|/**      * @return Returns the nameId.      */
specifier|public
name|int
name|getNameId
parameter_list|()
block|{
return|return
name|nameId
return|;
block|}
comment|/**      * @param nameIdValue The nameId to set.      */
specifier|public
name|void
name|setNameId
parameter_list|(
name|int
name|nameIdValue
parameter_list|)
block|{
name|this
operator|.
name|nameId
operator|=
name|nameIdValue
expr_stmt|;
block|}
comment|/**      * @return Returns the platformEncodingId.      */
specifier|public
name|int
name|getPlatformEncodingId
parameter_list|()
block|{
return|return
name|platformEncodingId
return|;
block|}
comment|/**      * @param platformEncodingIdValue The platformEncodingId to set.      */
specifier|public
name|void
name|setPlatformEncodingId
parameter_list|(
name|int
name|platformEncodingIdValue
parameter_list|)
block|{
name|this
operator|.
name|platformEncodingId
operator|=
name|platformEncodingIdValue
expr_stmt|;
block|}
comment|/**      * @return Returns the platformId.      */
specifier|public
name|int
name|getPlatformId
parameter_list|()
block|{
return|return
name|platformId
return|;
block|}
comment|/**      * @param platformIdValue The platformId to set.      */
specifier|public
name|void
name|setPlatformId
parameter_list|(
name|int
name|platformIdValue
parameter_list|)
block|{
name|this
operator|.
name|platformId
operator|=
name|platformIdValue
expr_stmt|;
block|}
comment|/**      * This will read the required data from the stream.      *       * @param ttf The font that is being read.      * @param data The stream to read the data from.      * @throws IOException If there is an error reading the data.      */
specifier|public
name|void
name|initData
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
name|platformId
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|platformEncodingId
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|languageId
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|nameId
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|stringLength
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
name|stringOffset
operator|=
name|data
operator|.
name|readUnsignedShort
argument_list|()
expr_stmt|;
block|}
comment|/**      * Return a string representation of this class.      *       * @return A string for this class.      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"platform="
operator|+
name|platformId
operator|+
literal|" pEncoding="
operator|+
name|platformEncodingId
operator|+
literal|" language="
operator|+
name|languageId
operator|+
literal|" name="
operator|+
name|nameId
return|;
block|}
comment|/**      * @return Returns the string.      */
specifier|public
name|String
name|getString
parameter_list|()
block|{
return|return
name|string
return|;
block|}
comment|/**      * @param stringValue The string to set.      */
specifier|public
name|void
name|setString
parameter_list|(
name|String
name|stringValue
parameter_list|)
block|{
name|this
operator|.
name|string
operator|=
name|stringValue
expr_stmt|;
block|}
block|}
end_class

end_unit

