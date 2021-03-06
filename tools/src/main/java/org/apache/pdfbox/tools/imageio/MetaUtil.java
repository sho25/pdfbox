begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|imageio
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|metadata
operator|.
name|IIOMetadata
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|metadata
operator|.
name|IIOMetadataNode
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  *  * @author Tilman Hausherr  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|MetaUtil
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|MetaUtil
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|SUN_TIFF_FORMAT
init|=
literal|"com_sun_media_imageio_plugins_tiff_image_1.0"
decl_stmt|;
specifier|static
specifier|final
name|String
name|JPEG_NATIVE_FORMAT
init|=
literal|"javax_imageio_jpeg_image_1.0"
decl_stmt|;
specifier|static
specifier|final
name|String
name|STANDARD_METADATA_FORMAT
init|=
literal|"javax_imageio_1.0"
decl_stmt|;
specifier|private
name|MetaUtil
parameter_list|()
block|{     }
comment|// logs metadata as an XML tree if debug is enabled
specifier|static
name|void
name|debugLogMetadata
parameter_list|(
name|IIOMetadata
name|metadata
parameter_list|,
name|String
name|format
parameter_list|)
block|{
if|if
condition|(
operator|!
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// see http://docs.oracle.com/javase/7/docs/api/javax/imageio/
comment|//     metadata/doc-files/standard_metadata.html
name|IIOMetadataNode
name|root
init|=
operator|(
name|IIOMetadataNode
operator|)
name|metadata
operator|.
name|getAsTree
argument_list|(
name|format
argument_list|)
decl_stmt|;
try|try
block|{
name|StringWriter
name|xmlStringWriter
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|StreamResult
name|streamResult
init|=
operator|new
name|StreamResult
argument_list|(
name|xmlStringWriter
argument_list|)
decl_stmt|;
name|TransformerFactory
name|transformerFactory
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|transformerFactory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Transformer
name|transformer
init|=
name|transformerFactory
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
comment|// see http://stackoverflow.com/a/1264872/535646
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
literal|"{http://xml.apache.org/xslt}indent-amount"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|DOMSource
name|domSource
init|=
operator|new
name|DOMSource
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|domSource
argument_list|,
name|streamResult
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"\n"
operator|+
name|xmlStringWriter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
decl||
name|TransformerException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|ex
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

