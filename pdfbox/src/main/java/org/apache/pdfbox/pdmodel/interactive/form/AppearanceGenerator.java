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
name|interactive
operator|.
name|form
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
comment|/**  * (Re-) Generate the appearance for a field.  *   * The fields appearance defines the 'look' the field has when it's rendered for display or printing.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|AppearanceGenerator
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
name|AppearanceGenerator
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|AppearanceGenerator
parameter_list|()
block|{     }
comment|/**      * Generate the appearances for a single field.      *       * @param field The field which appearances need to be generated.      * @throws IOException       */
specifier|public
specifier|static
name|void
name|generateFieldAppearances
parameter_list|(
name|PDField
name|field
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|field
operator|instanceof
name|PDVariableText
condition|)
block|{
name|AppearanceGeneratorHelper
name|apHelper
init|=
literal|null
decl_stmt|;
name|Object
name|fieldValue
init|=
literal|null
decl_stmt|;
name|apHelper
operator|=
operator|new
name|AppearanceGeneratorHelper
argument_list|(
name|field
operator|.
name|getAcroForm
argument_list|()
argument_list|,
operator|(
name|PDVariableText
operator|)
name|field
argument_list|)
expr_stmt|;
name|fieldValue
operator|=
name|field
operator|.
name|getValue
argument_list|()
expr_stmt|;
comment|// TODO: implement the handling for additional values.
if|if
condition|(
name|fieldValue
operator|instanceof
name|String
condition|)
block|{
name|apHelper
operator|.
name|setAppearanceValue
argument_list|(
operator|(
name|String
operator|)
name|fieldValue
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|fieldValue
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Can't generate the appearance for values typed "
operator|+
name|fieldValue
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
argument_list|)
expr_stmt|;
block|}
block|}
comment|// TODO: implement the handling for additional field types
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Unable to generate the field appearance."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

