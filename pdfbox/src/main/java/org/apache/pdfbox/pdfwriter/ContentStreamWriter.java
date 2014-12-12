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
name|pdfwriter
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
name|io
operator|.
name|OutputStream
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
name|COSBoolean
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
name|COSInteger
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
name|COSString
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
name|operator
operator|.
name|Operator
import|;
end_import

begin_comment
comment|/**  * A class that will take a list of tokens and write out a stream with them.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.8 $  */
end_comment

begin_class
specifier|public
class|class
name|ContentStreamWriter
block|{
specifier|private
name|OutputStream
name|output
decl_stmt|;
comment|/**      * space character.      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|SPACE
init|=
operator|new
name|byte
index|[]
block|{
literal|32
block|}
decl_stmt|;
comment|/**      * standard line separator      */
specifier|public
specifier|static
specifier|final
name|byte
index|[]
name|EOL
init|=
operator|new
name|byte
index|[]
block|{
literal|0x0A
block|}
decl_stmt|;
comment|/**      * This will create a new content stream writer.      *      * @param out The stream to write the data to.      */
specifier|public
name|ContentStreamWriter
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|output
operator|=
name|out
expr_stmt|;
block|}
comment|/**      * This will write out the list of tokens to the stream.      *      * @param tokens The tokens to write to the stream.      * @param start The start index into the list of tokens.      * @param end The end index into the list of tokens.      * @throws IOException If there is an error writing to the stream.      */
specifier|public
name|void
name|writeTokens
parameter_list|(
name|List
name|tokens
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|i
init|=
name|start
init|;
name|i
operator|<
name|end
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|o
init|=
name|tokens
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|writeObject
argument_list|(
name|o
argument_list|)
expr_stmt|;
comment|//write a space between each object.
name|output
operator|.
name|write
argument_list|(
literal|32
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|writeObject
parameter_list|(
name|Object
name|o
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|o
operator|instanceof
name|COSString
condition|)
block|{
name|COSWriter
operator|.
name|writeString
argument_list|(
operator|(
name|COSString
operator|)
name|o
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|COSFloat
condition|)
block|{
operator|(
operator|(
name|COSFloat
operator|)
name|o
operator|)
operator|.
name|writePDF
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|COSInteger
condition|)
block|{
operator|(
operator|(
name|COSInteger
operator|)
name|o
operator|)
operator|.
name|writePDF
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|COSBoolean
condition|)
block|{
operator|(
operator|(
name|COSBoolean
operator|)
name|o
operator|)
operator|.
name|writePDF
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|COSName
condition|)
block|{
operator|(
operator|(
name|COSName
operator|)
name|o
operator|)
operator|.
name|writePDF
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|COSArray
condition|)
block|{
name|COSArray
name|array
init|=
operator|(
name|COSArray
operator|)
name|o
decl_stmt|;
name|output
operator|.
name|write
argument_list|(
name|COSWriter
operator|.
name|ARRAY_OPEN
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|array
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|writeObject
argument_list|(
name|array
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|SPACE
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|write
argument_list|(
name|COSWriter
operator|.
name|ARRAY_CLOSE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|COSDictionary
condition|)
block|{
name|COSDictionary
name|obj
init|=
operator|(
name|COSDictionary
operator|)
name|o
decl_stmt|;
name|output
operator|.
name|write
argument_list|(
name|COSWriter
operator|.
name|DICT_OPEN
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|COSName
argument_list|,
name|COSBase
argument_list|>
name|entry
range|:
name|obj
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|writeObject
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|SPACE
argument_list|)
expr_stmt|;
name|writeObject
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|SPACE
argument_list|)
expr_stmt|;
block|}
block|}
name|output
operator|.
name|write
argument_list|(
name|COSWriter
operator|.
name|DICT_CLOSE
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|SPACE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|Operator
condition|)
block|{
name|Operator
name|op
init|=
operator|(
name|Operator
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"BI"
argument_list|)
condition|)
block|{
name|output
operator|.
name|write
argument_list|(
literal|"BI"
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|COSDictionary
name|dic
init|=
name|op
operator|.
name|getImageParameters
argument_list|()
decl_stmt|;
for|for
control|(
name|COSName
name|key
range|:
name|dic
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|dic
operator|.
name|getDictionaryObject
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|key
operator|.
name|writePDF
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|SPACE
argument_list|)
expr_stmt|;
name|writeObject
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|EOL
argument_list|)
expr_stmt|;
block|}
name|output
operator|.
name|write
argument_list|(
literal|"ID"
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|EOL
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|op
operator|.
name|getImageData
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|output
operator|.
name|write
argument_list|(
name|op
operator|.
name|getName
argument_list|()
operator|.
name|getBytes
argument_list|(
literal|"ISO-8859-1"
argument_list|)
argument_list|)
expr_stmt|;
name|output
operator|.
name|write
argument_list|(
name|EOL
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error:Unknown type in content stream:"
operator|+
name|o
argument_list|)
throw|;
block|}
block|}
comment|/**      * This will write out the list of tokens to the stream.      *      * @param tokens The tokens to write to the stream.      * @throws IOException If there is an error writing to the stream.      */
specifier|public
name|void
name|writeTokens
parameter_list|(
name|List
name|tokens
parameter_list|)
throws|throws
name|IOException
block|{
name|writeTokens
argument_list|(
name|tokens
argument_list|,
literal|0
argument_list|,
name|tokens
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

