/**
 *
 */
package org.lucamorreale.parsingexams;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author Luca Morreale
 *
 */
public class CustomFileFilter extends FileFilter{

    @Override
    public boolean accept(File file) {

        return file.isDirectory() || file.getAbsolutePath().endsWith(".txt") || file.getAbsolutePath().endsWith(".pdf");
    }

    @Override
    public String getDescription() {

        return ".txt or .pdf";
    }
}
