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
name|documentinterchange
operator|.
name|taggedpdf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
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
name|Collections
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

begin_comment
comment|/**  * The standard structure types.  *   * @author Johannes Koch  */
end_comment

begin_class
specifier|public
class|class
name|StandardStructureTypes
block|{
comment|/**      * Log instance.      */
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
name|StandardStructureTypes
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Grouping Elements
comment|/**      * Document      */
specifier|public
specifier|static
specifier|final
name|String
name|DOCUMENT
init|=
literal|"Document"
decl_stmt|;
comment|/**      * Part      */
specifier|public
specifier|static
specifier|final
name|String
name|PART
init|=
literal|"Part"
decl_stmt|;
comment|/**      * Art      */
specifier|public
specifier|static
specifier|final
name|String
name|ART
init|=
literal|"Art"
decl_stmt|;
comment|/**      * Sect      */
specifier|public
specifier|static
specifier|final
name|String
name|SECT
init|=
literal|"Sect"
decl_stmt|;
comment|/**      * Div      */
specifier|public
specifier|static
specifier|final
name|String
name|DIV
init|=
literal|"Div"
decl_stmt|;
comment|/**      * BlockQuote      */
specifier|public
specifier|static
specifier|final
name|String
name|BLOCK_QUOTE
init|=
literal|"BlockQuote"
decl_stmt|;
comment|/**      * Caption      */
specifier|public
specifier|static
specifier|final
name|String
name|CAPTION
init|=
literal|"Caption"
decl_stmt|;
comment|/**      * TOC      */
specifier|public
specifier|static
specifier|final
name|String
name|TOC
init|=
literal|"TOC"
decl_stmt|;
comment|/**      * TOCI      */
specifier|public
specifier|static
specifier|final
name|String
name|TOCI
init|=
literal|"TOCI"
decl_stmt|;
comment|/**      * Index      */
specifier|public
specifier|static
specifier|final
name|String
name|INDEX
init|=
literal|"Index"
decl_stmt|;
comment|/**      * NonStruct      */
specifier|public
specifier|static
specifier|final
name|String
name|NON_STRUCT
init|=
literal|"NonStruct"
decl_stmt|;
comment|/**      * Private      */
specifier|public
specifier|static
specifier|final
name|String
name|PRIVATE
init|=
literal|"Private"
decl_stmt|;
comment|// Block-Level Structure Elements
comment|/**      * P      */
specifier|public
specifier|static
specifier|final
name|String
name|P
init|=
literal|"P"
decl_stmt|;
comment|/**      * H      */
specifier|public
specifier|static
specifier|final
name|String
name|H
init|=
literal|"H"
decl_stmt|;
comment|/**      * H1      */
specifier|public
specifier|static
specifier|final
name|String
name|H1
init|=
literal|"H1"
decl_stmt|;
comment|/**      * H2      */
specifier|public
specifier|static
specifier|final
name|String
name|H2
init|=
literal|"H2"
decl_stmt|;
comment|/**      * H3      */
specifier|public
specifier|static
specifier|final
name|String
name|H3
init|=
literal|"H3"
decl_stmt|;
comment|/**      * H4      */
specifier|public
specifier|static
specifier|final
name|String
name|H4
init|=
literal|"H4"
decl_stmt|;
comment|/**      * H5      */
specifier|public
specifier|static
specifier|final
name|String
name|H5
init|=
literal|"H5"
decl_stmt|;
comment|/**      * H6      */
specifier|public
specifier|static
specifier|final
name|String
name|H6
init|=
literal|"H6"
decl_stmt|;
comment|/**      * L      */
specifier|public
specifier|static
specifier|final
name|String
name|L
init|=
literal|"L"
decl_stmt|;
comment|/**      * LI      */
specifier|public
specifier|static
specifier|final
name|String
name|LI
init|=
literal|"LI"
decl_stmt|;
comment|/**      * Lbl      */
specifier|public
specifier|static
specifier|final
name|String
name|LBL
init|=
literal|"Lbl"
decl_stmt|;
comment|/**      * LBody      */
specifier|public
specifier|static
specifier|final
name|String
name|L_BODY
init|=
literal|"LBody"
decl_stmt|;
comment|/**      * Table      */
specifier|public
specifier|static
specifier|final
name|String
name|TABLE
init|=
literal|"Table"
decl_stmt|;
comment|/**      * TR      */
specifier|public
specifier|static
specifier|final
name|String
name|TR
init|=
literal|"TR"
decl_stmt|;
comment|/**      * TH      */
specifier|public
specifier|static
specifier|final
name|String
name|TH
init|=
literal|"TH"
decl_stmt|;
comment|/**      * TD      */
specifier|public
specifier|static
specifier|final
name|String
name|TD
init|=
literal|"TD"
decl_stmt|;
comment|/**      * THead      */
specifier|public
specifier|static
specifier|final
name|String
name|T_HEAD
init|=
literal|"THead"
decl_stmt|;
comment|/**      * TBody      */
specifier|public
specifier|static
specifier|final
name|String
name|T_BODY
init|=
literal|"TBody"
decl_stmt|;
comment|/**      * TFoot      */
specifier|public
specifier|static
specifier|final
name|String
name|T_FOOT
init|=
literal|"TFoot"
decl_stmt|;
comment|// Inline-Level Structure Elements
comment|/**      * Span      */
specifier|public
specifier|static
specifier|final
name|String
name|SPAN
init|=
literal|"Span"
decl_stmt|;
comment|/**      * Quote      */
specifier|public
specifier|static
specifier|final
name|String
name|QUOTE
init|=
literal|"Quote"
decl_stmt|;
comment|/**      * Note      */
specifier|public
specifier|static
specifier|final
name|String
name|NOTE
init|=
literal|"Note"
decl_stmt|;
comment|/**      * Reference      */
specifier|public
specifier|static
specifier|final
name|String
name|REFERENCE
init|=
literal|"Reference"
decl_stmt|;
comment|/**      * BibEntry      */
specifier|public
specifier|static
specifier|final
name|String
name|BIB_ENTRY
init|=
literal|"BibEntry"
decl_stmt|;
comment|/**      * Code      */
specifier|public
specifier|static
specifier|final
name|String
name|CODE
init|=
literal|"Code"
decl_stmt|;
comment|/**      * Link      */
specifier|public
specifier|static
specifier|final
name|String
name|LINK
init|=
literal|"Link"
decl_stmt|;
comment|/**      * Annot      */
specifier|public
specifier|static
specifier|final
name|String
name|ANNOT
init|=
literal|"Annot"
decl_stmt|;
comment|/**      * Ruby      */
specifier|public
specifier|static
specifier|final
name|String
name|RUBY
init|=
literal|"Ruby"
decl_stmt|;
comment|/**      * RB      */
specifier|public
specifier|static
specifier|final
name|String
name|RB
init|=
literal|"RB"
decl_stmt|;
comment|/**      * RT      */
specifier|public
specifier|static
specifier|final
name|String
name|RT
init|=
literal|"RT"
decl_stmt|;
comment|/**      * RP      */
specifier|public
specifier|static
specifier|final
name|String
name|RP
init|=
literal|"RP"
decl_stmt|;
comment|/**      * Warichu      */
specifier|public
specifier|static
specifier|final
name|String
name|WARICHU
init|=
literal|"Warichu"
decl_stmt|;
comment|/**      * WT      */
specifier|public
specifier|static
specifier|final
name|String
name|WT
init|=
literal|"WT"
decl_stmt|;
comment|/**      * WP      */
specifier|public
specifier|static
specifier|final
name|String
name|WP
init|=
literal|"WP"
decl_stmt|;
comment|// Illustration Elements
comment|/**      * Figure      */
specifier|public
specifier|static
specifier|final
name|String
name|Figure
init|=
literal|"Figure"
decl_stmt|;
comment|/**      * Formula      */
specifier|public
specifier|static
specifier|final
name|String
name|FORMULA
init|=
literal|"Formula"
decl_stmt|;
comment|/**      * Form      */
specifier|public
specifier|static
specifier|final
name|String
name|FORM
init|=
literal|"Form"
decl_stmt|;
comment|/**      * All standard structure types.      */
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|types
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|Field
index|[]
name|fields
init|=
name|StandardStructureTypes
operator|.
name|class
operator|.
name|getFields
argument_list|()
decl_stmt|;
for|for
control|(
name|Field
name|field
range|:
name|fields
control|)
block|{
if|if
condition|(
name|Modifier
operator|.
name|isFinal
argument_list|(
name|field
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|types
operator|.
name|add
argument_list|(
name|field
operator|.
name|get
argument_list|(
literal|null
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
decl||
name|IllegalAccessException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|types
argument_list|)
expr_stmt|;
block|}
specifier|private
name|StandardStructureTypes
parameter_list|()
block|{     }
block|}
end_class

end_unit

