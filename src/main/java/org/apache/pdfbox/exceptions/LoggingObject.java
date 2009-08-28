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
name|exceptions
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
name|util
operator|.
name|logging
operator|.
name|FileHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|SimpleFormatter
import|;
end_import

begin_comment
comment|/**  * Implementation of base object to help with error-handling.  *  * @author<a href="mailto:DanielWilson@users.sourceforge.net">Daniel Wilson</a>  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|LoggingObject
block|{
specifier|private
specifier|static
name|Logger
name|logger
decl_stmt|;
comment|//dwilson 3/15/07
static|static
block|{
try|try
block|{
name|FileHandler
name|fh
init|=
operator|new
name|FileHandler
argument_list|(
literal|"PDFBox.log"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|fh
operator|.
name|setFormatter
argument_list|(
operator|new
name|SimpleFormatter
argument_list|()
argument_list|)
expr_stmt|;
name|logger
operator|=
name|Logger
operator|.
name|getLogger
argument_list|(
literal|"TestLog"
argument_list|)
expr_stmt|;
name|logger
operator|.
name|addHandler
argument_list|(
name|fh
argument_list|)
expr_stmt|;
comment|/*Set the log level here.             The lower your logging level, the more stuff will be logged.             Options are:                 * OFF -- log nothing                 * SEVERE (highest value)                 * WARNING                 * INFO                 * CONFIG                 * FINE                 * FINER                 * FINEST (lowest value)             http://java.sun.com/j2se/1.4.2/docs/api/java/util/logging/Level.html              I recommend INFO for debug builds and either SEVERE or OFF for production builds.             */
name|logger
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|)
expr_stmt|;
comment|//            logger_.setLevel(Level.INFO);
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Error while opening the logfile:"
argument_list|)
expr_stmt|;
name|exception
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Returns the main logger instance.      * @return the logger instance      */
specifier|protected
name|Logger
name|logger
parameter_list|()
comment|//dwilson 3/15/07
block|{
return|return
name|logger
return|;
block|}
comment|/**      * Constructs a String with the full stack trace of the given exception.      * @param e the exception      * @return the full stack trace as a string      */
specifier|protected
specifier|static
name|String
name|fullStackTrace
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|int
name|i
decl_stmt|;
name|StackTraceElement
index|[]
name|element
decl_stmt|;
name|StringBuffer
name|sRet
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|element
operator|=
name|e
operator|.
name|getStackTrace
argument_list|()
expr_stmt|;
for|for
control|(
name|i
operator|=
literal|0
init|;
name|i
operator|<
name|element
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|sRet
operator|.
name|append
argument_list|(
operator|(
name|element
index|[
name|i
index|]
operator|.
name|toString
argument_list|()
operator|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sRet
operator|.
name|append
argument_list|(
literal|"Caused By \n\t"
argument_list|)
operator|.
name|append
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|sRet
operator|.
name|append
argument_list|(
name|fullStackTrace
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|sRet
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

