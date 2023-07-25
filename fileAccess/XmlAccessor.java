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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import main.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import words.WordPair;

public final class XmlAccessor {

  private XmlAccessor() {
  }

  public static void save(List<WordPair> list, String path) {
    XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
    Document doc = new Document();
    doc.setRootElement(new Element(Properties.WORDLIST_TAG));
    File file = new File(path);
    try {
      for (WordPair pair : list) {

        Element element = new Element(Properties.WORDPAIR_TAG);

        Element nativeElement = new Element(Properties.NATIVE_TAG);
        Element foreignElement = new Element(Properties.FOREIGN_TAG);

        nativeElement.setText(pair.getNativeWord());
        foreignElement.setText(pair.getForeignWord());

        element.addContent(nativeElement);
        element.addContent(foreignElement);

        doc.getRootElement().addContent(element);
      }
      outputter.output(doc, new FileOutputStream(file));
    } catch (FileNotFoundException fnfEx) {
      fnfEx.printStackTrace();
    } catch (IOException ioEx) {
      ioEx.printStackTrace();
    }
  }

  public static List<WordPair> read(String path) {
    SAXBuilder builder = new SAXBuilder();
    List<WordPair> list = new ArrayList<WordPair>();
    try {
      Document doc = builder.build(new File(path));
      Iterator words = doc.getRootElement().getChildren().iterator();
      while (words.hasNext()) {
        Element element = (Element) words.next();
        WordPair pair = new WordPair();
        pair.setNativeWord(element.getChildText(Properties.NATIVE_TAG));
        pair.setForeignWord(element.getChildText(Properties.FOREIGN_TAG));
        pair.setAsk(true);
        list.add(pair);
      }
    } catch (JDOMException jdomEx) {
      jdomEx.printStackTrace();
    } catch (IOException ioEx) {
      ioEx.printStackTrace();
    }
    return list;
  }
}
