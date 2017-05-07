package gov.nasa.jpl.mbee.mdk.expression;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;

public class AutoCompleteJTextField implements DocumentListener {

    List<String> words;
    JTextField tf;

    private static final String COMMIT = "commit";

    private static enum Mode {INSERT, COMPLETION}

    ;
    private Mode mode = Mode.INSERT;

    public AutoCompleteJTextField(JTextField _tf, List<String> _words) {
        tf = _tf;
        words = _words;
        Collections.sort(words);

        tf.getDocument().addDocumentListener(this);
        tf.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), COMMIT);
        tf.getActionMap().put(COMMIT, new CommitAction());
    }

    private class CommitAction extends AbstractAction {

        private static final long serialVersionUID = -6003095633995129324L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (mode == Mode.COMPLETION) {
                int pos = tf.getSelectionEnd();
                StringBuffer s = new StringBuffer(tf.getText());
                s.insert(pos, " ");
                tf.setText(s.toString());
                tf.setCaretPosition(pos + 1);
                mode = Mode.INSERT;
            }
            else {
                tf.replaceSelection("\n");/////////////////////// ENTER
            }

        }
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        if (e.getLength() != 1) {
            return;
        }

        int pos = e.getOffset();
        String content = null;
        try {
            content = tf.getText(0, pos + 1);
        } catch (BadLocationException e1) {
        }

        //Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            if (!Character.isLetter(content.charAt(w))) {
                break;
            }
        }
        if (pos - w < 1) // At least 1 character few characters
        {
            return;
        }

        String prefix = content.substring(w + 1).toLowerCase();
        int n = Collections.binarySearch(words, prefix);
        if (n < 0 && -n <= words.size()) {
            String match = words.get(-n - 1);
            if (match.startsWith(prefix)) {
                String completion = match.substring(pos - w);
                SwingUtilities.invokeLater(
                        new CompletionTask(completion, pos + 1)
                );
            }
        }
    }

    private class CompletionTask implements Runnable {
        String completion;
        int position;

        CompletionTask(String _completion, int _position) {
            completion = _completion;
            position = _position;
        }

        @Override
        public void run() {
            StringBuffer s = new StringBuffer(tf.getText());
            s.insert(position, completion);
            tf.setText(s.toString());
            tf.setCaretPosition(position + completion.length());
            tf.moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }

    }
}
