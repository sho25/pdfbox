begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  *   */
end_comment

begin_comment
comment|/*   * This file includes code under the following terms:  * 			  *  Version: 2.0  *    *  Disclaimer: IMPORTANT:  This Apple software is supplied to you by   *  Apple Inc. ("Apple") in consideration of your agreement to the  *  following terms, and your use, installation, modification or  *  redistribution of this Apple software constitutes acceptance of these  *  terms.  If you do not agree with these terms, please do not use,  *  install, modify or redistribute this Apple software.  *    *  In consideration of your agreement to abide by the following terms, and  *  subject to these terms, Apple grants you a personal, non-exclusive  *  license, under Apple's copyrights in this original Apple software (the  *  "Apple Software"), to use, reproduce, modify and redistribute the Apple  *  Software, with or without modifications, in source and/or binary forms;  *  provided that if you redistribute the Apple Software in its entirety and  *  without modifications, you must retain this notice and the following  *  text and disclaimers in all such redistributions of the Apple Software.   *  Neither the name, trademarks, service marks or logos of Apple Inc.   *  may be used to endorse or promote products derived from the Apple  *  Software without specific prior written permission from Apple.  Except  *  as expressly stated in this notice, no other rights or licenses, express  *  or implied, are granted by Apple herein, including but not limited to  *  any patent rights that may be infringed by your derivative works or by  *  other works in which the Apple Software may be incorporated.  *    *  The Apple Software is provided by Apple on an "AS IS" basis.  APPLE  *  MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION  *  THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS  *  FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND  *  OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.  *    *  IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL  *  OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF  *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS  *  INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,  *  MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED  *  AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE),  *  STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF THE  *  POSSIBILITY OF SUCH DAMAGE.  *    *  Copyright (C) 2003-2007 Apple, Inc., All Rights Reserved  */
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
name|gui
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
name|InvocationHandler
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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Proxy
import|;
end_import

begin_comment
comment|/**  * Hooks existing preferences/about/quit functionality from an  * existing Java app into handlers for the Mac OS X application menu.  * Uses a Proxy object to dynamically implement the   * com.apple.eawt.ApplicationListener interface and register it with the  * com.apple.eawt.Application object.  This allows the complete project  * to be both built and run on any platform without any stubs or   * placeholders. Useful for developers looking to implement Mac OS X   * features while supporting multiple platforms with minimal impact.  */
end_comment

