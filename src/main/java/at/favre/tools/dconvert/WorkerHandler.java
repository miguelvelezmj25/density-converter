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

import at.favre.tools.dconvert.arg.Arguments;
import at.favre.tools.dconvert.converters.IPlatformConverter;
import at.favre.tools.dconvert.converters.Result;
import at.favre.tools.dconvert.converters.postprocessing.IPostProcessor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** Handles post processing tasks */
public class WorkerHandler<T> {

  private final List<T> processors;
  private final ExecutorService threadPool;
  private final Arguments arguments;
  private final Callback callback;
  private int jobCount;

  public WorkerHandler(List<T> processors, Arguments arguments, Callback callback) {
    this.processors = processors;
    this.threadPool =
        new ThreadPoolExecutor(
            arguments.threadCount,
            arguments.threadCount,
            5,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(1024 * 10));
    this.callback = callback;
    this.arguments = arguments;
  }

  public void start(List<File> allFiles) {
    this.jobCount = allFiles.size() * processors.size();

    InternalCallback internalCallback = new InternalCallback(callback);

    //    for (T processor : processors) {
    //      for (File fileToProcess : allFiles) {
    ////        threadPool.execute(new Worker(fileToProcess, processor, arguments,
    // internalCallback));
    //        ((IPlatformConverter) processor).convert(fileToProcess, arguments);
    //      }
    //    }

    for (T processor : processors) {
      for (File fileToProcess : allFiles) {
        Result result = null;
        if (processor instanceof IPostProcessor) {
          result =
              ((IPostProcessor) processor)
                  .process(fileToProcess, arguments.keepUnoptimizedFilesPostProcessor);
        } else if (processor instanceof IPlatformConverter) {
          result = ((IPlatformConverter) processor).convert(fileToProcess, arguments);
        }
        internalCallback.onJobFinished(result);
      }
    }

//    threadPool.shutdown();

    if (jobCount == 0) {
      callback.onFinished(
          0, new ArrayList<File>(), new StringBuilder(), new ArrayList<Exception>(), false);
    }
  }

  public interface Callback {
    void onProgress(float percent);

    void onFinished(
        int finishedJobs,
        List<File> outFiles,
        StringBuilder log,
        List<Exception> exceptions,
        boolean haltedDuringProcess);
  }

  private class Worker implements Runnable {
    private final Arguments arguments;
    private final File unprocessedFile;
    private final T processor;
    private final InternalCallback callback;

    public Worker(
        File unprocessedFile, T processors, Arguments arguments, InternalCallback callback) {
      this.unprocessedFile = unprocessedFile;
      this.arguments = arguments;
      this.processor = processors;
      this.callback = callback;
    }

    @Override
    public void run() {
      Result result = null;
      if (processor instanceof IPostProcessor) {
        result =
            ((IPostProcessor) processor)
                .process(unprocessedFile, arguments.keepUnoptimizedFilesPostProcessor);
      } else if (processor instanceof IPlatformConverter) {
        result = ((IPlatformConverter) processor).convert(unprocessedFile, arguments);
      }
      callback.onJobFinished(result);
    }
  }

  private class InternalCallback {
    private final List<Exception> exceptionList = new ArrayList<Exception>();
    private final Callback callback;
    private final StringBuilder logBuilder = new StringBuilder();
    private final List<File> files = new ArrayList<File>();
    private int currentJobCount = 0;
    private boolean canceled = false;

    public InternalCallback(Callback callback) {
      this.callback = callback;
    }

    synchronized void onJobFinished(Result result) {
      if (!canceled) {
        currentJobCount++;

        if (result != null) {
          if (result.log != null && result.log.length() > 0) {
            logBuilder.append(result.log).append("\n");
          }
          if (result.processedFiles != null) {
            files.addAll(result.processedFiles);
          }
          if (result.exception != null) {
            exceptionList.add(result.exception);

            if (arguments.haltOnError) {
              canceled = true;
              threadPool.shutdownNow();
              callback.onFinished(currentJobCount, files, logBuilder, exceptionList, true);
            }
          }
        }

        if (!canceled) {
          if (currentJobCount == jobCount) {
            callback.onFinished(currentJobCount, files, logBuilder, exceptionList, false);
          } else {
            callback.onProgress((float) currentJobCount / (float) jobCount);
          }
        }
      }
    }
  }
}
