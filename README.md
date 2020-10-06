# Density Converter
Program to convert images to specific formats and density versions.

**Input:** Image file.

**Output:** Converted image file to specific format and density.

## Bug report

I am using your program to convert an image (found in `./pictures`), but the program is taking too long to execute (about 49 seconds).
This is the configuration that I used: `scale = true`.
Could you please help me understand why is the program taking so long? Is there anything that I can do on my end to speed up the program?

## Scale option

Sets by how much to scale down images. 
When you pass `true` as a program argument, then `scale = true (1f)`.
When you pass `false` as a program argument, then `scale = false (3f)`.

## Global performance-influence model (in seconds)

`T = 18.21 + 118.13 x SCALE`

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