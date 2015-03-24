/**
 * A JTextField that accepts only integers.
 *
 * @author Luca Morreale
 */

package org.lucamorreale.parsingexams;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JIntField extends JTextField {

    public JIntField() {
        super();
    }

    public JIntField(int cols) {
        super(cols);
    }

    @Override
    protected Document createDefaultModel() {
        return new IntegerDocument();
    }

    static class IntegerDocument extends PlainDocument {

        @Override
        public void insertString( int offs, String str, AttributeSet a )
                throws BadLocationException {

            if ( str == null ) {
                return;
            }

            str = str.replaceAll("[^0-9]", "");
            str = str.trim();
            super.insertString( offs, str, a );
        }
    }
}