/*
 JStreb0r - a tool to learn vocabulary
 Copyright (C) 2006-2007  Patrick Bucher

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along
 with this program; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package fileAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileAccessor {

  public static String readInternally(Class clazz, String ressource) {
    String content = "", line = "";

    // get stream on ressource
    InputStream input = clazz.getResourceAsStream(ressource);
    Reader reader = new InputStreamReader(input);
    BufferedReader bufferedReader = new BufferedReader(reader);

    // read file
    boolean eof = false;
    while (!eof) {
      try {
        line = bufferedReader.readLine();
      } catch (IOException ioEx) {
        eof = true;
      }
      if (line == null) {
        eof = true;
      } else {
        content = content.concat(line.concat("\n"));
      }
    }

    return content;
  }

  public static boolean exists(String path) {
    return new File(path).exists();
  }

  public static String getName(String path) {
    return new File(path).getName();
  }
}
