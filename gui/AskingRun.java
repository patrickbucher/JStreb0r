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

package gui;

import gui.common.AskingMode;
import gui.common.GuiHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import main.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import words.SequentialChecker;
import words.WordPair;
import words.WordPairChecker;

public class AskingRun implements SelectionListener, KeyListener {

  private AskingMode mode;

  private List<WordPair> pairs;

  private Iterator<WordPair> iterator;

  private boolean shuffle;

  private WordPair currentPair;

  private WordPairChecker checker;

  private Shell parentShell;

  private Shell askShell;

  private Text txtFrom;

  private Text txtTo;

  private Button btnOk;

  private Button btnCancel;

  private int correct;

  private int asked;

  private ProgressBar bar;

  private boolean cancelled = false;

  public AskingRun(AskingMode mode, List<WordPair> list, Shell shell,
      boolean shuffle) {
    this.mode = mode;
    this.pairs = list;
    this.iterator = list.iterator();
    this.parentShell = shell;
    this.shuffle = shuffle;

    // clone list for the checker
    List<WordPair> clone = new ArrayList<WordPair>(list.size());
    clone.addAll(list);
    this.checker = new SequentialChecker(clone);
  }

  public void run() {
    // create & open shell
    askShell = createShell();

    // shuffle pair-order if neccessary
    if (shuffle) {
      Collections.shuffle(pairs);
    }

    // ask first pair
    askPair();

    // show result
    new ResultDialog(parentShell, asked, correct);
  }

  public void widgetDefaultSelected(SelectionEvent event) {
  }

  public void widgetSelected(SelectionEvent event) {
    if (event.getSource() == btnOk) {
      pressedOk();
    } else if (event.getSource() == btnCancel) {
      pressedCancel();
    }
  }

  public void keyReleased(KeyEvent event) {
    if (event.getSource() == txtTo) {
      if (event.character == SWT.CR) {
        pressedOk();
      }
    }
  }

  public void keyPressed(KeyEvent event) {
  }

  public List<WordPair> getList() {
    return pairs;
  }

  private Shell createShell() {
    // shell & layout
    Shell askShell = new Shell(parentShell, SWT.APPLICATION_MODAL
        | SWT.DIALOG_TRIM | SWT.RESIZE);
    askShell.setLayout(new GridLayout(3, false));

    // icon
    GuiHelper.setIcon(askShell, this.getClass());

    // status bar
    bar = new ProgressBar(askShell, SWT.NONE);
    bar.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 3, 1));
    bar.setMaximum(getAskingWordCount());

    // captions
    String lblTopCaption, lblCenterCaption, title;
    if (mode == AskingMode.NATIVE_TO_FOREIGN) {
      lblTopCaption = "Muttersprache";
      lblCenterCaption = "Fremdsprache";
      title = "Muttersprache - Fremdsprache";
    } else {
      lblTopCaption = "Fremdsprache";
      lblCenterCaption = "Muttersprache";
      title = "Fremdsprache - Muttersprache";
    }

    // "from" controls (label & text)
    Label lblFrom = new Label(askShell, SWT.NONE);
    lblFrom.setText(lblTopCaption);

    txtFrom = new Text(askShell, SWT.SINGLE | SWT.BORDER);
    txtFrom.setEditable(false);
    txtFrom.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));

    // "to" controls (label & text)
    Label lblTo = new Label(askShell, SWT.NONE);
    lblTo.setText(lblCenterCaption);

    txtTo = new Text(askShell, SWT.SINGLE | SWT.BORDER);
    txtTo.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 2, 1));
    txtTo.addKeyListener(this);

    // OK-button
    btnOk = new Button(askShell, SWT.PUSH);
    btnOk.setText("OK");
    btnOk.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, true, 2, 1));
    btnOk.addSelectionListener(this);

    // cancel-button
    btnCancel = new Button(askShell, SWT.PUSH);
    btnCancel.setText("Abbrechen");
    btnCancel.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, false, true));
    btnCancel.addSelectionListener(this);

    askShell.setText(title);
    return askShell;
  }

  private void askPair() {
    // get next pair
    while (iterator.hasNext() && !cancelled) {
      currentPair = iterator.next();

      if (currentPair.isAsk()) {
        // ask
        if (mode == AskingMode.NATIVE_TO_FOREIGN) {
          txtFrom.setText(currentPair.getNativeWord());
        } else {
          txtFrom.setText(currentPair.getForeignWord());
        }

        // show shell
        if (!askShell.isVisible()) {
          askShell.pack();
          askShell.setMinimumSize(askShell.getSize().x + 80,
              askShell.getSize().y);
          askShell.open();
        }
        askShell.pack();

        // widgets could be disposed now
        if (!cancelled) {
          txtTo.setText("");
          txtTo.forceFocus();
          asked++;
        }

        // wait for events
        while (!askShell.isDisposed()) {
          if (!askShell.getDisplay().readAndDispatch()) {
            askShell.getDisplay().sleep();
          }
        }
      } else {
        // if pair is inactive, ask the next one
        askPair();
      }
    }
    askShell.dispose();
  }

  private void pressedOk() {
    // get native and foreign word according to asking mode
    String nativeWord, foreignWord;
    if (mode == AskingMode.NATIVE_TO_FOREIGN) {
      nativeWord = txtFrom.getText();
      foreignWord = txtTo.getText();
    } else {
      nativeWord = txtTo.getText();
      foreignWord = txtFrom.getText();
    }
    WordPair check = new WordPair(nativeWord, foreignWord);

    if (checker.isCorrect(check)) {
      correct++;
      currentPair.setAsk(false);
      feedback(true);
    } else {
      feedback(false);
    }

    updateProgress();

    // next one
    askPair();
  }

  private void pressedCancel() {
    // cancel run
    cancelled = true;
    askShell.dispose();
  }

  private int getAskingWordCount() {
    return pairs.size();
  }

  private void updateProgress() {
    // update progress bar
    bar.setSelection(asked);
  }

  private Color getColor(int red, int green, int blue) {
    RGB rgb = new RGB(red, green, blue);
    Color color = new Color(askShell.getDisplay(), rgb);
    return color;
  }

  private void feedback(boolean correct) {
    // get color
    Color standardBack = txtTo.getBackground();
    Color standardFore = txtTo.getForeground();
    Color indicator;
    if (correct) {
      // green
      indicator = getColor(0, 255, 0);
    } else {
      // red
      indicator = getColor(255, 0, 0);
      // show right solution white
      Color white = getColor(255, 255, 255);
      txtTo.setForeground(white);
    }

    // set background-color
    txtTo.setBackground(indicator);
    txtTo.update();
    askShell.update();

    // wait a tick before resetting color
    try {
      Thread.sleep(Properties.DELAY_TIME);
    } catch (InterruptedException iEx) {
    }
    txtTo.setBackground(standardBack);
    txtTo.setForeground(standardFore);
  }
}
