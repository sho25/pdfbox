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
name|xmpbox
operator|.
name|schema
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|XMPMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|OECFType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|TextType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|type
operator|.
name|TypeMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|xml
operator|.
name|DomXmpParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xmpbox
operator|.
name|xml
operator|.
name|XmpSerializer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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

begin_class
specifier|public
class|class
name|TestExifXmp
block|{
annotation|@
name|Test
specifier|public
name|void
name|testNonStrict
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/validxmp/exif.xmp"
argument_list|)
decl_stmt|;
name|DomXmpParser
name|builder
init|=
operator|new
name|DomXmpParser
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setStrictParsing
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|XMPMetadata
name|rxmp
init|=
name|builder
operator|.
name|parse
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|ExifSchema
name|schema
init|=
operator|(
name|ExifSchema
operator|)
name|rxmp
operator|.
name|getSchema
argument_list|(
name|ExifSchema
operator|.
name|class
argument_list|)
decl_stmt|;
name|TextType
name|ss
init|=
operator|(
name|TextType
operator|)
name|schema
operator|.
name|getProperty
argument_list|(
name|ExifSchema
operator|.
name|SPECTRAL_SENSITIVITY
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|ss
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"spectral sens value"
argument_list|,
name|ss
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenerate
parameter_list|()
throws|throws
name|Exception
block|{
name|XMPMetadata
name|metadata
init|=
name|XMPMetadata
operator|.
name|createXMPMetadata
argument_list|()
decl_stmt|;
name|TypeMapping
name|tmapping
init|=
name|metadata
operator|.
name|getTypeMapping
argument_list|()
decl_stmt|;
name|ExifSchema
name|exif
init|=
operator|new
name|ExifSchema
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|addSchema
argument_list|(
name|exif
argument_list|)
expr_stmt|;
name|OECFType
name|oecf
init|=
operator|new
name|OECFType
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|oecf
operator|.
name|addProperty
argument_list|(
name|tmapping
operator|.
name|createInteger
argument_list|(
name|oecf
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|oecf
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|OECFType
operator|.
name|COLUMNS
argument_list|,
literal|14
argument_list|)
argument_list|)
expr_stmt|;
name|oecf
operator|.
name|setPropertyName
argument_list|(
name|ExifSchema
operator|.
name|OECF
argument_list|)
expr_stmt|;
name|exif
operator|.
name|addProperty
argument_list|(
name|oecf
argument_list|)
expr_stmt|;
name|XmpSerializer
name|serializer
init|=
operator|new
name|XmpSerializer
argument_list|()
decl_stmt|;
name|serializer
operator|.
name|serialize
argument_list|(
name|metadata
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

