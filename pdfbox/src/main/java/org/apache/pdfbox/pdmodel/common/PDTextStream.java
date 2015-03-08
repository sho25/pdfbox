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
name|common
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
name|ByteArrayInputStream
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
name|io
operator|.
name|IOException
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
name|io
operator|.
name|IOUtils
import|;
end_import

begin_comment
comment|/**  * A PDTextStream class is used when the PDF specification supports either  * a string or a stream for the value of an object.  This is usually when  * a value could be large or small, for example a JavaScript method.  This  * class will help abstract that and give a single unified interface to  * those types of fields.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|public
class|class
name|PDTextStream
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSString
name|string
decl_stmt|;
specifier|private
name|COSStream
name|stream
decl_stmt|;
comment|/**      * Constructor.      *      * @param str The string parameter.      */
specifier|public
name|PDTextStream
parameter_list|(
name|COSString
name|str
parameter_list|)
block|{
name|string
operator|=
name|str
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param str The string parameter.      */
specifier|public
name|PDTextStream
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|string
operator|=
operator|new
name|COSString
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param str The stream parameter.      */
specifier|public
name|PDTextStream
parameter_list|(
name|COSStream
name|str
parameter_list|)
block|{
name|stream
operator|=
name|str
expr_stmt|;
block|}
comment|/**      * This will create the text stream object.  base must either be a string      * or a stream.      *      * @param base The COS text stream object.      *      * @return A PDTextStream that wraps the base object.      */
specifier|public
specifier|static
name|PDTextStream
name|createTextStream
parameter_list|(
name|COSBase
name|base
parameter_list|)
block|{
name|PDTextStream
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|base
operator|instanceof
name|COSString
condition|)
block|{
name|retval
operator|=
operator|new
name|PDTextStream
argument_list|(
operator|(
name|COSString
operator|)
name|base
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|base
operator|instanceof
name|COSStream
condition|)
block|{
name|retval
operator|=
operator|new
name|PDTextStream
argument_list|(
operator|(
name|COSStream
operator|)
name|base
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
annotation|@
name|Override
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|string
operator|==
literal|null
condition|?
name|stream
else|:
name|string
return|;
block|}
comment|/**      * This will get this value as a string.  If this is a stream then it      * will load the entire stream into memory, so you should only do this when      * the stream is a manageable size.      *      * @return This value as a string.      *      * @throws IOException If an IO error occurs while accessing the stream.      */
specifier|public
name|String
name|getAsString
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|string
operator|!=
literal|null
condition|)
block|{
return|return
name|string
operator|.
name|getString
argument_list|()
return|;
block|}
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|stream
operator|.
name|getUnfilteredStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|is
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"ISO-8859-1"
argument_list|)
return|;
block|}
comment|/**      * This is the preferred way of getting data with this class as it uses      * a stream object.      *      * @return The stream object.      *      * @throws IOException If an IO error occurs while accessing the stream.      */
specifier|public
name|InputStream
name|getAsStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|retval
decl_stmt|;
if|if
condition|(
name|string
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|string
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
name|stream
operator|.
name|getUnfilteredStream
argument_list|()
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
block|}
end_class

end_unit

