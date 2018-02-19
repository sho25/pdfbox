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
name|io
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|BitSet
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

begin_comment
comment|/**  * Implements a memory page handling mechanism as base for creating (multiple)  * {@link RandomAccess} buffers each having its set of pages (implemented by  * {@link ScratchFileBuffer}). A buffer is created calling {@link #createBuffer()}.  *   *<p>Pages can be stored in main memory or in a temporary file. A mixed mode  * is supported storing a certain amount of pages in memory and only the  * additional ones in temporary file (defined by maximum main memory to  * be used).</p>  *   *<p>Pages can be marked as 'free' in order to re-use them. For in-memory pages  * this will release the used memory while for pages in temporary file this  * simply marks the area as free to re-use.</p>  *   *<p>If a temporary file was created (done with the first page to be stored  * in temporary file) it is deleted when {@link ScratchFile#close()} is called.</p>  *   *<p>Using this class for {@link RandomAccess} buffers allows for a direct control  * on the maximum memory usage and allows processing large files for which we  * otherwise would get an {@link OutOfMemoryError} in case of using {@link RandomAccessBuffer}.</p>  *   *<p>This base class for providing pages is thread safe (the buffer implementations are not).</p>  */
end_comment

begin_class
specifier|public
class|class
name|ScratchFile
implements|implements
name|Closeable
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
name|ScratchFile
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/** number of pages by which we enlarge the scratch file (reduce I/O-operations) */
specifier|private
specifier|static
specifier|final
name|int
name|ENLARGE_PAGE_COUNT
init|=
literal|16
decl_stmt|;
comment|/** in case of unrestricted main memory usage this is the initial number of pages      *  {@link #inMemoryPages} is setup for */
specifier|private
specifier|static
specifier|final
name|int
name|INIT_UNRESTRICTED_MAINMEM_PAGECOUNT
init|=
literal|100000
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|PAGE_SIZE
init|=
literal|4096
decl_stmt|;
specifier|private
specifier|final
name|Object
name|ioLock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|File
name|scratchFileDirectory
decl_stmt|;
comment|/** scratch file; only to be accessed under synchronization of {@link #ioLock} */
specifier|private
name|File
name|file
decl_stmt|;
comment|/** random access to scratch file; only to be accessed under synchronization of {@link #ioLock} */
specifier|private
name|java
operator|.
name|io
operator|.
name|RandomAccessFile
name|raf
decl_stmt|;
specifier|private
specifier|volatile
name|int
name|pageCount
init|=
literal|0
decl_stmt|;
specifier|private
specifier|final
name|BitSet
name|freePages
init|=
operator|new
name|BitSet
argument_list|()
decl_stmt|;
comment|/** holds pointers to in-memory page content; will be initialized once in case of restricted      *  main memory, otherwise it is enlarged as needed and first initialized to a size of      *  {@link #INIT_UNRESTRICTED_MAINMEM_PAGECOUNT} */
specifier|private
specifier|volatile
name|byte
index|[]
index|[]
name|inMemoryPages
decl_stmt|;
specifier|private
specifier|final
name|int
name|inMemoryMaxPageCount
decl_stmt|;
specifier|private
specifier|final
name|int
name|maxPageCount
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|useScratchFile
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|maxMainMemoryIsRestricted
decl_stmt|;
specifier|private
specifier|volatile
name|boolean
name|isClosed
init|=
literal|false
decl_stmt|;
comment|/**      * Initializes page handler. If a<code>scratchFileDirectory</code> is supplied,      * then the scratch file will be created in that directory.      *       *<p>All pages will be stored in the scratch file.</p>      *       * @param scratchFileDirectory The directory in which to create the scratch file      *                             or<code>null</code> to created it in the default temporary directory.      *       * @throws IOException If scratch file directory was given but don't exist.      */
specifier|public
name|ScratchFile
parameter_list|(
name|File
name|scratchFileDirectory
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|(
name|MemoryUsageSetting
operator|.
name|setupTempFileOnly
argument_list|()
operator|.
name|setTempDir
argument_list|(
name|scratchFileDirectory
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Initializes page handler. If a<code>scratchFileDirectory</code> is supplied,      * then the scratch file will be created in that directory.      *       *<p>Depending on the size of allowed memory usage a number of pages (memorySize/{@link #PAGE_SIZE})      * will be stored in-memory and only additional pages will be written to/read from scratch file.</p>      *       * @param memUsageSetting set how memory/temporary files are used for buffering streams etc.       *       * @throws IOException If scratch file directory was given but don't exist.      */
specifier|public
name|ScratchFile
parameter_list|(
name|MemoryUsageSetting
name|memUsageSetting
parameter_list|)
throws|throws
name|IOException
block|{
name|maxMainMemoryIsRestricted
operator|=
operator|(
operator|!
name|memUsageSetting
operator|.
name|useMainMemory
argument_list|()
operator|)
operator|||
name|memUsageSetting
operator|.
name|isMainMemoryRestricted
argument_list|()
expr_stmt|;
name|useScratchFile
operator|=
name|maxMainMemoryIsRestricted
condition|?
name|memUsageSetting
operator|.
name|useTempFile
argument_list|()
else|:
literal|false
expr_stmt|;
name|scratchFileDirectory
operator|=
name|useScratchFile
condition|?
name|memUsageSetting
operator|.
name|getTempDir
argument_list|()
else|:
literal|null
expr_stmt|;
if|if
condition|(
operator|(
name|scratchFileDirectory
operator|!=
literal|null
operator|)
operator|&&
operator|(
operator|!
name|scratchFileDirectory
operator|.
name|isDirectory
argument_list|()
operator|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Scratch file directory does not exist: "
operator|+
name|this
operator|.
name|scratchFileDirectory
argument_list|)
throw|;
block|}
name|maxPageCount
operator|=
name|memUsageSetting
operator|.
name|isStorageRestricted
argument_list|()
condition|?
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|memUsageSetting
operator|.
name|getMaxStorageBytes
argument_list|()
operator|/
name|PAGE_SIZE
argument_list|)
else|:
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
name|inMemoryMaxPageCount
operator|=
name|memUsageSetting
operator|.
name|useMainMemory
argument_list|()
condition|?
operator|(
name|memUsageSetting
operator|.
name|isMainMemoryRestricted
argument_list|()
condition|?
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|memUsageSetting
operator|.
name|getMaxMainMemoryBytes
argument_list|()
operator|/
name|PAGE_SIZE
argument_list|)
else|:
name|Integer
operator|.
name|MAX_VALUE
operator|)
else|:
literal|0
expr_stmt|;
name|inMemoryPages
operator|=
operator|new
name|byte
index|[
name|maxMainMemoryIsRestricted
condition|?
name|inMemoryMaxPageCount
else|:
name|INIT_UNRESTRICTED_MAINMEM_PAGECOUNT
index|]
index|[]
expr_stmt|;
name|freePages
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|inMemoryPages
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
comment|/**      * Getter for an instance using only unrestricted main memory for buffering      * (same as<code>new ScratchFile(MemoryUsageSetting.setupMainMemoryOnly())</code>).      *       * @return instance configured to only use main memory with no size restriction      */
specifier|public
specifier|static
name|ScratchFile
name|getMainMemoryOnlyInstance
parameter_list|()
block|{
try|try
block|{
return|return
operator|new
name|ScratchFile
argument_list|(
name|MemoryUsageSetting
operator|.
name|setupMainMemoryOnly
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
comment|// cannot happen for main memory setup
name|LOG
operator|.
name|error
argument_list|(
literal|"Unexpected exception occurred creating main memory scratch file instance: "
operator|+
name|ioe
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Getter for an instance to only use main-memory with the defined maximum.      *       * @param maxMainMemoryBytes maximum number of main-memory to be used;      *<code>-1</code> for no restriction;      *<code>0</code> will also be interpreted here as no restriction      *       * @return instance configured to only use main memory with no size restriction      */
specifier|public
specifier|static
name|ScratchFile
name|getMainMemoryOnlyInstance
parameter_list|(
name|long
name|maxMainMemoryBytes
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|ScratchFile
argument_list|(
name|MemoryUsageSetting
operator|.
name|setupMainMemoryOnly
argument_list|(
name|maxMainMemoryBytes
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
comment|// cannot happen for main memory setup
name|LOG
operator|.
name|error
argument_list|(
literal|"Unexpected exception occurred creating main memory scratch file instance: "
operator|+
name|ioe
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ioe
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns a new free page, either from free page pool      * or by enlarging scratch file (may be created).      *       * @return index of new page      */
name|int
name|getNewPage
parameter_list|()
throws|throws
name|IOException
block|{
synchronized|synchronized
init|(
name|freePages
init|)
block|{
name|int
name|idx
init|=
name|freePages
operator|.
name|nextSetBit
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|enlarge
argument_list|()
expr_stmt|;
name|idx
operator|=
name|freePages
operator|.
name|nextSetBit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Maximum allowed scratch file memory exceeded."
argument_list|)
throw|;
block|}
block|}
name|freePages
operator|.
name|clear
argument_list|(
name|idx
argument_list|)
expr_stmt|;
if|if
condition|(
name|idx
operator|>=
name|pageCount
condition|)
block|{
name|pageCount
operator|=
name|idx
operator|+
literal|1
expr_stmt|;
block|}
return|return
name|idx
return|;
block|}
block|}
comment|/**      * This will provide new free pages by either enlarging the scratch file       * by a number of pages defined by {@link #ENLARGE_PAGE_COUNT} - in case      * scratch file usage is allowed - or increase the {@link #inMemoryPages}      * array in case main memory was not restricted. If neither of both is      * allowed/the case than free pages count won't be changed. The same is true      * if no new pages could be added because we reached the maximum of      * {@link Integer#MAX_VALUE} pages.      *       *<p>If scratch file uage is allowed and scratch file does not exist already      * it will be created.</p>      *       *<p>Only to be called under synchronization on {@link #freePages}.</p>      */
specifier|private
name|void
name|enlarge
parameter_list|()
throws|throws
name|IOException
block|{
synchronized|synchronized
init|(
name|ioLock
init|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
if|if
condition|(
name|pageCount
operator|>=
name|maxPageCount
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|useScratchFile
condition|)
block|{
comment|// create scratch file is needed
if|if
condition|(
name|raf
operator|==
literal|null
condition|)
block|{
name|file
operator|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"PDFBox"
argument_list|,
literal|".tmp"
argument_list|,
name|scratchFileDirectory
argument_list|)
expr_stmt|;
try|try
block|{
name|raf
operator|=
operator|new
name|java
operator|.
name|io
operator|.
name|RandomAccessFile
argument_list|(
name|file
argument_list|,
literal|"rw"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|delete
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error deleting scratch file: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
block|}
name|long
name|fileLen
init|=
name|raf
operator|.
name|length
argument_list|()
decl_stmt|;
name|long
name|expectedFileLen
init|=
operator|(
operator|(
name|long
operator|)
name|pageCount
operator|-
name|inMemoryMaxPageCount
operator|)
operator|*
name|PAGE_SIZE
decl_stmt|;
if|if
condition|(
name|expectedFileLen
operator|!=
name|fileLen
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Expected scratch file size of "
operator|+
name|expectedFileLen
operator|+
literal|" but found "
operator|+
name|fileLen
argument_list|)
throw|;
block|}
comment|// enlarge if we do not overflow
if|if
condition|(
name|pageCount
operator|+
name|ENLARGE_PAGE_COUNT
operator|>
name|pageCount
condition|)
block|{
name|fileLen
operator|+=
name|ENLARGE_PAGE_COUNT
operator|*
name|PAGE_SIZE
expr_stmt|;
name|raf
operator|.
name|setLength
argument_list|(
name|fileLen
argument_list|)
expr_stmt|;
name|freePages
operator|.
name|set
argument_list|(
name|pageCount
argument_list|,
name|pageCount
operator|+
name|ENLARGE_PAGE_COUNT
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|maxMainMemoryIsRestricted
condition|)
block|{
comment|// increase number of in-memory pages
name|int
name|oldSize
init|=
name|inMemoryPages
operator|.
name|length
decl_stmt|;
name|int
name|newSize
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
operator|(
operator|(
name|long
operator|)
name|oldSize
operator|)
operator|*
literal|2
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
decl_stmt|;
comment|// this handles integer overflow
if|if
condition|(
name|newSize
operator|>
name|oldSize
condition|)
block|{
name|byte
index|[]
index|[]
name|newInMemoryPages
init|=
operator|new
name|byte
index|[
name|newSize
index|]
index|[]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|inMemoryPages
argument_list|,
literal|0
argument_list|,
name|newInMemoryPages
argument_list|,
literal|0
argument_list|,
name|oldSize
argument_list|)
expr_stmt|;
name|inMemoryPages
operator|=
name|newInMemoryPages
expr_stmt|;
name|freePages
operator|.
name|set
argument_list|(
name|oldSize
argument_list|,
name|newSize
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Returns byte size of a page.      *       * @return byte size of a page      */
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|PAGE_SIZE
return|;
block|}
comment|/**      * Reads the page with specified index.      *       * @param pageIdx index of page to read      *       * @return byte array of size {@link #PAGE_SIZE} filled with page data read from file       *       * @throws IOException      */
name|byte
index|[]
name|readPage
parameter_list|(
name|int
name|pageIdx
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|(
name|pageIdx
operator|<
literal|0
operator|)
operator|||
operator|(
name|pageIdx
operator|>=
name|pageCount
operator|)
condition|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Page index out of range: "
operator|+
name|pageIdx
operator|+
literal|". Max value: "
operator|+
operator|(
name|pageCount
operator|-
literal|1
operator|)
argument_list|)
throw|;
block|}
comment|// check if we have the page in memory
if|if
condition|(
name|pageIdx
operator|<
name|inMemoryMaxPageCount
condition|)
block|{
name|byte
index|[]
name|page
init|=
name|inMemoryPages
index|[
name|pageIdx
index|]
decl_stmt|;
comment|// handle case that we are closed
if|if
condition|(
name|page
operator|==
literal|null
condition|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Requested page with index "
operator|+
name|pageIdx
operator|+
literal|" was not written before."
argument_list|)
throw|;
block|}
return|return
name|page
return|;
block|}
synchronized|synchronized
init|(
name|ioLock
init|)
block|{
if|if
condition|(
name|raf
operator|==
literal|null
condition|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Missing scratch file to read page with index "
operator|+
name|pageIdx
operator|+
literal|" from."
argument_list|)
throw|;
block|}
name|byte
index|[]
name|page
init|=
operator|new
name|byte
index|[
name|PAGE_SIZE
index|]
decl_stmt|;
name|raf
operator|.
name|seek
argument_list|(
operator|(
operator|(
name|long
operator|)
name|pageIdx
operator|-
name|inMemoryMaxPageCount
operator|)
operator|*
name|PAGE_SIZE
argument_list|)
expr_stmt|;
name|raf
operator|.
name|readFully
argument_list|(
name|page
argument_list|)
expr_stmt|;
return|return
name|page
return|;
block|}
block|}
comment|/**      * Writes updated page. Page is either kept in-memory if pageIdx&lt; {@link #inMemoryMaxPageCount}      * or is written to scratch file.      *       *<p>Provided page byte array must not be re-used for other pages since we      * store it as is in case of in-memory handling.</p>      *       * @param pageIdx index of page to write      * @param page page to write (length has to be {@value #PAGE_SIZE})      *       * @throws IOException in case page index is out of range or page has wrong length      *                     or writing to file failed      */
name|void
name|writePage
parameter_list|(
name|int
name|pageIdx
parameter_list|,
name|byte
index|[]
name|page
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|(
name|pageIdx
operator|<
literal|0
operator|)
operator|||
operator|(
name|pageIdx
operator|>=
name|pageCount
operator|)
condition|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Page index out of range: "
operator|+
name|pageIdx
operator|+
literal|". Max value: "
operator|+
operator|(
name|pageCount
operator|-
literal|1
operator|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|page
operator|.
name|length
operator|!=
name|PAGE_SIZE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Wrong page size to write: "
operator|+
name|page
operator|.
name|length
operator|+
literal|". Expected: "
operator|+
name|PAGE_SIZE
argument_list|)
throw|;
block|}
if|if
condition|(
name|pageIdx
operator|<
name|inMemoryMaxPageCount
condition|)
block|{
if|if
condition|(
name|maxMainMemoryIsRestricted
condition|)
block|{
name|inMemoryPages
index|[
name|pageIdx
index|]
operator|=
name|page
expr_stmt|;
block|}
else|else
block|{
comment|// need synchronization since inMemoryPages may change
synchronized|synchronized
init|(
name|ioLock
init|)
block|{
name|inMemoryPages
index|[
name|pageIdx
index|]
operator|=
name|page
expr_stmt|;
block|}
block|}
comment|// in case we were closed in between throw exception
name|checkClosed
argument_list|()
expr_stmt|;
block|}
else|else
block|{
synchronized|synchronized
init|(
name|ioLock
init|)
block|{
name|checkClosed
argument_list|()
expr_stmt|;
name|raf
operator|.
name|seek
argument_list|(
operator|(
operator|(
name|long
operator|)
name|pageIdx
operator|-
name|inMemoryMaxPageCount
operator|)
operator|*
name|PAGE_SIZE
argument_list|)
expr_stmt|;
name|raf
operator|.
name|write
argument_list|(
name|page
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Checks if this page handler has already been closed. If so,      * an {@link IOException} is thrown.      *       * @throws IOException If {@link #close()} has already been called.      */
name|void
name|checkClosed
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|isClosed
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Scratch file already closed"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Creates a new buffer using this page handler.      *       * @return A new buffer.      *       * @throws IOException If an error occurred.      */
specifier|public
name|RandomAccess
name|createBuffer
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|ScratchFileBuffer
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Creates a new buffer using this page handler and initializes it with the      * data read from provided input stream (input stream is copied to buffer).      * The buffer data pointer is reset to point to first byte.      *       * @return A new buffer containing data read from input stream.      *       * @throws IOException If an error occurred.      */
specifier|public
name|RandomAccess
name|createBuffer
parameter_list|(
name|InputStream
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|ScratchFileBuffer
name|buf
init|=
operator|new
name|ScratchFileBuffer
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|byte
index|[]
name|byteBuffer
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|int
name|bytesRead
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|(
name|bytesRead
operator|=
name|input
operator|.
name|read
argument_list|(
name|byteBuffer
argument_list|)
operator|)
operator|>
operator|-
literal|1
condition|)
block|{
name|buf
operator|.
name|write
argument_list|(
name|byteBuffer
argument_list|,
literal|0
argument_list|,
name|bytesRead
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|seek
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
name|buf
return|;
block|}
comment|/**      * Allows a buffer which is cleared/closed to release its pages to be re-used.      *       * @param pageIndexes pages indexes of pages to release      * @param count number of page indexes contained in provided array       */
name|void
name|markPagesAsFree
parameter_list|(
name|int
index|[]
name|pageIndexes
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|count
parameter_list|)
block|{
synchronized|synchronized
init|(
name|freePages
init|)
block|{
for|for
control|(
name|int
name|aIdx
init|=
name|off
init|;
name|aIdx
operator|<
name|count
condition|;
name|aIdx
operator|++
control|)
block|{
name|int
name|pageIdx
init|=
name|pageIndexes
index|[
name|aIdx
index|]
decl_stmt|;
if|if
condition|(
operator|(
name|pageIdx
operator|>=
literal|0
operator|)
operator|&&
operator|(
name|pageIdx
operator|<
name|pageCount
operator|)
operator|&&
operator|(
operator|!
name|freePages
operator|.
name|get
argument_list|(
name|pageIdx
argument_list|)
operator|)
condition|)
block|{
name|freePages
operator|.
name|set
argument_list|(
name|pageIdx
argument_list|)
expr_stmt|;
if|if
condition|(
name|pageIdx
operator|<
name|inMemoryMaxPageCount
condition|)
block|{
name|inMemoryPages
index|[
name|pageIdx
index|]
operator|=
literal|null
expr_stmt|;
comment|// remark: not in ioLock synchronization since behavior won't
comment|// change even in case of parallel called 'enlarge' method
block|}
block|}
block|}
block|}
block|}
comment|/**      * Closes and deletes the temporary file. No further interaction with      * the scratch file or associated buffers can happen after this method is called.      * It also releases in-memory pages.      *       * @throws IOException If there was a problem closing or deleting the temporary file.      */
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|IOException
name|ioexc
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|ioLock
init|)
block|{
if|if
condition|(
name|isClosed
condition|)
block|{
return|return;
block|}
name|isClosed
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|raf
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|raf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|ioexc
operator|=
name|ioe
expr_stmt|;
block|}
block|}
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|delete
argument_list|()
condition|)
block|{
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
operator|(
name|ioexc
operator|==
literal|null
operator|)
condition|)
block|{
name|ioexc
operator|=
operator|new
name|IOException
argument_list|(
literal|"Error deleting scratch file: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
synchronized|synchronized
init|(
name|freePages
init|)
block|{
name|freePages
operator|.
name|clear
argument_list|()
expr_stmt|;
name|pageCount
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|ioexc
operator|!=
literal|null
condition|)
block|{
throw|throw
name|ioexc
throw|;
block|}
block|}
block|}
end_class

end_unit

