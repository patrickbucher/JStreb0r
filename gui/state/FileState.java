/*
 JStreb0r - a tool to learn vocabulary
 Copyright (C) 2008  Patrick Bucher

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

package gui.state;

import fileAccess.FileHandler;

import java.io.StringWriter;
import java.util.List;

import main.Properties;

import words.WordPair;

public abstract class FileState {

  /**
   * Saves the data to a file that will be specified by the user.
   * 
   * @param list -
   *          the list to be saved
   * @return String - the filename which was used to save the file
   */
  public abstract void saveAs(List<WordPair> list);

  /**
   * Saves the data to that file which was used the last time. If no file was
   * used yet, saveAs should be called internally.
   * 
   * @param list -
   *          the list to be saved
   * @return String - the filename which was used to save the file
   */
  public abstract void save(List<WordPair> list);

  /**
   * Opens an existing WordList from a file. An open-operation always leads to a
   * CleanSavedState.
   */
  public List<WordPair> open(StateContext shell) {
    List<WordPair> pairs = FileHandler.open(shell.getShell());
    String filename = FileHandler.getOpenedFilename();
    shell.setState(new CleanSavedState(shell, filename));
    return pairs;
  }

  public abstract String getFilename();

  public abstract void describe();

  protected StringWriter getTitle() {
    StringWriter title = new StringWriter();
    title.write(Properties.APP_NAME);
    title.write(' ');
    title.write(String.valueOf(Properties.VERSION));

    return title;
  }
}
