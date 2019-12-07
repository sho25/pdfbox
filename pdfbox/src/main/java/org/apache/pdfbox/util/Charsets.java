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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

begin_comment
comment|/**  * Utility class providing common Charsets used in PDFBox.  *  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Charsets
block|{
specifier|private
name|Charsets
parameter_list|()
block|{}
comment|/**      * ASCII charset      *      * @deprecated use {@link StandardCharsets}      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|Charset
name|US_ASCII
init|=
name|StandardCharsets
operator|.
name|US_ASCII
decl_stmt|;
comment|/**      * UTF-16BE charset      *      * @deprecated use {@link StandardCharsets}      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|Charset
name|UTF_16BE
init|=
name|StandardCharsets
operator|.
name|UTF_16BE
decl_stmt|;
comment|/**      * UTF-16LE charset      *      * @deprecated use {@link StandardCharsets}      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|Charset
name|UTF_16LE
init|=
name|StandardCharsets
operator|.
name|UTF_16LE
decl_stmt|;
comment|/**      * ISO-8859-1 charset      *      * @deprecated use {@link StandardCharsets}      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|Charset
name|ISO_8859_1
init|=
name|StandardCharsets
operator|.
name|ISO_8859_1
decl_stmt|;
comment|/**      * Windows-1252 charset      */
specifier|public
specifier|static
specifier|final
name|Charset
name|WINDOWS_1252
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"Windows-1252"
argument_list|)
decl_stmt|;
comment|/**      * UTF-8 charset      *      * @deprecated use {@link StandardCharsets}      */
annotation|@
name|Deprecated
specifier|public
specifier|static
specifier|final
name|Charset
name|UTF_8
init|=
name|StandardCharsets
operator|.
name|UTF_8
decl_stmt|;
block|}
end_class

end_unit

