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
name|pdfparser
package|;
end_package

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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|cos
operator|.
name|COSObjectKey
import|;
end_import

begin_comment
comment|/**  * This class will collect all XRef/trailer objects and creates correct  * xref/trailer information after all objects are read using startxref  * and 'Prev' information (unused XRef/trailer objects are discarded).  *  * In case of missing startxref or wrong startxref pointer all  * XRef/trailer objects are used to create xref table / trailer dictionary  * in order they occur.  *  * For each new xref object/XRef stream method {@link #nextXrefObj(long, XRefType)}  * must be called with start byte position. All following calls to  * {@link #setXRef(COSObjectKey, long)} or {@link #setTrailer(COSDictionary)}  * will add the data for this byte position.  *  * After all objects are parsed the startxref position must be provided  * using {@link #setStartxref(long)}. This is used to build the chain of  * active xref/trailer objects used for creating document trailer and xref table.  *  * @author Timo Böhme  */
end_comment

begin_class
specifier|public
class|class
name|XrefTrailerResolver
block|{
comment|/**      * A class which represents a xref/trailer object.      */
specifier|private
specifier|static
class|class
name|XrefTrailerObj
block|{
specifier|protected
name|COSDictionary
name|trailer
init|=
literal|null
decl_stmt|;
specifier|private
name|XRefType
name|xrefType
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|COSObjectKey
argument_list|,
name|Long
argument_list|>
name|xrefTable
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**          *  Default constructor.          */
specifier|private
name|XrefTrailerObj
parameter_list|()
block|{
name|xrefType
operator|=
name|XRefType
operator|.
name|TABLE
expr_stmt|;
block|}
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|xrefTable
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**       * The XRefType of a trailer.      */
specifier|public
enum|enum
name|XRefType
block|{
comment|/**          * XRef table type.          */
name|TABLE
block|,
comment|/**          * XRef stream type.          */
name|STREAM
block|}
specifier|private
specifier|final
name|Map
argument_list|<
name|Long
argument_list|,
name|XrefTrailerObj
argument_list|>
name|bytePosToXrefMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|XrefTrailerObj
name|curXrefTrailerObj
init|=
literal|null
decl_stmt|;
specifier|private
name|XrefTrailerObj
name|resolvedXrefTrailer
init|=
literal|null
decl_stmt|;
comment|/** Log instance. */
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
name|XrefTrailerResolver
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Returns the first trailer if at least one exists.      *       * @return the first trailer or null      */
specifier|public
specifier|final
name|COSDictionary
name|getFirstTrailer
parameter_list|()
block|{
if|if
condition|(
name|bytePosToXrefMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Set
argument_list|<
name|Long
argument_list|>
name|offsets
init|=
name|bytePosToXrefMap
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|Long
argument_list|>
name|sortedOffset
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|offsets
argument_list|)
decl_stmt|;
return|return
name|bytePosToXrefMap
operator|.
name|get
argument_list|(
name|sortedOffset
operator|.
name|first
argument_list|()
argument_list|)
operator|.
name|trailer
return|;
block|}
comment|/**      * Returns the last trailer if at least one exists.      *       * @return the last trailer ir null      */
specifier|public
specifier|final
name|COSDictionary
name|getLastTrailer
parameter_list|()
block|{
if|if
condition|(
name|bytePosToXrefMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Set
argument_list|<
name|Long
argument_list|>
name|offsets
init|=
name|bytePosToXrefMap
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|Long
argument_list|>
name|sortedOffset
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|offsets
argument_list|)
decl_stmt|;
return|return
name|bytePosToXrefMap
operator|.
name|get
argument_list|(
name|sortedOffset
operator|.
name|last
argument_list|()
argument_list|)
operator|.
name|trailer
return|;
block|}
comment|/**      * Returns the count of trailers.      *      * @return the count of trailers.      */
specifier|public
specifier|final
name|int
name|getTrailerCount
parameter_list|()
block|{
return|return
name|bytePosToXrefMap
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Signals that a new XRef object (table or stream) starts.      * @param startBytePos the offset to start at      * @param type the type of the Xref object      */
specifier|public
name|void
name|nextXrefObj
parameter_list|(
specifier|final
name|long
name|startBytePos
parameter_list|,
name|XRefType
name|type
parameter_list|)
block|{
name|curXrefTrailerObj
operator|=
operator|new
name|XrefTrailerObj
argument_list|()
expr_stmt|;
name|bytePosToXrefMap
operator|.
name|put
argument_list|(
name|startBytePos
argument_list|,
name|curXrefTrailerObj
argument_list|)
expr_stmt|;
name|curXrefTrailerObj
operator|.
name|xrefType
operator|=
name|type
expr_stmt|;
block|}
comment|/**      * Returns the XRefTxpe of the resolved trailer.      *       * @return the XRefType or null.      */
specifier|public
name|XRefType
name|getXrefType
parameter_list|()
block|{
return|return
operator|(
name|resolvedXrefTrailer
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|resolvedXrefTrailer
operator|.
name|xrefType
return|;
block|}
comment|/**      * Populate XRef HashMap of current XRef object.      * Will add an Xreftable entry that maps ObjectKeys to byte offsets in the file.      * @param objKey The objkey, with id and gen numbers      * @param offset The byte offset in this file      */
specifier|public
name|void
name|setXRef
parameter_list|(
name|COSObjectKey
name|objKey
parameter_list|,
name|long
name|offset
parameter_list|)
block|{
if|if
condition|(
name|curXrefTrailerObj
operator|==
literal|null
condition|)
block|{
comment|// should not happen...
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot add XRef entry for '"
operator|+
name|objKey
operator|.
name|getNumber
argument_list|()
operator|+
literal|"' because XRef start was not signalled."
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// PDFBOX-3506 check before adding to the map, to avoid entries from the table being
comment|// overwritten by obsolete entries in hybrid files (/XRefStm entry)
if|if
condition|(
operator|!
name|curXrefTrailerObj
operator|.
name|xrefTable
operator|.
name|containsKey
argument_list|(
name|objKey
argument_list|)
condition|)
block|{
name|curXrefTrailerObj
operator|.
name|xrefTable
operator|.
name|put
argument_list|(
name|objKey
argument_list|,
name|offset
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Adds trailer information for current XRef object.      *      * @param trailer the current document trailer dictionary      */
specifier|public
name|void
name|setTrailer
parameter_list|(
name|COSDictionary
name|trailer
parameter_list|)
block|{
if|if
condition|(
name|curXrefTrailerObj
operator|==
literal|null
condition|)
block|{
comment|// should not happen...
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot add trailer because XRef start was not signalled."
argument_list|)
expr_stmt|;
return|return;
block|}
name|curXrefTrailerObj
operator|.
name|trailer
operator|=
name|trailer
expr_stmt|;
block|}
comment|/**      * Returns the trailer last set by {@link #setTrailer(COSDictionary)}.      *       * @return the current trailer.      *       */
specifier|public
name|COSDictionary
name|getCurrentTrailer
parameter_list|()
block|{
return|return
name|curXrefTrailerObj
operator|.
name|trailer
return|;
block|}
comment|/**      * Sets the byte position of the first XRef      * (has to be called after very last startxref was read).      * This is used to resolve chain of active XRef/trailer.      *      * In case startxref position is not found we output a      * warning and use all XRef/trailer objects combined      * in byte position order.      * Thus for incomplete PDF documents with missing      * startxref one could call this method with parameter value -1.      *       * @param startxrefBytePosValue starting position of the first XRef      *       */
specifier|public
name|void
name|setStartxref
parameter_list|(
name|long
name|startxrefBytePosValue
parameter_list|)
block|{
if|if
condition|(
name|resolvedXrefTrailer
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Method must be called only ones with last startxref value."
argument_list|)
expr_stmt|;
return|return;
block|}
name|resolvedXrefTrailer
operator|=
operator|new
name|XrefTrailerObj
argument_list|()
expr_stmt|;
name|resolvedXrefTrailer
operator|.
name|trailer
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
name|XrefTrailerObj
name|curObj
init|=
name|bytePosToXrefMap
operator|.
name|get
argument_list|(
name|startxrefBytePosValue
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Long
argument_list|>
name|xrefSeqBytePos
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|curObj
operator|==
literal|null
condition|)
block|{
comment|// no XRef at given position
name|LOG
operator|.
name|warn
argument_list|(
literal|"Did not found XRef object at specified startxref position "
operator|+
name|startxrefBytePosValue
argument_list|)
expr_stmt|;
comment|// use all objects in byte position order (last entries overwrite previous ones)
name|xrefSeqBytePos
operator|.
name|addAll
argument_list|(
name|bytePosToXrefMap
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|xrefSeqBytePos
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// copy xref type
name|resolvedXrefTrailer
operator|.
name|xrefType
operator|=
name|curObj
operator|.
name|xrefType
expr_stmt|;
comment|// found starting Xref object
comment|// add this and follow chain defined by 'Prev' keys
name|xrefSeqBytePos
operator|.
name|add
argument_list|(
name|startxrefBytePosValue
argument_list|)
expr_stmt|;
while|while
condition|(
name|curObj
operator|.
name|trailer
operator|!=
literal|null
condition|)
block|{
name|long
name|prevBytePos
init|=
name|curObj
operator|.
name|trailer
operator|.
name|getLong
argument_list|(
name|COSName
operator|.
name|PREV
argument_list|,
operator|-
literal|1L
argument_list|)
decl_stmt|;
if|if
condition|(
name|prevBytePos
operator|==
operator|-
literal|1
condition|)
block|{
break|break;
block|}
name|curObj
operator|=
name|bytePosToXrefMap
operator|.
name|get
argument_list|(
name|prevBytePos
argument_list|)
expr_stmt|;
if|if
condition|(
name|curObj
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Did not found XRef object pointed to by 'Prev' key at position "
operator|+
name|prevBytePos
argument_list|)
expr_stmt|;
break|break;
block|}
name|xrefSeqBytePos
operator|.
name|add
argument_list|(
name|prevBytePos
argument_list|)
expr_stmt|;
comment|// sanity check to prevent infinite loops
if|if
condition|(
name|xrefSeqBytePos
operator|.
name|size
argument_list|()
operator|>=
name|bytePosToXrefMap
operator|.
name|size
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
comment|// have to reverse order so that later XRefs will overwrite previous ones
name|Collections
operator|.
name|reverse
argument_list|(
name|xrefSeqBytePos
argument_list|)
expr_stmt|;
block|}
comment|// merge used and sorted XRef/trailer
for|for
control|(
name|Long
name|bPos
range|:
name|xrefSeqBytePos
control|)
block|{
name|curObj
operator|=
name|bytePosToXrefMap
operator|.
name|get
argument_list|(
name|bPos
argument_list|)
expr_stmt|;
if|if
condition|(
name|curObj
operator|.
name|trailer
operator|!=
literal|null
condition|)
block|{
name|resolvedXrefTrailer
operator|.
name|trailer
operator|.
name|addAll
argument_list|(
name|curObj
operator|.
name|trailer
argument_list|)
expr_stmt|;
block|}
name|resolvedXrefTrailer
operator|.
name|xrefTable
operator|.
name|putAll
argument_list|(
name|curObj
operator|.
name|xrefTable
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets the resolved trailer. Might return<code>null</code> in case      * {@link #setStartxref(long)} was not called before.      *      * @return the trailer if available      */
specifier|public
name|COSDictionary
name|getTrailer
parameter_list|()
block|{
return|return
operator|(
name|resolvedXrefTrailer
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|resolvedXrefTrailer
operator|.
name|trailer
return|;
block|}
comment|/**      * Gets the resolved xref table. Might return<code>null</code> in case      *  {@link #setStartxref(long)} was not called before.      *      * @return the xrefTable if available      */
specifier|public
name|Map
argument_list|<
name|COSObjectKey
argument_list|,
name|Long
argument_list|>
name|getXrefTable
parameter_list|()
block|{
return|return
operator|(
name|resolvedXrefTrailer
operator|==
literal|null
operator|)
condition|?
literal|null
else|:
name|resolvedXrefTrailer
operator|.
name|xrefTable
return|;
block|}
comment|/** Returns object numbers which are referenced as contained      *  in object stream with specified object number.      *        *  This will scan resolved xref table for all entries having negated      *  stream object number as value.      *      *  @param objstmObjNr  object number of object stream for which contained object numbers      *                      should be returned      *                             *  @return set of object numbers referenced for given object stream      *          or<code>null</code> if {@link #setStartxref(long)} was not      *          called before so that no resolved xref table exists      */
specifier|public
name|Set
argument_list|<
name|Long
argument_list|>
name|getContainedObjectNumbers
parameter_list|(
specifier|final
name|int
name|objstmObjNr
parameter_list|)
block|{
if|if
condition|(
name|resolvedXrefTrailer
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|Set
argument_list|<
name|Long
argument_list|>
name|refObjNrs
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|long
name|cmpVal
init|=
operator|-
name|objstmObjNr
decl_stmt|;
for|for
control|(
name|Entry
argument_list|<
name|COSObjectKey
argument_list|,
name|Long
argument_list|>
name|xrefEntry
range|:
name|resolvedXrefTrailer
operator|.
name|xrefTable
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|xrefEntry
operator|.
name|getValue
argument_list|()
operator|==
name|cmpVal
condition|)
block|{
name|refObjNrs
operator|.
name|add
argument_list|(
name|xrefEntry
operator|.
name|getKey
argument_list|()
operator|.
name|getNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|refObjNrs
return|;
block|}
comment|/**      * Reset all data so that it can be used to rebuild the trailer.      *       */
specifier|protected
name|void
name|reset
parameter_list|()
block|{
for|for
control|(
name|XrefTrailerObj
name|trailerObj
range|:
name|bytePosToXrefMap
operator|.
name|values
argument_list|()
control|)
block|{
name|trailerObj
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
name|curXrefTrailerObj
operator|=
literal|null
expr_stmt|;
name|resolvedXrefTrailer
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

