/**
 * A JTextField that does not accepts number.
 *
 * @author Luca Morreale
 */

package org.lucamorreale.parsingexams;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class JLetterField extends JTextField {

    public JLetterField() {
        super();
    }

    public JLetterField(int cols) {
        super(cols);
    }

    @Override
    protected Document createDefaultModel() {
        return new LetterDocument();
    }

    static class LetterDocument extends PlainDocument {

        @Override
        public void insertString( int offs, String str, AttributeSet a )
                throws BadLocationException {

            if ( str == null ) {
                return;
            }

            str = str.replaceAll("[0-9]", "");
            super.insertString( offs, str, a );
        }
    }
}