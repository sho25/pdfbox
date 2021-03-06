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
package|;
end_package

begin_comment
comment|/**  * A simple command line utility to get the version of PDFBox.  *  * @author Ben Litchfield  */
end_comment

begin_class
specifier|final
class|class
name|Version
block|{
specifier|private
name|Version
parameter_list|()
block|{
comment|//should not be constructed.
block|}
comment|/**      * Get the version of PDFBox or unknown if it is not known.      *      * @return The version of pdfbox that is being used.      */
specifier|public
specifier|static
name|String
name|getVersion
parameter_list|()
block|{
name|String
name|version
init|=
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|util
operator|.
name|Version
operator|.
name|getVersion
argument_list|()
decl_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
condition|)
block|{
return|return
name|version
return|;
block|}
else|else
block|{
return|return
literal|"unknown"
return|;
block|}
block|}
comment|/**      * This will print out the version of PDF to System.out.      *      * @param args Command line arguments.      */
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
comment|// suppress the Dock icon on OS X
name|System
operator|.
name|setProperty
argument_list|(
literal|"apple.awt.UIElement"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|!=
literal|0
condition|)
block|{
name|usage
argument_list|()
expr_stmt|;
return|return;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Version:"
operator|+
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will print out a message telling how to use this example.      */
specifier|private
specifier|static
name|void
name|usage
parameter_list|()
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Usage: "
operator|+
name|Version
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

