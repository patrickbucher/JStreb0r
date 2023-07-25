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

package gui;

import java.text.DecimalFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ResultDialog {

  private Shell shell;

  public ResultDialog(Shell parent, int asked, int correct) {
    this.shell = new Shell(parent, SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM);
    shell.setText("Ergebnis");

    // layout
    shell.setLayout(new GridLayout(3, false));
    GridData numData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);

    // asked
    Label lblAsked = new Label(shell, SWT.LEFT);
    lblAsked.setText("Wortpaare abgefragt:");
    Label lblAskedNum = new Label(shell, SWT.RIGHT);
    lblAskedNum.setLayoutData(numData);
    lblAskedNum.setText(String.valueOf(asked));

    // correct
    Label lblCorrect = new Label(shell, SWT.NONE);
    lblCorrect.setText("Korrekt beantwortet:");
    Label lblCorrectNum = new Label(shell, SWT.NONE);
    lblCorrectNum.setLayoutData(numData);
    lblCorrectNum.setText(String.valueOf(correct));

    // wrong
    Label lblWrong = new Label(shell, SWT.LEFT);
    lblWrong.setText("Falsch beantwortet:");
    Label lblWrongNum = new Label(shell, SWT.NONE);
    lblWrongNum.setLayoutData(numData);
    lblWrongNum.setText(String.valueOf(asked - correct));

    // percentage
    Label lblPer = new Label(shell, SWT.NONE);
    lblPer.setText("Quote:");
    Label lblPerNum = new Label(shell, SWT.NONE);
    lblPerNum.setText(computePercentage(asked, correct));
    lblPerNum.setAlignment(SWT.RIGHT);
    Label lblPerSign = new Label(shell, SWT.NONE);
    lblPerSign.setText("%");

    // ok-button
    Button btnOK = new Button(shell, SWT.PUSH);
    btnOK.setText("OK");
    btnOK
        .setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 3, 1));
    btnOK.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        shell.dispose();
      }
    });

    // keep shell open
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!shell.getDisplay().readAndDispatch()) {
        shell.getDisplay().sleep();
      }
    }
  }

  private String computePercentage(int asked, int correct) {
    DecimalFormat formatter = new DecimalFormat("###.##");
    float percent = (float) correct / asked * 100;
    String strPercent = formatter.format(percent);
    return strPercent;
  }

}
