# Density Converter
Program to convert images to specific formats, dimensions, and sizes.

**Input:** Image file.

**Output:** Converted image files in specific formats and sizes.

## Run

* Import as maven project to your favorite Java IDE.
* Run the main method in `at.favre.tools.dconvert.Convert` from your IDE.
    * Make sure that you can debug the program from your IDE.
    * You can change the configuration options in the main method.

## Performance bug report
I am using your program to convert an image (found in ./pictures), but it is taking quite some time to execute (about 156 seconds). 
This is the configuration that I used:

* `SCALE = true`
* `SCALE_IS_HEIGHT_DP = false` 
* `COMPRESSION_QUALITY = 0.9` 
* `OUT_COMPRESSION = "jpg"` 
* `PLATFORM = "android"` 
* `UPSCALING_ALGO = "LANCZOS3"` 
* `DOWNSCALING_ALGO = "LANCZOS3"` 
* `ROUNDING_MODE = "floor"` 
* `SKIP_UPSCALING = false` 
* `SKIP_EXISTING = false` 
* `ANDROID_INCLUDE_LDPI_TVDPI = false`  
* `VERBOSE = true`  
* `ANDROID_MIPMAP_INSTEAD_OF_DRAWABLE = false`  
* `ANTI_ALIASING = false`  
* `POST_PROCESSOR_PNG_CRUSH = false`  
* `POST_PROCESSOR_WEBP = false`  
* `DRY_RUN = false`  
* `POST_PROCESSOR_MOZ_JPEG = false`  
* `KEEP_ORIGINAL_POST_PROCESSED_FILES = false`  
* `IOS_CREATE_IMAGESET_FOLDERS = false`  
* `CLEAN = true`  
* `HALT_ON_ERROR = false`  

I tried converting the same image multiple times with the same configuration, and I observed the same execution time. 
I even tried converting other images with the same configuration, executing the program multiple times, and the program still took quite some time. 
Could you please take a look at why the program is taking so long to convert images?

### SCALE
Whether to scale the output image to a larger size.

### SCALE_IS_HEIGHT_DP
Whether to interpret the scale as fixed height and not as width, if the scale is in dp.
 
### COMPRESSION_QUALITY
Only used with compression "jpg". Sets the quality [0-1.0] of the output image, where 1.0 is the highest quality. 
Default is 0.9.
 
### OUT_COMPRESSION
Sets the compression of the converted image. 
Can be "png", "jpg", "gif", "bmp", "png+jpg", or "strict".
The latter tries to use same compression as source. 
By default, it will convert to png except if the source compression is jpeg.
 
### PLATFORM
Can be "all", "android", "ios", "win" or "web". 
Sets the formats of the converted images.
E.g., set "android" if you only want to convert to android format. 
Default is [IOS, ANDROID].
 
### UPSCALING_ALGO
Algorithm to scale up the image.
 
### DOWNSCALING_ALGO
Algorithm to scale down the image.
 
### ROUNDING_MODE
Defines the rounding mode when scaling the dimensions. 
Possibles are "round" (rounds up of >= 0.5), "floor" (rounds down), and "ceil" (rounds up). 
Default is "round".
 
### SKIP_UPSCALING
Whether to only scale down, but not up to prevent image quality loss.
 
### SKIP_EXISTING
Whether to overwrite an already existing file.
 
### ANDROID_INCLUDE_LDPI_TVDPI
Android only.
Whether to create mipmap sub-folders instead of drawable.
  
### VERBOSE
Whether to log more verbosely to the console.
  
### ANDROID_MIPMAP_INSTEAD_OF_DRAWABLE
Android only.
Whether to include additional densities (ldpi and tvdpi).
  
### ANTI_ALIASING
Whether to create anti-aliases images with a little more blurred result.
A light 3x3 convolve matrix is used.
Useful for very small images.
  
### POST_PROCESSOR_PNG_CRUSH
Whether to post-process all pngs with pngcrush, a lossless compressor. 
  
### POST_PROCESSOR_WEBP
Whether to additionally convert all png/gif to lossless wepb and all jpg to lossy webp with cwebp. 
Does not delete source files.
  
### DRY_RUN
Whether to not create any images or folders. 
Useful as a fast preview in a log of what images and in what resolutions would be created.
  
### POST_PROCESSOR_MOZ_JPEG
Whether to post-process all jpegs with mozJpegs lossless compressor.
  
### KEEP_ORIGINAL_POST_PROCESSED_FILES
If a post-processor is run on an image, determine whether the original will be kept (renamed _orig).
Otherwise only the optimized image will be kept.
  
### IOS_CREATE_IMAGESET_FOLDERS
iOS only.
Whether to create .imageset folders and Content.json for every source image.
Default is to put all images in the root folder.
  
### CLEAN
Whether to delete all file and folders in the output directory that would be used in the current configuration before converting.
  
### HALT_ON_ERROR
Whether to stop the process if an error occurred during conversion.

## Task
**Address the bug report**. Specifically, **how would you answer the question** in the bug report **"why is the system taking so long to execute?"**?.

## Assumptions

* The input image is valid.
* You have successfully reproduced the bug report and confirmed the performance behavior.
Executing the program multiple times with different images, but with the same configuration, takes a long time.
* The program runs in fixed hardware. 
That is, the developer replying to the bug report cannot suggest the user to run the program on faster hardware.
