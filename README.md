# Density Converter
Program to convert images to specific formats and sizes.

**Input:** Image file.

**Output:** Converted image file to specific format and size.

## Run

* Import maven project to your favorite Java IDE
* Run `$ mvn clean compile`
* Execute the main method in `at.favre.tools.dconvert.Convert`
    * You can change the configuration options in the main method.

## Performance bug report
I am using your program to convert an image (found in `./pictures`), but the program is taking too long to execute (about 156 seconds).
This is the configuration that I used: 

* `SCALE = true`

I tried converting the same image multiple times with the same configuration, and the program always took too long to execute.
I even tried converting other images with the same configuration, executing the program multiple times, and the program still took a really long time.
Could you please take a look at why it is taking so long to convert images?

### SCALE option
Whether to scale the output image to a larger size.

## Global performance-influence model (in seconds)
`T = 22.73 + 134.09 x SCALE`

## Local performance-influence models (in seconds)
    
* com.mortennobel.imagescaling.ResampleOp.verticalFromWorkToDst([[B[BII)
    * `T = 9.46 + 95.3 x SCALE`
    
* at.favre.tools.dconvert.converters.scaling.ImageHandler.compressJpeg(Ljava/awt/image/BufferedImage;Lcom/twelvemonkeys/imageio/metadata/CompoundDirectory;FLjava/io/File;)V
    * `T = 3.36 + 22.07 x SCALE`
    
* com.mortennobel.imagescaling.ResampleOp.horizontallyFromSrcToWork(Ljava/awt/image/BufferedImage;[[BII)V
    * `T = 7.38 + 11.04 x SCALE`
    
* at.favre.tools.dconvert.converters.scaling.ImageHandler.scale(Lat/favre/tools/dconvert/converters/scaling/ScaleAlgorithm;Ljava/awt/image/BufferedImage;IILat/favre/tools/dconvert/arg/ImageType$ECompression;Ljava/awt/Color;)Ljava/awt/image/BufferedImage;
    * `T = 0.31 + 4.08 x SCALE`

## Task 1
**Debug the bug report**. Determine why the program is taking too long to execute based on the configuration indicated in the bug report.

*The program is taking too long to execute because of the option* `SCALE`. 
When `SCALE = true`, as indicated in the bug report, the program takes 156.82 seconds to execute.
When `SCALE = false`, the program takes 22.73 seconds to execute.

## Task 2
**Continue debugging the bug report**. Determine why the program is taking too long to execute based on the option `SCALE`.

*Setting* `SCALE = true` *significantly increases the performance of four methods in the program*.

## Task 3
**Continue debugging the bug report**. Determine why the four methods are taking too long to execute based on the option `SCALE`.

## Assumptions

* The input image is valid.
* You have successfully reproduced the bug report and confirmed the performance behavior.
Executing the program multiple times with different images, but with the same configuration, takes a long time.
* The program runs in fixed hardware. 
That is, the developer replying to the bug report cannot suggest the user to run the program on faster hardware.
