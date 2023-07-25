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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import gui.common.AskingMode;
import gui.common.Chars;
import gui.common.GuiHelper;
import gui.state.CleanInitialState;
import gui.state.CleanSavedState;
import gui.state.DirtySavedState;
import gui.state.DirtyState;
import gui.state.DirtyUnsavedState;
import gui.state.FileState;
import gui.state.StateContext;

import words.WordPair;

public class MainShell implements StateContext {

  private FileState state;

  private Text txtForeign;

  private Text txtNative;

  private Table table;

  private Shell shell;

  private Label lblCount;

  private MenuItem shuffleItem;

  public MainShell() {
    Display display = new Display();
    shell = new Shell(display);

    // set layout
    shell.setLayout(new GridLayout());

    // add menu
    addMenu();

    // add editing-group
    Group editingGroup = new Group(shell, SWT.TITLE);
    editingGroup.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
    editingGroup.setText("Wortpaar bearbeiten");
    addEditingWidgets(editingGroup);

    // add overview-group
    Group overviewGroup = new Group(shell, SWT.TITLE);
    overviewGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    overviewGroup.setText("Wortpaare");
    addOverviewWidgets(overviewGroup);

    // icon
    GuiHelper.setIcon(shell, this.getClass());

    // size
    shell.pack();
    shell.setMinimumSize(shell.getSize());
    shell.setSize(500, 500);

    // count
    updateCounter();

    // initial state
    state = new CleanInitialState(this);

    shell.addShellListener(new ShellAdapter() {
      public void shellClosed(ShellEvent event) {
        handleDirtyEditor();
      }
    });

    // keep shell open
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    shell.dispose();
  }

  public void setState(FileState state) {
    this.state = state;
  }

  public Shell getShell() {
    return shell;
  }

  public void setDescription(String description) {
    shell.setText(description);
  }

