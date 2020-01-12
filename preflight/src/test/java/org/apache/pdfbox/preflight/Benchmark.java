begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************************  *   * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *   * http://www.apache.org/licenses/LICENSE-2.0  *   * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  *   ****************************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
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
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|commons
operator|.
name|io
operator|.
name|FileUtils
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|preflight
operator|.
name|ValidationResult
operator|.
name|ValidationError
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
name|preflight
operator|.
name|parser
operator|.
name|PreflightParser
import|;
end_import

begin_class
specifier|public
class|class
name|Benchmark
block|{
comment|/**      * @param args      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|<
literal|3
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Usage : Benchmark loop resultFile<file1 ... filen|dir>"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|255
argument_list|)
expr_stmt|;
block|}
name|Integer
name|loop
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|FileWriter
name|resFile
init|=
operator|new
name|FileWriter
argument_list|(
operator|new
name|File
argument_list|(
name|args
index|[
literal|1
index|]
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|File
argument_list|>
name|lfd
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|2
init|;
name|i
operator|<
name|args
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|File
name|fi
init|=
operator|new
name|File
argument_list|(
name|args
index|[
name|i
index|]
argument_list|)
decl_stmt|;
if|if
condition|(
name|fi
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|Collection
argument_list|<
name|File
argument_list|>
name|cf
init|=
name|FileUtils
operator|.
name|listFiles
argument_list|(
name|fi
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Get All files contained by the dir
name|lfd
operator|.
name|addAll
argument_list|(
name|cf
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|lfd
operator|.
name|add
argument_list|(
name|fi
argument_list|)
expr_stmt|;
block|}
block|}
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"dd/MM/yyyy hh:mm:ss.Z"
argument_list|)
decl_stmt|;
name|long
name|startGTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|lfd
operator|.
name|size
argument_list|()
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
name|loop
condition|;
name|i
operator|++
control|)
block|{
name|File
name|file
init|=
name|lfd
operator|.
name|get
argument_list|(
name|i
operator|%
name|size
argument_list|)
decl_stmt|;
name|long
name|startLTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|ValidationResult
name|result
init|=
name|PreflightParser
operator|.
name|validate
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isValid
argument_list|()
condition|)
block|{
name|resFile
operator|.
name|write
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" isn't PDF/A\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|ValidationError
name|error
range|:
name|result
operator|.
name|getErrorsList
argument_list|()
control|)
block|{
name|resFile
operator|.
name|write
argument_list|(
name|error
operator|.
name|getErrorCode
argument_list|()
operator|+
literal|" : "
operator|+
name|error
operator|.
name|getDetails
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
name|long
name|endLTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|resFile
operator|.
name|write
argument_list|(
name|file
operator|.
name|getName
argument_list|()
operator|+
literal|" (ms) : "
operator|+
operator|(
name|endLTime
operator|-
name|startLTime
operator|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|resFile
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
name|long
name|endGTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|resFile
operator|.
name|write
argument_list|(
literal|"Start : "
operator|+
name|sdf
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|startGTime
argument_list|)
argument_list|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|resFile
operator|.
name|write
argument_list|(
literal|"End : "
operator|+
name|sdf
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|endGTime
argument_list|)
argument_list|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|resFile
operator|.
name|write
argument_list|(
literal|"Duration (ms) : "
operator|+
operator|(
name|endGTime
operator|-
name|startGTime
operator|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|resFile
operator|.
name|write
argument_list|(
literal|"Average (ms) : "
operator|+
call|(
name|int
call|)
argument_list|(
operator|(
name|endGTime
operator|-
name|startGTime
operator|)
operator|/
name|loop
argument_list|)
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Start : "
operator|+
name|sdf
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|startGTime
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"End : "
operator|+
name|sdf
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|(
name|endGTime
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Duration (ms) : "
operator|+
operator|(
name|endGTime
operator|-
name|startGTime
operator|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Average (ms) : "
operator|+
call|(
name|int
call|)
argument_list|(
operator|(
name|endGTime
operator|-
name|startGTime
operator|)
operator|/
name|loop
argument_list|)
argument_list|)
expr_stmt|;
name|resFile
operator|.
name|flush
argument_list|()
expr_stmt|;
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|resFile
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

