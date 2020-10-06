/*
 *  Copyright 2016 Patrick Favre-Bulle
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.favre.tools.dconvert;

import at.favre.tools.dconvert.arg.*;
import at.favre.tools.dconvert.util.MiscUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entry point of the app. Use arg -h to get help.
 */
public final class Convert {

    private static float SCALE;
    private static boolean SCALE_IS_HEIGHT_DP;
    private static float COMPRESSION_QUALITY;
    private static String OUT_COMPRESSION;
    private static String PLATFORM;
    private static String UPSCALING_ALGO;
    private static String DOWNSCALING_ALGO;
    private static String ROUNDING_MODE;
    private static boolean SKIP_UPSCALING;
    private static boolean SKIP_EXISTING;
    private static boolean ANDROID_INCLUDE_LDPI_TVDPI;
    private static boolean VERBOSE;
    private static boolean ANDROID_MIPMAP_INSTEAD_OF_DRAWABLE;
    private static boolean ANTI_ALIASING;
    private static boolean POST_PROCESSOR_PNG_CRUSH;
    private static boolean POST_PROCESSOR_WEBP;
    private static boolean DRY_RUN;
    private static boolean POST_PROCESSOR_MOZ_JPEG;
    private static boolean KEEP_ORIGINAL_POST_PROCESSED_FILES;
    private static boolean IOS_CREATE_IMAGESET_FOLDERS;
    private static boolean CLEAN;
    private static boolean HALT_ON_ERROR;

    private Convert() {
    }