begin_class
specifier|public
class|class
name|OSXAdapter
implements|implements
name|InvocationHandler
block|{
specifier|protected
name|Object
name|targetObject
decl_stmt|;
specifier|protected
name|Method
name|targetMethod
decl_stmt|;
specifier|protected
name|String
name|proxySignature
decl_stmt|;
specifier|static
name|Object
name|macOSXApplication
decl_stmt|;
comment|// Pass this method an Object and Method equipped to perform application shutdown logic
comment|// The method passed should return a boolean stating whether or not the quit should occur
specifier|public
specifier|static
name|void
name|setQuitHandler
parameter_list|(
name|Object
name|target
parameter_list|,
name|Method
name|quitHandler
parameter_list|)
block|{
name|setHandler
argument_list|(
operator|new
name|OSXAdapter
argument_list|(
literal|"handleQuit"
argument_list|,
name|target
argument_list|,
name|quitHandler
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Pass this method an Object and Method equipped to display application info
comment|// They will be called when the About menu item is selected from the application menu
specifier|public
specifier|static
name|void
name|setAboutHandler
parameter_list|(
name|Object
name|target
parameter_list|,
name|Method
name|aboutHandler
parameter_list|)
block|{
name|boolean
name|enableAboutMenu
init|=
operator|(
name|target
operator|!=
literal|null
operator|&&
name|aboutHandler
operator|!=
literal|null
operator|)
decl_stmt|;
if|if
condition|(
name|enableAboutMenu
condition|)
block|{
name|setHandler
argument_list|(
operator|new
name|OSXAdapter
argument_list|(
literal|"handleAbout"
argument_list|,
name|target
argument_list|,
name|aboutHandler
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// If we're setting a handler, enable the About menu item by calling
comment|// com.apple.eawt.Application reflectively
try|try
block|{
name|Method
name|enableAboutMethod
init|=
name|macOSXApplication
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethod
argument_list|(
literal|"setEnabledAboutMenu"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|boolean
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|enableAboutMethod
operator|.
name|invoke
argument_list|(
name|macOSXApplication
argument_list|,
operator|new
name|Object
index|[]
block|{
name|Boolean
operator|.
name|valueOf
argument_list|(
name|enableAboutMenu
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"OSXAdapter could not access the About Menu"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
comment|// Pass this method an Object and a Method equipped to display application options
comment|// They will be called when the Preferences menu item is selected from the application menu
specifier|public
specifier|static
name|void
name|setPreferencesHandler
parameter_list|(
name|Object
name|target
parameter_list|,
name|Method
name|prefsHandler
parameter_list|)
block|{
name|boolean
name|enablePrefsMenu
init|=
operator|(
name|target
operator|!=
literal|null
operator|&&
name|prefsHandler
operator|!=
literal|null
operator|)
decl_stmt|;
if|if
condition|(
name|enablePrefsMenu
condition|)
block|{
name|setHandler
argument_list|(
operator|new
name|OSXAdapter
argument_list|(
literal|"handlePreferences"
argument_list|,
name|target
argument_list|,
name|prefsHandler
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// If we're setting a handler, enable the Preferences menu item by calling
comment|// com.apple.eawt.Application reflectively
try|try
block|{
name|Method
name|enablePrefsMethod
init|=
name|macOSXApplication
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethod
argument_list|(
literal|"setEnabledPreferencesMenu"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|boolean
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|enablePrefsMethod
operator|.
name|invoke
argument_list|(
name|macOSXApplication
argument_list|,
operator|new
name|Object
index|[]
block|{
name|Boolean
operator|.
name|valueOf
argument_list|(
name|enablePrefsMenu
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"OSXAdapter could not access the About Menu"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
comment|// Pass this method an Object and a Method equipped to handle document events from the Finder
comment|// Documents are registered with the Finder via the CFBundleDocumentTypes dictionary in the
comment|// application bundle's Info.plist
specifier|public
specifier|static
name|void
name|setFileHandler
parameter_list|(
name|Object
name|target
parameter_list|,
name|Method
name|fileHandler
parameter_list|)
block|{
name|setHandler
argument_list|(
operator|new
name|OSXAdapter
argument_list|(
literal|"handleOpenFile"
argument_list|,
name|target
argument_list|,
name|fileHandler
argument_list|)
block|{
comment|// Override OSXAdapter.callTarget to send information on the
comment|// file to be opened
specifier|public
name|boolean
name|callTarget
parameter_list|(
name|Object
name|appleEvent
parameter_list|)
block|{
if|if
condition|(
name|appleEvent
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Method
name|getFilenameMethod
init|=
name|appleEvent
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethod
argument_list|(
literal|"getFilename"
argument_list|,
operator|(
name|Class
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
name|String
name|filename
init|=
operator|(
name|String
operator|)
name|getFilenameMethod
operator|.
name|invoke
argument_list|(
name|appleEvent
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
name|this
operator|.
name|targetMethod
operator|.
name|invoke
argument_list|(
name|this
operator|.
name|targetObject
argument_list|,
operator|new
name|Object
index|[]
block|{
name|filename
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// setHandler creates a Proxy object from the passed OSXAdapter and adds it as an ApplicationListener
specifier|public
specifier|static
name|void
name|setHandler
parameter_list|(
name|OSXAdapter
name|adapter
parameter_list|)
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|applicationClass
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"com.apple.eawt.Application"
argument_list|)
decl_stmt|;
if|if
condition|(
name|macOSXApplication
operator|==
literal|null
condition|)
block|{
name|macOSXApplication
operator|=
name|applicationClass
operator|.
name|getConstructor
argument_list|(
operator|(
name|Class
index|[]
operator|)
literal|null
argument_list|)
operator|.
name|newInstance
argument_list|(
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|applicationListenerClass
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"com.apple.eawt.ApplicationListener"
argument_list|)
decl_stmt|;
name|Method
name|addListenerMethod
init|=
name|applicationClass
operator|.
name|getDeclaredMethod
argument_list|(
literal|"addApplicationListener"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|applicationListenerClass
block|}
argument_list|)
decl_stmt|;
comment|// Create a proxy object around this handler that can be reflectively added as an Apple ApplicationListener
name|Object
name|osxAdapterProxy
init|=
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|OSXAdapter
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|applicationListenerClass
block|}
argument_list|,
name|adapter
argument_list|)
decl_stmt|;
name|addListenerMethod
operator|.
name|invoke
argument_list|(
name|macOSXApplication
argument_list|,
operator|new
name|Object
index|[]
block|{
name|osxAdapterProxy
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|cnfe
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"This version of Mac OS X does not support the Apple EAWT.  ApplicationEvent handling has been disabled ("
operator|+
name|cnfe
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// Likely a NoSuchMethodException or an IllegalAccessException loading/invoking eawt.Application methods
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Mac OS X Adapter could not talk to EAWT:"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
comment|// Each OSXAdapter has the name of the EAWT method it intends to listen for (handleAbout, for example),
comment|// the Object that will ultimately perform the task, and the Method to be called on that Object
specifier|protected
name|OSXAdapter
parameter_list|(
name|String
name|proxySignature
parameter_list|,
name|Object
name|target
parameter_list|,
name|Method
name|handler
parameter_list|)
block|{
name|this
operator|.
name|proxySignature
operator|=
name|proxySignature
expr_stmt|;
name|this
operator|.
name|targetObject
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|targetMethod
operator|=
name|handler
expr_stmt|;
block|}
comment|// Override this method to perform any operations on the event
comment|// that comes with the various callbacks
comment|// See setFileHandler above for an example
specifier|public
name|boolean
name|callTarget
parameter_list|(
name|Object
name|appleEvent
parameter_list|)
throws|throws
name|InvocationTargetException
throws|,
name|IllegalAccessException
block|{
name|Object
name|result
init|=
name|targetMethod
operator|.
name|invoke
argument_list|(
name|targetObject
argument_list|,
operator|(
name|Object
index|[]
operator|)
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|Boolean
operator|.
name|valueOf
argument_list|(
name|result
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|booleanValue
argument_list|()
return|;
block|}
comment|// InvocationHandler implementation
comment|// This is the entry point for our proxy object; it is called every time an ApplicationListener method is invoked
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
name|isCorrectMethod
argument_list|(
name|method
argument_list|,
name|args
argument_list|)
condition|)
block|{
name|boolean
name|handled
init|=
name|callTarget
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|setApplicationEventHandled
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|,
name|handled
argument_list|)
expr_stmt|;
block|}
comment|// All of the ApplicationListener methods are void; return null regardless of what happens
return|return
literal|null
return|;
block|}
comment|// Compare the method that was called to the intended method when the OSXAdapter instance was created
comment|// (e.g. handleAbout, handleQuit, handleOpenFile, etc.)
specifier|protected
name|boolean
name|isCorrectMethod
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
block|{
return|return
operator|(
name|targetMethod
operator|!=
literal|null
operator|&&
name|proxySignature
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
name|args
operator|.
name|length
operator|==
literal|1
operator|)
return|;
block|}
comment|// It is important to mark the ApplicationEvent as handled and cancel the default behavior
comment|// This method checks for a boolean result from the proxy method and sets the event accordingly
specifier|protected
name|void
name|setApplicationEventHandled
parameter_list|(
name|Object
name|event
parameter_list|,
name|boolean
name|handled
parameter_list|)
block|{
if|if
condition|(
name|event
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Method
name|setHandledMethod
init|=
name|event
operator|.
name|getClass
argument_list|()
operator|.
name|getDeclaredMethod
argument_list|(
literal|"setHandled"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|boolean
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
comment|// If the target method returns a boolean, use that as a hint
name|setHandledMethod
operator|.
name|invoke
argument_list|(
name|event
argument_list|,
operator|new
name|Object
index|[]
block|{
name|Boolean
operator|.
name|valueOf
argument_list|(
name|handled
argument_list|)
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"OSXAdapter was unable to handle an ApplicationEvent: "
operator|+
name|event
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

