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

import main.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import fileAccess.FileAccessor;
import gui.common.Chars;
import gui.common.GuiHelper;

public class AboutDialog {

  private Shell aboutShell;

  private Button btnClose;

  public AboutDialog(Shell parentShell) {
    // create shell
    aboutShell = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.RESIZE);
    aboutShell.setLayout(new GridLayout(1, false));

    // icon
    GuiHelper.setIcon(aboutShell, this.getClass());

    // add widgets
    addTab();
    addCloseButton();

    // set some shell properties
    aboutShell.setText(Chars.UUML_BIG + "ber JStreb0r");
    aboutShell.setSize(400, 400);

    // keep shell open
    aboutShell.open();
    while (!aboutShell.isDisposed()) {
      if (!aboutShell.getDisplay().readAndDispatch()) {
        aboutShell.getDisplay().sleep();
      }
    }
  }

  private void addTab() {
    // folder
    TabFolder folder = new TabFolder(aboutShell, SWT.NONE);
    folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    // info
    TabItem infoTab = new TabItem(folder, SWT.NONE);
    infoTab.setText("Info");
    Composite infoComp = new Composite(folder, SWT.NONE);
    infoTab.setControl(infoComp);
    addInfoWidgets(infoComp);

    // license
    TabItem licenseTab = new TabItem(folder, SWT.NONE);
    licenseTab.setText("Lizenz");
    Composite licenseComp = new Composite(folder, SWT.NONE);
    licenseTab.setControl(licenseComp);
    addLicenseWidgets(licenseComp);

    folder.pack();
  }

  private void addCloseButton() {
    // close-button
    btnClose = new Button(aboutShell, SWT.PUSH);
    btnClose.setText("&Schliessen");
    btnClose.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false));
    btnClose.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        aboutShell.dispose();
      }
    });
  }

  private void addInfoWidgets(Composite comp) {
    // layout
    comp.setLayout(new GridLayout(1, true));

    // title
    Label lblTitle = new Label(comp, SWT.NONE);
    lblTitle.setText(Properties.APP_NAME + " - " + Properties.APP_DESC);

    // version
    Label lblVersion;
    lblVersion = new Label(comp, SWT.NONE);
    lblVersion.setText("Version: " + Properties.VERSION + " ");

    // credits
    Label lblCredits = new Label(comp, SWT.NONE);
    lblCredits.setText("Credits:");
    Text txtCredits = new Text(comp, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
    txtCredits.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    txtCredits.setEditable(false);

    // load credits into textfield
    String credits = FileAccessor.readInternally(getClass(),
        Properties.CREDITS_PATH);
    txtCredits.setText(credits);
  }

  private void addLicenseWidgets(Composite comp) {
    // create one big textfield with scrollbars
    comp.setLayout(new GridLayout(1, true));
    Text txtLicense = new Text(comp, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL
        | SWT.V_SCROLL);
    txtLicense.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    txtLicense.setEditable(false);

    // load license into textfield
    String license = FileAccessor.readInternally(getClass(),
        Properties.LICENSE_PATH);
    txtLicense.setText(license);
  }
}
