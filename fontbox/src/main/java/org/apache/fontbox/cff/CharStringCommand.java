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
name|fontbox
operator|.
name|cff
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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

begin_comment
comment|/**  * This class represents a CharStringCommand.  *   * @author Villu Ruusmann  */
end_comment

begin_class
specifier|public
class|class
name|CharStringCommand
block|{
specifier|private
name|Key
name|commandKey
init|=
literal|null
decl_stmt|;
comment|/**      * Constructor with one value.      *       * @param b0 value      */
specifier|public
name|CharStringCommand
parameter_list|(
name|int
name|b0
parameter_list|)
block|{
name|setKey
argument_list|(
operator|new
name|Key
argument_list|(
name|b0
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor with two values.      *       * @param b0 value1      * @param b1 value2      */
specifier|public
name|CharStringCommand
parameter_list|(
name|int
name|b0
parameter_list|,
name|int
name|b1
parameter_list|)
block|{
name|setKey
argument_list|(
operator|new
name|Key
argument_list|(
name|b0
argument_list|,
name|b1
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor with an array as values.      *       * @param values array of values      */
specifier|public
name|CharStringCommand
parameter_list|(
name|int
index|[]
name|values
parameter_list|)
block|{
name|setKey
argument_list|(
operator|new
name|Key
argument_list|(
name|values
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * The key of the CharStringCommand.      * @return the key      */
specifier|public
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|commandKey
return|;
block|}
specifier|private
name|void
name|setKey
parameter_list|(
name|Key
name|key
parameter_list|)
block|{
name|commandKey
operator|=
name|key
expr_stmt|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|str
init|=
name|TYPE2_VOCABULARY
operator|.
name|get
argument_list|(
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
name|str
operator|=
name|TYPE1_VOCABULARY
operator|.
name|get
argument_list|(
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
return|return
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|'|'
return|;
block|}
return|return
name|str
operator|+
literal|'|'
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getKey
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|CharStringCommand
condition|)
block|{
name|CharStringCommand
name|that
init|=
operator|(
name|CharStringCommand
operator|)
name|object
decl_stmt|;
return|return
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getKey
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
comment|/**      * A static class to hold one or more int values as key.       */
specifier|public
specifier|static
class|class
name|Key
block|{
specifier|private
name|int
index|[]
name|keyValues
init|=
literal|null
decl_stmt|;
comment|/**          * Constructor with one value.          *           * @param b0 value          */
specifier|public
name|Key
parameter_list|(
name|int
name|b0
parameter_list|)
block|{
name|setValue
argument_list|(
operator|new
name|int
index|[]
block|{
name|b0
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**          * Constructor with two values.          *           * @param b0 value1          * @param b1 value2          */
specifier|public
name|Key
parameter_list|(
name|int
name|b0
parameter_list|,
name|int
name|b1
parameter_list|)
block|{
name|setValue
argument_list|(
operator|new
name|int
index|[]
block|{
name|b0
block|,
name|b1
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**          * Constructor with an array as values.          *           * @param values array of values          */
specifier|public
name|Key
parameter_list|(
name|int
index|[]
name|values
parameter_list|)
block|{
name|setValue
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
comment|/**          * Array the with the values.          *           * @return array with the values          */
specifier|public
name|int
index|[]
name|getValue
parameter_list|()
block|{
return|return
name|keyValues
return|;
block|}
specifier|private
name|void
name|setValue
parameter_list|(
name|int
index|[]
name|value
parameter_list|)
block|{
name|keyValues
operator|=
name|value
expr_stmt|;
block|}
comment|/**          * {@inheritDoc}          */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|toString
argument_list|(
name|getValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**          * {@inheritDoc}          */
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|keyValues
index|[
literal|0
index|]
operator|==
literal|12
operator|&&
name|keyValues
operator|.
name|length
operator|>
literal|1
condition|)
block|{
return|return
name|keyValues
index|[
literal|0
index|]
operator|^
name|keyValues
index|[
literal|1
index|]
return|;
block|}
return|return
name|keyValues
index|[
literal|0
index|]
return|;
block|}
comment|/**          * {@inheritDoc}          */
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|Key
condition|)
block|{
name|Key
name|that
init|=
operator|(
name|Key
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|keyValues
index|[
literal|0
index|]
operator|==
literal|12
operator|&&
name|that
operator|.
name|keyValues
index|[
literal|0
index|]
operator|==
literal|12
condition|)
block|{
if|if
condition|(
name|keyValues
operator|.
name|length
operator|>
literal|1
operator|&&
name|that
operator|.
name|keyValues
operator|.
name|length
operator|>
literal|1
condition|)
block|{
return|return
name|keyValues
index|[
literal|1
index|]
operator|==
name|that
operator|.
name|keyValues
index|[
literal|1
index|]
return|;
block|}
return|return
name|keyValues
operator|.
name|length
operator|==
name|that
operator|.
name|keyValues
operator|.
name|length
return|;
block|}
return|return
name|keyValues
index|[
literal|0
index|]
operator|==
name|that
operator|.
name|keyValues
index|[
literal|0
index|]
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
comment|/**      * A map with the Type1 vocabulary.      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|Key
argument_list|,
name|String
argument_list|>
name|TYPE1_VOCABULARY
decl_stmt|;
static|static
block|{
name|Map
argument_list|<
name|Key
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
literal|26
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"hstem"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|3
argument_list|)
argument_list|,
literal|"vstem"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|4
argument_list|)
argument_list|,
literal|"vmoveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|5
argument_list|)
argument_list|,
literal|"rlineto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|6
argument_list|)
argument_list|,
literal|"hlineto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|7
argument_list|)
argument_list|,
literal|"vlineto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|8
argument_list|)
argument_list|,
literal|"rrcurveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|9
argument_list|)
argument_list|,
literal|"closepath"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|10
argument_list|)
argument_list|,
literal|"callsubr"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|11
argument_list|)
argument_list|,
literal|"return"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|)
argument_list|,
literal|"escape"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|0
argument_list|)
argument_list|,
literal|"dotsection"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|1
argument_list|)
argument_list|,
literal|"vstem3"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|2
argument_list|)
argument_list|,
literal|"hstem3"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|6
argument_list|)
argument_list|,
literal|"seac"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|7
argument_list|)
argument_list|,
literal|"sbw"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|12
argument_list|)
argument_list|,
literal|"div"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|16
argument_list|)
argument_list|,
literal|"callothersubr"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|17
argument_list|)
argument_list|,
literal|"pop"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|33
argument_list|)
argument_list|,
literal|"setcurrentpoint"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|13
argument_list|)
argument_list|,
literal|"hsbw"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|14
argument_list|)
argument_list|,
literal|"endchar"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|21
argument_list|)
argument_list|,
literal|"rmoveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|22
argument_list|)
argument_list|,
literal|"hmoveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|30
argument_list|)
argument_list|,
literal|"vhcurveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|31
argument_list|)
argument_list|,
literal|"hvcurveto"
argument_list|)
expr_stmt|;
name|TYPE1_VOCABULARY
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
comment|/**      * A map with the Type2 vocabulary.      */
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|Key
argument_list|,
name|String
argument_list|>
name|TYPE2_VOCABULARY
decl_stmt|;
static|static
block|{
name|Map
argument_list|<
name|Key
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
literal|48
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"hstem"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|3
argument_list|)
argument_list|,
literal|"vstem"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|4
argument_list|)
argument_list|,
literal|"vmoveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|5
argument_list|)
argument_list|,
literal|"rlineto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|6
argument_list|)
argument_list|,
literal|"hlineto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|7
argument_list|)
argument_list|,
literal|"vlineto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|8
argument_list|)
argument_list|,
literal|"rrcurveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|10
argument_list|)
argument_list|,
literal|"callsubr"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|11
argument_list|)
argument_list|,
literal|"return"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|)
argument_list|,
literal|"escape"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|3
argument_list|)
argument_list|,
literal|"and"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|4
argument_list|)
argument_list|,
literal|"or"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|5
argument_list|)
argument_list|,
literal|"not"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|9
argument_list|)
argument_list|,
literal|"abs"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|10
argument_list|)
argument_list|,
literal|"add"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|11
argument_list|)
argument_list|,
literal|"sub"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|12
argument_list|)
argument_list|,
literal|"div"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|14
argument_list|)
argument_list|,
literal|"neg"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|15
argument_list|)
argument_list|,
literal|"eq"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|18
argument_list|)
argument_list|,
literal|"drop"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|20
argument_list|)
argument_list|,
literal|"put"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|21
argument_list|)
argument_list|,
literal|"get"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|22
argument_list|)
argument_list|,
literal|"ifelse"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|23
argument_list|)
argument_list|,
literal|"random"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|24
argument_list|)
argument_list|,
literal|"mul"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|26
argument_list|)
argument_list|,
literal|"sqrt"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|27
argument_list|)
argument_list|,
literal|"dup"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|28
argument_list|)
argument_list|,
literal|"exch"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|29
argument_list|)
argument_list|,
literal|"index"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|30
argument_list|)
argument_list|,
literal|"roll"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|34
argument_list|)
argument_list|,
literal|"hflex"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|35
argument_list|)
argument_list|,
literal|"flex"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|36
argument_list|)
argument_list|,
literal|"hflex1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|12
argument_list|,
literal|37
argument_list|)
argument_list|,
literal|"flex1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|14
argument_list|)
argument_list|,
literal|"endchar"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|18
argument_list|)
argument_list|,
literal|"hstemhm"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|19
argument_list|)
argument_list|,
literal|"hintmask"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|20
argument_list|)
argument_list|,
literal|"cntrmask"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|21
argument_list|)
argument_list|,
literal|"rmoveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|22
argument_list|)
argument_list|,
literal|"hmoveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|23
argument_list|)
argument_list|,
literal|"vstemhm"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|24
argument_list|)
argument_list|,
literal|"rcurveline"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|25
argument_list|)
argument_list|,
literal|"rlinecurve"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|26
argument_list|)
argument_list|,
literal|"vvcurveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|27
argument_list|)
argument_list|,
literal|"hhcurveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|28
argument_list|)
argument_list|,
literal|"shortint"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|29
argument_list|)
argument_list|,
literal|"callgsubr"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|30
argument_list|)
argument_list|,
literal|"vhcurveto"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
operator|new
name|Key
argument_list|(
literal|31
argument_list|)
argument_list|,
literal|"hvcurveto"
argument_list|)
expr_stmt|;
name|TYPE2_VOCABULARY
operator|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

