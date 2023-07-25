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

package gui.state;

import java.io.StringWriter;
import java.util.List;

import main.Properties;

import words.WordPair;

public class CleanInitialState extends FileState implements CleanState {

  private StateContext context;

  public CleanInitialState(StateContext context) {
    this.context = context;
    describe();
  }

  @Override
  public void save(List<WordPair> list) {
  }

  @Override
  public void saveAs(List<WordPair> list) {
  }

  @Override
  public String getFilename() {
    throw new UnsupportedOperationException("No filename in CleanInitialState");
  }

  @Override
  public void describe() {
    StringWriter title = getTitle();
    title.append(" - ");
    title.append(Properties.NEW_DESCRIPTION);
    context.setDescription(title.toString());
  }

}
