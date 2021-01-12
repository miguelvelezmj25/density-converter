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

/** Entry point of the app. Use arg -h to get help. */
public final class Convert {

  private Convert() {}

  public static void main(String[] rawArgs) {
    boolean androidIncludeLdpiTvdpi = false;
    boolean androidMipmapInsteadOfDrawable = false;
    boolean antiAliasing = false;
    boolean clean = true;
    float compressionQuality = 0.9f;
    String downScalingAlgo = EScalingAlgorithm.MITCHELL.getName();
    boolean dryRun = false;
    boolean haltOnError = false;
    boolean iosCreateImagesetFolders = false;
    boolean keepOriginalPostProcessedFiles = true;
    String outCompression = "jpg";
    String selectedPlatform = "android";
    boolean postProcessorMozJpeg = true;
    boolean postProcessorPngCrush = false;
    boolean postProcessorWebp = false;
    String roundingMode = "floor";
    float scale = 1;
    boolean scaleIsHeightDp = false;
    boolean skipExisting = true;
    boolean skipUpscaling = false;
    String upScalingAlgo = EScalingAlgorithm.BILINEAR.getName();
    boolean VERBOSE = true;

    File src = new File("./pictures/person.jpg");
    File dst = new File("./output");
    Set<EPlatform> platform = getPlatform(selectedPlatform);
    EOutputCompressionMode outputCompressionMode = getOutCompression(outCompression);
    EScaleMode scaleMode = scaleMode(false, scaleIsHeightDp);
    EScalingAlgorithm downScalingAlgorithm = EScalingAlgorithm.getByName(downScalingAlgo);
    EScalingAlgorithm upScalingAlgorithm = EScalingAlgorithm.getByName(upScalingAlgo);
    RoundingHandler.Strategy roundingHandler = getRoundingHandler(roundingMode);

    Arguments args =
        new Arguments(
            src,
            dst,
            scale,
            platform,
            outputCompressionMode,
            scaleMode,
            downScalingAlgorithm,
            upScalingAlgorithm,
            compressionQuality,
            1,
            skipExisting,
            skipUpscaling,
            VERBOSE,
            androidIncludeLdpiTvdpi,
            haltOnError,
            androidMipmapInsteadOfDrawable,
            iosCreateImagesetFolders,
            postProcessorPngCrush,
            postProcessorMozJpeg,
            postProcessorWebp,
            antiAliasing,
            dryRun,
            keepOriginalPostProcessedFiles,
            roundingHandler,
            false,
            clean);

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

    new DConvert()
        .execute(
            args,
            true,
            new DConvert.HandlerCallback() {
              @Override
              public void onProgress(float progress) {
                try {
                  System.out.write(MiscUtil.getCmdProgressBar(progress).getBytes());
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }

              @Override
              public void onFinished(
                  int finishedJobs,
                  List<Exception> exceptions,
                  long time,
                  boolean haltedDuringProcess,
                  String log) {
                System.out.print(MiscUtil.getCmdProgressBar(1f));

                System.out.println();

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
                System.out.println(
                    "execution finished ("
                        + time
                        + "ms) with "
                        + finishedJobs
                        + " finsihed jobs and "
                        + exceptions.size()
                        + " errors");
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
}
