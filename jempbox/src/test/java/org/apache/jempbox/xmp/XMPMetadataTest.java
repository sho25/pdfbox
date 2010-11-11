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
name|jempbox
operator|.
name|xmp
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
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_class
specifier|public
class|class
name|XMPMetadataTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testLoadDublincoreExample
parameter_list|()
throws|throws
name|IOException
block|{
comment|// http://dublincore.org/documents/dcmes-xml/
comment|// had to move the dc namespace declaration to get it parsed
name|String
name|xmpmeta
init|=
literal|"<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">\n"
operator|+
literal|"<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"
operator|+
comment|//"    xmlns:dc=\"http://purl.org/dc/elements/1.1/\">" +
literal|">"
operator|+
literal|"<rdf:Description\n"
operator|+
comment|//">" +
literal|"      xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
operator|+
literal|"<dc:title>The Mona Lisa</dc:title>\n"
operator|+
literal|"<dc:description>A painting by ...</dc:description>\n"
operator|+
literal|"</rdf:Description>\n"
operator|+
literal|"</rdf:RDF>\n"
operator|+
literal|"</x:xmpmeta>"
decl_stmt|;
name|XMPMetadata
name|xmp
init|=
name|XMPMetadata
operator|.
name|load
argument_list|(
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xmpmeta
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|XMPSchemaDublinCore
name|dc
init|=
name|xmp
operator|.
name|getDublinCoreSchema
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The Mona Lisa"
argument_list|,
name|dc
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A painting by ..."
argument_list|,
name|dc
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExiv2Xmp
parameter_list|()
throws|throws
name|IOException
block|{
comment|// XMP block as created by exiv2
name|String
name|xmpmeta
init|=
literal|"<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"XMP Core 4.4.0-Exiv2\">\n"
operator|+
literal|"<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n"
operator|+
literal|"<rdf:Description rdf:about=\"\"\n"
operator|+
literal|"    xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n"
operator|+
literal|"<dc:description>\n"
operator|+
literal|"<rdf:Alt>\n"
operator|+
literal|"<rdf:li xml:lang=\"x-default\">Dublin Core description</rdf:li>\n"
operator|+
literal|"</rdf:Alt>\n"
operator|+
literal|"</dc:description>\n"
operator|+
literal|"</rdf:Description>\n"
operator|+
literal|"</rdf:RDF>\n"
operator|+
literal|"</x:xmpmeta>"
decl_stmt|;
name|XMPMetadata
name|xmp
init|=
name|XMPMetadata
operator|.
name|load
argument_list|(
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xmpmeta
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Dublin Core description"
argument_list|,
name|xmp
operator|.
name|getDublinCoreSchema
argument_list|()
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDescriptionFromNodeText
parameter_list|()
throws|throws
name|IOException
block|{
comment|// From a jpeg, valid according to http://www.w3.org/RDF/Validator/
name|String
name|xmpmeta
init|=
literal|"<x:xmpmeta xmlns:x=\"adobe:ns:meta/\" x:xmptk=\"XMP Core 4.4.0\">\n"
operator|+
literal|"<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\">\n"
operator|+
literal|"<rdf:Description rdf:about=\"\"\n"
operator|+
literal|"            xmlns:exif=\"http://ns.adobe.com/exif/1.0/\">\n"
operator|+
literal|"<exif:UserComment>exif</exif:UserComment>\n"
operator|+
literal|"</rdf:Description>\n"
operator|+
literal|"<rdf:Description rdf:about=\"\"\n"
operator|+
literal|"            xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n"
operator|+
literal|"<dc:description>Dublin Core description</dc:description>\n"
operator|+
literal|"</rdf:Description>\n"
operator|+
literal|"</rdf:RDF>\n"
operator|+
literal|"</x:xmpmeta>"
decl_stmt|;
name|XMPMetadata
name|xmp
init|=
name|XMPMetadata
operator|.
name|load
argument_list|(
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|xmpmeta
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Dublin Core description"
argument_list|,
name|xmp
operator|.
name|getDublinCoreSchema
argument_list|()
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