  private void addMenu() {
    Menu menu = new Menu(shell, SWT.BAR);
    shell.setMenuBar(menu);

    // file
    MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
    fileItem.setText("&Datei");
    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
    fileItem.setMenu(fileMenu);

    MenuItem newItem = new MenuItem(fileMenu, SWT.PUSH);
    newItem.setText("Neu");
    newItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        newPressed();
      }
    });

    // separator
    new MenuItem(fileMenu, SWT.SEPARATOR);

    MenuItem openItem = new MenuItem(fileMenu, SWT.PUSH);
    openItem.setText(Chars.OUML_BIG + "ffnen");
    openItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        handleDirtyEditor();
        openFile();
      }
    });

    MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
    saveItem.setText("Speichern");
    saveItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        save();
      }
    });

    MenuItem saveAsItem = new MenuItem(fileMenu, SWT.PUSH);
    saveAsItem.setText("Speichern unter ...");
    saveAsItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        saveAs();
      }
    });

    // separator
    new MenuItem(fileMenu, SWT.SEPARATOR);

    // file - exit
    final MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
    exitItem.setText("Beenden");
    exitItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        shell.dispose();
      }
    });

    // ask
    MenuItem askItem = new MenuItem(menu, SWT.CASCADE);
    askItem.setText("&Abfragen");
    Menu askMenu = new Menu(shell, SWT.DROP_DOWN);
    askItem.setMenu(askMenu);

    // native - foreign
    final MenuItem nativeToForeignItem = new MenuItem(askMenu, SWT.PUSH);
    nativeToForeignItem.setText("Muttersprache - Fremdsprache");
    nativeToForeignItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        startAskingRun(AskingMode.NATIVE_TO_FOREIGN);
      }
    });

    // foreign - native
    final MenuItem foreignToNativeItem = new MenuItem(askMenu, SWT.PUSH);
    foreignToNativeItem.setText("Fremdsprache - Muttersprache");
    foreignToNativeItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        startAskingRun(AskingMode.FOREIGN_TO_NATIVE);
      }
    });

    // separator
    new MenuItem(askMenu, SWT.SEPARATOR);

    // shuffle
    shuffleItem = new MenuItem(askMenu, SWT.CHECK);
    shuffleItem.setText("Mischen");
    shuffleItem.setSelection(true);

    // help
    final MenuItem helpItem = new MenuItem(menu, SWT.CASCADE);
    helpItem.setText("&Hilfe");
    Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
    helpItem.setMenu(helpMenu);

    final MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
    aboutItem.setText(Chars.UUML_BIG + "ber JStreb0r");
    aboutItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        new AboutDialog(shell);
      }
    });
  }

  private void addEditingWidgets(Group editingGroup) {
    // layout
    editingGroup.setLayout(new GridLayout(5, false));

    // native word
    Label lblNative = new Label(editingGroup, SWT.NONE);
    lblNative.setText("Muttersprache");

    txtNative = new Text(editingGroup, SWT.SINGLE | SWT.BORDER);
    txtNative.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    txtNative.addKeyListener(new ReturnListener());

    // foreign word
    Label lblForeign = new Label(editingGroup, SWT.NONE);
    lblForeign.setText("Fremdsprache");

    txtForeign = new Text(editingGroup, SWT.SINGLE | SWT.BORDER);
    txtForeign.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    txtForeign.addKeyListener(new ReturnListener());

    // save-button
    Button btnSave = new Button(editingGroup, SWT.PUSH);
    btnSave.setText("&Speichern");
    btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false));
    btnSave.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        saveItem();
      }
    });
  }

  private void addOverviewWidgets(Group overviewGroup) {
    // layout
    overviewGroup.setLayout(new GridLayout(3, false));

    // table
    table = new Table(overviewGroup, SWT.BORDER | SWT.FULL_SELECTION
        | SWT.CHECK);
    table.setLinesVisible(true);
    table.setHeaderVisible(true);
    table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

    // columns
    final TableColumn colNative = new TableColumn(table, SWT.NONE);
    colNative.setText("Muttersprache");
    colNative.pack();

    final TableColumn colForeign = new TableColumn(table, SWT.NONE);
    colForeign.setText("Fremdsprache");
    colForeign.pack();

    lblCount = new Label(overviewGroup, SWT.NONE);
    lblCount.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false));

    // edit button
    final Button btnEdit = new Button(overviewGroup, SWT.PUSH);
    btnEdit.setText("&Bearbeiten");
    btnEdit.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, true, false));
    btnEdit.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        editItem();
      }
    });

    // delete button
    final Button btnDelete = new Button(overviewGroup, SWT.PUSH);
    btnDelete.setText("&L" + Chars.OUML_SMALL + "schen");
    btnDelete.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false));
    btnDelete.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        deleteItem();
      }
    });
  }

  private void saveItem() {
    boolean success = addWordPair();
    if (success) {
      txtNative.setText("");
      txtForeign.setText("");
      txtNative.forceFocus();
    }
  }

  private void deleteItem() {
    // delete item
    int selection = table.getSelectionIndex();
    if (selection != -1) {
      table.remove(selection);
      packTableColumns();
      updateCounter();

      if (state.getClass() == CleanSavedState.class) {
        state = new DirtySavedState(this, state.getFilename());
      }
    }
  }

  private void editItem() {
    // show selected item in editing-group and remove it from table
    int selection = table.getSelectionIndex();
    if (selection != -1) {
      TableItem item = table.getItem(selection);
      txtNative.setText(item.getText(0));
      txtForeign.setText(item.getText(1));
      table.remove(selection);
      updateCounter();
      txtNative.forceFocus();
    }
  }

  private void save() {
    // check if save possible
    if (state.getClass() == CleanInitialState.class) {
      saveNotPossibleError();
    } else {
      // save to file
      List<WordPair> list = fetchWordPairsFromTable();
      state.save(list);
    }
  }

  private void saveAs() {
    // check if save possible
    if (state.getClass() == CleanInitialState.class) {
      saveNotPossibleError();
    } else {
      // save to any file
      List<WordPair> list = fetchWordPairsFromTable();
      state.saveAs(list);
    }
  }

  private void saveNotPossibleError() {
    String title = "Fehler";
    String message = "Bitte erfassen Sie zumindest ein Wortpaar!";
    GuiHelper.showErrorMessage(shell, title, message);
  }

  private void handleDirtyEditor() {
    if (state instanceof DirtyState) {
      // ask for save, if data changed
      String title = "Daten geaendert";
      String question = "Die Daten wurden geaendert. Wollen Sie die "
          + Chars.AUML_BIG + "nderungen speichern?";
      int result = GuiHelper.showQuestionMessage(shell, title, question);
      if (result == SWT.YES) {
        save();
      }
    }
  }

  private void openFile() {
    // read choosen file
    List<WordPair> list = state.open(this);
    if (list != null) {
      showWordList(list);
    }
  }

  private void startAskingRun(AskingMode mode) {
    // get checked words from table
    List<WordPair> currentList = fetchWordPairsFromTable();

    // are there any active words?
    boolean hasActiveWords = false;
    for (WordPair pair : currentList) {
      if (pair.isAsk()) {
        hasActiveWords = true;
        break;
      }
    }

    // only ask, if there are any active words
    if (hasActiveWords) {
      table.setVisible(false);
      AskingRun run = new AskingRun(mode, currentList, shell, shuffleItem
          .getSelection());
      run.run();
      showWordList(run.getList());
      table.setVisible(true);
    } else {
      String title = "Fehler";
      String message = "Bitte w" + Chars.AUML_SMALL
          + "hlen Sie zumindest ein Wort zur Abfrage aus!";
      GuiHelper.showErrorMessage(shell, title, message);
    }
  }

  private void showWordList(List<WordPair> list) {
    // dispose items
    for (TableItem item : table.getItems()) {
      item.dispose();
    }

    // show new items
    for (WordPair pair : list) {
      addTableItem(pair);
    }
  }

  private List<WordPair> fetchWordPairsFromTable() {
    List<WordPair> list = new ArrayList<WordPair>();

    // map table into wordpairs and collect them in a list
    for (TableItem item : table.getItems()) {
      WordPair pair = new WordPair();
      pair.setAsk(item.getChecked());
      pair.setNativeWord(item.getText(0));
      pair.setForeignWord(item.getText(1));
      list.add(pair);
    }
    return list;
  }

  private boolean addWordPair() {
    boolean success = true;
    String nativeWord = txtNative.getText();
    String foreignWord = txtForeign.getText();

    // both textboxes must be filled
    if (!(nativeWord.equals("") || foreignWord.equals(""))) {
      // get pair from textboxes and add it to table
      WordPair pair = new WordPair();
      pair.setAsk(true);
      pair.setNativeWord(txtNative.getText());
      pair.setForeignWord(txtForeign.getText());
      addTableItem(pair);
      if (state instanceof CleanInitialState) {
        state = new DirtyUnsavedState(this);
      } else if (state instanceof CleanSavedState) {
        // TODO: is there a better solution?
        state = new DirtySavedState(this, state.getFilename());
      }
    } else {
      String title = "Fehler";
      String message = "Bitte f" + Chars.UUML_SMALL
          + "llen Sie alle Felder aus!";
      GuiHelper.showErrorMessage(shell, title, message);
      success = false;
    }
    return success;
  }

  private void addTableItem(WordPair pair) {
    // map wordpair to tableitem
    TableItem item = new TableItem(table, SWT.NONE);
    item.setChecked(pair.isAsk());
    item.setText(0, pair.getNativeWord());
    item.setText(1, pair.getForeignWord());

    packTableColumns();

    updateCounter();
  }

  private void updateCounter() {
    // shows count of wordpairs in a label
    int count = table.getItems().length;
    String caption = " Wortpaar";
    if (count > 1 || count == 0) {
      caption = caption.concat("e");
    }
    lblCount.setText(count + caption);
  }

  private void packTableColumns() {
    // pack columns
    for (TableColumn col : table.getColumns()) {
      col.pack();
    }
  }

  private void newPressed() {
    handleDirtyEditor();
    clearTable();
    state = new CleanInitialState(this);
  }

  private void clearTable() {
    // disposes all items
    for (TableItem item : table.getItems()) {
      item.dispose();
    }
    updateCounter();
  }

  private class ReturnListener extends KeyAdapter {

    public void keyPressed(KeyEvent event) {
      // pressed return/enter on a text-field
      if (event.character == SWT.CR) {
        if (event.getSource() == txtNative || event.getSource() == txtForeign) {
          saveItem();
        }
      }
    }

  }

}
