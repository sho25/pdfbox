begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|fdf
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
name|Writer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSDictionary
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|COSObjectable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|interactive
operator|.
name|digitalsignature
operator|.
name|PDSignature
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
comment|/**  * This represents an FDF catalog that is part of the FDF document.  *  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @version $Revision: 1.3 $  */
end_comment

begin_class
specifier|public
class|class
name|FDFCatalog
implements|implements
name|COSObjectable
block|{
specifier|private
name|COSDictionary
name|catalog
decl_stmt|;
comment|/**      * Default constructor.      */
specifier|public
name|FDFCatalog
parameter_list|()
block|{
name|catalog
operator|=
operator|new
name|COSDictionary
argument_list|()
expr_stmt|;
block|}
comment|/**      * Constructor.      *      * @param cat The FDF documents catalog.      */
specifier|public
name|FDFCatalog
parameter_list|(
name|COSDictionary
name|cat
parameter_list|)
block|{
name|catalog
operator|=
name|cat
expr_stmt|;
block|}
comment|/**      * This will create an FDF catalog from an XFDF XML document.      *      * @param element The XML document that contains the XFDF data.      * @throws IOException If there is an error reading from the dom.      */
specifier|public
name|FDFCatalog
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|IOException
block|{
name|this
argument_list|()
expr_stmt|;
name|FDFDictionary
name|fdfDict
init|=
operator|new
name|FDFDictionary
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|setFDF
argument_list|(
name|fdfDict
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will write this element as an XML document.      *      * @param output The stream to write the xml to.      *      * @throws IOException If there is an error writing the XML.      */
specifier|public
name|void
name|writeXML
parameter_list|(
name|Writer
name|output
parameter_list|)
throws|throws
name|IOException
block|{
name|FDFDictionary
name|fdf
init|=
name|getFDF
argument_list|()
decl_stmt|;
name|fdf
operator|.
name|writeXML
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSBase
name|getCOSObject
parameter_list|()
block|{
return|return
name|catalog
return|;
block|}
comment|/**      * Convert this standard java object to a COS object.      *      * @return The cos object that matches this Java object.      */
specifier|public
name|COSDictionary
name|getCOSDictionary
parameter_list|()
block|{
return|return
name|catalog
return|;
block|}
comment|/**      * This will get the version that was specified in the catalog dictionary.      *      * @return The FDF version.      */
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|getNameAsString
argument_list|(
literal|"Version"
argument_list|)
return|;
block|}
comment|/**      * This will set the version of the FDF document.      *      * @param version The new version for the FDF document.      */
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|catalog
operator|.
name|setName
argument_list|(
literal|"Version"
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the FDF dictionary.      *      * @return The FDF dictionary.      */
specifier|public
name|FDFDictionary
name|getFDF
parameter_list|()
block|{
name|COSDictionary
name|fdf
init|=
operator|(
name|COSDictionary
operator|)
name|catalog
operator|.
name|getDictionaryObject
argument_list|(
literal|"FDF"
argument_list|)
decl_stmt|;
name|FDFDictionary
name|retval
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fdf
operator|!=
literal|null
condition|)
block|{
name|retval
operator|=
operator|new
name|FDFDictionary
argument_list|(
name|fdf
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|retval
operator|=
operator|new
name|FDFDictionary
argument_list|()
expr_stmt|;
name|setFDF
argument_list|(
name|retval
argument_list|)
expr_stmt|;
block|}
return|return
name|retval
return|;
block|}
comment|/**      * This will set the FDF document.      *      * @param fdf The new FDF dictionary.      */
specifier|public
name|void
name|setFDF
parameter_list|(
name|FDFDictionary
name|fdf
parameter_list|)
block|{
name|catalog
operator|.
name|setItem
argument_list|(
literal|"FDF"
argument_list|,
name|fdf
argument_list|)
expr_stmt|;
block|}
comment|/**      * This will get the signature or null if there is none.      *      * @return The signature.      */
specifier|public
name|PDSignature
name|getSignature
parameter_list|()
block|{
name|PDSignature
name|signature
init|=
literal|null
decl_stmt|;
name|COSDictionary
name|sig
init|=
operator|(
name|COSDictionary
operator|)
name|catalog
operator|.
name|getDictionaryObject
argument_list|(
literal|"Sig"
argument_list|)
decl_stmt|;
if|if
condition|(
name|sig
operator|!=
literal|null
condition|)
block|{
name|signature
operator|=
operator|new
name|PDSignature
argument_list|(
name|sig
argument_list|)
expr_stmt|;
block|}
return|return
name|signature
return|;
block|}
comment|/**      * This will set the signature that is associated with this catalog.      *      * @param sig The new signature.      */
specifier|public
name|void
name|setSignature
parameter_list|(
name|PDSignature
name|sig
parameter_list|)
block|{
name|catalog
operator|.
name|setItem
argument_list|(
literal|"Sig"
argument_list|,
name|sig
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

