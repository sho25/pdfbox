begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Copyright (c) 2003, www.pdfbox.org  * All rights reserved.  *  * Redistribution and use in source and binary forms, with or without  * modification, are permitted provided that the following conditions are met:  *  * 1. Redistributions of source code must retain the above copyright notice,  *    this list of conditions and the following disclaimer.  * 2. Redistributions in binary form must reproduce the above copyright notice,  *    this list of conditions and the following disclaimer in the documentation  *    and/or other materials provided with the distribution.  * 3. Neither the name of pdfbox; nor the names of its  *    contributors may be used to endorse or promote products derived from this  *    software without specific prior written permission.  *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"  * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE  * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE  * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY  * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.  *  * http://www.pdfbox.org  *  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|filter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|HashMap
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

begin_comment
comment|/**  * This is the used for the LZWDecode filter.  This represents the dictionary mappings  * between codes and their values.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.4 $  */
end_comment

begin_class
class|class
name|LZWDictionary
block|{
specifier|private
name|Map
name|codeToData
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|LZWNode
name|root
init|=
operator|new
name|LZWNode
argument_list|()
decl_stmt|;
specifier|private
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|private
name|long
name|nextCode
init|=
literal|258
decl_stmt|;
specifier|private
name|int
name|codeSize
init|=
literal|9
decl_stmt|;
comment|/**      * constructor.      */
specifier|public
name|LZWDictionary
parameter_list|()
block|{
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|256
condition|;
name|i
operator|++
control|)
block|{
name|LZWNode
name|node
init|=
operator|new
name|LZWNode
argument_list|()
decl_stmt|;
name|node
operator|.
name|setCode
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|root
operator|.
name|setNode
argument_list|(
operator|(
name|byte
operator|)
name|i
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|codeToData
operator|.
name|put
argument_list|(
operator|new
name|Long
argument_list|(
name|i
argument_list|)
argument_list|,
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
name|i
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will get the value for the code.  It will return null if the code is not      * defined.      *      * @param code The key to the data.      *      * @return The data that is mapped to the code.      */
specifier|public
name|byte
index|[]
name|getData
parameter_list|(
name|long
name|code
parameter_list|)
block|{
return|return
operator|(
name|byte
index|[]
operator|)
name|codeToData
operator|.
name|get
argument_list|(
operator|new
name|Long
argument_list|(
name|code
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * This will take a visit from a byte[].  This will create new code entries as      * necessary.      *      * @param data The byte to get a visit from.      *      * @throws IOException If there is an error visiting this data.      */
specifier|public
name|void
name|visit
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|data
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|visit
argument_list|(
name|data
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This will take a visit from a byte.  This will create new code entries as      * necessary.      *      * @param data The byte to get a visit from.      *      * @throws IOException If there is an error visiting this data.      */
specifier|public
name|void
name|visit
parameter_list|(
name|byte
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|buffer
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|byte
index|[]
name|curBuffer
init|=
name|buffer
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|LZWNode
name|previous
init|=
literal|null
decl_stmt|;
name|LZWNode
name|current
init|=
name|root
decl_stmt|;
name|boolean
name|createNewCode
init|=
literal|false
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
name|curBuffer
operator|.
name|length
operator|&&
name|current
operator|!=
literal|null
condition|;
name|i
operator|++
control|)
block|{
name|previous
operator|=
name|current
expr_stmt|;
name|current
operator|=
name|current
operator|.
name|getNode
argument_list|(
name|curBuffer
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|current
operator|==
literal|null
condition|)
block|{
name|createNewCode
operator|=
literal|true
expr_stmt|;
name|current
operator|=
operator|new
name|LZWNode
argument_list|()
expr_stmt|;
name|previous
operator|.
name|setNode
argument_list|(
name|curBuffer
index|[
name|i
index|]
argument_list|,
name|current
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|createNewCode
condition|)
block|{
name|long
name|code
init|=
name|nextCode
operator|++
decl_stmt|;
name|current
operator|.
name|setCode
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|codeToData
operator|.
name|put
argument_list|(
operator|new
name|Long
argument_list|(
name|code
argument_list|)
argument_list|,
name|curBuffer
argument_list|)
expr_stmt|;
comment|/**             System.out.print( "Adding " + code + "='" );             for( int i=0; i<curBuffer.length; i++ )             {                 String hex = Integer.toHexString( ((curBuffer[i]+256)%256) );                 if( hex.length()<=1 )                 {                     hex = "0" + hex;                 }                 if( i != curBuffer.length -1 )                 {                     hex += " ";                 }                 System.out.print( hex.toUpperCase() );             }             System.out.println( "'" );             **/
name|buffer
operator|.
name|reset
argument_list|()
expr_stmt|;
name|buffer
operator|.
name|write
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|resetCodeSize
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * This will get the next code that will be created.      *      * @return The next code to be created.      */
specifier|public
name|long
name|getNextCode
parameter_list|()
block|{
return|return
name|nextCode
return|;
block|}
comment|/**      * This will get the size of the code in bits, 9, 10, or 11.      *      * @return The size of the code in bits.      */
specifier|public
name|int
name|getCodeSize
parameter_list|()
block|{
return|return
name|codeSize
return|;
block|}
comment|/**      * This will determine the code size.      */
specifier|private
name|void
name|resetCodeSize
parameter_list|()
block|{
if|if
condition|(
name|nextCode
operator|>=
literal|2048
condition|)
block|{
name|codeSize
operator|=
literal|12
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nextCode
operator|>=
literal|1024
condition|)
block|{
name|codeSize
operator|=
literal|11
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nextCode
operator|>=
literal|512
condition|)
block|{
name|codeSize
operator|=
literal|10
expr_stmt|;
block|}
else|else
block|{
name|codeSize
operator|=
literal|9
expr_stmt|;
block|}
block|}
comment|/**      * This will crear the internal buffer that the dictionary uses.      */
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|buffer
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
comment|/**      * This will folow the path to the data node.      *      * @param data The path to the node.      *      * @return The node that resides at that path.      */
specifier|public
name|LZWNode
name|getNode
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
name|root
operator|.
name|getNode
argument_list|(
name|data
argument_list|)
return|;
block|}
block|}
end_class

end_unit

