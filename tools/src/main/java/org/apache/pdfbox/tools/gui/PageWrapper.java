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
name|tools
operator|.
name|gui
package|;
end_package

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
name|tools
operator|.
name|PDFReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|JPanel
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|border
operator|.
name|LineBorder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Dimension
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseEvent
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|event
operator|.
name|MouseMotionListener
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

begin_comment
comment|/**  * A class to handle some prettyness around a single PDF page.  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PageWrapper
implements|implements
name|MouseMotionListener
block|{
specifier|private
name|JPanel
name|pageWrapper
init|=
operator|new
name|JPanel
argument_list|()
decl_stmt|;
specifier|private
name|PDFPagePanel
name|pagePanel
init|=
literal|null
decl_stmt|;
specifier|private
name|PDFReader
name|reader
init|=
literal|null
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|SPACE_AROUND_DOCUMENT
init|=
literal|20
decl_stmt|;
comment|/**      * Constructor.      * @param aReader The reader application that holds this page.      * @throws IOException If there is an error creating the page drawing objects.      */
specifier|public
name|PageWrapper
parameter_list|(
name|PDFReader
name|aReader
parameter_list|)
throws|throws
name|IOException
block|{
name|reader
operator|=
name|aReader
expr_stmt|;
name|pagePanel
operator|=
operator|new
name|PDFPagePanel
argument_list|()
expr_stmt|;
name|pageWrapper
operator|.
name|setLayout
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|pageWrapper
operator|.
name|add
argument_list|(
name|pagePanel
argument_list|)
expr_stmt|;
name|pagePanel
operator|.
name|setLocation
argument_list|(
name|SPACE_AROUND_DOCUMENT
argument_list|,
name|SPACE_AROUND_DOCUMENT
argument_list|)
expr_stmt|;
name|pageWrapper
operator|.
name|setBorder
argument_list|(
name|LineBorder
operator|.
name|createBlackLineBorder
argument_list|()
argument_list|)
expr_stmt|;
name|pagePanel
operator|.
name|addMouseMotionListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will display the PDF page in this component.      * @param page The PDF page to display.      */
specifier|public
name|void
name|displayPage
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|pagePanel
operator|.
name|setPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|pagePanel
operator|.
name|setPreferredSize
argument_list|(
name|pagePanel
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|Dimension
name|d
init|=
name|pagePanel
operator|.
name|getSize
argument_list|()
decl_stmt|;
name|d
operator|.
name|width
operator|+=
operator|(
name|SPACE_AROUND_DOCUMENT
operator|*
literal|2
operator|)
expr_stmt|;
name|d
operator|.
name|height
operator|+=
operator|(
name|SPACE_AROUND_DOCUMENT
operator|*
literal|2
operator|)
expr_stmt|;
name|pageWrapper
operator|.
name|setPreferredSize
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|pageWrapper
operator|.
name|validate
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will get the JPanel that can be displayed.      * @return The panel with the displayed PDF page.      */
specifier|public
name|JPanel
name|getPanel
parameter_list|()
block|{
return|return
name|pageWrapper
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|mouseDragged
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
comment|//do nothing when mouse moves.
block|}
annotation|@
name|Override
specifier|public
name|void
name|mouseMoved
parameter_list|(
name|MouseEvent
name|e
parameter_list|)
block|{
name|reader
operator|.
name|getBottomStatusPanel
argument_list|()
operator|.
name|getStatusLabel
argument_list|()
operator|.
name|setText
argument_list|(
name|e
operator|.
name|getX
argument_list|()
operator|+
literal|","
operator|+
name|e
operator|.
name|getY
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

