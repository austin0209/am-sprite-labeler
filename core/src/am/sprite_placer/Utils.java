package am.sprite_placer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Rectangle;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class Utils {

    private static boolean gettingTextInput;
    private static String spriteSheetPath;

    private Utils() { }

    public static boolean isGettingTextInput() {
        return gettingTextInput;
    }

    public static void getTextInput (final Input.TextInputListener listener, final String title, final String text, final String hint) {
        if (!gettingTextInput) {
            gettingTextInput = true;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JPanel panel = new JPanel(new FlowLayout());

                    JPanel textPanel = new JPanel() {
                        public boolean isOptimizedDrawingEnabled() {
                            return false;
                        }
                    };

                    textPanel.setLayout(new OverlayLayout(textPanel));
                    panel.add(textPanel);

                    final JTextField textField = new JTextField(20);
                    textField.setText(text);
                    textField.setAlignmentX(0.0f);
                    textPanel.add(textField);

                    final JLabel placeholderLabel = new JLabel(hint);
                    placeholderLabel.setForeground(Color.GRAY);
                    placeholderLabel.setAlignmentX(0.0f);
                    textPanel.add(placeholderLabel, 0);

                    textField.getDocument().addDocumentListener(new DocumentListener() {

                        @Override
                        public void removeUpdate(DocumentEvent arg0) {
                            this.updated();
                        }

                        @Override
                        public void insertUpdate(DocumentEvent arg0) {
                            this.updated();
                        }

                        @Override
                        public void changedUpdate(DocumentEvent arg0) {
                            this.updated();
                        }

                        private void updated() {
                            if (textField.getText().length() == 0)
                                placeholderLabel.setVisible(true);
                            else
                                placeholderLabel.setVisible(false);
                        }
                    });

                    JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null,
                            null);

                    pane.setInitialValue(null);
                    pane.setComponentOrientation(JOptionPane.getRootFrame().getComponentOrientation());

                    Border border = textField.getBorder();
                    placeholderLabel.setBorder(new EmptyBorder(border.getBorderInsets(textField)));

                    JDialog dialog = pane.createDialog(null, title);
                    dialog.setAlwaysOnTop(true);
                    pane.selectInitialValue();
                    dialog.addWindowFocusListener(new WindowFocusListener() {

                        @Override
                        public void windowLostFocus(WindowEvent arg0) {
                        }

                        @Override
                        public void windowGainedFocus(WindowEvent arg0) {
                            textField.requestFocusInWindow();
                        }
                    });

                    dialog.setVisible(true);
                    dialog.dispose();

                    Object selectedValue = pane.getValue();

                    if (selectedValue != null && (selectedValue instanceof Integer)
                            && ((Integer) selectedValue).intValue() == JOptionPane.OK_OPTION) {
                        listener.input(textField.getText());
                    } else {
                        listener.canceled();
                    }
                    gettingTextInput = false;
                }
            });
        }
    }

    public static void openFileBrowser() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final JFileChooser fc = new JFileChooser() {
                    // "hacky" solution to keep file chooser on top
                    @Override
                    protected JDialog createDialog(Component parent) {
                        JDialog dialog = super.createDialog(parent);
                        dialog.setAlwaysOnTop(true);
                        return dialog;
                    }
                };
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == 0) {
                    spriteSheetPath = fc.getSelectedFile().getAbsolutePath();
                }
            }
        });
    }

    public static String getSpritePath() {
        return spriteSheetPath;
    }

    public static void resetSpritePath() { spriteSheetPath = null; }
  
    public static Vector2 getMousePos(Viewport vp) {
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        vp.unproject(mousePos);
        return mousePos;
    }

    public static boolean isPointInsideRectangle(Vector2 p, com.badlogic.gdx.math.Rectangle r) {
        return p.x >= r.x && p.x <= r.x + r.width
                && p.y >= r.y && p.y <= r.y + r.height;
    }
}
