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
operator|.
name|pdfa
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jempbox
operator|.
name|xmp
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
name|jempbox
operator|.
name|xmp
operator|.
name|XMPSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/**  * Define XMP properties used PDFA extension schema description schemas.  * TODO 2 naked so far, implement  *   * @author Karsten Krieg (kkrieg@intarsys.de)  * @version $Revision: 1.1 $  */
end_comment

begin_class
specifier|public
class|class
name|XMPSchemaPDFAId
extends|extends
name|XMPSchema
block|{
comment|/**      * The namespace for this schema.      * This is the future amendment of the PDFA Spec with the trailing slash at end      */
specifier|public
specifier|static
specifier|final
name|String
name|NAMESPACE
init|=
literal|"http://www.aiim.org/pdfa/ns/id/"
decl_stmt|;
comment|/**      * Construct a new blank PDFA schema.      *      * @param parent The parent metadata schema that this will be part of.      */
specifier|public
name|XMPSchemaPDFAId
parameter_list|(
name|XMPMetadata
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"pdfaid"
argument_list|,
name|NAMESPACE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor from existing XML element.      *       * @param element The existing element.      * @param prefix The schema prefix.      */
specifier|public
name|XMPSchemaPDFAId
parameter_list|(
name|Element
name|element
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|super
argument_list|(
name|element
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the ISO19005 part number.      *       * @return The ISO 19005 part number.      */
specifier|public
name|Integer
name|getPart
parameter_list|()
block|{
return|return
name|getIntegerProperty
argument_list|(
name|prefix
operator|+
literal|":part"
argument_list|)
return|;
block|}
comment|/**      * Set the ISO19005 part number.      *       * @param part The ISO 19005 part number.      */
specifier|public
name|void
name|setPart
parameter_list|(
name|Integer
name|part
parameter_list|)
block|{
name|setIntegerProperty
argument_list|(
name|prefix
operator|+
literal|":part"
argument_list|,
name|part
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the amendment idenitifier.      *      * @param amd The amendment idenitifier.      */
specifier|public
name|void
name|setAmd
parameter_list|(
name|String
name|amd
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":amd"
argument_list|,
name|amd
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the amendment idenitifier.      *      * @return The amendment idenitifier.      */
specifier|public
name|String
name|getAmd
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":amd"
argument_list|)
return|;
block|}
comment|/**      * Set the conformance level.      *      * @param conformance The conformance level.      */
specifier|public
name|void
name|setConformance
parameter_list|(
name|String
name|conformance
parameter_list|)
block|{
name|setTextProperty
argument_list|(
name|prefix
operator|+
literal|":conformance"
argument_list|,
name|conformance
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the conformance level.      *      * @return The conformance level.      */
specifier|public
name|String
name|getConformance
parameter_list|()
block|{
return|return
name|getTextProperty
argument_list|(
name|prefix
operator|+
literal|":conformance"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

