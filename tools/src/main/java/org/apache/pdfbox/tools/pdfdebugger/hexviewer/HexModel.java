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
name|tools
operator|.
name|pdfdebugger
operator|.
name|hexviewer
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
name|List
import|;
end_import

begin_comment
comment|/**  * @author Khyrul Bashar  *  * A class that acts as a model for the hex viewer. It holds the data and provide the data as ncessary.  * It'll let listen for any underlying data changes.  */
end_comment

begin_class
class|class
name|HexModel
implements|implements
name|HexChangeListener
block|{
specifier|private
specifier|final
name|List
argument_list|<
name|Byte
argument_list|>
name|data
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|HexModelChangeListener
argument_list|>
name|modelChangeListeners
decl_stmt|;
comment|/**      * Constructor      * @param bytes Byte array.      */
specifier|public
name|HexModel
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|data
operator|=
operator|new
name|ArrayList
argument_list|<
name|Byte
argument_list|>
argument_list|(
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|byte
name|b
range|:
name|bytes
control|)
block|{
name|data
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
name|modelChangeListeners
operator|=
operator|new
name|ArrayList
argument_list|<
name|HexModelChangeListener
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**      * provides the byte for a specific index of the byte array.      * @param index int.      * @return byte instance      */
specifier|public
name|byte
name|getByte
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|data
operator|.
name|get
argument_list|(
name|index
argument_list|)
return|;
block|}
comment|/**      * Provides a character array of 16 characters on availability.      * @param lineNumber int. The line number of the characters. Line counting starts from 1.      * @return A char array.      */
specifier|public
name|char
index|[]
name|getLineChars
parameter_list|(
name|int
name|lineNumber
parameter_list|)
block|{
name|int
name|start
init|=
operator|(
name|lineNumber
operator|-
literal|1
operator|)
operator|*
literal|16
decl_stmt|;
name|int
name|length
init|=
name|data
operator|.
name|size
argument_list|()
operator|-
name|start
operator|<
literal|16
condition|?
name|data
operator|.
name|size
argument_list|()
operator|-
name|start
else|:
literal|16
decl_stmt|;
name|char
index|[]
name|chars
init|=
operator|new
name|char
index|[
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
name|chars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|Character
operator|.
name|toChars
argument_list|(
name|data
operator|.
name|get
argument_list|(
name|start
argument_list|)
operator|&
literal|0XFF
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
operator|!
name|isAsciiPrintable
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|c
operator|=
literal|'.'
expr_stmt|;
block|}
name|chars
index|[
name|i
index|]
operator|=
name|c
expr_stmt|;
name|start
operator|++
expr_stmt|;
block|}
return|return
name|chars
return|;
block|}
specifier|public
name|byte
index|[]
name|getBytesForLine
parameter_list|(
name|int
name|lineNumber
parameter_list|)
block|{
name|int
name|index
init|=
operator|(
name|lineNumber
operator|-
literal|1
operator|)
operator|*
literal|16
decl_stmt|;
name|int
name|length
init|=
name|Math
operator|.
name|min
argument_list|(
name|data
operator|.
name|size
argument_list|()
operator|-
name|index
argument_list|,
literal|16
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
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
name|bytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|bytes
index|[
name|i
index|]
operator|=
name|data
operator|.
name|get
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|index
operator|++
expr_stmt|;
block|}
return|return
name|bytes
return|;
block|}
comment|/**      * Provides the size of the model i.e. size of the input.      * @return int value.      */
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|data
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      *      * @return      */
specifier|public
name|int
name|totalLine
parameter_list|()
block|{
return|return
name|size
argument_list|()
operator|%
literal|16
operator|!=
literal|0
condition|?
name|size
argument_list|()
operator|/
literal|16
operator|+
literal|1
else|:
name|size
argument_list|()
operator|/
literal|16
return|;
block|}
specifier|public
specifier|static
name|int
name|lineNumber
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|int
name|elementNo
init|=
name|index
operator|+
literal|1
decl_stmt|;
return|return
name|elementNo
operator|%
literal|16
operator|!=
literal|0
condition|?
name|elementNo
operator|/
literal|16
operator|+
literal|1
else|:
name|elementNo
operator|/
literal|16
return|;
block|}
specifier|public
specifier|static
name|int
name|elementIndexInLine
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|index
operator|%
literal|16
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isAsciiPrintable
parameter_list|(
name|char
name|ch
parameter_list|)
block|{
return|return
name|ch
operator|>=
literal|32
operator|&&
name|ch
operator|<
literal|127
return|;
block|}
specifier|public
name|void
name|addHexModelChangeListener
parameter_list|(
name|HexModelChangeListener
name|listener
parameter_list|)
block|{
name|modelChangeListeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|updateModel
parameter_list|(
name|int
name|index
parameter_list|,
name|byte
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|data
operator|.
name|get
argument_list|(
name|index
argument_list|)
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|data
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|value
argument_list|)
expr_stmt|;
for|for
control|(
name|HexModelChangeListener
name|listener
range|:
name|modelChangeListeners
control|)
block|{
name|listener
operator|.
name|hexModelChanged
argument_list|(
operator|new
name|HexModelChangedEvent
argument_list|(
name|index
argument_list|,
name|HexModelChangedEvent
operator|.
name|SINGLE_CHANGE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|fireModelChanged
parameter_list|(
name|int
name|index
parameter_list|)
block|{
for|for
control|(
name|HexModelChangeListener
name|listener
range|:
name|modelChangeListeners
control|)
block|{
name|listener
operator|.
name|hexModelChanged
argument_list|(
operator|new
name|HexModelChangedEvent
argument_list|(
name|index
argument_list|,
name|HexModelChangedEvent
operator|.
name|SINGLE_CHANGE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|hexChanged
parameter_list|(
name|HexChangedEvent
name|event
parameter_list|)
block|{
name|int
name|index
init|=
name|event
operator|.
name|getByteIndex
argument_list|()
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
operator|&&
name|getByte
argument_list|(
name|index
argument_list|)
operator|!=
name|event
operator|.
name|getNewValue
argument_list|()
condition|)
block|{
name|data
operator|.
name|set
argument_list|(
name|index
argument_list|,
name|event
operator|.
name|getNewValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fireModelChanged
argument_list|(
name|index
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

