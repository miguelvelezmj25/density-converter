# Density Converter
Program to convert images to specific formats and sizes.

**Input:** Image file.

**Output:** Converted image file to specific format and size.

## Performance Bug Report

I am using your program to convert an image (found in `./pictures`), but the program is taking too long to execute (about 156 seconds).
This is the configuration that I used: 

* `scale = true`

### Scale option

Whether to scale the output image to a larger size.

## Task
Determine why the program is taking too long based on the configuration indicated in the bug report.  

## Assumptions

* The input image is valid.
* You have successfully reproduced the bug report and confirmed the performance behavior.
* The program runs in fixed hardware. 
That is, the developer replying to the bug report cannot suggest the user to run the program on faster hardware.

## Global performance-influence model (in seconds)

`T = 22.73 + 134.09 x SCALE`

## Local performance-influence models (in seconds)

* com.mortennobel.imagescaling.ImageUtils.setBGRPixels([BLjava/awt/image/BufferedImage;IIII)V
    * `T = 0.18 x SCALE`

* com.mortennobel.imagescaling.AdvancedResizeOp.fireProgressChanged(F)V
    * `T = 0.83 + 1.77 x SCALE`

* at.favre.tools.dconvert.util.ImageUtil.read(Ljavax/imageio/stream/ImageInputStream;Lat/favre/tools/dconvert/arg/ImageType;)Lat/favre/tools/dconvert/util/LoadedImage;
    * `T = 0.43`
    
* com.mortennobel.imagescaling.ResampleOp.verticalFromWorkToDst([[B[BII)
    * `T = 9.46 + 95.3 x SCALE`
    
* at.favre.tools.dconvert.DConvert.execute(Lat/favre/tools/dconvert/arg/Arguments;ZLat/favre/tools/dconvert/DConvert$HandlerCallback;)V
    * `T = 0.11`
    
* at.favre.tools.dconvert.converters.scaling.ImageHandler.compressJpeg(Ljava/awt/image/BufferedImage;Lcom/twelvemonkeys/imageio/metadata/CompoundDirectory;FLjava/io/File;)V
    * `T = 3.36 + 22.07 x SCALE`
    
* at.favre.tools.dconvert.converters.scaling.ImageHandler.scale(Lat/favre/tools/dconvert/converters/scaling/ScaleAlgorithm;Ljava/awt/image/BufferedImage;IILat/favre/tools/dconvert/arg/ImageType$ECompression;Ljava/awt/Color;)Ljava/awt/image/BufferedImage;
    * `T = 0.31 + 4.08 x SCALE`
    
* com.mortennobel.imagescaling.ResampleOp.horizontallyFromSrcToWork(Ljava/awt/image/BufferedImage;[[BII)V
    * `T = 7.38 + 11.04 x SCALE`
    
* com.mortennobel.imagescaling.ImageUtils.getPixelsBGR(Ljava/awt/image/BufferedImage;II[B[I)[B
    * `T = 0.85 - 0.55 x SCALE`
    
* com.mortennobel.imagescaling.ResampleOp.doFilter(Ljava/awt/image/BufferedImage;Ljava/awt/image/BufferedImage;II)Ljava/awt/image/BufferedImage;
    * `T = 0.20 x SCALE`