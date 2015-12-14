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
name|List
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
name|XPathConstants
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
name|COSBase
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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|Node
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
name|NodeList
import|;
end_import

begin_comment
comment|/**  * This represents a Ink FDF annotation.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|FDFAnnotationInk
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
name|FDFAnnotationInk
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
literal|"Ink"
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFAnnotationInk
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
name|FDFAnnotationInk
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
name|FDFAnnotationInk
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
name|NodeList
name|gestures
init|=
operator|(
name|NodeList
operator|)
name|xpath
operator|.
name|evaluate
argument_list|(
literal|"inklist/gesture"
argument_list|,
name|element
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
if|if
condition|(
name|gestures
operator|.
name|getLength
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: missing element 'gesture'"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|float
index|[]
argument_list|>
name|inklist
init|=
operator|new
name|ArrayList
argument_list|<
name|float
index|[]
argument_list|>
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
name|gestures
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|gestures
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|Element
condition|)
block|{
name|String
name|gesture
init|=
name|node
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getNodeValue
argument_list|()
decl_stmt|;
name|String
index|[]
name|gestureValues
init|=
name|gesture
operator|.
name|split
argument_list|(
literal|",|;"
argument_list|)
decl_stmt|;
name|float
index|[]
name|values
init|=
operator|new
name|float
index|[
name|gestureValues
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|gestureValues
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
name|values
index|[
name|j
index|]
operator|=
name|Float
operator|.
name|parseFloat
argument_list|(
name|gestureValues
index|[
name|j
index|]
argument_list|)
expr_stmt|;
block|}
name|inklist
operator|.
name|add
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
block|}
name|setInkList
argument_list|(
name|inklist
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XPathExpressionException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Error while evaluating XPath expression for inklist gestures"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Set the paths making up the freehand "scribble".      *       * The ink annotation is made up of one ore more disjoint paths. Each array entry is an array representing a stroked      * path, being a series of alternating horizontal and vertical coordinates in default user space.      *       * @param inklist the List of arrays representing the paths.      */
specifier|public
specifier|final
name|void
name|setInkList
parameter_list|(
name|List
argument_list|<
name|float
index|[]
argument_list|>
name|inklist
parameter_list|)
block|{
name|COSArray
name|newInklist
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
for|for
control|(
name|float
index|[]
name|array
range|:
name|inklist
control|)
block|{
name|COSArray
name|newArray
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|newArray
operator|.
name|setFloatArray
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|newInklist
operator|.
name|add
argument_list|(
name|newArray
argument_list|)
expr_stmt|;
block|}
name|annot
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|INKLIST
argument_list|,
name|newInklist
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the paths making up the freehand "scribble".      *      * @see #setInkList(List)      * @return the List of arrays representing the paths.      */
specifier|public
name|List
argument_list|<
name|float
index|[]
argument_list|>
name|getInkList
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
name|INKLIST
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|float
index|[]
argument_list|>
name|retval
init|=
operator|new
name|ArrayList
argument_list|<
name|float
index|[]
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|COSBase
name|entry
range|:
name|array
control|)
block|{
name|retval
operator|.
name|add
argument_list|(
operator|(
operator|(
name|COSArray
operator|)
name|entry
operator|)
operator|.
name|toFloatArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
comment|// Should never happen as this is a required item
block|}
block|}
block|}
end_class

end_unit

