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
name|interactive
operator|.
name|form
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

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
name|List
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
name|PDDocument
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
name|interactive
operator|.
name|annotation
operator|.
name|PDAnnotationWidget
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|HandleDifferentDALevelsTest
block|{
specifier|private
specifier|static
specifier|final
name|File
name|OUT_DIR
init|=
operator|new
name|File
argument_list|(
literal|"target/test-output"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|File
name|IN_DIR
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/pdfbox/pdmodel/interactive/form"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NAME_OF_PDF
init|=
literal|"DifferentDALevels.pdf"
decl_stmt|;
specifier|private
name|PDDocument
name|document
decl_stmt|;
specifier|private
name|PDAcroForm
name|acroForm
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|IOException
block|{
name|document
operator|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|File
argument_list|(
name|IN_DIR
argument_list|,
name|NAME_OF_PDF
argument_list|)
argument_list|)
expr_stmt|;
name|acroForm
operator|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
expr_stmt|;
name|OUT_DIR
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
comment|// prefill the fields to generate the appearance streams
name|PDTextField
name|field
init|=
operator|(
name|PDTextField
operator|)
name|acroForm
operator|.
name|getField
argument_list|(
literal|"SingleAnnotation"
argument_list|)
decl_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"single annotation"
argument_list|)
expr_stmt|;
name|field
operator|=
operator|(
name|PDTextField
operator|)
name|acroForm
operator|.
name|getField
argument_list|(
literal|"MultipeAnnotations-SameLayout"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"same layout"
argument_list|)
expr_stmt|;
name|field
operator|=
operator|(
name|PDTextField
operator|)
name|acroForm
operator|.
name|getField
argument_list|(
literal|"MultipleAnnotations-DifferentLayout"
argument_list|)
expr_stmt|;
name|field
operator|.
name|setValue
argument_list|(
literal|"different layout"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|OUT_DIR
argument_list|,
name|NAME_OF_PDF
argument_list|)
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|checkSingleAnnotation
parameter_list|()
throws|throws
name|IOException
block|{
name|PDTextField
name|field
init|=
operator|(
name|PDTextField
operator|)
name|acroForm
operator|.
name|getField
argument_list|(
literal|"SingleAnnotation"
argument_list|)
decl_stmt|;
name|String
name|fieldFontSetting
init|=
name|getFontSettingFromDA
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PDAnnotationWidget
argument_list|>
name|widgets
init|=
name|field
operator|.
name|getWidgets
argument_list|()
decl_stmt|;
for|for
control|(
name|PDAnnotationWidget
name|widget
range|:
name|widgets
control|)
block|{
name|String
name|contentAsString
init|=
operator|new
name|String
argument_list|(
name|widget
operator|.
name|getNormalAppearanceStream
argument_list|()
operator|.
name|getContentStream
argument_list|()
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|contentAsString
operator|.
name|indexOf
argument_list|(
name|fieldFontSetting
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|checkSameLayout
parameter_list|()
throws|throws
name|IOException
block|{
name|PDTextField
name|field
init|=
operator|(
name|PDTextField
operator|)
name|acroForm
operator|.
name|getField
argument_list|(
literal|"MultipeAnnotations-SameLayout"
argument_list|)
decl_stmt|;
name|String
name|fieldFontSetting
init|=
name|getFontSettingFromDA
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PDAnnotationWidget
argument_list|>
name|widgets
init|=
name|field
operator|.
name|getWidgets
argument_list|()
decl_stmt|;
for|for
control|(
name|PDAnnotationWidget
name|widget
range|:
name|widgets
control|)
block|{
name|String
name|contentAsString
init|=
operator|new
name|String
argument_list|(
name|widget
operator|.
name|getNormalAppearanceStream
argument_list|()
operator|.
name|getContentStream
argument_list|()
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"font setting in content stream shall be "
operator|+
name|fieldFontSetting
argument_list|,
name|contentAsString
operator|.
name|indexOf
argument_list|(
name|fieldFontSetting
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO: enable the test after issue 3687 has been fixed
comment|// @Test
specifier|public
name|void
name|checkDifferentLayout
parameter_list|()
throws|throws
name|IOException
block|{
name|PDTextField
name|field
init|=
operator|(
name|PDTextField
operator|)
name|acroForm
operator|.
name|getField
argument_list|(
literal|"MultipleAnnotations-DifferentLayout"
argument_list|)
decl_stmt|;
name|String
name|fieldFontSetting
init|=
name|getFontSettingFromDA
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PDAnnotationWidget
argument_list|>
name|widgets
init|=
name|field
operator|.
name|getWidgets
argument_list|()
decl_stmt|;
for|for
control|(
name|PDAnnotationWidget
name|widget
range|:
name|widgets
control|)
block|{
name|String
name|widgetFontSetting
init|=
name|getFontSettingFromDA
argument_list|(
name|widget
argument_list|)
decl_stmt|;
name|String
name|fontSetting
init|=
name|widgetFontSetting
operator|==
literal|null
condition|?
name|fieldFontSetting
else|:
name|widgetFontSetting
decl_stmt|;
name|String
name|contentAsString
init|=
operator|new
name|String
argument_list|(
name|widget
operator|.
name|getNormalAppearanceStream
argument_list|()
operator|.
name|getContentStream
argument_list|()
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"font setting in content stream shall be "
operator|+
name|fontSetting
argument_list|,
name|contentAsString
operator|.
name|indexOf
argument_list|(
name|fontSetting
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|IOException
block|{
name|document
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|private
name|String
name|getFontSettingFromDA
parameter_list|(
name|PDTextField
name|field
parameter_list|)
block|{
name|String
name|defaultAppearance
init|=
name|field
operator|.
name|getDefaultAppearance
argument_list|()
decl_stmt|;
comment|// get the font setting from the default appearance string
return|return
name|defaultAppearance
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|defaultAppearance
operator|.
name|lastIndexOf
argument_list|(
literal|"Tf"
argument_list|)
operator|+
literal|2
argument_list|)
return|;
block|}
specifier|private
name|String
name|getFontSettingFromDA
parameter_list|(
name|PDAnnotationWidget
name|widget
parameter_list|)
block|{
name|String
name|defaultAppearance
init|=
name|widget
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|DA
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultAppearance
operator|!=
literal|null
condition|)
block|{
return|return
name|defaultAppearance
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|defaultAppearance
operator|.
name|lastIndexOf
argument_list|(
literal|"Tf"
argument_list|)
operator|+
literal|2
argument_list|)
return|;
block|}
return|return
name|defaultAppearance
return|;
block|}
block|}
end_class

end_unit

