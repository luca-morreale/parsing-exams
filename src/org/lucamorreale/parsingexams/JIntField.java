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
    private static final long serialVersionUID = 3820227095170255379L;

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
        private static final long serialVersionUID = 6547426887571513113L;

        @Override
        public void insertString( int offs, String str, AttributeSet a )
                throws BadLocationException {

            if ( str == null ) {
                return;
            }

            String formattedString = str.replaceAll("[^0-9]", "");
            formattedString = formattedString.trim();
            super.insertString( offs, formattedString, a );
        }
    }
}