package frontend.gui;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TextComponentFocusBehaviour {

    public static void setTextComponentFocusBehaviour (JTextComponent component,  String placeHolder) {

        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (component.getText().equals(placeHolder)) {
                    component.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (component.getText().isEmpty()) {
                    component.setText(placeHolder);
                }
            }
        });
    }
}
