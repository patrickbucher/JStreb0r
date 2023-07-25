/*
 JStreb0r - a tool to learn vocabulary
 Copyright (C) 2007  Patrick Bucher

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

import java.util.List;

import main.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import words.WordPair;
import gui.common.Chars;
import gui.common.GuiHelper;

public class FileHandler {

  /** the filename of the latest file which was opened */
  private static String openedFile;

  public static String save(Shell parent, List<WordPair> wordList,
      String filename) {
    XmlAccessor.save(wordList, filename);
    return filename;
  }

  public static String save(Shell parent, List<WordPair> wordList) {
    String filename = getSavePath(parent);
    XmlAccessor.save(wordList, filename);
    return filename;
  }

  public static List<WordPair> open(Shell parent) {
    String filename = getOpenPath(parent);
    openedFile = filename;
    return XmlAccessor.read(filename);
  }

  public static String getOpenedFilename() {
    return openedFile;
  }

  private static String getSavePath(Shell shell) {

    // ask user for filename
    FileDialog dialog = new FileDialog(shell, SWT.SAVE);
    dialog.setText("Speichern unter ...");
    dialog.setFilterExtensions(new String[] { "*." + Properties.EXTENSION });
    String path = dialog.open();

    // path is null if user cancelled
    if (path != null) {
      // file extension
      path = addExtension(path);

      // overwrite file?
      if (FileAccessor.exists(path)) {
        String title = "Datei " + Chars.UUML_SMALL + "berschreiben?";
        String file = FileAccessor.getName(path);
        String question = "Wollen Sie die Datei '" + file + "' wirklich "
            + Chars.UUML_SMALL + "berschreiben?";
        int result = GuiHelper.showQuestionMessage(shell, title, question);
        if (result != SWT.YES) {
          path = "";
        }
      }
    }

    return path;
  }

  private static String getOpenPath(Shell shell) {

    // open dialog
    FileDialog dialog = new FileDialog(shell, SWT.OPEN);
    dialog.setFilterExtensions(new String[] { "*." + Properties.EXTENSION });
    dialog.setText(Chars.OUML_BIG + "ffnen");
    String path = dialog.open();

    // path is null if user cancelled
    if (path == null) {
      path = "";
    }
    return path;
  }

  private static String addExtension(String path) {
    // add file extension (if not specified)
    if (!path.endsWith(".") && !path.endsWith("." + Properties.EXTENSION)) {
      path = path.concat(".");
    }
    if (!path.endsWith(Properties.EXTENSION)) {
      path = path.concat(Properties.EXTENSION);
    }
    return path;
  }

}
