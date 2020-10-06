/*
 * Copyright (c) 2008, Harald Kuhr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.twelvemonkeys.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

/**
 * WindowsFileSystem
 * <p/>
 *
 * @author <a href="mailto:harald.kuhr@gmail.com">Harald Kuhr</a>
 * @version $Id: //depot/branches/personal/haraldk/twelvemonkeys/release-2/twelvemonkeys-core/src/main/java/com/twelvemonkeys/io/Win32FileSystem.java#2 $
 */
final class Win32FileSystem extends FileSystem {
    public long getFreeSpace(File pPath) {
        try {
            // Windows version
            // TODO: Test on W2K/95/98/etc... (tested on XP)
            BufferedReader reader = exec(new String[] {"CMD.EXE", "/C", "DIR", "/-C", pPath.getAbsolutePath()});

            String last = null;
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    last = line;
                }
            }
            finally {
                FileUtil.close(reader);
            }

            if (last != null) {
                int end = last.lastIndexOf(" bytes free");
                int start = last.lastIndexOf(' ', end - 1);

                if (start >= 0 && end >= 0) {
                    try {
                        return Long.parseLong(last.substring(start + 1, end));
                    }
                    catch (NumberFormatException ignore) {
                        // Ignore
                    }
                }
            }
        }
        catch (IOException ignore) {
            // Ignore
        }

        return 0l;
    }

    long getTotalSpace(File pPath) {
        // TODO: Implement, probably need some JNI stuff...
        // Distribute df.exe and execute from temp!? ;-)
        return getFreeSpace(pPath);
    }

    String getName() {
        return "Win32";
    }
}
