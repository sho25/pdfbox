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
name|encryption
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
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

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
import|;
end_import

begin_comment
comment|/**  * Manages security handlers for the application.  * It follows the singleton pattern.  * To be usable, security managers must be registered in it.  * Security managers are retrieved by the application when necessary.  *  * @author Benoit Guillon  * @author John Hewson  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SecurityHandlerFactory
block|{
comment|/** Singleton instance */
specifier|public
specifier|static
name|SecurityHandlerFactory
name|INSTANCE
init|=
operator|new
name|SecurityHandlerFactory
argument_list|()
decl_stmt|;
static|static
block|{
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|SecurityHandler
argument_list|>
argument_list|>
name|nameToHandler
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|SecurityHandler
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|ProtectionPolicy
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|SecurityHandler
argument_list|>
argument_list|>
name|policyToHandler
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|ProtectionPolicy
argument_list|>
argument_list|,
name|Class
argument_list|<
name|?
extends|extends
name|SecurityHandler
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|SecurityHandlerFactory
parameter_list|()
block|{
name|registerHandler
argument_list|(
name|StandardSecurityHandler
operator|.
name|FILTER
argument_list|,
name|StandardSecurityHandler
operator|.
name|class
argument_list|,
name|StandardProtectionPolicy
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerHandler
argument_list|(
name|PublicKeySecurityHandler
operator|.
name|FILTER
argument_list|,
name|PublicKeySecurityHandler
operator|.
name|class
argument_list|,
name|PublicKeyProtectionPolicy
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * Registers a security handler.      *      * If the security handler was already registered an exception is thrown.      * If another handler was previously registered for the same filter name or      * for the same policy name, an exception is thrown      *      * @param name the name of the filter      * @param securityHandler security handler class to register      * @param protectionPolicy protection policy class to register      */
specifier|public
name|void
name|registerHandler
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|SecurityHandler
argument_list|>
name|securityHandler
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|ProtectionPolicy
argument_list|>
name|protectionPolicy
parameter_list|)
block|{
if|if
condition|(
name|nameToHandler
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The security handler name is already registered"
argument_list|)
throw|;
block|}
name|nameToHandler
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|securityHandler
argument_list|)
expr_stmt|;
name|policyToHandler
operator|.
name|put
argument_list|(
name|protectionPolicy
argument_list|,
name|securityHandler
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a new security handler for the given protection policy, or null none is available.      * @param policy the protection policy for which to create a security handler      * @return a new SecurityHandler instance, or null none is available      */
specifier|public
name|SecurityHandler
name|newSecurityHandlerForPolicy
parameter_list|(
name|ProtectionPolicy
name|policy
parameter_list|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|SecurityHandler
argument_list|>
name|handlerClass
init|=
name|policyToHandler
operator|.
name|get
argument_list|(
name|policy
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|handlerClass
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Class
index|[]
name|argsClasses
init|=
block|{
name|policy
operator|.
name|getClass
argument_list|()
block|}
decl_stmt|;
name|Object
index|[]
name|args
init|=
block|{
name|policy
block|}
decl_stmt|;
try|try
block|{
name|Constructor
argument_list|<
name|?
extends|extends
name|SecurityHandler
argument_list|>
name|ctor
init|=
name|handlerClass
operator|.
name|getDeclaredConstructor
argument_list|(
name|argsClasses
argument_list|)
decl_stmt|;
return|return
name|ctor
operator|.
name|newInstance
argument_list|(
name|args
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// should not happen in normal operation
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
comment|// should not happen in normal operation
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
comment|// should not happen in normal operation
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
comment|// should not happen in normal operation
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Returns a new security handler for the given Filter name, or null none is available.      * @param name the Filter name from the PDF encryption dictionary      * @return a new SecurityHandler instance, or null none is available      */
specifier|public
name|SecurityHandler
name|newSecurityHandler
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|SecurityHandler
argument_list|>
name|handlerClass
init|=
name|nameToHandler
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|handlerClass
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Class
index|[]
name|argsClasses
init|=
block|{ }
decl_stmt|;
name|Object
index|[]
name|args
init|=
block|{ }
decl_stmt|;
try|try
block|{
name|Constructor
argument_list|<
name|?
extends|extends
name|SecurityHandler
argument_list|>
name|ctor
init|=
name|handlerClass
operator|.
name|getDeclaredConstructor
argument_list|(
name|argsClasses
argument_list|)
decl_stmt|;
return|return
name|ctor
operator|.
name|newInstance
argument_list|(
name|args
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// should not happen in normal operation
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
comment|// should not happen in normal operation
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
comment|// should not happen in normal operation
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
comment|// should not happen in normal operation
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

