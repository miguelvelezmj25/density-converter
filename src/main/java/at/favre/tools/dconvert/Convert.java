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
    EScalingAlgorithm downScalingAlgorithm = EScalingAlgorithm.MITCHELL;
    boolean dryRun = false;
    boolean haltOnError = false;
    boolean iosCreateImagesetFolders = false;
    boolean keepOriginalPostProcessedFiles = true;
    String outCompression = "jpg";
    String selectedPlatform = "android";
    boolean postProcessorMozJpeg = false;
    boolean postProcessorPngCrush = false;
    boolean postProcessorWebp = false;
    String roundingMode = "floor";
    float scale = 1;
    boolean scaleIsHeightDp = false;
    boolean skipExisting = true;
    boolean skipUpscaling = false;
    EScalingAlgorithm upScalingAlgorithm = EScalingAlgorithm.MITCHELL;
    boolean VERBOSE = true;

    File src = new File("./pictures/person.jpg");
    File dst = new File("./output");
    Set<EPlatform> platform = getPlatform(selectedPlatform);
    EOutputCompressionMode outputCompressionMode = getOutCompression(outCompression);
    EScaleMode scaleMode = scaleMode(false, scaleIsHeightDp);
    RoundingHandler.Strategy roundingHandler = getRoundingHandler(roundingMode);

    final Arguments args =
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
    if (roundingMode.equals("round")) {
      return RoundingHandler.Strategy.ROUND_HALF_UP;
    }
    if (roundingMode.equals("ceil")) {
      return RoundingHandler.Strategy.CEIL;
    }
    if (roundingMode.equals("floor")) {
      return RoundingHandler.Strategy.FLOOR;
    }
    throw new RuntimeException("unknown mode: " + roundingMode);
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
    if (outCompression.equals("strict")) {
      return EOutputCompressionMode.SAME_AS_INPUT_STRICT;
    }
    if (outCompression.equals("png")) {
      return EOutputCompressionMode.AS_PNG;
    }
    if (outCompression.equals("jpg")) {
      return EOutputCompressionMode.AS_JPG;
    }
    if (outCompression.equals("gif")) {
      return EOutputCompressionMode.AS_GIF;
    }
    if (outCompression.equals("bmp")) {
      return EOutputCompressionMode.AS_BMP;
    }
    if (outCompression.equals("png+jpg")) {
      return EOutputCompressionMode.AS_JPG_AND_PNG;
    }

    throw new RuntimeException("unknown compression type: " + outCompression);
  }

  private static Set<EPlatform> getPlatform(String platform) {
    Set<EPlatform> platformSet = new HashSet<EPlatform>();

    if (platform.equals("all")) {
      platformSet = EPlatform.getAll();
    } else if (platform.equals("android")) {
      platformSet.add(EPlatform.ANDROID);
    } else if (platform.equals("ios")) {
      platformSet.add(EPlatform.IOS);
    } else if (platform.equals("win")) {
      platformSet.add(EPlatform.WINDOWS);
    } else if (platform.equals("web")) {
      platformSet.add(EPlatform.WEB);
    } else {
      System.err.println("unknown mode: " + platform);
    }

    return platformSet;
  }

  private static boolean postProcess(boolean option) {
    return false;
  }
}