    public static void main(String[] rawArgs) {
        SCALE = scale(Boolean.parseBoolean(rawArgs[0]));
        SCALE_IS_HEIGHT_DP = false; //Boolean.parseBoolean(rawArgs[1]);
        COMPRESSION_QUALITY = compressionQuality(false); //compressionQuality(Boolean.parseBoolean(rawArgs[2]));
        OUT_COMPRESSION = outCompression(false); //outCompression(Boolean.parseBoolean(rawArgs[3]));
        PLATFORM = platform(false); //platform(Boolean.parseBoolean(rawArgs[4]));
        UPSCALING_ALGO = upScalingAlgo(false); //upScalingAlgo(Boolean.parseBoolean(rawArgs[5]));
        DOWNSCALING_ALGO = downScalingAlgo(false); //downScalingAlgo(Boolean.parseBoolean(rawArgs[6]));
        ROUNDING_MODE = roundingMode(false); //roundingMode(Boolean.parseBoolean(rawArgs[7]));
        SKIP_UPSCALING = false; //Boolean.parseBoolean(rawArgs[8]);
        SKIP_EXISTING = false; //Boolean.parseBoolean(rawArgs[9]);
        ANDROID_INCLUDE_LDPI_TVDPI = false;  //Boolean.parseBoolean(rawArgs[10]);
        VERBOSE = false;  //Boolean.parseBoolean(rawArgs[11]);
        ANDROID_MIPMAP_INSTEAD_OF_DRAWABLE = false;  //Boolean.parseBoolean(rawArgs[12]);
        ANTI_ALIASING = false;  //Boolean.parseBoolean(rawArgs[13]);
        POST_PROCESSOR_PNG_CRUSH = false;  //Boolean.parseBoolean(rawArgs[14]);
        POST_PROCESSOR_WEBP = false;  //Boolean.parseBoolean(rawArgs[15]);
        DRY_RUN = false;  //Boolean.parseBoolean(rawArgs[16]);
        POST_PROCESSOR_MOZ_JPEG = false;  //Boolean.parseBoolean(rawArgs[17]);
        KEEP_ORIGINAL_POST_PROCESSED_FILES = false;  //Boolean.parseBoolean(rawArgs[18]);
        IOS_CREATE_IMAGESET_FOLDERS = false;  //Boolean.parseBoolean(rawArgs[19]);
        CLEAN = false;  //Boolean.parseBoolean(rawArgs[20]);
        HALT_ON_ERROR = false;  //Boolean.parseBoolean(rawArgs[21]);

        File src = new File("./pictures/person.jpg");
        File dst = new File("./output");
        float scale = SCALE;
        Set<EPlatform> platform = getPlatform(PLATFORM);
        EOutputCompressionMode outputCompressionMode = getOutCompression(OUT_COMPRESSION);
        EScaleMode scaleMode = scaleMode(false, SCALE_IS_HEIGHT_DP);
        EScalingAlgorithm downScalingAlgorithm = EScalingAlgorithm.getByName(DOWNSCALING_ALGO);
        EScalingAlgorithm upScalingAlgorithm = EScalingAlgorithm.getByName(UPSCALING_ALGO);
        float compressionQuality = COMPRESSION_QUALITY;
        boolean skipExistingFiles = SKIP_EXISTING;
        boolean skipUpscaling = SKIP_UPSCALING;
        boolean verboseLog = VERBOSE;
        boolean includeAndroidLdpiTvdpi = ANDROID_INCLUDE_LDPI_TVDPI;
        boolean haltOnError = HALT_ON_ERROR;
        boolean createMipMapInsteadOfDrawableDir = ANDROID_MIPMAP_INSTEAD_OF_DRAWABLE;
        boolean iosCreateImagesetFolders = IOS_CREATE_IMAGESET_FOLDERS;
        boolean enablePngCrush = POST_PROCESSOR_PNG_CRUSH;
        boolean enableMozJpeg = POST_PROCESSOR_MOZ_JPEG;
        boolean postConvertWebp = POST_PROCESSOR_WEBP;
        boolean enableAntiAliasing = ANTI_ALIASING;
        boolean dryRun = DRY_RUN;
        boolean keepUnoptimizedFilesPostProcessor = KEEP_ORIGINAL_POST_PROCESSED_FILES;
        RoundingHandler.Strategy roundingHandler = getRoundingHandler(ROUNDING_MODE);
        boolean clearDirBeforeConvert = CLEAN;

        Arguments args = new Arguments(src, dst, scale, platform, outputCompressionMode,
                scaleMode,
                downScalingAlgorithm,
                upScalingAlgorithm,
                compressionQuality,
                1,
                skipExistingFiles,
                skipUpscaling,
                verboseLog,
                includeAndroidLdpiTvdpi,
                haltOnError,
                createMipMapInsteadOfDrawableDir,
                iosCreateImagesetFolders,
                enablePngCrush,
                enableMozJpeg,
                postConvertWebp,
                enableAntiAliasing,
                dryRun,
                keepUnoptimizedFilesPostProcessor,
                roundingHandler,
                false,
                clearDirBeforeConvert
        );

//        if (rawArgs.length < 1) {
//            new GUI().launchApp(rawArgs);
//            return;
//        }
//
//        Arguments args = CLIInterpreter.parse(analysisArgs.toArray(new String[0]));
//
//        if (args == null) {
//            return;
//        } else if (args == Arguments.START_GUI) {
//            System.out.println("start gui");
//            new GUI().launchApp(rawArgs);
//            return;
//        }

        System.out.println("start converting " + args.filesToProcess.size() + " files");

        new DConvert().execute(args, true, new DConvert.HandlerCallback() {
            @Override
            public void onProgress(float progress) {
                try {
                    System.out.write(MiscUtil.getCmdProgressBar(progress).getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinished(int finishedJobs, List<Exception> exceptions, long time, boolean haltedDuringProcess, String log) {
                System.out.print(MiscUtil.getCmdProgressBar(1f));

                System.out.println("");

                if (args.verboseLog) {
                    System.out.println("Log:");
                    System.out.println(log);
                }

                if (haltedDuringProcess) {
                    System.err.println("abort due to error");
                }
                if (exceptions.size() > 0) {
                    System.err.println("found " + exceptions.size() + " errors during execution");
                    if (args.verboseLog) {
                        for (Exception exception : exceptions) {
                            System.err.println("\terror: " + exception.getMessage());
                            exception.printStackTrace();
                        }
                    }
                }
                System.out.println("execution finished (" + time + "ms) with " + finishedJobs + " finsihed jobs and " + exceptions.size() + " errors");
            }
        });
    }

    private static RoundingHandler.Strategy getRoundingHandler(String roundingMode) {
        switch (roundingMode) {
            case "round":
                return RoundingHandler.Strategy.ROUND_HALF_UP;
            case "ceil":
                return RoundingHandler.Strategy.CEIL;
            case "floor":
                return RoundingHandler.Strategy.FLOOR;
            default:
                throw new RuntimeException("unknown mode: " + roundingMode);
        }
    }

    private static EScaleMode scaleMode(boolean dp, boolean scaleIsHeightDp) {
        if (dp && scaleIsHeightDp) {
            return EScaleMode.DP_HEIGHT;
        } else if (dp && !scaleIsHeightDp) {
            return EScaleMode.DP_WIDTH;
        } else {
            return EScaleMode.FACTOR;
        }
    }

    private static EOutputCompressionMode getOutCompression(String outCompression) {
        switch (outCompression) {
            case "strict":
                return EOutputCompressionMode.SAME_AS_INPUT_STRICT;
            case "png":
                return EOutputCompressionMode.AS_PNG;
            case "jpg":
                return EOutputCompressionMode.AS_JPG;
            case "gif":
                return EOutputCompressionMode.AS_GIF;
            case "bmp":
                return EOutputCompressionMode.AS_BMP;
            case "png+jpg":
                return EOutputCompressionMode.AS_JPG_AND_PNG;
            default:
                throw new RuntimeException("unknown compression type: " + outCompression);
        }
    }

    private static Set<EPlatform> getPlatform(String platform) {
        Set<EPlatform> platformSet = new HashSet<>();

        switch (platform) {
            case "all":
                platformSet = EPlatform.getAll();
                break;
            case "android":
                platformSet.add(EPlatform.ANDROID);
                break;
            case "ios":
                platformSet.add(EPlatform.IOS);
                break;
            case "win":
                platformSet.add(EPlatform.WINDOWS);
                break;
            case "web":
                platformSet.add(EPlatform.WEB);
                break;
            default:
                System.err.println("unknown mode: " + platform);
        }

        return platformSet;
    }

    private static boolean postProcess(boolean option) {
        return false;
    }

    private static String roundingMode(boolean option) {
        if (option) {
            return "ceil";
        }

        return "floor";
    }

    private static String downScalingAlgo(boolean option) {
        if (option) {
            return EScalingAlgorithm.LANCZOS3.getName();
        }

        return Arguments.DEFAULT_UPSCALING_QUALITY.getName();
    }

    private static String upScalingAlgo(boolean option) {
        if (option) {
            return EScalingAlgorithm.LANCZOS3.getName();
        }

        return Arguments.DEFAULT_DOWNSCALING_QUALITY.getName();
    }

    private static String platform(boolean option) {
        if (option) {
            return "ios";
        }

        return "android";
    }

    private static String outCompression(boolean option) {
        if (option) {
            return "png";
        }

        return "jpg";
    }

    private static float compressionQuality(boolean option) {
        if (option) {
            return 0.91f;
        }

        return Arguments.DEFAULT_COMPRESSION_QUALITY;
    }

    private static float scale(boolean option) {
        if (option) {
            return 1f;
        }

        return Arguments.DEFAULT_SCALE;
    }
}
