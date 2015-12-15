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
name|fdf
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
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPath
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpressionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
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
name|common
operator|.
name|PDRectangle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * This represents a FreeText FDF annotation.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|FDFAnnotationFreeText
extends|extends
name|FDFAnnotation
block|{
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
name|FDFAnnotationFreeText
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * COS Model value for SubType entry.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUBTYPE
init|=
literal|"FreeText"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFAnnotationFreeText
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|annot
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUBTYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param a An existing FDF Annotation.      */
specifier|public
name|FDFAnnotationFreeText
parameter_list|(
name|COSDictionary
name|a
parameter_list|)
block|{
name|super
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param element An XFDF element.      *      * @throws IOException If there is an error extracting information from the element.      */
specifier|public
name|FDFAnnotationFreeText
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|annot
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUBTYPE
argument_list|)
expr_stmt|;
name|setJustification
argument_list|(
name|element
operator|.
name|getAttribute
argument_list|(
literal|"justification"
argument_list|)
argument_list|)
expr_stmt|;
name|XPath
name|xpath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
try|try
block|{
name|setDefaultAppearance
argument_list|(
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"defaultappearance"
argument_list|,
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|setDefaultStyle
argument_list|(
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"defaultstyle"
argument_list|,
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XPathExpressionException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error while evaluating XPath expression"
argument_list|)
expr_stmt|;
block|}
name|initCallout
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|String
name|rotation
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"rotation"
argument_list|)
decl_stmt|;
if|if
condition|(
name|rotation
operator|!=
literal|null
operator|&&
operator|!
name|rotation
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setRotation
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|rotation
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|initFringe
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|String
name|lineEndingStyle
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"head"
argument_list|)
decl_stmt|;
if|if
condition|(
name|lineEndingStyle
operator|!=
literal|null
operator|&&
operator|!
name|lineEndingStyle
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setLineEndingStyle
argument_list|(
name|lineEndingStyle
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initFringe
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|fringe
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"fringe"
argument_list|)
decl_stmt|;
if|if
condition|(
name|fringe
operator|!=
literal|null
operator|&&
operator|!
name|fringe
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
index|[]
name|fringeValues
init|=
name|fringe
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
if|if
condition|(
name|fringeValues
operator|.
name|length
operator|!=
literal|4
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: wrong amount of numbers in attribute 'fringe'"
argument_list|)
throw|;
block|}
name|PDRectangle
name|rect
init|=
operator|new
name|PDRectangle
argument_list|()
decl_stmt|;
name|rect
operator|.
name|setLowerLeftX
argument_list|(
name|Float
operator|.
name|parseFloat
argument_list|(
name|fringeValues
index|[
literal|0
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setLowerLeftY
argument_list|(
name|Float
operator|.
name|parseFloat
argument_list|(
name|fringeValues
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightX
argument_list|(
name|Float
operator|.
name|parseFloat
argument_list|(
name|fringeValues
index|[
literal|2
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|rect
operator|.
name|setUpperRightY
argument_list|(
name|Float
operator|.
name|parseFloat
argument_list|(
name|fringeValues
index|[
literal|3
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|setFringe
argument_list|(
name|rect
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|initCallout
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|callout
init|=
name|element
operator|.
name|getAttribute
argument_list|(
literal|"callout"
argument_list|)
decl_stmt|;
if|if
condition|(
name|callout
operator|!=
literal|null
operator|&&
operator|!
name|callout
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
index|[]
name|calloutValues
init|=
name|callout
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|float
index|[]
name|values
init|=
operator|new
name|float
index|[
name|calloutValues
operator|.
name|length
index|]
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
name|calloutValues
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|values
index|[
name|i
index|]
operator|=
name|Float
operator|.
name|parseFloat
argument_list|(
name|calloutValues
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
name|setCallout
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will set the coordinates of the callout line.      *      * @param callout An array of four or six numbers specifying a callout line attached to the free      * text annotation. Six numbers [ x1 y1 x2 y2 x3 y3 ] represent the starting, knee point, and      * ending coordinates of the line in default user space, Four numbers [ x1 y1 x2 y2 ] represent      * the starting and ending coordinates of the line.      */
specifier|public
name|void
name|setCallout
parameter_list|(
name|float
index|[]
name|callout
parameter_list|)
block|{
name|COSArray
name|newCallout
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|newCallout
operator|.
name|setFloatArray
argument_list|(
name|callout
argument_list|)
expr_stmt|;
name|annot
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|CL
argument_list|,
name|newCallout
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the coordinates of the the callout line.      *      * @return An array of four or six numbers specifying a callout line attached to the free text      * annotation. Six numbers [ x1 y1 x2 y2 x3 y3 ] represent the starting, knee point, and ending      * coordinates of the line in default user space, Four numbers [ x1 y1 x2 y2 ] represent the      * starting and ending coordinates of the line.      */
specifier|public
name|float
index|[]
name|getCallout
parameter_list|()
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|annot
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|CL
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
return|return
name|array
operator|.
name|toFloatArray
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * This will set the form of quadding (justification) of the annotation text.      *       * @param justification The quadding of the text.      */
specifier|public
specifier|final
name|void
name|setJustification
parameter_list|(
name|String
name|justification
parameter_list|)
block|{
name|int
name|quadding
init|=
literal|0
decl_stmt|;
if|if
condition|(
literal|"centered"
operator|.
name|equals
argument_list|(
name|justification
argument_list|)
condition|)
block|{
name|quadding
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"right"
operator|.
name|equals
argument_list|(
name|justification
argument_list|)
condition|)
block|{
name|quadding
operator|=
literal|2
expr_stmt|;
block|}
name|annot
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|Q
argument_list|,
name|quadding
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the form of quadding (justification) of the annotation text.      *       * @return The quadding of the text.      */
specifier|public
name|String
name|getJustification
parameter_list|()
block|{
return|return
literal|""
operator|+
name|annot
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|Q
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the clockwise rotation in degrees.      *       * @param rotation The number of degrees of clockwise rotation.      */
specifier|public
specifier|final
name|void
name|setRotation
parameter_list|(
name|int
name|rotation
parameter_list|)
block|{
name|annot
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|ROTATE
argument_list|,
name|rotation
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the clockwise rotation in degrees.      *       * @return The number of degrees of clockwise rotation.      */
specifier|public
name|String
name|getRotation
parameter_list|()
block|{
return|return
name|annot
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|ROTATE
argument_list|)
return|;
block|}
comment|/**      * Set the default appearance string.      *      * @param appearance The new default appearance string.      */
specifier|public
specifier|final
name|void
name|setDefaultAppearance
parameter_list|(
name|String
name|appearance
parameter_list|)
block|{
name|annot
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DA
argument_list|,
name|appearance
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default appearance string.      *      * @return The default appearance of the annotation.      */
specifier|public
name|String
name|getDefaultAppearance
parameter_list|()
block|{
return|return
name|annot
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|DA
argument_list|)
return|;
block|}
comment|/**      * Set the default style string.      *      * @param style The new default style string.      */
specifier|public
specifier|final
name|void
name|setDefaultStyle
parameter_list|(
name|String
name|style
parameter_list|)
block|{
name|annot
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|DS
argument_list|,
name|style
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the default style string.      *      * @return The default style of the annotation.      */
specifier|public
name|String
name|getDefaultStyle
parameter_list|()
block|{
return|return
name|annot
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|DS
argument_list|)
return|;
block|}
comment|/**      * This will set the fringe rectangle. Giving the difference between the annotations rectangle      * and where the drawing occurs. (To take account of any effects applied through the BE entry      * for example)      *      * @param fringe the fringe      */
specifier|public
specifier|final
name|void
name|setFringe
parameter_list|(
name|PDRectangle
name|fringe
parameter_list|)
block|{
name|annot
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RD
argument_list|,
name|fringe
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the fringe. Giving the difference between the annotations rectangle and where      * the drawing occurs. (To take account of any effects applied through the BE entry for example)      *      * @return the rectangle difference      */
specifier|public
name|PDRectangle
name|getFringe
parameter_list|()
block|{
name|COSArray
name|rd
init|=
operator|(
name|COSArray
operator|)
name|annot
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RD
argument_list|)
decl_stmt|;
if|if
condition|(
name|rd
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|PDRectangle
argument_list|(
name|rd
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * This will set the line ending style.      *      * @param style The new style.      */
specifier|public
specifier|final
name|void
name|setLineEndingStyle
parameter_list|(
name|String
name|style
parameter_list|)
block|{
name|annot
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|LE
argument_list|,
name|style
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will retrieve the line ending style.      *      * @return The ending style for the start point.      */
specifier|public
name|String
name|getLineEndingStyle
parameter_list|()
block|{
return|return
name|annot
operator|.
name|getNameAsString
argument_list|(
name|COSName
operator|.
name|LE
argument_list|)
return|;
block|}
block|}
end_class

end_unit

