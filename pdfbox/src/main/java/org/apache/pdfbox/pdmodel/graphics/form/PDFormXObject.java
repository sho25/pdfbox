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
name|form
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
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|contentstream
operator|.
name|PDContentStream
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
name|pdmodel
operator|.
name|graphics
operator|.
name|PDXObject
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
comment|/* TODO There are further Form XObjects to implement:  + PDFormXObject |- PDReferenceXObject |- PDGroupXObject    |- PDTransparencyXObject  See PDF 32000 p111  When doing this all methods on PDFormXObject should probably be made final and all fields private. */
end_comment

begin_comment
comment|/**  * A Form XObject.  *   * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDFormXObject
extends|extends
name|PDXObject
implements|implements
name|PDContentStream
block|{
comment|// name of XObject in resources, to prevent recursion
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|PDGroup
name|group
decl_stmt|;
comment|/**      * Creates a Form XObject for reading.      * @param stream The XObject stream      */
specifier|public
name|PDFormXObject
parameter_list|(
name|PDStream
name|stream
parameter_list|)
block|{
name|super
argument_list|(
name|stream
argument_list|,
name|COSName
operator|.
name|FORM
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a Form XObject for reading.      * @param stream The XObject stream      * @param name The name of the form XObject, to prevent recursion.      */
specifier|public
name|PDFormXObject
parameter_list|(
name|PDStream
name|stream
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|stream
argument_list|,
name|COSName
operator|.
name|FORM
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Creates a Form Image XObject for writing, in the given document.      * @param document The current document      */
specifier|public
name|PDFormXObject
parameter_list|(
name|PDDocument
name|document
parameter_list|)
block|{
name|super
argument_list|(
name|document
argument_list|,
name|COSName
operator|.
name|FORM
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the form type, currently 1 is the only form type.      * @return The form type.      */
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
comment|/**      * Set the form type.      * @param formType The new form type.      */
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
comment|/**      * Returns the group attributes dictionary (Group XObject).      *      * @return the group attributes dictionary      */
specifier|public
name|PDGroup
name|getGroup
parameter_list|()
block|{
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
name|COSDictionary
name|dic
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
name|GROUP
argument_list|)
decl_stmt|;
if|if
condition|(
name|dic
operator|!=
literal|null
condition|)
block|{
name|group
operator|=
operator|new
name|PDGroup
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|group
return|;
block|}
annotation|@
name|Override
specifier|public
name|COSStream
name|getContentStream
parameter_list|()
block|{
return|return
name|getCOSStream
argument_list|()
return|;
block|}
comment|/**      * This will get the resources at this page and not look up the hierarchy.      * This attribute is inheritable, and findResources() should probably used.      * This will return null if no resources are available at this level.      * @return The resources at this level in the hierarchy.      */
annotation|@
name|Override
specifier|public
name|PDResources
name|getResources
parameter_list|()
block|{
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
return|return
operator|new
name|PDResources
argument_list|(
name|resources
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * This will set the resources for this page.      * @param resources The new resources for this page.      */
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
comment|/**      * An array of four numbers in the form coordinate system (see below),      * giving the coordinates of the left, bottom, right, and top edges, respectively,      * of the form XObject's bounding box.      * These boundaries are used to clip the form XObject and to determine its size for caching.      * @return The BBox of the form.      */
annotation|@
name|Override
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
comment|/**      * This will set the BBox (bounding box) for this form.      * @param bbox The new BBox for this form.      */
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
comment|/**      * This will get the optional Matrix of an XObjectForm. It maps the form space to user space.      * @return the form matrix if available, or the identity matrix.      */
annotation|@
name|Override
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
else|else
block|{
comment|// default value is the identity matrix
name|retval
operator|=
operator|new
name|Matrix
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Sets the optional Matrix entry for the form XObject.      * @param transform the transformation matrix      */
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
comment|/**      * This will get the key of this XObjectForm in the structural parent tree.      * Required if the form XObject contains marked-content sequences that are      * structural content items.      * @return the integer key of the XObjectForm's entry in the structural parent tree      */
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
comment|/**      * This will set the key for this XObjectForm in the structural parent tree.      * @param structParent The new key for this XObjectForm.      */
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

