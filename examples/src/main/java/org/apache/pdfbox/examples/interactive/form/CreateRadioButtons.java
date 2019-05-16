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
name|examples
operator|.
name|interactive
operator|.
name|form
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
name|COSDictionary
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
name|PDAppearanceContentStream
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
name|PDPage
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
name|PDPageContentStream
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
name|common
operator|.
name|PDRectangle
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
name|font
operator|.
name|PDType1Font
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
name|graphics
operator|.
name|color
operator|.
name|PDColor
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
name|graphics
operator|.
name|color
operator|.
name|PDDeviceRGB
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
name|PDAppearanceCharacteristicsDictionary
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
name|PDAppearanceDictionary
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
name|PDAppearanceEntry
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
name|PDAppearanceStream
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
name|PDBorderStyleDictionary
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
name|form
operator|.
name|PDAcroForm
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
name|form
operator|.
name|PDRadioButton
import|;
end_import

begin_comment
comment|/**  * Example to create radio buttons.  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|CreateRadioButtons
block|{
specifier|private
name|CreateRadioButtons
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
init|)
block|{
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDAcroForm
name|acroForm
init|=
operator|new
name|PDAcroForm
argument_list|(
name|document
argument_list|)
decl_stmt|;
comment|// if you want to see what Adobe does, activate this, open with Adobe
comment|// save the file, and then open it with PDFDebugger
comment|//acroForm.setNeedAppearances(true)
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|setAcroForm
argument_list|(
name|acroForm
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|,
literal|"c"
argument_list|)
decl_stmt|;
name|PDRadioButton
name|radioButton
init|=
operator|new
name|PDRadioButton
argument_list|(
name|acroForm
argument_list|)
decl_stmt|;
name|radioButton
operator|.
name|setPartialName
argument_list|(
literal|"MyRadioButton"
argument_list|)
expr_stmt|;
name|radioButton
operator|.
name|setExportValues
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|PDAppearanceCharacteristicsDictionary
name|appearanceCharacteristics
init|=
operator|new
name|PDAppearanceCharacteristicsDictionary
argument_list|(
operator|new
name|COSDictionary
argument_list|()
argument_list|)
decl_stmt|;
name|appearanceCharacteristics
operator|.
name|setBorderColour
argument_list|(
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|1
block|,
literal|0
block|,
literal|0
block|}
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
argument_list|)
expr_stmt|;
name|appearanceCharacteristics
operator|.
name|setBackground
argument_list|(
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0
block|,
literal|1
block|,
literal|0.3f
block|}
argument_list|,
name|PDDeviceRGB
operator|.
name|INSTANCE
argument_list|)
argument_list|)
expr_stmt|;
comment|// no caption => round
comment|// with caption => see checkbox example
name|List
argument_list|<
name|PDAnnotationWidget
argument_list|>
name|widgets
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|options
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PDAnnotationWidget
name|widget
init|=
operator|new
name|PDAnnotationWidget
argument_list|()
decl_stmt|;
name|widget
operator|.
name|setRectangle
argument_list|(
operator|new
name|PDRectangle
argument_list|(
literal|30
argument_list|,
name|PDRectangle
operator|.
name|A4
operator|.
name|getHeight
argument_list|()
operator|-
literal|40
operator|-
name|i
operator|*
literal|35
argument_list|,
literal|30
argument_list|,
literal|30
argument_list|)
argument_list|)
expr_stmt|;
name|widget
operator|.
name|setAppearanceCharacteristics
argument_list|(
name|appearanceCharacteristics
argument_list|)
expr_stmt|;
name|PDBorderStyleDictionary
name|borderStyleDictionary
init|=
operator|new
name|PDBorderStyleDictionary
argument_list|()
decl_stmt|;
name|borderStyleDictionary
operator|.
name|setWidth
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|borderStyleDictionary
operator|.
name|setStyle
argument_list|(
name|PDBorderStyleDictionary
operator|.
name|STYLE_SOLID
argument_list|)
expr_stmt|;
name|widget
operator|.
name|setBorderStyle
argument_list|(
name|borderStyleDictionary
argument_list|)
expr_stmt|;
name|widget
operator|.
name|setPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|COSDictionary
name|apNDict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|apNDict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|Off
argument_list|,
name|createAppearanceStream
argument_list|(
name|document
argument_list|,
name|widget
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|apNDict
operator|.
name|setItem
argument_list|(
name|options
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|createAppearanceStream
argument_list|(
name|document
argument_list|,
name|widget
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|PDAppearanceDictionary
name|appearance
init|=
operator|new
name|PDAppearanceDictionary
argument_list|()
decl_stmt|;
name|PDAppearanceEntry
name|appearanceNEntry
init|=
operator|new
name|PDAppearanceEntry
argument_list|(
name|apNDict
argument_list|)
decl_stmt|;
name|appearance
operator|.
name|setNormalAppearance
argument_list|(
name|appearanceNEntry
argument_list|)
expr_stmt|;
name|widget
operator|.
name|setAppearance
argument_list|(
name|appearance
argument_list|)
expr_stmt|;
name|widget
operator|.
name|setAppearanceState
argument_list|(
literal|"Off"
argument_list|)
expr_stmt|;
comment|// don't forget this, or button will be invisible
name|widgets
operator|.
name|add
argument_list|(
name|widget
argument_list|)
expr_stmt|;
name|page
operator|.
name|getAnnotations
argument_list|()
operator|.
name|add
argument_list|(
name|widget
argument_list|)
expr_stmt|;
block|}
name|radioButton
operator|.
name|setWidgets
argument_list|(
name|widgets
argument_list|)
expr_stmt|;
name|acroForm
operator|.
name|getFields
argument_list|()
operator|.
name|add
argument_list|(
name|radioButton
argument_list|)
expr_stmt|;
comment|// Set the texts
try|try
init|(
name|PDPageContentStream
name|contents
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
init|)
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
name|options
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|contents
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contents
operator|.
name|setFont
argument_list|(
name|PDType1Font
operator|.
name|HELVETICA
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|contents
operator|.
name|newLineAtOffset
argument_list|(
literal|70
argument_list|,
name|PDRectangle
operator|.
name|A4
operator|.
name|getHeight
argument_list|()
operator|-
literal|30
operator|-
name|i
operator|*
literal|35
argument_list|)
expr_stmt|;
name|contents
operator|.
name|showText
argument_list|(
name|options
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|contents
operator|.
name|endText
argument_list|()
expr_stmt|;
block|}
block|}
name|radioButton
operator|.
name|setValue
argument_list|(
literal|"c"
argument_list|)
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
literal|"RadioButtonsSample.pdf"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|PDAppearanceStream
name|createAppearanceStream
parameter_list|(
specifier|final
name|PDDocument
name|document
parameter_list|,
name|PDAnnotationWidget
name|widget
parameter_list|,
name|boolean
name|on
parameter_list|)
throws|throws
name|IOException
block|{
name|PDRectangle
name|rect
init|=
name|widget
operator|.
name|getRectangle
argument_list|()
decl_stmt|;
name|PDAppearanceStream
name|onAP
init|=
operator|new
name|PDAppearanceStream
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|onAP
operator|.
name|setBBox
argument_list|(
operator|new
name|PDRectangle
argument_list|(
name|rect
operator|.
name|getWidth
argument_list|()
argument_list|,
name|rect
operator|.
name|getHeight
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
init|(
name|PDAppearanceContentStream
name|onAPCS
init|=
operator|new
name|PDAppearanceContentStream
argument_list|(
name|onAP
argument_list|)
init|)
block|{
name|PDAppearanceCharacteristicsDictionary
name|appearanceCharacteristics
init|=
name|widget
operator|.
name|getAppearanceCharacteristics
argument_list|()
decl_stmt|;
name|PDColor
name|backgroundColor
init|=
name|appearanceCharacteristics
operator|.
name|getBackground
argument_list|()
decl_stmt|;
name|PDColor
name|borderColor
init|=
name|appearanceCharacteristics
operator|.
name|getBorderColour
argument_list|()
decl_stmt|;
name|float
name|lineWidth
init|=
name|getLineWidth
argument_list|(
name|widget
argument_list|)
decl_stmt|;
name|onAPCS
operator|.
name|setBorderLine
argument_list|(
name|lineWidth
argument_list|,
name|widget
operator|.
name|getBorderStyle
argument_list|()
argument_list|,
name|widget
operator|.
name|getBorder
argument_list|()
argument_list|)
expr_stmt|;
name|onAPCS
operator|.
name|setNonStrokingColor
argument_list|(
name|backgroundColor
argument_list|)
expr_stmt|;
name|float
name|radius
init|=
name|Math
operator|.
name|min
argument_list|(
name|rect
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|rect
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|)
decl_stmt|;
name|drawCircle
argument_list|(
name|onAPCS
argument_list|,
name|rect
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|rect
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|,
name|radius
argument_list|)
expr_stmt|;
name|onAPCS
operator|.
name|fill
argument_list|()
expr_stmt|;
name|onAPCS
operator|.
name|setStrokingColor
argument_list|(
name|borderColor
argument_list|)
expr_stmt|;
name|drawCircle
argument_list|(
name|onAPCS
argument_list|,
name|rect
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|rect
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|,
name|radius
operator|-
name|lineWidth
operator|/
literal|2
argument_list|)
expr_stmt|;
name|onAPCS
operator|.
name|stroke
argument_list|()
expr_stmt|;
if|if
condition|(
name|on
condition|)
block|{
name|onAPCS
operator|.
name|setNonStrokingColor
argument_list|(
literal|0f
argument_list|)
expr_stmt|;
name|drawCircle
argument_list|(
name|onAPCS
argument_list|,
name|rect
operator|.
name|getWidth
argument_list|()
operator|/
literal|2
argument_list|,
name|rect
operator|.
name|getHeight
argument_list|()
operator|/
literal|2
argument_list|,
operator|(
name|radius
operator|-
name|lineWidth
operator|)
operator|/
literal|2
argument_list|)
expr_stmt|;
name|onAPCS
operator|.
name|fill
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|onAP
return|;
block|}
specifier|static
name|float
name|getLineWidth
parameter_list|(
name|PDAnnotationWidget
name|widget
parameter_list|)
block|{
name|PDBorderStyleDictionary
name|bs
init|=
name|widget
operator|.
name|getBorderStyle
argument_list|()
decl_stmt|;
if|if
condition|(
name|bs
operator|!=
literal|null
condition|)
block|{
return|return
name|bs
operator|.
name|getWidth
argument_list|()
return|;
block|}
return|return
literal|1
return|;
block|}
specifier|static
name|void
name|drawCircle
parameter_list|(
name|PDAppearanceContentStream
name|cs
parameter_list|,
name|float
name|x
parameter_list|,
name|float
name|y
parameter_list|,
name|float
name|r
parameter_list|)
throws|throws
name|IOException
block|{
comment|// http://stackoverflow.com/a/2007782/535646
name|float
name|magic
init|=
name|r
operator|*
literal|0.551784f
decl_stmt|;
name|cs
operator|.
name|moveTo
argument_list|(
name|x
argument_list|,
name|y
operator|+
name|r
argument_list|)
expr_stmt|;
name|cs
operator|.
name|curveTo
argument_list|(
name|x
operator|+
name|magic
argument_list|,
name|y
operator|+
name|r
argument_list|,
name|x
operator|+
name|r
argument_list|,
name|y
operator|+
name|magic
argument_list|,
name|x
operator|+
name|r
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|cs
operator|.
name|curveTo
argument_list|(
name|x
operator|+
name|r
argument_list|,
name|y
operator|-
name|magic
argument_list|,
name|x
operator|+
name|magic
argument_list|,
name|y
operator|-
name|r
argument_list|,
name|x
argument_list|,
name|y
operator|-
name|r
argument_list|)
expr_stmt|;
name|cs
operator|.
name|curveTo
argument_list|(
name|x
operator|-
name|magic
argument_list|,
name|y
operator|-
name|r
argument_list|,
name|x
operator|-
name|r
argument_list|,
name|y
operator|-
name|magic
argument_list|,
name|x
operator|-
name|r
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|cs
operator|.
name|curveTo
argument_list|(
name|x
operator|-
name|r
argument_list|,
name|y
operator|+
name|magic
argument_list|,
name|x
operator|-
name|magic
argument_list|,
name|y
operator|+
name|r
argument_list|,
name|x
argument_list|,
name|y
operator|+
name|r
argument_list|)
expr_stmt|;
name|cs
operator|.
name|closePath
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

