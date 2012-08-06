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
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_class
specifier|public
class|class
name|IsartorPdfProvider
block|{
comment|//  public static File path;
comment|//  static {
comment|//    String ip = System.getProperty("isartor.path", null);
comment|//    if (ip != null) {
comment|//      path = new File(ip);
comment|//      if (!path.exists() || !path.isDirectory()) {
comment|//        path = null;
comment|//      }
comment|//    }
comment|//  }
specifier|public
specifier|static
name|InputStream
name|getIsartorDocument
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|IsartorPdfProvider
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
return|;
comment|//
comment|//    if (path == null) {
comment|//      return null;
comment|//    }
comment|//
comment|//    String[] ext = { "pdf" };
comment|//    Iterator<?> iter = FileUtils.iterateFiles(path, ext, true);
comment|//    while (iter.hasNext()) {
comment|//      Object o = iter.next();
comment|//      if (o instanceof File) {
comment|//        File isartorFile = (File) o;
comment|//        if (isartorFile.isFile()&& name.equals(isartorFile.getName())) {
comment|//          return isartorFile;
comment|//        }
comment|//      }
comment|//    }
comment|//    return null;
comment|//  }
block|}
block|}
end_class

end_unit

