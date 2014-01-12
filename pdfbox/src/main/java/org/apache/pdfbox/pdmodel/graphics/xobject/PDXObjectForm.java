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
name|graphics
operator|.
name|xobject
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|geom
operator|.
name|AffineTransform
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|cos
operator|.
name|COSNumber
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
name|COSStream
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
name|PDResources
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
name|common
operator|.
name|PDStream
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
name|util
operator|.
name|Matrix
import|;
end_import

begin_comment
comment|/**  * A form xobject.  *   * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  *   */
end_comment

begin_class
specifier|public
class|class
name|PDXObjectForm
extends|extends
name|PDXObject
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
name|PDXObjectForm
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The XObject subtype.      */
specifier|public
specifier|static
specifier|final
name|String
name|SUB_TYPE
init|=
literal|"Form"
decl_stmt|;
specifier|private
name|String
name|name
init|=
literal|null
decl_stmt|;
comment|/**      * Standard constuctor.      *       * @param formStream The XObject is passed as a COSStream.      */
specifier|public
name|PDXObjectForm
parameter_list|(
name|PDStream
name|formStream
parameter_list|)
block|{
name|super
argument_list|(
name|formStream
argument_list|)
expr_stmt|;
name|getCOSStream
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUB_TYPE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Standard constructor.      *       * @param formStream The XObject is passed as a COSStream.      */
specifier|public
name|PDXObjectForm
parameter_list|(
name|COSStream
name|formStream
parameter_list|)
block|{
name|this
argument_list|(
name|formStream
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**      * Standard constructor including the name of the XObjectForm      * to avoid recursions.      *       * @param formStream The XObject is passed as a COSStream.      * @param xobjectName The name of the XObjectForm.      */
specifier|public
name|PDXObjectForm
parameter_list|(
name|COSStream
name|formStream
parameter_list|,
name|String
name|xobjectName
parameter_list|)
block|{
name|super
argument_list|(
name|formStream
argument_list|)
expr_stmt|;
name|getCOSStream
argument_list|()
operator|.
name|setName
argument_list|(
name|COSName
operator|.
name|SUBTYPE
argument_list|,
name|SUB_TYPE
argument_list|)
expr_stmt|;
name|name
operator|=
name|xobjectName
expr_stmt|;
block|}
comment|/**      * This will get the form type, currently 1 is the only form type.      *       * @return The form type.      */
specifier|public
name|int
name|getFormType
parameter_list|()
block|{
return|return
name|getCOSStream
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|FORMTYPE
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * Set the form type.      *       * @param formType The new form type.      */
specifier|public
name|void
name|setFormType
parameter_list|(
name|int
name|formType
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|FORMTYPE
argument_list|,
name|formType
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the resources at this page and not look up the hierarchy. This attribute is inheritable, and      * findResources() should probably used. This will return null if no resources are available at this level.      *       * @return The resources at this level in the hierarchy.      */
specifier|public
name|PDResources
name|getResources
parameter_list|()
block|{
name|PDResources
name|retval
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|resources
init|=
operator|(
name|COSDictionary
operator|)
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|)
decl_stmt|;
if|if
condition|(
name|resources
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDResources
argument_list|(
name|resources
argument_list|)
expr_stmt|;
comment|// check for a recursion, see PDFBOX-1813
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|PDXObject
argument_list|>
name|xobjects
init|=
name|retval
operator|.
name|getXObjects
argument_list|()
decl_stmt|;
if|if
condition|(
name|xobjects
operator|!=
literal|null
operator|&&
name|xobjects
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|retval
operator|.
name|removeXObject
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Removed XObjectForm "
operator|+
name|name
operator|+
literal|" to avoid a recursion"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the resources for this page.      *       * @param resources The new resources for this page.      */
specifier|public
name|void
name|setResources
parameter_list|(
name|PDResources
name|resources
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|RESOURCES
argument_list|,
name|resources
argument_list|)
expr_stmt|;
block|}
comment|/**      * An array of four numbers in the form coordinate system (see below), giving the coordinates of the left, bottom,      * right, and top edges, respectively, of the form XObject's bounding box. These boundaries are used to clip the      * form XObject and to determine its size for caching.      *       * @return The BBox of the form.      */
specifier|public
name|PDRectangle
name|getBBox
parameter_list|()
block|{
name|PDRectangle
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|BBOX
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|PDRectangle
argument_list|(
name|array
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the BBox (bounding box) for this form.      *       * @param bbox The new BBox for this form.      */
specifier|public
name|void
name|setBBox
parameter_list|(
name|PDRectangle
name|bbox
parameter_list|)
block|{
if|if
condition|(
name|bbox
operator|==
literal|null
condition|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|removeItem
argument_list|(
name|COSName
operator|.
name|BBOX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getCOSStream
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|BBOX
argument_list|,
name|bbox
operator|.
name|getCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get the optional Matrix of an XObjectForm. It maps the form space into the user space      *       * @return the form matrix      */
specifier|public
name|Matrix
name|getMatrix
parameter_list|()
block|{
name|Matrix
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|MATRIX
argument_list|)
decl_stmt|;
if|if
condition|(
name|array
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|Matrix
argument_list|()
expr_stmt|;
name|retval
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setValue
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setValue
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|0
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
name|retval
operator|.
name|setValue
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
operator|(
operator|(
name|COSNumber
operator|)
name|array
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|)
operator|.
name|floatValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Sets the optional Matrix entry for the form XObject.      *       * @param transform the transformation matrix      */
specifier|public
name|void
name|setMatrix
parameter_list|(
name|AffineTransform
name|transform
parameter_list|)
block|{
name|COSArray
name|matrix
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|double
index|[]
name|values
init|=
operator|new
name|double
index|[
literal|6
index|]
decl_stmt|;
name|transform
operator|.
name|getMatrix
argument_list|(
name|values
argument_list|)
expr_stmt|;
for|for
control|(
name|double
name|v
range|:
name|values
control|)
block|{
name|matrix
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
operator|(
name|float
operator|)
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getCOSStream
argument_list|()
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|MATRIX
argument_list|,
name|matrix
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the key of this XObjectForm in the structural parent tree. Required if the form XObject contains      * marked-content sequences that are structural content items.      *       * @return the integer key of the XObjectForm's entry in the structural parent tree      */
specifier|public
name|int
name|getStructParents
parameter_list|()
block|{
return|return
name|getCOSStream
argument_list|()
operator|.
name|getInt
argument_list|(
name|COSName
operator|.
name|STRUCT_PARENTS
argument_list|,
literal|0
argument_list|)
return|;
block|}
comment|/**      * This will set the key for this XObjectForm in the structural parent tree.      *       * @param structParent The new key for this XObjectForm.      */
specifier|public
name|void
name|setStructParents
parameter_list|(
name|int
name|structParent
parameter_list|)
block|{
name|getCOSStream
argument_list|()
operator|.
name|setInt
argument_list|(
name|COSName
operator|.
name|STRUCT_PARENTS
argument_list|,
name|structParent
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

