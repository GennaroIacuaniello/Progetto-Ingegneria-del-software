package frontend.gui;

import javax.swing.text.JTextComponent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Classe di utilità per la gestione del comportamento dei campi di testo (Focus).
 * <p>
 * Swing non supporta nativamente il concetto di "Placeholder" (o Ghost Text) come HTML5 o JavaFX.
 * Questa classe colma questa lacuna fornendo un metodo statico per simulare questo comportamento.
 * Gestisce gli eventi di acquisizione e perdita del focus per mostrare o nascondere
 * un testo guida quando il campo è vuoto.
 * </p>
 */
public class TextComponentFocusBehaviour {

    /**
     * Costruttore privato.
     */
    private TextComponentFocusBehaviour() {

    }
    /**
     * Applica il comportamento di "Placeholder" a un componente di testo.
     * <p>
     * Aggiunge un {@link java.awt.event.FocusListener} al componente specificato per gestire due stati:
     * <ul>
     * <li><b>Focus Gained (Click nel campo):</b> Se il testo attuale corrisponde esattamente al placeholder,
     * il campo viene svuotato per permettere all'utente di scrivere.</li>
     * <li><b>Focus Lost (Click fuori dal campo):</b> Se l'utente non ha scritto nulla (il campo è vuoto),
     * viene reinserito il testo del placeholder.</li>
     * </ul>
     * Questo metodo accetta {@link JTextComponent}, quindi funziona sia per {@code JTextField} che per {@code JTextArea}.
     * </p>
     *
     * @param component   Il componente di testo a cui applicare il comportamento.
     * @param placeHolder La stringa da visualizzare quando il campo è vuoto e non ha il focus.
     */
    public static void setTextComponentFocusBehaviour (JTextComponent component,  String placeHolder) {

        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                // Se c'è scritto il placeholder, pulisci il campo per permettere l'input
                if (component.getText().equals(placeHolder)) {
                    component.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                // Se l'utente non ha scritto nulla, rimetti il placeholder
                if (component.getText().isEmpty()) {
                    component.setText(placeHolder);
                }
            }
        });
    }
}