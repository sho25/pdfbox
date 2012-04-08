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
package|;
end_package

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
name|HashMap
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
name|COSDocument
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
name|pdfparser
operator|.
name|ConformingPDFParser
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
name|persistence
operator|.
name|util
operator|.
name|COSObjectKey
import|;
end_import

begin_comment
comment|/**  *  * @author adam  */
end_comment

begin_class
specifier|public
class|class
name|ConformingPDDocument
extends|extends
name|PDDocument
block|{
comment|/**      * Maps ObjectKeys to a COSObject. Note that references to these objects      * are also stored in COSDictionary objects that map a name to a specific object.      */
specifier|private
specifier|final
name|Map
argument_list|<
name|COSObjectKey
argument_list|,
name|COSBase
argument_list|>
name|objectPool
init|=
operator|new
name|HashMap
argument_list|<
name|COSObjectKey
argument_list|,
name|COSBase
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ConformingPDFParser
name|parser
init|=
literal|null
decl_stmt|;
specifier|public
name|ConformingPDDocument
parameter_list|()
throws|throws
name|IOException
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ConformingPDDocument
parameter_list|(
name|COSDocument
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will load a document from an input stream.      * @param input The File which contains the document.      * @return The document that was loaded.      * @throws IOException If there is an error reading from the stream.      */
specifier|public
specifier|static
name|PDDocument
name|load
parameter_list|(
name|File
name|input
parameter_list|)
throws|throws
name|IOException
block|{
name|ConformingPDFParser
name|parser
init|=
operator|new
name|ConformingPDFParser
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|()
expr_stmt|;
return|return
name|parser
operator|.
name|getPDDocument
argument_list|()
return|;
block|}
comment|/**      * This will get an object from the pool.      * @param key The object key.      * @return The object in the pool or a new one if it has not been parsed yet.      * @throws IOException If there is an error getting the proxy object.      */
specifier|public
name|COSBase
name|getObjectFromPool
parameter_list|(
name|COSObjectKey
name|key
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|objectPool
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**      * This will get an object from the pool.      * @param key The object key.      * @return The object in the pool or a new one if it has not been parsed yet.      * @throws IOException If there is an error getting the proxy object.      */
specifier|public
name|List
argument_list|<
name|COSObjectKey
argument_list|>
name|getObjectKeysFromPool
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|COSObjectKey
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|COSObjectKey
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|COSObjectKey
name|key
range|:
name|objectPool
operator|.
name|keySet
argument_list|()
control|)
name|keys
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
name|keys
return|;
block|}
comment|/**      * This will get an object from the pool.      * @param number the object number      * @param generation the generation of this object you wish to load      * @return The object in the pool      * @throws IOException If there is an error getting the proxy object.      */
specifier|public
name|COSBase
name|getObjectFromPool
parameter_list|(
name|long
name|number
parameter_list|,
name|long
name|generation
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|objectPool
operator|.
name|get
argument_list|(
operator|new
name|COSObjectKey
argument_list|(
name|number
argument_list|,
name|generation
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|putObjectInPool
parameter_list|(
name|COSBase
name|object
parameter_list|,
name|long
name|number
parameter_list|,
name|long
name|generation
parameter_list|)
block|{
name|objectPool
operator|.
name|put
argument_list|(
operator|new
name|COSObjectKey
argument_list|(
name|number
argument_list|,
name|generation
argument_list|)
argument_list|,
name|object
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return the parser      */
specifier|public
name|ConformingPDFParser
name|getParser
parameter_list|()
block|{
return|return
name|parser
return|;
block|}
comment|/**      * @param parser the parser to set      */
specifier|public
name|void
name|setParser
parameter_list|(
name|ConformingPDFParser
name|parser
parameter_list|)
block|{
name|this
operator|.
name|parser
operator|=
name|parser
expr_stmt|;
block|}
block|}
end_class

end_unit

