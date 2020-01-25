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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|Loader
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
name|action
operator|.
name|PDActionJavaScript
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
name|action
operator|.
name|PDAnnotationAdditionalActions
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
name|action
operator|.
name|PDFormFieldAdditionalActions
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
name|PDField
import|;
end_import

begin_comment
comment|/**  * Show usage of different field triggers.  *   * This sample adds a JavaScript to be executed on the different field triggers available.  *   * This sample builds on the form generated by @link CreateSimpleForm so you need to run that first.  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|FieldTriggers
block|{
specifier|private
name|FieldTriggers
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
comment|// Load the PDF document created by SimpleForm.java
try|try
init|(
name|PDDocument
name|document
init|=
name|Loader
operator|.
name|loadPDF
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/SimpleForm.pdf"
argument_list|)
argument_list|)
init|)
block|{
name|PDAcroForm
name|acroForm
init|=
name|document
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAcroForm
argument_list|()
decl_stmt|;
comment|// Get the field and the widget associated to it.
comment|// Note: there might be multiple widgets
name|PDField
name|field
init|=
name|acroForm
operator|.
name|getField
argument_list|(
literal|"SampleField"
argument_list|)
decl_stmt|;
name|PDAnnotationWidget
name|widget
init|=
name|field
operator|.
name|getWidgets
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// Some of the actions are available to the widget, some are available to the form field.
comment|// See Table 8.44 and Table 8.46 in the PDF 1.7 specification
comment|// Actions for the widget
name|PDAnnotationAdditionalActions
name|annotationActions
init|=
operator|new
name|PDAnnotationAdditionalActions
argument_list|()
decl_stmt|;
comment|// Create an action when entering the annotations active area
name|PDActionJavaScript
name|jsEnterAction
init|=
operator|new
name|PDActionJavaScript
argument_list|()
decl_stmt|;
name|jsEnterAction
operator|.
name|setAction
argument_list|(
literal|"app.alert(\"On 'enter' action\")"
argument_list|)
expr_stmt|;
name|annotationActions
operator|.
name|setE
argument_list|(
name|jsEnterAction
argument_list|)
expr_stmt|;
comment|// Create an action when exiting the annotations active area
name|PDActionJavaScript
name|jsExitAction
init|=
operator|new
name|PDActionJavaScript
argument_list|()
decl_stmt|;
name|jsExitAction
operator|.
name|setAction
argument_list|(
literal|"app.alert(\"On 'exit' action\")"
argument_list|)
expr_stmt|;
name|annotationActions
operator|.
name|setX
argument_list|(
name|jsExitAction
argument_list|)
expr_stmt|;
comment|// Create an action when the mouse button is pressed inside the annotations active area
name|PDActionJavaScript
name|jsMouseDownAction
init|=
operator|new
name|PDActionJavaScript
argument_list|()
decl_stmt|;
name|jsMouseDownAction
operator|.
name|setAction
argument_list|(
literal|"app.alert(\"On 'mouse down' action\")"
argument_list|)
expr_stmt|;
name|annotationActions
operator|.
name|setD
argument_list|(
name|jsMouseDownAction
argument_list|)
expr_stmt|;
comment|// Create an action when the mouse button is released inside the annotations active area
name|PDActionJavaScript
name|jsMouseUpAction
init|=
operator|new
name|PDActionJavaScript
argument_list|()
decl_stmt|;
name|jsMouseUpAction
operator|.
name|setAction
argument_list|(
literal|"app.alert(\"On 'mouse up' action\")"
argument_list|)
expr_stmt|;
name|annotationActions
operator|.
name|setU
argument_list|(
name|jsMouseUpAction
argument_list|)
expr_stmt|;
comment|// Create an action when the annotation gets the input focus
name|PDActionJavaScript
name|jsFocusAction
init|=
operator|new
name|PDActionJavaScript
argument_list|()
decl_stmt|;
name|jsFocusAction
operator|.
name|setAction
argument_list|(
literal|"app.alert(\"On 'focus' action\")"
argument_list|)
expr_stmt|;
name|annotationActions
operator|.
name|setFo
argument_list|(
name|jsFocusAction
argument_list|)
expr_stmt|;
comment|// Create an action when the annotation loses the input focus
name|PDActionJavaScript
name|jsBlurredAction
init|=
operator|new
name|PDActionJavaScript
argument_list|()
decl_stmt|;
name|jsBlurredAction
operator|.
name|setAction
argument_list|(
literal|"app.alert(\"On 'blurred' action\")"
argument_list|)
expr_stmt|;
name|annotationActions
operator|.
name|setBl
argument_list|(
name|jsBlurredAction
argument_list|)
expr_stmt|;
name|widget
operator|.
name|setActions
argument_list|(
name|annotationActions
argument_list|)
expr_stmt|;
comment|// Actions for the field
name|PDFormFieldAdditionalActions
name|fieldActions
init|=
operator|new
name|PDFormFieldAdditionalActions
argument_list|()
decl_stmt|;
comment|// Create an action when the user types a keystroke in the field
name|PDActionJavaScript
name|jsKeystrokeAction
init|=
operator|new
name|PDActionJavaScript
argument_list|()
decl_stmt|;
name|jsKeystrokeAction
operator|.
name|setAction
argument_list|(
literal|"app.alert(\"On 'keystroke' action\")"
argument_list|)
expr_stmt|;
name|fieldActions
operator|.
name|setK
argument_list|(
name|jsKeystrokeAction
argument_list|)
expr_stmt|;
comment|// Create an action when the field is formatted to display the current value
name|PDActionJavaScript
name|jsFormattedAction
init|=
operator|new
name|PDActionJavaScript
argument_list|()
decl_stmt|;
name|jsFormattedAction
operator|.
name|setAction
argument_list|(
literal|"app.alert(\"On 'formatted' action\")"
argument_list|)
expr_stmt|;
name|fieldActions
operator|.
name|setF
argument_list|(
name|jsFormattedAction
argument_list|)
expr_stmt|;
comment|// Create an action when the field value changes
name|PDActionJavaScript
name|jsChangedAction
init|=
operator|new
name|PDActionJavaScript
argument_list|()
decl_stmt|;
name|jsChangedAction
operator|.
name|setAction
argument_list|(
literal|"app.alert(\"On 'change' action\")"
argument_list|)
expr_stmt|;
comment|// fieldActions.setV(jsChangedAction);
comment|// Create an action when the field value changes
name|PDActionJavaScript
name|jsRecalculateAction
init|=
operator|new
name|PDActionJavaScript
argument_list|()
decl_stmt|;
name|jsRecalculateAction
operator|.
name|setAction
argument_list|(
literal|"app.alert(\"On 'recalculate' action\")"
argument_list|)
expr_stmt|;
name|fieldActions
operator|.
name|setC
argument_list|(
name|jsRecalculateAction
argument_list|)
expr_stmt|;
comment|// Set the Additional Actions entry for the field
comment|// Note: this is a workaround as if there is only one widget the widget
comment|// and the form field may share the same dictionary. Now setting the
comment|// fields Additional Actions entry directly will overwrite the settings done for
comment|// the widget.
comment|// https://issues.apache.org/jira/browse/PDFBOX-3036
name|field
operator|.
name|getActions
argument_list|()
operator|.
name|getCOSObject
argument_list|()
operator|.
name|addAll
argument_list|(
name|fieldActions
operator|.
name|getCOSObject
argument_list|()
argument_list|)
expr_stmt|;
name|document
operator|.
name|save
argument_list|(
literal|"target/FieldTriggers.pdf"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

