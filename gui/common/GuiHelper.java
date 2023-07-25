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

package gui.common;

import java.io.InputStream;

import main.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public final class GuiHelper {

  private GuiHelper() {
  }

  public static void showInfoMessage(Shell shell, String title, String message) {
    // shows an info-messagebox
    MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
    box.setText(title);
    box.setMessage(message);
    box.open();
  }

  public static void showErrorMessage(Shell shell, String title, String message) {
    // shows an error-messagebox
    MessageBox box = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
    box.setText(title);
    box.setMessage(message);
    box.open();
  }

  public static int showQuestionMessage(Shell shell, String title,
      String question) {
    // shows a question-messagebox
    MessageBox box = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
    box.setText(title);
    box.setMessage(question);
    int result = box.open();
    return result;
  }

  public static void setIcon(Shell shell, Class clazz) {
    // icon
    InputStream input = clazz.getResourceAsStream(Properties.ICON_PATH);
    shell.setImage(new Image(shell.getDisplay(), input));
  }

}
