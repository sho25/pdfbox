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
name|documentnavigation
operator|.
name|outline
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|Color
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
name|pdmodel
operator|.
name|PDDestinationNameTreeNode
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
name|PDDocumentNameDictionary
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
name|documentinterchange
operator|.
name|logicalstructure
operator|.
name|PDStructureElement
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
name|interactive
operator|.
name|action
operator|.
name|PDActionFactory
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
name|PDAction
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
name|PDActionGoTo
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDDestination
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDNamedDestination
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDPageDestination
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
name|documentnavigation
operator|.
name|destination
operator|.
name|PDPageXYZDestination
import|;
end_import

begin_comment
comment|/**  * This represents an outline in a pdf document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  */
end_comment

begin_class
specifier|public
class|class
name|PDOutlineItem
extends|extends
name|PDOutlineNode
block|{
specifier|private
specifier|static
specifier|final
name|int
name|ITALIC_FLAG
init|=
literal|1
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|BOLD_FLAG
init|=
literal|2
decl_stmt|;
comment|/**      * Default Constructor.      */
specifier|public
name|PDOutlineItem
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor for an existing outline item.      *      * @param dic The storage dictionary.      */
specifier|public
name|PDOutlineItem
parameter_list|(
name|COSDictionary
name|dic
parameter_list|)
block|{
name|super
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
comment|/**      * Insert a sibling after this node.      *      * @param item The item to insert.      */
specifier|public
name|void
name|insertSiblingAfter
parameter_list|(
name|PDOutlineItem
name|item
parameter_list|)
block|{
name|item
operator|.
name|setParent
argument_list|(
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
name|PDOutlineItem
name|next
init|=
name|getNextSibling
argument_list|()
decl_stmt|;
name|setNextSibling
argument_list|(
name|item
argument_list|)
expr_stmt|;
name|item
operator|.
name|setPreviousSibling
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|next
operator|!=
literal|null
condition|)
block|{
name|item
operator|.
name|setNextSibling
argument_list|(
name|next
argument_list|)
expr_stmt|;
name|next
operator|.
name|setPreviousSibling
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
name|updateParentOpenCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
specifier|public
name|PDOutlineNode
name|getParent
parameter_list|()
block|{
return|return
name|super
operator|.
name|getParent
argument_list|()
return|;
block|}
comment|/**      * Return the previous sibling or null if there is no sibling.      *      * @return The previous sibling.      */
specifier|public
name|PDOutlineItem
name|getPreviousSibling
parameter_list|()
block|{
name|PDOutlineItem
name|last
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|lastDic
init|=
operator|(
name|COSDictionary
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|PREV
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastDic
operator|!=
literal|null
condition|)
block|{
name|last
operator|=
operator|new
name|PDOutlineItem
argument_list|(
name|lastDic
argument_list|)
expr_stmt|;
block|}
return|return
name|last
return|;
block|}
comment|/**      * Set the previous sibling, this will be maintained by this class.      *      * @param outlineNode The new previous sibling.      */
specifier|protected
name|void
name|setPreviousSibling
parameter_list|(
name|PDOutlineNode
name|outlineNode
parameter_list|)
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|PREV
argument_list|,
name|outlineNode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Return the next sibling or null if there is no next sibling.      *      * @return The next sibling.      */
specifier|public
name|PDOutlineItem
name|getNextSibling
parameter_list|()
block|{
name|PDOutlineItem
name|last
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|lastDic
init|=
operator|(
name|COSDictionary
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|NEXT
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastDic
operator|!=
literal|null
condition|)
block|{
name|last
operator|=
operator|new
name|PDOutlineItem
argument_list|(
name|lastDic
argument_list|)
expr_stmt|;
block|}
return|return
name|last
return|;
block|}
comment|/**      * Set the next sibling, this will be maintained by this class.      *      * @param outlineNode The new next sibling.      */
specifier|protected
name|void
name|setNextSibling
parameter_list|(
name|PDOutlineNode
name|outlineNode
parameter_list|)
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|NEXT
argument_list|,
name|outlineNode
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the title of this node.      *      * @return The title of this node.      */
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|node
operator|.
name|getString
argument_list|(
name|COSName
operator|.
name|TITLE
argument_list|)
return|;
block|}
comment|/**      * Set the title for this node.      *      * @param title The new title for this node.      */
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|node
operator|.
name|setString
argument_list|(
name|COSName
operator|.
name|TITLE
argument_list|,
name|title
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the page destination of this node.      *      * @return The page destination of this node.      * @throws IOException If there is an error creating the destination.      */
specifier|public
name|PDDestination
name|getDestination
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|PDDestination
operator|.
name|create
argument_list|(
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|DEST
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Set the page destination for this node.      *      * @param dest The new page destination for this node.      */
specifier|public
name|void
name|setDestination
parameter_list|(
name|PDDestination
name|dest
parameter_list|)
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|DEST
argument_list|,
name|dest
argument_list|)
expr_stmt|;
block|}
comment|/**      * A convenience method that will create an XYZ destination using only the defaults.      *      * @param page The page to refer to.      */
specifier|public
name|void
name|setDestination
parameter_list|(
name|PDPage
name|page
parameter_list|)
block|{
name|PDPageXYZDestination
name|dest
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|page
operator|!=
literal|null
condition|)
block|{
name|dest
operator|=
operator|new
name|PDPageXYZDestination
argument_list|()
expr_stmt|;
name|dest
operator|.
name|setPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
block|}
name|setDestination
argument_list|(
name|dest
argument_list|)
expr_stmt|;
block|}
comment|/**      * This method will attempt to find the page in this PDF document that this outline points to.      * If the outline does not point to anything then this method will return null.  If the outline      * is an action that is not a GoTo action then this methods will throw the OutlineNotLocationException      *      * @param doc The document to get the page from.      *      * @return The page that this outline will go to when activated or null if it does not point to anything.      * @throws IOException If there is an error when trying to find the page.      */
specifier|public
name|PDPage
name|findDestinationPage
parameter_list|(
name|PDDocument
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
name|PDDestination
name|dest
init|=
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|dest
operator|==
literal|null
condition|)
block|{
name|PDAction
name|outlineAction
init|=
name|getAction
argument_list|()
decl_stmt|;
if|if
condition|(
name|outlineAction
operator|instanceof
name|PDActionGoTo
condition|)
block|{
name|dest
operator|=
operator|(
operator|(
name|PDActionGoTo
operator|)
name|outlineAction
operator|)
operator|.
name|getDestination
argument_list|()
expr_stmt|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
name|PDPageDestination
name|pageDestination
decl_stmt|;
if|if
condition|(
name|dest
operator|instanceof
name|PDNamedDestination
condition|)
block|{
comment|//if we have a named destination we need to lookup the PDPageDestination
name|PDNamedDestination
name|namedDest
init|=
operator|(
name|PDNamedDestination
operator|)
name|dest
decl_stmt|;
name|PDDocumentNameDictionary
name|namesDict
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|namesDict
operator|!=
literal|null
condition|)
block|{
name|PDDestinationNameTreeNode
name|destsTree
init|=
name|namesDict
operator|.
name|getDests
argument_list|()
decl_stmt|;
if|if
condition|(
name|destsTree
operator|!=
literal|null
condition|)
block|{
name|pageDestination
operator|=
operator|(
name|PDPageDestination
operator|)
name|destsTree
operator|.
name|getValue
argument_list|(
name|namedDest
operator|.
name|getNamedDestination
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|dest
operator|instanceof
name|PDPageDestination
condition|)
block|{
name|pageDestination
operator|=
operator|(
name|PDPageDestination
operator|)
name|dest
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dest
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error: Unknown destination type "
operator|+
name|dest
argument_list|)
throw|;
block|}
name|PDPage
name|page
init|=
name|pageDestination
operator|.
name|getPage
argument_list|()
decl_stmt|;
if|if
condition|(
name|page
operator|==
literal|null
condition|)
block|{
name|int
name|pageNumber
init|=
name|pageDestination
operator|.
name|getPageNumber
argument_list|()
decl_stmt|;
if|if
condition|(
name|pageNumber
operator|!=
operator|-
literal|1
condition|)
block|{
name|List
name|allPages
init|=
name|doc
operator|.
name|getDocumentCatalog
argument_list|()
operator|.
name|getAllPages
argument_list|()
decl_stmt|;
name|page
operator|=
operator|(
name|PDPage
operator|)
name|allPages
operator|.
name|get
argument_list|(
name|pageNumber
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|page
return|;
block|}
comment|/**      * Get the action of this node.      *      * @return The action of this node.      */
specifier|public
name|PDAction
name|getAction
parameter_list|()
block|{
return|return
name|PDActionFactory
operator|.
name|createAction
argument_list|(
operator|(
name|COSDictionary
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|A
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Set the action for this node.      *      * @param action The new action for this node.      */
specifier|public
name|void
name|setAction
parameter_list|(
name|PDAction
name|action
parameter_list|)
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|A
argument_list|,
name|action
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the structure element of this node.      *      * @return The structure element of this node.      */
specifier|public
name|PDStructureElement
name|getStructureElement
parameter_list|()
block|{
name|PDStructureElement
name|se
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|dic
init|=
operator|(
name|COSDictionary
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|SE
argument_list|)
decl_stmt|;
if|if
condition|(
name|dic
operator|!=
literal|null
condition|)
block|{
name|se
operator|=
operator|new
name|PDStructureElement
argument_list|(
name|dic
argument_list|)
expr_stmt|;
block|}
return|return
name|se
return|;
block|}
comment|/**      * Set the structure element for this node.      *      * @param structureElement The new structure element for this node.      */
specifier|public
name|void
name|setStructuredElement
parameter_list|(
name|PDStructureElement
name|structureElement
parameter_list|)
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|SE
argument_list|,
name|structureElement
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the RGB text color of this node.  Default is black and this method      * will never return null.      *      * @return The structure element of this node.      */
specifier|public
name|PDColor
name|getTextColor
parameter_list|()
block|{
name|PDColor
name|retval
init|=
literal|null
decl_stmt|;
name|COSArray
name|csValues
init|=
operator|(
name|COSArray
operator|)
name|node
operator|.
name|getDictionaryObject
argument_list|(
name|COSName
operator|.
name|C
argument_list|)
decl_stmt|;
if|if
condition|(
name|csValues
operator|==
literal|null
condition|)
block|{
name|csValues
operator|=
operator|new
name|COSArray
argument_list|()
expr_stmt|;
name|csValues
operator|.
name|growToSize
argument_list|(
literal|3
argument_list|,
operator|new
name|COSFloat
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C
argument_list|,
name|csValues
argument_list|)
expr_stmt|;
block|}
name|retval
operator|=
operator|new
name|PDColor
argument_list|(
name|csValues
operator|.
name|toFloatArray
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|retval
return|;
block|}
comment|/**      * Set the RGB text color for this node.      *      * @param textColor The text color for this node.      */
specifier|public
name|void
name|setTextColor
parameter_list|(
name|PDColor
name|textColor
parameter_list|)
block|{
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C
argument_list|,
name|textColor
operator|.
name|toCOSArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the RGB text color for this node.      *      * @param textColor The text color for this node.      */
specifier|public
name|void
name|setTextColor
parameter_list|(
name|Color
name|textColor
parameter_list|)
block|{
name|COSArray
name|array
init|=
operator|new
name|COSArray
argument_list|()
decl_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|textColor
operator|.
name|getRed
argument_list|()
operator|/
literal|255f
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|textColor
operator|.
name|getGreen
argument_list|()
operator|/
literal|255f
argument_list|)
argument_list|)
expr_stmt|;
name|array
operator|.
name|add
argument_list|(
operator|new
name|COSFloat
argument_list|(
name|textColor
operator|.
name|getBlue
argument_list|()
operator|/
literal|255f
argument_list|)
argument_list|)
expr_stmt|;
name|node
operator|.
name|setItem
argument_list|(
name|COSName
operator|.
name|C
argument_list|,
name|array
argument_list|)
expr_stmt|;
block|}
comment|/**      * A flag telling if the text should be italic.      *      * @return The italic flag.      */
specifier|public
name|boolean
name|isItalic
parameter_list|()
block|{
return|return
name|node
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|ITALIC_FLAG
argument_list|)
return|;
block|}
comment|/**      * Set the italic property of the text.      *      * @param italic The new italic flag.      */
specifier|public
name|void
name|setItalic
parameter_list|(
name|boolean
name|italic
parameter_list|)
block|{
name|node
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|ITALIC_FLAG
argument_list|,
name|italic
argument_list|)
expr_stmt|;
block|}
comment|/**      * A flag telling if the text should be bold.      *      * @return The bold flag.      */
specifier|public
name|boolean
name|isBold
parameter_list|()
block|{
return|return
name|node
operator|.
name|getFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|BOLD_FLAG
argument_list|)
return|;
block|}
comment|/**      * Set the bold property of the text.      *      * @param bold The new bold flag.      */
specifier|public
name|void
name|setBold
parameter_list|(
name|boolean
name|bold
parameter_list|)
block|{
name|node
operator|.
name|setFlag
argument_list|(
name|COSName
operator|.
name|F
argument_list|,
name|BOLD_FLAG
argument_list|,
name|bold
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

