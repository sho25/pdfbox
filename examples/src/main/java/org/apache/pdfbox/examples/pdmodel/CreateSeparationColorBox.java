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
name|pdmodel
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSArray
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
name|COSFloat
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
name|COSInteger
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
name|function
operator|.
name|PDFunctionType2
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
name|PDColorSpace
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
name|PDSeparation
import|;
end_import

begin_comment
comment|/**  * This example shows how to use a separation color / spot color. Here it is a placeholder for gold,  * and it is displayed as yellow. You can see the colorspace in PDFDebugger by going to  * "Root/Pages/Kids/[0]/Resources/ColorSpace/cs1".  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
class|class
name|CreateSeparationColorBox
block|{
specifier|private
name|CreateSeparationColorBox
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
name|doc
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
argument_list|()
decl_stmt|;
name|doc
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|COSArray
name|separationArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|separationArray
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|SEPARATION
argument_list|)
expr_stmt|;
comment|// type
name|separationArray
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|getPDFName
argument_list|(
literal|"Gold"
argument_list|)
argument_list|)
expr_stmt|;
comment|// the name, e.g. metallic, fluorescent, glitter
name|separationArray
operator|.
name|add
argument_list|(
name|COSName
operator|.
name|DEVICERGB
argument_list|)
expr_stmt|;
comment|// alternate colorspace
comment|// tint transform function, results between C0=white (1 1 1) and C1=yellow (1 1 0)
name|COSDictionary
name|fdict
init|=
operator|new
name|COSDictionary
argument_list|()
decl_stmt|;
name|fdict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|FUNCTION_TYPE
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|COSArray
name|range
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|range
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|range
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|fdict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RANGE
argument_list|,
name|range
argument_list|)
expr_stmt|;
name|COSArray
name|domain
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|domain
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|domain
operator|.
name|add
argument_list|(
name|COSInteger
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|fdict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DOMAIN
argument_list|,
name|domain
argument_list|)
expr_stmt|;
name|COSArray
name|c0
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|c0
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|c0
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|c0
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|fdict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C0
argument_list|,
name|c0
argument_list|)
expr_stmt|;
name|COSArray
name|c1
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|c1
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|c1
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|c1
operator|.
name|add
argument_list|(
name|COSFloat
operator|.
name|get
argument_list|(
literal|"0"
argument_list|)
argument_list|)
expr_stmt|;
name|fdict
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C1
argument_list|,
name|c1
argument_list|)
expr_stmt|;
name|fdict
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|N
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|PDFunctionType2
name|func
init|=
operator|new
name|PDFunctionType2
argument_list|(
name|fdict
argument_list|)
decl_stmt|;
name|separationArray
operator|.
name|add
argument_list|(
name|func
argument_list|)
expr_stmt|;
name|PDColorSpace
name|spotColorSpace
init|=
operator|new
name|PDSeparation
argument_list|(
name|separationArray
argument_list|)
decl_stmt|;
try|try
init|(
name|PDPageContentStream
name|cs
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|doc
argument_list|,
name|page
argument_list|)
init|)
block|{
name|PDColor
name|color
init|=
operator|new
name|PDColor
argument_list|(
operator|new
name|float
index|[]
block|{
literal|0.5f
block|}
argument_list|,
name|spotColorSpace
argument_list|)
decl_stmt|;
name|cs
operator|.
name|setStrokingColor
argument_list|(
name|color
argument_list|)
expr_stmt|;
name|cs
operator|.
name|setLineWidth
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|cs
operator|.
name|addRect
argument_list|(
literal|50
argument_list|,
literal|50
argument_list|,
literal|500
argument_list|,
literal|700
argument_list|)
expr_stmt|;
name|cs
operator|.
name|stroke
argument_list|()
expr_stmt|;
block|}
name|doc
operator|.
name|save
argument_list|(
literal|"gold.pdf"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

