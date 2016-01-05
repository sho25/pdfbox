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
name|util
operator|.
name|filetypedetector
package|;
end_package

begin_comment
comment|/**  * @author Drew Noakes  *  * code taken from https://github.com/drewnoakes/metadata-extractor  *  * 2016-01-04  *  * latest commit number 73f1a48  *  * Enumeration of supported image file formats.  */
end_comment

begin_enum
specifier|public
enum|enum
name|FileType
block|{
name|UNKNOWN
block|,
name|JPEG
block|,
name|TIFF
block|,
name|PSD
block|,
name|PNG
block|,
name|BMP
block|,
name|GIF
block|,
name|ICO
block|,
name|PCX
block|,
name|RIFF
block|,
comment|/** Sony camera raw. */
name|ARW
block|,
comment|/** Canon camera raw, version 1. */
name|CRW
block|,
comment|/** Canon camera raw, version 2. */
name|CR2
block|,
comment|/** Nikon camera raw. */
name|NEF
block|,
comment|/** Olympus camera raw. */
name|ORF
block|,
comment|/** FujiFilm camera raw. */
name|RAF
block|,
comment|/** Panasonic camera raw. */
name|RW2
block|}
end_enum

end_unit

