begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*******************************************************************************  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *    *   http://www.apache.org/licenses/LICENSE-2.0  *    *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  *    *******************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|wink
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|BindException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketException
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
name|javax
operator|.
name|net
operator|.
name|ServerSocketFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLServerSocketFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
operator|.
name|Status
import|;
end_import

begin_comment
comment|/**  * Copied from  * http://svn.apache.org/repos/asf/wink/trunk/wink-component-test-support/src/main/java/org/apache/wink/client/MockHttpServer.java  * on 28.7.2018.  */
end_comment

begin_class
specifier|public
class|class
name|MockHttpServer
extends|extends
name|Thread
block|{
specifier|public
specifier|static
class|class
name|MockHttpServerResponse
block|{
comment|// mock response data
specifier|private
name|int
name|mockResponseCode
init|=
literal|200
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mockResponseHeaders
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|byte
index|[]
name|mockResponseContent
init|=
literal|"received message"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
specifier|private
name|String
name|mockResponseContentType
init|=
literal|"text/plain;charset=utf-8"
decl_stmt|;
specifier|private
name|boolean
name|mockResponseContentEchoRequest
decl_stmt|;
specifier|public
name|void
name|setMockResponseHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|)
block|{
name|mockResponseHeaders
operator|.
name|clear
argument_list|()
expr_stmt|;
name|mockResponseHeaders
operator|.
name|putAll
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setMockResponseHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|mockResponseHeaders
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMockResponseHeaders
parameter_list|()
block|{
return|return
name|mockResponseHeaders
return|;
block|}
specifier|public
name|void
name|setMockResponseCode
parameter_list|(
name|int
name|responseCode
parameter_list|)
block|{
name|this
operator|.
name|mockResponseCode
operator|=
name|responseCode
expr_stmt|;
block|}
specifier|public
name|int
name|getMockResponseCode
parameter_list|()
block|{
return|return
name|mockResponseCode
return|;
block|}
specifier|public
name|void
name|setMockResponseContent
parameter_list|(
name|String
name|content
parameter_list|)
block|{
name|mockResponseContent
operator|=
name|content
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setMockResponseContent
parameter_list|(
name|byte
index|[]
name|content
parameter_list|)
block|{
name|mockResponseContent
operator|=
name|content
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getMockResponseContent
parameter_list|()
block|{
return|return
name|mockResponseContent
return|;
block|}
specifier|public
name|void
name|setMockResponseContentType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|mockResponseContentType
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|String
name|getMockResponseContentType
parameter_list|()
block|{
return|return
name|mockResponseContentType
return|;
block|}
specifier|public
name|void
name|setMockResponseContentEchoRequest
parameter_list|(
name|boolean
name|echo
parameter_list|)
block|{
name|mockResponseContentEchoRequest
operator|=
name|echo
expr_stmt|;
block|}
specifier|public
name|boolean
name|getMockResponseContentEchoRequest
parameter_list|()
block|{
return|return
name|mockResponseContentEchoRequest
return|;
block|}
block|}
specifier|private
name|Thread
name|serverThread
init|=
literal|null
decl_stmt|;
specifier|private
name|ServerSocket
name|serverSocket
init|=
literal|null
decl_stmt|;
specifier|private
name|boolean
name|serverStarted
init|=
literal|false
decl_stmt|;
specifier|private
name|ServerSocketFactory
name|serverSocketFactory
init|=
literal|null
decl_stmt|;
specifier|private
name|int
name|serverPort
decl_stmt|;
specifier|private
name|int
name|readTimeOut
init|=
literal|5000
decl_stmt|;
comment|// 5
comment|// seconds
specifier|private
name|int
name|delayResponseTime
init|=
literal|0
decl_stmt|;
specifier|private
specifier|static
name|byte
index|[]
name|NEW_LINE
init|=
literal|"\r\n"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
comment|// request data
specifier|private
name|String
name|requestMethod
init|=
literal|null
decl_stmt|;
specifier|private
name|String
name|requestUrl
init|=
literal|null
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|requestHeaders
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ByteArrayOutputStream
name|requestContent
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|MockHttpServerResponse
argument_list|>
name|mockHttpServerResponses
init|=
operator|new
name|ArrayList
argument_list|<
name|MockHttpServerResponse
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|int
name|responseCounter
init|=
literal|0
decl_stmt|;
specifier|public
name|MockHttpServer
parameter_list|(
name|int
name|serverPort
parameter_list|)
block|{
name|this
argument_list|(
name|serverPort
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MockHttpServer
parameter_list|(
name|int
name|serverPort
parameter_list|,
name|boolean
name|ssl
parameter_list|)
block|{
name|mockHttpServerResponses
operator|.
name|add
argument_list|(
operator|new
name|MockHttpServerResponse
argument_list|()
argument_list|)
expr_stmt|;
comment|// set a
comment|// default
comment|// response
name|this
operator|.
name|serverPort
operator|=
name|serverPort
expr_stmt|;
try|try
block|{
name|serverSocketFactory
operator|=
name|ServerSocketFactory
operator|.
name|getDefault
argument_list|()
expr_stmt|;
if|if
condition|(
name|ssl
condition|)
block|{
name|serverSocketFactory
operator|=
name|SSLServerSocketFactory
operator|.
name|getDefault
argument_list|()
expr_stmt|;
block|}
while|while
condition|(
name|serverSocket
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|serverSocket
operator|=
name|serverSocketFactory
operator|.
name|createServerSocket
argument_list|(
operator|++
name|this
operator|.
name|serverPort
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BindException
name|e
parameter_list|)
block|{                  }
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|synchronized
name|void
name|startServer
parameter_list|()
block|{
if|if
condition|(
name|serverStarted
condition|)
return|return;
comment|// start the server thread
name|start
argument_list|()
expr_stmt|;
name|serverStarted
operator|=
literal|true
expr_stmt|;
comment|// wait for the server thread to start
name|waitForServerToStart
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|waitForServerToStart
parameter_list|()
block|{
try|try
block|{
name|wait
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|waitForServerToStop
parameter_list|()
block|{
try|try
block|{
name|wait
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
name|serverThread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
expr_stmt|;
name|executeLoop
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|executeLoop
parameter_list|()
block|{
name|serverStarted
argument_list|()
expr_stmt|;
try|try
block|{
while|while
condition|(
literal|true
condition|)
block|{
name|Socket
name|socket
init|=
name|serverSocket
operator|.
name|accept
argument_list|()
decl_stmt|;
name|HttpProcessor
name|processor
init|=
operator|new
name|HttpProcessor
argument_list|(
name|socket
argument_list|)
decl_stmt|;
name|processor
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|SocketException
condition|)
block|{
if|if
condition|(
operator|!
operator|(
literal|"Socket closed"
operator|.
name|equalsIgnoreCase
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|||
literal|"Socket is closed"
operator|.
name|equalsIgnoreCase
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
comment|// notify that the server was stopped
name|serverStopped
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|synchronized
name|void
name|serverStarted
parameter_list|()
block|{
comment|// notify the waiting thread that the thread started
name|notifyAll
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|synchronized
name|void
name|serverStopped
parameter_list|()
block|{
comment|// notify the waiting thread that the thread started
name|notifyAll
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|synchronized
name|void
name|stopServer
parameter_list|()
block|{
if|if
condition|(
operator|!
name|serverStarted
condition|)
return|return;
try|try
block|{
name|serverStarted
operator|=
literal|false
expr_stmt|;
comment|// the server may be sleeping somewhere...
name|serverThread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
comment|// close the server socket
name|serverSocket
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// wait for the server to stop
name|waitForServerToStop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
class|class
name|HttpProcessor
block|{
specifier|private
name|Socket
name|socket
decl_stmt|;
specifier|public
name|HttpProcessor
parameter_list|(
name|Socket
name|socket
parameter_list|)
throws|throws
name|SocketException
block|{
comment|// set the read timeout (5 seconds by default)
name|socket
operator|.
name|setSoTimeout
argument_list|(
name|readTimeOut
argument_list|)
expr_stmt|;
name|socket
operator|.
name|setKeepAlive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|socket
operator|=
name|socket
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|processRequest
argument_list|(
name|socket
argument_list|)
expr_stmt|;
name|processResponse
argument_list|(
name|socket
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
name|e
operator|instanceof
name|SocketException
condition|)
block|{
if|if
condition|(
operator|!
operator|(
literal|"socket closed"
operator|.
name|equalsIgnoreCase
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
try|try
block|{
name|socket
operator|.
name|shutdownOutput
argument_list|()
expr_stmt|;
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|processRequest
parameter_list|(
name|Socket
name|socket
parameter_list|)
throws|throws
name|IOException
block|{
name|requestContent
operator|.
name|reset
argument_list|()
expr_stmt|;
name|BufferedInputStream
name|is
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|socket
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|requestMethodHeader
init|=
operator|new
name|String
argument_list|(
name|readLine
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
name|processRequestMethod
argument_list|(
name|requestMethodHeader
argument_list|)
expr_stmt|;
name|processRequestHeaders
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|processRequestContent
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processRequestMethod
parameter_list|(
name|String
name|requestMethodHeader
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|requestMethodHeader
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"illegal http request"
argument_list|)
throw|;
block|}
name|requestMethod
operator|=
name|parts
index|[
literal|0
index|]
expr_stmt|;
name|requestUrl
operator|=
name|parts
index|[
literal|1
index|]
expr_stmt|;
block|}
specifier|private
name|void
name|processRequestHeaders
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|requestHeaders
operator|.
name|clear
argument_list|()
expr_stmt|;
name|byte
index|[]
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|readLine
argument_list|(
name|is
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
name|String
name|lineStr
init|=
operator|new
name|String
argument_list|(
name|line
argument_list|)
decl_stmt|;
comment|// if there are no more headers
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|lineStr
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
break|break;
block|}
name|addRequestHeader
argument_list|(
name|lineStr
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|processRequestContent
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|NumberFormatException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
operator|(
literal|"PUT"
operator|.
name|equals
argument_list|(
name|requestMethod
argument_list|)
operator|||
literal|"POST"
operator|.
name|equals
argument_list|(
name|requestMethod
argument_list|)
operator|)
condition|)
block|{
return|return;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|transferEncodingValues
init|=
name|requestHeaders
operator|.
name|get
argument_list|(
literal|"Transfer-Encoding"
argument_list|)
decl_stmt|;
name|String
name|transferEncoding
init|=
operator|(
name|transferEncodingValues
operator|==
literal|null
operator|||
name|transferEncodingValues
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
literal|null
else|:
name|transferEncodingValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"chunked"
operator|.
name|equals
argument_list|(
name|transferEncoding
argument_list|)
condition|)
block|{
name|processChunkedContent
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|processRegularContent
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|getMockResponseContentEchoRequest
argument_list|()
condition|)
block|{
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|setMockResponseContent
argument_list|(
name|requestContent
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|processRegularContent
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|contentLengthValues
init|=
name|requestHeaders
operator|.
name|get
argument_list|(
literal|"Content-Length"
argument_list|)
decl_stmt|;
name|String
name|contentLength
init|=
operator|(
name|contentLengthValues
operator|==
literal|null
operator|||
name|contentLengthValues
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
literal|null
else|:
name|contentLengthValues
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentLength
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|int
name|contentLen
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|contentLength
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|contentLen
index|]
decl_stmt|;
name|is
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|requestContent
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processChunkedContent
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|requestContent
operator|.
name|write
argument_list|(
literal|""
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|chunk
init|=
literal|null
decl_stmt|;
name|byte
index|[]
name|line
init|=
literal|null
decl_stmt|;
name|boolean
name|lastChunk
init|=
literal|false
decl_stmt|;
comment|// we should exit this loop only after we get to the end of stream
while|while
condition|(
operator|!
name|lastChunk
operator|&&
operator|(
name|line
operator|=
name|readLine
argument_list|(
name|is
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
name|String
name|lineStr
init|=
operator|new
name|String
argument_list|(
name|line
argument_list|)
decl_stmt|;
comment|// a chunk is identified as:
comment|// 1) not an empty line
comment|// 2) not 0. 0 means that there are no more chunks
if|if
condition|(
literal|"0"
operator|.
name|equals
argument_list|(
name|lineStr
argument_list|)
condition|)
block|{
name|lastChunk
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|lastChunk
condition|)
block|{
comment|// get the length of the current chunk (it is in hexadecimal
comment|// form)
name|int
name|chunkLen
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|lineStr
argument_list|,
literal|16
argument_list|)
decl_stmt|;
comment|// get the chunk
name|chunk
operator|=
name|getChunk
argument_list|(
name|is
argument_list|,
name|chunkLen
argument_list|)
expr_stmt|;
comment|// consume the newline after the chunk that separates
comment|// between
comment|// the chunk content and the next chunk size
name|readLine
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|requestContent
operator|.
name|write
argument_list|(
name|chunk
argument_list|)
expr_stmt|;
block|}
block|}
comment|// do one last read to consume the empty line after the last chunk
if|if
condition|(
name|lastChunk
condition|)
block|{
name|readLine
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|byte
index|[]
name|readLine
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
name|int
name|n
decl_stmt|;
name|ByteArrayOutputStream
name|tmpOs
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|n
operator|=
name|is
operator|.
name|read
argument_list|()
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|n
operator|==
literal|'\r'
condition|)
block|{
name|n
operator|=
name|is
operator|.
name|read
argument_list|()
expr_stmt|;
if|if
condition|(
name|n
operator|==
literal|'\n'
condition|)
block|{
return|return
name|tmpOs
operator|.
name|toByteArray
argument_list|()
return|;
block|}
else|else
block|{
name|tmpOs
operator|.
name|write
argument_list|(
literal|'\r'
argument_list|)
expr_stmt|;
if|if
condition|(
name|n
operator|!=
operator|-
literal|1
condition|)
block|{
name|tmpOs
operator|.
name|write
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
else|else
block|{
return|return
name|tmpOs
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|n
operator|==
literal|'\n'
condition|)
block|{
return|return
name|tmpOs
operator|.
name|toByteArray
argument_list|()
return|;
block|}
else|else
block|{
name|tmpOs
operator|.
name|write
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|tmpOs
operator|.
name|toByteArray
argument_list|()
return|;
block|}
specifier|private
name|byte
index|[]
name|getChunk
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|chunk
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|int
name|read
decl_stmt|;
name|int
name|totalRead
init|=
literal|0
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|512
index|]
decl_stmt|;
comment|// read len bytes as the chunk
while|while
condition|(
name|totalRead
operator|<
name|len
condition|)
block|{
name|read
operator|=
name|is
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|Math
operator|.
name|min
argument_list|(
name|bytes
operator|.
name|length
argument_list|,
name|len
operator|-
name|totalRead
argument_list|)
argument_list|)
expr_stmt|;
name|chunk
operator|.
name|write
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
name|totalRead
operator|+=
name|read
expr_stmt|;
block|}
return|return
name|chunk
operator|.
name|toByteArray
argument_list|()
return|;
block|}
specifier|private
name|void
name|addRequestHeader
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|String
index|[]
name|parts
init|=
name|line
operator|.
name|split
argument_list|(
literal|": "
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|requestHeaders
operator|.
name|get
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|==
literal|null
condition|)
block|{
name|values
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|requestHeaders
operator|.
name|put
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
name|values
operator|.
name|add
argument_list|(
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processResponse
parameter_list|(
name|Socket
name|socket
parameter_list|)
throws|throws
name|IOException
block|{
comment|// if delaying the response failed (because it was interrupted)
comment|// then don't send the response
if|if
condition|(
operator|!
name|delayResponse
argument_list|()
condition|)
return|return;
name|OutputStream
name|sos
init|=
name|socket
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|BufferedOutputStream
name|os
init|=
operator|new
name|BufferedOutputStream
argument_list|(
name|sos
argument_list|)
decl_stmt|;
name|String
name|reason
init|=
literal|""
decl_stmt|;
name|Status
name|statusCode
init|=
name|Response
operator|.
name|Status
operator|.
name|fromStatusCode
argument_list|(
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|getMockResponseCode
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|statusCode
operator|!=
literal|null
condition|)
block|{
name|reason
operator|=
name|statusCode
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|os
operator|.
name|write
argument_list|(
operator|(
literal|"HTTP/1.1 "
operator|+
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|getMockResponseCode
argument_list|()
operator|+
literal|" "
operator|+
name|reason
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|NEW_LINE
argument_list|)
expr_stmt|;
name|processResponseHeaders
argument_list|(
name|os
argument_list|)
expr_stmt|;
name|processResponseContent
argument_list|(
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|responseCounter
operator|++
expr_stmt|;
block|}
comment|// return:
comment|// true - delay was successful
comment|// false - delay was unsuccessful
specifier|private
name|boolean
name|delayResponse
parameter_list|()
block|{
comment|// delay the response by delayResponseTime milliseconds
if|if
condition|(
name|delayResponseTime
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delayResponseTime
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|processResponseContent
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|getMockResponseContent
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|os
operator|.
name|write
argument_list|(
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|getMockResponseContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|processResponseHeaders
parameter_list|(
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
name|addServerResponseHeaders
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|header
range|:
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|getMockResponseHeaders
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|os
operator|.
name|write
argument_list|(
operator|(
name|header
operator|+
literal|": "
operator|+
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|getMockResponseHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|header
argument_list|)
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|NEW_LINE
argument_list|)
expr_stmt|;
block|}
name|os
operator|.
name|write
argument_list|(
name|NEW_LINE
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addServerResponseHeaders
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mockResponseHeaders
init|=
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|getMockResponseHeaders
argument_list|()
decl_stmt|;
name|mockResponseHeaders
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|getMockResponseContentType
argument_list|()
argument_list|)
expr_stmt|;
name|mockResponseHeaders
operator|.
name|put
argument_list|(
literal|"Content-Length"
argument_list|,
name|mockHttpServerResponses
operator|.
name|get
argument_list|(
name|responseCounter
argument_list|)
operator|.
name|getMockResponseContent
argument_list|()
operator|.
name|length
operator|+
literal|""
argument_list|)
expr_stmt|;
name|mockResponseHeaders
operator|.
name|put
argument_list|(
literal|"Server"
argument_list|,
literal|"Mock HTTP Server v1.0"
argument_list|)
expr_stmt|;
name|mockResponseHeaders
operator|.
name|put
argument_list|(
literal|"Connection"
argument_list|,
literal|"closed"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setReadTimeout
parameter_list|(
name|int
name|milliseconds
parameter_list|)
block|{
name|readTimeOut
operator|=
name|milliseconds
expr_stmt|;
block|}
specifier|public
name|void
name|setDelayResponse
parameter_list|(
name|int
name|milliseconds
parameter_list|)
block|{
name|delayResponseTime
operator|=
name|milliseconds
expr_stmt|;
block|}
specifier|public
name|String
name|getRequestContentAsString
parameter_list|()
block|{
return|return
name|requestContent
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|byte
index|[]
name|getRequestContent
parameter_list|()
block|{
return|return
name|requestContent
operator|.
name|toByteArray
argument_list|()
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getRequestHeaders
parameter_list|()
block|{
return|return
name|requestHeaders
return|;
block|}
specifier|public
name|String
name|getRequestMethod
parameter_list|()
block|{
return|return
name|requestMethod
return|;
block|}
specifier|public
name|String
name|getRequestUrl
parameter_list|()
block|{
return|return
name|requestUrl
return|;
block|}
specifier|public
name|void
name|setMockHttpServerResponses
parameter_list|(
name|MockHttpServerResponse
modifier|...
name|responses
parameter_list|)
block|{
name|mockHttpServerResponses
operator|.
name|clear
argument_list|()
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
name|responses
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|mockHttpServerResponses
operator|.
name|add
argument_list|(
name|responses
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|MockHttpServerResponse
argument_list|>
name|getMockHttpServerResponses
parameter_list|()
block|{
return|return
name|mockHttpServerResponses
return|;
block|}
specifier|public
name|void
name|setServerPort
parameter_list|(
name|int
name|serverPort
parameter_list|)
block|{
name|this
operator|.
name|serverPort
operator|=
name|serverPort
expr_stmt|;
block|}
specifier|public
name|int
name|getServerPort
parameter_list|()
block|{
return|return
name|serverPort
return|;
block|}
block|}
end_class

end_unit

