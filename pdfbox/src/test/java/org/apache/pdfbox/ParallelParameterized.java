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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|RunnerScheduler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/**  * Runs Parameterized JUnit tests in parallel.  *  * @see {http://goo.gl/lkigES}  */
end_comment

begin_class
specifier|public
class|class
name|ParallelParameterized
extends|extends
name|Parameterized
block|{
specifier|private
specifier|static
class|class
name|FixedThreadPoolScheduler
implements|implements
name|RunnerScheduler
block|{
specifier|private
specifier|final
name|ExecutorService
name|executorService
decl_stmt|;
specifier|private
specifier|final
name|long
name|timeoutSeconds
decl_stmt|;
name|FixedThreadPoolScheduler
parameter_list|(
name|long
name|timeoutSeconds
parameter_list|)
block|{
name|this
operator|.
name|timeoutSeconds
operator|=
name|timeoutSeconds
expr_stmt|;
name|int
name|cores
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
decl_stmt|;
comment|// for debugging
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"JDK: "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.runtime.name"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Version: "
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
argument_list|)
expr_stmt|;
comment|// workaround Open JDK 6 bug which causes CMMException: Invalid profile data
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.runtime.name"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"OpenJDK Runtime Environment"
argument_list|)
operator|&&
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.specification.version"
argument_list|)
operator|.
name|equals
argument_list|(
literal|"1.6"
argument_list|)
condition|)
block|{
name|cores
operator|=
literal|1
expr_stmt|;
block|}
name|executorService
operator|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|cores
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|finished
parameter_list|()
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
name|executorService
operator|.
name|awaitTermination
argument_list|(
name|timeoutSeconds
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|exc
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|exc
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|schedule
parameter_list|(
name|Runnable
name|childStatement
parameter_list|)
block|{
name|executorService
operator|.
name|submit
argument_list|(
name|childStatement
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ParallelParameterized
parameter_list|(
name|Class
name|c
parameter_list|)
throws|throws
name|Throwable
block|{
name|super
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|long
name|timeoutSeconds
init|=
name|Long
operator|.
name|MAX_VALUE
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|getSimpleName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"TestRendering"
argument_list|)
condition|)
block|{
name|timeoutSeconds
operator|=
literal|30
expr_stmt|;
block|}
name|setScheduler
argument_list|(
operator|new
name|FixedThreadPoolScheduler
argument_list|(
name|timeoutSeconds
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

