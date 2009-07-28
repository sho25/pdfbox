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
name|graphics
operator|.
name|xobject
package|;
end_package

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|DataBufferByte
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|BufferedImage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|ColorModel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|IndexColorModel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|awt
operator|.
name|image
operator|.
name|WritableRaster
import|;
end_import

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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSArray
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
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
name|apache
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
name|apache
operator|.
name|pdfbox
operator|.
name|cos
operator|.
name|COSName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|PDStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|graphics
operator|.
name|color
operator|.
name|PDColorSpace
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|graphics
operator|.
name|predictor
operator|.
name|PredictorAlgorithm
import|;
end_import

begin_comment
comment|/**  * This class contains a PixelMap Image.  * @author<a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>  * @author mathiak  * @version $Revision: 1.10 $  */
end_comment

begin_class
specifier|public
class|class
name|PDPixelMap
extends|extends
name|PDXObjectImage
block|{
specifier|private
name|BufferedImage
name|image
init|=
literal|null
decl_stmt|;
comment|/**      * Standard constructor. Basically does nothing.      * @param pdStream The stream that holds the pixel map.      */
specifier|public
name|PDPixelMap
parameter_list|(
name|PDStream
name|pdStream
parameter_list|)
block|{
name|super
argument_list|(
name|pdStream
argument_list|,
literal|"png"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct a pixel map image from an AWT image.      *      * @param doc The PDF document to embed the image in.      * @param awtImage The image to read data from.      *      * @throws IOException If there is an error while embedding this image.      */
comment|/*      * This method is broken and needs to be implemented, any takers?     public PDPixelMap(PDDocument doc, BufferedImage awtImage) throws IOException     {         super( doc, "png");         image = awtImage;         setWidth( image.getWidth() );         setHeight( image.getHeight() );          ColorModel cm = image.getColorModel();         ColorSpace cs = cm.getColorSpace();         PDColorSpace pdColorSpace = PDColorSpaceFactory.createColorSpace( doc, cs );         setColorSpace( pdColorSpace );         //setColorSpace( )          PDStream stream = getPDStream();         OutputStream output = null;         try         {             output = stream.createOutputStream();             DataBuffer buffer = awtImage.getRaster().getDataBuffer();             if( buffer instanceof DataBufferByte )             {                 DataBufferByte byteBuffer = (DataBufferByte)buffer;                 byte[] data = byteBuffer.getData();                 output.write( data );             }             setBitsPerComponent( cm.getPixelSize() );         }         finally         {             if( output != null )             {                 output.close();             }         }     }*/
comment|/**      * Returns a {@link java.awt.image.BufferedImage} of the COSStream      * set in the constructor or null if the COSStream could not be encoded.      *      * @return {@inheritDoc}      *      * @throws IOException {@inheritDoc}      */
specifier|public
name|BufferedImage
name|getRGBImage
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
return|return
name|image
return|;
block|}
try|try
block|{
name|int
name|width
init|=
name|getWidth
argument_list|()
decl_stmt|;
name|int
name|height
init|=
name|getHeight
argument_list|()
decl_stmt|;
name|int
name|bpc
init|=
name|getBitsPerComponent
argument_list|()
decl_stmt|;
name|int
name|predictor
init|=
name|getPredictor
argument_list|()
decl_stmt|;
name|List
name|filters
init|=
name|getPDStream
argument_list|()
operator|.
name|getFilters
argument_list|()
decl_stmt|;
name|ColorModel
name|cm
decl_stmt|;
name|byte
index|[]
name|array
init|=
name|getPDStream
argument_list|()
operator|.
name|getByteArray
argument_list|()
decl_stmt|;
comment|// Get the ColorModel right
name|PDColorSpace
name|colorspace
init|=
name|getColorSpace
argument_list|()
decl_stmt|;
if|if
condition|(
name|colorspace
operator|==
literal|null
condition|)
block|{
name|logger
argument_list|()
operator|.
name|severe
argument_list|(
literal|"getColorSpace() returned NULL.  Predictor = "
operator|+
name|getPredictor
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
name|bpc
operator|==
literal|1
condition|)
block|{
name|byte
index|[]
name|map
init|=
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0xff
block|}
decl_stmt|;
name|cm
operator|=
operator|new
name|IndexColorModel
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
name|map
argument_list|,
name|map
argument_list|,
name|map
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cm
operator|=
name|colorspace
operator|.
name|createColorModel
argument_list|(
name|bpc
argument_list|)
expr_stmt|;
block|}
name|logger
argument_list|()
operator|.
name|info
argument_list|(
literal|"ColorModel: "
operator|+
name|cm
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|WritableRaster
name|raster
init|=
name|cm
operator|.
name|createCompatibleWritableRaster
argument_list|(
name|width
argument_list|,
name|height
argument_list|)
decl_stmt|;
name|DataBufferByte
name|buffer
init|=
operator|(
name|DataBufferByte
operator|)
name|raster
operator|.
name|getDataBuffer
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bufferData
init|=
name|buffer
operator|.
name|getData
argument_list|()
decl_stmt|;
comment|/**              * PDF Spec 1.6 3.3.3 LZW and Flate predictor function              *              * Basically if predictor> 10 and LZW or Flate is being used then the              * predictor is not used.              *              * "For LZWDecode and FlateDecode, a Predictor value greater than or equal to 10              * merely indicates that a PNG predictor is in use; the specific predictor function              * used is explicitly encoded in the incoming data. The value of Predictor supplied              * by the decoding filter need not match the value used when the data was encoded              * if they are both greater than or equal to 10."              */
if|if
condition|(
name|predictor
operator|<
literal|10
operator|||
name|filters
operator|==
literal|null
operator|||
operator|!
operator|(
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|LZW_DECODE
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|filters
operator|.
name|contains
argument_list|(
name|COSName
operator|.
name|FLATE_DECODE
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|PredictorAlgorithm
name|filter
init|=
name|PredictorAlgorithm
operator|.
name|getFilter
argument_list|(
name|predictor
argument_list|)
decl_stmt|;
name|filter
operator|.
name|setWidth
argument_list|(
name|width
argument_list|)
expr_stmt|;
name|filter
operator|.
name|setHeight
argument_list|(
name|height
argument_list|)
expr_stmt|;
name|filter
operator|.
name|setBpp
argument_list|(
operator|(
name|bpc
operator|*
literal|3
operator|)
operator|/
literal|8
argument_list|)
expr_stmt|;
name|filter
operator|.
name|decode
argument_list|(
name|array
argument_list|,
name|bufferData
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|array
argument_list|,
literal|0
argument_list|,
name|bufferData
argument_list|,
literal|0
argument_list|,
operator|(
name|array
operator|.
name|length
operator|<
name|bufferData
operator|.
name|length
condition|?
name|array
operator|.
name|length
else|:
name|bufferData
operator|.
name|length
operator|)
argument_list|)
expr_stmt|;
block|}
name|image
operator|=
operator|new
name|BufferedImage
argument_list|(
name|cm
argument_list|,
name|raster
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|image
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|logger
argument_list|()
operator|.
name|severe
argument_list|(
name|exception
operator|.
name|toString
argument_list|()
operator|+
literal|"\n at\n"
operator|+
name|FullStackTrace
argument_list|(
name|exception
argument_list|)
argument_list|)
expr_stmt|;
comment|//A NULL return is caught in pagedrawer.Invoke.process() so don't re-throw.
comment|//Returning the NULL falls through to Phlip Koch's TODO section.
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Writes the image as .png.      *      * {@inheritDoc}      */
specifier|public
name|void
name|write2OutputStream
parameter_list|(
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|getRGBImage
argument_list|()
expr_stmt|;
if|if
condition|(
name|image
operator|!=
literal|null
condition|)
block|{
name|ImageIO
operator|.
name|write
argument_list|(
name|image
argument_list|,
literal|"png"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * DecodeParms is an optional parameter for filters.      *      * It is provided if any of the filters has nondefault parameters. If there      * is only one filter it is a dictionary, if there are multiple filters it      * is an array with an entry for each filter. An array entry can hold a null      * value if only the default values are used or a dictionary with      * parameters.      *      * @return The decoding parameters.      *      */
specifier|public
name|COSDictionary
name|getDecodeParams
parameter_list|()
block|{
name|COSBase
name|decodeParms
init|=
name|getCOSStream
argument_list|()
operator|.
name|getDictionaryObject
argument_list|(
literal|"DecodeParms"
argument_list|)
decl_stmt|;
if|if
condition|(
name|decodeParms
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|decodeParms
operator|instanceof
name|COSDictionary
condition|)
block|{
return|return
operator|(
name|COSDictionary
operator|)
name|decodeParms
return|;
block|}
elseif|else
if|if
condition|(
name|decodeParms
operator|instanceof
name|COSArray
condition|)
block|{
comment|// not implemented yet, which index should we use?
return|return
literal|null
return|;
comment|//(COSDictionary)((COSArray)decodeParms).get(0);
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A code that selects the predictor algorithm.      *      *<ul>      *<li>1 No prediction (the default value)      *<li>2 TIFF Predictor 2      *<li>10 PNG prediction (on encoding, PNG None on all rows)      *<li>11 PNG prediction (on encoding, PNG Sub on all rows)      *<li>12 PNG prediction (on encoding, PNG Up on all rows)      *<li>13 PNG prediction (on encoding, PNG Average on all rows)      *<li>14 PNG prediction (on encoding, PNG Paeth on all rows)      *<li>15 PNG prediction (on encoding, PNG optimum)      *</ul>      *      * Default value: 1.      *      * @return predictor algorithm code      */
specifier|public
name|int
name|getPredictor
parameter_list|()
block|{
name|COSDictionary
name|decodeParms
init|=
name|getDecodeParams
argument_list|()
decl_stmt|;
if|if
condition|(
name|decodeParms
operator|!=
literal|null
condition|)
block|{
name|int
name|i
init|=
name|decodeParms
operator|.
name|getInt
argument_list|(
literal|"Predictor"
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|i
return|;
block|}
block|}
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

