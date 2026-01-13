package frontend.gui;

import javax.swing.*;

/**
 * Pulsante che gestisce il menu principale delle funzionalità amministrative.
 * <p>
 * Questa classe estende {@link IconButton} e visualizza un'icona di menu.
 * Al click, invece di eseguire un'azione diretta, istanzia e mostra un {@link JPopupMenu}
 * contenente le opzioni disponibili per l'amministratore (Creazione utenti, Dashboard, Creazione progetti).
 * </p>
 */
public class MenuButton extends IconButton {

    /**
     * Array contenente le etichette delle voci di menu disponibili.
     */
    private final String[] options = {"Crea nuova utenza",  "Visualizza DashBoard", "Crea nuovo progetto"};

    /**
     * Costruttore del pulsante Menu.
     * <p>
     * Inizializza il pulsante con l'icona "menuIcon.svg" e configura il listener
     * per l'apertura del popup.
     * </p>
     */
    public MenuButton() {

        super("/frontend/gui/images/menuIcon.svg", 50, 50);

        setActionListener();
    }

    /**
     * Configura l'azione da eseguire al click del pulsante principale.
     * <p>
     * Crea dinamicamente un {@link JPopupMenu}, lo popola con le voci definite nell'array {@code options}
     * e lo visualizza immediatamente sotto il pulsante.
     * </p>
     */
    private void setActionListener() {

        this.addActionListener(e -> {

            JPopupMenu popupMenu = new JPopupMenu();

            for (String option : options) {

                JMenuItem menuItem = new JMenuItem(option);

                setMenuItemActionListener(menuItem);

                popupMenu.add(menuItem);
            }

            // Mostra il menu popup allineato verticalmente sotto il bottone
            popupMenu.show(this, 0, this.getHeight());
        });
    }

    /**
     * Assegna la logica specifica ad ogni voce del menu.
     * <p>
     * Utilizza uno switch sul testo della voce di menu per determinare quale finestra di dialogo aprire:
     * <ul>
     * <li><b>Crea nuova utenza:</b> Apre {@link DevOrAdminCreationDialog}.</li>
     * <li><b>Visualizza DashBoard:</b> Apre {@link DashBoard}.</li>
     * <li><b>Crea nuovo progetto:</b> Apre {@link CreateProjectDialog}.</li>
     * </ul>
     * Il metodo recupera dinamicamente il frame genitore tramite {@link SwingUtilities} per gestire correttamente
     * la proprietà delle finestre modali.
     * </p>
     *
     * @param menuItem La singola voce di menu a cui associare l'azione.
     */
    private void setMenuItemActionListener(JMenuItem menuItem) {

        menuItem.addActionListener(actionEvent -> {
            JFrame owner;
            switch (menuItem.getText()) {

                case "Crea nuova utenza":

                    owner = (JFrame) SwingUtilities.getWindowAncestor(this);

                    DevOrAdminCreationDialog devOrAdminCreationDialog = new DevOrAdminCreationDialog(owner);
                    devOrAdminCreationDialog.setVisible(true);
                    break;

                case "Visualizza DashBoard":

                    JDialog dialog = new DashBoard((JFrame)SwingUtilities.getAncestorOfClass(JFrame.class, this));
                    dialog.pack();
                    dialog.setLocationRelativeTo(SwingUtilities.getAncestorOfClass(JFrame.class, this));
                    dialog.setVisible(true);
                    break;

                case "Crea nuovo progetto":

                    owner = (JFrame) SwingUtilities.getWindowAncestor(this);

                    CreateProjectDialog createProjectDialog = new CreateProjectDialog(owner);
                    createProjectDialog.setVisible(true);
                    break;
                default:
                    break;
            }
        });
    }
}